using System.Threading;
using Vine.Pages.Direct.ViewModels;
using Vine.Pages.Explore.ViewModels;
using Vine.Pages.Search.ViewModels;
using Vine.Pages.SelectDirectFriends.ViewModels;
using Vine.Pages.Settings.ViewModels;

namespace Vine.ViewModels;

public class ViewModelLocator
{
	private static object lockData = new object();

	private static DirectViewModel _Direct;

	private static SearchViewModel _Search;

	private static OtherAppViewModel _OtherApp;

	private static SettingsViewModel _Settings;

	private static SelectDirectFriendsViewModel _SelectDirectFriends;

	private static ExploreViewModel _Explore;

	public static DirectViewModel DirectStatic
	{
		get
		{
			if (_Direct != null)
			{
				return _Direct;
			}
			bool lockTaken = false;
			object obj = default(object);
			try
			{
				obj = lockData;
				Monitor.Enter(obj, ref lockTaken);
				if (_Direct != null)
				{
					return _Direct;
				}
				_Direct = new DirectViewModel();
				return _Direct;
			}
			finally
			{
				if (lockTaken)
				{
					Monitor.Exit(obj);
				}
			}
		}
	}

	public DirectViewModel Direct => DirectStatic;

	public static SearchViewModel SearchStatic
	{
		get
		{
			if (_Search != null)
			{
				return _Search;
			}
			bool lockTaken = false;
			object obj = default(object);
			try
			{
				obj = lockData;
				Monitor.Enter(obj, ref lockTaken);
				if (_Search != null)
				{
					return _Search;
				}
				_Search = new SearchViewModel();
				return _Search;
			}
			finally
			{
				if (lockTaken)
				{
					Monitor.Exit(obj);
				}
			}
		}
	}

	public SearchViewModel Search => SearchStatic;

	public static OtherAppViewModel OtherAppStatic
	{
		get
		{
			if (_OtherApp != null)
			{
				return _OtherApp;
			}
			bool lockTaken = false;
			object obj = default(object);
			try
			{
				obj = lockData;
				Monitor.Enter(obj, ref lockTaken);
				if (_OtherApp != null)
				{
					return _OtherApp;
				}
				_OtherApp = new OtherAppViewModel();
				return _OtherApp;
			}
			finally
			{
				if (lockTaken)
				{
					Monitor.Exit(obj);
				}
			}
		}
	}

	public OtherAppViewModel OtherApp => OtherAppStatic;

	public static SettingsViewModel SettingsStatic
	{
		get
		{
			if (_Settings != null)
			{
				return _Settings;
			}
			_Settings = new SettingsViewModel();
			return _Settings;
		}
	}

	public SettingsViewModel Settings => SettingsStatic;

	public static SelectDirectFriendsViewModel SelectDirectFriendsStatic
	{
		get
		{
			if (_SelectDirectFriends != null)
			{
				return _SelectDirectFriends;
			}
			bool lockTaken = false;
			object obj = default(object);
			try
			{
				obj = lockData;
				Monitor.Enter(obj, ref lockTaken);
				if (_SelectDirectFriends != null)
				{
					return _SelectDirectFriends;
				}
				_SelectDirectFriends = new SelectDirectFriendsViewModel();
				return _SelectDirectFriends;
			}
			finally
			{
				if (lockTaken)
				{
					Monitor.Exit(obj);
				}
			}
		}
	}

	public SelectDirectFriendsViewModel SelectDirectFriends => SelectDirectFriendsStatic;

	public static ExploreViewModel ExploreStatic
	{
		get
		{
			if (_Explore != null)
			{
				return _Explore;
			}
			bool lockTaken = false;
			object obj = default(object);
			try
			{
				obj = lockData;
				Monitor.Enter(obj, ref lockTaken);
				if (_Explore != null)
				{
					return _Explore;
				}
				_Explore = new ExploreViewModel();
				return _Explore;
			}
			finally
			{
				if (lockTaken)
				{
					Monitor.Exit(obj);
				}
			}
		}
	}

	public ExploreViewModel Explore => ExploreStatic;

	internal static void Clear()
	{
		_Settings = null;
		_Search = null;
		_SelectDirectFriends = null;
		_Explore = null;
	}
}
