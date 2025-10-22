using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using Vine.Common;
using Vine.Framework;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Media;

namespace Vine.Models;

public class InteractionModel
{
	private FrameworkElement _richBody;

	private FrameworkElement _richCommentText;

	public string Type { get; set; }

	public InteractionType InteractionType
	{
		get
		{
			if (NotificationTypeId == InteractionType.FollowRequest)
			{
				return NotificationTypeId;
			}
			InteractionType result = InteractionType.Unknown;
			Enum.TryParse<InteractionType>(Type.Replace(" ", ""), ignoreCase: true, out result);
			return result;
		}
	}

	public Milestone Milestone { get; set; }

	public string Body { get; set; }

	public bool IsNew { get; set; }

	public string NotificationId { get; set; }

	public string ActivityId { get; set; }

	public string Id
	{
		get
		{
			if (string.IsNullOrEmpty(ActivityId))
			{
				return NotificationId;
			}
			return ActivityId;
		}
	}

	public VineUserModel User { get; set; }

	public VineModel Post { get; set; }

	public string UserId { get; set; }

	public string AvatarUrl { get; set; }

	public InteractionType NotificationTypeId { get; set; }

	public bool FollowVisibility
	{
		get
		{
			if (InteractionType == InteractionType.TwitterFriendJoined || InteractionType == InteractionType.AddressBookFriendJoined || InteractionType == InteractionType.Followed)
			{
				return User != null;
			}
			return false;
		}
	}

	public string UserThumbnail
	{
		get
		{
			if (!string.IsNullOrEmpty(AvatarUrl))
			{
				return AvatarUrl;
			}
			if (Post != null && User != null)
			{
				return User.AvatarUrl;
			}
			return null;
		}
	}

	public string MilestoneThumbUrl
	{
		get
		{
			if (Milestone == null)
			{
				return null;
			}
			if (Post != null)
			{
				if (!Post.Private)
				{
					return Milestone.BackgroundImageUrl;
				}
				return Milestone.BackgroundImageUrl + "?vine-session-id=" + ApplicationSettings.Current.VineSession.Key;
			}
			return Milestone.BackgroundImageUrl;
		}
	}

	public string PostId { get; set; }

	public string ThumbnailUrl { get; set; }

	public string PostThumbnailUrl
	{
		get
		{
			if (Post == null)
			{
				return null;
			}
			if (!Post.Private)
			{
				return Post.ThumbnailUrl;
			}
			return Post.ThumbnailUrl + "?vine-session-id=" + ApplicationSettings.Current.VineSession.Key;
		}
	}

	public bool HasPost => Post != null;

	public DateTime Created { get; set; }

	public DateTime LastActivityTime { get; set; }

	[IgnoreDataMember]
	public FrameworkElement RichBody
	{
		get
		{
			if (_richBody != null)
			{
				return _richBody;
			}
			if (InteractionType == InteractionType.FollowRequest || string.IsNullOrWhiteSpace(ShortBody))
			{
				List<Entity> entities = Entities ?? new List<Entity>();
				return _richBody = (FrameworkElement)(object)EntityHelper.RichTextBlock(Body, entities, isLargeFont: false, null, boldFirst: false, isInteraction: true, (double)((IDictionary<object, object>)Application.Current.Resources)[(object)"TextStyleMediumSmallFontSize"]);
			}
			List<Entity> entities2 = ((ShortBodyEntities != null) ? ShortBodyEntities.ToList() : new List<Entity>());
			return _richBody = (FrameworkElement)(object)EntityHelper.RichTextBlock(ShortBody, entities2, isLargeFont: false, null, boldFirst: false, isInteraction: true, (double)((IDictionary<object, object>)Application.Current.Resources)[(object)"TextStyleMediumSmallFontSize"]);
		}
	}

