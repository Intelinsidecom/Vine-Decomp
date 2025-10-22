using Newtonsoft.Json;

namespace Vine.Services.Response;

public class User
{
	[JsonProperty(PropertyName = "username")]
	public string Name { get; set; }

	public int verified { get; set; }

	public string description { get; set; }

	[JsonProperty(PropertyName = "avatarUrl")]
	public string Picture { get; set; }

	[JsonProperty(PropertyName = "userId")]
	public string Id { get; set; }

	public string location { get; set; }
}
