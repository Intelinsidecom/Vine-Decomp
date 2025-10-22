using System;
using System.Collections.Specialized;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using Vine.Utils;

namespace Vine.Controls;

public class ModernAppBar : UserControl
{
	public static readonly DependencyProperty ButtonsProperty = DependencyProperty.Register("Buttons", typeof(DependencyObjectCollection<ModernAppBarButton>), typeof(ModernAppBar), new PropertyMetadata((PropertyChangedCallback)null));

	public static readonly DependencyProperty LeftButtonsProperty = DependencyProperty.Register("LeftButtons", typeof(DependencyObjectCollection<ModernAppBarButton>), typeof(ModernAppBar), new PropertyMetadata((PropertyChangedCallback)null));

	public static readonly DependencyProperty MenuItemsProperty = DependencyProperty.Register("MenuItems", typeof(DependencyObjectCollection<ModernAppBarMenuItem>), typeof(ModernAppBar), new PropertyMetadata((PropertyChangedCallback)null));

	public static readonly DependencyProperty IsMinimizedProperty = DependencyProperty.Register("IsMinimized", typeof(bool), typeof(ModernAppBar), new PropertyMetadata((object)false, new PropertyChangedCallback(IsMinimizedCallback)));

	internal UserControl RootControl;

	internal Grid RootPanel;

	internal VisualStateGroup VisualStateGroup;

	internal VisualState MenuClosedState;

	internal VisualState MenuOpenedState;

	internal VisualState MenuOpenedNoMenuState;

	internal VisualState MenuOpenedNoButtonState;

	internal Grid TransPanel;

	internal Grid MenuPanel;

	internal ScrollViewer Menu;

	internal StackPanel MenusItemsControl;

	internal Grid AppBarPanel;

	internal StackPanel LeftButtonsItemsControl;

	internal StackPanel ButtonsItemsControl;

	internal ModernAppBarButton DisplayMenuButton;

	private bool _contentLoaded;

