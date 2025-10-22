using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using RestSharp.Authenticators.OAuth;
using RestSharp.Authenticators.OAuth.Extensions;

namespace RestSharp.Authenticators;

public class OAuth1Authenticator
{
	public virtual string Realm { get; set; }

	public virtual OAuthParameterHandling ParameterHandling { get; set; }

	public virtual OAuthSignatureMethod SignatureMethod { get; set; }

	public virtual OAuthSignatureTreatment SignatureTreatment { get; set; }

	internal virtual OAuthType Type { get; set; }

	internal virtual string ConsumerKey { get; set; }

	internal virtual string ConsumerSecret { get; set; }

	internal virtual string Token { get; set; }

	internal virtual string TokenSecret { get; set; }

	internal virtual string Verifier { get; set; }

	internal virtual string Version { get; set; }

	internal virtual string CallbackUrl { get; set; }

	internal virtual string SessionHandle { get; set; }

	internal virtual string ClientUsername { get; set; }

	internal virtual string ClientPassword { get; set; }

	public static OAuth1Authenticator ForRequestToken(string consumerKey, string consumerSecret)
	{
		return new OAuth1Authenticator
		{
			ParameterHandling = OAuthParameterHandling.HttpAuthorizationHeader,
			SignatureMethod = OAuthSignatureMethod.HmacSha1,
			SignatureTreatment = OAuthSignatureTreatment.Escaped,
			ConsumerKey = consumerKey,
			ConsumerSecret = consumerSecret,
			Type = OAuthType.RequestToken
		};
	}

	public static OAuth1Authenticator ForXAuth(string consumerKey, string consumerSecret, string username, string password)
	{
		return new OAuth1Authenticator
		{
			ParameterHandling = OAuthParameterHandling.HttpAuthorizationHeaderWithPost,
			SignatureMethod = OAuthSignatureMethod.HmacSha1,
			SignatureTreatment = OAuthSignatureTreatment.Escaped,
			ConsumerKey = consumerKey,
			ConsumerSecret = consumerSecret,
			ClientUsername = username,
			ClientPassword = password,
			Type = OAuthType.ClientAuthentication
		};
	}

	public static OAuth1Authenticator ForRequestToken(string consumerKey, string consumerSecret, string callbackUrl)
	{
		OAuth1Authenticator oAuth1Authenticator = ForRequestToken(consumerKey, consumerSecret);
		oAuth1Authenticator.CallbackUrl = callbackUrl;
		return oAuth1Authenticator;
	}

	public static OAuth1Authenticator ForAccessToken(string consumerKey, string consumerSecret, string token, string tokenSecret)
	{
		return new OAuth1Authenticator
		{
			ParameterHandling = OAuthParameterHandling.HttpAuthorizationHeader,
			SignatureMethod = OAuthSignatureMethod.HmacSha1,
			SignatureTreatment = OAuthSignatureTreatment.Escaped,
			ConsumerKey = consumerKey,
			ConsumerSecret = consumerSecret,
			Token = token,
			TokenSecret = tokenSecret,
			Type = OAuthType.AccessToken
		};
	}

	public static OAuth1Authenticator ForAccessToken(string consumerKey, string consumerSecret, string token, string tokenSecret, string verifier)
	{
		OAuth1Authenticator oAuth1Authenticator = ForAccessToken(consumerKey, consumerSecret, token, tokenSecret);
		oAuth1Authenticator.Verifier = verifier;
		return oAuth1Authenticator;
	}

	public static OAuth1Authenticator ForAccessTokenRefresh(string consumerKey, string consumerSecret, string token, string tokenSecret, string sessionHandle)
	{
		OAuth1Authenticator oAuth1Authenticator = ForAccessToken(consumerKey, consumerSecret, token, tokenSecret);
		oAuth1Authenticator.SessionHandle = sessionHandle;
		return oAuth1Authenticator;
	}

