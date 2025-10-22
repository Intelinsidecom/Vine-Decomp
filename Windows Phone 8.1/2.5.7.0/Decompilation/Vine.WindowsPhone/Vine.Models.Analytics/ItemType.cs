using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace Vine.Models.Analytics;

[JsonConverter(typeof(StringEnumConverter))]
public enum ItemType
{
	USER = 1,
	POST,
	REPOST,
	COMMENT,
	ACTIVITY,
	VENUE,
	TAG,
	SUGGESTION,
	CHANNEL,
	AUDIO_TRACK
}
