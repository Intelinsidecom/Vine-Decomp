using System.Collections.Generic;

namespace Vine.Models;

public class VineUnifiedSearchResult<T>
{
	public int Count { get; set; }

	public int? NextPage { get; set; }

	public string Anchor { get; set; }

	public int? PreviousPage { get; set; }

	public int Size { get; set; }

	public IEnumerable<T> Records { get; set; }
}
