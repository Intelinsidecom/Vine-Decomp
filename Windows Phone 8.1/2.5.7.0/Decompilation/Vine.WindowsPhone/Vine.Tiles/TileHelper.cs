using System;
using System.Threading.Tasks;
using Vine.Background;
using Vine.Common;
using Vine.Events;
using Vine.Framework;
using Vine.Models;
using Vine.Views;
using Vine.Views.Capture;
using Windows.Data.Xml.Dom;
using Windows.Storage;
using Windows.UI.Notifications;
using Windows.UI.StartScreen;
using Windows.UI.Xaml.Media.Imaging;

namespace Vine.Tiles;

public class TileHelper
{
	public static async Task CreateCapturePageTile(string tileId)
	{
		if (SecondaryTile.Exists(tileId))
		{
			throw new InvalidOperationException("trying to create a tile that already exists");
		}
		Uri uri = new Uri("ms-appx:///Assets/Tiles/Capture_Medium.png");
		string text = Serialization.Serialize(new LaunchParameters
		{
			Type = CaptureViewHelper.GetCaptureView().ToString()
		});
		SecondaryTile val = new SecondaryTile(tileId, ResourceHelper.GetString("VineMyCapture"), text, uri, (TileSize)3);
		val.VisualElements.put_ShowNameOnSquare150x150Logo(true);
		val.VisualElements.put_ForegroundText((ForegroundText)1);
		await val.RequestCreateAsync();
	}

	public static async Task CreateProfileTile(string profileId, string userName, string tileId)
	{
		if (SecondaryTile.Exists(tileId))
		{
			throw new InvalidOperationException("trying to create a tile that already exists");
		}
		Uri uri = new Uri("ms-appx:///Assets/Tiles/FlipCycleTileMedium.png");
		string text = Serialization.Serialize(new LaunchParameters
		{
			Type = typeof(ProfileView).ToString(),
			Data = profileId
		});
		SecondaryTile val = new SecondaryTile(tileId, string.Format("{0}{1}", new object[2]
		{
			ResourceHelper.GetString("VineTilePrefix"),
			userName
		}), text, uri, (TileSize)3);
		val.VisualElements.put_Square30x30Logo(new Uri("ms-appx:///Assets/Tiles/FlipCycleTileSmall.png"));
		val.VisualElements.put_Wide310x150Logo(new Uri("ms-appx:///Assets/Tiles/FlipCycleTileWide.png"));
		val.VisualElements.put_ShowNameOnSquare150x150Logo(true);
		await val.RequestCreateAsync();
		BgLiveTiles.UpdateProfileTile(profileId, tileId);
	}

	public static async Task CreateChannelTagTile(string channelName, VineListViewParams model, RenderTargetBitmap wideTile, string tileId)
	{
		if (SecondaryTile.Exists(tileId))
		{
			throw new InvalidOperationException("trying to create a tile that already exists");
		}
		StorageFile val = await ToTileFileAsync(wideTile);
		Uri uri = new Uri($"ms-appdata:///Local/tileTemp/{val.Name}");
		Uri uri2 = new Uri("ms-appx:///Assets/Tiles/FlipCycleTileMedium.png");
		string text = Serialization.Serialize(new LaunchParameters
		{
			Type = typeof(ChannelVineListView).ToString(),
			Data = Serialization.SerializeType(model)
		});
		SecondaryTile val2 = new SecondaryTile(tileId, string.Format("{0}{1}", new object[2]
		{
			ResourceHelper.GetString("VineTilePrefix"),
			channelName
		}), text, uri2, (TileSize)3);
		val2.VisualElements.put_Square30x30Logo(new Uri("ms-appx:///Assets/Tiles/FlipCycleTileSmall.png"));
		val2.VisualElements.put_Wide310x150Logo(uri);
		val2.VisualElements.put_ShowNameOnSquare150x150Logo(true);
		val2.VisualElements.put_ShowNameOnWide310x150Logo(true);
		val2.VisualElements.put_ForegroundText((ForegroundText)1);
		val2.VisualElements.put_BackgroundColor(Extensions.ColorHex(model.Color));
		await val2.RequestCreateAsync();
		TileUpdateManager.CreateTileUpdaterForSecondaryTile(tileId).Clear();
		TileUpdateManager.CreateTileUpdaterForSecondaryTile(tileId).EnableNotificationQueue(true);
		TileUpdater val3 = TileUpdateManager.CreateTileUpdaterForSecondaryTile(tileId);
		XmlDocument val4 = new XmlDocument();
		val4.LoadXml("<tile><visual version=\"2\"><binding template=\"TileSquare71x71Image\"><image id=\"1\" src=\"{0}\"/></binding><binding template=\"TileSquare150x150Image\" fallback=\"TileSquareImage\"><image id=\"1\" src=\"{0}\"/></binding></visual></tile>", model.Icon);
		TileNotification val5 = new TileNotification(val4);
		val5.put_Tag(model.ChannelId.Substring(0, Math.Min(model.ChannelId.Length, 16)));
		val3.Update(val5);
	}

