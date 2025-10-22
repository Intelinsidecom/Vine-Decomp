using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Framework;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Shapes;

namespace Vine.Views;

public sealed class PullToRefreshListControl : BaseUserControl, IComponentConnector
{
	private Thickness _defaultPadding;

	private ListView _listView;

	private ScrollViewer _scrollViewer;

	private IPullToRefresh _refreshCtrl;

	private bool _magicCondition;

	private bool _ignoreScroll;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Grid PullToRefreshGrid;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Path RotatingArrow;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ProgressRing ProgressRing;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private CompositeTransform Transform;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public Thickness DefaultPadding
	{
		get
		{
			return _defaultPadding;
		}
		set
		{
			_defaultPadding = value;
			((Control)_listView).put_Padding(new Thickness(value.Left, value.Top - ((FrameworkElement)PullToRefreshGrid).Height, value.Right, value.Bottom));
		}
	}

	public ListView ListView => _listView;

	public ScrollViewer ScrollViewer => _scrollViewer;

	public PullToRefreshListControl()
	{
		InitializeComponent();
	}

	public void AddReferences(ListView listView, IPullToRefresh refreshController, double appbarOffset)
	{
		_refreshCtrl = refreshController;
		_listView = listView;
		_scrollViewer = ((FrameworkElement)(object)_listView).GetFirstLogicalChildByType<ScrollViewer>(applyTemplates: true);
		DefaultPadding = new Thickness(0.0, ((FrameworkElement)PullToRefreshGrid).Height, 0.0, appbarOffset);
		ScrollViewer scrollViewer = _scrollViewer;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<ScrollViewerViewChangingEventArgs>, EventRegistrationToken>)scrollViewer.add_ViewChanging, (Action<EventRegistrationToken>)scrollViewer.remove_ViewChanging, (EventHandler<ScrollViewerViewChangingEventArgs>)PTRScrollViewerOnViewChanging);
		scrollViewer = _scrollViewer;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<ScrollViewerViewChangedEventArgs>, EventRegistrationToken>)scrollViewer.add_ViewChanged, (Action<EventRegistrationToken>)scrollViewer.remove_ViewChanged, (EventHandler<ScrollViewerViewChangedEventArgs>)PTRScrollViewerOnViewChanged);
	}

	public void ScrollToTopAnimated()
	{
		_scrollViewer.ChangeView((double?)null, (double?)((FrameworkElement)PullToRefreshGrid).Height, (float?)null, false);
	}

	public async Task ScrollToTop()
	{
		_ignoreScroll = true;
		await _scrollViewer.ScrollToVerticalOffsetSpin(((FrameworkElement)PullToRefreshGrid).Height);
		_ignoreScroll = false;
	}

	public async Task UpdateListPadding()
	{
		Task task = ((FrameworkElement)(object)_scrollViewer).LayoutUpdatedAsync();
		Task task2 = Task.Delay(300);
		await Task.WhenAny(task, task2);
		double num = ((((ItemsControl)_listView).ItemsPanelRoot == null) ? 0.0 : ((FrameworkElement)((ItemsControl)_listView).ItemsPanelRoot).ActualHeight);
		if (num < ((FrameworkElement)_listView).ActualHeight)
		{
			((Control)_listView).put_Padding(new Thickness(DefaultPadding.Left, DefaultPadding.Top, DefaultPadding.Right, ((FrameworkElement)_listView).ActualHeight - num + DefaultPadding.Bottom));
		}
		else
		{
			((Control)_listView).put_Padding(DefaultPadding);
		}
	}

	private async void PTRScrollViewerOnViewChanged(object sender, ScrollViewerViewChangedEventArgs e)
	{
		if ((_scrollViewer.VerticalOffset == 0.0 && !e.IsIntermediate) || (_scrollViewer.VerticalOffset < 0.5 && _magicCondition))
		{
			_ignoreScroll = true;
			((UIElement)RotatingArrow).put_Opacity(0.0);
			((UIElement)ProgressRing).put_Visibility((Visibility)0);
			ProgressRing.put_IsActive(true);
			await _refreshCtrl.PullToRefresh();
			await UpdateListPadding();
			await ScrollToTop();
			await Task.Delay(1000);
			((UIElement)PullToRefreshGrid).put_Visibility((Visibility)1);
			((UIElement)ProgressRing).put_Visibility((Visibility)1);
			ProgressRing.put_IsActive(false);
			_ignoreScroll = false;
		}
		_magicCondition = _scrollViewer.VerticalOffset == 0.0 && e.IsIntermediate;
	}

	private void PTRScrollViewerOnViewChanging(object sender, ScrollViewerViewChangingEventArgs e)
	{
		//IL_002a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0030: Invalid comparison between Unknown and I4
		//IL_0055: Unknown result type (might be due to invalid IL or missing references)
		double verticalOffset = e.NextView.VerticalOffset;
		if (verticalOffset >= ((FrameworkElement)PullToRefreshGrid).Height - 10.0)
		{
			if ((int)((UIElement)PullToRefreshGrid).Visibility != 1)
			{
				((UIElement)PullToRefreshGrid).put_Visibility((Visibility)1);
			}
		}
		else if (!_ignoreScroll)
		{
			_ignoreScroll = true;
			if ((int)((UIElement)PullToRefreshGrid).Visibility != 0)
			{
				((UIElement)PullToRefreshGrid).put_Visibility((Visibility)0);
			}
			double num = Math.Min(1.0, (((FrameworkElement)PullToRefreshGrid).Height - verticalOffset) / ((FrameworkElement)PullToRefreshGrid).Height);
			Transform.put_Rotation(num * 270.0 - 90.0);
			((UIElement)RotatingArrow).put_Opacity(Math.Max(0.0, -0.5 + 1.5 * num));
			((FrameworkElement)RotatingArrow).put_Margin(new Thickness(0.0, -80.0 + num * 80.0, 0.0, 0.0));
			((FrameworkElement)RotatingArrow).put_Width(10.0 + num * 25.0);
			((FrameworkElement)RotatingArrow).put_Height(10.0 + num * 25.0);
			if (e.IsInertial && verticalOffset < ((FrameworkElement)PullToRefreshGrid).Height && !_magicCondition)
			{
				ScrollToTopAnimated();
			}
			_ignoreScroll = false;
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		//IL_006f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0079: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/PullToRefreshListControl.xaml"), (ComponentResourceLocation)0);
			PullToRefreshGrid = (Grid)((FrameworkElement)this).FindName("PullToRefreshGrid");
			RotatingArrow = (Path)((FrameworkElement)this).FindName("RotatingArrow");
			ProgressRing = (ProgressRing)((FrameworkElement)this).FindName("ProgressRing");
			Transform = (CompositeTransform)((FrameworkElement)this).FindName("Transform");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		_contentLoaded = true;
	}
}
