using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Vine.Models.Analytics;

namespace Vine.Scribe;

public class ScribeTimer
{
	private Timer _timer;

	private readonly List<ClientEvent> _scribes;

	private readonly object _locker = new object();

	public ScribeTimer()
	{
		_scribes = new List<ClientEvent>();
		Start();
	}

	private void TimerOnTick(object state)
	{
		Flush();
	}

	public Task Flush()
	{
		Task task = SendScribe();
		return Task.WhenAll(task);
	}

	private async Task SendScribe()
	{
		List<ClientEvent> scribes;
		lock (_locker)
		{
			scribes = _scribes.ToList();
			_scribes.Clear();
		}
		if (scribes.Any() && (await App.Api.ScribeList(new ScribeLog
		{
			Events = scribes
		})).HasError)
		{
			lock (_locker)
			{
				_scribes.AddRange(scribes);
			}
		}
	}

	public void Log(ClientEvent scribeEvent)
	{
		lock (_locker)
		{
			_scribes.Add(scribeEvent);
		}
	}

	[Conditional("DEBUG")]
	private static void DebugTrace(ClientEvent scribeEvent)
	{
		_ = string.Empty;
		if (scribeEvent.Navigation != null)
		{
			string.Format(" [section: {0}, view: {1}, timeline_api_url: {2}]", new object[3]
			{
				scribeEvent.Navigation.Section,
				scribeEvent.Navigation.View,
				scribeEvent.Navigation.TimelineApiUrl
			});
		}
	}

	public void Start()
	{
		if (_timer != null)
		{
			_timer.Dispose();
		}
		_timer = new Timer(TimerOnTick, null, TimeSpan.FromSeconds(0.0), TimeSpan.FromSeconds(30.0));
	}
}
