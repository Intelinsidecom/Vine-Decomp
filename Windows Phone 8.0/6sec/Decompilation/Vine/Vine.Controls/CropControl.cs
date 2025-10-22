using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Shapes;

namespace Vine.Controls;

[TemplatePart(Name = "PART_Canvas", Type = typeof(Canvas))]
[TemplatePart(Name = "PART_Image", Type = typeof(ContentPresenter))]
public class CropControl : ContentControl
{
	private enum DragMode
	{
		None,
		Full
	}

	private const string CanvasName = "PART_Canvas";

	private const string ImageName = "PART_Image";

	private FrameworkElement _medias;

	private Line _bottomLine;

	private Rectangle _bottomMaskingRectangle;

	private Canvas _canvas;

	private double _CropBottom;

	private double _CropBottomAtManipulationStart;

	private double _CropLeft;

	private double _CropLeftAtManipulationStart;

	private double _CropRight;

	private double _CropRightAtManipulationStart;

	private double _CropTop;

	private double _CropTopAtManipulationStart;

	private DragMode _dragMode;

	private bool _isLoaded;

	private bool _isTemplateApplied;

	private double _leftImageOffset;

	private Line _leftLine;

	private Rectangle _leftMaskingRectangle;

	private Line _rightLine;

	private Rectangle _rightMaskingRectangle;

	private double _scalingFactor;

	private double _topImageOffset;

	private Line _topLine;

	private Rectangle _topMaskingRectangle;

	public static readonly DependencyProperty ActualAspectRatioProperty = DependencyProperty.Register("ActualAspectRatio", typeof(double), typeof(CropControl), new PropertyMetadata((object)0.0));

	public static readonly DependencyProperty DesiredAspectRatioProperty = DependencyProperty.Register("DesiredAspectRatio", typeof(double), typeof(CropControl), new PropertyMetadata((object)1.0, new PropertyChangedCallback(OnDesiredAspectRatioChanged)));

	public static readonly DependencyProperty ForegroundProperty = DependencyProperty.Register("Foreground", typeof(Brush), typeof(CropControl), new PropertyMetadata((object)new SolidColorBrush(Colors.White), new PropertyChangedCallback(OnLayoutPropertyChanged)));

	public static readonly DependencyProperty MaskingBrushProperty;

	private bool _autoFit;

