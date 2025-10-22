using System;
using System.Collections.Generic;
using System.IO;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using System.Windows;
using Microsoft.Phone.BackgroundTransfer;
using Vine.Datas;
using Vine.Services;
using Vine.Utils;
using Windows.Storage;

namespace Vine;

internal class BackgroundDownloadManager
{
	public static Vine.Datas.Datas Data { get; set; }

	static BackgroundDownloadManager()
	{
		Data = DatasProvider.Instance;
	}

	public static void Init()
	{
		foreach (BackgroundTransferRequest item in BackgroundTransferService.Requests.ToList())
		{
			ManageTransfertCompleted(item);
			item.TransferStatusChanged += task_TransferStatusChanged;
		}
		RelaunchAllJobs();
	}

	public static void RelaunchAllJobs()
	{
		foreach (EncodingJob item in DatasProvider.Instance.Encodings.ToList())
		{
			RelaunchJob(item);
		}
	}

	public static void RelaunchJob(EncodingJob job)
	{
		if (DatasProvider.Instance.CurrentUser != null && job.UserId != DatasProvider.Instance.CurrentUser.User.Id)
		{
			return;
		}
		if (job.State == EncodingStep.ERROR && job.PreviousState != EncodingStep.NONE)
		{
			job.State = job.PreviousState;
			job.UpdateState();
			if (!string.IsNullOrEmpty(job.CurrentRequestId))
			{
				BackgroundTransferRequest val = BackgroundTransferService.Requests.FirstOrDefault((BackgroundTransferRequest f) => f.RequestId == job.CurrentRequestId);
				if (val != null)
				{
					BackgroundTransferService.Remove(val);
				}
			}
		}
		switch (job.State)
		{
		case EncodingStep.UPLOADINGVIDEO:
			UploadVideoToService(job);
			return;
		case EncodingStep.UPLOADINGIMAGE:
			UploadImageToService(job);
			return;
		case EncodingStep.INITIALIZATION:
			Initialization(job);
			return;
		}
		if ((job.State == EncodingStep.SENDENCODING || job.State == EncodingStep.GETIMAGE || job.State == EncodingStep.GETVIDEO || job.State == EncodingStep.UPLOADINGIMAGE || job.State == EncodingStep.UPLOADINGVIDEO) && !string.IsNullOrEmpty(job.CurrentRequestId) && BackgroundTransferService.Requests.FirstOrDefault((BackgroundTransferRequest f) => f.RequestId == job.CurrentRequestId) == null)
		{
			EncodingStep encodingStep = EncodingStep.NONE;
			switch (job.State)
			{
			case EncodingStep.SENDENCODING:
				encodingStep = EncodingStep.BEGIN;
				break;
			case EncodingStep.WAITINGENCODING:
				encodingStep = EncodingStep.WAITINGENCODING;
				break;
			case EncodingStep.GETVIDEO:
				encodingStep = EncodingStep.WAITINGENCODING;
				break;
			case EncodingStep.GETIMAGE:
				encodingStep = EncodingStep.GETVIDEO;
				break;
			}
			job.State = encodingStep;
			ManageState(encodingStep, job);
		}
	}

	private static async Task Initialization(EncodingJob job)
	{
	}

	public static bool AddTask(BackgroundTransferRequest task)
	{
		try
		{
			task.TransferStatusChanged += task_TransferStatusChanged;
			BackgroundTransferService.Add(task);
			return true;
		}
		catch (Exception)
		{
		}
		return false;
	}

	public static async Task Retry(EncodingJob job)
	{
		if (job.LocalEncodingFile == null || !IsolatedStorageFile.GetUserStoreForApplication().FileExists(job.LocalEncodingFile))
		{
			return;
		}
		if (job.CurrentRequestId != null)
		{
			BackgroundTransferRequest val = BackgroundTransferService.Find(job.CurrentRequestId);
			if (val != null)
			{
				BackgroundTransferService.Remove(val);
			}
		}
		if (job.RemoteMyServerJobId != null)
		{
			OwnService.RemoveVideo(job.RemoteMyServerJobId);
		}
		job.State = EncodingStep.BEGIN;
		job.LocalVideoPath = null;
		job.CurrentRequestId = null;
		job.RemoteMyServerJobId = null;
		job.AmazonVideo = null;
		job.AmazonCapture = null;
		job.UpdateState();
		await LaunchEncoding(job);
	}

