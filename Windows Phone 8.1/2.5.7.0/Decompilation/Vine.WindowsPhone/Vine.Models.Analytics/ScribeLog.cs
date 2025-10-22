using System.Collections.Generic;
using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class ScribeLog
{
	[JsonProperty("events")]
	public List<ClientEvent> Events { get; set; }
}
