using System;
using System.ComponentModel;
using System.IO;

namespace Facebook;

/// <summary>
/// Open read completed event args.
/// </summary>
internal class OpenReadCompletedEventArgs : AsyncCompletedEventArgs
{
	private readonly Stream _result;

	/// <summary>
	/// The response stream.
	/// </summary>
	public Stream Result
	{
		get
		{
			RaiseExceptionIfNecessary();
			return _result;
		}
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.OpenReadCompletedEventArgs" /> class.
	/// </summary>
	/// <param name="result">The response stream.</param>
	/// <param name="error">The exception.</param>
	/// <param name="cancelled"></param>
	/// <param name="userState"></param>
	public OpenReadCompletedEventArgs(Stream result, Exception error, bool cancelled, object userState)
		: base(error, cancelled, userState)
	{
		_result = result;
	}
}
