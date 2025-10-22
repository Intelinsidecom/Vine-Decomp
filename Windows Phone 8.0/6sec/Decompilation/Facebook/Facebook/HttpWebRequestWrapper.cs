using System;
using System.ComponentModel;
using System.IO;
using System.Net;
using System.Threading.Tasks;

namespace Facebook;

/// <summary>
/// Represents the http web request wrapper.
/// </summary>
[EditorBrowsable(EditorBrowsableState.Never)]
public class HttpWebRequestWrapper
{
	/// <summary>
	/// The http web request.
	/// </summary>
	private readonly HttpWebRequest _httpWebRequest;

	private object _cancelledLock = new object();

	/// <summary>
	/// Gets or sets the http method.
	/// </summary>
	public virtual string Method
	{
		get
		{
			return _httpWebRequest.Method;
		}
		set
		{
			_httpWebRequest.Method = value;
		}
	}

	/// <summary>
	/// Gets or sets the content type.
	/// </summary>
	public virtual string ContentType
	{
		get
		{
			return _httpWebRequest.ContentType;
		}
		set
		{
			_httpWebRequest.ContentType = value;
		}
	}

	/// <summary>
	/// Gets or sets the http headers.
	/// </summary>
	public virtual WebHeaderCollection Headers
	{
		get
		{
			return _httpWebRequest.Headers;
		}
		set
		{
			_httpWebRequest.Headers = value;
		}
	}

	/// <summary>
	/// Gets or sets a value that indicates whether the request should follow redirection responses.
	/// </summary>
	public virtual bool AllowAutoRedirect
	{
		get
		{
			return _httpWebRequest.AllowAutoRedirect;
		}
		set
		{
			_httpWebRequest.AllowAutoRedirect = value;
		}
	}

	/// <summary>
	/// Gets or sets the user agent.
	/// </summary>
	public virtual string UserAgent
	{
		get
		{
			return _httpWebRequest.UserAgent;
		}
		set
		{
			_httpWebRequest.UserAgent = value;
		}
	}

	/// <summary>
	/// Gets or sets the cookie container.
	/// </summary>
	public virtual CookieContainer CookieContainer
	{
		get
		{
			return _httpWebRequest.CookieContainer;
		}
		set
		{
			_httpWebRequest.CookieContainer = value;
		}
	}

	/// <summary>
	/// Gets or sets the credentials.
	/// </summary>
	public virtual ICredentials Credentials
	{
		get
		{
			return _httpWebRequest.Credentials;
		}
		set
		{
			_httpWebRequest.Credentials = value;
		}
	}

	/// <summary>
	/// Gets the request uri.
	/// </summary>
	public virtual Uri RequestUri => _httpWebRequest.RequestUri;

	/// <summary>
	/// Gets or sets the accept.
	/// </summary>
	public virtual string Accept
	{
		get
		{
			return _httpWebRequest.Accept;
		}
		set
		{
			_httpWebRequest.Accept = value;
		}
	}

	/// <summary>
	/// Gets or sets if the request to an Internet resource was cancelled.
	/// </summary>
	public virtual bool IsCancelled { get; set; }

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.HttpWebRequestWrapper" /> class.
	/// </summary>
	protected HttpWebRequestWrapper()
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.HttpWebRequestWrapper" /> class.
	/// </summary>
	/// <param name="httpWebRequest">
	/// The http web request.
	/// </param>
	public HttpWebRequestWrapper(HttpWebRequest httpWebRequest)
	{
		if (httpWebRequest == null)
		{
			throw new ArgumentNullException("httpWebRequest");
		}
		_httpWebRequest = httpWebRequest;
	}

	/// <summary>
	/// Try to set the value of the content length header.
	/// </summary>
	/// <param name="contentLength">
	/// The Content-Length header value.
	/// </param>
	/// <returns>
	/// True if ContentLength set, otherwise false.
	/// </returns>
	public virtual bool TrySetContentLength(long contentLength)
	{
		return false;
	}

	/// <summary>
	/// Try to set the value of the user agenet header.
	/// </summary>
	/// <param name="userAgent">
	/// The UserAgent value.
	/// </param>
	/// <returns>
	/// True if UserAgent is set, otherwise false.
	/// </returns>
	public virtual bool TrySetUserAgent(string userAgent)
	{
		UserAgent = userAgent;
		return true;
	}

	/// <summary>
	/// Begins the get response.
	/// </summary>
	/// <param name="callback">
	/// The callback.
	/// </param>
	/// <param name="state">
	/// The state.
	/// </param>
	/// <returns>
	/// The async result.
	/// </returns>
	public virtual IAsyncResult BeginGetResponse(AsyncCallback callback, object state)
	{
		return _httpWebRequest.BeginGetResponse(callback, state);
	}

	/// <summary>
	/// Begins getting the request stream.
	/// </summary>
	/// <param name="callback">
	/// The callback.
	/// </param>
	/// <param name="state">
	/// The state.
	/// </param>
	/// <returns>
	/// The async result.
	/// </returns>
	public virtual IAsyncResult BeginGetRequestStream(AsyncCallback callback, object state)
	{
		return _httpWebRequest.BeginGetRequestStream(callback, state);
	}

	/// <summary>
	/// Ends the http web get response.
	/// </summary>
	/// <param name="asyncResult">
	/// The async result.
	/// </param>
	/// <returns>
	/// The http web response.
	/// </returns>
	public virtual HttpWebResponseWrapper EndGetResponse(IAsyncResult asyncResult)
	{
		HttpWebResponse httpWebResponse = (HttpWebResponse)_httpWebRequest.EndGetResponse(asyncResult);
		return new HttpWebResponseWrapper(httpWebResponse);
	}

	/// <summary>
	/// Ends the get request stream.
	/// </summary>
	/// <param name="asyncResult">
	/// The async result.
	/// </param>
	/// <returns>
	/// The request stream.
	/// </returns>
	public virtual Stream EndGetRequestStream(IAsyncResult asyncResult)
	{
		return _httpWebRequest.EndGetRequestStream(asyncResult);
	}

	/// <summary>
	/// Asynchronously gets the response.
	/// </summary>
	/// <returns></returns>
	public virtual Task<HttpWebResponseWrapper> GetResponseAsync()
	{
		return Task<HttpWebResponseWrapper>.Factory.FromAsync(BeginGetResponse, EndGetResponse, null);
	}

	/// <summary>
	/// Asynchronously gets the request stream.
	/// </summary>
	/// <returns></returns>
	public virtual Task<Stream> GetRequestStreamAsync()
	{
		return Task<Stream>.Factory.FromAsync(BeginGetRequestStream, EndGetRequestStream, null);
	}

	/// <summary>
	/// Aborts the http web request.
	/// </summary>
	public virtual void Abort()
	{
		lock (_cancelledLock)
		{
			try
			{
				_httpWebRequest.Abort();
			}
			catch (WebException webException)
			{
				throw new WebExceptionWrapper(webException);
			}
			finally
			{
				IsCancelled = true;
			}
		}
	}
}
