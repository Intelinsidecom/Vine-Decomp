using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text.RegularExpressions;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using GalaSoft.MvvmLight.Command;
using GalaSoft.MvvmLight.Messaging;
using Gen.Services;
using Microsoft.Phone.Controls;
using Vine.Converters;
using Vine.Resources;
using Vine.Services;
using Windows.System;

namespace Vine.Controls;

public class CommentControl : UserControl
{
	public static readonly DependencyProperty CommentProperty = DependencyProperty.Register("Comment", typeof(IComment), typeof(CommentControl), new PropertyMetadata((object)null, new PropertyChangedCallback(CommentCallback)));

	private ICommand HashTagCommand;

	private static Regex regHashTag = new Regex("#\\w+", RegexOptions.Compiled | RegexOptions.Singleline);

	private RelayCommand<string> UriCommand;

	internal Border AvatarBorder;

	internal Image Avatar;

	internal TextBlock Date;

	internal TextBlock NameTxt;

	internal RichTextBox CommentTxt;

	private bool _contentLoaded;

	public RelayCommand<string> PeopleTagCommand { get; set; }

	public IComment Comment
	{
		get
		{
			return (IComment)((DependencyObject)this).GetValue(CommentProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(CommentProperty, (object)value);
		}
	}

	public event SelectHashTagHandler SelectedHashTag;

	public CommentControl()
	{
		InitializeComponent();
		HashTagCommand = new RelayCommand<string>(delegate(string s)
		{
			NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriForTag(s));
		});
		PeopleTagCommand = new RelayCommand<string>(delegate(string s)
		{
			NavigationServiceExt.ToProfileFromUsername(s);
		});
		UriCommand = new RelayCommand<string>(delegate(string s)
		{
			Launcher.LaunchUriAsync(new Uri(s));
		});
	}

	private void blockmenu_Click(object sender, RoutedEventArgs e)
	{
		Messenger.Default.Send(new NotificationMessage<IComment>(Comment, "COMMENTREMOVED"));
	}

	private void Reply_Click(object sender, RoutedEventArgs e)
	{
		Messenger.Default.Send(new NotificationMessage<IComment>(Comment, "COMMENTREPLY"));
	}

	private static void CommentCallback(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		((CommentControl)(object)d).CommentCallback((IComment)((DependencyPropertyChangedEventArgs)(ref e)).NewValue);
	}

