using System;
using System.Globalization;
using System.Windows.Data;
using Vine.Resources;
using Vine.Services;

namespace Vine.Converters;

public class StateToStringConverter : IValueConverter
{
	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		return Convert((EncodingStep)value);
	}

	public static string Convert(EncodingStep step)
	{
		switch (step)
		{
		case EncodingStep.BEGIN:
			return AppResources.VineUploadBegin;
		case EncodingStep.SENDENCODING:
			try
			{
				return string.Format(AppResources.VineUploadUploadTo6SecServer, AppVersion.AppName);
			}
			catch
			{
				return AppResources.VineUploadUploadTo6SecServer;
			}
		case EncodingStep.WAITINGENCODING:
			return AppResources.VineUploadWaitingEncodingResult;
		case EncodingStep.INITIALIZATION:
			return AppResources.Initialization;
		case EncodingStep.ENDED:
			return AppResources.VineUploadCompleted;
		case EncodingStep.ERROR:
			return AppResources.VineUploadError;
		case EncodingStep.GETVIDEO:
			return AppResources.VineUploadGetVideo;
		case EncodingStep.GETIMAGE:
			return AppResources.VineUploadGetImage;
		case EncodingStep.READYTOPUBLISH:
			return AppResources.VineUploadReadyToPublish;
		case EncodingStep.UPLOADINGIMAGE:
			try
			{
				return string.Format(AppResources.VineUploadUploadVineImage, AppVersion.ServiceName);
			}
			catch
			{
				return AppResources.VineUploadUploadVineImage;
			}
		case EncodingStep.UPLOADINGVIDEO:
			try
			{
				return string.Format(AppResources.VineUploadUploadVineVideo, AppVersion.ServiceName);
			}
			catch
			{
				return AppResources.VineUploadUploadVineVideo;
			}
		default:
			return null;
		}
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		throw new NotImplementedException();
	}
}
