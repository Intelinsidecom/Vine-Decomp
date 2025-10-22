using System;
using System.Globalization;
using System.Windows.Media;
using Microsoft.Phone.Info;

namespace Vine;

public static class VineGenUtils
{
	public static string ColorToString(Color c)
	{
		return ((Color)(ref c)).R.ToString("X2") + ((Color)(ref c)).G.ToString("X2") + ((Color)(ref c)).B.ToString("X2");
	}

	public static Color HexColor(string hex)
	{
		//IL_0051: Unknown result type (might be due to invalid IL or missing references)
		hex = hex.Replace("#", "");
		int num = int.Parse(hex, NumberStyles.HexNumber);
		byte b = byte.MaxValue;
		if (hex.Length == 8)
		{
			b = (byte)(num >> 24);
		}
		return Color.FromArgb(b, (byte)((num >> 16) & 0xFF), (byte)((num >> 8) & 0xFF), (byte)(num & 0xFF));
	}

	public static string GetDeviceUniqueID()
	{
		byte[] array = null;
		object obj = default(object);
		if (DeviceExtendedProperties.TryGetValue("DeviceUniqueId", ref obj))
		{
			array = (byte[])obj;
		}
		return BitConverter.ToString(array).Replace("-", "");
	}
}
