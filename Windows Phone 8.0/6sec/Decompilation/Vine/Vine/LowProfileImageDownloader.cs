using System;
using System.Collections.Generic;
using System.IO;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Net;
using System.Threading;
using System.Windows;
using Huyn.ZLib;
using Vine.Datas;

namespace Vine;

public static class LowProfileImageDownloader
{
	public class SaveImageCache
	{
		public string Local { get; set; }

		public DateTime LastAccess { get; set; }
	}

	private class LowProfileImageState
	{
		public WebRequest WebRequest { get; set; }

		public Uri Uri { get; private set; }

		public Action<Uri, string> Callback { get; set; }

		public LowProfileImageState(Uri uri, Action<Uri, string> callback)
		{
			Uri = uri;
			Callback = callback;
		}
	}

	private const int WorkItemQuantum = 5;

	private static readonly Thread _thread;

	private static readonly Stack<LowProfileImageState> _pendingRequests;

	private static readonly List<LowProfileImageState> _pendingResponses;

	private static readonly object _syncBlock;

	private static bool _exiting;

	public static string Directory;

	private static Dictionary<string, SaveImageCache> Cache;

	static LowProfileImageDownloader()
	{
		_thread = new Thread(WorkerThreadProc);
		_pendingRequests = new Stack<LowProfileImageState>();
		_pendingResponses = new List<LowProfileImageState>();
		_syncBlock = new object();
		Directory = "thumbData";
		Cache = new Dictionary<string, SaveImageCache>();
		_thread.Start();
		Application.Current.Exit += HandleApplicationExit;
		using (IsolatedStorageFile isolatedStorageFile = IsolatedStorageFile.GetUserStoreForApplication())
		{
			if (!isolatedStorageFile.DirectoryExists(Directory))
			{
				isolatedStorageFile.CreateDirectory(Directory);
			}
		}
		if (IsolatedStorageSettings.ApplicationSettings.Contains("ImageCache"))
		{
			Cache = (Dictionary<string, SaveImageCache>)IsolatedStorageSettings.ApplicationSettings["ImageCache"];
		}
	}

	public static void Save()
	{
		DateTime refdate = DateTime.Now.AddDays(-4.0);
		Dictionary<string, SaveImageCache> dictionary = (from d in Cache
			where d.Value.LastAccess > refdate
			orderby d.Value.LastAccess descending
			select d).Take(50).ToDictionary((KeyValuePair<string, SaveImageCache> d) => d.Key, (KeyValuePair<string, SaveImageCache> d) => d.Value);
		using (IsolatedStorageFile isolatedStorageFile = IsolatedStorageFile.GetUserStoreForApplication())
		{
			string[] fileNames = isolatedStorageFile.GetFileNames(Directory + "/*");
			foreach (string text in fileNames)
			{
				try
				{
					string res = Directory + "/" + text;
					if (dictionary.All((KeyValuePair<string, SaveImageCache> f) => f.Value.Local != res))
					{
						isolatedStorageFile.DeleteFile(res);
					}
				}
				catch
				{
				}
			}
		}
		IsolatedStorageSettings.ApplicationSettings["ImageCache"] = dictionary;
	}

	private static void HandleApplicationExit(object sender, EventArgs e)
	{
		_exiting = true;
		if (Monitor.TryEnter(_syncBlock, 100))
		{
			Monitor.Pulse(_syncBlock);
			Monitor.Exit(_syncBlock);
		}
	}

