using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.IO;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Runtime.Serialization;
using System.Threading.Tasks;
using System.Windows.Media.Imaging;
using Microsoft.Phone.BackgroundTransfer;
using Vine.Services;
using Vine.Services.Models;
using Vine.Services.Response.Auth;
using Windows.Storage;

namespace Vine.Datas;

public class Datas : INotifyPropertyChanged
{
	private DataUser _currentuser;

	private DataUser _Primaryuser;

	private bool? _autoPlayComputed;

	internal static AsyncLock asyncLock = new AsyncLock();

	public DataUser CurrentUserInValidation { get; set; }

	public int ModernColor { get; set; }

	public bool UseMyAccentColourForLiveTile { get; set; }

	[IgnoreDataMember]
	public bool AddStripComputed
	{
		get
		{
			if (ModernColor != 2)
			{
				return !RemoveStripes;
			}
			return false;
		}
	}

	public bool UseFullScreenPost { get; set; }

	public bool RemoveStripes { get; set; }

	public bool UseProgressBar { get; set; }

	public string ServerUri { get; set; }

	public ObservableCollection<EncodingJob> Encodings { get; set; }

	public bool AuthUsingTwitter { get; set; }

	public bool AutoPlay { get; set; }

	public bool DisplayedTutorial { get; set; }

	public bool AdGoBegin { get; set; }

	public int LastMessageNbr { get; set; }

	public bool DisableReview { get; set; }

	public bool HAU { get; set; }

	public bool DisplayedTutorialCamera { get; set; }

	public int NbrLaunch { get; set; }

	public bool HasEvaluate { get; set; }

	public string CurrentUserId { get; set; }

	[IgnoreDataMember]
	public DataUser CurrentUser
	{
		get
		{
			if (_currentuser == null)
			{
				_currentuser = Users.FirstOrDefault((DataUser u) => u.User.Id == CurrentUserId);
				if (_currentuser == null)
				{
					_currentuser = Users.FirstOrDefault();
				}
			}
			return _currentuser;
		}
	}

	public string PrimaryUserId { get; set; }

	[IgnoreDataMember]
	public DataUser PrimaryUser
	{
		get
		{
			if (_Primaryuser == null)
			{
				_Primaryuser = Users.FirstOrDefault((DataUser u) => u.User.Id == PrimaryUserId);
				if (_Primaryuser == null)
				{
					_Primaryuser = Users.FirstOrDefault();
				}
			}
			return _Primaryuser;
		}
	}

	[IgnoreDataMember]
	public bool AutoPlayComputed
	{
		get
		{
			return _autoPlayComputed ?? AutoPlay;
		}
		set
		{
			_autoPlayComputed = value;
		}
	}

	public bool GeoPositionAsked { get; set; }

	public DateTime ToastNightFrom { get; set; }

	public DateTime ToastNightTo { get; set; }

	public bool ToastDisableNight { get; set; }

	public bool DisableDoubleTap { get; set; }

	public List<DataUser> Users { get; set; }

	public bool UsePivotHome { get; set; }

	public List<CustomAd> CustomAds { get; set; }

	public double LastLatitude { get; set; }

	public double LastLongitude { get; set; }

