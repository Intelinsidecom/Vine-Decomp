using System;
using System.Security.Cryptography;
using System.Text;

namespace RestSharp.Authenticators.OAuth.Extensions;

internal static class OAuthExtensions
{
	public static string ToRequestValue(this OAuthSignatureMethod signatureMethod)
	{
		string text = signatureMethod.ToString().ToUpper();
		int num = text.IndexOf("SHA1");
		if (num <= -1)
		{
			return text;
		}
		return text.Insert(num, "-");
	}

	public static OAuthSignatureMethod FromRequestValue(this string signatureMethod)
	{
		if (!(signatureMethod == "HMAC-SHA1"))
		{
			if (signatureMethod == "RSA-SHA1")
			{
				return OAuthSignatureMethod.RsaSha1;
			}
			return OAuthSignatureMethod.PlainText;
		}
		return OAuthSignatureMethod.HmacSha1;
	}

	public static string HashWith(this string input, HashAlgorithm algorithm)
	{
		byte[] bytes = Encoding.UTF8.GetBytes(input);
		return Convert.ToBase64String(algorithm.ComputeHash(bytes));
	}
}
