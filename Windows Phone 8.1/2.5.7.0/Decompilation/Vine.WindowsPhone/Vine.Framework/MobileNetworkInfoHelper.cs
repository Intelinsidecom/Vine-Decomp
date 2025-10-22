using System;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using CellNative;
using Vine.Models;

namespace Vine.Framework;

public class MobileNetworkInfoHelper
{
	[Serializable]
	[CompilerGenerated]
	private sealed class _003C_003Ec
	{
		public static readonly _003C_003Ec _003C_003E9 = new _003C_003Ec();

		public static IMSICompletionHandler _003C_003E9__1_0;

		public static ConfigurationChangedEventHandler _003C_003E9__1_1;

		public static Action _003C_003E9__1_2;

		public static Action _003C_003E9__2_0;

		internal void _003CInitializeAsync_003Eb__1_0(object s, object o)
		{
			uint mCC = _cellcore.GetMCC();
			uint mNC = _cellcore.GetMNC();
			if (mCC != 0)
			{
				ApplicationSettings.Current.MCC = mCC.ToString();
				ApplicationSettings.Current.MNC = mNC.ToString();
			}
		}

		internal void _003CInitializeAsync_003Eb__1_1(object sender, object o)
		{
			_cellcore.GetConfigurationAsString();
		}

		internal void _003CInitializeAsync_003Eb__1_2()
		{
			_cellcore.Initialize();
		}

		internal void _003CShutdownAsync_003Eb__2_0()
		{
			_cellcore.Shutdown();
		}
	}

	private static Cellcore _cellcore;

	public static async Task InitializeAsync()
	{
		if (_cellcore != null || !string.IsNullOrEmpty(ApplicationSettings.Current.MCC))
		{
			return;
		}
		try
		{
			_cellcore = Cellcore.Factory();
			Cellcore cellcore = _cellcore;
			Func<IMSICompletionHandler, EventRegistrationToken> addMethod = cellcore.add_IMSICompleted;
			Action<EventRegistrationToken> removeMethod = cellcore.remove_IMSICompleted;
			object obj = _003C_003Ec._003C_003E9__1_0;
			if (obj == null)
			{
				IMSICompletionHandler val = delegate
				{
					uint mCC = _cellcore.GetMCC();
					uint mNC = _cellcore.GetMNC();
					if (mCC != 0)
					{
						ApplicationSettings.Current.MCC = mCC.ToString();
						ApplicationSettings.Current.MNC = mNC.ToString();
					}
				};
				_003C_003Ec._003C_003E9__1_0 = val;
				obj = (object)val;
			}
			WindowsRuntimeMarshal.AddEventHandler(addMethod, removeMethod, (IMSICompletionHandler)obj);
			cellcore = _cellcore;
			Func<ConfigurationChangedEventHandler, EventRegistrationToken> addMethod2 = cellcore.add_ConfigurationChanged;
			Action<EventRegistrationToken> removeMethod2 = cellcore.remove_ConfigurationChanged;
			object obj2 = _003C_003Ec._003C_003E9__1_1;
			if (obj2 == null)
			{
				ConfigurationChangedEventHandler val2 = delegate
				{
					_cellcore.GetConfigurationAsString();
				};
				_003C_003Ec._003C_003E9__1_1 = val2;
				obj2 = (object)val2;
			}
			WindowsRuntimeMarshal.AddEventHandler(addMethod2, removeMethod2, (ConfigurationChangedEventHandler)obj2);
			await Task.Run(delegate
			{
				_cellcore.Initialize();
			});
		}
		catch (Exception)
		{
		}
	}

	public static async Task ShutdownAsync()
	{
		if (_cellcore == null)
		{
			return;
		}
		try
		{
			await Task.Run(delegate
			{
				_cellcore.Shutdown();
			});
			_cellcore = null;
		}
		catch (Exception)
		{
		}
	}
}
