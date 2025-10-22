using System.Collections.Generic;
using Vine.Services.Models;

namespace Vine.Services.Response;

public class PagedUserData
{
	public int count { get; set; }

	public List<VineRecordPerson> records { get; set; }

	public string nextPage { get; set; }

	public string anchor { get; set; }

	public string previousPage { get; set; }

	public int size { get; set; }
}
