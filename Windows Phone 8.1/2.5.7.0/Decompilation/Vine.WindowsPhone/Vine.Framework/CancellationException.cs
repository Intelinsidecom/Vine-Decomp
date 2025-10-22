using System;

namespace Vine.Framework;

public class CancellationException : Exception
{
	public CancellationException()
	{
	}

	public CancellationException(string message)
		: base(message)
	{
	}
}
