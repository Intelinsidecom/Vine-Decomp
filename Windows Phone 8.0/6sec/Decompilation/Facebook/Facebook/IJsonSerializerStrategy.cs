using System;

namespace Facebook;

internal interface IJsonSerializerStrategy
{
	bool SerializeNonPrimitiveObject(object input, out object output);

	object DeserializeObject(object value, Type type);
}
