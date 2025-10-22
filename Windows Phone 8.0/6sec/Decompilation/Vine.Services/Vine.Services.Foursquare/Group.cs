using System.Collections.Generic;

namespace Vine.Services.Foursquare;

public class Group
{
	public string type { get; set; }

	public int count { get; set; }

	public List<Item> items { get; set; }
}
