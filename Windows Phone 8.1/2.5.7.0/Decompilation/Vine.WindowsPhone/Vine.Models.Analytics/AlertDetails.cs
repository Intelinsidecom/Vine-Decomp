using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class AlertDetails
{
	[JsonProperty("name")]
	public string Name { get; set; }

	[JsonProperty("action")]
	public AlertAction Action { get; set; }
}
