using System.Collections.Generic;

namespace Vine.Services.Foursquare;

public class Hours
{
	public string status { get; set; }

	public bool isOpen { get; set; }

	public List<Timeframe> timeframes { get; set; }
}
