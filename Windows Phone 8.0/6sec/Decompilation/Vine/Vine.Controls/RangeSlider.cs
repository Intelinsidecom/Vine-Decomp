using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Input;
using System.Windows.Shapes;

namespace Vine.Controls;

[TemplatePart(Name = "PART_Thumb_Upper", Type = typeof(Thumb))]
[TemplatePart(Name = "PART_Thumb_Lower", Type = typeof(Thumb))]
[TemplatePart(Name = "PART_Rectangle_Middle", Type = typeof(Rectangle))]
[TemplatePart(Name = "PART_Rectangle_Progress", Type = typeof(Rectangle))]
public class RangeSlider : Control
{
	private double halfTheThumbWidth = 18.0;

	private const string ThumbPartName_Upper = "PART_Thumb_Upper";

	private const string RectanglePartName = "PART_Rectangle_Middle";

	private const string RectangleProgressPartName = "PART_Rectangle_Progress";

	private const string ThumbPartNameLower = "PART_Thumb_Lower";

	private const string TextName = "PART_Text";

	private FrameworkElement _upperThumb;

	private FrameworkElement _lowerthumb;

	private Rectangle _middleFillRectangle;

	private TextBlock _text;

	private int _interval;

	private bool isThumbSelected = true;

	public static readonly DependencyProperty UpperValueProperty = DependencyProperty.Register("UpperValue", typeof(double), typeof(RangeSlider), new PropertyMetadata((object)0.0, new PropertyChangedCallback(UpperValuePropertyChanged)));

	public static readonly DependencyProperty LowerValueProperty = DependencyProperty.Register("LowerValue", typeof(double), typeof(RangeSlider), new PropertyMetadata((object)0.0, new PropertyChangedCallback(LowerValuePropertyChanged)));

	public static readonly DependencyProperty MinimumProperty = DependencyProperty.Register("Minimum", typeof(double), typeof(RangeSlider), new PropertyMetadata((object)0.0));

	public static readonly DependencyProperty MaximumProperty = DependencyProperty.Register("Maximum", typeof(double), typeof(RangeSlider), new PropertyMetadata((object)100.0, new PropertyChangedCallback(ValuesPropertyChanged)));

	public static readonly DependencyProperty MinimumIntervalProperty = DependencyProperty.Register("MinimumInterval", typeof(int), typeof(RangeSlider), new PropertyMetadata((object)1));

	public static readonly DependencyProperty MaximumIntervalProperty = DependencyProperty.Register("MaximumInterval", typeof(int), typeof(RangeSlider), new PropertyMetadata((object)1));

	private Rectangle _middleFillProgressRectangle;

	private double _currentProgress;

	public bool IsThumbSelected
	{
		get
		{
			return isThumbSelected;
		}
		set
		{
			isThumbSelected = value;
			IsThumb_Selected = value;
		}
	}

	public static bool IsThumb_Selected { get; set; }

