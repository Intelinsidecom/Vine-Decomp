using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Globalization;
using System.IO;
using System.IO.IsolatedStorage;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Security.Cryptography;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Gen.Services;
using Microsoft.Phone.UserData;
using Newtonsoft.Json;
using Service.Services;
using Vine.Datas;
using Vine.Services.Models;
using Vine.Services.Models.Direct;
using Vine.Services.Response;
using Vine.Services.Response.Auth;
using Vine.Services.Response.Notifications;
using Vine.Services.Utils;
using Vine.Utils;
using Windows.Storage;

namespace Vine.Services;

public class VineServiceWithAuth : ServiceBase, IServiceWithAuth
{
	private DataUser _User;

	public const string TmpVideoFolder = "tmpVideo";

	public static async Task RemoveTempFile()
	{
		try
		{
			StorageFolder val = await ApplicationData.Current.TemporaryFolder.GetFolderAsync("tmpVideo");
			if (val == null)
			{
				return;
			}
			foreach (StorageFile item in (await val.GetFilesAsync()).ToList())
			{
				await item.DeleteAsync();
			}
		}
		catch
		{
		}
	}

	public async Task<Uri> DownloadAndGetUriAsync(IPostRecord post)
	{
		string filename = (from d in new SHA1Managed().ComputeHash(Encoding.UTF8.GetBytes(post.VideoLink))
			select d.ToString("X2")).Aggregate((string a, string b) => a + b);
		StorageFolder folder = await ApplicationData.Current.LocalFolder.CreateFolderAsync("tmpVideo", (CreationCollisionOption)3);
		if (!(await folder.FileExistsAsync(filename)))
		{
			try
			{
				HttpResponseMessage httpResponseMessage = await _httpClient.GetAsync(new Uri(post.VideoLink));
				if (!httpResponseMessage.IsSuccessStatusCode)
				{
					return null;
				}
				Stream stream = await httpResponseMessage.Content.ReadAsStreamAsync();
				using Stream filestream = await ((IStorageFile)(object)(await folder.CreateFileAsync(filename, (CreationCollisionOption)1))).OpenStreamForWriteAsync();
				await stream.CopyToAsync(filestream);
			}
			catch
			{
				return null;
			}
		}
		return new Uri("tmpVideo/" + filename, UriKind.Relative);
	}

	public async Task<Stream> DownloadAsync(IPostRecord post)
	{
		Uri uri = await DownloadAndGetUriAsync(post);
		if (uri != null)
		{
			using (IsolatedStorageFile isf = IsolatedStorageFile.GetUserStoreForApplication())
			{
				using IsolatedStorageFileStream file = isf.OpenFile(uri.OriginalString, FileMode.Open);
				MemoryStream mem = new MemoryStream();
				await file.CopyToAsync(mem);
				mem.Position = 0L;
				return mem;
			}
		}
		return null;
	}

