using System.Collections.Generic;
using System.Collections.ObjectModel;

namespace Vine.Background.Web;

internal class ParameterCollection : Collection<KeyValuePair<string, string>>
{
	public extern void AddIfHasValue(string name, bool? b);

	public extern void AddIfHasValue(string name, long? l);

	public extern void AddIfHasValue(string name, double? d);

	public extern void AddIfHasValue(string name, int? i);

	public extern void AddIfHasValue(string name, string s);

	public extern void AddIfNotNull(string name, string s);

	public extern void Add(string name, string value);

	public extern void Add(string name, bool b);

	public extern void Add(string name, long l);

	public extern void Add(string name, double d);

	public extern void Add(string name, int i);

	public extern ParameterCollection();
}
