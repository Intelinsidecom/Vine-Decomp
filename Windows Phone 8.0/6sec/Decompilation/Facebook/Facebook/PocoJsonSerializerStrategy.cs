using System;
using System.Collections;
using System.Collections.Generic;
using System.Globalization;
using System.Reflection;
using Facebook.Reflection;

namespace Facebook;

internal class PocoJsonSerializerStrategy : IJsonSerializerStrategy
{
	internal IDictionary<Type, ReflectionUtils.ConstructorDelegate> ConstructorCache;

	internal IDictionary<Type, IDictionary<string, ReflectionUtils.GetDelegate>> GetCache;

	internal IDictionary<Type, IDictionary<string, KeyValuePair<Type, ReflectionUtils.SetDelegate>>> SetCache;

	internal static readonly Type[] EmptyTypes = new Type[0];

	internal static readonly Type[] ArrayConstructorParameterTypes = new Type[1] { typeof(int) };

	private static readonly string[] Iso8601Format = new string[3] { "yyyy-MM-dd\\THH:mm:ss.FFFFFFF\\Z", "yyyy-MM-dd\\THH:mm:ss\\Z", "yyyy-MM-dd\\THH:mm:ssK" };

	public PocoJsonSerializerStrategy()
	{
		ConstructorCache = new ReflectionUtils.ThreadSafeDictionary<Type, ReflectionUtils.ConstructorDelegate>(ContructorDelegateFactory);
		GetCache = new ReflectionUtils.ThreadSafeDictionary<Type, IDictionary<string, ReflectionUtils.GetDelegate>>(GetterValueFactory);
		SetCache = new ReflectionUtils.ThreadSafeDictionary<Type, IDictionary<string, KeyValuePair<Type, ReflectionUtils.SetDelegate>>>(SetterValueFactory);
	}

	internal virtual ReflectionUtils.ConstructorDelegate ContructorDelegateFactory(Type key)
	{
		return ReflectionUtils.GetContructor(key, key.IsArray ? ArrayConstructorParameterTypes : EmptyTypes);
	}

	internal virtual IDictionary<string, ReflectionUtils.GetDelegate> GetterValueFactory(Type type)
	{
		IDictionary<string, ReflectionUtils.GetDelegate> dictionary = new Dictionary<string, ReflectionUtils.GetDelegate>();
		foreach (PropertyInfo property in ReflectionUtils.GetProperties(type))
		{
			if (property.CanRead)
			{
				MethodInfo getterMethodInfo = ReflectionUtils.GetGetterMethodInfo(property);
				if (!getterMethodInfo.IsStatic && getterMethodInfo.IsPublic)
				{
					dictionary[property.Name] = ReflectionUtils.GetGetMethod(property);
				}
			}
		}
		foreach (FieldInfo field in ReflectionUtils.GetFields(type))
		{
			if (!field.IsStatic && field.IsPublic)
			{
				dictionary[field.Name] = ReflectionUtils.GetGetMethod(field);
			}
		}
		return dictionary;
	}

	internal virtual IDictionary<string, KeyValuePair<Type, ReflectionUtils.SetDelegate>> SetterValueFactory(Type type)
	{
		IDictionary<string, KeyValuePair<Type, ReflectionUtils.SetDelegate>> dictionary = new Dictionary<string, KeyValuePair<Type, ReflectionUtils.SetDelegate>>();
		foreach (PropertyInfo property in ReflectionUtils.GetProperties(type))
		{
			if (property.CanWrite)
			{
				MethodInfo setterMethodInfo = ReflectionUtils.GetSetterMethodInfo(property);
				if (!setterMethodInfo.IsStatic && setterMethodInfo.IsPublic)
				{
					dictionary[property.Name] = new KeyValuePair<Type, ReflectionUtils.SetDelegate>(property.PropertyType, ReflectionUtils.GetSetMethod(property));
				}
			}
		}
		foreach (FieldInfo field in ReflectionUtils.GetFields(type))
		{
			if (!field.IsInitOnly && !field.IsStatic && field.IsPublic)
			{
				dictionary[field.Name] = new KeyValuePair<Type, ReflectionUtils.SetDelegate>(field.FieldType, ReflectionUtils.GetSetMethod(field));
			}
		}
		return dictionary;
	}

	public virtual bool SerializeNonPrimitiveObject(object input, out object output)
	{
		if (!TrySerializeKnownTypes(input, out output))
		{
			return TrySerializeUnknownTypes(input, out output);
		}
		return true;
	}

