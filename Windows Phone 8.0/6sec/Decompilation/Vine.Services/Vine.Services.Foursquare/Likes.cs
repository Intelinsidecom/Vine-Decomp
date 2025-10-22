using System.Collections.Generic;

namespace Vine.Services.Foursquare;

public class Likes
{
	public int count { get; set; }

	public List<Group> groups { get; set; }

	public string summary { get; set; }
}
