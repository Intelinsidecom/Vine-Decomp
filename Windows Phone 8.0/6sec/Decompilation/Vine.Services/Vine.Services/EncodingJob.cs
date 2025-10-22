using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Windows;
using Gen.Services;
using Microsoft.Phone.BackgroundTransfer;
using Vine.Services.Foursquare;

namespace Vine.Services;

public class EncodingJob : INotifyPropertyChanged
{
	private BackgroundTransferRequest _exrequest;

	public bool Saved { get; set; }

	public string Id { get; set; }

	public DateTime Date { get; set; }

	public EncodingStep State { get; set; }

	public string CurrentRequestId { get; set; }

	public string RemoteMyServerJobId { get; set; }

	public string LocalVideoPath { get; set; }

	public List<IPerson> CommentPeople { get; set; }

	public string LocalImagePath { get; set; }

	public string UserId { get; set; }

	public double Duration { get; set; }

	public double Progress { get; set; }

	public string AmazonVideo { get; set; }

	public string AmazonCapture { get; set; }

	public string VineComment { get; set; }

	public bool VineShareTwitter { get; set; }

	public bool VineShareFacebook { get; set; }

	public Venue Place { get; set; }

	public string FoursquareRequestId { get; set; }

	public string HashEncoding { get; set; }

	public string LocalEncodingFile { get; set; }

	public EncodingStep PreviousState { get; set; }

	public long ChannelId { get; set; }

	public string ChannelName { get; set; }

	public string ServiceItemId { get; set; }

	public bool VineShareTumblr { get; set; }

	public bool VineShareVK { get; set; }

	public bool VineShareFlickr { get; set; }

	public bool VineShareFoursquare { get; set; }

	public double Longitude { get; set; }

	public double Latitude { get; set; }

	public bool UseGeoloc { get; set; }

	public bool IsDirect { get; set; }

	public List<EncodingJobReceiver> SelectedUsers { get; set; }

	public string UriStatus { get; set; }

	public bool NoNeedEncoding { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public EncodingJob()
	{
		CommentPeople = new List<IPerson>();
	}

	public void ChangeUserId(string newid)
	{
		UserId = newid;
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			RaisePropertyChanged("UserId");
		});
	}

	public void UpdateState()
	{
		Progress = 0.0;
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			RaisePropertyChanged("Progress");
			RaisePropertyChanged("State");
		});
	}

	public void RaisePropertyChanged(string name)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(name));
		}
	}

	public void Register(BackgroundTransferRequest request)
	{
		if (_exrequest != null)
		{
			_exrequest.TransferProgressChanged -= requestReceived_TransferProgressChanged;
			_exrequest.TransferProgressChanged -= requestSent_TransferProgressChanged;
		}
		if (request.Method == "GET")
		{
			request.TransferProgressChanged += requestReceived_TransferProgressChanged;
		}
		else
		{
			request.TransferProgressChanged += requestSent_TransferProgressChanged;
		}
		_exrequest = request;
	}

	private void requestSent_TransferProgressChanged(object sender, BackgroundTransferEventArgs e)
	{
		if (e.Request.TotalBytesToSend == 0L)
		{
			SetProgress(0.0);
		}
		else
		{
			SetProgress((double)e.Request.BytesSent / (double)e.Request.TotalBytesToSend);
		}
	}

	public void SetProgress(double val)
	{
		Progress = val;
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			RaisePropertyChanged("Progress");
		});
	}

	private void requestReceived_TransferProgressChanged(object sender, BackgroundTransferEventArgs e)
	{
		if (e.Request.TotalBytesToReceive == 0L)
		{
			SetProgress(0.0);
		}
		else
		{
			SetProgress((double)e.Request.BytesReceived / (double)e.Request.TotalBytesToReceive);
		}
	}

	public void SetPlace(Venue venue, string foursquareRequestId)
	{
		Place = venue;
		FoursquareRequestId = foursquareRequestId;
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			RaisePropertyChanged("Place");
		});
	}

	public void SetChannel(long id, string name)
	{
		ChannelId = id;
		ChannelName = name;
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			RaisePropertyChanged("ChannelName");
		});
	}
}
