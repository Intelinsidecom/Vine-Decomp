using System.Collections.Generic;

namespace Vine.Services.Response.Notifications;

public class NotificationsData
{
	public int count { get; set; }

	public List<RecordNotification> records { get; set; }

	public int? nextPage { get; set; }

	public string anchor { get; set; }

	public int? previousPage { get; set; }

	public int size { get; set; }
}
