using Vine.Models;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace Vine.Views.TemplateSelectors;

public class TaggingTemplateSelector : DataTemplateSelector
{
	public DataTemplate MentionTemplate { get; set; }

	public DataTemplate HashtagTemplate { get; set; }

	protected override DataTemplate SelectTemplateCore(object item, DependencyObject container)
	{
		Entity entity = (Entity)item;
		if (entity != null && entity.EntityType == EntityType.mention)
		{
			return MentionTemplate;
		}
		return HashtagTemplate;
	}
}
