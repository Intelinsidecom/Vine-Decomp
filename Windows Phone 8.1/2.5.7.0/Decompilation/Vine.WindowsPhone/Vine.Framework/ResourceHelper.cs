using Windows.ApplicationModel.Resources;

namespace Vine.Framework;

public static class ResourceHelper
{
	public static string GetString(string name)
	{
		//IL_000d: Unknown result type (might be due to invalid IL or missing references)
		name += "/Text";
		return new ResourceLoader().GetString(name);
	}
}
