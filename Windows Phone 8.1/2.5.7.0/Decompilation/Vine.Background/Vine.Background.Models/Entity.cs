using System.Runtime.CompilerServices;
using System.Runtime.Serialization;
using Windows.Foundation.Metadata;

namespace Vine.Background.Models;

[MarshalingBehavior(/*Could not decode attribute arguments.*/)]
[Threading(/*Could not decode attribute arguments.*/)]
[Version(21889117u)]
[Activatable(21889117u)]
public sealed class Entity : IEntityClass
{
	public extern string Type
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	[IgnoreDataMember]
	public extern EntityType EntityType { get; }

	public extern string Id
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string Title
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string Link
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern int[] Range
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string Text
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern Entity();
}
