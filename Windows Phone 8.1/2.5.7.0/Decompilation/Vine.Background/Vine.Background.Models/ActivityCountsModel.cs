using System.Runtime.CompilerServices;

namespace Vine.Background.Models;

internal class ActivityCountsModel
{
	public extern int Notifications
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern int Messages
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern ActivityCountsModel();
}
