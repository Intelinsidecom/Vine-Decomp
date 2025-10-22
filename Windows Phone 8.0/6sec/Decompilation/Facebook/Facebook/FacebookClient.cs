using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Net;
using System.Reflection;
using System.Security.Cryptography;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Facebook;

/// <summary>
/// Provides access to the Facbook Platform.
/// </summary>
public class FacebookClient
{
	private const string AtLeastOneBatchParameterRequried = "At least one batch parameter is required";

	private const string OnlyOneAttachmentAllowedPerBatchRequest = "Only one attachement (FacebookMediaObject/FacebookMediaStream) allowed per FacebookBatchParamter.";

	private const int BufferSize = 4096;

	private const string AttachmentMustHavePropertiesSetError = "Attachment (FacebookMediaObject/FacebookMediaStream) must have a content type, file name, and value set.";

	private const string AttachmentValueIsNull = "The value of attachment (FacebookMediaObject/FacebookMediaStream) is null.";

	private const string UnknownResponse = "Unknown facebook response.";

	private const string MultiPartFormPrefix = "--";

	private const string MultiPartNewLine = "\r\n";

	private const string ETagKey = "_etag_";

	private const string InvalidSignedRequest = "Invalid signed_request";

	private HttpWebRequestWrapper _httpWebRequest;

	private object _httpWebRequestLocker = new object();

	internal static readonly string[] LegacyRestApiReadOnlyCalls;

	private string _accessToken;

	private string _appId;

	private string _appSecret;

	private bool _isSecureConnection;

	private bool _useFacebookBeta;

	private Func<object, string> _serializeJson;

	private static Func<object, string> _defaultJsonSerializer;

	private Func<string, Type, object> _deserializeJson;

	private static Func<string, Type, object> _defaultJsonDeserializer;

	private Func<Uri, HttpWebRequestWrapper> _httpWebRequestFactory;

	private static Func<Uri, HttpWebRequestWrapper> _defaultHttpWebRequestFactory;

	/// <remarks>For unit testing</remarks>
	internal Func<string> Boundary { get; set; }

	/// <summary>
	/// Gets or sets the access token.
	/// </summary>
	public virtual string AccessToken
	{
		get
		{
			return _accessToken;
		}
		set
		{
			_accessToken = value;
		}
	}

	/// <summary>
	/// Gets or sets the app id.
	/// </summary>
	public virtual string AppId
	{
		get
		{
			return _appId;
		}
		set
		{
			_appId = value;
		}
	}

	/// <summary>
	/// Gets or sets the app secret.
	/// </summary>
	public virtual string AppSecret
	{
		get
		{
			return _appSecret;
		}
		set
		{
			_appSecret = value;
		}
	}

	/// <summary>
	/// Gets or sets the value indicating whether to add return_ssl_resource as default parameter in every request.
	/// </summary>
	public virtual bool IsSecureConnection
	{
		get
		{
			return _isSecureConnection;
		}
		set
		{
			_isSecureConnection = value;
		}
	}

	/// <summary>
	/// Gets or sets the value indicating whether to use Facebook beta.
	/// </summary>
	public virtual bool UseFacebookBeta
	{
		get
		{
			return _useFacebookBeta;
		}
		set
		{
			_useFacebookBeta = value;
		}
	}

	/// <summary>
	/// Serialize object to json.
	/// </summary>
	[EditorBrowsable(EditorBrowsableState.Never)]
	[Obsolete("Use SetJsonSerializers")]
	public virtual Func<object, string> SerializeJson
	{
		get
		{
			return _serializeJson ?? (_serializeJson = _defaultJsonSerializer);
		}
		set
		{
			_serializeJson = value;
		}
	}

	/// <summary>
	/// Deserialize json to object.
	/// </summary>
	[EditorBrowsable(EditorBrowsableState.Never)]
	[Obsolete("Use SetJsonSerializers")]
	public virtual Func<string, Type, object> DeserializeJson
	{
		get
		{
			return _deserializeJson;
		}
		set
		{
			_deserializeJson = value ?? (_deserializeJson = _defaultJsonDeserializer);
		}
	}

	/// <summary>
	/// Http web request factory.
	/// </summary>
	[EditorBrowsable(EditorBrowsableState.Never)]
	[Obsolete("Use SetHttpWebRequestFactory.")]
	public virtual Func<Uri, HttpWebRequestWrapper> HttpWebRequestFactory
	{
		get
		{
			return _httpWebRequestFactory;
		}
		set
		{
			_httpWebRequestFactory = value ?? (_httpWebRequestFactory = _defaultHttpWebRequestFactory);
		}
	}

	/// <summary>
	/// Event handler for get completion.
	/// </summary>
	public event EventHandler<FacebookApiEventArgs> GetCompleted;

	/// <summary>
	/// Event handler for post completion.
	/// </summary>
	public event EventHandler<FacebookApiEventArgs> PostCompleted;

	/// <summary>
	/// Event handler for delete completion.
	/// </summary>
	public event EventHandler<FacebookApiEventArgs> DeleteCompleted;

	/// <summary>
	/// Event handler for upload progress changed.
	/// </summary>
	public event EventHandler<FacebookUploadProgressChangedEventArgs> UploadProgressChanged;

	/// <summary>
	/// Event handler when http web request wrapper is created for async api only.
	/// (used internally by TPL for cancellation support)
	/// </summary>
	private event EventHandler<HttpWebRequestCreatedEventArgs> HttpWebRequestWrapperCreated;

	/// <summary>
	/// Cancels asynchronous requests.
	/// </summary>
	/// <remarks>
	/// Does not cancel requests created using XTaskAsync methods.
	/// </remarks>
	public virtual void CancelAsync()
	{
		lock (_httpWebRequestLocker)
		{
			if (_httpWebRequest != null)
			{
				_httpWebRequest.Abort();
			}
		}
	}

	/// <summary>
	/// Makes an asynchronous request to the Facebook server.
	/// </summary>
	/// <param name="httpMethod">Http method. (GET/POST/DELETE)</param>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <param name="resultType">The type of deserialize object into.</param>
	/// <param name="userState">The user state.</param>
	[EditorBrowsable(EditorBrowsableState.Never)]
	[Obsolete("Use ApiTaskAsync instead.")]
	protected virtual void ApiAsync(HttpMethod httpMethod, string path, object parameters, Type resultType, object userState)
	{
		IList<int> batchEtags = null;
		Stream input;
		bool containsEtag;
		HttpHelper httpHelper = PrepareRequest(httpMethod, path, parameters, resultType, out input, out containsEtag, out batchEtags);
		_httpWebRequest = httpHelper.HttpWebRequest;
		if (this.HttpWebRequestWrapperCreated != null)
		{
			this.HttpWebRequestWrapperCreated(this, new HttpWebRequestCreatedEventArgs(userState, httpHelper.HttpWebRequest));
		}
		EventHandler<FacebookUploadProgressChangedEventArgs> uploadProgressChanged = this.UploadProgressChanged;
		bool notifyUploadProgressChanged = uploadProgressChanged != null && httpHelper.HttpWebRequest.Method == "POST";
		httpHelper.OpenReadCompleted += delegate(object o, OpenReadCompletedEventArgs e)
		{
			FacebookApiEventArgs args;
			if (e.Cancelled)
			{
				args = new FacebookApiEventArgs(e.Error, cancelled: true, userState, null);
			}
			else if (e.Error == null)
			{
				string responseString = null;
				try
				{
					using (Stream stream = e.Result)
					{
						HttpWebResponseWrapper httpWebResponse = httpHelper.HttpWebResponse;
						if (httpWebResponse != null && httpWebResponse.StatusCode == HttpStatusCode.NotModified)
						{
							JsonObject jsonObject = new JsonObject();
							JsonObject jsonObject2 = new JsonObject();
							string[] allKeys = httpWebResponse.Headers.AllKeys;
							foreach (string text in allKeys)
							{
								jsonObject2[text] = httpWebResponse.Headers[text];
							}
							jsonObject["headers"] = jsonObject2;
							args = new FacebookApiEventArgs(null, cancelled: false, userState, jsonObject);
							OnCompleted(httpMethod, args);
							return;
						}
						using StreamReader streamReader = new StreamReader(stream);
						responseString = streamReader.ReadToEnd();
					}
					try
					{
						object result = ProcessResponse(httpHelper, responseString, resultType, containsEtag, batchEtags);
						args = new FacebookApiEventArgs(null, cancelled: false, userState, result);
					}
					catch (Exception error)
					{
						args = new FacebookApiEventArgs(error, cancelled: false, userState, null);
					}
				}
				catch (Exception error2)
				{
					args = (httpHelper.HttpWebRequest.IsCancelled ? new FacebookApiEventArgs(error2, cancelled: true, userState, null) : new FacebookApiEventArgs(error2, cancelled: false, userState, null));
				}
			}
			else if (!(e.Error is WebExceptionWrapper webExceptionWrapper))
			{
				args = new FacebookApiEventArgs(e.Error, httpHelper.HttpWebRequest.IsCancelled, userState, null);
			}
			else if (webExceptionWrapper.GetResponse() == null)
			{
				args = new FacebookApiEventArgs(webExceptionWrapper, cancelled: false, userState, null);
			}
			else
			{
				HttpWebResponseWrapper httpWebResponse2 = httpHelper.HttpWebResponse;
				if (httpWebResponse2.StatusCode != HttpStatusCode.NotModified)
				{
					httpHelper.OpenReadAsync();
					return;
				}
				JsonObject jsonObject3 = new JsonObject();
				JsonObject jsonObject4 = new JsonObject();
				string[] allKeys2 = httpWebResponse2.Headers.AllKeys;
				foreach (string text2 in allKeys2)
				{
					jsonObject4[text2] = httpWebResponse2.Headers[text2];
				}
				jsonObject3["headers"] = jsonObject4;
				args = new FacebookApiEventArgs(null, cancelled: false, userState, jsonObject3);
			}
			OnCompleted(httpMethod, args);
		};
		if (input == null)
		{
			httpHelper.OpenReadAsync();
			return;
		}
		httpHelper.OpenWriteCompleted += delegate(object o, OpenWriteCompletedEventArgs e)
		{
			FacebookApiEventArgs args;
			if (e.Cancelled)
			{
				input.Dispose();
				args = new FacebookApiEventArgs(e.Error, cancelled: true, userState, null);
			}
			else if (e.Error == null)
			{
				try
				{
					using (Stream stream = e.Result)
					{
						byte[] array = new byte[4096];
						if (notifyUploadProgressChanged)
						{
							long length = input.Length;
							long num = 0L;
							int num2;
							while ((num2 = input.Read(array, 0, array.Length)) != 0)
							{
								stream.Write(array, 0, num2);
								stream.Flush();
								num += num2;
								OnUploadProgressChanged(new FacebookUploadProgressChangedEventArgs(0L, 0L, num, length, (int)(num * 100 / length), userState));
							}
						}
						else
						{
							int num2;
							while ((num2 = input.Read(array, 0, array.Length)) != 0)
							{
								stream.Write(array, 0, num2);
								stream.Flush();
							}
						}
					}
					httpHelper.OpenReadAsync();
					return;
				}
				catch (Exception error)
				{
					args = new FacebookApiEventArgs(error, httpHelper.HttpWebRequest.IsCancelled, userState, null);
				}
				finally
				{
					input.Dispose();
				}
			}
			else
			{
				input.Dispose();
				if (e.Error is WebExceptionWrapper webExceptionWrapper)
				{
					WebExceptionWrapper webExceptionWrapper2 = webExceptionWrapper;
					if (webExceptionWrapper2.GetResponse() != null)
					{
						httpHelper.OpenReadAsync();
						return;
					}
				}
				args = new FacebookApiEventArgs(e.Error, cancelled: false, userState, null);
			}
			OnCompleted(httpMethod, args);
		};
		httpHelper.OpenWriteAsync();
	}

