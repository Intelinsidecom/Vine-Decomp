using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class VMRecipient
{
	[JsonProperty("user")]
	public UserDetails UserDetails { get; set; }

	[JsonProperty("is_phone")]
	public bool IsPhone { get; set; }

	[JsonProperty("is_email")]
	public bool IsEmail { get; set; }
}