	public virtual object DeserializeObject(object value, Type type)
	{
		object obj = null;
		if (value is string)
		{
			string text = value as string;
			obj = ((!string.IsNullOrEmpty(text)) ? (((object)type != typeof(DateTime) && (!ReflectionUtils.IsNullableType(type) || (object)Nullable.GetUnderlyingType(type) != typeof(DateTime))) ? (((object)type != typeof(Guid) && (!ReflectionUtils.IsNullableType(type) || (object)Nullable.GetUnderlyingType(type) != typeof(Guid))) ? text : ((object)new Guid(text))) : ((object)DateTime.ParseExact(text, Iso8601Format, CultureInfo.InvariantCulture, DateTimeStyles.AdjustToUniversal | DateTimeStyles.AssumeUniversal))) : (((object)type == typeof(Guid)) ? ((object)default(Guid)) : ((!ReflectionUtils.IsNullableType(type) || (object)Nullable.GetUnderlyingType(type) != typeof(Guid)) ? text : null)));
		}
		else if (value is bool)
		{
			obj = value;
		}
		else if (value == null)
		{
			obj = null;
		}
		else if ((value is long && (object)type == typeof(long)) || (value is double && (object)type == typeof(double)))
		{
			obj = value;
		}
		else
		{
			if ((!(value is double) || (object)type == typeof(double)) && (!(value is long) || (object)type == typeof(long)))
			{
				if (value is IDictionary<string, object>)
				{
					IDictionary<string, object> dictionary = (IDictionary<string, object>)value;
					if (ReflectionUtils.IsTypeDictionary(type))
					{
						Type[] genericTypeArguments = ReflectionUtils.GetGenericTypeArguments(type);
						Type type2 = genericTypeArguments[0];
						Type type3 = genericTypeArguments[1];
						Type key = typeof(Dictionary<, >).MakeGenericType(type2, type3);
						IDictionary dictionary2 = (IDictionary)ConstructorCache[key]();
						foreach (KeyValuePair<string, object> item in dictionary)
						{
							dictionary2.Add(item.Key, DeserializeObject(item.Value, type3));
						}
						obj = dictionary2;
					}
					else if ((object)type == typeof(object))
					{
						obj = value;
					}
					else
					{
						obj = ConstructorCache[type]();
						foreach (KeyValuePair<string, KeyValuePair<Type, ReflectionUtils.SetDelegate>> item2 in SetCache[type])
						{
							if (dictionary.TryGetValue(item2.Key, out var value2))
							{
								value2 = DeserializeObject(value2, item2.Value.Key);
								item2.Value.Value(obj, value2);
							}
						}
					}
				}
				else if (value is IList<object>)
				{
					IList<object> list = (IList<object>)value;
					IList list2 = null;
					if (type.IsArray)
					{
						list2 = (IList)ConstructorCache[type](list.Count);
						int num = 0;
						foreach (object item3 in list)
						{
							list2[num++] = DeserializeObject(item3, type.GetElementType());
						}
					}
					else if (ReflectionUtils.IsTypeGenericeCollectionInterface(type) || ReflectionUtils.IsAssignableFrom(typeof(IList), type))
					{
						Type type4 = ReflectionUtils.GetGenericTypeArguments(type)[0];
						Type key2 = typeof(List<>).MakeGenericType(type4);
						list2 = (IList)ConstructorCache[key2](list.Count);
						foreach (object item4 in list)
						{
							list2.Add(DeserializeObject(item4, type4));
						}
					}
					obj = list2;
				}
				return obj;
			}
			obj = (typeof(IConvertible).IsAssignableFrom(type) ? Convert.ChangeType(value, type, CultureInfo.InvariantCulture) : value);
		}
		if (ReflectionUtils.IsNullableType(type))
		{
			return ReflectionUtils.ToNullableType(obj, type);
		}
		if (obj == null && (object)type == typeof(Guid))
		{
			return default(Guid);
		}
		return obj;
	}

	protected virtual object SerializeEnum(Enum p)
	{
		return Convert.ToDouble(p, CultureInfo.InvariantCulture);
	}

	protected virtual bool TrySerializeKnownTypes(object input, out object output)
	{
		bool result = true;
		if (input is DateTime)
		{
			output = ((DateTime)input).ToUniversalTime().ToString(Iso8601Format[0], CultureInfo.InvariantCulture);
		}
		else if (input is Guid)
		{
			output = ((Guid)input).ToString("D");
		}
		else if (input is Uri)
		{
			output = input.ToString();
		}
		else if (input is Enum)
		{
			output = SerializeEnum((Enum)input);
		}
		else
		{
			result = false;
			output = null;
		}
		return result;
	}

	protected virtual bool TrySerializeUnknownTypes(object input, out object output)
	{
		output = null;
		Type type = input.GetType();
		if (type.FullName == null)
		{
			return false;
		}
		IDictionary<string, object> dictionary = new JsonObject();
		IDictionary<string, ReflectionUtils.GetDelegate> dictionary2 = GetCache[type];
		foreach (KeyValuePair<string, ReflectionUtils.GetDelegate> item in dictionary2)
		{
			if (item.Value != null)
			{
				dictionary.Add(item.Key, item.Value(input));
			}
		}
		output = dictionary;
		return true;
	}
}
