using System;
using System.Collections.Generic;
using Windows.UI.Xaml;

namespace Vine.Models;

public class VineContactViewModel : NotifyObject
{
	private Visibility _selectionVisibility = (Visibility)1;

	private VineUserModel _user;

	private Visibility _userVisibility = (Visibility)1;

	private Visibility _phoneSelectionVisibility = (Visibility)1;

	private Visibility _phoneVisibility = (Visibility)1;

	private Visibility _emailSelectionVisibility = (Visibility)1;

	private Visibility _emailVisibility = (Visibility)1;

	private bool _isSelected;

	public string Section { get; set; }

	public List<string> Emails { get; set; }

	public List<Tuple<string, string>> Phones { get; set; }

	public Visibility SelectionVisibility
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return _selectionVisibility;
		}
		set
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			//IL_0002: Unknown result type (might be due to invalid IL or missing references)
			_selectionVisibility = value;
			NotifyOfPropertyChange(() => SelectionVisibility);
		}
	}

	public VineUserModel User
	{
		get
		{
			return _user;
		}
		set
		{
			_user = value;
		}
	}

	public Visibility UserVisibility
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return _userVisibility;
		}
		set
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			//IL_0002: Unknown result type (might be due to invalid IL or missing references)
			_userVisibility = value;
			OnPropertyChanged("UserVisibility");
		}
	}

	public Visibility PhoneSelectionVisibility
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return _phoneSelectionVisibility;
		}
		set
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			//IL_0002: Unknown result type (might be due to invalid IL or missing references)
			_phoneSelectionVisibility = value;
			OnPropertyChanged("PhoneSelectionVisibility");
		}
	}

	public Visibility PhoneVisibility
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return _phoneVisibility;
		}
		set
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			//IL_0002: Unknown result type (might be due to invalid IL or missing references)
			_phoneVisibility = value;
			OnPropertyChanged("PhoneVisibility");
		}
	}

	public Visibility EmailSelectionVisibility
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return _emailSelectionVisibility;
		}
		set
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			//IL_0002: Unknown result type (might be due to invalid IL or missing references)
			_emailSelectionVisibility = value;
			OnPropertyChanged("EmailSelectionVisibility");
		}
	}

	public Visibility EmailVisibility
	{
		get
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			return _emailVisibility;
		}
		set
		{
			//IL_0001: Unknown result type (might be due to invalid IL or missing references)
			//IL_0002: Unknown result type (might be due to invalid IL or missing references)
			_emailVisibility = value;
			OnPropertyChanged("EmailVisibility");
		}
	}

	public string HeaderText { get; set; }

	public bool IsSelected
	{
		get
		{
			return _isSelected;
		}
		set
		{
			_isSelected = value;
			if (_isSelected)
			{
				if (User.UserType == VineUserType.User)
				{
					SelectionVisibility = (Visibility)0;
					return;
				}
				PhoneSelectionVisibility = (Visibility)(string.IsNullOrEmpty(User.PhoneNumber) ? 1 : 0);
				EmailSelectionVisibility = (Visibility)(string.IsNullOrEmpty(User.Email) ? 1 : 0);
			}
			else
			{
				SelectionVisibility = (Visibility)1;
				EmailSelectionVisibility = (Visibility)1;
				PhoneSelectionVisibility = (Visibility)1;
			}
		}
	}
}
