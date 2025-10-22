using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class ActivityDetails
{
	[JsonProperty("activity_id")]
	public long ActivityId { get; set; }

	[JsonProperty("activity_type")]
	public string ActivityType { get; set; }

	[JsonProperty("n_more")]
	public int NMore { get; set; }
}
