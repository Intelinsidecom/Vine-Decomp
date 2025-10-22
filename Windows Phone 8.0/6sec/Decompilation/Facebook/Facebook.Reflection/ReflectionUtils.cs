using System;
using System.Collections;
using System.Collections.Generic;
using System.Globalization;
using System.Linq.Expressions;
using System.Reflection;

namespace Facebook.Reflection;

internal class ReflectionUtils
{
	public delegate object GetDelegate(object source);

	public delegate void SetDelegate(object source, object value);

	public delegate object ConstructorDelegate(params object[] args);

	public delegate TValue ThreadSafeDictionaryValueFactory<TKey, TValue>(TKey key);

	private static class Assigner<T>
	{
		public static T Assign(ref T left, T right)
		{
			return left = right;
		}
	}

	public sealed class ThreadSafeDictionary<TKey, TValue> : IDictionary<TKey, TValue>, ICollection<KeyValuePair<TKey, TValue>>, IEnumerable<KeyValuePair<TKey, TValue>>, IEnumerable
	{
		private readonly object _lock = new object();

		private readonly ThreadSafeDictionaryValueFactory<TKey, TValue> _valueFactory;

		private Dictionary<TKey, TValue> _dictionary;

		public ICollection<TKey> Keys => _dictionary.Keys;

		public ICollection<TValue> Values => _dictionary.Values;

		public TValue this[TKey key]
		{
			get
			{
				return Get(key);
			}
			set
			{
				throw new NotImplementedException();
			}
		}

		public int Count => _dictionary.Count;

		public bool IsReadOnly
		{
			get
			{
				throw new NotImplementedException();
			}
		}

		public ThreadSafeDictionary(ThreadSafeDictionaryValueFactory<TKey, TValue> valueFactory)
		{
			_valueFactory = valueFactory;
		}

		private TValue Get(TKey key)
		{
			if (_dictionary == null)
			{
				return AddValue(key);
			}
			if (!_dictionary.TryGetValue(key, out var value))
			{
				return AddValue(key);
			}
			return value;
		}

		private TValue AddValue(TKey key)
		{
			TValue val = _valueFactory(key);
			lock (_lock)
			{
				if (_dictionary == null)
				{
					_dictionary = new Dictionary<TKey, TValue>();
					_dictionary[key] = val;
				}
				else
				{
					if (_dictionary.TryGetValue(key, out var value))
					{
						return value;
					}
					Dictionary<TKey, TValue> dictionary = new Dictionary<TKey, TValue>(_dictionary);
					dictionary[key] = val;
					_dictionary = dictionary;
				}
			}
			return val;
		}

		public void Add(TKey key, TValue value)
		{
			throw new NotImplementedException();
		}

		public bool ContainsKey(TKey key)
		{
			return _dictionary.ContainsKey(key);
		}

		public bool Remove(TKey key)
		{
			throw new NotImplementedException();
		}

		public bool TryGetValue(TKey key, out TValue value)
		{
			value = this[key];
			return true;
		}

		public void Add(KeyValuePair<TKey, TValue> item)
		{
			throw new NotImplementedException();
		}

		public void Clear()
		{
			throw new NotImplementedException();
		}

		public bool Contains(KeyValuePair<TKey, TValue> item)
		{
			throw new NotImplementedException();
		}

		public void CopyTo(KeyValuePair<TKey, TValue>[] array, int arrayIndex)
		{
			throw new NotImplementedException();
		}

		public bool Remove(KeyValuePair<TKey, TValue> item)
		{
			throw new NotImplementedException();
		}

		public IEnumerator<KeyValuePair<TKey, TValue>> GetEnumerator()
		{
			return _dictionary.GetEnumerator();
		}

		IEnumerator IEnumerable.GetEnumerator()
		{
			return _dictionary.GetEnumerator();
		}
	}

	private static readonly object[] EmptyObjects = new object[0];

	public static Attribute GetAttribute(MemberInfo info, Type type)
	{
		if ((object)info == null || (object)type == null || !Attribute.IsDefined(info, type))
		{
			return null;
		}
		return Attribute.GetCustomAttribute(info, type);
	}

	public static Attribute GetAttribute(Type objectType, Type attributeType)
	{
		if ((object)objectType == null || (object)attributeType == null || !Attribute.IsDefined(objectType, attributeType))
		{
			return null;
		}
		return Attribute.GetCustomAttribute(objectType, attributeType);
	}

	public static Type[] GetGenericTypeArguments(Type type)
	{
		return type.GetGenericArguments();
	}

	public static bool IsTypeGenericeCollectionInterface(Type type)
	{
		if (!type.IsGenericType)
		{
			return false;
		}
		Type genericTypeDefinition = type.GetGenericTypeDefinition();
		if ((object)genericTypeDefinition != typeof(IList<>) && (object)genericTypeDefinition != typeof(ICollection<>))
		{
			return (object)genericTypeDefinition == typeof(IEnumerable<>);
		}
		return true;
	}

