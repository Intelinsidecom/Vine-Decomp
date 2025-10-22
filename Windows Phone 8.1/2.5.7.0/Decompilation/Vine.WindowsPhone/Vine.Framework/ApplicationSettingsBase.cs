using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Vine.Common;
using Windows.Security.Credentials;
using Windows.Storage;

namespace Vine.Framework;

public abstract class ApplicationSettingsBase
{
	private readonly Dictionary<string, object> _cache;

	protected ApplicationSettingsBase()
	{
		_cache = new Dictionary<string, object>();
	}

	protected DateTime? GetDateTime(string key)
	{
		long? value = GetValue<long?>(key, null);
		if (!value.HasValue)
		{
			return null;
		}
		return new DateTime(value.Value, DateTimeKind.Utc);
	}

	protected void SetDateTime(string key, DateTime? value)
	{
		if (!value.HasValue)
		{
			SetValue(key, null);
		}
		else
		{
			SetValue(key, value.Value.Ticks);
		}
	}

	protected T GetValue<T>(string key, T defaultObj)
	{
		T result = defaultObj;
		if (((IDictionary<string, object>)ApplicationData.Current.LocalSettings.Values).ContainsKey(key))
		{
			result = (T)((IDictionary<string, object>)ApplicationData.Current.LocalSettings.Values)[key];
		}
		return result;
	}

	protected void SetValue(string key, object obj)
	{
		((IDictionary<string, object>)ApplicationData.Current.LocalSettings.Values)[key] = obj;
	}

	protected void SetSecureObject(string key, object obj)
	{
		//IL_000d: Unknown result type (might be due to invalid IL or missing references)
		//IL_0013: Expected O, but got Unknown
		//IL_005b: Unknown result type (might be due to invalid IL or missing references)
		//IL_0065: Expected O, but got Unknown
		//IL_0041: Unknown result type (might be due to invalid IL or missing references)
		//IL_004b: Expected O, but got Unknown
		_cache[key] = obj;
		PasswordVault val = new PasswordVault();
		if (obj == null)
		{
			string empty = string.Empty;
			try
			{
				PasswordCredential obj2 = val.Retrieve("Vine", key);
				obj2.RetrievePassword();
				empty = obj2.Password;
			}
			catch (Exception)
			{
				return;
			}
			val.Remove(new PasswordCredential("Vine", key, empty));
		}
		else
		{
			string text = Serialization.Serialize(obj);
			val.Add(new PasswordCredential("Vine", key, text));
		}
	}

	protected T GetSecureObject<T>(string key, T defaultObj)
	{
		//IL_0022: Unknown result type (might be due to invalid IL or missing references)
		//IL_0028: Expected O, but got Unknown
		if (_cache.ContainsKey(key))
		{
			return (T)_cache[key];
		}
		string text = null;
		PasswordVault val = new PasswordVault();
		try
		{
			PasswordCredential obj = val.Retrieve("Vine", key);
			obj.RetrievePassword();
			text = obj.Password;
		}
		catch (Exception)
		{
		}
		T val2 = defaultObj;
		if (!string.IsNullOrEmpty(text))
		{
			val2 = Serialization.Deserialize<T>(text);
		}
		_cache[key] = val2;
		return val2;
	}

	protected async Task SetFileObject(string key, object obj)
	{
		_cache[key] = obj;
		string json = Serialization.Serialize(obj);
		StorageFolder folder = await FolderHelper.GetSettingsExtFolderAsync();
		StorageFile file = null;
		try
		{
			file = await folder.GetFileAsync(key);
		}
		catch
		{
		}
		if (file == null)
		{
			file = await folder.CreateFileAsync(key, (CreationCollisionOption)1);
		}
		if (json == null)
		{
			await file.DeleteAsync();
		}
		else
		{
			await FileIO.WriteTextAsync((IStorageFile)(object)file, json);
		}
	}

	protected async Task<T> GetFileObject<T>(string key, T defaultObj)
	{
		if (_cache.ContainsKey(key))
		{
			return (T)_cache[key];
		}
		string json = null;
		try
		{
			json = await FileIO.ReadTextAsync((IStorageFile)(object)(await (await FolderHelper.GetSettingsExtFolderAsync()).GetFileAsync(key)));
		}
		catch
		{
		}
		T val = defaultObj;
		if (!string.IsNullOrEmpty(json))
		{
			val = Serialization.Deserialize<T>(json);
		}
		_cache[key] = val;
		return val;
	}

	protected void SetObject(string key, object obj)
	{
		_cache[key] = obj;
		string obj2 = Serialization.Serialize(obj);
		SetValue(key, obj2);
	}

	protected T GetObject<T>(string key, T defaultObj)
	{
		if (_cache.ContainsKey(key))
		{
			return (T)_cache[key];
		}
		string text = null;
		if (((IDictionary<string, object>)ApplicationData.Current.LocalSettings.Values).ContainsKey(key))
		{
			text = (string)((IDictionary<string, object>)ApplicationData.Current.LocalSettings.Values)[key];
		}
		T val = defaultObj;
		if (!string.IsNullOrEmpty(text))
		{
			val = Serialization.Deserialize<T>(text);
		}
		_cache[key] = val;
		return val;
	}
}
