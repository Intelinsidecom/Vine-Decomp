using Vine.Framework;

namespace Vine.Models.Analytics;

public class SessionEndedEvent : ClientEvent
{
	public SessionEndedEvent()
		: base("session_ended")
	{
		base.EventDetails = new EventDetails
		{
			TimeStamp = TimeStampHelper.GetSecondsSinceUnixEpoch()
		};
	}
}
