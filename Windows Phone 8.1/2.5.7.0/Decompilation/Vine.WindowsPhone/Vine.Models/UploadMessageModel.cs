using System.Collections.Generic;
using Newtonsoft.Json;

namespace Vine.Models;

public class UploadMessageModel
{
	[JsonProperty("locale")]
	public string Locale { get; set; }

	[JsonProperty("to")]
	public List<Dictionary<string, object>> To { get; set; }

	[JsonProperty("message")]
	public string Message { get; set; }

	[JsonProperty("created")]
	public string Created { get; set; }

	[JsonProperty("videoUrl")]
	public string VideoUrl { get; set; }

	[JsonProperty("thumbnailUrl")]
	public string ThumbnailUrl { get; set; }

	[JsonProperty("postId")]
	public string PostId { get; set; }

	[JsonProperty("maxLoops")]
	public int? MaxLoops { get; set; }

	[JsonProperty("entities")]
	public List<Entity> Entities { get; set; }

	public UploadMessageModel()
	{
		Entities = new List<Entity>();
	}
}
