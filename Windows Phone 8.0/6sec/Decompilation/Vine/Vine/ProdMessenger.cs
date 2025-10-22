using System;
using System.Collections.Generic;
using System.Globalization;
using System.Net;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Xml.Linq;
using Vine.Controls.Toast;
using Vine.Datas;
using Vine.Services;
using Vine.Services.Models;

namespace Vine;

public class ProdMessenger
{
	internal static void GetMessage()
	{
		WebClient webClient = new WebClient();
		webClient.DownloadStringCompleted += ProdMessenger_DownloadStringCompleted;
		webClient.DownloadStringAsync(new Uri("http://www2.huynapps.com/wp/6sec/info.php?c=" + CultureInfo.CurrentUICulture.Name + "&u=" + CultureInfo.CurrentCulture.Name + "&v=" + AppVersion.Version));
	}

	private static void ProdMessenger_DownloadStringCompleted(object sender, DownloadStringCompletedEventArgs e)
	{
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Expected O, but got Unknown
		if (e.Cancelled || e.Error != null)
		{
			return;
		}
		try
		{
			XElement xElement = XElement.Parse(e.Result);
			XElement xElement2 = xElement.Element("Message");
			Vine.Datas.Datas instance = DatasProvider.Instance;
			if (xElement2 != null)
			{
				int num = int.Parse(xElement2.Attribute("version").Value);
				string value = xElement2.Value;
				if (instance.LastMessageNbr < num)
				{
					ToastPrompt toastPrompt = new ToastPrompt();
					toastPrompt.Title = "6Sec Breaking News";
					toastPrompt.Message = value;
					toastPrompt.MillisecondsUntilHidden = 8000;
					toastPrompt.TextOrientation = (Orientation)0;
					toastPrompt.ImageSource = (ImageSource)new BitmapImage(new Uri("/Assets/ApplicationIcon.png", UriKind.Relative));
					toastPrompt.Show();
					instance.LastMessageNbr = num;
				}
			}
			XAttribute xAttribute = xElement.Attribute("disable-review");
			if (xAttribute != null)
			{
				instance.DisableReview = xAttribute.Value == "true";
			}
			else
			{
				instance.DisableReview = false;
			}
			XAttribute xAttribute2 = xElement.Attribute("adgobeg");
			if (xAttribute2 != null)
			{
				instance.AdGoBegin = xAttribute2.Value == "true";
			}
			XAttribute xAttribute3 = xElement.Attribute("serveruri");
			if (xAttribute3 != null && !string.IsNullOrEmpty(xAttribute3.Value))
			{
				instance.ServerUri = xAttribute3.Value;
			}
			List<CustomAd> list = new List<CustomAd>();
			XElement xElement3 = xElement.Element("CustomAds");
			if (xElement3 != null)
			{
				foreach (XElement item in xElement3.Elements("Ad"))
				{
					try
					{
						string value2 = item.Attribute("image").Value;
						string value3 = item.Attribute("link").Value;
						list.Add(new CustomAd
						{
							Image = value2,
							Link = value3
						});
					}
					catch
					{
					}
				}
			}
			DatasProvider.Instance.CustomAds = list;
		}
		catch
		{
		}
	}
}
