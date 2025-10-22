using Newtonsoft.Json;

namespace Vine.Models;

public class LoopCountsModel
{
	[JsonProperty("postId")]
	public string PostId { get; set; }

	[JsonProperty("ts")]
	public long ts { get; set; }

	[JsonProperty("count")]
	public long count { get; set; }
}
