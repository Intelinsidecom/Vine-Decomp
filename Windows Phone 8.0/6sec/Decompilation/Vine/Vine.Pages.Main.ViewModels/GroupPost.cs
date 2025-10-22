using System.Collections.ObjectModel;
using Vine.Services.Models;

namespace Vine.Pages.Main.ViewModels;

public class GroupPost
{
	public ObservableCollection<IPostRecord> Posts { get; set; }

	public GroupPost()
	{
		Posts = new ObservableCollection<IPostRecord>();
	}
}
