namespace Vine.Models;

public enum InteractionType
{
	Unknown = 0,
	FollowRequest = 13,
	Count = 14,
	Followed = 15,
	Liked = 16,
	GroupedComment = 17,
	Reposted = 18,
	TwitterFriendJoined = 19,
	AddressBookFriendJoined = 20,
	MentionedPost = 21,
	MentionedComment = 22,
	Mentioned = 23,
	FollowApproved = 24,
	MilestoneFollowers = 25,
	MilestoneUserLoops = 26,
	MilestonePostLoops = 27,
	MilestonePosts = 28,
	FriendPost = 29,
	Header = 30,
	RepostRepost = 31,
	RepostLike = 32,
	FirstPopNow = 33,
	FirstFriendFinder = 34,
	FirstExplore = 35,
	First = 36,
	CampaignChannel = 37,
	Recommendation = 38,
	FriendHiatusPost = 39
}
