using Vine.Models;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace Vine.Views.TemplateSelectors;

public class ContactTemplateSelector : DataTemplateSelector
{
	public DataTemplate VineUserTemplate { get; set; }

	public DataTemplate ContactTemplate { get; set; }

	public DataTemplate HeaderTemplate { get; set; }

	protected override DataTemplate SelectTemplateCore(object item, DependencyObject container)
	{
		VineContactViewModel vineContactViewModel = (VineContactViewModel)item;
		if (vineContactViewModel.User == null)
		{
			return HeaderTemplate;
		}
		if (vineContactViewModel.User.UserType == VineUserType.User)
		{
			return VineUserTemplate;
		}
		return ContactTemplate;
	}
}
