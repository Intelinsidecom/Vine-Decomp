using Vine.Framework;

namespace Vine.Models.Analytics;

public class SessionStartedEvent : ClientEvent
{
	public SessionStartedEvent()
		: base("session_started")
	{
		base.EventDetails = new EventDetails
		{
			TimeStamp = TimeStampHelper.GetSecondsSinceUnixEpoch()
		};
	}
}
