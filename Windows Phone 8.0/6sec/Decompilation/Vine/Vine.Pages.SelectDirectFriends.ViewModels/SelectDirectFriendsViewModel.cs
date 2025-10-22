using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using Gen.Services;
using Vine.Datas;
using Vine.Services;
using Vine.Utils;

namespace Vine.Pages.SelectDirectFriends.ViewModels;

public class SelectDirectFriendsViewModel : INotifyPropertyChanged
{
	public List<IPerson> SelectedPerson { get; set; }

	public List<GroupingLayer<string, IPerson>> GroupedDirect { get; set; }

	public ObservableCollection<IPerson> SearchResults { get; set; }

	public bool IsLoading { get; set; }

	public ObservableCollection<IPerson> OtherItems { get; set; }

	public string _search { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public SelectDirectFriendsViewModel()
	{
		SelectedPerson = new List<IPerson>();
		OtherItems = new ObservableCollection<IPerson>();
		if (DatasProvider.Instance.CurrentUser != null && DatasProvider.Instance.CurrentUser.User != null)
		{
			ManageDirectSuggestions();
		}
		SearchResults = new ObservableCollection<IPerson>();
	}

	private async void ManageDirectSuggestions()
	{
		IsLoading = true;
		RaisePropertyChanged("IsLoading");
		VineServiceWithAuth service = DatasProvider.Instance.CurrentUser.Service;
		try
		{
			IListPersons listPersons = await service.GetFollowersAsync();
			List<IPerson> list = new List<IPerson>();
			foreach (IPerson other in OtherItems)
			{
				IPerson person = listPersons.Persons.FirstOrDefault((IPerson o) => o.Id == other.Id);
				if (person != null)
				{
					list.Add(other);
					SelectedPerson.Add(person);
				}
			}
			foreach (IPerson item in list)
			{
				OtherItems.Remove(item);
			}
			GenerateGroupFriends(listPersons.Persons);
		}
		catch
		{
		}
		IsLoading = false;
		RaisePropertyChanged("IsLoading");
	}

	private void GenerateGroupFriends(List<IPerson> followers)
	{
		List<GroupingLayer<string, IPerson>> groupedDirect = (from friend in followers
			group friend by StringUtils.GetNoAccentLetter(friend.Name[0]) into g
			orderby g.Key
			select new GroupingLayer<string, IPerson>(g.Key.ToString(), g.OrderBy((IPerson ff) => ff.Name))).ToList();
		GroupedDirect = groupedDirect;
		RaisePropertyChanged("GroupedDirect");
	}

	public void InitFilterOther()
	{
		List<IPerson> list = new List<IPerson>();
		if (GroupedDirect == null)
		{
			return;
		}
		foreach (GroupingLayer<string, IPerson> item in GroupedDirect)
		{
			foreach (IPerson i in item)
			{
				IPerson person = OtherItems.FirstOrDefault((IPerson other) => other.Id == i.Id);
				if (person != null)
				{
					list.Add(i);
					OtherItems.Remove(person);
				}
			}
		}
		if (SelectedPerson == null)
		{
			SelectedPerson = list;
		}
		else
		{
			SelectedPerson = SelectedPerson.Union(list).ToList();
		}
	}

	private void RaisePropertyChanged(string p)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(p));
		}
	}

	internal void Clear()
	{
		OtherItems.Clear();
		SelectedPerson.Clear();
	}

	internal async Task SearchUsers(string search)
	{
		IsLoading = true;
		RaisePropertyChanged("IsLoading");
		VineServiceWithAuth service = DatasProvider.Instance.CurrentUser.Service;
		try
		{
			IListPersons listPersons = ((!string.IsNullOrEmpty(search)) ? (await service.SearchUserMentionAsync(search)) : (await service.GetFollowersAsync()));
			SearchResults = new ObservableCollection<IPerson>(listPersons.Persons);
			RaisePropertyChanged("SearchResults");
		}
		catch
		{
		}
		IsLoading = false;
		RaisePropertyChanged("IsLoading");
	}

	public void RemoveOtherUser(IPerson person)
	{
		if (OtherItems != null)
		{
			OtherItems.Remove(person);
		}
	}

	internal List<IPerson> AddOtherUsers(List<IPerson> persons)
	{
		List<IPerson> list = new List<IPerson>();
		foreach (IPerson person in persons)
		{
			IPerson person2 = (from gr in GroupedDirect
				from item in gr
				where item.Id == person.Id
				select item).FirstOrDefault();
			if (person2 != null)
			{
				list.Add(person2);
			}
			else if (OtherItems.Where((IPerson item) => item.Id == person.Id).FirstOrDefault() == null)
			{
				OtherItems.Add(person);
			}
		}
		return list;
	}

	public void BackupSelectedPerson(List<IPerson> selected)
	{
		SelectedPerson = selected ?? new List<IPerson>();
		FilterSearchInt();
	}

	private void FilterSearchInt()
	{
		if (SelectedPerson == null)
		{
			return;
		}
		foreach (IPerson item in SelectedPerson)
		{
			IPerson person = SearchResults.Where((IPerson s) => s.Id == item.Id).FirstOrDefault();
			if (person != null)
			{
				SearchResults.Remove(person);
			}
		}
	}

	public async Task LoadMoreUsers(string search)
	{
		IsLoading = true;
		RaisePropertyChanged("IsLoading");
		VineServiceWithAuth service = DatasProvider.Instance.CurrentUser.Service;
		try
		{
			IListPersons res = default(IListPersons);
			_ = res;
			res = await service.SearchUserAsync(search, "");
			if (search != _search)
			{
				return;
			}
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				IsLoading = false;
				if (res != null && res.Persons != null)
				{
					foreach (IPerson record in res.Persons)
					{
						if (!SearchResults.Any((IPerson d) => d.Id == record.Id))
						{
							SearchResults.Add(record);
						}
					}
					RaisePropertyChanged("IsLoading");
				}
				else
				{
					RaisePropertyChanged("IsLoading");
				}
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

	internal void Reinit()
	{
	}
}
