using System.Collections.Generic;
using System.Linq;
using Vine.Framework;
using Windows.UI.Xaml.Media;

namespace Vine.Models;

public class ConversationViewModel
{
	private SolidColorBrush _currentUserBrush;

	private Brush _currentUserLightBrush;

	private Brush _otherUserBrush;

	private Brush _otherUserLightBrush;

	public VineUserModel OtherUser { get; set; }

	public BaseConversationModel Record { get; set; }

	public List<string> DeletedMsgIds { get; set; }

	public VineUserModel CurrentUser => ApplicationSettings.Current.User;

	public string LastMessageDateDisplay
	{
		get
		{
			if (Record.Msgs.FirstOrDefault() != null)
			{
				return Record.Msgs.FirstOrDefault().Created.ToVineTime();
			}
			return Record.Created.ToVineTime();
		}
	}

	public SolidColorBrush CurrentUserBrush
	{
		get
		{
			if (_currentUserBrush == null)
			{
				_currentUserBrush = CurrentUser.ProfileBgBrush;
			}
			return _currentUserBrush;
		}
	}

	public Brush CurrentUserLightBrush
	{
		get
		{
			if (_currentUserLightBrush == null)
			{
				_currentUserLightBrush = CurrentUser.ProfileBgLightBrush;
			}
			return _currentUserLightBrush;
		}
	}

	public Brush OtherUserBrush
	{
		get
		{
			if (_otherUserBrush == null)
			{
				_otherUserBrush = (Brush)(object)OtherUser.ProfileBgBrush;
			}
			return _otherUserBrush;
		}
	}

	public Brush OtherUserLightBrush
	{
		get
		{
			if (_otherUserLightBrush == null)
			{
				_otherUserLightBrush = OtherUser.ProfileBgLightBrush;
			}
			return _otherUserLightBrush;
		}
	}

	public ConversationViewModel()
	{
		DeletedMsgIds = new List<string>();
	}

	public ConversationViewModel(VineUserType type, string identifier, BaseConversationModel record)
		: this()
	{
		OtherUser = new VineUserModel
		{
			UserId = identifier,
			Username = identifier,
			UserType = type
		};
		Record = record;
	}

	public ConversationViewModel(VineUserModel otherUser, BaseConversationModel record)
		: this()
	{
		OtherUser = otherUser;
		Record = record;
	}
}
