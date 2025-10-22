using System.ComponentModel;
using Gen.Services;
using Newtonsoft.Json;

namespace Vine.Services.Models;

public class VineProfile : IProfile, IPerson, INotifyPropertyChanged
{
	[JsonProperty(PropertyName = "followApprovalPending")]
	public bool AskFollowing { get; set; }

	public string SubName => Location;

	[JsonProperty(PropertyName = "username")]
	public string Name { get; set; }

	[JsonProperty(PropertyName = "email")]
	public string Email { get; set; }

	[JsonProperty(PropertyName = "phoneNumber")]
	public string PhoneNumber { get; set; }

	[JsonProperty(PropertyName = "followerCount")]
	public int FollowerCount { get; set; }

	[JsonProperty(PropertyName = "description")]
	public string Description { get; set; }

	[JsonProperty(PropertyName = "avatarUrl")]
	public string Picture { get; set; }

	[JsonProperty(PropertyName = "userId")]
	public string Id { get; set; }

	[JsonProperty(PropertyName = "private")]
	public bool Private { get; set; }

	[JsonProperty(PropertyName = "likeCount")]
	public int LikeCount { get; set; }

	[JsonProperty(PropertyName = "following")]
	public bool? Following { get; set; }

	[JsonProperty(PropertyName = "postCount")]
	public int PostCount { get; set; }

	[JsonProperty(PropertyName = "location")]
	public string Location { get; set; }

	[JsonProperty(PropertyName = "followingCount")]
	public int FollowingCount { get; set; }

	[JsonProperty(PropertyName = "explicitContent")]
	public bool ExplicitContent { get; set; }

	[JsonProperty(PropertyName = "blocking")]
	public bool BlockedByMe { get; set; }

	[JsonProperty(PropertyName = "blocked")]
	public bool BlockMe { get; set; }

	[JsonProperty(PropertyName = "includePromoted")]
	public bool IncludePromoted { get; set; }

	[JsonProperty(PropertyName = "edition")]
	public string Edition { get; set; }

	public bool IsVerified { get; set; }

	[JsonProperty(PropertyName = "verified")]
	public int Verified { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public void ChangeName(string username)
	{
		Name = username;
		RaisePropertyChanged("Name");
	}

	public void ChangeDescription(string description)
	{
		Description = description;
		RaisePropertyChanged("Description");
	}

	public void ChangeLocation(string location)
	{
		Location = location;
		RaisePropertyChanged("Location");
	}

	public void ChangePicture(string uri)
	{
		Picture = uri;
		RaisePropertyChanged("Picture");
	}

	public void RaisePropertyChanged(string name)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(name));
		}
	}

	public void ChangeFollow(bool val)
	{
	}

	public string GetPublicName()
	{
		return Name;
	}
}
