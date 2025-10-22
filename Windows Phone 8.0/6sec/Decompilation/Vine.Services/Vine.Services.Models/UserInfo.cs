using Newtonsoft.Json;

namespace Vine.Services.Models;

public class UserInfo
{
	[JsonProperty("private")]
	public int Private { get; set; }
}
