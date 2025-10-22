using System;
using System.Collections.Generic;
using System.Globalization;

namespace Facebook;

/// <summary>
/// Represents the authentication result of Facebook.
/// </summary>
public class FacebookOAuthResult
{
	/// <summary>
	/// The access token.
	/// </summary>
	private readonly string _accessToken;

	/// <summary>
	/// Date and Time when the access token expires.
	/// </summary>
	private readonly DateTime _expires;

	/// <summary>
	/// Error that happens when using OAuth2 protocol.
	/// </summary>
	private readonly string _error;

	/// <summary>
	/// Short error reason for failed authentication if there was an error.
	/// </summary>
	private readonly string _errorReason;

	/// <summary>
	/// Long error description for failed authentication if there was an error.
	/// </summary>
	private readonly string _errorDescription;

	/// <summary>
	/// The code used to exchange access token.
	/// </summary>
	private readonly string _code;

	/// <summary>
	/// Gets or sets an opaque state used to maintain application state between the request and callback.
	/// </summary>
	private readonly string _state;

	/// <summary>
	/// Error that happens when using OAuth2 protocol.
	/// </summary>
	/// <remarks>
	/// https://developers.facebook.com/docs/oauth/errors/
	/// </remarks>
	public virtual string Error => _error;

	/// <summary>
	/// Gets the short error reason for failed authentication if an error occurred.
	/// </summary>
	public virtual string ErrorReason => _errorReason;

	/// <summary>
	/// Gets the long error description for failed authentication if an error occurred.
	/// </summary>
	public virtual string ErrorDescription => _errorDescription;

	/// <summary>
	/// Gets the <see cref="T:System.DateTime" /> when the access token will expire.
	/// </summary>
	public virtual DateTime Expires => _expires;

	/// <summary>
	/// Gets the access token.
	/// </summary>
	public virtual string AccessToken => _accessToken;

	/// <summary>
	/// Gets a value indicating whether access token or code was successfully retrieved.
	/// </summary>
	public virtual bool IsSuccess
	{
		get
		{
			if (string.IsNullOrEmpty(Error))
			{
				if (string.IsNullOrEmpty(AccessToken))
				{
					return !string.IsNullOrEmpty(Code);
				}
				return true;
			}
			return false;
		}
	}

	/// <summary>
	/// Gets the code used to exchange with Facebook to retrieve access token.
	/// </summary>
	public virtual string Code => _code;

	/// <summary>
	/// Gets an opaque state used to maintain application state between the request and callback.
	/// </summary>
	public virtual string State => _state;

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookOAuthResult" /> class.
	/// </summary>
	protected FacebookOAuthResult()
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookOAuthResult" /> class.
	/// </summary>
	/// <param name="parameters">
	/// The parameters.
	/// </param>
	/// <remarks>
	/// The values of parameters should not be url encoded.
	/// </remarks>
	internal FacebookOAuthResult(IDictionary<string, object> parameters)
	{
		if (parameters == null)
		{
			throw new ArgumentNullException("parameters");
		}
		if (parameters.ContainsKey("state"))
		{
			_state = parameters["state"].ToString();
		}
		if (parameters.ContainsKey("error"))
		{
			_error = parameters["error"].ToString();
			if (parameters.ContainsKey("error_reason"))
			{
				_errorReason = parameters["error_reason"].ToString();
			}
			if (parameters.ContainsKey("error_description"))
			{
				_errorDescription = parameters["error_description"].ToString();
			}
			return;
		}
		if (parameters.ContainsKey("code"))
		{
			_code = parameters["code"].ToString();
		}
		if (parameters.ContainsKey("access_token"))
		{
			_accessToken = parameters["access_token"].ToString();
		}
		if (parameters.ContainsKey("expires_in"))
		{
			double num = Convert.ToDouble(parameters["expires_in"], CultureInfo.InvariantCulture);
			_expires = ((num > 0.0) ? DateTime.UtcNow.AddSeconds(num) : DateTime.MaxValue);
		}
	}
}
