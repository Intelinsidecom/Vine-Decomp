using System;
using System.Collections.Generic;
using System.Linq;

namespace Vine.Models;

public abstract class BaseConversationModel : NotifyObject
{
	private long _unreadMessageCount;

	public abstract List<VineMessageModel> Msgs { get; set; }

	public DateTime FirstMsg
	{
		get
		{
			if (Msgs.FirstOrDefault() != null)
			{
				return Msgs.FirstOrDefault().Created;
			}
			return DateTime.Now;
		}
	}

	public string LastMessage { get; set; }

	public string ConversationId { get; set; }

	public DateTime Created { get; set; }

	public string LastMessageRead { get; set; }

	public string Inbox { get; set; }

	public long UnreadMessageCount
	{
		get
		{
			return _unreadMessageCount;
		}
		set
		{
			_unreadMessageCount = value;
			OnPropertyChanged("UnreadMessageCount");
		}
	}
}
