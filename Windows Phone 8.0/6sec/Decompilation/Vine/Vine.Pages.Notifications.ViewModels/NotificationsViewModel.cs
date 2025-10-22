using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media;
using Gen.Services;
using Microsoft.Phone.Shell;
using Vine.Datas;

namespace Vine.Pages.Notifications.ViewModels;

public class NotificationsViewModel : INotifyPropertyChanged
{
	public string UserId { get; set; }

	public ObservableCollection<INotification> Notifications { get; set; }

	public string NextPage { get; set; }

	public bool IsLoading { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public NotificationsViewModel()
	{
		Notifications = new ObservableCollection<INotification>();
		LoadNotifications(DatasProvider.Instance.CurrentUser.User.Id);
	}

	public async Task LoadNotifications(string id)
	{
		UserId = id;
		Notifications.Clear();
		NextPage = "1";
		LoadMore();
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		try
		{
			IListNotifications res = default(IListNotifications);
			_ = res;
			res = await currentUser.Service.GetNotificationsFollowRequestAsync(id, null);
			IsLoading = false;
			if (res == null || res.Notifications == null)
			{
				return;
			}
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				foreach (INotification item in Enumerable.Reverse(res.Notifications))
				{
					Notifications.Insert(0, item);
				}
			});
		}
		catch
		{
		}
	}

	public async Task LoadMore()
	{
		if (string.IsNullOrEmpty(NextPage))
		{
			return;
		}
		IsLoading = true;
		RaisePropertyChanged("IsLoading");
		string currentpage = NextPage;
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		try
		{
			IListNotifications res = default(IListNotifications);
			_ = res;
			res = await currentUser.Service.GetNotificationsAsync(UserId, NextPage);
			IsLoading = false;
			if (res != null && res.Notifications != null)
			{
				NextPage = ((currentpage != res.NextPage) ? res.NextPage : null);
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					//IL_0065: Unknown result type (might be due to invalid IL or missing references)
					//IL_006a: Unknown result type (might be due to invalid IL or missing references)
					//IL_008e: Unknown result type (might be due to invalid IL or missing references)
					//IL_0087: Unknown result type (might be due to invalid IL or missing references)
					//IL_0098: Unknown result type (might be due to invalid IL or missing references)
					//IL_00a9: Expected O, but got Unknown
					foreach (INotification notification in res.Notifications)
					{
						Notifications.Add(notification);
					}
					RaisePropertyChanged("IsLoading");
					Vine.Datas.Datas instance = DatasProvider.Instance;
					ShellTile.ActiveTiles.First().Update((ShellTileData)new IconicTileData
					{
						BackgroundColor = (Color)(instance.UseMyAccentColourForLiveTile ? Colors.Transparent : ((Color)Application.Current.Resources[(object)"PrincColor"])),
						Count = 0
					});
				});
			}
			else
			{
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					RaisePropertyChanged("IsLoading");
				});
			}
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

	private void RaisePropertyChanged(string p)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(p));
		}
	}
}
