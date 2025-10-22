using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using GalaSoft.MvvmLight.Messaging;
using Gen.Services;
using Microsoft.Phone.UserData;
using Vine.Datas;
using Vine.Models;
using Vine.Services;
using Vine.Services.Models;

namespace Vine.Pages.Likes.ViewModels;

public class LikesViewModel : INotifyPropertyChanged
{
	public ObservableCollection<IPerson> Likes { get; set; }

	public bool IsLoading { get; set; }

	public string NextPage { get; set; }

	public string ServiceUri { get; set; }

	public Dictionary<string, string> Params { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public LikesViewModel()
	{
		Likes = new ObservableCollection<IPerson>();
		Messenger.Default.Register(this, delegate(FollowMessage message)
		{
			IPerson like = Likes.FirstOrDefault((IPerson l) => l.Id == message.UserId);
			if (like != null)
			{
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					like.ChangeFollow(message.Follow);
				});
			}
		});
	}

	public void LoadLikes(string id)
	{
		Likes.Clear();
		NextPage = "1";
		ServiceUri = UriServiceProvider.GetUriForLikesFromPost(id);
		LoadMore();
	}

	public void LoadFollowers(string id)
	{
		Likes.Clear();
		NextPage = "1";
		ServiceUri = UriServiceProvider.GetUriForFollowersFromUser(id);
		LoadMore();
	}

	public void LoadFollowing(string id)
	{
		Likes.Clear();
		NextPage = "1";
		ServiceUri = UriServiceProvider.GetUriForFollowingFromUser(id);
		LoadMore();
	}

	public async Task LoadMore()
	{
		if (string.IsNullOrEmpty(NextPage))
		{
			return;
		}
		IsLoading = true;
		RaisePropertyChanged("IsLoading");
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		if (currentUser == null)
		{
			return;
		}
		try
		{
			IListPersons listPersons = await currentUser.Service.GetPagedUsersAsync(ServiceUri, NextPage, Params);
			if (listPersons == null || listPersons.Persons == null)
			{
				return;
			}
			NextPage = listPersons.NextPage;
			List<string> list = Likes.Select((IPerson f) => f.Id).ToList();
			foreach (IPerson person in listPersons.Persons)
			{
				if (person.Id == null || !list.Contains(person.Id))
				{
					Likes.Add(person);
				}
			}
		}
		catch (ServiceServerErrorException)
		{
		}
		finally
		{
			IsLoading = false;
			RaisePropertyChanged("IsLoading");
		}
	}

	private void RaisePropertyChanged(string p)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(p));
		}
	}

	public async void SearchContactFromPhone()
	{
		IsLoading = true;
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			RaisePropertyChanged("IsLoading");
		});
		Contacts val = new Contacts();
		val.SearchCompleted += async delegate(object senderr, ContactsSearchEventArgs er)
		{
			DataUser currentUser = DatasProvider.Instance.CurrentUser;
			if (currentUser == null)
			{
				return;
			}
			try
			{
				IListPersons res = default(IListPersons);
				_ = res;
				res = await currentUser.Service.SuggestedContactsAsync(RegionInfo.CurrentRegion.TwoLetterISORegionName, er.Results);
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					foreach (IPerson person in res.Persons)
					{
						Likes.Add(person);
					}
				});
				IsLoading = false;
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					RaisePropertyChanged("IsLoading");
				});
			}
			catch (ServiceServerErrorException)
			{
				IsLoading = false;
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					RaisePropertyChanged("IsLoading");
				});
			}
		};
		val.SearchAsync(string.Empty, (FilterKind)0, (object)"");
	}

	internal async Task SearchContactFromTwitter()
	{
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		try
		{
			List<VineRecordPerson> res = default(List<VineRecordPerson>);
			_ = res;
			res = await currentUser.Service.SuggestedTwitterAsync(DatasProvider.Instance.CurrentUser.User.Id);
			if (res != null)
			{
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					foreach (VineRecordPerson item in res)
					{
						Likes.Add(item);
					}
				});
			}
			IsLoading = false;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				RaisePropertyChanged("IsLoading");
			});
		}
		catch
		{
			IsLoading = false;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				RaisePropertyChanged("IsLoading");
			});
		}
	}

	internal void AddLikes(List<IPerson> list)
	{
		foreach (IPerson item in list)
		{
			Likes.Add(item);
		}
	}

	internal void LoadFacebookContacts()
	{
	}
}
