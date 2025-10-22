using System;
using System.Globalization;

namespace Facebook;

/// <summary>
/// Represent errors that occur while calling a Facebook API.
/// </summary>
public class FacebookApiException : Exception
{
	/// <summary>
	/// Gets or sets the type of the error.
	/// </summary>
	/// <value>The type of the error.</value>
	public string ErrorType { get; set; }

	/// <summary>
	/// Gets or sets the code of the error.
	/// </summary>
	/// <value>The code of the error.</value>
	public int ErrorCode { get; set; }

	/// <summary>
	/// Gets or sets the error subcode.
	/// </summary>
	/// <value>The code of the error subcode.</value>
	public int ErrorSubcode { get; set; }

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookApiException" /> class.
	/// </summary>
	public FacebookApiException()
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookApiException" /> class.
	/// </summary>
	/// <param name="message">The message.</param>
	public FacebookApiException(string message)
		: base(message)
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookApiException" /> class.
	/// </summary>
	/// <param name="message">The message.</param>
	/// <param name="errorType">Type of the error.</param>
	public FacebookApiException(string message, string errorType)
		: this(string.Format(CultureInfo.InvariantCulture, "({0}) {1}", new object[2]
		{
			errorType ?? "Unknown",
			message
		}))
	{
		ErrorType = errorType;
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookApiException" /> class.
	/// </summary>
	/// <param name="message">The message.</param>
	/// <param name="errorType">Type of the error.</param>
	/// <param name="errorCode">Code of the error.</param>
	public FacebookApiException(string message, string errorType, int errorCode)
		: this(string.Format(CultureInfo.InvariantCulture, "({0} - #{1}) {2}", new object[3]
		{
			errorType ?? "Unknown",
			errorCode,
			message
		}))
	{
		ErrorType = errorType;
		ErrorCode = errorCode;
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookApiException" /> class.
	/// </summary>
	/// <param name="message">The message.</param>
	/// <param name="errorType">Type of the error.</param>
	/// <param name="errorCode">Code of the error.</param>
	/// <param name="errorSubcode">Subcode of the error.</param>
	public FacebookApiException(string message, string errorType, int errorCode, int errorSubcode)
		: this(message, errorType, errorCode)
	{
		ErrorSubcode = errorSubcode;
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookApiException" /> class.
	/// </summary>
	/// <param name="message">The message.</param>
	/// <param name="innerException">The inner exception.</param>
	public FacebookApiException(string message, Exception innerException)
		: base(message, innerException)
	{
	}
}
