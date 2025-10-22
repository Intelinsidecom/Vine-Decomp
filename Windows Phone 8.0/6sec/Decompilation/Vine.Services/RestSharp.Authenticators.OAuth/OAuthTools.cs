using System;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading;
using RestSharp.Authenticators.OAuth.Extensions;
using Vine.Utils;

namespace RestSharp.Authenticators.OAuth;

internal static class OAuthTools
{
	private const string AlphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

	private const string Digit = "1234567890";

	private const string Lower = "abcdefghijklmnopqrstuvwxyz";

	private const string Unreserved = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890-._~";

	private const string Upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static readonly Random _random;

	private static readonly object _randomLock;

	private static readonly Encoding _encoding;

	private static readonly string[] UriRfc3986CharsToEscape;

	private static readonly string[] UriRfc3968EscapedHex;

	static OAuthTools()
	{
		_randomLock = new object();
		_encoding = Encoding.UTF8;
		UriRfc3986CharsToEscape = new string[5] { "!", "*", "'", "(", ")" };
		UriRfc3968EscapedHex = new string[5] { "%21", "%2A", "%27", "%28", "%29" };
		_random = new Random();
	}

	public static string GetNonce()
	{
		char[] array = new char[16];
		bool lockTaken = false;
		object randomLock = default(object);
		try
		{
			randomLock = _randomLock;
			Monitor.Enter(randomLock, ref lockTaken);
			for (int i = 0; i < array.Length; i++)
			{
				array[i] = "abcdefghijklmnopqrstuvwxyz1234567890"[_random.Next(0, "abcdefghijklmnopqrstuvwxyz1234567890".Length)];
			}
		}
		finally
		{
			if (lockTaken)
			{
				Monitor.Exit(randomLock);
			}
		}
		return new string(array);
	}

	public static string GetTimestamp()
	{
		return GetTimestamp(DateTime.UtcNow);
	}

	public static string GetTimestamp(DateTime dateTime)
	{
		return dateTime.ToUnixTime().ToString();
	}

	public static string UrlEncodeRelaxed(string value)
	{
		StringBuilder stringBuilder = new StringBuilder(Uri.EscapeDataString(value));
		for (int i = 0; i < UriRfc3986CharsToEscape.Length; i++)
		{
			string oldValue = UriRfc3986CharsToEscape[i];
			stringBuilder.Replace(oldValue, UriRfc3968EscapedHex[i]);
		}
		return stringBuilder.ToString();
	}

	public static string UrlEncodeStrict(string value)
	{
		StringBuilder stringBuilder = new StringBuilder();
		foreach (char c in value)
		{
			if ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890-._~".Contains(c))
			{
				stringBuilder.Append(c);
			}
			else
			{
				stringBuilder.Append($"%{(int)c:X2}");
			}
		}
		return stringBuilder.ToString();
	}

	public static string NormalizeRequestParameters(WebParameterCollection parameters)
	{
		return SortParametersExcludingSignature(parameters).Concatenate("=", "&");
	}

	public static WebParameterCollection SortParametersExcludingSignature(WebParameterCollection parameters)
	{
		WebParameterCollection webParameterCollection = new WebParameterCollection(from p in parameters
			where !p.Name.EqualsIgnoreCase("oauth_signature")
			select new WebParameter(UrlEncodeStrict(p.Name), UrlEncodeStrict(p.Value)));
		webParameterCollection.Sort((WebPair x, WebPair y) => (string.CompareOrdinal(x.Name, y.Name) == 0) ? string.CompareOrdinal(x.Value, y.Value) : string.CompareOrdinal(x.Name, y.Name));
		return webParameterCollection;
	}

	public static string ConstructRequestUrl(Uri url)
	{
		if (url == null)
		{
			throw new ArgumentNullException("url");
		}
		StringBuilder stringBuilder = new StringBuilder();
		string value = $"{url.Scheme}://{url.Host}";
		string text = $":{url.Port}";
		bool flag = url.Scheme == "http" && url.Port == 80;
		bool flag2 = url.Scheme == "https" && url.Port == 443;
		stringBuilder.Append(value);
		stringBuilder.Append((!flag && !flag2) ? text : "");
		stringBuilder.Append(url.AbsolutePath);
		return stringBuilder.ToString();
	}

	public static string ConcatenateRequestElements(string method, string url, WebParameterCollection parameters)
	{
		StringBuilder stringBuilder = new StringBuilder();
		string value = method.ToUpper().Then("&");
		string value2 = UrlEncodeStrict(ConstructRequestUrl(url.AsUri())).Then("&");
		string value3 = UrlEncodeStrict(NormalizeRequestParameters(parameters));
		stringBuilder.Append(value);
		stringBuilder.Append(value2);
		stringBuilder.Append(value3);
		return stringBuilder.ToString();
	}

	public static string GetSignature(OAuthSignatureMethod signatureMethod, string signatureBase, string consumerSecret)
	{
		return GetSignature(signatureMethod, OAuthSignatureTreatment.Escaped, signatureBase, consumerSecret, null);
	}

	public static string GetSignature(OAuthSignatureMethod signatureMethod, OAuthSignatureTreatment signatureTreatment, string signatureBase, string consumerSecret)
	{
		return GetSignature(signatureMethod, signatureTreatment, signatureBase, consumerSecret, null);
	}

	public static string GetSignature(OAuthSignatureMethod signatureMethod, string signatureBase, string consumerSecret, string tokenSecret)
	{
		return GetSignature(signatureMethod, OAuthSignatureTreatment.Escaped, consumerSecret, tokenSecret);
	}

	public static string GetSignature(OAuthSignatureMethod signatureMethod, OAuthSignatureTreatment signatureTreatment, string signatureBase, string consumerSecret, string tokenSecret)
	{
		if (string.IsNullOrEmpty(tokenSecret))
		{
			tokenSecret = string.Empty;
		}
		consumerSecret = UrlEncodeStrict(consumerSecret);
		tokenSecret = UrlEncodeStrict(tokenSecret);
		string text;
		switch (signatureMethod)
		{
		case OAuthSignatureMethod.HmacSha1:
		{
			HMACSHA1 hMACSHA = new HMACSHA1();
			string s = $"{consumerSecret}&{tokenSecret}";
			hMACSHA.Key = _encoding.GetBytes(s);
			text = signatureBase.HashWith(hMACSHA);
			break;
		}
		case OAuthSignatureMethod.PlainText:
			text = $"{consumerSecret}&{tokenSecret}";
			break;
		default:
			throw new NotImplementedException("Only HMAC-SHA1 is currently supported.");
		}
		if (signatureTreatment != OAuthSignatureTreatment.Escaped)
		{
			return text;
		}
		return UrlEncodeStrict(text);
	}
}