	/// <summary>
	/// Raise OnGetCompleted event handler.
	/// </summary>
	/// <param name="args">The <see cref="T:Facebook.FacebookApiEventArgs" />.</param>
	[Obsolete]
	protected virtual void OnGetCompleted(FacebookApiEventArgs args)
	{
		if (this.GetCompleted != null)
		{
			this.GetCompleted(this, args);
		}
	}

	/// <summary>
	/// Raise OnPostCompleted event handler.
	/// </summary>
	/// <param name="args">The <see cref="T:Facebook.FacebookApiEventArgs" />.</param>
	[Obsolete]
	protected virtual void OnPostCompleted(FacebookApiEventArgs args)
	{
		if (this.PostCompleted != null)
		{
			this.PostCompleted(this, args);
		}
	}

	/// <summary>
	/// Raise OnDeletedCompleted event handler.
	/// </summary>
	/// <param name="args">The <see cref="T:Facebook.FacebookApiEventArgs" />.</param>
	[Obsolete]
	protected virtual void OnDeleteCompleted(FacebookApiEventArgs args)
	{
		if (this.DeleteCompleted != null)
		{
			this.DeleteCompleted(this, args);
		}
	}

	/// <summary>
	/// Raise OnUploadProgressCompleted event handler.
	/// </summary>
	/// <param name="args">The <see cref="T:Facebook.FacebookApiEventArgs" />.</param>
	[Obsolete]
	protected void OnUploadProgressChanged(FacebookUploadProgressChangedEventArgs args)
	{
		if (this.UploadProgressChanged != null)
		{
			this.UploadProgressChanged(this, args);
		}
	}

