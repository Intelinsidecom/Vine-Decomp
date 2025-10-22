using System;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Media;
using System.Windows.Shapes;
using Vine.Services.Models;

namespace Vine.Controls;

public class DirectStateControl : UserControl
{
	private static Brush SeenBrush;

	private static Brush LikedBrush;

	private static Brush CommentedBrush;

	public static readonly DependencyProperty StateProperty;

	internal Grid LayoutRoot;

	internal Ellipse Round;

	internal Path StatePath;

	private bool _contentLoaded;

	public DirectState State
	{
		get
		{
			return (DirectState)((DependencyObject)this).GetValue(StateProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(StateProperty, (object)value);
		}
	}

	static DirectStateControl()
	{
		//IL_0026: Unknown result type (might be due to invalid IL or missing references)
		//IL_0030: Expected O, but got Unknown
		//IL_002b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0035: Expected O, but got Unknown
		//IL_0048: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0057: Expected O, but got Unknown
		//IL_0065: Unknown result type (might be due to invalid IL or missing references)
		//IL_006a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0074: Expected O, but got Unknown
		//IL_0085: Unknown result type (might be due to invalid IL or missing references)
		//IL_008a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0094: Expected O, but got Unknown
		StateProperty = DependencyProperty.Register("State", typeof(DirectState), typeof(DirectStateControl), new PropertyMetadata((object)DirectState.UNSEEN, new PropertyChangedCallback(StateCallback)));
		LikedBrush = (Brush)new SolidColorBrush(Color.FromArgb(byte.MaxValue, byte.MaxValue, (byte)57, (byte)46));
		SeenBrush = (Brush)new SolidColorBrush(Color.FromArgb(byte.MaxValue, (byte)122, (byte)198, (byte)93));
		CommentedBrush = (Brush)new SolidColorBrush(Color.FromArgb(byte.MaxValue, (byte)57, (byte)159, (byte)223));
	}

	public DirectStateControl()
	{
		InitializeComponent();
	}

	private static void StateCallback(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		DirectState val = (DirectState)((DependencyPropertyChangedEventArgs)(ref e)).NewValue;
		((DirectStateControl)(object)d).StateCallback(val);
	}

	private void StateCallback(DirectState val)
	{
		//IL_0041: Unknown result type (might be due to invalid IL or missing references)
		//IL_0046: Unknown result type (might be due to invalid IL or missing references)
		//IL_0066: Expected O, but got Unknown
		//IL_0089: Unknown result type (might be due to invalid IL or missing references)
		//IL_008e: Unknown result type (might be due to invalid IL or missing references)
		//IL_00ae: Expected O, but got Unknown
		//IL_00d1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d6: Unknown result type (might be due to invalid IL or missing references)
		//IL_00f6: Expected O, but got Unknown
		if (val == DirectState.UNSEEN)
		{
			((UIElement)LayoutRoot).Visibility = (Visibility)1;
			return;
		}
		((UIElement)LayoutRoot).Visibility = (Visibility)0;
		switch (val)
		{
		case DirectState.SEEN:
		{
			((Shape)Round).Fill = SeenBrush;
			Binding val4 = new Binding
			{
				Source = (string)Application.Current.Resources[(object)"CheckPath"]
			};
			BindingOperations.SetBinding((DependencyObject)(object)StatePath, Path.DataProperty, (BindingBase)(object)val4);
			break;
		}
		case DirectState.LIKED:
		{
			((Shape)Round).Fill = LikedBrush;
			Binding val3 = new Binding
			{
				Source = (string)Application.Current.Resources[(object)"LikePath"]
			};
			BindingOperations.SetBinding((DependencyObject)(object)StatePath, Path.DataProperty, (BindingBase)(object)val3);
			break;
		}
		case DirectState.COMMENTED:
		{
			((Shape)Round).Fill = CommentedBrush;
			Binding val2 = new Binding
			{
				Source = (string)Application.Current.Resources[(object)"CommentPath"]
			};
			BindingOperations.SetBinding((DependencyObject)(object)StatePath, Path.DataProperty, (BindingBase)(object)val2);
			break;
		}
		}
	}

	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Controls/DirectStateControl.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			Round = (Ellipse)((FrameworkElement)this).FindName("Round");
			StatePath = (Path)((FrameworkElement)this).FindName("StatePath");
		}
	}
}