	public static OAuth1Authenticator ForAccessTokenRefresh(string consumerKey, string consumerSecret, string token, string tokenSecret, string verifier, string sessionHandle)
	{
		OAuth1Authenticator oAuth1Authenticator = ForAccessToken(consumerKey, consumerSecret, token, tokenSecret);
		oAuth1Authenticator.SessionHandle = sessionHandle;
		oAuth1Authenticator.Verifier = verifier;
		return oAuth1Authenticator;
	}

	public static OAuth1Authenticator ForClientAuthentication(string consumerKey, string consumerSecret, string username, string password)
	{
		return new OAuth1Authenticator
		{
			ParameterHandling = OAuthParameterHandling.HttpAuthorizationHeader,
			SignatureMethod = OAuthSignatureMethod.HmacSha1,
			SignatureTreatment = OAuthSignatureTreatment.Escaped,
			ConsumerKey = consumerKey,
			ConsumerSecret = consumerSecret,
			ClientUsername = username,
			ClientPassword = password,
			Type = OAuthType.ClientAuthentication
		};
	}

	public static OAuth1Authenticator ForProtectedResource(string consumerKey, string consumerSecret, string accessToken, string accessTokenSecret)
	{
		return new OAuth1Authenticator
		{
			Type = OAuthType.ProtectedResource,
			ParameterHandling = OAuthParameterHandling.HttpAuthorizationHeader,
			SignatureMethod = OAuthSignatureMethod.HmacSha1,
			SignatureTreatment = OAuthSignatureTreatment.Escaped,
			ConsumerKey = consumerKey,
			ConsumerSecret = consumerSecret,
			Token = accessToken,
			TokenSecret = accessTokenSecret
		};
	}

	public void Authenticate(HttpClient client, HttpRequestMessage message, List<KeyValuePair<string, string>> requestParameters)
	{
		Uri uri = message.RequestUri;
		if (client.BaseAddress != null)
		{
			uri = new Uri(client.BaseAddress, message.RequestUri);
		}
		Authenticate(uri.OriginalString, message.Method.ToString(), requestParameters, out var generatedHeaders);
		foreach (KeyValuePair<string, string> item in generatedHeaders)
		{
			message.Headers.Add(item.Key, item.Value);
		}
		if (requestParameters == null)
		{
			return;
		}
		StringBuilder stringBuilder = new StringBuilder();
		bool flag = true;
		foreach (KeyValuePair<string, string> requestParameter in requestParameters)
		{
			if (flag)
			{
				flag = false;
			}
			else
			{
				stringBuilder.Append("&");
			}
			stringBuilder.Append(OAuthTools.UrlEncodeStrict(requestParameter.Key));
			stringBuilder.Append("=");
			stringBuilder.Append(OAuthTools.UrlEncodeStrict(requestParameter.Value));
		}
		message.Content = new StringContent(stringBuilder.ToString(), Encoding.UTF8, "application/x-www-form-urlencoded");
	}

	public void Authenticate(string url, string requestMethod, List<KeyValuePair<string, string>> requestParameters, out List<KeyValuePair<string, string>> generatedHeaders)
	{
		OAuthWorkflow workflow = new OAuthWorkflow
		{
			ConsumerKey = ConsumerKey,
			ConsumerSecret = ConsumerSecret,
			ParameterHandling = ParameterHandling,
			SignatureMethod = SignatureMethod,
			SignatureTreatment = SignatureTreatment,
			Verifier = Verifier,
			Version = Version,
			CallbackUrl = CallbackUrl,
			SessionHandle = SessionHandle,
			Token = Token,
			TokenSecret = TokenSecret,
			ClientUsername = ClientUsername,
			ClientPassword = ClientPassword
		};
		AddOAuthData(url, requestMethod, requestParameters, out generatedHeaders, workflow);
	}

