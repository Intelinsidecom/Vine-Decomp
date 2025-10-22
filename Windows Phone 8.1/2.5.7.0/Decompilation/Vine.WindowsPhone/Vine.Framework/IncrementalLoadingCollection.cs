using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Threading.Tasks;
using Vine.Views;
using Windows.Foundation;
using Windows.UI.Xaml.Data;

namespace Vine.Framework;

public class IncrementalLoadingCollection<T> : ObservableCollection<T>, ISupportIncrementalLoading
{
	private readonly WeakReference _source;

	private readonly int _itemsPerPage;

	private bool _hasMoreItems;

	private WeakReference _pullToRefreshView;

	private bool _disableLoading;

	public bool HasMoreItems => _hasMoreItems;

	public int CurrentPage { get; set; }

	public string Anchor { get; set; }

	public IncrementalLoadingCollection(IIncrementalSource<T> source)
	{
		_source = new WeakReference(source, trackResurrection: false);
		_itemsPerPage = 20;
		_hasMoreItems = false;
	}

	public IncrementalLoadingCollection(IIncrementalSource<T> source, PullToRefreshListControl pullToRefreshView)
		: this(source)
	{
		_pullToRefreshView = new WeakReference(pullToRefreshView, trackResurrection: false);
	}

	public Task<PagedItemsResult<T>> Refresh()
	{
		CurrentPage = 1;
		_hasMoreItems = false;
		return LoadMoreItemsAPI((uint)_itemsPerPage);
	}

	public void ResetItems(IList<T> items, bool scrollToTop = true)
	{
		_hasMoreItems = false;
		List<T> list = base.Items.ToList();
		foreach (T item in items)
		{
			Add(item);
		}
		foreach (T item2 in list)
		{
			Remove(item2);
		}
		_hasMoreItems = items.Any();
		UpdatePullToRefresh(scrollToTop);
	}

	public void ClearAndStop(bool disableLoading = false)
	{
		_hasMoreItems = false;
		_disableLoading = disableLoading;
		Clear();
		CurrentPage = 1;
	}

	private async Task<PagedItemsResult<T>> LoadMoreItemsAPI(uint count)
	{
		PagedItemsResult<T> result = null;
		if (!_disableLoading)
		{
			await DispatcherEx.InvokeBackground(async delegate
			{
				_ = result;
				result = await ((IIncrementalSource<T>)_source.Target).GetPagedItems(CurrentPage, _itemsPerPage, Anchor);
				if (result.IsCancelled)
				{
					result.ApiResult.ErrorParsed = new CancellationException("IIncrementalSource cancelled in GetPagedItems");
				}
				else
				{
					IList<T> viewModels = result.ViewModels;
					_hasMoreItems = viewModels?.Any() ?? false;
					if (_hasMoreItems)
					{
						Anchor = result.Anchor;
					}
				}
			});
		}
		return result;
	}

	private async Task<LoadMoreItemsResult> LoadMoreItemsTask(uint count)
	{
		uint resultCount = 0u;
		if (_source.Target == null || !HasMoreItems || _disableLoading)
		{
			_hasMoreItems = false;
			return new LoadMoreItemsResult
			{
				Count = 0u
			};
		}
		if (CurrentPage == 1)
		{
			CurrentPage = 2;
		}
		PagedItemsResult<T> pagedItemsResult = await LoadMoreItemsAPI(count);
		pagedItemsResult.ApiResult.PopUpErrorIfExists();
		if (pagedItemsResult.ViewModels != null && pagedItemsResult.ViewModels.Any())
		{
			CurrentPage++;
			resultCount = (uint)pagedItemsResult.ViewModels.Count;
			foreach (T viewModel in pagedItemsResult.ViewModels)
			{
				Add(viewModel);
			}
			await UpdatePullToRefresh(scrollToTop: false);
		}
		return new LoadMoreItemsResult
		{
			Count = resultCount
		};
	}

	public IAsyncOperation<LoadMoreItemsResult> LoadMoreItemsAsync(uint count)
	{
		return LoadMoreItemsTask(count).AsAsyncOperation();
	}

	private async Task UpdatePullToRefresh(bool scrollToTop)
	{
		if (_pullToRefreshView != null && _pullToRefreshView.Target != null)
		{
			PullToRefreshListControl pullToRefreshView = (PullToRefreshListControl)_pullToRefreshView.Target;
			await pullToRefreshView.UpdateListPadding();
			if (scrollToTop)
			{
				await pullToRefreshView.ScrollToTop();
			}
		}
	}
}
