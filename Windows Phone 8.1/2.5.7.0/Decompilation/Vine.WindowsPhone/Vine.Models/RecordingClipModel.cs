using System;
using Windows.Media.Editing;

namespace Vine.Models;

public class RecordingClipModel
{
	public const int MagicStartOffset = 2;

	public string VideoFilePath { get; set; }

	public string GhostFilePath { get; set; }

	public long VideoFileDuration { get; set; }

	public long FrameRate { get; set; }

	public long FileStartTime { get; set; }

	public long FileEndTime { get; set; }

	public long EditStartTime { get; set; }

	public long EditEndTime { get; set; }

	public void ToMediaClip(MediaClip clip)
	{
		clip.put_TrimTimeFromStart(TimeSpan.FromTicks(FileStartTime + EditStartTime));
		clip.put_TrimTimeFromEnd(TimeSpan.FromTicks(VideoFileDuration - (FileStartTime + EditEndTime)));
	}
}
