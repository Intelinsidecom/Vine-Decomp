using System;
using System.ComponentModel;
using System.IO;
using System.Net;
using System.Threading;
using System.Threading.Tasks;

namespace Facebook;

/// <summary>
/// Http helper.
/// </summary>
internal class HttpHelper
{
	private const string ErrorPerformingHttpRequest = "An error occurred performing a http web request.";

	private readonly HttpWebRequestWrapper _httpWebRequest;

	private HttpWebResponseWrapper _httpWebResponse;

	private Exception _innerException;

	/// <summary>
	/// Gets the inner exception.
	/// </summary>
	public Exception InnerException => _innerException;

	/// <summary>
	/// Gets or sets the timeout.
	/// </summary>
	public int Timeout { get; set; }

	/// <summary>
	/// Gets the http web request.
	/// </summary>
	public HttpWebRequestWrapper HttpWebRequest => _httpWebRequest;

	/// <summary>
	/// Gets the http web response.
	/// </summary>
	public HttpWebResponseWrapper HttpWebResponse => _httpWebResponse;

	/// <summary>
	/// Open read completed event handler.
	/// </summary>
	public event OpenReadCompletedEventHandler OpenReadCompleted;

	/// <summary>
	/// Open write completed event handler.
	/// </summary>
	public event OpenWriteCompletedEventHandler OpenWriteCompleted;

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.HttpHelper" /> class.
	/// </summary>
	/// <param name="url"></param>
	public HttpHelper(Uri url)
		: this(new HttpWebRequestWrapper((HttpWebRequest)WebRequest.Create(url)))
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.HttpHelper" /> class.
	/// </summary>
	/// <param name="url"></param>
	public HttpHelper(string url)
		: this(new Uri(url))
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.HttpHelper" /> class.
	/// </summary>
	/// <param name="httpWebRequest"></param>
	public HttpHelper(HttpWebRequestWrapper httpWebRequest)
	{
		_httpWebRequest = httpWebRequest;
	}

	/// <summary>
	/// Asynchronously opens the response stream for read.
	/// </summary>
	/// <param name="userToken"></param>
	public virtual void OpenReadAsync(object userToken)
	{
		if (_httpWebResponse == null)
		{
			WebExceptionWrapper webExceptionWrapper = null;
			try
			{
				IAsyncResult asyncResult = _httpWebRequest.BeginGetResponse(delegate(IAsyncResult ar)
				{
					ResponseCallback(ar, userToken);
				}, null);
				int num = 0;
				if (Timeout > 0)
				{
					num = Timeout;
				}
				if (num > 0)
				{
					ThreadPool.RegisterWaitForSingleObject(asyncResult.AsyncWaitHandle, ScanTimoutCallback, userToken, num, executeOnlyOnce: true);
				}
				return;
			}
			catch (WebException ex)
			{
				if (ex.Response != null)
				{
					_httpWebResponse = new HttpWebResponseWrapper((HttpWebResponse)ex.Response);
				}
				webExceptionWrapper = (WebExceptionWrapper)(_innerException = new WebExceptionWrapper(ex));
				return;
			}
			catch (WebExceptionWrapper webExceptionWrapper2)
			{
				_httpWebResponse = webExceptionWrapper2.GetResponse();
				webExceptionWrapper = (WebExceptionWrapper)(_innerException = webExceptionWrapper2);
				return;
			}
			catch (Exception innerException)
			{
				webExceptionWrapper = (WebExceptionWrapper)(_innerException = new WebExceptionWrapper(new WebException("An error occurred performing a http web request.", innerException)));
				return;
			}
			finally
			{
				if (webExceptionWrapper != null)
				{
					OnOpenReadCompleted(new OpenReadCompletedEventArgs(null, webExceptionWrapper, webExceptionWrapper.Status == WebExceptionStatus.RequestCanceled, userToken));
				}
			}
		}
		ResponseCallback(null, userToken);
	}

	/// <summary>
	/// Asynchronously opens the response stream for read.
	/// </summary>
	public virtual void OpenReadAsync()
	{
		OpenReadAsync(null);
	}

