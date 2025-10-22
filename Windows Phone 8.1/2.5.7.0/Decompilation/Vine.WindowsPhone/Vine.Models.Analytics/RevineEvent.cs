using System.Collections.Generic;
using Vine.Framework;

namespace Vine.Models.Analytics;

public class RevineEvent : ClientEvent
{
	public RevineEvent(VineViewModel vine, Section section, string view)
		: base("revine")
	{
		base.Navigation = new AppNavigation
		{
			Section = section,
			View = view,
			TimelineApiUrl = vine.TimelineApiUrl
		};
		base.EventDetails = new EventDetails
		{
			TimeStamp = TimeStampHelper.GetSecondsSinceUnixEpoch(),
			Items = new List<EventItem>
			{
				new RepostItem(long.Parse(vine.Model.PostId), (vine.Model.Repost != null && !string.IsNullOrEmpty(vine.Model.Repost.RepostId)) ? long.Parse(vine.Model.Repost.RepostId) : 0, long.Parse(vine.Model.UserId), (vine.Model.Repost != null && vine.Model.Repost.User != null) ? long.Parse(vine.Model.Repost.User.UserId) : 0, vine.Model.Liked, vine.Model.Repost != null && vine.Model.Repost.User != null)
			}
		};
	}
}
