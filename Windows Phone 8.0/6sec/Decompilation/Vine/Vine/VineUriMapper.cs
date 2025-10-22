using System;
using System.Collections.Generic;
using System.Windows.Navigation;
using Vine.Datas;

namespace Vine;

internal class VineUriMapper : UriMapperBase
{
	private string tempUri;

	public override Uri MapUri(Uri uri)
	{
		tempUri = uri.ToString();
		bool flag = true;
		Uri uri2 = null;
		if (tempUri.StartsWith("/Main"))
		{
			string extra = null;
			int num = tempUri.IndexOf('?');
			if (num > 0)
			{
				extra = tempUri.Substring(num + 1);
			}
			uri2 = NavigationServiceExt.UriTimeline(null, removebackentry: false, null, extra);
		}
		if (tempUri.StartsWith("/Profile/"))
		{
			uri2 = NavigationServiceExt.UriProfile(tempUri.Substring(9));
		}
		else if ((tempUri.Contains("ShareContent") || tempUri.Contains("EditPhotoContent")) && tempUri.Contains("FileId"))
		{
			int startIndex = tempUri.IndexOf("?");
			uri2 = new Uri("/Pages/CropImage/CropPage.xaml" + tempUri.Substring(startIndex), UriKind.Relative);
		}
		else if (tempUri.Contains("RichMediaEdit") && tempUri.Contains("token"))
		{
			int startIndex2 = tempUri.IndexOf("?");
			uri2 = new Uri("/Pages/Main/MainPage.xaml" + tempUri.Substring(startIndex2), UriKind.Relative);
		}
		else
		{
			try
			{
				if (tempUri.StartsWith("/Timeline/"))
				{
					uri2 = new Uri("/Pages/Main/MainPage.xaml?type=" + tempUri.Substring(10), UriKind.Relative);
				}
				if (tempUri.StartsWith("/Protocol?"))
				{
					IDictionary<string, string> dictionary = HttpParseQuery.ParseQueryString(tempUri.Substring(10));
					if (dictionary.ContainsKey("encodedLaunchUri"))
					{
						string text = dictionary["encodedLaunchUri"];
						string[] array = text.Split(new char[1] { '?' }, 2);
						if (array.Length == 2)
						{
							IDictionary<string, string> dictionary2 = HttpParseQuery.ParseQueryString(array[1]);
							if (dictionary2.ContainsKey("message"))
							{
								_ = dictionary2["message"];
							}
							text = array[0];
						}
						string[] array2 = text.Split(new char[1] { ':' }, 2);
						if (array2.Length == 2)
						{
							string[] array3 = array2[1].TrimStart('/').Split('/');
							switch (array3[0])
							{
							case "_captcha":
							{
								DataUser currentUserInValidation = DatasProvider.Instance.CurrentUserInValidation;
								if (currentUserInValidation != null)
								{
									string text2 = ((currentUserInValidation.TwitterAccess != null) ? currentUserInValidation.TwitterAccess.ScreenName : null);
									string forcedEmail = ((text2 == null) ? currentUserInValidation.Email : null);
									flag = false;
									uri2 = NavigationServiceExt.UriLogin(removebackentry: true, null, directshowresetpassword: false, forcedEmail, text2);
								}
								break;
							}
							case "url":
								if (array3[1].Contains("vine.co"))
								{
									uri2 = new Uri("/Pages/AnalyzeUri/AnalyzeUriPage.xaml?uri=" + array3[1], UriKind.Relative);
								}
								break;
							case "user":
								uri2 = NavigationServiceExt.UriProfile(array3[1], removebackentry: true);
								break;
							case "profile":
								uri2 = NavigationServiceExt.UriProfileFromName(array3[1], removebackentry: true);
								break;
							case "post":
								uri2 = NavigationServiceExt.UriPost(array3[1], removebackentry: true);
								break;
							}
						}
					}
					if (uri2 == null)
					{
						uri2 = NavigationServiceExt.UriTimeline();
					}
				}
			}
			catch
			{
				uri2 = NavigationServiceExt.UriTimeline();
			}
		}
		if (tempUri.Contains("ViewfinderLaunch"))
		{
			uri2 = new Uri("/Pages/Camera/CameraPage.xaml", UriKind.Relative);
		}
		Vine.Datas.Datas instance = DatasProvider.Instance;
		if (flag && (instance.CurrentUser == null || instance.CurrentUser.User == null) && !uri.OriginalString.StartsWith("/Pages/CreateAccount"))
		{
			string originalString = (uri2 ?? uri).OriginalString;
			originalString = ((!originalString.Contains("?")) ? (originalString + "?fromcreation=true") : (originalString + "&fromcreation=true"));
			return NavigationServiceExt.UriLogin(removebackentry: false, originalString);
		}
		if (uri2 != null)
		{
			return uri2;
		}
		return uri;
	}
}
