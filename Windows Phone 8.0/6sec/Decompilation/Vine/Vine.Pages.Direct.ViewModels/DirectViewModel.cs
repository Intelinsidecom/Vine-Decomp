using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Threading.Tasks;
using System.Windows;
using Vine.Datas;
using Vine.Services.Models;

namespace Vine.Pages.Direct.ViewModels;

public class DirectViewModel : INotifyPropertyChanged
{
	private DirectData _directInfo;

	private DirectData _directInfoOthers;

	private int _pollUpdateInProgress;

	private DateTime _lastActivityDateTime;

	public bool IsLoading { get; set; }

	public ObservableCollection<Conversation> Shares
	{
		get
		{
			if (_directInfo == null)
			{
				return null;
			}
			return _directInfo.Records;
		}
	}

	public ObservableCollection<Conversation> SharesOthers
	{
		get
		{
			if (_directInfoOthers == null)
			{
				return null;
			}
			return _directInfoOthers.Records;
		}
	}

	public int NotifCounter { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public void StartPollUpdate()
	{
		_lastActivityDateTime = DateTime.Now;
		_pollUpdateInProgress = (1 + _pollUpdateInProgress) % 2147483637;
		PollActivity(_pollUpdateInProgress);
	}

	private void PollActivity(int pollinprogress)
	{
		if (_pollUpdateInProgress != pollinprogress)
		{
			return;
		}
		Reload(displayloading: false, delegate
		{
			TimerHelper.ToTimeNoUI(TimeSpan.FromSeconds(30.0), delegate
			{
				PollActivity(pollinprogress);
			});
		});
	}

	public void StopPollUpdate(bool evenchecker = false)
	{
		_pollUpdateInProgress = (1 + _pollUpdateInProgress) % 2147483637;
	}

	internal async Task<bool> ReloadAsync(bool clearnotif)
	{
		return await Task.Run(delegate
		{
			TaskCompletionSource<bool> cs = new TaskCompletionSource<bool>();
			Reload(clearnotif, delegate(bool users)
			{
				cs.TrySetResult(users);
			});
			return cs.Task;
		});
	}

	internal async Task Reload(bool displayloading = true, Action<bool> callback = null)
	{
		_directInfo = null;
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			RaisePropertyChanged("Shares");
			if (displayloading)
			{
				IsLoading = true;
				RaisePropertyChanged("IsLoading");
			}
		});
		bool directinboxpassed = false;
		bool directpendingpassed = false;
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		try
		{
			currentUser.Service.GetMessageAsync(null).ContinueWith((Func<Task<DirectData>, Task>)async delegate(Task<DirectData> t)
			{
				if (t.Status == TaskStatus.RanToCompletion)
				{
					if (t.Result != null)
					{
						_directInfo = t.Result;
						((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
						{
							RaisePropertyChanged("NotifCounter");
							RaisePropertyChanged("Shares");
							if (displayloading)
							{
								IsLoading = false;
								RaisePropertyChanged("IsLoading");
							}
							if (callback != null)
							{
								callback(obj: true);
							}
						});
					}
				}
				else
				{
					directinboxpassed = true;
					((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
					{
						if (displayloading)
						{
							IsLoading = false;
							RaisePropertyChanged("IsLoading");
						}
						if (callback != null)
						{
							callback(obj: false);
						}
					});
				}
			}, TaskScheduler.FromCurrentSynchronizationContext());
			currentUser.Service.GetMessageAsync("other").ContinueWith((Func<Task<DirectData>, Task>)async delegate(Task<DirectData> t)
			{
				if (t.Status == TaskStatus.RanToCompletion)
				{
					if (t.Result != null)
					{
						_directInfoOthers = t.Result;
						((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
						{
							RaisePropertyChanged("NotifCounter");
							RaisePropertyChanged("SharesOthers");
						});
					}
				}
				else
				{
					directpendingpassed = true;
				}
			}, TaskScheduler.FromCurrentSynchronizationContext());
		}
		catch
		{
		}
	}

	internal void Remove(Conversation share)
	{
		IsLoading = true;
		RaisePropertyChanged("IsLoading");
	}

	public void ClearCounter()
	{
		if (_directInfo != null)
		{
			_directInfo.UnreadMessageCount = 0;
			RaisePropertyChanged("NotifCounter");
		}
	}

	private void RaisePropertyChanged(string name)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(name));
		}
	}

	internal void SetNotifCounter(int nbrnotif)
	{
		NotifCounter = nbrnotif;
		RaisePropertyChanged("NotifCounter");
	}
}
