namespace Vine.Models;

public class SearchResultModel
{
	public VineSearchSuggestions Suggestion { get; set; }

	public VineUserModel User { get; set; }

	public VineTagModel Tag { get; set; }

	public VineRecentSearch Recent { get; set; }

	public bool HeaderViewAllVisible { get; set; }

	public string HeaderText { get; set; }

	public SearchType SearchType { get; set; }

	public VineModel Vine { get; set; }

	public bool IsSearchSuggestion => Suggestion != null;

	public bool IsHeader => HeaderText != null;

	public bool IsUser => User != null;

	public bool IsTag => Tag != null;

	public bool IsVine => Vine != null;

	public bool IsRecent => Recent != null;
}
