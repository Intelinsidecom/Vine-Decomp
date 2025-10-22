using System;

namespace Vine.Annotations;

[AttributeUsage(AttributeTargets.Class, AllowMultiple = true)]
public sealed class AspRequiredAttributeAttribute : Attribute
{
	public string Attribute { get; private set; }

	public AspRequiredAttributeAttribute([NotNull] string attribute)
	{
		Attribute = attribute;
	}
}
