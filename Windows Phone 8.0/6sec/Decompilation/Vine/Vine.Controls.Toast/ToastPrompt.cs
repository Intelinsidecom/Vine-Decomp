using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Threading;

namespace Vine.Controls.Toast;

public class ToastPrompt : Control
{
	private const string ToastImageName = "ToastImage";

	public static readonly DependencyProperty OverlayProperty = DependencyProperty.Register("Overlay", typeof(Brush), typeof(ToastPrompt), new PropertyMetadata(Application.Current.Resources[(object)"PhoneSemitransparentBrush"]));

	public static readonly DependencyProperty MillisecondsUntilHiddenProperty = DependencyProperty.Register("MillisecondsUntilHidden", typeof(int), typeof(ToastPrompt), new PropertyMetadata((object)4000));

	public static readonly DependencyProperty IsTimerEnabledProperty = DependencyProperty.Register("IsTimerEnabled", typeof(bool), typeof(ToastPrompt), new PropertyMetadata((object)true));

	public static readonly DependencyProperty TitleProperty = DependencyProperty.Register("Title", typeof(string), typeof(ToastPrompt), new PropertyMetadata((object)""));

	public static readonly DependencyProperty MessageProperty = DependencyProperty.Register("Message", typeof(string), typeof(ToastPrompt), new PropertyMetadata((object)""));

	public static readonly DependencyProperty ImageSourceProperty = DependencyProperty.Register("ImageSource", typeof(ImageSource), typeof(ToastPrompt), new PropertyMetadata(new PropertyChangedCallback(OnImageSource)));

	public static readonly DependencyProperty TextOrientationProperty = DependencyProperty.Register("TextOrientation", typeof(Orientation), typeof(ToastPrompt), new PropertyMetadata((object)(Orientation)0));

	private readonly TranslateTransform _translate = new TranslateTransform();

	protected Image ToastImage;

	private bool _alreadyFired;

	private DialogService _popUp;

	private DispatcherTimer _timer;

	public bool IsOpen
	{
		get
		{
			if (_popUp != null)
			{
				return _popUp.IsOpen;
			}
			return false;
		}
	}

	protected internal bool IsBackKeyOverride { get; set; }

