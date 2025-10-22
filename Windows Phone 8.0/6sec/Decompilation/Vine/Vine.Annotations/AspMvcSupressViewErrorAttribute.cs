using System;

namespace Vine.Annotations;

[AttributeUsage(AttributeTargets.Class | AttributeTargets.Method)]
public sealed class AspMvcSupressViewErrorAttribute : Attribute
{
}
