using System.Collections.Generic;

namespace Vine.Pages.Facebook;

public class FacebookPageRootObject
{
	public List<FacebookPageInfo> data { get; set; }

	public Paging paging { get; set; }
}
