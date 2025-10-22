using System.Collections.Generic;
using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class ShareDetails
{
	[JsonProperty("share_targets")]
	public List<ShareTarget> ShareTargets { get; set; }

	[JsonProperty("has_comment")]
	public bool HasComment { get; set; }

	[JsonProperty("post_id")]
	public string PostId { get; set; }

	[JsonProperty("message_recipients")]
	public List<VMRecipient> MessageRecipients { get; set; }
}
