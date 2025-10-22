using System;
using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class AppState
{
	[JsonProperty("ab_connected")]
	public bool ContactsEnabled { get; set; }

	[JsonProperty("fb_connected")]
	public bool FacebookConnected { get; set; }

	[JsonProperty("tw_connected")]
	public bool TwitterConnected { get; set; }

	[JsonProperty("logged_in_user_id")]
	public long UserId { get; set; }

	[JsonProperty("video_cache_size")]
	public long VideoCacheSize { get; set; }

	public static AppState GetDefault()
	{
		try
		{
			return new AppState
			{
				ContactsEnabled = ApplicationSettings.Current.IsAddressBookEnabled,
				FacebookConnected = ApplicationSettings.Current.IsFacebookEnabled,
				TwitterConnected = ApplicationSettings.Current.IsTwitterEnabled,
				UserId = ((ApplicationSettings.Current.UserId == null) ? 0 : long.Parse(ApplicationSettings.Current.UserId)),
				VideoCacheSize = ApplicationSettings.Current.VineCacheSize
			};
		}
		catch (Exception)
		{
			return new AppState();
		}
	}
}
