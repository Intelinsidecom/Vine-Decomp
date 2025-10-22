using System.Linq;
using Windows.Networking.Connectivity;
using Windows.Security.ExchangeActiveSyncProvisioning;

namespace Vine.Framework;

public static class PhonePerformanceHelper
{
	private static readonly string[] LowPerformingPhones = new string[34]
	{
		"rm-1017", "rm-1018", "rm-1019", "rm-1020", "rm-1031", "rm-1032", "rm-1034", "rm-1062", "rm-1063", "rm-1064",
		"rm-1065", "rm-1066", "rm-1067", "rm-1068", "rm-1069", "rm-1070", "rm-1071", "rm-1072", "rm-1073", "rm-1074",
		"rm-1075", "rm-1077", "rm-846", "rm-914", "rm-917", "rm-941", "rm-974", "rm-975", "rm-976", "rm-977",
		"rm-978", "rm-979", "rm-997", "rm-998"
	};

	private const byte CellGoodSignalThreshold = 3;

	private const byte WifiGoodSignalThreshold = 2;

	private static readonly WwanDataClass[] HighQualityDataClasses = (WwanDataClass[])(object)new WwanDataClass[2]
	{
		(WwanDataClass)32,
		(WwanDataClass)8
	};

	private static PerformanceLevel? _devicePerformanceLevel;

	public static PerformanceLevel DevicePerformance
	{
		get
		{
			//IL_000c: Unknown result type (might be due to invalid IL or missing references)
			if (!_devicePerformanceLevel.HasValue)
			{
				string text = new EasClientDeviceInformation().SystemProductName.ToLower();
				_devicePerformanceLevel = PerformanceLevel.High;
				string[] lowPerformingPhones = LowPerformingPhones;
				foreach (string value in lowPerformingPhones)
				{
					if (text.StartsWith(value))
					{
						_devicePerformanceLevel = PerformanceLevel.Low;
						break;
					}
				}
			}
			return _devicePerformanceLevel.Value;
		}
	}

	public static PerformanceLevel CurrentPerformanceLevel
	{
		get
		{
			if (DevicePerformance == PerformanceLevel.High && NetworkPerformanceLevel == PerformanceLevel.High)
			{
				return PerformanceLevel.High;
			}
			return PerformanceLevel.Low;
		}
	}

	public static PerformanceLevel NetworkPerformanceLevel
	{
		get
		{
			//IL_0068: Unknown result type (might be due to invalid IL or missing references)
			//IL_006d: Unknown result type (might be due to invalid IL or missing references)
			//IL_0074: Unknown result type (might be due to invalid IL or missing references)
			ConnectionProfile internetConnectionProfile = NetworkInformation.GetInternetConnectionProfile();
			if (internetConnectionProfile != null)
			{
				byte? signalBars = internetConnectionProfile.GetSignalBars();
				if (internetConnectionProfile.IsWlanConnectionProfile && signalBars >= 2)
				{
					return PerformanceLevel.High;
				}
				if (internetConnectionProfile.IsWwanConnectionProfile)
				{
					WwanDataClass currentDataClass = internetConnectionProfile.WwanConnectionProfileDetails.GetCurrentDataClass();
					if (!HighQualityDataClasses.Contains(currentDataClass) || !(signalBars >= 3))
					{
						return PerformanceLevel.Low;
					}
					return PerformanceLevel.High;
				}
			}
			return PerformanceLevel.Low;
		}
	}
}
