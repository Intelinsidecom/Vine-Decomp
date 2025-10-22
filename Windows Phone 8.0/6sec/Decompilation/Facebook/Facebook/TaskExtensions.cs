using System;
using System.Threading.Tasks;

namespace Facebook;

internal static class TaskExtensions
{
	public static Task<T2> Then<T1, T2>(this Task<T1> first, Func<T1, T2> next)
	{
		if (first == null)
		{
			throw new ArgumentNullException("first");
		}
		if (next == null)
		{
			throw new ArgumentNullException("next");
		}
		TaskCompletionSource<T2> tcs = new TaskCompletionSource<T2>();
		first.ContinueWith(delegate
		{
			if (first.IsFaulted)
			{
				tcs.TrySetException(first.Exception.InnerExceptions);
			}
			else
			{
				if (!first.IsCanceled)
				{
					try
					{
						T2 result = next(first.Result);
						tcs.TrySetResult(result);
						return;
					}
					catch (Exception exception)
					{
						tcs.TrySetException(exception);
						return;
					}
				}
				tcs.TrySetCanceled();
			}
		});
		return tcs.Task;
	}
}
