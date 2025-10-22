using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using Vine.Framework;

namespace Vine.Models;

public class VineModel
{
	public string UserId { get; set; }

	public string UserName { get; set; }

	public string AvatarUrl { get; set; }

	public string ProfileBackground { get; set; }

	public string PostId { get; set; }

	public bool Liked { get; set; }

	public bool Private { get; set; }

	public string MyRepostId { get; set; }

	public string ThumbnailUrl { get; set; }

	public string VideoUrl { get; set; }

	public string VideoLowUrl { get; set; }

	public string ShareUrl { get; set; }

	public string Description { get; set; }

	public string PermalinkUrl { get; set; }

	public string VenueName { get; set; }

	public DateTime Created { get; set; }

	public RepostModel Repost { get; set; }

	public VineStatModel Likes { get; set; }

	public VineStatModel Reposts { get; set; }

	public VineStatModel Comments { get; set; }

	public VineLoopModel Loops { get; set; }

	public bool HasSimilarPosts { get; set; }

	[JsonProperty("audio_tracks")]
	public List<AudioTracks> AudioTracks { get; set; }

	public List<Entity> Entities { get; set; }

	public string VineUrl
	{
		get
		{
			if (PhonePerformanceHelper.CurrentPerformanceLevel == PerformanceLevel.Low && VideoLowUrl != null)
			{
				return VideoLowUrl;
			}
			return VideoUrl;
		}
	}

	public string Reference { get; set; }

	public string MosaicType { get; set; }

	public string Title { get; set; }

	public List<VineModel> Records { get; set; }

	public string Link { get; set; }

	public string LinkPath
	{
		get
		{
			if (Link == null || !Uri.TryCreate(Link, UriKind.Absolute, out var result))
			{
				return null;
			}
			return "/" + result.Host + result.PathAndQuery;
		}
	}

	public string Type { get; set; }

	public int Page { get; set; }

	public int Size { get; set; }

	public RecordType RecordType
	{
		get
		{
			if (string.IsNullOrEmpty(Type))
			{
				return RecordType.Post;
			}
			if (Enum.TryParse<RecordType>(Type, ignoreCase: true, out var result))
			{
				return result;
			}
			return RecordType.Unknown;
		}
	}

	public MosaicType ParsedMosaicType
	{
		get
		{
			if (Enum.TryParse<MosaicType>(MosaicType, ignoreCase: true, out var result))
			{
				return result;
			}
			return Vine.Models.MosaicType.Unknown;
		}
	}
}
