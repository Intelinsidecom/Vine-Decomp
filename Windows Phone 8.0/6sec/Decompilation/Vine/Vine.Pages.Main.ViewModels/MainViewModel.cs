using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using GalaSoft.MvvmLight.Messaging;
using Gen.Services;
using Vine.Datas;
using Vine.Resources;
using Vine.Services;
using Vine.Services.Models;
using Vine.Services.Response;
using Vine.Utils;
using Windows.Devices.Geolocation;
using Windows.Foundation;

namespace Vine.Pages.Main.ViewModels;

public class MainViewModel : INotifyPropertyChanged
{
	private string _nextPage;

	internal static double? latitude;

	internal static double? longitude;

	private Action<IListPosts> callback;

	private Action<ServiceServerErrorException> failcallback;

	private string nextpage;

	private bool _alreadyLoginRequired;

	public bool NoFeed { get; set; }

	public string LoadingMessage { get; set; }

	public ObservableCollection<IPostRecord> Posts { get; set; }

	public ObservableCollection<IPostRecord> PostsGrouped { get; set; }

	public bool IsLoading { get; set; }

	public string FeedType { get; set; }

	public Vine.Datas.Datas Data { get; set; }

	public string Sort { get; private set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public MainViewModel()
	{
		Data = DatasProvider.Instance;
		Posts = new ObservableCollection<IPostRecord>();
		_nextPage = "";
		LoadingMessage = AppResources.LoadingPosts;
		FeedType = UriServiceProvider.GetUriForWall();
		PostsGrouped = new ObservableCollection<IPostRecord>();
		Messenger.Default.Register(this, delegate(NotificationMessage<string> message)
		{
			if (message.Notification == "RemovePost")
			{
				string postid = message.Content;
				IPostRecord postRecord = Posts.FirstOrDefault((IPostRecord f) => f.PostId == postid);
				if (postRecord != null)
				{
					Posts.Remove(postRecord);
				}
			}
		});
	}

	public async Task GetData(string type, string nextpage, int nbrpost, Action<IListPosts> callback, Action<ServiceServerErrorException> failcallback)
	{
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		if (currentUser == null)
		{
			return;
		}
		try
		{
			callback(await currentUser.Service.TimelinesAsync(type, nextpage, 20, Sort));
		}
		catch (ServiceServerErrorException obj)
		{
			failcallback(obj);
		}
	}

	private async void geo_StatusChanged(Geolocator geoloc, StatusChangedEventArgs args)
	{
		if ((int)args.Status == 0)
		{
			WindowsRuntimeMarshal.RemoveEventHandler<TypedEventHandler<Geolocator, StatusChangedEventArgs>>((Action<EventRegistrationToken>)geoloc.remove_StatusChanged, (TypedEventHandler<Geolocator, StatusChangedEventArgs>)geo_StatusChanged);
			Geoposition val = await geoloc.GetGeopositionAsync(TimeSpan.FromMinutes(3.0), TimeSpan.FromSeconds(15.0));
			latitude = val.Coordinate.Latitude;
			longitude = val.Coordinate.Longitude;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				LaunchNearby(nextpage, callback, failcallback);
			});
		}
	}

	public async Task LaunchNearby(string nextpage, Action<IListPosts> callback, Action<ServiceServerErrorException> failcallback)
	{
	}

	public void LoadData(bool clearafter = false, string forcednextpage = null, Action<IListPosts> callback = null)
	{
		NoFeed = false;
		RaisePropertyChanged("NoFeed");
		IsLoading = true;
		RaisePropertyChanged("IsLoading");
		if (!clearafter)
		{
			if (forcednextpage != null)
			{
				_nextPage = forcednextpage;
			}
		}
		else
		{
			_nextPage = "0";
		}
		if (_nextPage == null)
		{
			return;
		}
		Vine.Datas.Datas instance = DatasProvider.Instance;
		DateTime now = DateTime.Now;
		if ((now - instance.CurrentUser.LastLogin).TotalHours > 12.0 || instance.CurrentUser.LastLogin > now)
		{
			ReconnectYou(clearafter);
			return;
		}
		GetData(FeedType, _nextPage, 20, delegate(IListPosts res)
		{
			_nextPage = res.NextPage;
			IsLoading = false;
			List<IPostRecord> newposts;
			if (clearafter)
			{
				newposts = res.Posts;
			}
			else
			{
				List<string> ids = Posts.Select((IPostRecord p) => p.PostId).ToList();
				newposts = res.Posts.Where((IPostRecord post) => !ids.Contains(post.PostId)).ToList();
			}
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				if (clearafter)
				{
					Posts.Clear();
					if (PostsGrouped != null)
					{
						PostsGrouped.Clear();
					}
				}
				foreach (IPostRecord item in newposts)
				{
					Posts.Add(item);
				}
				if (callback != null)
				{
					callback(res);
				}
				if (PostsGrouped != null)
				{
					UpdateGroup(newposts);
				}
				RaisePropertyChanged("IsLoading");
			});
		}, delegate(ServiceServerErrorException ex)
		{
			if (ex.StatusCode == 103)
			{
				ReconnectYou(clearafter);
			}
			else if (ex.ReasonError == ServiceServerErrorType.CHECKPOINT)
			{
				ServiceUtils.ManageCheckPoint(ex.Checkpoint);
			}
			else
			{
				_alreadyLoginRequired = false;
				IsLoading = false;
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					if (Posts.Count == 0)
					{
						NoFeed = true;
						RaisePropertyChanged("NoFeed");
					}
					ToastHelper.Show(ex.HttpErrorMessage, afternav: false, (Orientation)0);
					RaisePropertyChanged("IsLoading");
				});
			}
		});
	}

	private async Task ReconnectYou(bool clear)
	{
		Vine.Datas.Datas instance = DatasProvider.Instance;
		if (instance.CurrentUser == null)
		{
			return;
		}
		try
		{
			ReconnectInfo reconnectInfo = await instance.CurrentUser.Service.ReconnectAsync();
			if (reconnectInfo.IsReconnected)
			{
				LoadData(clear);
				return;
			}
			if (reconnectInfo.IsBadUser)
			{
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					NavigationServiceExt.ToLogin(removebackentry: true);
				});
				return;
			}
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				if (Posts.Count == 0)
				{
					NoFeed = true;
					RaisePropertyChanged("NoFeed");
				}
			});
		}
		catch
		{
		}
	}

	public void RaisePropertyChanged(string name)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(name));
		}
	}

	public void Reload(Action<IListPosts> callback = null)
	{
		LoadData(clearafter: true, null, callback);
	}

	public void Reinit(string newtag, string sort = null)
	{
		_nextPage = "0";
		Posts.Clear();
		Sort = sort;
		FeedType = newtag;
	}

	internal void EmptyGroup()
	{
		PostsGrouped.Clear();
	}

	internal void CreateGroup()
	{
		UpdateGroup(Posts);
	}

	internal void UpdateGroup(IEnumerable<IPostRecord> posts)
	{
		IPostRecord postRecord = PostsGrouped.LastOrDefault();
		if (postRecord != null && postRecord is MorePost)
		{
			PostsGrouped.Remove(postRecord);
		}
		List<string> ids = PostsGrouped.Select((IPostRecord p) => p.PostId).ToList();
		Func<IPostRecord, bool> func2 = default(Func<IPostRecord, bool>);
		Func<IPostRecord, bool> func = func2;
		if (func == null)
		{
			func = (func2 = (IPostRecord p) => !ids.Contains(p.PostId));
		}
		foreach (IPostRecord item in posts.Where(func))
		{
			PostsGrouped.Add(item);
		}
		if (_nextPage != null)
		{
			PostsGrouped.Add(new MorePost());
		}
	}

	internal void SetForcedPosts(IEnumerable<IPostRecord> ForcedPosts)
	{
		foreach (IPostRecord ForcedPost in ForcedPosts)
		{
			Posts.Add(ForcedPost);
		}
		CreateGroup();
	}

	internal void SetNextPage(string p)
	{
		_nextPage = p;
	}

	internal bool LoadCachedData()
	{
		try
		{
			DataUser currentUser = DatasProvider.Instance.CurrentUser;
			if (currentUser == null)
			{
				return false;
			}
			IListPosts res = currentUser.Service.GetCachedTimeLine();
			if (res == null || res.Posts == null)
			{
				return false;
			}
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				foreach (IPostRecord post in res.Posts)
				{
					Posts.Add(post);
				}
			});
			return res.Posts.Count > 0;
		}
		catch
		{
		}
		return false;
	}

	internal void SetIsLoading(bool val)
	{
		IsLoading = val;
		RaisePropertyChanged("IsLoading");
	}

	internal void FollowChannel(bool following)
	{
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		if (currentUser != null)
		{
			int length = FeedType.LastIndexOf('/');
			string id = FeedType.Substring(0, length);
			currentUser.Service.FollowChannelAsync(id, following);
		}
	}
}
