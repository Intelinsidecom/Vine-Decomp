using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Markup;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Vine.Utils;

namespace Vine.Controls.Toast;

public class DialogService
{
	private const string SlideUpStoryboard = "\r\n        <Storyboard  xmlns=\"http://schemas.microsoft.com/winfx/2006/xaml/presentation\">\r\n            <DoubleAnimationUsingKeyFrames Storyboard.TargetProperty=\"(UIElement.RenderTransform).(TranslateTransform.Y)\" \r\n                                           Storyboard.TargetName=\"LayoutRoot\">\r\n                    <EasingDoubleKeyFrame KeyTime=\"0\" Value=\"-100\"/>\r\n                    <EasingDoubleKeyFrame KeyTime=\"0:0:0.5\" Value=\"0\">\r\n                        <EasingDoubleKeyFrame.EasingFunction>\r\n                           <CircleEase EasingMode=\"EaseOut\"/>\r\n                        </EasingDoubleKeyFrame.EasingFunction>\r\n                    </EasingDoubleKeyFrame>\r\n                </DoubleAnimationUsingKeyFrames>\r\n            <DoubleAnimation Storyboard.TargetProperty=\"(UIElement.Opacity)\" From=\"0\" To=\"1\" Duration=\"0:0:0.350\" \r\n                                 Storyboard.TargetName=\"LayoutRoot\">\r\n                <DoubleAnimation.EasingFunction>\r\n                    <ExponentialEase EasingMode=\"EaseOut\" Exponent=\"6\"/>\r\n                </DoubleAnimation.EasingFunction>\r\n            </DoubleAnimation>\r\n        </Storyboard>";

	private const string SlideDownStoryboard = "\r\n        <Storyboard  xmlns=\"http://schemas.microsoft.com/winfx/2006/xaml/presentation\">\r\n            <DoubleAnimationUsingKeyFrames Storyboard.TargetProperty=\"(UIElement.RenderTransform).(TranslateTransform.Y)\" \r\n                                           Storyboard.TargetName=\"LayoutRoot\">\r\n                <EasingDoubleKeyFrame KeyTime=\"0\" Value=\"0\"/>\r\n                <EasingDoubleKeyFrame KeyTime=\"0:0:0.3\" Value=\"-150\">\r\n                    <EasingDoubleKeyFrame.EasingFunction>\r\n                           <CircleEase EasingMode=\"EaseIn\"/>\r\n                  </EasingDoubleKeyFrame.EasingFunction>\r\n                </EasingDoubleKeyFrame>\r\n            </DoubleAnimationUsingKeyFrames>\r\n            <DoubleAnimation Storyboard.TargetProperty=\"(UIElement.Opacity)\" From=\"1\" To=\"0\" Duration=\"0:0:0.25\" \r\n                                 Storyboard.TargetName=\"LayoutRoot\">\r\n                <DoubleAnimation.EasingFunction>\r\n                    <ExponentialEase EasingMode=\"EaseIn\" Exponent=\"6\"/>\r\n                </DoubleAnimation.EasingFunction>\r\n            </DoubleAnimation>\r\n        </Storyboard>";

	private Storyboard _hideStoryboard;

	private Panel _overlay;

	private PhoneApplicationPage _page;

	private Panel _popupContainer;

	private Frame _rootVisual;

	private Storyboard _showStoryboard;

	public FrameworkElement Child { get; set; }

	public double VerticalOffset { get; set; }

	public Brush BackgroundBrush { get; set; }

	internal bool IsOpen { get; set; }

	protected internal bool IsBackKeyOverride { get; set; }

	public bool HasPopup { get; set; }

	internal PhoneApplicationPage Page
	{
		get
		{
			PhoneApplicationPage result;
			if ((result = _page) == null)
			{
				result = (_page = ((DependencyObject)(object)RootVisual).GetVisualDescendants().OfType<PhoneApplicationPage>().FirstOrDefault());
			}
			return result;
		}
	}

	internal Frame RootVisual
	{
		get
		{
			Frame result;
			if ((result = _rootVisual) == null)
			{
				ref Frame rootVisual = ref _rootVisual;
				UIElement rootVisual2 = Application.Current.RootVisual;
				result = (rootVisual = (Frame)(object)((rootVisual2 is Frame) ? rootVisual2 : null));
			}
			return result;
		}
	}

	internal Panel PopupContainer
	{
		get
		{
			if (_popupContainer == null)
			{
				IEnumerable<ContentPresenter> source = ((DependencyObject)(object)RootVisual).GetVisualDescendants().OfType<ContentPresenter>();
				for (int i = 0; i < source.Count(); i++)
				{
					IEnumerable<Panel> source2 = ((DependencyObject)(object)source.ElementAt(i)).GetVisualDescendants().OfType<Panel>();
					if (source2.Count() > 0)
					{
						_popupContainer = source2.First();
						break;
					}
				}
			}
			return _popupContainer;
		}
	}

