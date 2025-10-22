using System;
using System.ComponentModel;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using Vine.Datas;
using Vine.Resources;
using Vine.Services;
using Vine.Services.Models;
using Vine.Services.Models.Direct;

namespace Vine.Pages.Chat.ViewModels;

public class ChatViewModel : INotifyPropertyChanged
{
	private int _pollUpdateInProgress;

	private DateTime _lastActivityDateTime;

	private Action _callbackWhenNewMessage;

	public Conversation Conversation { get; set; }

	public DataUser CurrentUser { get; set; }

	public bool IsLoading { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public void StartPollUpdate()
	{
		_lastActivityDateTime = DateTime.Now;
		_pollUpdateInProgress = (1 + _pollUpdateInProgress) % 2147483637;
		PollActivity(_pollUpdateInProgress);
	}

	private async Task PollActivity(int pollinprogress)
	{
		if (_pollUpdateInProgress != pollinprogress)
		{
			return;
		}
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		DirectConversationData res = default(DirectConversationData);
		_ = res;
		res = await currentUser.Service.GetConversationAsync(Conversation.ConversationId, 1);
		try
		{
			int lastminute = _lastActivityDateTime.Minute;
			_lastActivityDateTime = DateTime.Now;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				if (Conversation.Merge(res) && _callbackWhenNewMessage != null)
				{
					_callbackWhenNewMessage();
				}
				if (lastminute != _lastActivityDateTime.Minute)
				{
					Conversation.UpdateTimeOfMessages();
				}
			});
			TimerHelper.ToTimeNoUI(TimeSpan.FromSeconds(3.0), delegate
			{
				PollActivity(pollinprogress);
			});
		}
		catch
		{
			TimerHelper.ToTimeNoUI(TimeSpan.FromSeconds(4.0), delegate
			{
				PollActivity(pollinprogress);
			});
		}
	}

	public void StopPollUpdate(bool evenchecker = false)
	{
		_pollUpdateInProgress = (1 + _pollUpdateInProgress) % 2147483637;
	}

	internal void SetConversation(Conversation conversation, Action callbackWhenNewMessage)
	{
		Conversation = conversation;
		_callbackWhenNewMessage = callbackWhenNewMessage;
		RaisePropertyChanged("Conversation");
		StartPollUpdate();
	}

	public async Task PostMessage(string message, Action<bool> callback)
	{
		SetLoading(loading: true);
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		try
		{
			DirectMessage res = default(DirectMessage);
			_ = res;
			res = await currentUser.Service.SendMessageAsync(Conversation, message);
			Conversation conversation = Conversation;
			string lastMessageRead = (Conversation.LastMessage = res.MessageId);
			conversation.LastMessageRead = lastMessageRead;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				Conversation.AddMessage(res);
				SetLoading(loading: false);
				callback(obj: true);
			});
		}
		catch (ServiceServerErrorException ex)
		{
			ServiceServerErrorException ex2 = ex;
			ServiceServerErrorException ex3 = ex2;
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				ToastHelper.Show(ex3.HttpErrorMessage ?? AppResources.ToastCantConnect, afternav: false, (Orientation)0);
				SetLoading(loading: false);
				callback(obj: false);
			});
		}
	}

	private void RaisePropertyChanged(string name)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(name));
		}
	}

	internal void SetLoading(bool loading)
	{
		IsLoading = loading;
		RaisePropertyChanged("IsLoading");
	}
}
