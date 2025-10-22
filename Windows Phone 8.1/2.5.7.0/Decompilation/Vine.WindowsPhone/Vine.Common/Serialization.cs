using System.Runtime.Serialization.Formatters;
using Newtonsoft.Json;

namespace Vine.Common;

public static class Serialization
{
	public static T DeserializeType<T>(string json)
	{
		T result = default(T);
		if (!string.IsNullOrWhiteSpace(json))
		{
			return JsonConvert.DeserializeObject<T>(json, new JsonSerializerSettings
			{
				TypeNameHandling = TypeNameHandling.All,
				TypeNameAssemblyFormat = FormatterAssemblyStyle.Simple
			});
		}
		return result;
	}

	public static object DeserializeType(string json)
	{
		object result = null;
		if (!string.IsNullOrWhiteSpace(json))
		{
			result = JsonConvert.DeserializeObject(json, new JsonSerializerSettings
			{
				TypeNameHandling = TypeNameHandling.All,
				TypeNameAssemblyFormat = FormatterAssemblyStyle.Simple
			});
		}
		return result;
	}

	public static T Deserialize<T>(string json)
	{
		T result = default(T);
		if (!string.IsNullOrWhiteSpace(json))
		{
			return JsonConvert.DeserializeObject<T>(json);
		}
		return result;
	}

	public static string SerializeType(object obj)
	{
		if (obj == null)
		{
			return null;
		}
		return JsonConvert.SerializeObject(obj, Formatting.None, new JsonSerializerSettings
		{
			TypeNameHandling = TypeNameHandling.All,
			TypeNameAssemblyFormat = FormatterAssemblyStyle.Simple,
			ContractResolver = new WritablePropertiesOnlyResolver()
		});
	}

	public static string Serialize(object obj)
	{
		if (obj == null)
		{
			return null;
		}
		return JsonConvert.SerializeObject(obj, new JsonSerializerSettings
		{
			ContractResolver = new WritablePropertiesOnlyResolver()
		});
	}

	public static string SerializeIgnoreNulls(object obj)
	{
		if (obj == null)
		{
			return null;
		}
		return JsonConvert.SerializeObject(obj, new JsonSerializerSettings
		{
			ContractResolver = new WritablePropertiesOnlyResolver(),
			NullValueHandling = NullValueHandling.Ignore
		});
	}

	public static T DeepCopy<T>(object obj) where T : class
	{
		if (obj == null)
		{
			return null;
		}
		return Deserialize<T>(Serialize(obj));
	}
}
