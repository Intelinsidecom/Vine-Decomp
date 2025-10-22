using System.Collections.Generic;
using Newtonsoft.Json;

namespace Vine.Models;

public class UploadLoopsModel
{
	[JsonProperty("loops")]
	public List<LoopCountsModel> loops { get; set; }
}
