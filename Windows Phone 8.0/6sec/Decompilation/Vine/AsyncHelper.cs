using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;

public static class AsyncHelper
{
	private class ExclusiveSynchronizationContext : SynchronizationContext
	{
		private bool done;

		private readonly AutoResetEvent workItemsWaiting = new AutoResetEvent(initialState: false);

		private readonly Queue<Tuple<SendOrPostCallback, object>> items = new Queue<Tuple<SendOrPostCallback, object>>();

		public Exception InnerException { get; set; }

		public override void Send(SendOrPostCallback d, object state)
		{
			throw new NotSupportedException("We cannot send to our same thread");
		}

		public override void Post(SendOrPostCallback d, object state)
		{
			bool lockTaken = false;
			Queue<Tuple<SendOrPostCallback, object>> obj = default(Queue<Tuple<SendOrPostCallback, object>>);
			try
			{
				obj = items;
				Monitor.Enter(obj, ref lockTaken);
				items.Enqueue(Tuple.Create(d, state));
			}
			finally
			{
				if (lockTaken)
				{
					Monitor.Exit(obj);
				}
			}
			workItemsWaiting.Set();
		}

		public void EndMessageLoop()
		{
			Post(delegate
			{
				done = true;
			}, null);
		}

		public void BeginMessageLoop()
		{
			Queue<Tuple<SendOrPostCallback, object>> obj = default(Queue<Tuple<SendOrPostCallback, object>>);
			while (!done)
			{
				Tuple<SendOrPostCallback, object> tuple = null;
				bool lockTaken = false;
				try
				{
					obj = items;
					Monitor.Enter(obj, ref lockTaken);
					if (items.Count > 0)
					{
						tuple = items.Dequeue();
					}
				}
				finally
				{
					if (lockTaken)
					{
						Monitor.Exit(obj);
					}
				}
				if (tuple != null)
				{
					tuple.Item1(tuple.Item2);
					if (InnerException != null)
					{
						throw InnerException;
					}
				}
				else
				{
					workItemsWaiting.WaitOne();
				}
			}
		}

		public override SynchronizationContext CreateCopy()
		{
			return this;
		}
	}

	public static void RunSync(Func<Task> task)
	{
		SynchronizationContext current = SynchronizationContext.Current;
		ExclusiveSynchronizationContext synch = new ExclusiveSynchronizationContext();
		SynchronizationContext.SetSynchronizationContext(synch);
		synch.Post(async delegate
		{
			_ = 1;
			try
			{
				await task();
			}
			catch (Exception innerException)
			{
				synch.InnerException = innerException;
				throw;
			}
			finally
			{
				synch.EndMessageLoop();
			}
		}, null);
		synch.BeginMessageLoop();
		SynchronizationContext.SetSynchronizationContext(current);
	}

	public static T RunSync<T>(Func<Task<T>> task)
	{
		SynchronizationContext current = SynchronizationContext.Current;
		ExclusiveSynchronizationContext synch = new ExclusiveSynchronizationContext();
		SynchronizationContext.SetSynchronizationContext(synch);
		T ret = default(T);
		synch.Post(async delegate
		{
			_ = 1;
			try
			{
				_ = ret;
				ret = await task();
			}
			catch (Exception innerException)
			{
				synch.InnerException = innerException;
				throw;
			}
			finally
			{
				synch.EndMessageLoop();
			}
		}, null);
		synch.BeginMessageLoop();
		SynchronizationContext.SetSynchronizationContext(current);
		return ret;
	}
}
