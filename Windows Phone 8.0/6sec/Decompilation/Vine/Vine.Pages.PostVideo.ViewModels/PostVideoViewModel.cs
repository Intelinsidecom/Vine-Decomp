using System.Collections.Generic;
using System.ComponentModel;
using Gen.Services;
using Vine.Services;

namespace Vine.Pages.PostVideo.ViewModels;

public class PostVideoViewModel : INotifyPropertyChanged
{
	public List<IPerson> DirectPersons { get; set; }

	public EncodingJob Job { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	private void RaisePropertyChanged(string p)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(p));
		}
	}

	public void SetDirectPersons(List<IPerson> toList)
	{
		DirectPersons = toList;
		RaisePropertyChanged("DirectPersons");
	}

	public void SetCurrentJob(EncodingJob currentJob)
	{
		Job = currentJob;
		RaisePropertyChanged("Job");
	}
}
