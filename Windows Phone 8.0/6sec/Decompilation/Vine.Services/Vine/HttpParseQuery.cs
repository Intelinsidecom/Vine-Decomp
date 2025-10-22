using System.Collections.Generic;
using System.Net;

namespace Vine;

public static class HttpParseQuery
{
	public static IDictionary<string, string> ParseQueryString(string query)
	{
		Dictionary<string, string> dictionary = new Dictionary<string, string>();
		if (query.Length == 0)
		{
			return dictionary;
		}
		string text = HttpUtility.HtmlDecode(query);
		int length = text.Length;
		int num = 0;
		bool flag = true;
		while (num <= length)
		{
			int num2 = -1;
			int num3 = -1;
			for (int i = num; i < length; i++)
			{
				if (num2 == -1 && text[i] == '=')
				{
					num2 = i + 1;
				}
				else if (text[i] == '&')
				{
					num3 = i;
					break;
				}
			}
			if (flag)
			{
				flag = false;
				if (text[num] == '?')
				{
					num++;
				}
			}
			string text2;
			if (num2 == -1)
			{
				text2 = null;
				num2 = num;
			}
			else
			{
				text2 = HttpUtility.UrlDecode(text.Substring(num, num2 - num - 1));
			}
			if (num3 < 0)
			{
				num = -1;
				num3 = text.Length;
			}
			else
			{
				num = num3 + 1;
			}
			string value = HttpUtility.UrlDecode(text.Substring(num2, num3 - num2));
			if (text2 != null)
			{
				dictionary.Add(text2, value);
			}
			if (num == -1)
			{
				break;
			}
		}
		return dictionary;
	}
}
