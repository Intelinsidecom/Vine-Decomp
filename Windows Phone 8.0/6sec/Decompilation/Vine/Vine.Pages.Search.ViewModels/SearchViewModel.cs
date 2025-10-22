using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using Gen.Services;
using Vine.Datas;
using Vine.Pages.Main.ViewModels;
using Vine.Services;
using Vine.Services.Models;

namespace Vine.Pages.Search.ViewModels;

public class SearchViewModel : INotifyPropertyChanged
{
	public bool LoadingPopular { get; set; }

	public List<IPostRecord> PopularPosts { get; set; }

	public ObservableCollection<ITag> ResultsTags { get; set; }

	public ObservableCollection<IPerson> ResultsUsers { get; set; }

	public string Search { get; set; }

	public bool IsLoading { get; set; }

	public string NextPageTags { get; set; }

	public string NextPageUsers { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public SearchViewModel()
	{
		ResultsTags = new ObservableCollection<ITag>();
		ResultsUsers = new ObservableCollection<IPerson>();
	}

	protected async Task InitSuggestions()
	{
	}

	public async Task LoadPopulars()
	{
		LoadingPopular = true;
		RaisePropertyChanged("LoadingPopular");
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		if (currentUser == null)
		{
			return;
		}
		try
		{
			PopularPosts = (await currentUser.Service.TimelinesAsync(UriServiceProvider.GetUriPopular(), null, 14)).Posts.Take(14).ToList();
			PopularPosts.Add(new MorePost());
			LoadingPopular = false;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				RaisePropertyChanged("LoadingPopular");
				RaisePropertyChanged("PopularPosts");
			});
		}
		catch (ServiceServerErrorException)
		{
		}
	}

	internal void SearchTags(string search, bool clearothers = false)
	{
		search = search.TrimStart('#');
		if (search.Length >= 3)
		{
			Search = search;
			NextPageTags = "1";
			if (clearothers)
			{
				ResultsUsers.Clear();
				NextPageTags = "1";
			}
			LoadMoreTags(clear: true);
		}
	}

	public async Task LoadMoreTags(bool clear)
	{
		if (string.IsNullOrEmpty(NextPageTags))
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
			IListTags listTags = await currentUser.Service.SearchTagAsync(Search, NextPageTags);
			if (listTags == null || listTags.Tags == null)
			{
				return;
			}
			NextPageTags = listTags.NextPage;
			if (clear)
			{
				ResultsTags.Clear();
			}
			foreach (ITag tag in listTags.Tags)
			{
				ResultsTags.Add(tag);
			}
		}
		catch
		{
			if (clear)
			{
				ResultsTags.Clear();
			}
		}
		finally
		{
			IsLoading = false;
			RaisePropertyChanged("IsLoading");
		}
	}

	public async Task LoadMoreUsers(string search, bool clear)
	{
		if (string.IsNullOrEmpty(NextPageUsers))
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
			IListPersons listPersons = await currentUser.Service.SearchUserAsync(search, NextPageUsers);
			if (search != Search)
			{
				return;
			}
			if (clear)
			{
				ResultsUsers.Clear();
			}
			if (listPersons == null || listPersons.Persons == null)
			{
				return;
			}
			NextPageUsers = listPersons.NextPage;
			foreach (IPerson record in listPersons.Persons)
			{
				if (!ResultsUsers.Any((IPerson d) => d.Id == record.Id))
				{
					ResultsUsers.Add(record);
				}
			}
		}
		catch
		{
			if (clear)
			{
				ResultsUsers.Clear();
			}
		}
		finally
		{
			IsLoading = false;
			RaisePropertyChanged("IsLoading");
		}
	}

	internal void SearchUsers(string search, bool clearothers = false)
	{
		Search = search;
		NextPageUsers = "1";
		if (clearothers)
		{
			ResultsTags.Clear();
			NextPageTags = "1";
		}
		if (search.Length >= 3)
		{
			LoadMoreUsers(search, clear: true);
		}
	}

	private void RaisePropertyChanged(string p)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(p));
		}
	}

	internal void RefreshPopular()
	{
		LoadPopulars();
	}
}
