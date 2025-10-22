namespace Vine.Services.Response;

public class PostCommentRootObject : VineJsonResponse
{
	public CreateCommentData data { get; set; }
}