	public static async Task CreateSearchTile(string tag, string searchDisplay, int pivotIndex, string tileId, RenderTargetBitmap smallTile)
	{
		if (SecondaryTile.Exists(tileId))
		{
			throw new InvalidOperationException("trying to create a tile that already exists");
		}
		Uri mediumLogo = new Uri("ms-appx:///Assets/Tiles/Tile_Search_Medium.png");
		StorageFile val = await ToTileFileAsync(smallTile);
		Uri uri = new Uri($"ms-appdata:///Local/tileTemp/{val.Name}");
		LaunchParameters obj = new LaunchParameters
		{
			Type = typeof(HomeView).ToString()
		};
		SearchView.SearchViewParams obj2 = new SearchView.SearchViewParams
		{
			Pivot = pivotIndex,
			SearchTerm = tag
		};
		obj.Data = Serialization.SerializeType(obj2);
		string text = Serialization.Serialize(obj);
		SecondaryTile val2 = new SecondaryTile(tileId, string.Format("{0}{1}", new object[2] { "    ", searchDisplay }), text, mediumLogo, (TileSize)3);
		val2.VisualElements.put_Square71x71Logo(uri);
		val2.VisualElements.put_ShowNameOnSquare150x150Logo(true);
		val2.VisualElements.put_ForegroundText((ForegroundText)1);
		await val2.RequestCreateAsync();
	}

	public static async Task CreateSpecificTagTile(string tag, string tileId, RenderTargetBitmap smallTile)
	{
		if (SecondaryTile.Exists(tileId))
		{
			throw new InvalidOperationException("trying to create a tile that already exists");
		}
		LaunchParameters launchParameters = new LaunchParameters();
		launchParameters.Type = typeof(TagVineListView).ToString();
		launchParameters.Data = tag;
		string tileActivationArguments = Serialization.Serialize(launchParameters);
		StorageFile val = await ToTileFileAsync(smallTile);
		Uri uri = new Uri($"ms-appdata:///Local/tileTemp/{val.Name}");
		SecondaryTile val2 = new SecondaryTile();
		val2.put_TileId(tileId);
		val2.put_DisplayName(string.Format("{0}#{1}", new object[2]
		{
			ResourceHelper.GetString("VineTilePrefix"),
			tag
		}));
		val2.put_Arguments(tileActivationArguments);
		val2.VisualElements.put_Square71x71Logo(uri);
		val2.VisualElements.put_Square150x150Logo(new Uri("ms-appx:///Assets/Tiles/FlipCycleTileMedium.png"));
		val2.VisualElements.put_ShowNameOnSquare150x150Logo(true);
		val2.VisualElements.put_Square30x30Logo(new Uri("ms-appx:///Assets/Tiles/FlipCycleTileSmall.png"));
		await val2.RequestCreateAsync();
		BgLiveTiles.UpdateSpecificTagTile(tag, tileId);
	}

