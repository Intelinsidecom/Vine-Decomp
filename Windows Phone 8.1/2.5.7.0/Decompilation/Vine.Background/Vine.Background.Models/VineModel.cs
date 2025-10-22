using System;
using System.Collections.Generic;
using System.Runtime.CompilerServices;

namespace Vine.Background.Models;

internal class VineModel
{
	public extern string UserId
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string UserName
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

	public extern string ProfileBackground
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

	public extern bool Liked
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern bool Private
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string MyRepostId
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

	public extern string ShareUrl
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string Description
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

	public extern RepostModel Repost
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern VineStatModel Likes
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern VineStatModel Reposts
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern VineStatModel Comments
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern VineLoopModel Loops
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

	public extern string ThumbnailUrlAuth { get; }

	public extern VineModel();
}
