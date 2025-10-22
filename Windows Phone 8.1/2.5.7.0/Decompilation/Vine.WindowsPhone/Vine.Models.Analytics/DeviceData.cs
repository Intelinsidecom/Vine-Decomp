using System;
using System.Collections.Generic;
using System.Linq;
using Newtonsoft.Json;
using NodaTime;
using Windows.Devices.Sensors;
using Windows.Networking.Connectivity;
using Windows.Phone.Devices.Power;
using Windows.Security.ExchangeActiveSyncProvisioning;
using Windows.System.UserProfile;

namespace Vine.Models.Analytics;

public class DeviceData
{
	[JsonProperty("os")]
	public string OS { get; set; }

	[JsonProperty("os_version")]
	public string OSVersion { get; set; }

	[JsonProperty("manufacturer")]
	public string Manufacturer { get; set; }

	[JsonProperty("device_name")]
	public string DeviceName { get; set; }

	[JsonProperty("device_model")]
	public string DeviceModel { get; set; }

	[JsonProperty("battery_level")]
	public double BatteryLevel { get; set; }

	[JsonProperty("brightness")]
	public double? Brightness { get; set; }

	[JsonProperty("orientation")]
	public ScribeOrientation? Orientation { get; set; }

	[JsonProperty("internet_access_type")]
	public InternetAccessType InternetAccessType { get; set; }

	[JsonProperty("language_codes")]
	public string[] LanguageCodes { get; set; }

	[JsonProperty("radio_details")]
	public MobileRadioDetails RadioDetails { get; set; }

	[JsonProperty("timezone")]
	public string TimeZone { get; set; }

	public static DeviceData GetDefault()
	{
		DeviceData deviceData = new DeviceData();
		try
		{
			SetDeviceInfo(deviceData);
			deviceData.OSVersion = ApplicationSettings.Current.OSVersion;
			deviceData.TimeZone = GetTimeZone();
			deviceData.InternetAccessType = GetInternetAccessType();
			deviceData.LanguageCodes = GlobalizationPreferences.Languages.ToArray();
			string mCC = ApplicationSettings.Current.MCC;
			string mNC = ApplicationSettings.Current.MNC;
			if (deviceData.InternetAccessType == InternetAccessType.MOBILE && !string.IsNullOrEmpty(mCC))
			{
				deviceData.RadioDetails = new MobileRadioDetails
				{
					MobileNetworkOperatorCode = mNC,
					MobileNetworkOperatorIsoCountryCode = mCC,
					MobileNetworkOperatorName = GetMobileNetworkOperatorName()
				};
			}
		}
		catch (Exception)
		{
		}
		return deviceData;
	}

	private static string GetTimeZone()
	{
		try
		{
			return DateTimeZoneProviders.Tzdb.GetSystemDefault().Id;
		}
		catch (Exception)
		{
			return null;
		}
	}

	private static void SetDeviceInfo(DeviceData deviceData)
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Expected O, but got Unknown
		try
		{
			EasClientDeviceInformation val = new EasClientDeviceInformation();
			deviceData.Manufacturer = val.SystemManufacturer;
			deviceData.DeviceName = val.SystemSku;
			deviceData.DeviceModel = val.SystemProductName;
			deviceData.OS = val.OperatingSystem;
			deviceData.BatteryLevel = (float)Battery.GetDefault().RemainingChargePercent / 100f;
		}
		catch (Exception)
		{
		}
	}

	private static ScribeOrientation GetDeviceOrientation()
	{
		//IL_000a: Unknown result type (might be due to invalid IL or missing references)
		//IL_000f: Unknown result type (might be due to invalid IL or missing references)
		//IL_0010: Unknown result type (might be due to invalid IL or missing references)
		//IL_002a: Expected I4, but got Unknown
		try
		{
			SimpleOrientationSensor val = SimpleOrientationSensor.GetDefault();
			if (val != null)
			{
				SimpleOrientation currentOrientation = val.GetCurrentOrientation();
				switch ((int)currentOrientation)
				{
				case 0:
				case 2:
					return ScribeOrientation.PORTRAIT;
				case 1:
				case 3:
					return ScribeOrientation.LANDSCAPE;
				case 4:
					return ScribeOrientation.FACE_UP;
				}
			}
		}
		catch (Exception)
		{
		}
		return ScribeOrientation.PORTRAIT;
	}

	private static InternetAccessType GetInternetAccessType()
	{
		try
		{
			ConnectionProfile internetConnectionProfile = NetworkInformation.GetInternetConnectionProfile();
			if (internetConnectionProfile != null)
			{
				if (internetConnectionProfile.IsWlanConnectionProfile)
				{
					return InternetAccessType.WIFI;
				}
				if (internetConnectionProfile.IsWwanConnectionProfile)
				{
					return InternetAccessType.MOBILE;
				}
			}
		}
		catch (Exception)
		{
		}
		return InternetAccessType.UNREACHABLE;
	}

	private static string GetMobileNetworkOperatorName()
	{
		try
		{
			ConnectionProfile internetConnectionProfile = NetworkInformation.GetInternetConnectionProfile();
			if (internetConnectionProfile != null && internetConnectionProfile.IsWwanConnectionProfile)
			{
				using IEnumerator<string> enumerator = internetConnectionProfile.GetNetworkNames().GetEnumerator();
				if (enumerator.MoveNext())
				{
					return enumerator.Current;
				}
			}
		}
		catch (Exception)
		{
		}
		return null;
	}
}
