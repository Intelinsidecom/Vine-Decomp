using System.Collections.Generic;

namespace Vine.Services.Response;

public class Comments
{
	public int count { get; set; }

	public List<VineComment> records { get; set; }

	public int? nextPage { get; set; }

	public object previousPage { get; set; }

	public int size { get; set; }
}
