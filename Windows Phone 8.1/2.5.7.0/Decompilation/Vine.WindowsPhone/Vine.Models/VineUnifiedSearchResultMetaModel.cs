namespace Vine.Models;

public class VineUnifiedSearchResultMetaModel
{
	public int? DisplayCount { get; set; }

	public VineUnifiedSearchResult<VineUserModel> Users { get; set; }

	public VineUnifiedSearchResult<VineTagModel> Tags { get; set; }

	public VineUnifiedSearchResult<VineModel> Posts { get; set; }
}
