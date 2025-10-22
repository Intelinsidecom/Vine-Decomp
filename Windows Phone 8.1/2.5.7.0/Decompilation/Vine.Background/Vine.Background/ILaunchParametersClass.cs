using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using System.Runtime.Serialization;
using Windows.Foundation.Metadata;

namespace Vine.Background;

[CompilerGenerated]
[Guid(2029131673u, 21403, 21590, 109, 149, 77, 242, 8, 115, 61, 113)]
[Version(21889117u)]
[ExclusiveTo(typeof(LaunchParameters))]
internal interface ILaunchParametersClass
{
	[DataMember]
	string Type
	{
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		get;
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		[param: In]
		set;
	}

	[DataMember]
	string Data
	{
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		get;
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		[param: In]
		set;
	}

	[DataMember]
	string Title
	{
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		get;
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		[param: In]
		set;
	}

	[DataMember]
	string Msg
	{
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		get;
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		[param: In]
		set;
	}

	[DataMember]
	string ConversationId
	{
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		get;
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		[param: In]
		set;
	}
}
