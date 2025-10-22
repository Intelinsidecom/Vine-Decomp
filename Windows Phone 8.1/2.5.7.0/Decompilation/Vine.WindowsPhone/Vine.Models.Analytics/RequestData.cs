using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class RequestData
{
	[JsonProperty("ip")]
	public string IP { get; set; }

	[JsonProperty("timestamp")]
	public double TimeStamp { get; set; }

	[JsonProperty("request_id")]
	public string RequestId { get; set; }

	[JsonProperty("client_id")]
	public string ClientId { get; set; }

	[JsonProperty("user_agent")]
	public string UserAgent { get; set; }

	[JsonProperty("server")]
	public string Server { get; set; }

	[JsonProperty("session_id")]
	public string SessionId { get; set; }
}
