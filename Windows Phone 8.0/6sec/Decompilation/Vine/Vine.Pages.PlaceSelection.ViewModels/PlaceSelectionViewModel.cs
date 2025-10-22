using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Windows;
using Vine.Datas;
using Vine.Resources;
using Vine.Services.Foursquare;
using Windows.Devices.Geolocation;

namespace Vine.Pages.PlaceSelection.ViewModels;

public class PlaceSelectionViewModel : INotifyPropertyChanged
{
	private double? _latitude;

	private double? _longitude;

	public List<Venue> Places { get; set; }

	public string FoursquareRequestId { get; set; }

	public bool IsLoading { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public async void LoadPlace(string search = null)
	{
		IsLoading = true;
		RaisePropertyChanged("IsLoading");
		try
		{
			if (!_latitude.HasValue || !_longitude.HasValue)
			{
				Geolocator val = new Geolocator();
				val.put_DesiredAccuracy((PositionAccuracy)0);
				Geoposition val2 = await val.GetGeopositionAsync();
				_latitude = val2.Coordinate.Latitude;
				_longitude = val2.Coordinate.Longitude;
				if (_latitude.HasValue && _longitude.HasValue)
				{
					Vine.Datas.Datas instance = DatasProvider.Instance;
					instance.LastLatitude = _latitude.Value;
					instance.LastLongitude = _longitude.Value;
				}
			}
			FoursquareService.GetPlaceAroundMe(search, _latitude.Value, _longitude.Value, delegate(List<Venue> res, string requestid)
			{
				IsLoading = false;
				FoursquareRequestId = requestid;
				if (!string.IsNullOrWhiteSpace(search))
				{
					if (res == null)
					{
						res = new List<Venue>();
					}
					res.Insert(0, new Venue
					{
						IsCustom = true,
						Subtitle = AppResources.CreateACustomLocation,
						Title = string.Format(AppResources.AddALocation, search),
						name = search
					});
				}
				Places = res;
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					RaisePropertyChanged("IsLoading");
					RaisePropertyChanged("Places");
				});
			});
		}
		catch
		{
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				IsLoading = false;
				((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
				{
					//IL_0010: Unknown result type (might be due to invalid IL or missing references)
					RaisePropertyChanged("IsLoading");
					MessageBox.Show(AppResources.ToastCantFindYourPosition);
				});
			});
		}
	}

	private void RaisePropertyChanged(string p)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(p));
		}
	}

	internal void SetPosition(double latitude, double longitude)
	{
		_latitude = latitude;
		_longitude = longitude;
	}
}
