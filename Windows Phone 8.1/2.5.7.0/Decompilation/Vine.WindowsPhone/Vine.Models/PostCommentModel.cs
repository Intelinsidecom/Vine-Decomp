using System.Collections.Generic;
using Newtonsoft.Json;

namespace Vine.Models;

public class PostCommentModel
{
	[JsonProperty("comment")]
	public string Comment { get; set; }

	[JsonProperty("entities")]
	public List<Entity> Entities { get; set; }

	public PostCommentModel()
	{
		Entities = new List<Entity>();
	}
}
