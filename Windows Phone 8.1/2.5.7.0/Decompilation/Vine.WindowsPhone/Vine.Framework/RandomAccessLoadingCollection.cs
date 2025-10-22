using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation.Collections;

namespace Vine.Framework;

public class RandomAccessLoadingCollection<T> : IObservableVector<object>, IList<object>, ICollection<object>, IEnumerable<object>, IEnumerable, INotifyCollectionChanged
{
	private class VectorChangedEventArgs : IVectorChangedEventArgs
	{
		public CollectionChange CollectionChange { get; set; }

		public uint Index { get; set; }
	}

	private SortedDictionary<int, T> _items;

	private List<int> _itemsLoading;

	private List<T> _itemsUnloading;

	private T _defaultT;

	private int _count;

	private const int MaxRealizedItemCount = 50;

	private object _locker = new object();

	public List<int> ItemsLoading => _itemsLoading;

	public List<T> ItemsUnLoading => _itemsUnloading;

	public object this[int index]
	{
		get
		{
			bool flag = false;
			object result;
			lock (_locker)
			{
				if (!_items.ContainsKey(index))
				{
					_items[index] = _defaultT;
					_itemsLoading.Insert(0, index);
					TrimItems(index);
					flag = true;
				}
				result = _items[index];
			}
			if (flag)
			{
				RaiseItemsLoadStateChanged(index);
			}
			return result;
		}
		set
		{
			lock (_locker)
			{
				_items[index] = (T)value;
			}
			RaiseVectorChanged((CollectionChange)3, index);
		}
	}

	public int Count
	{
		get
		{
			return _count;
		}
		set
		{
			_count = value;
			RaiseVectorChanged((CollectionChange)0, 0);
		}
	}

	public bool IsReadOnly => false;

	public event NotifyCollectionChangedEventHandler CollectionChanged;

	public event VectorChangedEventHandler<object> VectorChanged
	{
		[CompilerGenerated]
		add
		{
			return EventRegistrationTokenTable<VectorChangedEventHandler<object>>.GetOrCreateEventRegistrationTokenTable(ref this.VectorChanged).AddEventHandler(value);
		}
		[CompilerGenerated]
		remove
		{
			EventRegistrationTokenTable<VectorChangedEventHandler<object>>.GetOrCreateEventRegistrationTokenTable(ref this.VectorChanged).RemoveEventHandler(value);
		}
	}

	public event EventHandler<int> ItemsLoadStateChanged;

	public RandomAccessLoadingCollection(T defaultT, int size)
	{
		_itemsLoading = new List<int>();
		_itemsUnloading = new List<T>();
		_items = new SortedDictionary<int, T>();
		_defaultT = defaultT;
		_count = size;
	}

	public void Clear()
	{
		_items.Clear();
		_itemsLoading.Clear();
		RaiseVectorChanged((CollectionChange)0, 0);
	}

	public void PopLoadingItems(List<int> itemsLoadedToPop, List<T> itemsUnloadedToPop)
	{
		lock (_locker)
		{
			foreach (int item in itemsLoadedToPop)
			{
				_itemsLoading.Remove(item);
			}
			foreach (T item2 in itemsUnloadedToPop)
			{
				_itemsUnloading.Remove(item2);
			}
		}
	}

	private void TrimItems(int index)
	{
		if (_items.Count > 50)
		{
			int num = _items.Keys.First();
			int num2 = _items.Keys.Last();
			int key = ((index > num && Math.Abs(index - num) > Math.Abs(num2 - index)) ? num : num2);
			T item = _items[key];
			_itemsUnloading.Add(item);
			_items.Remove(key);
		}
	}

	public List<int> TakeTopTenLoading()
	{
		lock (_locker)
		{
			return ItemsLoading.Take(10).ToList();
		}
	}

	public List<T> TakeTopTenUnloading()
	{
		lock (_locker)
		{
			return ItemsUnLoading.Take(10).ToList();
		}
	}

	public int IndexOf(object item)
	{
		T itemToFind = (T)item;
		lock (_locker)
		{
			if (_items != null && _items.Any() && _items.ContainsValue(itemToFind))
			{
				KeyValuePair<int, T> keyValuePair = _items.FirstOrDefault((KeyValuePair<int, T> i) => i.Value.Equals(itemToFind));
				if (keyValuePair.Value.Equals(itemToFind))
				{
					return keyValuePair.Key;
				}
			}
			return -1;
		}
	}

	public bool Remove(object item)
	{
		int num = IndexOf(item);
		if (num == -1)
		{
			return false;
		}
		RemoveAt(num);
		return true;
	}

	public void Insert(int index, object item)
	{
		throw new NotImplementedException();
	}

	public void RemoveAt(int index)
	{
		throw new NotImplementedException();
	}

	public void Add(object item)
	{
		throw new NotImplementedException();
	}

	public IEnumerator<object> GetEnumerator()
	{
		throw new NotImplementedException();
	}

	IEnumerator IEnumerable.GetEnumerator()
	{
		throw new NotImplementedException();
	}

	public bool Contains(object item)
	{
		throw new NotImplementedException();
	}

	public void CopyTo(object[] array, int arrayIndex)
	{
		throw new NotImplementedException();
	}

	private void RaiseItemsLoadStateChanged(int index)
	{
		this.ItemsLoadStateChanged?.Invoke(this, index);
	}

	private void RaiseVectorChanged(CollectionChange collectionChange, int index)
	{
		//IL_001c: Unknown result type (might be due to invalid IL or missing references)
		EventRegistrationTokenTable<VectorChangedEventHandler<object>>.GetOrCreateEventRegistrationTokenTable(ref this.VectorChanged).InvocationList?.Invoke((IObservableVector<object>)this, (IVectorChangedEventArgs)(object)new VectorChangedEventArgs
		{
			CollectionChange = collectionChange,
			Index = (uint)index
		});
	}
}
