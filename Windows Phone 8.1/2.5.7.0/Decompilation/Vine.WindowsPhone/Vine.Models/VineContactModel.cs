using System.Collections.Generic;
using System.Linq;
using Windows.ApplicationModel.Contacts;

namespace Vine.Models;

public class VineContactModel
{
	public string name { get; set; }

	public string phoneNumber { get; set; }

	public static List<VineContactModel> ConvertContactsForVine(IEnumerable<Contact> contacts)
	{
		List<VineContactModel> list = new List<VineContactModel>();
		foreach (Contact contact in contacts)
		{
			if (contact.Phones != null && contact.Phones.Any() && contact.DisplayName != null)
			{
				foreach (ContactPhone phone in contact.Phones)
				{
					list.Add(new VineContactModel
					{
						name = contact.DisplayName,
						phoneNumber = phone.Number
					});
				}
			}
			else if (contact.DisplayName != null)
			{
				list.Add(new VineContactModel
				{
					name = contact.DisplayName
				});
			}
		}
		return list;
	}
}
