using System;
using System.Collections.Generic;
using RestSharp.Authenticators.OAuth.Extensions;
using Vine;

namespace RestSharp.Authenticators.OAuth;

internal class OAuthWorkflow
{
	public virtual string Version { get; set; }

	public virtual string ConsumerKey { get; set; }

	public virtual string ConsumerSecret { get; set; }

	public virtual string Token { get; set; }

	public virtual string TokenSecret { get; set; }

	public virtual string CallbackUrl { get; set; }

	public virtual string Verifier { get; set; }

	public virtual string SessionHandle { get; set; }

	public virtual OAuthSignatureMethod SignatureMethod { get; set; }

	public virtual OAuthSignatureTreatment SignatureTreatment { get; set; }

	public virtual OAuthParameterHandling ParameterHandling { get; set; }

	public virtual string ClientUsername { get; set; }

	public virtual string ClientPassword { get; set; }

	public virtual string RequestTokenUrl { get; set; }

	public virtual string AccessTokenUrl { get; set; }

	public virtual string AuthorizationUrl { get; set; }

	public OAuthWebQueryInfo BuildRequestTokenInfo(string method)
	{
		return BuildRequestTokenInfo(method, null);
	}

	public virtual OAuthWebQueryInfo BuildRequestTokenInfo(string method, WebParameterCollection parameters)
	{
		ValidateTokenRequestState();
		if (parameters == null)
		{
			parameters = new WebParameterCollection();
		}
		string timestamp = OAuthTools.GetTimestamp();
		string nonce = OAuthTools.GetNonce();
		AddAuthParameters(parameters, timestamp, nonce);
		string signatureBase = OAuthTools.ConcatenateRequestElements(method, RequestTokenUrl, parameters);
		string signature = OAuthTools.GetSignature(SignatureMethod, SignatureTreatment, signatureBase, ConsumerSecret);
		return new OAuthWebQueryInfo
		{
			WebMethod = method,
			ParameterHandling = ParameterHandling,
			ConsumerKey = ConsumerKey,
			SignatureMethod = SignatureMethod.ToRequestValue(),
			SignatureTreatment = SignatureTreatment,
			Signature = signature,
			Timestamp = timestamp,
			Nonce = nonce,
			Version = (Version ?? "1.0"),
			Callback = (CallbackUrl ?? ""),
			TokenSecret = TokenSecret,
			ConsumerSecret = ConsumerSecret
		};
	}

	public virtual OAuthWebQueryInfo BuildAccessTokenInfo(string method)
	{
		return BuildAccessTokenInfo(method, null);
	}

	public virtual OAuthWebQueryInfo BuildAccessTokenInfo(string method, WebParameterCollection parameters)
	{
		ValidateAccessRequestState();
		if (parameters == null)
		{
			parameters = new WebParameterCollection();
		}
		Uri uri = new Uri(AccessTokenUrl);
		string timestamp = OAuthTools.GetTimestamp();
		string nonce = OAuthTools.GetNonce();
		AddAuthParameters(parameters, timestamp, nonce);
		string signatureBase = OAuthTools.ConcatenateRequestElements(method, uri.ToString(), parameters);
		string signature = OAuthTools.GetSignature(SignatureMethod, SignatureTreatment, signatureBase, ConsumerSecret, TokenSecret);
		return new OAuthWebQueryInfo
		{
			WebMethod = method,
			ParameterHandling = ParameterHandling,
			ConsumerKey = ConsumerKey,
			Token = Token,
			SignatureMethod = SignatureMethod.ToRequestValue(),
			SignatureTreatment = SignatureTreatment,
			Signature = signature,
			Timestamp = timestamp,
			Nonce = nonce,
			Version = (Version ?? "1.0"),
			Verifier = Verifier,
			Callback = CallbackUrl,
			TokenSecret = TokenSecret,
			ConsumerSecret = ConsumerSecret
		};
	}

	public virtual OAuthWebQueryInfo BuildClientAuthAccessTokenInfo(string method, WebParameterCollection parameters)
	{
		ValidateClientAuthAccessRequestState();
		if (parameters == null)
		{
			parameters = new WebParameterCollection();
		}
		Uri uri = new Uri(AccessTokenUrl);
		string timestamp = OAuthTools.GetTimestamp();
		string nonce = OAuthTools.GetNonce();
		AddXAuthParameters(parameters, timestamp, nonce);
		string signatureBase = OAuthTools.ConcatenateRequestElements(method, uri.ToString(), parameters);
		string signature = OAuthTools.GetSignature(SignatureMethod, SignatureTreatment, signatureBase, ConsumerSecret);
		return new OAuthWebQueryInfo
		{
			WebMethod = method,
			ParameterHandling = ParameterHandling,
			ClientMode = "client_auth",
			ClientUsername = ClientUsername,
			ClientPassword = ClientPassword,
			ConsumerKey = ConsumerKey,
			SignatureMethod = SignatureMethod.ToRequestValue(),
			SignatureTreatment = SignatureTreatment,
			Signature = signature,
			Timestamp = timestamp,
			Nonce = nonce,
			Version = (Version ?? "1.0"),
			TokenSecret = TokenSecret,
			ConsumerSecret = ConsumerSecret
		};
	}

