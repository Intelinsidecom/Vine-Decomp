using System.Net;

namespace Vine.Services;

public class TileGenerator
{
	public static string GetDistantTile(string tag, string imageuri, string thumbtag = null, string thumb = null)
	{
		return "http://www2.huynapps.com/6Sec/service/tiles/tilegenerator.php?tag=" + HttpUtility.UrlEncode(tag) + "&uri=" + HttpUtility.UrlEncode(imageuri) + "&app=" + AppVersion.AppName + ((thumb != null) ? ("&thumb=" + HttpUtility.UrlEncode(thumb)) : "") + ((thumbtag != null) ? ("&thumbtag=" + HttpUtility.UrlEncode(thumbtag)) : "");
	}
}
