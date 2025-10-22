using Vine.Services.Models;

namespace Vine.Services.Response;

public class ProfileRootObject : VineJsonResponse
{
	public VineProfile data { get; set; }
}
