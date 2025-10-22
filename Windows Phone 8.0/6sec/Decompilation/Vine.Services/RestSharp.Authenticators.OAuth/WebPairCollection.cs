using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

namespace RestSharp.Authenticators.OAuth;

internal class WebPairCollection : IList<WebPair>, ICollection<WebPair>, IEnumerable<WebPair>, IEnumerable
{
	private IList<WebPair> _parameters;

	public virtual WebPair this[string name] => this.SingleOrDefault((WebPair p) => p.Name.Equals(name));

	public virtual IEnumerable<string> Names => _parameters.Select((WebPair p) => p.Name);

	public virtual IEnumerable<string> Values => _parameters.Select((WebPair p) => p.Value);

	public virtual int Count => _parameters.Count;

	public virtual bool IsReadOnly => _parameters.IsReadOnly;

	public virtual WebPair this[int index]
	{
		get
		{
			return _parameters[index];
		}
		set
		{
			_parameters[index] = value;
		}
	}

	public WebPairCollection(IEnumerable<WebPair> parameters)
	{
		_parameters = new List<WebPair>(parameters);
	}

	public WebPairCollection(IDictionary<string, string> collection)
		: this()
	{
		AddCollection(collection);
	}

	public void AddCollection(IDictionary<string, string> collection)
	{
		foreach (string key in collection.Keys)
		{
			WebPair item = new WebPair(key, collection[key]);
			_parameters.Add(item);
		}
	}

	public WebPairCollection()
	{
		_parameters = new List<WebPair>(0);
	}

	public WebPairCollection(int capacity)
	{
		_parameters = new List<WebPair>(capacity);
	}

	private void AddCollection(IEnumerable<WebPair> collection)
	{
		foreach (WebPair item2 in collection)
		{
			WebPair item = new WebPair(item2.Name, item2.Value);
			_parameters.Add(item);
		}
	}

	public virtual void AddRange(WebPairCollection collection)
	{
		AddCollection(collection);
	}

	public virtual void AddRange(IEnumerable<WebPair> collection)
	{
		AddCollection(collection);
	}

	public virtual void Sort(Comparison<WebPair> comparison)
	{
		List<WebPair> list = new List<WebPair>(_parameters);
		list.Sort(comparison);
		_parameters = list;
	}

	public virtual bool RemoveAll(IEnumerable<WebPair> parameters)
	{
		bool flag = true;
		WebPair[] array = parameters.ToArray();
		foreach (WebPair item in array)
		{
			flag &= _parameters.Remove(item);
		}
		if (flag)
		{
			return array.Length != 0;
		}
		return false;
	}

	public virtual void Add(string name, string value)
	{
		WebPair item = new WebPair(name, value);
		_parameters.Add(item);
	}

	public virtual IEnumerator<WebPair> GetEnumerator()
	{
		return _parameters.GetEnumerator();
	}

	IEnumerator IEnumerable.GetEnumerator()
	{
		return GetEnumerator();
	}

	public virtual void Add(WebPair parameter)
	{
		_parameters.Add(parameter);
	}

	public virtual void Clear()
	{
		_parameters.Clear();
	}

	public virtual bool Contains(WebPair parameter)
	{
		return _parameters.Contains(parameter);
	}

	public virtual void CopyTo(WebPair[] parameters, int arrayIndex)
	{
		_parameters.CopyTo(parameters, arrayIndex);
	}

	public virtual bool Remove(WebPair parameter)
	{
		return _parameters.Remove(parameter);
	}

	public virtual int IndexOf(WebPair parameter)
	{
		return _parameters.IndexOf(parameter);
	}

	public virtual void Insert(int index, WebPair parameter)
	{
		_parameters.Insert(index, parameter);
	}

	public virtual void RemoveAt(int index)
	{
		_parameters.RemoveAt(index);
	}
}
