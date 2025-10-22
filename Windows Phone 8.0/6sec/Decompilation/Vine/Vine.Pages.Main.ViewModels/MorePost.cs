using System;
using Gen.Services;
using Vine.Services.Models;

namespace Vine.Pages.Main.ViewModels;

internal class MorePost : IPostRecord
{
	public bool IsVideo { get; set; }

	public string PostId { get; set; }

	public int NbrLikes { get; set; }

	public int NbrComments { get; set; }

	public int IsFirstItem { get; set; }

	public string AvatarUrl { get; set; }

	public DateTime Date { get; set; }

	public string UserId { get; set; }

	public string UserName { get; set; }

	public string Thumb => "/Assets/more.png";

	public string MinThumb => "/Assets/more.png";

	public string Description { get; set; }

	public string VideoLink { get; set; }

	public bool IsMyPost { get; set; }

	public string PlaceName { get; set; }

	public string PlaceId { get; set; }

	public double? Longitude { get; set; }

	public double? Latitude { get; set; }

	public string RepostByName { get; set; }

	public string RepostById { get; set; }

	public string ShareUrl { get; set; }

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
