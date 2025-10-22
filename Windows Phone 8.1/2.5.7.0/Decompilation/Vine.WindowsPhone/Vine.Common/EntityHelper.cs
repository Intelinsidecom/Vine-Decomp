using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Vine.Framework;
using Vine.Models;
using Vine.Views;
using Windows.UI.Text;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Documents;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;

namespace Vine.Common;

public class EntityHelper
{
	public static RichTextBlock RichTextBlock(string body, List<Entity> entities, bool isLargeFont, Brush entityColor = null, bool boldFirst = false, bool isInteraction = false, double? fontSize = null)
	{
		//IL_0002: Unknown result type (might be due to invalid IL or missing references)
		//IL_0008: Expected O, but got Unknown
		//IL_000f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0015: Expected O, but got Unknown
		//IL_0028: Unknown result type (might be due to invalid IL or missing references)
		//IL_002d: Unknown result type (might be due to invalid IL or missing references)
		//IL_003a: Expected O, but got Unknown
		//IL_03ed: Unknown result type (might be due to invalid IL or missing references)
		//IL_03f2: Unknown result type (might be due to invalid IL or missing references)
		//IL_03ff: Expected O, but got Unknown
		//IL_024e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0253: Unknown result type (might be due to invalid IL or missing references)
		//IL_0268: Unknown result type (might be due to invalid IL or missing references)
		//IL_0272: Expected O, but got Unknown
		//IL_0272: Unknown result type (might be due to invalid IL or missing references)
		//IL_011a: Unknown result type (might be due to invalid IL or missing references)
		//IL_011f: Unknown result type (might be due to invalid IL or missing references)
		//IL_02cf: Unknown result type (might be due to invalid IL or missing references)
		//IL_02dd: Unknown result type (might be due to invalid IL or missing references)
		//IL_02e5: Unknown result type (might be due to invalid IL or missing references)
		//IL_0145: Expected O, but got Unknown
		//IL_02fd: Unknown result type (might be due to invalid IL or missing references)
		//IL_014f: Unknown result type (might be due to invalid IL or missing references)
		//IL_030c: Expected O, but got Unknown
		//IL_0174: Unknown result type (might be due to invalid IL or missing references)
		//IL_01a8: Unknown result type (might be due to invalid IL or missing references)
		//IL_01b2: Expected O, but got Unknown
		//IL_03a6: Unknown result type (might be due to invalid IL or missing references)
		//IL_03b0: Expected O, but got Unknown
		//IL_03b0: Unknown result type (might be due to invalid IL or missing references)
		//IL_03b7: Expected O, but got Unknown
		//IL_0365: Unknown result type (might be due to invalid IL or missing references)
		//IL_036f: Expected O, but got Unknown
		//IL_01e1: Unknown result type (might be due to invalid IL or missing references)
		//IL_01da: Unknown result type (might be due to invalid IL or missing references)
		RichTextBlock val = new RichTextBlock();
		val.put_IsTextSelectionEnabled(false);
		Paragraph val2 = new Paragraph();
		((ICollection<Block>)val.Blocks).Add((Block)(object)val2);
		InlineCollection inlines = val2.Inlines;
		Run val3 = new Run();
		val3.put_Text(string.Empty);
		Run val4 = val3;
		((ICollection<Inline>)inlines).Add((Inline)(object)val4);
		if (body == null || entities == null)
		{
			return val;
		}
		bool flag = false;
		int num = 0;
		int num2 = 0;
		while (num2 < body.Length && num2 >= 0)
		{
			if (char.IsSurrogate(body[num2]))
			{
				Run obj = val4;
				obj.put_Text(obj.Text + body[num2]);
				Run obj2 = val4;
				obj2.put_Text(obj2.Text + body[num2 + 1]);
				num++;
				num2++;
				num2++;
				continue;
			}
			foreach (Entity entity in entities)
			{
				int num3 = entity.Range[0] + num;
				if (num3 != num2)
				{
					continue;
				}
				num2 = entity.Range[1] + num;
				if (num2 == num3)
				{
					num2++;
				}
				FrameworkElement val7;
				if (entity.EntityType == EntityType.user)
				{
					TextBlock val5 = new TextBlock();
					val5.put_Text((!string.IsNullOrEmpty(entity.Title)) ? entity.Title : entity.Text);
					TextBlock val6 = val5;
					val7 = (FrameworkElement)(object)val6;
					if (isInteraction)
					{
						val6.put_FontWeight(FontWeights.SemiBold);
					}
					else
					{
						val6.put_Foreground((Brush)((entityColor != null) ? ((object)entityColor) : ((object)(Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"])));
					}
					TextBlock val8 = val6;
					WindowsRuntimeMarshal.AddEventHandler((Func<TappedEventHandler, EventRegistrationToken>)((UIElement)val8).add_Tapped, (Action<EventRegistrationToken>)((UIElement)val8).remove_Tapped, new TappedEventHandler(tbUser_Tapped));
					((FrameworkElement)val6).put_Tag((object)entity);
					if (entity.Range[0] == entity.Range[1])
					{
						val6.put_FontWeight(boldFirst ? FontWeights.Bold : val6.FontWeight);
						((FrameworkElement)val6).put_Margin(isLargeFont ? new Thickness(0.0, 0.0, 5.5, 0.0) : new Thickness(0.0, 0.0, 5.0, 0.0));
					}
				}
				else
				{
					Button val9 = new Button();
					((FrameworkElement)val9).put_Style((Style)((IDictionary<object, object>)Application.Current.Resources)[(object)"TextHyperlinkButtonStyle"]);
					((FrameworkElement)val9).put_Margin(isLargeFont ? new Thickness(0.0, 0.0, 0.0, -2.4) : new Thickness(0.0, 0.0, 0.0, -2.0));
					((Control)val9).put_FontSize(CalculateFontSize(isLargeFont, fontSize));
					((FrameworkElement)val9).put_Tag((object)entity);
					((Control)val9).put_Foreground((Brush)((entityColor != null) ? ((object)entityColor) : ((object)(Brush)((IDictionary<object, object>)Application.Current.Resources)[(object)"VineGreenBrush"])));
					Button val10 = val9;
					val7 = (FrameworkElement)(object)val10;
					switch (entity.EntityType)
					{
					case EntityType.tag:
					{
						((ContentControl)val10).put_Content((object)("#" + entity.Title));
						Button val11 = val10;
						WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)((ButtonBase)val11).add_Click, (Action<EventRegistrationToken>)((ButtonBase)val11).remove_Click, new RoutedEventHandler(tbTag_Tapped));
						break;
					}
					case EntityType.mention:
					{
						((ContentControl)val10).put_Content((object)entity.Title);
						Button val11 = val10;
						WindowsRuntimeMarshal.AddEventHandler((Func<RoutedEventHandler, EventRegistrationToken>)((ButtonBase)val11).add_Click, (Action<EventRegistrationToken>)((ButtonBase)val11).remove_Click, new RoutedEventHandler(tbUser_Tapped));
						break;
					}
					}
				}
				InlineUIContainer val12 = new InlineUIContainer();
				val12.put_Child((UIElement)(object)val7);
				((ICollection<Inline>)inlines).Add((Inline)(object)val12);
				flag = true;
				break;
			}
			if (flag)
			{
				Run val13 = new Run();
				val13.put_Text(string.Empty);
				val4 = val13;
				((ICollection<Inline>)inlines).Add((Inline)(object)val4);
				flag = false;
			}
			else
			{
				string text = ((body[num2] == ' ') ? "\u200b " : (body[num2].ToString() ?? ""));
				Run obj3 = val4;
				obj3.put_Text(obj3.Text + text);
				num2++;
			}
		}
		return val;
	}

	private static double CalculateFontSize(bool isLargeFont, double? fontSize)
	{
		double? num = fontSize;
		return (double)(num.HasValue ? ((object)num.GetValueOrDefault()) : (isLargeFont ? ((IDictionary<object, object>)Application.Current.Resources)[(object)"TextStyleLargeFontSize"] : ((IDictionary<object, object>)Application.Current.Resources)[(object)"TextStyleMediumFontSize"]));
	}

	public static List<Run> GetFormattedStringRuns(string body, string parameter, FontWeight parameterFontWeight, Brush parameterForeground, Type parameterType)
	{
		//IL_0023: Unknown result type (might be due to invalid IL or missing references)
		//IL_002a: Expected O, but got Unknown
		//IL_0085: Unknown result type (might be due to invalid IL or missing references)
		List<Run> list = new List<Run>();
		string[] array = body.Split(' ');
		foreach (string text in array)
		{
			Run val = new Run();
			if (text != "{0}")
			{
				val.put_Text(text + " ");
			}
			else
			{
				if ((object)parameterType == typeof(long))
				{
					long count = Convert.ToInt64(parameter);
					val.put_Text(count.ToVineCount() + " ");
				}
				else
				{
					val.put_Text(parameter);
				}
				((TextElement)val).put_FontWeight(parameterFontWeight);
				if (parameterForeground != null)
				{
					((TextElement)val).put_Foreground(parameterForeground);
				}
			}
			list.Add(val);
		}
		return list;
	}

	private static void tbTag_Tapped(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		Entity entity = (Entity)((FrameworkElement)sender).Tag;
		App.RootFrame.Navigate(typeof(TagVineListView), (object)entity.Title);
	}

	private static void tbUser_Tapped(object sender, RoutedEventArgs e)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		Entity entity = (Entity)((FrameworkElement)sender).Tag;
		if (entity != null)
		{
			App.RootFrame.Navigate(typeof(ProfileView), (object)entity.Id);
		}
	}

	public static List<Entity> BuildFromList(string text, List<VineUserModel> users)
	{
		List<Entity> list = new List<Entity>();
		List<VineUserModel> list2 = users.Where((VineUserModel x) => text.Contains(x.Username)).ToList();
		int startIndex = 0;
		foreach (VineUserModel item2 in list2)
		{
			int num = text.IndexOf(item2.Username, startIndex);
			int num2 = num + item2.Username.Length;
			startIndex = num2;
			Entity entity = new Entity();
			entity.Id = item2.UserId;
			entity.Text = item2.Username;
			entity.Type = EntityType.mention.ToString();
			entity.Range = new int[2] { num, num2 };
			Entity item = entity;
			list.Add(item);
		}
		return list;
	}

	public static List<Entity> BuildFromList(string text, List<Entity> entityList)
	{
		List<Entity> list = new List<Entity>();
		List<Entity> list2 = (from x in entityList
			where text.Contains(x.Text)
			orderby x.Range[0]
			select x).ToList();
		int startIndex = 0;
		foreach (Entity item2 in list2)
		{
			int num = text.IndexOf(item2.Text, startIndex);
			int num2 = num + item2.Text.Length;
			startIndex = num2;
			Entity entity = new Entity();
			entity.Id = item2.Id;
			entity.Text = item2.Text;
			entity.Type = item2.Type;
			entity.Range = new int[2] { num, num2 };
			Entity item = entity;
			list.Add(item);
		}
		return list;
	}
}
