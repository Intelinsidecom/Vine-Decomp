using System;
using System.Collections.Generic;
using System.IO;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Windows.Foundation;
using Windows.Graphics.Imaging;
using Windows.Storage;
using Windows.Storage.Streams;

namespace Windows.UI.Xaml.Media.Imaging;

public static class WriteableBitmapExtensions
{
	public enum BlendMode
	{
		Alpha,
		Additive,
		Subtractive,
		Mask,
		Multiply,
		ColorKeying,
		None
	}

	public enum Interpolation
	{
		NearestNeighbor,
		Bilinear
	}

	public enum FlipMode
	{
		Vertical,
		Horizontal
	}

	internal const int SizeOfArgb = 4;

	private const float StepFactor = 2f;

	public static int[,] KernelGaussianBlur5x5 = new int[5, 5]
	{
		{ 1, 4, 7, 4, 1 },
		{ 4, 16, 26, 16, 4 },
		{ 7, 26, 41, 26, 7 },
		{ 4, 16, 26, 16, 4 },
		{ 1, 4, 7, 4, 1 }
	};

	public static int[,] KernelGaussianBlur3x3 = new int[3, 3]
	{
		{ 16, 26, 16 },
		{ 26, 41, 26 },
		{ 16, 26, 16 }
	};

	public static int[,] KernelSharpen3x3 = new int[3, 3]
	{
		{ 0, -2, 0 },
		{ -2, 11, -2 },
		{ 0, -2, 0 }
	};

	private static int ConvertColor(Color color)
	{
		int num = color.A + 1;
		return (color.A << 24) | ((byte)(color.R * num >> 8) << 16) | ((byte)(color.G * num >> 8) << 8) | (byte)(color.B * num >> 8);
	}

	public static void Clear(this WriteableBitmap bmp, Color color)
	{
		int num = ConvertColor(color);
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int[] pixels = bitmapContext.Pixels;
		int width = bitmapContext.Width;
		int height = bitmapContext.Height;
		int num2 = width * 4;
		for (int i = 0; i < width; i++)
		{
			pixels[i] = num;
		}
		int num3 = 1;
		int num4 = 1;
		while (num4 < height)
		{
			BitmapContext.BlockCopy(bitmapContext, 0, bitmapContext, num4 * num2, num3 * num2);
			num4 += num3;
			num3 = Math.Min(2 * num3, height - num4);
		}
	}

