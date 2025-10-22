using System;

namespace Vine.Annotations;

[AttributeUsage(AttributeTargets.Method | AttributeTargets.Parameter)]
public sealed class AspMvcViewAttribute : Attribute
{
}