	private void CommentCallback(IComment comment)
	{
		//IL_0076: Unknown result type (might be due to invalid IL or missing references)
		//IL_007b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0085: Expected O, but got Unknown
		//IL_00a6: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b0: Expected O, but got Unknown
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		//IL_0045: Unknown result type (might be due to invalid IL or missing references)
		//IL_004f: Expected O, but got Unknown
		//IL_0153: Unknown result type (might be due to invalid IL or missing references)
		//IL_015a: Expected O, but got Unknown
		//IL_01c2: Unknown result type (might be due to invalid IL or missing references)
		//IL_01c7: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ce: Unknown result type (might be due to invalid IL or missing references)
		//IL_01db: Unknown result type (might be due to invalid IL or missing references)
		//IL_01e8: Unknown result type (might be due to invalid IL or missing references)
		//IL_01ef: Unknown result type (might be due to invalid IL or missing references)
		//IL_01f0: Unknown result type (might be due to invalid IL or missing references)
		//IL_01fa: Unknown result type (might be due to invalid IL or missing references)
		//IL_020f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0219: Expected O, but got Unknown
		//IL_0219: Unknown result type (might be due to invalid IL or missing references)
		//IL_022e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0238: Expected O, but got Unknown
		//IL_023a: Expected O, but got Unknown
		//IL_01a2: Unknown result type (might be due to invalid IL or missing references)
		//IL_01a7: Unknown result type (might be due to invalid IL or missing references)
		//IL_01c2: Expected O, but got Unknown
		//IL_029a: Unknown result type (might be due to invalid IL or missing references)
		//IL_029f: Unknown result type (might be due to invalid IL or missing references)
		//IL_02b1: Expected O, but got Unknown
		if (comment == null)
		{
			return;
		}
		if (Avatar.Source != null)
		{
			((BitmapImage)Avatar.Source).UriSource = null;
			Avatar.Source = null;
		}
		try
		{
			Avatar.Source = (ImageSource)new BitmapImage(new Uri(comment.Avatar));
		}
		catch
		{
		}
		try
		{
			Date.Text = ShortDateStringToTimePassedConverter.ConvertDate(comment.Created);
		}
		catch
		{
		}
		ContextMenu contextMenu = new ContextMenu();
		((Control)contextMenu).Background = (Brush)new SolidColorBrush(Colors.White);
		contextMenu.IsZoomEnabled = false;
		ContextMenu contextMenu2 = contextMenu;
		MenuItem menuItem = new MenuItem
		{
			Header = AppResources.ReplyComment
		};
		menuItem.Click += new RoutedEventHandler(Reply_Click);
		((PresentationFrameworkCollection<object>)(object)((ItemsControl)contextMenu2).Items).Add((object)menuItem);
		ContextMenuService.SetContextMenu((DependencyObject)(object)this, contextMenu2);
		try
		{
			NameTxt.Text = comment.Username;
			int num = 0;
			string comment2 = comment.Comment;
			List<Tuple<ICommand, int, string, string>> list = new List<Tuple<ICommand, int, string, string>>();
			foreach (Match item in regHashTag.Matches(comment2))
			{
				list.Add(new Tuple<ICommand, int, string, string>(HashTagCommand, item.Index, item.Value, item.Value.Substring(1)));
			}
			Paragraph val = new Paragraph();
			foreach (Tuple<ICommand, int, string, string> item2 in list.OrderBy((Tuple<ICommand, int, string, string> l) => l.Item2))
			{
				if (item2.Item2 > num)
				{
					((PresentationFrameworkCollection<Inline>)(object)val.Inlines).Add((Inline)new Run
					{
						Text = comment2.Substring(num, item2.Item2 - num)
					});
				}
				Hyperlink val2 = new Hyperlink
				{
					MouseOverTextDecorations = null,
					Command = item2.Item1,
					CommandParameter = item2.Item4,
					TextDecorations = null,
					FontWeight = FontWeights.SemiBold,
					Foreground = (Brush)Application.Current.Resources[(object)"DarkGreyBrush"],
					MouseOverForeground = (Brush)Application.Current.Resources[(object)"DarkGreyBrush"]
				};
				((Span)val2).Inlines.Add(item2.Item3);
				((PresentationFrameworkCollection<Inline>)(object)val.Inlines).Add((Inline)(object)val2);
				num = item2.Item2 + item2.Item3.Length;
			}
			if (num < comment2.Length)
			{
				((PresentationFrameworkCollection<Inline>)(object)val.Inlines).Add((Inline)new Run
				{
					Text = comment2.Substring(num)
				});
			}
			((PresentationFrameworkCollection<Block>)(object)CommentTxt.Blocks).Clear();
			((PresentationFrameworkCollection<Block>)(object)CommentTxt.Blocks).Add((Block)(object)val);
		}
		catch
		{
		}
	}

	private void Profile_Tap(object sender, GestureEventArgs e)
	{
		NavigationServiceExt.ToProfile(Comment.UserId);
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Controls/CommentControl.xaml", UriKind.Relative));
			AvatarBorder = (Border)((FrameworkElement)this).FindName("AvatarBorder");
			Avatar = (Image)((FrameworkElement)this).FindName("Avatar");
			Date = (TextBlock)((FrameworkElement)this).FindName("Date");
			NameTxt = (TextBlock)((FrameworkElement)this).FindName("NameTxt");
			CommentTxt = (RichTextBox)((FrameworkElement)this).FindName("CommentTxt");
		}
	}
}
