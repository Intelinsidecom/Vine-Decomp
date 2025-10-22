using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using Gen.Services;
using Microsoft.Phone.Info;
using Microsoft.Phone.Shell;
using Vine.Datas;
using Vine.Services;
using Vine.Services.Models;
using Windows.Storage;

namespace Vine.Utils;

public class AppUtils
{
	private static NumberFormatInfo nfi = new NumberFormatInfo
	{
		NumberDecimalSeparator = "."
	};

	private static string appid = "lqyaExjqTV5RDtoPQBwV";

	private static string token = "FrW90uwwhzz4U-dn0_wyfw";

	public static async Task ClearVideoFiles()
	{
		try
		{
			StorageFolder _dataFolder;
			try
			{
				_dataFolder = await ApplicationData.Current.LocalFolder.GetFolderAsync("mpegpart");
			}
			catch
			{
				return;
			}
			IReadOnlyList<StorageFile> thefiles = await _dataFolder.GetFilesAsync();
			for (int i = 0; i < thefiles.Count; i++)
			{
				await thefiles[i].DeleteAsync((StorageDeleteOption)0);
			}
		}
		catch
		{
		}
		DatasProvider.Instance.Save();
	}

	public static string GenerateHereMap(double lat, double lng)
	{
		return string.Format("http://m.nok.it/?app_id=" + appid + "&app_code=" + token + "&c={0},{1}&nord&h=120&w=480&z=17&t=6&nodot=true", lat.ToString(nfi), lng.ToString(nfi));
	}

	public static string GetDeviceUniqueID()
	{
		object obj = default(object);
		if (DeviceExtendedProperties.TryGetValue("DeviceUniqueId", ref obj))
		{
			return BitConverter.ToString((byte[])obj).Replace("-", "");
		}
		return "";
	}

	public static void CreateProfileTile(IProfile profile, IPostRecord post)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0018: Expected O, but got Unknown
		//IL_00ae: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c9: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e4: Expected O, but got Unknown
		FlipTileData val = new FlipTileData
		{
			Title = profile.GetPublicName()
		};
		((StandardTileData)val).BackgroundImage = new Uri("/Assets/Tiles/DarkTile.png", UriKind.Relative);
		val.WideBackgroundImage = new Uri("/Assets/Tiles/WideDarkTile.png", UriKind.Relative);
		Uri uri = new Uri("/Profile/" + profile.Id, UriKind.Relative);
		ShellTile.Create(uri, (ShellTileData)(object)val, true);
		if (post != null)
		{
			string distantTile = TileGenerator.GetDistantTile(UriServiceProvider.GetUriForUser(profile.Id), post.Thumb, "profile/" + profile.Id, profile.Picture);
			ShellTile val2 = ShellTile.ActiveTiles.FirstOrDefault((ShellTile t) => t.NavigationUri == uri);
			if (val2 != null)
			{
				val2.Update((ShellTileData)new FlipTileData
				{
					BackgroundImage = new Uri(distantTile + "&size=apollo"),
					WideBackgroundImage = new Uri(distantTile + "&size=apolloW")
				});
			}
		}
	}

	public static void CreateHomeTile(IPostRecord post)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0017: Expected O, but got Unknown
		//IL_008e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0093: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a9: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c4: Expected O, but got Unknown
		FlipTileData val = new FlipTileData
		{
			Title = "Vine"
		};
		((StandardTileData)val).BackgroundImage = new Uri("/Assets/Tiles/DarkTile.png", UriKind.Relative);
		val.WideBackgroundImage = new Uri("/Assets/Tiles/WideDarkTile.png", UriKind.Relative);
		Uri uri = new Uri("/Main?entry=secondarytile", UriKind.Relative);
		ShellTile.Create(uri, (ShellTileData)(object)val, true);
		if (post != null)
		{
			string distantTile = TileGenerator.GetDistantTile(UriServiceProvider.GetUriForUser(post.UserId), post.Thumb);
			ShellTile val2 = ShellTile.ActiveTiles.FirstOrDefault((ShellTile t) => t.NavigationUri == uri);
			if (val2 != null)
			{
				val2.Update((ShellTileData)new FlipTileData
				{
					BackgroundImage = new Uri(distantTile + "&size=apollo"),
					WideBackgroundImage = new Uri(distantTile + "&size=apolloW")
				});
			}
		}
	}
}