	public static bool IsAssignableFrom(Type type1, Type type2)
	{
		return type1.IsAssignableFrom(type2);
	}

	public static bool IsTypeDictionary(Type type)
	{
		if (typeof(IDictionary).IsAssignableFrom(type))
		{
			return true;
		}
		if (!type.IsGenericType)
		{
			return false;
		}
		Type genericTypeDefinition = type.GetGenericTypeDefinition();
		return (object)genericTypeDefinition == typeof(IDictionary<, >);
	}

	public static bool IsNullableType(Type type)
	{
		if (type.IsGenericType)
		{
			return (object)type.GetGenericTypeDefinition() == typeof(Nullable<>);
		}
		return false;
	}

	public static object ToNullableType(object obj, Type nullableType)
	{
		if (obj != null)
		{
			return Convert.ChangeType(obj, Nullable.GetUnderlyingType(nullableType), CultureInfo.InvariantCulture);
		}
		return null;
	}

	public static bool IsValueType(Type type)
	{
		return type.IsValueType;
	}

	public static IEnumerable<ConstructorInfo> GetConstructors(Type type)
	{
		return type.GetConstructors();
	}

	public static ConstructorInfo GetConstructorInfo(Type type, params Type[] argsType)
	{
		IEnumerable<ConstructorInfo> constructors = GetConstructors(type);
		foreach (ConstructorInfo item in constructors)
		{
			ParameterInfo[] parameters = item.GetParameters();
			if (argsType.Length != parameters.Length)
			{
				continue;
			}
			int num = 0;
			bool flag = true;
			ParameterInfo[] parameters2 = item.GetParameters();
			foreach (ParameterInfo parameterInfo in parameters2)
			{
				if ((object)parameterInfo.ParameterType != argsType[num])
				{
					flag = false;
					break;
				}
			}
			if (flag)
			{
				return item;
			}
		}
		return null;
	}

	public static IEnumerable<PropertyInfo> GetProperties(Type type)
	{
		return type.GetProperties(BindingFlags.Instance | BindingFlags.Static | BindingFlags.Public | BindingFlags.NonPublic);
	}

	public static IEnumerable<FieldInfo> GetFields(Type type)
	{
		return type.GetFields(BindingFlags.Instance | BindingFlags.Static | BindingFlags.Public | BindingFlags.NonPublic);
	}

	public static MethodInfo GetGetterMethodInfo(PropertyInfo propertyInfo)
	{
		return propertyInfo.GetGetMethod(nonPublic: true);
	}

	public static MethodInfo GetSetterMethodInfo(PropertyInfo propertyInfo)
	{
		return propertyInfo.GetSetMethod(nonPublic: true);
	}

	public static ConstructorDelegate GetContructor(ConstructorInfo constructorInfo)
	{
		return GetConstructorByExpression(constructorInfo);
	}

	public static ConstructorDelegate GetContructor(Type type, params Type[] argsType)
	{
		return GetConstructorByExpression(type, argsType);
	}

	public static ConstructorDelegate GetConstructorByReflection(ConstructorInfo constructorInfo)
	{
		return (object[] args) => constructorInfo.Invoke(args);
	}

	public static ConstructorDelegate GetConstructorByReflection(Type type, params Type[] argsType)
	{
		ConstructorInfo constructorInfo = GetConstructorInfo(type, argsType);
		if ((object)constructorInfo != null)
		{
			return GetConstructorByReflection(constructorInfo);
		}
		return null;
	}

	public static ConstructorDelegate GetConstructorByExpression(ConstructorInfo constructorInfo)
	{
		ParameterInfo[] parameters = constructorInfo.GetParameters();
		ParameterExpression parameterExpression = Expression.Parameter(typeof(object[]), "args");
		Expression[] array = new Expression[parameters.Length];
		for (int i = 0; i < parameters.Length; i++)
		{
			Expression index = Expression.Constant(i);
			Type parameterType = parameters[i].ParameterType;
			Expression expression = Expression.ArrayIndex(parameterExpression, index);
			Expression expression2 = Expression.Convert(expression, parameterType);
			array[i] = expression2;
		}
		NewExpression body = Expression.New(constructorInfo, array);
		Expression<Func<object[], object>> expression3 = Expression.Lambda<Func<object[], object>>(body, new ParameterExpression[1] { parameterExpression });
		Func<object[], object> compiledLambda = expression3.Compile();
		return (object[] args) => compiledLambda(args);
	}

	public static ConstructorDelegate GetConstructorByExpression(Type type, params Type[] argsType)
	{
		ConstructorInfo constructorInfo = GetConstructorInfo(type, argsType);
		if ((object)constructorInfo != null)
		{
			return GetConstructorByExpression(constructorInfo);
		}
		return null;
	}

