using System;
using System.Collections.Generic;
using System.Text;

namespace RestSharp.Authenticators.OAuth.Extensions;

internal static class CollectionExtensions
{
	public static IEnumerable<T> AsEnumerable<T>(this T item)
	{
		return new T[1] { item };
	}

	public static IEnumerable<T> And<T>(this T item, T other)
	{
		return new T[2] { item, other };
	}

	public static IEnumerable<T> And<T>(this IEnumerable<T> items, T item)
	{
		foreach (T item2 in items)
		{
			yield return item2;
		}
		yield return item;
	}

	public static K TryWithKey<T, K>(this IDictionary<T, K> dictionary, T key)
	{
		if (!dictionary.ContainsKey(key))
		{
			return default(K);
		}
		return dictionary[key];
	}

	public static IEnumerable<T> ToEnumerable<T>(this object[] items) where T : class
	{
		for (int i = 0; i < items.Length; i++)
		{
			yield return items[i] as T;
		}
	}

	public static void ForEach<T>(this IEnumerable<T> items, Action<T> action)
	{
		foreach (T item in items)
		{
			action(item);
		}
	}

	public static string Concatenate(this WebParameterCollection collection, string separator, string spacer)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int count = collection.Count;
		int num = 0;
		foreach (WebPair item in collection)
		{
			stringBuilder.Append(item.Name);
			stringBuilder.Append(separator);
			stringBuilder.Append(item.Value);
			num++;
			if (num < count)
			{
				stringBuilder.Append(spacer);
			}
		}
		return stringBuilder.ToString();
	}
}
