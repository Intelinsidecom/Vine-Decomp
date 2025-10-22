using System;
using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public abstract class ClientEvent
{
	[JsonProperty("app_state")]
	public AppState AppState { get; set; }

	[JsonProperty("device_data")]
	public DeviceData DeviceData { get; set; }

	[JsonProperty("navigation")]
	public AppNavigation Navigation { get; set; }

	[JsonProperty("client_id")]
	public string ClientId { get; set; }

	[JsonProperty("event_type")]
	public string EventType { get; set; }

	[JsonProperty("event_details")]
	public EventDetails EventDetails { get; set; }

	protected ClientEvent(string eventType)
	{
		try
		{
			EventType = eventType;
			AppState = AppState.GetDefault();
			DeviceData = DeviceData.GetDefault();
			ClientId = "windows/" + ApplicationSettings.Current.ClientVersion;
		}
		catch (Exception)
		{
		}
	}
}
