using Vine.Models;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace Vine.Views.TemplateSelectors;

public class VineListTemplateSelector : DataTemplateSelector
{
	public DataTemplate PostTemplate { get; set; }

	public DataTemplate PostMosaicTemplate { get; set; }

	public DataTemplate AvatarPostMosaicTemplate { get; set; }

	public DataTemplate UrlActionTemplate { get; set; }

	public DataTemplate UserMosaicTemplate { get; set; }

	protected override DataTemplate SelectTemplateCore(object item, DependencyObject container)
	{
		VineModel model = ((VineViewModel)item).Model;
		if (model.RecordType == RecordType.Post)
		{
			return PostTemplate;
		}
		if (model.RecordType == RecordType.PostMosaic)
		{
			if (model.ParsedMosaicType == MosaicType.AvatarIncluded)
			{
				return AvatarPostMosaicTemplate;
			}
			return PostMosaicTemplate;
		}
		if (model.RecordType == RecordType.UrlAction)
		{
			return UrlActionTemplate;
		}
		if (model.RecordType == RecordType.UserMosaic)
		{
			return UserMosaicTemplate;
		}
		return PostTemplate;
	}
}
