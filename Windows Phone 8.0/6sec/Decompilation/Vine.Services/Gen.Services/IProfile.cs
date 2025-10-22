namespace Gen.Services;

public interface IProfile : IPerson
{
	bool AskFollowing { get; }

	string Email { get; set; }

	string PhoneNumber { get; set; }

	int FollowerCount { get; }

	string Description { get; }

	bool Private { get; set; }

	int LikeCount { get; }

	new bool? Following { get; set; }

	int PostCount { get; }

	int FollowingCount { get; }

	bool ExplicitContent { get; set; }

	bool BlockedByMe { get; set; }

	bool BlockMe { get; }

	bool IncludePromoted { get; set; }

	void ChangeName(string username);

	void ChangeDescription(string description);

	void ChangeLocation(string location);

	void ChangePicture(string uri);

	string GetPublicName();
}
