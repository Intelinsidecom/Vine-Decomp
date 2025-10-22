using System.Collections.Generic;
using Newtonsoft.Json;

namespace Vine.Models;

public class UploadRequestModel
{
	[JsonProperty("channelId")]
	public string ChannelId { get; set; }

	[JsonProperty("description")]
	public string Description { get; set; }

	[JsonProperty("thumbnailUrl")]
	public string ThumbnailUrl { get; set; }

	[JsonProperty("videoUrl")]
	public string VideoUrl { get; set; }

	[JsonProperty("postToTwitter")]
	public bool PostToTwitter { get; set; }

	[JsonProperty("postToFacebook")]
	public bool PostToFacebook { get; set; }

	[JsonProperty("entities")]
	public List<Entity> Entities { get; set; }

	public UploadRequestModel()
	{
		Entities = new List<Entity>();
	}
}
