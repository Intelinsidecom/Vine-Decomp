using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Collections.Specialized;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading;
using System.Threading.Tasks;
using Vine.Common;
using Vine.Events;
using Vine.Framework;
using Windows.ApplicationModel;
using Windows.System.Profile;
using Windows.Storage;
using Windows.Storage.Streams;

namespace Vine.Models;

public class ApplicationSettings : ApplicationSettingsBase
{
	private static ApplicationSettings _current;

	private string _deviceId;

	private string _clientVersion;

	private ObservableCollection<VineCacheModel> _vineCacheModel;

	private SemaphoreSlim _mutex = new SemaphoreSlim(1);

	public static ApplicationSettings Current => _current ?? (_current = new ApplicationSettings());

	public string NotificationChannelUri
	{
		get
		{
			return GetValue<string>("NotificationChannelUri", null);
		}
		set
		{
			SetValue("NotificationChannelUri", value);
		}
	}

	public bool IsVolumeMuted
	{
		get
		{
			return GetValue("IsVolumeMuted", defaultObj: false);
		}
		set
		{
			SetValue("IsVolumeMuted", value);
			EventAggregator.Current.Publish(new MuteSettingChanged());
		}
	}

	public string UnhandledException
	{
		get
		{
			return GetValue<string>("UnhandledException", null);
		}
		set
		{
			SetValue("UnhandledException", value);
		}
	}

	public bool IsTwitterEnabled
	{
		get
		{
			return User?.TwitterConnected ?? false;
		}
		set
		{
			VineUserModel user = User;
			user.TwitterConnected = value;
			User = user;
		}
	}

	public bool IsFacebookEnabled
	{
		get
		{
			return User?.FacebookConnected ?? false;
		}
		set
		{
			VineUserModel user = User;
			user.FacebookConnected = value;
			User = user;
		}
	}

	public bool IsTwitterOn
	{
		get
		{
			return GetValue("IsTwitterOn", defaultObj: false);
		}
		set
		{
			SetValue("IsTwitterOn", value);
		}
	}

	public bool IsFacebookOn
	{
		get
		{
			return GetValue("IsFacebookOn", defaultObj: false);
		}
		set
		{
			SetValue("IsFacebookOn", value);
		}
	}

	public bool IsEmailVerified
	{
		get
		{
			return User.VerifiedEmail;
		}
		set
		{
			VineUserModel user = User;
			user.VerifiedEmail = value;
			User = user;
		}
	}

	public bool IsPhoneVerified
	{
		get
		{
			return User.VerifiedPhoneNumber;
		}
		set
		{
			VineUserModel user = User;
			user.VerifiedPhoneNumber = value;
			User = user;
		}
	}

	public bool IsAddressBookEnabled
	{
		get
		{
			return GetValue("IsAddressBookEnabled", defaultObj: false);
		}
		set
		{
			SetValue("IsAddressBookEnabled", value);
		}
	}

	public bool HasSeenCaptureTutorial
	{
		get
		{
			return GetValue("HasSeenCaptureTutorial", defaultObj: false);
		}
		set
		{
			SetValue("HasSeenCaptureTutorial", value);
		}
	}

	public bool HasSeenVMWelcomeTutorial
	{
		get
		{
			return GetValue("HasSeenVMWelcomeTutorial", defaultObj: false);
		}
		set
		{
			SetValue("HasSeenVMWelcomeTutorial", value);
		}
	}

	public bool HasSeenVMCreateHint
	{
		get
		{
			return GetValue("HasSeenVMCreateHint", defaultObj: false);
		}
		set
		{
			SetValue("HasSeenVMCreateHint", value);
		}
	}

	public bool HasSeenEmptyPostsHint
	{
		get
		{
			return GetValue("HasSeenEmptyPostsHint", defaultObj: false);
		}
		set
		{
			SetValue("HasSeenEmptyPostsHint", value);
		}
	}

	public bool HasSeenVMReplyHint
	{
		get
		{
			return GetValue("HasSeenVMReplyHint", defaultObj: false);
		}
		set
		{
			SetValue("HasSeenVMReplyHint", value);
		}
	}

