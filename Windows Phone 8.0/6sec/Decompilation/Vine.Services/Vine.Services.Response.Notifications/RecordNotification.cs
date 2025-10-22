using Gen.Services;
using Newtonsoft.Json;

namespace Vine.Services.Response.Notifications;

public class RecordNotification : INotification
{
	[JsonProperty(PropertyName = "body")]
	public string Body { get; set; }

	[JsonProperty(PropertyName = "avatarUrl")]
	public string Avatar { get; set; }

	[JsonProperty(PropertyName = "thumbnailUrl")]
	public string Picture { get; set; }

	[JsonProperty(PropertyName = "created")]
	public string Created { get; set; }

	[JsonProperty(PropertyName = "userId")]
	public string UserId { get; set; }

	[JsonProperty(PropertyName = "postId")]
	public string PostId { get; set; }
}
