using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Net;
using System.Threading;
using System.Threading.Tasks;
using Vine.Common;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Windows.ApplicationModel.Resources.Core;
using Windows.Storage.Streams;
using Windows.Web.Http;
using Windows.Web.Http.Headers;

namespace Vine.Web;

public class WebDataProvider : IDataProvider
{
	public enum WebPlatform
	{
		Vine,
		Twitter,
		Facebook
	}

	private const string ApiBaseUri = "https://apivi.a1429.lol";

	private static readonly string[] LangValues = new string[44]
	{
		"ar", "af", "da", "de", "en", "en-GB", "es", "fa", "fi", "fil",
		"fr", "he", "hi", "hu", "id", "in", "it", "ja", "ko", "ms",
		"ms-Latn-MY", "msa", "nb", "nb-NO", "nl", "no", "pl", "pt", "ru", "sw",
		"sv", "th", "tl", "tr", "zh-CN", "zh-HK", "zh-Hans", "zh-Hans-CN", "zh-Hant", "zh-Hant-HK",
		"zh-Hant-TW", "zh-TW", "zh-rCN", "zh-rHK"
	};

	private static string _acceptLang;

	public string ConsumerKey { get; set; }

	public string ConsumerSecret { get; set; }

	public WebDataProvider()
	{
		ConsumerKey = Constants.VINE_API_KEY;
		ConsumerSecret = Constants.VINE_API_SECRET;
	}

	private async Task<ApiResult<T>> Delete<T>(string path, ParameterCollection parameters, WebPlatform platform)
	{
		ApiResult<T> result = new ApiResult<T>();
		try
		{
			HttpClient val = new HttpClient();
			SetHeaders(val, platform);
			if (parameters != null && parameters.Count > 0)
			{
				path = string.Format("{0}?{1}", new object[2]
				{
					path,
					parameters.ToDataString()
				});
			}
			HttpResponseMessage val2 = (result.HttpResponse = await val.DeleteAsync(new Uri(path)).AsTask<HttpResponseMessage, HttpProgress>().ConfigureAwait(continueOnCapturedContext: false));
			string json = (result.ResponseContent = await val2.Content.ReadAsStringAsync().AsTask<string, ulong>().ConfigureAwait(continueOnCapturedContext: false));
			if (result.HasError && !string.IsNullOrEmpty(result.ResponseContent))
			{
				BaseVineResponseModel baseVineResponseModel = Serialization.Deserialize<BaseVineResponseModel>(result.ResponseContent);
				if (baseVineResponseModel != null && baseVineResponseModel.Code == "419")
				{
					result.RequiresCaptcha = true;
					BaseVineResponseModel<string> captchaResponse = Serialization.Deserialize<BaseVineResponseModel<string>>(result.ResponseContent);
					DispatcherEx.BeginInvoke(delegate
					{
						App.Captcha(captchaResponse.Data);
					});
				}
			}
			if ((object)typeof(T) != typeof(string))
			{
				result.Model = Serialization.Deserialize<T>(json);
			}
			LogIfApiFailed(result);
		}
		catch (Exception error)
		{
			result.Error = error;
		}
		return result;
	}

	public async Task<ApiResult<Stream>> GetVineAuthStream(string path, ParameterCollection parameters)
	{
		ApiResult<Stream> result = new ApiResult<Stream>();
		try
		{
			HttpClient val = new HttpClient();
			SetHeaders(val, WebPlatform.Vine);
			if (parameters != null && parameters.Count > 0)
			{
				path = string.Format("{0}?{1}", new object[2]
				{
					path,
					parameters.ToDataString()
				});
			}
			HttpResponseMessage val2 = (result.HttpResponse = await val.GetAsync(new Uri(path)).AsTask<HttpResponseMessage, HttpProgress>().ConfigureAwait(continueOnCapturedContext: false));
			result.Model = (await val2.Content.ReadAsInputStreamAsync().AsTask<IInputStream, ulong>().ConfigureAwait(continueOnCapturedContext: false)).AsStreamForRead();
			LogIfApiFailed(result);
		}
		catch (Exception error)
		{
			result.Error = error;
		}
		return result;
	}

	private async Task<ApiResult<T>> Get<T>(string path, ParameterCollection parameters, WebPlatform platform, CancellationToken? cancellation = null)
	{
		ApiResult<T> result = new ApiResult<T>();
		try
		{
			HttpClient httpClient = new HttpClient();
			SetHeaders(httpClient, platform);
			if (parameters != null && parameters.Count > 0)
			{
				path = ((path.IndexOf("?", StringComparison.Ordinal) <= 0) ? string.Format("{0}?{1}", new object[2]
				{
					path,
					parameters.ToDataString()
				}) : string.Format("{0}&{1}", new object[2]
				{
					path,
					parameters.ToDataString()
				}));
			}
			HttpResponseMessage val;
			if (!cancellation.HasValue)
			{
				val = await httpClient.GetAsync(new Uri(path)).AsTask<HttpResponseMessage, HttpProgress>().ConfigureAwait(continueOnCapturedContext: false);
			}
			else
			{
				val = await httpClient.GetAsync(new Uri(path)).AsTask<HttpResponseMessage, HttpProgress>(cancellation.Value).ConfigureAwait(continueOnCapturedContext: false);
				cancellation.Value.ThrowIfCancellationRequested();
			}
			result.HttpResponse = val;
			string json = (result.ResponseContent = await val.Content.ReadAsStringAsync().AsTask<string, ulong>().ConfigureAwait(continueOnCapturedContext: false));
			if ((object)typeof(T) != typeof(string))
			{
				result.Model = Serialization.Deserialize<T>(json);
			}
			LogIfApiFailed(result);
		}
		catch (Exception error)
		{
			result.Error = error;
		}
		if (result.HasError && result.Model is BaseVineResponseModel { Code: "103" })
		{
			DispatcherEx.BeginInvoke(App.SignOut);
		}
		return result;
	}

