using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Threading.Tasks;
using Vine.Framework;
using Vine.Models;
using Vine.Web;
using Windows.Storage;
using Windows.UI.StartScreen;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class UpgradeView : BasePage, IComponentConnector
{
	private bool _isBusy;

	private StorageFile SettingsFile;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public bool IsBusy
	{
		get
		{
			return _isBusy;
		}
		set
		{
			SetProperty(ref _isBusy, value, "IsBusy");
		}
	}

	public UpgradeView()
	{
		InitializeComponent();
	}

	protected override async void LoadState(object sender, LoadStateEventArgs e)
	{
		base.LoadState(sender, e);
		IsBusy = true;
		try
		{
			string userIdKeyHeader = "<Key>userid</Key><Value xmlns:d3p1=\"http://www.w3.org/2001/XMLSchema\" i:type=\"d3p1:string\">";
			string vineIdKeyHeader = "<KeyValueOfstringanyType><Key>key</Key><Value xmlns:d3p1=\"http://www.w3.org/2001/XMLSchema\" i:type=\"d3p1:string\">";
			string firstRunKeyHeader = "<Key>firstRunUserSet</Key><Value i:type=\"ArrayOfstring\"><string>\"";
			ref StorageFile settingsFile = ref SettingsFile;
			object navigationParameter = e.NavigationParameter;
			settingsFile = (StorageFile)((navigationParameter is StorageFile) ? navigationParameter : null);
			string obj = await FileIO.ReadTextAsync((IStorageFile)(object)SettingsFile);
			string text = obj.Substring(obj.IndexOf(userIdKeyHeader)).Replace(userIdKeyHeader, "");
			string userId = text.Substring(0, text.IndexOf("</Value>"));
			text = obj.Substring(obj.IndexOf(vineIdKeyHeader)).Replace(vineIdKeyHeader, "");
			string vineKey = text.Substring(0, text.IndexOf("</Value>"));
			if (obj.Contains(firstRunKeyHeader))
			{
				ApplicationSettings.Current.HasSeenCaptureTutorial = true;
			}
			foreach (SecondaryTile item in await SecondaryTile.FindAllAsync())
			{
				await item.RequestDeleteAsync();
			}
			ApplicationSettings.Current.VineSession = new VineAuthToken
			{
				UserId = userId,
				Key = vineKey
			};
			ApiResult<BaseVineResponseModel<VineUserModel>> apiResult = await App.Api.GetUserMe();
			if (!apiResult.HasError)
			{
				ApplicationSettings.Current.User = apiResult.Model.Data;
				App.RootFrame.Navigate(typeof(HomeView));
			}
			else
			{
				((Page)this).Frame.Navigate(typeof(WelcomeView));
			}
		}
		catch (Exception)
		{
			((Page)this).Frame.Navigate(typeof(WelcomeView));
		}
		await RemoveOldFiles();
		IsBusy = false;
	}

	private async Task RemoveOldFiles()
	{
		string[] array = new string[2] { "default_avatar.png", "__ApplicationSettings" };
		string[] folders = new string[5] { "AppCache", "Drafts", "IECompatCache", "iecompatuaCache", "Media" };
		string[] array2 = array;
		foreach (string text in array2)
		{
			try
			{
				await (await StorageFile.GetFileFromPathAsync(ApplicationData.Current.LocalFolder.Path + "\\" + text)).DeleteAsync();
			}
			catch (Exception)
			{
			}
		}
		array2 = folders;
		foreach (string text2 in array2)
		{
			try
			{
				await (await StorageFolder.GetFolderFromPathAsync(ApplicationData.Current.LocalFolder.Path + "\\" + text2)).DeleteAsync();
			}
			catch (Exception)
			{
			}
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/UpgradeView.xaml"), (ComponentResourceLocation)0);
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		_contentLoaded = true;
	}
}
