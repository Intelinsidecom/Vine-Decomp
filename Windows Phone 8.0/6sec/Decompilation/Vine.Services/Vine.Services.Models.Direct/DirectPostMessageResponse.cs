using Newtonsoft.Json;
using Vine.Services.Response;

namespace Vine.Services.Models.Direct;

public class DirectPostMessageResponse : VineJsonResponse
{
	[JsonProperty("data")]
	public Data Data { get; set; }
}
