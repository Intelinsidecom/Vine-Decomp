using System;
using System.CodeDom.Compiler;
using System.Diagnostics;
using System.Runtime.CompilerServices;
using Windows.UI.Xaml;

namespace Vine;

public static class Program
{
	[Serializable]
	[CompilerGenerated]
	private sealed class _003C_003Ec
	{
		public static readonly _003C_003Ec _003C_003E9 = new _003C_003Ec();

		public static ApplicationInitializationCallback _003C_003E9__0_0;

		internal void _003CMain_003Eb__0_0(ApplicationInitializationCallbackParams p)
		{
			new App();
		}
	}

	[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", " 4.0.0.0")]
	[DebuggerNonUserCode]
	private static void Main(string[] args)
	{
		//IL_0014: Unknown result type (might be due to invalid IL or missing references)
		//IL_0019: Unknown result type (might be due to invalid IL or missing references)
		//IL_001f: Expected O, but got Unknown
		object obj = _003C_003Ec._003C_003E9__0_0;
		if (obj == null)
		{
			ApplicationInitializationCallback val = delegate
			{
				new App();
			};
			_003C_003Ec._003C_003E9__0_0 = val;
			obj = (object)val;
		}
		Application.Start((ApplicationInitializationCallback)obj);
	}
}
