using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Threading.Tasks;
using Vine.Common;
using Windows.Storage;
using Windows.Storage.Streams;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace Vine.Framework;

internal sealed class SuspensionManager
{
	private static Dictionary<string, object> _sessionState = new Dictionary<string, object>();

	private const string _sessionStateFilename = "_sessionState.json";

	private static DependencyProperty FrameSessionStateKeyProperty = DependencyProperty.RegisterAttached("_FrameSessionStateKey", typeof(string), typeof(SuspensionManager), (PropertyMetadata)null);

	private static DependencyProperty FrameSessionBaseKeyProperty = DependencyProperty.RegisterAttached("_FrameSessionBaseKeyParams", typeof(string), typeof(SuspensionManager), (PropertyMetadata)null);

	private static DependencyProperty FrameSessionStateProperty = DependencyProperty.RegisterAttached("_FrameSessionState", typeof(Dictionary<string, object>), typeof(SuspensionManager), (PropertyMetadata)null);

	private static List<WeakReference<Frame>> _registeredFrames = new List<WeakReference<Frame>>();

	public static Dictionary<string, object> SessionState => _sessionState;

	public static async Task SaveAsync()
	{
		try
		{
			foreach (WeakReference<Frame> registeredFrame in _registeredFrames)
			{
				if (registeredFrame.TryGetTarget(out var target))
				{
					SaveFrameNavigationState(target);
				}
			}
			await TaskManager.CleanUpTasks.WaitForEmptyQueue();
			string json = Serialization.SerializeType(_sessionState);
			await FileIO.WriteTextAsync((IStorageFile)(object)(await ApplicationData.Current.LocalFolder.CreateFileAsync("_sessionState.json", (CreationCollisionOption)1)), json, (UnicodeEncoding)0);
		}
		catch (Exception e)
		{
			throw new SuspensionManagerException(e);
		}
	}

	public static async Task RestoreAsync(string sessionBaseKey = null)
	{
		_sessionState = new Dictionary<string, object>();
		try
		{
			_sessionState = Serialization.DeserializeType<Dictionary<string, object>>(await FileIO.ReadTextAsync((IStorageFile)(object)(await ApplicationData.Current.LocalFolder.GetFileAsync("_sessionState.json"))));
			foreach (WeakReference<Frame> registeredFrame in _registeredFrames)
			{
				if (registeredFrame.TryGetTarget(out var target) && (string)((DependencyObject)target).GetValue(FrameSessionBaseKeyProperty) == sessionBaseKey)
				{
					((DependencyObject)target).ClearValue(FrameSessionStateProperty);
					RestoreFrameNavigationState(target);
				}
			}
		}
		catch (Exception e)
		{
			Debugger.Break();
			throw new SuspensionManagerException(e);
		}
	}

	public static void RegisterFrame(Frame frame, string sessionStateKey, string sessionBaseKey = null)
	{
		if (((DependencyObject)frame).GetValue(FrameSessionStateKeyProperty) != null)
		{
			throw new InvalidOperationException("Frames can only be registered to one session state key");
		}
		if (((DependencyObject)frame).GetValue(FrameSessionStateProperty) != null)
		{
			throw new InvalidOperationException("Frames must be either be registered before accessing frame session state, or not registered at all");
		}
		if (!string.IsNullOrEmpty(sessionBaseKey))
		{
			((DependencyObject)frame).SetValue(FrameSessionBaseKeyProperty, (object)sessionBaseKey);
			sessionStateKey = sessionBaseKey + "_" + sessionStateKey;
		}
		((DependencyObject)frame).SetValue(FrameSessionStateKeyProperty, (object)sessionStateKey);
		_registeredFrames.Add(new WeakReference<Frame>(frame));
		RestoreFrameNavigationState(frame);
	}

	public static void UnregisterFrame(Frame frame)
	{
		SessionState.Remove((string)((DependencyObject)frame).GetValue(FrameSessionStateKeyProperty));
		_registeredFrames.RemoveAll((WeakReference<Frame> weakFrameReference) => !weakFrameReference.TryGetTarget(out var target) || target == frame);
	}

	public static Dictionary<string, object> SessionStateForFrame(Frame frame)
	{
		Dictionary<string, object> dictionary = (Dictionary<string, object>)((DependencyObject)frame).GetValue(FrameSessionStateProperty);
		if (dictionary == null)
		{
			string text = (string)((DependencyObject)frame).GetValue(FrameSessionStateKeyProperty);
			if (text != null)
			{
				if (!_sessionState.ContainsKey(text))
				{
					_sessionState[text] = new Dictionary<string, object>();
				}
				dictionary = (Dictionary<string, object>)_sessionState[text];
			}
			else
			{
				dictionary = new Dictionary<string, object>();
			}
			((DependencyObject)frame).SetValue(FrameSessionStateProperty, (object)dictionary);
		}
		return dictionary;
	}

	private static void RestoreFrameNavigationState(Frame frame)
	{
		Dictionary<string, object> dictionary = SessionStateForFrame(frame);
		if (dictionary.ContainsKey("Navigation"))
		{
			frame.SetNavigationState((string)dictionary["Navigation"]);
		}
	}

	private static void SaveFrameNavigationState(Frame frame)
	{
		SessionStateForFrame(frame)["Navigation"] = frame.GetNavigationState();
	}
}
