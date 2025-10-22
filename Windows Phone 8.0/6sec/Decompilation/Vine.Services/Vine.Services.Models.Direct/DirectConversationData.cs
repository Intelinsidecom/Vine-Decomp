using System.Collections.ObjectModel;
using Newtonsoft.Json;

namespace Vine.Services.Models.Direct;

public class DirectConversationData
{
	[JsonProperty("count")]
	public long Count { get; set; }

	[JsonProperty("records")]
	public ObservableCollection<DirectMessage> Records { get; set; }

	[JsonProperty("users")]
	public User[] Users { get; set; }

	[JsonProperty("size")]
	public long Size { get; set; }

	[JsonProperty("anchorStr")]
	public string AnchorStr { get; set; }

	[JsonProperty("previousPage")]
	public object PreviousPage { get; set; }

	[JsonProperty("anchor")]
	public string Anchor { get; set; }

	[JsonProperty("lastMessage")]
	public string LastMessage { get; set; }

	[JsonProperty("inbox")]
	public string Inbox { get; set; }

	[JsonProperty("lastMessageRead")]
	public string LastMessageRead { get; set; }

	[JsonProperty("unreadMessageCount")]
	public int UnreadMessageCount { get; set; }

	[JsonProperty("nextPage")]
	public object NextPage { get; set; }
}
