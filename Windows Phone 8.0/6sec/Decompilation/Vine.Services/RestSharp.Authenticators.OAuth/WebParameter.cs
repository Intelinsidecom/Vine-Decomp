using System.Diagnostics;

namespace RestSharp.Authenticators.OAuth;

[DebuggerDisplay("{Name}:{Value}")]
internal class WebParameter : WebPair
{
	public WebParameter(string name, string value)
		: base(name, value)
	{
	}
}
