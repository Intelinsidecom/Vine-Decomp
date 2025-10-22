using System.Collections.Generic;
using System.Diagnostics;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using System.Threading.Tasks;
using Vine.Background.Models;
using Vine.Background.Web;
using Windows.Foundation.Metadata;
using Windows.UI.Notifications;
using Windows.UI.StartScreen;

namespace Vine.Background;

[MarshalingBehavior(/*Could not decode attribute arguments.*/)]
[Threading(/*Could not decode attribute arguments.*/)]
[Version(21889117u)]
[Static(typeof(IBgLiveTilesStatic), 21889117u)]
public static class BgLiveTiles : IBgLiveTilesClass
{
	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CUpdateTiles_003Ed__1 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncVoidMethodBuilder _003C_003Et__builder;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<ActivityCountsModel>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CUpdateTilesAsync_003Ed__6 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder _003C_003Et__builder;

		public int newNotifications;

		public int newMessages;

		private TaskAwaiter _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CUpdateProfileTiles_003Ed__7 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder _003C_003Et__builder;

		private TaskAwaiter<IReadOnlyList<SecondaryTile>> _003C_003Eu__1;

		private IEnumerator<SecondaryTile> _003C_003E7__wrap1;

		private TaskAwaiter _003C_003Eu__2;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CUpdateSpecificTagTiles_003Ed__8 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder _003C_003Et__builder;

		private TaskAwaiter<IReadOnlyList<SecondaryTile>> _003C_003Eu__1;

		private IEnumerator<SecondaryTile> _003C_003E7__wrap1;

		private TaskAwaiter _003C_003Eu__2;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CUpdateMainTile_003Ed__9 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder _003C_003Et__builder;

		public int newNotifications;

		public int newMessages;

		private TaskAwaiter<int> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CUpdateMainTileHomeTimeline_003Ed__10 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder<int> _003C_003Et__builder;

		public int tileCount;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CUpdateMainTileNotifications_003Ed__11 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder<int> _003C_003Et__builder;

		public int newNotifications;

		public int tileCount;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<InteractionMetaModel>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CUpdateMainTileMsgs_003Ed__12 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder<int> _003C_003Et__builder;

		public int newMessages;

		public int tileCount;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<ConversationMetaModel>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CUpdateProfileTileAsync_003Ed__14 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder _003C_003Et__builder;

		public string tileId;

		public string profileId;

		private WebDataProvider _003Cclient_003E5__1;

		private VineUserModel _003Cuser_003E5__2;

		private TileUpdater _003Cupdator_003E5__3;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<VineUserModel>>> _003C_003Eu__1;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> _003C_003Eu__2;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CUpdateSpecificTagTileAsync_003Ed__15 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder _003C_003Et__builder;

		public string tileId;

		public string tag;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CUpdatePopularNowTileAsync_003Ed__16 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder _003C_003Et__builder;

		public string tileId;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CUpdateOnTheRiseTileAsync_003Ed__17 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder _003C_003Et__builder;

		public string tileId;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	private static readonly string[] LangValues;

	public static extern void UpdateMainTileCount([In] int count);

	[AsyncStateMachine(typeof(_003CUpdateTiles_003Ed__1))]
	public static extern void UpdateTiles();

	public static extern void UpdateProfileTile([In] string profileId, [In] string tileId);

	public static extern void UpdateSpecificTagTile([In] string tag, [In] string tileId);

	public static extern void UpdatePopularNowTile([In] string tileId);

	public static extern void UpdateOnTheRiseTile([In] string tileId);

	[AsyncStateMachine(typeof(_003CUpdateTilesAsync_003Ed__6))]
	internal static extern Task UpdateTilesAsync(int newNotifications, int newMessages);

	[AsyncStateMachine(typeof(_003CUpdateProfileTiles_003Ed__7))]
	internal static extern Task UpdateProfileTiles();

	[AsyncStateMachine(typeof(_003CUpdateSpecificTagTiles_003Ed__8))]
	internal static extern Task UpdateSpecificTagTiles();

	[AsyncStateMachine(typeof(_003CUpdateMainTile_003Ed__9))]
	internal static extern Task UpdateMainTile(int newNotifications, int newMessages);

	[AsyncStateMachine(typeof(_003CUpdateMainTileHomeTimeline_003Ed__10))]
	private static extern Task<int> UpdateMainTileHomeTimeline(int tileCount);

	[AsyncStateMachine(typeof(_003CUpdateMainTileNotifications_003Ed__11))]
	private static extern Task<int> UpdateMainTileNotifications(int newNotifications, int tileCount);

	[AsyncStateMachine(typeof(_003CUpdateMainTileMsgs_003Ed__12))]
	private static extern Task<int> UpdateMainTileMsgs(int newMessages, int tileCount);

	private static extern void UpdateMainTileLogoFront(int count);

	[AsyncStateMachine(typeof(_003CUpdateProfileTileAsync_003Ed__14))]
	internal static extern Task UpdateProfileTileAsync(string profileId, string tileId);

	[AsyncStateMachine(typeof(_003CUpdateSpecificTagTileAsync_003Ed__15))]
	internal static extern Task UpdateSpecificTagTileAsync(string tag, string tileId);

	[AsyncStateMachine(typeof(_003CUpdatePopularNowTileAsync_003Ed__16))]
	internal static extern Task UpdatePopularNowTileAsync(string tileId);

	[AsyncStateMachine(typeof(_003CUpdateOnTheRiseTileAsync_003Ed__17))]
	internal static extern Task UpdateOnTheRiseTileAsync(string tileId);

	private static extern string GetNewMessagesString();
}
