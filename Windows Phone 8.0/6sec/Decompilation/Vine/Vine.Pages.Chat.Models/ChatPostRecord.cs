using System;
using Gen.Services;
using Vine.Services.Models;

namespace Vine.Pages.Chat.Models;

public class ChatPostRecord : IPostRecord
{
	public bool IsVideo => true;

	public string PostId => "";

	public int NbrLikes => 0;

	public int NbrComments => 0;

	public string AvatarUrl { get; set; }

	public DateTime Date { get; set; }

	public string UserId { get; set; }

	public string UserName { get; set; }

	public string Thumb { get; set; }

	public string MinThumb => Thumb;

	public string Description { get; set; }

	public string VideoLink { get; set; }

	public bool IsMyPost => false;

	public string PlaceName => null;

	public string PlaceId => null;

	public double? Longitude => null;

	public double? Latitude => null;

	public string RepostByName => null;

	public string RepostById => null;

	public string ShareUrl => null;

	public bool Liked { get; set; }

	public string GetLargestImage()
	{
		return Thumb;
	}

	public IListPersons GetLikes()
	{
		return null;
	}

	public IListComments GetComments()
	{
		return null;
	}

	public void ChangeNbrLike(int p)
	{
	}

	public void AddComment(IComment comment)
	{
	}
}
