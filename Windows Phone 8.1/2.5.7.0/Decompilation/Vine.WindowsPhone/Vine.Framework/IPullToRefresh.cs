using System.Threading.Tasks;

namespace Vine.Framework;

public interface IPullToRefresh
{
	Task PullToRefresh();
}
