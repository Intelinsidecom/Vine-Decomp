using System.Collections.Generic;

namespace Vine.Services.Response;

public class CommentsData
{
	public int count { get; set; }

	public List<VineComment> records { get; set; }

	public int? nextPage { get; set; }

	public long anchor { get; set; }

	public int? previousPage { get; set; }

	public int size { get; set; }
}
