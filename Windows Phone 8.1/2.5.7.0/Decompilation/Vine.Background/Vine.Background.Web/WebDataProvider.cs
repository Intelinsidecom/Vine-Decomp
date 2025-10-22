using System.Diagnostics;
using System.Net.Http;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using System.Threading;
using System.Threading.Tasks;
using Vine.Background.Models;

namespace Vine.Background.Web;

internal class WebDataProvider
{
	private enum WebPlatform
	{
		Vine
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CGet_003Ed__2<T> : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder<ApiResult<T>> _003C_003Et__builder;

		public WebDataProvider _003C_003E4__this;

		public WebPlatform platform;

		public ParameterCollection parameters;

		public string path;

		public CancellationToken? cancellation;

		private HttpClient _003ChttpClient_003E5__1;

		private ApiResult<T> _003Cresult_003E5__2;

		private ConfiguredTaskAwaitable<HttpResponseMessage>.ConfiguredTaskAwaiter _003C_003Eu__1;

		private ConfiguredTaskAwaitable<string>.ConfiguredTaskAwaiter _003C_003Eu__2;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CGetTimeline_003Ed__15 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> _003C_003Et__builder;

		public int page;

		public int count;

		public WebDataProvider _003C_003E4__this;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CGetTimelineUser_003Ed__16 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> _003C_003Et__builder;

		public string userId;

		public int page;

		public int count;

		public WebDataProvider _003C_003E4__this;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CGetTimelineByTag_003Ed__17 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> _003C_003Et__builder;

		public string tag;

		public int page;

		public int count;

		public WebDataProvider _003C_003E4__this;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CGetUser_003Ed__18 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder<ApiResult<BaseVineResponseModel<VineUserModel>>> _003C_003Et__builder;

		public string userId;

		public WebDataProvider _003C_003E4__this;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<VineUserModel>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CGetPopularNow_003Ed__19 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> _003C_003Et__builder;

		public int page;

		public int count;

		public WebDataProvider _003C_003E4__this;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CGetOnTheRise_003Ed__20 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> _003C_003Et__builder;

		public int page;

		public int count;

		public WebDataProvider _003C_003E4__this;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CGetNotifications_003Ed__21 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder<ApiResult<BaseVineResponseModel<InteractionMetaModel>>> _003C_003Et__builder;

		public string userId;

		public int page;

		public WebDataProvider _003C_003E4__this;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<InteractionMetaModel>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CGetActivityCounts_003Ed__22 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder<ApiResult<BaseVineResponseModel<ActivityCountsModel>>> _003C_003Et__builder;

		public string userId;

		public WebDataProvider _003C_003E4__this;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<ActivityCountsModel>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CGetVineMessageConversations_003Ed__23 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder<ApiResult<BaseVineResponseModel<ConversationMetaModel>>> _003C_003Et__builder;

		public int page;

		public int count;

		public WebDataProvider _003C_003E4__this;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<ConversationMetaModel>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	private static readonly string[] LangValues;

	private static string _acceptLang;

	public extern string ConsumerKey
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string ConsumerSecret
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern WebDataProvider();

	[AsyncStateMachine(typeof(_003CGet_003Ed__2<>))]
	private extern Task<ApiResult<T>> Get<T>(string path, ParameterCollection parameters, WebPlatform platform, CancellationToken? cancellation = null);

	private extern string AcceptLangValue();

	private extern void SetHeaders(HttpClient httpClient, WebPlatform platform);

	[AsyncStateMachine(typeof(_003CGetTimeline_003Ed__15))]
	public extern Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetTimeline(int page, int count);

	[AsyncStateMachine(typeof(_003CGetTimelineUser_003Ed__16))]
	public extern Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetTimelineUser(string userId, int page, int count);

	[AsyncStateMachine(typeof(_003CGetTimelineByTag_003Ed__17))]
	public extern Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetTimelineByTag(string tag, int page, int count);

	[AsyncStateMachine(typeof(_003CGetUser_003Ed__18))]
	public extern Task<ApiResult<BaseVineResponseModel<VineUserModel>>> GetUser(string userId);

	[AsyncStateMachine(typeof(_003CGetPopularNow_003Ed__19))]
	public extern Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetPopularNow(int page, int count);

	[AsyncStateMachine(typeof(_003CGetOnTheRise_003Ed__20))]
	public extern Task<ApiResult<BaseVineResponseModel<VineTimelineMetaData>>> GetOnTheRise(int page, int count);

	[AsyncStateMachine(typeof(_003CGetNotifications_003Ed__21))]
	public extern Task<ApiResult<BaseVineResponseModel<InteractionMetaModel>>> GetNotifications(string userId, int page, int count);

	[AsyncStateMachine(typeof(_003CGetActivityCounts_003Ed__22))]
	public extern Task<ApiResult<BaseVineResponseModel<ActivityCountsModel>>> GetActivityCounts(string userId);

	[AsyncStateMachine(typeof(_003CGetVineMessageConversations_003Ed__23))]
	public extern Task<ApiResult<BaseVineResponseModel<ConversationMetaModel>>> GetVineMessageConversations(int page, int count);
}