	public int CaptureOrientation { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public Datas()
	{
		UseFullScreenPost = true;
		ServerUri = "https://huynapps.com/6Sec/service/";
		Encodings = new ObservableCollection<EncodingJob>();
		ToastNightFrom = new DateTime(1983, 8, 19, 22, 0, 0);
		ToastNightTo = new DateTime(1983, 8, 19, 7, 0, 0);
		ToastDisableNight = false;
		DisableDoubleTap = false;
		AutoPlay = true;
		Users = new List<DataUser>();
		UsePivotHome = false;
		UseMyAccentColourForLiveTile = true;
		RemoveStripes = true;
	}

	public void ReinitUser()
	{
		_currentuser = null;
	}

	public void CreateNewUser(string login, string password, TwitterAccess twitter, AuthData data)
	{
		DataUser dataUser = Users.FirstOrDefault((DataUser u) => u.User.Id == data.Id);
		DataUser dataUser2;
		if (dataUser != null)
		{
			if (password != null)
			{
				dataUser.Password = password;
			}
			dataUser.User = data;
			if (twitter != null)
			{
				dataUser.TwitterAccess = twitter;
			}
			dataUser2 = dataUser;
		}
		else
		{
			dataUser2 = new DataUser
			{
				Email = login,
				Password = password,
				TwitterAccess = twitter,
				User = data
			};
			dataUser2.Init();
			Users.Add(dataUser2);
		}
		CurrentUserId = data.Id;
		_currentuser = dataUser2;
		dataUser2.LastLogin = DateTime.Now;
	}

	public void ChangePrimaryUser(string id)
	{
		PrimaryUserId = id;
		_Primaryuser = null;
	}

	public void RaisePropertyChanged(string name)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(name));
		}
	}

	public void RemoveUser(DataUser user = null)
	{
		if (user == null)
		{
			if (CurrentUser != null)
			{
				Users.Remove(CurrentUser);
			}
			_currentuser = null;
			return;
		}
		Users.Remove(user);
		if (_currentuser == user)
		{
			_currentuser = null;
		}
		if (_Primaryuser == user)
		{
			_Primaryuser = null;
		}
	}

	public async Task<EncodingJob> CreateEncodingJob(double duration)
	{
		EncodingJob job = new EncodingJob
		{
			UserId = CurrentUser.User.Id,
			Duration = duration,
			Id = DateTime.Now.Ticks.ToString(),
			Date = DateTime.Now
		};
		try
		{
			StorageFile windowsRuntimeFile = await ApplicationData.Current.TemporaryFolder.GetFileAsync("currentimagevideo.jpg");
			string text = "/shared/transfers/" + job.Id + "_capture.jpg";
			Stream stream = null;
			using (Stream source = ((IStorageFile)(object)windowsRuntimeFile).OpenStreamForReadAsync().Result)
			{
				BitmapImage val = new BitmapImage();
				((BitmapSource)val).SetSource(source);
				WriteableBitmap val2 = new WriteableBitmap((BitmapSource)val);
				MemoryStream memoryStream = new MemoryStream();
				Extensions.SaveJpeg(val2, (Stream)memoryStream, 480, 480, 0, 95);
			}
			using (IsolatedStorageFile isolatedStorageFile = IsolatedStorageFile.GetUserStoreForApplication())
			{
				using IsolatedStorageFileStream destination = isolatedStorageFile.OpenFile(text, FileMode.Create);
				stream.Position = 0L;
				stream.CopyTo(destination);
			}
			job.LocalImagePath = text;
		}
		catch
		{
		}
		Encodings.Insert(0, job);
		Save();
		return job;
	}

	public void RemoveJob(EncodingJob _currentJob)
	{
		if (_currentJob == null)
		{
			return;
		}
		Encodings.Remove(_currentJob);
		if (_currentJob.RemoteMyServerJobId != null)
		{
			OwnService.RemoveVideo(_currentJob.RemoteMyServerJobId);
		}
		if (_currentJob.CurrentRequestId == null)
		{
			return;
		}
		BackgroundTransferRequest val = BackgroundTransferService.Find(_currentJob.CurrentRequestId);
		if (val == null)
		{
			return;
		}
		try
		{
			if (BackgroundTransferService.Requests.Contains(val))
			{
				BackgroundTransferService.Remove(val);
			}
		}
		catch
		{
		}
	}

	public void RemoveAllJobs()
	{
		foreach (EncodingJob item in Encodings.ToList())
		{
			RemoveJob(item);
		}
		foreach (BackgroundTransferRequest item2 in BackgroundTransferService.Requests.ToList())
		{
			try
			{
				BackgroundTransferService.Remove(item2);
			}
			catch
			{
			}
		}
	}

	public Stream GetStream()
	{
		MemoryStream memoryStream = new MemoryStream();
		new DataContractSerializer(typeof(Datas)).WriteObject(memoryStream, this);
		memoryStream.Position = 0L;
		return memoryStream;
	}

	public async Task Save()
	{
		using (await asyncLock.LockAsync())
		{
			_ = 3;
			try
			{
				Stream stream = GetStream();
				using Stream destination = await ((IStorageFile)(object)(await ApplicationData.Current.LocalFolder.CreateFileAsync("6secdatas.dat", (CreationCollisionOption)1))).OpenStreamForWriteAsync();
				stream.CopyTo(destination);
				stream.Flush();
			}
			catch (Exception)
			{
			}
		}
	}

	internal void Init()
	{
		if (Users != null)
		{
			foreach (DataUser user in Users)
			{
				user.Init();
			}
		}
		foreach (EncodingJob item in Encodings.Where((EncodingJob e) => e.State == EncodingStep.NONE).ToList())
		{
			Encodings.Remove(item);
		}
	}
}
