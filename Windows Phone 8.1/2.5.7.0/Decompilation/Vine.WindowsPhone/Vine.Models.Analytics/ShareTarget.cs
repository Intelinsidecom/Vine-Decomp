using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace Vine.Models.Analytics;

[JsonConverter(typeof(StringEnumConverter))]
public enum ShareTarget
{
	VINE = 1,
	REVINE,
	TWITTER,
	FACEBOOK,
	TUMBLR,
	VM
}
