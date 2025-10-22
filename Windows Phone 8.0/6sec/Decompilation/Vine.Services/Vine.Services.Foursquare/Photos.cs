using System.Collections.Generic;

namespace Vine.Services.Foursquare;

public class Photos
{
	public int count { get; set; }

	public List<Group2> groups { get; set; }

	public string summary { get; set; }
}
