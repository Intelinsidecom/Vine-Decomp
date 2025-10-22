using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using Vine.Common;
using Vine.Framework;
using Windows.UI.Xaml;

namespace Vine.Models;

public class CommentModel
{
	private FrameworkElement _richBody;

	public bool IsUserComment => User.UserId == ApplicationSettings.Current.UserId;

	public string Comment { get; set; }

	public List<Entity> Entities { get; set; }

	public VineUserModel User { get; set; }

	public string PostId { get; set; }

	public string CommentId { get; set; }

	public DateTime Created { get; set; }

	public string CreatedText => Created.ToVineTime();

	[IgnoreDataMember]
	public FrameworkElement RichBody
	{
		get
		{
			if (_richBody != null)
			{
				return _richBody;
			}
			return _richBody = (FrameworkElement)(object)EntityHelper.RichTextBlock(Comment, Entities, isLargeFont: false, null, boldFirst: true);
		}
	}

	public void ClearRichBody()
	{
		_richBody = null;
	}

	public void AddUserToStart()
	{
		Comment = " " + Comment;
		Entities.Apply(delegate(Entity x)
		{
			x.Range[0] = x.Range[0] + 1;
		});
		Entities.Apply(delegate(Entity x)
		{
			x.Range[1] = x.Range[1] + 1;
		});
		Entities.Insert(0, new Entity
		{
			Id = User.UserId,
			Title = User.Username,
			Type = EntityType.user.ToString(),
			Range = new int[2]
		});
	}
}
