using Gen.Services;

namespace Vine.Models;

public class SimpleUser : IPerson
{
	public string Name { get; set; }

	public string Id { get; set; }

	public string SubName => Location;

	public bool IsVerified { get; set; }

	public string Picture { get; set; }

	public bool? Following { get; set; }

	public string Location { get; set; }

	public bool AskFollowing { get; set; }

	public void ChangeFollow(bool val)
	{
	}
}
