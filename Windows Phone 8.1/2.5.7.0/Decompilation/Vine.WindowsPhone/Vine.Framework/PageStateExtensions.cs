using System.Diagnostics;

namespace Vine.Framework;

public static class PageStateExtensions
{
	public static T LoadValueOrDefault<T>(this LoadStateEventArgs args, string key)
	{
		return args.LoadValueOrDefault(key, default(T));
	}

	public static T LoadValueOrDefault<T>(this LoadStateEventArgs args, string key, T defaultValue)
	{
		if (args.PageState != null && args.PageState.TryGetValue(key, out var value))
		{
			if (value is T)
			{
				return (T)value;
			}
			Debugger.Break();
		}
		return defaultValue;
	}
}
