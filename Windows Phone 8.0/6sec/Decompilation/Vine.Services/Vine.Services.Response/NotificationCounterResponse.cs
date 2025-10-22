using Newtonsoft.Json;

namespace Vine.Services.Response;

public class NotificationCounterResponse : VineJsonResponse
{
	[JsonProperty("data")]
	public NotificationCounter Data { get; set; }
}