	private static async void task_TransferStatusChanged(object sender, BackgroundTransferEventArgs e)
	{
		ManageTransfertCompleted(e.Request);
	}

	private static async void ManageTransfertCompleted(BackgroundTransferRequest request)
	{
		TransferStatus transferStatus = request.TransferStatus;
		if ((int)transferStatus != 8)
		{
			return;
		}
		string[] array = request.Tag.Split('|');
		string jobid = array[0];
		EncodingJob encodingJob = Data.Encodings.FirstOrDefault((EncodingJob j) => j.Id == jobid);
		if (encodingJob == null)
		{
			try
			{
				BackgroundTransferService.Remove(request);
				return;
			}
			catch
			{
				return;
			}
		}
		Vine.Datas.Datas instance = DatasProvider.Instance;
		if (encodingJob.UserId != instance.CurrentUser.User.Id)
		{
			return;
		}
		if (request.StatusCode == 200 || request.StatusCode == 206)
		{
			EncodingStep moment = (EncodingStep)int.Parse(array[1]);
			try
			{
				if (BackgroundTransferService.Requests.Contains(request))
				{
					BackgroundTransferService.Remove(request);
				}
			}
			catch
			{
			}
			ManageState(moment, encodingJob);
			return;
		}
		encodingJob.PreviousState = encodingJob.State;
		encodingJob.State = EncodingStep.ERROR;
		encodingJob.UpdateState();
		try
		{
			if (BackgroundTransferService.Requests.Contains(request))
			{
				BackgroundTransferService.Remove(request);
			}
		}
		catch
		{
		}
	}

	public static void BeginProcess(EncodingJob job)
	{
		if (job.NoNeedEncoding)
		{
			UploadVideoToService(job);
		}
		else
		{
			LaunchEncoding(job);
		}
	}

	public static async Task RemoveJob(EncodingJob job)
	{
		DatasProvider.Instance.RemoveJob(job);
		StorageFolder val = await ApplicationData.Current.LocalFolder.GetFolderAsync("shared");
		if (val == null)
		{
			return;
		}
		StorageFolder val2 = await val.GetFolderAsync("transfers");
		if (val2 == null)
		{
			return;
		}
		string jobId = job.Id + "_";
		foreach (StorageFile item in (await val2.GetFilesAsync()).ToList())
		{
			if (item.Name.StartsWith(jobId))
			{
				await item.DeleteAsync();
			}
		}
	}

	public static async Task RemoveAllJobs()
	{
		DatasProvider.Instance.RemoveAllJobs();
		StorageFolder val = await ApplicationData.Current.LocalFolder.GetFolderAsync("shared");
		if (val == null)
		{
			return;
		}
		StorageFolder val2 = await val.GetFolderAsync("transfers");
		if (val2 == null)
		{
			return;
		}
		foreach (StorageFile item in (await val2.GetFilesAsync()).ToList())
		{
			await item.DeleteAsync();
		}
	}

	private static async Task ManageState(EncodingStep moment, EncodingJob job)
	{
		switch (moment)
		{
		case EncodingStep.BEGIN:
			BeginProcess(job);
			break;
		case EncodingStep.SENDENCODING:
		{
			using IsolatedStorageFile isoStore2 = IsolatedStorageFile.GetUserStoreForApplication();
			string downloadedFile2 = "/shared/transfers/" + job.Id + "_responseupload";
			if (isoStore2.FileExists(downloadedFile2))
			{
				using (IsolatedStorageFileStream stream2 = isoStore2.OpenFile(downloadedFile2, FileMode.Open))
				{
					ManageServerAnswer(job, await new StreamReader(stream2).ReadToEndAsync());
				}
				isoStore2.DeleteFile(downloadedFile2);
			}
			break;
		}
		case EncodingStep.WAITINGENCODING:
		{
			using IsolatedStorageFile isoStore = IsolatedStorageFile.GetUserStoreForApplication();
			string downloadedFile = "/shared/transfers/" + job.Id + "_status.txt";
			if (isoStore.FileExists(downloadedFile))
			{
				string content = "";
				using (IsolatedStorageFileStream stream = isoStore.OpenFile(downloadedFile, FileMode.Open))
				{
					content = await new StreamReader(stream).ReadToEndAsync();
				}
				isoStore.DeleteFile(downloadedFile);
				ManageServerAnswer(job, content);
			}
			else
			{
				CheckIfEncoded(job);
			}
			break;
		}
		case EncodingStep.GETVIDEO:
			LaunchDownloadImage(job);
			break;
		case EncodingStep.GETIMAGE:
			UploadVideoToService(job);
			break;
		case EncodingStep.ENDED:
		case EncodingStep.ERROR:
			break;
		}
	}

