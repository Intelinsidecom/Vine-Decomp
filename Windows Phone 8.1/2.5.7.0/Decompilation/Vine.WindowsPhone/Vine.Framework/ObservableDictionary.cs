using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation.Collections;

namespace Vine.Framework;

public class ObservableDictionary : IObservableMap<string, object>, IDictionary<string, object>, ICollection<KeyValuePair<string, object>>, IEnumerable<KeyValuePair<string, object>>, IEnumerable
{
	private class ObservableDictionaryChangedEventArgs : IMapChangedEventArgs<string>
	{
		public CollectionChange CollectionChange { get; private set; }

		public string Key { get; private set; }

		public ObservableDictionaryChangedEventArgs(CollectionChange change, string key)
		{
			//IL_0007: Unknown result type (might be due to invalid IL or missing references)
			CollectionChange = change;
			Key = key;
		}
	}

	private Dictionary<string, object> _dictionary = new Dictionary<string, object>();

	[CompilerGenerated]
	private EventRegistrationTokenTable<MapChangedEventHandler<string, object>> m_MapChanged;

	public object this[string key]
	{
		get
		{
			return _dictionary[key];
		}
		set
		{
			_dictionary[key] = value;
			InvokeMapChanged((CollectionChange)3, key);
		}
	}

	public ICollection<string> Keys => _dictionary.Keys;

	public ICollection<object> Values => _dictionary.Values;

	public int Count => _dictionary.Count;

	public bool IsReadOnly => false;

	public event MapChangedEventHandler<string, object> MapChanged
	{
		[CompilerGenerated]
		add
		{
			return EventRegistrationTokenTable<MapChangedEventHandler<string, object>>.GetOrCreateEventRegistrationTokenTable(ref this.m_MapChanged).AddEventHandler(value);
		}
		[CompilerGenerated]
		remove
		{
			EventRegistrationTokenTable<MapChangedEventHandler<string, object>>.GetOrCreateEventRegistrationTokenTable(ref this.m_MapChanged).RemoveEventHandler(value);
		}
	}

	private void InvokeMapChanged(CollectionChange change, string key)
	{
		//IL_0016: Unknown result type (might be due to invalid IL or missing references)
		EventRegistrationTokenTable<MapChangedEventHandler<string, object>>.GetOrCreateEventRegistrationTokenTable(ref this.MapChanged).InvocationList?.Invoke((IObservableMap<string, object>)this, (IMapChangedEventArgs<string>)new ObservableDictionaryChangedEventArgs(change, key));
	}

	public void Add(string key, object value)
	{
		_dictionary.Add(key, value);
		InvokeMapChanged((CollectionChange)1, key);
	}

	public void Add(KeyValuePair<string, object> item)
	{
		Add(item.Key, item.Value);
	}

	public bool Remove(string key)
	{
		if (_dictionary.Remove(key))
		{
			InvokeMapChanged((CollectionChange)2, key);
			return true;
		}
		return false;
	}

	public bool Remove(KeyValuePair<string, object> item)
	{
		if (_dictionary.TryGetValue(item.Key, out var value) && object.Equals(item.Value, value) && _dictionary.Remove(item.Key))
		{
			InvokeMapChanged((CollectionChange)2, item.Key);
			return true;
		}
		return false;
	}

	public void Clear()
	{
		string[] array = _dictionary.Keys.ToArray();
		_dictionary.Clear();
		string[] array2 = array;
		foreach (string key in array2)
		{
			InvokeMapChanged((CollectionChange)2, key);
		}
	}

	public bool ContainsKey(string key)
	{
		return _dictionary.ContainsKey(key);
	}

	public bool TryGetValue(string key, out object value)
	{
		return _dictionary.TryGetValue(key, out value);
	}

	public bool Contains(KeyValuePair<string, object> item)
	{
		return _dictionary.Contains(item);
	}

	public IEnumerator<KeyValuePair<string, object>> GetEnumerator()
	{
		return _dictionary.GetEnumerator();
	}

	IEnumerator IEnumerable.GetEnumerator()
	{
		return _dictionary.GetEnumerator();
	}

	public void CopyTo(KeyValuePair<string, object>[] array, int arrayIndex)
	{
		int num = array.Length;
		foreach (KeyValuePair<string, object> item in _dictionary)
		{
			if (arrayIndex >= num)
			{
				break;
			}
			array[arrayIndex++] = item;
		}
	}
}
