using Vine.Framework;

namespace Vine.Models.Analytics;

public class ViewImpressionEvent : ClientEvent
{
	public ViewImpressionEvent(Section section, string view)
		: base("view_impression")
	{
		base.Navigation = new AppNavigation
		{
			Section = section,
			View = view
		};
		base.EventDetails = new EventDetails
		{
			TimeStamp = TimeStampHelper.GetSecondsSinceUnixEpoch()
		};
	}
}
