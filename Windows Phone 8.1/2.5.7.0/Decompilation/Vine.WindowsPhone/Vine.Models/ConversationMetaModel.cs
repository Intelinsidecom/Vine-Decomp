using System.Collections.Generic;

namespace Vine.Models;

public class ConversationMetaModel
{
	public List<ConversationModel> Records { get; set; }

	public List<VineUserModel> Users { get; set; }
}
