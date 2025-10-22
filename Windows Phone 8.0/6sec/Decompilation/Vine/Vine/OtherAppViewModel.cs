using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading;
using System.Windows;
using System.Xml.Linq;

namespace Vine;

public class OtherAppViewModel : INotifyPropertyChanged
{
	public List<OtherApp> OtherApps { get; set; }

	public bool IsLoading { get; set; }

	public event PropertyChangedEventHandler PropertyChanged;

	public OtherAppViewModel()
	{
		IsLoading = true;
		LoadApps();
	}

	private void LoadApps()
	{
		WebRequest request = WebRequest.Create("http://www.feelmygeek.com/windowsphone/otherapps.php?lang=" + Thread.CurrentThread.CurrentUICulture.TwoLetterISOLanguageName);
		request.BeginGetResponse(delegate(IAsyncResult iar)
		{
			string text = "";
			XElement xElement = (from manifest in XElement.Load("WMAppManifest.xml").Descendants("App")
				select (manifest)).SingleOrDefault();
			if (xElement != null)
			{
				text = xElement.Attribute("Title").Value;
			}
			try
			{
				using StreamReader streamReader = new StreamReader(request.EndGetResponse(iar).GetResponseStream(), Encoding.UTF8);
				string text2 = "";
				OtherApps = new List<OtherApp>();
				while ((text2 = streamReader.ReadLine()) != null)
				{
					string[] array = text2.Split('\t');
					if (!text.Equals(array[0], StringComparison.InvariantCultureIgnoreCase))
					{
						OtherApps.Add(new OtherApp
						{
							Name = array[0],
							Id = array[1],
							Image = array[2],
							Description = array[3]
						});
					}
				}
			}
			catch
			{
				OtherApps = new List<OtherApp>
				{
					new OtherApp
					{
						Name = "Vine",
						Description = "the best newsreader for windows phone",
						Id = "8355da61-1ac5-49cd-a753-7f6afed2bb62",
						Image = "http://catalog.zune.net/v3.2/fr-FR/apps/8355da61-1ac5-49cd-a753-7f6afed2bb62/primaryImage?width=120&height=120&resize=true"
					},
					new OtherApp
					{
						Name = "SQUARO",
						Description = "casual game with a touch of Sudoku and Minesweeper",
						Id = "bb1a3440-c151-4b75-853c-f4bcf5ab152a",
						Image = "http://catalog.zune.net/v3.2/fr-FR/apps/bb1a3440-c151-4b75-853c-f4bcf5ab152a/primaryImage?width=120&height=120&resize=true"
					},
					new OtherApp
					{
						Name = "TVSHOW",
						Description = "the most complete application to track all your favorite TV Shows",
						Id = "f593e6f6-cd49-e011-854c-00237de2db9e",
						Image = "http://catalog.zune.net/v3.2/fr-FR/apps/f593e6f6-cd49-e011-854c-00237de2db9e/primaryImage?width=120&height=120&resize=true"
					}
				};
			}
			((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
			{
				RaisePropertyChanged("OtherApps");
				IsLoading = false;
				RaisePropertyChanged("IsLoading");
			});
		}, null);
	}

	private void RaisePropertyChanged(string name)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(name));
		}
	}
}
