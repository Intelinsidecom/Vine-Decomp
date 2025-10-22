using System;
using System.ComponentModel;
using System.IO;

namespace Facebook;

/// <summary>
/// Open write completed event args.
/// </summary>
internal class OpenWriteCompletedEventArgs : AsyncCompletedEventArgs
{
	private readonly Stream _result;

	/// <summary>
	/// The request stream.
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
	/// Initializes a new instance of the <see cref="T:Facebook.OpenWriteCompletedEventArgs" /> class.
	/// </summary>
	/// <param name="result">The request stream.</param>
	/// <param name="error"></param>
	/// <param name="cancelled"></param>
	/// <param name="userState"></param>
	public OpenWriteCompletedEventArgs(Stream result, Exception error, bool cancelled, object userState)
		: base(error, cancelled, userState)
	{
		_result = result;
	}
}
