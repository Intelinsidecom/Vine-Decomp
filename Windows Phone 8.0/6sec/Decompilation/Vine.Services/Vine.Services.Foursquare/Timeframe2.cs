using System.Collections.Generic;

namespace Vine.Services.Foursquare;

public class Timeframe2
{
	public string days { get; set; }

	public bool includesToday { get; set; }

	public List<Open2> open { get; set; }

	public List<object> segments { get; set; }
}
