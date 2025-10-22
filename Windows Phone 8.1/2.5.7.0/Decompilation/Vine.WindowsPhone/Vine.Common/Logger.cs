using System;
using System.Diagnostics;

namespace Vine.Common;

public class Logger
{
	[Conditional("DEBUG")]
	public static void Log(Exception e)
	{
	}

	[Conditional("DEBUG")]
	public static void Log(string message)
	{
	}
}
