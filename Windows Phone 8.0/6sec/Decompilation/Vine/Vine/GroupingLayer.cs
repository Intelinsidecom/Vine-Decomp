using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Windows.Media;

namespace Vine;

public class GroupingLayer<TKey, TItem> : ObservableCollection<TItem>
{
	public TKey Key { get; protected set; }

	public Brush Brush { get; set; }

	public GroupingLayer(TKey key, IEnumerable<TItem> items)
		: base(items)
	{
		Key = key;
	}

	public GroupingLayer(IGrouping<TKey, TItem> grouping)
		: base((IEnumerable<TItem>)grouping)
	{
		Key = grouping.Key;
	}
}
