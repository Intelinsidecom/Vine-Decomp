using System.Collections.Generic;
using System.Runtime.Serialization;

namespace Vine.Models;

public class VineMessageMetaModel : BaseConversationModel
{
	public List<VineMessageModel> Records { get; set; }

	public List<VineUserModel> Users { get; set; }

	[IgnoreDataMember]
	public override List<VineMessageModel> Msgs
	{
		get
		{
			return Records;
		}
		set
		{
			Records = value;
		}
	}
}
