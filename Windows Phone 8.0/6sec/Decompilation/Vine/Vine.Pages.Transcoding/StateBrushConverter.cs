using System;
using System.Globalization;
using System.Windows;
using System.Windows.Data;
using System.Windows.Media;
using Vine.Services;

namespace Vine.Pages.Transcoding;

public class StateBrushConverter : IValueConverter
{
	private static Brush ErrorBrush = (Brush)new SolidColorBrush(Colors.Black);

	private static Brush NormalBrush = (Brush)Application.Current.Resources[(object)"DarkGreyBrush"];

	private static Brush PublishBrush = (Brush)Application.Current.Resources[(object)"PrincBrush"];

	public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
	{
		return (EncodingStep)value switch
		{
			EncodingStep.READYTOPUBLISH => PublishBrush, 
			EncodingStep.ERROR => ErrorBrush, 
			_ => NormalBrush, 
		};
	}

	public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
	{
		throw new NotImplementedException();
	}
}