	private async Task<ApiResult<T>> Post<T>(string path, ParameterCollection parameters, WebPlatform platform, string payloadJson = null)
	{
		ApiResult<T> result = new ApiResult<T>();
		try
		{
			HttpClient val = new HttpClient();
			SetHeaders(val, platform);
			if (platform == WebPlatform.Twitter)
			{
				string token = null;
				string tokenSecret = null;
				string text = OAuth.GenerateAuthorizationHeader("POST", path, ConsumerKey, ConsumerSecret, token, tokenSecret, parameters, multiPart: false);
				val.DefaultRequestHeaders.put_Authorization(new HttpCredentialsHeaderValue("OAuth", text));
			}
			IHttpContent val2 = null;
			if (parameters != null && parameters.Any())
			{
				val2 = (IHttpContent)new HttpFormUrlEncodedContent((IEnumerable<KeyValuePair<string, string>>)parameters);
			}
			else if (!string.IsNullOrEmpty(payloadJson))
			{
				val2 = (IHttpContent)new HttpStringContent(payloadJson, (UnicodeEncoding)0, "application/json");
			}
			HttpResponseMessage val3 = (result.HttpResponse = await val.PostAsync(new Uri(path), val2).AsTask<HttpResponseMessage, HttpProgress>().ConfigureAwait(continueOnCapturedContext: false));
			string json = (result.ResponseContent = await val3.Content.ReadAsStringAsync().AsTask<string, ulong>().ConfigureAwait(continueOnCapturedContext: false));
			if (platform == WebPlatform.Twitter && result.HasError)
			{
				TwitterErrorResponseModel errors = Serialization.Deserialize<TwitterErrorResponseModel>(result.ResponseContent);
				result.Error = new TwitterException(errors);
			}
			else
			{
				if (result.HasError && !string.IsNullOrEmpty(result.ResponseContent))
				{
					BaseVineResponseModel baseVineResponseModel = Serialization.Deserialize<BaseVineResponseModel>(result.ResponseContent);
					if (baseVineResponseModel != null && baseVineResponseModel.Code == "419")
					{
						result.RequiresCaptcha = true;
						BaseVineResponseModel<string> captchaResponse = Serialization.Deserialize<BaseVineResponseModel<string>>(result.ResponseContent);
						DispatcherEx.BeginInvoke(delegate
						{
							App.Captcha(captchaResponse.Data);
						});
					}
				}
				if ((object)typeof(T) != typeof(string))
				{
					result.Model = Serialization.Deserialize<T>(json);
				}
			}
			LogIfApiFailed(result);
		}
		catch (Exception error)
		{
			result.Error = error;
		}
		return result;
	}

	private async Task<ApiResult<T>> Put<T>(string path, Stream stream, string contentType, WebPlatform platform, ParameterCollection parameters = null, string payloadJson = null, IProgress<HttpProgress> progressCallback = null)
	{
		ApiResult<T> result = new ApiResult<T>();
		try
		{
			HttpClient httpClient = new HttpClient();
			SetHeaders(httpClient, platform);
			new HttpResponseMessage();
			HttpResponseMessage val;
			if (stream != null)
			{
				HttpStreamContent body = new HttpStreamContent(stream.AsInputStream());
				body.Headers.put_ContentType(new HttpMediaTypeHeaderValue(contentType));
				val = ((progressCallback != null) ? (await httpClient.PutAsync(new Uri(path), (IHttpContent)(object)body).AsTask<HttpResponseMessage, HttpProgress>(progressCallback).ConfigureAwait(continueOnCapturedContext: false)) : (await httpClient.PutAsync(new Uri(path), (IHttpContent)(object)body).AsTask<HttpResponseMessage, HttpProgress>().ConfigureAwait(continueOnCapturedContext: false)));
			}
			else
			{
				IHttpContent val2 = null;
				if (parameters != null)
				{
					val2 = (IHttpContent)new HttpFormUrlEncodedContent((IEnumerable<KeyValuePair<string, string>>)parameters);
				}
				else if (!string.IsNullOrEmpty(payloadJson))
				{
					val2 = (IHttpContent)new HttpStringContent(payloadJson, (UnicodeEncoding)0, "application/json");
				}
				val = await httpClient.PutAsync(new Uri(path), val2).AsTask<HttpResponseMessage, HttpProgress>().ConfigureAwait(continueOnCapturedContext: false);
			}
			result.HttpResponse = val;
			string json = (result.ResponseContent = await val.Content.ReadAsStringAsync().AsTask<string, ulong>().ConfigureAwait(continueOnCapturedContext: false));
			if (result.HasError && !string.IsNullOrEmpty(result.ResponseContent))
			{
				BaseVineResponseModel baseVineResponseModel = Serialization.Deserialize<BaseVineResponseModel>(result.ResponseContent);
				if (baseVineResponseModel != null && baseVineResponseModel.Code == "419")
				{
					result.RequiresCaptcha = true;
					BaseVineResponseModel<string> captchaResponse = Serialization.Deserialize<BaseVineResponseModel<string>>(result.ResponseContent);
					DispatcherEx.BeginInvoke(delegate
					{
						App.Captcha(captchaResponse.Data);
					});
				}
			}
			if ((object)typeof(T) != typeof(string))
			{
				result.Model = Serialization.Deserialize<T>(json);
			}
			LogIfApiFailed(result);
		}
		catch (Exception error)
		{
			result.Error = error;
		}
		return result;
	}

	private void LogIfApiFailed(ApiResult result)
	{
	}

	public static string AcceptLangValue()
	{
		if (_acceptLang != null)
		{
			return _acceptLang;
		}
		string text = ((IDictionary<string, string>)ResourceContext.GetForCurrentView().QualifierValues)["Language"];
		text = text.Split(';')[0];
		string acceptLang = text;
		if (!LangValues.Contains(text))
		{
			string[] array = text.Split('-');
			if (array.Length != 0)
			{
				acceptLang = array[0];
			}
		}
		_acceptLang = acceptLang;
		return _acceptLang;
	}

