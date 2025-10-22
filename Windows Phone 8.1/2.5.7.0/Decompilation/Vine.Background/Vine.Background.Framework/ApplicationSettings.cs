using System;
using System.Threading.Tasks;
using Vine.Background.Models;

namespace Vine.Background.Framework;

internal sealed class ApplicationSettings : ApplicationSettingsBase
{
	private static ApplicationSettings _current;

	private VineAuthToken _vineSession;

	private string _clientVersion;

	public static extern ApplicationSettings Current { get; }

	public extern VineAuthToken VineSession { get; set; }

	public extern TimeSpan? ServerOffset { get; }

	public extern bool IsNotLoggedIn { get; }

	public extern string ClientVersion { get; }

	public extern string LastNotificationToastId { get; set; }

	public extern string LatestMessageToastId { get; set; }

	public extern Task<long> GetBgTaskInvokeCount();

	public extern Task SetBgTaskInvokeCount(long count);

	public extern Task<long> GetBgTaskCompleteCount();

	public extern Task SetBgTaskCompleteCount(long count);

	public extern ApplicationSettings();
}
