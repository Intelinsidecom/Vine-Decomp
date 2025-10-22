using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;

namespace Vine.Background.Web;

internal class OAuth
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

	private static readonly DateTime Epoch;

	private const string Version = "1.0";

	private const string SignatureMethod = "HMAC-SHA1";

	private const string UnreservedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.~";

	public static extern string GenerateAuthorizationHeader(string method, string url, string consumerKey, string consumerSecret, string token, string tokenSecret, Collection<KeyValuePair<string, string>> data, bool multiPart);

	public static extern string OAuthUrlEncode(string value);

	private static extern IEnumerable<KeyValuePair<string, string>> GetQueryParameters(Uri uri);

	private static extern string GetBaseUri(Uri uri);

	public static extern string EncodeParameters(IEnumerable<KeyValuePair<string, string>> parameters);

	public static extern string CreateSignatureBaseString(string method, string url, string encodedParameters);

	private static extern string GetSigningKey(string consumerSecret, string tokenSecret);

	private static extern string GetSignature(string sigBaseString, string consumerSecretKey);

	private static extern string Sign(string method, string url, string consumerSecret, string tokenSecret, IEnumerable<KeyValuePair<string, string>> parameters);

	public static extern string GenerateTimestamp(TimeSpan? serverOffset);

	public static extern string GenerateNonce();

	public extern OAuth();
}
