using System;

namespace Vine.Controls;

public class ApplicationBarSubMenuItem
{
	public ApplicationBarSubMenuIconButton Parent { get; set; }

	public string Text { get; set; }

	public bool IsEnabled { get; set; }

	public event EventHandler Click;

	public ApplicationBarSubMenuItem()
	{
		IsEnabled = true;
	}

	internal void ExecuteClick()
	{
		if (this.Click != null)
		{
			this.Click(this, EventArgs.Empty);
		}
	}
}
