using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Globalization;
using System.Linq;
using System.Runtime.Serialization;
using Gen.Services;
using Newtonsoft.Json;
using Vine.Datas;
using Vine.Services.Response;

namespace Vine.Services.Models;

public class VinePostRecord : IPostRecord, INotifyPropertyChanged
{
	public bool IsMyPostOrRevinedByMe
	{
		get
		{
			string id = DatasProvider.Instance.CurrentUser.User.Id;
			if (!(UserId == id))
			{
				if (Repost != null)
				{
					return Repost.UserId == id;
				}
				return false;
			}
			return true;
		}
	}

	public bool IsVideo => true;

	[JsonProperty(PropertyName = "repost")]
	public Repost Repost { get; set; }

	[JsonProperty(PropertyName = "liked")]
	public bool Liked { get; set; }

	[JsonProperty(PropertyName = "foursquareVenueId")]
	public string PlaceId { get; set; }

	public int @private { get; set; }

	public Likes likes { get; set; }

	public Loops loops { get; set; }

	public int postToFacebook { get; set; }

	[JsonProperty(PropertyName = "thumbnailUrl")]
	public string Thumb { get; set; }

	public string MinThumb => Thumb;

	public int explicitContent { get; set; }

	public int verified { get; set; }

	[JsonProperty(PropertyName = "avatarUrl")]
	public string AvatarUrl { get; set; }

	public Comments comments { get; set; }

	public List<Entity> entities { get; set; }

	public string location { get; set; }

	[JsonProperty(PropertyName = "username")]
	public string UserName { get; set; }

	[JsonProperty(PropertyName = "description")]
	public string Description { get; set; }

	public List<object> tags { get; set; }

	public string postVerified { get; set; }

	[JsonProperty(PropertyName = "postId")]
	public string PostId { get; set; }

	[JsonProperty(PropertyName = "videoUrls")]
	public List<VineVideo> VideoUrls { get; set; }

	public DateTime Date { get; set; }

	[JsonProperty(PropertyName = "created")]
	public string DateStr
	{
		get
		{
			_ = Date;
			return Date.ToString();
		}
		set
		{
			Date = DateTime.Parse(value, new CultureInfo("en-US"), DateTimeStyles.AssumeUniversal);
		}
	}

	public bool IsMyPost
	{
		get
		{
			if (UserId != null)
			{
				return UserId == DatasProvider.Instance.CurrentUser.User.Id;
			}
			return false;
		}
	}

	[JsonProperty(PropertyName = "shareUrl")]
	public string ShareUrl { get; set; }

	public int promoted { get; set; }

	public string venueCategoryId { get; set; }

	[JsonProperty(PropertyName = "venueName")]
	public string PlaceName { get; set; }

	public double? Longitude => null;

	public double? Latitude => null;

	public string venueCategoryShortName { get; set; }

	public string venueCountryCode { get; set; }

	public string venueState { get; set; }

	public string venueAddress { get; set; }

	public string venueCategoryIconUrl { get; set; }

	public string venueCity { get; set; }

	[IgnoreDataMember]
	public int IsFirstItem { get; set; }

	[JsonProperty(PropertyName = "myRepostId")]
	public string MyRepostId { get; set; }

	public string RepostByName
	{
		get
		{
			if (Repost != null)
			{
				return Repost.Username;
			}
			return null;
		}
	}

	[JsonProperty(PropertyName = "userId")]
	public string UserId { get; set; }

	public string RepostById
	{
		get
		{
			if (Repost != null)
			{
				return Repost.UserId;
			}
			return null;
		}
	}

	public string VideoLink
	{
		get
		{
			if (VideoUrls == null)
			{
				return null;
			}
			VineVideo vineVideo = VideoUrls.Where((VineVideo v) => v.format == "h264" && v.rate == "0").FirstOrDefault();
			if (vineVideo != null)
			{
				return vineVideo.videoUrl;
			}
			vineVideo = VideoUrls.Where((VineVideo v) => v.format == "h264").FirstOrDefault();
			if (vineVideo != null)
			{
				return vineVideo.videoUrl;
			}
			return VideoUrls.FirstOrDefault()?.videoUrl;
		}
	}

	public int NbrLikes
	{
		get
		{
			if (likes != null)
			{
				return likes.count;
			}
			return 0;
		}
		set
		{
			likes.count = value;
		}
	}

	public int NbrComments
	{
		get
		{
			if (comments != null)
			{
				return comments.count;
			}
			return 0;
		}
	}

	public event PropertyChangedEventHandler PropertyChanged;

	public IListPersons GetLikes()
	{
		return new IListPersons
		{
			Persons = ((IEnumerable<VineRecordPerson>)likes.records).Select((Func<VineRecordPerson, IPerson>)((VineRecordPerson f) => f)).ToList()
		};
	}

	public IListComments GetComments()
	{
		return new IListComments
		{
			Comments = ((IEnumerable<VineComment>)comments.records).Select((Func<VineComment, IComment>)((VineComment f) => f)).ToList(),
			NextPage = (comments.nextPage.HasValue ? comments.nextPage.ToString() : null)
		};
	}

	public void ChangeNbrLike(int p)
	{
		NbrLikes = p;
		RaisePropertyChanged("NbrLikes");
	}

	private void RaisePropertyChanged(string p)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(p));
		}
	}

	public void AddComment(IComment comment)
	{
	}

	public string GetLargestImage()
	{
		return Thumb;
	}
}
