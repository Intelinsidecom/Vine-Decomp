namespace Vine.TwitterAuth;

public class TwitterProfileInfo
{
	public int id { get; set; }

	public string name { get; set; }

	public string screen_name { get; set; }

	public string location { get; set; }

	public string description { get; set; }

	public string profile_image_url { get; set; }

	public bool default_profile_image { get; set; }
}
