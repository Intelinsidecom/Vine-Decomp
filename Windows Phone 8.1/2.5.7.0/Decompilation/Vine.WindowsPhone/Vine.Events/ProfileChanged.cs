using Vine.Models;

namespace Vine.Events;

public class ProfileChanged
{
	public VineUserModel Profile { get; set; }
}
