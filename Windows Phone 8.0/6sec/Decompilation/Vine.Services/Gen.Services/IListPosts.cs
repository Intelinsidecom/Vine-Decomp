using System.Collections.Generic;
using Vine.Services.Models;

namespace Gen.Services;

public class IListPosts
{
	public List<IPostRecord> Posts { get; set; }

	public string NextPage { get; set; }

	public bool Followed { get; set; }
}
