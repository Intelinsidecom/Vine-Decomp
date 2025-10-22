namespace Vine.Test;

public class MemoryCheckpoint
{
	public string Text { get; private set; }

	public long MemoryUsage { get; private set; }

	internal MemoryCheckpoint(string text, long memoryUsage)
	{
		Text = text;
		MemoryUsage = memoryUsage;
	}
}
