using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class HttpRequestDetails
{
	[JsonProperty("method")]
	public string Method { get; set; }

	[JsonProperty("url")]
	public string Url { get; set; }

	[JsonProperty("http_status")]
	public long HttpStatus { get; set; }

	[JsonProperty("network_error")]
	public NetworkErrorCode? NetworkError { get; set; }

	[JsonProperty("os_error_details")]
	public string OsErrorDetails { get; set; }

	[JsonProperty("api_error")]
	public int ApiError { get; set; }
}
