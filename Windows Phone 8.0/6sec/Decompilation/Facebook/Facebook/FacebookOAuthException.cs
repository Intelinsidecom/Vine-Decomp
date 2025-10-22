using System;

namespace Facebook;

/// <summary>
/// Represents errors that occur as a result of problems with the OAuth access token.
/// </summary>
public class FacebookOAuthException : FacebookApiException
{
	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookOAuthException" /> class.
	/// </summary>
	public FacebookOAuthException()
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookOAuthException" /> class.
	/// </summary>
	/// <param name="message">The message.</param>
	public FacebookOAuthException(string message)
		: base(message)
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookOAuthException" /> class.
	/// </summary>
	/// <param name="message">The message.</param>
	/// <param name="errorType">The error type.</param>
	public FacebookOAuthException(string message, string errorType)
		: base(message, errorType)
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookOAuthException" /> class.
	/// </summary>
	/// <param name="message">The message.</param>
	/// <param name="errorType">Type of the error.</param>
	/// <param name="errorCode">Code of the error.</param>
	public FacebookOAuthException(string message, string errorType, int errorCode)
		: base(message, errorType, errorCode)
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookOAuthException" /> class.
	/// </summary>
	/// <param name="message">The message.</param>
	/// <param name="errorType">Type of the error.</param>
	/// <param name="errorCode">Code of the error.</param>
	/// <param name="errorSubcode">Subcode of the error.</param>
	public FacebookOAuthException(string message, string errorType, int errorCode, int errorSubcode)
		: base(message, errorType, errorCode, errorSubcode)
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookOAuthException" /> class.
	/// </summary>
	/// <param name="message">The message.</param>
	/// <param name="innerException">The inner exception.</param>
	public FacebookOAuthException(string message, Exception innerException)
		: base(message, innerException)
	{
	}
}
