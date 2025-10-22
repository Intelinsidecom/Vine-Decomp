using System;
using System.Diagnostics;
using System.Text.RegularExpressions;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using GalaSoft.MvvmLight.Command;
using Gen.Services;
using Vine.Converters;
using Vine.Services;
using Vine.Services.Response.Notifications;

namespace Vine;

public class NotificationControl : UserControl
{
	public static readonly DependencyProperty NotificationProperty = DependencyProperty.Register("Notification", typeof(INotification), typeof(NotificationControl), new PropertyMetadata((object)null, new PropertyChangedCallback(NotificationCallback)));

	private Regex regNotif = new Regex("<:\\s*(?<type>[^\\|\\s]+)\\s*\\|\\s*(?<id>[^\\|\\s]+)\\s*:>(?<text>.*?)<:>", RegexOptions.Compiled | RegexOptions.Singleline);

	internal Grid LayoutRoot;

	internal Image Avatar;

	internal RichTextBox BodyText;

	internal TextBlock Date;

	internal Border PicturePanel;

	internal Image Picture;

	private bool _contentLoaded;

	public INotification Notification
	{
		get
		{
			return (INotification)((DependencyObject)this).GetValue(NotificationProperty);
		}
		set
		{
			((DependencyObject)this).SetValue(NotificationProperty, (object)value);
		}
	}

	public RelayCommand<string> UserCommand { get; set; }

	public RelayCommand<string> PostCommand { get; set; }

	public NotificationControl()
	{
		InitializeComponent();
		UserCommand = new RelayCommand<string>(delegate(string s)
		{
			NavigationServiceExt.ToProfile(s);
		});
		PostCommand = new RelayCommand<string>(delegate(string s)
		{
			NavigationServiceExt.ToPost(s);
		});
	}

	private static void NotificationCallback(DependencyObject d, DependencyPropertyChangedEventArgs e)
	{
		((NotificationControl)(object)d).NotificationCallback((INotification)((DependencyPropertyChangedEventArgs)(ref e)).NewValue);
	}

	private void NotificationCallback(INotification recordNotification)
	{
		//IL_00ad: Unknown result type (might be due to invalid IL or missing references)
		//IL_00b3: Expected O, but got Unknown
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		//IL_0032: Expected O, but got Unknown
		//IL_005d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0067: Expected O, but got Unknown
		//IL_0106: Unknown result type (might be due to invalid IL or missing references)
		//IL_010b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0112: Unknown result type (might be due to invalid IL or missing references)
		//IL_0119: Unknown result type (might be due to invalid IL or missing references)
		//IL_011a: Unknown result type (might be due to invalid IL or missing references)
		//IL_0124: Unknown result type (might be due to invalid IL or missing references)
		//IL_0139: Unknown result type (might be due to invalid IL or missing references)
		//IL_0143: Expected O, but got Unknown
		//IL_0143: Unknown result type (might be due to invalid IL or missing references)
		//IL_0158: Unknown result type (might be due to invalid IL or missing references)
		//IL_0162: Expected O, but got Unknown
		//IL_0164: Expected O, but got Unknown
		//IL_00e6: Unknown result type (might be due to invalid IL or missing references)
		//IL_00eb: Unknown result type (might be due to invalid IL or missing references)
		//IL_0106: Expected O, but got Unknown
		//IL_026f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0274: Unknown result type (might be due to invalid IL or missing references)
		//IL_0286: Expected O, but got Unknown
		try
		{
			Uri uri = new Uri(string.IsNullOrEmpty(recordNotification.Avatar) ? "http://v.cdn.vine.co/avatars/default.png" : recordNotification.Avatar, UriKind.Absolute);
			Avatar.Source = (ImageSource)new BitmapImage(uri);
			if (!string.IsNullOrEmpty(recordNotification.Picture))
			{
				((UIElement)PicturePanel).Visibility = (Visibility)0;
				Picture.Source = (ImageSource)new BitmapImage(new Uri(recordNotification.Picture, UriKind.Absolute));
			}
			else
			{
				((UIElement)PicturePanel).Visibility = (Visibility)1;
				Picture.Source = null;
			}
		}
		catch
		{
		}
		Date.Text = DateStringToTimePassedConverter.ConvertDate(recordNotification.Created);
		MatchCollection matchCollection = regNotif.Matches(recordNotification.Body);
		Paragraph val = new Paragraph();
		int num = 0;
		string body = recordNotification.Body;
		foreach (Match item in matchCollection)
		{
			if (item.Index > num)
			{
				((PresentationFrameworkCollection<Inline>)(object)val.Inlines).Add((Inline)new Run
				{
					Text = body.Substring(num, item.Index - num)
				});
			}
			Hyperlink val2 = new Hyperlink
			{
				MouseOverTextDecorations = null,
				TextDecorations = null,
				FontWeight = FontWeights.SemiBold,
				Foreground = (Brush)Application.Current.Resources[(object)"PrincBrush"],
				MouseOverForeground = (Brush)Application.Current.Resources[(object)"PrincBrush"]
			};
			string value = item.Groups["type"].Value;
			if (!(value == "user"))
			{
				if (value == "post")
				{
					val2.Command = PostCommand;
					val2.CommandParameter = item.Groups["id"].Value.Substring(12);
				}
			}
			else
			{
				val2.Command = UserCommand;
				val2.CommandParameter = item.Groups["id"].Value.Substring(15);
			}
			((Span)val2).Inlines.Add(item.Groups["text"].Value);
			((PresentationFrameworkCollection<Inline>)(object)val.Inlines).Add((Inline)(object)val2);
			num = item.Index + item.Length;
		}
		if (num < body.Length)
		{
			((PresentationFrameworkCollection<Inline>)(object)val.Inlines).Add((Inline)new Run
			{
				Text = body.Substring(num)
			});
		}
		((PresentationFrameworkCollection<Block>)(object)BodyText.Blocks).Clear();
		((PresentationFrameworkCollection<Block>)(object)BodyText.Blocks).Add((Block)(object)val);
	}

	private void Avatar_Tap(object sender, GestureEventArgs e)
	{
		NavigationServiceExt.ToProfile(Notification.UserId);
	}

	private void MainPicture_Tap(object sender, GestureEventArgs e)
	{
		NavigationServiceExt.ToTimeline(UriServiceProvider.GetUriForOnePicture(((RecordNotification)Notification).PostId));
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
		if (!_contentLoaded)
		{
			_contentLoaded = true;
			Application.LoadComponent((object)this, new Uri("/Vine;component/Pages/Notifications/NotificationControl.xaml", UriKind.Relative));
			LayoutRoot = (Grid)((FrameworkElement)this).FindName("LayoutRoot");
			Avatar = (Image)((FrameworkElement)this).FindName("Avatar");
			BodyText = (RichTextBox)((FrameworkElement)this).FindName("BodyText");
			Date = (TextBlock)((FrameworkElement)this).FindName("Date");
			PicturePanel = (Border)((FrameworkElement)this).FindName("PicturePanel");
			Picture = (Image)((FrameworkElement)this).FindName("Picture");
		}
	}
}
