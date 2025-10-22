using System.Runtime.CompilerServices;

namespace Vine.Background.Models;

internal class BaseVineResponseModel<T>
{
	public extern string Code
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string Error
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern bool Success
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern T Data
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern BaseVineResponseModel();
}
