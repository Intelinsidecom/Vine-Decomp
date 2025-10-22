using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Windows.System;
using Windows.UI;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Media;

namespace Vine.Test;

public static class MemoryDiagnosticsHelper
{
	private static Popup popup;

	private static InstanceCountPopupControl counterPopup;

	private static TextBlock currentMemoryBlock;

	private static TextBlock peakMemoryBlock;

	private static DispatcherTimer timer;

	private static bool forceGc;

	private static int lastSafetyBand = -1;

	private const long MAX_CHECKPOINTS = 10L;

	private static Queue<MemoryCheckpoint> recentCheckpoints;

	private static bool alreadyFailedPeak = false;

	private static long _maxMemory;

	public static IEnumerable<MemoryCheckpoint> RecentCheckpoints
	{
		get
		{
			if (recentCheckpoints == null)
			{
				yield break;
			}
			foreach (MemoryCheckpoint recentCheckpoint in recentCheckpoints)
			{
				yield return recentCheckpoint;
			}
		}
	}

	public static void Start(TimeSpan timespan, bool forceGc)
	{
		if (timer == null)
		{
			_maxMemory = (long)MemoryManager.AppMemoryUsageLimit;
			MemoryDiagnosticsHelper.forceGc = forceGc;
			recentCheckpoints = new Queue<MemoryCheckpoint>();
			StartTimer(timespan);
			ShowPopup();
		}
	}

	public static void Stop()
	{
		HidePopup();
		StopTimer();
		recentCheckpoints = null;
	}

	public static void Checkpoint(string text)
	{
		if (recentCheckpoints != null)
		{
			if ((long)recentCheckpoints.Count >= 9L)
			{
				recentCheckpoints.Dequeue();
			}
			recentCheckpoints.Enqueue(new MemoryCheckpoint(text, GetCurrentMemoryUsage()));
		}
	}

	public static long GetCurrentMemoryUsage()
	{
		return 0L;
	}

	public static long GetPeakMemoryUsage()
	{
		return 0L;
	}

	private static void ShowPopup()
	{
		//IL_0014: Unknown result type (might be due to invalid IL or missing references)
		//IL_001e: Expected O, but got Unknown
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0033: Expected O, but got Unknown
		//IL_0033: Unknown result type (might be due to invalid IL or missing references)
		//IL_0038: Unknown result type (might be due to invalid IL or missing references)
		//IL_003f: Unknown result type (might be due to invalid IL or missing references)
		//IL_004f: Expected O, but got Unknown
		//IL_004f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0054: Unknown result type (might be due to invalid IL or missing references)
		//IL_005b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0062: Unknown result type (might be due to invalid IL or missing references)
		//IL_0068: Unknown result type (might be due to invalid IL or missing references)
		//IL_0072: Expected O, but got Unknown
		//IL_0072: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a2: Expected O, but got Unknown
		//IL_00a2: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b2: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b9: Unknown result type (might be due to invalid IL or missing references)
		//IL_00c5: Expected O, but got Unknown
		//IL_00c5: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ca: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d5: Unknown result type (might be due to invalid IL or missing references)
		//IL_00dc: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e3: Unknown result type (might be due to invalid IL or missing references)
		//IL_0117: Expected O, but got Unknown
		//IL_012d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0132: Unknown result type (might be due to invalid IL or missing references)
		//IL_013d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0144: Unknown result type (might be due to invalid IL or missing references)
		//IL_0150: Expected O, but got Unknown
		counterPopup = new InstanceCountPopupControl();
		counterPopup.Open();
		popup = new Popup();
		double num = 17.0;
		Brush val = (Brush)new SolidColorBrush(Colors.Red);
		Grid val2 = new Grid();
		((FrameworkElement)val2).put_HorizontalAlignment((HorizontalAlignment)3);
		((FrameworkElement)val2).put_Width(400.0);
		Grid val3 = val2;
		StackPanel val4 = new StackPanel();
		val4.put_Orientation((Orientation)1);
		((FrameworkElement)val4).put_HorizontalAlignment((HorizontalAlignment)2);
		((Panel)val4).put_Background((Brush)new SolidColorBrush(Colors.Transparent));
		((FrameworkElement)val4).put_Margin(new Thickness(0.0, 29.0, 24.0, 0.0));
		StackPanel val5 = val4;
		TextBlock val6 = new TextBlock();
		val6.put_Text("---");
		val6.put_FontSize(num);
		val6.put_Foreground(val);
		currentMemoryBlock = val6;
		TextBlock val7 = new TextBlock();
		val7.put_Text("");
		val7.put_FontSize(num);
		val7.put_Foreground(val);
		((FrameworkElement)val7).put_Margin(new Thickness(5.0, 0.0, 0.0, 0.0));
		peakMemoryBlock = val7;
		((ICollection<UIElement>)((Panel)val5).Children).Add((UIElement)(object)currentMemoryBlock);
		UIElementCollection children = ((Panel)val5).Children;
		TextBlock val8 = new TextBlock();
		val8.put_Text(" MB");
		val8.put_FontSize(num);
		val8.put_Foreground(val);
		((ICollection<UIElement>)children).Add((UIElement)val8);
		((ICollection<UIElement>)((Panel)val5).Children).Add((UIElement)(object)peakMemoryBlock);
		((ICollection<UIElement>)((Panel)val3).Children).Add((UIElement)(object)val5);
		popup.put_Child((UIElement)(object)val3);
		popup.put_IsOpen(true);
	}

