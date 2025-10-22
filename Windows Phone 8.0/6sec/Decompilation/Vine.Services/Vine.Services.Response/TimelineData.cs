using System.Collections.Generic;
using Vine.Services.Models;

namespace Vine.Services.Response;

public class TimelineData
{
	public int count { get; set; }

	public List<VinePostRecord> records { get; set; }

	public string nextPage { get; set; }

	public string anchor { get; set; }

	public TimelineChannel channel { get; set; }

	public int size { get; set; }
}
