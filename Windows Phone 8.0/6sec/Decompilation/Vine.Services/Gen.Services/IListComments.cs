using System.Collections.Generic;

namespace Gen.Services;

public class IListComments
{
	public List<IComment> Comments { get; set; }

	public string NextPage { get; set; }
}