	private static void StartTimer(TimeSpan timespan)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_000a: Expected O, but got Unknown
		timer = new DispatcherTimer();
		timer.put_Interval(timespan);
		DispatcherTimer val = timer;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)val.add_Tick, (Action<EventRegistrationToken>)val.remove_Tick, (EventHandler<object>)timer_Tick);
		timer.Start();
	}

	private static void timer_Tick(object sender, object e)
	{
		if (forceGc)
		{
			GC.Collect();
		}
		DispatcherEx.BeginInvoke(delegate
		{
			UpdateCurrentMemoryUsage();
			UpdatePeakMemoryUsage();
		});
	}

	private static void UpdatePeakMemoryUsage()
	{
		//IL_003d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0047: Expected O, but got Unknown
		if (!alreadyFailedPeak && GetPeakMemoryUsage() >= _maxMemory)
		{
			alreadyFailedPeak = true;
			Checkpoint("*MEMORY USAGE FAIL*");
			peakMemoryBlock.put_Text("FAIL!");
			peakMemoryBlock.put_Foreground((Brush)new SolidColorBrush(Colors.Red));
			_ = Debugger.IsAttached;
		}
	}

	private static void UpdateCurrentMemoryUsage()
	{
		long currentMemoryUsage = GetCurrentMemoryUsage();
		currentMemoryBlock.put_Text($"{(double)currentMemoryUsage / 1048576.0:N1}");
		int safetyBand = GetSafetyBand(currentMemoryUsage);
		if (safetyBand != lastSafetyBand)
		{
			currentMemoryBlock.put_Foreground(GetBrushForSafetyBand(safetyBand));
			lastSafetyBand = safetyBand;
		}
	}

	private static Brush GetBrushForSafetyBand(int safetyBand)
	{
		//IL_000e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0014: Expected O, but got Unknown
		//IL_0019: Unknown result type (might be due to invalid IL or missing references)
		//IL_001f: Expected O, but got Unknown
		//IL_0024: Unknown result type (might be due to invalid IL or missing references)
		//IL_002a: Expected O, but got Unknown
		return (Brush)(safetyBand switch
		{
			0 => (object)new SolidColorBrush(Colors.Green), 
			1 => (object)new SolidColorBrush(Colors.Orange), 
			_ => (object)new SolidColorBrush(Colors.Red), 
		});
	}

	private static int GetSafetyBand(long mem)
	{
		double num = (double)mem / (double)_maxMemory;
		if (num <= 0.75)
		{
			return 0;
		}
		if (num <= 0.9)
		{
			return 1;
		}
		return 2;
	}

	private static void StopTimer()
	{
		timer.Stop();
		timer = null;
	}

	private static void HidePopup()
	{
		popup.put_IsOpen(false);
		popup = null;
	}

	[Conditional("DEBUG")]
	public static void IncrementInstanceCounter(Type type)
	{
		_ = counterPopup;
	}

	[Conditional("DEBUG")]
	public static void DecrementInstanceCounter(Type type)
	{
		_ = counterPopup;
	}
}