	public static GetDelegate GetGetMethod(PropertyInfo propertyInfo)
	{
		return GetGetMethodByExpression(propertyInfo);
	}

	public static GetDelegate GetGetMethod(FieldInfo fieldInfo)
	{
		return GetGetMethodByExpression(fieldInfo);
	}

	public static GetDelegate GetGetMethodByReflection(PropertyInfo propertyInfo)
	{
		MethodInfo methodInfo = GetGetterMethodInfo(propertyInfo);
		return (object source) => methodInfo.Invoke(source, EmptyObjects);
	}

	public static GetDelegate GetGetMethodByReflection(FieldInfo fieldInfo)
	{
		return (object source) => fieldInfo.GetValue(source);
	}

	public static GetDelegate GetGetMethodByExpression(PropertyInfo propertyInfo)
	{
		MethodInfo getterMethodInfo = GetGetterMethodInfo(propertyInfo);
		ParameterExpression parameterExpression = Expression.Parameter(typeof(object), "instance");
		UnaryExpression instance = ((!IsValueType(propertyInfo.DeclaringType)) ? Expression.TypeAs(parameterExpression, propertyInfo.DeclaringType) : Expression.Convert(parameterExpression, propertyInfo.DeclaringType));
		Func<object, object> compiled = Expression.Lambda<Func<object, object>>(Expression.TypeAs(Expression.Call(instance, getterMethodInfo), typeof(object)), new ParameterExpression[1] { parameterExpression }).Compile();
		return (object source) => compiled(source);
	}

	public static GetDelegate GetGetMethodByExpression(FieldInfo fieldInfo)
	{
		ParameterExpression parameterExpression = Expression.Parameter(typeof(object), "instance");
		MemberExpression expression = Expression.Field(Expression.Convert(parameterExpression, fieldInfo.DeclaringType), fieldInfo);
		GetDelegate compiled = Expression.Lambda<GetDelegate>(Expression.Convert(expression, typeof(object)), new ParameterExpression[1] { parameterExpression }).Compile();
		return (object source) => compiled(source);
	}

	public static SetDelegate GetSetMethod(PropertyInfo propertyInfo)
	{
		return GetSetMethodByExpression(propertyInfo);
	}

	public static SetDelegate GetSetMethod(FieldInfo fieldInfo)
	{
		return GetSetMethodByExpression(fieldInfo);
	}

	public static SetDelegate GetSetMethodByReflection(PropertyInfo propertyInfo)
	{
		MethodInfo methodInfo = GetSetterMethodInfo(propertyInfo);
		return delegate(object source, object value)
		{
			methodInfo.Invoke(source, new object[1] { value });
		};
	}

	public static SetDelegate GetSetMethodByReflection(FieldInfo fieldInfo)
	{
		return delegate(object source, object value)
		{
			fieldInfo.SetValue(source, value);
		};
	}

	public static SetDelegate GetSetMethodByExpression(PropertyInfo propertyInfo)
	{
		MethodInfo setterMethodInfo = GetSetterMethodInfo(propertyInfo);
		ParameterExpression parameterExpression = Expression.Parameter(typeof(object), "instance");
		ParameterExpression parameterExpression2 = Expression.Parameter(typeof(object), "value");
		UnaryExpression instance = ((!IsValueType(propertyInfo.DeclaringType)) ? Expression.TypeAs(parameterExpression, propertyInfo.DeclaringType) : Expression.Convert(parameterExpression, propertyInfo.DeclaringType));
		UnaryExpression unaryExpression = ((!IsValueType(propertyInfo.PropertyType)) ? Expression.TypeAs(parameterExpression2, propertyInfo.PropertyType) : Expression.Convert(parameterExpression2, propertyInfo.PropertyType));
		Action<object, object> compiled = Expression.Lambda<Action<object, object>>(Expression.Call(instance, setterMethodInfo, unaryExpression), new ParameterExpression[2] { parameterExpression, parameterExpression2 }).Compile();
		return delegate(object source, object val)
		{
			compiled(source, val);
		};
	}

	public static SetDelegate GetSetMethodByExpression(FieldInfo fieldInfo)
	{
		ParameterExpression parameterExpression = Expression.Parameter(typeof(object), "instance");
		ParameterExpression parameterExpression2 = Expression.Parameter(typeof(object), "value");
		Action<object, object> compiled = Expression.Lambda<Action<object, object>>(Assign(Expression.Field(Expression.Convert(parameterExpression, fieldInfo.DeclaringType), fieldInfo), Expression.Convert(parameterExpression2, fieldInfo.FieldType)), new ParameterExpression[2] { parameterExpression, parameterExpression2 }).Compile();
		return delegate(object source, object val)
		{
			compiled(source, val);
		};
	}

	public static BinaryExpression Assign(Expression left, Expression right)
	{
		MethodInfo method = typeof(Assigner<>).MakeGenericType(left.Type).GetMethod("Assign");
		return Expression.Add(left, right, method);
	}
}
