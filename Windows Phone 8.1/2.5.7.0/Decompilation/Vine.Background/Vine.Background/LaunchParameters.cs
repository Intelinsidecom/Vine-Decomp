using System.Runtime.CompilerServices;
using System.Runtime.Serialization;
using Vine.Background.Models;
using Windows.Foundation.Metadata;

namespace Vine.Background;

[DataContract]
[MarshalingBehavior(/*Could not decode attribute arguments.*/)]
[Threading(/*Could not decode attribute arguments.*/)]
[Version(21889117u)]
[Activatable(21889117u)]
public sealed class LaunchParameters : ILaunchParametersClass
{
	[DataMember]
	public extern string Type
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	[DataMember]
	public extern string Data
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	[DataMember]
	public extern string Title
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	[DataMember]
	public extern string Msg
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	[DataMember]
	public extern string ConversationId
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	[DataMember]
	internal extern ConversationViewModel ConversationViewModel
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern LaunchParameters();
}
