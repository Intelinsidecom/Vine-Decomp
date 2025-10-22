using System.ComponentModel;
using System.Windows;
using System.Windows.Controls;

namespace Vine.Controls;

public class ProgressRing : Control
{
	public class TemplateSettingValues : DependencyObject
	{
		public static readonly DependencyProperty MaxSideLengthProperty = DependencyProperty.Register("MaxSideLength", typeof(double), typeof(TemplateSettingValues), new PropertyMetadata((object)0.0));

		public static readonly DependencyProperty EllipseDiameterProperty = DependencyProperty.Register("EllipseDiameter", typeof(double), typeof(TemplateSettingValues), new PropertyMetadata((object)0.0));

		public static readonly DependencyProperty EllipseOffsetProperty = DependencyProperty.Register("EllipseOffset", typeof(Thickness), typeof(TemplateSettingValues), new PropertyMetadata((object)default(Thickness)));

		public double MaxSideLength
		{
			get
			{
				return (double)((DependencyObject)this).GetValue(MaxSideLengthProperty);
			}
			set
			{
				((DependencyObject)this).SetValue(MaxSideLengthProperty, (object)value);
			}
		}

		public double EllipseDiameter
		{
			get
			{
				return (double)((DependencyObject)this).GetValue(EllipseDiameterProperty);
			}
			set
			{
				((DependencyObject)this).SetValue(EllipseDiameterProperty, (object)value);
			}
		}

		public Thickness EllipseOffset
		{
			get
			{
				//IL_000b: Unknown result type (might be due to invalid IL or missing references)
				return (Thickness)((DependencyObject)this).GetValue(EllipseOffsetProperty);
			}
			set
			{
				//IL_0006: Unknown result type (might be due to invalid IL or missing references)
				((DependencyObject)this).SetValue(EllipseOffsetProperty, (object)value);
			}
		}

		public TemplateSettingValues(double width)
		{
			//IL_002d: Unknown result type (might be due to invalid IL or missing references)
			MaxSideLength = 400.0;
			EllipseDiameter = width / 10.0;
			EllipseOffset = new Thickness(EllipseDiameter);
		}
	}

	private bool hasAppliedTemplate;

	public static readonly DependencyProperty IsActiveProperty = DependencyProperty.Register("IsActive", typeof(bool), typeof(ProgressRing), new PropertyMetadata((object)false, new PropertyChangedCallback(IsActiveChanged)));

	public static readonly DependencyProperty TemplateSettingsProperty = DependencyProperty.Register("TemplateSettings", typeof(TemplateSettingValues), typeof(ProgressRing), new PropertyMetadata((object)new TemplateSettingValues(100.0)));

	public bool IsActive
	{
		get
		{
			return (bool)((DependencyObject)this).GetValue(IsActiveProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(IsActiveProperty, (object)value);
		}
	}

	public TemplateSettingValues TemplateSettings
	{
		get
		{
			return (TemplateSettingValues)((DependencyObject)this).GetValue(TemplateSettingsProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(TemplateSettingsProperty, (object)value);
		}
	}

	public ProgressRing()
	{
		((Control)this).DefaultStyleKey = typeof(ProgressRing);
		TemplateSettings = new TemplateSettingValues(60.0);
	}

	public override void OnApplyTemplate()
	{
		((FrameworkElement)this).OnApplyTemplate();
		hasAppliedTemplate = true;
		UpdateState(IsActive);
	}

	private void UpdateState(bool isActive)
	{
		if (hasAppliedTemplate)
		{
			string text = (isActive ? "Active" : "Inactive");
			VisualStateManager.GoToState((Control)(object)this, text, true);
		}
	}

	protected override Size MeasureOverride(Size availableSize)
	{
		//IL_003f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0040: Unknown result type (might be due to invalid IL or missing references)
		double width = 100.0;
		if (!DesignerProperties.IsInDesignTool)
		{
			width = ((((FrameworkElement)this).Width != double.NaN) ? ((FrameworkElement)this).Width : ((Size)(ref availableSize)).Width);
		}
		TemplateSettings = new TemplateSettingValues(width);
		return ((FrameworkElement)this).MeasureOverride(availableSize);
	}

	private static void IsActiveChanged(DependencyObject d, DependencyPropertyChangedEventArgs args)
	{
		ProgressRing obj = (ProgressRing)(object)d;
		bool isActive = (bool)((DependencyPropertyChangedEventArgs)(ref args)).NewValue;
		obj.UpdateState(isActive);
	}
}
