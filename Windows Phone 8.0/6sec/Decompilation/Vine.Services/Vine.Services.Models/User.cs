using Gen.Services;
using Newtonsoft.Json;

namespace Vine.Services.Models;

public class User : IPerson
{
	[JsonProperty("externalUser")]
	public string ExternalUser { get; set; }

	[JsonProperty("phoneNumber")]
	public string PhoneNumber { get; set; }

	[JsonProperty("userId")]
	public string Id { get; set; }

	[JsonProperty("username")]
	public string Name { get; set; }

	[JsonProperty("verified")]
	public string Verified { get; set; }

	[JsonProperty("avatarUrl")]
	public string Picture { get; set; }

	[JsonProperty("user")]
	public UserInfo UserInfo { get; set; }

	[JsonProperty("location")]
	public string Location { get; set; }

	public bool IsVerified { get; set; }

	public string SubName => Location;

	public bool? Following { get; set; }

	public void ChangeFollow(bool val)
	{
		Following = val;
	}
}
