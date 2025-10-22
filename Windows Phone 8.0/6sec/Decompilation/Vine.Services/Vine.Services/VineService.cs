using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Service.Services;
using Vine.Services.Response;
using Vine.Services.Response.Auth;

namespace Vine.Services;

public class VineService : ServiceBase
{
	public static VineService Instance = new VineService();

	private Regex _regPopularTags = new Regex("data-idx=\"[^\"]+\"\\s*>\\#(?<tag>[^<]+)</a>");

	protected override void ManageUnauthorized(string tokenUsed)
	{
	}

	public Task<AuthData> CreateAccountWithTwitterAsync(TwitterAccess twitter, string name, string phone, string location, string description)
	{
		List<KeyValuePair<string, string>> list = new List<KeyValuePair<string, string>>
		{
			new KeyValuePair<string, string>("authenticate", "1"),
			new KeyValuePair<string, string>("twitterId", twitter.UserId),
			new KeyValuePair<string, string>("twitterOauthSecret", twitter.AccessTokenSecret),
			new KeyValuePair<string, string>("twitterOauthToken", twitter.AccessToken),
			new KeyValuePair<string, string>("username", name)
		};
		if (!string.IsNullOrWhiteSpace(phone))
		{
			list.Add(new KeyValuePair<string, string>("phoneNumber", phone));
		}
		if (!string.IsNullOrEmpty(description))
		{
			list.Add(new KeyValuePair<string, string>("description", description));
		}
		if (!string.IsNullOrEmpty(location))
		{
			list.Add(new KeyValuePair<string, string>("location", location));
		}
		Uri uri = new Uri("users", UriKind.Relative);
		return SendRequest(HttpMethod.Post, uri, new FormUrlEncodedContent(list), async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			AuthJsonObject authJsonObject = new JsonSerializer().Deserialize<AuthJsonObject>(new JsonTextReader(reader));
			return authJsonObject.success ? authJsonObject.data : null;
		});
	}

	public Task<AuthData> LoginAsync(string email, string password)
	{
		List<KeyValuePair<string, string>> nameValueCollection = new List<KeyValuePair<string, string>>
		{
			new KeyValuePair<string, string>("password", password),
			new KeyValuePair<string, string>("username", email)
		};
		Uri uri = new Uri("users/authenticate", UriKind.Relative);
		return SendRequest(HttpMethod.Post, uri, new FormUrlEncodedContent(nameValueCollection), async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			AuthJsonObject authJsonObject = new JsonSerializer().Deserialize<AuthJsonObject>(new JsonTextReader(reader));
			return authJsonObject.success ? authJsonObject.data : null;
		});
	}

	public Task<bool> ResetPasswordAsync(string email)
	{
		Uri uri = new Uri("users/forgotPassword?email=" + HttpUtility.UrlEncode(email), UriKind.Relative);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			VineJsonResponse vineJsonResponse = new JsonSerializer().Deserialize<VineJsonResponse>(new JsonTextReader(reader));
			if (vineJsonResponse.success)
			{
				return vineJsonResponse.success;
			}
			throw new Exception();
		});
	}

	public Task<AuthData> LoginTwitterAsync(TwitterAccess access)
	{
		string deviceUniqueID = VineGenUtils.GetDeviceUniqueID();
		List<KeyValuePair<string, string>> nameValueCollection = new List<KeyValuePair<string, string>>
		{
			new KeyValuePair<string, string>("deviceToken", deviceUniqueID),
			new KeyValuePair<string, string>("twitterId", access.UserId),
			new KeyValuePair<string, string>("twitterOauthSecret", access.AccessTokenSecret),
			new KeyValuePair<string, string>("twitterOauthToken", access.AccessToken)
		};
		Uri uri = new Uri("users/authenticate/twitter", UriKind.Relative);
		return SendRequest(HttpMethod.Post, uri, new FormUrlEncodedContent(nameValueCollection), async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			AuthJsonObject authJsonObject = new JsonSerializer().Deserialize<AuthJsonObject>(new JsonTextReader(reader));
			return authJsonObject.success ? authJsonObject.data : null;
		});
	}

	public Task<AuthData> CreateAccountAsync(string name, string email, string password, string phone)
	{
		List<KeyValuePair<string, string>> list = new List<KeyValuePair<string, string>>
		{
			new KeyValuePair<string, string>("authenticate", "1"),
			new KeyValuePair<string, string>("email", email),
			new KeyValuePair<string, string>("password", password),
			new KeyValuePair<string, string>("username", name),
			new KeyValuePair<string, string>("locale", RegionInfo.CurrentRegion.TwoLetterISORegionName)
		};
		if (!string.IsNullOrWhiteSpace(phone))
		{
			list.Add(new KeyValuePair<string, string>("phoneNumber", phone));
		}
		Uri uri = new Uri("users", UriKind.Relative);
		return SendRequest(HttpMethod.Post, uri, new FormUrlEncodedContent(list), async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			AuthJsonObject authJsonObject = new JsonSerializer().Deserialize<AuthJsonObject>(new JsonTextReader(reader));
			return authJsonObject.success ? authJsonObject.data : null;
		});
	}

	public Task<List<string>> GetPopularTags()
	{
		Uri uri = new Uri("http://uvr.a1429.lol/explo", UriKind.Absolute);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			string input = new StreamReader(await response.Content.ReadAsStreamAsync()).ReadToEnd();
			List<string> list = new List<string>();
			foreach (Match item in _regPopularTags.Matches(input))
			{
				list.Add(item.Groups["tag"].Value);
			}
			return list;
		});
	}
}
