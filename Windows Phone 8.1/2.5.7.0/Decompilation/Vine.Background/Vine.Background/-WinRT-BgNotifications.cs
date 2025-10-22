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
[Static(typeof(IBgNotificationsStatic), 21889117u)]
internal sealed class _003CWinRT_003EBgNotifications : IBgNotificationsClass, IStringable
{
	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	public static extern void UpdateMessagesAndNotifications([In] int messages, [In] int notifications);

	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	extern string IStringable.ToString();
}
