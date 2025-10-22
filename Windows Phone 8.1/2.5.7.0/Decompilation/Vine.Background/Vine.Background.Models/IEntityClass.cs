using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using System.Runtime.Serialization;
using Windows.Foundation.Metadata;

namespace Vine.Background.Models;

[CompilerGenerated]
[Guid(2945685141u, 24072, 21437, 102, 40, 14, 145, 57, 253, 67, 184)]
[Version(21889117u)]
[ExclusiveTo(typeof(Entity))]
internal interface IEntityClass
{
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

	[IgnoreDataMember]
	EntityType EntityType
	{
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		get;
	}

	string Id
	{
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		get;
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		[param: In]
		set;
	}

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

	string Link
	{
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		get;
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		[param: In]
		set;
	}

	int[] Range
	{
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		get;
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		[param: In]
		set;
	}

	string Text
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
