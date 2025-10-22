using System;
using System.Net.Http;
using System.Runtime.CompilerServices;

namespace Vine.Background.Web;

internal class ApiResult<T>
{
	public extern T Model
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern bool HasError { get; }

	public extern HttpResponseMessage HttpResponse
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string RequestContent
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string ResponseContent
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern Exception Error
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern Exception ErrorParsed
	{
		[CompilerGenerated]
		get;
		[CompilerGenerated]
		set;
	}

	public extern string XDate { get; }

	public extern ApiResult();
}
