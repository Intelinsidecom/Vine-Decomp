using System;
using System.Runtime.Serialization;
using Newtonsoft.Json;

namespace Vine.Models;

public class Entity
{
	[JsonProperty("type")]
	public string Type { get; set; }

	[IgnoreDataMember]
	public EntityType EntityType
	{
		get
		{
			if (!Enum.TryParse<EntityType>(Type, ignoreCase: true, out var result))
			{
				return EntityType.Unknown;
			}
			return result;
		}
	}

	[JsonProperty("id")]
	public string Id { get; set; }

	[JsonProperty("title")]
	public string Title { get; set; }

	[JsonProperty("link")]
	public string Link { get; set; }

	[JsonProperty("range")]
	public int[] Range { get; set; }

	[JsonProperty("text")]
	public string Text { get; set; }

	[IgnoreDataMember]
	public VineUserModel User { get; set; }
}
