using System;
using System.Collections.Generic;
using System.IO;
using System.Threading;

namespace Facebook;

internal class CombinationStream : Stream
{
	internal class CombinationStreamAsyncResult : IAsyncResult
	{
		private readonly object _asyncState;

		private readonly ManualResetEvent _manualResetEvent;

		public int BytesRead;

		public bool IsCompleted { get; internal set; }

		public WaitHandle AsyncWaitHandle => _manualResetEvent;

		public object AsyncState => _asyncState;

		public bool CompletedSynchronously { get; internal set; }

		public Exception Exception { get; internal set; }

		public CombinationStreamAsyncResult(object asyncState)
		{
			_asyncState = asyncState;
			_manualResetEvent = new ManualResetEvent(initialState: false);
		}

		internal void SetAsyncWaitHandle()
		{
			_manualResetEvent.Set();
		}
	}

	private readonly IList<Stream> _streams;

	private readonly IList<int> _streamsToDispose;

	private int _currentStreamIndex;

	private Stream _currentStream;

	private long _length = -1L;

	private long _postion;

	public IList<Stream> InternalStreams => _streams;

	public override bool CanRead => true;

	public override bool CanSeek => false;

	public override bool CanWrite => false;

	public override long Length
	{
		get
		{
			if (_length == -1)
			{
				_length = 0L;
				foreach (Stream stream in _streams)
				{
					_length += stream.Length;
				}
			}
			return _length;
		}
	}

	public override long Position
	{
		get
		{
			return _postion;
		}
		set
		{
			throw new NotImplementedException();
		}
	}

	public CombinationStream(IList<Stream> streams)
		: this(streams, null)
	{
	}

	public CombinationStream(IList<Stream> streams, IList<int> streamsToDispose)
	{
		if (streams == null)
		{
			throw new ArgumentNullException("streams");
		}
		_streams = streams;
		_streamsToDispose = streamsToDispose;
		if (streams.Count > 0)
		{
			_currentStream = streams[_currentStreamIndex++];
		}
	}

	public override void Flush()
	{
		if (_currentStream != null)
		{
			_currentStream.Flush();
		}
	}

	public override long Seek(long offset, SeekOrigin origin)
	{
		throw new InvalidOperationException("Stream is not seekable.");
	}

	public override void SetLength(long value)
	{
		_length = value;
	}

	public override int Read(byte[] buffer, int offset, int count)
	{
		int num = 0;
		int num2 = offset;
		while (count > 0)
		{
			int num3 = _currentStream.Read(buffer, num2, count);
			num += num3;
			num2 += num3;
			_postion += num3;
			if (num3 <= count)
			{
				count -= num3;
			}
			if (count > 0)
			{
				if (_currentStreamIndex >= _streams.Count)
				{
					break;
				}
				_currentStream = _streams[_currentStreamIndex++];
			}
		}
		return num;
	}

	public override IAsyncResult BeginRead(byte[] buffer, int offset, int count, AsyncCallback callback, object state)
	{
		CombinationStreamAsyncResult asyncResult = new CombinationStreamAsyncResult(state);
		if (count > 0)
		{
			AsyncCallback rc = null;
			rc = delegate(IAsyncResult readresult)
			{
				try
				{
					int num = _currentStream.EndRead(readresult);
					asyncResult.BytesRead += num;
					offset += num;
					_postion += num;
					if (num <= count)
					{
						count -= num;
					}
					if (count > 0)
					{
						if (_currentStreamIndex >= _streams.Count)
						{
							asyncResult.CompletedSynchronously = false;
							asyncResult.SetAsyncWaitHandle();
							asyncResult.IsCompleted = true;
							callback(asyncResult);
						}
						else
						{
							_currentStream = _streams[_currentStreamIndex++];
							_currentStream.BeginRead(buffer, offset, count, rc, readresult.AsyncState);
						}
					}
					else
					{
						asyncResult.CompletedSynchronously = false;
						asyncResult.SetAsyncWaitHandle();
						asyncResult.IsCompleted = true;
						callback(asyncResult);
					}
				}
				catch (Exception exception)
				{
					asyncResult.Exception = exception;
					asyncResult.CompletedSynchronously = false;
					asyncResult.SetAsyncWaitHandle();
					asyncResult.IsCompleted = true;
					callback(asyncResult);
				}
			};
			_currentStream.BeginRead(buffer, offset, count, rc, state);
		}
		else
		{
			asyncResult.CompletedSynchronously = true;
			asyncResult.SetAsyncWaitHandle();
			asyncResult.IsCompleted = true;
			callback(asyncResult);
		}
		return asyncResult;
	}

	public override int EndRead(IAsyncResult asyncResult)
	{
		asyncResult.AsyncWaitHandle.WaitOne();
		CombinationStreamAsyncResult combinationStreamAsyncResult = (CombinationStreamAsyncResult)asyncResult;
		if (combinationStreamAsyncResult.Exception != null)
		{
			throw combinationStreamAsyncResult.Exception;
		}
		return combinationStreamAsyncResult.BytesRead;
	}

	public override IAsyncResult BeginWrite(byte[] buffer, int offset, int count, AsyncCallback callback, object state)
	{
		throw new InvalidOperationException("Stream is not writable");
	}

	public override void Write(byte[] buffer, int offset, int count)
	{
		throw new InvalidOperationException("Stream is not writable");
	}

	protected override void Dispose(bool disposing)
	{
		base.Dispose(disposing);
		if (_streamsToDispose == null)
		{
			foreach (Stream internalStream in InternalStreams)
			{
				internalStream.Dispose();
			}
			return;
		}
		for (int i = 0; i < InternalStreams.Count; i++)
		{
			InternalStreams[i].Dispose();
		}
	}
}
