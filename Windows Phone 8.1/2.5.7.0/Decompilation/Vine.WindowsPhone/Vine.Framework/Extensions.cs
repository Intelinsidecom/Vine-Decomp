using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Vine.Common;
using Vine.Web;
using Windows.ApplicationModel.Background;
using Windows.Data.Xml.Dom;
using Windows.Graphics.Imaging;
using Windows.Storage;
using Windows.Storage.Streams;
using Windows.UI;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Media.Imaging;
using Windows.UI.Xaml.Navigation;

namespace Vine.Framework;

public static class Extensions
{
	public static bool IsActivePage(this Page page)
	{
		if (App.RootFrame != null && ((ContentControl)App.RootFrame).Content != null)
		{
			return ((ContentControl)App.RootFrame).Content.Equals(page);
		}
		return false;
	}

	public static void NavigateWithObject(this Frame frame, Type sourcePageType, object param)
	{
		string text = Serialization.SerializeType(param);
		frame.Navigate(sourcePageType, (object)text);
	}

	public static void GoBackTo(this Frame frame, Type sourcePageType)
	{
		IList<PageStackEntry> backStack = App.RootFrame.BackStack;
		int num = backStack.Count - 1;
		while (num >= 0 && (object)backStack[num].SourcePageType != sourcePageType && num != 0)
		{
			backStack.RemoveAt(num);
			num--;
		}
		App.RootFrame.GoBack();
	}

	public static async Task ToFileAsync(this RenderTargetBitmap bitmap, StorageFile file)
	{
		IBuffer pixels = await bitmap.GetPixelsAsync();
		IRandomAccessStream stream = await file.OpenAsync((FileAccessMode)1);
		try
		{
			BitmapEncoder obj = await BitmapEncoder.CreateAsync(BitmapEncoder.BmpEncoderId, stream);
			byte[] array = pixels.ToArray();
			obj.SetPixelData((BitmapPixelFormat)87, (BitmapAlphaMode)1, (uint)bitmap.PixelWidth, (uint)bitmap.PixelHeight, 96.0, 96.0, array);
			await obj.FlushAsync();
		}
		finally
		{
			((IDisposable)stream)?.Dispose();
		}
	}

	public static async Task ToFileAsync(this WriteableBitmap bitmap, StorageFile file)
	{
		IRandomAccessStream stream = await file.OpenAsync((FileAccessMode)1);
		try
		{
			BitmapEncoder encoder = await BitmapEncoder.CreateAsync(BitmapEncoder.JpegEncoderId, stream);
			Stream stream2 = bitmap.PixelBuffer.AsStream();
			byte[] pixels = new byte[stream2.Length];
			await stream2.ReadAsync(pixels, 0, pixels.Length);
			encoder.SetPixelData((BitmapPixelFormat)87, (BitmapAlphaMode)2, (uint)((BitmapSource)bitmap).PixelWidth, (uint)((BitmapSource)bitmap).PixelHeight, 96.0, 96.0, pixels);
			await encoder.FlushAsync();
		}
		finally
		{
			((IDisposable)stream)?.Dispose();
		}
	}

	public static async Task<BitmapImage> ToBitmap(this StorageFile file)
	{
		BitmapDecoder val = await BitmapDecoder.CreateAsync(await file.OpenAsync((FileAccessMode)0));
		InMemoryRandomAccessStream ras = new InMemoryRandomAccessStream();
		await (await BitmapEncoder.CreateForTranscodingAsync((IRandomAccessStream)(object)ras, val)).FlushAsync();
		BitmapImage val2 = new BitmapImage();
		((BitmapSource)val2).SetSource((IRandomAccessStream)(object)ras);
		return val2;
	}

	public static async Task<BitmapImage> ToBitmapScaled(this StorageFile file, uint height, uint width)
	{
		BitmapDecoder val = await BitmapDecoder.CreateAsync(await file.OpenAsync((FileAccessMode)0));
		InMemoryRandomAccessStream ras = new InMemoryRandomAccessStream();
		BitmapEncoder obj = await BitmapEncoder.CreateForTranscodingAsync((IRandomAccessStream)(object)ras, val);
		obj.BitmapTransform.put_ScaledHeight(height);
		obj.BitmapTransform.put_ScaledWidth(width);
		await obj.FlushAsync();
		BitmapImage val2 = new BitmapImage();
		((BitmapSource)val2).SetSource((IRandomAccessStream)(object)ras);
		return val2;
	}

