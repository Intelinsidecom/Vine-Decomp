using System.Xml.Serialization;

namespace Vine;

public class FoursquareAccess
{
	[XmlAttribute("at")]
	public string AccessToken { get; set; }
}
