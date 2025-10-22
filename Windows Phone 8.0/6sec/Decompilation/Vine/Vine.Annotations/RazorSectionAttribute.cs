using System;

namespace Vine.Annotations;

[AttributeUsage(AttributeTargets.Method | AttributeTargets.Parameter)]
public sealed class RazorSectionAttribute : Attribute
{
}
