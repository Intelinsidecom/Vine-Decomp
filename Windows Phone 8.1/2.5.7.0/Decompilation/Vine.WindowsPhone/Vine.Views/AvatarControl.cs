using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.IO;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Models;
using Vine.Web;
using Windows.Graphics.Imaging;
using Windows.Storage;
using Windows.Storage.Streams;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;

namespace Vine.Views;

public sealed class AvatarControl : UserControl, IComponentConnector
{
	public static readonly DependencyProperty IsBusyProperty = DependencyProperty.Register("IsBusy", typeof(bool), typeof(AvatarControl), new PropertyMetadata((object)false));

	public static readonly DependencyProperty BusyVisibleProperty = DependencyProperty.Register("BusyVisible", typeof(Visibility), typeof(AvatarControl), new PropertyMetadata((object)(Visibility)1));

	public static readonly DependencyProperty DisableFlyoutProperty = DependencyProperty.Register("DisableFlyout", typeof(bool), typeof(AvatarControl), new PropertyMetadata((object)false));

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private UserControl userControl;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private MenuFlyout flyModify;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Button btnAvatar;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private ProgressRing prgAvatar;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public bool IsBusy
	{
		get
		{
			return (bool)((DependencyObject)this).GetValue(IsBusyProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(IsBusyProperty, (object)value);
		}
	}

	public Visibility BusyVisible
	{
		get
		{
			//IL_000b: Unknown result type (might be due to invalid IL or missing references)
			return (Visibility)((DependencyObject)this).GetValue(BusyVisibleProperty);
		}
		set
		{
			//IL_0006: Unknown result type (might be due to invalid IL or missing references)
			((DependencyObject)this).SetValue(BusyVisibleProperty, (object)value);
		}
	}

	public bool DisableFlyout
	{
		get
		{
			return (bool)((DependencyObject)this).GetValue(DisableFlyoutProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(DisableFlyoutProperty, (object)value);
		}
	}

	public VineUserModel User => (VineUserModel)((FrameworkElement)this).DataContext;

	public AvatarControl()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		InitializeComponent();
		WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)((FrameworkElement)this).add_Loaded, (Action<EventRegistrationToken>)((FrameworkElement)this).remove_Loaded, new RoutedEventHandler(AvatarControl_Loaded));
	}

	private void AvatarControl_Loaded(object sender, RoutedEventArgs e)
	{
		if (DisableFlyout)
		{
			btnAvatar.put_Flyout((FlyoutBase)null);
		}
	}

	private void btnAvatar_Click(object sender, RoutedEventArgs e)
	{
		if (DisableFlyout)
		{
			ChooseNewAvatar();
		}
	}

	private void MenuFlyoutItemChooseExisting_OnClick(object sender, RoutedEventArgs e)
	{
		ChooseNewAvatar();
	}

	public static void ChooseNewAvatar()
	{
		App.RootFrame.Navigate(typeof(AvatarCropView));
	}

	private async void MenuFlyoutItemRemovePhoto_OnClick(object sender, RoutedEventArgs e)
	{
		(await CropAndUpload(null)).PopUpErrorIfExists();
	}

	public async Task<ApiResult> CropAndUpload(StorageFile file)
	{
		IsBusy = true;
		BusyVisible = (Visibility)0;
		ApiResult result;
		if (file == null)
		{
			ApiResult<BaseVineResponseModel<AvatarResponseModel>> apiResult = await App.Api.UploadAvatar(string.Empty);
			result = apiResult;
			if (!apiResult.HasError)
			{
				ApiResult<BaseVineResponseModel<VineUserModel>> apiResult2 = await App.Api.GetUserMe();
				result = apiResult2;
				if (!result.HasError)
				{
					User.AvatarUrl = apiResult2.Model.Data.AvatarUrl;
					ApplicationSettings.Current.User = apiResult2.Model.Data;
				}
				App.TempNewAvatar = null;
			}
		}
		else
		{
			try
			{
				InMemoryRandomAccessStream ras = new InMemoryRandomAccessStream();
				BitmapDecoder decoder = await BitmapDecoder.CreateAsync(await file.OpenAsync((FileAccessMode)0));
				BitmapEncoder val = await BitmapEncoder.CreateForTranscodingAsync((IRandomAccessStream)(object)ras, decoder);
				uint orientedPixelHeight = decoder.OrientedPixelHeight;
				uint orientedPixelWidth = decoder.OrientedPixelWidth;
				BitmapBounds val2 = default(BitmapBounds);
				if (orientedPixelHeight > orientedPixelWidth)
				{
					double num = 400.0 / (double)orientedPixelWidth * (double)orientedPixelHeight;
					val.BitmapTransform.put_ScaledHeight((uint)Math.Ceiling(num));
					val.BitmapTransform.put_ScaledWidth(400u);
					double num2 = num - 400.0;
					val2.Height = 400u;
					val2.Width = 400u;
					val2.X = 0u;
					val2.Y = (uint)(num2 / 2.0);
				}
				else
				{
					double num3 = 400.0 / (double)orientedPixelHeight * (double)orientedPixelWidth;
					val.BitmapTransform.put_ScaledWidth((uint)Math.Ceiling(num3));
					val.BitmapTransform.put_ScaledHeight(400u);
					double num4 = num3 - 400.0;
					val2.Height = 400u;
					val2.Width = 400u;
					val2.Y = 0u;
					val2.X = (uint)(num4 / 2.0);
				}
				val.BitmapTransform.put_Bounds(val2);
				await val.FlushAsync();
				ApiResult<string> apiResult3 = await App.Api.UploadAvatar(((IRandomAccessStream)(object)ras).AsStream());
				result = apiResult3;
				if (!apiResult3.HasError)
				{
					ApiResult<BaseVineResponseModel<AvatarResponseModel>> apiResult4 = await App.Api.UploadAvatar(apiResult3.XUploadKey);
					result = apiResult4;
					if (!apiResult4.HasError)
					{
						User.AvatarUrl = apiResult4.Model.Data.AvatarUrl;
						VineUserModel user = ApplicationSettings.Current.User;
						user.AvatarUrl = User.AvatarUrl;
						ApplicationSettings.Current.User = user;
					}
					App.TempNewAvatar = null;
				}
			}
			catch (Exception ex)
			{
				BusyVisible = (Visibility)1;
				IsBusy = false;
				return ApiResult.UnExpectedError(ex);
			}
		}
		BusyVisible = (Visibility)1;
		IsBusy = false;
		return result;
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
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/AvatarControl.xaml"), (ComponentResourceLocation)0);
			userControl = (UserControl)((FrameworkElement)this).FindName("userControl");
			flyModify = (MenuFlyout)((FrameworkElement)this).FindName("flyModify");
			btnAvatar = (Button)((FrameworkElement)this).FindName("btnAvatar");
			prgAvatar = (ProgressRing)((FrameworkElement)this).FindName("prgAvatar");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		//IL_001a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0020: Expected O, but got Unknown
		//IL_0041: Unknown result type (might be due to invalid IL or missing references)
		//IL_004b: Expected O, but got Unknown
		//IL_004e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0054: Expected O, but got Unknown
		//IL_0075: Unknown result type (might be due to invalid IL or missing references)
		//IL_007f: Expected O, but got Unknown
		//IL_0082: Unknown result type (might be due to invalid IL or missing references)
		//IL_0088: Expected O, but got Unknown
		//IL_00a9: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b3: Expected O, but got Unknown
		switch (connectionId)
		{
		case 1:
		{
			MenuFlyoutItem val2 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Click, (Action<EventRegistrationToken>)val2.remove_Click, new RoutedEventHandler(MenuFlyoutItemChooseExisting_OnClick));
			break;
		}
		case 2:
		{
			MenuFlyoutItem val2 = (MenuFlyoutItem)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val2.add_Click, (Action<EventRegistrationToken>)val2.remove_Click, new RoutedEventHandler(MenuFlyoutItemRemovePhoto_OnClick));
			break;
		}
		case 3:
		{
			ButtonBase val = (ButtonBase)target;
			WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)val.add_Click, (Action<EventRegistrationToken>)val.remove_Click, new RoutedEventHandler(btnAvatar_Click));
			break;
		}
		}
		_contentLoaded = true;
	}
}