	public DependencyObjectCollection<ModernAppBarButton> Buttons
	{
		get
		{
			return (DependencyObjectCollection<ModernAppBarButton>)((DependencyObject)this).GetValue(ButtonsProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(ButtonsProperty, (object)value);
		}
	}

	public DependencyObjectCollection<ModernAppBarButton> LeftButtons
	{
		get
		{
			return (DependencyObjectCollection<ModernAppBarButton>)((DependencyObject)this).GetValue(LeftButtonsProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(LeftButtonsProperty, (object)value);
		}
	}

	public DependencyObjectCollection<ModernAppBarMenuItem> MenuItems
	{
		get
		{
			return (DependencyObjectCollection<ModernAppBarMenuItem>)((DependencyObject)this).GetValue(MenuItemsProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(MenuItemsProperty, (object)value);
		}
	}

	public bool IsMenuOpened
	{
		get
		{
			//IL_0006: Unknown result type (might be due to invalid IL or missing references)
			//IL_000c: Invalid comparison between Unknown and I4
			return (int)((UIElement)TransPanel).Visibility == 0;
		}
		set
		{
			if (value)
			{
				ShowMenu();
			}
			else
			{
				HideMenu();
			}
		}
	}

	public bool IsMinimized
	{
		get
		{
			return (bool)((DependencyObject)this).GetValue(IsMinimizedProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(IsMinimizedProperty, (object)value);
		}
	}

	public ModernAppBar()
	{
		InitializeComponent();
		Buttons = new DependencyObjectCollection<ModernAppBarButton>();
		Buttons.CollectionChanged += Buttons_CollectionChanged;
		LeftButtons = new DependencyObjectCollection<ModernAppBarButton>();
		LeftButtons.CollectionChanged += LeftButtons_CollectionChanged;
		MenuItems = new DependencyObjectCollection<ModernAppBarMenuItem>();
		MenuItems.CollectionChanged += Menus_CollectionChanged;
		InitMenu();
	}

	private void Buttons_CollectionChanged(object sender, NotifyCollectionChangedEventArgs e)
	{
		//IL_002b: Unknown result type (might be due to invalid IL or missing references)
		if (e.NewItems != null)
		{
			int newStartingIndex = e.NewStartingIndex;
			foreach (ModernAppBarButton newItem in e.NewItems)
			{
				((FrameworkElement)newItem).Margin = ((Control)this).Padding;
				((PresentationFrameworkCollection<UIElement>)(object)((Panel)ButtonsItemsControl).Children).Insert(newStartingIndex++, (UIElement)(object)newItem);
			}
		}
		if (e.OldItems == null)
		{
			return;
		}
		foreach (object oldItem in e.OldItems)
		{
			((PresentationFrameworkCollection<UIElement>)(object)((Panel)ButtonsItemsControl).Children).Remove((UIElement)(object)(ModernAppBarButton)oldItem);
		}
	}

	private void LeftButtons_CollectionChanged(object sender, NotifyCollectionChangedEventArgs e)
	{
		if (e.NewItems != null)
		{
			int newStartingIndex = e.NewStartingIndex;
			foreach (object newItem in e.NewItems)
			{
				((PresentationFrameworkCollection<UIElement>)(object)((Panel)LeftButtonsItemsControl).Children).Insert(newStartingIndex++, (UIElement)(object)(ModernAppBarButton)newItem);
			}
		}
		if (e.OldItems == null)
		{
			return;
		}
		foreach (object oldItem in e.OldItems)
		{
			((PresentationFrameworkCollection<UIElement>)(object)((Panel)LeftButtonsItemsControl).Children).Remove((UIElement)(object)(ModernAppBarButton)oldItem);
		}
	}

	private void Menus_CollectionChanged(object sender, NotifyCollectionChangedEventArgs e)
	{
		if (e.NewItems != null)
		{
			int newStartingIndex = e.NewStartingIndex;
			foreach (object newItem in e.NewItems)
			{
				((PresentationFrameworkCollection<UIElement>)(object)((Panel)MenusItemsControl).Children).Insert(newStartingIndex++, (UIElement)(object)(ModernAppBarMenuItem)newItem);
			}
		}
		if (e.OldItems != null)
		{
			foreach (object oldItem in e.OldItems)
			{
				((PresentationFrameworkCollection<UIElement>)(object)((Panel)MenusItemsControl).Children).Remove((UIElement)(object)(ModernAppBarMenuItem)oldItem);
			}
		}
		((UIElement)DisplayMenuButton).Visibility = (Visibility)(((PresentationFrameworkCollection<UIElement>)(object)((Panel)MenusItemsControl).Children).Count <= 0);
	}

	private void DisplayMenu_Click(object sender, RoutedEventArgs e)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		if ((int)((UIElement)TransPanel).Visibility == 0)
		{
			HideMenu();
		}
		else
		{
			ShowMenu();
		}
	}

	private void Menu_OnTap(object sender, GestureEventArgs e)
	{
		HideMenu();
	}

	private void HideMenu()
	{
		VisualStateManager.GoToState((Control)(object)this, "MenuClosedState", true);
		((UIElement)TransPanel).Visibility = (Visibility)1;
	}

	private void ShowMenu()
	{
		((UIElement)TransPanel).Visibility = (Visibility)0;
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			if (MenuItems.Count == 0)
			{
				VisualStateManager.GoToState((Control)(object)this, "MenuOpenedNoMenuState", true);
			}
			else
			{
				Menu.ScrollToVerticalOffset(0.0);
				((UIElement)MenuPanel).Visibility = (Visibility)0;
				if (Buttons.Count == 0)
				{
					VisualStateManager.GoToState((Control)(object)this, "MenuOpenedNoButtonState", true);
				}
				else
				{
					VisualStateManager.GoToState((Control)(object)this, "MenuOpenedState", true);
				}
			}
		});
	}

	private static void IsMinimizedCallback(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		((ModernAppBar)(object)d).InitMenu();
	}

	private void InitMenu()
	{
		//IL_0069: Unknown result type (might be due to invalid IL or missing references)
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		double num = (double)Application.Current.Resources[(object)"ModernAppBarButtonLabelMaxSize"];
		if (IsMinimized)
		{
			((TranslateTransform)((UIElement)RootPanel).RenderTransform).Y = 6.0 + num + (double)(PhoneScreenSizeHelper.IsPhablet() ? 22 : 32);
			((Control)DisplayMenuButton).VerticalContentAlignment = (VerticalAlignment)0;
		}
		else
		{
			((TranslateTransform)((UIElement)RootPanel).RenderTransform).Y = 6.0 + num;
			((Control)DisplayMenuButton).VerticalContentAlignment = (VerticalAlignment)1;
		}
	}

	private void Bar_OnManipulationDelta(object sender, ManipulationDeltaEventArgs e)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0025: Unknown result type (might be due to invalid IL or missing references)
		//IL_002a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0047: Unknown result type (might be due to invalid IL or missing references)
		//IL_004c: Unknown result type (might be due to invalid IL or missing references)
		Point translation = e.DeltaManipulation.Translation;
		if (0.0 - ((Point)(ref translation)).Y > 8.0)
		{
			translation = e.DeltaManipulation.Translation;
			double num = Math.Abs(((Point)(ref translation)).Y) * 2.0;
			translation = e.DeltaManipulation.Translation;
			if (num > Math.Abs(((Point)(ref translation)).X) && !IsMenuOpened)
			{
				ShowMenu();
			}
		}
	}

