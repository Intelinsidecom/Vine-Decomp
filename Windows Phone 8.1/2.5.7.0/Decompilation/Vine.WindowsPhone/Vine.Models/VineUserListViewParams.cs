namespace Vine.Models;

public class VineUserListViewParams
{
	public UserListType Type { get; set; }

	public string PostId { get; set; }

	public string UserId { get; set; }

	public string ActivityId { get; set; }

	public string SearchText { get; set; }
}