	private void SetHeaders(HttpClient httpClient, WebPlatform platform)
	{
		//IL_0027: Unknown result type (might be due to invalid IL or missing references)
		//IL_0031: Expected O, but got Unknown
		//IL_003c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0046: Expected O, but got Unknown
		//IL_005b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0065: Expected O, but got Unknown
		//IL_007c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0086: Expected O, but got Unknown
		HttpRequestHeaderCollection defaultRequestHeaders = httpClient.DefaultRequestHeaders;
		string value = "winphone/" + ApplicationSettings.Current.ClientVersion;
		((ICollection<HttpMediaTypeWithQualityHeaderValue>)defaultRequestHeaders.Accept).Add(new HttpMediaTypeWithQualityHeaderValue("application/json"));
		((ICollection<HttpLanguageRangeWithQualityHeaderValue>)defaultRequestHeaders.AcceptLanguage).Add(new HttpLanguageRangeWithQualityHeaderValue(AcceptLangValue()));
		((ICollection<HttpProductInfoHeaderValue>)defaultRequestHeaders.UserAgent).Add(new HttpProductInfoHeaderValue("winphone", ApplicationSettings.Current.ClientVersion));
		((IDictionary<string, string>)defaultRequestHeaders).Add("X-Vine-Client", value);
		((ICollection<HttpContentCodingWithQualityHeaderValue>)defaultRequestHeaders.AcceptEncoding).Add(new HttpContentCodingWithQualityHeaderValue("gzip, deflate"));
		switch (platform)
		{
		case WebPlatform.Twitter:
			((IDictionary<string, string>)defaultRequestHeaders).Add("X-Twitter-Client", "VineForWindowsPhone");
			((IDictionary<string, string>)defaultRequestHeaders).Add("X-Twitter-Client-Version", ApplicationSettings.Current.ClientVersion);
			((IDictionary<string, string>)defaultRequestHeaders).Add("X-Client-UUID", ApplicationSettings.Current.DeviceId);
			break;
		case WebPlatform.Vine:
			if (ApplicationSettings.Current.VineSession != null)
			{
				((IDictionary<string, string>)defaultRequestHeaders).Add("vine-session-id", ApplicationSettings.Current.VineSession.Key);
			}
			break;
		}
	}

