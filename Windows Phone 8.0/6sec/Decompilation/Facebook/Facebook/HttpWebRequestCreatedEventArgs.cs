using System;

namespace Facebook;

internal class HttpWebRequestCreatedEventArgs : EventArgs
{
	private readonly object _userToken;

	private readonly HttpWebRequestWrapper _httpWebRequestWrapper;

	public HttpWebRequestWrapper HttpWebRequest => _httpWebRequestWrapper;

	public object UserState => _userToken;

	public HttpWebRequestCreatedEventArgs(object userToken, HttpWebRequestWrapper httpWebRequestWrapper)
	{
		_userToken = userToken;
		_httpWebRequestWrapper = httpWebRequestWrapper;
	}
}
