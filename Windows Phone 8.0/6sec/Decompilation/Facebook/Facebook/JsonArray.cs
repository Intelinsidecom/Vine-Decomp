using System.Collections.Generic;
using System.ComponentModel;

namespace Facebook;

/// <summary>
/// Represents the json array.
/// </summary>
[EditorBrowsable(EditorBrowsableState.Never)]
public class JsonArray : List<object>
{
	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.JsonArray" /> class. 
	/// </summary>
	public JsonArray()
	{
	}

	/// <summary>
	/// Initializes a new instance of the <see cref="T:Facebook.JsonArray" /> class. 
	/// </summary>
	/// <param name="capacity">The capacity of the json array.</param>
	public JsonArray(int capacity)
		: base(capacity)
	{
	}

	/// <summary>
	/// The json representation of the array.
	/// </summary>
	/// <returns>The json representation of the array.</returns>
	public override string ToString()
	{
		return SimpleJson.SerializeObject(this) ?? string.Empty;
	}
}
