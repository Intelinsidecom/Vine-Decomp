using System;
using System.Globalization;

namespace Vine.Models;

public class Milestone
{
	public string OverlayColor { get; set; }

	public string BackgroundImageUrl { get; set; }

	public string Title { get; set; }

	public string OverlayAlpha { get; set; }

	public string IconUrl { get; set; }

	public string GaussianBlurRadius { get; set; }

	public string BackgroundVideoUrl { get; set; }

	public string MilestoneUrl { get; set; }

	public double OverlayOpacity => Convert.ToDouble(OverlayAlpha, CultureInfo.InvariantCulture);

	public string OverlayHex => OverlayColor.Replace("0x", "#");
}
