using System.Collections.Generic;

namespace Gen.Services;

public class IListNotifications
{
	public List<INotification> Notifications { get; set; }

	public string NextPage { get; set; }
}
