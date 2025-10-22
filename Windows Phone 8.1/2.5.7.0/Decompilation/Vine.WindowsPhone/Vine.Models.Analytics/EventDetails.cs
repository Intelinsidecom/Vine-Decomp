using System.Collections.Generic;
using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class EventDetails
{
	[JsonProperty("timestamp")]
	public double TimeStamp { get; set; }

	[JsonProperty("items")]
	public List<EventItem> Items { get; set; }

	[JsonProperty("http_performance_data")]
	public HttpPerformanceData HttpPerformanceData { get; set; }

	[JsonProperty("http_request_details")]
	public HttpRequestDetails HttpRequestDetails { get; set; }

	[JsonProperty("timing")]
	public TimingDetails TimingDetails { get; set; }

	[JsonProperty("share")]
	public ShareDetails ShareDetails { get; set; }

	[JsonProperty("launch")]
	public LaunchDetails LaunchDetails { get; set; }

	[JsonProperty("alert")]
	public AlertDetails AlertDetails { get; set; }
}
