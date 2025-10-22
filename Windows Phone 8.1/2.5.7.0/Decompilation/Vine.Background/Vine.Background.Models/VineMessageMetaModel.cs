using System.Collections.Generic;
using System.Runtime.CompilerServices;

namespace Vine.Background.Models;

internal class VineMessageMetaModel
{
	public extern List<VineMessageModel> Records
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern List<VineUserModel> Users
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern VineMessageMetaModel();
}
