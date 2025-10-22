using System;
using System.Text;

namespace Vine.Views;

public static class FacebookConstants
{
	public const sbyte API_MAGIC = 21;

	public static readonly sbyte[] RAW_ACCESS_KEY = new sbyte[16]
	{
		-28, -28, -35, -32, -31, -34, -32, -36, -29, -28,
		-32, -36, -29, -28, -34, -27
	};

	public static readonly string API_KEY = Transform(21, RAW_ACCESS_KEY);

	public const string RedirectUrl = "https://www.facebook.com/connect/login_success.html";

	public const string Permissions = "publish_actions";

	public static Uri AuthUri => new Uri(string.Format("https://m.facebook.com/dialog/oauth?client_id={0}&redirect_uri={1}&scope={2}&response_type=token", new object[3] { API_KEY, "https://www.facebook.com/connect/login_success.html", "publish_actions" }));

	public static string Transform(sbyte magic, sbyte[] m)
	{
		StringBuilder stringBuilder = new StringBuilder(m.Length);
		foreach (sbyte b in m)
		{
			stringBuilder.Append((char)(magic - b));
		}
		return stringBuilder.ToString();
	}
}
