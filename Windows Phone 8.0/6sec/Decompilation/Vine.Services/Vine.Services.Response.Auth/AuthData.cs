using System;
using System.ComponentModel;
using System.Windows;
using Gen.Services;
using Newtonsoft.Json;

namespace Vine.Services.Response.Auth;

public class AuthData : IAccount, INotifyPropertyChanged
{
	public string Name { get; set; }

	public string Id { get; set; }

	public string username
	{
		get
		{
			return Name;
		}
		set
		{
			Name = value;
		}
	}

	public string userId
	{
		get
		{
			return Id;
		}
		set
		{
			Id = value;
		}
	}

	public string Picture { get; set; }

	[JsonProperty(PropertyName = "edition")]
	public string Edition { get; set; }

	public string key { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public void ChangeName(string username)
	{
		Name = username;
	}

	public void RaisePropertyChanged(string name)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(name));
		}
	}

	public void ChangePicture(string p)
	{
		Picture = p;
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			RaisePropertyChanged("Picture");
		});
	}
}
