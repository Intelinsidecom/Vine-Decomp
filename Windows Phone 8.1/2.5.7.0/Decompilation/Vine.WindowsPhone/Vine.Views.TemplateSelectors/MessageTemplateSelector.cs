using Vine.Models;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace Vine.Views.TemplateSelectors;

public class MessageTemplateSelector : DataTemplateSelector
{
	public DataTemplate CurrentUserMessageVideoTemplate { get; set; }

	public DataTemplate CurrentUserMessageTextTemplate { get; set; }

	public DataTemplate CurrentUserMessagePostTemplate { get; set; }

	public DataTemplate VideoUploadJobTemplate { get; set; }

	public DataTemplate OtherUserMessageVideoTemplate { get; set; }

	public DataTemplate OtherUserMessageTextTemplate { get; set; }

	public DataTemplate OtherUserMessagePostTemplate { get; set; }

	protected override DataTemplate SelectTemplateCore(object item, DependencyObject container)
	{
		//IL_0098: Unknown result type (might be due to invalid IL or missing references)
		//IL_009e: Expected O, but got Unknown
		VineMessageViewModel vineMessageViewModel = (VineMessageViewModel)item;
		if (vineMessageViewModel.Model.UploadJob != null)
		{
			return VideoUploadJobTemplate;
		}
		if (vineMessageViewModel.Model.VideoUrl != null)
		{
			if (!vineMessageViewModel.User.IsCurrentUser)
			{
				return OtherUserMessageVideoTemplate;
			}
			return CurrentUserMessageVideoTemplate;
		}
		if (vineMessageViewModel.Model.Post != null)
		{
			if (!vineMessageViewModel.User.IsCurrentUser)
			{
				return OtherUserMessagePostTemplate;
			}
			return CurrentUserMessagePostTemplate;
		}
		if (!string.IsNullOrEmpty(vineMessageViewModel.Model.Message))
		{
			if (!vineMessageViewModel.User.IsCurrentUser)
			{
				return OtherUserMessageTextTemplate;
			}
			return CurrentUserMessageTextTemplate;
		}
		return new DataTemplate();
	}
}
