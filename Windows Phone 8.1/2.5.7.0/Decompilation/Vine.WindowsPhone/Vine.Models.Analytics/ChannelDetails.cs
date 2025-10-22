using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class ChannelDetails
{
	[JsonProperty("channel_id")]
	public string ChannelId { get; set; }
}
