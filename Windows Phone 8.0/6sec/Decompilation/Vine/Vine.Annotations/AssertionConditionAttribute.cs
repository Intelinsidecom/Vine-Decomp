using System;

namespace Vine.Annotations;

[AttributeUsage(AttributeTargets.Parameter)]
public sealed class AssertionConditionAttribute : Attribute
{
	public AssertionConditionType ConditionType { get; private set; }

	public AssertionConditionAttribute(AssertionConditionType conditionType)
	{
		ConditionType = conditionType;
	}
}
