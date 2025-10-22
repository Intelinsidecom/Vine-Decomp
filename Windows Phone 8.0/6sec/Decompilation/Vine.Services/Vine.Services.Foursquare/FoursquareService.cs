using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Net;
using Newtonsoft.Json;

namespace Vine.Services.Foursquare;

public class FoursquareService
{
	private static string ClientId = "FWS2UVMYESYM5Z2M4005XORPSH3LQZUD0KRVWDWJSAYIKZXT";

	private static string ClientSecret = "CXBPEOO1N5E2XFXLK3HAC4VJQXDC21FAKBKRPFICNARKWFQO";

	public static void GetInfo(string id, Action<Venue> callback)
	{
		WebRequest request = WebRequest.Create("https://api.foursquare.com/v2/venues/" + id + "?client_id=" + ClientId + "&client_secret=" + ClientSecret + "&v=20120725");
		request.BeginGetResponse(delegate(IAsyncResult iar)
		{
			try
			{
				WebResponse webResponse = request.EndGetResponse(iar);
				RootObject rootObject = new JsonSerializer().Deserialize<RootObject>(new JsonTextReader(new StreamReader(webResponse.GetResponseStream())));
				callback(rootObject.response.venue);
			}
			catch
			{
				callback(null);
			}
		}, null);
	}

	public static void GetPlaceAroundMe(string search, double lat, double lng, Action<List<Venue>, string> callback)
	{
		NumberFormatInfo numberFormatInfo = new NumberFormatInfo
		{
			NumberDecimalSeparator = "."
		};
		WebRequest request = WebRequest.Create(string.Format("https://api.foursquare.com/v2/venues/search?ll={2},{3}&client_id={0}&client_secret={1}&v=20120725{4}", ClientId, ClientSecret, lat.ToString(numberFormatInfo), lng.ToString(numberFormatInfo), (search != null) ? ("&query=" + HttpUtility.UrlEncode(search)) : ""));
		request.BeginGetResponse(delegate(IAsyncResult iar)
		{
			try
			{
				WebResponse webResponse = request.EndGetResponse(iar);
				SearchRootObject searchRootObject = new JsonSerializer().Deserialize<SearchRootObject>(new JsonTextReader(new StreamReader(webResponse.GetResponseStream())));
				callback(searchRootObject.response.venues, searchRootObject.meta.requestId);
			}
			catch
			{
				callback(null, null);
			}
		}, null);
	}
}
