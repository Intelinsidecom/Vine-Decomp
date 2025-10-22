using Newtonsoft.Json;
using Vine.Services.Response;

namespace Vine.Services.Models;

public class DirectConversationsInfo : VineJsonResponse
{
	[JsonProperty("data")]
	public DirectData Data { get; set; }
}
