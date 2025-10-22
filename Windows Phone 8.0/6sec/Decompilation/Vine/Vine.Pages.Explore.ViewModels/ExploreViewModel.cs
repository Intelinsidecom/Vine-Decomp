using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO.IsolatedStorage;
using System.Threading.Tasks;
using System.Windows;
using Vine.Datas;
using Vine.Services;
using Vine.Services.Models;

namespace Vine.Pages.Explore.ViewModels;

public class ExploreViewModel : INotifyPropertyChanged
{
	public List<string> Tags { get; set; }

	public bool IsLoading { get; set; }

	public Vine.Datas.Datas Data { get; set; }

	public List<Channel> Channels { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public ExploreViewModel()
	{
		Init();
		Data = DatasProvider.Instance;
	}

	public async Task Init()
	{
		IsLoading = true;
		RaisePropertyChanged("IsLoading");
		VineService.Instance.GetPopularTags().ContinueWith(delegate(Task<List<string>> t)
		{
			if (t.Status == TaskStatus.RanToCompletion)
			{
				Tags = t.Result;
				RaisePropertyChanged("Tags");
			}
			IsLoading = false;
			RaisePropertyChanged("IsLoading");
		}, TaskContinuationOptions.ExecuteSynchronously);
		ChannelsCache channelsCache = null;
		if (IsolatedStorageSettings.ApplicationSettings.TryGetValue<ChannelsCache>("channels", ref channelsCache))
		{
			Channels = channelsCache.Channels;
		}
		if (channelsCache != null && !(channelsCache.Date < DateTime.Now.AddDays(-2.0)))
		{
			return;
		}
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		try
		{
			List<Channel> channels = (Channels = await currentUser.Service.ListChannelsAsync());
			IsolatedStorageSettings.ApplicationSettings["channels"] = new ChannelsCache
			{
				Channels = channels,
				Date = DateTime.Now
			};
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				RaisePropertyChanged("Channels");
			});
		}
		catch
		{
		}
	}

	private void RaisePropertyChanged(string p)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(p));
		}
	}
}
