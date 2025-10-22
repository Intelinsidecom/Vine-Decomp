using System;
using System.Collections.Generic;

namespace Vine.Models;

public class VineMessageModel
{
	public MessageError Error { get; set; }

	public DateTime Created { get; set; }

	public string UserId { get; set; }

	public string ConversationId { get; set; }

	public List<Entity> Entities { get; set; }

	public string MessageId { get; set; }

	public string Message { get; set; }

	public string ThumbnailUrl { get; set; }

	public string VideoUrl { get; set; }

	public string ShareUrl { get; set; }

	public VineModel Post { get; set; }

	public UploadJob UploadJob { get; set; }
}
