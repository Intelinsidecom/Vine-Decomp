using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using Windows.Foundation;
using Windows.Foundation.Metadata;

namespace Vine.Background;

[ComImport]
[MarshalingBehavior(/*Could not decode attribute arguments.*/)]
[Threading(/*Could not decode attribute arguments.*/)]
[Version(21889117u)]
[CompilerGenerated]
[Static(typeof(IBgLiveTilesStatic), 21889117u)]
internal sealed class _003CWinRT_003EBgLiveTiles : IBgLiveTilesClass, IStringable
{
	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	public static extern void UpdateMainTileCount([In] int count);

	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	public static extern void UpdateOnTheRiseTile([In] string tileId);

	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	public static extern void UpdatePopularNowTile([In] string tileId);

	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	public static extern void UpdateProfileTile([In] string profileId, [In] string tileId);

	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	public static extern void UpdateSpecificTagTile([In] string tag, [In] string tileId);

	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	[AsyncStateMachine(typeof(BgLiveTiles._003CUpdateTiles_003Ed__1))]
	public static extern void UpdateTiles();

	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	extern string IStringable.ToString();
}
