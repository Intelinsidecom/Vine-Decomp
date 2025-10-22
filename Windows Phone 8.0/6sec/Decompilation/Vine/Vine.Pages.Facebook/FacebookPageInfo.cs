using System.Collections.Generic;
using Newtonsoft.Json;

namespace Vine.Pages.Facebook;

public class FacebookPageInfo
{
	public string category { get; set; }

	[JsonProperty(PropertyName = "name")]
	public string Name { get; set; }

	public string access_token { get; set; }

	public List<string> perms { get; set; }

	public string id { get; set; }
}
