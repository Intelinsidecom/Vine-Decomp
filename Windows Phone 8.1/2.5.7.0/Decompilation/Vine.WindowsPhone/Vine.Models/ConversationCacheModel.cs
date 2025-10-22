using System.Collections.Generic;

namespace Vine.Models;

public class ConversationCacheModel
{
	public List<ConversationViewModel> Conversations { get; set; }

	public ConversationCacheModel()
	{
		Conversations = new List<ConversationViewModel>();
	}
}
