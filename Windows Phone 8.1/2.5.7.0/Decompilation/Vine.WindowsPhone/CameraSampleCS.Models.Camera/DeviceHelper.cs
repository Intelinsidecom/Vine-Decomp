using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using Windows.Devices.Enumeration;

namespace CameraSampleCS.Models.Camera;

public static class DeviceHelper
{
	private static readonly object SyncRoot = new object();

	private static volatile IDictionary<Panel, DeviceInformation> CameraDeviceInfoCache;

	public static async Task<DeviceInformation> GetCameraDeviceInfoAsync(CameraType cameraType)
	{
		Panel desiredPanel = (Panel)(cameraType switch
		{
			CameraType.Primary => 2, 
			CameraType.FrontFacing => 1, 
			_ => throw new ArgumentException(string.Format(CultureInfo.InvariantCulture, "Camera type {0} is not supported.", new object[1] { cameraType }), "cameraType"), 
		});
		await EnumerateCameraDevicesAsync();
		return CameraDeviceInfoCache.ContainsKey(desiredPanel) ? CameraDeviceInfoCache[desiredPanel] : null;
	}

	private static async Task EnumerateCameraDevicesAsync()
	{
		if (CameraDeviceInfoCache != null)
		{
			return;
		}
		IDictionary<Panel, DeviceInformation> devices = new Dictionary<Panel, DeviceInformation>();
		foreach (DeviceInformation item in ((IEnumerable<DeviceInformation>)(await DeviceInformation.FindAllAsync((DeviceClass)4))).Where((DeviceInformation d) => d.IsEnabled && d.EnclosureLocation != null))
		{
			devices[item.EnclosureLocation.Panel] = item;
		}
		lock (SyncRoot)
		{
			if (CameraDeviceInfoCache == null)
			{
				CameraDeviceInfoCache = devices;
			}
		}
	}
}
