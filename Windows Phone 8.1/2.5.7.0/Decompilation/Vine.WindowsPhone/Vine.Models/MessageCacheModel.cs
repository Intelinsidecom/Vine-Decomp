using System.Collections.Generic;

namespace Vine.Models;

public class MessageCacheModel
{
	public List<VineMessageViewModel> Messages { get; set; }

	public List<string> DeletedIds { get; set; }

	public MessageCacheModel()
	{
		Messages = new List<VineMessageViewModel>();
		DeletedIds = new List<string>();
	}
}
