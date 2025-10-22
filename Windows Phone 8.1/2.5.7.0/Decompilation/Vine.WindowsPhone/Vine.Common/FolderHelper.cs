using System;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Vine.Models;
using Windows.Storage;
using Windows.Storage.FileProperties;

namespace Vine.Common;

public static class FolderHelper
{
	public const string VideoFolder = "video";

	public const string TileFolder = "tileTemp";

	public const string ConvoFolder = "convo";

	public const string SettingsExtFolder = "settingsExt";

	public const string CachedVinesFolder = "vineCache";

	public static StorageFolder PicturesLibrary => KnownFolders.PicturesLibrary;

	public static Task<StorageFolder> GetSettingsExtFolderAsync()
	{
		return GetChildFolderAsync(ApplicationData.Current.LocalFolder, "settingsExt");
	}

	public static Task<StorageFolder> GetTileFolderAsync()
	{
		return GetChildFolderAsync(ApplicationData.Current.LocalFolder, "tileTemp");
	}

	public static Task<StorageFolder> GetVideoFolderAsync()
	{
		return GetChildFolderAsync(ApplicationData.Current.LocalFolder, "video");
	}

	public static Task<StorageFolder> GetCachedVinesFolderAsync()
	{
		return GetChildFolderAsync(ApplicationData.Current.LocalFolder, "vineCache");
	}

	public static async Task<StorageFolder> GetActiveRecordingFolderAsync()
	{
		return await GetChildFolderAsync(await GetVideoFolderAsync(), "active");
	}

	public static Task<StorageFolder> GetConversationFolderAsync()
	{
		return GetChildFolderAsync(ApplicationData.Current.LocalFolder, "convo");
	}

	public static async Task<ConversationCacheModel> GetConversationInboxCache(bool primary)
	{
		StorageFile file = null;
		try
		{
			file = await StorageFile.GetFileFromPathAsync(ApplicationData.Current.LocalFolder.Path + "\\convo" + (primary ? "\\inbox" : "\\other") + ApplicationSettings.Current.UserId);
		}
		catch (FileNotFoundException)
		{
		}
		if (file != null)
		{
			ConversationCacheModel conversationCacheModel = Serialization.DeserializeType<ConversationCacheModel>(await FileIO.ReadTextAsync((IStorageFile)(object)file));
			if (conversationCacheModel != null)
			{
				conversationCacheModel.Conversations = conversationCacheModel.Conversations.OrderByDescending((ConversationViewModel x) => x.Record.Created).ToList();
				return conversationCacheModel;
			}
		}
		return new ConversationCacheModel();
	}

	public static async Task SaveConversationInboxCache(bool primary, ConversationCacheModel model)
	{
		StorageFile obj = await GetChildFileAsync(await GetConversationFolderAsync(), (primary ? "inbox" : "other") + ApplicationSettings.Current.UserId);
		string text = Serialization.SerializeType(model);
		await FileIO.WriteTextAsync((IStorageFile)(object)obj, text);
	}

	public static async Task<StorageFile> GetConversationThreadCacheFile(string userId)
	{
		StorageFile file = null;
		try
		{
			file = await StorageFile.GetFileFromPathAsync(ApplicationData.Current.LocalFolder.Path + "\\convo\\" + userId);
		}
		catch (FileNotFoundException)
		{
		}
		return file;
	}

	public static async Task<MessageCacheModel> GetConversationThreadCache(string userId)
	{
		StorageFile val = await GetConversationThreadCacheFile(userId);
		if (val != null)
		{
			MessageCacheModel messageCacheModel = Serialization.Deserialize<MessageCacheModel>(await FileIO.ReadTextAsync((IStorageFile)(object)val));
			if (messageCacheModel != null)
			{
				messageCacheModel.Messages = messageCacheModel.Messages.OrderByDescending((VineMessageViewModel x) => x.Model.Created).ToList();
				return messageCacheModel;
			}
		}
		return new MessageCacheModel();
	}

	public static async Task SaveConversationThreadCache(MessageCacheModel model, string otherUserId)
	{
		StorageFile obj = await GetChildFileAsync(await GetConversationFolderAsync(), otherUserId);
		string text = Serialization.Serialize(model);
		await FileIO.WriteTextAsync((IStorageFile)(object)obj, text);
	}

	public static async Task<StorageFile> GetChildFileAsync(StorageFolder folder, string name)
	{
		bool notFound = false;
		StorageFile file = null;
		try
		{
			file = await folder.GetFileAsync(name);
		}
		catch (FileNotFoundException)
		{
			notFound = true;
		}
		if (notFound)
		{
			file = await folder.CreateFileAsync(name);
		}
		return file;
	}

	public static async Task<StorageFolder> GetChildFolderAsync(StorageFolder parentFolder, string name)
	{
		bool notFound = false;
		StorageFolder folder = null;
		try
		{
			folder = await parentFolder.GetFolderAsync(name);
		}
		catch (FileNotFoundException)
		{
			notFound = true;
		}
		if (notFound)
		{
			folder = await parentFolder.CreateFolderAsync(name, (CreationCollisionOption)3);
		}
		return folder;
	}

	public static async Task<long> GetFolderSize(StorageFolder folder)
	{
		long folderSize = 0L;
		try
		{
			folderSize = (await Task.WhenAll((await folder.GetFilesAsync()).Select((StorageFile f) => f.GetBasicPropertiesAsync().AsTask<BasicProperties>()))).Sum((BasicProperties result) => (long)result.Size);
		}
		catch
		{
		}
		return folderSize;
	}
}
