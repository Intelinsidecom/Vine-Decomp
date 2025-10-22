using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Net;
using System.Text;
using Vine.Models;
using Windows.Security.Cryptography;
using Windows.Security.Cryptography.Core;
using Windows.Storage.Streams;

namespace Vine.Web;

public class OAuth
{
	private static class Parameters
	{
		internal const string OAuthConsumerKey = "oauth_consumer_key";

		internal const string OAuthConsumerSecret = "oauth_consumer_secret";

		internal const string OAuthNonce = "oauth_nonce";

		internal const string OAuthSignature = "oauth_signature";

		internal const string OAuthSignatureMethod = "oauth_signature_method";

		internal const string OAuthTimestamp = "oauth_timestamp";

		internal const string OAuthToken = "oauth_token";

		internal const string OAuthVersion = "oauth_version";
	}

	private static readonly DateTime Epoch = new DateTime(1970, 1, 1);

	private const string Version = "1.0";

	private const string SignatureMethod = "HMAC-SHA1";

	private const string UnreservedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.~";

	public static string GenerateAuthorizationHeader(string method, string url, string consumerKey, string consumerSecret, string token, string tokenSecret, Collection<KeyValuePair<string, string>> data, bool multiPart)
	{
		Uri uri = new Uri(url);
		Collection<KeyValuePair<string, string>> collection = new Collection<KeyValuePair<string, string>>();
		if (!multiPart)
		{
			foreach (KeyValuePair<string, string> queryParameter in GetQueryParameters(uri))
			{
				collection.Add(queryParameter);
			}
			if (data != null)
			{
				foreach (KeyValuePair<string, string> datum in data)
				{
					collection.Add(new KeyValuePair<string, string>(datum.Key, datum.Value));
				}
			}
		}
		string text = GenerateNonce();
		string text2 = GenerateTimestamp(ApplicationSettings.Current.ServerOffset);
		collection.Add(new KeyValuePair<string, string>("oauth_consumer_key", consumerKey));
		collection.Add(new KeyValuePair<string, string>("oauth_nonce", text));
		collection.Add(new KeyValuePair<string, string>("oauth_signature_method", "HMAC-SHA1"));
		collection.Add(new KeyValuePair<string, string>("oauth_timestamp", text2));
		collection.Add(new KeyValuePair<string, string>("oauth_version", "1.0"));
		if (!string.IsNullOrEmpty(token))
		{
			collection.Add(new KeyValuePair<string, string>("oauth_token", token));
		}
		string stringToEscape = Sign(method, GetBaseUri(uri), consumerSecret, (!string.IsNullOrEmpty(token)) ? tokenSecret : string.Empty, collection);
		return string.Format("{0}=\"{1}\", {2}=\"{3}\", {4}=\"{5}\", {6}=\"{7}\", {8}=\"{9}\", {10}=\"{11}\"{12}", "oauth_nonce", Uri.EscapeDataString(text), "oauth_signature_method", Uri.EscapeDataString("HMAC-SHA1"), "oauth_timestamp", Uri.EscapeDataString(text2), "oauth_consumer_key", Uri.EscapeDataString(consumerKey), "oauth_signature", Uri.EscapeDataString(stringToEscape), "oauth_version", "1.0", (!string.IsNullOrEmpty(token)) ? string.Format(", {0}=\"{1}\"", new object[2]
		{
			"oauth_token",
			Uri.EscapeDataString(token)
		}) : string.Empty);
	}

	public static string OAuthUrlEncode(string value)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < value.Length; i++)
		{
			char c = value[i];
			if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.~".IndexOf(c) != -1)
			{
				stringBuilder.Append(c);
				continue;
			}
			string text;
			if (char.IsSurrogate(c))
			{
				text = WebUtility.UrlEncode(c.ToString() + value[i + 1]);
				i++;
			}
			else
			{
				text = Uri.EscapeDataString(c.ToString());
				if (!text.Contains("%"))
				{
					text = "%" + $"{(int)c:X2}";
				}
			}
			stringBuilder.Append(text.ToUpper());
		}
		return stringBuilder.ToString();
	}

	private static IEnumerable<KeyValuePair<string, string>> GetQueryParameters(Uri uri)
	{
		List<KeyValuePair<string, string>> list = new List<KeyValuePair<string, string>>();
		if (uri.Query.Length > 1)
		{
			string[] array = uri.Query.Substring(1).Split('&');
			for (int i = 0; i < array.Length; i++)
			{
				string[] array2 = array[i].Split('=');
				list.Add(new KeyValuePair<string, string>(WebUtility.UrlDecode(array2[0]), WebUtility.UrlDecode(array2[1])));
			}
		}
		return list;
	}

	private static string GetBaseUri(Uri uri)
	{
		int num = uri.OriginalString.IndexOf("?");
		if (num <= -1)
		{
			return uri.OriginalString;
		}
		return uri.OriginalString.Substring(0, num);
	}

	public static string EncodeParameters(IEnumerable<KeyValuePair<string, string>> parameters)
	{
		IEnumerable<string> source = from kvp in parameters.ToDictionary((KeyValuePair<string, string> kvp) => OAuthUrlEncode(kvp.Key), (KeyValuePair<string, string> kvp) => OAuthUrlEncode(kvp.Value))
			orderby kvp.Key
			select string.Format("{0}={1}", new object[2] { kvp.Key, kvp.Value });
		return string.Join("&", source.ToArray());
	}

	public static string CreateSignatureBaseString(string method, string url, string encodedParameters)
	{
		return string.Format("{0}&{1}&{2}", new object[3]
		{
			method.ToUpperInvariant(),
			OAuthUrlEncode(url),
			OAuthUrlEncode(encodedParameters)
		});
	}

	private static string GetSigningKey(string consumerSecret, string tokenSecret)
	{
		return string.Format("{0}&{1}", new object[2]
		{
			OAuthUrlEncode(consumerSecret),
			OAuthUrlEncode(tokenSecret)
		});
	}

	private static string GetSignature(string sigBaseString, string consumerSecretKey)
	{
		IBuffer val = CryptographicBuffer.ConvertStringToBinary(consumerSecretKey, (BinaryStringEncoding)0);
		CryptographicKey obj = MacAlgorithmProvider.OpenAlgorithm("HMAC_SHA1").CreateKey(val);
		IBuffer val2 = CryptographicBuffer.ConvertStringToBinary(sigBaseString, (BinaryStringEncoding)0);
		return CryptographicBuffer.EncodeToBase64String(CryptographicEngine.Sign(obj, val2));
	}

	private static string Sign(string method, string url, string consumerSecret, string tokenSecret, IEnumerable<KeyValuePair<string, string>> parameters)
	{
		string encodedParameters = EncodeParameters(parameters);
		string sigBaseString = CreateSignatureBaseString(method, url, encodedParameters);
		string signingKey = GetSigningKey(consumerSecret, tokenSecret);
		return GetSignature(sigBaseString, signingKey);
	}

	public static string GenerateTimestamp(TimeSpan? serverOffset)
	{
		if (!serverOffset.HasValue)
		{
			return Convert.ToInt64((DateTime.UtcNow - Epoch).TotalSeconds).ToString();
		}
		return Convert.ToInt64((DateTime.UtcNow + serverOffset.Value - Epoch).TotalSeconds).ToString();
	}

	public static string GenerateNonce()
	{
		return Guid.NewGuid().ToString().Replace("-", string.Empty);
	}
}
