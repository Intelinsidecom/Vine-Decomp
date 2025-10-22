using System.Collections.Generic;
using Vine.Services.Models;

namespace Vine.Services.Response;

public class TagsData
{
	public long count { get; set; }

	public List<VineTag> records { get; set; }

	public string nextPage { get; set; }

	public string anchor { get; set; }

	public string previousPage { get; set; }

	public long size { get; set; }
}
