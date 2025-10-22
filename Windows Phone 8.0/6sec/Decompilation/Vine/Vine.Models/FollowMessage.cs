using GalaSoft.MvvmLight.Messaging;

namespace Vine.Models;

public class FollowMessage : MessageBase
{
	public string UserId { get; set; }

	public bool Follow { get; set; }

	public FollowMessage(string id, bool follow)
	{
		UserId = id;
		Follow = follow;
	}
}
