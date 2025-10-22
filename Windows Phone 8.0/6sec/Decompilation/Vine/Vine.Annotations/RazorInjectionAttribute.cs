using System;

namespace Vine.Annotations;

[AttributeUsage(AttributeTargets.Assembly, AllowMultiple = true)]
public sealed class RazorInjectionAttribute : Attribute
{
	public string Type { get; private set; }

	public string FieldName { get; private set; }

	public RazorInjectionAttribute(string type, string fieldName)
	{
		Type = type;
		FieldName = fieldName;
	}
}
