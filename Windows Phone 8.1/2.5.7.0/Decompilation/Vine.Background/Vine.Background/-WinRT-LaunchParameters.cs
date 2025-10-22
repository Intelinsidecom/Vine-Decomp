using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using System.Runtime.Serialization;
using Windows.Foundation;
using Windows.Foundation.Metadata;

namespace Vine.Background;

[ComImport]
[DataContract]
[MarshalingBehavior(/*Could not decode attribute arguments.*/)]
[Threading(/*Could not decode attribute arguments.*/)]
[Version(21889117u)]
[CompilerGenerated]
[Activatable(21889117u)]
internal sealed class _003CWinRT_003ELaunchParameters : ILaunchParametersClass, IStringable
{
	[DataMember]
	public extern string Type
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
	public extern string Data
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
	public extern string Title
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
	public extern string Msg
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
	public extern string ConversationId
	{
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		get;
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		[param: In]
		set;
	}

	[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
	extern string IStringable.ToString();
}
