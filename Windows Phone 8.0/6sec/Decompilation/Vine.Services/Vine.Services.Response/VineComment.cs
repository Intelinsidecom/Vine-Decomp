using System;
using System.Globalization;
using Gen.Services;
using Newtonsoft.Json;

namespace Vine.Services.Response;

public class VineComment : IComment
{
	private static CultureInfo culture = new CultureInfo("en-US");

	public string Id { get; set; }

	[JsonProperty(PropertyName = "comment")]
	public string Comment { get; set; }

	[JsonProperty(PropertyName = "username")]
	public string Username { get; set; }

	[JsonProperty(PropertyName = "created")]
	public string createdstring { get; set; }

	public DateTime Created
	{
		get
		{
			DateTime result = DateTime.MinValue;
			DateTime.TryParse(createdstring, culture, DateTimeStyles.AssumeUniversal, out result);
			return result;
		}
	}

	public bool IsRemovable { get; set; }

	public string location { get; set; }

	[JsonProperty(PropertyName = "userId")]
	public string UserId { get; set; }

	[JsonProperty(PropertyName = "avatarUrl")]
	public string Avatar { get; set; }
}
