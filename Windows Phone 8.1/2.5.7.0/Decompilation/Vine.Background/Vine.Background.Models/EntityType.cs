using Windows.Foundation.Metadata;

namespace Vine.Background.Models;

[Version(21889117u)]
public enum EntityType
{
	Unknown,
	mention,
	user,
	userList,
	tag,
	post,
	commentList
}
