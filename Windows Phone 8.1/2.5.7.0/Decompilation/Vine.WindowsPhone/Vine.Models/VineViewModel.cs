using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization;
using System.Threading.Tasks;
using Vine.Common;
using Vine.Framework;
using Vine.Models.Analytics;
using Vine.Views;
using Vine.Web;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Media;

namespace Vine.Models;

public class VineViewModel : NotifyObject
{
	private bool _isPlaying;

	private bool _isFinishedBuffering;

	private bool _isLoadingVideo;

	private Uri _vineUri;

	private bool _isDownloading;

	private Visibility _thumbVisibility;

	private VineToggleButtonState? _likeButtonState;

	private VineToggleButtonState? _revineButtonState;

	private FrameworkElement _richBody;

	private Brush _secondaryBrush;

	private bool? _hasMusic;

	private IEnumerable<MosaicImageViewModel> _mosaicThumbnails;

	public string LikeStatText => Model.Likes.Count.ToVineCount();

	public string CommentStatText => Model.Comments.Count.ToVineCount();

	public string RevineStatText => Model.Reposts.Count.ToVineCount();

	public bool IsRevinedByMe
	{
		get
		{
			if (!string.IsNullOrEmpty(Model.MyRepostId))
			{
				return Model.MyRepostId != "0";
			}
			return false;
		}
	}

	public bool IsRevined
	{
		get
		{
			if (Model.Repost != null)
			{
				return Model.Repost.User != null;
			}
			return false;
		}
	}

	public string RevinedByText
	{
		get
		{
			if (!IsRevined)
			{
				return string.Empty;
			}
			return string.Format(ResourceHelper.GetString("FeedRepostLabel"), new object[1] { Model.Repost.User.Username });
		}
	}

	public bool HasSimilarVines => Model.HasSimilarPosts;

	public bool IsMyPost => ApplicationSettings.Current.UserId == Model.UserId;

	public bool RevineEnabled
	{
		get
		{
			if (!IsMyPost)
			{
				return !Model.Private;
			}
			return false;
		}
	}

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

	public Uri PlayingVideoUrl
	{
		get
		{
			if (IsPlaying && _vineUri != null)
			{
				return _vineUri;
			}
			if (IsPlaying)
			{
				string uriString;
				if (Model.Private)
				{
					char c = (Model.VineUrl.Contains('?') ? '&' : '?');
					uriString = string.Format("{0}{1}vine-session-id={2}", new object[3]
					{
						Model.VineUrl,
						c,
						ApplicationSettings.Current.VineSession.Key
					});
				}
				else
				{
					uriString = Model.VineUrl;
				}
				return new Uri(uriString);
			}
			return null;
		}
	}

	public string ThumbnailUrlAuth
	{
		get
		{
			if (!Model.Private)
			{
				return Model.ThumbnailUrl;
			}
			return Model.ThumbnailUrl + "?vine-session-id=" + ApplicationSettings.Current.VineSession.Key;
		}
	}

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

	public VineToggleButtonState LikeButtonState
	{
		get
		{
			if (!_likeButtonState.HasValue)
			{
				_likeButtonState = (Model.Liked ? VineToggleButtonState.On : VineToggleButtonState.Off);
			}
			return _likeButtonState.Value;
		}
		set
		{
			SetProperty(ref _likeButtonState, value, "LikeButtonState");
		}
	}

	public VineToggleButtonState RevineButtonState
	{
		get
		{
			if (!_revineButtonState.HasValue)
			{
				_revineButtonState = (IsRevinedByMe ? VineToggleButtonState.On : VineToggleButtonState.Off);
			}
			return _revineButtonState.Value;
		}
		set
		{
			SetProperty(ref _revineButtonState, value, "RevineButtonState");
		}
	}

	public string LoopText
	{
		get
		{
			if (DisplayLoops == 0L)
			{
				DisplayLoops = Model.Loops.Count;
			}
			return DisplayLoops.ToCommaSeperated();
		}
	}

	public string LoopLabelText
	{
		get
		{
			if (Model.Loops.Count != 1)
			{
				return string.Format(" {0}", new object[1] { ResourceHelper.GetString("FeedCellTextLoops") });
			}
			return string.Format(" {0}", new object[1] { ResourceHelper.GetString("FeedCellTextLoop") });
		}
	}

	public string CreatedText
	{
		get
		{
			if (string.IsNullOrEmpty(Model.VenueName))
			{
				return Model.Created.ToVineTime();
			}
			return string.Format("{0} at {1}", new object[2]
			{
				Model.Created.ToVineTime(),
				Model.VenueName
			});
		}
	}

	public Visibility LocationVisibility
	{
		get
		{
			if (string.IsNullOrEmpty(Model.VenueName))
			{
				return (Visibility)1;
			}
			return (Visibility)0;
		}
	}

