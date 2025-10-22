using System;
using Vine.Common;
using Vine.Events;
using Windows.Data.Xml.Dom;
using Windows.UI.Notifications;

namespace Vine.Framework;

public static class ToastHelper
{
	public static void Show(string title, string msg, string type = "ignore", string tag = null, string data = null)
	{
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		//IL_002e: Expected O, but got Unknown
		//IL_004c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0052: Expected O, but got Unknown
		string text = Serialization.Serialize(new LaunchParameters
		{
			Type = type,
			Title = title,
			Msg = msg,
			Data = data
		});
		XmlDocument val = new XmlDocument();
		val.LoadXml("<toast launch=\"{2}\"><visual ><binding template=\"ToastImageAndText02\"><image id=\"1\" src=\"\" /><text id=\"1\" >{0}</text><text id=\"2\" >{1}</text></binding></visual></toast>", title, msg, text);
		ToastNotification val2 = new ToastNotification(val);
		if (type == "ignore")
		{
			val2.put_ExpirationTime((DateTimeOffset?)(DateTimeOffset)DateTime.UtcNow.AddSeconds(1.0));
		}
		if (tag != null)
		{
			val2.put_Tag(tag);
		}
		ToastNotificationManager.CreateToastNotifier().Show(val2);
	}

	public static void Delete(string tag)
	{
		ToastNotificationManager.History.Remove(tag);
	}
}
