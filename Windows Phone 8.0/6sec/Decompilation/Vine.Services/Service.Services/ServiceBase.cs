using System;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Phone.Info;
using Newtonsoft.Json;
using Vine.Services;
using Vine.Services.Response;

namespace Service.Services;

public abstract class ServiceBase
{
	public delegate Task<T> ManageHttpResponse<T>(HttpResponseMessage message);

	public const string BASE_URL = "https://apivi.a1429.lol/";

	internal string _currentToken;

	private DateTimeOffset MinDate;

	private string deviceid;

	protected HttpClient _httpClient;

	protected HttpClientHandler _httpHandler;

	protected ServiceBase()
	{
		MinDate = new DateTimeOffset(2000, 1, 1, 1, 0, 0, TimeSpan.Zero);
		InitHttpClient();
	}

	public void InitHttpClient()
	{
		HttpClientHandler httpClientHandler = new HttpClientHandler
		{
			UseCookies = true,
			AllowAutoRedirect = false
		};
		if (httpClientHandler.SupportsAutomaticDecompression)
		{
			httpClientHandler.AutomaticDecompression = DecompressionMethods.GZip | DecompressionMethods.Deflate;
		}
		_httpHandler = httpClientHandler;
		HttpClient httpClient = new HttpClient(httpClientHandler)
		{
			BaseAddress = new Uri("https://apivi.a1429.lol/")
		};
		httpClient.DefaultRequestHeaders.AcceptEncoding.Add(new StringWithQualityHeaderValue("gzip"));
		httpClient.DefaultRequestHeaders.AcceptLanguage.Add(new StringWithQualityHeaderValue(CultureInfo.CurrentUICulture.Name));
		if (CultureInfo.CurrentUICulture.Name != "en-US")
		{
			httpClient.DefaultRequestHeaders.AcceptLanguage.Add(new StringWithQualityHeaderValue("en-US"));
		}
		httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
		httpClient.DefaultRequestHeaders.Add("X-Vine-Client", ServiceInfo.VineVersion);
		httpClient.DefaultRequestHeaders.CacheControl = CacheControlHeaderValue.Parse("no-cache");
		httpClient.DefaultRequestHeaders.UserAgent.ParseAdd(ServiceInfo.UserAgent);
		_httpClient = httpClient;
	}

	public static bool DetectFastAppResumeException(Exception error)
	{
		if (error is HttpRequestException)
		{
			error = error.InnerException;
		}
		if (error is WebException && ((WebException)error).Status == WebExceptionStatus.RequestCanceled)
		{
			return true;
		}
		if (error is NotSupportedException && ((NotSupportedException)error).Message == "Stream does not support writing.")
		{
			return true;
		}
		return false;
	}

	internal static bool DetectFastAppResumeResponse(HttpResponseMessage res)
	{
		return false;
	}

	internal static bool DetectFastAppResumeResponse(HttpWebResponse res)
	{
		return false;
	}

	public async Task<T> GetRequest<T>(Uri uri, ManageHttpResponse<T> manageResponse, Action manageRetryAction = null)
	{
		return await SendRequest(HttpMethod.Get, uri, null, manageResponse, manageRetryAction);
	}

