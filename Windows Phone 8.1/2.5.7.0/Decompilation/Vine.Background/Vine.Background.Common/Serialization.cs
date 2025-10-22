using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

namespace Vine.Background.Common;

internal sealed class Serialization
{
	public class WritablePropertiesOnlyResolver : DefaultContractResolver
	{
		protected override extern IList<JsonProperty> CreateProperties(Type type, MemberSerialization memberSerialization);

		public extern WritablePropertiesOnlyResolver();
	}

	public static extern string Serialize(object obj);

	public static extern T Deserialize<T>(string json);

	public extern Serialization();
}
