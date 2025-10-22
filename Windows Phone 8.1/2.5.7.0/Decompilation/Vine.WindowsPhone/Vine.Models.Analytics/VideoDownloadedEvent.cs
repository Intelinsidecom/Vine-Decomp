using Vine.Framework;
using Vine.Web;
using Windows.Web.Http;

namespace Vine.Models.Analytics;

public class VideoDownloadedEvent : ClientEvent
{
	public VideoDownloadedEvent(ApiResult apiResult, Section section, string view)
		: base("video_downloaded")
	{
		//IL_0051: Unknown result type (might be due to invalid IL or missing references)
		base.Navigation = new AppNavigation
		{
			Section = section,
			View = view
		};
		base.EventDetails = new EventDetails
		{
			TimeStamp = TimeStampHelper.GetSecondsSinceUnixEpoch()
		};
		HttpResponseMessage httpResponse = apiResult.HttpResponse;
		if (httpResponse != null)
		{
			base.EventDetails.HttpRequestDetails = new HttpRequestDetails
			{
				HttpStatus = (long)httpResponse.StatusCode,
				Method = ((httpResponse.RequestMessage != null && httpResponse.RequestMessage.Method != null) ? httpResponse.RequestMessage.Method.Method : null),
				Url = ((httpResponse.RequestMessage != null && httpResponse.RequestMessage.RequestUri != null) ? httpResponse.RequestMessage.RequestUri.ToString() : null)
			};
		}
	}
}