	public async Task<ApiResult<AccessToken>> LoginTwitter(string username, string password)
	{
		if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(password))
		{
			throw new ArgumentNullException("username & password are required");
		}
		if (username.StartsWith("@"))
		{
			username = username.Substring(1);
		}
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("x_auth_mode", "client_auth");
		parameterCollection.Add("x_auth_password", password);
		parameterCollection.Add("x_auth_username", username);
		parameterCollection.Add("send_error_codes", bool.TrueString);
		parameterCollection.AddIfHasValue("oauth_verifier", (string)null);
		ApiResult<string> apiResult = await Post<string>("https://api.twitter.com/oauth/access_token", parameterCollection, WebPlatform.Twitter);
		if (apiResult.HasError)
		{
			return new ApiResult<AccessToken>
			{
				ErrorParsed = new Exception("network error or auth error"),
				Error = apiResult.Error,
				RequestContent = apiResult.RequestContent,
				ResponseContent = apiResult.ResponseContent,
				HttpResponse = apiResult.HttpResponse
			};
		}
		Dictionary<string, string> dictionary = (from pair in apiResult.ResponseContent.Split('&')
			select pair.Split('=')).ToDictionary((string[] tokens) => tokens[0], (string[] tokens) => tokens[1]);
		string oAuth_Token = dictionary["oauth_token"];
		string oAuth_Token_Secret = dictionary["oauth_token_secret"];
		AccessToken model = new AccessToken
		{
			OAuth_Token = oAuth_Token,
			OAuth_Token_Secret = oAuth_Token_Secret,
			Screen_Name = dictionary["screen_name"],
			User_Id = dictionary["user_id"]
		};
		return new ApiResult<AccessToken>
		{
			Model = model,
			HttpResponse = apiResult.HttpResponse
		};
	}

	public async Task<ApiResult<VineAuthTokenResult>> LoginVineWithTwitterToken(AccessToken accessToken)
	{
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("deviceToken", ApplicationSettings.Current.DeviceId);
		parameterCollection.Add("twitterId", accessToken.User_Id);
		parameterCollection.Add("twitterOauthSecret", accessToken.OAuth_Token_Secret);
		parameterCollection.Add("twitterOauthToken", accessToken.OAuth_Token);
		return await Post<VineAuthTokenResult>("https://apivi.a1429.lol/users/authenticate/twitter", parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<VineAuthTokenResult>> LoginEmail(string email, string password)
	{
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("username", email);
		parameterCollection.Add("password", password);
		return await Post<VineAuthTokenResult>("https://apivi.a1429.lol/users/authenticate", parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<string>> UploadVideoMp4(Stream stream, IProgress<HttpProgress> progressCallback)
	{
		string path = "https://api-uvr.a1429.lol/upload/videos/1.3.4.mp4";
		ApiResult<string> apiResult = await Put<string>(path, stream, "video/mp4", WebPlatform.Vine, null, null, progressCallback);
		if (!apiResult.HasError && string.IsNullOrEmpty(apiResult.XUploadKey))
		{
			apiResult.ErrorParsed = new Exception("no upload key in response header");
		}
		return apiResult;
	}

	public async Task<ApiResult<string>> UploadVideoThumbnail(Stream stream, IProgress<HttpProgress> progressCallback)
	{
		string path = "https://api-uvr.a1429.lol/upload/thumbs/1.3.4.mp4.jpg";
		ApiResult<string> apiResult = await Put<string>(path, stream, "image/jpeg", WebPlatform.Vine, null, null, progressCallback);
		if (!apiResult.HasError && string.IsNullOrEmpty(apiResult.XUploadKey))
		{
			apiResult.ErrorParsed = new Exception("no upload key in response header");
		}
		return apiResult;
	}

	public async Task<ApiResult<string>> UploadAvatar(Stream stream)
	{
		string path = "https://api-uvr.a1429.lol/upload/avatars/1.3.4.jpg";
		ApiResult<string> apiResult = await Put<string>(path, stream, "image/jpeg", WebPlatform.Vine);
		if (!apiResult.HasError && string.IsNullOrEmpty(apiResult.XUploadKey))
		{
			apiResult.ErrorParsed = new Exception("no upload key in response header");
		}
		return apiResult;
	}

	public async Task<ApiResult<BaseVineResponseModel<AvatarResponseModel>>> UploadAvatar(string xUploadKey)
	{
		ParameterCollection parameterCollection = new ParameterCollection();
		string userId = ApplicationSettings.Current.UserId;
		parameterCollection.Add("avatarUrl", xUploadKey);
		string path = "https://apivi.a1429.lol/users/" + userId;
		return await Put<BaseVineResponseModel<AvatarResponseModel>>(path, null, "image/jpeg", WebPlatform.Vine, parameterCollection);
	}

	public async Task<ApiResult> UploadVine(string videoUrl, string thumbnailUrl, string description, string channelId, bool postToTwitter, bool postToFacebook, List<Entity> entities)
	{
		UploadRequestModel uploadRequestModel = new UploadRequestModel
		{
			VideoUrl = videoUrl,
			ThumbnailUrl = thumbnailUrl,
			ChannelId = channelId,
			Description = description,
			PostToFacebook = postToFacebook,
			PostToTwitter = postToTwitter
		};
		if (entities != null)
		{
			uploadRequestModel.Entities = entities;
		}
		string payloadJson = Serialization.Serialize(uploadRequestModel);
		string path = "https://apivi.a1429.lol/posts";
		return await Post<string>(path, null, WebPlatform.Vine, payloadJson);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetTimeline(int page, int count)
	{
		string path = "https://apivi.a1429.lol/timelines/graph";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<VineTimelineMetaData>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> ResetPassword(string email)
	{
		string path = "https://apivi.a1429.lol/users/forgotPassword?email=" + email;
		ParameterCollection parameters = new ParameterCollection();
		return await Get<BaseVineResponseModel>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetTimelineByTag(string tag, int page, int count)
	{
		string path = "https://apivi.a1429.lol/timelines/tags/" + tag;
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<VineTimelineMetaData>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetPopularNow(int page, int count)
	{
		string path = "https://apivi.a1429.lol/timelines/popular";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<VineTimelineMetaData>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetOnTheRise(int page, int count)
	{
		string path = "https://apivi.a1429.lol/timelines/users/trending";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<VineTimelineMetaData>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetTimelineByPath(string path, int page, int count, string anchor)
	{
		string path2 = "https://apivi.a1429.lol" + path;
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		parameterCollection.Add("anchor", anchor);
		return await Get<BaseVineResponseModel<VineTimelineMetaData>>(path2, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<ChannelMetaData>>> GetChannelList()
	{
		string path = "https://apivi.a1429.lol/channels/featured";
		ParameterCollection parameters = new ParameterCollection();
		return await Get<BaseVineResponseModel<ChannelMetaData>>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetChannelFeatured(string channel, int page, int count)
	{
		string path = "https://apivi.a1429.lol/timelines/channels/" + channel + "/popular";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<VineTimelineMetaData>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetChannelRecent(string channel, int page, int count)
	{
		string path = "https://apivi.a1429.lol/timelines/channels/" + channel + "/recent";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<VineTimelineMetaData>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetUserLikes(string userId, int page, int count)
	{
		string path = "https://apivi.a1429.lol/timelines/users/" + userId + "/likes";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<VineTimelineMetaData>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetTimelineUser(string userId, int page, int count)
	{
		string path = "https://apivi.a1429.lol/timelines/users/" + userId;
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<VineTimelineMetaData>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetPost(string postId, int page = 1)
	{
		string path = "https://apivi.a1429.lol/timelines/posts/" + postId;
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", 20);
		return await Get<BaseVineResponseModel<VineTimelineMetaData>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineUserModel>>> GetUser(string userId)
	{
		string path = "https://apivi.a1429.lol/users/profiles/" + userId;
		ParameterCollection parameters = new ParameterCollection();
		return await Get<BaseVineResponseModel<VineUserModel>>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineUserModel>>> GetUserMe()
	{
		string path = "https://apivi.a1429.lol/users/me";
		ParameterCollection parameters = new ParameterCollection();
		return await Get<BaseVineResponseModel<VineUserModel>>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> PutUser(string userId, string username = null, string description = null, string location = null, string email = null, string phoneNumber = null, string avatarUrl = null, bool? isPrivate = null, string bgColor = null)
	{
		string path = "https://apivi.a1429.lol/users/" + userId;
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.AddIfNotNull("username", username);
		parameterCollection.AddIfNotNull("description", description);
		parameterCollection.AddIfNotNull("location", location);
		parameterCollection.AddIfNotNull("profileBackground", bgColor);
		parameterCollection.AddIfNotNull("email", email);
		parameterCollection.AddIfNotNull("phoneNumber", phoneNumber);
		parameterCollection.AddIfHasValue("avatarUrl", avatarUrl);
		if (isPrivate.HasValue)
		{
			parameterCollection.Add("private", isPrivate.Value ? "1" : "0");
		}
		return await Put<BaseVineResponseModel>(path, null, "", WebPlatform.Vine, parameterCollection);
	}

	public async Task<ApiResult<BaseVineResponseModel>> PutUserPreferences(string userId, bool? hiddenTwitter = null, bool? hiddenPhoneNumber = null, bool? hiddenEmail = null)
	{
		string path = "https://apivi.a1429.lol/users/" + userId + "/preferences";
		ParameterCollection parameterCollection = new ParameterCollection();
		if (hiddenTwitter.HasValue)
		{
			parameterCollection.Add("hiddenTwitter", hiddenTwitter.Value ? "1" : "0");
		}
		if (hiddenPhoneNumber.HasValue)
		{
			parameterCollection.Add("hiddenPhoneNumber", hiddenPhoneNumber.Value ? "1" : "0");
		}
		if (hiddenEmail.HasValue)
		{
			parameterCollection.Add("hiddenEmail", hiddenEmail.Value ? "1" : "0");
		}
		return await Put<BaseVineResponseModel>(path, null, "", WebPlatform.Vine, parameterCollection);
	}

	public async Task<ApiResult<BaseVineResponseModel>> PostExplicit(string userId)
	{
		string path = "https://apivi.a1429.lol/users/" + userId + "/explicit";
		return await Post<BaseVineResponseModel>(path, new ParameterCollection(), WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> DeleteExplicit(string userId)
	{
		string path = "https://apivi.a1429.lol/users/" + userId + "/explicit";
		return await Delete<BaseVineResponseModel>(path, new ParameterCollection(), WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> FollowUser(string userId)
	{
		string path = "https://apivi.a1429.lol/users/" + userId + "/followers";
		ParameterCollection parameters = new ParameterCollection();
		return await Post<BaseVineResponseModel>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> Like(string postId)
	{
		string path = "https://apivi.a1429.lol/posts/" + postId + "/likes";
		ParameterCollection parameters = new ParameterCollection();
		return await Post<BaseVineResponseModel>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> ReportVine(string postId)
	{
		string path = "https://apivi.a1429.lol/posts/" + postId + "/complaints";
		ParameterCollection parameters = new ParameterCollection();
		return await Post<BaseVineResponseModel>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> ReportUser(string reportUserId)
	{
		string path = "https://apivi.a1429.lol/users/" + reportUserId + "/complaints";
		ParameterCollection parameters = new ParameterCollection();
		return await Post<BaseVineResponseModel>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> UnVine(string postId, string repostId)
	{
		string path = "https://apivi.a1429.lol/posts/" + postId + "/repost/" + repostId;
		ParameterCollection parameters = new ParameterCollection();
		return await Delete<BaseVineResponseModel>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<RevineModel>>> Revine(string postId)
	{
		string path = "https://apivi.a1429.lol/posts/" + postId + "/repost";
		ParameterCollection parameters = new ParameterCollection();
		return await Post<BaseVineResponseModel<RevineModel>>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> UnLike(string postId)
	{
		string path = "https://apivi.a1429.lol/posts/" + postId + "/likes";
		ParameterCollection parameters = new ParameterCollection();
		return await Delete<BaseVineResponseModel>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> DeletePost(string postId)
	{
		string path = "https://apivi.a1429.lol/posts/" + postId;
		ParameterCollection parameters = new ParameterCollection();
		return await Delete<BaseVineResponseModel>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> UnfollowUser(string userId)
	{
		string path = "https://apivi.a1429.lol/users/" + userId + "/followers";
		ParameterCollection parameters = new ParameterCollection();
		return await Delete<BaseVineResponseModel>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<InteractionMetaModel>>> GetNotifications(int page, int count)
	{
		string path = "https://apivi.a1429.lol/users/" + ApplicationSettings.Current.UserId + "/notifications/grouped";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<InteractionMetaModel>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineUsersMetaModel>>> GetNotificationUsers(string activityId, int page, int count)
	{
		string path = "https://apivi.a1429.lol/users/" + ApplicationSettings.Current.UserId + "/notifications/grouped/" + activityId + "/users";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<VineUsersMetaModel>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<InteractionMetaModel>>> GetFollowingApprovalsPending()
	{
		string path = "https://apivi.a1429.lol/users/" + ApplicationSettings.Current.UserId + "/notifications/followRequests";
		ParameterCollection parameters = new ParameterCollection();
		return await Get<BaseVineResponseModel<InteractionMetaModel>>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> AcceptFollowerRequest(string userId, string followerId)
	{
		string path = "https://apivi.a1429.lol/users/" + userId + "/followers/requests/" + followerId;
		ParameterCollection parameters = new ParameterCollection();
		return await Put<BaseVineResponseModel>(path, null, "", WebPlatform.Vine, parameters);
	}

	public async Task<ApiResult<BaseVineResponseModel>> DeclineFollowerRequest(string userId, string followerId)
	{
		string path = "https://apivi.a1429.lol/users/" + userId + "/followers/requests/" + followerId;
		ParameterCollection parameters = new ParameterCollection();
		return await Delete<BaseVineResponseModel>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineUnifiedSearchMetaModel>>> UnifiedSearch(string query, CancellationToken cancellation, int? page = null, int? size = null, string searchType = "typing", string sort = "top")
	{
		string path = "https://apivi.a1429.lol/search/sectioned";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("q", query);
		parameterCollection.Add("mode", searchType);
		parameterCollection.Add("sort", sort);
		parameterCollection.AddIfHasValue("page", page);
		parameterCollection.AddIfHasValue("size", size);
		return await Get<BaseVineResponseModel<VineUnifiedSearchMetaModel>>(path, parameterCollection, WebPlatform.Vine, cancellation);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineUsersMetaModel>>> UserSearch(string query, CancellationToken cancellation, int? page = null, int? size = null, string searchType = null)
	{
		string path = "https://apivi.a1429.lol/users/search/" + WebUtility.UrlEncode(query);
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.AddIfHasValue("st", searchType);
		parameterCollection.AddIfHasValue("page", page);
		parameterCollection.AddIfHasValue("size", size);
		return await Get<BaseVineResponseModel<VineUsersMetaModel>>(path, parameterCollection, WebPlatform.Vine, cancellation);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineUsersMetaModel>>> GetPostLikes(string postId, int page, int count)
	{
		string path = "https://apivi.a1429.lol/posts/" + postId + "/likes";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<VineUsersMetaModel>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<List<VineUserModel>>>> GetTwitterFriends(string userId)
	{
		string path = "https://apivi.a1429.lol/users/" + userId + "/following/suggested/twitter";
		ParameterCollection parameters = new ParameterCollection();
		return await Put<BaseVineResponseModel<List<VineUserModel>>>(path, null, null, WebPlatform.Vine, parameters);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineUsersMetaModel>>> GetPostRevines(string postId, int page, int count)
	{
		string path = "https://apivi.a1429.lol/posts/" + postId + "/reposts";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<VineUsersMetaModel>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineUsersMetaModel>>> GetFollowers(string userId, int page, int count)
	{
		string path = "https://apivi.a1429.lol/users/" + userId + "/followers";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<VineUsersMetaModel>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineUsersMetaModel>>> GetFollowing(string userId, int page, int count)
	{
		string path = "https://apivi.a1429.lol/users/" + userId + "/following";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<VineUsersMetaModel>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineTagMetaModel>>> TagSearch(string query, CancellationToken cancellation, int? page = null, int? size = null)
	{
		string path = "https://apivi.a1429.lol/tags/search/" + query;
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.AddIfHasValue("page", page);
		parameterCollection.AddIfHasValue("size", size);
		return await Get<BaseVineResponseModel<VineTagMetaModel>>(path, parameterCollection, WebPlatform.Vine, cancellation);
	}

	public async Task<ApiResult<BaseVineResponseModel<CommentMetaModel>>> GetPostComments(string postId, int page, int count)
	{
		string path = "https://apivi.a1429.lol/posts/" + postId + "/comments";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<CommentMetaModel>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<CommentModel>>> PostComment(string postId, string comment, List<Entity> entities)
	{
		string path = "https://apivi.a1429.lol/posts/" + postId + "/comments";
		string payloadJson = Serialization.Serialize(new PostCommentModel
		{
			Comment = comment,
			Entities = entities
		});
		return await Post<BaseVineResponseModel<CommentModel>>(path, null, WebPlatform.Vine, payloadJson);
	}

	public async Task<ApiResult<BaseVineResponseModel>> SetExplicitContent(string userID)
	{
		string path = "https://apivi.a1429.lol/users/" + userID + "/explicit";
		ParameterCollection parameters = new ParameterCollection();
		return await Post<BaseVineResponseModel>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> ClearExplicitContent(string userID)
	{
		string path = "https://apivi.a1429.lol/users/" + userID + "/explicit";
		ParameterCollection parameters = new ParameterCollection();
		return await Delete<BaseVineResponseModel>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> DeleteComment(string postId, string commentId)
	{
		string path = "https://apivi.a1429.lol/posts/" + postId + "/comments/" + commentId;
		ParameterCollection parameters = new ParameterCollection();
		return await Delete<BaseVineResponseModel>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> BlockUser(string userId, string blockId)
	{
		string path = "https://apivi.a1429.lol/users/" + userId + "/blocked/" + blockId;
		ParameterCollection parameters = new ParameterCollection();
		return await Post<BaseVineResponseModel>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> UnblockUser(string userId, string blockId)
	{
		string path = "https://apivi.a1429.lol/users/" + userId + "/blocked/" + blockId;
		ParameterCollection parameters = new ParameterCollection();
		return await Delete<BaseVineResponseModel>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineAuthToken>>> CreateAccount(string username, string email, string password, string phoneNumber = "", string description = "", string location = "")
	{
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("email", email);
		parameterCollection.Add("username", username);
		parameterCollection.Add("password", password);
		parameterCollection.Add("phoneNumber", phoneNumber);
		parameterCollection.Add("authenticate", "true");
		return await Post<BaseVineResponseModel<VineAuthToken>>("https://apivi.a1429.lol/users", parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineAuthToken>>> CreateAccountWithTwitterCredentials(string username, string twitterId, AccessToken accessToken)
	{
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("username", username);
		parameterCollection.Add("twitterId", accessToken.User_Id);
		parameterCollection.Add("twitterOauthSecret", accessToken.OAuth_Token_Secret);
		parameterCollection.Add("twitterOauthToken", accessToken.OAuth_Token);
		parameterCollection.Add("authenticate", "true");
		return await Post<BaseVineResponseModel<VineAuthToken>>("https://apivi.a1429.lol/users", parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineAuthToken>>> UpdateTwitterCredentials(string twitterId, string oauthToken, string oauthSecret)
	{
		string path = "https://apivi.a1429.lol/users/" + ApplicationSettings.Current.VineSession.UserId;
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("twitterId", twitterId);
		parameterCollection.Add("twitterOauthSecret", oauthSecret);
		parameterCollection.Add("twitterOauthToken", oauthToken);
		return await Put<BaseVineResponseModel<VineAuthToken>>(path, null, null, WebPlatform.Vine, parameterCollection);
	}

	public async Task<ApiResult<BaseVineResponseModel>> UpdateFacebookCredentials(string facebookId, string facebookOAuthToken, string userID)
	{
		string path = "https://apivi.a1429.lol/users/" + userID;
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("facebookId", facebookId);
		parameterCollection.Add("facebookOauthToken", facebookOAuthToken);
		return await Put<BaseVineResponseModel>(path, null, null, WebPlatform.Vine, parameterCollection);
	}

	public async Task<ApiResult<BaseVineResponseModel<List<VineUserModel>>>> GetUserContacts(string userId, List<VineContactModel> contacts)
	{
		string path = "https://apivi.a1429.lol/users/" + userId + "/following/suggested/contacts";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("addressBook", Serialization.Serialize(contacts));
		parameterCollection.Add("locale", RegionInfo.CurrentRegion.TwoLetterISORegionName);
		return await Put<BaseVineResponseModel<List<VineUserModel>>>(path, null, null, WebPlatform.Vine, parameterCollection);
	}

	public async Task<ApiResult<FbUserModel>> FbMeGet(string token)
	{
		string path = "https://graph.facebook.com/me?access_token=" + token;
		ParameterCollection parameters = new ParameterCollection();
		return await Get<FbUserModel>(path, parameters, WebPlatform.Facebook);
	}

	public async Task<ApiResult<FbPermissions>> FbPermissionsGet(string userId, string token)
	{
		string path = string.Format("https://graph.facebook.com/{0}/permissions?access_token={1}", new object[2] { userId, token });
		ParameterCollection parameters = new ParameterCollection();
		return await Get<FbPermissions>(path, parameters, WebPlatform.Facebook);
	}

	public async Task<ApiResult> FbPermissionsDelete(string userId, string token)
	{
		string path = string.Format("https://graph.facebook.com/{0}/permissions?access_token={1}", new object[2] { userId, token });
		ParameterCollection parameters = new ParameterCollection();
		return await Delete<string>(path, parameters, WebPlatform.Facebook);
	}

	public async Task<ApiResult<BaseVineResponseModel<ConversationMetaModel>>> GetVineMessageConversations(int page, int count, bool inbox)
	{
		string path = "https://apivi.a1429.lol/conversations";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		if (!inbox)
		{
			parameterCollection.Add("inbox", "other");
		}
		return await Get<BaseVineResponseModel<ConversationMetaModel>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineMessageMetaModel>>> GetVineMessagesByConversations(string conversationId, int page, int count, bool prefetch = true)
	{
		string path = "https://apivi.a1429.lol/conversations/" + conversationId;
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		parameterCollection.Add("prefetch", prefetch ? "1" : "0");
		return await Get<BaseVineResponseModel<VineMessageMetaModel>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineMessageActivityCounts>>> GetActivityCounts(string userId)
	{
		string path = "https://apivi.a1429.lol/users/" + userId + "/activityCounts";
		ParameterCollection parameters = new ParameterCollection();
		return await Get<BaseVineResponseModel<VineMessageActivityCounts>>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineUsersMetaModel>>> GetFriends(string userId, int count, string anchor)
	{
		string path = "https://apivi.a1429.lol/users/" + userId + "/friends";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("size", count);
		parameterCollection.AddIfHasValue("anchor", anchor);
		return await Get<BaseVineResponseModel<VineUsersMetaModel>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> DeleteConversation(string userId, string conversationId, string lastMessageId, bool ignore)
	{
		string text = "https://apivi.a1429.lol/conversations/" + conversationId;
		if (ignore)
		{
			text += "/ignored";
		}
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("lastMessageId", lastMessageId);
		return await Delete<BaseVineResponseModel>(text, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel>> IgnoreConversation(string userId, string conversationId)
	{
		string path = "https://apivi.a1429.lol/conversations/" + conversationId + "/ignored";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("user_id", userId);
		return await Post<BaseVineResponseModel>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineMessageResponseMetaModel>>> PostConversation(List<string> userIds, List<string> phoneNumbers, List<string> emails, string message, string videoUrl, string thumbnailUrl, string postId, int? maxLoops, string locale)
	{
		string path = "https://apivi.a1429.lol/conversations";
		List<Dictionary<string, object>> list = new List<Dictionary<string, object>>();
		if (userIds != null && userIds.Count > 0)
		{
			foreach (string userId in userIds)
			{
				Dictionary<string, object> dictionary = new Dictionary<string, object>();
				dictionary.Add("userId", userId);
				list.Add(dictionary);
			}
		}
		if (emails != null && emails.Count > 0)
		{
			foreach (string email in emails)
			{
				Dictionary<string, object> dictionary2 = new Dictionary<string, object>();
				dictionary2.Add("email", email);
				list.Add(dictionary2);
			}
		}
		if (phoneNumbers != null && phoneNumbers.Count > 0)
		{
			foreach (string phoneNumber in phoneNumbers)
			{
				Dictionary<string, object> dictionary3 = new Dictionary<string, object>();
				dictionary3.Add("phone", phoneNumber);
				list.Add(dictionary3);
			}
		}
		if (string.IsNullOrEmpty(videoUrl))
		{
			string.IsNullOrEmpty(postId);
		}
		UploadMessageModel uploadMessageModel = new UploadMessageModel
		{
			Locale = locale,
			To = list,
			Created = DateTime.UtcNow.ToString("o"),
			ThumbnailUrl = thumbnailUrl,
			VideoUrl = videoUrl
		};
		if (!string.IsNullOrEmpty(message))
		{
			uploadMessageModel.Message = message;
		}
		if (!string.IsNullOrEmpty(postId))
		{
			uploadMessageModel.PostId = postId;
		}
		if (maxLoops.HasValue)
		{
			uploadMessageModel.MaxLoops = maxLoops;
		}
		string payloadJson = Serialization.Serialize(uploadMessageModel);
		ApiResult<BaseVineResponseModel<VineMessageResponseMetaModel>> apiResult = await Post<BaseVineResponseModel<VineMessageResponseMetaModel>>(path, null, WebPlatform.Vine, payloadJson);
		if (!apiResult.HasError)
		{
			CheckMessageError(apiResult);
		}
		return apiResult;
	}

	public async Task<ApiResult<BaseVineResponseModel<VineMessageResponseMetaModel>>> ReplyToConversation(string conversationId, string message, string videoUrl, string thumbnailUrl)
	{
		string path = "https://apivi.a1429.lol/conversations/" + conversationId;
		string payloadJson = Serialization.Serialize(new UploadMessageModel
		{
			Created = DateTime.UtcNow.ToString(),
			Message = message,
			ThumbnailUrl = thumbnailUrl,
			VideoUrl = videoUrl
		});
		ApiResult<BaseVineResponseModel<VineMessageResponseMetaModel>> apiResult = await Post<BaseVineResponseModel<VineMessageResponseMetaModel>>(path, null, WebPlatform.Vine, payloadJson);
		if (!apiResult.HasError)
		{
			CheckMessageError(apiResult);
		}
		return apiResult;
	}

	private void CheckMessageError(ApiResult<BaseVineResponseModel<VineMessageResponseMetaModel>> result)
	{
		if (result.Model != null && result.Model.Data != null && result.Model.Data.Messages.Any() && result.Model.Data.Messages[0].Error != null)
		{
			result.ErrorParsed = new MessageException(result.Model.Data.Messages[0].Error.Code, result.Model.Data.Messages[0].Error.Message);
		}
	}

	public async Task<ApiResult> VerifyEmail(string code)
	{
		string path = "https://apivi.a1429.lol/users/verifyEmail";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("t", code);
		return await Post<string>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult> SendVerifyEmailRequest()
	{
		string path = "https://apivi.a1429.lol/users/verifyEmail";
		ParameterCollection parameters = new ParameterCollection();
		return await Post<string>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult> VerifyPhone(string code)
	{
		string path = "https://apivi.a1429.lol/users/verifyPhoneNumber";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("t", code);
		return await Post<string>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult> SendVerifyPhoneRequest()
	{
		string path = "https://apivi.a1429.lol/users/verifyPhoneNumber";
		ParameterCollection parameters = new ParameterCollection();
		return await Post<string>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<LoopResponseModel>>> UpdateLoops(List<LoopCountsModel> loopCounts)
	{
		string path = "https://apivi.a1429.lol/loops";
		ParameterCollection parameters = new ParameterCollection();
		string payloadJson = Serialization.Serialize(new UploadLoopsModel
		{
			loops = loopCounts
		});
		return await Post<BaseVineResponseModel<LoopResponseModel>>(path, parameters, WebPlatform.Vine, payloadJson);
	}

	public async Task<ApiResult> DeactivateAccount()
	{
		string path = "https://apivi.a1429.lol/users/me";
		new ParameterCollection();
		return await Delete<BaseVineResponseModel>(path, new ParameterCollection(), WebPlatform.Vine);
	}

	public async Task<ApiResult<VineAuthTokenResult>> ActivateAccountFromTwitter(AccessToken accessToken)
	{
		string path = "https://apivi.a1429.lol/users/reactivate/twitter";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("deviceToken", ApplicationSettings.Current.DeviceId);
		parameterCollection.Add("twitterId", accessToken.User_Id);
		parameterCollection.Add("twitterOauthSecret", accessToken.OAuth_Token_Secret);
		parameterCollection.Add("twitterOauthToken", accessToken.OAuth_Token);
		return await Post<VineAuthTokenResult>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<VineAuthTokenResult>> ActivateAccountFromVine(string email, string password)
	{
		string path = "https://apivi.a1429.lol/users/reactivate";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("username", email);
		parameterCollection.Add("password", password);
		return await Post<VineAuthTokenResult>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<string>> BulkFollowUsers(IEnumerable<string> userIdsToFollow)
	{
		ParameterCollection parameterCollection = new ParameterCollection();
		foreach (string item in userIdsToFollow)
		{
			parameterCollection.Add("userIds[]", item);
		}
		string path = "https://apivi.a1429.lol/users/followers";
		return await Post<string>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<List<VineUserModel>>>> GetSuggestedToFollow(string userId)
	{
		string path = $"https://apivi.a1429.lol/users/{userId}/following/suggested";
		ParameterCollection parameters = new ParameterCollection();
		Task<ApiResult<BaseVineResponseModel<VineUsersMetaModel>>> getTask = Get<BaseVineResponseModel<VineUsersMetaModel>>(path, parameters, WebPlatform.Vine);
		Task task = Task.Delay(new TimeSpan(0, 0, 10));
		await Task.WhenAny(getTask, task);
		if (getTask.IsCompleted)
		{
			ApiResult<BaseVineResponseModel<VineUsersMetaModel>> result = getTask.Result;
			ApiResult<BaseVineResponseModel<List<VineUserModel>>> apiResult = new ApiResult<BaseVineResponseModel<List<VineUserModel>>>
			{
				Error = result.Error,
				HttpResponse = result.HttpResponse,
				RequestContent = result.RequestContent,
				RequiresCaptcha = result.RequiresCaptcha,
				ResponseContent = result.ResponseContent
			};
			if (!result.HasError)
			{
				apiResult.Model = new BaseVineResponseModel<List<VineUserModel>>
				{
					Code = result.Model.Code,
					Data = result.Model.Data.Records,
					Error = result.Model.Error,
					Success = result.Model.Success
				};
			}
			return apiResult;
		}
		return new ApiResult<BaseVineResponseModel<List<VineUserModel>>>
		{
			Error = new TimeoutException()
		};
	}

	public async Task<ApiResult> ScribeList(ScribeLog log)
	{
		ParameterCollection parameters = new ParameterCollection();
		string payloadJson = Serialization.SerializeIgnoreNulls(log);
		return await Post<string>("https://apivi.a1429.lol/jot", parameters, WebPlatform.Vine, payloadJson);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineUsersMetaModel>>> GetSimilarSuggestions(string userId)
	{
		string path = "https://apivi.a1429.lol/recommendations/similarUsersByFollowers/" + userId;
		ParameterCollection parameters = new ParameterCollection();
		return await Get<BaseVineResponseModel<VineUsersMetaModel>>(path, parameters, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetSimilarPosts(string postId, int page, int count)
	{
		string path = "https://apivi.a1429.lol/timelines/similar/post/" + postId;
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("page", page);
		parameterCollection.Add("size", count);
		return await Get<BaseVineResponseModel<VineTimelineMetaData>>(path, parameterCollection, WebPlatform.Vine);
	}

	public async Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetSearchTimeline(string query, ListType listType, int? page, int? count)
	{
		new CancellationTokenSource();
		string value = ((listType == ListType.SearchFeatured) ? "top" : "recent");
		string path = "https://apivi.a1429.lol/search/posts";
		ParameterCollection parameterCollection = new ParameterCollection();
		parameterCollection.Add("q", query);
		parameterCollection.Add("sort", value);
		parameterCollection.AddIfHasValue("page", page);
		parameterCollection.AddIfHasValue("size", count);
		return await Get<BaseVineResponseModel<VineTimelineMetaData>>(path, parameterCollection, WebPlatform.Vine);
	}
}
