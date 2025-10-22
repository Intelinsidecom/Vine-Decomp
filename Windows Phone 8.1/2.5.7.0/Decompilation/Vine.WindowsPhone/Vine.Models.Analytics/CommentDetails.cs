using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class CommentDetails
{
	[JsonProperty("comment_id")]
	public long CommentId { get; set; }

	[JsonProperty("author_id")]
	public long AuthorId { get; set; }
}
