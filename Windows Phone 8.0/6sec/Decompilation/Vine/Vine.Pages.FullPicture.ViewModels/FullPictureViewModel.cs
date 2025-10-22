using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using Gen.Services;
using Vine.Datas;
using Vine.Services;
using Vine.Services.Models;

namespace Vine.Pages.FullPicture.ViewModels;

public class FullPictureViewModel : INotifyPropertyChanged
{
	[CompilerGenerated]
	private RoutedEventHandler m_CommentLoaded;

	public bool IsLoading { get; set; }

	public IPostRecord Post { get; set; }

	public bool IsMyPost { get; set; }

	public string NextPage { get; set; }

	public bool HasMoreComments { get; set; }

	public ObservableCollection<IComment> Comments { get; set; }

	public bool Ready { get; set; }

	public bool IsCommentLoading { get; set; }

	public bool IsDirect { get; set; }

	public event RoutedEventHandler CommentLoaded
	{
		[CompilerGenerated]
		add
		{
			//IL_0010: Unknown result type (might be due to invalid IL or missing references)
			//IL_0016: Expected O, but got Unknown
			RoutedEventHandler val = this.m_CommentLoaded;
			RoutedEventHandler val2;
			do
			{
				val2 = val;
				RoutedEventHandler value2 = (RoutedEventHandler)Delegate.Combine((Delegate)(object)val2, (Delegate)(object)value);
				val = Interlocked.CompareExchange(ref this.m_CommentLoaded, value2, val2);
			}
			while (val != val2);
		}
		[CompilerGenerated]
		remove
		{
			//IL_0010: Unknown result type (might be due to invalid IL or missing references)
			//IL_0016: Expected O, but got Unknown
			RoutedEventHandler val = this.m_CommentLoaded;
			RoutedEventHandler val2;
			do
			{
				val2 = val;
				RoutedEventHandler value2 = (RoutedEventHandler)Delegate.Remove((Delegate)(object)val2, (Delegate)(object)value);
				val = Interlocked.CompareExchange(ref this.m_CommentLoaded, value2, val2);
			}
			while (val != val2);
		}
	}

	public event PropertyChangedEventHandler PropertyChanged;

	private async Task CommentRemoved(IComment comment)
	{
	}

	public void Init(IPostRecord post)
	{
		if (post == null)
		{
			return;
		}
		Post = post;
		IListComments comments = post.GetComments();
		string id = DatasProvider.Instance.CurrentUser.User.Id;
		NextPage = comments?.NextPage;
		if (comments == null)
		{
			NextPage = null;
		}
		else
		{
			NextPage = ((comments != null && comments.Comments != null && comments.Comments.Count > 1) ? comments.NextPage : "1");
		}
		HasMoreComments = !string.IsNullOrEmpty(NextPage) && post.NbrComments > comments.Comments.Count;
		IsMyPost = post.UserId == id;
		try
		{
			if (comments != null)
			{
				List<IComment> comments2 = comments.Comments;
				ObservableCollection<IComment> observableCollection = new ObservableCollection<IComment>();
				foreach (IComment item in (IEnumerable<IComment>)comments2)
				{
					item.IsRemovable = IsMyPost || id == item.UserId;
					observableCollection.Add(item);
				}
				Comments = observableCollection;
			}
			else
			{
				Comments = new ObservableCollection<IComment>();
			}
		}
		catch
		{
			Comments = new ObservableCollection<IComment>();
		}
		Ready = true;
		((DependencyObject)Deployment.Current).Dispatcher.BeginInvoke((Action)delegate
		{
			RaisePropertyChanged("HasMoreComments");
			RaisePropertyChanged("Comments");
			RaisePropertyChanged("Ready");
		});
		if (Comments.Count < 15)
		{
			LoadMore();
		}
	}

	public async Task LoadPost(string postid)
	{
		_ = DatasProvider.Instance.CurrentUser;
	}

	public async Task<IComment> LoadMore()
	{
		if (string.IsNullOrEmpty(NextPage))
		{
			HasMoreComments = false;
			RaisePropertyChanged("HasMoreComments");
			return null;
		}
		return await LoadComments(NextPage);
	}

	private async Task<IComment> LoadComments(string page, bool clear = false)
	{
		IsCommentLoading = true;
		RaisePropertyChanged("IsCommentLoading");
		HasMoreComments = false;
		RaisePropertyChanged("HasMoreComments");
		DataUser currentUser = DatasProvider.Instance.CurrentUser;
		try
		{
			IListComments listComments = await currentUser.Service.GetMoreCommentsAsync(Post.PostId, page);
			if (listComments != null && listComments.Comments != null)
			{
				NextPage = listComments.NextPage;
				try
				{
					if (clear)
					{
						Comments.Clear();
					}
					string id = DatasProvider.Instance.CurrentUser.User.Id;
					List<string> list = Comments.Select((IComment c) => c.Id).Distinct().ToList();
					foreach (IComment item in (IEnumerable<IComment>)listComments.Comments)
					{
						if (item.Id == null || !list.Contains(item.Id))
						{
							item.IsRemovable = item.UserId == id || IsMyPost;
							Comments.Add(item);
						}
					}
				}
				catch
				{
				}
				HasMoreComments = !string.IsNullOrEmpty(NextPage);
				RaisePropertyChanged("HasMoreComments");
				if (this.CommentLoaded != null)
				{
					this.CommentLoaded.Invoke((object)this, new RoutedEventArgs());
				}
				return listComments.Comments.LastOrDefault();
			}
			HasMoreComments = true;
			RaisePropertyChanged("HasMoreComments");
		}
		catch (ServiceServerErrorException)
		{
		}
		finally
		{
			IsCommentLoading = false;
			RaisePropertyChanged("IsCommentLoading");
		}
		return null;
	}

	private void RaisePropertyChanged(string p)
	{
		if (this.PropertyChanged != null)
		{
			this.PropertyChanged(this, new PropertyChangedEventArgs(p));
		}
	}

	internal async Task Reload()
	{
		await LoadComments("1", clear: true);
	}

	internal void AddMyComment(IComment comment)
	{
		Comments.Add(comment);
	}

	internal void SetDirect()
	{
	}
}
