using System;
using System.Globalization;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using Vine.Datas;
using Vine.Resources;
using Vine.Services;

namespace Vine.Utils;

public class ServiceUtils
{
	private static DateTime lastdatetime = DateTime.MinValue;

	public static async Task ResetPassword(string email, Action<bool> callback)
	{
		if (string.IsNullOrEmpty(email))
		{
			ToastHelper.Show(AppResources.ToastPleaseSetEmailFirst, afternav: false, (Orientation)0);
		}
		else
		{
			if ((int)MessageBox.Show(AppResources.ToastResetPasswordMessage, AppResources.ToastResetPasswordTitle, (MessageBoxButton)1) != 1)
			{
				return;
			}
			try
			{
				if (await VineService.Instance.ResetPasswordAsync(email))
				{
					ToastHelper.Show(AppResources.ToastResetPasswordInstructions, afternav: false, (Orientation)0);
				}
				else
				{
					ToastHelper.Show(AppResources.ToastCantResetPassword, afternav: false, (Orientation)0);
				}
				callback(obj: true);
			}
			catch (ServiceServerErrorException ex)
			{
				ToastHelper.Show(ex.HttpErrorMessage ?? AppResources.ToastCantResetPassword, afternav: false, (Orientation)0);
				callback(obj: false);
			}
		}
	}

	internal static void ManageCheckPoint(string info)
	{
		if (string.IsNullOrEmpty(info) || (DateTime.Now - lastdatetime).TotalMinutes <= 1.0)
		{
			return;
		}
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			//IL_0024: Unknown result type (might be due to invalid IL or missing references)
			//IL_002a: Invalid comparison between Unknown and I4
			if ((int)MessageBox.Show(string.Format(AppResources.CheckpointMessage + "\n\n" + AppResources.CheckpointEnterCredential, AppVersion.ServiceName), "Vine checkpoint", (MessageBoxButton)1) == 1)
			{
				DataUser currentUser = DatasProvider.Instance.CurrentUser;
				NavigationServiceExt.ToCheckpoint((info + "?ed=" + currentUser.User.Edition) ?? CultureInfo.CurrentCulture.TwoLetterISOLanguageName.ToUpper());
			}
		});
	}
}
