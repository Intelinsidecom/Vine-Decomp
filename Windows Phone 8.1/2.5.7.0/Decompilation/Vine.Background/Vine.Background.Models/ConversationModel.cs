using System;
using System.Collections.Generic;
using System.Runtime.CompilerServices;

namespace Vine.Background.Models;

internal class ConversationModel
{
	public extern string LastMessage
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string ConversationId
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern long UnreadMessageCount
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern DateTime Created
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string LastMessageRead
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string Inbox
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern List<string> Users
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern List<VineMessageModel> Messages
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern List<Entity> Entities
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern ConversationModel();
}
