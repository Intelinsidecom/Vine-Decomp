using System.Runtime.Serialization;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace Vine.Models.Analytics;

[JsonConverter(typeof(StringEnumConverter))]
public enum Section
{
	[EnumMember(Value = "")]
	None,
	[EnumMember(Value = "home")]
	Home,
	[EnumMember(Value = "explore")]
	Explore,
	[EnumMember(Value = "activity")]
	Activity,
	[EnumMember(Value = "my_profile")]
	MyProfile,
	[EnumMember(Value = "vm")]
	VM,
	[EnumMember(Value = "capture")]
	Capture,
	[EnumMember(Value = "logged_out")]
	LoggedOut
}
