using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Vine.Common;
using Vine.Framework;
using Windows.Graphics.Imaging;
using Windows.Media.Editing;
using Windows.Storage;
using Windows.Storage.Streams;

namespace Vine.Models;

public class RecordingVineModel
{
	public string DraftId { get; set; }

	public string UploadId { get; set; }

	public List<RecordingClipModel> Clips { get; set; }

	public string LastRenderedVideoFilePath { get; set; }

	public string LastRenderedThumbFilePath { get; set; }

	public int SavedClips { get; set; }

	public bool HasPendingChangesOnCapture => SavedClips != Clips.Count;

	public long Duration
	{
		get
		{
			if (!Clips.Any())
			{
				return 0L;
			}
			return Clips.Sum((RecordingClipModel clip) => clip.EditEndTime - clip.EditStartTime) + (Clips.Count - 1) * 2 * Clips[0].FrameRate;
		}
	}

	public bool IsClipsSequentialOneFile
	{
		get
		{
			if (Clips.Select((RecordingClipModel x) => x.VideoFilePath).Distinct().ToList()
				.Count != 1)
			{
				return false;
			}
			if (Clips.Last().FileEndTime != Duration)
			{
				return false;
			}
			for (int num = 1; num < Clips.Count; num++)
			{
				if (Clips[num - 1].FileStartTime > Clips[num].FileStartTime)
				{
					return false;
				}
			}
			return true;
		}
	}

	public RecordingVineModel()
	{
		Clips = new List<RecordingClipModel>();
	}

	public async Task<MediaComposition> GenerateMediaComposition()
	{
		MediaComposition mediaComposition = new MediaComposition();
		foreach (RecordingClipModel clipModel in Clips)
		{
			MediaClip val = await MediaClip.CreateFromFileAsync((IStorageFile)(object)(await StorageFile.GetFileFromPathAsync(clipModel.VideoFilePath)));
			clipModel.ToMediaClip(val);
			mediaComposition.Clips.Add(val);
		}
		return mediaComposition;
	}

	public async Task DiscardDraftChanges()
	{
		RecordingVineModel recordingVineModel = (await ApplicationSettings.Current.GetRecordingDrafts()).FirstOrDefault((RecordingVineModel x) => x.DraftId == DraftId);
		if (recordingVineModel == null)
		{
			return;
		}
		List<string> videos = new List<string>();
		for (int i = recordingVineModel.Clips.Count; i < Clips.Count; i++)
		{
			videos.Add(Clips[i].VideoFilePath);
			if (Clips[i].GhostFilePath != null)
			{
				await (await StorageFile.GetFileFromPathAsync(Clips[i].GhostFilePath)).DeleteAsync();
			}
		}
		videos = videos.Distinct().ToList();
		foreach (string item in videos)
		{
			await (await StorageFile.GetFileFromPathAsync(item)).DeleteAsync();
		}
	}

	public async Task DeleteAsync()
	{
		await (await GetFolder()).DeleteAsync();
		if (DraftId != null)
		{
			List<RecordingVineModel> list = await ApplicationSettings.Current.GetRecordingDrafts();
			list.RemoveAll((RecordingVineModel x) => x.DraftId == DraftId);
			await ApplicationSettings.Current.SetRecordingDrafts(list);
		}
	}

	public async Task SaveToDrafts()
	{
		if (DraftId == null)
		{
			string draftId = "D" + DateTime.UtcNow.Ticks.ToStringInvariantCulture();
			await RenameFolder(draftId);
			DraftId = draftId;
		}
		await SaveThumbAsync();
		SavedClips = Clips.Count;
		List<RecordingVineModel> list = await ApplicationSettings.Current.GetRecordingDrafts();
		list.RemoveAll((RecordingVineModel x) => x.DraftId == DraftId);
		RecordingVineModel item = Serialization.DeepCopy<RecordingVineModel>(this);
		list.Add(item);
		await ApplicationSettings.Current.SetRecordingDrafts(list);
	}

	private async Task RenameFolder(string newFolderName)
	{
		StorageFolder val = await GetFolder();
		string oldFolderName = val.Name;
		await val.RenameAsync(newFolderName);
		RenameProperties(oldFolderName, newFolderName);
	}

	private void RenameProperties(string oldFolderName, string newFolderName)
	{
		if (LastRenderedThumbFilePath != null)
		{
			LastRenderedThumbFilePath = LastRenderedThumbFilePath.Replace(oldFolderName, newFolderName);
		}
		LastRenderedVideoFilePath = LastRenderedVideoFilePath.Replace(oldFolderName, newFolderName);
		foreach (RecordingClipModel clip in Clips)
		{
			clip.GhostFilePath = clip.GhostFilePath.Replace(oldFolderName, newFolderName);
			clip.VideoFilePath = clip.VideoFilePath.Replace(oldFolderName, newFolderName);
		}
	}

