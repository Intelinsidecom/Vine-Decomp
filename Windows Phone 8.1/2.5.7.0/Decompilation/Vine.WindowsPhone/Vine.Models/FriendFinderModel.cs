namespace Vine.Models;

public class FriendFinderModel
{
	public FriendFinderListSource Source { get; set; }

	public string HeaderText { get; set; }

	public bool SeeAllVisible { get; set; }

	public VineUserModel VineUserModel { get; set; }

	public bool IsHeader => HeaderText != null;

	public bool IsUser => VineUserModel != null;
}
