using System.Collections.Generic;
using System.Collections.ObjectModel;
using Vine.Framework;

namespace Vine.Web;

public class ParameterCollection : Collection<KeyValuePair<string, string>>
{
	public void AddIfHasValue(string name, bool? b)
	{
		if (b.HasValue)
		{
			Add(name, b.Value.ToString().ToLowerInvariant());
		}
	}

	public void AddIfHasValue(string name, long? l)
	{
		if (l.HasValue)
		{
			Add(name, l.Value.ToStringInvariantCulture());
		}
	}

	public void AddIfHasValue(string name, double? d)
	{
		if (d.HasValue)
		{
			Add(name, d.Value.ToStringInvariantCulture());
		}
	}

	public void AddIfHasValue(string name, int? i)
	{
		if (i.HasValue)
		{
			Add(name, i.Value.ToStringInvariantCulture());
		}
	}

	public void AddIfHasValue(string name, string s)
	{
		if (!string.IsNullOrEmpty(s))
		{
			Add(name, s);
		}
	}

	public void AddIfNotNull(string name, string s)
	{
		if (s != null)
		{
			Add(name, s);
		}
	}

	public void Add(string name, string value)
	{
		if (value != null)
		{
			Add(new KeyValuePair<string, string>(name, value));
		}
	}

	public void Add(string name, bool b)
	{
		Add(name, b.ToString().ToLowerInvariant());
	}

	public void Add(string name, long l)
	{
		Add(name, l.ToStringInvariantCulture());
	}

	public void Add(string name, double d)
	{
		Add(name, d.ToStringInvariantCulture());
	}

	public void Add(string name, int i)
	{
		Add(name, i.ToStringInvariantCulture());
	}
}
