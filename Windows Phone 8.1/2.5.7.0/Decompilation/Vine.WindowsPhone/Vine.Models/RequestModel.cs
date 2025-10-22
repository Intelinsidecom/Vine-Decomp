using System.Threading.Tasks;
using Vine.Web;

namespace Vine.Models;

public class RequestModel : NotifyObject
{
	public string Path { get; set; }

	public ParameterCollection Parameters { get; set; }

	public WebDataProvider.WebPlatform Platform { get; set; }

	public RequestType Type { get; set; }

	public Task Task { get; set; }

	public string PayloadJson { get; set; }

	public RequestType RequestType { get; set; }

	public RequestModel(string path, ParameterCollection parameters, WebDataProvider.WebPlatform platform, RequestType type, Task task, string payloadJson = null)
	{
		Path = path;
		Parameters = parameters;
		Platform = platform;
		Type = type;
		Task = task;
		PayloadJson = payloadJson;
	}
}