	private async Task CopyToFolder(StorageFolder oldFolder, StorageFolder newFolder)
	{
		foreach (IStorageItem item in await oldFolder.GetItemsAsync())
		{
			StorageFile val = (StorageFile)(object)((item is StorageFile) ? item : null);
			if (val != null)
			{
				val.CopyAsync((IStorageFolder)(object)newFolder);
			}
		}
		RenameProperties(oldFolder.Name, newFolder.Name);
	}

	public static async Task<UploadJob> NewUploadMsgJob(RecordingVineModel vine, string conversationId)
	{
		RecordingVineModel uploadVine = Serialization.DeepCopy<RecordingVineModel>(vine);
		await uploadVine.SaveThumbAsync();
		string uploadId = "U" + DateTime.UtcNow.Ticks.ToStringInvariantCulture();
		StorageFolder oldFolder = await uploadVine.GetFolder();
		uploadVine.UploadId = uploadId;
		await uploadVine.CopyToFolder(oldFolder, await uploadVine.GetFolder());
		return new UploadJob
		{
			Id = uploadId,
			VideoPath = uploadVine.LastRenderedVideoFilePath,
			ThumbPath = uploadVine.LastRenderedThumbFilePath,
			ChannelId = "0",
			Vine = uploadVine,
			IsMessage = true,
			ReplyConversationId = conversationId
		};
	}

	public static async Task<UploadJob> NewUploadJob(RecordingVineModel vine, string text, string channel, bool isMessage)
	{
		RecordingVineModel uploadVine = Serialization.DeepCopy<RecordingVineModel>(vine);
		string uploadId = "U" + DateTime.UtcNow.Ticks.ToStringInvariantCulture();
		StorageFolder oldFolder = await uploadVine.GetFolder();
		uploadVine.UploadId = uploadId;
		await uploadVine.CopyToFolder(oldFolder, await uploadVine.GetFolder());
		return new UploadJob
		{
			Id = uploadId,
			VideoPath = uploadVine.LastRenderedVideoFilePath,
			ThumbPath = uploadVine.LastRenderedThumbFilePath,
			ChannelId = channel,
			Text = text,
			Vine = uploadVine,
			IsMessage = isMessage
		};
	}

	public async Task SaveThumbAsync()
	{
		RecordingClipModel clipModel = Clips.FirstOrDefault();
		if (clipModel == null)
		{
			return;
		}
		StorageFile obj = await StorageFile.GetFileFromPathAsync(clipModel.VideoFilePath);
		MediaComposition mediaComposition = new MediaComposition();
		MediaClip item = await MediaClip.CreateFromFileAsync((IStorageFile)(object)obj);
		mediaComposition.Clips.Add(item);
		ImageStream imgStream = await mediaComposition.GetThumbnailAsync(TimeSpan.FromTicks(clipModel.EditStartTime), 0, 0, (VideoFramePrecision)0);
		IRandomAccessStream val = await (await NewThumbRenderFile()).OpenAsync((FileAccessMode)1);
		using (Stream stream = ((IInputStream)(object)imgStream).AsStreamForRead())
		{
			using Stream destination = ((IOutputStream)(object)val).AsStreamForWrite();
			stream.CopyTo(destination);
			stream.Flush();
		}
		((IDisposable)val).Dispose();
		imgStream.Dispose();
	}

	public async Task SaveToCameraRollAsync()
	{
		await (await StorageFile.GetFileFromPathAsync(LastRenderedVideoFilePath)).CopyAsync((IStorageFolder)(object)KnownFolders.CameraRoll, "vine" + DateTime.Now.ToString("yy-MM-dd-hh-mm-ss") + ".mp4", (NameCollisionOption)0);
	}

	public async Task<StorageFile> NewVideoRenderFile()
	{
		StorageFile val = await (await GetFolder()).CreateFileAsync("vine.mp4", (CreationCollisionOption)0);
		LastRenderedVideoFilePath = val.Path;
		return val;
	}

	public async Task<StorageFile> NewVideoCaptureFile()
	{
		StorageFile val = await (await GetFolder()).CreateFileAsync("capture.mp4", (CreationCollisionOption)0);
		LastRenderedVideoFilePath = val.Path;
		return val;
	}

	public async Task<StorageFile> NewThumbRenderFile()
	{
		StorageFile val = await (await GetFolder()).CreateFileAsync("thumb.jpg", (CreationCollisionOption)1);
		LastRenderedThumbFilePath = val.Path;
		return val;
	}

	public async Task<StorageFile> NewGhostFile()
	{
		return await (await GetFolder()).CreateFileAsync("ghost.jpg", (CreationCollisionOption)0);
	}

	private async Task<StorageFolder> GetFolder()
	{
		return (UploadId != null) ? (await FolderHelper.GetChildFolderAsync(await FolderHelper.GetVideoFolderAsync(), UploadId)) : ((DraftId == null) ? (await FolderHelper.GetActiveRecordingFolderAsync()) : (await FolderHelper.GetChildFolderAsync(await FolderHelper.GetVideoFolderAsync(), DraftId)));
	}
}
