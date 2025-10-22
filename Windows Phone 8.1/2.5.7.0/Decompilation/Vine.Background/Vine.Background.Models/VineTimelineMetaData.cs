using System.Collections.Generic;
using System.Runtime.CompilerServices;

namespace Vine.Background.Models;

internal class VineTimelineMetaData
{
	public extern List<VineModel> Records
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern VineTimelineMetaData();
}
