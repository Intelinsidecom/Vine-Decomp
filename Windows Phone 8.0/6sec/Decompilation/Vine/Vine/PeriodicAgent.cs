using System;
using System.Windows;
using Microsoft.Phone.Scheduler;
using Vine.Resources;
using Vine.Services;
using Windows.ApplicationModel.Background;

namespace Vine;

public class PeriodicAgent
{
	private static string periodicTaskName = "MyScheduledTaskAgent";

	public static string resourceIntensiveTaskName = "IntensiveTask";

	public static bool HasPeriodicAgent()
	{
		return ScheduledActionService.Find("MyScheduledTaskAgent") != null;
	}

	internal static ScheduledAction GetPeriodAgent()
	{
		return ScheduledActionService.Find(periodicTaskName);
	}

	private static void RemoveAgent(string name)
	{
		try
		{
			ScheduledActionService.Remove(name);
		}
		catch (Exception)
		{
		}
	}

	public static void StartResourceIntensiveAgent()
	{
		//IL_0022: Unknown result type (might be due to invalid IL or missing references)
		//IL_0028: Expected O, but got Unknown
		ScheduledAction obj = ScheduledActionService.Find(resourceIntensiveTaskName);
		ResourceIntensiveTask val = (ResourceIntensiveTask)(object)((obj is ResourceIntensiveTask) ? obj : null);
		if (val != null)
		{
			RemoveAgent(resourceIntensiveTaskName);
		}
		val = new ResourceIntensiveTask(resourceIntensiveTaskName);
		((ScheduledTask)val).Description = "resource-intensive task is necessary to update live tile";
		try
		{
			ScheduledActionService.Add((ScheduledAction)(object)val);
		}
		catch (InvalidOperationException)
		{
		}
	}

	public static PeriodicTask StartPeriodicAgent()
	{
		//IL_0022: Unknown result type (might be due to invalid IL or missing references)
		//IL_0027: Unknown result type (might be due to invalid IL or missing references)
		//IL_003d: Expected O, but got Unknown
		ScheduledAction obj = ScheduledActionService.Find(periodicTaskName);
		PeriodicTask val = (PeriodicTask)(object)((obj is PeriodicTask) ? obj : null);
		if (val != null)
		{
			RemoveAgent(periodicTaskName);
		}
		val = new PeriodicTask(periodicTaskName)
		{
			Description = AppVersion.AppName + " agent is used to update live tiles periodically"
		};
		try
		{
			BackgroundExecutionManager.RequestAccessAsync();
		}
		catch
		{
		}
		try
		{
			ScheduledActionService.Add((ScheduledAction)(object)val);
			return val;
		}
		catch (InvalidOperationException ex)
		{
			if (ex.Message.Contains("BNS Error: The action is disabled"))
			{
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					//IL_0005: Unknown result type (might be due to invalid IL or missing references)
					MessageBox.Show(AppResources.BackgroundAgentProblem);
				});
			}
			return null;
		}
	}

	internal static void RemovePeriodicAgent()
	{
		if (ScheduledActionService.Find(periodicTaskName) is PeriodicTask)
		{
			RemoveAgent(periodicTaskName);
		}
	}

	internal static void RemoveRessourceIntensive()
	{
		if (ScheduledActionService.Find(resourceIntensiveTaskName) is PeriodicTask)
		{
			RemoveAgent(resourceIntensiveTaskName);
		}
	}
}