	public double Minimum
	{
		get
		{
			return (double)((DependencyObject)this).GetValue(MinimumProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(MinimumProperty, (object)value);
		}
	}

	public double Maximum
	{
		get
		{
			return (double)((DependencyObject)this).GetValue(MaximumProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(MaximumProperty, (object)value);
		}
	}

	public double LowerValue
	{
		get
		{
			return (double)((DependencyObject)this).GetValue(LowerValueProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(LowerValueProperty, (object)value);
			if (this.LowerValueChanged != null)
			{
				this.LowerValueChanged(this, value);
			}
		}
	}

	public double UpperValue
	{
		get
		{
			return (double)((DependencyObject)this).GetValue(UpperValueProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(UpperValueProperty, (object)value);
			if (this.UpperValueChanged != null)
			{
				this.UpperValueChanged(this, value);
			}
		}
	}

	public int MinimumInterval
	{
		get
		{
			return (int)((DependencyObject)this).GetValue(MinimumIntervalProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(MinimumIntervalProperty, (object)value);
		}
	}

	public int MaximumInterval
	{
		get
		{
			return (int)((DependencyObject)this).GetValue(MaximumIntervalProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(MaximumIntervalProperty, (object)value);
		}
	}

	public event EventHandler<double> LowerValueChanged;

	public event EventHandler<double> UpperValueChanged;

	public RangeSlider()
	{
		((Control)this).DefaultStyleKey = typeof(RangeSlider);
	}

	private static void ValuesPropertyChanged(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		((RangeSlider)(object)d).UpdateAll();
	}

	private void UpdateAll()
	{
		UpdateUpper();
		UpdateLower();
	}

	public void SetProgress(double progress)
	{
		_currentProgress = progress;
		if (_middleFillProgressRectangle != null)
		{
			double num = (((FrameworkElement)this).ActualWidth - halfTheThumbWidth * 2.0) / (Maximum - Minimum);
			double lowerValue = LowerValue;
			double num2 = Math.Min(progress, MaximumInterval);
			((FrameworkElement)_middleFillProgressRectangle).Width = Math.Max(0.0, (num2 - lowerValue) * num);
		}
	}

	public override void OnApplyTemplate()
	{
		//IL_00c6: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d0: Expected O, but got Unknown
		((FrameworkElement)this).OnApplyTemplate();
		ref Rectangle middleFillRectangle = ref _middleFillRectangle;
		DependencyObject templateChild = ((Control)this).GetTemplateChild("PART_Rectangle_Middle");
		middleFillRectangle = (Rectangle)(object)((templateChild is Rectangle) ? templateChild : null);
		ref Rectangle middleFillProgressRectangle = ref _middleFillProgressRectangle;
		DependencyObject templateChild2 = ((Control)this).GetTemplateChild("PART_Rectangle_Progress");
		middleFillProgressRectangle = (Rectangle)(object)((templateChild2 is Rectangle) ? templateChild2 : null);
		ref FrameworkElement upperThumb = ref _upperThumb;
		DependencyObject templateChild3 = ((Control)this).GetTemplateChild("PART_Thumb_Upper");
		upperThumb = (FrameworkElement)(object)((templateChild3 is FrameworkElement) ? templateChild3 : null);
		ref TextBlock text = ref _text;
		DependencyObject templateChild4 = ((Control)this).GetTemplateChild("PART_Text");
		text = (TextBlock)(object)((templateChild4 is TextBlock) ? templateChild4 : null);
		if (_upperThumb != null)
		{
			((UIElement)_upperThumb).ManipulationDelta += ThumbUpper_DragDelta;
			UpdateUpper();
		}
		ref FrameworkElement lowerthumb = ref _lowerthumb;
		DependencyObject templateChild5 = ((Control)this).GetTemplateChild("PART_Thumb_Lower");
		lowerthumb = (FrameworkElement)(object)((templateChild5 is FrameworkElement) ? templateChild5 : null);
		if (_lowerthumb != null)
		{
			((UIElement)_lowerthumb).ManipulationDelta += ThumbLower_DragDelta;
			UpdateLower();
		}
		((FrameworkElement)this).SizeChanged += new SizeChangedEventHandler(RangeSlider_SizeChanged);
	}

	private void RangeSlider_SizeChanged(object sender, SizeChangedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0014: Unknown result type (might be due to invalid IL or missing references)
		Size val = e.NewSize;
		double width = ((Size)(ref val)).Width;
		val = e.PreviousSize;
		if (width != ((Size)(ref val)).Width)
		{
			UpdateAll();
		}
	}

	private static void OnValueChanged(DependencyObject dependencyObject, DependencyPropertyChangedEventArgs e)
	{
		RangeSlider rangeSlider = (RangeSlider)(object)dependencyObject;
		if (IsThumb_Selected)
		{
			rangeSlider.UpperValue = Convert.ToDouble(((DependencyPropertyChangedEventArgs)(ref e)).NewValue);
		}
		else
		{
			rangeSlider.LowerValue = Convert.ToDouble(((DependencyPropertyChangedEventArgs)(ref e)).NewValue);
		}
	}

	private static void LowerValuePropertyChanged(DependencyObject dependencyObject, DependencyPropertyChangedEventArgs e)
	{
		((RangeSlider)(object)dependencyObject).UpdateLower();
	}

	private static void UpperValuePropertyChanged(DependencyObject dependencyObject, DependencyPropertyChangedEventArgs e)
	{
		((RangeSlider)(object)dependencyObject).UpdateUpper();
	}

	private void ThumbUpper_DragDelta(object sender, ManipulationDeltaEventArgs e)
	{
		//IL_0025: Unknown result type (might be due to invalid IL or missing references)
		//IL_002a: Unknown result type (might be due to invalid IL or missing references)
		IsThumbSelected = true;
		((UIElement)_lowerthumb).Visibility = (Visibility)0;
		((UIElement)_upperThumb).Visibility = (Visibility)0;
		Point translation = e.DeltaManipulation.Translation;
		double x = ((Point)(ref translation)).X;
		double left = Canvas.GetLeft((UIElement)(object)_upperThumb);
		if (left + x < 0.0)
		{
			UpperValue = 0.0;
		}
		else if (left + x + _upperThumb.ActualWidth > ((FrameworkElement)this).ActualWidth)
		{
			UpperValue = Maximum;
		}
		else
		{
			double actualWidth = ((FrameworkElement)this).ActualWidth;
			double num = x / actualWidth;
			double num2 = (Maximum - Minimum) * num;
			UpperValue = Math.Min(Math.Max(UpperValue + num2, LowerValue + (double)MinimumInterval), LowerValue + (double)MaximumInterval);
		}
		if (LowerValue == UpperValue && x < 0.0)
		{
			((UIElement)_lowerthumb).Visibility = (Visibility)0;
			((UIElement)_upperThumb).Visibility = (Visibility)1;
		}
		if (LowerValue == Minimum && LowerValue == UpperValue)
		{
			((UIElement)_lowerthumb).Visibility = (Visibility)1;
			((UIElement)_upperThumb).Visibility = (Visibility)0;
		}
		SetProgress(0.0);
	}

	private void ThumbLower_DragDelta(object sender, ManipulationDeltaEventArgs e)
	{
		//IL_0025: Unknown result type (might be due to invalid IL or missing references)
		//IL_002a: Unknown result type (might be due to invalid IL or missing references)
		((UIElement)_lowerthumb).Visibility = (Visibility)0;
		((UIElement)_upperThumb).Visibility = (Visibility)0;
		IsThumbSelected = false;
		Point translation = e.DeltaManipulation.Translation;
		double x = ((Point)(ref translation)).X;
		double left = Canvas.GetLeft((UIElement)(object)_lowerthumb);
		if (left + x < 0.0)
		{
			LowerValue = 0.0;
		}
		else if (left + x + _lowerthumb.ActualWidth > ((FrameworkElement)this).ActualWidth)
		{
			LowerValue = Maximum;
		}
		else
		{
			double actualWidth = ((FrameworkElement)this).ActualWidth;
			double num = x / actualWidth;
			double num2 = (Maximum - Minimum) * num;
			LowerValue = Math.Max(Math.Min(LowerValue + num2, UpperValue - (double)MinimumInterval), UpperValue - (double)MaximumInterval);
		}
		if (LowerValue == UpperValue && x > 0.0)
		{
			((UIElement)_lowerthumb).Visibility = (Visibility)1;
			((UIElement)_upperThumb).Visibility = (Visibility)0;
		}
		if (LowerValue == Maximum && LowerValue == UpperValue)
		{
			((UIElement)_lowerthumb).Visibility = (Visibility)0;
			((UIElement)_upperThumb).Visibility = (Visibility)1;
		}
		SetProgress(0.0);
	}

	public void UpdateUpper()
	{
		double num = (((FrameworkElement)this).ActualWidth - halfTheThumbWidth * 2.0) / (Maximum - Minimum);
		if (_upperThumb != null)
		{
			Canvas.SetLeft((UIElement)(object)_upperThumb, num * (UpperValue - Minimum));
		}
		if (_middleFillRectangle != null && _text != null)
		{
			Canvas.SetLeft((UIElement)(object)_middleFillRectangle, num * (LowerValue - Minimum) + halfTheThumbWidth);
			TextBlock text = _text;
			double width = (((FrameworkElement)_middleFillRectangle).Width = Math.Max(0.0, num * (UpperValue - LowerValue)));
			((FrameworkElement)text).Width = width;
			_text.Text = ((UpperValue - LowerValue) / 1000.0).ToString("F") + "s";
		}
	}

	public void UpdateLower()
	{
		double num = (((FrameworkElement)this).ActualWidth - halfTheThumbWidth * 2.0) / (Maximum - Minimum);
		if (_lowerthumb != null)
		{
			Canvas.SetLeft((UIElement)(object)_lowerthumb, num * LowerValue);
		}
		if (_middleFillRectangle != null && _text != null)
		{
			Canvas.SetLeft((UIElement)(object)_middleFillRectangle, num * LowerValue + halfTheThumbWidth);
			Canvas.SetLeft((UIElement)(object)_middleFillProgressRectangle, num * LowerValue + halfTheThumbWidth);
			Canvas.SetLeft((UIElement)(object)_text, num * LowerValue + halfTheThumbWidth);
			Rectangle middleFillRectangle = _middleFillRectangle;
			double width = (((FrameworkElement)_text).Width = Math.Max(0.0, num * (UpperValue - LowerValue)));
			((FrameworkElement)middleFillRectangle).Width = width;
			((FrameworkElement)_middleFillProgressRectangle).Width = 0.0;
			_text.Text = ((UpperValue - LowerValue) / 1000.0).ToString("F") + "s";
		}
	}
}