	public TimeSpan BeginTime { get; set; }

	public event EventHandler Closed;

	public event EventHandler Opened;

	private bool InitializePopup()
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		//IL_005b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0065: Expected O, but got Unknown
		//IL_00a0: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c6: Unknown result type (might be due to invalid IL or missing references)
		try
		{
			_overlay = (Panel)new Grid();
			Grid.SetColumnSpan((FrameworkElement)(object)_overlay, int.MaxValue);
			Grid.SetRowSpan((FrameworkElement)(object)_overlay, int.MaxValue);
			ref Storyboard showStoryboard = ref _showStoryboard;
			object obj = XamlReader.Load("\r\n        <Storyboard  xmlns=\"http://schemas.microsoft.com/winfx/2006/xaml/presentation\">\r\n            <DoubleAnimationUsingKeyFrames Storyboard.TargetProperty=\"(UIElement.RenderTransform).(TranslateTransform.Y)\" \r\n                                           Storyboard.TargetName=\"LayoutRoot\">\r\n                    <EasingDoubleKeyFrame KeyTime=\"0\" Value=\"-100\"/>\r\n                    <EasingDoubleKeyFrame KeyTime=\"0:0:0.5\" Value=\"0\">\r\n                        <EasingDoubleKeyFrame.EasingFunction>\r\n                           <CircleEase EasingMode=\"EaseOut\"/>\r\n                        </EasingDoubleKeyFrame.EasingFunction>\r\n                    </EasingDoubleKeyFrame>\r\n                </DoubleAnimationUsingKeyFrames>\r\n            <DoubleAnimation Storyboard.TargetProperty=\"(UIElement.Opacity)\" From=\"0\" To=\"1\" Duration=\"0:0:0.350\" \r\n                                 Storyboard.TargetName=\"LayoutRoot\">\r\n                <DoubleAnimation.EasingFunction>\r\n                    <ExponentialEase EasingMode=\"EaseOut\" Exponent=\"6\"/>\r\n                </DoubleAnimation.EasingFunction>\r\n            </DoubleAnimation>\r\n        </Storyboard>");
			showStoryboard = (Storyboard)((obj is Storyboard) ? obj : null);
			ref Storyboard hideStoryboard = ref _hideStoryboard;
			object obj2 = XamlReader.Load("\r\n        <Storyboard  xmlns=\"http://schemas.microsoft.com/winfx/2006/xaml/presentation\">\r\n            <DoubleAnimationUsingKeyFrames Storyboard.TargetProperty=\"(UIElement.RenderTransform).(TranslateTransform.Y)\" \r\n                                           Storyboard.TargetName=\"LayoutRoot\">\r\n                <EasingDoubleKeyFrame KeyTime=\"0\" Value=\"0\"/>\r\n                <EasingDoubleKeyFrame KeyTime=\"0:0:0.3\" Value=\"-150\">\r\n                    <EasingDoubleKeyFrame.EasingFunction>\r\n                           <CircleEase EasingMode=\"EaseIn\"/>\r\n                  </EasingDoubleKeyFrame.EasingFunction>\r\n                </EasingDoubleKeyFrame>\r\n            </DoubleAnimationUsingKeyFrames>\r\n            <DoubleAnimation Storyboard.TargetProperty=\"(UIElement.Opacity)\" From=\"1\" To=\"0\" Duration=\"0:0:0.25\" \r\n                                 Storyboard.TargetName=\"LayoutRoot\">\r\n                <DoubleAnimation.EasingFunction>\r\n                    <ExponentialEase EasingMode=\"EaseIn\" Exponent=\"6\"/>\r\n                </DoubleAnimation.EasingFunction>\r\n            </DoubleAnimation>\r\n        </Storyboard>");
			hideStoryboard = (Storyboard)((obj2 is Storyboard) ? obj2 : null);
			((UIElement)_overlay).RenderTransform = (Transform)new TranslateTransform();
			((PresentationFrameworkCollection<UIElement>)(object)_overlay.Children).Add((UIElement)(object)Child);
			if (BackgroundBrush != null)
			{
				_overlay.Background = BackgroundBrush;
			}
			Panel overlay = _overlay;
			Thickness margin = ((FrameworkElement)PopupContainer).Margin;
			((FrameworkElement)overlay).Margin = new Thickness(0.0 - ((Thickness)(ref margin)).Left, VerticalOffset, 0.0, 0.0);
			((UIElement)_overlay).Opacity = 0.0;
			((PresentationFrameworkCollection<UIElement>)(object)PopupContainer.Children).Add((UIElement)(object)_overlay);
		}
		catch
		{
			return false;
		}
		return true;
	}

	protected internal void SetAlignmentsOnOverlay(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment)
	{
		//IL_000e: Unknown result type (might be due to invalid IL or missing references)
		//IL_001a: Unknown result type (might be due to invalid IL or missing references)
		if (_overlay != null)
		{
			((FrameworkElement)_overlay).HorizontalAlignment = horizontalAlignment;
			((FrameworkElement)_overlay).VerticalAlignment = verticalAlignment;
		}
	}

	public void ShowAfterNavigating()
	{
		//IL_0012: Unknown result type (might be due to invalid IL or missing references)
		//IL_001c: Expected O, but got Unknown
		((Page)Page).NavigationService.Navigated += new NavigatedEventHandler(NavigationService_Navigated);
	}

	private void NavigationService_Navigated(object sender, NavigationEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_000d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0017: Expected O, but got Unknown
		//IL_002f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0039: Expected O, but got Unknown
		((NavigationService)sender).Navigated -= new NavigatedEventHandler(NavigationService_Navigated);
		object content = e.Content;
		PhoneApplicationPage val = (PhoneApplicationPage)((content is PhoneApplicationPage) ? content : null);
		if (val != null)
		{
			((FrameworkElement)val).Loaded += new RoutedEventHandler(page_Loaded);
		}
	}

	private void page_Loaded(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_000d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0017: Expected O, but got Unknown
		((FrameworkElement)(PhoneApplicationPage)sender).Loaded -= new RoutedEventHandler(page_Loaded);
		Show();
	}

	public void Show()
	{
		//IL_0041: Unknown result type (might be due to invalid IL or missing references)
		//IL_004b: Expected O, but got Unknown
		IsOpen = true;
		if (!InitializePopup())
		{
			return;
		}
		if (!IsBackKeyOverride)
		{
			Page.BackKeyPress += OnBackKeyPress;
		}
		((Page)Page).NavigationService.Navigated += new NavigatedEventHandler(OnNavigated);
		((Timeline)_showStoryboard).Completed += _showStoryboard_Completed;
		Storyboard showStoryboard = _showStoryboard;
		((Timeline)showStoryboard).BeginTime = ((Timeline)showStoryboard).BeginTime + BeginTime;
		foreach (Timeline item in (PresentationFrameworkCollection<Timeline>)(object)_showStoryboard.Children)
		{
			Storyboard.SetTarget(item, (DependencyObject)(object)_overlay);
		}
		((FrameworkElement)(object)PopupContainer).InvokeOnLayoutUpdated(delegate
		{
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				try
				{
					_showStoryboard.Begin();
				}
				catch
				{
				}
			});
		});
	}

	private void _showStoryboard_Completed(object sender, EventArgs e)
	{
		Storyboard val = (Storyboard)((sender is Storyboard) ? sender : null);
		if (val != null)
		{
			((Timeline)val).Completed -= _showStoryboard_Completed;
		}
		if (this.Opened != null)
		{
			this.Opened(this, null);
		}
	}

	private void OnNavigated(object sender, NavigationEventArgs e)
	{
		Hide();
	}

	public void Hide()
	{
		//IL_003a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0044: Expected O, but got Unknown
		if (!IsOpen)
		{
			return;
		}
		if (Page != null)
		{
			Page.BackKeyPress -= OnBackKeyPress;
			((Page)Page).NavigationService.Navigated -= new NavigatedEventHandler(OnNavigated);
			_page = null;
		}
		_hideStoryboard.Stop();
		foreach (Timeline item in (PresentationFrameworkCollection<Timeline>)(object)_hideStoryboard.Children)
		{
			Storyboard.SetTarget(item, (DependencyObject)(object)_overlay);
		}
		((Timeline)_hideStoryboard).Completed += _hideStoryboard_Completed;
		_hideStoryboard.Begin();
	}

	private void _hideStoryboard_Completed(object sender, EventArgs e)
	{
		if (_hideStoryboard != null)
		{
			((Timeline)_hideStoryboard).Completed -= _hideStoryboard_Completed;
			_hideStoryboard = null;
		}
		IsOpen = false;
		if (PopupContainer != null)
		{
			((PresentationFrameworkCollection<UIElement>)(object)PopupContainer.Children).Remove((UIElement)(object)_overlay);
		}
		if (_overlay != null)
		{
			((PresentationFrameworkCollection<UIElement>)(object)_overlay.Children).Clear();
			_overlay = null;
		}
		if (this.Closed != null)
		{
			this.Closed(this, null);
		}
	}

	public void OnBackKeyPress(object sender, CancelEventArgs e)
	{
		if (HasPopup)
		{
			e.Cancel = true;
		}
		else if (IsOpen)
		{
			e.Cancel = true;
			Hide();
		}
	}
}