	[IgnoreDataMember]
	public FrameworkElement RichBody
	{
		get
		{
			if (_richBody != null)
			{
				return _richBody;
			}
			_richBody = GetRichBody();
			return _richBody;
		}
		set
		{
			SetProperty(ref _richBody, value, "RichBody");
		}
	}

	public VineModel Model { get; set; }

	public Brush SecondaryBrush
	{
		get
		{
			return _secondaryBrush;
		}
		set
		{
			if (SetProperty(ref _secondaryBrush, value, "SecondaryBrush"))
			{
				RichBody = GetRichBody();
			}
		}
	}

	public int PendingLoopCount { get; set; }

	public int LoopsWatchedCount { get; set; }

	public long DisplayLoops { get; set; }

	public DateTime LastLoopFinishTime { get; set; }

	public DateTime FirstLoopStartTime { get; set; }

	public bool IsDownloaded { get; set; }

	public Section Section { get; set; }

	public string View { get; set; }

	public string TimelineApiUrl { get; set; }

	public bool HasMusic
	{
		get
		{
			if (!_hasMusic.HasValue)
			{
				if (Model.AudioTracks != null && Model.AudioTracks.Any() && Model.AudioTracks[0].Track != null)
				{
					_hasMusic = true;
				}
				else
				{
					_hasMusic = false;
				}
			}
			return _hasMusic.Value;
		}
	}

	public bool IsSeamlessLooping { get; set; }

	public int LoopsPerClip { get; set; }

	public IEnumerable<MosaicImageViewModel> MosaicThumbnails
	{
		get
		{
			if (_mosaicThumbnails == null && Model.Records != null)
			{
				_mosaicThumbnails = (from m in Model.Records.Where((VineModel m) => m != null).Take(5).Reverse()
					select new MosaicImageViewModel(m)).ToList();
			}
			return _mosaicThumbnails;
		}
	}

	public VineViewModel(VineModel model, Brush highlightBrush, Section section, string view)
	{
		Model = model;
		Section = section;
		View = view;
		_secondaryBrush = highlightBrush;
	}

	public void FallbackToRemoteUri()
	{
		string text = null;
		if (Model.Private)
		{
			char c = (Model.VineUrl.Contains('?') ? '&' : '?');
			text = string.Format("{0}{1}vine-session-id={2}", new object[3]
			{
				Model.VineUrl,
				c,
				ApplicationSettings.Current.VineSession.Key
			});
		}
		else
		{
			text = Model.VineUrl;
		}
		_vineUri = new Uri(text);
		IsDownloaded = false;
		IsSeamlessLooping = false;
		NotifyOfPropertyChange(() => PlayingVideoUrl);
	}

	public async Task<bool> IsCached()
	{
		if (_vineUri != null && _vineUri.Scheme != "http")
		{
			return true;
		}
		Uri uri = await ApplicationSettings.Current.GetVineCacheFilepath(Model.PostId);
		if (uri != null)
		{
			IsDownloaded = true;
			_vineUri = uri;
			return true;
		}
		return false;
	}

	public async Task DownloadVine()
	{
		if (IsDownloaded || _isDownloading || Model.VineUrl == null)
		{
			return;
		}
		_isDownloading = true;
		Uri cacheFile = await ApplicationSettings.Current.GetVineCacheFilepath(Model.PostId);
		if (cacheFile == null)
		{
			string vineUrl;
			if (Model.Private)
			{
				char c = (Model.VineUrl.Contains('?') ? '&' : '?');
				vineUrl = string.Format("{0}{1}vine-session-id={2}", new object[3]
				{
					Model.VineUrl,
					c,
					ApplicationSettings.Current.VineSession.Key
				});
			}
			else
			{
				vineUrl = Model.VineUrl;
			}
			ApiResult<Stream> stream = await App.Api.GetVineAuthStream(vineUrl, null);
			if (!stream.HasError)
			{
				try
				{
					VineViewModel vineViewModel = this;
					_ = vineViewModel._vineUri;
					vineViewModel._vineUri = await ApplicationSettings.Current.SetVineCacheFile(Model.PostId, stream.Model);
				}
				catch
				{
				}
			}
			App.ScribeService.Log(new VideoDownloadedEvent(stream, Section, View));
			if (_vineUri == null)
			{
				_vineUri = new Uri(vineUrl);
			}
		}
		else
		{
			_vineUri = cacheFile;
		}
		_isDownloading = false;
		IsDownloaded = true;
	}

	private FrameworkElement GetRichBody()
	{
		if (Model == null)
		{
			return null;
		}
		return (FrameworkElement)(object)EntityHelper.RichTextBlock(Model.Description, Model.Entities, isLargeFont: false, SecondaryBrush);
	}

	public void NotifyCountChange()
	{
		NotifyOfPropertyChange(() => LikeStatText);
		NotifyOfPropertyChange(() => CommentStatText);
		NotifyOfPropertyChange(() => RevineStatText);
	}

	public void NotifyLoopChange()
	{
		NotifyOfPropertyChange(() => LoopText);
	}
}
