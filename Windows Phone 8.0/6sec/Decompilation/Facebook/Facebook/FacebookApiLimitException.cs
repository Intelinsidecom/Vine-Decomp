using System;

namespace Facebook;

/// <summary>
/// Represents errors that occur as a result of problems with the OAuth access token.
/// </summary>
public class FacebookApiLimitException : FacebookApiException
{
	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookApiLimitException" /> class. 
	/// </summary>
	public FacebookApiLimitException()
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookApiLimitException" /> class. 
	/// </summary>
	/// <param name="message">
	/// The message.
	/// </param>
	public FacebookApiLimitException(string message)
		: base(message)
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookApiLimitException" /> class. 
	/// </summary>
	/// <param name="message">The message.</param>
	/// <param name="errorType">The error type.</param>
	public FacebookApiLimitException(string message, string errorType)
		: base(message, errorType)
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookApiLimitException" /> class. 
	/// </summary>
	/// <param name="message">
	/// The message.
	/// </param>
	/// <param name="innerException">
	/// The inner exception.
	/// </param>
	public FacebookApiLimitException(string message, Exception innerException)
		: base(message, innerException)
	{
	}
}