	public bool HasSeenShareMessagePage
	{
		get
		{
			return GetValue("HasSeenShareMessagePage", defaultObj: false);
		}
		set
		{
			SetValue("HasSeenShareMessagePage", value);
		}
	}

	public int ButtonsTutorialState
	{
		get
		{
			return GetValue("ButtonsTutorialState", 0);
		}
		set
		{
			SetValue("ButtonsTutorialState", value);
		}
	}

	public long VineCacheSize
	{
		get
		{
			return GetValue("VineCacheSize", 0L);
		}
		set
		{
			SetValue("VineCacheSize", value);
		}
	}

	public DateTime LastUpdatedTile
	{
		get
		{
			return new DateTime(GetValue("LastUpdatedTile", DateTime.MinValue.Ticks), DateTimeKind.Utc);
		}
		set
		{
			long ticks = value.ToUniversalTime().Ticks;
			SetValue("LastUpdatedTile", ticks);
		}
	}

	public string UserId => VineSession?.UserId;

	public FbUserModel FbSession
	{
		get
		{
			return GetSecureObject<FbUserModel>("FbSession", null);
		}
		set
		{
			SetSecureObject("FbSession", value);
		}
	}

	public VineAuthToken VineSession
	{
		get
		{
			return GetSecureObject<VineAuthToken>("VineSession", null);
		}
		set
		{
			SetSecureObject("VineSession", value);
		}
	}

	public VineUserModel User
	{
		get
		{
			return GetSecureObject<VineUserModel>("User", null);
		}
		set
		{
			SetSecureObject("User", value);
		}
	}

	public TimeSpan? ServerOffset => null;

	public bool IsNotLoggedIn => VineSession == null;

	public string DeviceId
	{
		get
		{
			if (_deviceId == null)
			{
				byte[] inArray = HardwareIdentification.GetPackageSpecificToken((IBuffer)null).Id.ToArray();
				_deviceId = Convert.ToBase64String(inArray);
			}
			return _deviceId;
		}
	}

	public string ClientVersion
	{
		get
		{
			//IL_0012: Unknown result type (might be due to invalid IL or missing references)
			//IL_0017: Unknown result type (might be due to invalid IL or missing references)
			//IL_0026: Unknown result type (might be due to invalid IL or missing references)
			//IL_0034: Unknown result type (might be due to invalid IL or missing references)
			//IL_0042: Unknown result type (might be due to invalid IL or missing references)
			//IL_0050: Unknown result type (might be due to invalid IL or missing references)
			if (_clientVersion == null)
			{
				PackageVersion version = Package.Current.Id.Version;
				_clientVersion = $"{version.Major}.{version.Minor}.{version.Build}.{version.Revision}";
			}
			return _clientVersion;
		}
	}

	public string OSVersion
	{
		get
		{
			return GetValue("OSVersion", string.Empty);
		}
		set
		{
			SetValue("OSVersion", value);
		}
	}

	public string MCC
	{
		get
		{
			return GetValue("MCC", string.Empty);
		}
		set
		{
			SetValue("MCC", value);
		}
	}

	public string MNC
	{
		get
		{
			return GetValue("MNC", string.Empty);
		}
		set
		{
			SetValue("MNC", value);
		}
	}

	~ApplicationSettings()
	{
		_mutex.Dispose();
		_vineCacheModel.CollectionChanged -= VineCacheModel_CollectionChanged;
	}

	public Task<RecordingVineModel> GetRecordingActiveVine()
	{
		return GetFileObject<RecordingVineModel>("RecordingActiveVine", null);
	}

	public Task SetRecordingActiveVine(RecordingVineModel value)
	{
		return SetFileObject("RecordingActiveVine", value);
	}

	public Task<List<RecordingVineModel>> GetRecordingDrafts()
	{
		return GetFileObject("RecordingDrafts", new List<RecordingVineModel>());
	}

	public Task SetRecordingDrafts(List<RecordingVineModel> value)
	{
		return SetFileObject("RecordingDrafts", value);
	}

	public Task<List<UploadJob>> GetUploadJobs()
	{
		return GetFileObject("UploadJobs", new List<UploadJob>());
	}

	public Task SetUploadJobs(List<UploadJob> value)
	{
		return SetFileObject("UploadJobs", value);
	}

