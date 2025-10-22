using System;
using System.ComponentModel;
using System.IO;
using System.Net;

namespace Facebook;

/// <summary>
/// Wrapper for http web response.
/// </summary>
[EditorBrowsable(EditorBrowsableState.Never)]
public class HttpWebResponseWrapper
{
	/// <summary>
	/// The http web response.
	/// </summary>
	private readonly HttpWebResponse _httpWebResponse;

	/// <summary>
	/// Gets the http method.
	/// </summary>
	public virtual string Method => _httpWebResponse.Method;

	/// <summary>
	/// Gets the content length.
	/// </summary>
	public virtual long ContentLength => _httpWebResponse.ContentLength;

	/// <summary>
	/// Gets the content type.
	/// </summary>
	public virtual string ContentType => _httpWebResponse.ContentType;

	/// <summary>
	/// Gets the response uri.
	/// </summary>
	public virtual Uri ResponseUri => _httpWebResponse.ResponseUri;

	/// <summary>
	/// Gets the http status code.
	/// </summary>
	public virtual HttpStatusCode StatusCode => _httpWebResponse.StatusCode;

	/// <summary>
	/// Gets the status description.
	/// </summary>
	public virtual string StatusDescription => _httpWebResponse.StatusDescription;

	/// <summary>
	/// Gets the http cookies.
	/// </summary>
	public virtual CookieCollection Cookies => _httpWebResponse.Cookies;

	/// <summary>
	/// Gets the http headers.
	/// </summary>
	public virtual WebHeaderCollection Headers => _httpWebResponse.Headers;

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.HttpWebResponseWrapper" /> class.
	/// </summary>
	protected HttpWebResponseWrapper()
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.HttpWebResponseWrapper" /> class.
	/// </summary>
	/// <param name="httpWebResponse">
	/// The http web response.
	/// </param>
	public HttpWebResponseWrapper(HttpWebResponse httpWebResponse)
	{
		if (httpWebResponse == null)
		{
			throw new ArgumentNullException("httpWebResponse");
		}
		_httpWebResponse = httpWebResponse;
	}

	/// <summary>
	/// Gets the response stream.
	/// </summary>
	/// <returns>
	/// The response stream.
	/// </returns>
	public virtual Stream GetResponseStream()
	{
		return _httpWebResponse.GetResponseStream();
	}
}
