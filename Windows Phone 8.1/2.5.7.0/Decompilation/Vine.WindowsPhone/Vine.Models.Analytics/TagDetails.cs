using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class TagDetails
{
	[JsonProperty("tag_id")]
	public string TagId { get; set; }
}
