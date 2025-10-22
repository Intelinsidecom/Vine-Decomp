using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using Windows.Foundation.Metadata;

namespace Vine.Background;

[CompilerGenerated]
[Guid(3105925029u, 54772, 24122, 88, 145, 209, 113, 84, 60, 244, 20)]
[Version(21889117u)]
[ExclusiveTo(typeof(BgNotifications))]
internal interface IBgNotificationsStatic
{
	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	void UpdateMessagesAndNotifications([In] int messages, [In] int notifications);
}
