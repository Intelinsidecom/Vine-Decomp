using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization;
using System.Threading.Tasks;
using Vine.Framework;
using Vine.Special;
using Vine.Web;
using Windows.ApplicationModel.Chat;
using Windows.Storage;
using Windows.Storage.Streams;
using Windows.Web.Http;

namespace Vine.Models;

public class UploadJob : NotifyObject, IProgress<HttpProgress>
{
	private double _progress;

	private double _stageComplete;

	private double _progressStageWeight;

	[IgnoreDataMember]
	private bool _isActive;

	[IgnoreDataMember]
	private bool _requiresValidation;

	[IgnoreDataMember]
	private string _errorMessage;

	public string Id { get; set; }

	[IgnoreDataMember]
	public double Progress
	{
		get
		{
			return _progress;
		}
		set
		{
			SetProperty(ref _progress, value, "Progress");
		}
	}

	public bool IsActive
	{
		get
		{
			return _isActive;
		}
		set
		{
			SetProperty(ref _isActive, value, "IsActive");
			NotifyOfPropertyChange(() => StatusText);
			NotifyOfPropertyChange(() => IsNotActive);
			NotifyOfPropertyChange(() => RefreshIcon);
		}
	}

	public bool RequiresValidation
	{
		get
		{
			return _requiresValidation;
		}
		set
		{
			SetProperty(ref _requiresValidation, value, "RequiresValidation");
			NotifyOfPropertyChange(() => RefreshIcon);
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
			SetProperty(ref _errorMessage, value, "ErrorMessage");
		}
	}

	[IgnoreDataMember]
	public string RefreshIcon
	{
		get
		{
			if (RequiresValidation)
			{
				return "M 30.5653,20.904L 28,32L 25.3333,32L 22.768,20.904C 22.7027,20.6147 22.6667,20.3133 22.6667,20C 22.6667,17.792 24.4573,16 26.6667,16C 28.876,16 30.6667,17.792 30.6667,20C 30.6667,20.3133 30.632,20.6147 30.5653,20.904 Z M 26.6667,42.6667C 24.4573,42.6667 22.6667,40.8787 22.6667,38.6667C 22.6667,36.4586 24.4573,34.6667 26.6667,34.6667C 28.876,34.6667 30.6667,36.4586 30.6667,38.6667C 30.6667,40.8787 28.876,42.6667 26.6667,42.6667 Z M 52.9947,44.0347L 28.9947,1.36804C 28.5387,0.552002 27.6667,0 26.6667,0C 25.6667,0 24.7947,0.552002 24.3387,1.36804L 0.338654,44.0347C 0.122681,44.42 0,44.8627 0,45.3333C 0,46.808 1.19467,48 2.66666,48L 50.6667,48C 52.14,48 53.3333,46.808 53.3333,45.3333C 53.3333,44.8627 53.2107,44.42 52.9947,44.0347 Z";
			}
			return "M 25.8444,66.4638C 26.3417,66.2584 26.6671,65.7704 26.6671,65.2318L 26.6671,59.8984C 41.3724,59.8984 53.3337,47.9357 53.3337,33.2318C 53.3337,25.7798 50.1831,18.6171 44.6897,13.5784C 43.6044,12.5825 41.9177,12.6558 40.9231,13.741C 39.9257,14.8264 39.9991,16.5131 41.0857,17.5078C 45.4804,21.5397 48.0004,27.2704 48.0004,33.2318C 48.0004,44.9944 38.4297,54.5651 26.6671,54.5651L 26.6671,49.2318C 26.6671,48.6931 26.3417,48.2064 25.8444,47.9998C 25.3457,47.7944 24.7724,47.9077 24.3911,48.2891L 16.3911,56.2891C 16.1311,56.5491 16.0004,56.8904 16.0004,57.2318C 16.0004,57.5731 16.1311,57.9144 16.3911,58.1744L 24.3911,66.1744C 24.7724,66.5557 25.3457,66.6691 25.8444,66.4638 Z M 12.2493,48.9561C 7.85468,44.924 5.33333,39.1934 5.33333,33.232C 5.33333,21.4693 14.904,11.8987 26.6667,11.8987L 26.6667,17.232C 26.6667,17.7707 26.9933,18.2573 27.4907,18.4641C 27.9893,18.6694 28.5613,18.556 28.9427,18.1746L 36.9427,10.1746C 37.204,9.91473 37.3333,9.5733 37.3333,9.23199C 37.3333,8.89069 37.204,8.54932 36.9427,8.28937L 28.9427,0.289368C 28.5613,-0.09198 27.9893,-0.205322 27.4907,0C 26.9933,0.205322 26.6667,0.693359 26.6667,1.23199L 26.6667,6.56537C 11.9627,6.56537 0,18.528 0,33.232C 0,40.684 3.15201,47.8467 8.64534,52.8853C 9.72934,53.8813 11.4173,53.8093 12.412,52.7227C 13.4093,51.6374 13.3347,49.9507 12.2493,48.9561 Z ";
		}
	}

	public bool IsNotActive => !IsActive;

	public string StatusText
	{
		get
		{
			if (!IsActive)
			{
				return ResourceHelper.GetString("upload_failed");
			}
			return ResourceHelper.GetString("uploading");
		}
	}

	public string VideoPath { get; set; }

	public string ThumbPath { get; set; }

	public string ChannelId { get; set; }

