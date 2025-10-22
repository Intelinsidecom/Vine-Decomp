using System;
using Gen.Services;

namespace Vine.Services.Models;

public interface IPostRecord
{
	bool IsVideo { get; }

	string PostId { get; }

	int NbrLikes { get; }

	int NbrComments { get; }

	string AvatarUrl { get; }

	DateTime Date { get; }

	string UserId { get; }

	string UserName { get; }

	string Thumb { get; }

	string MinThumb { get; }

	string Description { get; }

	string VideoLink { get; }

	bool IsMyPost { get; }

	string PlaceName { get; }

	string PlaceId { get; }

	double? Longitude { get; }

	double? Latitude { get; }

	string RepostByName { get; }

	string RepostById { get; }

	string ShareUrl { get; }

	bool Liked { get; set; }

	string GetLargestImage();

	IListPersons GetLikes();

	IListComments GetComments();

	void ChangeNbrLike(int p);

	void AddComment(IComment comment);
}
