using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class AppNavigation
{
	[JsonProperty("section")]
	public Section Section { get; set; }

	[JsonProperty("view")]
	public string View { get; set; }

	[JsonProperty("subview")]
	public string Subview { get; set; }

	[JsonProperty("capture_source_section")]
	public string CaptureSourceSection { get; set; }

	[JsonProperty("timeline_api_url")]
	public string TimelineApiUrl { get; set; }

	[JsonProperty("search_query")]
	public string SearchQuery { get; set; }

	[JsonProperty("filtering")]
	public Filtering? Filtering { get; set; }

	[JsonProperty("new_search_view")]
	public bool NewSearchView { get; set; }

	public AppNavigation()
	{
		NewSearchView = false;
		TimelineApiUrl = null;
		Section = Section.None;
	}
}
