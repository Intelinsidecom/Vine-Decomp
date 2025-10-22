namespace Vine.Models;

public class ReplyVmParameters
{
	public string ConversationId { get; set; }

	public VineUserModel OtherUser { get; set; }
}
