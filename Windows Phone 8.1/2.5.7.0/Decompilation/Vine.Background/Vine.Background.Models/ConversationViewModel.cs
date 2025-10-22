using System.Runtime.CompilerServices;

namespace Vine.Background.Models;

internal class ConversationViewModel
{
	public extern VineUserModel User
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern ConversationModel Record
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern ConversationViewModel(ConversationMetaModel metaModel, ConversationModel record);
}
