using System.Collections.Generic;

namespace Vine.Models;

public class VineMessageResponseMetaModel
{
	public List<VineMessageModel> Messages { get; set; }

	public List<VineUserModel> Users { get; set; }
}