	public Brush GlyphBrush
	{
		get
		{
			//IL_004a: Unknown result type (might be due to invalid IL or missing references)
			//IL_0050: Expected O, but got Unknown
			//IL_0064: Unknown result type (might be due to invalid IL or missing references)
			//IL_006a: Expected O, but got Unknown
			//IL_0098: Unknown result type (might be due to invalid IL or missing references)
			//IL_009e: Expected O, but got Unknown
			//IL_007e: Unknown result type (might be due to invalid IL or missing references)
			//IL_0084: Expected O, but got Unknown
			switch (InteractionType)
			{
			case InteractionType.Followed:
				return (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"ApplicationPageBackgroundThemeBrush"];
			case InteractionType.Liked:
				return (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineYellowBrush"];
			case InteractionType.Reposted:
				return (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenElectricBrush"];
			case InteractionType.GroupedComment:
			case InteractionType.MentionedPost:
			case InteractionType.MentionedComment:
			case InteractionType.Mentioned:
				return (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VinePurpleBrush"];
			default:
				return null;
			}
		}
	}

	public bool GlyphVisibility
	{
		get
		{
			if (GlyphData != null)
			{
				return InteractionType != InteractionType.Followed;
			}
			return false;
		}
	}

	public bool GlyphFollowedVisibility
	{
		get
		{
			if (GlyphData != null)
			{
				return InteractionType == InteractionType.Followed;
			}
			return false;
		}
	}

	public string GlyphData
	{
		get
		{
			switch (InteractionType)
			{
			case InteractionType.Followed:
				return "M 51.112,43.0627C 44.1333,37.6467 35.4507,34.6667 26.6667,34.6667C 17.8827,34.6667 9.20001,37.6467 2.22131,43.0574C 0.820007,44.1453 0,45.8174 0,47.5894C 0,50.756 2.56537,53.3333 5.73468,53.3333L 47.5987,53.3333C 50.7654,53.3333 53.3333,50.7654 53.3333,47.5987C 53.3333,45.8281 52.5133,44.1453 51.112,43.0627 Z M 37.3333,10.6666C 37.3333,22.6666 32,29.3333 26.6667,29.3333C 21.3333,29.3333 16,22.6666 16,10.6666C 16,4.776 20.7733,0 26.6667,0C 32.5573,0 37.3333,4.776 37.3333,10.6666 Z ";
			case InteractionType.Reposted:
				return "M 8.39066,23.0574L 15.0573,16.3908C 15.3173,16.1307 15.6586,16 16,16C 16.3413,16 16.6826,16.1307 16.9427,16.3908L 23.6093,23.0574C 23.9907,23.4388 24.1053,24.012 23.8987,24.5107C 23.692,25.008 23.2053,25.3333 22.6667,25.3333L 18.6667,25.3333L 18.6667,31.3333C 18.6667,33.1707 20.1613,34.6667 22,34.6667L 26.6667,34.6667C 28.14,34.6667 29.3333,35.8613 29.3333,37.3333C 29.3333,38.8068 28.14,40 26.6667,40L 22,40C 17.2213,40 13.3333,36.112 13.3333,31.3333L 13.3333,25.3333L 9.33331,25.3333C 8.79468,25.3333 8.30798,25.008 8.10132,24.5107C 7.89465,24.012 8.00931,23.4388 8.39066,23.0574 Z M 29.4346,28.8228C 29.6413,28.3254 30.128,28 30.6667,28L 34.6667,28L 34.6667,22C 34.6667,20.1628 33.172,18.6667 31.3333,18.6667L 26.6667,18.6667C 25.1946,18.6667 24,17.4734 24,16C 24,14.528 25.1946,13.3333 26.6667,13.3333L 31.3333,13.3333C 36.112,13.3333 40,17.2214 40,22L 40,28L 44,28C 44.5386,28 45.0253,28.3254 45.232,28.8228C 45.4387,29.3215 45.324,29.8947 44.9427,30.2761L 38.276,36.9427C 38.016,37.2028 37.6747,37.3333 37.3333,37.3333C 36.992,37.3333 36.6507,37.2028 36.3907,36.9427L 29.724,30.2761C 29.3427,29.8947 29.228,29.3215 29.4346,28.8228 Z M 0,26.6667C 0,41.3947 11.9373,53.3333 26.6667,53.3333C 41.396,53.3333 53.3333,41.3947 53.3333,26.6667C 53.3333,11.9388 41.396,0 26.6667,0C 11.9373,0 0,11.9388 0,26.6667 Z ";
			case InteractionType.GroupedComment:
			case InteractionType.MentionedPost:
			case InteractionType.MentionedComment:
			case InteractionType.Mentioned:
				return "M7,2C4.239,2,2,4.238,2,7c0,0.958,0.274,1.85,0.742,2.611l-0.703,1.698c-0.054,0.127-0.05,0.265,0,0.383 c0.049,0.118,0.143,0.218,0.27,0.271c0.127,0.053,0.265,0.049,0.382,0l1.698-0.703C5.15,11.726,6.042,12,7,12c2.762,0,5-2.238,5-5 S9.762,2,7,2z M5.5,4.667h3c0.275,0,0.5,0.225,0.5,0.5c0,0.275-0.225,0.5-0.5,0.5h-3c-0.275,0-0.5-0.225-0.5-0.5C5,4.892,5.225,4.667,5.5,4.667z M8.5,9.667h-3c-0.275,0-0.5-0.225-0.5-0.5c0-0.275,0.225-0.5,0.5-0.5h3c0.275,0,0.5,0.225,0.5,0.5 C9,9.441,8.775,9.667,8.5,9.667z M9.5,7.667h-5c-0.275,0-0.5-0.225-0.5-0.5c0-0.275,0.225-0.5,0.5-0.5h5c0.275,0,0.5,0.225,0.5,0.5 C10,7.441,9.775,7.667,9.5,7.667z";
			default:
				return null;
			}
		}
	}

	public bool HeartGlyphVisibility
	{
		get
		{
			if (InteractionType != InteractionType.Liked)
			{
				return InteractionType == InteractionType.RepostLike;
			}
			return true;
		}
	}

	public List<Entity> Entities { get; set; }

	public List<Entity> ShortBodyEntities { get; set; }

	public List<Entity> CommentTextEntities { get; set; }

	public string CreatedText
	{
		get
		{
			if (!(LastActivityTime != DateTime.MinValue))
			{
				return Created.ToVineTime();
			}
			return LastActivityTime.ToVineTime();
		}
	}

	public string HeaderText { get; set; }

	public bool IsHeaderVisible { get; set; }

	public string ShortBody { get; set; }

	public string CommentText { get; set; }

	[IgnoreDataMember]
	public FrameworkElement RichCommentText
	{
		get
		{
			if (_richCommentText != null)
			{
				return _richCommentText;
			}
			List<Entity> entities = ((CommentTextEntities != null) ? CommentTextEntities.ToList() : new List<Entity>());
			return _richCommentText = (FrameworkElement)(object)EntityHelper.RichTextBlock(CommentText, entities, isLargeFont: false, null, boldFirst: false, isInteraction: true, (double)((IDictionary<object, object>)Application.Current.Resources)[(object)"TextStyleSmallFontSize"]);
		}
	}
}
