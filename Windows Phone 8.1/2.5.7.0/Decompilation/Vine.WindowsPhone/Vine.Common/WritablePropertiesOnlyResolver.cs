using System;
using System.Collections.Generic;
using System.Linq;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

namespace Vine.Common;

public class WritablePropertiesOnlyResolver : DefaultContractResolver
{
	protected override IList<JsonProperty> CreateProperties(Type type, MemberSerialization memberSerialization)
	{
		return (from p in base.CreateProperties(type, memberSerialization)
			where p.Writable
			select p).ToList();
	}
}
