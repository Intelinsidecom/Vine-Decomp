using System.Collections.Generic;

namespace Vine.Models;

public class TwitterErrorResponseModel
{
	public List<TwitterErrorModel> Errors { get; set; }
}