	public static async Task<BackgroundTaskRegistration> RegisterBackgroundTimedTask(string taskEntryPoint)
	{
		if ((int)(await BackgroundExecutionManager.RequestAccessAsync()) != 3)
		{
			BackgroundTaskBuilder val = new BackgroundTaskBuilder();
			val.put_Name(taskEntryPoint);
			val.put_TaskEntryPoint(taskEntryPoint);
			val.SetTrigger((IBackgroundTrigger)new TimeTrigger(15u, false));
			return val.Register();
		}
		return null;
	}

	public static void Apply<T>(this IEnumerable<T> enumerable, Action<T> action)
	{
		foreach (T item in enumerable)
		{
			action(item);
		}
	}

	public static object DarkResource(this ResourceDictionary dic, string key)
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Expected O, but got Unknown
		return ((IDictionary<object, object>)(ResourceDictionary)dic.ThemeDictionaries["Dark"])[(object)key];
	}

	public static object LightResource(this ResourceDictionary dic, string key)
	{
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Expected O, but got Unknown
		return ((IDictionary<object, object>)(ResourceDictionary)dic.ThemeDictionaries["Light"])[(object)key];
	}

	public static Color ColorHex(string hex)
	{
		try
		{
			hex = hex.Replace("#", "").Replace("0x", "");
			byte a = byte.MaxValue;
			byte b = byte.MaxValue;
			byte b2 = byte.MaxValue;
			byte b3 = byte.MaxValue;
			int num = 0;
			if (hex.Length == 8)
			{
				a = byte.Parse(hex.Substring(0, 2), NumberStyles.HexNumber, CultureInfo.InvariantCulture);
				num = 2;
			}
			b = byte.Parse(hex.Substring(num, 2), NumberStyles.HexNumber, CultureInfo.InvariantCulture);
			b2 = byte.Parse(hex.Substring(num + 2, 2), NumberStyles.HexNumber, CultureInfo.InvariantCulture);
			b3 = byte.Parse(hex.Substring(num + 4, 2), NumberStyles.HexNumber, CultureInfo.InvariantCulture);
			return Color.FromArgb(a, b, b2, b3);
		}
		catch (Exception)
		{
			return Color.FromArgb(0, 0, 0, 0);
		}
	}

	public static void Repopulate<T>(this ICollection<T> source, IEnumerable<T> newItemsSource)
	{
		source.Clear();
		foreach (T item in newItemsSource)
		{
			source.Add(item);
		}
	}

	public static string ToCommaSeperated(this int integer)
	{
		return $"{integer:n0}";
	}

	public static string ToCommaSeperated(this long integer)
	{
		return $"{integer:n0}";
	}

	public static string ToVineTime(this DateTime time)
	{
		DateTime utcNow = DateTime.UtcNow;
		string empty = string.Empty;
		TimeSpan timeSpan = utcNow.Subtract(time);
		if (timeSpan.TotalSeconds < 120.0)
		{
			return ResourceHelper.GetString("just_now");
		}
		if (timeSpan.TotalMinutes < 1.0)
		{
			return ((int)timeSpan.TotalSeconds > 1) ? string.Format(ResourceHelper.GetString("time_secs_verbose_ago_other"), new object[1] { timeSpan.Seconds }) : ResourceHelper.GetString("time_secs_verbose_ago_one");
		}
		if (timeSpan.TotalHours < 1.0)
		{
			return ((int)timeSpan.TotalMinutes > 1) ? string.Format(ResourceHelper.GetString("time_mins_verbose_ago_other"), new object[1] { timeSpan.Minutes }) : ResourceHelper.GetString("time_mins_verbose_ago_one");
		}
		if (timeSpan.TotalDays < 1.0)
		{
			return ((int)timeSpan.TotalHours > 1) ? string.Format(ResourceHelper.GetString("time_hours_verbose_ago_other"), new object[1] { timeSpan.Hours }) : ResourceHelper.GetString("time_hours_verbose_ago_one");
		}
		if (timeSpan.TotalDays < 7.0)
		{
			return ((int)timeSpan.TotalDays > 1) ? string.Format(ResourceHelper.GetString("time_days_verbose_ago_other"), new object[1] { timeSpan.Days }) : ResourceHelper.GetString("time_days_verbose_ago_one");
		}
		return time.ToLocalTime().ToString("MMM dd", CultureInfo.CurrentCulture);
	}

	public static string ToVineMsgTime(this DateTime time)
	{
		if (time.Day == DateTime.UtcNow.Day)
		{
			return string.Format("Today {0}", new object[1] { time.ToLocalTime().ToString("h:mm tt") });
		}
		if (DateTime.UtcNow.DayOfYear - 1 == time.DayOfYear)
		{
			return string.Format("Yesterday {0}", new object[1] { time.ToLocalTime().ToString("h:mm tt") });
		}
		return time.ToLocalTime().ToString("g");
	}

	public static string ToVineCount(this int count)
	{
		return ((long)count).ToVineCount();
	}

	public static string ToVineCount(this long count)
	{
		if (count < 10000)
		{
			return $"{count:n0}";
		}
		if (10000 <= count && count < 1000000)
		{
			return Math.Round((double)count / 1000.0, 1) + "k";
		}
		return Math.Round((double)count / 1000000.0, 1) + "M";
	}

	public static TimeSpan Abs(this TimeSpan timeSpan)
	{
		if (timeSpan.Ticks < 0)
		{
			timeSpan = timeSpan.Negate();
		}
		return timeSpan;
	}

	public static string ToXmlFormattedString(this string xml)
	{
		return xml.Replace("&", "&amp;").Replace("<", "&lt;").Replace(">", "&gt;")
			.Replace("\"", "&quot;")
			.Replace("'", "&apos;");
	}

	public static void LoadXml(this XmlDocument xmlDoc, string format, params object[] values)
	{
		object[] array = new object[values.Length];
		for (int i = 0; i < values.Length; i++)
		{
			object obj = values[i];
			string xml = ((!(obj is long)) ? ((!(obj is int)) ? ((!(obj is double)) ? obj.ToString() : ((double)obj).ToStringInvariantCulture()) : ((int)obj).ToStringInvariantCulture()) : ((long)obj).ToStringInvariantCulture());
			xml = xml.ToXmlFormattedString();
			array[i] = xml;
		}
		string text = string.Format(CultureInfo.InvariantCulture, format, array);
		xmlDoc.LoadXml(text);
	}

	public static string ToStringInvariantCulture(this long number)
	{
		return number.ToString(CultureInfo.InvariantCulture);
	}

	public static string ToStringInvariantCulture(this int number)
	{
		return number.ToString(CultureInfo.InvariantCulture);
	}

	public static string ToStringInvariantCulture(this double number)
	{
		return number.ToString(CultureInfo.InvariantCulture);
	}

	public static string ToDataString(this IEnumerable<KeyValuePair<string, string>> nameValueCollection)
	{
		return string.Join("&", nameValueCollection.Select((KeyValuePair<string, string> kvp) => string.Format("{0}={1}", new object[2]
		{
			OAuth.OAuthUrlEncode(kvp.Key),
			OAuth.OAuthUrlEncode(kvp.Value)
		})).ToArray());
	}

	public static string ToDelimitedString<T>(this IEnumerable<T> source, string delimiter)
	{
		return source.ToDelimitedString((T x) => x.ToString(), delimiter);
	}

	private static string ToDelimitedString<T>(this IEnumerable<T> source, Func<T, string> selector, string delimiter)
	{
		if (source == null)
		{
			return string.Empty;
		}
		if (selector == null)
		{
			throw new ArgumentNullException("selector", "Must provide a valid property selector");
		}
		return string.Join(delimiter, source.Select(selector).ToArray());
	}
}
