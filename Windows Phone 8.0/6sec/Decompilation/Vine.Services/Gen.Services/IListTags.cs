using System.Collections.Generic;

namespace Gen.Services;

public class IListTags
{
	public List<ITag> Tags { get; set; }

	public string NextPage { get; set; }
}
