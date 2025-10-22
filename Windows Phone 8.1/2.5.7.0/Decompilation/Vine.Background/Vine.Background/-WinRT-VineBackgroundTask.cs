using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using Windows.ApplicationModel.Background;
using Windows.Foundation;
using Windows.Foundation.Metadata;

namespace Vine.Background;

[ComImport]
[MarshalingBehavior(/*Could not decode attribute arguments.*/)]
[Threading(/*Could not decode attribute arguments.*/)]
[Version(21889117u)]
[CompilerGenerated]
[Activatable(21889117u)]
internal sealed class _003CWinRT_003EVineBackgroundTask : IBackgroundTask, IStringable
{
	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	[AsyncStateMachine(typeof(VineBackgroundTask._003CRun_003Ed__0))]
	public extern void Run([In] IBackgroundTaskInstance taskInstance);

	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	extern string IStringable.ToString();
}
