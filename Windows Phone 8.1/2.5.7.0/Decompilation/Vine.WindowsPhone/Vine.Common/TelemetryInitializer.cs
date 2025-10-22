using System.Threading.Tasks;
using Microsoft.ApplicationInsights;
using Microsoft.ApplicationInsights.Channel;
using Microsoft.ApplicationInsights.Extensibility;
using Vine.Models;

namespace Vine.Common;

public class TelemetryInitializer : ITelemetryInitializer
{
	public void Initialize(ITelemetry telemetry)
	{
		string value = ((!ApplicationSettings.Current.IsNotLoggedIn) ? ApplicationSettings.Current.UserId : "loggedout");
		telemetry.Context.Properties["userId"] = value;
	}

	public static async Task InitializeAsync()
	{
		await WindowsAppInitializer.InitializeAsync("cf23c5ab-0c73-469b-911d-c6dd1578a53a", WindowsCollectors.Metadata | WindowsCollectors.Session);
		TelemetryConfiguration.Active.TelemetryInitializers.Add(new TelemetryInitializer());
		TelemetryConfiguration.Active.TelemetryChannel = new PersistenceChannel();
	}
}
