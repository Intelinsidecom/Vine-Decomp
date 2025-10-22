using System;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Shapes;
using Vine.Datas;
using Vine.Services.Models;

namespace Vine.Controls;

public class PostControlBack : UserControl
{
	private PostControl _postControl;

	internal Grid BackPanel;

	internal Grid ContentPanel;

	internal Border Option2;

	internal Border Option2Bis;

	internal TextBlock Option2BisTextBlock;

	internal Grid BackLike;

	internal Path LikedPath;

	internal ProgressBar LikedProgress;

	internal ProgressBar RevineProgress;

	internal Button RemovePanel;

	internal Button BackShowMenuButton;

	internal Rectangle ShadowB1;

	internal Rectangle ShadowB2;

	private bool _contentLoaded;

	public PostControlBack(PostControl postControl, bool displayAtBottom)
	{
		//IL_004c: Unknown result type (might be due to invalid IL or missing references)
		//IL_00cd: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d2: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d9: Unknown result type (might be due to invalid IL or missing references)
		_postControl = postControl;
		InitializeComponent();
		if (displayAtBottom)
		{
			((FrameworkElement)ContentPanel).VerticalAlignment = (VerticalAlignment)2;
			((FrameworkElement)ContentPanel).Margin = new Thickness(0.0, 0.0, 0.0, 48.0);
		}
		if (postControl.Post is VinePostRecord vinePostRecord)
		{
			((UIElement)Option2Bis).Visibility = (Visibility)(vinePostRecord.IsMyPost ? 1 : 0);
			((UIElement)Option2Bis).Opacity = ((vinePostRecord.MyRepostId != "0") ? 0.33 : 1.0);
		}
		else
		{
			((UIElement)Option2Bis).Visibility = (Visibility)1;
		}
		Rectangle shadowB = ShadowB2;
		Visibility visibility = (((UIElement)ShadowB1).Visibility = ((UIElement)postControl.ShadowF1).Visibility);
		((UIElement)shadowB).Visibility = visibility;
	}

	private void Option2_Tap(object sender, GestureEventArgs e)
	{
	}

	private void Option2Bis_Tap(object sender, GestureEventArgs e)
	{
		_postControl.RegramAction();
	}

	private void BackComment_Tap(object sender, GestureEventArgs e)
	{
		_postControl.CommentAction();
	}

	private async void Liked_Tap(object sender, GestureEventArgs e)
	{
		if (DatasProvider.Instance.CurrentUser != null)
		{
			((UIElement)BackLike).Opacity = 0.2;
			LikedProgress.IsIndeterminate = true;
			if (_postControl != null)
			{
				_postControl.LikeAction();
			}
		}
	}

	private void Share_Tap(object sender, GestureEventArgs e)
	{
		_postControl.SharePost();
	}

	private void Option_Tap(object sender, GestureEventArgs e)
	{
		//IL_001a: Unknown result type (might be due to invalid IL or missing references)
		((UIElement)(FrameworkElement)sender).Opacity = 0.5;
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			//IL_0017: Unknown result type (might be due to invalid IL or missing references)
			_postControl.SavePicture();
			((UIElement)(FrameworkElement)sender).Opacity = 1.0;
		});
	}

	private async void Remove_Click(object sender, RoutedEventArgs e)
	{
		if (_postControl.Post.RepostById == DatasProvider.Instance.CurrentUser.Id)
		{
			_postControl.RegramAction();
		}
		else
		{
			_postControl.RemoveAction();
		}
	}

	private void BackTurnPost_Click(object sender, RoutedEventArgs e)
	{
		_postControl.BackTurnAction();
	}

	public void SetRecord(IPostRecord record)
	{
		//IL_008c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0065: Unknown result type (might be due to invalid IL or missing references)
		if (record is VinePostRecord)
		{
			((UIElement)RemovePanel).Visibility = (Visibility)(!((VinePostRecord)record).IsMyPostOrRevinedByMe);
		}
		else
		{
			((UIElement)RemovePanel).Visibility = (Visibility)(!record.IsMyPost);
		}
		if (_postControl.FullScreen)
		{
			((PresentationFrameworkCollection<ColumnDefinition>)(object)BackPanel.ColumnDefinitions)[1].Width = new GridLength(1.0, (GridUnitType)2);
		}
		else
		{
			((PresentationFrameworkCollection<ColumnDefinition>)(object)BackPanel.ColumnDefinitions)[1].Width = new GridLength(452.0, (GridUnitType)1);
		}
		SetLike(record.Liked);
	}

	internal void SetLike(bool liked)
	{
		//IL_0072: Unknown result type (might be due to invalid IL or missing references)
		//IL_007c: Expected O, but got Unknown
		//IL_001d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0027: Expected O, but got Unknown
		//IL_0041: Unknown result type (might be due to invalid IL or missing references)
		//IL_004b: Expected O, but got Unknown
		if (liked)
		{
			((Shape)LikedPath).Stroke = (Brush)Application.Current.Resources[(object)"LikeBrush"];
			((Shape)LikedPath).Fill = (Brush)Application.Current.Resources[(object)"LikeOpacityBrush"];
		}
		else
		{
			((Shape)LikedPath).Fill = null;
			((Shape)LikedPath).Stroke = (Brush)Application.Current.Resources[(object)"BackCommandBrush"];
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
		//IL_006f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0079: Expected O, but got Unknown
		//IL_0085: Unknown result type (might be due to invalid IL or missing references)
		//IL_008f: Expected O, but got Unknown
		//IL_009b: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a5: Expected O, but got Unknown
		//IL_00b1: Unknown result type (might be due to invalid IL or missing references)
		//IL_00bb: Expected O, but got Unknown
		//IL_00c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_00d1: Expected O, but got Unknown
		//IL_00dd: Unknown result type (might be due to invalid IL or missing references)
		//IL_00e7: Expected O, but got Unknown
		//IL_00f3: Unknown result type (might be due to invalid IL or missing references)
		//IL_00fd: Expected O, but got Unknown
		//IL_0109: Unknown result type (might be due to invalid IL or missing references)
		//IL_0113: Expected O, but got Unknown
		//IL_011f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0129: Expected O, but got Unknown
		//IL_0135: Unknown result type (might be due to invalid IL or missing references)
		//IL_013f: Expected O, but got Unknown
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Controls/PostControlBack.xaml", UriKind.Relative));
			BackPanel = (Grid)((FrameworkElement)this).FindName("BackPanel");
			ContentPanel = (Grid)((FrameworkElement)this).FindName("ContentPanel");
			Option2 = (Border)((FrameworkElement)this).FindName("Option2");
			Option2Bis = (Border)((FrameworkElement)this).FindName("Option2Bis");
			Option2BisTextBlock = (TextBlock)((FrameworkElement)this).FindName("Option2BisTextBlock");
			BackLike = (Grid)((FrameworkElement)this).FindName("BackLike");
			LikedPath = (Path)((FrameworkElement)this).FindName("LikedPath");
			LikedProgress = (ProgressBar)((FrameworkElement)this).FindName("LikedProgress");
			RevineProgress = (ProgressBar)((FrameworkElement)this).FindName("RevineProgress");
			RemovePanel = (Button)((FrameworkElement)this).FindName("RemovePanel");
			BackShowMenuButton = (Button)((FrameworkElement)this).FindName("BackShowMenuButton");
			ShadowB1 = (Rectangle)((FrameworkElement)this).FindName("ShadowB1");
			ShadowB2 = (Rectangle)((FrameworkElement)this).FindName("ShadowB2");
		}
	}
}
