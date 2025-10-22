using System;
using System.Collections.Generic;
using Vine.Framework;
using Vine.Models;
using Windows.UI.Xaml.Controls;
using Windows.Web.Http;

namespace Vine.Web;

public class ApiResult<T> : ApiResult
{
	private static Dictionary<string, string> CustomErrorMessages = new Dictionary<string, string>
	{
		{
			"32",
			ResourceHelper.GetString("error_auth_username_pass")
		},
		{
			"187",
			ResourceHelper.GetString("TwitterDuplicatePost")
		},
		{
			"231",
			ResourceHelper.GetString("Twitter2FactorLoginError")
		}
	};

	public T Model { get; set; }

	public override bool HasError
	{
		get
		{
			if (base.Error == null && base.ErrorParsed == null && ((object)typeof(T) == typeof(string) || Model != null))
			{
				if (base.HttpResponse != null)
				{
					return !base.HttpResponse.IsSuccessStatusCode;
				}
				return false;
			}
			return true;
		}
	}

	public override string GetErrorMessage(string defaultMessage)
	{
		string text = GetErrorMessage();
		if (string.IsNullOrWhiteSpace(text))
		{
			text = defaultMessage;
		}
		return text;
	}

	private string GetErrorMessage()
	{
		string result = null;
		if (HasError)
		{
			TwitterException ex = base.Error as TwitterException;
			BaseVineResponseModel baseVineResponseModel = Model as BaseVineResponseModel;
			if (ex != null && ex.Errors != null && ex.Errors.Count > 0)
			{
				TwitterErrorModel twitterErrorModel = ex.Errors[0];
				if (twitterErrorModel.Code != null && CustomErrorMessages.ContainsKey(twitterErrorModel.Code))
				{
					result = CustomErrorMessages[twitterErrorModel.Code];
				}
				else if (!string.IsNullOrWhiteSpace(twitterErrorModel.Message))
				{
					result = twitterErrorModel.Message;
				}
			}
			else if (baseVineResponseModel != null)
			{
				if (baseVineResponseModel.Code != null && CustomErrorMessages.ContainsKey(baseVineResponseModel.Code))
				{
					result = CustomErrorMessages[baseVineResponseModel.Code];
				}
				else if (!string.IsNullOrWhiteSpace(baseVineResponseModel.Error))
				{
					result = baseVineResponseModel.Error;
				}
			}
		}
		return result;
	}
}
public abstract class ApiResult
{
	public abstract bool HasError { get; }

	public bool HasConnectivityError
	{
		get
		{
			Exception ex = ErrorParsed ?? Error;
			if (HasError && HttpResponse == null && ex != null && ex.Message != null)
			{
				return ex.Message.StartsWith("Exception from HRESULT: 0x8");
			}
			return false;
		}
	}

	public HttpResponseMessage HttpResponse { get; set; }

	public string RequestContent { get; set; }

	public string ResponseContent { get; set; }

	public Exception Error { get; set; }

	public Exception ErrorParsed { get; set; }

	public bool RequiresCaptcha { get; set; }

	public string XUploadKey
	{
		get
		{
			string result = null;
			if (HttpResponse != null && HttpResponse.Headers != null && ((IDictionary<string, string>)HttpResponse.Headers).TryGetValue("X-Upload-Key", out string value))
			{
				result = value;
			}
			return result;
		}
	}

	public string XDate
	{
		get
		{
			string result = null;
			if (HttpResponse != null && HttpResponse.Headers != null && ((IDictionary<string, string>)HttpResponse.Headers).TryGetValue("Date", out string value))
			{
				result = value;
			}
			return result;
		}
	}

	public abstract string GetErrorMessage(string defaultMessage);

	public void PopUpErrorIfExists()
	{
		if (HasError && !RequiresCaptcha)
		{
			if (ErrorParsed == null)
			{
				_ = Error;
			}
			_ = ((ContentControl)App.RootFrame).Content;
			MessageException ex = ErrorParsed as MessageException;
			if (HasConnectivityError)
			{
				ToastHelper.Show(ResourceHelper.GetString("vine"), ResourceHelper.GetString("error_auth_check_internet_connection"), "msgBoxOnTap");
			}
			else if (ex != null && ex.Code == 616)
			{
				string errorMessage = GetErrorMessage(ResourceHelper.GetString("VerificationPromptAlertMessageRequired"));
				ToastHelper.Show(ResourceHelper.GetString("vine"), errorMessage, "msgBoxOnTap");
			}
			else if (!(Error is OperationCanceledException) && !(ErrorParsed is CancellationException))
			{
				string errorMessage2 = GetErrorMessage(ResourceHelper.GetString("API_ERROR_UNKNOWN_ERROR"));
				ToastHelper.Show(ResourceHelper.GetString("vine"), errorMessage2, "msgBoxOnTap");
			}
		}
	}

	public void LogError()
	{
	}

	internal static ApiResult UnExpectedError(Exception ex = null)
	{
		return new ApiResult<string>
		{
			Error = ex,
			ErrorParsed = new Exception("unexpected error")
		};
	}
}
