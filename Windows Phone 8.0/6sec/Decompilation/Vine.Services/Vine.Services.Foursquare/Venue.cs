namespace Vine.Services.Foursquare;

public class Venue
{
	private string title;

	private string subtitle;

	public string id { get; set; }

	public string name { get; set; }

	public Location location { get; set; }

	public string canonicalUrl { get; set; }

	public bool verified { get; set; }

	public bool restricted { get; set; }

	public bool dislike { get; set; }

	public double rating { get; set; }

	public int createdAt { get; set; }

	public string shortUrl { get; set; }

	public string timeZone { get; set; }

	public bool IsCustom { get; set; }

	public string Title
	{
		get
		{
			return title ?? name;
		}
		set
		{
			title = value;
		}
	}

	public string Subtitle
	{
		get
		{
			string text = subtitle;
			if (text == null)
			{
				if (location == null)
				{
					return "";
				}
				text = ((!string.IsNullOrEmpty(location.address)) ? "" : (location.address + "\n")) + location.city;
			}
			return text;
		}
		set
		{
			subtitle = value;
		}
	}
}
