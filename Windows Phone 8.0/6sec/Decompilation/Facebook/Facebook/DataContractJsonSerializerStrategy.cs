using System;
using System.Collections.Generic;
using System.Reflection;
using System.Runtime.Serialization;
using Facebook.Reflection;

namespace Facebook;

internal class DataContractJsonSerializerStrategy : PocoJsonSerializerStrategy
{
	public DataContractJsonSerializerStrategy()
	{
		GetCache = new ReflectionUtils.ThreadSafeDictionary<Type, IDictionary<string, ReflectionUtils.GetDelegate>>(GetterValueFactory);
		SetCache = new ReflectionUtils.ThreadSafeDictionary<Type, IDictionary<string, KeyValuePair<Type, ReflectionUtils.SetDelegate>>>(SetterValueFactory);
	}

	internal override IDictionary<string, ReflectionUtils.GetDelegate> GetterValueFactory(Type type)
	{
		if (ReflectionUtils.GetAttribute(type, typeof(DataContractAttribute)) == null)
		{
			return base.GetterValueFactory(type);
		}
		IDictionary<string, ReflectionUtils.GetDelegate> dictionary = new Dictionary<string, ReflectionUtils.GetDelegate>();
		string jsonKey;
		foreach (PropertyInfo property in ReflectionUtils.GetProperties(type))
		{
			if (property.CanRead)
			{
				MethodInfo getterMethodInfo = ReflectionUtils.GetGetterMethodInfo(property);
				if (!getterMethodInfo.IsStatic && CanAdd(property, out jsonKey))
				{
					dictionary[jsonKey] = ReflectionUtils.GetGetMethod(property);
				}
			}
		}
		foreach (FieldInfo field in ReflectionUtils.GetFields(type))
		{
			if (!field.IsStatic && CanAdd(field, out jsonKey))
			{
				dictionary[jsonKey] = ReflectionUtils.GetGetMethod(field);
			}
		}
		return dictionary;
	}

	internal override IDictionary<string, KeyValuePair<Type, ReflectionUtils.SetDelegate>> SetterValueFactory(Type type)
	{
		if (ReflectionUtils.GetAttribute(type, typeof(DataContractAttribute)) == null)
		{
			return base.SetterValueFactory(type);
		}
		IDictionary<string, KeyValuePair<Type, ReflectionUtils.SetDelegate>> dictionary = new Dictionary<string, KeyValuePair<Type, ReflectionUtils.SetDelegate>>();
		string jsonKey;
		foreach (PropertyInfo property in ReflectionUtils.GetProperties(type))
		{
			if (property.CanWrite)
			{
				MethodInfo setterMethodInfo = ReflectionUtils.GetSetterMethodInfo(property);
				if (!setterMethodInfo.IsStatic && CanAdd(property, out jsonKey))
				{
					dictionary[jsonKey] = new KeyValuePair<Type, ReflectionUtils.SetDelegate>(property.PropertyType, ReflectionUtils.GetSetMethod(property));
				}
			}
		}
		foreach (FieldInfo field in ReflectionUtils.GetFields(type))
		{
			if (!field.IsInitOnly && !field.IsStatic && CanAdd(field, out jsonKey))
			{
				dictionary[jsonKey] = new KeyValuePair<Type, ReflectionUtils.SetDelegate>(field.FieldType, ReflectionUtils.GetSetMethod(field));
			}
		}
		return dictionary;
	}

	private static bool CanAdd(MemberInfo info, out string jsonKey)
	{
		jsonKey = null;
		if (ReflectionUtils.GetAttribute(info, typeof(IgnoreDataMemberAttribute)) != null)
		{
			return false;
		}
		DataMemberAttribute dataMemberAttribute = (DataMemberAttribute)ReflectionUtils.GetAttribute(info, typeof(DataMemberAttribute));
		if (dataMemberAttribute == null)
		{
			return false;
		}
		jsonKey = (string.IsNullOrEmpty(dataMemberAttribute.Name) ? info.Name : dataMemberAttribute.Name);
		return true;
	}
}