	[Obsolete]
	private void OnCompleted(HttpMethod httpMethod, FacebookApiEventArgs args)
	{
		switch (httpMethod)
		{
		case HttpMethod.Get:
			OnGetCompleted(args);
			break;
		case HttpMethod.Post:
			OnPostCompleted(args);
			break;
		case HttpMethod.Delete:
			OnDeleteCompleted(args);
			break;
		default:
			throw new ArgumentOutOfRangeException("httpMethod");
		}
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <returns>The json result.</returns>
	[Obsolete("Use GetTaskAsync instead.")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	public virtual void GetAsync(string path)
	{
		GetAsync(path, null, null);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="parameters">The parameters</param>
	/// <returns>The json result.</returns>
	[EditorBrowsable(EditorBrowsableState.Never)]
	[Obsolete("Use GetTaskAsync instead.")]
	public virtual void GetAsync(object parameters)
	{
		GetAsync(null, parameters, null);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <returns>The json result.</returns>
	[EditorBrowsable(EditorBrowsableState.Never)]
	[Obsolete("Use GetTaskAsync instead.")]
	public virtual void GetAsync(string path, object parameters)
	{
		GetAsync(path, parameters, null);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <param name="userState">The user state.</param>
	/// <returns>The json result.</returns>
	[EditorBrowsable(EditorBrowsableState.Never)]
	[Obsolete("Use GetTaskAsync instead.")]
	public virtual void GetAsync(string path, object parameters, object userState)
	{
		GetAsync(path, parameters, userState, null);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <param name="userState">The user state.</param>
	/// <param name="resultType">The result type.</param>
	/// <returns>The json result.</returns>
	[Obsolete("Use GetTaskAsync instead.")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	public virtual void GetAsync(string path, object parameters, object userState, Type resultType)
	{
		ApiAsync(HttpMethod.Get, path, parameters, resultType, userState);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <param name="userState">The user state.</param>
	/// <typeparam name="TResult">The result type.</typeparam>
	[Obsolete("Use GetTaskAsync instead.")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	public virtual void GetAsync<TResult>(string path, object parameters, object userState)
	{
		ApiAsync(HttpMethod.Get, path, parameters, typeof(TResult), userState);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <typeparam name="TResult">The result type.</typeparam>
	[Obsolete("Use GetTaskAsync instead.")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	public virtual void GetAsync<TResult>(string path, object parameters)
	{
		GetAsync<TResult>(path, parameters, null);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <typeparam name="TResult">The result type.</typeparam>
	[EditorBrowsable(EditorBrowsableState.Never)]
	[Obsolete("Use GetTaskAsync instead.")]
	public virtual void GetAsync<TResult>(string path)
	{
		GetAsync<TResult>(path, null);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="parameters">The parameters</param>
	/// <typeparam name="TResult">The result type.</typeparam>
	[EditorBrowsable(EditorBrowsableState.Never)]
	[Obsolete("Use GetTaskAsync instead.")]
	public virtual void GetAsync<TResult>(object parameters)
	{
		GetAsync<TResult>(null, parameters);
	}

	/// <summary>
	/// Makes an asynchronous POST request to the Facebook server.
	/// </summary>
	/// <param name="parameters">The parameters</param>
	/// <returns>The json result.</returns>
	[EditorBrowsable(EditorBrowsableState.Never)]
	[Obsolete("Use PostTaskAsync instead.")]
	public virtual void PostAsync(object parameters)
	{
		PostAsync(null, parameters, null);
	}

	/// <summary>
	/// Makes an asynchronous POST request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <returns>The json result.</returns>
	[EditorBrowsable(EditorBrowsableState.Never)]
	[Obsolete("Use PostTaskAsync instead.")]
	public virtual void PostAsync(string path, object parameters)
	{
		PostAsync(path, parameters, null);
	}

	/// <summary>
	/// Makes an asynchronous POST request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <param name="userState">The user state.</param>
	/// <returns>The json result.</returns>
	[Obsolete("Use PostTaskAsync instead.")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	public virtual void PostAsync(string path, object parameters, object userState)
	{
		ApiAsync(HttpMethod.Post, path, parameters, null, userState);
	}

	/// <summary>
	/// Makes an asynchronous DELETE request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <returns>The json result.</returns>
	[Obsolete("Use DeleteTaskAsync instead.")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	public virtual void DeleteAsync(string path)
	{
		DeleteAsync(path, null, null);
	}

	/// <summary>
	/// Makes an asynchronous DELETE request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <param name="userState">The user state.</param>
	/// <returns>The json result.</returns>
	[EditorBrowsable(EditorBrowsableState.Never)]
	[Obsolete("Use DeleteTaskAsync instead.")]
	public virtual void DeleteAsync(string path, object parameters, object userState)
	{
		ApiAsync(HttpMethod.Delete, path, parameters, null, userState);
	}

	/// <returns>The task of json result with headers.</returns>
	protected virtual Task<object> ApiTaskAsync(HttpMethod httpMethod, string path, object parameters, Type resultType, object userState, CancellationToken cancellationToken, IProgress<FacebookUploadProgressChangedEventArgs> uploadProgress)
	{
		TaskCompletionSource<object> tcs = new TaskCompletionSource<object>(userState);
		if (cancellationToken.IsCancellationRequested)
		{
			tcs.TrySetCanceled();
			return tcs.Task;
		}
		HttpWebRequestWrapper httpWebRequest = null;
		EventHandler<HttpWebRequestCreatedEventArgs> httpWebRequestCreatedHandler = null;
		httpWebRequestCreatedHandler = (EventHandler<HttpWebRequestCreatedEventArgs>)Delegate.Combine(httpWebRequestCreatedHandler, (EventHandler<HttpWebRequestCreatedEventArgs>)delegate(object o, HttpWebRequestCreatedEventArgs e)
		{
			if (e.UserState == tcs)
			{
				httpWebRequest = e.HttpWebRequest;
			}
		});
		CancellationTokenRegistration ctr = cancellationToken.Register(delegate
		{
			try
			{
				if (httpWebRequest != null)
				{
					httpWebRequest.Abort();
				}
			}
			catch
			{
			}
		});
		EventHandler<FacebookUploadProgressChangedEventArgs> uploadProgressHandler = null;
		if (uploadProgress != null)
		{
			uploadProgressHandler = delegate(object sender, FacebookUploadProgressChangedEventArgs e)
			{
				if (e.UserState == tcs)
				{
					uploadProgress.Report(new FacebookUploadProgressChangedEventArgs(e.BytesReceived, e.TotalBytesToReceive, e.BytesSent, e.TotalBytesToSend, e.ProgressPercentage, userState));
				}
			};
			UploadProgressChanged += uploadProgressHandler;
		}
		EventHandler<FacebookApiEventArgs> handler = null;
		handler = delegate(object sender, FacebookApiEventArgs e)
		{
			TransferCompletionToTask(tcs, e, e.GetResultData, delegate
			{
				ctr.Dispose();
				RemoveTaskAsyncHandlers(httpMethod, handler);
				HttpWebRequestWrapperCreated -= httpWebRequestCreatedHandler;
				if (uploadProgressHandler != null)
				{
					UploadProgressChanged -= uploadProgressHandler;
				}
			});
		};
		if (httpMethod == HttpMethod.Get)
		{
			GetCompleted += handler;
		}
		else if (httpMethod == HttpMethod.Post)
		{
			PostCompleted += handler;
		}
		else
		{
			if (httpMethod != HttpMethod.Delete)
			{
				throw new ArgumentOutOfRangeException("httpMethod");
			}
			DeleteCompleted += handler;
		}
		HttpWebRequestWrapperCreated += httpWebRequestCreatedHandler;
		try
		{
			ApiAsync(httpMethod, path, parameters, resultType, tcs);
		}
		catch
		{
			RemoveTaskAsyncHandlers(httpMethod, handler);
			HttpWebRequestWrapperCreated -= httpWebRequestCreatedHandler;
			if (uploadProgressHandler != null)
			{
				UploadProgressChanged -= uploadProgressHandler;
			}
			throw;
		}
		return tcs.Task;
	}

	/// <summary>
	/// Makes an asynchronous request to the Facebook server.
	/// </summary>
	/// <param name="httpMethod">Http method. (GET/POST/DELETE)</param>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <param name="resultType">The type of deserialize object into.</param>
	/// <param name="userState">The user state.</param>
	/// <param name="cancellationToken">The cancellation token.</param>
	/// <returns>The task of json result with headers.</returns>
	protected virtual Task<object> ApiTaskAsync(HttpMethod httpMethod, string path, object parameters, Type resultType, object userState, CancellationToken cancellationToken)
	{
		return ApiTaskAsync(httpMethod, path, parameters, resultType, userState, cancellationToken, null);
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

	private void RemoveTaskAsyncHandlers(HttpMethod httpMethod, EventHandler<FacebookApiEventArgs> handler)
	{
		switch (httpMethod)
		{
		case HttpMethod.Get:
			GetCompleted -= handler;
			break;
		case HttpMethod.Post:
			PostCompleted -= handler;
			break;
		case HttpMethod.Delete:
			DeleteCompleted -= handler;
			break;
		}
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <returns>The json result task.</returns>
	public virtual Task<object> GetTaskAsync(string path)
	{
		return GetTaskAsync(path, null, CancellationToken.None);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="parameters">The parameters</param>
	/// <returns>The json result task.</returns>
	public virtual Task<object> GetTaskAsync(object parameters)
	{
		return GetTaskAsync(null, parameters, CancellationToken.None);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <returns>The json result task.</returns>
	public virtual Task<object> GetTaskAsync(string path, object parameters)
	{
		return GetTaskAsync(path, parameters, CancellationToken.None);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <param name="cancellationToken">The cancellation token.</param>
	/// <returns>The json result task.</returns>
	public virtual Task<object> GetTaskAsync(string path, object parameters, CancellationToken cancellationToken)
	{
		return GetTaskAsync(path, parameters, cancellationToken, null);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <param name="cancellationToken">The cancellation token.</param>
	/// <param name="resultType">The result type.</param>
	/// <returns>The json result task.</returns>
	public virtual Task<object> GetTaskAsync(string path, object parameters, CancellationToken cancellationToken, Type resultType)
	{
		return ApiTaskAsync(HttpMethod.Get, path, parameters, resultType, null, cancellationToken);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <param name="cancellationToken">The cancellation token.</param>
	/// <typeparam name="TResult">The result type.</typeparam>
	/// <returns>The json result task.</returns>
	public virtual Task<TResult> GetTaskAsync<TResult>(string path, object parameters, CancellationToken cancellationToken)
	{
		return GetTaskAsync(path, parameters, cancellationToken, typeof(TResult)).Then((object result) => (TResult)result);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <typeparam name="TResult">The result type.</typeparam>
	/// <returns>The json result task.</returns>
	public virtual Task<TResult> GetTaskAsync<TResult>(string path, object parameters)
	{
		return GetTaskAsync<TResult>(path, parameters, CancellationToken.None);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="parameters">The parameters</param>
	/// <typeparam name="TResult">The result type.</typeparam>
	/// <returns>The json result task.</returns>
	public virtual Task<TResult> GetTaskAsync<TResult>(object parameters)
	{
		return GetTaskAsync<TResult>(null, parameters);
	}

	/// <summary>
	/// Makes an asynchronous GET request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <typeparam name="TResult">The result type.</typeparam>
	/// <returns>The json result task.</returns>
	public virtual Task<TResult> GetTaskAsync<TResult>(string path)
	{
		return GetTaskAsync<TResult>(path, null);
	}

	/// <summary>
	/// Makes an asynchronous POST request to the Facebook server.
	/// </summary>
	/// <param name="parameters">The parameters</param>
	/// <returns>The json result task.</returns>
	public virtual Task<object> PostTaskAsync(object parameters)
	{
		return PostTaskAsync(null, parameters, CancellationToken.None);
	}

	/// <summary>
	/// Makes an asynchronous POST request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <returns>The json result task.</returns>
	public virtual Task<object> PostTaskAsync(string path, object parameters)
	{
		return PostTaskAsync(path, parameters, CancellationToken.None);
	}

	/// <summary>
	/// Makes an asynchronous POST request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <param name="cancellationToken">The cancellation token.</param>
	/// <returns>The json result task.</returns>
	public virtual Task<object> PostTaskAsync(string path, object parameters, CancellationToken cancellationToken)
	{
		return ApiTaskAsync(HttpMethod.Post, path, parameters, null, null, cancellationToken);
	}

	/// <summary>
	/// Makes an asynchronous POST request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <param name="userState">The user state.</param>
	/// <param name="cancellationToken">The cancellation token.</param>
	/// <returns>The json result task.</returns>
	public virtual Task<object> PostTaskAsync(string path, object parameters, object userState, CancellationToken cancellationToken)
	{
		return ApiTaskAsync(HttpMethod.Post, path, parameters, null, userState, cancellationToken);
	}

	/// <returns>The json result task.</returns>
	public virtual Task<object> PostTaskAsync(string path, object parameters, object userState, CancellationToken cancellationToken, IProgress<FacebookUploadProgressChangedEventArgs> uploadProgress)
	{
		return ApiTaskAsync(HttpMethod.Post, path, parameters, null, userState, cancellationToken, uploadProgress);
	}

	/// <summary>
	/// Makes an asynchronous DELETE request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <returns>The json result task.</returns>
	public virtual Task<object> DeleteTaskAsync(string path)
	{
		return DeleteTaskAsync(path, null, CancellationToken.None);
	}

	/// <summary>
	/// Makes an asynchronous DELETE request to the Facebook server.
	/// </summary>
	/// <param name="path">The resource path or the resource url.</param>
	/// <param name="parameters">The parameters</param>
	/// <param name="cancellationToken">The cancellation token.</param>
	/// <returns>The json result task.</returns>
	public virtual Task<object> DeleteTaskAsync(string path, object parameters, CancellationToken cancellationToken)
	{
		return ApiTaskAsync(HttpMethod.Delete, path, parameters, null, null, cancellationToken);
	}

	/// <summary>
	/// Makes an asynchronous request to the Facebook server.
	/// </summary>
	/// <param name="batchParameters">The list of batch parameters.</param>
	/// <param name="userState">The user state.</param>
	/// <param name="parameters">The parameters.</param>
	[EditorBrowsable(EditorBrowsableState.Never)]
	[Obsolete("Use BatchTaskAsync instead.")]
	public virtual void BatchAsync(FacebookBatchParameter[] batchParameters, object userState, object parameters)
	{
		object parameters2 = PrepareBatchRequest(batchParameters, parameters);
		PostAsync(null, parameters2, userState);
	}

	/// <summary>
	/// Makes an asynchronous request to the Facebook server.
	/// </summary>
	/// <param name="batchParameters">The list of batch parameters.</param>
	/// <param name="userState">The user state.</param>
	[Obsolete("Use BatchTaskAsync instead.")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	public virtual void BatchAsync(FacebookBatchParameter[] batchParameters, object userState)
	{
		BatchAsync(batchParameters, userState, null);
	}

	/// <summary>
	/// Makes an asynchronous request to the Facebook server.
	/// </summary>
	/// <param name="batchParameters">The list of batch parameters.</param>
	[Obsolete("Use BatchTaskAsync instead.")]
	[EditorBrowsable(EditorBrowsableState.Never)]
	public virtual void BatchAsync(FacebookBatchParameter[] batchParameters)
	{
		BatchAsync(batchParameters, null);
	}

	internal object PrepareBatchRequest(FacebookBatchParameter[] batchParameters, object parameters)
	{
		if (batchParameters == null)
		{
			throw new ArgumentNullException("batchParameters");
		}
		if (batchParameters.Length == 0)
		{
			throw new ArgumentNullException("batchParameters", "At least one batch parameter is required");
		}
		IDictionary<string, object> dictionary = new Dictionary<string, object>();
		IList<object> list = (IList<object>)(dictionary["batch"] = new List<object>());
		IDictionary<string, FacebookMediaObject> mediaObjects;
		IDictionary<string, FacebookMediaStream> mediaStreams;
		foreach (FacebookBatchParameter facebookBatchParameter in batchParameters)
		{
			IDictionary<string, object> dictionary2 = ToDictionary(facebookBatchParameter.Data, out mediaObjects, out mediaStreams);
			if (mediaObjects.Count + mediaStreams.Count > 0)
			{
				throw new ArgumentException("Attachments (FacebookMediaObject/FacebookMediaStream) are only allowed in FacebookBatchParameter.Parameters");
			}
			if (dictionary2 == null)
			{
				dictionary2 = new Dictionary<string, object>();
			}
			if (!dictionary2.ContainsKey("method"))
			{
				switch (facebookBatchParameter.HttpMethod)
				{
				case HttpMethod.Get:
					dictionary2["method"] = "GET";
					break;
				case HttpMethod.Post:
					dictionary2["method"] = "POST";
					break;
				case HttpMethod.Delete:
					dictionary2["method"] = "DELETE";
					break;
				default:
					throw new ArgumentOutOfRangeException();
				}
			}
			IList<string> list3 = new List<string>();
			IDictionary<string, object> dictionary3 = ToDictionary(facebookBatchParameter.Parameters, out mediaObjects, out mediaStreams) ?? new Dictionary<string, object>();
			bool flag = false;
			string value = null;
			if (dictionary3.ContainsKey("_etag_"))
			{
				value = (string)dictionary3["_etag_"];
				flag = true;
				dictionary3.Remove("_etag_");
			}
			bool flag2 = false;
			foreach (KeyValuePair<string, FacebookMediaObject> item in mediaObjects)
			{
				if (flag2)
				{
					throw new ArgumentException("Only one attachement (FacebookMediaObject/FacebookMediaStream) allowed per FacebookBatchParamter.", "batchParameters");
				}
				if (dictionary.ContainsKey(item.Key))
				{
					throw new ArgumentException(string.Format(CultureInfo.InvariantCulture, "Attachment (FacebookMediaObject/FacebookMediaStream) with key '{0}' already exists", new object[1] { item.Key }));
				}
				list3.Add(HttpHelper.UrlEncode(item.Key));
				dictionary.Add(item.Key, item.Value);
				flag2 = true;
			}
			foreach (KeyValuePair<string, FacebookMediaStream> item2 in mediaStreams)
			{
				if (flag2)
				{
					throw new ArgumentException("Only one attachement (FacebookMediaObject/FacebookMediaStream) allowed per FacebookBatchParamter.", "batchParameters");
				}
				if (dictionary.ContainsKey(item2.Key))
				{
					throw new ArgumentException(string.Format(CultureInfo.InvariantCulture, "Attachment (FacebookMediaObject/FacebookMediaStream) with key '{0}' already exists", new object[1] { item2.Key }));
				}
				list3.Add(HttpHelper.UrlEncode(item2.Key));
				dictionary.Add(item2.Key, item2.Value);
				flag2 = true;
			}
			if (list3.Count > 0 && !dictionary2.ContainsKey("attached_files"))
			{
				dictionary2["attached_files"] = string.Join(",", list3.ToArray());
			}
			if (!dictionary2["method"].ToString().Equals("POST", StringComparison.OrdinalIgnoreCase))
			{
				if (!dictionary2.ContainsKey("relative_url"))
				{
					string value2 = ParseUrlQueryString(facebookBatchParameter.Path, dictionary3, forceParseAllUrls: false);
					SerializeParameters(dictionary3);
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.Append(value2).Append("?");
					foreach (KeyValuePair<string, object> item3 in dictionary3)
					{
						stringBuilder.AppendFormat("{0}={1}&", new object[2]
						{
							HttpHelper.UrlEncode(item3.Key),
							HttpHelper.UrlEncode(BuildHttpQuery(item3.Value, HttpHelper.UrlEncode))
						});
					}
					if (stringBuilder.Length > 0)
					{
						stringBuilder.Length--;
					}
					dictionary2["relative_url"] = stringBuilder.ToString();
				}
			}
			else
			{
				string value2 = ParseUrlQueryString(facebookBatchParameter.Path, dictionary3, forceParseAllUrls: false);
				SerializeParameters(dictionary3);
				if (!dictionary2.ContainsKey("relative_url") && value2.Length > 0)
				{
					dictionary2["relative_url"] = value2;
				}
				if (!dictionary2.ContainsKey("body"))
				{
					StringBuilder stringBuilder2 = new StringBuilder();
					foreach (KeyValuePair<string, object> item4 in dictionary3)
					{
						stringBuilder2.AppendFormat("{0}={1}&", new object[2]
						{
							HttpHelper.UrlEncode(item4.Key),
							HttpHelper.UrlEncode(BuildHttpQuery(item4.Value, HttpHelper.UrlEncode))
						});
					}
					if (stringBuilder2.Length > 0)
					{
						stringBuilder2.Length--;
						dictionary2["body"] = stringBuilder2.ToString();
					}
				}
			}
			if (flag)
			{
				dictionary2["_etag_"] = value;
			}
			list.Add(dictionary2);
		}
		IDictionary<string, object> dictionary4 = ToDictionary(parameters, out mediaObjects, out mediaStreams);
		if (dictionary4 != null)
		{
			foreach (KeyValuePair<string, object> item5 in dictionary4)
			{
				dictionary[item5.Key] = item5.Value;
			}
		}
		foreach (KeyValuePair<string, FacebookMediaObject> item6 in mediaObjects)
		{
			dictionary[item6.Key] = item6.Value;
		}
		foreach (KeyValuePair<string, FacebookMediaStream> item7 in mediaStreams)
		{
			dictionary[item7.Key] = item7.Value;
		}
		return dictionary;
	}

	internal object ProcessBatchResponse(object result, IList<int> batchEtags)
	{
		if (result == null)
		{
			return null;
		}
		JsonArray jsonArray = new JsonArray();
		IList<object> list = (IList<object>)result;
		int num = 0;
		foreach (object item2 in list)
		{
			if (item2 == null)
			{
				jsonArray.Add(null);
			}
			else
			{
				IDictionary<string, object> dictionary = (IDictionary<string, object>)item2;
				long num2 = Convert.ToInt64(dictionary["code"], CultureInfo.InvariantCulture);
				object obj = null;
				try
				{
					if (batchEtags != null && batchEtags.Contains(num))
					{
						JsonObject jsonObject = new JsonObject();
						IList<object> list2 = (IList<object>)dictionary["headers"];
						JsonObject jsonObject2 = new JsonObject();
						foreach (object item3 in list2)
						{
							IDictionary<string, object> dictionary2 = (IDictionary<string, object>)item3;
							jsonObject2[(string)dictionary2["name"]] = dictionary2["value"];
						}
						jsonObject["headers"] = jsonObject2;
						if (num2 != 304)
						{
							string responseString = (string)dictionary["body"];
							obj = (jsonObject["body"] = ProcessResponse(null, responseString, null, containsEtag: true, null));
						}
						obj = jsonObject;
					}
					else
					{
						string responseString2 = (string)dictionary["body"];
						obj = ProcessResponse(null, responseString2, null, containsEtag: false, null);
					}
					jsonArray.Add(obj);
				}
				catch (Exception item)
				{
					jsonArray.Add(item);
				}
			}
			num++;
		}
		return jsonArray;
	}

	/// <summary>
	/// Makes an asynchronous batch request to the Facebook server.
	/// </summary>
	/// <param name="batchParameters">
	/// List of batch parameters.
	/// </param>
	/// <returns>
	/// The json result task.
	/// </returns>
	public virtual Task<object> BatchTaskAsync(params FacebookBatchParameter[] batchParameters)
	{
		return BatchTaskAsync(batchParameters, null, CancellationToken.None);
	}

	/// <summary>
	/// Makes an asynchronous batch request to the Facebook server.
	/// </summary>
	/// <param name="batchParameters">
	/// List of batch parameters.
	/// </param>
	/// <param name="userState">
	/// The user state.
	/// </param>
	/// <param name="cancellationToken">
	/// The cancellation token.
	/// </param>
	/// <returns>
	/// The json result task.
	/// </returns>
	public virtual Task<object> BatchTaskAsync(FacebookBatchParameter[] batchParameters, object userState, CancellationToken cancellationToken, IProgress<FacebookUploadProgressChangedEventArgs> uploadProgress)
	{
		return BatchTaskAsync(batchParameters, userState, null, cancellationToken, uploadProgress);
	}

	/// <summary>
	/// Makes an asynchronous batch request to the Facebook server.
	/// </summary>
	/// <param name="batchParameters">
	/// List of batch parameters.
	/// </param>
	/// <param name="userState">
	/// The user state.
	/// </param>
	/// <param name="parameters">
	/// The parameters.
	/// </param>
	/// <param name="cancellationToken">
	/// The cancellation token.
	/// </param>
	/// <returns>
	/// The json result task.
	/// </returns>
	public virtual Task<object> BatchTaskAsync(FacebookBatchParameter[] batchParameters, object userState, object parameters, CancellationToken cancellationToken, IProgress<FacebookUploadProgressChangedEventArgs> uploadProgress)
	{
		object parameters2 = PrepareBatchRequest(batchParameters, parameters);
		return PostTaskAsync(null, parameters2, userState, cancellationToken, uploadProgress);
	}

	public virtual Task<object> BatchTaskAsync(FacebookBatchParameter[] batchParameters, object userToken, CancellationToken cancellationToken)
	{
		return BatchTaskAsync(batchParameters, userToken, null, cancellationToken);
	}

	public virtual Task<object> BatchTaskAsync(FacebookBatchParameter[] batchParameters, object userToken, object parameters, CancellationToken cancellationToken)
	{
		return BatchTaskAsync(batchParameters, userToken, parameters, cancellationToken, null);
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookClient" /> class.
	/// </summary>
	static FacebookClient()
	{
		LegacyRestApiReadOnlyCalls = new string[60]
		{
			"admin.getallocation", "admin.getappproperties", "admin.getbannedusers", "admin.getlivestreamvialink", "admin.getmetrics", "admin.getrestrictioninfo", "application.getpublicinfo", "auth.getapppublickey", "auth.getsession", "auth.getsignedpublicsessiondata",
			"comments.get", "connect.getunconnectedfriendscount", "dashboard.getactivity", "dashboard.getcount", "dashboard.getglobalnews", "dashboard.getnews", "dashboard.multigetcount", "dashboard.multigetnews", "data.getcookies", "events.get",
			"events.getmembers", "fbml.getcustomtags", "feed.getappfriendstories", "feed.getregisteredtemplatebundlebyid", "feed.getregisteredtemplatebundles", "fql.multiquery", "fql.query", "friends.arefriends", "friends.get", "friends.getappusers",
			"friends.getlists", "friends.getmutualfriends", "gifts.get", "groups.get", "groups.getmembers", "intl.gettranslations", "links.get", "notes.get", "notifications.get", "pages.getinfo",
			"pages.isadmin", "pages.isappadded", "pages.isfan", "permissions.checkavailableapiaccess", "permissions.checkgrantedapiaccess", "photos.get", "photos.getalbums", "photos.gettags", "profile.getinfo", "profile.getinfooptions",
			"stream.get", "stream.getcomments", "stream.getfilters", "users.getinfo", "users.getloggedinuser", "users.getstandardinfo", "users.hasapppermission", "users.isappuser", "users.isverified", "video.getuploadlimits"
		};
		SetDefaultJsonSerializers(null, null);
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookClient" /> class.
	/// </summary>
	public FacebookClient()
	{
		_deserializeJson = _defaultJsonDeserializer;
		_httpWebRequestFactory = _defaultHttpWebRequestFactory;
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookClient" /> class.
	/// </summary>
	/// <param name="accessToken">The facebook access_token.</param>
	/// <exception cref="T:System.ArgumentNullException">Access token in null or empty.</exception>
	public FacebookClient(string accessToken)
		: this()
	{
		if (string.IsNullOrEmpty(accessToken))
		{
			throw new ArgumentNullException("accessToken");
		}
		_accessToken = accessToken;
	}

	/// <summary>
	/// Sets the default json seriazliers and deserializers.
	/// </summary>
	/// <param name="jsonSerializer">Json serializer</param>
	/// <param name="jsonDeserializer">Json deserializer</param>
	public static void SetDefaultJsonSerializers(Func<object, string> jsonSerializer, Func<string, Type, object> jsonDeserializer)
	{
		_defaultJsonSerializer = jsonSerializer ?? new Func<object, string>(SimpleJson.SerializeObject);
		_defaultJsonDeserializer = jsonDeserializer ?? new Func<string, Type, object>(SimpleJson.DeserializeObject);
	}

	/// <summary>
	/// Sets the json seriazliers and deserializers for the current instance of <see cref="T:Facebook.FacebookClient" />.
	/// </summary>
	/// <param name="jsonSerializer">Json serializer</param>
	/// <param name="jsonDeserializer">Json deserializer</param>
	public virtual void SetJsonSerializers(Func<object, string> jsonSerializer, Func<string, Type, object> jsonDeserializer)
	{
		SerializeJson = jsonSerializer;
		DeserializeJson = jsonDeserializer;
	}

	/// <summary>
	/// Sets the default http web request factory.
	/// </summary>
	/// <param name="httpWebRequestFactory"></param>
	public static void SetDefaultHttpWebRequestFactory(Func<Uri, HttpWebRequestWrapper> httpWebRequestFactory)
	{
		_defaultHttpWebRequestFactory = httpWebRequestFactory;
	}

	/// <summary>
	/// Sets the http web request factory for the current instance of <see cref="T:Facebook.FacebookClient" />.
	/// </summary>
	/// <param name="httpWebRequestFactory"></param>
	public virtual void SetHttpWebRequestFactory(Func<Uri, HttpWebRequestWrapper> httpWebRequestFactory)
	{
		HttpWebRequestFactory = httpWebRequestFactory;
	}

	private HttpHelper PrepareRequest(HttpMethod httpMethod, string path, object parameters, Type resultType, out Stream input, out bool containsEtag, out IList<int> batchEtags)
	{
		input = null;
		containsEtag = false;
		batchEtags = null;
		IDictionary<string, FacebookMediaObject> mediaObjects;
		IDictionary<string, FacebookMediaStream> mediaStreams;
		IDictionary<string, object> dictionary = ToDictionary(parameters, out mediaObjects, out mediaStreams) ?? new Dictionary<string, object>();
		if (!dictionary.ContainsKey("access_token") && !string.IsNullOrEmpty(AccessToken))
		{
			dictionary["access_token"] = AccessToken;
		}
		if (!dictionary.ContainsKey("return_ssl_resources") && IsSecureConnection)
		{
			dictionary["return_ssl_resources"] = true;
		}
		string text = null;
		if (dictionary.ContainsKey("_etag_"))
		{
			text = (string)dictionary["_etag_"];
			dictionary.Remove("_etag_");
			containsEtag = true;
		}
		bool isLegacyRestApi = false;
		path = ParseUrlQueryString(path, dictionary, forceParseAllUrls: false, out var uri, out isLegacyRestApi);
		if (dictionary.ContainsKey("format"))
		{
			dictionary["format"] = "json-strings";
		}
		string text2 = null;
		if (dictionary.ContainsKey("method"))
		{
			text2 = (string)dictionary["method"];
			if (text2.Equals("DELETE", StringComparison.OrdinalIgnoreCase))
			{
				throw new ArgumentException("Parameter cannot contain method=delete. Use Delete or DeleteAsync or DeleteTaskAsync methods instead.", "parameters");
			}
			dictionary.Remove("method");
			isLegacyRestApi = true;
		}
		else if (isLegacyRestApi)
		{
			throw new ArgumentException("Parameters should contain rest 'method' name", "parameters");
		}
		UriBuilder uriBuilder2;
		if (uri == null)
		{
			UriBuilder uriBuilder = new UriBuilder();
			uriBuilder.Scheme = "https";
			uriBuilder2 = uriBuilder;
			if (isLegacyRestApi)
			{
				if (string.IsNullOrEmpty(text2))
				{
					throw new InvalidOperationException("Legacy rest api 'method' in parameters is null or empty.");
				}
				path = "method/" + text2;
				dictionary["format"] = "json-strings";
				if (text2.Equals("video.upload"))
				{
					uriBuilder2.Host = (UseFacebookBeta ? "api-video.beta.facebook.com" : "api-video.facebook.com");
				}
				else if (LegacyRestApiReadOnlyCalls.Contains(text2))
				{
					uriBuilder2.Host = (UseFacebookBeta ? "api-read.beta.facebook.com" : "api-read.facebook.com");
				}
				else
				{
					uriBuilder2.Host = (UseFacebookBeta ? "api.beta.facebook.com" : "api.facebook.com");
				}
			}
			else
			{
				if (dictionary.ContainsKey("batch") && (!dictionary.ContainsKey("_process_batch_response_") || (bool)dictionary["_process_batch_response_"]))
				{
					batchEtags = new List<int>();
					if (dictionary["batch"] is IList<object> list)
					{
						for (int i = 0; i < list.Count; i++)
						{
							if (!(list[i] is IDictionary<string, object> dictionary2))
							{
								continue;
							}
							IDictionary<string, object> dictionary3 = null;
							if (dictionary2.ContainsKey("headers"))
							{
								dictionary3 = (IDictionary<string, object>)dictionary2["headers"];
							}
							bool flag = dictionary2.ContainsKey("_etag_");
							if (flag)
							{
								if (string.IsNullOrEmpty((string)dictionary2["_etag_"]))
								{
									batchEtags.Add(i);
									dictionary2.Remove("_etag_");
									continue;
								}
								if (dictionary3 == null)
								{
									dictionary3 = (IDictionary<string, object>)(dictionary2["headers"] = new Dictionary<string, object>());
								}
							}
							if (flag)
							{
								if (!dictionary3.ContainsKey("If-None-Match"))
								{
									dictionary3["If-None-Match"] = string.Concat('"', dictionary2["_etag_"], '"');
								}
								dictionary2.Remove("_etag_");
								batchEtags.Add(i);
							}
							else if (dictionary3 != null && dictionary3.ContainsKey("If-None-Match"))
							{
								batchEtags.Add(i);
							}
						}
					}
				}
				path = path ?? string.Empty;
				if (httpMethod == HttpMethod.Post && path.EndsWith("/videos", StringComparison.OrdinalIgnoreCase))
				{
					uriBuilder2.Host = (UseFacebookBeta ? "graph-video.beta.facebook.com" : "graph-video.facebook.com");
				}
				else
				{
					uriBuilder2.Host = (UseFacebookBeta ? "graph.beta.facebook.com" : "graph.facebook.com");
				}
			}
		}
		else
		{
			UriBuilder uriBuilder3 = new UriBuilder();
			uriBuilder3.Host = uri.Host;
			uriBuilder3.Scheme = uri.Scheme;
			uriBuilder2 = uriBuilder3;
		}
		uriBuilder2.Path = path;
		string contentType = null;
		long? num = null;
		StringBuilder stringBuilder = new StringBuilder();
		SerializeParameters(dictionary);
		if (dictionary.ContainsKey("access_token"))
		{
			string text3 = dictionary["access_token"] as string;
			if (!string.IsNullOrEmpty(text3) && text3 != "null")
			{
				stringBuilder.AppendFormat("access_token={0}&", new object[1] { text3 });
			}
			dictionary.Remove("access_token");
		}
		if (httpMethod != HttpMethod.Post)
		{
			if (containsEtag && httpMethod != HttpMethod.Get)
			{
				throw new ArgumentException(string.Format(CultureInfo.InvariantCulture, "{0} is only supported for http get method.", new object[1] { "_etag_" }), "httpMethod");
			}
			if (mediaObjects.Count > 0 && mediaStreams.Count > 0)
			{
				throw new InvalidOperationException("Attachments (FacebookMediaObject/FacebookMediaStream) are valid only in POST requests.");
			}
			foreach (KeyValuePair<string, object> item in dictionary)
			{
				stringBuilder.AppendFormat("{0}={1}&", new object[2]
				{
					HttpHelper.UrlEncode(item.Key),
					HttpHelper.UrlEncode(BuildHttpQuery(item.Value, HttpHelper.UrlEncode))
				});
			}
		}
		else
		{
			if (mediaObjects.Count == 0 && mediaStreams.Count == 0)
			{
				contentType = "application/x-www-form-urlencoded";
				StringBuilder stringBuilder2 = new StringBuilder();
				foreach (KeyValuePair<string, object> item2 in dictionary)
				{
					stringBuilder2.AppendFormat("{0}={1}&", new object[2]
					{
						HttpHelper.UrlEncode(item2.Key),
						HttpHelper.UrlEncode(BuildHttpQuery(item2.Value, HttpHelper.UrlEncode))
					});
				}
				if (stringBuilder2.Length > 0)
				{
					stringBuilder2.Length--;
				}
				input = ((stringBuilder2.Length == 0) ? null : new MemoryStream(Encoding.UTF8.GetBytes(stringBuilder2.ToString())));
			}
			else
			{
				string text4 = ((Boundary == null) ? DateTime.UtcNow.Ticks.ToString("x", CultureInfo.InvariantCulture) : Boundary());
				contentType = "multipart/form-data; boundary=" + text4;
				List<Stream> list2 = new List<Stream>();
				List<int> list3 = new List<int>();
				StringBuilder stringBuilder3 = new StringBuilder();
				foreach (KeyValuePair<string, object> item3 in dictionary)
				{
					stringBuilder3.Append("--").Append(text4).Append("\r\n");
					stringBuilder3.Append("Content-Disposition: form-data; name=\"").Append(item3.Key).Append("\"");
					stringBuilder3.Append("\r\n").Append("\r\n");
					stringBuilder3.Append(BuildHttpQuery(item3.Value, HttpHelper.UrlEncode));
					stringBuilder3.Append("\r\n");
				}
				list3.Add(list2.Count);
				list2.Add(new MemoryStream(Encoding.UTF8.GetBytes(stringBuilder3.ToString())));
				foreach (KeyValuePair<string, FacebookMediaObject> item4 in mediaObjects)
				{
					StringBuilder stringBuilder4 = new StringBuilder();
					FacebookMediaObject value = item4.Value;
					if (value.ContentType == null || value.GetValue() == null || string.IsNullOrEmpty(value.FileName))
					{
						throw new InvalidOperationException("Attachment (FacebookMediaObject/FacebookMediaStream) must have a content type, file name, and value set.");
					}
					stringBuilder4.Append("--").Append(text4).Append("\r\n");
					stringBuilder4.Append("Content-Disposition: form-data; name=\"").Append(item4.Key).Append("\"; filename=\"")
						.Append(value.FileName)
						.Append("\"")
						.Append("\r\n");
					stringBuilder4.Append("Content-Type: ").Append(value.ContentType).Append("\r\n")
						.Append("\r\n");
					list3.Add(list2.Count);
					list2.Add(new MemoryStream(Encoding.UTF8.GetBytes(stringBuilder4.ToString())));
					byte[] value2 = value.GetValue();
					if (value2 == null)
					{
						throw new InvalidOperationException("The value of attachment (FacebookMediaObject/FacebookMediaStream) is null.");
					}
					list3.Add(list2.Count);
					list2.Add(new MemoryStream(value2));
					list3.Add(list2.Count);
					list2.Add(new MemoryStream(Encoding.UTF8.GetBytes("\r\n")));
				}
				foreach (KeyValuePair<string, FacebookMediaStream> item5 in mediaStreams)
				{
					StringBuilder stringBuilder5 = new StringBuilder();
					FacebookMediaStream value3 = item5.Value;
					if (value3.ContentType == null || value3.GetValue() == null || string.IsNullOrEmpty(value3.FileName))
					{
						throw new InvalidOperationException("Attachment (FacebookMediaObject/FacebookMediaStream) must have a content type, file name, and value set.");
					}
					stringBuilder5.Append("--").Append(text4).Append("\r\n");
					stringBuilder5.Append("Content-Disposition: form-data; name=\"").Append(item5.Key).Append("\"; filename=\"")
						.Append(value3.FileName)
						.Append("\"")
						.Append("\r\n");
					stringBuilder5.Append("Content-Type: ").Append(value3.ContentType).Append("\r\n")
						.Append("\r\n");
					list3.Add(list2.Count);
					list2.Add(new MemoryStream(Encoding.UTF8.GetBytes(stringBuilder5.ToString())));
					Stream value4 = value3.GetValue();
					if (value4 == null)
					{
						throw new InvalidOperationException("The value of attachment (FacebookMediaObject/FacebookMediaStream) is null.");
					}
					list2.Add(value4);
					list3.Add(list2.Count);
					list2.Add(new MemoryStream(Encoding.UTF8.GetBytes("\r\n")));
				}
				list3.Add(list2.Count);
				list2.Add(new MemoryStream(Encoding.UTF8.GetBytes("\r\n" + "--" + text4 + "--" + "\r\n")));
				input = new CombinationStream(list2, list3);
			}
			num = ((input == null) ? 0 : input.Length);
		}
		if (stringBuilder.Length > 0)
		{
			stringBuilder.Length--;
		}
		uriBuilder2.Query = stringBuilder.ToString();
		HttpWebRequestWrapper httpWebRequestWrapper = ((HttpWebRequestFactory == null) ? new HttpWebRequestWrapper((HttpWebRequest)WebRequest.Create(uriBuilder2.Uri)) : HttpWebRequestFactory(uriBuilder2.Uri));
		switch (httpMethod)
		{
		case HttpMethod.Get:
			httpWebRequestWrapper.Method = "GET";
			break;
		case HttpMethod.Delete:
			httpWebRequestWrapper.Method = "DELETE";
			httpWebRequestWrapper.TrySetContentLength(0L);
			break;
		case HttpMethod.Post:
			httpWebRequestWrapper.Method = "POST";
			break;
		default:
			throw new ArgumentOutOfRangeException("httpMethod");
		}
		httpWebRequestWrapper.ContentType = contentType;
		if (!string.IsNullOrEmpty(text))
		{
			httpWebRequestWrapper.Headers[HttpRequestHeader.IfNoneMatch] = '"' + text + '"';
		}
		if (num.HasValue)
		{
			httpWebRequestWrapper.TrySetContentLength(num.Value);
		}
		httpWebRequestWrapper.TrySetUserAgent("Facebook C# SDK");
		return new HttpHelper(httpWebRequestWrapper);
	}

	private object ProcessResponse(HttpHelper httpHelper, string responseString, Type resultType, bool containsEtag, IList<int> batchEtags)
	{
		try
		{
			object obj = null;
			Exception ex = null;
			if (httpHelper == null)
			{
				obj = DeserializeJson(responseString, resultType);
			}
			else
			{
				HttpWebResponseWrapper httpWebResponse = httpHelper.HttpWebResponse;
				if (httpWebResponse == null)
				{
					throw new InvalidOperationException("Unknown facebook response.");
				}
				if (!httpWebResponse.ContentType.Contains("text/javascript") && !httpWebResponse.ContentType.Contains("application/json"))
				{
					if (httpWebResponse.StatusCode == HttpStatusCode.OK && httpWebResponse.ContentType.Contains("text/plain"))
					{
						if (httpWebResponse.ResponseUri.AbsolutePath == "/oauth/access_token")
						{
							JsonObject jsonObject = new JsonObject();
							string[] array = responseString.Split('&');
							foreach (string text in array)
							{
								string[] array2 = text.Split('=');
								if (array2.Length == 2)
								{
									jsonObject[array2[0]] = array2[1];
								}
							}
							if (jsonObject.ContainsKey("expires"))
							{
								jsonObject["expires"] = Convert.ToInt64(jsonObject["expires"], CultureInfo.InvariantCulture);
							}
							return DeserializeJson(jsonObject.ToString(), resultType);
						}
						throw new InvalidOperationException("Unknown facebook response.");
					}
					throw new InvalidOperationException("Unknown facebook response.");
				}
				obj = DeserializeJson(responseString, null);
				ex = GetException(httpHelper, obj);
				if (ex == null && (object)resultType != null)
				{
					obj = DeserializeJson(responseString, resultType);
				}
			}
			if (ex == null)
			{
				if (containsEtag && httpHelper != null)
				{
					JsonObject jsonObject2 = new JsonObject();
					HttpWebResponseWrapper httpWebResponse2 = httpHelper.HttpWebResponse;
					JsonObject jsonObject3 = new JsonObject();
					string[] allKeys = httpWebResponse2.Headers.AllKeys;
					foreach (string text2 in allKeys)
					{
						jsonObject3[text2] = httpWebResponse2.Headers[text2];
					}
					jsonObject2["headers"] = jsonObject3;
					jsonObject2["body"] = obj;
					return jsonObject2;
				}
				return (batchEtags == null) ? obj : ProcessBatchResponse(obj, batchEtags);
			}
			throw ex;
		}
		catch (FacebookApiException)
		{
			throw;
		}
		catch (Exception)
		{
			if (httpHelper != null && httpHelper.InnerException != null)
			{
				throw httpHelper.InnerException;
			}
			throw;
		}
	}

	private void SerializeParameters(IDictionary<string, object> parameters)
	{
		List<string> list = new List<string>();
		foreach (string key in parameters.Keys)
		{
			if (!(parameters[key] is string))
			{
				list.Add(key);
			}
		}
		foreach (string item in list)
		{
			parameters[item] = SerializeJson(parameters[item]);
		}
	}

	internal static Exception GetException(HttpHelper httpHelper, object result)
	{
		if (result == null)
		{
			return null;
		}
		if (!(result is IDictionary<string, object> dictionary))
		{
			return null;
		}
		FacebookApiException result2 = null;
		if (httpHelper != null)
		{
			HttpWebResponseWrapper httpWebResponse = httpHelper.HttpWebResponse;
			Uri responseUri = httpWebResponse.ResponseUri;
			if (responseUri.Host == "api.facebook.com" || responseUri.Host == "api-read.facebook.com" || responseUri.Host == "api-video.facebook.com" || responseUri.Host == "api.beta.facebook.com" || responseUri.Host == "api-read.beta.facebook.com" || responseUri.Host == "api-video.facebook.com")
			{
				if (dictionary.ContainsKey("error_code"))
				{
					string text = dictionary["error_code"].ToString();
					string text2 = null;
					if (dictionary.ContainsKey("error_msg"))
					{
						text2 = dictionary["error_msg"] as string;
					}
					switch (text)
					{
					case "190":
						return new FacebookOAuthException(text2, text);
					default:
						if (text2 == null || !text2.Contains("request limit reached"))
						{
							break;
						}
						goto case "4";
					case "4":
					case "API_EC_TOO_MANY_CALLS":
						return new FacebookApiLimitException(text2, text);
					}
					return new FacebookApiException(text2, text);
				}
				return null;
			}
		}
		if (dictionary.ContainsKey("error"))
		{
			if (dictionary["error"] is IDictionary<string, object> dictionary2)
			{
				string text3 = dictionary2["type"] as string;
				string text4 = dictionary2["message"] as string;
				int result3 = 0;
				if (dictionary2.ContainsKey("code"))
				{
					int.TryParse(dictionary2["code"].ToString(), out result3);
				}
				int result4 = 0;
				if (dictionary2.ContainsKey("error_subcode"))
				{
					int.TryParse(dictionary2["error_subcode"].ToString(), out result4);
				}
				if (!string.IsNullOrEmpty(text3) && !string.IsNullOrEmpty(text4))
				{
					result2 = ((text3 == "OAuthException") ? new FacebookOAuthException(text4, text3, result3, result4) : ((!(text3 == "API_EC_TOO_MANY_CALLS") && !text4.Contains("request limit reached")) ? new FacebookApiException(text4, text3, result3, result4) : new FacebookApiLimitException(text4, text3)));
				}
			}
			else
			{
				long? num = null;
				if (dictionary["error"] is long)
				{
					num = (long)dictionary["error"];
				}
				if (!num.HasValue && dictionary["error"] is int)
				{
					num = (int)dictionary["error"];
				}
				string text5 = null;
				if (dictionary.ContainsKey("error_description"))
				{
					text5 = dictionary["error_description"] as string;
				}
				if (num.HasValue && !string.IsNullOrEmpty(text5))
				{
					result2 = ((num != 190) ? new FacebookApiException(text5, num.Value.ToString(CultureInfo.InvariantCulture)) : new FacebookOAuthException(text5, "API_EC_PARAM_ACCESS_TOKEN"));
				}
			}
		}
		return result2;
	}

	/// <summary>
	/// Converts the parameters to IDictionary&lt;string,object&gt;
	/// </summary>
	/// <param name="parameters">The parameter to convert.</param>
	/// <param name="mediaObjects">The extracted Facebook media objects.</param>
	/// <param name="mediaStreams">The extracted Facebook media streams.</param>
	/// <returns>The converted dictionary.</returns>
	private static IDictionary<string, object> ToDictionary(object parameters, out IDictionary<string, FacebookMediaObject> mediaObjects, out IDictionary<string, FacebookMediaStream> mediaStreams)
	{
		mediaObjects = new Dictionary<string, FacebookMediaObject>();
		mediaStreams = new Dictionary<string, FacebookMediaStream>();
		IDictionary<string, object> dictionary = parameters as IDictionary<string, object>;
		if (dictionary == null)
		{
			if (parameters == null)
			{
				return null;
			}
			dictionary = new Dictionary<string, object>();
			PropertyInfo[] properties = parameters.GetType().GetProperties();
			foreach (PropertyInfo propertyInfo in properties)
			{
				if (propertyInfo.CanRead)
				{
					dictionary[propertyInfo.Name] = propertyInfo.GetValue(parameters, null);
				}
			}
		}
		foreach (KeyValuePair<string, object> item in dictionary)
		{
			if (item.Value is FacebookMediaObject)
			{
				mediaObjects.Add(item.Key, (FacebookMediaObject)item.Value);
			}
			else if (item.Value is FacebookMediaStream)
			{
				mediaStreams.Add(item.Key, (FacebookMediaStream)item.Value);
			}
		}
		foreach (KeyValuePair<string, FacebookMediaObject> mediaObject in mediaObjects)
		{
			dictionary.Remove(mediaObject.Key);
		}
		foreach (KeyValuePair<string, FacebookMediaStream> mediaStream in mediaStreams)
		{
			dictionary.Remove(mediaStream.Key);
		}
		return dictionary;
	}

	/// <summary>
	/// Converts the parameters to http query.
	/// </summary>
	/// <param name="parameter">The parameter to convert.</param>
	/// <param name="encode">Url encoder function.</param>
	/// <returns>The http query.</returns>
	/// <remarks>
	/// The result is not url encoded. The caller needs to take care of url encoding the result.
	/// </remarks>
	private static string BuildHttpQuery(object parameter, Func<string, string> encode)
	{
		if (parameter == null)
		{
			return "null";
		}
		if (parameter is string)
		{
			return (string)parameter;
		}
		if (parameter is bool)
		{
			if (!(bool)parameter)
			{
				return "false";
			}
			return "true";
		}
		if (parameter is int || parameter is long || parameter is float || parameter is double || parameter is decimal || parameter is byte || parameter is sbyte || parameter is short || parameter is ushort || parameter is uint || parameter is ulong)
		{
			return parameter.ToString();
		}
		if (parameter is Uri)
		{
			return parameter.ToString();
		}
		StringBuilder stringBuilder = new StringBuilder();
		if (parameter is IEnumerable<KeyValuePair<string, object>>)
		{
			foreach (KeyValuePair<string, object> item in (IEnumerable<KeyValuePair<string, object>>)parameter)
			{
				stringBuilder.AppendFormat("{0}={1}&", new object[2]
				{
					encode(item.Key),
					encode(BuildHttpQuery(item.Value, encode))
				});
			}
		}
		else if (parameter is IEnumerable<KeyValuePair<string, string>>)
		{
			foreach (KeyValuePair<string, string> item2 in (IEnumerable<KeyValuePair<string, string>>)parameter)
			{
				stringBuilder.AppendFormat("{0}={1}&", new object[2]
				{
					encode(item2.Key),
					encode(item2.Value)
				});
			}
		}
		else
		{
			if (!(parameter is IEnumerable))
			{
				if (parameter is DateTime)
				{
					return DateTimeConvertor.ToIso8601FormattedDateTime((DateTime)parameter);
				}
				IDictionary<string, FacebookMediaObject> mediaObjects;
				IDictionary<string, FacebookMediaStream> mediaStreams;
				IDictionary<string, object> parameter2 = ToDictionary(parameter, out mediaObjects, out mediaStreams);
				if (mediaObjects.Count > 0 || mediaStreams.Count > 0)
				{
					throw new InvalidOperationException("Parameter can contain attachements (FacebookMediaObject/FacebookMediaStream) only in the top most level.");
				}
				return BuildHttpQuery(parameter2, encode);
			}
			foreach (object item3 in (IEnumerable)parameter)
			{
				stringBuilder.AppendFormat("{0},", new object[1] { encode(BuildHttpQuery(item3, encode)) });
			}
		}
		if (stringBuilder.Length > 0)
		{
			stringBuilder.Length--;
		}
		return stringBuilder.ToString();
	}

	private static string ParseUrlQueryString(string path, IDictionary<string, object> parameters, bool forceParseAllUrls, out Uri uri, out bool isLegacyRestApi)
	{
		if (parameters == null)
		{
			throw new ArgumentNullException("parameters");
		}
		isLegacyRestApi = false;
		uri = null;
		if (Uri.TryCreate(path, UriKind.Absolute, out uri))
		{
			if (forceParseAllUrls)
			{
				path = uri.AbsolutePath + uri.Query;
			}
			else
			{
				switch (uri.Host)
				{
				case "graph.facebook.com":
				case "graph-video.facebook.com":
				case "graph.beta.facebook.com":
				case "graph-video.beta.facebook.com":
					path = uri.AbsolutePath + uri.Query;
					break;
				case "api.facebook.com":
				case "api-read.facebook.com":
				case "api-video.facebook.com":
				case "api.beta.facebook.com":
				case "api-read.beta.facebook.com":
				case "api-video.beta.facebook.com":
					isLegacyRestApi = true;
					path = uri.AbsolutePath + uri.Query;
					break;
				default:
					uri = null;
					break;
				}
			}
		}
		if (string.IsNullOrEmpty(path))
		{
			return string.Empty;
		}
		if (path.Length > 0 && path[0] == '/')
		{
			path = path.Substring(1);
		}
		string[] array = path.Split('?');
		path = array[0];
		if (array.Length == 2 && array[1] != null)
		{
			string text = array[1];
			string[] array2 = text.Split('&');
			string[] array3 = array2;
			foreach (string text2 in array3)
			{
				if (!string.IsNullOrEmpty(text2))
				{
					string[] array4 = text2.Split('=');
					if (array4.Length != 2 || string.IsNullOrEmpty(array4[0]))
					{
						throw new ArgumentException("Invalid path", "path");
					}
					string key = HttpHelper.UrlDecode(array4[0]);
					if (!parameters.ContainsKey(key))
					{
						parameters[key] = HttpHelper.UrlDecode(array4[1]);
					}
				}
			}
		}
		else if (array.Length > 2)
		{
			throw new ArgumentException("Invalid path", "path");
		}
		return path;
	}

	internal static string ParseUrlQueryString(string path, IDictionary<string, object> parameters, bool forceParseAllUrls)
	{
		Uri uri;
		bool isLegacyRestApi;
		return ParseUrlQueryString(path, parameters, forceParseAllUrls, out uri, out isLegacyRestApi);
	}

	/// <summary>
	/// Tries parsing the facebook signed_request.
	/// </summary>
	/// <param name="appSecret">The app secret.</param>
	/// <param name="signedRequestValue">The signed_request value.</param>
	/// <param name="signedRequest">The parsed signed request.</param>
	/// <returns>True if signed request parsed successfully otherwise false.</returns>
	public virtual bool TryParseSignedRequest(string appSecret, string signedRequestValue, out object signedRequest)
	{
		signedRequest = null;
		try
		{
			signedRequest = ParseSignedRequest(appSecret, signedRequestValue);
			return true;
		}
		catch
		{
			return false;
		}
	}

	/// <summary>
	/// Tries parsing the facebook signed_request.
	/// </summary>
	/// <param name="signedRequestValue">The signed_request value.</param>
	/// <param name="signedRequest">The parsed signed request.</param>
	/// <returns>True if signed request parsed successfully otherwise false.</returns>
	public virtual bool TryParseSignedRequest(string signedRequestValue, out object signedRequest)
	{
		return TryParseSignedRequest(AppSecret, signedRequestValue, out signedRequest);
	}

	/// <summary>
	/// Parse the facebook signed_request.
	/// </summary>
	/// <param name="appSecret">The appsecret.</param>
	/// <param name="signedRequestValue">The signed_request value.</param>
	/// <returns>The parse signed_request value.</returns>
	/// <exception cref="T:System.ArgumentNullException">Throws if appSecret or signedRequestValue is null or empty.</exception>
	/// <exception cref="T:System.InvalidOperationException">If the signedRequestValue is an invalid signed_request.</exception>
	public virtual object ParseSignedRequest(string appSecret, string signedRequestValue)
	{
		if (string.IsNullOrEmpty(appSecret))
		{
			throw new ArgumentNullException("appSecret");
		}
		if (string.IsNullOrEmpty(signedRequestValue))
		{
			throw new ArgumentNullException("signedRequestValue");
		}
		string[] array = signedRequestValue.Split('.');
		if (array.Length != 2)
		{
			throw new InvalidOperationException("Invalid signed_request");
		}
		string text = array[0];
		string text2 = array[1];
		if (string.IsNullOrEmpty(text) || string.IsNullOrEmpty(text2))
		{
			throw new InvalidOperationException("Invalid signed_request");
		}
		byte[] array2 = Base64UrlDecode(text2);
		object result = DeserializeJson(Encoding.UTF8.GetString(array2, 0, array2.Length), null);
		byte[] bytes = Encoding.UTF8.GetBytes(appSecret);
		byte[] array3 = ComputeHmacSha256Hash(Encoding.UTF8.GetBytes(text2), bytes);
		byte[] array4 = Base64UrlDecode(text);
		if (array3.Length != array4.Length)
		{
			throw new InvalidOperationException("Invalid signed_request");
		}
		bool flag = true;
		for (int i = 0; i < array3.Length; i++)
		{
			flag &= array3[i] == array4[i];
		}
		if (!flag)
		{
			throw new InvalidOperationException("Invalid signed_request");
		}
		return result;
	}

	/// <summary>
	/// Parse the facebook signed_request.
	/// </summary>
	/// <param name="signedRequestValue">The signed_request value.</param>
	/// <returns>The parse signed_request value.</returns>
	/// <exception cref="T:System.ArgumentNullException">Throws if appSecret or signedRequestValue is null or empty.</exception>
	/// <exception cref="T:System.InvalidOperationException">If the signedRequestValue is an invalid signed_request.</exception>
	public virtual object ParseSignedRequest(string signedRequestValue)
	{
		return ParseSignedRequest(AppSecret, signedRequestValue);
	}

	/// <summary>
	/// Base64 Url decode.
	/// </summary>
	/// <param name="base64UrlSafeString">
	/// The base 64 url safe string.
	/// </param>
	/// <returns>
	/// The base 64 url decoded string.
	/// </returns>c
	private static byte[] Base64UrlDecode(string base64UrlSafeString)
	{
		if (string.IsNullOrEmpty(base64UrlSafeString))
		{
			throw new ArgumentNullException("base64UrlSafeString");
		}
		base64UrlSafeString = base64UrlSafeString.PadRight(base64UrlSafeString.Length + (4 - base64UrlSafeString.Length % 4) % 4, '=');
		base64UrlSafeString = base64UrlSafeString.Replace('-', '+').Replace('_', '/');
		return Convert.FromBase64String(base64UrlSafeString);
	}

	/// <summary>
	/// Computes the Hmac Sha 256 Hash.
	/// </summary>
	/// <param name="data">
	/// The data to hash.
	/// </param>
	/// <param name="key">
	/// The hash key.
	/// </param>
	/// <returns>
	/// The Hmac Sha 256 hash.
	/// </returns>
	private static byte[] ComputeHmacSha256Hash(byte[] data, byte[] key)
	{
		using HMACSHA256 hMACSHA = new HMACSHA256(key);
		return hMACSHA.ComputeHash(data);
	}

	/// <summary>
	/// Try parsing the url to <see cref="T:Facebook.FacebookOAuthResult" />.
	/// </summary>
	/// <param name="url">The url to parse</param>
	/// <param name="facebookOAuthResult">The facebook oauth result.</param>
	/// <returns>True if parse successful, otherwise false.</returns>
	public virtual bool TryParseOAuthCallbackUrl(Uri url, out FacebookOAuthResult facebookOAuthResult)
	{
		facebookOAuthResult = null;
		try
		{
			facebookOAuthResult = ParseOAuthCallbackUrl(url);
			return true;
		}
		catch
		{
			return false;
		}
	}

	/// <summary>
	/// Parse the url to <see cref="T:Facebook.FacebookOAuthResult" />.
	/// </summary>
	/// <param name="uri"></param>
	/// <returns></returns>
	/// <exception cref="T:System.NotImplementedException"></exception>
	public virtual FacebookOAuthResult ParseOAuthCallbackUrl(Uri uri)
	{
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		bool flag = false;
		if (!string.IsNullOrEmpty(uri.Fragment))
		{
			string text = uri.Fragment.Substring(1);
			ParseUrlQueryString("?" + text, dictionary, forceParseAllUrls: true);
			if (dictionary.ContainsKey("access_token"))
			{
				flag = true;
			}
		}
		Dictionary<string, object> dictionary2 = new Dictionary<string, object>();
		ParseUrlQueryString(uri.Query, dictionary2, forceParseAllUrls: true);
		if (dictionary2.ContainsKey("code") || (dictionary2.ContainsKey("error") && dictionary2.ContainsKey("error_description")))
		{
			flag = true;
		}
		foreach (KeyValuePair<string, object> item in dictionary2)
		{
			dictionary[item.Key] = item.Value;
		}
		if (flag)
		{
			return new FacebookOAuthResult(dictionary);
		}
		throw new InvalidOperationException("Could not parse Facebook OAuth url.");
	}

	/// <summary>
	/// Parses the dialog callback url to an object of the resulting data.
	/// </summary>
	/// <param name="uri"></param>
	/// <returns></returns>
	/// <exception cref="T:System.NotImplementedException"></exception>
	public virtual object ParseDialogCallbackUrl(Uri uri)
	{
		Dictionary<string, object> dictionary = new Dictionary<string, object>();
		ParseUrlQueryString(uri.Query, dictionary, forceParseAllUrls: true);
		string arg = SerializeJson(dictionary);
		return DeserializeJson(arg, null);
	}

	/// <summary>
	/// Gets the Facebook dialog url.
	/// </summary>
	/// <param name="dialog">
	/// The dialog name. Values can be oauth, feed, pagetab, friends, pay, apprequests, and send.
	/// </param>
	/// <param name="parameters">
	///  The parameters.
	/// </param>
	/// <returns>
	/// The dialog url.
	/// </returns>
	/// <exception cref="T:System.ArgumentNullException">
	/// If dialog or parameters is null.
	/// </exception>
	public virtual Uri GetDialogUrl(string dialog, object parameters)
	{
		if (string.IsNullOrEmpty(dialog))
		{
			throw new ArgumentNullException("dialog");
		}
		if (parameters == null)
		{
			throw new ArgumentNullException("parameters");
		}
		IDictionary<string, FacebookMediaObject> mediaObjects;
		IDictionary<string, FacebookMediaStream> mediaStreams;
		IDictionary<string, object> dictionary = ToDictionary(parameters, out mediaObjects, out mediaStreams) ?? new Dictionary<string, object>();
		bool flag = false;
		if (dictionary.ContainsKey("mobile"))
		{
			flag = (bool)dictionary["mobile"];
			dictionary.Remove("mobile");
		}
		if (dialog.Equals("oauth", StringComparison.OrdinalIgnoreCase) && !dictionary.ContainsKey("client_id") && !string.IsNullOrEmpty(AppId))
		{
			dictionary.Add("client_id", AppId);
		}
		if (!dialog.Equals("oauth", StringComparison.OrdinalIgnoreCase) && !dictionary.ContainsKey("app_id") && !string.IsNullOrEmpty(AppId))
		{
			dictionary.Add("app_id", AppId);
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.AppendFormat(flag ? "https://m.facebook.com/dialog/{0}?" : "https://www.facebook.com/dialog/{0}?", new object[1] { dialog });
		foreach (KeyValuePair<string, object> item in dictionary)
		{
			stringBuilder.AppendFormat("{0}={1}&", new object[2]
			{
				HttpHelper.UrlEncode(item.Key),
				HttpHelper.UrlEncode(BuildHttpQuery(item.Value, HttpHelper.UrlEncode))
			});
		}
		stringBuilder.Length--;
		return new Uri(stringBuilder.ToString());
	}

	/// <summary>
	/// Gets the Facebook OAuth login url.
	/// </summary>
	/// <param name="parameters">
	/// The parameters.
	/// </param>
	/// <returns>
	/// The login url.
	/// </returns>
	/// <exception cref="T:System.ArgumentNullException">
	/// If parameters is null.
	/// </exception>
	public virtual Uri GetLoginUrl(object parameters)
	{
		return GetDialogUrl("oauth", parameters);
	}

	/// <summary>
	/// Gets the Facebook OAuth logout url.
	/// </summary>
	/// <param name="parameters">
	/// The parameters.
	/// </param>
	/// <returns>
	/// The logout url.
	/// </returns>
	public virtual Uri GetLogoutUrl(object parameters)
	{
		if (parameters == null)
		{
			throw new ArgumentNullException("parameters");
		}
		IDictionary<string, FacebookMediaObject> mediaObjects;
		IDictionary<string, FacebookMediaStream> mediaStreams;
		IDictionary<string, object> dictionary = ToDictionary(parameters, out mediaObjects, out mediaStreams);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.Append("https://www.facebook.com/logout.php?");
		if (dictionary != null)
		{
			foreach (KeyValuePair<string, object> item in dictionary)
			{
				stringBuilder.AppendFormat("{0}={1}&", new object[2]
				{
					HttpHelper.UrlEncode(item.Key),
					HttpHelper.UrlEncode(BuildHttpQuery(item.Value, HttpHelper.UrlEncode))
				});
			}
		}
		stringBuilder.Length--;
		return new Uri(stringBuilder.ToString());
	}
}
