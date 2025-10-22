using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using GalaSoft.MvvmLight.Messaging;
using Gen.Services;
using Vine.Datas;
using Vine.Pages.Main.ViewModels;
using Vine.Services;
using Vine.Services.Models;

namespace Vine.Pages.Profile.ViewModels;

public class ProfileViewModel : INotifyPropertyChanged
{
	public List<IPostRecord> Posts { get; set; }

	public bool IsLoading { get; set; }

	public bool IsLoadingPost { get; set; }

	public IProfile Profile { get; set; }

	public string NextPage { get; set; }

	public string UserId { get; set; }

	public bool IsProtected { get; set; }

	public List<IPostRecord> AllPosts { get; private set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public ProfileViewModel()
	{
		Messenger.Default.Register(this, delegate(NotificationMessage<string> message)
		{
			if (message.Notification == "RemovePost")
			{
				string postid = message.Content;
				if (Posts != null)
				{
					IPostRecord postRecord = Posts.FirstOrDefault((IPostRecord f) => f.PostId == postid);
					if (postRecord != null)
					{
						Posts.Remove(postRecord);
					}
				}
			}
		});
		Messenger.Default.Register(this, delegate(NotificationMessage<IProfile> message)
		{
			if (message.Notification == "ProfileUpdated")
			{
				IProfile content = message.Content;
				if (content.Id == UserId)
				{
					Profile = content;
					RaisePropertyChanged("Profile");
				}
			}
		});
	}

	public async Task LoadProfile(string id, Action callback, bool force = false)
	{
		if (!force && Profile != null)
		{
			return;
		}
		UserId = id;
		IsLoading = true;
		RaisePropertyChanged("IsLoading");
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		if (currentUser == null)
		{
			return;
		}
		try
		{
			IProfile profile = await currentUser.Service.GetProfilInfoAsync(id);
			IsLoading = false;
			Profile = profile;
			IsProtected = DatasProvider.Instance.CurrentUser.User.Id != id && profile.Following.HasValue && !profile.Following.Value && profile.Private;
			RaisePropertyChanged("IsProtected");
			RaisePropertyChanged("Profile");
			RaisePropertyChanged("IsLoading");
			callback?.Invoke();
		}
		catch (ServiceServerErrorException ex)
		{
			IsLoading = false;
			ToastHelper.Show(ex.HttpErrorMessage, afternav: false, (Orientation)0);
			RaisePropertyChanged("IsLoading");
		}
		IsLoadingPost = true;
		RaisePropertyChanged("IsLoadingPost");
		try
		{
			IListPosts listPosts = await currentUser.Service.TimelinesAsync(UriServiceProvider.GetUriForUser(id), null, 15);
			if (listPosts != null)
			{
				if (string.IsNullOrEmpty(listPosts.NextPage))
				{
					Posts = listPosts.Posts;
				}
				else
				{
					List<IPostRecord> list = listPosts.Posts.Take(17).ToList();
					list.Add(new MorePost());
					Posts = list;
				}
				AllPosts = listPosts.Posts;
				NextPage = listPosts.NextPage;
				IsLoadingPost = false;
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					RaisePropertyChanged("Posts");
					RaisePropertyChanged("HasMore");
					RaisePropertyChanged("IsLoadingPost");
				});
			}
		}
		catch (ServiceServerErrorException ex2)
		{
			ServiceServerErrorException ex3 = ex2;
			ServiceServerErrorException ex4 = ex3;
			IsLoadingPost = false;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				if (ex4.HttpErrorMessage != null && !ex4.HttpErrorMessage.StartsWith("Not authorized"))
				{
					ToastHelper.Show(ex4.HttpErrorMessage, afternav: false, (Orientation)0);
				}
				RaisePropertyChanged("IsLoadingPost");
			});
		}
	}

	public void RaisePropertyChanged(string name)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(name));
		}
	}

	internal void Refresh(Action callback = null, bool forced = false)
	{
		LoadProfile(UserId, callback, forced);
	}
}
