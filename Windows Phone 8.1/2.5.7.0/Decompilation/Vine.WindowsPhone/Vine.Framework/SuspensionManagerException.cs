using System;

namespace Vine.Framework;

public class SuspensionManagerException : Exception
{
	public SuspensionManagerException()
	{
	}

	public SuspensionManagerException(Exception e)
		: base("SuspensionManager failed", e)
	{
	}
}
