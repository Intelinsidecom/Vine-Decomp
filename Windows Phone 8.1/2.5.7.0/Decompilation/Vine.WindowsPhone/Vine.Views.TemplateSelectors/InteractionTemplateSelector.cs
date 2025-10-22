using Vine.Models;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace Vine.Views.TemplateSelectors;

public class InteractionTemplateSelector : DataTemplateSelector
{
	public DataTemplate FollowRequestTemplate { get; set; }

	public DataTemplate NotificationTemplate { get; set; }

	public DataTemplate MilestoneTemplate { get; set; }

	public DataTemplate GroupedCountTemplate { get; set; }

	public DataTemplate HeaderTemplate { get; set; }

	protected override DataTemplate SelectTemplateCore(object item, DependencyObject container)
	{
		InteractionModel interactionModel = (InteractionModel)item;
		if (interactionModel.NotificationTypeId == InteractionType.FollowRequest)
		{
			return FollowRequestTemplate;
		}
		if (interactionModel.Milestone != null)
		{
			return MilestoneTemplate;
		}
		if (interactionModel.InteractionType == InteractionType.Count)
		{
			return GroupedCountTemplate;
		}
		if (interactionModel.InteractionType == InteractionType.Header)
		{
			return HeaderTemplate;
		}
		return NotificationTemplate;
	}
}
