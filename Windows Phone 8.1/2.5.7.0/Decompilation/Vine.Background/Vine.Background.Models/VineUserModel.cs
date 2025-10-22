using System.Runtime.CompilerServices;
using Windows.Foundation.Metadata;

namespace Vine.Background.Models;

[MarshalingBehavior(/*Could not decode attribute arguments.*/)]
[Threading(/*Could not decode attribute arguments.*/)]
[Version(21889117u)]
[Activatable(21889117u)]
public sealed class VineUserModel : IVineUserModelClass
{
	public extern string UserId
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string Username
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string AvatarUrl
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern VineUserModel();
}
