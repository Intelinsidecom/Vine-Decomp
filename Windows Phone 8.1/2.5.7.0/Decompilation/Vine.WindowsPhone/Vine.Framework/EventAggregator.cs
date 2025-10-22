using System;
using System.Collections.Generic;

namespace Vine.Framework;

public class EventAggregator
{
	private static EventAggregator _current;

	private readonly List<WeakReference> _listeners = new List<WeakReference>();

	public static EventAggregator Current => _current ?? (_current = new EventAggregator());

	public void Publish<T>(T message)
	{
		int num = 0;
		lock (_listeners)
		{
			for (int i = 0; i < _listeners.Count; i++)
			{
				object target = _listeners[i].Target;
				if (target == null)
				{
					_listeners.RemoveAt(i);
					i--;
				}
				else if (target is IEventHandler<T>)
				{
					num++;
					((IEventHandler<T>)target).Handle(message);
				}
			}
		}
	}

	public void Subscribe(object listener)
	{
		lock (_listeners)
		{
			if (GetIndexOfAndPrune(listener) == -1)
			{
				_listeners.Add(new WeakReference(listener));
			}
		}
	}

	public void Unsubscribe(object listener)
	{
		lock (_listeners)
		{
			int indexOfAndPrune = GetIndexOfAndPrune(listener);
			_listeners.RemoveAt(indexOfAndPrune);
		}
	}

	private int GetIndexOfAndPrune(object listener)
	{
		for (int i = 0; i < _listeners.Count; i++)
		{
			object target = _listeners[i].Target;
			if (target == null)
			{
				_listeners.RemoveAt(i);
				i--;
			}
			else if (target == listener)
			{
				return i;
			}
		}
		return -1;
	}
}
