using System;
using System.IO;
using System.Runtime.Serialization;
using System.Threading.Tasks;
using System.Xml.Serialization;
using Vine.Services.Utils;
using Windows.Storage;

namespace Vine.Datas;

public class DatasProvider
{
	public const string DATAFILE = "6secdatas.dat";

	public const string DATAFILEBAK = "6secdatas.dat.bak";

	private static Datas _instance;

	[XmlIgnore]
	public static Datas Instance
	{
		get
		{
			if (_instance != null)
			{
				return _instance;
			}
			AsyncHelper.RunSync(async delegate
			{
				using (await Datas.asyncLock.LockAsync())
				{
					if (_instance != null)
					{
						return;
					}
					await Init();
				}
			});
			return _instance;
		}
	}

	public static async Task Init()
	{
		StorageFolder folder = ApplicationData.Current.LocalFolder;
		try
		{
			if (_instance != null)
			{
				return;
			}
			if (await folder.FileExistsAsync("6secdatas.dat"))
			{
				StorageFile file = default(StorageFile);
				_ = file;
				file = await folder.GetFileAsync("6secdatas.dat");
				DataContractSerializer xmlSerializer = new DataContractSerializer(typeof(Datas));
				using Stream stream = await ((IStorageFile)(object)file).OpenStreamForReadAsync();
				if (xmlSerializer.ReadObject(stream) is Datas instance)
				{
					_instance = instance;
					if (_instance != null)
					{
						_instance.Init();
						Task.Run(async delegate
						{
							try
							{
								StorageFile val = await folder.CreateFileAsync("6secdatas.dat.bak", (CreationCollisionOption)1);
								await file.CopyAndReplaceAsync((IStorageFile)(object)val);
							}
							catch
							{
							}
						});
						return;
					}
				}
			}
		}
		catch (Exception)
		{
		}
		if (await folder.FileExistsAsync("6secdatas.dat.bak"))
		{
			try
			{
				using Stream stream2 = await ((IStorageFile)(object)(await folder.GetFileAsync("6secdatas.dat.bak"))).OpenStreamForReadAsync();
				if (new DataContractSerializer(typeof(Datas)).ReadObject(stream2) is Datas instance2)
				{
					_instance = instance2;
					if (Instance != null)
					{
						_instance.Init();
						return;
					}
				}
			}
			catch
			{
			}
		}
		_instance = new Datas();
		_instance.Init();
	}
}
