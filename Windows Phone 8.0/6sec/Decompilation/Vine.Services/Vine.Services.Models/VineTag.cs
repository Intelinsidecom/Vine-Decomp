using Gen.Services;
using Newtonsoft.Json;

namespace Vine.Services.Models;

public class VineTag : ITag
{
	[JsonProperty(PropertyName = "tagId")]
	public string Id { get; set; }

	[JsonProperty(PropertyName = "tag")]
	public string Tag { get; set; }

	[JsonProperty(PropertyName = "postCount")]
	public long Count { get; set; }
}
