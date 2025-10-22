using System;
using System.Collections.Generic;
using System.Threading;
using System.Windows.Threading;

namespace Vine;

public class TimerHelper
{
	private static Dictionary<Action, Timer> timers = new Dictionary<Action, Timer>();

	public static void ToTime(TimeSpan timespan, Action callback)
	{
		//IL_000e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0013: Unknown result type (might be due to invalid IL or missing references)
		//IL_001f: Expected O, but got Unknown
		DispatcherTimer dispatcher = new DispatcherTimer
		{
			Interval = timespan
		};
		dispatcher.Tick += delegate
		{
			dispatcher.Stop();
			callback();
		};
		dispatcher.Start();
	}

	public static void ToTimeNoUI(TimeSpan timespan, Action callback)
	{
		try
		{
			Timer timer = new Timer(TimerCallback, callback, -1, -1);
			timers[callback] = timer;
			timer.Change(timespan, TimeSpan.FromMilliseconds(-1.0));
		}
		catch
		{
		}
	}

	private static void TimerCallback(object state)
	{
		Action action = (Action)state;
		try
		{
			timers.Remove(action);
		}
		catch
		{
		}
		action();
	}
}
