using System.Collections.Generic;

namespace Vine.Services.Response;

public class Entity
{
	public string type { get; set; }

	public List<int> range { get; set; }

	public string link { get; set; }

	public string id { get; set; }

	public string title { get; set; }
}
