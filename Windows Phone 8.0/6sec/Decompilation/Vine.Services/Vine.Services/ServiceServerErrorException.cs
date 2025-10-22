using System;

namespace Vine.Services;

public class ServiceServerErrorException : Exception
{
	public string HttpErrorMessage { get; set; }

	public string ErrorType { get; set; }

	public int StatusCode { get; set; }

	public ServiceServerErrorType ReasonError { get; set; }

	public string Checkpoint { get; set; }

	public ServiceServerErrorException(ServiceServerErrorType reasonError)
	{
		ReasonError = reasonError;
	}
}
