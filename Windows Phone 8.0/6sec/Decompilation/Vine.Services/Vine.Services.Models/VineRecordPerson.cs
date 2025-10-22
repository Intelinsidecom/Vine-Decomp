using System.ComponentModel;
using Gen.Services;
using Newtonsoft.Json;

namespace Vine.Services.Models;

public class VineRecordPerson : INotifyPropertyChanged, IPerson
{
	[JsonProperty(PropertyName = "username")]
	public string Name { get; set; }

	[JsonProperty(PropertyName = "avatarUrl")]
	public string Picture { get; set; }

	public string SubName => Location;

	[JsonProperty(PropertyName = "following")]
	public bool? Following { get; set; }

	[JsonProperty(PropertyName = "blocked")]
	public string Blocked { get; set; }

	[JsonProperty(PropertyName = "userId")]
	public string Id { get; set; }

	[JsonProperty(PropertyName = "location")]
	public string Location { get; set; }

	public object likeId { get; set; }

	[JsonProperty(PropertyName = "user")]
	public User User { get; set; }

	public bool IsVerified { get; set; }

	public string created { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public void ChangeFollow(bool val)
	{
		if (Following != val)
		{
			Following = val;
			if (this.PropertyChanged != null)
			{
				this.PropertyChanged(this, new PropertyChangedEventArgs("Following"));
			}
		}
	}
}
