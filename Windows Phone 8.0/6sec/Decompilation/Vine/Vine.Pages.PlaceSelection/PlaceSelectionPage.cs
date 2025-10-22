using System;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Reactive;
using Vine.Datas;
using Vine.Pages.PlaceSelection.ViewModels;
using Vine.Services;
using Vine.Services.Foursquare;

namespace Vine.Pages.PlaceSelection;

public class PlaceSelectionPage : PhoneApplicationPage
{
	private PlaceSelectionViewModel ViewModel;

	private bool _isInit;

	internal Grid LayoutRoot;

	internal VisualStateGroup VisualStateGroup;

	internal VisualState DirectSearch;

	internal VisualState Popular;

	internal StackPanel WhitePanel;

	internal Rectangle ShadowRectangle;

	internal TextBox SearchBox;

	internal Grid ContentPanel;

	private bool _contentLoaded;

	public PlaceSelectionPage()
	{
		//IL_0049: Unknown result type (might be due to invalid IL or missing references)
		//IL_0053: Expected O, but got Unknown
		ViewModel = new PlaceSelectionViewModel();
		((FrameworkElement)this).DataContext = ViewModel;
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Panel)LayoutRoot).Background = (Brush)Application.Current.Resources[(object)"StripeBrush"];
			((UIElement)ShadowRectangle).Visibility = (Visibility)0;
		}
		ObservableExtensions.Subscribe<IEvent<KeyEventArgs>>(Observable.Throttle<IEvent<KeyEventArgs>>(Observable.FromEvent<KeyEventArgs>((object)SearchBox, "KeyUp"), TimeSpan.FromMilliseconds(300.0)), (Action<IEvent<KeyEventArgs>>)delegate
		{
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				Search(SearchBox.Text);
			});
		});
	}

	protected override void OnNavigatedTo(NavigationEventArgs e)
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		((Page)this).OnNavigatedTo(e);
		if (!_isInit || (int)e.NavigationMode == 0)
		{
			_isInit = true;
			if (((Page)this).NavigationContext.QueryString.ContainsKey("lat") && ((Page)this).NavigationContext.QueryString.ContainsKey("lng"))
			{
				NumberFormatInfo provider = new NumberFormatInfo
				{
					NumberDecimalSeparator = "."
				};
				double longitude = double.Parse(((Page)this).NavigationContext.QueryString["lng"], provider);
				double latitude = double.Parse(((Page)this).NavigationContext.QueryString["lat"], provider);
				ViewModel.SetPosition(latitude, longitude);
			}
			ViewModel.LoadPlace();
		}
	}

	private void Search(string search)
	{
		((PlaceSelectionViewModel)((FrameworkElement)this).DataContext).LoadPlace(search);
	}

	private void Place_SelectionChanged(object sender, SelectionChangedEventArgs e)
	{
		if (e.AddedItems.Count == 0 || e.AddedItems[0] == null)
		{
			return;
		}
		Venue venue = e.AddedItems[0] as Venue;
		string encoid = ((Page)this).NavigationContext.QueryString["id"];
		if (!(encoid == "FROMIMAGE"))
		{
			EncodingJob encodingJob = DatasProvider.Instance.Encodings.FirstOrDefault((EncodingJob enc) => enc.Id == encoid);
			if (encodingJob != null)
			{
				encodingJob.SetPlace(venue, ((PlaceSelectionViewModel)((FrameworkElement)this).DataContext).FoursquareRequestId);
				DatasProvider.Instance.Save();
			}
		}
		((Page)this).NavigationService.GoBack();
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/PlaceSelection/PlaceSelectionPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			VisualStateGroup = (VisualStateGroup)((FrameworkElement)this).FindName("VisualStateGroup");
			DirectSearch = (VisualState)((FrameworkElement)this).FindName("DirectSearch");
			Popular = (VisualState)((FrameworkElement)this).FindName("Popular");
			WhitePanel = (StackPanel)((FrameworkElement)this).FindName("WhitePanel");
			ShadowRectangle = (Rectangle)((FrameworkElement)this).FindName("ShadowRectangle");
			SearchBox = (TextBox)((FrameworkElement)this).FindName("SearchBox");
			ContentPanel = (Grid)((FrameworkElement)this).FindName("ContentPanel");
		}
	}
}
