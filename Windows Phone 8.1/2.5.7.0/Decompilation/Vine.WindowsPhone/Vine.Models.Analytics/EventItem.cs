using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public abstract class EventItem
{
	[JsonProperty("item_type")]
	public ItemType ItemType { get; set; }

	protected EventItem(ItemType itemType)
	{
		ItemType = itemType;
	}
}
