using System;
using System.Threading.Tasks;
using Windows.Foundation;
using Windows.Networking.PushNotifications;

namespace Vine.Framework;

public class PushHelper
{
	public static async Task<string> GetChannel()
	{
		IAsyncOperation<PushNotificationChannel> source = PushNotificationChannelManager.CreatePushNotificationChannelForApplicationAsync();
		try
		{
			return (await source.AsTask<PushNotificationChannel>()).Uri;
		}
		catch (Exception)
		{
		}
		return null;
	}
}
