using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class LaunchDetails
{
	[JsonProperty("web_src")]
	public string WebSrc { get; set; }
}
