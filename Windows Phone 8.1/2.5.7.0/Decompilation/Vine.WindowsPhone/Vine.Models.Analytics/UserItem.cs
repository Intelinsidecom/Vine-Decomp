using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class UserItem : EventItem
{
	[JsonProperty("user")]
	public UserDetails UserDetails { get; set; }

	public UserItem()
		: base(ItemType.USER)
	{
	}
}
