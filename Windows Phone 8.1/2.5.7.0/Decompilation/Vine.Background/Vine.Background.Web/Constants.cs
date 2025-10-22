namespace Vine.Background.Web;

internal static class Constants
{
	public const sbyte API_MAGIC = 21;

	private static readonly sbyte[] RAW_API_KEY;

	private static readonly sbyte[] RAW_API_SECRET;

	public static readonly string VINE_API_KEY;

	public static readonly string VINE_API_SECRET;

	public static extern string Transform(sbyte magic, sbyte[] m);
}
