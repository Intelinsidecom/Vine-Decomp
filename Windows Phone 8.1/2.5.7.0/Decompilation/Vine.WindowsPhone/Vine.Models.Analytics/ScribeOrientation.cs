using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace Vine.Models.Analytics;

[JsonConverter(typeof(StringEnumConverter))]
public enum ScribeOrientation
{
	PORTRAIT = 1,
	LANDSCAPE,
	FACE_UP
}
