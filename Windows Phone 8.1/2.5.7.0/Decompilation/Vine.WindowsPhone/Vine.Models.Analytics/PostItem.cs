using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class PostItem : EventItem
{
	[JsonProperty("position")]
	public Position Position { get; set; }

	[JsonProperty("post_or_repost")]
	public PostOrRepostDetails PostOrRepostDetails { get; set; }

	public PostItem(long postId, long repostId, long postAuthorId, long repostAuthorId, bool liked, bool reposted)
		: base(ItemType.POST)
	{
		Position = new Position();
		PostOrRepostDetails = new PostOrRepostDetails
		{
			PostId = postId,
			RepostId = repostId,
			PostAuthorId = postAuthorId,
			RepostAuthorId = repostAuthorId,
			IsLiked = liked,
			IsReposted = reposted
		};
	}
}
