using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class HttpPerformanceData
{
	[JsonProperty("start_timestamp")]
	public double StartTimestamp { get; set; }

	[JsonProperty("duration")]
	public double Duration { get; set; }

	[JsonProperty("duration_to_request_sent")]
	public double? DurationToRequestSent { get; set; }

	[JsonProperty("duration_to_first_byte")]
	public double? DurationToFirstByte { get; set; }

	[JsonProperty("bytes_sent")]
	public long BytesSent { get; set; }

	[JsonProperty("bytes_rcvd")]
	public long BytesReceived { get; set; }
}
