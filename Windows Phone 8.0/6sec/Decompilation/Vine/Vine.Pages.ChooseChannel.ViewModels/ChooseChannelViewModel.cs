using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO.IsolatedStorage;
using System.Threading.Tasks;
using System.Windows;
using Vine.Datas;
using Vine.Pages.Explore.ViewModels;
using Vine.Services.Models;

namespace Vine.Pages.ChooseChannel.ViewModels;

public class ChooseChannelViewModel : INotifyPropertyChanged
{
	public bool IsLoading { get; set; }

	public Vine.Datas.Datas Data { get; set; }

	public List<Channel> Channels { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public ChooseChannelViewModel()
	{
		Init();
		Data = DatasProvider.Instance;
	}

	public async Task Init()
	{
		IsLoading = true;
		RaisePropertyChanged("IsLoading");
		ChannelsCache channelsCache = null;
		if (IsolatedStorageSettings.ApplicationSettings.TryGetValue<ChannelsCache>("channels", ref channelsCache))
		{
			Channels = channelsCache.Channels;
		}
		if (channelsCache == null || channelsCache.Date < DateTime.Now.AddDays(-2.0))
		{
			DataUser currentUser = DatasProvider.Instance.CurrentUser;
			try
			{
				List<Channel> channels = (Channels = await currentUser.Service.ListChannelsAsync());
				IsolatedStorageSettings.ApplicationSettings["channels"] = new ChannelsCache
				{
					Channels = channels,
					Date = DateTime.Now
				};
				IsLoading = false;
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					RaisePropertyChanged("IsLoading");
					RaisePropertyChanged("Channels");
				});
				return;
			}
			catch
			{
				IsLoading = false;
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					RaisePropertyChanged("IsLoading");
				});
				return;
			}
		}
		IsLoading = false;
		RaisePropertyChanged("IsLoading");
	}

	private void RaisePropertyChanged(string p)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(p));
		}
	}
}
