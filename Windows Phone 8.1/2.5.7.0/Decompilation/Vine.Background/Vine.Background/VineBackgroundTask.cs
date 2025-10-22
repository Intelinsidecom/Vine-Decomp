using System.Diagnostics;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using System.Threading.Tasks;
using Vine.Background.Models;
using Vine.Background.Web;
using Windows.ApplicationModel.Background;
using Windows.Foundation.Metadata;

namespace Vine.Background;

[MarshalingBehavior(/*Could not decode attribute arguments.*/)]
[Threading(/*Could not decode attribute arguments.*/)]
[Version(21889117u)]
[Activatable(21889117u)]
public sealed class VineBackgroundTask : IBackgroundTask
{
	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CRun_003Ed__0 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncVoidMethodBuilder _003C_003Et__builder;

		public IBackgroundTaskInstance taskInstance;

		public VineBackgroundTask _003C_003E4__this;

		private BackgroundTaskDeferral _003Cdeferral_003E5__1;

		private TaskAwaiter<Task> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CRunAsync_003Ed__1 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder _003C_003Et__builder;

		private TaskAwaiter<ApiResult<BaseVineResponseModel<ActivityCountsModel>>> _003C_003Eu__1;

		private TaskAwaiter _003C_003Eu__2;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[AsyncStateMachine(typeof(_003CRun_003Ed__0))]
	public extern void Run(IBackgroundTaskInstance taskInstance);

	[AsyncStateMachine(typeof(_003CRunAsync_003Ed__1))]
	private extern Task RunAsync(IBackgroundTaskInstance taskInstance);

	public extern VineBackgroundTask();
}
