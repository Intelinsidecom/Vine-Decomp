using System;
using System.Net;

namespace Vine.Services;

public class OwnService
{
	public static void RemoveVideo(string uri)
	{
		try
		{
			int num = uri.IndexOf("/data");
			string text = uri.Substring(0, num);
			string text2 = uri.Substring(num + 1);
			string text3 = text2;
			text2 = text3.Substring(text3.IndexOf("/") + 1);
			HttpWebRequest req = WebRequest.CreateHttp(text + "/publish.php?id=" + HttpUtility.UrlEncode(text2));
			req.BeginGetResponse(delegate(IAsyncResult iar)
			{
				try
				{
					req.EndGetResponse(iar);
				}
				catch
				{
				}
			}, null);
		}
		catch
		{
		}
	}
}
