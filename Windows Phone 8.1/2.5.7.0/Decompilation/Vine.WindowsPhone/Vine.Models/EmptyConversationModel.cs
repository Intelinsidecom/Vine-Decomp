using System;
using System.Collections.Generic;

namespace Vine.Models;

public class EmptyConversationModel : BaseConversationModel
{
	public override List<VineMessageModel> Msgs
	{
		get
		{
			return new List<VineMessageModel>();
		}
		set
		{
			throw new NotImplementedException();
		}
	}
}
