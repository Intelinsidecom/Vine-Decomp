using System.Collections.Generic;
using System.Runtime.CompilerServices;

namespace Vine.Background.Models;

internal class ConversationMetaModel
{
	public extern List<ConversationModel> Records
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

	public extern ConversationMetaModel();
}
