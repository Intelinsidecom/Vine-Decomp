using Vine.Models;

namespace Vine.Test;

public class MemoryObject : NotifyObject
{
	private int _instanceCount;

	public int InstanceCount
	{
		get
		{
			return _instanceCount;
		}
		set
		{
			_instanceCount = value;
			NotifyOfPropertyChange(() => InstanceCount);
			NotifyOfPropertyChange(() => DisplayText);
		}
	}

	public string Type { get; set; }

	public string DisplayText => InstanceCount + " " + Type;
}
