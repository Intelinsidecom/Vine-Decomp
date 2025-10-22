using Vine.Models;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace Vine.Views.TemplateSelectors;

public class FriendFinderTemplateSelector : DataTemplateSelector
{
	public DataTemplate UserTemplate { get; set; }

	public DataTemplate HeaderTemplate { get; set; }

	protected override DataTemplate SelectTemplateCore(object item, DependencyObject container)
	{
		if ((item as FriendFinderModel).IsUser)
		{
			return UserTemplate;
		}
		return HeaderTemplate;
	}
}
