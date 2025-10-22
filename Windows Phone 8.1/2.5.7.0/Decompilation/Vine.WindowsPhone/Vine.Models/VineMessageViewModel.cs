using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using Vine.Common;
using Vine.Framework;
using Windows.UI.Text;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Documents;
using Windows.UI.Xaml.Media;

namespace Vine.Models;

public class VineMessageViewModel : NotifyObject
{
	private bool _requiresVerfication;

	private string _errorMessage;

	private bool _hasError;

	private bool _isPlaying;

	private bool _isLoadingVideo;

	private bool _isFinishedBuffering;

	private string _thumbnailUrlAuth;

	private Visibility _thumbVisibility;

	public List<Run> YouShareText
	{
		get
		{
			//IL_0090: Unknown result type (might be due to invalid IL or missing references)
			//IL_0095: Unknown result type (might be due to invalid IL or missing references)
			//IL_00a1: Expected O, but got Unknown
			//IL_0051: Unknown result type (might be due to invalid IL or missing references)
			//IL_006a: Unknown result type (might be due to invalid IL or missing references)
			//IL_007e: Expected O, but got Unknown
			if (Model.Post != null && Model.Post.UserName != null)
			{
				return EntityHelper.GetFormattedStringRuns(string.Format(ResourceHelper.GetString("VNMessageVideoShareYou"), new object[1] { ResourceHelper.GetString("VNMessageVideoShareObject") }), Model.Post.UserName, FontWeights.SemiBold, (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"], typeof(string));
			}
			string text = ResourceHelper.GetString("share_vm_sender_deleted");
			List<Run> list = new List<Run>();
			Run val = new Run();
			val.put_Text(text);
			list.Add(val);
			return list;
		}
	}

	public List<Run> ShareText
	{
		get
		{
			//IL_00c1: Unknown result type (might be due to invalid IL or missing references)
			//IL_00c6: Unknown result type (might be due to invalid IL or missing references)
			//IL_00d2: Expected O, but got Unknown
			//IL_0082: Unknown result type (might be due to invalid IL or missing references)
			//IL_009b: Unknown result type (might be due to invalid IL or missing references)
			//IL_00af: Expected O, but got Unknown
			if (Model.Post != null && Model.Post.UserName != null)
			{
				return EntityHelper.GetFormattedStringRuns(string.Format(ResourceHelper.GetString("VNMessageVideoShare"), new object[1] { ResourceHelper.GetString("VNMessageVideoShareObject") }), (Model.Post != null && !string.IsNullOrEmpty(Model.Post.UserName)) ? Model.Post.UserName : "", FontWeights.SemiBold, (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"], typeof(string));
			}
			string text = ResourceHelper.GetString("share_vm_receiver_deleted");
			List<Run> list = new List<Run>();
			Run val = new Run();
			val.put_Text(text);
			list.Add(val);
			return list;
		}
	}

	public VineMessageModel Model { get; set; }

	public VineUserModel User { get; set; }

	public bool ShowCreatedDisplay { get; set; }

	public string CreatedDisplay
	{
		get
		{
			if (!ShowCreatedDisplay)
			{
				return string.Empty;
			}
			return Model.Created.ToVineMsgTime();
		}
	}

	public bool RequiresVerification
	{
		get
		{
			return _requiresVerfication;
		}
		set
		{
			_requiresVerfication = value;
			OnPropertyChanged("RequiresVerification");
		}
	}

	public string ErrorMessage
	{
		get
		{
			return _errorMessage;
		}
		set
		{
			_errorMessage = value;
			OnPropertyChanged("ErrorMessage");
			NotifyOfPropertyChange(() => LightBrush);
			NotifyOfPropertyChange(() => UserBrush);
		}
	}

	public bool HasError
	{
		get
		{
			return _hasError;
		}
		set
		{
			_hasError = value;
			OnPropertyChanged("HasError");
		}
	}

	public Brush UserBrush
	{
		get
		{
			//IL_0021: Unknown result type (might be due to invalid IL or missing references)
			//IL_0027: Expected O, but got Unknown
			if (!string.IsNullOrEmpty(ErrorMessage))
			{
				return (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGrayMediumBrush"];
			}
			return (Brush)(object)User.ProfileBgBrush;
		}
	}

	public Brush LightBrush
	{
		get
		{
			//IL_0021: Unknown result type (might be due to invalid IL or missing references)
			//IL_0027: Expected O, but got Unknown
			if (!string.IsNullOrEmpty(ErrorMessage))
			{
				return (Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGrayVerySoftBrush"];
			}
			return User.ProfileBgLightBrush;
		}
	}

	[IgnoreDataMember]
	public bool IsPlaying
	{
		get
		{
			return _isPlaying;
		}
		set
		{
			if (_isPlaying != value)
			{
				_isPlaying = value;
				IsLoadingVideo = value;
				OnPropertyChanged("IsPlaying");
				NotifyOfPropertyChange(() => PlayingVideoUrl);
				ThumbVisibility = (Visibility)((IsPlaying && IsFinishedBuffering) ? 1 : 0);
			}
		}
	}

	[IgnoreDataMember]
	public bool IsLoadingVideo
	{
		get
		{
			return _isLoadingVideo;
		}
		set
		{
			SetProperty(ref _isLoadingVideo, value, "IsLoadingVideo");
		}
	}

	[IgnoreDataMember]
	public bool IsFinishedBuffering
	{
		get
		{
			return _isFinishedBuffering;
		}
		set
		{
			SetProperty(ref _isFinishedBuffering, value, "IsFinishedBuffering");
			ThumbVisibility = (Visibility)((IsPlaying && IsFinishedBuffering) ? 1 : 0);
		}
	}

	public string PlayingVideoUrl
	{
		get
		{
			if (IsPlaying)
			{
				if (Model.Post != null)
				{
					return Model.Post.VideoUrl;
				}
				return Model.VideoUrl + "?vine-session-id=" + ApplicationSettings.Current.VineSession.Key;
			}
			return null;
		}
	}

	public string PostDescription
	{
		get
		{
			if (Model.Post == null || Model.Post.Description == null)
			{
				return ResourceHelper.GetString("share_vm_deleted");
			}
			return Model.Post.Description;
		}
	}

	public bool IsPostDeleted
	{
		get
		{
			if (Model.Error != null)
			{
				return Model.Error.Code == 613;
			}
			return false;
		}
	}

	[IgnoreDataMember]
	public string ThumbnailUrlAuth
	{
		get
		{
			if (_thumbnailUrlAuth != null)
			{
				return _thumbnailUrlAuth;
			}
			if (Model.ThumbnailUrl == null)
			{
				return null;
			}
			return Model.ThumbnailUrl + "?vine-session-id=" + ApplicationSettings.Current.VineSession.Key;
		}
	}

	[IgnoreDataMember]
	public Visibility ThumbVisibility
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return _thumbVisibility;
		}
		set
		{
			//IL_0007: Unknown result type (might be due to invalid IL or missing references)
			SetProperty(ref _thumbVisibility, value, "ThumbVisibility");
		}
	}

	public VineMessageViewModel()
	{
	}

	public VineMessageViewModel(VineMessageModel model, VineUserModel user)
	{
		Model = model;
		User = user;
	}

	public VineMessageViewModel(UploadJob uploadJob)
	{
		User = ApplicationSettings.Current.User;
		Model = new VineMessageModel
		{
			ConversationId = uploadJob.ReplyConversationId,
			Created = DateTime.UtcNow,
			ThumbnailUrl = null,
			VideoUrl = null,
			UserId = User.UserId,
			UploadJob = uploadJob
		};
		_thumbnailUrlAuth = uploadJob.ThumbPath;
	}
}
