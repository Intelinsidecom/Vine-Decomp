using Newtonsoft.Json;

namespace Vine.Services.Response;

public class NotificationCounter
{
	[JsonProperty("notifications")]
	public int Notifications { get; set; }

	[JsonProperty("messages")]
	public int Messages { get; set; }
}