	private static void WorkerThreadProc(object unused)
	{
		object syncBlock = default(object);
		object syncBlock2 = default(object);
		while (!_exiting)
		{
			try
			{
				bool lockTaken = false;
				try
				{
					syncBlock = _syncBlock;
					Monitor.Enter(syncBlock, ref lockTaken);
					if (_pendingRequests.Count == 0 || _pendingResponses.Count >= 5)
					{
						Monitor.Wait(_syncBlock);
						if (_exiting)
						{
							break;
						}
					}
				}
				finally
				{
					if (lockTaken)
					{
						Monitor.Exit(syncBlock);
					}
				}
				while (_pendingRequests.Count > 0 && _pendingResponses.Count < 5)
				{
					LowProfileImageState lowProfileImageState = _pendingRequests.Pop();
					try
					{
						HttpWebRequest httpWebRequest = WebRequest.CreateHttp(lowProfileImageState.Uri);
						httpWebRequest.Headers[HttpRequestHeader.Referer] = "http://" + lowProfileImageState.Uri.Host + "/";
						httpWebRequest.Headers[HttpRequestHeader.AcceptEncoding] = "gzip";
						httpWebRequest.Headers["X-Vine-Client"] = "android/2.0.0";
						httpWebRequest.Headers["vine-session-id"] = DatasProvider.Instance.CurrentUser.User.key;
						httpWebRequest.AllowReadStreamBuffering = true;
						lowProfileImageState.WebRequest = httpWebRequest;
						httpWebRequest.BeginGetResponse(HandleGetResponseResult, lowProfileImageState);
						_pendingResponses.Add(lowProfileImageState);
						Thread.Sleep(1);
					}
					catch (Exception)
					{
						bool lockTaken2 = false;
						try
						{
							syncBlock2 = _syncBlock;
							Monitor.Enter(syncBlock2, ref lockTaken2);
							Monitor.Pulse(_syncBlock);
						}
						finally
						{
							if (lockTaken2)
							{
								Monitor.Exit(syncBlock2);
							}
						}
					}
				}
			}
			catch
			{
			}
		}
	}

	private static void SaveImg(LowProfileImageState responseState, string filepath, Stream stream)
	{
		try
		{
			using (IsolatedStorageFile isolatedStorageFile = IsolatedStorageFile.GetUserStoreForApplication())
			{
				using IsolatedStorageFileStream isolatedStorageFileStream = isolatedStorageFile.CreateFile(filepath);
				byte[] array = new byte[stream.Length];
				stream.Read(array, 0, array.Length);
				isolatedStorageFileStream.Write(array, 0, array.Length);
			}
			Cache[responseState.Uri.OriginalString] = new SaveImageCache
			{
				LastAccess = DateTime.Now,
				Local = filepath
			};
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				responseState.Callback(responseState.Uri, filepath);
			});
		}
		catch
		{
		}
	}

	public static void DownloadImage(Uri uri, Action<Uri, string> callback)
	{
		if (Cache.ContainsKey(uri.OriginalString))
		{
			SaveImageCache saveImageCache = Cache[uri.OriginalString];
			saveImageCache.LastAccess = DateTime.Now;
			callback(uri, saveImageCache.Local);
			return;
		}
		bool lockTaken = false;
		object syncBlock = default(object);
		try
		{
			syncBlock = _syncBlock;
			Monitor.Enter(syncBlock, ref lockTaken);
			_pendingRequests.Push(new LowProfileImageState(uri, callback));
			Monitor.Pulse(_syncBlock);
		}
		finally
		{
			if (lockTaken)
			{
				Monitor.Exit(syncBlock);
			}
		}
	}

	private static void HandleGetResponseResult(IAsyncResult result)
	{
		LowProfileImageState lowProfileImageState = (LowProfileImageState)result.AsyncState;
		try
		{
			WebResponse webResponse = lowProfileImageState.WebRequest.EndGetResponse(result);
			string text = Directory + "/" + Guid.NewGuid().ToString();
			using (IsolatedStorageFile isolatedStorageFile = IsolatedStorageFile.GetUserStoreForApplication())
			{
				while (isolatedStorageFile.FileExists(text))
				{
					text = Directory + "/" + Guid.NewGuid().ToString();
					Thread.Sleep(1);
				}
				Stream stream = webResponse.GetResponseStream();
				if (webResponse.Headers[HttpRequestHeader.ContentEncoding] == "gzip")
				{
					stream = ZlibStream.UnCompress(stream);
				}
				if (stream.CanRead)
				{
					SaveImg(lowProfileImageState, text, stream);
				}
			}
			_pendingResponses.Remove(lowProfileImageState);
			bool lockTaken = false;
			object syncBlock = default(object);
			try
			{
				syncBlock = _syncBlock;
				Monitor.Enter(syncBlock, ref lockTaken);
				Monitor.Pulse(_syncBlock);
			}
			finally
			{
				if (lockTaken)
				{
					Monitor.Exit(syncBlock);
				}
			}
		}
		catch (Exception)
		{
			_pendingResponses.Remove(lowProfileImageState);
			bool lockTaken2 = false;
			object syncBlock2 = default(object);
			try
			{
				syncBlock2 = _syncBlock;
				Monitor.Enter(syncBlock2, ref lockTaken2);
				Monitor.Pulse(_syncBlock);
			}
			finally
			{
				if (lockTaken2)
				{
					Monitor.Exit(syncBlock2);
				}
			}
		}
		Thread.Sleep(1);
	}
}
