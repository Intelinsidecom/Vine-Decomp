using System;

namespace Vine.Framework;

public class SimpleStopWatch
{
	private long _startTime;

	private long _endTime;

	public long ElapsedTicks
	{
		get
		{
			if (!IsRunning)
			{
				return _endTime - _startTime;
			}
			return Now - _startTime;
		}
	}

	public bool IsRunning { get; private set; }

	public long Now => DateTime.Now.Ticks;

	public void Start()
	{
		IsRunning = true;
		_startTime = Now;
	}

	public void Stop()
	{
		IsRunning = false;
		_endTime = Now;
	}
}
