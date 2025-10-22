using Newtonsoft.Json;

namespace Vine.Services.Response;

public class Repost
{
	[JsonProperty(PropertyName = "username")]
	public string Username { get; set; }

	[JsonProperty(PropertyName = "userid")]
	public string UserId { get; set; }
}
