using System.Collections.Generic;
using System.Runtime.Serialization;
using Vine.Framework;

namespace Vine.Models;

public class ConversationModel : BaseConversationModel
{
	public List<VineMessageModel> Messages { get; set; }

	public List<string> Users { get; set; }

	public string CreatedDisplay => base.Created.ToVineTime();

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