	public Task<bool> BlockUserAsync(string requestid, bool answer)
	{
		Uri uri = new Uri("users/" + _User.Id + "/blocked/" + requestid, UriKind.Relative);
		return SendRequest(answer ? HttpMethod.Post : HttpMethod.Delete, uri, null, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			VineJsonResponse vineJsonResponse = new JsonSerializer().Deserialize<VineJsonResponse>(new JsonTextReader(reader));
			if (vineJsonResponse.success)
			{
				return vineJsonResponse.success;
			}
			throw new Exception();
		});
	}

	public Task<bool> AnswerFollowingRequestAsync(string requestid, bool answer)
	{
		Uri uri = new Uri("users/" + _User.Id + "/followers/requests/" + requestid, UriKind.Relative);
		return SendRequest(answer ? HttpMethod.Put : HttpMethod.Delete, uri, null, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			VineJsonResponse vineJsonResponse = new JsonSerializer().Deserialize<VineJsonResponse>(new JsonTextReader(reader));
			if (vineJsonResponse.success)
			{
				return vineJsonResponse.success;
			}
			throw new Exception();
		});
	}

	public Task<bool> UploadAsync(EncodingJob job)
	{
		string text = "";
		if (job.IsDirect)
		{
			string text2 = "";
			int num = 10;
			foreach (EncodingJobReceiver selectedUser in job.SelectedUsers)
			{
				string text3 = "{\"userId\":" + JsonConvert.SerializeObject(selectedUser.Id).Replace("/", "\\/") + ",\"display\":" + JsonConvert.SerializeObject(selectedUser.Name).Replace("/", "\\/") + ",\"recipientId\":\"" + num++ + "\"}";
				if (text2.Length > 0)
				{
					text2 += ",";
				}
				text2 += text3;
			}
			text = "{\"to\":[" + text2 + "],\"message\":" + JsonConvert.SerializeObject(job.VineComment).Replace("/", "\\/") + ",\"model\":\"Nexus 4\",\"created\":" + DateTime.Now.ToUnixTimeMilli() + ",\"videoUrl\":" + JsonConvert.SerializeObject(job.AmazonVideo).Replace("/", "\\/") + ",\"locale\":\"" + RegionInfo.CurrentRegion.TwoLetterISORegionName + "\",\"thumbnailUrl\":" + JsonConvert.SerializeObject(job.AmazonCapture).Replace("/", "\\/") + ",\"device\":\"mako\",\"version\":\"1400152\"}";
		}
		else
		{
			string text4 = "";
			foreach (IPerson commentPerson in job.CommentPeople)
			{
				foreach (Match item in new Regex("(^|[^\\w])(?<name>" + Regex.Escape(commentPerson.Name) + ")($|[^\\w])", RegexOptions.Compiled).Matches(job.VineComment))
				{
					int index = item.Groups["name"].Index;
					string text5 = "{\"type\":\"mention\",\"id\":\"" + commentPerson.Id + "\",\"text\":\"" + commentPerson.Name.Replace("\\", "\\\\").Replace("\"", "\\\"") + "\",\"range\":[" + index + "," + (index + commentPerson.Name.Length) + "]}";
					if (text4.Length > 0)
					{
						text4 += ",";
					}
					text4 += text5;
				}
			}
			text = "{\"channelId\":\"" + job.ChannelId + "\",\"thumbnailUrl\":" + JsonConvert.SerializeObject(job.AmazonCapture).Replace("/", "\\/") + ",\"videoUrl\":" + JsonConvert.SerializeObject(job.AmazonVideo).Replace("/", "\\/") + ",\"description\":" + JsonConvert.SerializeObject(job.VineComment).Replace("/", "\\/") + (job.VineShareTwitter ? ",\"postToTwitter\":true" : "") + (job.VineShareFacebook ? ",\"postToFacebook\":true" : "") + ",\"entities\":[" + text4 + "]";
			if (job.Place != null)
			{
				text = text + ",\"venueName\":\"" + job.Place.name.Replace("/", "\\/") + "\",\"foursquareVenueId\":\"" + job.Place.id.Replace("/", "\\/") + "\"";
			}
			text += "}";
		}
		string text6 = "";
		text6 = ((!job.IsDirect) ? "posts" : "conversations");
		Uri uri = new Uri(text6, UriKind.Relative);
		return SendRequest(HttpMethod.Post, uri, new StringContent(text, Encoding.UTF8, "application/json"), async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			if (new JsonSerializer().Deserialize<VineJsonResponse>(new JsonTextReader(reader)).success)
			{
				return true;
			}
			throw new Exception();
		});
	}

	public Task<bool> DisconnectTwitterAsync()
	{
		string postdata = "twitterId=%5Cs&twitterOauthSecret=%5Cs&twitterOauthToken=%5Cs";
		return ModifyProfileAsync(postdata);
	}

	public Task<bool> DisconnectFacebookAsync()
	{
		string postdata = "facebookId=%5Cs&facebookOauthToken=%5Cs";
		return ModifyProfileAsync(postdata);
	}

	public Task<bool> ModifyProfileAsync(string name, string value)
	{
		return ModifyProfileAsync(name, value, null);
	}

	public Task<bool> ModifyProfileAsync(string name, string value, string extra)
	{
		string text = name + "=" + HttpUtility.UrlEncode(value);
		if (extra != null)
		{
			text += extra;
		}
		return ModifyProfileAsync(text);
	}

	public Task<bool> ModifyProfileAsync(string postdata)
	{
		Uri uri = new Uri("users/" + _User.Id, UriKind.Relative);
		return SendRequest(HttpMethod.Put, uri, new StringContent(postdata, Encoding.UTF8, "application/x-www-form-urlencoded"), async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			if (new JsonSerializer().Deserialize<VineJsonResponse>(new JsonTextReader(reader)).success)
			{
				return true;
			}
			throw new Exception();
		});
	}

	public Task<bool> ModifyPreferencesAsync(string name, string value)
	{
		string content = name + "=" + HttpUtility.UrlEncode(value);
		Uri uri = new Uri("users/" + _User.Id + "/preferences", UriKind.Relative);
		return SendRequest(HttpMethod.Put, uri, new StringContent(content, Encoding.UTF8, "application/x-www-form-urlencoded"), async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			if (new JsonSerializer().Deserialize<VineJsonResponse>(new JsonTextReader(reader)).success)
			{
				return true;
			}
			throw new Exception();
		});
	}

	public Task<bool> ModifyExplicitAsync(bool isExplicit)
	{
		Uri uri = new Uri("users/" + _User.Id + "/explicit", UriKind.Relative);
		return SendRequest(isExplicit ? HttpMethod.Post : HttpMethod.Delete, uri, null, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			if (new JsonSerializer().Deserialize<VineJsonResponse>(new JsonTextReader(reader)).success)
			{
				return true;
			}
			throw new Exception();
		});
	}

	public Task<bool> FollowUserAsync(string id, bool follow)
	{
		Uri uri = new Uri("users/" + id + "/followers", UriKind.Relative);
		return SendRequest(follow ? HttpMethod.Post : HttpMethod.Delete, uri, null, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			AuthJsonObject authJsonObject = new JsonSerializer().Deserialize<AuthJsonObject>(new JsonTextReader(reader));
			if (authJsonObject.success)
			{
				return authJsonObject.success;
			}
			throw new Exception();
		});
	}

	public Task<bool> FollowChannelAsync(string id, bool follow)
	{
		Uri uri = new Uri(id + "/followers", UriKind.Relative);
		return SendRequest(follow ? HttpMethod.Post : HttpMethod.Delete, uri, null, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			AuthJsonObject authJsonObject = new JsonSerializer().Deserialize<AuthJsonObject>(new JsonTextReader(reader));
			if (authJsonObject.success)
			{
				return authJsonObject.success;
			}
			throw new Exception();
		});
	}

	public IListPosts GetCachedTimeLine()
	{
		try
		{
			using IsolatedStorageFile isolatedStorageFile = IsolatedStorageFile.GetUserStoreForApplication();
			if (isolatedStorageFile.DirectoryExists("cache"))
			{
				string path = "cache/" + DatasProvider.Instance.CurrentUser.User.Id + "-timeline";
				using IsolatedStorageFileStream stream = isolatedStorageFile.OpenFile(path, FileMode.Open);
				StreamReader reader = new StreamReader(stream);
				TimelineRootObject timelineRootObject = new JsonSerializer().Deserialize<TimelineRootObject>(new JsonTextReader(reader));
				if (timelineRootObject != null && timelineRootObject.success)
				{
					return new IListPosts
					{
						Posts = timelineRootObject.data.records.Where((VinePostRecord p) => p.Thumb != null).Select((Func<VinePostRecord, IPostRecord>)((VinePostRecord p) => p)).ToList()
					};
				}
			}
		}
		catch
		{
		}
		return null;
	}

	public Task<IListPosts> TimelinesAsync(string type, string param, int nbrpost, string sort = null)
	{
		Uri uri = new Uri("timelines/" + type + "?" + param + "&size=" + nbrpost + ((sort != null) ? ("&sort=" + sort) : ""), UriKind.Relative);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			TimelineRootObject timelineRootObject = new JsonSerializer().Deserialize<TimelineRootObject>(new JsonTextReader(reader));
			if (timelineRootObject.success)
			{
				string nextPage = null;
				bool followed = false;
				if (timelineRootObject.data.channel != null)
				{
					followed = timelineRootObject.data.channel.following;
				}
				if (!string.IsNullOrEmpty(timelineRootObject.data.nextPage))
				{
					nextPage = "page=" + timelineRootObject.data.nextPage + "&anchor=" + timelineRootObject.data.anchor;
				}
				return new IListPosts
				{
					Followed = followed,
					NextPage = nextPage,
					Posts = ((IEnumerable<VinePostRecord>)timelineRootObject.data.records).Select((Func<VinePostRecord, IPostRecord>)((VinePostRecord f) => f)).ToList()
				};
			}
			return (IListPosts)null;
		});
	}

	public Task<IListPersons> SuggestedContactsAsync(string culture, IEnumerable<Contact> contacts)
	{
		string text = "[" + (from contact in contacts
			where contact.PhoneNumbers != null
			from phone in contact.PhoneNumbers
			select "{\"name\":\"" + contact.DisplayName + "\",\"phoneNumber\":\"" + phone.PhoneNumber.Replace(" ", "") + "\"}").Aggregate((string a, string b) => a + "," + b) + "]";
		string content = "addressBook=" + HttpUtility.UrlEncode(text) + "&locale=" + culture;
		Uri uri = new Uri("users/" + _User.Id + "/following/suggested/contacts", UriKind.Relative);
		return SendRequest(HttpMethod.Post, uri, new StringContent(content, Encoding.UTF8, "application/x-www-form-urlencoded"), async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			SuggestContactResponse suggestContactResponse = new JsonSerializer().Deserialize<SuggestContactResponse>(new JsonTextReader(reader));
			return suggestContactResponse.success ? new IListPersons
			{
				Persons = ((IEnumerable<VineRecordPerson>)suggestContactResponse.data).Select((Func<VineRecordPerson, IPerson>)((VineRecordPerson f) => f)).ToList()
			} : null;
		});
	}

	public Task<List<VineRecordPerson>> SuggestedTwitterAsync(string userid)
	{
		Uri uri = new Uri("users/" + userid + "/following/suggested/twitter", UriKind.Relative);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			SuggestContactResponse suggestContactResponse = new JsonSerializer().Deserialize<SuggestContactResponse>(new JsonTextReader(reader));
			return suggestContactResponse.success ? suggestContactResponse.data : null;
		});
	}

	public Task<PendingNotificationsInfo> GetPendingNotificationsAsync()
	{
		Uri uri = new Uri("users/" + _User.Id + "/activityCounts?rand=" + DateTime.Now.Ticks, UriKind.Relative);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			NotificationCounterResponse notificationCounterResponse = new JsonSerializer().Deserialize<NotificationCounterResponse>(new JsonTextReader(reader));
			return notificationCounterResponse.success ? new PendingNotificationsInfo
			{
				NbrNotifications = notificationCounterResponse.Data.Notifications,
				NbrMessages = notificationCounterResponse.Data.Messages
			} : null;
		});
	}

	public Task<IListNotifications> GetNotificationsAsync(string userid, string nextPage)
	{
		Uri uri = new Uri("users/" + userid + "/notifications" + ((nextPage != null) ? ("?page=" + nextPage) : ""), UriKind.Relative);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			NotificationsRootObject notificationsRootObject = new JsonSerializer().Deserialize<NotificationsRootObject>(new JsonTextReader(reader));
			return notificationsRootObject.success ? new IListNotifications
			{
				NextPage = notificationsRootObject.data.nextPage.ToString(),
				Notifications = ((IEnumerable<RecordNotification>)notificationsRootObject.data.records).Select((Func<RecordNotification, INotification>)((RecordNotification f) => f)).ToList()
			} : null;
		});
	}

	public Task<IListNotifications> GetNotificationsFollowRequestAsync(string userid, string nextPage)
	{
		Uri uri = new Uri("users/" + userid + "/notifications/followRequests", UriKind.Relative);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			NotificationsRootObject notificationsRootObject = new JsonSerializer().Deserialize<NotificationsRootObject>(new JsonTextReader(reader));
			return notificationsRootObject.success ? new IListNotifications
			{
				NextPage = notificationsRootObject.data.nextPage.ToString(),
				Notifications = ((IEnumerable<RecordNotification>)notificationsRootObject.data.records).Select((Func<RecordNotification, INotification>)((RecordNotification f) => f)).ToList()
			} : null;
		});
	}

	public Task<bool> SharePostTwitterAsync(string postId, string message)
	{
		string content = "message=" + HttpUtility.UrlEncode(message);
		Uri uri = new Uri("posts/" + postId + "/share/twitter", UriKind.Relative);
		return SendRequest(HttpMethod.Post, uri, new StringContent(content, Encoding.UTF8, "application/x-www-form-urlencoded"), async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			VineJsonResponse vineJsonResponse = new JsonSerializer().Deserialize<VineJsonResponse>(new JsonTextReader(reader));
			if (vineJsonResponse.success)
			{
				return vineJsonResponse.success;
			}
			throw new Exception();
		});
	}

	public Task<List<Channel>> ListChannelsAsync()
	{
		Uri uri = new Uri("channels/featured", UriKind.Relative);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			ChannelRootObject channelRootObject = new JsonSerializer().Deserialize<ChannelRootObject>(new JsonTextReader(reader));
			if (channelRootObject.success)
			{
				List<Channel> list = new List<Channel>();
				foreach (RecordChannel record in channelRootObject.data.records)
				{
					list.Add(new Channel
					{
						Id = record.channelId,
						Name = record.channel.Replace("wierd", "weird"),
						Thumb = "https://vine.co" + record.retinaIconUrl,
						BackgroundColor = VineGenUtils.HexColor(record.backgroundColor),
						ForegroundColor = VineGenUtils.HexColor(record.fontColor)
					});
				}
				return list;
			}
			return (List<Channel>)null;
		});
	}

	public Task<string> ReVineAsync(string postId, string revineid)
	{
		Uri uri = new Uri("posts/" + postId + "/repost" + ((revineid != "0") ? ("/" + revineid) : ""), UriKind.Relative);
		return SendRequest((revineid == "0") ? HttpMethod.Post : HttpMethod.Delete, uri, null, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			RevineRootObject revineRootObject = new JsonSerializer().Deserialize<RevineRootObject>(new JsonTextReader(reader));
			return revineRootObject.success ? ((revineRootObject.data != null) ? (revineRootObject.data.repostId ?? "0") : "0") : null;
		});
	}

	public Task<bool> AddTwitterToProfileAsync(TwitterAccess access)
	{
		string content = "twitterId=" + access.UserId + "&twitterOauthSecret=" + HttpUtility.UrlEncode(access.AccessTokenSecret) + "&twitterOauthToken=" + HttpUtility.UrlEncode(access.AccessToken);
		Uri uri = new Uri("users/" + _User.Id, UriKind.Relative);
		return SendRequest(HttpMethod.Put, uri, new StringContent(content, Encoding.UTF8, "application/x-www-form-urlencoded"), async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			VineJsonResponse vineJsonResponse = new JsonSerializer().Deserialize<VineJsonResponse>(new JsonTextReader(reader));
			if (vineJsonResponse.success)
			{
				return vineJsonResponse.success;
			}
			throw new Exception();
		});
	}

	public async Task LogoutAsync()
	{
		Uri uri = new Uri("users/authenticate", UriKind.Relative);
		await SendRequest(HttpMethod.Delete, uri, null, async (HttpResponseMessage response) => true);
	}

	public Task<bool> LikePostAsync(string postId, bool like)
	{
		Uri uri = new Uri("posts/" + postId + "/likes", UriKind.Relative);
		return SendRequest(like ? HttpMethod.Post : HttpMethod.Delete, uri, null, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			if (new JsonSerializer().Deserialize<VineJsonResponse>(new JsonTextReader(reader)).success)
			{
				return like;
			}
			throw new Exception();
		});
	}

	public Task<IListPersons> GetPagedUsersAsync(string service, string nextPage, Dictionary<string, string> param)
	{
		Uri uri = new Uri(service + "?page=" + nextPage + "&size=20", UriKind.Relative);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			PagedUserRootObject pagedUserRootObject = new JsonSerializer().Deserialize<PagedUserRootObject>(new JsonTextReader(reader));
			return pagedUserRootObject.success ? new IListPersons
			{
				NextPage = pagedUserRootObject.data.nextPage.ToString(),
				Persons = ((IEnumerable<VineRecordPerson>)pagedUserRootObject.data.records).Select((Func<VineRecordPerson, IPerson>)((VineRecordPerson f) => f)).ToList()
			} : null;
		});
	}

	public Task<IListTags> SearchTagAsync(string search, string nextpage)
	{
		Uri uri = new Uri("tags/search/" + search + "?size=50&page=" + nextpage, UriKind.Relative);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			TagsRootObject tagsRootObject = new JsonSerializer().Deserialize<TagsRootObject>(new JsonTextReader(reader));
			return tagsRootObject.success ? new IListTags
			{
				NextPage = tagsRootObject.data.nextPage,
				Tags = ((IEnumerable<VineTag>)tagsRootObject.data.records).Select((Func<VineTag, ITag>)((VineTag f) => f)).ToList()
			} : null;
		});
	}

	public Task<IListPersons> SearchUserAsync(string search, string nextpage)
	{
		Uri uri = new Uri("users/search/" + search + "?page=" + nextpage + "&size=50&rand=" + DateTime.Now.Ticks, UriKind.Relative);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			PagedUserRootObject pagedUserRootObject = new JsonSerializer().Deserialize<PagedUserRootObject>(new JsonTextReader(reader));
			return pagedUserRootObject.success ? new IListPersons
			{
				NextPage = pagedUserRootObject.data.nextPage,
				Persons = ((IEnumerable<VineRecordPerson>)pagedUserRootObject.data.records).Select((Func<VineRecordPerson, IPerson>)((VineRecordPerson f) => f)).ToList()
			} : null;
		});
	}

	public Task<IListPersons> SearchUserMentionAsync(string search)
	{
		Uri uri = new Uri("users/search/" + search + "?st=mention", UriKind.Relative);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			PagedUserRootObject pagedUserRootObject = new JsonSerializer().Deserialize<PagedUserRootObject>(new JsonTextReader(reader));
			return pagedUserRootObject.success ? new IListPersons
			{
				Persons = ((IEnumerable<VineRecordPerson>)pagedUserRootObject.data.records).Select((Func<VineRecordPerson, IPerson>)((VineRecordPerson f) => f)).ToList(),
				NextPage = pagedUserRootObject.data.nextPage.ToString()
			} : null;
		});
	}

	public Task<IListPersons> GetFollowersAsync()
	{
		Uri uri = new Uri("users/" + DatasProvider.Instance.CurrentUser.User.Id + "/following?page=1&size=40", UriKind.Relative);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			PagedUserRootObject pagedUserRootObject = new JsonSerializer().Deserialize<PagedUserRootObject>(new JsonTextReader(reader));
			return pagedUserRootObject.success ? new IListPersons
			{
				Persons = ((IEnumerable<VineRecordPerson>)pagedUserRootObject.data.records).Select((Func<VineRecordPerson, IPerson>)((VineRecordPerson f) => f)).ToList(),
				NextPage = pagedUserRootObject.data.nextPage.ToString()
			} : null;
		});
	}

	public Task<IListComments> GetMoreCommentsAsync(string PostId, string NextPage)
	{
		Uri uri = new Uri("posts/" + PostId + "/comments?page=" + NextPage + "&size=20&anchor=0", UriKind.Relative);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			CommentsRootObject commentsRootObject = new JsonSerializer().Deserialize<CommentsRootObject>(new JsonTextReader(reader));
			return commentsRootObject.success ? new IListComments
			{
				Comments = ((IEnumerable<VineComment>)commentsRootObject.data.records).Select((Func<VineComment, IComment>)((VineComment f) => f)).ToList(),
				NextPage = commentsRootObject.data.nextPage.ToString()
			} : null;
		});
	}

	public async Task<ReconnectInfo> ReconnectAsync()
	{
		Vine.Datas.Datas data = DatasProvider.Instance;
		try
		{
			AuthData newauth = ((data.CurrentUser.TwitterAccess == null) ? (await VineService.Instance.LoginAsync(data.CurrentUser.Email, data.CurrentUser.Password)) : (await VineService.Instance.LoginTwitterAsync(data.CurrentUser.TwitterAccess)));
			data.CurrentUser.Update(newauth);
			data.CurrentUser.LastLogin = DateTime.Now;
			data.Save();
			return new ReconnectInfo
			{
				IsReconnected = true
			};
		}
		catch (ServiceServerErrorException ex)
		{
			bool flag = ex.ErrorType == "101";
			if (flag)
			{
				data.RemoveUser();
				data.Save();
			}
			return new ReconnectInfo
			{
				IsBadUser = flag,
				IsReconnected = false
			};
		}
	}

	public Task<IProfile> GetMyProfilForSettingsAsync()
	{
		return GetProfilInfoAsync(DatasProvider.Instance.CurrentUser.User.userId);
	}

	public Task<IProfile> GetProfilInfoAsync(string userId)
	{
		Vine.Datas.Datas instance = DatasProvider.Instance;
		string uriString = ((instance.CurrentUser == null || instance.CurrentUser.User == null || !(userId == instance.CurrentUser.User.Id)) ? ("users/profiles/" + userId) : ("users/me?rand=" + DateTime.Now.Ticks));
		Uri uri = new Uri(uriString, UriKind.Relative);
		return GetRequest(uri, (ManageHttpResponse<IProfile>)async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			ProfileRootObject profileRootObject = new JsonSerializer().Deserialize<ProfileRootObject>(new JsonTextReader(reader));
			return profileRootObject.success ? profileRootObject.data : null;
		}, (Action)null);
	}

	public async Task<bool> DirectUploadAsync(EncodingJob job, List<IPerson> persons)
	{
		return true;
	}

	public Task<IComment> PostCommentAsync(string id, string comment, List<IPerson> persons)
	{
		string text = "";
		foreach (IPerson person in persons)
		{
			foreach (Match item in new Regex("(^|[^\\w])(?<name>" + Regex.Escape(person.Name) + ")($|[^\\w])", RegexOptions.Compiled).Matches(comment))
			{
				int index = item.Groups["name"].Index;
				string text2 = "{\"type\":\"mention\",\"id\":\"" + person.Id + "\",\"text\":\"" + person.Name.Replace("\\", "\\\\").Replace("\"", "\\\"") + "\",\"range\":[" + index + "," + (index + person.Name.Length) + "]}";
				if (text.Length > 0)
				{
					text += ",";
				}
				text += text2;
			}
		}
		string content = "{\"comment\":" + JsonConvert.SerializeObject(comment).Replace("/", "\\/") + ",\"entities\":[" + text + "]}";
		Uri uri = new Uri("posts/" + id + "/comments", UriKind.Relative);
		return SendRequest(HttpMethod.Post, uri, new StringContent(content, Encoding.UTF8, "application/json"), async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			PostCommentRootObject postCommentRootObject = new JsonSerializer().Deserialize<PostCommentRootObject>(new JsonTextReader(reader));
			if (postCommentRootObject.success)
			{
				AuthData user = DatasProvider.Instance.CurrentUser.User;
				return new VineComment
				{
					Comment = comment,
					UserId = user.Id,
					Username = user.username,
					Avatar = user.Picture,
					createdstring = postCommentRootObject.data.created
				};
			}
			return (IComment)null;
		});
	}

	public Task<bool> RemovePostAsync(string id)
	{
		Uri uri = new Uri("posts/" + id, UriKind.Relative);
		return SendRequest(HttpMethod.Delete, uri, null, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			VineJsonResponse vineJsonResponse = new JsonSerializer().Deserialize<VineJsonResponse>(new JsonTextReader(reader));
			if (vineJsonResponse.success)
			{
				return vineJsonResponse.success;
			}
			throw new Exception();
		});
	}

	public Task<bool> SharePostFacebookAsync(string postId)
	{
		Uri uri = new Uri("posts/" + postId + "/share/facebook", UriKind.Relative);
		return SendRequest(HttpMethod.Post, uri, new StringContent(""), async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			VineJsonResponse vineJsonResponse = new JsonSerializer().Deserialize<VineJsonResponse>(new JsonTextReader(reader));
			if (vineJsonResponse.success)
			{
				return vineJsonResponse.success;
			}
			throw new Exception();
		});
	}

	public Task<bool> AddFacebookToProfileAsync(string facebookId, string facebookToken)
	{
		string content = "facebookId=" + HttpUtility.UrlEncode(facebookId) + "&facebookOauthToken=" + HttpUtility.UrlEncode(facebookToken);
		Uri uri = new Uri("users/" + _User.Id, UriKind.Relative);
		return SendRequest(HttpMethod.Put, uri, new StringContent(content, Encoding.UTF8, "application/x-www-form-urlencoded"), async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			VineJsonResponse vineJsonResponse = new JsonSerializer().Deserialize<VineJsonResponse>(new JsonTextReader(reader));
			if (vineJsonResponse.success)
			{
				return vineJsonResponse.success;
			}
			throw new Exception();
		});
	}

	public Task<string> UploadVideoAsync(EncodingJob job, string uriService, string distantjobid)
	{
		MemoryStream memoryStream = new MemoryStream();
		using (IsolatedStorageFile isolatedStorageFile = IsolatedStorageFile.GetUserStoreForApplication())
		{
			using IsolatedStorageFileStream isolatedStorageFileStream = isolatedStorageFile.OpenFile(job.LocalVideoPath, FileMode.Open);
			isolatedStorageFileStream.CopyTo(memoryStream);
		}
		_ = GetLongGuid() + ".mp4";
		Uri uri = new Uri("https://api-uvr.a1429.lol/upload/videos/" + distantjobid + ".mp4" + (job.IsDirect ? "?private=1" : ""));
		ByteArrayContent byteArrayContent = new ByteArrayContent(memoryStream.ToArray());
		byteArrayContent.Headers.ContentType = new MediaTypeHeaderValue("video/mp4");
		return SendRequest(HttpMethod.Put, uri, byteArrayContent, async delegate(HttpResponseMessage response)
		{
			IEnumerable<string> values = response.Headers.GetValues("x-upload-key");
			return values.Any() ? values.First() : null;
		});
	}

	public string GetLongGuid()
	{
		return "1.3";
	}

	public async Task<string> UploadImageAsync(StorageFile file, string prefix)
	{
		MemoryStream memorystreamVideo = new MemoryStream();
		using (Stream stream = await ((IStorageFile)(object)file).OpenStreamForReadAsync())
		{
			await stream.CopyToAsync(memorystreamVideo);
			memorystreamVideo.Position = 0L;
		}
		Uri uri = new Uri("https://api-uvr.a1429.lol/upload/thumbs/" + prefix + ".mp4.jpg");
		ByteArrayContent byteArrayContent = new ByteArrayContent(memorystreamVideo.ToArray());
		byteArrayContent.Headers.ContentType = new MediaTypeHeaderValue("image/jpeg");
		return await SendRequest(HttpMethod.Put, uri, byteArrayContent, async delegate(HttpResponseMessage response)
		{
			IEnumerable<string> values = response.Headers.GetValues("x-upload-key");
			return values.Any() ? values.First() : null;
		});
	}

	public Task<string> UploadAvatarAsync(Stream image)
	{
		MemoryStream memoryStream = new MemoryStream();
		image.CopyTo(memoryStream);
		string text = "1.3";
		Uri uri = new Uri("https://api-uvr.a1429.lol/upload/avatars/" + text + ".jpg");
		ByteArrayContent byteArrayContent = new ByteArrayContent(memoryStream.ToArray());
		byteArrayContent.Headers.ContentType = new MediaTypeHeaderValue("image/jpeg");
		return SendRequest(HttpMethod.Put, uri, byteArrayContent, async delegate(HttpResponseMessage response)
		{
			IEnumerable<string> values = response.Headers.GetValues("x-upload-key");
			return values.Any() ? values.First() : null;
		});
	}

	protected override void ManageUnauthorized(string tokenUsed)
	{
	}

	public void Init(DataUser dataUser)
	{
		_User = dataUser;
		_httpClient.DefaultRequestHeaders.Add("vine-session-id", dataUser.User.key);
	}

	public Task<DirectData> GetMessageAsync(string boxname)
	{
		Uri uri = new Uri("conversations?" + ((boxname != null) ? ("inbox=" + boxname + "&") : "") + "page=1&size=30", UriKind.Relative);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			DirectConversationsInfo directConversationsInfo = new JsonSerializer
			{
				DateTimeZoneHandling = DateTimeZoneHandling.Utc
			}.Deserialize<DirectConversationsInfo>(new JsonTextReader(reader));
			if (directConversationsInfo.success)
			{
				Vine.Services.Models.User[] users = directConversationsInfo.Data.Users;
				foreach (Vine.Services.Models.User user in users)
				{
					if (string.IsNullOrEmpty(user.Name))
					{
						user.Name = user.PhoneNumber;
					}
				}
				foreach (Conversation record in directConversationsInfo.Data.Records)
				{
					string userid = record.UsersId.FirstOrDefault();
					Vine.Services.Models.User user2 = directConversationsInfo.Data.Users.FirstOrDefault((Vine.Services.Models.User u) => u.Id == userid);
					record.Messages = new ObservableCollection<DirectMessage>(record.Messages.OrderBy((DirectMessage c) => c.Created));
					record.User = user2;
				}
				return directConversationsInfo.Data;
			}
			return (DirectData)null;
		});
	}

	public Task<DirectConversationData> GetConversationAsync(string conversationId, int page)
	{
		Uri uri = new Uri("conversations/" + conversationId + "?prefetch=0&page=" + page, UriKind.Relative);
		return GetRequest(uri, async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			DirectConversationInfo directConversationInfo = new JsonSerializer
			{
				DateTimeZoneHandling = DateTimeZoneHandling.Utc
			}.Deserialize<DirectConversationInfo>(new JsonTextReader(reader));
			if (directConversationInfo.success)
			{
				Vine.Services.Models.User[] users = directConversationInfo.Data.Users;
				foreach (Vine.Services.Models.User user in users)
				{
					if (string.IsNullOrEmpty(user.Name))
					{
						user.Name = user.PhoneNumber;
					}
				}
				foreach (DirectMessage record in directConversationInfo.Data.Records)
				{
					string userid = record.UserId;
					Vine.Services.Models.User user2 = directConversationInfo.Data.Users.FirstOrDefault((Vine.Services.Models.User u) => u.Id == userid);
					record.User = user2;
				}
				directConversationInfo.Data.Records = new ObservableCollection<DirectMessage>(directConversationInfo.Data.Records.OrderBy((DirectMessage c) => c.Created));
				return directConversationInfo.Data;
			}
			return (DirectConversationData)null;
		});
	}

	public Task<DirectMessage> SendMessageAsync(Conversation conversation, string message)
	{
		Vine.Services.Models.User user = conversation.User;
		string content = "{\"to\":[{\"display\":" + JsonConvert.SerializeObject(user.Name).Replace("/", "\\/") + ",\"userId\":" + user.Id + "}],\"message\":" + JsonConvert.SerializeObject(message).Replace("/", "\\/") + ",\"created\":" + DateTime.Now.ToUnixTimeMilli() + ",\"locale\":\"" + CultureInfo.CurrentUICulture.TwoLetterISOLanguageName + "\"}";
		Uri uri = new Uri("conversations/" + conversation.ConversationId, UriKind.Relative);
		return SendRequest(HttpMethod.Post, uri, new StringContent(content, Encoding.UTF8, "application/json"), async delegate(HttpResponseMessage response)
		{
			StreamReader reader = new StreamReader(await response.Content.ReadAsStreamAsync());
			DirectPostMessageResponse directPostMessageResponse = new JsonSerializer
			{
				DateTimeZoneHandling = DateTimeZoneHandling.Utc
			}.Deserialize<DirectPostMessageResponse>(new JsonTextReader(reader));
			return directPostMessageResponse.success ? directPostMessageResponse.Data.Messages[0] : null;
		});
	}
}
