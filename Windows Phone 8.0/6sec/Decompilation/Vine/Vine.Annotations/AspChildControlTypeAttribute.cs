using System;

namespace Vine.Annotations;

[AttributeUsage(AttributeTargets.Class, AllowMultiple = true)]
public sealed class AspChildControlTypeAttribute : Attribute
{
	public string TagName { get; private set; }

	public Type ControlType { get; private set; }

	public AspChildControlTypeAttribute(string tagName, Type controlType)
	{
		TagName = tagName;
		ControlType = controlType;
	}
}
