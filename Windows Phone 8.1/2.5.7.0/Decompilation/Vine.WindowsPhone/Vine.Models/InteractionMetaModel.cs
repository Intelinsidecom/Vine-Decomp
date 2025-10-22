using System.Collections.Generic;
using Vine.Framework;

namespace Vine.Models;

public class InteractionMetaModel : IncrementalLoadingBaseModel
{
	public List<InteractionModel> Records { get; set; }
}
