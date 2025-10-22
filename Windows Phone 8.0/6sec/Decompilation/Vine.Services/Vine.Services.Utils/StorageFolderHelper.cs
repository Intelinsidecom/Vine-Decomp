using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using Windows.Storage;

namespace Vine.Services.Utils;

public static class StorageFolderHelper
{
	[DebuggerHidden]
	public static async Task<bool> FileExistsAsync(this StorageFolder folder, string filepath)
	{
		_ = 1;
		try
		{
			await folder.GetFileAsync(filepath);
			return true;
		}
		catch
		{
			return false;
		}
	}

	[DebuggerHidden]
	public static async Task<bool> FolderExistsAsync(this StorageFolder folder, string folderpath)
	{
		_ = 1;
		try
		{
			await folder.GetFolderAsync(folderpath);
			return true;
		}
		catch
		{
			return false;
		}
	}

	public static async Task DeleteFolderRecursiveAsync(this StorageFolder folder, bool onlyFiles = false)
	{
		try
		{
			foreach (StorageFile item in await folder.GetFilesAsync())
			{
				await item.DeleteAsync();
			}
			foreach (StorageFolder subFolder in await folder.GetFoldersAsync())
			{
				await subFolder.DeleteFolderRecursiveAsync();
				if (!onlyFiles)
				{
					await subFolder.DeleteAsync();
				}
			}
			if (!onlyFiles)
			{
				await folder.DeleteAsync();
			}
		}
		catch
		{
		}
	}

	public static async Task<StorageFolder> CreateFolderWithSubFoldersAsync(this StorageFolder currentFolder, string path)
	{
		IEnumerable<string> splittedPath = from p in path.Split('\\', '/')
			where !string.IsNullOrEmpty(p)
			select p;
		return await currentFolder.CreateFolderWithSubFoldersAsync(splittedPath);
	}

	public static async Task<StorageFolder> CreateFolderWithSubFoldersAsync(this StorageFolder currentFolder, IEnumerable<string> splittedPath)
	{
		StorageFolder val = currentFolder;
		foreach (string item in splittedPath)
		{
			StorageFolder val2 = await val.CreateFolderAsync(item, (CreationCollisionOption)3);
			if (val2 == null)
			{
				return null;
			}
			val = val2;
		}
		return val;
	}

	public static async Task<StorageFile> CreateFileWithSubFoldersAsync(this StorageFolder currentFolder, string path, CreationCollisionOption option)
	{
		//IL_0012: Unknown result type (might be due to invalid IL or missing references)
		//IL_0013: Unknown result type (might be due to invalid IL or missing references)
		IEnumerable<string> splittedPath = from p in path.Split('\\', '/')
			where !string.IsNullOrEmpty(p)
			select p;
		StorageFolder val = await currentFolder.CreateFolderWithSubFoldersAsync(splittedPath.Take(splittedPath.Count() - 1));
		if (val == null)
		{
			return null;
		}
		return await val.CreateFileAsync(splittedPath.Last(), option);
	}
}
