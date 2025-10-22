using System;
using System.Windows.Input;

namespace Vine.Models;

public class RetryJobCommand : ICommand
{
	public event EventHandler CanExecuteChanged;

	public bool CanExecute(object parameter)
	{
		return true;
	}

	public void Execute(object parameter)
	{
		UploadJob job = (UploadJob)parameter;
		UploadJobsViewModel.Current.Run(job);
	}
}