	public static void Clear(this WriteableBitmap bmp)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		bitmapContext.Clear();
	}

	public static WriteableBitmap Clone(this WriteableBitmap bmp)
	{
		using BitmapContext src = bmp.GetBitmapContext(ReadWriteMode.ReadOnly);
		WriteableBitmap val = BitmapFactory.New(src.Width, src.Height);
		using (BitmapContext dest = val.GetBitmapContext())
		{
			BitmapContext.BlockCopy(src, 0, dest, 0, src.Length * 4);
		}
		return val;
	}

	public static void ForEach(this WriteableBitmap bmp, Func<int, int, Color> func)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int[] pixels = bitmapContext.Pixels;
		int width = bitmapContext.Width;
		int height = bitmapContext.Height;
		int num = 0;
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				Color color = func(j, i);
				pixels[num++] = ConvertColor(color);
			}
		}
	}

	public static void ForEach(this WriteableBitmap bmp, Func<int, int, Color, Color> func)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int[] pixels = bitmapContext.Pixels;
		int width = bitmapContext.Width;
		int height = bitmapContext.Height;
		int num = 0;
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				int num2 = pixels[num];
				byte b = (byte)(num2 >> 24);
				int num3 = b;
				if (num3 == 0)
				{
					num3 = 1;
				}
				num3 = 65280 / num3;
				Color arg = Color.FromArgb(b, (byte)(((num2 >> 16) & 0xFF) * num3 >> 8), (byte)(((num2 >> 8) & 0xFF) * num3 >> 8), (byte)((num2 & 0xFF) * num3 >> 8));
				Color color = func(j, i, arg);
				pixels[num++] = ConvertColor(color);
			}
		}
	}

	public static int GetPixeli(this WriteableBitmap bmp, int x, int y)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		return bitmapContext.Pixels[y * bitmapContext.Width + x];
	}

	public static Color GetPixel(this WriteableBitmap bmp, int x, int y)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int num = bitmapContext.Pixels[y * bitmapContext.Width + x];
		byte b = (byte)(num >> 24);
		int num2 = b;
		if (num2 == 0)
		{
			num2 = 1;
		}
		num2 = 65280 / num2;
		return Color.FromArgb(b, (byte)(((num >> 16) & 0xFF) * num2 >> 8), (byte)(((num >> 8) & 0xFF) * num2 >> 8), (byte)((num & 0xFF) * num2 >> 8));
	}

	public static byte GetBrightness(this WriteableBitmap bmp, int x, int y)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext(ReadWriteMode.ReadOnly);
		int num = bitmapContext.Pixels[y * bitmapContext.Width + x];
		byte b = (byte)(num >> 16);
		byte b2 = (byte)(num >> 8);
		byte b3 = (byte)num;
		return (byte)(b * 6966 + b2 * 23436 + b3 * 2366 >> 15);
	}

	public static void SetPixeli(this WriteableBitmap bmp, int index, byte r, byte g, byte b)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		bitmapContext.Pixels[index] = -16777216 | (r << 16) | (g << 8) | b;
	}

	public static void SetPixel(this WriteableBitmap bmp, int x, int y, byte r, byte g, byte b)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		bitmapContext.Pixels[y * bitmapContext.Width + x] = -16777216 | (r << 16) | (g << 8) | b;
	}

	public static void SetPixeli(this WriteableBitmap bmp, int index, byte a, byte r, byte g, byte b)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		bitmapContext.Pixels[index] = (a << 24) | (r << 16) | (g << 8) | b;
	}

	public static void SetPixel(this WriteableBitmap bmp, int x, int y, byte a, byte r, byte g, byte b)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		bitmapContext.Pixels[y * bitmapContext.Width + x] = (a << 24) | (r << 16) | (g << 8) | b;
	}

	public static void SetPixeli(this WriteableBitmap bmp, int index, Color color)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		bitmapContext.Pixels[index] = ConvertColor(color);
	}

	public static void SetPixel(this WriteableBitmap bmp, int x, int y, Color color)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		bitmapContext.Pixels[y * bitmapContext.Width + x] = ConvertColor(color);
	}

	public static void SetPixeli(this WriteableBitmap bmp, int index, byte a, Color color)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int num = a + 1;
		bitmapContext.Pixels[index] = (a << 24) | ((byte)(color.R * num >> 8) << 16) | ((byte)(color.G * num >> 8) << 8) | (byte)(color.B * num >> 8);
	}

	public static void SetPixel(this WriteableBitmap bmp, int x, int y, byte a, Color color)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int num = a + 1;
		bitmapContext.Pixels[y * bitmapContext.Width + x] = (a << 24) | ((byte)(color.R * num >> 8) << 16) | ((byte)(color.G * num >> 8) << 8) | (byte)(color.B * num >> 8);
	}

	public static void SetPixeli(this WriteableBitmap bmp, int index, int color)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		bitmapContext.Pixels[index] = color;
	}

	public static void SetPixel(this WriteableBitmap bmp, int x, int y, int color)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		bitmapContext.Pixels[y * bitmapContext.Width + x] = color;
	}

	public static void Blit(this WriteableBitmap bmp, Rect destRect, WriteableBitmap source, Rect sourceRect, BlendMode BlendMode)
	{
		bmp.Blit(destRect, source, sourceRect, Colors.White, BlendMode);
	}

	public static void Blit(this WriteableBitmap bmp, Rect destRect, WriteableBitmap source, Rect sourceRect)
	{
		bmp.Blit(destRect, source, sourceRect, Colors.White, BlendMode.Alpha);
	}

	public static void Blit(this WriteableBitmap bmp, Point destPosition, WriteableBitmap source, Rect sourceRect, Color color, BlendMode BlendMode)
	{
		Rect destRect = new Rect(destPosition, new Size(sourceRect.Width, sourceRect.Height));
		bmp.Blit(destRect, source, sourceRect, color, BlendMode);
	}

	public static void Blit(this WriteableBitmap bmp, Rect destRect, WriteableBitmap source, Rect sourceRect, Color color, BlendMode BlendMode)
	{
		if (color.A == 0)
		{
			return;
		}
		int num = (int)destRect.Width;
		int num2 = (int)destRect.Height;
		using BitmapContext src = source.GetBitmapContext(ReadWriteMode.ReadOnly);
		using BitmapContext dest = bmp.GetBitmapContext();
		int width = src.Width;
		int width2 = dest.Width;
		int height = dest.Height;
		Rect rect = new Rect(0.0, 0.0, width2, height);
		rect.Intersect(destRect);
		if (rect.IsEmpty)
		{
			return;
		}
		int[] pixels = src.Pixels;
		int[] pixels2 = dest.Pixels;
		int length = src.Length;
		_ = dest.Length;
		int num3 = -1;
		int num4 = (int)destRect.X;
		int num5 = (int)destRect.Y;
		int num6 = 0;
		int num7 = 0;
		int num8 = 0;
		int num9 = 0;
		int a = color.A;
		int r = color.R;
		int g = color.G;
		int b = color.B;
		bool flag = color != Colors.White;
		int num10 = (int)sourceRect.Width;
		double num11 = sourceRect.Width / destRect.Width;
		double num12 = sourceRect.Height / destRect.Height;
		int num13 = (int)sourceRect.X;
		int num14 = (int)sourceRect.Y;
		int num15 = -1;
		int num16 = -1;
		double num17 = num14;
		int num18 = num5;
		for (int i = 0; i < num2; i++)
		{
			if (num18 >= 0 && num18 < height)
			{
				double num19 = num13;
				int num20 = num4 + num18 * width2;
				int num21 = num4;
				int num22 = pixels[0];
				if (BlendMode == BlendMode.None && !flag)
				{
					num3 = (int)num19 + (int)num17 * width;
					int num23 = ((num21 < 0) ? (-num21) : 0);
					int num24 = num21 + num23;
					int num25 = width - num23;
					int num26 = ((num24 + num25 < width2) ? num25 : (width2 - num24));
					if (num26 > num10)
					{
						num26 = num10;
					}
					if (num26 > num)
					{
						num26 = num;
					}
					BitmapContext.BlockCopy(src, (num3 + num23) * 4, dest, (num20 + num23) * 4, num26 * 4);
				}
				else
				{
					for (int j = 0; j < num; j++)
					{
						if (num21 >= 0 && num21 < width2)
						{
							if ((int)num19 != num15 || (int)num17 != num16)
							{
								num3 = (int)num19 + (int)num17 * width;
								if (num3 >= 0 && num3 < length)
								{
									num22 = pixels[num3];
									num9 = (num22 >> 24) & 0xFF;
									num6 = (num22 >> 16) & 0xFF;
									num7 = (num22 >> 8) & 0xFF;
									num8 = num22 & 0xFF;
									if (flag && num9 != 0)
									{
										num9 = num9 * a * 32897 >> 23;
										num6 = (num6 * r * 32897 >> 23) * a * 32897 >> 23;
										num7 = (num7 * g * 32897 >> 23) * a * 32897 >> 23;
										num8 = (num8 * b * 32897 >> 23) * a * 32897 >> 23;
										num22 = (num9 << 24) | (num6 << 16) | (num7 << 8) | num8;
									}
								}
								else
								{
									num9 = 0;
								}
							}
							switch (BlendMode)
							{
							case BlendMode.None:
								pixels2[num20] = num22;
								break;
							case BlendMode.ColorKeying:
								num6 = (num22 >> 16) & 0xFF;
								num7 = (num22 >> 8) & 0xFF;
								num8 = num22 & 0xFF;
								if (num6 != color.R || num7 != color.G || num8 != color.B)
								{
									pixels2[num20] = num22;
								}
								break;
							case BlendMode.Mask:
							{
								int num43 = pixels2[num20];
								int num28 = (num43 >> 24) & 0xFF;
								int num29 = (num43 >> 16) & 0xFF;
								int num30 = (num43 >> 8) & 0xFF;
								int num31 = num43 & 0xFF;
								num43 = (num28 * num9 * 32897 >> 23 << 24) | (num29 * num9 * 32897 >> 23 << 16) | (num30 * num9 * 32897 >> 23 << 8) | (num31 * num9 * 32897 >> 23);
								pixels2[num20] = num43;
								break;
							}
							default:
							{
								if (num9 <= 0)
								{
									break;
								}
								int num27 = pixels2[num20];
								int num28 = (num27 >> 24) & 0xFF;
								if ((num9 == 255 || num28 == 0) && BlendMode != BlendMode.Additive && BlendMode != BlendMode.Subtractive && BlendMode != BlendMode.Multiply)
								{
									pixels2[num20] = num22;
									break;
								}
								int num29 = (num27 >> 16) & 0xFF;
								int num30 = (num27 >> 8) & 0xFF;
								int num31 = num27 & 0xFF;
								switch (BlendMode)
								{
								case BlendMode.Alpha:
								{
									int num42 = 255 - num9;
									num27 = ((num28 & 0xFF) << 24) | (((num6 * num9 + num42 * num29 >> 8) & 0xFF) << 16) | (((num7 * num9 + num42 * num30 >> 8) & 0xFF) << 8) | ((num8 * num9 + num42 * num31 >> 8) & 0xFF);
									break;
								}
								case BlendMode.Additive:
								{
									int num41 = ((255 <= num9 + num28) ? 255 : (num9 + num28));
									num27 = (num41 << 24) | (((num41 <= num6 + num29) ? num41 : (num6 + num29)) << 16) | (((num41 <= num7 + num30) ? num41 : (num7 + num30)) << 8) | ((num41 <= num8 + num31) ? num41 : (num8 + num31));
									break;
								}
								case BlendMode.Subtractive:
								{
									int num40 = num28;
									num27 = (num40 << 24) | (((num6 < num29) ? (num6 - num29) : 0) << 16) | (((num7 < num30) ? (num7 - num30) : 0) << 8) | ((num8 < num31) ? (num8 - num31) : 0);
									break;
								}
								case BlendMode.Multiply:
								{
									int num32 = num9 * num28 + 128;
									int num33 = num6 * num29 + 128;
									int num34 = num7 * num30 + 128;
									int num35 = num8 * num31 + 128;
									int num36 = (num32 >> 8) + num32 >> 8;
									int num37 = (num33 >> 8) + num33 >> 8;
									int num38 = (num34 >> 8) + num34 >> 8;
									int num39 = (num35 >> 8) + num35 >> 8;
									num27 = (num36 << 24) | (((num36 <= num37) ? num36 : num37) << 16) | (((num36 <= num38) ? num36 : num38) << 8) | ((num36 <= num39) ? num36 : num39);
									break;
								}
								}
								pixels2[num20] = num27;
								break;
							}
							}
						}
						num21++;
						num20++;
						num19 += num11;
					}
				}
			}
			num17 += num12;
			num18++;
		}
	}

	public static byte[] ToByteArray(this WriteableBitmap bmp, int offset, int count)
	{
		using BitmapContext src = bmp.GetBitmapContext();
		if (count == -1)
		{
			count = src.Length;
		}
		int num = count * 4;
		byte[] array = new byte[num];
		BitmapContext.BlockCopy(src, offset, array, 0, num);
		return array;
	}

	public static byte[] ToByteArray(this WriteableBitmap bmp, int count)
	{
		return bmp.ToByteArray(0, count);
	}

	public static byte[] ToByteArray(this WriteableBitmap bmp)
	{
		return bmp.ToByteArray(0, -1);
	}

	public static WriteableBitmap FromByteArray(this WriteableBitmap bmp, byte[] buffer, int offset, int count)
	{
		using BitmapContext dest = bmp.GetBitmapContext();
		BitmapContext.BlockCopy(buffer, offset, dest, 0, count);
		return bmp;
	}

	public static WriteableBitmap FromByteArray(this WriteableBitmap bmp, byte[] buffer, int count)
	{
		return bmp.FromByteArray(buffer, 0, count);
	}

	public static WriteableBitmap FromByteArray(this WriteableBitmap bmp, byte[] buffer)
	{
		return bmp.FromByteArray(buffer, 0, buffer.Length);
	}

	public static void WriteTga(this WriteableBitmap bmp, Stream destination)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int width = bitmapContext.Width;
		int height = bitmapContext.Height;
		int[] pixels = bitmapContext.Pixels;
		byte[] array = new byte[bitmapContext.Length * 4];
		int num = 0;
		int num2 = width << 2;
		int num3 = width << 3;
		int num4 = (height - 1) * num2;
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				int num5 = pixels[num];
				byte b = (byte)(num5 >> 24);
				int num6 = b;
				if (num6 == 0)
				{
					num6 = 1;
				}
				num6 = 65280 / num6;
				array[num4 + 3] = b;
				array[num4 + 2] = (byte)(((num5 >> 16) & 0xFF) * num6 >> 8);
				array[num4 + 1] = (byte)(((num5 >> 8) & 0xFF) * num6 >> 8);
				array[num4] = (byte)((num5 & 0xFF) * num6 >> 8);
				num++;
				num4 += 4;
			}
			num4 -= num3;
		}
		byte[] buffer = new byte[18]
		{
			0,
			0,
			2,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			(byte)(width & 0xFF),
			(byte)((width & 0xFF00) >> 8),
			(byte)(height & 0xFF),
			(byte)((height & 0xFF00) >> 8),
			32,
			0
		};
		using BinaryWriter binaryWriter = new BinaryWriter(destination);
		binaryWriter.Write(buffer);
		binaryWriter.Write(array);
	}

	public static async Task<WriteableBitmap> FromContent(this WriteableBitmap bmp, Uri uri, BitmapPixelFormat pixelFormat = (BitmapPixelFormat)0)
	{
		IRandomAccessStream stream = await (await StorageFile.GetFileFromApplicationUriAsync(uri)).OpenAsync((FileAccessMode)0);
		try
		{
			return await bmp.FromStream(stream, (BitmapPixelFormat)0);
		}
		finally
		{
			((IDisposable)stream)?.Dispose();
		}
	}

	public static async Task<WriteableBitmap> FromStream(this WriteableBitmap bmp, Stream stream, BitmapPixelFormat pixelFormat = (BitmapPixelFormat)0)
	{
		InMemoryRandomAccessStream dstStream = new InMemoryRandomAccessStream();
		try
		{
			await RandomAccessStream.CopyAsync(stream.AsInputStream(), (IOutputStream)(object)dstStream);
			return await bmp.FromStream((IRandomAccessStream)(object)dstStream, (BitmapPixelFormat)0);
		}
		finally
		{
			((IDisposable)dstStream)?.Dispose();
		}
	}

	public static async Task<WriteableBitmap> FromStream(this WriteableBitmap bmp, IRandomAccessStream stream, BitmapPixelFormat pixelFormat = (BitmapPixelFormat)0)
	{
		//IL_0012: Unknown result type (might be due to invalid IL or missing references)
		//IL_0013: Unknown result type (might be due to invalid IL or missing references)
		BitmapDecoder decoder = await BitmapDecoder.CreateAsync(stream);
		BitmapTransform transform = new BitmapTransform();
		if ((int)pixelFormat == 0)
		{
			pixelFormat = decoder.BitmapPixelFormat;
		}
		byte[] pixels = (await decoder.GetPixelDataAsync(pixelFormat, decoder.BitmapAlphaMode, transform, (ExifOrientationMode)1, (ColorManagementMode)1)).DetachPixelData();
		bmp = new WriteableBitmap((int)decoder.OrientedPixelWidth, (int)decoder.OrientedPixelHeight);
		using Stream stream2 = bmp.PixelBuffer.AsStream();
		stream2.Seek(0L, SeekOrigin.Begin);
		stream2.Write(pixels, 0, (int)stream2.Length);
		return bmp;
	}

	public static async Task ToStream(this WriteableBitmap bmp, IRandomAccessStream destinationStream, Guid encoderId)
	{
		byte[] pixels;
		using (Stream stream = bmp.PixelBuffer.AsStream())
		{
			pixels = new byte[(int)stream.Length];
			await stream.ReadAsync(pixels, 0, pixels.Length);
		}
		BitmapEncoder encoder = await BitmapEncoder.CreateAsync(encoderId, destinationStream);
		encoder.SetPixelData((BitmapPixelFormat)87, (BitmapAlphaMode)0, (uint)((BitmapSource)bmp).PixelWidth, (uint)((BitmapSource)bmp).PixelHeight, 96.0, 96.0, pixels);
		await encoder.FlushAsync();
	}

	public static async Task ToStreamAsJpeg(this WriteableBitmap bmp, IRandomAccessStream destinationStream)
	{
		await bmp.ToStream(destinationStream, BitmapEncoder.JpegEncoderId);
	}

	public static async Task<WriteableBitmap> FromPixelBuffer(this WriteableBitmap bmp, IBuffer pixelBuffer, int width, int height)
	{
		bmp = new WriteableBitmap(width, height);
		using Stream srcStream = pixelBuffer.AsStream();
		using (Stream destStream = bmp.PixelBuffer.AsStream())
		{
			srcStream.Seek(0L, SeekOrigin.Begin);
			await srcStream.CopyToAsync(destStream);
		}
		return bmp;
	}

	public static void FillRectangle(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.FillRectangle(x1, y1, x2, y2, color2);
	}

	public static void FillRectangle(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int color)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int width = bitmapContext.Width;
		int height = bitmapContext.Height;
		int[] pixels = bitmapContext.Pixels;
		if ((x1 >= 0 || x2 >= 0) && (y1 >= 0 || y2 >= 0) && (x1 < width || x2 < width) && (y1 < height || y2 < height))
		{
			if (x1 < 0)
			{
				x1 = 0;
			}
			if (y1 < 0)
			{
				y1 = 0;
			}
			if (x2 < 0)
			{
				x2 = 0;
			}
			if (y2 < 0)
			{
				y2 = 0;
			}
			if (x1 >= width)
			{
				x1 = width - 1;
			}
			if (y1 >= height)
			{
				y1 = height - 1;
			}
			if (x2 >= width)
			{
				x2 = width - 1;
			}
			if (y2 >= height)
			{
				y2 = height - 1;
			}
			int num = y1 * width;
			int num2 = num + x1;
			int num3 = num + x2;
			for (int i = num2; i <= num3; i++)
			{
				pixels[i] = color;
			}
			int count = (x2 - x1 + 1) * 4;
			int srcOffset = num2 * 4;
			int num4 = y2 * width + x1;
			for (int j = num2 + width; j <= num4; j += width)
			{
				BitmapContext.BlockCopy(bitmapContext, srcOffset, bitmapContext, j * 4, count);
			}
		}
	}

	public static void FillEllipse(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.FillEllipse(x1, y1, x2, y2, color2);
	}

	public static void FillEllipse(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int color)
	{
		int num = x2 - x1 >> 1;
		int num2 = y2 - y1 >> 1;
		int xc = x1 + num;
		int yc = y1 + num2;
		bmp.FillEllipseCentered(xc, yc, num, num2, color);
	}

	public static void FillEllipseCentered(this WriteableBitmap bmp, int xc, int yc, int xr, int yr, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.FillEllipseCentered(xc, yc, xr, yr, color2);
	}

	public static void FillEllipseCentered(this WriteableBitmap bmp, int xc, int yc, int xr, int yr, int color)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int[] pixels = bitmapContext.Pixels;
		int width = bitmapContext.Width;
		int height = bitmapContext.Height;
		if (xr < 1 || yr < 1)
		{
			return;
		}
		int num = xr;
		int num2 = 0;
		int num3 = xr * xr << 1;
		int num4 = yr * yr << 1;
		int num5 = yr * yr * (1 - (xr << 1));
		int num6 = xr * xr;
		int num7 = 0;
		int num8 = num4 * xr;
		int num9 = 0;
		int num12;
		int num13;
		int num10;
		int num11;
		while (num8 >= num9)
		{
			num10 = yc + num2;
			num11 = yc - num2;
			if (num10 < 0)
			{
				num10 = 0;
			}
			if (num10 >= height)
			{
				num10 = height - 1;
			}
			if (num11 < 0)
			{
				num11 = 0;
			}
			if (num11 >= height)
			{
				num11 = height - 1;
			}
			num12 = num10 * width;
			num13 = num11 * width;
			int num14 = xc + num;
			int num15 = xc - num;
			if (num14 < 0)
			{
				num14 = 0;
			}
			if (num14 >= width)
			{
				num14 = width - 1;
			}
			if (num15 < 0)
			{
				num15 = 0;
			}
			if (num15 >= width)
			{
				num15 = width - 1;
			}
			for (int i = num15; i <= num14; i++)
			{
				pixels[i + num12] = color;
				pixels[i + num13] = color;
			}
			num2++;
			num9 += num3;
			num7 += num6;
			num6 += num3;
			if (num5 + (num7 << 1) > 0)
			{
				num--;
				num8 -= num4;
				num7 += num5;
				num5 += num4;
			}
		}
		num = 0;
		num2 = yr;
		num10 = yc + num2;
		num11 = yc - num2;
		if (num10 < 0)
		{
			num10 = 0;
		}
		if (num10 >= height)
		{
			num10 = height - 1;
		}
		if (num11 < 0)
		{
			num11 = 0;
		}
		if (num11 >= height)
		{
			num11 = height - 1;
		}
		num12 = num10 * width;
		num13 = num11 * width;
		num5 = yr * yr;
		num6 = xr * xr * (1 - (yr << 1));
		num7 = 0;
		num8 = 0;
		num9 = num3 * yr;
		while (num8 <= num9)
		{
			int num14 = xc + num;
			int num15 = xc - num;
			if (num14 < 0)
			{
				num14 = 0;
			}
			if (num14 >= width)
			{
				num14 = width - 1;
			}
			if (num15 < 0)
			{
				num15 = 0;
			}
			if (num15 >= width)
			{
				num15 = width - 1;
			}
			for (int j = num15; j <= num14; j++)
			{
				pixels[j + num12] = color;
				pixels[j + num13] = color;
			}
			num++;
			num8 += num4;
			num7 += num5;
			num5 += num4;
			if (num6 + (num7 << 1) > 0)
			{
				num2--;
				num10 = yc + num2;
				num11 = yc - num2;
				if (num10 < 0)
				{
					num10 = 0;
				}
				if (num10 >= height)
				{
					num10 = height - 1;
				}
				if (num11 < 0)
				{
					num11 = 0;
				}
				if (num11 >= height)
				{
					num11 = height - 1;
				}
				num12 = num10 * width;
				num13 = num11 * width;
				num9 -= num3;
				num7 += num6;
				num6 += num3;
			}
		}
	}

	public static void FillPolygon(this WriteableBitmap bmp, int[] points, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.FillPolygon(points, color2);
	}

	public static void FillPolygon(this WriteableBitmap bmp, int[] points, int color)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int width = bitmapContext.Width;
		int height = bitmapContext.Height;
		int[] pixels = bitmapContext.Pixels;
		int num = points.Length;
		int num2 = points.Length >> 1;
		int[] array = new int[num2];
		int num3 = height;
		int num4 = 0;
		for (int i = 1; i < num; i += 2)
		{
			int num5 = points[i];
			if (num5 < num3)
			{
				num3 = num5;
			}
			if (num5 > num4)
			{
				num4 = num5;
			}
		}
		if (num3 < 0)
		{
			num3 = 0;
		}
		if (num4 >= height)
		{
			num4 = height - 1;
		}
		for (int j = num3; j <= num4; j++)
		{
			float num6 = points[0];
			float num7 = points[1];
			int num8 = 0;
			for (int k = 2; k < num; k += 2)
			{
				float num9 = points[k];
				float num10 = points[k + 1];
				if ((num7 < (float)j && num10 >= (float)j) || (num10 < (float)j && num7 >= (float)j))
				{
					array[num8++] = (int)(num6 + ((float)j - num7) / (num10 - num7) * (num9 - num6));
				}
				num6 = num9;
				num7 = num10;
			}
			for (int l = 1; l < num8; l++)
			{
				int num11 = array[l];
				int num12 = l;
				while (num12 > 0 && array[num12 - 1] > num11)
				{
					array[num12] = array[num12 - 1];
					num12--;
				}
				array[num12] = num11;
			}
			for (int m = 0; m < num8 - 1; m += 2)
			{
				int num13 = array[m];
				int num14 = array[m + 1];
				if (num14 > 0 && num13 < width)
				{
					if (num13 < 0)
					{
						num13 = 0;
					}
					if (num14 >= width)
					{
						num14 = width - 1;
					}
					for (int n = num13; n <= num14; n++)
					{
						pixels[j * width + n] = color;
					}
				}
			}
		}
	}

	public static void FillQuad(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.FillQuad(x1, y1, x2, y2, x3, y3, x4, y4, color2);
	}

	public static void FillQuad(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int color)
	{
		bmp.FillPolygon(new int[10] { x1, y1, x2, y2, x3, y3, x4, y4, x1, y1 }, color);
	}

	public static void FillTriangle(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int x3, int y3, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.FillTriangle(x1, y1, x2, y2, x3, y3, color2);
	}

	public static void FillTriangle(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int x3, int y3, int color)
	{
		bmp.FillPolygon(new int[8] { x1, y1, x2, y2, x3, y3, x1, y1 }, color);
	}

	private static List<int> ComputeBezierPoints(int x1, int y1, int cx1, int cy1, int cx2, int cy2, int x2, int y2, int color, BitmapContext context, int w, int h)
	{
		_ = context.Pixels;
		int num = Math.Min(x1, Math.Min(cx1, Math.Min(cx2, x2)));
		int num2 = Math.Min(y1, Math.Min(cy1, Math.Min(cy2, y2)));
		int num3 = Math.Max(x1, Math.Max(cx1, Math.Max(cx2, x2)));
		int num4 = Math.Max(y1, Math.Max(cy1, Math.Max(cy2, y2)));
		int num5 = num3 - num;
		int num6 = num4 - num2;
		if (num5 > num6)
		{
			num6 = num5;
		}
		List<int> list = new List<int>();
		if (num6 != 0)
		{
			float num7 = 2f / (float)num6;
			int num8 = x1;
			int num9 = y1;
			for (float num10 = 0f; num10 <= 1f; num10 += num7)
			{
				float num11 = num10 * num10;
				float num12 = 1f - num10;
				float num13 = num12 * num12;
				num8 = (int)(num12 * num13 * (float)x1 + 3f * num10 * num13 * (float)cx1 + 3f * num12 * num11 * (float)cx2 + num10 * num11 * (float)x2);
				num9 = (int)(num12 * num13 * (float)y1 + 3f * num10 * num13 * (float)cy1 + 3f * num12 * num11 * (float)cy2 + num10 * num11 * (float)y2);
				list.Add(num8);
				list.Add(num9);
			}
			list.Add(x2);
			list.Add(y2);
		}
		return list;
	}

	public static void FillBeziers(this WriteableBitmap bmp, int[] points, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.FillBeziers(points, color2);
	}

	public static void FillBeziers(this WriteableBitmap bmp, int[] points, int color)
	{
		using BitmapContext context = bmp.GetBitmapContext();
		int width = context.Width;
		int height = context.Height;
		int x = points[0];
		int y = points[1];
		List<int> list = new List<int>();
		for (int i = 2; i + 5 < points.Length; i += 6)
		{
			int num = points[i + 4];
			int num2 = points[i + 5];
			list.AddRange(ComputeBezierPoints(x, y, points[i], points[i + 1], points[i + 2], points[i + 3], num, num2, color, context, width, height));
			x = num;
			y = num2;
		}
		bmp.FillPolygon(list.ToArray(), color);
	}

	private static List<int> ComputeSegmentPoints(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, float tension, int color, BitmapContext context, int w, int h)
	{
		_ = context.Pixels;
		int num = Math.Min(x1, Math.Min(x2, Math.Min(x3, x4)));
		int num2 = Math.Min(y1, Math.Min(y2, Math.Min(y3, y4)));
		int num3 = Math.Max(x1, Math.Max(x2, Math.Max(x3, x4)));
		int num4 = Math.Max(y1, Math.Max(y2, Math.Max(y3, y4)));
		int num5 = num3 - num;
		int num6 = num4 - num2;
		if (num5 > num6)
		{
			num6 = num5;
		}
		List<int> list = new List<int>();
		if (num6 != 0)
		{
			float num7 = 2f / (float)num6;
			float num8 = tension * (float)(x3 - x1);
			float num9 = tension * (float)(y3 - y1);
			float num10 = tension * (float)(x4 - x2);
			float num11 = tension * (float)(y4 - y2);
			float num12 = num8 + num10 + (float)(2 * x2) - (float)(2 * x3);
			float num13 = num9 + num11 + (float)(2 * y2) - (float)(2 * y3);
			float num14 = -2f * num8 - num10 - (float)(3 * x2) + (float)(3 * x3);
			float num15 = -2f * num9 - num11 - (float)(3 * y2) + (float)(3 * y3);
			for (float num16 = 0f; num16 <= 1f; num16 += num7)
			{
				float num17 = num16 * num16;
				int item = (int)(num12 * num17 * num16 + num14 * num17 + num8 * num16 + (float)x2);
				int item2 = (int)(num13 * num17 * num16 + num15 * num17 + num9 * num16 + (float)y2);
				list.Add(item);
				list.Add(item2);
			}
			list.Add(x3);
			list.Add(y3);
		}
		return list;
	}

	public static void FillCurve(this WriteableBitmap bmp, int[] points, float tension, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.FillCurve(points, tension, color2);
	}

	public static void FillCurve(this WriteableBitmap bmp, int[] points, float tension, int color)
	{
		using BitmapContext context = bmp.GetBitmapContext();
		int width = context.Width;
		int height = context.Height;
		List<int> list = ComputeSegmentPoints(points[0], points[1], points[0], points[1], points[2], points[3], points[4], points[5], tension, color, context, width, height);
		int i;
		for (i = 2; i < points.Length - 4; i += 2)
		{
			list.AddRange(ComputeSegmentPoints(points[i - 2], points[i - 1], points[i], points[i + 1], points[i + 2], points[i + 3], points[i + 4], points[i + 5], tension, color, context, width, height));
		}
		list.AddRange(ComputeSegmentPoints(points[i - 2], points[i - 1], points[i], points[i + 1], points[i + 2], points[i + 3], points[i + 2], points[i + 3], tension, color, context, width, height));
		bmp.FillPolygon(list.ToArray(), color);
	}

	public static void FillCurveClosed(this WriteableBitmap bmp, int[] points, float tension, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.FillCurveClosed(points, tension, color2);
	}

	public static void FillCurveClosed(this WriteableBitmap bmp, int[] points, float tension, int color)
	{
		using BitmapContext context = bmp.GetBitmapContext();
		int width = context.Width;
		int height = context.Height;
		int num = points.Length;
		List<int> list = ComputeSegmentPoints(points[num - 2], points[num - 1], points[0], points[1], points[2], points[3], points[4], points[5], tension, color, context, width, height);
		int i;
		for (i = 2; i < num - 4; i += 2)
		{
			list.AddRange(ComputeSegmentPoints(points[i - 2], points[i - 1], points[i], points[i + 1], points[i + 2], points[i + 3], points[i + 4], points[i + 5], tension, color, context, width, height));
		}
		list.AddRange(ComputeSegmentPoints(points[i - 2], points[i - 1], points[i], points[i + 1], points[i + 2], points[i + 3], points[0], points[1], tension, color, context, width, height));
		list.AddRange(ComputeSegmentPoints(points[i], points[i + 1], points[i + 2], points[i + 3], points[0], points[1], points[2], points[3], tension, color, context, width, height));
		bmp.FillPolygon(list.ToArray(), color);
	}

	public static WriteableBitmap Convolute(this WriteableBitmap bmp, int[,] kernel)
	{
		int num = 0;
		foreach (int num2 in kernel)
		{
			num += num2;
		}
		return bmp.Convolute(kernel, num, 0);
	}

	public static WriteableBitmap Convolute(this WriteableBitmap bmp, int[,] kernel, int kernelFactorSum, int kernelOffsetSum)
	{
		int num = kernel.GetUpperBound(0) + 1;
		int num2 = kernel.GetUpperBound(1) + 1;
		if ((num2 & 1) == 0)
		{
			throw new InvalidOperationException("Kernel width must be odd!");
		}
		if ((num & 1) == 0)
		{
			throw new InvalidOperationException("Kernel height must be odd!");
		}
		using BitmapContext bitmapContext = bmp.GetBitmapContext(ReadWriteMode.ReadOnly);
		int width = bitmapContext.Width;
		int height = bitmapContext.Height;
		WriteableBitmap val = BitmapFactory.New(width, height);
		using BitmapContext bitmapContext2 = val.GetBitmapContext();
		int[] pixels = bitmapContext.Pixels;
		int[] pixels2 = bitmapContext2.Pixels;
		int num3 = 0;
		int num4 = num2 >> 1;
		int num5 = num >> 1;
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				int num6 = 0;
				int num7 = 0;
				int num8 = 0;
				int num9 = 0;
				for (int k = -num4; k <= num4; k++)
				{
					int num10 = k + j;
					if (num10 < 0)
					{
						num10 = 0;
					}
					else if (num10 >= width)
					{
						num10 = width - 1;
					}
					for (int l = -num5; l <= num5; l++)
					{
						int num11 = l + i;
						if (num11 < 0)
						{
							num11 = 0;
						}
						else if (num11 >= height)
						{
							num11 = height - 1;
						}
						int num12 = pixels[num11 * width + num10];
						int num13 = kernel[l + num4, k + num5];
						num6 += ((num12 >> 24) & 0xFF) * num13;
						num7 += ((num12 >> 16) & 0xFF) * num13;
						num8 += ((num12 >> 8) & 0xFF) * num13;
						num9 += (num12 & 0xFF) * num13;
					}
				}
				int num14 = num6 / kernelFactorSum + kernelOffsetSum;
				int num15 = num7 / kernelFactorSum + kernelOffsetSum;
				int num16 = num8 / kernelFactorSum + kernelOffsetSum;
				int num17 = num9 / kernelFactorSum + kernelOffsetSum;
				byte b = (byte)((num14 > 255) ? 255u : ((num14 >= 0) ? ((uint)num14) : 0u));
				byte b2 = (byte)((num15 > 255) ? 255u : ((num15 >= 0) ? ((uint)num15) : 0u));
				byte b3 = (byte)((num16 > 255) ? 255u : ((num16 >= 0) ? ((uint)num16) : 0u));
				byte b4 = (byte)((num17 > 255) ? 255u : ((num17 >= 0) ? ((uint)num17) : 0u));
				pixels2[num3++] = (b << 24) | (b2 << 16) | (b3 << 8) | b4;
			}
		}
		return val;
	}

	public static WriteableBitmap Invert(this WriteableBitmap bmp)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		WriteableBitmap val = BitmapFactory.New(bitmapContext.Width, bitmapContext.Height);
		using BitmapContext bitmapContext2 = val.GetBitmapContext();
		int[] pixels = bitmapContext2.Pixels;
		int[] pixels2 = bitmapContext.Pixels;
		int length = bitmapContext.Length;
		for (int i = 0; i < length; i++)
		{
			int num = pixels2[i];
			int num2 = (num >> 24) & 0xFF;
			int num3 = (num >> 16) & 0xFF;
			int num4 = (num >> 8) & 0xFF;
			int num5 = num & 0xFF;
			num3 = 255 - num3;
			num4 = 255 - num4;
			num5 = 255 - num5;
			pixels[i] = (num2 << 24) | (num3 << 16) | (num4 << 8) | num5;
		}
		return val;
	}

	public static void DrawLineBresenham(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.DrawLineBresenham(x1, y1, x2, y2, color2);
	}

	public static void DrawLineBresenham(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int color)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int width = bitmapContext.Width;
		int height = bitmapContext.Height;
		int[] pixels = bitmapContext.Pixels;
		int num = x2 - x1;
		int num2 = y2 - y1;
		int num3 = 0;
		if (num < 0)
		{
			num = -num;
			num3 = -1;
		}
		else if (num > 0)
		{
			num3 = 1;
		}
		int num4 = 0;
		if (num2 < 0)
		{
			num2 = -num2;
			num4 = -1;
		}
		else if (num2 > 0)
		{
			num4 = 1;
		}
		int num5;
		int num6;
		int num7;
		int num8;
		int num9;
		int num10;
		if (num > num2)
		{
			num5 = num3;
			num6 = 0;
			num7 = num3;
			num8 = num4;
			num9 = num2;
			num10 = num;
		}
		else
		{
			num5 = 0;
			num6 = num4;
			num7 = num3;
			num8 = num4;
			num9 = num;
			num10 = num2;
		}
		int num11 = x1;
		int num12 = y1;
		int num13 = num10 >> 1;
		if (num12 < height && num12 >= 0 && num11 < width && num11 >= 0)
		{
			pixels[num12 * width + num11] = color;
		}
		for (int i = 0; i < num10; i++)
		{
			num13 -= num9;
			if (num13 < 0)
			{
				num13 += num10;
				num11 += num7;
				num12 += num8;
			}
			else
			{
				num11 += num5;
				num12 += num6;
			}
			if (num12 < height && num12 >= 0 && num11 < width && num11 >= 0)
			{
				pixels[num12 * width + num11] = color;
			}
		}
	}

	public static void DrawLineDDA(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.DrawLineDDA(x1, y1, x2, y2, color2);
	}

	public static void DrawLineDDA(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int color)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int width = bitmapContext.Width;
		int height = bitmapContext.Height;
		int[] pixels = bitmapContext.Pixels;
		int num = x2 - x1;
		int num2 = y2 - y1;
		int num3 = ((num2 >= 0) ? num2 : (-num2));
		int num4 = ((num >= 0) ? num : (-num));
		if (num4 > num3)
		{
			num3 = num4;
		}
		if (num3 == 0)
		{
			return;
		}
		float num5 = (float)num / (float)num3;
		float num6 = (float)num2 / (float)num3;
		float num7 = x1;
		float num8 = y1;
		for (int i = 0; i < num3; i++)
		{
			if (num8 < (float)height && num8 >= 0f && num7 < (float)width && num7 >= 0f)
			{
				pixels[(int)num8 * width + (int)num7] = color;
			}
			num7 += num5;
			num8 += num6;
		}
	}

	public static void DrawLine(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.DrawLine(x1, y1, x2, y2, color2);
	}

	public static void DrawLine(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int color)
	{
		using BitmapContext context = bmp.GetBitmapContext();
		DrawLine(context, context.Width, context.Height, x1, y1, x2, y2, color);
	}

	public static void DrawLine(BitmapContext context, int pixelWidth, int pixelHeight, int x1, int y1, int x2, int y2, int color)
	{
		int[] pixels = context.Pixels;
		int num = x2 - x1;
		int num2 = y2 - y1;
		int num3 = ((num2 < 0) ? (-num2) : num2);
		int num4 = ((num < 0) ? (-num) : num);
		if (num4 > num3)
		{
			if (num < 0)
			{
				int num5 = x1;
				x1 = x2;
				x2 = num5;
				num5 = y1;
				y1 = y2;
				y2 = num5;
			}
			int num6 = (num2 << 8) / num;
			int num7 = y1 << 8;
			int num8 = y2 << 8;
			int num9 = pixelHeight << 8;
			if (y1 < y2)
			{
				if (y1 >= pixelHeight || y2 < 0)
				{
					return;
				}
				if (num7 < 0)
				{
					if (num6 == 0)
					{
						return;
					}
					int num10 = num7;
					num7 = num6 - 1 + (num7 + 1) % num6;
					x1 += (num7 - num10) / num6;
				}
				if (num8 >= num9 && num6 != 0)
				{
					num8 = num9 - 1 - (num9 - 1 - num7) % num6;
					x2 = x1 + (num8 - num7) / num6;
				}
			}
			else
			{
				if (y2 >= pixelHeight || y1 < 0)
				{
					return;
				}
				if (num7 >= num9)
				{
					if (num6 == 0)
					{
						return;
					}
					int num11 = num7;
					num7 = num9 - 1 + (num6 - (num9 - 1 - num11) % num6);
					x1 += (num7 - num11) / num6;
				}
				if (num8 < 0 && num6 != 0)
				{
					num8 = num7 % num6;
					x2 = x1 + (num8 - num7) / num6;
				}
			}
			if (x1 < 0)
			{
				num7 -= num6 * x1;
				x1 = 0;
			}
			if (x2 >= pixelWidth)
			{
				x2 = pixelWidth - 1;
			}
			int num12 = num7;
			int num13 = num12 >> 8;
			int num14 = num13;
			int num15 = x1 + num13 * pixelWidth;
			int num16 = ((num6 < 0) ? (1 - pixelWidth) : (1 + pixelWidth));
			for (int i = x1; i <= x2; i++)
			{
				pixels[num15] = color;
				num12 += num6;
				num13 = num12 >> 8;
				if (num13 != num14)
				{
					num14 = num13;
					num15 += num16;
				}
				else
				{
					num15++;
				}
			}
		}
		else
		{
			if (num3 == 0)
			{
				return;
			}
			if (num2 < 0)
			{
				int num17 = x1;
				x1 = x2;
				x2 = num17;
				num17 = y1;
				y1 = y2;
				y2 = num17;
			}
			int num18 = x1 << 8;
			int num19 = x2 << 8;
			int num20 = pixelWidth << 8;
			int num21 = (num << 8) / num2;
			if (x1 < x2)
			{
				if (x1 >= pixelWidth || x2 < 0)
				{
					return;
				}
				if (num18 < 0)
				{
					if (num21 == 0)
					{
						return;
					}
					int num22 = num18;
					num18 = num21 - 1 + (num18 + 1) % num21;
					y1 += (num18 - num22) / num21;
				}
				if (num19 >= num20 && num21 != 0)
				{
					num19 = num20 - 1 - (num20 - 1 - num18) % num21;
					y2 = y1 + (num19 - num18) / num21;
				}
			}
			else
			{
				if (x2 >= pixelWidth || x1 < 0)
				{
					return;
				}
				if (num18 >= num20)
				{
					if (num21 == 0)
					{
						return;
					}
					int num23 = num18;
					num18 = num20 - 1 + (num21 - (num20 - 1 - num23) % num21);
					y1 += (num18 - num23) / num21;
				}
				if (num19 < 0 && num21 != 0)
				{
					num19 = num18 % num21;
					y2 = y1 + (num19 - num18) / num21;
				}
			}
			if (y1 < 0)
			{
				num18 -= num21 * y1;
				y1 = 0;
			}
			if (y2 >= pixelHeight)
			{
				y2 = pixelHeight - 1;
			}
			int num24 = num18 + (y1 * pixelWidth << 8);
			int num25 = (pixelWidth << 8) + num21;
			for (int j = y1; j <= y2; j++)
			{
				pixels[num24 >> 8] = color;
				num24 += num25;
			}
		}
	}

	public static void DrawLineAa(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.DrawLineAa(x1, y1, x2, y2, color2);
	}

	public static void DrawLineAa(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int color)
	{
		using BitmapContext context = bmp.GetBitmapContext();
		DrawLineAa(context, context.Width, context.Height, x1, y1, x2, y2, color);
	}

	public static void DrawLineAa(BitmapContext context, int pixelWidth, int pixelHeight, int x1, int y1, int x2, int y2, int color)
	{
		if (x1 == x2 && y1 == y2)
		{
			return;
		}
		if (x1 < 1)
		{
			x1 = 1;
		}
		if (x1 > pixelWidth - 2)
		{
			x1 = pixelWidth - 2;
		}
		if (y1 < 1)
		{
			y1 = 1;
		}
		if (y1 > pixelHeight - 2)
		{
			y1 = pixelHeight - 2;
		}
		if (x2 < 1)
		{
			x2 = 1;
		}
		if (x2 > pixelWidth - 2)
		{
			x2 = pixelWidth - 2;
		}
		if (y2 < 1)
		{
			y2 = 1;
		}
		if (y2 > pixelHeight - 2)
		{
			y2 = pixelHeight - 2;
		}
		int num = y1 * pixelWidth + x1;
		int num2 = x2 - x1;
		int num3 = y2 - y1;
		int num4 = (color >> 24) & 0xFF;
		uint srb = (uint)(color & 0xFF00FF);
		uint sg = (uint)((color >> 8) & 0xFF);
		int num5 = num2;
		int num6 = num3;
		if (num2 < 0)
		{
			num5 = -num2;
		}
		if (num3 < 0)
		{
			num6 = -num3;
		}
		int num7;
		int num8;
		int num9;
		int num10;
		int num11;
		int num12;
		if (num5 > num6)
		{
			num7 = num5;
			num8 = num6;
			num9 = x2;
			num10 = y2;
			num11 = 1;
			num12 = pixelWidth;
			if (num2 < 0)
			{
				num11 = -num11;
			}
			if (num3 < 0)
			{
				num12 = -num12;
			}
		}
		else
		{
			num7 = num6;
			num8 = num5;
			num9 = y2;
			num10 = x2;
			num11 = pixelWidth;
			num12 = 1;
			if (num3 < 0)
			{
				num11 = -num11;
			}
			if (num2 < 0)
			{
				num12 = -num12;
			}
		}
		int num13 = num9 + num7;
		int num14 = (num8 << 1) - num7;
		int num15 = num8 << 1;
		int num16 = num8 - num7 << 1;
		double num17 = 1.0 / (4.0 * Math.Sqrt(num7 * num7 + num8 * num8));
		double num18 = 0.75 - 2.0 * ((double)num7 * num17);
		int num19 = (int)(num17 * 1024.0);
		int num20 = (int)(num18 * 1024.0 * (double)num4);
		int num21 = (int)(768.0 * (double)num4);
		int num22 = num19 * num4;
		int num23 = num7 * num22;
		int num24 = num14 * num22;
		int num25 = 0;
		int num26 = num15 * num22;
		int num27 = num16 * num22;
		do
		{
			AlphaBlendNormalOnPremultiplied(context, num, num21 - num25 >> 10, srb, sg);
			AlphaBlendNormalOnPremultiplied(context, num + num12, num20 + num25 >> 10, srb, sg);
			AlphaBlendNormalOnPremultiplied(context, num - num12, num20 - num25 >> 10, srb, sg);
			if (num14 < 0)
			{
				num25 = num24 + num23;
				num14 += num15;
				num24 += num26;
			}
			else
			{
				num25 = num24 - num23;
				num14 += num16;
				num24 += num27;
				num10++;
				num += num12;
			}
			num9++;
			num += num11;
		}
		while (num9 < num13);
	}

	private static void AlphaBlendNormalOnPremultiplied(BitmapContext context, int index, int sa, uint srb, uint sg)
	{
		int[] pixels = context.Pixels;
		uint num = (uint)pixels[index];
		uint num2 = num >> 24;
		uint num3 = (num >> 8) & 0xFF;
		uint num4 = num & 0xFF00FF;
		pixels[index] = (int)((sa + (num2 * (255 - sa) * 32897 >> 23) << 24) | (((sg - num3) * sa + (num3 << 8)) & 0xFFFFFF00u) | ((((srb - num4) * sa >> 8) + num4) & 0xFF00FF));
	}

	public static void DrawPolyline(this WriteableBitmap bmp, int[] points, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.DrawPolyline(points, color2);
	}

	public static void DrawPolyline(this WriteableBitmap bmp, int[] points, int color)
	{
		using BitmapContext context = bmp.GetBitmapContext();
		int width = context.Width;
		int height = context.Height;
		int x = points[0];
		int y = points[1];
		for (int i = 2; i < points.Length; i += 2)
		{
			int num = points[i];
			int num2 = points[i + 1];
			DrawLine(context, width, height, x, y, num, num2, color);
			x = num;
			y = num2;
		}
	}

	public static void DrawTriangle(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int x3, int y3, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.DrawTriangle(x1, y1, x2, y2, x3, y3, color2);
	}

	public static void DrawTriangle(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int x3, int y3, int color)
	{
		using BitmapContext context = bmp.GetBitmapContext();
		int width = context.Width;
		int height = context.Height;
		DrawLine(context, width, height, x1, y1, x2, y2, color);
		DrawLine(context, width, height, x2, y2, x3, y3, color);
		DrawLine(context, width, height, x3, y3, x1, y1, color);
	}

	public static void DrawQuad(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.DrawQuad(x1, y1, x2, y2, x3, y3, x4, y4, color2);
	}

	public static void DrawQuad(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int color)
	{
		using BitmapContext context = bmp.GetBitmapContext();
		int width = context.Width;
		int height = context.Height;
		DrawLine(context, width, height, x1, y1, x2, y2, color);
		DrawLine(context, width, height, x2, y2, x3, y3, color);
		DrawLine(context, width, height, x3, y3, x4, y4, color);
		DrawLine(context, width, height, x4, y4, x1, y1, color);
	}

	public static void DrawRectangle(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.DrawRectangle(x1, y1, x2, y2, color2);
	}

	public static void DrawRectangle(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int color)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int width = bitmapContext.Width;
		int height = bitmapContext.Height;
		int[] pixels = bitmapContext.Pixels;
		if ((x1 >= 0 || x2 >= 0) && (y1 >= 0 || y2 >= 0) && (x1 < width || x2 < width) && (y1 < height || y2 < height))
		{
			if (x1 < 0)
			{
				x1 = 0;
			}
			if (y1 < 0)
			{
				y1 = 0;
			}
			if (x2 < 0)
			{
				x2 = 0;
			}
			if (y2 < 0)
			{
				y2 = 0;
			}
			if (x1 >= width)
			{
				x1 = width - 1;
			}
			if (y1 >= height)
			{
				y1 = height - 1;
			}
			if (x2 >= width)
			{
				x2 = width - 1;
			}
			if (y2 >= height)
			{
				y2 = height - 1;
			}
			int num = y1 * width;
			int num2 = y2 * width;
			int num3 = num2 + x1;
			int num4 = num + x2;
			int num5 = num + x1;
			for (int i = num5; i <= num4; i++)
			{
				pixels[i] = color;
				pixels[num3] = color;
				num3++;
			}
			num4 = num5 + width;
			num3 -= width;
			for (int j = num + x2 + width; j <= num3; j += width)
			{
				pixels[j] = color;
				pixels[num4] = color;
				num4 += width;
			}
		}
	}

	public static void DrawEllipse(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.DrawEllipse(x1, y1, x2, y2, color2);
	}

	public static void DrawEllipse(this WriteableBitmap bmp, int x1, int y1, int x2, int y2, int color)
	{
		int num = x2 - x1 >> 1;
		int num2 = y2 - y1 >> 1;
		int xc = x1 + num;
		int yc = y1 + num2;
		bmp.DrawEllipseCentered(xc, yc, num, num2, color);
	}

	public static void DrawEllipseCentered(this WriteableBitmap bmp, int xc, int yc, int xr, int yr, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.DrawEllipseCentered(xc, yc, xr, yr, color2);
	}

	public static void DrawEllipseCentered(this WriteableBitmap bmp, int xc, int yc, int xr, int yr, int color)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int[] pixels = bitmapContext.Pixels;
		int width = bitmapContext.Width;
		int height = bitmapContext.Height;
		if (xr < 1 || yr < 1)
		{
			return;
		}
		int num = xr;
		int num2 = 0;
		int num3 = xr * xr << 1;
		int num4 = yr * yr << 1;
		int num5 = yr * yr * (1 - (xr << 1));
		int num6 = xr * xr;
		int num7 = 0;
		int num8 = num4 * xr;
		int num9 = 0;
		int num12;
		int num13;
		int num10;
		int num11;
		while (num8 >= num9)
		{
			num10 = yc + num2;
			num11 = yc - num2;
			if (num10 < 0)
			{
				num10 = 0;
			}
			if (num10 >= height)
			{
				num10 = height - 1;
			}
			if (num11 < 0)
			{
				num11 = 0;
			}
			if (num11 >= height)
			{
				num11 = height - 1;
			}
			num12 = num10 * width;
			num13 = num11 * width;
			int num14 = xc + num;
			int num15 = xc - num;
			if (num14 < 0)
			{
				num14 = 0;
			}
			if (num14 >= width)
			{
				num14 = width - 1;
			}
			if (num15 < 0)
			{
				num15 = 0;
			}
			if (num15 >= width)
			{
				num15 = width - 1;
			}
			pixels[num14 + num12] = color;
			pixels[num15 + num12] = color;
			pixels[num15 + num13] = color;
			pixels[num14 + num13] = color;
			num2++;
			num9 += num3;
			num7 += num6;
			num6 += num3;
			if (num5 + (num7 << 1) > 0)
			{
				num--;
				num8 -= num4;
				num7 += num5;
				num5 += num4;
			}
		}
		num = 0;
		num2 = yr;
		num10 = yc + num2;
		num11 = yc - num2;
		if (num10 < 0)
		{
			num10 = 0;
		}
		if (num10 >= height)
		{
			num10 = height - 1;
		}
		if (num11 < 0)
		{
			num11 = 0;
		}
		if (num11 >= height)
		{
			num11 = height - 1;
		}
		num12 = num10 * width;
		num13 = num11 * width;
		num5 = yr * yr;
		num6 = xr * xr * (1 - (yr << 1));
		num7 = 0;
		num8 = 0;
		num9 = num3 * yr;
		while (num8 <= num9)
		{
			int num14 = xc + num;
			int num15 = xc - num;
			if (num14 < 0)
			{
				num14 = 0;
			}
			if (num14 >= width)
			{
				num14 = width - 1;
			}
			if (num15 < 0)
			{
				num15 = 0;
			}
			if (num15 >= width)
			{
				num15 = width - 1;
			}
			pixels[num14 + num12] = color;
			pixels[num15 + num12] = color;
			pixels[num15 + num13] = color;
			pixels[num14 + num13] = color;
			num++;
			num8 += num4;
			num7 += num5;
			num5 += num4;
			if (num6 + (num7 << 1) > 0)
			{
				num2--;
				num10 = yc + num2;
				num11 = yc - num2;
				if (num10 < 0)
				{
					num10 = 0;
				}
				if (num10 >= height)
				{
					num10 = height - 1;
				}
				if (num11 < 0)
				{
					num11 = 0;
				}
				if (num11 >= height)
				{
					num11 = height - 1;
				}
				num12 = num10 * width;
				num13 = num11 * width;
				num9 -= num3;
				num7 += num6;
				num6 += num3;
			}
		}
	}

	public static void DrawBezier(this WriteableBitmap bmp, int x1, int y1, int cx1, int cy1, int cx2, int cy2, int x2, int y2, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.DrawBezier(x1, y1, cx1, cy1, cx2, cy2, x2, y2, color2);
	}

	public static void DrawBezier(this WriteableBitmap bmp, int x1, int y1, int cx1, int cy1, int cx2, int cy2, int x2, int y2, int color)
	{
		int num = Math.Min(x1, Math.Min(cx1, Math.Min(cx2, x2)));
		int num2 = Math.Min(y1, Math.Min(cy1, Math.Min(cy2, y2)));
		int num3 = Math.Max(x1, Math.Max(cx1, Math.Max(cx2, x2)));
		int num4 = Math.Max(y1, Math.Max(cy1, Math.Max(cy2, y2)));
		int num5 = num3 - num;
		int num6 = num4 - num2;
		if (num5 > num6)
		{
			num6 = num5;
		}
		if (num6 == 0)
		{
			return;
		}
		using BitmapContext context = bmp.GetBitmapContext();
		int width = context.Width;
		int height = context.Height;
		float num7 = 2f / (float)num6;
		int x3 = x1;
		int y3 = y1;
		for (float num8 = num7; num8 <= 1f; num8 += num7)
		{
			float num9 = num8 * num8;
			float num10 = 1f - num8;
			float num11 = num10 * num10;
			int num12 = (int)(num10 * num11 * (float)x1 + 3f * num8 * num11 * (float)cx1 + 3f * num10 * num9 * (float)cx2 + num8 * num9 * (float)x2);
			int num13 = (int)(num10 * num11 * (float)y1 + 3f * num8 * num11 * (float)cy1 + 3f * num10 * num9 * (float)cy2 + num8 * num9 * (float)y2);
			DrawLine(context, width, height, x3, y3, num12, num13, color);
			x3 = num12;
			y3 = num13;
		}
		DrawLine(context, width, height, x3, y3, x2, y2, color);
	}

	public static void DrawBeziers(this WriteableBitmap bmp, int[] points, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.DrawBeziers(points, color2);
	}

	public static void DrawBeziers(this WriteableBitmap bmp, int[] points, int color)
	{
		int x = points[0];
		int y = points[1];
		for (int i = 2; i + 5 < points.Length; i += 6)
		{
			int num = points[i + 4];
			int num2 = points[i + 5];
			bmp.DrawBezier(x, y, points[i], points[i + 1], points[i + 2], points[i + 3], num, num2, color);
			x = num;
			y = num2;
		}
	}

	private static void DrawCurveSegment(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, float tension, int color, BitmapContext context, int w, int h)
	{
		int num = Math.Min(x1, Math.Min(x2, Math.Min(x3, x4)));
		int num2 = Math.Min(y1, Math.Min(y2, Math.Min(y3, y4)));
		int num3 = Math.Max(x1, Math.Max(x2, Math.Max(x3, x4)));
		int num4 = Math.Max(y1, Math.Max(y2, Math.Max(y3, y4)));
		int num5 = num3 - num;
		int num6 = num4 - num2;
		if (num5 > num6)
		{
			num6 = num5;
		}
		if (num6 != 0)
		{
			float num7 = 2f / (float)num6;
			int x5 = x2;
			int y5 = y2;
			float num8 = tension * (float)(x3 - x1);
			float num9 = tension * (float)(y3 - y1);
			float num10 = tension * (float)(x4 - x2);
			float num11 = tension * (float)(y4 - y2);
			float num12 = num8 + num10 + (float)(2 * x2) - (float)(2 * x3);
			float num13 = num9 + num11 + (float)(2 * y2) - (float)(2 * y3);
			float num14 = -2f * num8 - num10 - (float)(3 * x2) + (float)(3 * x3);
			float num15 = -2f * num9 - num11 - (float)(3 * y2) + (float)(3 * y3);
			for (float num16 = num7; num16 <= 1f; num16 += num7)
			{
				float num17 = num16 * num16;
				int num18 = (int)(num12 * num17 * num16 + num14 * num17 + num8 * num16 + (float)x2);
				int num19 = (int)(num13 * num17 * num16 + num15 * num17 + num9 * num16 + (float)y2);
				DrawLine(context, w, h, x5, y5, num18, num19, color);
				x5 = num18;
				y5 = num19;
			}
			DrawLine(context, w, h, x5, y5, x3, y3, color);
		}
	}

	public static void DrawCurve(this WriteableBitmap bmp, int[] points, float tension, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.DrawCurve(points, tension, color2);
	}

	public static void DrawCurve(this WriteableBitmap bmp, int[] points, float tension, int color)
	{
		using BitmapContext context = bmp.GetBitmapContext();
		int width = context.Width;
		int height = context.Height;
		DrawCurveSegment(points[0], points[1], points[0], points[1], points[2], points[3], points[4], points[5], tension, color, context, width, height);
		int i;
		for (i = 2; i < points.Length - 4; i += 2)
		{
			DrawCurveSegment(points[i - 2], points[i - 1], points[i], points[i + 1], points[i + 2], points[i + 3], points[i + 4], points[i + 5], tension, color, context, width, height);
		}
		DrawCurveSegment(points[i - 2], points[i - 1], points[i], points[i + 1], points[i + 2], points[i + 3], points[i + 2], points[i + 3], tension, color, context, width, height);
	}

	public static void DrawCurveClosed(this WriteableBitmap bmp, int[] points, float tension, Color color)
	{
		int color2 = ConvertColor(color);
		bmp.DrawCurveClosed(points, tension, color2);
	}

	public static void DrawCurveClosed(this WriteableBitmap bmp, int[] points, float tension, int color)
	{
		using BitmapContext context = bmp.GetBitmapContext();
		int width = context.Width;
		int height = context.Height;
		int num = points.Length;
		DrawCurveSegment(points[num - 2], points[num - 1], points[0], points[1], points[2], points[3], points[4], points[5], tension, color, context, width, height);
		int i;
		for (i = 2; i < num - 4; i += 2)
		{
			DrawCurveSegment(points[i - 2], points[i - 1], points[i], points[i + 1], points[i + 2], points[i + 3], points[i + 4], points[i + 5], tension, color, context, width, height);
		}
		DrawCurveSegment(points[i - 2], points[i - 1], points[i], points[i + 1], points[i + 2], points[i + 3], points[0], points[1], tension, color, context, width, height);
		DrawCurveSegment(points[i], points[i + 1], points[i + 2], points[i + 3], points[0], points[1], points[2], points[3], tension, color, context, width, height);
	}

	public static WriteableBitmap Crop(this WriteableBitmap bmp, int x, int y, int width, int height)
	{
		using BitmapContext src = bmp.GetBitmapContext();
		int width2 = src.Width;
		int height2 = src.Height;
		if (x > width2 || y > height2)
		{
			return BitmapFactory.New(0, 0);
		}
		if (x < 0)
		{
			x = 0;
		}
		if (x + width > width2)
		{
			width = width2 - x;
		}
		if (y < 0)
		{
			y = 0;
		}
		if (y + height > height2)
		{
			height = height2 - y;
		}
		WriteableBitmap val = BitmapFactory.New(width, height);
		using BitmapContext dest = val.GetBitmapContext();
		for (int i = 0; i < height; i++)
		{
			int srcOffset = ((y + i) * width2 + x) * 4;
			int destOffset = i * width * 4;
			BitmapContext.BlockCopy(src, srcOffset, dest, destOffset, width * 4);
		}
		return val;
	}

	public static WriteableBitmap Crop(this WriteableBitmap bmp, Rect region)
	{
		return bmp.Crop((int)region.X, (int)region.Y, (int)region.Width, (int)region.Height);
	}

	public static WriteableBitmap Resize(this WriteableBitmap bmp, int width, int height, Interpolation interpolation)
	{
		using BitmapContext srcContext = bmp.GetBitmapContext();
		int[] array = Resize(srcContext, srcContext.Width, srcContext.Height, width, height, interpolation);
		WriteableBitmap val = BitmapFactory.New(width, height);
		using (BitmapContext dest = val.GetBitmapContext())
		{
			BitmapContext.BlockCopy(array, 0, dest, 0, 4 * array.Length);
		}
		return val;
	}

	public static int[] Resize(BitmapContext srcContext, int widthSource, int heightSource, int width, int height, Interpolation interpolation)
	{
		return Resize(srcContext.Pixels, widthSource, heightSource, width, height, interpolation);
	}

	public static int[] Resize(int[] pixels, int widthSource, int heightSource, int width, int height, Interpolation interpolation)
	{
		int[] array = new int[width * height];
		float num = (float)widthSource / (float)width;
		float num2 = (float)heightSource / (float)height;
		switch (interpolation)
		{
		case Interpolation.NearestNeighbor:
		{
			int num20 = 0;
			for (int k = 0; k < height; k++)
			{
				for (int l = 0; l < width; l++)
				{
					float num4 = (float)l * num;
					float num5 = (float)k * num2;
					int num6 = (int)num4;
					int num7 = (int)num5;
					array[num20++] = pixels[num7 * widthSource + num6];
				}
			}
			break;
		}
		case Interpolation.Bilinear:
		{
			int num3 = 0;
			for (int i = 0; i < height; i++)
			{
				for (int j = 0; j < width; j++)
				{
					float num4 = (float)j * num;
					float num5 = (float)i * num2;
					int num6 = (int)num4;
					int num7 = (int)num5;
					float num8 = num4 - (float)num6;
					float num9 = num5 - (float)num7;
					float num10 = 1f - num8;
					float num11 = 1f - num9;
					int num12 = num6 + 1;
					if (num12 >= widthSource)
					{
						num12 = num6;
					}
					int num13 = num7 + 1;
					if (num13 >= heightSource)
					{
						num13 = num7;
					}
					int num14 = pixels[num7 * widthSource + num6];
					byte b = (byte)(num14 >> 24);
					byte b2 = (byte)(num14 >> 16);
					byte b3 = (byte)(num14 >> 8);
					byte b4 = (byte)num14;
					num14 = pixels[num7 * widthSource + num12];
					byte b5 = (byte)(num14 >> 24);
					byte b6 = (byte)(num14 >> 16);
					byte b7 = (byte)(num14 >> 8);
					byte b8 = (byte)num14;
					num14 = pixels[num13 * widthSource + num6];
					byte b9 = (byte)(num14 >> 24);
					byte b10 = (byte)(num14 >> 16);
					byte b11 = (byte)(num14 >> 8);
					byte b12 = (byte)num14;
					num14 = pixels[num13 * widthSource + num12];
					byte b13 = (byte)(num14 >> 24);
					byte b14 = (byte)(num14 >> 16);
					byte b15 = (byte)(num14 >> 8);
					byte b16 = (byte)num14;
					float num15 = num10 * (float)(int)b + num8 * (float)(int)b5;
					float num16 = num10 * (float)(int)b9 + num8 * (float)(int)b13;
					byte b17 = (byte)(num11 * num15 + num9 * num16);
					num15 = num10 * (float)(int)b2 + num8 * (float)(int)b6;
					num16 = num10 * (float)(int)b10 + num8 * (float)(int)b14;
					float num17 = num11 * num15 + num9 * num16;
					num15 = num10 * (float)(int)b3 + num8 * (float)(int)b7;
					num16 = num10 * (float)(int)b11 + num8 * (float)(int)b15;
					float num18 = num11 * num15 + num9 * num16;
					num15 = num10 * (float)(int)b4 + num8 * (float)(int)b8;
					num16 = num10 * (float)(int)b12 + num8 * (float)(int)b16;
					float num19 = num11 * num15 + num9 * num16;
					byte b18 = (byte)num17;
					byte b19 = (byte)num18;
					byte b20 = (byte)num19;
					array[num3++] = (b17 << 24) | (b18 << 16) | (b19 << 8) | b20;
				}
			}
			break;
		}
		}
		return array;
	}

	public static WriteableBitmap Rotate(this WriteableBitmap bmp, int angle)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int width = bitmapContext.Width;
		int height = bitmapContext.Height;
		int[] pixels = bitmapContext.Pixels;
		int num = 0;
		WriteableBitmap val = null;
		angle %= 360;
		if (angle > 0 && angle <= 90)
		{
			val = BitmapFactory.New(height, width);
			using BitmapContext bitmapContext2 = val.GetBitmapContext();
			int[] pixels2 = bitmapContext2.Pixels;
			for (int i = 0; i < width; i++)
			{
				for (int num2 = height - 1; num2 >= 0; num2--)
				{
					int num3 = num2 * width + i;
					pixels2[num] = pixels[num3];
					num++;
				}
			}
		}
		else if (angle > 90 && angle <= 180)
		{
			val = BitmapFactory.New(width, height);
			using BitmapContext bitmapContext3 = val.GetBitmapContext();
			int[] pixels3 = bitmapContext3.Pixels;
			for (int num4 = height - 1; num4 >= 0; num4--)
			{
				for (int num5 = width - 1; num5 >= 0; num5--)
				{
					int num6 = num4 * width + num5;
					pixels3[num] = pixels[num6];
					num++;
				}
			}
		}
		else if (angle > 180 && angle <= 270)
		{
			val = BitmapFactory.New(height, width);
			using BitmapContext bitmapContext4 = val.GetBitmapContext();
			int[] pixels4 = bitmapContext4.Pixels;
			for (int num7 = width - 1; num7 >= 0; num7--)
			{
				for (int j = 0; j < height; j++)
				{
					int num8 = j * width + num7;
					pixels4[num] = pixels[num8];
					num++;
				}
			}
		}
		else
		{
			val = bmp.Clone();
		}
		return val;
	}

	public static WriteableBitmap RotateFree(this WriteableBitmap bmp, double angle, bool crop = true)
	{
		double num = -Math.PI / 180.0 * angle;
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int width = bitmapContext.Width;
		int height = bitmapContext.Height;
		int num2;
		int num3;
		if (crop)
		{
			num2 = width;
			num3 = height;
		}
		else
		{
			double num4 = angle / (180.0 / Math.PI);
			num2 = (int)Math.Ceiling(Math.Abs(Math.Sin(num4) * (double)height) + Math.Abs(Math.Cos(num4) * (double)width));
			num3 = (int)Math.Ceiling(Math.Abs(Math.Sin(num4) * (double)width) + Math.Abs(Math.Cos(num4) * (double)height));
		}
		int num5 = width / 2;
		int num6 = height / 2;
		int num7 = num2 / 2;
		int num8 = num3 / 2;
		WriteableBitmap val = BitmapFactory.New(num2, num3);
		using BitmapContext bitmapContext2 = val.GetBitmapContext();
		int[] pixels = bitmapContext2.Pixels;
		int[] pixels2 = bitmapContext.Pixels;
		int width2 = bitmapContext.Width;
		for (int i = 0; i < num3; i++)
		{
			for (int j = 0; j < num2; j++)
			{
				int num9 = j - num7;
				int num10 = num8 - i;
				double num11 = Math.Sqrt(num9 * num9 + num10 * num10);
				double num12;
				if (num9 == 0)
				{
					if (num10 == 0)
					{
						pixels[i * num2 + j] = pixels2[num6 * width2 + num5];
						continue;
					}
					num12 = ((num10 >= 0) ? (Math.PI / 2.0) : 4.71238898038469);
				}
				else
				{
					num12 = Math.Atan2(num10, num9);
				}
				num12 -= num;
				double num13 = num11 * Math.Cos(num12);
				double num14 = num11 * Math.Sin(num12);
				num13 += (double)num5;
				num14 = (double)num6 - num14;
				int num15 = (int)Math.Floor(num13);
				int num16 = (int)Math.Floor(num14);
				int num17 = (int)Math.Ceiling(num13);
				int num18 = (int)Math.Ceiling(num14);
				if (num15 >= 0 && num17 >= 0 && num15 < width && num17 < width && num16 >= 0 && num18 >= 0 && num16 < height && num18 < height)
				{
					double num19 = num13 - (double)num15;
					double num20 = num14 - (double)num16;
					int num21 = pixels2[num16 * width2 + num15];
					int num22 = pixels2[num16 * width2 + num17];
					int num23 = pixels2[num18 * width2 + num15];
					int num24 = pixels2[num18 * width2 + num17];
					double num25 = (1.0 - num19) * (double)((num21 >> 24) & 0xFF) + num19 * (double)((num22 >> 24) & 0xFF);
					double num26 = (1.0 - num19) * (double)((num21 >> 16) & 0xFF) + num19 * (double)((num22 >> 16) & 0xFF);
					double num27 = (1.0 - num19) * (double)((num21 >> 8) & 0xFF) + num19 * (double)((num22 >> 8) & 0xFF);
					double num28 = (1.0 - num19) * (double)(num21 & 0xFF) + num19 * (double)(num22 & 0xFF);
					double num29 = (1.0 - num19) * (double)((num23 >> 24) & 0xFF) + num19 * (double)((num24 >> 24) & 0xFF);
					double num30 = (1.0 - num19) * (double)((num23 >> 16) & 0xFF) + num19 * (double)((num24 >> 16) & 0xFF);
					double num31 = (1.0 - num19) * (double)((num23 >> 8) & 0xFF) + num19 * (double)((num24 >> 8) & 0xFF);
					double num32 = (1.0 - num19) * (double)(num23 & 0xFF) + num19 * (double)(num24 & 0xFF);
					int num33 = (int)Math.Round((1.0 - num20) * num26 + num20 * num30);
					int num34 = (int)Math.Round((1.0 - num20) * num27 + num20 * num31);
					int num35 = (int)Math.Round((1.0 - num20) * num28 + num20 * num32);
					int num36 = (int)Math.Round((1.0 - num20) * num25 + num20 * num29);
					if (num33 < 0)
					{
						num33 = 0;
					}
					if (num33 > 255)
					{
						num33 = 255;
					}
					if (num34 < 0)
					{
						num34 = 0;
					}
					if (num34 > 255)
					{
						num34 = 255;
					}
					if (num35 < 0)
					{
						num35 = 0;
					}
					if (num35 > 255)
					{
						num35 = 255;
					}
					if (num36 < 0)
					{
						num36 = 0;
					}
					if (num36 > 255)
					{
						num36 = 255;
					}
					int num37 = num36 + 1;
					pixels[i * num2 + j] = (num36 << 24) | ((byte)(num33 * num37 >> 8) << 16) | ((byte)(num34 * num37 >> 8) << 8) | (byte)(num35 * num37 >> 8);
				}
			}
		}
		return val;
	}

	public static WriteableBitmap Flip(this WriteableBitmap bmp, FlipMode flipMode)
	{
		using BitmapContext bitmapContext = bmp.GetBitmapContext();
		int width = bitmapContext.Width;
		int height = bitmapContext.Height;
		int[] pixels = bitmapContext.Pixels;
		int num = 0;
		WriteableBitmap val = null;
		switch (flipMode)
		{
		case FlipMode.Horizontal:
		{
			val = BitmapFactory.New(width, height);
			using (BitmapContext bitmapContext3 = val.GetBitmapContext())
			{
				int[] pixels3 = bitmapContext3.Pixels;
				for (int num4 = height - 1; num4 >= 0; num4--)
				{
					for (int j = 0; j < width; j++)
					{
						int num5 = num4 * width + j;
						pixels3[num] = pixels[num5];
						num++;
					}
				}
			}
			break;
		}
		case FlipMode.Vertical:
		{
			val = BitmapFactory.New(width, height);
			using (BitmapContext bitmapContext2 = val.GetBitmapContext())
			{
				int[] pixels2 = bitmapContext2.Pixels;
				for (int i = 0; i < height; i++)
				{
					for (int num2 = width - 1; num2 >= 0; num2--)
					{
						int num3 = i * width + num2;
						pixels2[num] = pixels[num3];
						num++;
					}
				}
			}
			break;
		}
		}
		return val;
	}
}
