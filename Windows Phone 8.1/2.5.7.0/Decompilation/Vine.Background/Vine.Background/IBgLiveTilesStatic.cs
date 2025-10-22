using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using Windows.Foundation.Metadata;

namespace Vine.Background;

[CompilerGenerated]
[Guid(83810324u, 53792, 21633, 90, 236, 80, 147, 107, 243, 147, 43)]
[Version(21889117u)]
[ExclusiveTo(typeof(BgLiveTiles))]
internal interface IBgLiveTilesStatic
{
	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	void UpdateMainTileCount([In] int count);

	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	void UpdateOnTheRiseTile([In] string tileId);

	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	void UpdatePopularNowTile([In] string tileId);

	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	void UpdateProfileTile([In] string profileId, [In] string tileId);

	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	void UpdateSpecificTagTile([In] string tag, [In] string tileId);

	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	[AsyncStateMachine(typeof(BgLiveTiles._003CUpdateTiles_003Ed__1))]
	void UpdateTiles();
}
