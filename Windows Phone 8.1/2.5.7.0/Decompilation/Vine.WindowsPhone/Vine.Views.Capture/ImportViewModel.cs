using Vine.Models;
using Windows.Storage.Streams;
using Windows.UI.Xaml.Media;

namespace Vine.Views.Capture;

public class ImportViewModel : NotifyObject
{
	private ImageSource _thumb;

	public IRandomAccessStream Stream { get; set; }

	public ImageSource Thumb
	{
		get
		{
			return _thumb;
		}
		set
		{
			SetProperty(ref _thumb, value, "Thumb");
		}
	}
}
