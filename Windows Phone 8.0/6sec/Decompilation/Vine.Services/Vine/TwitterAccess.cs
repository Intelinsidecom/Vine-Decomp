using System.Xml.Serialization;

namespace Vine;

public class TwitterAccess
{
	[XmlAttribute("at")]
	public string AccessToken { get; set; }

	[XmlAttribute("se")]
	public string AccessTokenSecret { get; set; }

	[XmlAttribute("ui")]
	public string UserId { get; set; }

	[XmlAttribute("sc")]
	public string ScreenName { get; set; }
}
