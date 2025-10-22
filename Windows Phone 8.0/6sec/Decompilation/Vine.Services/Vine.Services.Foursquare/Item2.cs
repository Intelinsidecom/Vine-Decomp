namespace Vine.Services.Foursquare;

public class Item2
{
	public string id { get; set; }

	public int createdAt { get; set; }

	public string text { get; set; }

	public string canonicalUrl { get; set; }

	public Likes2 likes { get; set; }

	public Todo todo { get; set; }

	public User2 user { get; set; }
}
