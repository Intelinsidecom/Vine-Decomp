using System.Collections.Generic;
using System.IO;
using System.Threading.Tasks;
using System.Windows;
using Vine.Datas;
using Vine.Services;
using Vine.Services.Models;
using Windows.Storage.Pickers;

namespace Vine.Utils;

public static class MediaSaver
{
	public static async Task Save(IPostRecord post)
	{
		if (post.IsVideo && post.VideoLink != null)
		{
			Stream stream = await DatasProvider.Instance.CurrentUser.Service.DownloadAsync(post);
			if (stream != null)
			{
				((App)(object)Application.Current).StreamToSave = stream;
				FileSavePicker val = new FileSavePicker();
				val.put_SuggestedFileName(AppVersion.AppName + "-" + post.PostId);
				val.FileTypeChoices.Add("Video File", new List<string> { ".mp4" });
				val.PickSaveFileAndContinue();
			}
		}
	}
}
