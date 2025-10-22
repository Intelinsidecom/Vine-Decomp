using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class Position
{
	[JsonProperty("offset")]
	public int Offset { get; set; }
}
