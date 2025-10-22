using System.Collections.Generic;

namespace Vine.Services.Foursquare;

public class Popular
{
	public string status { get; set; }

	public bool isOpen { get; set; }

	public List<Timeframe2> timeframes { get; set; }
}
