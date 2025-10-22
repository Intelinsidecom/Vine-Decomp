namespace Vine.Models;

public class VineListViewParams
{
	public string Title { get; set; }

	public ListType Type { get; set; }

	public string Color { get; set; }

	public string ChannelId { get; set; }

	public string Icon { get; set; }

	public bool ChannelShowRecent { get; set; }

	public string PostId { get; set; }

	public string TimelinePath { get; set; }
}