	public string Text { get; set; }

	public List<Entity> Entities { get; set; }

	public bool PostToTwitter { get; set; }

	public bool PostToFb { get; set; }

	public bool IsMessage { get; set; }

	public List<string> UserIds { get; set; }

	public List<string> PhoneNumbers { get; set; }

	public List<string> Emails { get; set; }

	public string Message { get; set; }

	public string Locale { get; set; }

	public int? MaxLoops { get; set; }

	public string PostId { get; set; }

	public RecordingVineModel Vine { get; set; }

	public string ReplyConversationId { get; set; }

	public Task DeleteAsync()
	{
		return Vine.DeleteAsync();
	}

	public async Task<ApiResult> Execute()
	{
		DispatcherEx.BeginInvoke(delegate
		{
			ErrorMessage = null;
			RequiresValidation = false;
			IsActive = true;
			Progress = 0.0;
		});
		ApiResult result = new ErrorApiResult();
		try
		{
			Stream stream = await ((IStorageFile)(object)(await StorageFile.GetFileFromPathAsync(VideoPath))).OpenStreamForReadAsync();
			_stageComplete = 0.0;
			_progressStageWeight = 0.75;
			ApiResult<string> apiResult = (ApiResult<string>)(result = await App.Api.UploadVideoMp4(stream, this));
			string videoUploadKey = null;
			if (!apiResult.HasError)
			{
				videoUploadKey = apiResult.XUploadKey;
			}
			if (!apiResult.HasError)
			{
				Stream stream2 = ((IRandomAccessStream)(object)(await (await StorageFile.GetFileFromPathAsync(ThumbPath)).OpenReadAsync())).AsStream();
				_stageComplete = Progress;
				_progressStageWeight = 0.2;
				ApiResult<string> apiResult2 = (ApiResult<string>)(result = await App.Api.UploadVideoThumbnail(stream2, this));
				string thumbUploadKey = null;
				if (!apiResult2.HasError)
				{
					thumbUploadKey = apiResult2.XUploadKey;
				}
				if (!apiResult2.HasError)
				{
					_stageComplete = Progress;
					_progressStageWeight = 0.05;
					ApiResult submitResult = default(ApiResult);
					if (IsMessage)
					{
						if (ReplyConversationId == null)
						{
							_ = submitResult;
							submitResult = await App.Api.PostConversation(UserIds, PhoneNumbers, Emails, Message, videoUploadKey, thumbUploadKey, PostId, MaxLoops, Locale);
						}
						else
						{
							_ = submitResult;
							submitResult = await App.Api.ReplyToConversation(ReplyConversationId, Message, videoUploadKey, thumbUploadKey);
						}
						if (PhoneNumbers != null && PhoneNumbers.Any() && ((ApiResult<BaseVineResponseModel<VineMessageResponseMetaModel>>)submitResult).Model.Data.Messages.Any() && !string.IsNullOrEmpty(((ApiResult<BaseVineResponseModel<VineMessageResponseMetaModel>>)submitResult).Model.Data.Messages[0].ShareUrl))
						{
							await DispatcherEx.BeginInvoke(async delegate
							{
								ChatMessage val = new ChatMessage();
								val.put_Body(string.Format("{0}{1}", new object[2]
								{
									string.Format(ResourceHelper.GetString("sms_text"), new object[1] { ((ApiResult<BaseVineResponseModel<VineMessageResponseMetaModel>>)submitResult).Model.Data.Messages[0].ShareUrl }),
									ResourceHelper.GetString("sms_footer")
								}));
								ChatMessage val2 = val;
								foreach (string phoneNumber in PhoneNumbers)
								{
									val2.Recipients.Add(phoneNumber);
								}
								await ReservedEx.SendSmsMessage(val2);
							});
						}
					}
					else
					{
						_ = submitResult;
						submitResult = await App.Api.UploadVine(videoUploadKey, thumbUploadKey, Text, ChannelId, PostToTwitter, PostToFb, Entities);
					}
					result = submitResult;
					Report(new HttpProgress
					{
						BytesSent = 1uL,
						TotalBytesToSend = 1uL
					});
					if (!submitResult.HasError)
					{
						if (!IsMessage)
						{
							await Vine.SaveToCameraRollAsync();
						}
						RecordingVineModel recordingVineModel = (await ApplicationSettings.Current.GetRecordingDrafts()).FirstOrDefault((RecordingVineModel x) => x.DraftId == Vine.DraftId);
						if (recordingVineModel != null)
						{
							await recordingVineModel.DeleteAsync();
						}
						await Vine.DeleteAsync();
					}
				}
			}
		}
		catch (Exception)
		{
			DispatcherEx.BeginInvoke(delegate
			{
				result.PopUpErrorIfExists();
			});
		}
		DispatcherEx.BeginInvoke(delegate
		{
			IsActive = false;
		});
		return result;
	}

	public void Report(HttpProgress value)
	{
		//IL_000d: Unknown result type (might be due to invalid IL or missing references)
		//IL_000e: Unknown result type (might be due to invalid IL or missing references)
		DispatcherEx.BeginInvoke(delegate
		{
			if (value.TotalBytesToSend.HasValue)
			{
				Progress = _stageComplete + _progressStageWeight * ((double)value.BytesSent / (double)value.TotalBytesToSend.Value) * 100.0;
			}
		});
	}
}
