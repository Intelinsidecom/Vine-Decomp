using System.ComponentModel;

namespace Vine.Models;

public class UserControlWrapper : NotifyObject
{
	private INotifyPropertyChanged _model;

	public INotifyPropertyChanged Model
	{
		get
		{
			return _model;
		}
		set
		{
			SetProperty(ref _model, value, "Model");
		}
	}
}
