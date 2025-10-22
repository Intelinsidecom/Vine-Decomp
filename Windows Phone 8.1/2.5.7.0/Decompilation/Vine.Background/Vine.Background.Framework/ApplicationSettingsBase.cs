using System;
using System.Diagnostics;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using System.Threading.Tasks;
using Windows.Storage;

namespace Vine.Background.Framework;

internal abstract class ApplicationSettingsBase
{
	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CSetFileObject_003Ed__8 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder _003C_003Et__builder;

		public object obj;

		public string key;

		private StorageFile _003Cfile_003E5__1;

		private StorageFolder _003Cfolder_003E5__2;

		private string _003Cjson_003E5__3;

		private TaskAwaiter<StorageFolder> _003C_003Eu__1;

		private TaskAwaiter<StorageFile> _003C_003Eu__2;

		private TaskAwaiter _003C_003Eu__3;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CGetFileObject_003Ed__9<T> : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder<T> _003C_003Et__builder;

		public string key;

		public T defaultObj;

		private string _003Cjson_003E5__1;

		private TaskAwaiter<StorageFolder> _003C_003Eu__1;

		private TaskAwaiter<StorageFile> _003C_003Eu__2;

		private TaskAwaiter<string> _003C_003Eu__3;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	[StructLayout(LayoutKind.Auto)]
	[CompilerGenerated]
	private struct _003CGetChildFolderAsync_003Ed__10 : IAsyncStateMachine
	{
		public int _003C_003E1__state;

		public AsyncTaskMethodBuilder<StorageFolder> _003C_003Et__builder;

		public StorageFolder parentFolder;

		public string name;

		private bool _003CnotFound_003E5__1;

		private StorageFolder _003Cfolder_003E5__2;

		private TaskAwaiter<StorageFolder> _003C_003Eu__1;

		private extern void MoveNext();

		[DebuggerHidden]
		private extern void SetStateMachine(IAsyncStateMachine stateMachine);
	}

	protected extern DateTime? GetDateTime(string key);

	protected extern void SetDateTime(string key, DateTime? value);

	protected extern T GetValue<T>(string key, T defaultObj);

	protected extern void SetValue(string key, object obj);

	protected extern void SetSecureObject(string key, object obj);

	protected extern T GetSecureObject<T>(string key, T defaultObj);

	protected extern void SetObject(string key, object obj);

	protected extern T GetObject<T>(string key, T defaultObj);

	[AsyncStateMachine(typeof(_003CSetFileObject_003Ed__8))]
	protected extern Task SetFileObject(string key, object obj);

	[AsyncStateMachine(typeof(_003CGetFileObject_003Ed__9<>))]
	protected extern Task<T> GetFileObject<T>(string key, T defaultObj);

	[AsyncStateMachine(typeof(_003CGetChildFolderAsync_003Ed__10))]
	private static extern Task<StorageFolder> GetChildFolderAsync(StorageFolder parentFolder, string name);

	protected extern ApplicationSettingsBase();
}
