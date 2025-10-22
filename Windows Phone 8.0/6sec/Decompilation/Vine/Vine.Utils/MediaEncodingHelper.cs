using Windows.Media.MediaProperties;

namespace Vine.Utils;

public class MediaEncodingHelper
{
	public static MediaEncodingProfile GetEncoding()
	{
		MediaEncodingProfile val = MediaEncodingProfile.CreateMp4((VideoEncodingQuality)6);
		VideoEncodingProperties video = val.Video;
		uint num;
		val.Video.put_Width(num = 480u);
		video.put_Height(num);
		val.Video.put_Bitrate(700000u);
		val.Video.FrameRate.put_Numerator(30000u);
		val.Video.FrameRate.put_Denominator(1001u);
		return val;
	}
}
