using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class PostOrRepostDetails
{
	[JsonProperty("post_id")]
	public long PostId { get; set; }

	[JsonProperty("repost_id")]
	public long RepostId { get; set; }

	[JsonProperty("post_author_id")]
	public long PostAuthorId { get; set; }

	[JsonProperty("repost_author_id")]
	public long RepostAuthorId { get; set; }

	[JsonProperty("liked")]
	public bool IsLiked { get; set; }

	[JsonProperty("reposted")]
	public bool IsReposted { get; set; }
}
