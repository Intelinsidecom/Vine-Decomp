using System;
using System.Collections.Generic;
using System.Linq;

namespace Vine.Models;

public class FbPermissions
{
	private const string PublishPermissionKey = "publish_actions";

	private const string PermissionGranted = "granted";

	public List<FbPermission> Data { get; set; }

	public bool HasPublishPermission
	{
		get
		{
			FbPermission fbPermission = Data.FirstOrDefault((FbPermission p) => "publish_actions".Equals(p.Permission, StringComparison.OrdinalIgnoreCase));
			if (fbPermission != null)
			{
				return "granted".Equals(fbPermission.Status, StringComparison.OrdinalIgnoreCase);
			}
			return false;
		}
	}
}
