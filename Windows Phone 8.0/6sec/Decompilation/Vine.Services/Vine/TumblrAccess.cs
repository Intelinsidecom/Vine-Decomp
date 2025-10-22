using System.Xml.Serialization;

namespace Vine;

public class TumblrAccess
{
	[XmlAttribute("at")]
	public string AccessToken { get; set; }

	[XmlAttribute("se")]
	public string AccessTokenSecret { get; set; }
}
