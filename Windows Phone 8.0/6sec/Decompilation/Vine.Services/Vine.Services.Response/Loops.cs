using System;
using Newtonsoft.Json;

namespace Vine.Services.Response;

public class Loops
{
	[JsonProperty(PropertyName = "count")]
	public double Count { get; set; }

	[JsonProperty(PropertyName = "onFire")]
	public double OnFire { get; set; }

	[JsonProperty(PropertyName = "velocity")]
	public double Velocity { get; set; }

	public DateTime CountStarted { get; set; }

	public Loops()
	{
		CountStarted = DateTime.Now;
	}
}
