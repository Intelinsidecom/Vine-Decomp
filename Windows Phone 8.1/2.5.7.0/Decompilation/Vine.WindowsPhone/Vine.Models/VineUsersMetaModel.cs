using System.Collections.Generic;

namespace Vine.Models;

public class VineUsersMetaModel
{
	public string Anchor { get; set; }

	public int? NextPage { get; set; }

	public List<VineUserModel> Records { get; set; }
}
