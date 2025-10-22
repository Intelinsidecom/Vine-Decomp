using Windows.Foundation.Metadata;

namespace Vine.Background.Framework;

[MarshalingBehavior(/*Could not decode attribute arguments.*/)]
[Threading(/*Could not decode attribute arguments.*/)]
[Version(21889117u)]
public static class ToastHelper : IToastHelperClass
{
	internal static extern void Show(string title, string msg, string type, string tag, string data);
}
