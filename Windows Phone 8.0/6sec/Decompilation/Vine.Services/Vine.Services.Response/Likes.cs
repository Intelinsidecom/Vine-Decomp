using System.Collections.Generic;
using Vine.Services.Models;

namespace Vine.Services.Response;

public class Likes
{
	public int count { get; set; }

	public List<VineRecordPerson> records { get; set; }

	public int? nextPage { get; set; }

	public object previousPage { get; set; }

	public int size { get; set; }
}