	public static async Task CreatePopularNowTile(VineListViewParams listViewParams, RenderTargetBitmap smallTile, string tileId)
	{
		if (SecondaryTile.Exists(tileId))
		{
			throw new InvalidOperationException("trying to create a tile that already exists");
		}
		LaunchParameters launchParameters = new LaunchParameters();
		launchParameters.Type = typeof(TagVineListView).ToString();
		launchParameters.Data = Serialization.SerializeType(listViewParams);
		string tileActivationArguments = Serialization.Serialize(launchParameters);
		StorageFile val = await ToTileFileAsync(smallTile);
		Uri uri = new Uri($"ms-appdata:///Local/tileTemp/{val.Name}");
		SecondaryTile val2 = new SecondaryTile();
		val2.put_TileId(tileId);
		val2.put_DisplayName(string.Format("{0}{1}", new object[2]
		{
			ResourceHelper.GetString("VineTilePrefix"),
			ResourceHelper.GetString("PopularNow")
		}));
		val2.put_Arguments(tileActivationArguments);
		val2.VisualElements.put_Square150x150Logo(new Uri("ms-appx:///Assets/Tiles/FlipCycleTileMedium.png"));
		val2.VisualElements.put_Wide310x150Logo(new Uri("ms-appx:///Assets/Tiles/FlipCycleTileWide.png"));
		val2.VisualElements.put_ShowNameOnSquare150x150Logo(true);
		val2.VisualElements.put_Square71x71Logo(uri);
		await val2.RequestCreateAsync();
		BgLiveTiles.UpdatePopularNowTile(tileId);
	}

	public static async Task CreateOnTheRiseTile(VineListViewParams listViewParams, RenderTargetBitmap smallTile, string tileId)
	{
		if (SecondaryTile.Exists(tileId))
		{
			throw new InvalidOperationException("trying to create a tile that already exists");
		}
		LaunchParameters launchParameters = new LaunchParameters();
		launchParameters.Type = typeof(TagVineListView).ToString();
		launchParameters.Data = Serialization.SerializeType(listViewParams);
		string tileActivationArguments = Serialization.Serialize(launchParameters);
		StorageFile val = await ToTileFileAsync(smallTile);
		Uri uri = new Uri($"ms-appdata:///Local/tileTemp/{val.Name}");
		SecondaryTile val2 = new SecondaryTile();
		val2.put_TileId(tileId);
		val2.put_DisplayName(string.Format("{0}{1}", new object[2]
		{
			ResourceHelper.GetString("VineTilePrefix"),
			ResourceHelper.GetString("OnTheRise")
		}));
		val2.put_Arguments(tileActivationArguments);
		val2.VisualElements.put_Square150x150Logo(new Uri("ms-appx:///Assets/Tiles/FlipCycleTileMedium.png"));
		val2.VisualElements.put_Wide310x150Logo(new Uri("ms-appx:///Assets/Tiles/FlipCycleTileWide.png"));
		val2.VisualElements.put_ShowNameOnSquare150x150Logo(true);
		val2.VisualElements.put_Square71x71Logo(uri);
		await val2.RequestCreateAsync();
		BgLiveTiles.UpdateOnTheRiseTile(tileId);
	}

	public static async Task DeleteSecondaryTile(string tileId)
	{
		if (!SecondaryTile.Exists(tileId))
		{
			throw new InvalidOperationException("trying to delete a tile that does not exists");
		}
		await new SecondaryTile(tileId).RequestDeleteAsync();
	}

	public static async Task<StorageFile> ToTileFileAsync(RenderTargetBitmap bitmap)
	{
		StorageFile file = await (await FolderHelper.GetTileFolderAsync()).CreateFileAsync("Tile", (CreationCollisionOption)0);
		await bitmap.ToFileAsync(file);
		return file;
	}
}
