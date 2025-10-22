using System;
using System.Diagnostics;

namespace Vine.Background.Common;

internal class Logger
{
	[Conditional("DEBUG")]
	public static extern void Log(Exception e);

	[Conditional("DEBUG")]
	public static extern void Log(string message);

	public extern Logger();
}
