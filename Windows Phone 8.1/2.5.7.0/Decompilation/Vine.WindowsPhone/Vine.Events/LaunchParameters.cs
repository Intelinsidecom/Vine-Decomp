using System;
using Vine.Framework;
using Vine.Views;
using Vine.Views.Capture;
using Windows.UI.Popups;
using Windows.UI.Xaml.Controls;

namespace Vine.Events;

public class LaunchParameters
{
	public string Type { get; set; }

	public string Data { get; set; }

	public string Title { get; set; }

	public string Msg { get; set; }

	public void Navigate()
	{
		App.ClearBackStack = true;
		if (!NavigateHelper())
		{
			App.ClearBackStack = false;
		}
	}

	private bool NavigateHelper()
	{
		if (Type == "ignore")
		{
			return false;
		}
		if (Type == "msgBoxOnTap")
		{
			DispatcherEx.BeginInvoke(async delegate
			{
				MessageDialog val = new MessageDialog(Msg, Title);
				val.Commands.Add((IUICommand)new UICommand(ResourceHelper.GetString("Ok")));
				try
				{
					await val.ShowAsync();
				}
				catch
				{
				}
			});
			return false;
		}
		if (Type == "interactions")
		{
			if (((ContentControl)App.RootFrame).Content is HomeView && App.HomePivotIndex == 2)
			{
				return false;
			}
			App.HomePivotIndex = 2;
			App.RootFrame.Navigate(typeof(HomeView));
		}
		else if (Type == "conversations")
		{
			if (((ContentControl)App.RootFrame).Content is HomeView && App.HomePivotIndex == 4)
			{
				return false;
			}
			App.HomePivotIndex = 4;
			App.RootFrame.Navigate(typeof(HomeView));
		}
		else
		{
			if (IsActivePage())
			{
				return false;
			}
			if (Type.Equals("Vine.Views.SearchView"))
			{
				Type = "Vine.Views.HomeView";
			}
			if (Type.Equals("Vine.Views.Capture.CaptureView"))
			{
				Type = CaptureViewHelper.GetCaptureView().FullName;
			}
			Type type = System.Type.GetType(Type);
			App.RootFrame.Navigate(type, (object)Data);
		}
		return true;
	}

	private bool IsActivePage()
	{
		if (((ContentControl)App.RootFrame).Content != null)
		{
			BasePage basePage = (BasePage)((ContentControl)App.RootFrame).Content;
			Type type = System.Type.GetType(Type);
			if ((object)((object)basePage).GetType() == type && basePage.NavigationParam as string == Data)
			{
				return true;
			}
		}
		return false;
	}
}
