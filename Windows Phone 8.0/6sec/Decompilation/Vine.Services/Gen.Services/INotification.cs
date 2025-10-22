namespace Gen.Services;

public interface INotification
{
	string Body { get; }

	string Avatar { get; }

	string Picture { get; }

	string Created { get; }

	string UserId { get; }
}
