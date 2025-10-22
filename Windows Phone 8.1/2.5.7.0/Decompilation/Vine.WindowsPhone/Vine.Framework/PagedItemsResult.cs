using System.Collections.Generic;
using Vine.Web;

namespace Vine.Framework;

public class PagedItemsResult<T>
{
	public ApiResult ApiResult { get; set; }

	public IList<T> ViewModels { get; set; }

	public string Anchor { get; set; }

	public bool IsCancelled { get; set; }
}
