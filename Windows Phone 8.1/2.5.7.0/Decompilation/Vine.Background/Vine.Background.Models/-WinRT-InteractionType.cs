using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using Windows.Foundation.Metadata;

namespace Vine.Background.Models;

[ComImport]
[Version(21889117u)]
[CompilerGenerated]
internal enum _003CWinRT_003EInteractionType
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
