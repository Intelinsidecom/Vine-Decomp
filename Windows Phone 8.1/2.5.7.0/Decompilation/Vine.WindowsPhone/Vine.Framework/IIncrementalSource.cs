using System.Threading.Tasks;

namespace Vine.Framework;

public interface IIncrementalSource<T>
{
	Task<PagedItemsResult<T>> GetPagedItems(int page, int count, string anchor);
}
