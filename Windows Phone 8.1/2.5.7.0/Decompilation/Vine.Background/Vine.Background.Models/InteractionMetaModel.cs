using System.Collections.Generic;
using System.Runtime.CompilerServices;

namespace Vine.Background.Models;

internal class InteractionMetaModel
{
	public extern List<InteractionModel> Records
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern InteractionMetaModel();
}
