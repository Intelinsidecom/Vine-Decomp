using System;
using System.ComponentModel;
using System.Net;

namespace Facebook;

/// <summary>
/// Wrapper for web exceptions.
/// </summary>
[EditorBrowsable(EditorBrowsableState.Never)]
public class WebExceptionWrapper : Exception
{
	private readonly WebException _webException;

	private readonly WebExceptionStatus _status = WebExceptionStatus.UnknownError;

	/// <summary>
	/// Returns the actual web exception.
	/// </summary>
	public virtual WebException ActualWebException => _webException;

	/// <summary>
	/// Returns the web exception status.
	/// </summary>
	public virtual WebExceptionStatus Status => _status;

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.WebExceptionWrapper" /> class.
	/// </summary>
	protected WebExceptionWrapper()
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.WebExceptionWrapper" /> class.
	/// </summary>
	public WebExceptionWrapper(WebException webException)
		: base(webException?.Message, webException?.InnerException)
	{
		_webException = webException;
		_status = webException?.Status ?? WebExceptionStatus.UnknownError;
	}

	/// <summary>
	/// Gets the response of the web exception.
	/// </summary>
	/// <returns>
	/// Returns the response of the web exception if it has a response, otherwise null.
	/// </returns>
	public virtual HttpWebResponseWrapper GetResponse()
	{
		if (_webException.Response != null)
		{
			return new HttpWebResponseWrapper((HttpWebResponse)_webException.Response);
		}
		return null;
	}
}
