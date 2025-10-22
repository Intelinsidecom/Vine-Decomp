namespace Vine.Models;

public class VineAuthTokenResult
{
	public VineAuthToken Data { get; set; }

	public string Code { get; set; }

	public bool Success { get; set; }

	public string Error { get; set; }
}
