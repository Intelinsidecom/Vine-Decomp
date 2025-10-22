using System.ComponentModel;
using System.Threading.Tasks;
using System.Windows.Controls;
using Gen.Services;
using Vine.Datas;
using Vine.Services;
using Vine.Services.Models;

namespace Vine.Pages.Settings.ViewModels;

public class SettingsViewModel : INotifyPropertyChanged
{
	public bool IsProfileLoading { get; set; }

	public IProfile Profile { get; set; }

	public Vine.Datas.Datas Data { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public SettingsViewModel()
	{
		Data = DatasProvider.Instance;
	}

	public void RaisePropertyChanged(string name)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(name));
		}
	}

	internal async Task LoadProfile()
	{
		if (Profile != null)
		{
			return;
		}
		IsProfileLoading = true;
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		try
		{
			IProfile profile = await currentUser.Service.GetMyProfilForSettingsAsync();
			IsProfileLoading = false;
			Profile = profile;
			Data.CurrentUser.Update((VineProfile)profile);
			RaisePropertyChanged("Profile");
			RaisePropertyChanged("IsProfileLoading");
		}
		catch (ServiceServerErrorException ex)
		{
			IsProfileLoading = false;
			ToastHelper.Show(ex.HttpErrorMessage, afternav: false, (Orientation)0);
			RaisePropertyChanged("IsProfileLoading");
		}
	}
}
