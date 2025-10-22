using System.Diagnostics;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using System.Threading.Tasks;
using Vine.Background.Models;
using Vine.Background.Web;
using Windows.Foundation.Metadata;

namespace Vine.Background;

[MarshalingBehavior(/*Could not decode attribute arguments.*/)]
[Threading(/*Could not decode attribute arguments.*/)]
[Version(21889117u)]
[Static(typeof(IBgNotificationsStatic), 21889117u)]
public static class BgNotifications : IBgNotificationsClass
{
	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CUpdateMessagesAndNotificationsAsync_003Ed__1 : IAsyncStateMachine
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
	private struct _003CGetNotificationsAsync_003Ed__2 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder _003C_003Et__builder;

		public int count;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<InteractionMetaModel>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CGetMessagesAsync_003Ed__3 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder _003C_003Et__builder;

		public int counts;

		private string _003ClatestId_003E5__1;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<ConversationMetaModel>>> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	private static readonly string[] LangValues;

	public static extern void UpdateMessagesAndNotifications([In] int messages, [In] int notifications);

	[AsyncStateMachine(typeof(_003CUpdateMessagesAndNotificationsAsync_003Ed__1))]
	internal static extern Task UpdateMessagesAndNotificationsAsync(int newNotifications, int newMessages);

	[AsyncStateMachine(typeof(_003CGetNotificationsAsync_003Ed__2))]
	internal static extern Task GetNotificationsAsync(int count);

	[AsyncStateMachine(typeof(_003CGetMessagesAsync_003Ed__3))]
	internal static extern Task GetMessagesAsync(int counts);

	private static extern string GetMessageSentString();

	private static extern string GetCountsString(bool messages);
}
