namespace Facebook;

/// <summary>
/// Represents a batch parameter for the creating batch requests.
/// </summary>
/// <remarks>
/// http://developers.facebook.com/docs/api/batch/
/// </remarks>
public class FacebookBatchParameter
{
	/// <summary>
	/// Gets or sets the http method.
	/// </summary>
	public HttpMethod HttpMethod { get; set; }

	/// <summary>
	/// Gets or sets the resource path.
	/// </summary>
	public string Path { get; set; }

	/// <summary>
	/// Gets or sets the parameters.
	/// </summary>
	public object Parameters { get; set; }

	/// <summary>
	/// Gets or sets the raw data parameter.
	/// </summary>
	public object Data { get; set; }

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookBatchParameter" /> class.
	/// </summary>
	public FacebookBatchParameter()
	{
		HttpMethod = HttpMethod.Get;
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookBatchParameter" /> class.
	/// </summary>
	/// <param name="path">
	/// The resource path.
	/// </param>
	public FacebookBatchParameter(string path)
		: this(HttpMethod.Get, path)
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookBatchParameter" /> class.
	/// </summary>
	/// <param name="httpMethod">
	/// The http method.
	/// </param>
	/// <param name="path">
	/// The resource path.
	/// </param>
	public FacebookBatchParameter(HttpMethod httpMethod, string path)
		: this(httpMethod, path, null)
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookBatchParameter" /> class.
	/// </summary>
	/// <param name="path">
	/// The resource path.
	/// </param>
	/// <param name="parameters">
	/// The parameters.
	/// </param>
	public FacebookBatchParameter(string path, object parameters)
		: this(HttpMethod.Get, path, parameters)
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.FacebookBatchParameter" /> class.
	/// </summary>
	/// <param name="httpMethod">
	/// The http method.
	/// </param>
	/// <param name="path">
	/// The resource path.
	/// </param>
	/// <param name="parameters">
	/// The parameters.
	/// </param>
	public FacebookBatchParameter(HttpMethod httpMethod, string path, object parameters)
	{
		HttpMethod = httpMethod;
		Path = path;
		Parameters = parameters;
	}
}
