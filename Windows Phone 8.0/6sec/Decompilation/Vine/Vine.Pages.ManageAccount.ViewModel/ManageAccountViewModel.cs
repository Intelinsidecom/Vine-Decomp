using System.Collections.ObjectModel;
using Vine.Datas;
using Vine.Pages.ManageAccount.ViewModels;

namespace Vine.Pages.ManageAccount.ViewModel;

public class ManageAccountViewModel
{
	private Vine.Datas.Datas Data;

	public ObservableCollection<ManageUser> Users { get; set; }

	public ManageAccountViewModel()
	{
		Data = DatasProvider.Instance;
		Users = new ObservableCollection<ManageUser>();
		foreach (DataUser user in Data.Users)
		{
			Users.Add(new ManageUser
			{
				User = user
			});
		}
	}

	public void UpdatePrimaryAccount()
	{
		foreach (ManageUser user in Users)
		{
			user.UpdatePrimaryAccount();
		}
	}

	internal void RemoveUser(ManageUser user)
	{
		Vine.Datas.Datas instance = DatasProvider.Instance;
		bool isPrimary = user.IsPrimary;
		Users.Remove(user);
		instance.RemoveUser(user.User);
		if (isPrimary)
		{
			UpdatePrimaryAccount();
		}
		if (instance.Users.Count == 0)
		{
			NavigationServiceExt.ToLogin(removebackentry: true);
		}
	}

	internal void SetPrimaryUser(ManageUser user)
	{
		Data.ChangePrimaryUser(user.User.User.Id);
		UpdatePrimaryAccount();
	}
}
