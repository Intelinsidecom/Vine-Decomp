using Vine.Models;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace Vine.Views.TemplateSelectors;

public class SearchResultTemplateSelector : DataTemplateSelector
{
	public DataTemplate HeaderTemplate { get; set; }

	public DataTemplate SuggestedSearchTermTemplate { get; set; }

	public DataTemplate UserTemplate { get; set; }

	public DataTemplate TagTemplate { get; set; }

	public DataTemplate VineTemplate { get; set; }

	public DataTemplate RecentTemplate { get; set; }

	protected override DataTemplate SelectTemplateCore(object item, DependencyObject container)
	{
		if (!(item is SearchResultModel searchResultModel))
		{
			return null;
		}
		if (searchResultModel.IsSearchSuggestion)
		{
			return SuggestedSearchTermTemplate;
		}
		if (searchResultModel.IsUser)
		{
			return UserTemplate;
		}
		if (searchResultModel.IsTag)
		{
			return TagTemplate;
		}
		if (searchResultModel.IsVine)
		{
			return VineTemplate;
		}
		if (searchResultModel.IsRecent)
		{
			return RecentTemplate;
		}
		return HeaderTemplate;
	}
}
