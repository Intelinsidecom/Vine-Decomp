using System.Xml.Serialization;

namespace Vine;

public class FlickrAccess
{
	[XmlAttribute("at")]
	public string AccessToken { get; set; }

	[XmlAttribute("ui")]
	public string UserId { get; set; }

	[XmlAttribute("ats")]
	public string AccessTokenSecret { get; set; }

	[XmlAttribute("un")]
	public string Username { get; set; }
}
