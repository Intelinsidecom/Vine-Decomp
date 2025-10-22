using System;
using Gen.Services;

namespace Vine.Services.Models;

public class SamplePostRecord : IPostRecord
{
	public string PostId => null;

	public double? Longitude { get; set; }

	public double? Latitude { get; set; }

	public int NbrLikes { get; set; }

	public int NbrComments { get; set; }

	public int IsFirstItem
	{
		get
		{
			return 0;
		}
		set
		{
		}
	}

	public string AvatarUrl { get; set; }

	public DateTime Date { get; set; }

	public string UserId => null;

	public string MinThumb { get; set; }

	public string UserName { get; set; }

	public string Thumb { get; set; }

	public string Description { get; set; }

	public string VideoLink { get; set; }

	public bool IsMyPost { get; set; }

	public string PlaceName { get; set; }

	public string PlaceId { get; set; }

	public string RepostByName { get; set; }

	public string RepostById { get; set; }

	public string ShareUrl { get; set; }

	public bool Liked { get; set; }

	public bool IsVideo { get; set; }

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
