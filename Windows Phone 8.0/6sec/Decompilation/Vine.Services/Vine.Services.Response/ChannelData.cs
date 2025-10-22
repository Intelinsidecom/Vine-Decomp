using System.Collections.Generic;

namespace Vine.Services.Response;

public class ChannelData
{
	public int count { get; set; }

	public List<RecordChannel> records { get; set; }

	public object nextPage { get; set; }

	public string anchor { get; set; }

	public object previousPage { get; set; }

	public int size { get; set; }
}
