using System;
using System.Collections.Generic;
using System.Runtime.CompilerServices;

namespace Vine.Background.Models;

internal class InteractionModel
{
	public extern string NotificationId
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

	public extern string AvatarUrl
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string Body
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern InteractionType NotificationTypeId
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string PostId
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

	public extern DateTime Created
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

	public extern string FormattedBody { get; }

	public extern string CreatedText { get; }

	public extern InteractionModel();
}
