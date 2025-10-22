using System;
using System.Threading.Tasks;
using Windows.ApplicationModel.Core;
using Windows.System.Threading;
using Windows.UI.Core;

namespace Vine.Framework;

public class DispatcherEx
{
	public static Task Invoke(Func<Task> action)
	{
		//IL_002f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0039: Expected O, but got Unknown
		TaskCompletionSource<bool> tcs = new TaskCompletionSource<bool>();
		CoreApplication.MainView.CoreWindow.Dispatcher.RunAsync((CoreDispatcherPriority)0, (DispatchedHandler)async delegate
		{
			await action();
			tcs.TrySetResult(result: true);
		});
		return tcs.Task;
	}

	public static async Task BeginInvoke(Action action)
	{
		await CoreApplication.MainView.CoreWindow.Dispatcher.RunAsync((CoreDispatcherPriority)0, (DispatchedHandler)delegate
		{
			action();
		});
	}

	public static Task InvokeBackground(Func<Task> action)
	{
		//IL_001e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0028: Expected O, but got Unknown
		TaskCompletionSource<bool> tcs = new TaskCompletionSource<bool>();
		ThreadPool.RunAsync((WorkItemHandler)async delegate
		{
			await action();
			tcs.TrySetResult(result: true);
		});
		return tcs.Task;
	}
}
