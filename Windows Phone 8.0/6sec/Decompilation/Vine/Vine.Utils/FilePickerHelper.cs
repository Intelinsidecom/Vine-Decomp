using System.Threading.Tasks;
using System.Windows;
using Microsoft.Phone.Shell;
using Vine.Pages.CropVideo;
using Vine.Resources;
using Windows.ApplicationModel.Activation;
using Windows.Storage;
using Windows.Storage.Pickers;

namespace Vine.Utils;

public class FilePickerHelper
{
	private bool _navigatetocrop;

	private bool _navigatetocropVideo;

	public void Launch()
	{
		//IL_0016: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0022: Unknown result type (might be due to invalid IL or missing references)
		//IL_0029: Unknown result type (might be due to invalid IL or missing references)
		PhoneApplicationService.Current.ContractActivated += Application_ContractActivated;
		FileOpenPicker val = new FileOpenPicker();
		val.put_ViewMode((PickerViewMode)1);
		val.put_SuggestedStartLocation((PickerLocationId)6);
		val.FileTypeFilter.Add(".mp4");
		val.PickSingleFileAndContinue();
	}

	private async void Application_ContractActivated(object sender, IActivatedEventArgs e)
	{
		PhoneApplicationService.Current.ContractActivated -= Application_ContractActivated;
		FileOpenPickerContinuationEventArgs e2 = (FileOpenPickerContinuationEventArgs)(object)((e is FileOpenPickerContinuationEventArgs) ? e : null);
		if (e2 != null && e2.Files.Count == 1)
		{
			StorageFile val = e2.Files[0];
			if (val.ContentType.StartsWith("video/"))
			{
				CropVideoPage.VideoFile = val;
				_navigatetocropVideo = true;
			}
		}
	}

	public async Task<bool> ManageOnNavigationAsync(bool forcedDirect = false)
	{
		if (_navigatetocrop)
		{
			_navigatetocrop = false;
			NavigationServiceExt.ToCrop("forcedstream=true&forceDirect=" + forcedDirect);
			return true;
		}
		if (_navigatetocropVideo && CropVideoPage.VideoFile != null)
		{
			_navigatetocropVideo = false;
			if (!((await CropVideoPage.VideoFile.Properties.GetVideoPropertiesAsync()).Duration.TotalMilliseconds < 3000.0))
			{
				NavigationServiceExt.ToCropVideo("forcedstream=true&forceDirect=" + forcedDirect);
				return true;
			}
			MessageBox.Show(AppResources.VideoTooShort);
		}
		return false;
	}
}
