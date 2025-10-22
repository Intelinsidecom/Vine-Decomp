using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Threading.Tasks;
using Vine.Framework;
using Vine.Views;
using Vine.Web;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace Vine.Models;

public class UploadJobsViewModel : NotifyObject
{
	private static UploadJobsViewModel _current;

	public static UploadJobsViewModel Current => _current ?? (_current = new UploadJobsViewModel());

	public ObservableCollection<UploadJob> Items { get; set; }

	public Visibility ShowOnHomeView
	{
		get
		{
			if (FirstItem != null)
			{
				return (Visibility)0;
			}
			return (Visibility)1;
		}
	}

	public UploadJob FirstItem
	{
		get
		{
			if (!Items.Any())
			{
				return null;
			}
			return Items[0];
		}
	}

	public RetryJobCommand RetryCommand => new RetryJobCommand();

	private UploadJobsViewModel()
	{
		Items = new ObservableCollection<UploadJob>();
		RefreshItems();
	}

	public async Task RefreshItems()
	{
		List<UploadJob> newItemsSource = await ApplicationSettings.Current.GetUploadJobs();
		Items.Repopulate(newItemsSource);
		NotifyOfPropertyChange(() => FirstItem);
		NotifyOfPropertyChange(() => ShowOnHomeView);
	}

	public void Delete(UploadJob job)
	{
		int num = Items.IndexOf(job);
		Items.RemoveAt(num);
		if (num == 0)
		{
			NotifyOfPropertyChange(() => FirstItem);
			NotifyOfPropertyChange(() => ShowOnHomeView);
		}
		ApplicationSettings.Current.SetUploadJobs(Items.ToList());
		job.DeleteAsync();
		if (!Items.Any() && ((ContentControl)App.RootFrame).Content is UploadJobsView)
		{
			App.RootFrame.GoBack();
		}
	}

	public void Add(UploadJob job)
	{
		Run(job);
		Items.Insert(0, job);
		NotifyOfPropertyChange(() => FirstItem);
		NotifyOfPropertyChange(() => ShowOnHomeView);
		ApplicationSettings.Current.SetUploadJobs(Items.ToList());
	}

	public async Task Run(UploadJob job)
	{
		ApiResult result = null;
		await DispatcherEx.InvokeBackground(async delegate
		{
			_ = result;
			result = await job.Execute();
		});
		if (!result.HasError)
		{
			Delete(job);
		}
	}
}
