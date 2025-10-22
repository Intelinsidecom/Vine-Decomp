using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Vine.Models;

namespace Vine.Web;

public class TwitterException : Exception
{
	public override string Message
	{
		get
		{
			if (Errors != null)
			{
				return ToString();
			}
			return base.Message;
		}
	}

	public List<TwitterErrorModel> Errors { get; set; }

	public TwitterException()
	{
		Errors = new List<TwitterErrorModel>();
	}

	public TwitterException(string message)
		: this(message, (Exception)null)
	{
	}

	public TwitterException(string message, Exception inner)
		: base(message, inner)
	{
		Errors = new List<TwitterErrorModel>
		{
			new TwitterErrorModel
			{
				Code = "-1",
				Message = message
			}
		};
	}

	public TwitterException(TwitterErrorResponseModel errors)
		: this(errors, null)
	{
	}

	public TwitterException(TwitterErrorResponseModel errors, Exception inner)
		: base(null, inner)
	{
		if (errors != null)
		{
			Errors = errors.Errors;
		}
	}

	public TwitterException(string errorCode, string message)
		: this()
	{
		Errors = new List<TwitterErrorModel>
		{
			new TwitterErrorModel
			{
				Code = errorCode,
				Message = message
			}
		};
	}

	public override string ToString()
	{
		if (Errors != null)
		{
			StringBuilder stringBuilder = new StringBuilder();
			foreach (TwitterErrorModel error in Errors)
			{
				stringBuilder.Append(error).AppendLine();
			}
			return stringBuilder.ToString();
		}
		return base.ToString();
	}

	public bool HasErrorCode(string code)
	{
		if (Errors != null)
		{
			return Errors.Any((TwitterErrorModel m) => m.Code == code);
		}
		return false;
	}
}
