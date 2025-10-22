using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media;
using Gen.Services;
using Microsoft.Phone;
using Microsoft.Phone.Scheduler;
using Microsoft.Phone.Shell;
using Vine.Datas;
using Vine.Services;
using Vine.Services.Models;
using Vine.Services.Response;

namespace Vine.MyScheduledTaskAgent;

public class ScheduledAgent : ScheduledTaskAgent
{
	private int _nbrAction;

	private bool _allinit;

	static ScheduledAgent()
	{
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			Application.Current.UnhandledException += UnhandledException;
		});
	}

	private static void UnhandledException(object sender, ApplicationUnhandledExceptionEventArgs e)
	{
		if (Debugger.IsAttached)
		{
			Debugger.Break();
		}
	}

	protected override void OnInvoke(ScheduledTask task)
	{
		//IL_014c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0151: Unknown result type (might be due to invalid IL or missing references)
		//IL_0168: Unknown result type (might be due to invalid IL or missing references)
		//IL_0184: Expected O, but got Unknown
		Vine.Datas.Datas instance = DatasProvider.Instance;
		if (instance == null || (instance.PrimaryUser != null && instance.PrimaryUser.User == null))
		{
			((BackgroundAgent)this).NotifyComplete();
			return;
		}
		Random random = new Random((int)DateTime.Now.Ticks);
		List<ShellTile> list = (from d in ShellTile.ActiveTiles.Skip(1)
			where d.NavigationUri.OriginalString.StartsWith("/Timeline/") || d.NavigationUri.OriginalString.StartsWith("/Profile/")
			orderby random.Next()
			select d).ToList();
		ShellTile val = ShellTile.ActiveTiles.FirstOrDefault((ShellTile s) => s.NavigationUri.OriginalString == "/Main?entry=secondarytile");
		if (val != null)
		{
			list.Remove(val);
			_nbrAction++;
			UpdateOneTile(val);
		}
		foreach (ShellTile item in list.Take(3))
		{
			_nbrAction++;
			UpdateOneTile(item);
		}
		foreach (ShellTile item2 in list.Skip(3))
		{
			string distantTile = TileGenerator.GetDistantTile(item2.NavigationUri.OriginalString.Substring(10), "direct");
			item2.Update((ShellTileData)new FlipTileData
			{
				BackgroundImage = new Uri(distantTile + "&size=apollo"),
				WideBackgroundImage = new Uri(distantTile + "&size=apolloW")
			});
		}
		UpdateData(instance);
		_allinit = true;
		if (_nbrAction == 0)
		{
			((BackgroundAgent)this).NotifyComplete();
		}
	}

	protected async Task UpdateOneTile(ShellTile tile)
	{
		DataUser primaryUser = DatasProvider.Instance.PrimaryUser;
		if (primaryUser == null)
		{
			return;
		}
		if (tile.NavigationUri.OriginalString.StartsWith("/Profile/"))
		{
			string userid = tile.NavigationUri.OriginalString.Substring(9);
			string uri = UriServiceProvider.GetUriForUser(userid);
			try
			{
				IListPosts listPosts = await primaryUser.Service.TimelinesAsync(uri, null, 1);
				if (listPosts != null && listPosts.Posts.Count > 0)
				{
					string distantTile = TileGenerator.GetDistantTile(uri, listPosts.Posts[0].Thumb, "profile/" + tile.NavigationUri.OriginalString.Substring(9), "direct");
					tile.Update((ShellTileData)new FlipTileData
					{
						BackgroundImage = new Uri(distantTile + "&size=apollo"),
						WideBackgroundImage = new Uri(distantTile + "&size=apolloW")
					});
				}
				return;
			}
			catch
			{
				return;
			}
			finally
			{
				EndMethod();
			}
		}
		if (tile.NavigationUri.OriginalString.StartsWith("/Main?"))
		{
			try
			{
				IListPosts listPosts2 = await primaryUser.Service.TimelinesAsync(UriServiceProvider.GetUriForWall(), null, 1);
				if (listPosts2 != null && listPosts2.Posts.Count > 0)
				{
					IPostRecord postRecord = listPosts2.Posts.First();
					string distantTile2 = TileGenerator.GetDistantTile(UriServiceProvider.GetUriForUser(postRecord.UserId), postRecord.Thumb);
					tile.Update((ShellTileData)new FlipTileData
					{
						BackgroundImage = new Uri(distantTile2 + "&size=apollo"),
						WideBackgroundImage = new Uri(distantTile2 + "&size=apolloW")
					});
				}
				return;
			}
			catch
			{
				return;
			}
			finally
			{
				EndMethod();
			}
		}
		string tag = tile.NavigationUri.OriginalString.Substring(10);
		try
		{
			IListPosts listPosts3 = await primaryUser.Service.TimelinesAsync(tag, null, 1);
			if (listPosts3 != null && listPosts3.Posts.Count > 0)
			{
				string distantTile3 = TileGenerator.GetDistantTile(tag, listPosts3.Posts[0].Thumb);
				tile.Update((ShellTileData)new FlipTileData
				{
					BackgroundImage = new Uri(distantTile3 + "&size=apollo"),
					WideBackgroundImage = new Uri(distantTile3 + "&size=apolloW")
				});
			}
		}
		catch
		{
			EndMethod();
		}
	}

	private void EndMethod()
	{
		_nbrAction--;
		if (_allinit && _nbrAction == 0)
		{
			((BackgroundAgent)this).NotifyComplete();
		}
	}

	private async Task<PendingNotificationsInfo> UpdateTile(Vine.Datas.Datas data, bool isRetry = false)
	{
		DataUser primaryUser = data.PrimaryUser;
		ServiceServerErrorException _ex;
		try
		{
			PendingNotificationsInfo pendingNotificationsInfo = await primaryUser.Service.GetPendingNotificationsAsync();
			ShellTile obj = ShellTile.ActiveTiles.First();
			IconicTileData val = new IconicTileData();
			Color backgroundColor;
			if (!data.UseMyAccentColourForLiveTile)
			{
				Color val2 = default(Color);
				((Color)(ref val2)).A = byte.MaxValue;
				((Color)(ref val2)).R = 0;
				((Color)(ref val2)).G = 180;
				((Color)(ref val2)).B = 135;
				backgroundColor = val2;
			}
			else
			{
				backgroundColor = Colors.Transparent;
			}
			val.BackgroundColor = backgroundColor;
			val.Count = pendingNotificationsInfo.NbrMessages + pendingNotificationsInfo.NbrNotifications;
			obj.Update((ShellTileData)val);
			return pendingNotificationsInfo;
		}
		catch (ServiceServerErrorException ex)
		{
			_ex = ex;
		}
		if (_ex.ErrorType == "103" && !isRetry)
		{
			await primaryUser.Service.ReconnectAsync();
			return await UpdateTile(data, isRetry: true);
		}
		return null;
	}

	private async Task UpdateData(Vine.Datas.Datas data)
	{
		_nbrAction++;
		await UpdateTile(data);
		EndMethod();
	}
}
