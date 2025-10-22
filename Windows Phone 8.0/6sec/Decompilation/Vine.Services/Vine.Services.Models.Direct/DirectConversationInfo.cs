using Newtonsoft.Json;
using Vine.Services.Response;

namespace Vine.Services.Models.Direct;

public class DirectConversationInfo : VineJsonResponse
{
	[JsonProperty("data")]
	public DirectConversationData Data { get; set; }
}
