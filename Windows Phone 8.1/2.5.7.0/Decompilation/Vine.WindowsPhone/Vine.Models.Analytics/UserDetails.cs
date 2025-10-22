using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class UserDetails
{
	[JsonProperty("user_id")]
	public long UserId { get; set; }

	[JsonProperty("following")]
	public bool Following { get; set; }
}
