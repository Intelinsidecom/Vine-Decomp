using System;
using System.Collections.Generic;
using Vine.Services.Models;

namespace Vine.Pages.Explore.ViewModels;

public class ChannelsCache
{
	public List<Channel> Channels { get; set; }

	public DateTime Date { get; set; }
}
