using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Vine.Framework;

public class TaskManager
{
	private static TaskManager _cleanupTasks;

	private readonly List<Func<Task>> _tasks = new List<Func<Task>>();

	private object _locker = new object();

	private Task _executeQueueTask;

	public static TaskManager CleanUpTasks => _cleanupTasks ?? (_cleanupTasks = new TaskManager());

	public async Task RunAtEndOfQueue(Func<Task> action)
	{
		lock (_locker)
		{
			_tasks.Add(action);
			if (_executeQueueTask == null)
			{
				_executeQueueTask = ExecuteQueue();
			}
		}
		await WaitForEmptyQueue();
	}

	public async Task WaitForEmptyQueue()
	{
		if (_executeQueueTask != null)
		{
			await _executeQueueTask;
			_executeQueueTask = null;
		}
	}

	private async Task ExecuteQueue(bool shouldLock = false)
	{
		List<Func<Task>> list;
		if (shouldLock)
		{
			lock (_locker)
			{
				list = _tasks.ToList();
				_tasks.Clear();
			}
		}
		else
		{
			list = _tasks.ToList();
			_tasks.Clear();
		}
		foreach (Func<Task> item in list)
		{
			await item();
		}
		if (_tasks.Any())
		{
			await ExecuteQueue(shouldLock: true);
		}
	}
}
