using System;
using System.Net.NetworkInformation;
using Windows.Networking.Connectivity;

namespace Vine.Framework;

public static class NetworkHelper
{
	public static bool CheckForConnectivity()
	{
		//IL_0018: Unknown result type (might be due to invalid IL or missing references)
		//IL_001d: Unknown result type (might be due to invalid IL or missing references)
		//IL_001e: Unknown result type (might be due to invalid IL or missing references)
		//IL_0020: Invalid comparison between Unknown and I4
		//IL_0022: Unknown result type (might be due to invalid IL or missing references)
		//IL_0024: Invalid comparison between Unknown and I4
		if (!NetworkInterface.GetIsNetworkAvailable())
		{
			return false;
		}
		try
		{
			ConnectionProfile internetConnectionProfile = NetworkInformation.GetInternetConnectionProfile();
			if (internetConnectionProfile == null)
			{
				return false;
			}
			NetworkConnectivityLevel networkConnectivityLevel = internetConnectionProfile.GetNetworkConnectivityLevel();
			if ((int)networkConnectivityLevel != 3 && (int)networkConnectivityLevel != 2)
			{
				return false;
			}
			return true;
		}
		catch (Exception)
		{
			return false;
		}
	}
}
