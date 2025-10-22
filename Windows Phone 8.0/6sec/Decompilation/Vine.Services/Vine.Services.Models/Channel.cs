using System.Windows.Media;

namespace Vine.Services.Models;

public class Channel
{
	public string Thumb { get; set; }

	public long Id { get; set; }

	public Color BackgroundColor { get; set; }

	public string Name { get; set; }

	public Color ForegroundColor { get; set; }
}
