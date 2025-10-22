using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using Windows.Foundation.Metadata;

namespace Vine.Background.Models;

[CompilerGenerated]
[Guid(2625452019u, 45785, 22425, 76, 164, 243, 14, 93, 173, 89, 239)]
[Version(21889117u)]
[ExclusiveTo(typeof(VineUserModel))]
internal interface IVineUserModelClass
{
	string UserId
	{
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		get;
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		[param: In]
		set;
	}

	string Username
	{
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		get;
		[MethodImpl(MethodImplOptions.InternalCall, MethodCodeType = MethodCodeType.Runtime)]
		[CompilerGenerated]
		[param: In]
		set;
	}

	string AvatarUrl
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