	private static void ManageServerAnswer(EncodingJob job, string content)
	{
		if (content.StartsWith("ID\t"))
		{
			string uriStatus = content.Substring(3);
			job.State = EncodingStep.WAITINGENCODING;
			job.UriStatus = uriStatus;
			Data.Save();
			CheckIfEncoded(job);
		}
		else if (content.StartsWith("READY\t"))
		{
			string remoteMyServerJobId = content.Substring(6);
			job.RemoteMyServerJobId = remoteMyServerJobId;
			LaunchDownloadVideo(job);
		}
		else if (content.StartsWith("UPLOADED\t"))
		{
			string[] array = content.Split('\t');
			job.AmazonVideo = array[1];
			job.AmazonCapture = array[2];
			job.RemoteMyServerJobId = array[3];
			job.State = EncodingStep.READYTOPUBLISH;
			job.UpdateState();
			Data.Save();
		}
		else if (content.StartsWith("VIDUPLOADED\t"))
		{
			string[] array2 = content.Split('\t');
			job.AmazonVideo = array2[1];
			job.RemoteMyServerJobId = array2[2];
			UploadImageToService(job);
		}
		else
		{
			job.PreviousState = job.State;
			job.State = EncodingStep.ERROR;
			job.UpdateState();
			Data.Save();
		}
	}

	private static void CheckIfEncoded(EncodingJob job, int nbr = 0)
	{
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		//IL_002e: Expected O, but got Unknown
		BackgroundTransferRequest val = new BackgroundTransferRequest(new Uri(job.UriStatus + "&rand=" + DateTime.Now.Ticks, UriKind.Absolute));
		val.DownloadLocation = new Uri("/shared/transfers/" + job.Id + "_status.txt", UriKind.Relative);
		val.Tag = job.Id + "|" + 3;
		val.TransferPreferences = (TransferPreferences)3;
		AddTask(val);
		job.Register(val);
		Data.Save();
	}

	private static async Task LaunchEncoding(EncodingJob job, Dictionary<string, string> extraheaders = null)
	{
		try
		{
			BackgroundTransferRequest val = new BackgroundTransferRequest(new Uri(DatasProvider.Instance.ServerUri + "upload.php?did=" + AppUtils.GetDeviceUniqueID() + "&uid=" + HttpUtility.UrlEncode(DatasProvider.Instance.CurrentUser.User.key)));
			val.Tag = job.Id + "|" + 2;
			val.TransferPreferences = (TransferPreferences)3;
			val.DownloadLocation = new Uri("shared/transfers/" + job.Id + "_responseupload", UriKind.Relative);
			val.Method = "POST";
			val.UploadLocation = new Uri(job.LocalEncodingFile, UriKind.Relative);
			val.Headers["6Sec-type"] = (job.LocalEncodingFile.EndsWith(".zip") ? "zip" : "mp4");
			val.Headers["6Sec-privacy"] = (job.IsDirect ? "true" : "false");
			val.Headers.Add("6Sec-h", job.HashEncoding);
			if (extraheaders != null)
			{
				foreach (KeyValuePair<string, string> extraheader in extraheaders)
				{
					val.Headers.Add(extraheader.Key, extraheader.Value);
				}
			}
			try
			{
				AddTask(val);
				job.State = EncodingStep.SENDENCODING;
				job.UpdateState();
				job.Register(val);
				job.CurrentRequestId = val.RequestId;
				DatasProvider.Instance.Save();
			}
			catch
			{
			}
		}
		catch
		{
		}
	}

	public static async Task PrepareEncoding(EncodingJob job, IEnumerable<StorageFile> thefiles, StorageFile thumbFile)
	{
		StorageFolder _transfertFolder = await (await ApplicationData.Current.LocalFolder.GetFolderAsync("shared")).GetFolderAsync("transfers");
		job.LocalImagePath = (await thumbFile.CopyAsync((IStorageFolder)(object)_transfertFolder, job.Id + "_thumb.jpg", (NameCollisionOption)1)).Path;
		job.LocalEncodingFile = "/shared/transfers/" + job.Id + "_source.mp4";
		job.LocalEncodingFile = "/shared/transfers/" + job.Id + "_source.mp4";
		await thefiles.First().CopyAsync((IStorageFolder)(object)_transfertFolder, job.Id + "_source.mp4", (NameCollisionOption)1);
		job.LocalVideoPath = job.LocalEncodingFile;
		Data.Save();
	}

