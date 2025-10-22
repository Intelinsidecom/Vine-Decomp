using System;
using System.Collections.Generic;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Text.RegularExpressions;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace Vine.Framework;

public class OSVersionHelper
{
	private const string Html = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\r\n        <html>\r\n        <head>\r\n        <script language=\"javascript\" type=\"text/javascript\">\r\n            function notifyUA() {\r\n                window.external.notify(navigator.userAgent);\r\n            }\r\n        </script>\r\n        </head>\r\n        <body onload=\"notifyUA();\"></body>\r\n        </html>";

	private static bool? _isWindows10;

	public static bool IsWindows10
	{
		get
		{
			if (!_isWindows10.HasValue)
			{
				try
				{
					_isWindows10 = CheckWindows10();
				}
				catch
				{
					_isWindows10 = false;
				}
			}
			return _isWindows10.Value;
		}
	}

	public static void GetOSVersion(Panel rootElement, Action<string> callback)
	{
		//IL_0015: Unknown result type (might be due to invalid IL or missing references)
		//IL_001f: Expected O, but got Unknown
		//IL_0053: Unknown result type (might be due to invalid IL or missing references)
		//IL_005d: Expected O, but got Unknown
		//IL_0085: Unknown result type (might be due to invalid IL or missing references)
		//IL_008f: Expected O, but got Unknown
		WebView browser = new WebView();
		((UIElement)browser).put_Visibility((Visibility)1);
		WebView val = browser;
		WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)((FrameworkElement)val).add_Loaded, (Action<EventRegistrationToken>)((FrameworkElement)val).remove_Loaded, (RoutedEventHandler)delegate
		{
			browser.NavigateToString("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\r\n        <html>\r\n        <head>\r\n        <script language=\"javascript\" type=\"text/javascript\">\r\n            function notifyUA() {\r\n                window.external.notify(navigator.userAgent);\r\n            }\r\n        </script>\r\n        </head>\r\n        <body onload=\"notifyUA();\"></body>\r\n        </html>");
		});
		val = browser;
		WindowsRuntimeMarshal.AddEventHandler((Func<NotifyEventHandler, EventRegistrationToken>)val.add_ScriptNotify, (Action<EventRegistrationToken>)val.remove_ScriptNotify, (NotifyEventHandler)delegate(object sender, NotifyEventArgs args)
		{
			string value = args.Value;
			((ICollection<UIElement>)rootElement.Children).Remove((UIElement)(object)browser);
			callback(ExtractOSVersionFromUserAgent(value));
		});
		((ICollection<UIElement>)rootElement.Children).Add((UIElement)(object)browser);
	}

	private static string ExtractOSVersionFromUserAgent(string userAgent)
	{
		Match match = Regex.Match(userAgent, "Windows Phone ([^;]+)");
		if (match.Success && match.Groups.Count > 1)
		{
			return match.Groups[1].Value;
		}
		return null;
	}

	private static bool CheckWindows10()
	{
		return (object)Type.GetType("Windows.System.Profile.AnalyticsInfo, Windows, ContentType=WindowsRuntime") != null;
	}
}
