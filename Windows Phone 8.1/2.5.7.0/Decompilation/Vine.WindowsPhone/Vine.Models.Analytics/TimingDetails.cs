using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class TimingDetails
{
	[JsonProperty("start_timestamp")]
	public double StartTimestamp { get; set; }

	[JsonProperty("duration")]
	public double Duration { get; set; }
}
