using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Runtime.Serialization;
using Gen.Services;
using Vine.Services;
using Vine.Services.Models;
using Vine.Services.Response.Auth;

namespace Vine.Datas;

public class DataUser : IDataUser, INotifyPropertyChanged
{
	public VineServiceWithAuth Service { get; set; }

	public bool SaveImageWhenPublish { get; set; }

	public List<string> Tags { get; set; }

	public string Password { get; set; }

	public AuthData User { get; set; }

	public TwitterAccess TwitterAccess { get; set; }

	public VKAccess VKAccess { get; set; }

	public FoursquareAccess FoursquareAccess { get; set; }

	public FlickrAccess FlickrAccess { get; set; }

	public FacebookAccess FacebookAccess { get; set; }

	public TumblrAccess TumblrAccess { get; set; }

	public string Email { get; set; }

	public string Name
	{
		get
		{
			if (User == null)
			{
				return Email;
			}
			return User.Name;
		}
	}

	public string Id
	{
		get
		{
			if (User == null)
			{
				return null;
			}
			return User.Id;
		}
	}

	[IgnoreDataMember]
	public Conversation CurrentConversation { get; set; }

	public DateTime LastLogin { get; internal set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public DataUser()
	{
		SaveImageWhenPublish = true;
		Tags = new List<string>();
		Service = new VineServiceWithAuth();
	}

	public void Init()
	{
		Service.Init(this);
	}

	public void Update(VineProfile userProfile)
	{
		if (User != null && userProfile != null && User.Id == userProfile.Id)
		{
			User.Edition = userProfile.Edition;
		}
	}

	public void RaisePropertyChanged(string name)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(name));
		}
	}

	internal void Update(AuthData newauth)
	{
		if (string.IsNullOrEmpty(newauth.Picture) && User != null)
		{
			newauth.Picture = User.Picture;
		}
		User = newauth;
	}
}
