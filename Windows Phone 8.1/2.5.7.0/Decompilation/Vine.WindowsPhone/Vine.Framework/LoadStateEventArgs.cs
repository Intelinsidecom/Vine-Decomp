using System;
using System.Collections.Generic;
using Windows.UI.Xaml.Navigation;

namespace Vine.Framework;

public class LoadStateEventArgs : EventArgs
{
	public object NavigationParameter { get; private set; }

	public Dictionary<string, object> PageState { get; private set; }

	public NavigationEventArgs NavigationEvent { get; private set; }

	public LoadStateEventArgs(object navigationParameter, Dictionary<string, object> pageState, NavigationEventArgs navigationEvent)
	{
		NavigationParameter = navigationParameter;
		PageState = pageState;
		NavigationEvent = navigationEvent;
	}
}
