using System.Collections.Generic;
using Vine.Services.Models;

namespace Vine.Services.Response;

public class SuggestContactResponse : VineJsonResponse
{
	public List<VineRecordPerson> data { get; set; }
}
