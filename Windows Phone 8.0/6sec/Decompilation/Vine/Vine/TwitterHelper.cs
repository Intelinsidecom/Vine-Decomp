using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using Newtonsoft.Json;
using RestSharp.Authenticators;
using Vine.Services;
using Vine.TwitterAuth;

namespace Vine;

public class TwitterHelper
{
	private readonly TwitterAccess _twitterSettings;

	private readonly bool _authorized;

	private HttpClient _client;

	private OAuth1Authenticator _oauth;

	public event StringEventHandler ErrorEvent;

	public event EventHandler TweetCompletedEvent;

	public TwitterHelper(TwitterAccess access)
	{
		_twitterSettings = access;
		if (_twitterSettings != null && !string.IsNullOrEmpty(_twitterSettings.AccessToken) && !string.IsNullOrEmpty(_twitterSettings.AccessTokenSecret))
		{
			_authorized = true;
			_oauth = OAuth1Authenticator.ForProtectedResource(TwitterSettings.ConsumerKey, TwitterSettings.ConsumerKeySecret, _twitterSettings.AccessToken, _twitterSettings.AccessTokenSecret);
			_client = new HttpClient
			{
				BaseAddress = new Uri("https://api.twitter.com")
			};
			_client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
			_client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("text/json"));
			_client.DefaultRequestHeaders.UserAgent.TryParseAdd(ServiceInfo.UserAgent);
		}
	}

	public async Task<TwitterProfileInfo> GetInfoUserAsync(string user_id)
	{
		Uri requestUri = new Uri("1.1/users/show.json", UriKind.Relative);
		List<KeyValuePair<string, string>> requestParameters = new List<KeyValuePair<string, string>>
		{
			new KeyValuePair<string, string>("user_id", user_id)
		};
		HttpRequestMessage httpRequestMessage = new HttpRequestMessage(HttpMethod.Post, requestUri);
		_oauth.Authenticate(_client, httpRequestMessage, requestParameters);
		HttpResponseMessage httpResponseMessage = await _client.SendAsync(httpRequestMessage);
		if (httpResponseMessage.IsSuccessStatusCode)
		{
			try
			{
				StreamReader reader = new StreamReader(await httpResponseMessage.Content.ReadAsStreamAsync());
				return new JsonSerializer().Deserialize<TwitterProfileInfo>(new JsonTextReader(reader));
			}
			catch
			{
			}
		}
		return null;
	}
}
