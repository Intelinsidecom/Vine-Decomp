namespace Vine.Events;

public class ConversationDeleted
{
	public string ConversationId { get; set; }

	public string OtherUserId { get; set; }
}
