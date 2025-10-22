using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
using System.Net;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Threading;
using Facebook;
using Microsoft.Phone.Controls;
using Newtonsoft.Json;
using Vine.Datas;
using Vine.Pages.Facebook;

namespace Vine;

public class FBHelper
{
	public static string AppId;

	public static string AppSecret;

	private readonly string[] _extendedPermissions = new string[2] { "offline_access", "publish_stream" };

	protected WebBrowser Browser;

	private Action<string, string> CallbackLogin;

	private DispatcherTimer Timer;

	private bool _shorttoken;

	private static FacebookClient fb;

	static FBHelper()
	{
		AppId = "1185475921592170";
		AppSecret = "31fccce8904e3be10f2127a02d59e05e";
		fb = new FacebookClient
		{
			AppId = AppId
		};
	}

	public void SetBrowser(WebBrowser bro)
	{
		if (Browser != bro)
		{
			if (bro != null)
			{
				bro.Navigating -= Browser_Navigating;
			}
			Browser = bro;
			Browser.Navigating += Browser_Navigating;
		}
	}

	private void Browser_Navigating(object sender, NavigatingEventArgs e)
	{
		//IL_00ff: Unknown result type (might be due to invalid IL or missing references)
		//IL_0105: Expected O, but got Unknown
		if (Timer != null)
		{
			Timer.Stop();
			Timer = null;
		}
		if (new FacebookClient().TryParseOAuthCallbackUrl(e.Uri, out var facebookOAuthResult))
		{
			((UIElement)Browser).Visibility = (Visibility)1;
			if (facebookOAuthResult.IsSuccess)
			{
				if (_shorttoken)
				{
					if (CallbackLogin != null)
					{
						((CancelEventArgs)(object)e).Cancel = true;
						fb.AccessToken = facebookOAuthResult.AccessToken;
						RetrieveUserId(delegate(string id)
						{
							CallbackLogin(fb.AccessToken, id);
						});
					}
				}
				else
				{
					GetLongOAuth(facebookOAuthResult.AccessToken, CallbackLogin);
				}
			}
			else
			{
				CallbackLogin(null, null);
			}
		}
		else if (!(e.Uri == null))
		{
			if (e.Uri.LocalPath.StartsWith("/login.php") || e.Uri.AbsoluteUri.Contains("facebook.com/dialog/oauth"))
			{
				((UIElement)Browser).Visibility = (Visibility)0;
			}
			else if (e.Uri.LocalPath == "/connect/uiserver.php")
			{
				DispatcherTimer val = new DispatcherTimer();
				val.Interval = TimeSpan.FromMilliseconds(600.0);
				Timer = val;
				Timer.Tick += t_Tick;
				Timer.Start();
			}
		}
	}

	public IDictionary<string, string> ParseParams(string paramsString)
	{
		try
		{
			if (string.IsNullOrEmpty(paramsString))
			{
				return new Dictionary<string, string>();
			}
			Dictionary<string, string> dictionary = new Dictionary<string, string>();
			if (paramsString.StartsWith("?"))
			{
				paramsString = paramsString.Substring(1);
			}
			int length = paramsString.Length;
			for (int i = 0; i < length; i++)
			{
				int num = i;
				int num2 = -1;
				for (; i < length; i++)
				{
					switch (paramsString[i])
					{
					case '=':
						if (num2 < 0)
						{
							num2 = i;
						}
						continue;
					default:
						continue;
					case '&':
						break;
					}
					break;
				}
				string text;
				string text2;
				if (num2 >= 0)
				{
					text = paramsString.Substring(num, num2 - num);
					text2 = paramsString.Substring(num2 + 1, i - num2 - 1);
				}
				else
				{
					text = paramsString.Substring(num, i - num);
					text2 = null;
				}
				dictionary.Add(HttpUtility.UrlDecode(text), HttpUtility.UrlDecode(text2));
				if (i == length - 1 && paramsString[i] == '&')
				{
					dictionary.Add(null, string.Empty);
				}
			}
			return dictionary;
		}
		catch
		{
			return new Dictionary<string, string>();
		}
	}

	private void GetLongOAuth(string accessToken, Action<string, string> callbackLogin)
	{
		WebClient webClient = new WebClient();
		webClient.DownloadStringCompleted += delegate(object senderr, DownloadStringCompletedEventArgs er)
		{
			if (er.Cancelled || er.Error != null)
			{
				if (callbackLogin != null)
				{
					callbackLogin(null, null);
				}
			}
			else
			{
				try
				{
					IDictionary<string, string> dictionary = ParseParams(er.Result);
					if (dictionary.ContainsKey("access_token"))
					{
						fb.AccessToken = dictionary["access_token"];
						if (callbackLogin != null)
						{
							RetrieveUserId(delegate(string userid)
							{
								callbackLogin(fb.AccessToken, userid);
							});
						}
						return;
					}
				}
				catch
				{
				}
				if (callbackLogin != null)
				{
					callbackLogin(null, null);
				}
			}
		};
		webClient.DownloadStringAsync(new Uri("https://graph.facebook.com/oauth/access_token?client_id=" + AppId + "&client_secret=" + AppSecret + "&grant_type=fb_exchange_token&fb_exchange_token=" + HttpUtility.UrlEncode(accessToken)));
	}

	private async Task RetrieveUserId(Action<string> callback)
	{
		_ = 1;
		try
		{
			JsonObject jsonObject = (JsonObject)(await fb.GetTaskAsync("/me"));
			callback((string)jsonObject["id"]);
		}
		catch
		{
		}
	}

	private void t_Tick(object sender, EventArgs e)
	{
		((UIElement)Browser).Visibility = (Visibility)0;
	}

	public static void GetPages(Action<List<FacebookPageInfo>> callback)
	{
		string requestUriString = "https://graph.facebook.com/me/accounts?type=page&sdk=android&migration_bundle=fbsdk%3A20121003&format=json&access_token=" + DatasProvider.Instance.CurrentUser.FacebookAccess.AccessToken;
		HttpWebRequest request = WebRequest.CreateHttp(requestUriString);
		request.BeginGetResponse(delegate(IAsyncResult iar)
		{
			try
			{
				Stream responseStream = request.EndGetResponse(iar).GetResponseStream();
				FacebookPageRootObject facebookPageRootObject = new JsonSerializer().Deserialize<FacebookPageRootObject>(new JsonTextReader(new StreamReader(responseStream)));
				callback(facebookPageRootObject.data);
			}
			catch
			{
				callback(null);
			}
		}, null);
	}

	public void ConnectMe(bool shorttoken, Action<string, string> callback)
	{
		_shorttoken = shorttoken;
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		dictionary["display"] = "touch";
		dictionary["state"] = new Random().Next();
		dictionary["scope"] = _extendedPermissions;
		dictionary["response_type"] = "token";
		dictionary["redirect_uri"] = "https://www.vine.co";
		dictionary["client_id"] = AppId;
		CallbackLogin = callback;
		Uri loginUrl = fb.GetLoginUrl(dictionary);
		Browser.Navigate(loginUrl);
	}

	internal void DisconnectBrowser()
	{
		if (Browser != null)
		{
			Browser.Navigating -= Browser_Navigating;
		}
	}
}
