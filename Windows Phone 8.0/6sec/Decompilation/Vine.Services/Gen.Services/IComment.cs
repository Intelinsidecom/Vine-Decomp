using System;

namespace Gen.Services;

public interface IComment
{
	string Id { get; set; }

	string Comment { get; }

	string Username { get; }

	string UserId { get; }

	DateTime Created { get; }

	string Avatar { get; }

	bool IsRemovable { get; set; }
}
