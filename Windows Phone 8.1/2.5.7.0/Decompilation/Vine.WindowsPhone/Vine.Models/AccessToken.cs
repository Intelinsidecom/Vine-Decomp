namespace Vine.Models;

public class AccessToken
{
	public string OAuth_Token { get; set; }

	public string OAuth_Token_Secret { get; set; }

	public string User_Id { get; set; }

	public string Screen_Name { get; set; }

	public override string ToString()
	{
		return $"oauth_token {OAuth_Token}, oauth_token_secret {OAuth_Token_Secret}, user_id {User_Id}, screen_name {Screen_Name}";
	}
}
