using System.Collections.Generic;
using Vine.Framework;

namespace Vine.Models.Analytics;

public class CommentEvent : ClientEvent
{
	public CommentEvent(string commentId, string authorId, Section section, string view)
		: base("comment")
	{
		base.Navigation = new AppNavigation
		{
			Section = section,
			View = view
		};
		base.EventDetails = new EventDetails
		{
			TimeStamp = TimeStampHelper.GetSecondsSinceUnixEpoch(),
			Items = new List<EventItem>
			{
				new CommentItem(long.Parse(authorId), long.Parse(commentId))
			}
		};
	}
}