	public virtual OAuthWebQueryInfo BuildProtectedResourceInfo(string method, WebParameterCollection parameters, string url)
	{
		ValidateProtectedResourceState();
		IDictionary<string, string> dictionary = HttpParseQuery.ParseQueryString(new Uri(url).Query);
		foreach (string key in dictionary.Keys)
		{
			string text = method.ToUpperInvariant();
			if (text == "POST")
			{
				parameters.Add(new HttpPostParameter(key, dictionary[key]));
			}
			else
			{
				parameters.Add(key, dictionary[key]);
			}
		}
		string timestamp = OAuthTools.GetTimestamp();
		string nonce = OAuthTools.GetNonce();
		AddAuthParameters(parameters, timestamp, nonce);
		string signatureBase = OAuthTools.ConcatenateRequestElements(method, url, parameters);
		string signature = OAuthTools.GetSignature(SignatureMethod, SignatureTreatment, signatureBase, ConsumerSecret, TokenSecret);
		return new OAuthWebQueryInfo
		{
			WebMethod = method,
			ParameterHandling = ParameterHandling,
			ConsumerKey = ConsumerKey,
			Token = Token,
			SignatureMethod = SignatureMethod.ToRequestValue(),
			SignatureTreatment = SignatureTreatment,
			Signature = signature,
			Timestamp = timestamp,
			Nonce = nonce,
			Version = (Version ?? "1.0"),
			Callback = CallbackUrl,
			ConsumerSecret = ConsumerSecret,
			TokenSecret = TokenSecret
		};
	}

	private void ValidateTokenRequestState()
	{
		if (string.IsNullOrEmpty(RequestTokenUrl))
		{
			throw new ArgumentException("You must specify a request token URL");
		}
		if (string.IsNullOrEmpty(ConsumerKey))
		{
			throw new ArgumentException("You must specify a consumer key");
		}
		if (string.IsNullOrEmpty(ConsumerSecret))
		{
			throw new ArgumentException("You must specify a consumer secret");
		}
	}

	private void ValidateAccessRequestState()
	{
		if (string.IsNullOrEmpty(AccessTokenUrl))
		{
			throw new ArgumentException("You must specify an access token URL");
		}
		if (string.IsNullOrEmpty(ConsumerKey))
		{
			throw new ArgumentException("You must specify a consumer key");
		}
		if (string.IsNullOrEmpty(ConsumerSecret))
		{
			throw new ArgumentException("You must specify a consumer secret");
		}
		if (string.IsNullOrEmpty(Token))
		{
			throw new ArgumentException("You must specify a token");
		}
	}

	private void ValidateClientAuthAccessRequestState()
	{
		if (string.IsNullOrEmpty(AccessTokenUrl))
		{
			throw new ArgumentException("You must specify an access token URL");
		}
		if (string.IsNullOrEmpty(ConsumerKey))
		{
			throw new ArgumentException("You must specify a consumer key");
		}
		if (string.IsNullOrEmpty(ConsumerSecret))
		{
			throw new ArgumentException("You must specify a consumer secret");
		}
		if (string.IsNullOrEmpty(ClientUsername) || string.IsNullOrEmpty(ClientPassword))
		{
			throw new ArgumentException("You must specify user credentials");
		}
	}

	private void ValidateProtectedResourceState()
	{
		if (string.IsNullOrEmpty(ConsumerKey))
		{
			throw new ArgumentException("You must specify a consumer key");
		}
		if (string.IsNullOrEmpty(ConsumerSecret))
		{
			throw new ArgumentException("You must specify a consumer secret");
		}
	}

	private void AddAuthParameters(ICollection<WebPair> parameters, string timestamp, string nonce)
	{
		WebParameterCollection webParameterCollection = new WebParameterCollection
		{
			new WebPair("oauth_consumer_key", ConsumerKey),
			new WebPair("oauth_nonce", nonce),
			new WebPair("oauth_signature_method", SignatureMethod.ToRequestValue()),
			new WebPair("oauth_timestamp", timestamp),
			new WebPair("oauth_version", Version ?? "1.0")
		};
		if (!string.IsNullOrEmpty(Token))
		{
			webParameterCollection.Add(new WebPair("oauth_token", Token));
		}
		if (!string.IsNullOrEmpty(CallbackUrl))
		{
			webParameterCollection.Add(new WebPair("oauth_callback", CallbackUrl));
		}
		if (!string.IsNullOrEmpty(Verifier))
		{
			webParameterCollection.Add(new WebPair("oauth_verifier", Verifier));
		}
		if (!string.IsNullOrEmpty(SessionHandle))
		{
			webParameterCollection.Add(new WebPair("oauth_session_handle", SessionHandle));
		}
		foreach (WebPair item in webParameterCollection)
		{
			parameters.Add(item);
		}
	}

	private void AddXAuthParameters(ICollection<WebPair> parameters, string timestamp, string nonce)
	{
		foreach (WebPair item in new WebParameterCollection
		{
			new WebPair("x_auth_username", ClientUsername),
			new WebPair("x_auth_password", ClientPassword),
			new WebPair("x_auth_mode", "client_auth"),
			new WebPair("oauth_consumer_key", ConsumerKey),
			new WebPair("oauth_signature_method", SignatureMethod.ToRequestValue()),
			new WebPair("oauth_timestamp", timestamp),
			new WebPair("oauth_nonce", nonce),
			new WebPair("oauth_version", Version ?? "1.0")
		})
		{
			parameters.Add(item);
		}
	}
}
