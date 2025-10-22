using System;

namespace Vine.Models;

public class MessageException : Exception
{
	public int Code { get; set; }

	public string ResponseMessage { get; set; }

	public MessageException(int code, string message)
	{
		Code = code;
		ResponseMessage = message;
	}
}
