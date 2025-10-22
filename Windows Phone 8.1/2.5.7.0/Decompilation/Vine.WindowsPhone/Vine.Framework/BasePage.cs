using Vine.Common;
using Vine.Events;
using Vine.Views;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Media.Animation;
using Windows.UI.Xaml.Navigation;

namespace Vine.Framework;

public class BasePage : NotifyPage, IEventHandler<AppResuming>
{
	protected readonly NavigationHelper _navigationHelper;

	public NavigationHelper NavigationHelper => _navigationHelper;

	public object NavigationParam { get; set; }

	public object NavigationObject { get; set; }

	public bool AlwaysClearBackStack { get; set; }

	public BasePage()
	{
		if (ResourceHelper.GetString("PageFlowDirection") == "RightToLeft")
		{
			((FrameworkElement)this).put_FlowDirection((FlowDirection)1);
		}
		else
		{
			((FrameworkElement)this).put_FlowDirection((FlowDirection)0);
		}
		_navigationHelper = new NavigationHelper((Page)(object)this);
		_navigationHelper.LoadState += LoadState;
		_navigationHelper.SaveState += SaveState;
		EventAggregator.Current.Subscribe(this);
	}

	protected virtual void LoadState(object sender, LoadStateEventArgs e)
	{
		NavigationParam = e.NavigationParameter;
		if (NavigationParam is string text && text.StartsWith("{"))
		{
			NavigationObject = Serialization.DeserializeType(text);
		}
	}

	protected void LoadState()
	{
		LoadState(this, new LoadStateEventArgs(NavigationParam, _navigationHelper.PageState, null));
	}

	protected virtual void SaveState(object sender, SaveStateEventArgs e)
	{
	}

	public void Handle(AppResuming e)
	{
		if (((Page)(object)this).IsActivePage())
		{
			PageResuming();
		}
	}

	public virtual void PageResuming()
	{
		LoadState();
	}

	public virtual void ClearPageState()
	{
		NavigationHelper.ClearPageState();
	}

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
		if (AlwaysClearBackStack || App.ClearBackStack)
		{
			App.ClearBackStack = false;
			App.RootFrame.BackStack.Clear();
		}
		_navigationHelper.OnNavigatedTo(e);
	}

	protected override void OnNavigatedFrom(NavigationEventArgs e)
	{
		_navigationHelper.OnNavigatedFrom(e);
	}

	public void ClearNavigationParam()
	{
		//IL_0052: Unknown result type (might be due to invalid IL or missing references)
		//IL_005c: Expected O, but got Unknown
		NavigationParam = null;
		NavigationObject = null;
		if (App.RootFrame.BackStack != null && App.RootFrame.BackStack.Count > 0)
		{
			App.RootFrame.BackStack.RemoveAt(0);
			App.RootFrame.BackStack.Add(new PageStackEntry(typeof(HomeView), (object)null, (NavigationTransitionInfo)null));
		}
	}
}
