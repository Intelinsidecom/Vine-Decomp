using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Shapes;
using Vine.Converters;

namespace Huyn.BubbleText;

public class BubbleText : ContentControl
{
	private TextBlock _date;

	private Path _bottomrightcorner;

	private Border _thumb;

	private Path _topleftcorner;

	public static readonly DependencyProperty DateProperty = DependencyProperty.Register("Date", typeof(DateTime?), typeof(BubbleText), new PropertyMetadata((object)null, new PropertyChangedCallback(DateChanged)));

	public static readonly DependencyProperty IsMeProperty = DependencyProperty.Register("IsMe", typeof(bool), typeof(BubbleText), new PropertyMetadata((object)false, new PropertyChangedCallback(IsMeChanged)));

	public DateTime? Date
	{
		get
		{
			return (DateTime?)((DependencyObject)this).GetValue(DateProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(DateProperty, (object)value);
		}
	}

	public bool IsMe
	{
		get
		{
			return (bool)((DependencyObject)this).GetValue(IsMeProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(IsMeProperty, (object)value);
		}
	}

	public BubbleText()
	{
		((Control)this).DefaultStyleKey = typeof(BubbleText);
	}

	public override void OnApplyTemplate()
	{
		((FrameworkElement)this).OnApplyTemplate();
		ref TextBlock date = ref _date;
		DependencyObject templateChild = ((Control)this).GetTemplateChild("Date");
		date = (TextBlock)(object)((templateChild is TextBlock) ? templateChild : null);
		ref Path topleftcorner = ref _topleftcorner;
		DependencyObject templateChild2 = ((Control)this).GetTemplateChild("TopLeftCorner");
		topleftcorner = (Path)(object)((templateChild2 is Path) ? templateChild2 : null);
		ref Path bottomrightcorner = ref _bottomrightcorner;
		DependencyObject templateChild3 = ((Control)this).GetTemplateChild("BottomRightCorner");
		bottomrightcorner = (Path)(object)((templateChild3 is Path) ? templateChild3 : null);
		SetDate(Date);
		SetCorner(IsMe);
	}

	private static void DateChanged(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		(d as BubbleText).SetDate(((DependencyPropertyChangedEventArgs)(ref e)).NewValue as DateTime?);
	}

	private void SetDate(DateTime? date)
	{
		if (_date != null)
		{
			if (!date.HasValue)
			{
				_date.Text = "";
				((UIElement)_date).Visibility = (Visibility)1;
			}
			else
			{
				_date.Text = DateStringToTimePassedConverter.ConvertDate(date.Value);
			}
		}
	}

	private static void IsMeChanged(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		(d as BubbleText).SetCorner((bool)((DependencyPropertyChangedEventArgs)(ref e)).NewValue);
	}

	private void SetCorner(bool isme)
	{
		if (_topleftcorner != null && _bottomrightcorner != null)
		{
			if (!isme)
			{
				((UIElement)_topleftcorner).Visibility = (Visibility)0;
				((UIElement)_bottomrightcorner).Visibility = (Visibility)1;
				((FrameworkElement)this).HorizontalAlignment = (HorizontalAlignment)0;
			}
			else
			{
				((UIElement)_topleftcorner).Visibility = (Visibility)1;
				((UIElement)_bottomrightcorner).Visibility = (Visibility)0;
				((FrameworkElement)this).HorizontalAlignment = (HorizontalAlignment)2;
			}
		}
	}
}
