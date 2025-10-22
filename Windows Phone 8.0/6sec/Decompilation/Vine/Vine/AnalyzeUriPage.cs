using System;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Runtime.CompilerServices;
using System.Text.RegularExpressions;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Vine.Datas;
using Vine.Services;

namespace Vine;

public class AnalyzeUriPage : PhoneApplicationPage
{
	internal Grid LayoutRoot;

	internal Grid LoadingPanel;

	internal ProgressBar ProgressLoading;

	private bool _contentLoaded;

	public AnalyzeUriPage()
	{
		//IL_0032: Unknown result type (might be due to invalid IL or missing references)
		//IL_003c: Expected O, but got Unknown
		InitializeComponent();
		if (DatasProvider.Instance.AddStripComputed)
		{
			((Panel)LayoutRoot).Background = (Brush)Application.Current.Resources[(object)"StripeBrush"];
		}
	}

	private void HideLoading()
	{
		((UIElement)LoadingPanel).Visibility = (Visibility)1;
		ProgressLoading.IsIndeterminate = false;
	}

	private void ShowLoading()
	{
		((UIElement)LoadingPanel).Visibility = (Visibility)0;
		ProgressLoading.IsIndeterminate = true;
	}

	protected override async void OnNavigatedTo(NavigationEventArgs e)
	{
		_003C_003En__FabricatedMethod0(e);
		ShowLoading();
		string requestUriString = ((Page)this).NavigationContext.QueryString["uri"];
		HttpWebRequest request = WebRequest.CreateHttp(requestUriString);
		request.BeginGetResponse(delegate(IAsyncResult iar)
		{
			try
			{
				string input = new StreamReader(request.EndGetResponse(iar).GetResponseStream()).ReadToEnd();
				Match match = new Regex("vine://post/(?<id>\\d+)", RegexOptions.Multiline).Match(input);
				if (match.Success)
				{
					string id = match.Groups["id"].Value;
					((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
					{
						NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriForOnePicture(id), removebackentry: true);
					});
				}
			}
			catch
			{
			}
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				NavigationServiceExt.ToTimeline(null, removebackentry: true);
			});
		}, null);
	}

	protected override void OnNavigatedFrom(NavigationEventArgs e)
	{
		((Page)this).OnNavigatedFrom(e);
		HideLoading();
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/AnalyzeUri/AnalyzeUriPage.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			LoadingPanel = (Grid)((FrameworkElement)this).FindName("LoadingPanel");
			ProgressLoading = (ProgressBar)((FrameworkElement)this).FindName("ProgressLoading");
		}
	}

	[CompilerGenerated]
	private void _003C_003En__FabricatedMethod0(NavigationEventArgs e)
	{
		((Page)this).OnNavigatedTo(e);
	}
}
