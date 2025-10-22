using System;
using System.Diagnostics;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using Vine.Pages.Main.ViewModels;
using Vine.Services.Models;
using Vine.Utils;

namespace Vine.Controls;

public class GroupPostControl : UserControl
{
	private static bool _isPhablet;

	public static readonly DependencyProperty PostProperty;

	internal Grid ThumbPanel;

	internal Image Thumb;

	internal Path ThumbVideo;

	private bool _contentLoaded;

	public IPostRecord Post
	{
		get
		{
			return (IPostRecord)((DependencyObject)this).GetValue(PostProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(PostProperty, (object)value);
		}
	}

	static GroupPostControl()
	{
		//IL_0021: Unknown result type (might be due to invalid IL or missing references)
		//IL_002b: Expected O, but got Unknown
		//IL_0026: Unknown result type (might be due to invalid IL or missing references)
		//IL_0030: Expected O, but got Unknown
		PostProperty = DependencyProperty.Register("Post", typeof(IPostRecord), typeof(GroupPostControl), new PropertyMetadata((object)null, new PropertyChangedCallback(PostCallback)));
		_isPhablet = PhoneScreenSizeHelper.IsPhablet();
	}

	public GroupPostControl()
	{
		InitializeComponent();
	}

	private static void PostCallback(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		((GroupPostControl)(object)d).PostCallback((IPostRecord)((DependencyPropertyChangedEventArgs)(ref e)).NewValue);
	}

	private void PostCallback(IPostRecord post)
	{
		//IL_0018: Unknown result type (might be due to invalid IL or missing references)
		//IL_0065: Unknown result type (might be due to invalid IL or missing references)
		//IL_006f: Expected O, but got Unknown
		//IL_009c: Unknown result type (might be due to invalid IL or missing references)
		//IL_00a6: Expected O, but got Unknown
		if (Thumb.Source != null)
		{
			((BitmapImage)Thumb.Source).UriSource = null;
			Thumb.Source = null;
		}
		if (post == null)
		{
			((UIElement)ThumbPanel).Visibility = (Visibility)1;
			return;
		}
		((UIElement)ThumbPanel).Visibility = (Visibility)0;
		if (post is MorePost)
		{
			Thumb.Source = (ImageSource)new BitmapImage(new Uri(post.Thumb, UriKind.Relative));
			((UIElement)ThumbVideo).Visibility = (Visibility)1;
		}
		else
		{
			Thumb.Source = (ImageSource)new BitmapImage(new Uri(_isPhablet ? post.Thumb : post.MinThumb));
			((UIElement)ThumbVideo).Visibility = (Visibility)(!post.IsVideo);
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
			Application.LoadComponent((object)this, new Uri("/Vine;component/Controls/GroupPostControl.xaml", UriKind.Relative));
			ThumbPanel = (Grid)((FrameworkElement)this).FindName("ThumbPanel");
			Thumb = (Image)((FrameworkElement)this).FindName("Thumb");
			ThumbVideo = (Path)((FrameworkElement)this).FindName("ThumbVideo");
		}
	}
}