	public Brush Overlay
	{
		get
		{
			//IL_000b: Unknown result type (might be due to invalid IL or missing references)
			//IL_0011: Expected O, but got Unknown
			return (Brush)((DependencyObject)this).GetValue(OverlayProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(OverlayProperty, (object)value);
		}
	}

	public int MillisecondsUntilHidden
	{
		get
		{
			return (int)((DependencyObject)this).GetValue(MillisecondsUntilHiddenProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(MillisecondsUntilHiddenProperty, (object)value);
		}
	}

	public bool IsTimerEnabled
	{
		get
		{
			return (bool)((DependencyObject)this).GetValue(IsTimerEnabledProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(IsTimerEnabledProperty, (object)value);
		}
	}

	public string Title
	{
		get
		{
			return (string)((DependencyObject)this).GetValue(TitleProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(TitleProperty, (object)value);
		}
	}

	public string Message
	{
		get
		{
			return (string)((DependencyObject)this).GetValue(MessageProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(MessageProperty, (object)value);
		}
	}

	public ImageSource ImageSource
	{
		get
		{
			//IL_000b: Unknown result type (might be due to invalid IL or missing references)
			//IL_0011: Expected O, but got Unknown
			return (ImageSource)((DependencyObject)this).GetValue(ImageSourceProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(ImageSourceProperty, (object)value);
		}
	}

	public Orientation TextOrientation
	{
		get
		{
			//IL_000b: Unknown result type (might be due to invalid IL or missing references)
			return (Orientation)((DependencyObject)this).GetValue(TextOrientationProperty);
		}
		set
		{
			//IL_0006: Unknown result type (might be due to invalid IL or missing references)
			((DependencyObject)this).SetValue(TextOrientationProperty, (object)value);
		}
	}

	public ToastPrompt()
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		//IL_0055: Unknown result type (might be due to invalid IL or missing references)
		//IL_005f: Expected O, but got Unknown
		((DependencyObject)this).Dispatcher.BeginInvoke((Action)delegate
		{
			((Control)this).ApplyTemplate();
		});
		((Control)this).DefaultStyleKey = typeof(ToastPrompt);
		IsBackKeyOverride = true;
		Overlay = (Brush)Application.Current.Resources[(object)"TransparentBrush"];
		((UIElement)this).ManipulationStarted += ToastPrompt_ManipulationStarted;
		((UIElement)this).ManipulationDelta += ToastPrompt_ManipulationDelta;
		((UIElement)this).ManipulationCompleted += ToastPrompt_ManipulationCompleted;
		((UIElement)this).RenderTransform = (Transform)(object)_translate;
	}

	public override void OnApplyTemplate()
	{
		//IL_0015: Unknown result type (might be due to invalid IL or missing references)
		//IL_001b: Unknown result type (might be due to invalid IL or missing references)
		((FrameworkElement)this).OnApplyTemplate();
		if (_popUp != null)
		{
			_popUp.SetAlignmentsOnOverlay(((FrameworkElement)this).HorizontalAlignment, ((FrameworkElement)this).VerticalAlignment);
		}
		ref Image toastImage = ref ToastImage;
		DependencyObject templateChild = ((Control)this).GetTemplateChild("ToastImage");
		toastImage = (Image)(object)((templateChild is Image) ? templateChild : null);
		if (ToastImage != null && ImageSource != null)
		{
			ToastImage.Source = ImageSource;
			SetImageVisibility(ImageSource);
		}
	}

	public void Hide()
	{
		_popUp_Closed(this, null);
	}

	private void _popUp_Closed(object sender, EventArgs e)
	{
		if (!_alreadyFired)
		{
			OnCompleted();
		}
		if (_popUp != null)
		{
			_popUp.Child = null;
			_popUp = null;
		}
	}

	private void ToastPrompt_ManipulationCompleted(object sender, ManipulationCompletedEventArgs e)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0024: Unknown result type (might be due to invalid IL or missing references)
		//IL_0029: Unknown result type (might be due to invalid IL or missing references)
		//IL_0049: Unknown result type (might be due to invalid IL or missing references)
		//IL_004e: Unknown result type (might be due to invalid IL or missing references)
		Point val = e.TotalManipulation.Translation;
		if (!(((Point)(ref val)).X > 200.0))
		{
			val = e.FinalVelocities.LinearVelocity;
			if (!(((Point)(ref val)).X > 1000.0))
			{
				val = e.TotalManipulation.Translation;
				if (((Point)(ref val)).X < 20.0)
				{
					OnCompleted();
					return;
				}
				_translate.X = 0.0;
				if (_timer != null)
				{
					_timer.Start();
				}
				return;
			}
		}
		OnCompleted();
	}

	private void ToastPrompt_ManipulationDelta(object sender, ManipulationDeltaEventArgs e)
	{
		//IL_0017: Unknown result type (might be due to invalid IL or missing references)
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		TranslateTransform translate = _translate;
		double x = _translate.X;
		Point translation = e.DeltaManipulation.Translation;
		translate.X = x + ((Point)(ref translation)).X;
		if (_translate.X < 0.0)
		{
			_translate.X = 0.0;
		}
	}

	private void ToastPrompt_ManipulationStarted(object sender, ManipulationStartedEventArgs e)
	{
		if (_timer != null)
		{
			_timer.Stop();
		}
	}

	public void Show(TimeSpan BeginTime, bool afterNavigation)
	{
		//IL_003c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0042: Expected O, but got Unknown
		((DependencyObject)this).Dispatcher.BeginInvoke((Action)delegate
		{
			try
			{
				_popUp = new DialogService
				{
					Child = (FrameworkElement)(object)this,
					BackgroundBrush = Overlay,
					IsBackKeyOverride = IsBackKeyOverride,
					BeginTime = BeginTime
				};
				_popUp.Closed += _popUp_Closed;
				if (afterNavigation)
				{
					_popUp.ShowAfterNavigating();
				}
				else
				{
					_popUp.Show();
				}
			}
			catch
			{
			}
		});
		if (IsTimerEnabled)
		{
			DispatcherTimer val = new DispatcherTimer();
			val.Interval = TimeSpan.FromMilliseconds(MillisecondsUntilHidden) + BeginTime;
			_timer = val;
			_timer.Tick += _timer_Tick;
			_timer.Start();
		}
	}

	public void Show(bool afterNavigation = false)
	{
		Show(TimeSpan.FromMilliseconds(0.0), afterNavigation);
	}

	private void _timer_Tick(object sender, EventArgs e)
	{
		DestroyTimer();
		OnCompleted();
	}

	public void OnCompleted()
	{
		DestroyTimer();
		_alreadyFired = true;
		if (_popUp != null)
		{
			_popUp.Hide();
		}
	}

	private void DestroyTimer()
	{
		if (_timer != null)
		{
			_timer.Stop();
			_timer = null;
		}
	}

	private static void OnImageSource(DependencyObject o, DependencyPropertyChangedEventArgs e)
	{
		if (o is ToastPrompt { ToastImage: not null } toastPrompt)
		{
			object newValue = ((DependencyPropertyChangedEventArgs)(ref e)).NewValue;
			toastPrompt.SetImageVisibility((ImageSource)((newValue is ImageSource) ? newValue : null));
		}
	}

	private void SetImageVisibility(ImageSource source)
	{
		((UIElement)ToastImage).Visibility = (Visibility)(source == null);
	}
}
