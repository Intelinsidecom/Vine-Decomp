using Vine.Models;
using Windows.Media.Editing;
using Windows.UI.Xaml.Media;

namespace Vine.Views.Capture;

public class EditClipsViewModel : NotifyObject
{
	private ImageSource _thumb;

	private bool _isActive;

	private double _opacity = 0.4;

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

	public MediaComposition Composition { get; set; }

	public MediaClip MediaClip { get; set; }

	public RecordingClipModel ClipModel { get; set; }

	public bool IsActive
	{
		get
		{
			return _isActive;
		}
		set
		{
			SetProperty(ref _isActive, value, "IsActive");
		}
	}

	public double Opacity
	{
		get
		{
			return _opacity;
		}
		set
		{
			SetProperty(ref _opacity, value, "Opacity");
		}
	}

	public bool IsPlaying
	{
		get
		{
			return Opacity == 1.0;
		}
		set
		{
			if (value)
			{
				Opacity = 1.0;
			}
			else
			{
				Opacity = 0.4;
			}
		}
	}

	public MediaClip ToMediaClip()
	{
		MediaClip val = MediaClip.Clone();
		ClipModel.ToMediaClip(val);
		return val;
	}
}
