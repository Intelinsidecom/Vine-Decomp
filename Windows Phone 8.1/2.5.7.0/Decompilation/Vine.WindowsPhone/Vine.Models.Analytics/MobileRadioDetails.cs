using Newtonsoft.Json;

namespace Vine.Models.Analytics;

public class MobileRadioDetails
{
	[JsonProperty("mobile_network_operator_code")]
	public string MobileNetworkOperatorCode { get; set; }

	[JsonProperty("mobile_network_operator_iso_country_code")]
	public string MobileNetworkOperatorIsoCountryCode { get; set; }

	[JsonProperty("mobile_network_operator_name")]
	public string MobileNetworkOperatorName { get; set; }
}
