using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class CommentItem : EventItem
{
	[JsonProperty("position")]
	public Position Position { get; set; }

	[JsonProperty("comment")]
	public CommentDetails Comment { get; set; }

	public CommentItem(long authorId, long commentId)
		: base(ItemType.COMMENT)
	{
		Position = new Position();
		Comment = new CommentDetails
		{
			AuthorId = authorId,
			CommentId = commentId
		};
	}
}