	/// <summary>
	/// Asynchronously opens the request stream for write.
	/// </summary>
	/// <param name="userToken"></param>
	public virtual void OpenWriteAsync(object userToken)
	{
		WebExceptionWrapper webExceptionWrapper = null;
		try
		{
			_httpWebRequest.BeginGetRequestStream(delegate(IAsyncResult ar)
			{
				Stream result = null;
				WebExceptionWrapper webExceptionWrapper2 = null;
				try
				{
					result = _httpWebRequest.EndGetRequestStream(ar);
				}
				catch (WebException ex2)
				{
					if (ex2.Response != null)
					{
						_httpWebResponse = new HttpWebResponseWrapper((HttpWebResponse)ex2.Response);
					}
					webExceptionWrapper2 = new WebExceptionWrapper(ex2);
					_innerException = ex2;
				}
				catch (WebExceptionWrapper webExceptionWrapper3)
				{
					_httpWebResponse = webExceptionWrapper3.GetResponse();
					webExceptionWrapper2 = webExceptionWrapper3;
					_innerException = webExceptionWrapper3;
				}
				catch (Exception innerException3)
				{
					webExceptionWrapper2 = new WebExceptionWrapper(new WebException("An error occurred performing a http web request.", innerException3));
					_innerException = webExceptionWrapper2;
				}
				OnOpenWriteCompleted(new OpenWriteCompletedEventArgs(result, webExceptionWrapper2, webExceptionWrapper2 != null && webExceptionWrapper2.Status == WebExceptionStatus.RequestCanceled, userToken));
			}, userToken);
		}
		catch (WebException ex)
		{
			webExceptionWrapper = new WebExceptionWrapper(ex);
			_innerException = ex;
		}
		catch (WebExceptionWrapper innerException)
		{
			webExceptionWrapper = (WebExceptionWrapper)(_innerException = innerException);
		}
		catch (Exception innerException2)
		{
			webExceptionWrapper = (WebExceptionWrapper)(_innerException = new WebExceptionWrapper(new WebException("An error occurred performing a http web request.", innerException2)));
		}
		finally
		{
			if (webExceptionWrapper != null)
			{
				OnOpenWriteCompleted(new OpenWriteCompletedEventArgs(null, webExceptionWrapper, webExceptionWrapper.Status == WebExceptionStatus.RequestCanceled, userToken));
			}
		}
	}

	/// <summary>
	/// Asynchronously opens the request stream for write.
	/// </summary>
	public virtual void OpenWriteAsync()
	{
		OpenWriteAsync(null);
	}

	private void ResponseCallback(IAsyncResult asyncResult, object userToken)
	{
		WebExceptionWrapper webExceptionWrapper = null;
		Stream result = null;
		try
		{
			if (_httpWebResponse == null)
			{
				_httpWebResponse = _httpWebRequest.EndGetResponse(asyncResult);
			}
			result = _httpWebResponse.GetResponseStream();
		}
		catch (WebException ex)
		{
			if (ex.Response != null)
			{
				_httpWebResponse = new HttpWebResponseWrapper((HttpWebResponse)ex.Response);
			}
			webExceptionWrapper = (WebExceptionWrapper)(_innerException = new WebExceptionWrapper(ex));
		}
		catch (WebExceptionWrapper webExceptionWrapper2)
		{
			_httpWebResponse = webExceptionWrapper2.GetResponse();
			webExceptionWrapper = (WebExceptionWrapper)(_innerException = webExceptionWrapper2);
		}
		catch (Exception innerException)
		{
			webExceptionWrapper = (WebExceptionWrapper)(_innerException = new WebExceptionWrapper(new WebException("An error occurred performing a http web request.", innerException)));
		}
		OnOpenReadCompleted(new OpenReadCompletedEventArgs(result, webExceptionWrapper, webExceptionWrapper != null && webExceptionWrapper.Status == WebExceptionStatus.RequestCanceled, userToken));
	}

	private void ScanTimoutCallback(object state, bool timedOut)
	{
		if (HttpWebRequest != null && timedOut)
		{
			HttpWebRequest.Abort();
		}
	}

	private static void TransferCompletionToTask<T>(TaskCompletionSource<T> tcs, AsyncCompletedEventArgs e, Func<T> getResult, Action unregisterHandler)
	{
		if (e.UserState != tcs)
		{
			return;
		}
		try
		{
			unregisterHandler();
		}
		finally
		{
			if (e.Cancelled)
			{
				tcs.TrySetCanceled();
			}
			else if (e.Error != null)
			{
				tcs.TrySetException(e.Error);
			}
			else
			{
				tcs.TrySetResult(getResult());
			}
		}
	}