	public async Task<T> SendRequest<T>(HttpMethod httpMethod, Uri uri, HttpContent content, ManageHttpResponse<T> manageResponse, Action manageRetryAction = null, bool isretry = false, bool ignoreTokenUnauthorized = false)
	{
		if (!CheckInternetConnection())
		{
			throw new ServiceServerErrorException(ServiceServerErrorType.NOHTTPCLIENT);
		}
		string tokenUsed = _currentToken;
		bool needRetry = false;
		if (httpMethod != HttpMethod.Get && content == null)
		{
			content = new StringContent("");
		}
		HttpRequestMessage httpRequestMessage = new HttpRequestMessage(httpMethod, uri)
		{
			Content = content
		};
		httpRequestMessage.Headers.IfModifiedSince = MinDate;
		httpRequestMessage.Headers.CacheControl = CacheControlHeaderValue.Parse("no-cache");
		HttpResponseMessage response = null;
		HttpStatusCode? statusCode = null;
		try
		{
			response = await _httpClient.SendAsync(httpRequestMessage);
			if (response.StatusCode == HttpStatusCode.NotFound)
			{
				bool flag = false;
				if (response.Content != null)
				{
					flag = (await response.Content.ReadAsStreamAsync()).Length > 0;
				}
				if (!flag)
				{
					throw new ServiceServerErrorException(ServiceServerErrorType.NOTCONNECTED);
				}
			}
			if (response.StatusCode == HttpStatusCode.ServiceUnavailable)
			{
				needRetry = true;
			}
			else
			{
				if (DetectFastAppResumeResponse(response))
				{
					throw new ServiceServerErrorException(ServiceServerErrorType.FASTAPPRESUME);
				}
				if (response.IsSuccessStatusCode && response.Content != null)
				{
					try
					{
						T val = await manageResponse(response);
						if (val != null)
						{
							return val;
						}
					}
					catch (ServiceServerErrorException ex)
					{
						throw ex;
					}
					catch (Exception)
					{
					}
				}
				if (response.StatusCode == HttpStatusCode.NotModified)
				{
					return default(T);
				}
				statusCode = response.StatusCode;
				if (response.Content.Headers.ContentType.MediaType == "application/json")
				{
					ErrorResponse errorResponse = JsonConvert.DeserializeObject<ErrorResponse>(await response.Content.ReadAsStringAsync());
					if (errorResponse.error == "419")
					{
						throw new ServiceServerErrorException(ServiceServerErrorType.CHECKPOINT)
						{
							HttpErrorMessage = null,
							Checkpoint = errorResponse.data
						};
					}
					if (response.StatusCode == HttpStatusCode.Unauthorized)
					{
						ManageUnauthorized(tokenUsed);
					}
					throw new ServiceServerErrorException((!ignoreTokenUnauthorized && response.StatusCode == HttpStatusCode.Unauthorized) ? ServiceServerErrorType.UNAUTHORIZED : ServiceServerErrorType.SERVER)
					{
						HttpErrorMessage = errorResponse.data,
						ErrorType = errorResponse.code
					};
				}
			}
			if (!ignoreTokenUnauthorized && response.StatusCode == HttpStatusCode.Unauthorized)
			{
				ManageUnauthorized(tokenUsed);
				throw new ServiceServerErrorException(ServiceServerErrorType.UNAUTHORIZED);
			}
		}
		catch (ServiceServerErrorException)
		{
			throw;
		}
		catch (HttpRequestException)
		{
			throw new ServiceServerErrorException(ServiceServerErrorType.NOTCONNECTED);
		}
		catch (Exception error)
		{
			if (!isretry || DetectFastAppResumeException(error))
			{
				needRetry = true;
			}
		}
		if (needRetry)
		{
			manageRetryAction?.Invoke();
			try
			{
				if (response != null && response.Headers.Contains("Retry-After"))
				{
					int result = 0;
					if (int.TryParse(response.Headers.GetValues("Retry-After").First(), out result))
					{
						await Task.Delay(result * 1000);
					}
				}
			}
			catch
			{
			}
			return await SendRequest(httpMethod, uri, content, manageResponse, manageRetryAction, isretry: true);
		}
		ServiceServerErrorException ex5 = new ServiceServerErrorException(ServiceServerErrorType.SERVER);
		if (statusCode.HasValue)
		{
			ex5.StatusCode = (int)statusCode.Value;
		}
		throw ex5;
	}

	protected abstract void ManageUnauthorized(string tokenUsed);

	private bool CheckInternetConnection()
	{
		return _httpClient != null;
	}

	public string GetDeviceId(string username)
	{
		if (deviceid == null)
		{
			byte[] array = null;
			object obj = default(object);
			if (DeviceExtendedProperties.TryGetValue("DeviceUniqueId", ref obj))
			{
				array = (byte[])obj;
				deviceid = BitConverter.ToString(array).Replace("-", "").ToLower();
			}
			else
			{
				deviceid = "";
			}
		}
		string text = BitConverter.ToString(new SHA1Managed().ComputeHash(new MemoryStream(Encoding.UTF8.GetBytes(username)))).Replace("-", "").ToLower();
		return "android-" + (text + deviceid).Substring(0, 16);
	}

	public string GetGuidFromUser(string username)
	{
		string text = BitConverter.ToString(new SHA1Managed().ComputeHash(new MemoryStream(Encoding.UTF8.GetBytes(username)))).Replace("-", "").ToLower();
		return (text.Substring(0, 10) + text.Substring(13, 22)).Insert(8, "-").Insert(13, "-").Insert(18, "-")
			.Insert(23, "-");
	}

	protected void ClearCookies()
	{
		InitHttpClient();
	}
}
