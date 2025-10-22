using System.Globalization;

namespace Vine.Utils;

public static class StringUtils
{
	private static string[] alphabet = new string[26]
	{
		"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
		"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
		"U", "V", "W", "X", "Y", "Z"
	};

	public static char GetNoAccentLetter(char letter)
	{
		char c = char.ToUpper(letter);
		if (c >= '0' && c <= '9')
		{
			return '#';
		}
		if (c >= 'A' && c <= 'Z')
		{
			return c;
		}
		string strB = c.ToString();
		string[] array = alphabet;
		foreach (string text in array)
		{
			if (string.Compare(text, strB, CultureInfo.CurrentCulture, CompareOptions.IgnoreNonSpace) == 0)
			{
				return text[0];
			}
		}
		return '@';
	}
}
