using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class SuggestionDetails
{
	[JsonProperty("suggested_query")]
	public string SuggestedQuery { get; set; }
}
