using System;
using System.Runtime.InteropServices;
using System.Threading.Tasks;
using Windows.ApplicationModel.Chat;

namespace Vine.Special;

public static class ReservedEx
{
	private const int URLMON_OPTION_USERAGENT = 268435457;

	public static async Task<bool> CanSendSmsMessage()
	{
		try
		{
			foreach (ChatMessageTransport item in await ChatMessageManager.GetTransportsAsync())
			{
				if (item.IsActive)
				{
					return true;
				}
			}
		}
		catch (Exception)
		{
		}
		return false;
	}

	public static async Task SendSmsMessage(ChatMessage msg)
	{
		await (await ChatMessageManager.RequestStoreAsync()).SendMessageAsync(msg);
	}

	public static void UserAgent(string userAgent)
	{
		UrlMkSetSessionOption(268435457, userAgent, userAgent.Length, 0);
	}

	[DllImport("urlmon.dll", CharSet = CharSet.Ansi)]
	private static extern int UrlMkSetSessionOption(int dwOption, string pBuffer, int dwBufferLength, int dwReserved);
}
