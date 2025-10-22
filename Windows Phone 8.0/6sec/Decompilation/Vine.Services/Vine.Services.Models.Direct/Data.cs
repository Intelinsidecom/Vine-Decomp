using Newtonsoft.Json;

namespace Vine.Services.Models.Direct;

public class Data
{
	[JsonProperty("messages")]
	public DirectMessage[] Messages { get; set; }
}
