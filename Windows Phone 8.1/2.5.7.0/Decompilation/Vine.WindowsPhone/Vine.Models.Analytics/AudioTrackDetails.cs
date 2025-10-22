using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class AudioTrackDetails
{
	[JsonProperty("track_id")]
	public string TrackId { get; set; }

	[JsonProperty("itunes_id")]
	public string ItunesId { get; set; }

	[JsonProperty("artist")]
	public string Artist { get; set; }

	[JsonProperty("track")]
	public string Track { get; set; }
}
