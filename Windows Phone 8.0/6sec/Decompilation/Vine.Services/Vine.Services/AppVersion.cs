using System.Threading.Tasks;
using Windows.ApplicationModel.Store;

namespace Vine.Services;

public class AppVersion
{
	private static string InAppAds;

	private static string InAppUpload;

	private static LicenseInformation _licenseInfo;

	public static string Version { get; set; }

	public static string AppName { get; set; }

	public static string ServiceName { get; set; }

	static AppVersion()
	{
		InAppAds = "6Sec_RemoveAd";
		InAppUpload = "6Sec_Upload";
		Version = "3.0";
		_licenseInfo = CurrentApp.LicenseInformation;
		AppName = "6sec";
		ServiceName = "Vine";
	}

	public static bool IsHaveAds()
	{
		try
		{
			return !_licenseInfo.ProductLicenses[InAppAds].IsActive;
		}
		catch
		{
		}
		return true;
	}

	public static bool IsCanUpload()
	{
		return true;
	}

	public static async Task<bool> BuyAds()
	{
		_ = 1;
		try
		{
			await CurrentApp.RequestProductPurchaseAsync(InAppAds, false);
			return true;
		}
		catch
		{
		}
		return false;
	}

	public static async Task<bool> BuyUpload()
	{
		_ = 1;
		try
		{
			await CurrentApp.RequestProductPurchaseAsync(InAppUpload, false);
			return true;
		}
		catch
		{
		}
		return false;
	}
}
