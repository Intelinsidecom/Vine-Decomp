using System;
using System.Collections.Generic;
using System.Runtime.CompilerServices;

namespace Vine.Background.Models;

internal class VineMessageModel
{
	public extern DateTime Created
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string UserId
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern VineUserModel User
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

	public extern List<Entity> Entities
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string MessageId
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string Message
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string ThumbnailUrl
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string VideoUrl
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern VineModel Post
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern VineMessageModel();
}