	private void TransPanel_OnManipulationDelta(object sender, ManipulationDeltaEventArgs e)
	{
		e.Handled = true;
		if (IsMenuOpened)
		{
			HideMenu();
		}
	}

	private void TransPanel_OnManipulationStarted(object sender, ManipulationStartedEventArgs e)
	{
		e.Handled = true;
	}

	private void Empty_Tap(object sender, GestureEventArgs e)
	{
		e.Handled = true;
	}

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
		//IL_0085: Unknown result type (might be due to invalid IL or missing references)
		//IL_008f: Expected O, but got Unknown
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Expected O, but got Unknown
		//IL_00b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bb: Expected O, but got Unknown
		//IL_00c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d1: Expected O, but got Unknown
		//IL_00dd: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e7: Expected O, but got Unknown
		//IL_00f3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fd: Expected O, but got Unknown
		//IL_0109: Unknown result type (might be due to invalid IL or missing references)
		//IL_0113: Expected O, but got Unknown
		//IL_011f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0129: Expected O, but got Unknown
		//IL_0135: Unknown result type (might be due to invalid IL or missing references)
		//IL_013f: Expected O, but got Unknown
		//IL_014b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0155: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Controls/ModernAppBar/ModernAppBar.xaml", UriKind.Relative));
			RootControl = (UserControl)((FrameworkElement)this).FindName("RootControl");
			RootPanel = (Grid)((FrameworkElement)this).FindName("RootPanel");
			VisualStateGroup = (VisualStateGroup)((FrameworkElement)this).FindName("VisualStateGroup");
			MenuClosedState = (VisualState)((FrameworkElement)this).FindName("MenuClosedState");
			MenuOpenedState = (VisualState)((FrameworkElement)this).FindName("MenuOpenedState");
			MenuOpenedNoMenuState = (VisualState)((FrameworkElement)this).FindName("MenuOpenedNoMenuState");
			MenuOpenedNoButtonState = (VisualState)((FrameworkElement)this).FindName("MenuOpenedNoButtonState");
			TransPanel = (Grid)((FrameworkElement)this).FindName("TransPanel");
			MenuPanel = (Grid)((FrameworkElement)this).FindName("MenuPanel");
			Menu = (ScrollViewer)((FrameworkElement)this).FindName("Menu");
			MenusItemsControl = (StackPanel)((FrameworkElement)this).FindName("MenusItemsControl");
			AppBarPanel = (Grid)((FrameworkElement)this).FindName("AppBarPanel");
			LeftButtonsItemsControl = (StackPanel)((FrameworkElement)this).FindName("LeftButtonsItemsControl");
			ButtonsItemsControl = (StackPanel)((FrameworkElement)this).FindName("ButtonsItemsControl");
			DisplayMenuButton = (ModernAppBarButton)((FrameworkElement)this).FindName("DisplayMenuButton");
		}
	}
}
