using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Runtime.Serialization;
using Newtonsoft.Json;
using Vine.Services.Models.Direct;

namespace Vine.Services.Models;

public class Conversation : INotifyPropertyChanged
{
	[JsonProperty("lastMessage")]
	public string LastMessage { get; set; }

	[JsonProperty("messages")]
	public ObservableCollection<DirectMessage> Messages { get; set; }

	public DirectMessage LastConversationMessage => Messages.LastOrDefault();

	[JsonProperty("conversationId")]
	public string ConversationId { get; set; }

	[JsonProperty("unreadMessageCount")]
	public int UnreadMessageCount { get; set; }

	[JsonProperty("createdBy")]
	public string CreatedBy { get; set; }

	[JsonProperty("lastMessageRead")]
	public string LastMessageRead { get; set; }

	[JsonProperty("users")]
	public string[] UsersId { get; set; }

	[JsonProperty("inbox")]
	public string Inbox { get; set; }

	[IgnoreDataMember]
	public User User { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public void AddMessage(DirectMessage message)
	{
		Messages.Add(message);
	}

	public bool Merge(DirectConversationData res)
	{
		bool result = false;
		LastMessageRead = res.LastMessageRead;
		LastMessage = res.LastMessage;
		UnreadMessageCount = res.UnreadMessageCount;
		DateTime mindate = Messages.Min((DirectMessage d) => d.Created);
		ObservableCollection<DirectMessage> records = res.Records;
		Func<DirectMessage, bool> func2 = default(Func<DirectMessage, bool>);
		Func<DirectMessage, bool> func = func2;
		if (func == null)
		{
			func = (func2 = (DirectMessage c) => c.Created > mindate);
		}
		foreach (DirectMessage message in from c in records.Where(func)
			orderby c.Created
			select c)
		{
			if (Messages.FirstOrDefault((DirectMessage c) => c.MessageId == message.MessageId) == null)
			{
				result = true;
				Messages.Add(message);
			}
		}
		return result;
	}

	public void ResetCounter()
	{
		UnreadMessageCount = 0;
		RaisePropertyChanged("UnreadMessageCount");
	}

	public void RaisePropertyChanged(string name)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(name));
		}
	}

	public void UpdateTimeOfMessages()
	{
		if (Messages == null)
		{
			return;
		}
		foreach (DirectMessage message in Messages)
		{
			message.UpdateCounter();
		}
	}
}
