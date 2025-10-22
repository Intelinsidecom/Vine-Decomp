using System.Collections.Generic;

namespace Vine.Services.Foursquare;

public class Timeframe
{
	public string days { get; set; }

	public bool includesToday { get; set; }

	public List<Open> open { get; set; }

	public List<object> segments { get; set; }
}
