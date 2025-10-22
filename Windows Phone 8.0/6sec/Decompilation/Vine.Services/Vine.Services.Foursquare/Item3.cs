namespace Vine.Services.Foursquare;

public class Item3
{
	public string id { get; set; }

	public string name { get; set; }

	public string description { get; set; }

	public string type { get; set; }

	public User3 user { get; set; }

	public bool editable { get; set; }

	public bool @public { get; set; }

	public bool collaborative { get; set; }

	public string url { get; set; }

	public string canonicalUrl { get; set; }

	public int createdAt { get; set; }

	public int updatedAt { get; set; }

	public bool logView { get; set; }

	public Followers followers { get; set; }

	public ListItems listItems { get; set; }

	public Photo5 photo { get; set; }
}
