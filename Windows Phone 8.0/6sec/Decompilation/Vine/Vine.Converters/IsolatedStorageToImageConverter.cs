using System;
using System.Globalization;
using System.IO;
using System.IO.IsolatedStorage;
using System.Windows.Data;
using System.Windows.Media.Imaging;

namespace Vine.Converters;

public class IsolatedStorageToImageConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		//IL_0025: Unknown result type (might be due to invalid IL or missing references)
		//IL_002a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0032: Expected O, but got Unknown
		if (value != null)
		{
			string text = (string)value;
			if (text != null)
			{
				using IsolatedStorageFile isolatedStorageFile = IsolatedStorageFile.GetUserStoreForApplication();
				try
				{
					if (isolatedStorageFile.FileExists(text))
					{
						using (IsolatedStorageFileStream source = isolatedStorageFile.OpenFile(text, FileMode.Open))
						{
							BitmapImage val = new BitmapImage();
							((BitmapSource)val).SetSource((Stream)source);
							return (object)val;
						}
					}
				}
				catch
				{
				}
			}
			return null;
		}
		return new Uri("/Assets/DefaultEncoding.jpg", UriKind.Relative);
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		throw new NotImplementedException();
	}
}
