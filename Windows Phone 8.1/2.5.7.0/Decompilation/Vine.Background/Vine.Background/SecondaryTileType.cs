using Windows.Foundation.Metadata;

namespace Vine.Background;

[Version(21889117u)]
public enum SecondaryTileType
{
	SearchTag,
	TagResult,
	SearchPerson,
	PersonResult,
	ChannelResult,
	CapturePage,
	OnTheRise,
	PopularNow
}
