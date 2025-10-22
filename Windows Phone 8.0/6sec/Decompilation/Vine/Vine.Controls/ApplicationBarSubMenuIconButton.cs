using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;

namespace Vine.Controls;

public class ApplicationBarSubMenuIconButton : ApplicationBarIconButton
{
	private Grid Menu { get; set; }

	private Popup Popup { get; set; }

	public List<ApplicationBarSubMenuItem> Items { get; set; }

	public IApplicationBar Bar { get; set; }

	public ApplicationBarSubMenuIconButton()
	{
		Items = new List<ApplicationBarSubMenuItem>();
		((ApplicationBarIconButton)this).Click += ApplicationBarSubMenuIconButton_Click;
	}

	public void EnableAllItems()
	{
		foreach (ApplicationBarSubMenuItem item in Items)
		{
			item.IsEnabled = true;
		}
	}

	private Storyboard GenerateStoryboardIn(Grid menu)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Expected O, but got Unknown
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000c: Expected O, but got Unknown
		//IL_0027: Unknown result type (might be due to invalid IL or missing references)
		//IL_007b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0085: Expected O, but got Unknown
		//IL_008d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0093: Expected O, but got Unknown
		Storyboard val = new Storyboard();
		DoubleAnimation val2 = new DoubleAnimation();
		((PresentationFrameworkCollection<Timeline>)(object)val.Children).Add((Timeline)(object)val2);
		((Timeline)val2).Duration = new Duration(TimeSpan.FromSeconds(1.0));
		val2.From = 90.0;
		val2.To = 0.0;
		((Timeline)val2).SpeedRatio = 8.0;
		Storyboard.SetTarget((Timeline)(object)val2, (DependencyObject)(object)menu);
		Storyboard.SetTargetProperty((Timeline)(object)val2, new PropertyPath("(UIElement.Projection).(PlaneProjection.RotationX)", new object[0]));
		if (((UIElement)menu).Projection == null)
		{
			PlaneProjection val3 = (PlaneProjection)(object)(((UIElement)menu).Projection = (Projection)new PlaneProjection());
			val3.RotationX = 90.0;
		}
		return val;
	}

	private Storyboard GenerateStoryboardOut(Grid menu)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Expected O, but got Unknown
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000c: Expected O, but got Unknown
		//IL_0027: Unknown result type (might be due to invalid IL or missing references)
		//IL_007b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0085: Expected O, but got Unknown
		//IL_008e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0098: Expected O, but got Unknown
		Storyboard val = new Storyboard();
		DoubleAnimation val2 = new DoubleAnimation();
		((PresentationFrameworkCollection<Timeline>)(object)val.Children).Add((Timeline)(object)val2);
		((Timeline)val2).Duration = new Duration(TimeSpan.FromSeconds(1.0));
		val2.From = 0.0;
		val2.To = 90.0;
		((Timeline)val2).SpeedRatio = 8.0;
		Storyboard.SetTarget((Timeline)(object)val2, (DependencyObject)(object)menu);
		Storyboard.SetTargetProperty((Timeline)(object)val2, new PropertyPath("(UIElement.Projection).(PlaneProjection.RotationX)", new object[0]));
		if (((UIElement)menu).Projection == null)
		{
			((UIElement)menu).Projection = (Projection)new PlaneProjection();
		}
		return val;
	}

	private void ApplicationBarSubMenuIconButton_Click(object sender, EventArgs e)
	{
		OpenPopup();
	}

	private void OpenPopup()
	{
		//IL_000a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0014: Unknown result type (might be due to invalid IL or missing references)
		//IL_001a: Expected O, but got Unknown
		//IL_004a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0050: Expected O, but got Unknown
		//IL_0066: Unknown result type (might be due to invalid IL or missing references)
		//IL_006b: Unknown result type (might be due to invalid IL or missing references)
		//IL_007c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0083: Expected O, but got Unknown
		//IL_011d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0122: Unknown result type (might be due to invalid IL or missing references)
		//IL_012e: Unknown result type (might be due to invalid IL or missing references)
		//IL_013a: Unknown result type (might be due to invalid IL or missing references)
		//IL_013b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0140: Unknown result type (might be due to invalid IL or missing references)
		//IL_014a: Expected O, but got Unknown
		//IL_014c: Expected O, but got Unknown
		//IL_00e5: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ea: Unknown result type (might be due to invalid IL or missing references)
		//IL_00f6: Unknown result type (might be due to invalid IL or missing references)
		//IL_0102: Unknown result type (might be due to invalid IL or missing references)
		//IL_010a: Unknown result type (might be due to invalid IL or missing references)
		//IL_011b: Expected O, but got Unknown
		//IL_016d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0174: Expected O, but got Unknown
		//IL_0184: Unknown result type (might be due to invalid IL or missing references)
		//IL_018b: Expected O, but got Unknown
		//IL_019a: Unknown result type (might be due to invalid IL or missing references)
		//IL_019f: Unknown result type (might be due to invalid IL or missing references)
		//IL_01eb: Unknown result type (might be due to invalid IL or missing references)
		//IL_01f0: Unknown result type (might be due to invalid IL or missing references)
		//IL_01fa: Expected O, but got Unknown
		//IL_01ab: Unknown result type (might be due to invalid IL or missing references)
		//IL_01b0: Unknown result type (might be due to invalid IL or missing references)
		//IL_0232: Unknown result type (might be due to invalid IL or missing references)
		//IL_0246: Unknown result type (might be due to invalid IL or missing references)
		//IL_024b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0252: Expected O, but got Unknown
		//IL_0253: Unknown result type (might be due to invalid IL or missing references)
		//IL_0258: Unknown result type (might be due to invalid IL or missing references)
		//IL_01bc: Unknown result type (might be due to invalid IL or missing references)
		//IL_01c1: Unknown result type (might be due to invalid IL or missing references)
		//IL_029d: Unknown result type (might be due to invalid IL or missing references)
		//IL_02a2: Unknown result type (might be due to invalid IL or missing references)
		//IL_02a9: Expected O, but got Unknown
		//IL_0264: Unknown result type (might be due to invalid IL or missing references)
		//IL_0269: Unknown result type (might be due to invalid IL or missing references)
		//IL_01cd: Unknown result type (might be due to invalid IL or missing references)
		//IL_01d2: Unknown result type (might be due to invalid IL or missing references)
		//IL_0275: Unknown result type (might be due to invalid IL or missing references)
		//IL_027a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0286: Unknown result type (might be due to invalid IL or missing references)
		//IL_028b: Unknown result type (might be due to invalid IL or missing references)
		//IL_02c4: Unknown result type (might be due to invalid IL or missing references)
		//IL_02cb: Expected O, but got Unknown
		//IL_0348: Unknown result type (might be due to invalid IL or missing references)
		PhoneApplicationPage val = (PhoneApplicationPage)((ContentControl)(PhoneApplicationFrame)Application.Current.RootVisual).Content;
		IApplicationBar applicationBar = val.ApplicationBar;
		if (!applicationBar.IsVisible)
		{
			return;
		}
		Bar = applicationBar;
		applicationBar.IsVisible = false;
		val.BackKeyPress += page_BackKeyPress;
		Popup val2 = new Popup();
		((FrameworkElement)val2).Width = ((FrameworkElement)val).ActualWidth;
		Size renderSize = Application.Current.RootVisual.RenderSize;
		double height = ((Size)(ref renderSize)).Height;
		((FrameworkElement)val2).Height = height;
		Grid val3 = new Grid();
		object obj = Application.Current.Resources[(object)"PhoneBackgroundBrush"];
		SolidColorBrush fill = (SolidColorBrush)((obj is SolidColorBrush) ? obj : null);
		object obj2 = Application.Current.Resources[(object)"PhoneChromeBrush"];
		SolidColorBrush background = (SolidColorBrush)((obj2 is SolidColorBrush) ? obj2 : null);
		object obj3 = Application.Current.Resources[(object)"PhoneForegroundBrush"];
		SolidColorBrush val4 = (SolidColorBrush)((obj3 is SolidColorBrush) ? obj3 : null);
		Rectangle val5 = ((applicationBar.Opacity != 1.0) ? new Rectangle
		{
			Width = ((FrameworkElement)val2).Width,
			Height = ((FrameworkElement)val2).Height,
			Fill = (Brush)new SolidColorBrush(Colors.Transparent)
		} : new Rectangle
		{
			Width = ((FrameworkElement)val2).Width,
			Height = ((FrameworkElement)val2).Height,
			Fill = (Brush)(object)fill,
			Opacity = 0.5
		});
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)val3).Children).Add((UIElement)(object)val5);
		((UIElement)val5).Tap += background_Tap;
		Grid val6 = new Grid();
		((FrameworkElement)val6).VerticalAlignment = (VerticalAlignment)2;
		((FrameworkElement)val6).HorizontalAlignment = (HorizontalAlignment)3;
		StackPanel val7 = new StackPanel();
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)val6).Children).Add((UIElement)(object)val7);
		Color val8 = applicationBar.BackgroundColor;
		if (((Color)(ref val8)).A == 0)
		{
			val8 = applicationBar.BackgroundColor;
			if (((Color)(ref val8)).R == 0)
			{
				val8 = applicationBar.BackgroundColor;
				if (((Color)(ref val8)).G == 0)
				{
					val8 = applicationBar.BackgroundColor;
					if (((Color)(ref val8)).B == 0)
					{
						((Panel)val6).Background = (Brush)(object)background;
						goto IL_01fa;
					}
				}
			}
		}
		((Panel)val6).Background = (Brush)new SolidColorBrush(applicationBar.BackgroundColor);
		goto IL_01fa;
		IL_01fa:
		((Panel)val6).Background.Opacity = applicationBar.Opacity;
		((FrameworkElement)val7).Margin = new Thickness(24.0, 24.0, 0.0, 24.0);
		Storyboard val9 = GenerateStoryboardIn(val6);
		SolidColorBrush background2 = new SolidColorBrush(Colors.Transparent);
		val8 = applicationBar.ForegroundColor;
		SolidColorBrush foreground;
		if (((Color)(ref val8)).A == 0)
		{
			val8 = applicationBar.ForegroundColor;
			if (((Color)(ref val8)).R == 0)
			{
				val8 = applicationBar.ForegroundColor;
				if (((Color)(ref val8)).G == 0)
				{
					val8 = applicationBar.ForegroundColor;
					if (((Color)(ref val8)).B == 0)
					{
						foreground = val4;
						goto IL_02a9;
					}
				}
			}
		}
		foreground = new SolidColorBrush(applicationBar.ForegroundColor);
		goto IL_02a9;
		IL_02a9:
		foreach (ApplicationBarSubMenuItem item in Items)
		{
			ContentControl val10 = new ContentControl();
			((Control)val10).Background = (Brush)(object)background2;
			val10.Content = item.Text.ToLower();
			((Control)val10).IsEnabled = item.IsEnabled;
			((Control)val10).Foreground = (Brush)(object)foreground;
			((UIElement)val10).Opacity = ((!item.IsEnabled) ? 0.5 : 1.0);
			((FrameworkElement)val10).Margin = new Thickness(0.0, 12.0, 0.0, 12.0);
			((Control)val10).HorizontalContentAlignment = (HorizontalAlignment)0;
			((Control)val10).FontSize = 30.0;
			((UIElement)val10).Tap += text_Tap;
			item.Parent = this;
			((FrameworkElement)val10).Tag = item;
			((PresentationFrameworkCollection<UIElement>)(object)((Panel)val7).Children).Add((UIElement)(object)val10);
		}
		((PresentationFrameworkCollection<UIElement>)(object)((Panel)val3).Children).Add((UIElement)(object)val6);
		val2.Child = (UIElement)(object)val3;
		val2.IsOpen = true;
		val9.Begin();
		Popup = val2;
		Menu = val6;
	}

	private void background_Tap(object sender, GestureEventArgs e)
	{
		ClosePopup();
	}

	private void text_Tap(object sender, GestureEventArgs e)
	{
		ContentControl val = (ContentControl)((sender is ContentControl) ? sender : null);
		_003F val2 = val;
		object obj = Application.Current.Resources[(object)"PhoneAccentBrush"];
		((Control)val2).Foreground = (Brush)((obj is SolidColorBrush) ? obj : null);
		ApplicationBarSubMenuItem item = ((FrameworkElement)val).Tag as ApplicationBarSubMenuItem;
		ClosePopup(delegate
		{
			item.ExecuteClick();
		});
	}

	private void page_BackKeyPress(object sender, CancelEventArgs e)
	{
		if (Popup.IsOpen)
		{
			e.Cancel = true;
			ClosePopup();
		}
	}

	public void ClosePopup(Action completed = null)
	{
		if (!Popup.IsOpen)
		{
			return;
		}
		Storyboard storyboardOut = GenerateStoryboardOut(Menu);
		EventHandler handler = null;
		handler = delegate
		{
			((Timeline)storyboardOut).Completed -= handler;
			Popup.IsOpen = false;
			if (completed != null)
			{
				completed();
			}
			IApplicationBar bar = Bar;
			if (bar != null)
			{
				bar.IsVisible = true;
			}
		};
		((Timeline)storyboardOut).Completed += handler;
		storyboardOut.Begin();
	}

	private childItem FindVisualChild<childItem>(DependencyObject obj) where childItem : DependencyObject
	{
		for (int i = 0; i < VisualTreeHelper.GetChildrenCount(obj); i++)
		{
			DependencyObject child = VisualTreeHelper.GetChild(obj, i);
			if (child != null && child is childItem)
			{
				return (childItem)(object)child;
			}
			childItem val = FindVisualChild<childItem>(child);
			if (val != null)
			{
				return val;
			}
		}
		return default(childItem);
	}
}
