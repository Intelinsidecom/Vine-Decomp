using System;
using System.IO;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using Vine.Datas;

namespace Vine.ImageDownload;

public class ImageDownload
{
	public static readonly DependencyProperty ImageSourceProperty = DependencyProperty.Register("ImageSource", typeof(string), typeof(Image), new PropertyMetadata((object)null, new PropertyChangedCallback(ImageSourceCallback)));

	private static void ImageSourceCallback(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		//IL_0008: Unknown result type (might be due to invalid IL or missing references)
		//IL_0012: Expected O, but got Unknown
		Image image = (Image)d;
		string text = (string)((DependencyPropertyChangedEventArgs)(ref e)).NewValue;
		if (string.IsNullOrEmpty(text))
		{
			return;
		}
		HttpWebRequest videorequest = WebRequest.CreateHttp(text);
		videorequest.Headers[HttpRequestHeader.AcceptEncoding] = "gzip";
		videorequest.Headers["X-Vine-Client"] = "android/2.0.0";
		videorequest.Headers["vine-session-id"] = DatasProvider.Instance.CurrentUser.User.key;
		videorequest.AllowReadStreamBuffering = true;
		videorequest.BeginGetResponse(delegate(IAsyncResult iar)
		{
			try
			{
				WebResponse webResponse = videorequest.EndGetResponse(iar);
				Stream stream = webResponse.GetResponseStream();
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					//IL_0000: Unknown result type (might be due to invalid IL or missing references)
					//IL_0006: Expected O, but got Unknown
					BitmapImage val = new BitmapImage();
					((BitmapSource)val).SetSource(stream);
					image.Source = (ImageSource)(object)val;
				});
			}
			catch
			{
			}
		}, null);
	}

	public static bool GetImageSource(DependencyObject source)
	{
		return (bool)source.GetValue(ImageSourceProperty);
	}

	public static void SetImageSource(DependencyObject source, bool value)
	{
		source.SetValue(ImageSourceProperty, (object)value);
	}
}
