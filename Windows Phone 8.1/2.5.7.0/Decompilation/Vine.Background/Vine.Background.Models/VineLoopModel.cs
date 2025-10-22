using System.Runtime.CompilerServices;

namespace Vine.Background.Models;

internal class VineLoopModel
{
	public extern long Count
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern long Velocity
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern bool OnFire
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern VineLoopModel();
}
