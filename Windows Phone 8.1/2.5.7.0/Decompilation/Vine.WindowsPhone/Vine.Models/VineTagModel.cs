using Vine.Framework;

namespace Vine.Models;

public class VineTagModel : NotifyObject
{
	public string TagId { get; set; }

	public string Tag { get; set; }

	public int PostCount { get; set; }

	public string FormattedTag => "#" + Tag;

	public string FormattedPostCount => string.Format((PostCount == 1) ? ResourceHelper.GetString("profile_posts_one") : ResourceHelper.GetString("profile_posts_other"), new object[1] { PostCount.ToVineCount() });
}
