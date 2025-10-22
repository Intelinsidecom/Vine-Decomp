using Windows.Security.Cryptography;
using Windows.Security.Cryptography.Core;
using Windows.Storage.Streams;

namespace Vine.Framework;

public static class HashHelper
{
	public static string GetHash(string algoritm, string s)
	{
		HashAlgorithmProvider obj = HashAlgorithmProvider.OpenAlgorithm(algoritm);
		IBuffer val = CryptographicBuffer.ConvertStringToBinary(s, (BinaryStringEncoding)0);
		return CryptographicBuffer.EncodeToHexString(obj.HashData(val));
	}
}
