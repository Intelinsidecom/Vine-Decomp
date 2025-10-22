using System.Collections.Generic;

namespace Gen.Services;

public class IListPersons
{
	public List<IPerson> Persons { get; set; }

	public string NextPage { get; set; }
}
