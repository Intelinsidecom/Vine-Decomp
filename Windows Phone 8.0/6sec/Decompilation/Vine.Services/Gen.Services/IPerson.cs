namespace Gen.Services;

public interface IPerson
{
	string Name { get; set; }

	string Picture { get; set; }

	string Id { get; set; }

	string Location { get; set; }

	string SubName { get; }

	bool? Following { get; set; }

	bool IsVerified { get; set; }

	void ChangeFollow(bool val);
}
