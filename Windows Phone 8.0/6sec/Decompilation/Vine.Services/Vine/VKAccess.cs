using System.Xml.Serialization;

namespace Vine;

public class VKAccess
{
	[XmlAttribute("at")]
	public string AccessToken { get; set; }

	[XmlAttribute("ui")]
	public string UserId { get; set; }
}
