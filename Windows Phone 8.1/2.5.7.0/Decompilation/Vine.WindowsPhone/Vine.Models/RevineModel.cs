using System;
using Newtonsoft.Json;

namespace Vine.Models;

public class RevineModel
{
	[JsonProperty("repostId")]
	public string RepostId { get; set; }

	[JsonProperty("postId")]
	public string PostId { get; set; }

	[JsonProperty("created")]
	public DateTime Created { get; set; }
}
