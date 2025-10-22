namespace Vine.Web;

public class ErrorApiResult : ApiResult
{
	public override bool HasError => true;

	public override string GetErrorMessage(string defaultMessage)
	{
		return defaultMessage;
	}
}