	private static async Task LaunchDownloadImage(EncodingJob job)
	{
		if (job.LocalImagePath != null)
		{
			try
			{
				using (await ((IStorageFile)(object)(await StorageFile.GetFileFromPathAsync(job.LocalImagePath))).OpenStreamForReadAsync())
				{
					job.State = EncodingStep.GETIMAGE;
					ManageState(job.State, job);
					return;
				}
			}
			catch
			{
			}
		}
		try
		{
			StorageFolder val = await (await ApplicationData.Current.LocalFolder.GetFolderAsync("shared")).GetFolderAsync("transfers");
			BackgroundTransferRequest val2 = new BackgroundTransferRequest(new Uri(job.RemoteMyServerJobId + "/capture.jpg", UriKind.Absolute));
			val2.Tag = job.Id + "|" + 7;
			job.LocalImagePath = Path.Combine(val.Path, job.Id + "_capture.jpg");
			val2.DownloadLocation = new Uri("/shared/transfers/" + job.Id + "_capture.jpg", UriKind.Relative);
			val2.TransferPreferences = (TransferPreferences)3;
			AddTask(val2);
			job.Register(val2);
			job.State = EncodingStep.GETIMAGE;
			Data.Save();
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				job.UpdateState();
			});
		}
		catch
		{
		}
	}

	private static void LaunchDownloadVideo(EncodingJob job)
	{
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		//IL_002e: Expected O, but got Unknown
		try
		{
			BackgroundTransferRequest val = new BackgroundTransferRequest(new Uri(job.RemoteMyServerJobId + "/video.mp4", UriKind.Absolute));
			job.LocalVideoPath = "/shared/transfers/" + job.Id + "_video.mp4";
			val.Tag = job.Id + "|" + 6;
			val.DownloadLocation = new Uri(job.LocalVideoPath, UriKind.Relative);
			val.TransferPreferences = (TransferPreferences)3;
			AddTask(val);
			job.Register(val);
			job.State = EncodingStep.GETVIDEO;
			Data.Save();
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				job.UpdateState();
			});
		}
		catch
		{
		}
	}

	public static async Task UploadVideoToService(EncodingJob job)
	{
		Vine.Datas.Datas Data = DatasProvider.Instance;
		DataUser dataUser = Data.Users.FirstOrDefault((DataUser u) => u.Id == job.UserId);
		if (dataUser == null)
		{
			return;
		}
		job.State = EncodingStep.UPLOADINGVIDEO;
		job.UpdateState();
		Data.Save();
		try
		{
			string amazonVideo = await dataUser.Service.UploadVideoAsync(job, null, "1.3");
			job.State = EncodingStep.UPLOADINGIMAGE;
			job.UpdateState();
			job.AmazonVideo = amazonVideo;
			Data.Save();
			UploadImageToService(job);
		}
		catch
		{
			job.PreviousState = job.State;
			job.State = EncodingStep.ERROR;
			job.UpdateState();
		}
	}

	private static async void UploadImageToService(EncodingJob job)
	{
		try
		{
			DataUser user = Data.Users.FirstOrDefault((DataUser u) => u.Id == job.UserId);
			if (user == null)
			{
				return;
			}
			job.State = EncodingStep.UPLOADINGIMAGE;
			job.UpdateState();
			Data.Save();
			try
			{
				string jobid = "1.3";
				StorageFile file = await StorageFile.GetFileFromPathAsync(job.LocalImagePath);
				string amazonCapture = await user.Service.UploadImageAsync(file, jobid);
				job.AmazonCapture = amazonCapture;
				job.State = EncodingStep.READYTOPUBLISH;
				job.UpdateState();
				Data.Save();
			}
			catch (ServiceServerErrorException)
			{
				job.PreviousState = job.State;
				job.State = EncodingStep.ERROR;
				job.UpdateState();
			}
		}
		catch
		{
		}
	}

	private static string ToString(byte[] p)
	{
		string text = "";
		foreach (byte b in p)
		{
			text += $"{b:x2}";
		}
		return text;
	}

	internal static async Task Start(EncodingJob _currentJob)
	{
		await LaunchEncoding(_currentJob);
	}
}
