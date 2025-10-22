using System;
using System.Threading.Tasks;
using Windows.UI.Xaml;

namespace Vine.Framework;

public static class EventAsync
{
	private sealed class EventHandlerTaskSource<TEventArgs>
	{
		private readonly TaskCompletionSource<object> tcs;

		private readonly Action<EventHandler<TEventArgs>> removeEventHandler;

		public Task<object> Task => tcs.Task;

		public EventHandlerTaskSource(Action<EventHandler<TEventArgs>> addEventHandler, Action<EventHandler<TEventArgs>> removeEventHandler, Action beginAction = null)
		{
			if (addEventHandler == null)
			{
				throw new ArgumentNullException("addEventHandler");
			}
			if (removeEventHandler == null)
			{
				throw new ArgumentNullException("removeEventHandler");
			}
			tcs = new TaskCompletionSource<object>();
			this.removeEventHandler = removeEventHandler;
			addEventHandler(EventCompleted);
			beginAction?.Invoke();
		}

		private void EventCompleted(object sender, TEventArgs args)
		{
			removeEventHandler(EventCompleted);
			tcs.SetResult(args);
		}
	}

	private sealed class RoutedEventHandlerTaskSource
	{
		private readonly TaskCompletionSource<RoutedEventArgs> tcs;

		private readonly Action<RoutedEventHandler> removeEventHandler;

		public Task<RoutedEventArgs> Task => tcs.Task;

		public RoutedEventHandlerTaskSource(Action<RoutedEventHandler> addEventHandler, Action<RoutedEventHandler> removeEventHandler, Action beginAction = null)
		{
			//IL_003c: Unknown result type (might be due to invalid IL or missing references)
			//IL_0046: Expected O, but got Unknown
			if (addEventHandler == null)
			{
				throw new ArgumentNullException("addEventHandler");
			}
			if (removeEventHandler == null)
			{
				throw new ArgumentNullException("removeEventHandler");
			}
			tcs = new TaskCompletionSource<RoutedEventArgs>();
			this.removeEventHandler = removeEventHandler;
			addEventHandler(new RoutedEventHandler(EventCompleted));
			beginAction?.Invoke();
		}

		private void EventCompleted(object sender, RoutedEventArgs args)
		{
			//IL_000d: Unknown result type (might be due to invalid IL or missing references)
			//IL_0017: Expected O, but got Unknown
			removeEventHandler(new RoutedEventHandler(EventCompleted));
			tcs.SetResult(args);
		}
	}

	public static Task<object> FromEvent<T>(Action<EventHandler<T>> addEventHandler, Action<EventHandler<T>> removeEventHandler, Action beginAction = null)
	{
		return new EventHandlerTaskSource<T>(addEventHandler, removeEventHandler, beginAction).Task;
	}

	public static Task<RoutedEventArgs> FromRoutedEvent(Action<RoutedEventHandler> addEventHandler, Action<RoutedEventHandler> removeEventHandler, Action beginAction = null)
	{
		return new RoutedEventHandlerTaskSource(addEventHandler, removeEventHandler, beginAction).Task;
	}
}