	/// <summary>
	/// Asynchronously open the response stream for read.
	/// </summary>
	/// <param name="cancellationToken"></param>
	/// <returns></returns>
	public virtual Task<Stream> OpenReadTaskAsync(CancellationToken cancellationToken)
	{
		TaskCompletionSource<Stream> tcs = new TaskCompletionSource<Stream>(this);
		CancellationTokenRegistration ctr = cancellationToken.Register(delegate
		{
			try
			{
				CancelAsync();
			}
			catch (WebExceptionWrapper webExceptionWrapper)
			{
				if (webExceptionWrapper.Status != WebExceptionStatus.RequestCanceled)
				{
					throw;
				}
			}
		});
		OpenReadCompletedEventHandler handler = null;
		handler = delegate(object sender, OpenReadCompletedEventArgs e)
		{
			TransferCompletionToTask(tcs, e, () => e.Result, delegate
			{
				ctr.Dispose();
				OpenReadCompleted -= handler;
			});
		};
		OpenReadCompleted += handler;
		try
		{
			if (cancellationToken.IsCancellationRequested)
			{
				tcs.TrySetCanceled();
			}
			else
			{
				OpenReadAsync(tcs);
				if (cancellationToken.IsCancellationRequested)
				{
					tcs.TrySetCanceled();
				}
			}
		}
		catch
		{
			OpenReadCompleted -= handler;
			throw;
		}
		return tcs.Task;
	}

	/// <summary>
	/// Asynchronously opens the response stream for read.
	/// </summary>
	/// <returns></returns>
	public virtual Task<Stream> OpenReadTaskAsync()
	{
		return OpenReadTaskAsync(CancellationToken.None);
	}

	/// <summary>
	/// Asynchronously opens the request stream for write.
	/// </summary>
	/// <returns></returns>
	public virtual Task<Stream> OpenWriteTaskAsync()
	{
		return OpenWriteTaskAsync(CancellationToken.None);
	}

	/// <summary>
	/// Asynchronously opens the request stream for write.
	/// </summary>
	/// <param name="cancellationToken"></param>
	/// <returns></returns>
	public virtual Task<Stream> OpenWriteTaskAsync(CancellationToken cancellationToken)
	{
		TaskCompletionSource<Stream> tcs = new TaskCompletionSource<Stream>(this);
		CancellationTokenRegistration ctr = cancellationToken.Register(delegate
		{
			try
			{
				CancelAsync();
			}
			catch (WebExceptionWrapper webExceptionWrapper)
			{
				if (webExceptionWrapper.Status != WebExceptionStatus.RequestCanceled)
				{
					throw;
				}
			}
		});
		OpenWriteCompletedEventHandler handler = null;
		handler = delegate(object sender, OpenWriteCompletedEventArgs e)
		{
			TransferCompletionToTask(tcs, e, () => e.Result, delegate
			{
				ctr.Dispose();
				OpenWriteCompleted -= handler;
			});
		};
		OpenWriteCompleted += handler;
		try
		{
			if (cancellationToken.IsCancellationRequested)
			{
				tcs.TrySetCanceled();
			}
			else
			{
				OpenWriteAsync(tcs);
				if (cancellationToken.IsCancellationRequested)
				{
					tcs.TrySetCanceled();
				}
			}
		}
		catch
		{
			OpenWriteCompleted -= handler;
			throw;
		}
		return tcs.Task;
	}

	/// <summary>
	/// Cancels the http web request.
	/// </summary>
	public void CancelAsync()
	{
		try
		{
			HttpWebRequest.Abort();
		}
		catch (WebExceptionWrapper)
		{
			throw;
		}
		catch (WebException webException)
		{
			throw new WebExceptionWrapper(webException);
		}
		catch (Exception innerException)
		{
			throw new WebExceptionWrapper(new WebException("An error occurred performing a http web request.", innerException));
		}
	}

	/// <summary>
	/// On open read completed.
	/// </summary>
	/// <param name="args"></param>
	protected virtual void OnOpenReadCompleted(OpenReadCompletedEventArgs args)
	{
		if (this.OpenReadCompleted != null)
		{
			this.OpenReadCompleted(this, args);
		}
	}

	/// <summary>
	/// On open write completed.
	/// </summary>
	/// <param name="args"></param>
	protected virtual void OnOpenWriteCompleted(OpenWriteCompletedEventArgs args)
	{
		if (this.OpenWriteCompleted != null)
		{
			this.OpenWriteCompleted(this, args);
		}
	}

	/// <summary>
	/// Url encodes the specified string.
	/// </summary>
	/// <param name="s">The string to url encode.</param>
	/// <returns>The url encoded string.</returns>
	public static string UrlEncode(string s)
	{
		return Uri.EscapeDataString(s);
	}

	/// <summary>
	/// Url decodes the specified string.
	/// </summary>
	/// <param name="s">The string to url decode.</param>
	/// <returns>The url decoded string.</returns>
	public static string UrlDecode(string s)
	{
		return HttpUtility.UrlDecode(s);
	}

	/// <summary>
	/// Html decode the speficied string.
	/// </summary>
	/// <param name="s">The string to html decode.</param>
	/// <returns>The html decoded string.</returns>
	public static string HtmlDecode(string s)
	{
		return HttpUtility.HtmlDecode(s);
	}
}
