namespace Huyn.ZLib;

internal sealed class ZlibCodec
{
	public int AvailableBytesIn;

	public int AvailableBytesOut;

	public byte[] InputBuffer;

	public string Message;

	public int NextIn;

	public int NextOut;

	public byte[] OutputBuffer;

	public long TotalBytesIn;

	public long TotalBytesOut;

	public int WindowBits = 15;

	internal long _Adler32;

	internal InflateManager istate;

	public long Adler32 => _Adler32;

	public ZlibCodec()
	{
		if (InitializeInflate() != 0)
		{
			throw new ZlibException("Cannot initialize for inflate.");
		}
	}

	public int InitializeInflate()
	{
		return InitializeInflate(WindowBits);
	}

	public int InitializeInflate(bool expectRfc1950Header)
	{
		return InitializeInflate(WindowBits, expectRfc1950Header);
	}

	public int InitializeInflate(int windowBits)
	{
		WindowBits = windowBits;
		return InitializeInflate(windowBits, expectRfc1950Header: true);
	}

	public int InitializeInflate(int windowBits, bool expectRfc1950Header)
	{
		WindowBits = windowBits;
		istate = new InflateManager(expectRfc1950Header);
		return istate.Initialize(this, windowBits);
	}

	public int Inflate(FlushType flush)
	{
		if (istate == null)
		{
			throw new ZlibException("No Inflate State!");
		}
		return istate.Inflate(flush);
	}

	public int EndInflate()
	{
		if (istate == null)
		{
			throw new ZlibException("No Inflate State!");
		}
		int result = istate.End();
		istate = null;
		return result;
	}

	public int SyncInflate()
	{
		if (istate == null)
		{
			throw new ZlibException("No Inflate State!");
		}
		return istate.Sync();
	}

	public int SetDictionary(byte[] dictionary)
	{
		if (istate != null)
		{
			return istate.SetDictionary(dictionary);
		}
		throw new ZlibException("No Inflate state!");
	}
}
