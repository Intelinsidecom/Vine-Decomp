using System;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq.Expressions;
using System.Runtime.CompilerServices;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace Vine.Framework;

public class NotifyPage : Page, INotifyPropertyChanged
{
	public double WindowWidth => Window.Current.CoreWindow.Bounds.Width;

	public double WindowHeight => Window.Current.CoreWindow.Bounds.Height;

	public GridLength WindowWidthGridLength => new GridLength(WindowWidth);

	public event PropertyChangedEventHandler PropertyChanged
	{
		add
		{
			_propertyChanged += value;
		}
		remove
		{
			_propertyChanged -= value;
		}
	}

	private event PropertyChangedEventHandler _propertyChanged = delegate
	{
	};

	protected bool SetProperty<T>(ref T storage, T value, [CallerMemberName] string propertyName = null)
	{
		if (object.Equals(storage, value))
		{
			return false;
		}
		storage = value;
		NotifyOfPropertyChange(propertyName);
		return true;
	}

	protected void OnPropertyChanged([CallerMemberName] string propertyName = null)
	{
		NotifyOfPropertyChange(propertyName);
	}

	protected void NotifyOfPropertyChange<TProperty>(Expression<Func<TProperty>> property)
	{
		MemberExpression memberExpression = ((!(property.Body is UnaryExpression)) ? ((MemberExpression)property.Body) : ((MemberExpression)((UnaryExpression)property.Body).Operand));
		NotifyOfPropertyChange(memberExpression.Member.Name);
	}

	public void NotifyOfPropertyChange(string property)
	{
		RaisePropertyChanged(property, verifyProperty: true);
	}

	private void RaisePropertyChanged(string property, bool verifyProperty)
	{
		this._propertyChanged?.Invoke(this, new PropertyChangedEventArgs(property));
	}

	[Conditional("DEBUG")]
	private void VerifyProperty(string property)
	{
	}
}
