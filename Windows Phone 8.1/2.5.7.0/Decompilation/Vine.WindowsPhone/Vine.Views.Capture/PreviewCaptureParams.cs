using Vine.Models;

namespace Vine.Views.Capture;

public class PreviewCaptureParams
{
	public ReplyVmParameters VMParameters { get; set; }

	public bool IsMessageTabDefault { get; set; }

	public string FullCaptureVideoFile { get; set; }
}
