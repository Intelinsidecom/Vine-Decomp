namespace Vine.Web;

public class EmptyApiResult : ApiResult
{
	public override bool HasError => false;

	public override string GetErrorMessage(string defaultMessage)
	{
		return string.Empty;
	}
}
