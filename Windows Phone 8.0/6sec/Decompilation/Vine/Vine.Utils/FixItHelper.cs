using System.IO;
using System.Net.Http;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using Vine.Datas;
using Vine.Services;

namespace Vine.Utils;

public static class FixItHelper
{
	public static async Task Show()
	{
		if ((int)MessageBox.Show("Click OK to send debug data to the developer?", "Debug", (MessageBoxButton)1) == 2)
		{
			return;
		}
		Stream stream = DatasProvider.Instance.GetStream();
		HttpClient httpClient = new HttpClient();
		MultipartFormDataContent multipartFormDataContent = new MultipartFormDataContent();
		multipartFormDataContent.Add(new StringContent(AppVersion.AppName), "app");
		multipartFormDataContent.Add(new StreamContent(stream), "data");
		try
		{
			if ((await httpClient.PostAsync("http://www.feelmygeek.com/fixit/upload.php", multipartFormDataContent)).IsSuccessStatusCode)
			{
				ToastHelper.Show("data sent, thanks a lot!", afternav: false, (Orientation)0);
			}
		}
		catch
		{
		}
	}
}
