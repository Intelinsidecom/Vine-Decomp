using Vine.Models;

namespace Vine.Views;

public class MenuTag
{
	public VineContactViewModel Target { get; set; }

	public VineUserType UserType { get; set; }

	public string Value { get; set; }
}
