using System.Collections.Generic;
using System.Threading.Tasks;
using Gen.Services;
using Microsoft.Phone.UserData;
using Vine.Services.Response;
using Windows.Storage;

namespace Vine.Services;

public interface IServiceWithAuth
{
	Task<bool> AddFacebookToProfileAsync(string facebookId, string facebookToken);

	Task<bool> AnswerFollowingRequestAsync(string requestid, bool answer);

	Task<bool> BlockUserAsync(string requestid, bool block);

	Task<bool> DisconnectFacebookAsync();

	Task<bool> DisconnectTwitterAsync();

	Task<bool> FollowUserAsync(string id, bool follow);

	Task<IListComments> GetMoreCommentsAsync(string PostId, string NextPage);

	Task<IListNotifications> GetNotificationsFollowRequestAsync(string userId, string NextPage);

	Task<IListPersons> GetPagedUsersAsync(string service, string NextPage, Dictionary<string, string> param);

	Task<PendingNotificationsInfo> GetPendingNotificationsAsync();

	Task<bool> LikePostAsync(string postId, bool like);

	Task LogoutAsync();

	Task<IComment> PostCommentAsync(string id, string comment, List<IPerson> persons);

	Task<bool> RemovePostAsync(string id);

	Task<IListTags> SearchTagAsync(string search, string nextpage);

	Task<IListPersons> SearchUserAsync(string search, string nextpage);

	Task<IListPersons> SearchUserMentionAsync(string search);

	Task<IListPersons> SuggestedContactsAsync(string culture, IEnumerable<Contact> contacts);

	Task<IListPosts> TimelinesAsync(string type, string param, int nbrpost, string sort = null);

	Task<bool> UploadAsync(EncodingJob job);

	Task<IProfile> GetProfilInfoAsync(string userId);

	Task<IProfile> GetMyProfilForSettingsAsync();

	Task<bool> AddTwitterToProfileAsync(TwitterAccess access);

	Task<string> UploadImageAsync(StorageFile file, string prefix);

	IListPosts GetCachedTimeLine();
}
