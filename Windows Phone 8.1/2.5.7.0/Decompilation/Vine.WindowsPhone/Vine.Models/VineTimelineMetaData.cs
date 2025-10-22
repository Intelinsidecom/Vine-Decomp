using System.Collections.Generic;

namespace Vine.Models;

public class VineTimelineMetaData
{
	public List<VineModel> Records { get; set; }

	public string Anchor { get; set; }
}
