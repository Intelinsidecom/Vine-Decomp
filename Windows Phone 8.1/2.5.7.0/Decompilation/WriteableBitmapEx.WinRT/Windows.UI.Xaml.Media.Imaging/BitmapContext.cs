using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.IO;
using System.Runtime.InteropServices.WindowsRuntime;

namespace Windows.UI.Xaml.Media.Imaging;

public struct BitmapContext : IDisposable
{
	private readonly WriteableBitmap writeableBitmap;

	private readonly ReadWriteMode mode;

	private static readonly IDictionary<WriteableBitmap, int> UpdateCountByBmp = new ConcurrentDictionary<WriteableBitmap, int>();

	private static readonly IDictionary<WriteableBitmap, int[]> PixelCacheByBmp = new ConcurrentDictionary<WriteableBitmap, int[]>();

	private int length;

	private int[] pixels;

	public WriteableBitmap WriteableBitmap => writeableBitmap;

	public int Width => ((BitmapSource)writeableBitmap).PixelWidth;

	public int Height => ((BitmapSource)writeableBitmap).PixelHeight;

	public int[] Pixels => pixels;

	public int Length => length;

	public BitmapContext(WriteableBitmap writeableBitmap)
		: this(writeableBitmap, ReadWriteMode.ReadWrite)
	{
	}

	public BitmapContext(WriteableBitmap writeableBitmap, ReadWriteMode mode)
	{
		this.writeableBitmap = writeableBitmap;
		this.mode = mode;
		if (!UpdateCountByBmp.ContainsKey(writeableBitmap))
		{
			UpdateCountByBmp.Add(writeableBitmap, 1);
			length = ((BitmapSource)writeableBitmap).PixelWidth * ((BitmapSource)writeableBitmap).PixelHeight;
			pixels = new int[length];
			CopyPixels();
			PixelCacheByBmp.Add(writeableBitmap, pixels);
		}
		else
		{
			IncrementRefCount(writeableBitmap);
			pixels = PixelCacheByBmp[writeableBitmap];
			length = pixels.Length;
		}
	}

	private unsafe void CopyPixels()
	{
		fixed (byte* ptr = writeableBitmap.PixelBuffer.ToArray())
		{
			fixed (int* ptr2 = pixels)
			{
				for (int i = 0; i < length; i++)
				{
					ptr2[i] = (ptr[i * 4 + 3] << 24) | (ptr[i * 4 + 2] << 16) | (ptr[i * 4 + 1] << 8) | ptr[i * 4];
				}
			}
		}
	}

	public static void BlockCopy(BitmapContext src, int srcOffset, BitmapContext dest, int destOffset, int count)
	{
		Buffer.BlockCopy(src.Pixels, srcOffset, dest.Pixels, destOffset, count);
	}

	public static void BlockCopy(Array src, int srcOffset, BitmapContext dest, int destOffset, int count)
	{
		Buffer.BlockCopy(src, srcOffset, dest.Pixels, destOffset, count);
	}

	public static void BlockCopy(BitmapContext src, int srcOffset, Array dest, int destOffset, int count)
	{
		Buffer.BlockCopy(src.Pixels, srcOffset, dest, destOffset, count);
	}

	public void Clear()
	{
		int[] array = Pixels;
		Array.Clear(array, 0, array.Length);
	}

	public unsafe void Dispose()
	{
		if (DecrementRefCount(writeableBitmap) != 0)
		{
			return;
		}
		UpdateCountByBmp.Remove(writeableBitmap);
		PixelCacheByBmp.Remove(writeableBitmap);
		if (mode != ReadWriteMode.ReadWrite)
		{
			return;
		}
		using (Stream stream = writeableBitmap.PixelBuffer.AsStream())
		{
			byte[] array = new byte[length * 4];
			fixed (int* ptr = pixels)
			{
				int num = 0;
				int num2 = 0;
				while (num2 < length)
				{
					int num3 = ptr[num2];
					array[num + 3] = (byte)((num3 >> 24) & 0xFF);
					array[num + 2] = (byte)((num3 >> 16) & 0xFF);
					array[num + 1] = (byte)((num3 >> 8) & 0xFF);
					array[num] = (byte)(num3 & 0xFF);
					num2++;
					num += 4;
				}
				stream.Write(array, 0, length * 4);
			}
		}
		writeableBitmap.Invalidate();
	}

	private static void IncrementRefCount(WriteableBitmap target)
	{
		UpdateCountByBmp[target]++;
	}

	private static int DecrementRefCount(WriteableBitmap target)
	{
		int num = UpdateCountByBmp[target];
		num--;
		UpdateCountByBmp[target] = num;
		return num;
	}
}
