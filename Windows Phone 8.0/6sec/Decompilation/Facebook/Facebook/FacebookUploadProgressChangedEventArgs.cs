using System.ComponentModel;

namespace Facebook;

/// <summary>
/// Represents Facebook api upload progress changed event args.
/// </summary>
public class FacebookUploadProgressChangedEventArgs : ProgressChangedEventArgs
{
	private readonly long _received;

	private readonly long _sent;

	private readonly long _totalRecived;

	private readonly long _totalSend;

	/// <summary>
	/// Bytes received.
	/// </summary>
	public long BytesReceived => _received;

	/// <summary>
	/// Total bytes to receive.
	/// </summary>
	public long TotalBytesToReceive => _totalRecived;

	/// <summary>
	/// Bytes sent.
	/// </summary>
	public long BytesSent => _sent;

	/// <summary>
	/// Total bytes to send.
	/// </summary>
	public long TotalBytesToSend => _totalSend;

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookUploadProgressChangedEventArgs" /> class.
	/// </summary>
	/// <param name="bytesReceived">Bytes received.</param>
	/// <param name="totalBytesToReceive">Total bytes to receive.</param>
	/// <param name="bytesSent">Bytes sent.</param>
	/// <param name="totalBytesToSend">Total bytes to send.</param>
	/// <param name="progressPercentage">Progress percentage.</param>
	/// <param name="userToken">User token.</param>
	public FacebookUploadProgressChangedEventArgs(long bytesReceived, long totalBytesToReceive, long bytesSent, long totalBytesToSend, int progressPercentage, object userToken)
		: base(progressPercentage, userToken)
	{
		_received = bytesReceived;
		_totalRecived = totalBytesToReceive;
		_sent = bytesSent;
		_totalSend = totalBytesToSend;
	}
}
