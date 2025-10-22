using System.IO;

namespace Huyn.ZLib;

public class ZlibStream
{
	public static Stream UnCompress(Stream stream)
	{
		ZlibBaseStream zlibBaseStream = new ZlibBaseStream(stream, ZlibStreamFlavor.GZIP, leaveOpen: false);
		byte[] array = new byte[1024];
		MemoryStream memoryStream = new MemoryStream();
		int count;
		while ((count = zlibBaseStream.Read(array, 0, array.Length)) != 0)
		{
			memoryStream.Write(array, 0, count);
		}
		memoryStream.Position = 0L;
		return memoryStream;
	}
}
