using System.ComponentModel;
using System.Xml.Serialization;

namespace Vine;

public class FacebookAccess : INotifyPropertyChanged
{
	[XmlAttribute("at")]
	public string AccessToken { get; set; }

	[XmlAttribute("ui")]
	public string UserId { get; set; }

	public string PageName { get; set; }

	public string CurrentAccessToken
	{
		get
		{
			if (PageAccessToken != null)
			{
				return PageAccessToken;
			}
			return AccessToken;
		}
	}

	public string PageAccessToken { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public FacebookAccess()
	{
		PageName = "Timeline";
	}

	public void SetPage(string name, string accessToken)
	{
		PageName = name;
		RaisePropertyChanged("PageName");
		PageAccessToken = accessToken;
	}

	public void RaisePropertyChanged(string name)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(name));
		}
	}
}
