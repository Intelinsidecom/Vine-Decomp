namespace Vine.Framework;

public interface IEventHandler<in T>
{
	void Handle(T e);
}