	public double ActualAspectRatio
	{
		get
		{
			return (double)((DependencyObject)this).GetValue(ActualAspectRatioProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(ActualAspectRatioProperty, (object)value);
		}
	}

	public double DesiredAspectRatio
	{
		get
		{
			return (double)((DependencyObject)this).GetValue(DesiredAspectRatioProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(DesiredAspectRatioProperty, (object)value);
		}
	}

	private bool IsFixedAspectRatio => !double.IsNaN(DesiredAspectRatio);

	public Brush Foreground
	{
		get
		{
			//IL_000b: Unknown result type (might be due to invalid IL or missing references)
			//IL_0011: Expected O, but got Unknown
			return (Brush)((DependencyObject)this).GetValue(ForegroundProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(ForegroundProperty, (object)value);
		}
	}

	public Brush MaskingBrush
	{
		get
		{
			//IL_000b: Unknown result type (might be due to invalid IL or missing references)
			//IL_0011: Expected O, but got Unknown
			return (Brush)((DependencyObject)this).GetValue(MaskingBrushProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(MaskingBrushProperty, (object)value);
		}
	}

	static CropControl()
	{
		//IL_0027: Unknown result type (might be due to invalid IL or missing references)
		//IL_0031: Expected O, but got Unknown
		//IL_0064: Unknown result type (might be due to invalid IL or missing references)
		//IL_006e: Expected O, but got Unknown
		//IL_0069: Unknown result type (might be due to invalid IL or missing references)
		//IL_0073: Expected O, but got Unknown
		//IL_0091: Unknown result type (might be due to invalid IL or missing references)
		//IL_0096: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a2: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ac: Expected O, but got Unknown
		//IL_00ac: Expected O, but got Unknown
		//IL_00a7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b1: Expected O, but got Unknown
		//IL_00d1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e4: Unknown result type (might be due to invalid IL or missing references)
		//IL_00f0: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fa: Expected O, but got Unknown
		//IL_00fa: Expected O, but got Unknown
		//IL_00f5: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ff: Expected O, but got Unknown
		Type typeFromHandle = typeof(Brush);
		Type typeFromHandle2 = typeof(CropControl);
		Color val = default(Color);
		((Color)(ref val)).A = 160;
		MaskingBrushProperty = DependencyProperty.Register("MaskingBrush", typeFromHandle, typeFromHandle2, new PropertyMetadata((object)new SolidColorBrush(val), new PropertyChangedCallback(OnLayoutPropertyChanged)));
	}

	public CropControl()
	{
		//IL_001e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0028: Expected O, but got Unknown
		//IL_0030: Unknown result type (might be due to invalid IL or missing references)
		//IL_003a: Expected O, but got Unknown
		//IL_0042: Unknown result type (might be due to invalid IL or missing references)
		//IL_004c: Expected O, but got Unknown
		((Control)this).DefaultStyleKey = typeof(CropControl);
		((FrameworkElement)this).Loaded += new RoutedEventHandler(OnLoaded);
		((FrameworkElement)this).Unloaded += new RoutedEventHandler(OnUnloaded);
		((FrameworkElement)this).SizeChanged += new SizeChangedEventHandler(CropControl_SizeChanged);
	}

	private void CropControl_SizeChanged(object sender, SizeChangedEventArgs e)
	{
		//IL_0002: Unknown result type (might be due to invalid IL or missing references)
		Setup(e.NewSize);
	}

	private void ProcessManipulationDelta(ManipulationDelta delta, bool done)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_0076: Unknown result type (might be due to invalid IL or missing references)
		//IL_007b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0085: Unknown result type (might be due to invalid IL or missing references)
		//IL_008a: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0020: Unknown result type (might be due to invalid IL or missing references)
		//IL_0035: Unknown result type (might be due to invalid IL or missing references)
		//IL_003a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0235: Unknown result type (might be due to invalid IL or missing references)
		//IL_023a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0246: Unknown result type (might be due to invalid IL or missing references)
		//IL_024b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0257: Unknown result type (might be due to invalid IL or missing references)
		//IL_025c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0268: Unknown result type (might be due to invalid IL or missing references)
		//IL_026d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0752: Unknown result type (might be due to invalid IL or missing references)
		//IL_0290: Unknown result type (might be due to invalid IL or missing references)
		//IL_0295: Unknown result type (might be due to invalid IL or missing references)
		//IL_02f1: Unknown result type (might be due to invalid IL or missing references)
		//IL_02f6: Unknown result type (might be due to invalid IL or missing references)
		//IL_02b0: Unknown result type (might be due to invalid IL or missing references)
		//IL_02b5: Unknown result type (might be due to invalid IL or missing references)
		//IL_0338: Unknown result type (might be due to invalid IL or missing references)
		//IL_033d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0316: Unknown result type (might be due to invalid IL or missing references)
		//IL_031b: Unknown result type (might be due to invalid IL or missing references)
		//IL_02d3: Unknown result type (might be due to invalid IL or missing references)
		//IL_02d8: Unknown result type (might be due to invalid IL or missing references)
		//IL_0399: Unknown result type (might be due to invalid IL or missing references)
		//IL_039e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0358: Unknown result type (might be due to invalid IL or missing references)
		//IL_035d: Unknown result type (might be due to invalid IL or missing references)
		//IL_03be: Unknown result type (might be due to invalid IL or missing references)
		//IL_03c3: Unknown result type (might be due to invalid IL or missing references)
		//IL_037b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0380: Unknown result type (might be due to invalid IL or missing references)
		Point val = delta.Scale;
		if (((Point)(ref val)).Y == 0.0)
		{
			val = delta.Translation;
			if (((Point)(ref val)).X == 0.0)
			{
				val = delta.Translation;
				if (((Point)(ref val)).Y == 0.0)
				{
					return;
				}
			}
		}
		ReinitImageCrop();
		double num = _CropRight - _CropLeft;
		double num2 = _CropBottom - _CropTop;
		double num3 = num;
		double num4 = num2;
		val = delta.Scale;
		double x = ((Point)(ref val)).X;
		val = delta.Scale;
		double num5 = Math.Max(x, ((Point)(ref val)).Y);
		if (num5 > 0.0)
		{
			num3 = num * num5;
			num4 = num2 * num5;
		}
		if ((num4 < 50.0 || num3 < 50.0) && num2 > num4)
		{
			return;
		}
		if (num3 > _medias.ActualWidth)
		{
			num4 *= _medias.ActualWidth / num3;
			num3 = _medias.ActualWidth;
		}
		if (num4 > _medias.ActualHeight)
		{
			num3 *= _medias.ActualHeight / num4;
			num4 = _medias.ActualHeight;
		}
		_CropLeft -= (num3 - num) / 2.0;
		_CropTop -= (num4 - num2) / 2.0;
		_CropRight += (num3 - num) / 2.0;
		_CropBottom += (num4 - num2) / 2.0;
		if (_CropLeft < 0.0)
		{
			ReinitImageCrop();
			return;
		}
		if (_CropRight > _medias.ActualWidth)
		{
			ReinitImageCrop();
			return;
		}
		if (_CropTop < 0.0)
		{
			_CropBottom -= _CropTop;
			_CropTop = 0.0;
		}
		else if (_CropBottom > _medias.ActualHeight)
		{
			_CropTop -= _CropBottom - _medias.ActualHeight;
			_CropBottom = _medias.ActualHeight;
		}
		val = delta.Translation;
		double num6 = ((Point)(ref val)).X;
		val = delta.Translation;
		double num7 = ((Point)(ref val)).Y;
		val = delta.Translation;
		double num8 = ((Point)(ref val)).X;
		val = delta.Translation;
		double num9 = ((Point)(ref val)).Y;
		if (_dragMode != DragMode.None)
		{
			if (_dragMode == DragMode.Full)
			{
				val = delta.Translation;
				if (((Point)(ref val)).X < 0.0)
				{
					double cropLeft = _CropLeft;
					val = delta.Translation;
					if (cropLeft + ((Point)(ref val)).X < 0.0)
					{
						double num10 = num6;
						double cropLeft2 = _CropLeft;
						val = delta.Translation;
						num8 = (num6 = num10 - (cropLeft2 + ((Point)(ref val)).X));
					}
				}
				else
				{
					double cropRight = _CropRight;
					val = delta.Translation;
					if (cropRight + ((Point)(ref val)).X > _medias.ActualWidth)
					{
						double num11 = num6;
						double cropRight2 = _CropRight;
						val = delta.Translation;
						num8 = (num6 = num11 - (cropRight2 + ((Point)(ref val)).X - _medias.ActualWidth));
					}
				}
				val = delta.Translation;
				if (((Point)(ref val)).Y < 0.0)
				{
					double cropTop = _CropTop;
					val = delta.Translation;
					if (cropTop + ((Point)(ref val)).Y < 0.0)
					{
						double num12 = num7;
						double cropTop2 = _CropTop;
						val = delta.Translation;
						num9 = (num7 = num12 - (cropTop2 + ((Point)(ref val)).Y));
					}
				}
				else
				{
					double cropBottom = _CropBottom;
					val = delta.Translation;
					if (cropBottom + ((Point)(ref val)).Y > _medias.ActualHeight)
					{
						double num13 = num7;
						double cropBottom2 = _CropBottom;
						val = delta.Translation;
						num9 = (num7 = num13 - (cropBottom2 + ((Point)(ref val)).Y - _medias.ActualHeight));
					}
				}
			}
			double num14 = Math.Max(_scalingFactor, 1.0);
			if (double.IsNaN(num7) || double.IsNaN(num9))
			{
				num3 = Math.Max(num14, Math.Min(_CropRight + num8, _medias.ActualWidth) - Math.Max(0.0, _CropLeft + num6));
				num4 = Math.Max(num14, num3 / DesiredAspectRatio);
				double num15 = _CropBottom - _CropTop - num4;
				if (double.IsNaN(num7))
				{
					if (double.IsNaN(num9))
					{
						num7 = num15 / 2.0;
						num9 = 0.0 - num15 / 2.0;
					}
					else
					{
						num7 = num15;
					}
				}
				else
				{
					num9 = 0.0 - num15;
				}
				num7 = Math.Max(num7, 0.0 - _CropTop);
				num9 = Math.Min(num9, _medias.ActualHeight - _CropBottom);
				num4 = _CropBottom - _CropTop - num7 + num9;
				double num16 = num4 * DesiredAspectRatio;
				num15 = num3 - num16;
				if (num6 != 0.0)
				{
					num6 += num15;
				}
				else
				{
					num8 -= num15;
				}
			}
			if (double.IsNaN(num6) || double.IsNaN(num8))
			{
				num4 = Math.Max(num14, Math.Min(_CropBottom + num9, _medias.ActualHeight) - Math.Max(0.0, _CropTop + num7));
				num3 = Math.Max(num14, num4 * DesiredAspectRatio);
				double num17 = _CropRight - _CropLeft - num3;
				if (double.IsNaN(num6))
				{
					if (double.IsNaN(num8))
					{
						num6 = num17 / 2.0;
						num8 = 0.0 - num17 / 2.0;
					}
					else
					{
						num6 = num17;
					}
				}
				else
				{
					num8 = 0.0 - num17;
				}
				num6 = Math.Max(num6, 0.0 - _CropLeft);
				num8 = Math.Min(num8, _medias.ActualWidth - _CropRight);
				num3 = _CropRight - _CropLeft - num6 + num8;
				double num18 = num3 / DesiredAspectRatio;
				num17 = num4 - num18;
				if (num7 != 0.0)
				{
					num7 += num17;
				}
				else
				{
					num9 -= num17;
				}
			}
			_CropLeft = Math.Max(0.0, _CropLeft + num6);
			_CropTop = Math.Max(0.0, _CropTop + num7);
			_CropRight = Math.Min(_medias.ActualWidth, _CropRight + num8);
			_CropBottom = Math.Min(_medias.ActualHeight, _CropBottom + num9);
			if (num6 != 0.0)
			{
				_CropLeft = Math.Floor(Math.Min(_CropRight - num14, _CropLeft));
			}
			if (num7 != 0.0)
			{
				_CropTop = Math.Floor(Math.Min(_CropBottom - num14, _CropTop));
			}
			if (num8 != 0.0)
			{
				_CropRight = Math.Ceiling(Math.Max(_CropLeft + num14, _CropRight));
			}
			if (num9 != 0.0)
			{
				_CropBottom = Math.Ceiling(Math.Max(_CropTop + num14, _CropBottom));
			}
		}
		AdjustLayout(((UIElement)_medias).RenderSize);
	}

	private void ReinitImageCrop()
	{
		_CropLeft = _CropLeftAtManipulationStart;
		_CropTop = _CropTopAtManipulationStart;
		_CropRight = _CropRightAtManipulationStart;
		_CropBottom = _CropBottomAtManipulationStart;
	}

	private T GetTemplateChild<T>(string name) where T : class
	{
		if (!(((Control)this).GetTemplateChild(name) is T result))
		{
			throw new InvalidOperationException(string.Concat("CropControl requires an ", typeof(T), " called ", name, " in its template."));
		}
		return result;
	}

	public override void OnApplyTemplate()
	{
		//IL_0035: Unknown result type (might be due to invalid IL or missing references)
		//IL_003f: Expected O, but got Unknown
		//IL_00a5: Unknown result type (might be due to invalid IL or missing references)
		((FrameworkElement)this).OnApplyTemplate();
		_canvas = GetTemplateChild<Canvas>("PART_Canvas");
		_medias = GetTemplateChild<FrameworkElement>("PART_Image");
		_medias.SizeChanged += new SizeChangedEventHandler(Medias_SizeChanged);
		((UIElement)_canvas).ManipulationStarted += OnManipulationStarted;
		((UIElement)_canvas).ManipulationDelta += OnManipulationDelta;
		_CropLeft = (_CropRight = (_CropTop = (_CropBottom = 50.0)));
		_isTemplateApplied = true;
		Setup(((UIElement)_medias).RenderSize);
	}

	private void Medias_SizeChanged(object sender, SizeChangedEventArgs e)
	{
		//IL_0002: Unknown result type (might be due to invalid IL or missing references)
		DoFullLayout(e.NewSize);
	}

	private DragMode CalculateDragMode(Point point)
	{
		if (((Point)(ref point)).X < _CropLeft || ((Point)(ref point)).X > _CropRight || ((Point)(ref point)).Y < _CropTop || ((Point)(ref point)).Y > _CropBottom)
		{
			return DragMode.None;
		}
		return DragMode.Full;
	}

	private void OnManipulationStarted(object sender, ManipulationStartedEventArgs e)
	{
		//IL_0052: Unknown result type (might be due to invalid IL or missing references)
		if (!_autoFit && _medias.ActualWidth != 0.0)
		{
			_CropLeftAtManipulationStart = _CropLeft;
			_CropTopAtManipulationStart = _CropTop;
			_CropRightAtManipulationStart = _CropRight;
			_CropBottomAtManipulationStart = _CropBottom;
			_dragMode = CalculateDragMode(e.ManipulationOrigin);
			e.Handled = true;
		}
	}

	private void OnManipulationDelta(object sender, ManipulationDeltaEventArgs e)
	{
		if (!_autoFit && _medias.ActualWidth != 0.0)
		{
			ProcessManipulationDelta(e.CumulativeManipulation, done: false);
			e.Handled = true;
		}
	}

	private static void OnDesiredAspectRatioChanged(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		if (d is CropControl cropControl)
		{
			cropControl.SetCropForDesiredAspectRatio(((UIElement)cropControl._medias).RenderSize);
		}
	}

	private static void OnLayoutPropertyChanged(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		//IL_0011: Unknown result type (might be due to invalid IL or missing references)
		if (d is CropControl cropControl)
		{
			cropControl.DoFullLayout(((UIElement)cropControl._medias).RenderSize);
		}
	}

	private void OnLoaded(object sender, RoutedEventArgs e)
	{
		//IL_000e: Unknown result type (might be due to invalid IL or missing references)
		_isLoaded = true;
		Setup(((UIElement)_medias).RenderSize);
	}

	private void OnUnloaded(object sender, RoutedEventArgs e)
	{
		_ = _isTemplateApplied;
	}

	private void SetWidthHeight(FrameworkElement element, double width, double height)
	{
		element.Width = width;
		element.Height = height;
	}

	private void SetCanvasXYWithOffset(UIElement element, double x, double y)
	{
		Canvas.SetLeft(element, x + _leftImageOffset);
		Canvas.SetTop(element, y + _topImageOffset);
	}

	private void SetPositionWithOffset(Line line, double x1, double y1, double x2, double y2)
	{
		line.X1 = x1 + _leftImageOffset;
		line.Y1 = y1 + _topImageOffset;
		if (double.IsNaN(x2))
		{
			line.X2 = line.X1;
		}
		else
		{
			line.X2 = Math.Max(line.X1, x2 + _leftImageOffset);
		}
		if (double.IsNaN(y2))
		{
			line.Y2 = line.Y1;
		}
		else
		{
			line.Y2 = Math.Max(line.Y1, y2 + _topImageOffset);
		}
	}

	private void AdjustLayout(Size size)
	{
		if (((Size)(ref size)).Width != 0.0)
		{
			SetPositionWithOffset(_topLine, _CropLeft, _CropTop, _CropRight, double.NaN);
			SetPositionWithOffset(_leftLine, _CropLeft, _CropTop, double.NaN, _CropBottom);
			SetPositionWithOffset(_bottomLine, _CropLeft, _CropBottom, _CropRight, double.NaN);
			SetPositionWithOffset(_rightLine, _CropRight, _CropTop, double.NaN, _CropBottom);
			((FrameworkElement)_leftMaskingRectangle).Width = _CropLeft;
			Canvas.SetLeft((UIElement)(object)_topMaskingRectangle, _CropLeft + _leftImageOffset);
			SetWidthHeight((FrameworkElement)(object)_topMaskingRectangle, _CropRight - _CropLeft, _CropTop);
			((FrameworkElement)_rightMaskingRectangle).Width = ((Size)(ref size)).Width - _CropRight;
			Canvas.SetLeft((UIElement)(object)_rightMaskingRectangle, _CropRight + _leftImageOffset);
			SetCanvasXYWithOffset((UIElement)(object)_bottomMaskingRectangle, _CropLeft, _CropBottom);
			SetWidthHeight((FrameworkElement)(object)_bottomMaskingRectangle, _CropRight - _CropLeft, ((Size)(ref size)).Height - _CropBottom);
		}
	}

	public void Setup(Size size)
	{
		//IL_0012: Unknown result type (might be due to invalid IL or missing references)
		if (_isLoaded && _isTemplateApplied)
		{
			DoFullLayout(size);
		}
	}

	private void DoFullLayout(Size size)
	{
		//IL_0072: Unknown result type (might be due to invalid IL or missing references)
		//IL_0145: Unknown result type (might be due to invalid IL or missing references)
		if (_isTemplateApplied)
		{
			MaybeCreateMaskingRectangle(ref _leftMaskingRectangle);
			MaybeCreateMaskingRectangle(ref _topMaskingRectangle);
			MaybeCreateMaskingRectangle(ref _rightMaskingRectangle);
			MaybeCreateMaskingRectangle(ref _bottomMaskingRectangle);
			MaybeCreateLine(ref _leftLine);
			MaybeCreateLine(ref _topLine);
			MaybeCreateLine(ref _rightLine);
			MaybeCreateLine(ref _bottomLine);
			SetCropForDesiredAspectRatio(size);
			SetWidthHeight((FrameworkElement)(object)_leftLine, ((Size)(ref size)).Width, ((Size)(ref size)).Height);
			SetWidthHeight((FrameworkElement)(object)_topLine, ((Size)(ref size)).Width, ((Size)(ref size)).Height);
			SetWidthHeight((FrameworkElement)(object)_rightLine, ((Size)(ref size)).Width, ((Size)(ref size)).Height);
			SetWidthHeight((FrameworkElement)(object)_bottomLine, ((Size)(ref size)).Width, ((Size)(ref size)).Height);
			SetCanvasXYWithOffset((UIElement)(object)_leftMaskingRectangle, 0.0, 0.0);
			((FrameworkElement)_leftMaskingRectangle).Height = ((Size)(ref size)).Height;
			Canvas.SetTop((UIElement)(object)_topMaskingRectangle, _topImageOffset);
			Canvas.SetTop((UIElement)(object)_rightMaskingRectangle, _topImageOffset);
			((FrameworkElement)_rightMaskingRectangle).Height = ((Size)(ref size)).Height;
			AdjustLayout(size);
		}
	}

	private void SetCropForDesiredAspectRatio(Size size)
	{
		if (!_isTemplateApplied || ((Size)(ref size)).Width == 0.0)
		{
			return;
		}
		_CropLeft = (_CropTop = 0.0);
		if (IsFixedAspectRatio)
		{
			double num = ((Size)(ref size)).Width / ((Size)(ref size)).Height;
			if (DesiredAspectRatio <= 0.0)
			{
				DesiredAspectRatio = num;
			}
			else if (DesiredAspectRatio > num)
			{
				double num2 = ((Size)(ref size)).Width / DesiredAspectRatio;
				_CropTop = (((Size)(ref size)).Height - num2) / 2.0;
			}
			else
			{
				double num3 = ((Size)(ref size)).Height * DesiredAspectRatio;
				_CropLeft = (((Size)(ref size)).Width - num3) / 2.0;
			}
		}
		_CropRight = ((Size)(ref size)).Width - _CropLeft;
		_CropBottom = ((Size)(ref size)).Height - _CropTop;
	}

	private Rectangle MaybeCreateMaskingRectangle(ref Rectangle rectangle)
	{
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		if (rectangle == null)
		{
			rectangle = new Rectangle();
			((PresentationFrameworkCollection<UIElement>)(object)((Panel)_canvas).Children).Add((UIElement)(object)rectangle);
		}
		((Shape)rectangle).Fill = MaskingBrush;
		return rectangle;
	}

	private Shape MaybeCreateDragControl(ref Shape dragControl)
	{
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		if (dragControl == null)
		{
			dragControl = (Shape)new Ellipse();
			((PresentationFrameworkCollection<UIElement>)(object)((Panel)_canvas).Children).Add((UIElement)(object)dragControl);
		}
		SetWidthHeight((FrameworkElement)(object)dragControl, 0.0, 0.0);
		dragControl.Stroke = Foreground;
		dragControl.StrokeThickness = 3.0;
		return dragControl;
	}

	private Line MaybeCreateLine(ref Line line)
	{
		//IL_002c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0031: Unknown result type (might be due to invalid IL or missing references)
		//IL_0040: Expected O, but got Unknown
		//IL_0040: Unknown result type (might be due to invalid IL or missing references)
		//IL_004f: Expected O, but got Unknown
		//IL_0054: Expected O, but got Unknown
		//IL_0005: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		if (line == null)
		{
			line = new Line();
			((PresentationFrameworkCollection<UIElement>)(object)((Panel)_canvas).Children).Add((UIElement)(object)line);
		}
		((Shape)line).Stroke = Foreground;
		Line obj = line;
		DoubleCollection val = new DoubleCollection();
		((PresentationFrameworkCollection<double>)val).Add(3.0);
		((PresentationFrameworkCollection<double>)val).Add(3.0);
		((Shape)obj).StrokeDashArray = val;
		((UIElement)line).Opacity = 0.5;
		((Shape)line).StrokeThickness = 1.0;
		return line;
	}

	public Rect GetSelection()
	{
		//IL_005c: Unknown result type (might be due to invalid IL or missing references)
		double num = _CropLeft / _medias.ActualWidth;
		double num2 = _CropTop / _medias.ActualHeight;
		double num3 = (_CropRight - _CropLeft) / _medias.ActualWidth;
		double num4 = (_CropBottom - _CropTop) / _medias.ActualHeight;
		return new Rect(num, num2, num3, num4);
	}

	public void SetAutoFit(bool autofit)
	{
		//IL_0048: Unknown result type (might be due to invalid IL or missing references)
		if (_canvas != null)
		{
			_autoFit = autofit;
			if (_autoFit)
			{
				((UIElement)_canvas).Visibility = (Visibility)1;
				return;
			}
			((UIElement)_canvas).Visibility = (Visibility)0;
			Setup(new Size(_medias.ActualWidth, _medias.ActualHeight));
		}
	}
}
