using Vine.Models.Analytics;

namespace Vine.Views;

public class CommentNavigationObject
{
	public bool IsFocused { get; set; }

	public string PostId { get; set; }

	public Section Section { get; set; }
}
