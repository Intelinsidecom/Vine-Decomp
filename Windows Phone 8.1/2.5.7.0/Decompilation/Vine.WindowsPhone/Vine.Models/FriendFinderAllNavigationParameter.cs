using System.Collections.Generic;

namespace Vine.Models;

internal class FriendFinderAllNavigationParameter
{
	public FriendFinderListSource Source { get; set; }

	public List<VineUserModel> VineUsers { get; set; }

	public bool IsNux { get; set; }
}
