namespace Vine.Models;

public class BaseVineResponseModel
{
	public string Code { get; set; }

	public string Error { get; set; }

	public bool Success { get; set; }
}
public class BaseVineResponseModel<T> : BaseVineResponseModel
{
	public T Data { get; set; }
}
