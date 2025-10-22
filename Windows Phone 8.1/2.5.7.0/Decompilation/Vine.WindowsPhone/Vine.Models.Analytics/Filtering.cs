using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace Vine.Models.Analytics;

[JsonConverter(typeof(StringEnumConverter))]
public enum Filtering
{
	TOP = 1,
	RECENT
}
