using Windows.Foundation.Metadata;

namespace Vine.Background.Models;

[Version(21889117u)]
public enum InteractionType
{
	Followed = 1,
	Liked = 2,
	Commented = 3,
	Revined = 4,
	Mentioned = 10,
	MentionedUnknown = 11,
	FollowRequest = 13,
	ApprovedFollowRequest = 14
}