	private void AddOAuthData(string url, string requestMethod, List<KeyValuePair<string, string>> requestParameters, out List<KeyValuePair<string, string>> generatedHeaders, OAuthWorkflow workflow)
	{
		generatedHeaders = new List<KeyValuePair<string, string>>();
		int num = url.IndexOf('?');
		if (num != -1)
		{
			url = url.Substring(0, num);
		}
		string method = requestMethod.ToUpperInvariant();
		WebParameterCollection webParameterCollection = new WebParameterCollection();
		if (requestParameters != null)
		{
			foreach (KeyValuePair<string, string> requestParameter in requestParameters)
			{
				webParameterCollection.Add(new WebPair(requestParameter.Key, requestParameter.Value));
			}
		}
		OAuthWebQueryInfo oAuthWebQueryInfo;
		switch (Type)
		{
		case OAuthType.RequestToken:
			workflow.RequestTokenUrl = url;
			oAuthWebQueryInfo = workflow.BuildRequestTokenInfo(method, webParameterCollection);
			break;
		case OAuthType.AccessToken:
			workflow.AccessTokenUrl = url;
			oAuthWebQueryInfo = workflow.BuildAccessTokenInfo(method, webParameterCollection);
			break;
		case OAuthType.ClientAuthentication:
			workflow.AccessTokenUrl = url;
			oAuthWebQueryInfo = workflow.BuildClientAuthAccessTokenInfo(method, webParameterCollection);
			break;
		case OAuthType.ProtectedResource:
			oAuthWebQueryInfo = workflow.BuildProtectedResourceInfo(method, webParameterCollection, url);
			break;
		default:
			throw new ArgumentOutOfRangeException();
		}
		switch (ParameterHandling)
		{
		case OAuthParameterHandling.HttpAuthorizationHeader:
			webParameterCollection.Add("oauth_signature", oAuthWebQueryInfo.Signature);
			generatedHeaders.Add(new KeyValuePair<string, string>("Authorization", GetAuthorizationHeader(webParameterCollection)));
			break;
		case OAuthParameterHandling.HttpAuthorizationHeaderWithPost:
			webParameterCollection.Add("oauth_signature", oAuthWebQueryInfo.Signature);
			generatedHeaders.Add(new KeyValuePair<string, string>("Authorization", GetAuthorizationHeader(webParameterCollection)));
			{
				foreach (WebPair item in webParameterCollection.Where((WebPair webPair) => webPair.Name.StartsWith("x_auth")))
				{
					requestParameters.Add(new KeyValuePair<string, string>(item.Name, item.Value));
				}
				break;
			}
		case OAuthParameterHandling.UrlOrPostParameters:
			webParameterCollection.Add("oauth_signature", oAuthWebQueryInfo.Signature);
			{
				foreach (WebPair parameter in webParameterCollection.Where((WebPair webPair) => !webPair.Name.IsNullOrBlank() && webPair.Name.StartsWith("oauth_")))
				{
					if (!requestParameters.Any((KeyValuePair<string, string> f) => f.Key == parameter.Name))
					{
						requestParameters.Add(new KeyValuePair<string, string>(parameter.Name, parameter.Value));
					}
				}
				break;
			}
		default:
			throw new ArgumentOutOfRangeException();
		}
	}

	private string GetAuthorizationHeader(WebPairCollection parameters)
	{
		StringBuilder stringBuilder = new StringBuilder("OAuth ");
		if (!Realm.IsNullOrBlank())
		{
			stringBuilder.Append("realm=\"{0}\",".FormatWith(OAuthTools.UrlEncodeRelaxed(Realm)));
		}
		parameters.Sort((WebPair l, WebPair r) => l.Name.CompareTo(r.Name));
		int num = 0;
		List<WebPair> list = parameters.Where((WebPair parameter) => !parameter.Name.IsNullOrBlank() && !parameter.Value.IsNullOrBlank() && parameter.Name.StartsWith("oauth_")).ToList();
		foreach (WebPair item in list)
		{
			num++;
			string format = ((num < list.Count) ? "{0}=\"{1}\"," : "{0}=\"{1}\"");
			stringBuilder.Append(string.Format(format, item.Name, item.Value));
		}
		return stringBuilder.ToString();
	}
}
