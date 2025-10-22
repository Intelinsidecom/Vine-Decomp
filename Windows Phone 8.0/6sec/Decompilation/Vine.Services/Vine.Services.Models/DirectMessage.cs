using System;
using System.ComponentModel;
using System.Runtime.Serialization;
using Newtonsoft.Json;
using Vine.Datas;

namespace Vine.Services.Models;

public class DirectMessage : INotifyPropertyChanged
{
	private bool? _isme;

	[JsonProperty("created")]
	public DateTime Created { get; set; }

	[JsonProperty("userId")]
	public string UserId { get; set; }

	public bool IsMe
	{
		get
		{
			if (!_isme.HasValue)
			{
				_isme = UserId == DatasProvider.Instance.CurrentUserId;
			}
			return _isme.Value;
		}
	}

	[JsonProperty("conversationId")]
	public string ConversationId { get; set; }

	[JsonProperty("messageId")]
	public string MessageId { get; set; }

	[JsonProperty("message")]
	public string Message { get; set; }

	[JsonProperty("videoUrl")]
	public string VideoUrl { get; set; }

	[JsonProperty("thumbnailUrl")]
	public string ThumbnailUrl { get; set; }

	[IgnoreDataMember]
	public User User { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	internal void UpdateCounter()
	{
		Created = new DateTime(Created.Ticks);
		RaisePropertyChanged("Created");
	}

	public void RaisePropertyChanged(string name)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(name));
		}
	}
}
