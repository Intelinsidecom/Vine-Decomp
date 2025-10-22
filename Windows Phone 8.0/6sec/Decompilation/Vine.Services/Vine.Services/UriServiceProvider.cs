using System.Net;

namespace Vine.Services;

public static class UriServiceProvider
{
	public static double MaxVideoLength => 6.0;

	public static string GetUriForLikesFromPost(string id)
	{
		return "posts/" + id + "/likes";
	}

	public static string GetUriForTag(string tag)
	{
		return "tags/" + HttpUtility.UrlEncode(tag);
	}

	public static string GetUriForUser(string userid)
	{
		return "users/" + userid;
	}

	public static string GetUriForLikes(string userid)
	{
		return "users/" + userid + "/likes";
	}

	public static string GetUriForWall()
	{
		return "graph";
	}

	public static string GetUriForFollowersFromUser(string id)
	{
		return "users/" + id + "/followers";
	}

	public static string GetUriForFollowingFromUser(string id)
	{
		return "users/" + id + "/following";
	}

	public static string GetUriPopular()
	{
		return "popular";
	}

	public static string GetUriForOnePicture(string id)
	{
		return "posts/" + id;
	}
}
