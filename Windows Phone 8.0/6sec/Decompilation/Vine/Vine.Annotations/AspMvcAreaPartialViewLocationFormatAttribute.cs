using System;

namespace Vine.Annotations;

[AttributeUsage(AttributeTargets.Assembly, AllowMultiple = true)]
public sealed class AspMvcAreaPartialViewLocationFormatAttribute : Attribute
{
	public string Format { get; private set; }

	public AspMvcAreaPartialViewLocationFormatAttribute(string format)
	{
		Format = format;
	}
}
