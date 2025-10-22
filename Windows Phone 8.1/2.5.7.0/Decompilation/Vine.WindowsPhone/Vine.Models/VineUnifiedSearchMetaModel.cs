using System.Collections.Generic;

namespace Vine.Models;

public class VineUnifiedSearchMetaModel
{
	public IEnumerable<VineSearchSuggestions> Suggestions { get; set; }

	public IEnumerable<VineUnifiedSearchResultMetaModel> Results { get; set; }
}
