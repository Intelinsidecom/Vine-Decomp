using Vine.Models;

namespace Vine.Views;

public class ShareViewParameters
{
	public UploadJob Job { get; set; }

	public string PostId { get; set; }

	public bool IsSingleSelect { get; set; }
}
