using System.Collections.Generic;
using System.Runtime.Serialization;

namespace Vine.Models;

public class VineMessageVideoResponseModel : BaseConversationModel
{
	public List<VineMessageModel> Messages { get; set; }

	public List<VineUserModel> Users { get; set; }

	[IgnoreDataMember]
	public override List<VineMessageModel> Msgs
	{
		get
		{
			return Messages;
		}
		set
		{
			Messages = value;
		}
	}
}