	public Task<List<VineRecentSearch>> GetRecentSearches()
	{
		return GetFileObject("RecentSearches", new List<VineRecentSearch>());
	}

	public Task SetRecentSearches(List<VineRecentSearch> value)
	{
		return SetFileObject("RecentSearches", value);
	}

	public async Task<Uri> SetVineCacheFile(string vineId, Stream vineStream)
	{
		StorageFolder folder = await FolderHelper.GetCachedVinesFolderAsync();
		StorageFile windowsRuntimeFile = await folder.CreateFileAsync(vineId, (CreationCollisionOption)1);
		long vineSize = 0L;
		using (Stream outputStream = await ((IStorageFile)(object)windowsRuntimeFile).OpenStreamForWriteAsync())
		{
			await vineStream.CopyToAsync(outputStream);
			vineStream.Dispose();
			vineSize = outputStream.Length;
		}
		(await GetCacheList()).Add(new VineCacheModel
		{
			PostId = vineId,
			TimeCached = DateTime.UtcNow.Ticks,
			Bytes = vineSize
		});
		return new Uri(Path.Combine(new string[2] { folder.Path, vineId }));
	}

	private async Task<ObservableCollection<VineCacheModel>> GetCacheList()
	{
		if (_vineCacheModel == null)
		{
			_vineCacheModel = new ObservableCollection<VineCacheModel>((IEnumerable<VineCacheModel>)(await GetFileObject("CachedVinesList", new List<VineCacheModel>())));
			_vineCacheModel.CollectionChanged += VineCacheModel_CollectionChanged;
		}
		return _vineCacheModel;
	}

	private async Task SaveCacheList(List<VineCacheModel> vineCache)
	{
		try
		{
			StorageFile obj = await (await FolderHelper.GetCachedVinesFolderAsync()).CreateFileAsync("CachedVinesList", (CreationCollisionOption)1);
			string text = Serialization.Serialize(vineCache);
			await FileIO.WriteTextAsync((IStorageFile)(object)obj, text);
		}
		catch
		{
		}
	}

	private async void VineCacheModel_CollectionChanged(object sender, NotifyCollectionChangedEventArgs e)
	{
		if (e.Action != NotifyCollectionChangedAction.Add)
		{
			return;
		}
		await _mutex.WaitAsync();
		VineCacheModel[] source = (from VineCacheModel i in e.NewItems
			where i != null
			select i).ToArray();
		long cacheSize = VineCacheSize;
		cacheSize += source.Sum((VineCacheModel i) => i.Bytes);
		ObservableCollection<VineCacheModel> list = sender as ObservableCollection<VineCacheModel>;
		bool hasError = false;
		try
		{
			if (list.Count > 100)
			{
				VineCacheModel toDelete = list[0];
				StorageFolder folder = await FolderHelper.GetCachedVinesFolderAsync();
				try
				{
					StorageFile val = await folder.GetFileAsync(toDelete.PostId);
					if (val != null)
					{
						await val.DeleteAsync();
					}
					cacheSize -= toDelete.Bytes;
					list.RemoveAt(0);
				}
				catch (FileNotFoundException)
				{
					list.RemoveAt(0);
					hasError = true;
					Debugger.Break();
				}
				catch (NullReferenceException)
				{
					list.RemoveAt(0);
					hasError = true;
					Debugger.Break();
				}
				catch (Exception)
				{
					hasError = true;
					Debugger.Break();
				}
				if (hasError)
				{
					cacheSize = await FolderHelper.GetFolderSize(folder);
				}
			}
			VineCacheSize = cacheSize;
			await SetFileObject("CachedVinesList", list.ToList());
		}
		finally
		{
			_mutex.Release();
		}
	}

	public async Task<Uri> GetVineCacheFilepath(string vineId)
	{
		try
		{
			if ((await GetCacheList()).FirstOrDefault((VineCacheModel v) => v != null && v.PostId == vineId) != null)
			{
				return new Uri(Path.Combine(new string[3]
				{
					ApplicationData.Current.LocalFolder.Path,
					"vineCache",
					vineId
				}));
			}
		}
		catch (Exception)
		{
		}
		return null;
	}
}
