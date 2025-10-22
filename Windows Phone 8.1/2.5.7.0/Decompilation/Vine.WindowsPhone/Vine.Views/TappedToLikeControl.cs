using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Media.Animation;
using Windows.UI.Xaml.Shapes;

namespace Vine.Views;

public sealed class TappedToLikeControl : ContentControl, IComponentConnector
{
	private const int WidthOffset = 52;

	private const int HeightOffset = 46;

	private const int RotationAngle = 8;

	private static Random _randomGenerator;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private CompositeTransform RotationTransform;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Storyboard MoveUpStoryboard;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Storyboard EntranceStoryboard;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Storyboard EnteredStoryboard;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Storyboard ExitStoryboard;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private Path TapToLike;

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	private bool _contentLoaded;

	public int ZRotation { get; private set; }

	private static Random RandomGenerator
	{
		get
		{
			if (_randomGenerator == null)
			{
				_randomGenerator = new Random();
			}
			return _randomGenerator;
		}
	}

	public TappedToLikeControl()
	{
		InitializeComponent();
	}

	public int Init(double x, double y, int? lastRotatedAngle)
	{
		double left = x - 52.0;
		double top = y - 46.0;
		((FrameworkElement)this).put_Margin(new Thickness(left, top, 0.0, 0.0));
		if (lastRotatedAngle.HasValue)
		{
			ZRotation = lastRotatedAngle.Value * -1;
		}
		else if (RandomGenerator.Next() % 2 == 0)
		{
			ZRotation = 8;
		}
		else
		{
			ZRotation = 8;
		}
		return ZRotation;
	}

	protected override void OnApplyTemplate()
	{
		((FrameworkElement)this).OnApplyTemplate();
		EntranceStoryboard.Begin();
		MoveUpStoryboard.Begin();
		Storyboard entranceStoryboard = EntranceStoryboard;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)((Timeline)entranceStoryboard).add_Completed, (Action<EventRegistrationToken>)((Timeline)entranceStoryboard).remove_Completed, (EventHandler<object>)EntranceStoryboard_Completed);
	}

	private void EntranceStoryboard_Completed(object sender, object e)
	{
		EnteredStoryboard.Begin();
		Storyboard enteredStoryboard = EnteredStoryboard;
		WindowsRuntimeMarshal.AddEventHandler((Func<EventHandler<object>, EventRegistrationToken>)((Timeline)enteredStoryboard).add_Completed, (Action<EventRegistrationToken>)((Timeline)enteredStoryboard).remove_Completed, (EventHandler<object>)EnteredStoryboard_Completed);
		WindowsRuntimeMarshal.RemoveEventHandler((Action<EventRegistrationToken>)((Timeline)EntranceStoryboard).remove_Completed, (EventHandler<object>)EntranceStoryboard_Completed);
	}

	private void EnteredStoryboard_Completed(object sender, object e)
	{
		ExitStoryboard.Begin();
		WindowsRuntimeMarshal.RemoveEventHandler((Action<EventRegistrationToken>)((Timeline)EnteredStoryboard).remove_Completed, (EventHandler<object>)EntranceStoryboard_Completed);
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void InitializeComponent()
	{
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0037: Expected O, but got Unknown
		//IL_0043: Unknown result type (might be due to invalid IL or missing references)
		//IL_004d: Expected O, but got Unknown
		//IL_0059: Unknown result type (might be due to invalid IL or missing references)
		//IL_0063: Expected O, but got Unknown
		//IL_006f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0079: Expected O, but got Unknown
		//IL_0085: Unknown result type (might be due to invalid IL or missing references)
		//IL_008f: Expected O, but got Unknown
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("ms-appx:///Views/TappedToLikeControl.xaml"), (ComponentResourceLocation)0);
			RotationTransform = (CompositeTransform)((FrameworkElement)this).FindName("RotationTransform");
			MoveUpStoryboard = (Storyboard)((FrameworkElement)this).FindName("MoveUpStoryboard");
			EntranceStoryboard = (Storyboard)((FrameworkElement)this).FindName("EntranceStoryboard");
			EnteredStoryboard = (Storyboard)((FrameworkElement)this).FindName("EnteredStoryboard");
			ExitStoryboard = (Storyboard)((FrameworkElement)this).FindName("ExitStoryboard");
			TapToLike = (Path)((FrameworkElement)this).FindName("TapToLike");
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	public void Connect(int connectionId, object target)
	{
		_contentLoaded = true;
	}
}
