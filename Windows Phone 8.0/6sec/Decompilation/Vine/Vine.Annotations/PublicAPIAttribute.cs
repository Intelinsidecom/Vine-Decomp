using System;

namespace Vine.Annotations;

[MeansImplicitUse(ImplicitUseTargetFlags.WithMembers)]
public sealed class PublicAPIAttribute : Attribute
{
	public string Comment { get; private set; }

	public PublicAPIAttribute()
	{
	}

	public PublicAPIAttribute([NotNull] string comment)
	{
		Comment = comment;
	}
}
