using System.ComponentModel;
using Vine.Datas;

namespace Vine.Pages.ManageAccount.ViewModels;

public class ManageUser : INotifyPropertyChanged
{
	private static Vine.Datas.Datas _data;

	public DataUser User { get; set; }

	public bool IsPrimary => User == _data.PrimaryUser;

	public bool IsCurrent => User == _data.CurrentUser;

	public event PropertyChangedEventHandler PropertyChanged;

	static ManageUser()
	{
		_data = DatasProvider.Instance;
	}

	internal void UpdatePrimaryAccount()
	{
		RaisePropertyChanged("IsPrimary");
	}

	public void RaisePropertyChanged(string name)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(name));
		}
	}
}
