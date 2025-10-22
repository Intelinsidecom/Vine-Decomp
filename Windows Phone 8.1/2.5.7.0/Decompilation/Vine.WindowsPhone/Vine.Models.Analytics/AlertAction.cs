using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace Vine.Models.Analytics;

[JsonConverter(typeof(StringEnumConverter))]
public enum AlertAction
{
	CANCEL,
	CONFIRM,
	RESERVED_2,
	RESERVED_3,
	RESERVED_4,
	RESERVED_5
}
