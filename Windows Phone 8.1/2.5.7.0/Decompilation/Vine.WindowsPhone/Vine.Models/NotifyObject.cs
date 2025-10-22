using System;
using System.ComponentModel;
using System.Diagnostics;
using System.Linq.Expressions;
using System.Runtime.CompilerServices;

namespace Vine.Models;

public class NotifyObject : INotifyPropertyChanged
{
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
