using System;
using System.Collections.Generic;
using Windows.UI.Xaml.Navigation;

namespace Vine.Framework;

public class SaveStateEventArgs : EventArgs
{
	public Dictionary<string, object> PageState { get; private set; }

	public NavigationEventArgs NavigationEventArgs { get; set; }

	public SaveStateEventArgs(Dictionary<string, object> pageState)
	{
		PageState = pageState;
	}
}
