using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.IO;
using System.Threading;
using HockeyApp.HockeySDK_WP81_XamlTypeInfo;
using Microsoft.Xaml.Interactivity;
using VideoEdit;
using Vine.Converters;
using Vine.Framework;
using Vine.Models;
using Vine.Models.Analytics;
using Vine.Test;
using Vine.Tiles;
using Vine.Views;
using Vine.Views.Capture;
using Vine.Views.designs;
using Vine.Views.Settings;
using Vine.Views.TemplateSelectors;
using Windows.Foundation;
using Windows.Media.Capture;
using Windows.Media.Editing;
using Windows.Storage;
using Windows.Storage.FileProperties;
using Windows.UI;
using Windows.UI.Text;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Documents;
using Windows.UI.Xaml.Markup;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Media.Imaging;

namespace Vine.Vine_WindowsPhone_XamlTypeInfo;

[GeneratedCode("Microsoft.Windows.UI.Xaml.Build.Tasks", "4.0.0.0")]
[DebuggerNonUserCode]
internal class XamlTypeInfoProvider
{
	private Dictionary<string, IXamlType> _xamlTypeCacheByName = new Dictionary<string, IXamlType>();

	private Dictionary<Type, IXamlType> _xamlTypeCacheByType = new Dictionary<Type, IXamlType>();

	private Dictionary<string, IXamlMember> _xamlMembers = new Dictionary<string, IXamlMember>();

	private string[] _typeNameTable;

	private Type[] _typeTable;

	private List<IXamlMetadataProvider> _otherProviders;

	private List<IXamlMetadataProvider> OtherProviders
	{
		get
		{
			if (_otherProviders == null)
			{
				_otherProviders = new List<IXamlMetadataProvider>();
				IXamlMetadataProvider item = (IXamlMetadataProvider)(object)new XamlMetaDataProvider();
				_otherProviders.Add(item);
			}
			return _otherProviders;
		}
	}

	public IXamlType GetXamlTypeByType(Type type)
	{
		if (_xamlTypeCacheByType.TryGetValue(type, out var value))
		{
			return value;
		}
		int num = LookupTypeIndexByType(type);
		if (num != -1)
		{
			value = CreateXamlType(num);
		}
		XamlUserType xamlUserType = value as XamlUserType;
		if (value == null || (xamlUserType != null && xamlUserType.IsReturnTypeStub && !xamlUserType.IsLocalType))
		{
			IXamlType val = CheckOtherMetadataProvidersForType(type);
			if (val != null && (val.IsConstructible || value == null))
			{
				value = val;
			}
		}
		if (value != null)
		{
			_xamlTypeCacheByName.Add(value.FullName, value);
			_xamlTypeCacheByType.Add(value.UnderlyingType, value);
		}
		return value;
	}

	public IXamlType GetXamlTypeByName(string typeName)
	{
		if (string.IsNullOrEmpty(typeName))
		{
			return null;
		}
		if (_xamlTypeCacheByName.TryGetValue(typeName, out var value))
		{
			return value;
		}
		int num = LookupTypeIndexByName(typeName);
		if (num != -1)
		{
			value = CreateXamlType(num);
		}
		XamlUserType xamlUserType = value as XamlUserType;
		if (value == null || (xamlUserType != null && xamlUserType.IsReturnTypeStub && !xamlUserType.IsLocalType))
		{
			IXamlType val = CheckOtherMetadataProvidersForName(typeName);
			if (val != null && (val.IsConstructible || value == null))
			{
				value = val;
			}
		}
		if (value != null)
		{
			_xamlTypeCacheByName.Add(value.FullName, value);
			_xamlTypeCacheByType.Add(value.UnderlyingType, value);
		}
		return value;
	}

	public IXamlMember GetMemberByLongName(string longMemberName)
	{
		if (string.IsNullOrEmpty(longMemberName))
		{
			return null;
		}
		if (_xamlMembers.TryGetValue(longMemberName, out var value))
		{
			return value;
		}
		value = CreateXamlMember(longMemberName);
		if (value != null)
		{
			_xamlMembers.Add(longMemberName, value);
		}
		return value;
	}

	private void InitTypeTables()
	{
		_typeNameTable = new string[243];
		_typeNameTable[0] = "Windows.UI.Color";
		_typeNameTable[1] = "System.ValueType";
		_typeNameTable[2] = "Object";
		_typeNameTable[3] = "Byte";
		_typeNameTable[4] = "Vine.Converters.VisibleIfTrueConverter";
		_typeNameTable[5] = "Boolean";
		_typeNameTable[6] = "Vine.Converters.NoneToVisibilityConverter";
		_typeNameTable[7] = "Windows.UI.Text.FontWeight";
		_typeNameTable[8] = "Vine.Tiles.ChannelTileWide";
		_typeNameTable[9] = "Windows.UI.Xaml.Controls.UserControl";
		_typeNameTable[10] = "Vine.Tiles.GenericSmallTile";
		_typeNameTable[11] = "Vine.Tiles.SpecificTagSmallTile";
		_typeNameTable[12] = "Vine.Tiles.SearchSmallTile";
		_typeNameTable[13] = "Vine.Views.AvatarControl";
		_typeNameTable[14] = "Windows.UI.Xaml.Visibility";
		_typeNameTable[15] = "Vine.Models.VineUserModel";
		_typeNameTable[16] = "Vine.Models.NotifyObject";
		_typeNameTable[17] = "Vine.Framework.BasePage";
		_typeNameTable[18] = "Vine.Framework.NotifyPage";
		_typeNameTable[19] = "Windows.UI.Xaml.Controls.Page";
		_typeNameTable[20] = "Vine.Framework.NavigationHelper";
		_typeNameTable[21] = "Windows.UI.Xaml.DependencyObject";
		_typeNameTable[22] = "Double";
		_typeNameTable[23] = "Windows.UI.Xaml.GridLength";
		_typeNameTable[24] = "Vine.Views.AvatarCropView";
		_typeNameTable[25] = "Vine.Views.Capture.CaptureView10";
		_typeNameTable[26] = "Vine.Views.Capture.CaptureView10.TutorialState";
		_typeNameTable[27] = "System.Enum";
		_typeNameTable[28] = "String";
		_typeNameTable[29] = "Vine.Views.Capture.CaptureView10.ButtonsTutorialEnum";
		_typeNameTable[30] = "Windows.Foundation.Rect";
		_typeNameTable[31] = "Windows.UI.Xaml.Thickness";
		_typeNameTable[32] = "Int32";
		_typeNameTable[33] = "Windows.UI.Xaml.Media.ImageSource";
		_typeNameTable[34] = "Windows.UI.Xaml.Media.Brush";
		_typeNameTable[35] = "Windows.Media.Capture.MediaCapture";
		_typeNameTable[36] = "Vine.Models.ReplyVmParameters";
		_typeNameTable[37] = "Vine.Views.Capture.ChannelSelectView";
		_typeNameTable[38] = "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.ChannelModel>";
		_typeNameTable[39] = "System.Collections.ObjectModel.Collection`1<Vine.Models.ChannelModel>";
		_typeNameTable[40] = "Vine.Models.ChannelModel";
		_typeNameTable[41] = "Vine.Views.Capture.DraftsView";
		_typeNameTable[42] = "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.RecordingVineModel>";
		_typeNameTable[43] = "System.Collections.ObjectModel.Collection`1<Vine.Models.RecordingVineModel>";
		_typeNameTable[44] = "Vine.Models.RecordingVineModel";
		_typeNameTable[45] = "System.Collections.Generic.List`1<Vine.Models.RecordingClipModel>";
		_typeNameTable[46] = "Vine.Models.RecordingClipModel";
		_typeNameTable[47] = "Int64";
		_typeNameTable[48] = "Vine.Views.Capture.EditClipsView";
		_typeNameTable[49] = "System.Collections.ObjectModel.ObservableCollection`1<Vine.Views.Capture.EditClipsViewModel>";
		_typeNameTable[50] = "System.Collections.ObjectModel.Collection`1<Vine.Views.Capture.EditClipsViewModel>";
		_typeNameTable[51] = "Vine.Views.Capture.EditClipsViewModel";
		_typeNameTable[52] = "Windows.Media.Editing.MediaComposition";
		_typeNameTable[53] = "Windows.Media.Editing.MediaClip";
		_typeNameTable[54] = "System.Collections.ObjectModel.ObservableCollection`1<Windows.UI.Xaml.Media.Imaging.BitmapImage>";
		_typeNameTable[55] = "System.Collections.ObjectModel.Collection`1<Windows.UI.Xaml.Media.Imaging.BitmapImage>";
		_typeNameTable[56] = "Windows.UI.Xaml.Media.Imaging.BitmapImage";
		_typeNameTable[57] = "Vine.Views.Capture.ImportListItemView";
		_typeNameTable[58] = "Vine.Views.Capture.ImportView";
		_typeNameTable[59] = "Vine.Framework.RandomAccessLoadingCollection`1<Vine.Views.Capture.ImportViewModel>";
		_typeNameTable[60] = "Vine.Views.Capture.PreviewCaptureView";
		_typeNameTable[61] = "Vine.Views.Capture.PreviewCaptureParams";
		_typeNameTable[62] = "Windows.Storage.StorageFile";
		_typeNameTable[63] = "Vine.Views.TemplateSelectors.TaggingTemplateSelector";
		_typeNameTable[64] = "Windows.UI.Xaml.Controls.DataTemplateSelector";
		_typeNameTable[65] = "Windows.UI.Xaml.DataTemplate";
		_typeNameTable[66] = "Microsoft.Xaml.Interactivity.Interaction";
		_typeNameTable[67] = "Microsoft.Xaml.Interactivity.BehaviorCollection";
		_typeNameTable[68] = "Windows.UI.Xaml.DependencyObjectCollection";
		_typeNameTable[69] = "Vine.Framework.TextBoxUpdateBehavior";
		_typeNameTable[70] = "Vine.Framework.Behavior`1<Windows.UI.Xaml.Controls.TextBox>";
		_typeNameTable[71] = "Windows.UI.Xaml.Controls.TextBox";
		_typeNameTable[72] = "Vine.Views.Capture.ShareCaptureView";
		_typeNameTable[73] = "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.Entity>";
		_typeNameTable[74] = "System.Collections.ObjectModel.Collection`1<Vine.Models.Entity>";
		_typeNameTable[75] = "Vine.Models.Entity";
		_typeNameTable[76] = "Vine.Models.EntityType";
		_typeNameTable[77] = "Int32[]";
		_typeNameTable[78] = "System.Array";
		_typeNameTable[79] = "Vine.Views.MusicInformationControl";
		_typeNameTable[80] = "Vine.Framework.BaseUserControl";
		_typeNameTable[81] = "Vine.Views.VineListControl";
		_typeNameTable[82] = "Vine.Models.Analytics.Section";
		_typeNameTable[83] = "Vine.Views.ProfileControl";
		_typeNameTable[84] = "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.VineViewModel>";
		_typeNameTable[85] = "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.VineViewModel>";
		_typeNameTable[86] = "System.Collections.ObjectModel.Collection`1<Vine.Models.VineViewModel>";
		_typeNameTable[87] = "Vine.Models.VineViewModel";
		_typeNameTable[88] = "System.Uri";
		_typeNameTable[89] = "Vine.Views.VineToggleButtonState";
		_typeNameTable[90] = "Windows.UI.Xaml.FrameworkElement";
		_typeNameTable[91] = "Vine.Models.VineModel";
		_typeNameTable[92] = "System.DateTime";
		_typeNameTable[93] = "System.Collections.Generic.IEnumerable`1<Vine.Models.MosaicImageViewModel>";
		_typeNameTable[94] = "Vine.Views.VineListControl.Tab";
		_typeNameTable[95] = "Vine.Models.VineListViewParams";
		_typeNameTable[96] = "System.Collections.Generic.List`1<Vine.Models.VineModel>";
		_typeNameTable[97] = "Vine.Models.RepostModel";
		_typeNameTable[98] = "Vine.Models.VineStatModel";
		_typeNameTable[99] = "Vine.Models.VineLoopModel";
		_typeNameTable[100] = "System.Collections.Generic.List`1<Vine.Models.AudioTracks>";
		_typeNameTable[101] = "Vine.Models.AudioTracks";
		_typeNameTable[102] = "Vine.Models.Track";
		_typeNameTable[103] = "System.Collections.Generic.List`1<Vine.Models.Entity>";
		_typeNameTable[104] = "Vine.Models.RecordType";
		_typeNameTable[105] = "Vine.Models.MosaicType";
		_typeNameTable[106] = "Windows.UI.Xaml.Controls.PanelScrollingDirection";
		_typeNameTable[107] = "Vine.Views.ChannelVineListView";
		_typeNameTable[108] = "Windows.UI.Xaml.Controls.IconElement";
		_typeNameTable[109] = "Vine.Views.CommentsView";
		_typeNameTable[110] = "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.CommentModel>";
		_typeNameTable[111] = "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.CommentModel>";
		_typeNameTable[112] = "System.Collections.ObjectModel.Collection`1<Vine.Models.CommentModel>";
		_typeNameTable[113] = "Vine.Models.CommentModel";
		_typeNameTable[114] = "Vine.Views.Capture.CaptureView8";
		_typeNameTable[115] = "Vine.Views.Capture.CaptureView8.TutorialState";
		_typeNameTable[116] = "Vine.Views.Capture.CaptureView8.ButtonsTutorialEnum";
		_typeNameTable[117] = "Vine.Views.PullToRefreshListControl";
		_typeNameTable[118] = "Windows.UI.Xaml.Controls.ListView";
		_typeNameTable[119] = "Windows.UI.Xaml.Controls.ScrollViewer";
		_typeNameTable[120] = "Vine.Views.ConversationList";
		_typeNameTable[121] = "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.ConversationViewModel>";
		_typeNameTable[122] = "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.ConversationViewModel>";
		_typeNameTable[123] = "System.Collections.ObjectModel.Collection`1<Vine.Models.ConversationViewModel>";
		_typeNameTable[124] = "Vine.Models.ConversationViewModel";
		_typeNameTable[125] = "Vine.Models.BaseConversationModel";
		_typeNameTable[126] = "System.Collections.Generic.List`1<String>";
		_typeNameTable[127] = "Windows.UI.Xaml.Media.SolidColorBrush";
		_typeNameTable[128] = "Vine.Views.designs.InteractionsTemplate";
		_typeNameTable[129] = "Vine.Views.designs.MilestoneNotificationDesign";
		_typeNameTable[130] = "Vine.Views.designs.TimelineTemplate1";
		_typeNameTable[131] = "Vine.Views.designs.TimelineTemplate2";
		_typeNameTable[132] = "Vine.Views.CaptchaView";
		_typeNameTable[133] = "Vine.Views.designs.VMTemplate1";
		_typeNameTable[134] = "Vine.Views.designs.VMTemplate2";
		_typeNameTable[135] = "Vine.Views.TemplateSelectors.FriendFinderTemplateSelector";
		_typeNameTable[136] = "Vine.Views.VineToggleButton";
		_typeNameTable[137] = "Windows.UI.Xaml.Controls.Button";
		_typeNameTable[138] = "Windows.UI.Xaml.Controls.ContentControl";
		_typeNameTable[139] = "Vine.Views.FriendFinderAllView";
		_typeNameTable[140] = "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.FriendFinderModel>";
		_typeNameTable[141] = "System.Collections.ObjectModel.Collection`1<Vine.Models.FriendFinderModel>";
		_typeNameTable[142] = "Vine.Models.FriendFinderModel";
		_typeNameTable[143] = "Vine.Models.FriendFinderListSource";
		_typeNameTable[144] = "Vine.Views.FriendFinderView";
		_typeNameTable[145] = "Vine.Views.TemplateSelectors.SearchResultTemplateSelector";
		_typeNameTable[146] = "Vine.Views.SearchControl";
		_typeNameTable[147] = "Vine.Models.UserControlWrapper";
		_typeNameTable[148] = "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.SearchResultModel>";
		_typeNameTable[149] = "System.Collections.ObjectModel.Collection`1<Vine.Models.SearchResultModel>";
		_typeNameTable[150] = "Vine.Models.SearchResultModel";
		_typeNameTable[151] = "Vine.Models.VineSearchSuggestions";
		_typeNameTable[152] = "Vine.Models.VineTagModel";
		_typeNameTable[153] = "Vine.Models.VineRecentSearch";
		_typeNameTable[154] = "Vine.Models.SearchType";
		_typeNameTable[155] = "System.Threading.CancellationTokenSource";
		_typeNameTable[156] = "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.VineRecentSearch>";
		_typeNameTable[157] = "System.Collections.ObjectModel.Collection`1<Vine.Models.VineRecentSearch>";
		_typeNameTable[158] = "Vine.Views.SearchTagsAllView";
		_typeNameTable[159] = "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.VineTagModel>";
		_typeNameTable[160] = "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.VineTagModel>";
		_typeNameTable[161] = "System.Collections.ObjectModel.Collection`1<Vine.Models.VineTagModel>";
		_typeNameTable[162] = "Vine.Views.SettingsPrivacyView";
		_typeNameTable[163] = "Vine.Views.TemplateSelectors.ContactTemplateSelector";
		_typeNameTable[164] = "Vine.Views.ShareMessageControl";
		_typeNameTable[165] = "System.Collections.Generic.List`1<Vine.Models.VineContactViewModel>";
		_typeNameTable[166] = "Vine.Models.VineContactViewModel";
		_typeNameTable[167] = "System.Collections.Generic.List`1<System.Tuple`2<String, String>>";
		_typeNameTable[168] = "System.Tuple`2<String, String>";
		_typeNameTable[169] = "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.VineContactViewModel>";
		_typeNameTable[170] = "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.VineContactViewModel>";
		_typeNameTable[171] = "System.Collections.ObjectModel.Collection`1<Vine.Models.VineContactViewModel>";
		_typeNameTable[172] = "Vine.Views.ShareMessageView";
		_typeNameTable[173] = "Vine.Views.TappedToLikeControl";
		_typeNameTable[174] = "Vine.Views.UpgradeView";
		_typeNameTable[175] = "Vine.Views.VerifyPhoneCodeEnterView";
		_typeNameTable[176] = "Vine.Views.VerifyEmailCodeEnterView";
		_typeNameTable[177] = "Vine.Views.ExploreControl";
		_typeNameTable[178] = "Vine.Views.TOSControl";
		_typeNameTable[179] = "Vine.Framework.PasswordBoxUpdateBehavior";
		_typeNameTable[180] = "Vine.Framework.Behavior`1<Windows.UI.Xaml.Controls.PasswordBox>";
		_typeNameTable[181] = "Windows.UI.Xaml.Controls.PasswordBox";
		_typeNameTable[182] = "Vine.Views.SignUpEmailDetailsView";
		_typeNameTable[183] = "Vine.Views.SignUpEmailView";
		_typeNameTable[184] = "Vine.Views.VineMessagesInbox";
		_typeNameTable[185] = "System.Collections.Generic.List`1<Vine.Models.VineUserModel>";
		_typeNameTable[186] = "Vine.Framework.RelayCommand";
		_typeNameTable[187] = "Vine.Models.VineUserType";
		_typeNameTable[188] = "System.Collections.Generic.List`1<Windows.UI.Xaml.Documents.Run>";
		_typeNameTable[189] = "Windows.UI.Xaml.Documents.Run";
		_typeNameTable[190] = "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.VineUserModel>";
		_typeNameTable[191] = "System.Collections.ObjectModel.Collection`1<Vine.Models.VineUserModel>";
		_typeNameTable[192] = "Vine.Views.InteractionsControl";
		_typeNameTable[193] = "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.InteractionModel>";
		_typeNameTable[194] = "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.InteractionModel>";
		_typeNameTable[195] = "System.Collections.ObjectModel.Collection`1<Vine.Models.InteractionModel>";
		_typeNameTable[196] = "Vine.Models.InteractionModel";
		_typeNameTable[197] = "Vine.Models.InteractionType";
		_typeNameTable[198] = "Vine.Models.Milestone";
		_typeNameTable[199] = "Vine.Views.HomeView";
		_typeNameTable[200] = "Vine.Models.UploadJobsViewModel";
		_typeNameTable[201] = "Vine.Views.TemplateSelectors.InteractionTemplateSelector";
		_typeNameTable[202] = "Vine.Views.LoginEmailView";
		_typeNameTable[203] = "Vine.Views.LoginTwitterView";
		_typeNameTable[204] = "Vine.Framework.ExtensionsForUi";
		_typeNameTable[205] = "System.IO.Stream";
		_typeNameTable[206] = "Vine.Views.ProfileView";
		_typeNameTable[207] = "Vine.Views.FacebookView";
		_typeNameTable[208] = "System.Collections.Generic.Dictionary`2<String, String>";
		_typeNameTable[209] = "System.UriHostNameType";
		_typeNameTable[210] = "String[]";
		_typeNameTable[211] = "Vine.Views.SettingsContentView";
		_typeNameTable[212] = "Vine.Views.SettingsView";
		_typeNameTable[213] = "Vine.Views.Settings.AttributionView";
		_typeNameTable[214] = "Vine.Views.SingleVineView";
		_typeNameTable[215] = "Vine.Models.SingleVineViewParams";
		_typeNameTable[216] = "Vine.Views.TagVineListView";
		_typeNameTable[217] = "Vine.Views.UploadJobsView";
		_typeNameTable[218] = "Vine.Views.VerifyPhoneEditControl";
		_typeNameTable[219] = "Vine.Views.VerifyPhonePopupControl";
		_typeNameTable[220] = "Vine.Views.VerifyEmailPopupControl";
		_typeNameTable[221] = "Vine.Views.TemplateSelectors.MessageTemplateSelector";
		_typeNameTable[222] = "Vine.Views.VineMessagesThreadView";
		_typeNameTable[223] = "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.VineMessageViewModel>";
		_typeNameTable[224] = "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.VineMessageViewModel>";
		_typeNameTable[225] = "System.Collections.ObjectModel.Collection`1<Vine.Models.VineMessageViewModel>";
		_typeNameTable[226] = "Vine.Models.VineMessageViewModel";
		_typeNameTable[227] = "Vine.Models.VineMessageModel";
		_typeNameTable[228] = "Vine.Views.TemplateSelectors.VineListTemplateSelector";
		_typeNameTable[229] = "Vine.Views.VinePressedButton";
		_typeNameTable[230] = "Vine.Views.ResetPasswordView";
		_typeNameTable[231] = "Vine.Views.VineUserListView";
		_typeNameTable[232] = "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.VineUserModel>";
		_typeNameTable[233] = "Vine.Models.VineUserListViewParams";
		_typeNameTable[234] = "Vine.Views.WebView";
		_typeNameTable[235] = "Vine.Views.WelcomeView";
		_typeNameTable[236] = "Vine.Test.InstanceCountPopupControl";
		_typeNameTable[237] = "VideoEdit.MediaFile";
		_typeNameTable[238] = "UInt32";
		_typeNameTable[239] = "VideoEdit.StreamInfo";
		_typeNameTable[240] = "VideoEdit.AudioProp";
		_typeNameTable[241] = "VideoEdit.VideoProp";
		_typeNameTable[242] = "Windows.Storage.FileProperties.StorageItemThumbnail";
		_typeTable = new Type[243];
		_typeTable[0] = typeof(Color);
		_typeTable[1] = typeof(ValueType);
		_typeTable[2] = typeof(object);
		_typeTable[3] = typeof(byte);
		_typeTable[4] = typeof(VisibleIfTrueConverter);
		_typeTable[5] = typeof(bool);
		_typeTable[6] = typeof(NoneToVisibilityConverter);
		_typeTable[7] = typeof(FontWeight);
		_typeTable[8] = typeof(ChannelTileWide);
		_typeTable[9] = typeof(UserControl);
		_typeTable[10] = typeof(GenericSmallTile);
		_typeTable[11] = typeof(SpecificTagSmallTile);
		_typeTable[12] = typeof(SearchSmallTile);
		_typeTable[13] = typeof(AvatarControl);
		_typeTable[14] = typeof(Visibility);
		_typeTable[15] = typeof(VineUserModel);
		_typeTable[16] = typeof(NotifyObject);
		_typeTable[17] = typeof(BasePage);
		_typeTable[18] = typeof(NotifyPage);
		_typeTable[19] = typeof(Page);
		_typeTable[20] = typeof(NavigationHelper);
		_typeTable[21] = typeof(DependencyObject);
		_typeTable[22] = typeof(double);
		_typeTable[23] = typeof(GridLength);
		_typeTable[24] = typeof(AvatarCropView);
		_typeTable[25] = typeof(CaptureView10);
		_typeTable[26] = typeof(CaptureView10.TutorialState);
		_typeTable[27] = typeof(Enum);
		_typeTable[28] = typeof(string);
		_typeTable[29] = typeof(CaptureView10.ButtonsTutorialEnum);
		_typeTable[30] = typeof(Rect);
		_typeTable[31] = typeof(Thickness);
		_typeTable[32] = typeof(int);
		_typeTable[33] = typeof(ImageSource);
		_typeTable[34] = typeof(Brush);
		_typeTable[35] = typeof(MediaCapture);
		_typeTable[36] = typeof(ReplyVmParameters);
		_typeTable[37] = typeof(ChannelSelectView);
		_typeTable[38] = typeof(ObservableCollection<ChannelModel>);
		_typeTable[39] = typeof(Collection<ChannelModel>);
		_typeTable[40] = typeof(ChannelModel);
		_typeTable[41] = typeof(DraftsView);
		_typeTable[42] = typeof(ObservableCollection<RecordingVineModel>);
		_typeTable[43] = typeof(Collection<RecordingVineModel>);
		_typeTable[44] = typeof(RecordingVineModel);
		_typeTable[45] = typeof(List<RecordingClipModel>);
		_typeTable[46] = typeof(RecordingClipModel);
		_typeTable[47] = typeof(long);
		_typeTable[48] = typeof(EditClipsView);
		_typeTable[49] = typeof(ObservableCollection<EditClipsViewModel>);
		_typeTable[50] = typeof(Collection<EditClipsViewModel>);
		_typeTable[51] = typeof(EditClipsViewModel);
		_typeTable[52] = typeof(MediaComposition);
		_typeTable[53] = typeof(MediaClip);
		_typeTable[54] = typeof(ObservableCollection<BitmapImage>);
		_typeTable[55] = typeof(Collection<BitmapImage>);
		_typeTable[56] = typeof(BitmapImage);
		_typeTable[57] = typeof(ImportListItemView);
		_typeTable[58] = typeof(ImportView);
		_typeTable[59] = typeof(RandomAccessLoadingCollection<ImportViewModel>);
		_typeTable[60] = typeof(PreviewCaptureView);
		_typeTable[61] = typeof(PreviewCaptureParams);
		_typeTable[62] = typeof(StorageFile);
		_typeTable[63] = typeof(TaggingTemplateSelector);
		_typeTable[64] = typeof(DataTemplateSelector);
		_typeTable[65] = typeof(DataTemplate);
		_typeTable[66] = typeof(Interaction);
		_typeTable[67] = typeof(BehaviorCollection);
		_typeTable[68] = typeof(DependencyObjectCollection);
		_typeTable[69] = typeof(TextBoxUpdateBehavior);
		_typeTable[70] = typeof(Behavior<TextBox>);
		_typeTable[71] = typeof(TextBox);
		_typeTable[72] = typeof(ShareCaptureView);
		_typeTable[73] = typeof(ObservableCollection<Entity>);
		_typeTable[74] = typeof(Collection<Entity>);
		_typeTable[75] = typeof(Entity);
		_typeTable[76] = typeof(EntityType);
		_typeTable[77] = typeof(int[]);
		_typeTable[78] = typeof(Array);
		_typeTable[79] = typeof(MusicInformationControl);
		_typeTable[80] = typeof(BaseUserControl);
		_typeTable[81] = typeof(VineListControl);
		_typeTable[82] = typeof(Section);
		_typeTable[83] = typeof(ProfileControl);
		_typeTable[84] = typeof(IncrementalLoadingCollection<VineViewModel>);
		_typeTable[85] = typeof(ObservableCollection<VineViewModel>);
		_typeTable[86] = typeof(Collection<VineViewModel>);
		_typeTable[87] = typeof(VineViewModel);
		_typeTable[88] = typeof(Uri);
		_typeTable[89] = typeof(VineToggleButtonState);
		_typeTable[90] = typeof(FrameworkElement);
		_typeTable[91] = typeof(VineModel);
		_typeTable[92] = typeof(DateTime);
		_typeTable[93] = typeof(IEnumerable<MosaicImageViewModel>);
		_typeTable[94] = typeof(VineListControl.Tab);
		_typeTable[95] = typeof(VineListViewParams);
		_typeTable[96] = typeof(List<VineModel>);
		_typeTable[97] = typeof(RepostModel);
		_typeTable[98] = typeof(VineStatModel);
		_typeTable[99] = typeof(VineLoopModel);
		_typeTable[100] = typeof(List<AudioTracks>);
		_typeTable[101] = typeof(AudioTracks);
		_typeTable[102] = typeof(Track);
		_typeTable[103] = typeof(List<Entity>);
		_typeTable[104] = typeof(RecordType);
		_typeTable[105] = typeof(MosaicType);
		_typeTable[106] = typeof(PanelScrollingDirection);
		_typeTable[107] = typeof(ChannelVineListView);
		_typeTable[108] = typeof(IconElement);
		_typeTable[109] = typeof(CommentsView);
		_typeTable[110] = typeof(IncrementalLoadingCollection<CommentModel>);
		_typeTable[111] = typeof(ObservableCollection<CommentModel>);
		_typeTable[112] = typeof(Collection<CommentModel>);
		_typeTable[113] = typeof(CommentModel);
		_typeTable[114] = typeof(CaptureView8);
		_typeTable[115] = typeof(CaptureView8.TutorialState);
		_typeTable[116] = typeof(CaptureView8.ButtonsTutorialEnum);
		_typeTable[117] = typeof(PullToRefreshListControl);
		_typeTable[118] = typeof(ListView);
		_typeTable[119] = typeof(ScrollViewer);
		_typeTable[120] = typeof(ConversationList);
		_typeTable[121] = typeof(IncrementalLoadingCollection<ConversationViewModel>);
		_typeTable[122] = typeof(ObservableCollection<ConversationViewModel>);
		_typeTable[123] = typeof(Collection<ConversationViewModel>);
		_typeTable[124] = typeof(ConversationViewModel);
		_typeTable[125] = typeof(BaseConversationModel);
		_typeTable[126] = typeof(List<string>);
		_typeTable[127] = typeof(SolidColorBrush);
		_typeTable[128] = typeof(InteractionsTemplate);
		_typeTable[129] = typeof(MilestoneNotificationDesign);
		_typeTable[130] = typeof(TimelineTemplate1);
		_typeTable[131] = typeof(TimelineTemplate2);
		_typeTable[132] = typeof(CaptchaView);
		_typeTable[133] = typeof(VMTemplate1);
		_typeTable[134] = typeof(VMTemplate2);
		_typeTable[135] = typeof(FriendFinderTemplateSelector);
		_typeTable[136] = typeof(VineToggleButton);
		_typeTable[137] = typeof(Button);
		_typeTable[138] = typeof(ContentControl);
		_typeTable[139] = typeof(FriendFinderAllView);
		_typeTable[140] = typeof(ObservableCollection<FriendFinderModel>);
		_typeTable[141] = typeof(Collection<FriendFinderModel>);
		_typeTable[142] = typeof(FriendFinderModel);
		_typeTable[143] = typeof(FriendFinderListSource);
		_typeTable[144] = typeof(FriendFinderView);
		_typeTable[145] = typeof(SearchResultTemplateSelector);
		_typeTable[146] = typeof(SearchControl);
		_typeTable[147] = typeof(UserControlWrapper);
		_typeTable[148] = typeof(ObservableCollection<SearchResultModel>);
		_typeTable[149] = typeof(Collection<SearchResultModel>);
		_typeTable[150] = typeof(SearchResultModel);
		_typeTable[151] = typeof(VineSearchSuggestions);
		_typeTable[152] = typeof(VineTagModel);
		_typeTable[153] = typeof(VineRecentSearch);
		_typeTable[154] = typeof(SearchType);
		_typeTable[155] = typeof(CancellationTokenSource);
		_typeTable[156] = typeof(ObservableCollection<VineRecentSearch>);
		_typeTable[157] = typeof(Collection<VineRecentSearch>);
		_typeTable[158] = typeof(SearchTagsAllView);
		_typeTable[159] = typeof(IncrementalLoadingCollection<VineTagModel>);
		_typeTable[160] = typeof(ObservableCollection<VineTagModel>);
		_typeTable[161] = typeof(Collection<VineTagModel>);
		_typeTable[162] = typeof(SettingsPrivacyView);
		_typeTable[163] = typeof(ContactTemplateSelector);
		_typeTable[164] = typeof(ShareMessageControl);
		_typeTable[165] = typeof(List<VineContactViewModel>);
		_typeTable[166] = typeof(VineContactViewModel);
		_typeTable[167] = typeof(List<Tuple<string, string>>);
		_typeTable[168] = typeof(Tuple<string, string>);
		_typeTable[169] = typeof(IncrementalLoadingCollection<VineContactViewModel>);
		_typeTable[170] = typeof(ObservableCollection<VineContactViewModel>);
		_typeTable[171] = typeof(Collection<VineContactViewModel>);
		_typeTable[172] = typeof(ShareMessageView);
		_typeTable[173] = typeof(TappedToLikeControl);
		_typeTable[174] = typeof(UpgradeView);
		_typeTable[175] = typeof(VerifyPhoneCodeEnterView);
		_typeTable[176] = typeof(VerifyEmailCodeEnterView);
		_typeTable[177] = typeof(ExploreControl);
		_typeTable[178] = typeof(TOSControl);
		_typeTable[179] = typeof(PasswordBoxUpdateBehavior);
		_typeTable[180] = typeof(Behavior<PasswordBox>);
		_typeTable[181] = typeof(PasswordBox);
		_typeTable[182] = typeof(SignUpEmailDetailsView);
		_typeTable[183] = typeof(SignUpEmailView);
		_typeTable[184] = typeof(VineMessagesInbox);
		_typeTable[185] = typeof(List<VineUserModel>);
		_typeTable[186] = typeof(RelayCommand);
		_typeTable[187] = typeof(VineUserType);
		_typeTable[188] = typeof(List<Run>);
		_typeTable[189] = typeof(Run);
		_typeTable[190] = typeof(ObservableCollection<VineUserModel>);
		_typeTable[191] = typeof(Collection<VineUserModel>);
		_typeTable[192] = typeof(InteractionsControl);
		_typeTable[193] = typeof(IncrementalLoadingCollection<InteractionModel>);
		_typeTable[194] = typeof(ObservableCollection<InteractionModel>);
		_typeTable[195] = typeof(Collection<InteractionModel>);
		_typeTable[196] = typeof(InteractionModel);
		_typeTable[197] = typeof(InteractionType);
		_typeTable[198] = typeof(Milestone);
		_typeTable[199] = typeof(HomeView);
		_typeTable[200] = typeof(UploadJobsViewModel);
		_typeTable[201] = typeof(InteractionTemplateSelector);
		_typeTable[202] = typeof(LoginEmailView);
		_typeTable[203] = typeof(LoginTwitterView);
		_typeTable[204] = typeof(ExtensionsForUi);
		_typeTable[205] = typeof(Stream);
		_typeTable[206] = typeof(ProfileView);
		_typeTable[207] = typeof(FacebookView);
		_typeTable[208] = typeof(Dictionary<string, string>);
		_typeTable[209] = typeof(UriHostNameType);
		_typeTable[210] = typeof(string[]);
		_typeTable[211] = typeof(SettingsContentView);
		_typeTable[212] = typeof(SettingsView);
		_typeTable[213] = typeof(AttributionView);
		_typeTable[214] = typeof(SingleVineView);
		_typeTable[215] = typeof(SingleVineViewParams);
		_typeTable[216] = typeof(TagVineListView);
		_typeTable[217] = typeof(UploadJobsView);
		_typeTable[218] = typeof(VerifyPhoneEditControl);
		_typeTable[219] = typeof(VerifyPhonePopupControl);
		_typeTable[220] = typeof(VerifyEmailPopupControl);
		_typeTable[221] = typeof(MessageTemplateSelector);
		_typeTable[222] = typeof(VineMessagesThreadView);
		_typeTable[223] = typeof(IncrementalLoadingCollection<VineMessageViewModel>);
		_typeTable[224] = typeof(ObservableCollection<VineMessageViewModel>);
		_typeTable[225] = typeof(Collection<VineMessageViewModel>);
		_typeTable[226] = typeof(VineMessageViewModel);
		_typeTable[227] = typeof(VineMessageModel);
		_typeTable[228] = typeof(VineListTemplateSelector);
		_typeTable[229] = typeof(VinePressedButton);
		_typeTable[230] = typeof(ResetPasswordView);
		_typeTable[231] = typeof(VineUserListView);
		_typeTable[232] = typeof(IncrementalLoadingCollection<VineUserModel>);
		_typeTable[233] = typeof(VineUserListViewParams);
		_typeTable[234] = typeof(WebView);
		_typeTable[235] = typeof(WelcomeView);
		_typeTable[236] = typeof(InstanceCountPopupControl);
		_typeTable[237] = typeof(MediaFile);
		_typeTable[238] = typeof(uint);
		_typeTable[239] = typeof(StreamInfo);
		_typeTable[240] = typeof(AudioProp);
		_typeTable[241] = typeof(VideoProp);
		_typeTable[242] = typeof(StorageItemThumbnail);
	}

	private int LookupTypeIndexByName(string typeName)
	{
		if (_typeNameTable == null)
		{
			InitTypeTables();
		}
		for (int i = 0; i < _typeNameTable.Length; i++)
		{
			if (string.CompareOrdinal(_typeNameTable[i], typeName) == 0)
			{
				return i;
			}
		}
		return -1;
	}

	private int LookupTypeIndexByType(Type type)
	{
		if (_typeTable == null)
		{
			InitTypeTables();
		}
		for (int i = 0; i < _typeTable.Length; i++)
		{
			if ((object)type == _typeTable[i])
			{
				return i;
			}
		}
		return -1;
	}

	private object Activate_4_VisibleIfTrueConverter()
	{
		return new VisibleIfTrueConverter();
	}

	private object Activate_6_NoneToVisibilityConverter()
	{
		return new NoneToVisibilityConverter();
	}

	private object Activate_8_ChannelTileWide()
	{
		return new ChannelTileWide();
	}

	private object Activate_10_GenericSmallTile()
	{
		return new GenericSmallTile();
	}

	private object Activate_11_SpecificTagSmallTile()
	{
		return new SpecificTagSmallTile();
	}

	private object Activate_12_SearchSmallTile()
	{
		return new SearchSmallTile();
	}

	private object Activate_13_AvatarControl()
	{
		return new AvatarControl();
	}

	private object Activate_15_VineUserModel()
	{
		return new VineUserModel();
	}

	private object Activate_16_NotifyObject()
	{
		return new NotifyObject();
	}

	private object Activate_17_BasePage()
	{
		return new BasePage();
	}

	private object Activate_18_NotifyPage()
	{
		return new NotifyPage();
	}

	private object Activate_24_AvatarCropView()
	{
		return new AvatarCropView();
	}

	private object Activate_25_CaptureView10()
	{
		return new CaptureView10();
	}

	private object Activate_35_MediaCapture()
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Expected O, but got Unknown
		return (object)new MediaCapture();
	}

	private object Activate_36_ReplyVmParameters()
	{
		return new ReplyVmParameters();
	}

	private object Activate_37_ChannelSelectView()
	{
		return new ChannelSelectView();
	}

	private object Activate_38_ObservableCollection()
	{
		return new ObservableCollection<ChannelModel>();
	}

	private object Activate_39_Collection()
	{
		return new Collection<ChannelModel>();
	}

	private object Activate_40_ChannelModel()
	{
		return new ChannelModel();
	}

	private object Activate_41_DraftsView()
	{
		return new DraftsView();
	}

	private object Activate_42_ObservableCollection()
	{
		return new ObservableCollection<RecordingVineModel>();
	}

	private object Activate_43_Collection()
	{
		return new Collection<RecordingVineModel>();
	}

	private object Activate_44_RecordingVineModel()
	{
		return new RecordingVineModel();
	}

	private object Activate_45_List()
	{
		return new List<RecordingClipModel>();
	}

	private object Activate_46_RecordingClipModel()
	{
		return new RecordingClipModel();
	}

	private object Activate_48_EditClipsView()
	{
		return new EditClipsView();
	}

	private object Activate_49_ObservableCollection()
	{
		return new ObservableCollection<EditClipsViewModel>();
	}

	private object Activate_50_Collection()
	{
		return new Collection<EditClipsViewModel>();
	}

	private object Activate_51_EditClipsViewModel()
	{
		return new EditClipsViewModel();
	}

	private object Activate_52_MediaComposition()
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Expected O, but got Unknown
		return (object)new MediaComposition();
	}

	private object Activate_54_ObservableCollection()
	{
		return new ObservableCollection<BitmapImage>();
	}

	private object Activate_55_Collection()
	{
		return new Collection<BitmapImage>();
	}

	private object Activate_57_ImportListItemView()
	{
		return new ImportListItemView();
	}

	private object Activate_58_ImportView()
	{
		return new ImportView();
	}

	private object Activate_60_PreviewCaptureView()
	{
		return new PreviewCaptureView();
	}

	private object Activate_61_PreviewCaptureParams()
	{
		return new PreviewCaptureParams();
	}

	private object Activate_63_TaggingTemplateSelector()
	{
		return new TaggingTemplateSelector();
	}

	private object Activate_67_BehaviorCollection()
	{
		return new BehaviorCollection();
	}

	private object Activate_69_TextBoxUpdateBehavior()
	{
		return new TextBoxUpdateBehavior();
	}

	private object Activate_72_ShareCaptureView()
	{
		return new ShareCaptureView();
	}

	private object Activate_73_ObservableCollection()
	{
		return new ObservableCollection<Entity>();
	}

	private object Activate_74_Collection()
	{
		return new Collection<Entity>();
	}

	private object Activate_75_Entity()
	{
		return new Entity();
	}

	private object Activate_79_MusicInformationControl()
	{
		return new MusicInformationControl();
	}

	private object Activate_81_VineListControl()
	{
		return new VineListControl();
	}

	private object Activate_83_ProfileControl()
	{
		return new ProfileControl();
	}

	private object Activate_85_ObservableCollection()
	{
		return new ObservableCollection<VineViewModel>();
	}

	private object Activate_86_Collection()
	{
		return new Collection<VineViewModel>();
	}

	private object Activate_91_VineModel()
	{
		return new VineModel();
	}

	private object Activate_95_VineListViewParams()
	{
		return new VineListViewParams();
	}

	private object Activate_96_List()
	{
		return new List<VineModel>();
	}

	private object Activate_97_RepostModel()
	{
		return new RepostModel();
	}

	private object Activate_98_VineStatModel()
	{
		return new VineStatModel();
	}

	private object Activate_99_VineLoopModel()
	{
		return new VineLoopModel();
	}

	private object Activate_100_List()
	{
		return new List<AudioTracks>();
	}

	private object Activate_101_AudioTracks()
	{
		return new AudioTracks();
	}

	private object Activate_102_Track()
	{
		return new Track();
	}

	private object Activate_103_List()
	{
		return new List<Entity>();
	}

	private object Activate_107_ChannelVineListView()
	{
		return new ChannelVineListView();
	}

	private object Activate_109_CommentsView()
	{
		return new CommentsView();
	}

	private object Activate_111_ObservableCollection()
	{
		return new ObservableCollection<CommentModel>();
	}

	private object Activate_112_Collection()
	{
		return new Collection<CommentModel>();
	}

	private object Activate_113_CommentModel()
	{
		return new CommentModel();
	}

	private object Activate_114_CaptureView8()
	{
		return new CaptureView8();
	}

	private object Activate_117_PullToRefreshListControl()
	{
		return new PullToRefreshListControl();
	}

	private object Activate_120_ConversationList()
	{
		return new ConversationList();
	}

	private object Activate_122_ObservableCollection()
	{
		return new ObservableCollection<ConversationViewModel>();
	}

	private object Activate_123_Collection()
	{
		return new Collection<ConversationViewModel>();
	}

	private object Activate_124_ConversationViewModel()
	{
		return new ConversationViewModel();
	}

	private object Activate_126_List()
	{
		return new List<string>();
	}

	private object Activate_128_InteractionsTemplate()
	{
		return new InteractionsTemplate();
	}

	private object Activate_129_MilestoneNotificationDesign()
	{
		return new MilestoneNotificationDesign();
	}

	private object Activate_130_TimelineTemplate1()
	{
		return new TimelineTemplate1();
	}

	private object Activate_131_TimelineTemplate2()
	{
		return new TimelineTemplate2();
	}

	private object Activate_132_CaptchaView()
	{
		return new CaptchaView();
	}

	private object Activate_133_VMTemplate1()
	{
		return new VMTemplate1();
	}

	private object Activate_134_VMTemplate2()
	{
		return new VMTemplate2();
	}

	private object Activate_135_FriendFinderTemplateSelector()
	{
		return new FriendFinderTemplateSelector();
	}

	private object Activate_136_VineToggleButton()
	{
		return new VineToggleButton();
	}

	private object Activate_139_FriendFinderAllView()
	{
		return new FriendFinderAllView();
	}

	private object Activate_140_ObservableCollection()
	{
		return new ObservableCollection<FriendFinderModel>();
	}

	private object Activate_141_Collection()
	{
		return new Collection<FriendFinderModel>();
	}

	private object Activate_142_FriendFinderModel()
	{
		return new FriendFinderModel();
	}

	private object Activate_144_FriendFinderView()
	{
		return new FriendFinderView();
	}

	private object Activate_145_SearchResultTemplateSelector()
	{
		return new SearchResultTemplateSelector();
	}

	private object Activate_146_SearchControl()
	{
		return new SearchControl();
	}

	private object Activate_147_UserControlWrapper()
	{
		return new UserControlWrapper();
	}

	private object Activate_148_ObservableCollection()
	{
		return new ObservableCollection<SearchResultModel>();
	}

	private object Activate_149_Collection()
	{
		return new Collection<SearchResultModel>();
	}

	private object Activate_150_SearchResultModel()
	{
		return new SearchResultModel();
	}

	private object Activate_151_VineSearchSuggestions()
	{
		return new VineSearchSuggestions();
	}

	private object Activate_152_VineTagModel()
	{
		return new VineTagModel();
	}

	private object Activate_153_VineRecentSearch()
	{
		return new VineRecentSearch();
	}

	private object Activate_155_CancellationTokenSource()
	{
		return new CancellationTokenSource();
	}

	private object Activate_156_ObservableCollection()
	{
		return new ObservableCollection<VineRecentSearch>();
	}

	private object Activate_157_Collection()
	{
		return new Collection<VineRecentSearch>();
	}

	private object Activate_158_SearchTagsAllView()
	{
		return new SearchTagsAllView();
	}

	private object Activate_160_ObservableCollection()
	{
		return new ObservableCollection<VineTagModel>();
	}

	private object Activate_161_Collection()
	{
		return new Collection<VineTagModel>();
	}

	private object Activate_162_SettingsPrivacyView()
	{
		return new SettingsPrivacyView();
	}

	private object Activate_163_ContactTemplateSelector()
	{
		return new ContactTemplateSelector();
	}

	private object Activate_164_ShareMessageControl()
	{
		return new ShareMessageControl();
	}

	private object Activate_165_List()
	{
		return new List<VineContactViewModel>();
	}

	private object Activate_166_VineContactViewModel()
	{
		return new VineContactViewModel();
	}

	private object Activate_167_List()
	{
		return new List<Tuple<string, string>>();
	}

	private object Activate_170_ObservableCollection()
	{
		return new ObservableCollection<VineContactViewModel>();
	}

	private object Activate_171_Collection()
	{
		return new Collection<VineContactViewModel>();
	}

	private object Activate_172_ShareMessageView()
	{
		return new ShareMessageView();
	}

	private object Activate_173_TappedToLikeControl()
	{
		return new TappedToLikeControl();
	}

	private object Activate_174_UpgradeView()
	{
		return new UpgradeView();
	}

	private object Activate_175_VerifyPhoneCodeEnterView()
	{
		return new VerifyPhoneCodeEnterView();
	}

	private object Activate_176_VerifyEmailCodeEnterView()
	{
		return new VerifyEmailCodeEnterView();
	}

	private object Activate_177_ExploreControl()
	{
		return new ExploreControl();
	}

	private object Activate_178_TOSControl()
	{
		return new TOSControl();
	}

	private object Activate_179_PasswordBoxUpdateBehavior()
	{
		return new PasswordBoxUpdateBehavior();
	}

	private object Activate_182_SignUpEmailDetailsView()
	{
		return new SignUpEmailDetailsView();
	}

	private object Activate_183_SignUpEmailView()
	{
		return new SignUpEmailView();
	}

	private object Activate_184_VineMessagesInbox()
	{
		return new VineMessagesInbox();
	}

	private object Activate_185_List()
	{
		return new List<VineUserModel>();
	}

	private object Activate_188_List()
	{
		return new List<Run>();
	}

	private object Activate_190_ObservableCollection()
	{
		return new ObservableCollection<VineUserModel>();
	}

	private object Activate_191_Collection()
	{
		return new Collection<VineUserModel>();
	}

	private object Activate_192_InteractionsControl()
	{
		return new InteractionsControl();
	}

	private object Activate_194_ObservableCollection()
	{
		return new ObservableCollection<InteractionModel>();
	}

	private object Activate_195_Collection()
	{
		return new Collection<InteractionModel>();
	}

	private object Activate_196_InteractionModel()
	{
		return new InteractionModel();
	}

	private object Activate_198_Milestone()
	{
		return new Milestone();
	}

	private object Activate_199_HomeView()
	{
		return new HomeView();
	}

	private object Activate_201_InteractionTemplateSelector()
	{
		return new InteractionTemplateSelector();
	}

	private object Activate_202_LoginEmailView()
	{
		return new LoginEmailView();
	}

	private object Activate_203_LoginTwitterView()
	{
		return new LoginTwitterView();
	}

	private object Activate_206_ProfileView()
	{
		return new ProfileView();
	}

	private object Activate_207_FacebookView()
	{
		return new FacebookView();
	}

	private object Activate_208_Dictionary()
	{
		return new Dictionary<string, string>();
	}

	private object Activate_211_SettingsContentView()
	{
		return new SettingsContentView();
	}

	private object Activate_212_SettingsView()
	{
		return new SettingsView();
	}

	private object Activate_213_AttributionView()
	{
		return new AttributionView();
	}

	private object Activate_214_SingleVineView()
	{
		return new SingleVineView();
	}

	private object Activate_215_SingleVineViewParams()
	{
		return new SingleVineViewParams();
	}

	private object Activate_216_TagVineListView()
	{
		return new TagVineListView();
	}

	private object Activate_217_UploadJobsView()
	{
		return new UploadJobsView();
	}

	private object Activate_221_MessageTemplateSelector()
	{
		return new MessageTemplateSelector();
	}

	private object Activate_222_VineMessagesThreadView()
	{
		return new VineMessagesThreadView();
	}

	private object Activate_224_ObservableCollection()
	{
		return new ObservableCollection<VineMessageViewModel>();
	}

	private object Activate_225_Collection()
	{
		return new Collection<VineMessageViewModel>();
	}

	private object Activate_226_VineMessageViewModel()
	{
		return new VineMessageViewModel();
	}

	private object Activate_227_VineMessageModel()
	{
		return new VineMessageModel();
	}

	private object Activate_228_VineListTemplateSelector()
	{
		return new VineListTemplateSelector();
	}

	private object Activate_229_VinePressedButton()
	{
		return new VinePressedButton();
	}

	private object Activate_230_ResetPasswordView()
	{
		return new ResetPasswordView();
	}

	private object Activate_231_VineUserListView()
	{
		return new VineUserListView();
	}

	private object Activate_233_VineUserListViewParams()
	{
		return new VineUserListViewParams();
	}

	private object Activate_234_WebView()
	{
		return new WebView();
	}

	private object Activate_235_WelcomeView()
	{
		return new WelcomeView();
	}

	private object Activate_236_InstanceCountPopupControl()
	{
		return new InstanceCountPopupControl();
	}

	private object Activate_237_MediaFile()
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Expected O, but got Unknown
		return (object)new MediaFile();
	}

	private object Activate_239_StreamInfo()
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Expected O, but got Unknown
		return (object)new StreamInfo();
	}

	private object Activate_240_AudioProp()
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Expected O, but got Unknown
		return (object)new AudioProp();
	}

	private object Activate_241_VideoProp()
	{
		//IL_0000: Unknown result type (might be due to invalid IL or missing references)
		//IL_0006: Expected O, but got Unknown
		return (object)new VideoProp();
	}

	private void VectorAdd_38_ObservableCollection(object instance, object item)
	{
		ICollection<ChannelModel> obj = (ICollection<ChannelModel>)instance;
		ChannelModel item2 = (ChannelModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_39_Collection(object instance, object item)
	{
		ICollection<ChannelModel> obj = (ICollection<ChannelModel>)instance;
		ChannelModel item2 = (ChannelModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_42_ObservableCollection(object instance, object item)
	{
		ICollection<RecordingVineModel> obj = (ICollection<RecordingVineModel>)instance;
		RecordingVineModel item2 = (RecordingVineModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_43_Collection(object instance, object item)
	{
		ICollection<RecordingVineModel> obj = (ICollection<RecordingVineModel>)instance;
		RecordingVineModel item2 = (RecordingVineModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_45_List(object instance, object item)
	{
		ICollection<RecordingClipModel> obj = (ICollection<RecordingClipModel>)instance;
		RecordingClipModel item2 = (RecordingClipModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_49_ObservableCollection(object instance, object item)
	{
		ICollection<EditClipsViewModel> obj = (ICollection<EditClipsViewModel>)instance;
		EditClipsViewModel item2 = (EditClipsViewModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_50_Collection(object instance, object item)
	{
		ICollection<EditClipsViewModel> obj = (ICollection<EditClipsViewModel>)instance;
		EditClipsViewModel item2 = (EditClipsViewModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_54_ObservableCollection(object instance, object item)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_000d: Expected O, but got Unknown
		ICollection<BitmapImage> obj = (ICollection<BitmapImage>)instance;
		BitmapImage item2 = (BitmapImage)item;
		obj.Add(item2);
	}

	private void VectorAdd_55_Collection(object instance, object item)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_000d: Expected O, but got Unknown
		ICollection<BitmapImage> obj = (ICollection<BitmapImage>)instance;
		BitmapImage item2 = (BitmapImage)item;
		obj.Add(item2);
	}

	private void VectorAdd_59_RandomAccessLoadingCollection(object instance, object item)
	{
		((ICollection<object>)instance).Add(item);
	}

	private void VectorAdd_67_BehaviorCollection(object instance, object item)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_000d: Expected O, but got Unknown
		ICollection<DependencyObject> obj = (ICollection<DependencyObject>)instance;
		DependencyObject item2 = (DependencyObject)item;
		obj.Add(item2);
	}

	private void VectorAdd_73_ObservableCollection(object instance, object item)
	{
		ICollection<Entity> obj = (ICollection<Entity>)instance;
		Entity item2 = (Entity)item;
		obj.Add(item2);
	}

	private void VectorAdd_74_Collection(object instance, object item)
	{
		ICollection<Entity> obj = (ICollection<Entity>)instance;
		Entity item2 = (Entity)item;
		obj.Add(item2);
	}

	private void VectorAdd_84_IncrementalLoadingCollection(object instance, object item)
	{
		ICollection<VineViewModel> obj = (ICollection<VineViewModel>)instance;
		VineViewModel item2 = (VineViewModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_85_ObservableCollection(object instance, object item)
	{
		ICollection<VineViewModel> obj = (ICollection<VineViewModel>)instance;
		VineViewModel item2 = (VineViewModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_86_Collection(object instance, object item)
	{
		ICollection<VineViewModel> obj = (ICollection<VineViewModel>)instance;
		VineViewModel item2 = (VineViewModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_96_List(object instance, object item)
	{
		ICollection<VineModel> obj = (ICollection<VineModel>)instance;
		VineModel item2 = (VineModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_100_List(object instance, object item)
	{
		ICollection<AudioTracks> obj = (ICollection<AudioTracks>)instance;
		AudioTracks item2 = (AudioTracks)item;
		obj.Add(item2);
	}

	private void VectorAdd_103_List(object instance, object item)
	{
		ICollection<Entity> obj = (ICollection<Entity>)instance;
		Entity item2 = (Entity)item;
		obj.Add(item2);
	}

	private void VectorAdd_110_IncrementalLoadingCollection(object instance, object item)
	{
		ICollection<CommentModel> obj = (ICollection<CommentModel>)instance;
		CommentModel item2 = (CommentModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_111_ObservableCollection(object instance, object item)
	{
		ICollection<CommentModel> obj = (ICollection<CommentModel>)instance;
		CommentModel item2 = (CommentModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_112_Collection(object instance, object item)
	{
		ICollection<CommentModel> obj = (ICollection<CommentModel>)instance;
		CommentModel item2 = (CommentModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_121_IncrementalLoadingCollection(object instance, object item)
	{
		ICollection<ConversationViewModel> obj = (ICollection<ConversationViewModel>)instance;
		ConversationViewModel item2 = (ConversationViewModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_122_ObservableCollection(object instance, object item)
	{
		ICollection<ConversationViewModel> obj = (ICollection<ConversationViewModel>)instance;
		ConversationViewModel item2 = (ConversationViewModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_123_Collection(object instance, object item)
	{
		ICollection<ConversationViewModel> obj = (ICollection<ConversationViewModel>)instance;
		ConversationViewModel item2 = (ConversationViewModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_126_List(object instance, object item)
	{
		ICollection<string> obj = (ICollection<string>)instance;
		string item2 = (string)item;
		obj.Add(item2);
	}

	private void VectorAdd_140_ObservableCollection(object instance, object item)
	{
		ICollection<FriendFinderModel> obj = (ICollection<FriendFinderModel>)instance;
		FriendFinderModel item2 = (FriendFinderModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_141_Collection(object instance, object item)
	{
		ICollection<FriendFinderModel> obj = (ICollection<FriendFinderModel>)instance;
		FriendFinderModel item2 = (FriendFinderModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_148_ObservableCollection(object instance, object item)
	{
		ICollection<SearchResultModel> obj = (ICollection<SearchResultModel>)instance;
		SearchResultModel item2 = (SearchResultModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_149_Collection(object instance, object item)
	{
		ICollection<SearchResultModel> obj = (ICollection<SearchResultModel>)instance;
		SearchResultModel item2 = (SearchResultModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_156_ObservableCollection(object instance, object item)
	{
		ICollection<VineRecentSearch> obj = (ICollection<VineRecentSearch>)instance;
		VineRecentSearch item2 = (VineRecentSearch)item;
		obj.Add(item2);
	}

	private void VectorAdd_157_Collection(object instance, object item)
	{
		ICollection<VineRecentSearch> obj = (ICollection<VineRecentSearch>)instance;
		VineRecentSearch item2 = (VineRecentSearch)item;
		obj.Add(item2);
	}

	private void VectorAdd_159_IncrementalLoadingCollection(object instance, object item)
	{
		ICollection<VineTagModel> obj = (ICollection<VineTagModel>)instance;
		VineTagModel item2 = (VineTagModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_160_ObservableCollection(object instance, object item)
	{
		ICollection<VineTagModel> obj = (ICollection<VineTagModel>)instance;
		VineTagModel item2 = (VineTagModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_161_Collection(object instance, object item)
	{
		ICollection<VineTagModel> obj = (ICollection<VineTagModel>)instance;
		VineTagModel item2 = (VineTagModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_165_List(object instance, object item)
	{
		ICollection<VineContactViewModel> obj = (ICollection<VineContactViewModel>)instance;
		VineContactViewModel item2 = (VineContactViewModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_167_List(object instance, object item)
	{
		ICollection<Tuple<string, string>> obj = (ICollection<Tuple<string, string>>)instance;
		Tuple<string, string> item2 = (Tuple<string, string>)item;
		obj.Add(item2);
	}

	private void VectorAdd_169_IncrementalLoadingCollection(object instance, object item)
	{
		ICollection<VineContactViewModel> obj = (ICollection<VineContactViewModel>)instance;
		VineContactViewModel item2 = (VineContactViewModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_170_ObservableCollection(object instance, object item)
	{
		ICollection<VineContactViewModel> obj = (ICollection<VineContactViewModel>)instance;
		VineContactViewModel item2 = (VineContactViewModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_171_Collection(object instance, object item)
	{
		ICollection<VineContactViewModel> obj = (ICollection<VineContactViewModel>)instance;
		VineContactViewModel item2 = (VineContactViewModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_185_List(object instance, object item)
	{
		ICollection<VineUserModel> obj = (ICollection<VineUserModel>)instance;
		VineUserModel item2 = (VineUserModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_188_List(object instance, object item)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_000d: Expected O, but got Unknown
		ICollection<Run> obj = (ICollection<Run>)instance;
		Run item2 = (Run)item;
		obj.Add(item2);
	}

	private void VectorAdd_190_ObservableCollection(object instance, object item)
	{
		ICollection<VineUserModel> obj = (ICollection<VineUserModel>)instance;
		VineUserModel item2 = (VineUserModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_191_Collection(object instance, object item)
	{
		ICollection<VineUserModel> obj = (ICollection<VineUserModel>)instance;
		VineUserModel item2 = (VineUserModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_193_IncrementalLoadingCollection(object instance, object item)
	{
		ICollection<InteractionModel> obj = (ICollection<InteractionModel>)instance;
		InteractionModel item2 = (InteractionModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_194_ObservableCollection(object instance, object item)
	{
		ICollection<InteractionModel> obj = (ICollection<InteractionModel>)instance;
		InteractionModel item2 = (InteractionModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_195_Collection(object instance, object item)
	{
		ICollection<InteractionModel> obj = (ICollection<InteractionModel>)instance;
		InteractionModel item2 = (InteractionModel)item;
		obj.Add(item2);
	}

	private void MapAdd_208_Dictionary(object instance, object key, object item)
	{
		IDictionary<string, string> obj = (IDictionary<string, string>)instance;
		string key2 = (string)key;
		string value = (string)item;
		obj.Add(key2, value);
	}

	private void VectorAdd_223_IncrementalLoadingCollection(object instance, object item)
	{
		ICollection<VineMessageViewModel> obj = (ICollection<VineMessageViewModel>)instance;
		VineMessageViewModel item2 = (VineMessageViewModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_224_ObservableCollection(object instance, object item)
	{
		ICollection<VineMessageViewModel> obj = (ICollection<VineMessageViewModel>)instance;
		VineMessageViewModel item2 = (VineMessageViewModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_225_Collection(object instance, object item)
	{
		ICollection<VineMessageViewModel> obj = (ICollection<VineMessageViewModel>)instance;
		VineMessageViewModel item2 = (VineMessageViewModel)item;
		obj.Add(item2);
	}

	private void VectorAdd_232_IncrementalLoadingCollection(object instance, object item)
	{
		ICollection<VineUserModel> obj = (ICollection<VineUserModel>)instance;
		VineUserModel item2 = (VineUserModel)item;
		obj.Add(item2);
	}

	private IXamlType CreateXamlType(int typeIndex)
	{
		XamlSystemBaseType result = null;
		string fullName = _typeNameTable[typeIndex];
		Type type = _typeTable[typeIndex];
		switch (typeIndex)
		{
		case 0:
		{
			XamlUserType xamlUserType184 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.ValueType"));
			xamlUserType184.AddMemberName("A");
			xamlUserType184.AddMemberName("B");
			xamlUserType184.AddMemberName("G");
			xamlUserType184.AddMemberName("R");
			result = xamlUserType184;
			break;
		}
		case 1:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			break;
		case 2:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 3:
		{
			XamlUserType xamlUserType183 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.ValueType"));
			xamlUserType183.SetIsReturnTypeStub();
			result = xamlUserType183;
			break;
		}
		case 4:
		{
			XamlUserType xamlUserType182 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType182.Activator = Activate_4_VisibleIfTrueConverter;
			xamlUserType182.AddMemberName("InvertValue");
			xamlUserType182.SetIsLocalType();
			result = xamlUserType182;
			break;
		}
		case 5:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 6:
		{
			XamlUserType xamlUserType181 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType181.Activator = Activate_6_NoneToVisibilityConverter;
			xamlUserType181.AddMemberName("InvertValue");
			xamlUserType181.SetIsLocalType();
			result = xamlUserType181;
			break;
		}
		case 7:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.ValueType"));
			break;
		case 8:
		{
			XamlUserType xamlUserType180 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.UserControl"));
			xamlUserType180.Activator = Activate_8_ChannelTileWide;
			xamlUserType180.SetIsLocalType();
			result = xamlUserType180;
			break;
		}
		case 9:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 10:
		{
			XamlUserType xamlUserType179 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.UserControl"));
			xamlUserType179.Activator = Activate_10_GenericSmallTile;
			xamlUserType179.SetIsLocalType();
			result = xamlUserType179;
			break;
		}
		case 11:
		{
			XamlUserType xamlUserType178 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.UserControl"));
			xamlUserType178.Activator = Activate_11_SpecificTagSmallTile;
			xamlUserType178.SetIsLocalType();
			result = xamlUserType178;
			break;
		}
		case 12:
		{
			XamlUserType xamlUserType177 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.UserControl"));
			xamlUserType177.Activator = Activate_12_SearchSmallTile;
			xamlUserType177.SetIsLocalType();
			result = xamlUserType177;
			break;
		}
		case 13:
		{
			XamlUserType xamlUserType176 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.UserControl"));
			xamlUserType176.Activator = Activate_13_AvatarControl;
			xamlUserType176.AddMemberName("IsBusy");
			xamlUserType176.AddMemberName("BusyVisible");
			xamlUserType176.AddMemberName("DisableFlyout");
			xamlUserType176.AddMemberName("User");
			xamlUserType176.SetIsLocalType();
			result = xamlUserType176;
			break;
		}
		case 14:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 15:
		{
			XamlUserType xamlUserType175 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Models.NotifyObject"));
			xamlUserType175.Activator = Activate_15_VineUserModel;
			xamlUserType175.AddMemberName("Section");
			xamlUserType175.AddMemberName("User");
			xamlUserType175.AddMemberName("ExternalUser");
			xamlUserType175.AddMemberName("ProfileBackground");
			xamlUserType175.AddMemberName("ProfileBgBannerBrush");
			xamlUserType175.AddMemberName("ProfileBgBrush");
			xamlUserType175.AddMemberName("ProfileBgLightBrush");
			xamlUserType175.AddMemberName("PhoneNumber");
			xamlUserType175.AddMemberName("Email");
			xamlUserType175.AddMemberName("Username");
			xamlUserType175.AddMemberName("Description");
			xamlUserType175.AddMemberName("AvatarUrl");
			xamlUserType175.AddMemberName("Location");
			xamlUserType175.AddMemberName("UserId");
			xamlUserType175.AddMemberName("FollowingCount");
			xamlUserType175.AddMemberName("FollowerCount");
			xamlUserType175.AddMemberName("PostCount");
			xamlUserType175.AddMemberName("LikeCount");
			xamlUserType175.AddMemberName("LoopCount");
			xamlUserType175.AddMemberName("Blocked");
			xamlUserType175.AddMemberName("Following");
			xamlUserType175.AddMemberName("Verified");
			xamlUserType175.AddMemberName("FollowRequested");
			xamlUserType175.AddMemberName("Private");
			xamlUserType175.AddMemberName("TwitterScreenname");
			xamlUserType175.AddMemberName("TwitterDisplayScreenname");
			xamlUserType175.AddMemberName("TwitterConnected");
			xamlUserType175.AddMemberName("VerifiedPhoneNumber");
			xamlUserType175.AddMemberName("VerifiedEmail");
			xamlUserType175.AddMemberName("FacebookConnected");
			xamlUserType175.AddMemberName("ExplicitContent");
			xamlUserType175.AddMemberName("HiddenEmail");
			xamlUserType175.AddMemberName("HiddenEmailButtonState");
			xamlUserType175.AddMemberName("HiddenPhoneNumber");
			xamlUserType175.AddMemberName("HiddenPhoneNumberButtonState");
			xamlUserType175.AddMemberName("HiddenTwitter");
			xamlUserType175.AddMemberName("HiddenTwitterButtonState");
			xamlUserType175.AddMemberName("FollowApprovalPending");
			xamlUserType175.AddMemberName("ByLine");
			xamlUserType175.AddMemberName("FollowingEnabled");
			xamlUserType175.AddMemberName("FollowButtonState");
			xamlUserType175.AddMemberName("ExplicitContentButtonState");
			xamlUserType175.AddMemberName("ProtectedButtonState");
			xamlUserType175.AddMemberName("IsCurrentUser");
			xamlUserType175.AddMemberName("AreVinesViewable");
			xamlUserType175.AddMemberName("FollowCommand");
			xamlUserType175.AddMemberName("UserType");
			xamlUserType175.AddMemberName("RichFollowers");
			xamlUserType175.AddMemberName("RichFollowing");
			xamlUserType175.AddMemberName("RichLoops");
			xamlUserType175.AddMemberName("LoopCountShortened");
			xamlUserType175.AddMemberName("RichPostCount");
			xamlUserType175.AddMemberName("RichLikesCount");
			xamlUserType175.SetIsLocalType();
			result = xamlUserType175;
			break;
		}
		case 16:
		{
			XamlUserType xamlUserType174 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType174.Activator = Activate_16_NotifyObject;
			xamlUserType174.SetIsLocalType();
			result = xamlUserType174;
			break;
		}
		case 17:
		{
			XamlUserType xamlUserType173 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.NotifyPage"));
			xamlUserType173.Activator = Activate_17_BasePage;
			xamlUserType173.AddMemberName("NavigationHelper");
			xamlUserType173.AddMemberName("NavigationParam");
			xamlUserType173.AddMemberName("NavigationObject");
			xamlUserType173.AddMemberName("AlwaysClearBackStack");
			xamlUserType173.SetIsLocalType();
			result = xamlUserType173;
			break;
		}
		case 18:
		{
			XamlUserType xamlUserType172 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.Page"));
			xamlUserType172.Activator = Activate_18_NotifyPage;
			xamlUserType172.AddMemberName("WindowWidth");
			xamlUserType172.AddMemberName("WindowHeight");
			xamlUserType172.AddMemberName("WindowWidthGridLength");
			xamlUserType172.SetIsLocalType();
			result = xamlUserType172;
			break;
		}
		case 19:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 20:
		{
			XamlUserType xamlUserType171 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.DependencyObject"));
			xamlUserType171.SetIsReturnTypeStub();
			xamlUserType171.SetIsLocalType();
			result = xamlUserType171;
			break;
		}
		case 21:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 22:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 23:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 24:
		{
			XamlUserType xamlUserType170 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType170.Activator = Activate_24_AvatarCropView;
			xamlUserType170.SetIsLocalType();
			result = xamlUserType170;
			break;
		}
		case 25:
		{
			XamlUserType xamlUserType169 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType169.Activator = Activate_25_CaptureView10;
			xamlUserType169.AddMemberName("CurrentTutorialState");
			xamlUserType169.AddMemberName("TutorialHintVisibility");
			xamlUserType169.AddMemberName("TutorialWelcomeVisibility");
			xamlUserType169.AddMemberName("TutorialMessage");
			xamlUserType169.AddMemberName("ButtonTutorialCameraToolsVisibility");
			xamlUserType169.AddMemberName("ButtonTutorialUndoVisibility");
			xamlUserType169.AddMemberName("ButtonTutorialGrabVideoVisibility");
			xamlUserType169.AddMemberName("ButtonTutorialState");
			xamlUserType169.AddMemberName("PreviewGridClip");
			xamlUserType169.AddMemberName("CaptureMargin");
			xamlUserType169.AddMemberName("NextButtonVisibility");
			xamlUserType169.AddMemberName("IsBusy");
			xamlUserType169.AddMemberName("IsTorchSupported");
			xamlUserType169.AddMemberName("IsFrontCameraSupported");
			xamlUserType169.AddMemberName("IsFocusSupported");
			xamlUserType169.AddMemberName("IsFocusLocked");
			xamlUserType169.AddMemberName("IsGhostModeHighlighted");
			xamlUserType169.AddMemberName("IsGridHighlighted");
			xamlUserType169.AddMemberName("IsExpanded");
			xamlUserType169.AddMemberName("IsGridVisible");
			xamlUserType169.AddMemberName("IsUndoHighlighted");
			xamlUserType169.AddMemberName("IsCameraHighlighted");
			xamlUserType169.AddMemberName("IsTorchHighlighted");
			xamlUserType169.AddMemberName("IsFocusModeHighlighted");
			xamlUserType169.AddMemberName("PendingChanges");
			xamlUserType169.AddMemberName("RecordingDraftCount");
			xamlUserType169.AddMemberName("DraftNumber");
			xamlUserType169.AddMemberName("IsDraftsEnabled");
			xamlUserType169.AddMemberName("IsUndoEnabled");
			xamlUserType169.AddMemberName("GhostImageSource");
			xamlUserType169.AddMemberName("FocusButtonBrush");
			xamlUserType169.AddMemberName("GhostButtonBrush");
			xamlUserType169.AddMemberName("GridButtonBrush");
			xamlUserType169.AddMemberName("CameraButtonBrush");
			xamlUserType169.AddMemberName("WrenchBrush");
			xamlUserType169.AddMemberName("TorchButtonBrush");
			xamlUserType169.AddMemberName("MediaCapture");
			xamlUserType169.AddMemberName("VMParameters");
			xamlUserType169.SetIsLocalType();
			result = xamlUserType169;
			break;
		}
		case 26:
		{
			XamlUserType xamlUserType168 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Enum"));
			xamlUserType168.AddEnumValue("Welcome", CaptureView10.TutorialState.Welcome);
			xamlUserType168.AddEnumValue("FirstCapture", CaptureView10.TutorialState.FirstCapture);
			xamlUserType168.AddEnumValue("FirstCaptureRunning", CaptureView10.TutorialState.FirstCaptureRunning);
			xamlUserType168.AddEnumValue("FirstCaptureDone", CaptureView10.TutorialState.FirstCaptureDone);
			xamlUserType168.AddEnumValue("SecondCapture", CaptureView10.TutorialState.SecondCapture);
			xamlUserType168.AddEnumValue("SecondCaptureRunning", CaptureView10.TutorialState.SecondCaptureRunning);
			xamlUserType168.AddEnumValue("SecondCaptureDone", CaptureView10.TutorialState.SecondCaptureDone);
			xamlUserType168.AddEnumValue("ThirdCapture", CaptureView10.TutorialState.ThirdCapture);
			xamlUserType168.AddEnumValue("ThirdCaptureRunning", CaptureView10.TutorialState.ThirdCaptureRunning);
			xamlUserType168.AddEnumValue("ThirdCaptureDone", CaptureView10.TutorialState.ThirdCaptureDone);
			xamlUserType168.AddEnumValue("Encoding", CaptureView10.TutorialState.Encoding);
			xamlUserType168.AddEnumValue("FinalMessage", CaptureView10.TutorialState.FinalMessage);
			xamlUserType168.AddEnumValue("NotRunning", CaptureView10.TutorialState.NotRunning);
			xamlUserType168.SetIsLocalType();
			result = xamlUserType168;
			break;
		}
		case 27:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.ValueType"));
			break;
		case 28:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 29:
		{
			XamlUserType xamlUserType167 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Enum"));
			xamlUserType167.AddEnumValue("CameraRoll", CaptureView10.ButtonsTutorialEnum.CameraRoll);
			xamlUserType167.AddEnumValue("Undo", CaptureView10.ButtonsTutorialEnum.Undo);
			xamlUserType167.AddEnumValue("Delete", CaptureView10.ButtonsTutorialEnum.Delete);
			xamlUserType167.AddEnumValue("Tools", CaptureView10.ButtonsTutorialEnum.Tools);
			xamlUserType167.AddEnumValue("Done", CaptureView10.ButtonsTutorialEnum.Done);
			xamlUserType167.SetIsLocalType();
			result = xamlUserType167;
			break;
		}
		case 30:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 31:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 32:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 33:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 34:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 35:
		{
			XamlUserType xamlUserType166 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType166.SetIsReturnTypeStub();
			result = xamlUserType166;
			break;
		}
		case 36:
		{
			XamlUserType xamlUserType165 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType165.SetIsReturnTypeStub();
			xamlUserType165.SetIsLocalType();
			result = xamlUserType165;
			break;
		}
		case 37:
		{
			XamlUserType xamlUserType164 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType164.Activator = Activate_37_ChannelSelectView;
			xamlUserType164.AddMemberName("Items");
			xamlUserType164.AddMemberName("IsBusy");
			xamlUserType164.SetIsLocalType();
			result = xamlUserType164;
			break;
		}
		case 38:
		{
			XamlUserType xamlUserType163 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Vine.Models.ChannelModel>"));
			xamlUserType163.CollectionAdd = VectorAdd_38_ObservableCollection;
			xamlUserType163.SetIsReturnTypeStub();
			result = xamlUserType163;
			break;
		}
		case 39:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_39_Collection,
				CollectionAdd = VectorAdd_39_Collection
			};
			break;
		case 40:
		{
			XamlUserType xamlUserType162 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType162.Activator = Activate_40_ChannelModel;
			xamlUserType162.AddMemberName("IconFullUrl");
			xamlUserType162.AddMemberName("ChannelId");
			xamlUserType162.AddMemberName("ExploreName");
			xamlUserType162.SetIsLocalType();
			result = xamlUserType162;
			break;
		}
		case 41:
		{
			XamlUserType xamlUserType161 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType161.Activator = Activate_41_DraftsView;
			xamlUserType161.AddMemberName("Items");
			xamlUserType161.AddMemberName("DraftId");
			xamlUserType161.SetIsLocalType();
			result = xamlUserType161;
			break;
		}
		case 42:
		{
			XamlUserType xamlUserType160 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Vine.Models.RecordingVineModel>"));
			xamlUserType160.CollectionAdd = VectorAdd_42_ObservableCollection;
			xamlUserType160.SetIsReturnTypeStub();
			result = xamlUserType160;
			break;
		}
		case 43:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_43_Collection,
				CollectionAdd = VectorAdd_43_Collection
			};
			break;
		case 44:
		{
			XamlUserType xamlUserType159 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType159.Activator = Activate_44_RecordingVineModel;
			xamlUserType159.AddMemberName("DraftId");
			xamlUserType159.AddMemberName("UploadId");
			xamlUserType159.AddMemberName("Clips");
			xamlUserType159.AddMemberName("LastRenderedVideoFilePath");
			xamlUserType159.AddMemberName("LastRenderedThumbFilePath");
			xamlUserType159.AddMemberName("SavedClips");
			xamlUserType159.AddMemberName("HasPendingChangesOnCapture");
			xamlUserType159.AddMemberName("Duration");
			xamlUserType159.AddMemberName("IsClipsSequentialOneFile");
			xamlUserType159.SetIsLocalType();
			result = xamlUserType159;
			break;
		}
		case 45:
		{
			XamlUserType xamlUserType158 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType158.CollectionAdd = VectorAdd_45_List;
			xamlUserType158.SetIsReturnTypeStub();
			result = xamlUserType158;
			break;
		}
		case 46:
		{
			XamlUserType xamlUserType157 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType157.Activator = Activate_46_RecordingClipModel;
			xamlUserType157.AddMemberName("VideoFilePath");
			xamlUserType157.AddMemberName("GhostFilePath");
			xamlUserType157.AddMemberName("VideoFileDuration");
			xamlUserType157.AddMemberName("FrameRate");
			xamlUserType157.AddMemberName("FileStartTime");
			xamlUserType157.AddMemberName("FileEndTime");
			xamlUserType157.AddMemberName("EditStartTime");
			xamlUserType157.AddMemberName("EditEndTime");
			xamlUserType157.SetIsLocalType();
			result = xamlUserType157;
			break;
		}
		case 47:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 48:
		{
			XamlUserType xamlUserType156 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType156.Activator = Activate_48_EditClipsView;
			xamlUserType156.AddMemberName("Items");
			xamlUserType156.AddMemberName("TrimThumbnails");
			xamlUserType156.AddMemberName("TrimThumbnailWidth");
			xamlUserType156.AddMemberName("IsFinishedLoading");
			xamlUserType156.AddMemberName("IsBusy");
			xamlUserType156.AddMemberName("TrimHighlightRectX");
			xamlUserType156.AddMemberName("TrimHighlightRectWidth");
			xamlUserType156.AddMemberName("Selected");
			xamlUserType156.AddMemberName("HasSelection");
			xamlUserType156.AddMemberName("HasMoreThanOneClip");
			xamlUserType156.AddMemberName("RightSliderValue");
			xamlUserType156.AddMemberName("LeftSliderValue");
			xamlUserType156.SetIsLocalType();
			result = xamlUserType156;
			break;
		}
		case 49:
		{
			XamlUserType xamlUserType155 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Vine.Views.Capture.EditClipsViewModel>"));
			xamlUserType155.CollectionAdd = VectorAdd_49_ObservableCollection;
			xamlUserType155.SetIsReturnTypeStub();
			result = xamlUserType155;
			break;
		}
		case 50:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_50_Collection,
				CollectionAdd = VectorAdd_50_Collection
			};
			break;
		case 51:
		{
			XamlUserType xamlUserType154 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Models.NotifyObject"));
			xamlUserType154.Activator = Activate_51_EditClipsViewModel;
			xamlUserType154.AddMemberName("Thumb");
			xamlUserType154.AddMemberName("Composition");
			xamlUserType154.AddMemberName("MediaClip");
			xamlUserType154.AddMemberName("ClipModel");
			xamlUserType154.AddMemberName("IsActive");
			xamlUserType154.AddMemberName("Opacity");
			xamlUserType154.AddMemberName("IsPlaying");
			xamlUserType154.SetIsLocalType();
			result = xamlUserType154;
			break;
		}
		case 52:
		{
			XamlUserType xamlUserType153 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType153.SetIsReturnTypeStub();
			result = xamlUserType153;
			break;
		}
		case 53:
		{
			XamlUserType xamlUserType152 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType152.SetIsReturnTypeStub();
			result = xamlUserType152;
			break;
		}
		case 54:
		{
			XamlUserType xamlUserType151 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Windows.UI.Xaml.Media.Imaging.BitmapImage>"));
			xamlUserType151.CollectionAdd = VectorAdd_54_ObservableCollection;
			xamlUserType151.SetIsReturnTypeStub();
			result = xamlUserType151;
			break;
		}
		case 55:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_55_Collection,
				CollectionAdd = VectorAdd_55_Collection
			};
			break;
		case 56:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 57:
		{
			XamlUserType xamlUserType150 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.UserControl"));
			xamlUserType150.Activator = Activate_57_ImportListItemView;
			xamlUserType150.SetIsLocalType();
			result = xamlUserType150;
			break;
		}
		case 58:
		{
			XamlUserType xamlUserType149 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType149.Activator = Activate_58_ImportView;
			xamlUserType149.AddMemberName("Items");
			xamlUserType149.AddMemberName("IsAutoPlay");
			xamlUserType149.AddMemberName("TrimSliderValue");
			xamlUserType149.AddMemberName("ScrubValue");
			xamlUserType149.AddMemberName("ScrubSliderVisibility");
			xamlUserType149.AddMemberName("ScrubImg");
			xamlUserType149.AddMemberName("IsBusy");
			xamlUserType149.AddMemberName("IsProgressZero");
			xamlUserType149.AddMemberName("ProgressValue");
			xamlUserType149.SetIsLocalType();
			result = xamlUserType149;
			break;
		}
		case 59:
		{
			XamlUserType xamlUserType148 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType148.CollectionAdd = VectorAdd_59_RandomAccessLoadingCollection;
			xamlUserType148.SetIsReturnTypeStub();
			xamlUserType148.SetIsLocalType();
			result = xamlUserType148;
			break;
		}
		case 60:
		{
			XamlUserType xamlUserType147 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType147.Activator = Activate_60_PreviewCaptureView;
			xamlUserType147.AddMemberName("HasTutorialBeenSeen");
			xamlUserType147.AddMemberName("TutorialMessage");
			xamlUserType147.AddMemberName("Params");
			xamlUserType147.AddMemberName("RenderFile");
			xamlUserType147.AddMemberName("IsBusy");
			xamlUserType147.AddMemberName("IsProgressZero");
			xamlUserType147.AddMemberName("ProgressValue");
			xamlUserType147.AddMemberName("ShareButtonBrush");
			xamlUserType147.AddMemberName("IsFinishedLoading");
			xamlUserType147.SetIsLocalType();
			result = xamlUserType147;
			break;
		}
		case 61:
		{
			XamlUserType xamlUserType146 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType146.SetIsReturnTypeStub();
			xamlUserType146.SetIsLocalType();
			result = xamlUserType146;
			break;
		}
		case 62:
		{
			XamlUserType xamlUserType145 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType145.SetIsReturnTypeStub();
			result = xamlUserType145;
			break;
		}
		case 63:
		{
			XamlUserType xamlUserType144 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.DataTemplateSelector"));
			xamlUserType144.Activator = Activate_63_TaggingTemplateSelector;
			xamlUserType144.AddMemberName("MentionTemplate");
			xamlUserType144.AddMemberName("HashtagTemplate");
			xamlUserType144.SetIsLocalType();
			result = xamlUserType144;
			break;
		}
		case 64:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 65:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 66:
		{
			XamlUserType xamlUserType143 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType143.AddMemberName("Behaviors");
			result = xamlUserType143;
			break;
		}
		case 67:
		{
			XamlUserType xamlUserType142 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.DependencyObjectCollection"));
			xamlUserType142.CollectionAdd = VectorAdd_67_BehaviorCollection;
			xamlUserType142.SetIsReturnTypeStub();
			result = xamlUserType142;
			break;
		}
		case 68:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 69:
		{
			XamlUserType xamlUserType141 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.Behavior`1<Windows.UI.Xaml.Controls.TextBox>"));
			xamlUserType141.Activator = Activate_69_TextBoxUpdateBehavior;
			xamlUserType141.SetIsLocalType();
			result = xamlUserType141;
			break;
		}
		case 70:
		{
			XamlUserType xamlUserType140 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.DependencyObject"));
			xamlUserType140.AddMemberName("AssociatedObject");
			xamlUserType140.AddMemberName("Object");
			xamlUserType140.SetIsLocalType();
			result = xamlUserType140;
			break;
		}
		case 71:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 72:
		{
			XamlUserType xamlUserType139 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType139.Activator = Activate_72_ShareCaptureView;
			xamlUserType139.AddMemberName("MessageHeader");
			xamlUserType139.AddMemberName("OkLabel");
			xamlUserType139.AddMemberName("AddTagTitle");
			xamlUserType139.AddMemberName("IsMention");
			xamlUserType139.AddMemberName("TutorialHintVisibility");
			xamlUserType139.AddMemberName("IsBusy");
			xamlUserType139.AddMemberName("TextInput");
			xamlUserType139.AddMemberName("CharsLeft");
			xamlUserType139.AddMemberName("IsTwitterOn");
			xamlUserType139.AddMemberName("IsFacebookOn");
			xamlUserType139.AddMemberName("HeaderBrush");
			xamlUserType139.AddMemberName("IsVineOn");
			xamlUserType139.AddMemberName("Channel");
			xamlUserType139.AddMemberName("ChannelStatus");
			xamlUserType139.AddMemberName("IsCommenting");
			xamlUserType139.AddMemberName("TagBarVisibility");
			xamlUserType139.AddMemberName("AutoCompleteList");
			xamlUserType139.AddMemberName("IsAutoCompleteListOpen");
			xamlUserType139.SetIsLocalType();
			result = xamlUserType139;
			break;
		}
		case 73:
		{
			XamlUserType xamlUserType138 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Vine.Models.Entity>"));
			xamlUserType138.CollectionAdd = VectorAdd_73_ObservableCollection;
			xamlUserType138.SetIsReturnTypeStub();
			result = xamlUserType138;
			break;
		}
		case 74:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_74_Collection,
				CollectionAdd = VectorAdd_74_Collection
			};
			break;
		case 75:
		{
			XamlUserType xamlUserType137 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType137.Activator = Activate_75_Entity;
			xamlUserType137.AddMemberName("Type");
			xamlUserType137.AddMemberName("EntityType");
			xamlUserType137.AddMemberName("Id");
			xamlUserType137.AddMemberName("Title");
			xamlUserType137.AddMemberName("Link");
			xamlUserType137.AddMemberName("Range");
			xamlUserType137.AddMemberName("Text");
			xamlUserType137.AddMemberName("User");
			xamlUserType137.SetIsLocalType();
			result = xamlUserType137;
			break;
		}
		case 76:
		{
			XamlUserType xamlUserType136 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Enum"));
			xamlUserType136.AddEnumValue("Unknown", EntityType.Unknown);
			xamlUserType136.AddEnumValue("mention", EntityType.mention);
			xamlUserType136.AddEnumValue("user", EntityType.user);
			xamlUserType136.AddEnumValue("userList", EntityType.userList);
			xamlUserType136.AddEnumValue("tag", EntityType.tag);
			xamlUserType136.AddEnumValue("post", EntityType.post);
			xamlUserType136.AddEnumValue("commentList", EntityType.commentList);
			xamlUserType136.SetIsLocalType();
			result = xamlUserType136;
			break;
		}
		case 77:
		{
			XamlUserType xamlUserType135 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Array"));
			xamlUserType135.SetIsReturnTypeStub();
			result = xamlUserType135;
			break;
		}
		case 78:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			break;
		case 79:
		{
			XamlUserType xamlUserType134 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BaseUserControl"));
			xamlUserType134.Activator = Activate_79_MusicInformationControl;
			xamlUserType134.AddMemberName("MusicTrack");
			xamlUserType134.AddMemberName("MusicArtist");
			xamlUserType134.SetIsLocalType();
			result = xamlUserType134;
			break;
		}
		case 80:
		{
			XamlUserType xamlUserType133 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.UserControl"));
			xamlUserType133.SetIsLocalType();
			result = xamlUserType133;
			break;
		}
		case 81:
		{
			XamlUserType xamlUserType132 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.NotifyPage"));
			xamlUserType132.Activator = Activate_81_VineListControl;
			xamlUserType132.AddMemberName("Section");
			xamlUserType132.AddMemberName("SecondaryBrush");
			xamlUserType132.AddMemberName("MosaicThumbnailMargin");
			xamlUserType132.AddMemberName("PullToRefreshMargin");
			xamlUserType132.AddMemberName("ListViewPadding");
			xamlUserType132.AddMemberName("Header");
			xamlUserType132.AddMemberName("MusicControl");
			xamlUserType132.AddMemberName("HeaderSuccessfulTemplate");
			xamlUserType132.AddMemberName("Footer");
			xamlUserType132.AddMemberName("FooterTemplate");
			xamlUserType132.AddMemberName("EmptyText");
			xamlUserType132.AddMemberName("IsEmpty");
			xamlUserType132.AddMemberName("HasError");
			xamlUserType132.AddMemberName("ErrorText");
			xamlUserType132.AddMemberName("ShowRetry");
			xamlUserType132.AddMemberName("DisablePullToRefresh");
			xamlUserType132.AddMemberName("ProfileView");
			xamlUserType132.AddMemberName("Items");
			xamlUserType132.AddMemberName("IsVolumeMuted");
			xamlUserType132.AddMemberName("CurrentTab");
			xamlUserType132.AddMemberName("UserId");
			xamlUserType132.AddMemberName("PostId");
			xamlUserType132.AddMemberName("SearchTag");
			xamlUserType132.AddMemberName("ListParams");
			xamlUserType132.AddMemberName("PageStateItems");
			xamlUserType132.AddMemberName("PageStateScrollOffset");
			xamlUserType132.AddMemberName("IsBusy");
			xamlUserType132.AddMemberName("Active");
			xamlUserType132.AddMemberName("IsFinishedLoading");
			xamlUserType132.AddMemberName("ScrollOffset");
			xamlUserType132.AddMemberName("ScrollingDirection");
			xamlUserType132.SetIsLocalType();
			result = xamlUserType132;
			break;
		}
		case 82:
		{
			XamlUserType xamlUserType131 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Enum"));
			xamlUserType131.AddEnumValue("None", Section.None);
			xamlUserType131.AddEnumValue("Home", Section.Home);
			xamlUserType131.AddEnumValue("Explore", Section.Explore);
			xamlUserType131.AddEnumValue("Activity", Section.Activity);
			xamlUserType131.AddEnumValue("MyProfile", Section.MyProfile);
			xamlUserType131.AddEnumValue("VM", Section.VM);
			xamlUserType131.AddEnumValue("Capture", Section.Capture);
			xamlUserType131.AddEnumValue("LoggedOut", Section.LoggedOut);
			xamlUserType131.SetIsLocalType();
			result = xamlUserType131;
			break;
		}
		case 83:
		{
			XamlUserType xamlUserType130 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.NotifyPage"));
			xamlUserType130.Activator = Activate_83_ProfileControl;
			xamlUserType130.AddMemberName("Section");
			xamlUserType130.AddMemberName("PullToRefreshMargin");
			xamlUserType130.AddMemberName("ListViewPadding");
			xamlUserType130.AddMemberName("IsScrolledBelowPlaceholder");
			xamlUserType130.AddMemberName("ProfileHeaderHeight");
			xamlUserType130.AddMemberName("FollowApprovalBusy");
			xamlUserType130.AddMemberName("FollowApprovalNotBusy");
			xamlUserType130.AddMemberName("FooterMargin");
			xamlUserType130.AddMemberName("LikeBrush");
			xamlUserType130.AddMemberName("ControlWrapper");
			xamlUserType130.AddMemberName("User");
			xamlUserType130.AddMemberName("UserId");
			xamlUserType130.AddMemberName("IsBusy");
			xamlUserType130.AddMemberName("IsEmpty");
			xamlUserType130.AddMemberName("EmptyText");
			xamlUserType130.AddMemberName("IsSwitchingTab");
			xamlUserType130.AddMemberName("HasError");
			xamlUserType130.AddMemberName("ErrorText");
			xamlUserType130.AddMemberName("ShowRetry");
			xamlUserType130.AddMemberName("IsFinishedLoading");
			xamlUserType130.AddMemberName("TutorialHintVisibility");
			xamlUserType130.AddMemberName("ShowExplore");
			xamlUserType130.AddMemberName("ShowSuggestions");
			xamlUserType130.AddMemberName("_suggestedToFollow");
			xamlUserType130.AddMemberName("VisibleSuggestedToFollow");
			xamlUserType130.AddMemberName("IsSuggestedLoaded");
			xamlUserType130.AddMemberName("IsActive");
			xamlUserType130.AddMemberName("PostBrush");
			xamlUserType130.SetIsLocalType();
			result = xamlUserType130;
			break;
		}
		case 84:
		{
			XamlUserType xamlUserType129 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.VineViewModel>"));
			xamlUserType129.CollectionAdd = VectorAdd_84_IncrementalLoadingCollection;
			xamlUserType129.SetIsReturnTypeStub();
			xamlUserType129.SetIsLocalType();
			result = xamlUserType129;
			break;
		}
		case 85:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Vine.Models.VineViewModel>"))
			{
				Activator = Activate_85_ObservableCollection,
				CollectionAdd = VectorAdd_85_ObservableCollection
			};
			break;
		case 86:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_86_Collection,
				CollectionAdd = VectorAdd_86_Collection
			};
			break;
		case 87:
		{
			XamlUserType xamlUserType128 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Models.NotifyObject"));
			xamlUserType128.AddMemberName("LikeStatText");
			xamlUserType128.AddMemberName("CommentStatText");
			xamlUserType128.AddMemberName("RevineStatText");
			xamlUserType128.AddMemberName("IsRevinedByMe");
			xamlUserType128.AddMemberName("IsRevined");
			xamlUserType128.AddMemberName("RevinedByText");
			xamlUserType128.AddMemberName("HasSimilarVines");
			xamlUserType128.AddMemberName("IsMyPost");
			xamlUserType128.AddMemberName("RevineEnabled");
			xamlUserType128.AddMemberName("IsPlaying");
			xamlUserType128.AddMemberName("IsFinishedBuffering");
			xamlUserType128.AddMemberName("IsLoadingVideo");
			xamlUserType128.AddMemberName("PlayingVideoUrl");
			xamlUserType128.AddMemberName("ThumbnailUrlAuth");
			xamlUserType128.AddMemberName("ThumbVisibility");
			xamlUserType128.AddMemberName("LikeButtonState");
			xamlUserType128.AddMemberName("RevineButtonState");
			xamlUserType128.AddMemberName("LoopText");
			xamlUserType128.AddMemberName("LoopLabelText");
			xamlUserType128.AddMemberName("CreatedText");
			xamlUserType128.AddMemberName("LocationVisibility");
			xamlUserType128.AddMemberName("RichBody");
			xamlUserType128.AddMemberName("Model");
			xamlUserType128.AddMemberName("SecondaryBrush");
			xamlUserType128.AddMemberName("PendingLoopCount");
			xamlUserType128.AddMemberName("LoopsWatchedCount");
			xamlUserType128.AddMemberName("DisplayLoops");
			xamlUserType128.AddMemberName("LastLoopFinishTime");
			xamlUserType128.AddMemberName("FirstLoopStartTime");
			xamlUserType128.AddMemberName("IsDownloaded");
			xamlUserType128.AddMemberName("Section");
			xamlUserType128.AddMemberName("View");
			xamlUserType128.AddMemberName("TimelineApiUrl");
			xamlUserType128.AddMemberName("HasMusic");
			xamlUserType128.AddMemberName("IsSeamlessLooping");
			xamlUserType128.AddMemberName("LoopsPerClip");
			xamlUserType128.AddMemberName("MosaicThumbnails");
			xamlUserType128.SetIsLocalType();
			result = xamlUserType128;
			break;
		}
		case 88:
		{
			XamlUserType xamlUserType127 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType127.AddMemberName("AbsolutePath");
			xamlUserType127.AddMemberName("AbsoluteUri");
			xamlUserType127.AddMemberName("Authority");
			xamlUserType127.AddMemberName("DnsSafeHost");
			xamlUserType127.AddMemberName("Fragment");
			xamlUserType127.AddMemberName("Host");
			xamlUserType127.AddMemberName("HostNameType");
			xamlUserType127.AddMemberName("IsAbsoluteUri");
			xamlUserType127.AddMemberName("IsDefaultPort");
			xamlUserType127.AddMemberName("IsFile");
			xamlUserType127.AddMemberName("IsLoopback");
			xamlUserType127.AddMemberName("IsUnc");
			xamlUserType127.AddMemberName("LocalPath");
			xamlUserType127.AddMemberName("OriginalString");
			xamlUserType127.AddMemberName("PathAndQuery");
			xamlUserType127.AddMemberName("Port");
			xamlUserType127.AddMemberName("Query");
			xamlUserType127.AddMemberName("Scheme");
			xamlUserType127.AddMemberName("Segments");
			xamlUserType127.AddMemberName("UserEscaped");
			xamlUserType127.AddMemberName("UserInfo");
			result = xamlUserType127;
			break;
		}
		case 89:
		{
			XamlUserType xamlUserType126 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Enum"));
			xamlUserType126.AddEnumValue("Off", VineToggleButtonState.Off);
			xamlUserType126.AddEnumValue("On", VineToggleButtonState.On);
			xamlUserType126.AddEnumValue("Disabled", VineToggleButtonState.Disabled);
			xamlUserType126.AddEnumValue("NotFollowing", VineToggleButtonState.NotFollowing);
			xamlUserType126.AddEnumValue("Following", VineToggleButtonState.Following);
			xamlUserType126.AddEnumValue("FollowRequested", VineToggleButtonState.FollowRequested);
			xamlUserType126.SetIsLocalType();
			result = xamlUserType126;
			break;
		}
		case 90:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 91:
		{
			XamlUserType xamlUserType125 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType125.Activator = Activate_91_VineModel;
			xamlUserType125.AddMemberName("UserId");
			xamlUserType125.AddMemberName("UserName");
			xamlUserType125.AddMemberName("AvatarUrl");
			xamlUserType125.AddMemberName("ProfileBackground");
			xamlUserType125.AddMemberName("PostId");
			xamlUserType125.AddMemberName("Liked");
			xamlUserType125.AddMemberName("Private");
			xamlUserType125.AddMemberName("MyRepostId");
			xamlUserType125.AddMemberName("ThumbnailUrl");
			xamlUserType125.AddMemberName("VideoUrl");
			xamlUserType125.AddMemberName("VideoLowUrl");
			xamlUserType125.AddMemberName("ShareUrl");
			xamlUserType125.AddMemberName("Description");
			xamlUserType125.AddMemberName("PermalinkUrl");
			xamlUserType125.AddMemberName("VenueName");
			xamlUserType125.AddMemberName("Created");
			xamlUserType125.AddMemberName("Repost");
			xamlUserType125.AddMemberName("Likes");
			xamlUserType125.AddMemberName("Reposts");
			xamlUserType125.AddMemberName("Comments");
			xamlUserType125.AddMemberName("Loops");
			xamlUserType125.AddMemberName("HasSimilarPosts");
			xamlUserType125.AddMemberName("AudioTracks");
			xamlUserType125.AddMemberName("Entities");
			xamlUserType125.AddMemberName("VineUrl");
			xamlUserType125.AddMemberName("Reference");
			xamlUserType125.AddMemberName("MosaicType");
			xamlUserType125.AddMemberName("Title");
			xamlUserType125.AddMemberName("Records");
			xamlUserType125.AddMemberName("Link");
			xamlUserType125.AddMemberName("LinkPath");
			xamlUserType125.AddMemberName("Type");
			xamlUserType125.AddMemberName("Page");
			xamlUserType125.AddMemberName("Size");
			xamlUserType125.AddMemberName("RecordType");
			xamlUserType125.AddMemberName("ParsedMosaicType");
			xamlUserType125.SetIsLocalType();
			result = xamlUserType125;
			break;
		}
		case 92:
		{
			XamlUserType xamlUserType124 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.ValueType"));
			xamlUserType124.SetIsReturnTypeStub();
			result = xamlUserType124;
			break;
		}
		case 93:
		{
			XamlUserType xamlUserType123 = new XamlUserType(this, fullName, type, null);
			xamlUserType123.SetIsReturnTypeStub();
			result = xamlUserType123;
			break;
		}
		case 94:
		{
			XamlUserType xamlUserType122 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Enum"));
			xamlUserType122.AddEnumValue("Posts", VineListControl.Tab.Posts);
			xamlUserType122.AddEnumValue("Likes", VineListControl.Tab.Likes);
			xamlUserType122.SetIsLocalType();
			result = xamlUserType122;
			break;
		}
		case 95:
		{
			XamlUserType xamlUserType121 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType121.SetIsReturnTypeStub();
			xamlUserType121.SetIsLocalType();
			result = xamlUserType121;
			break;
		}
		case 96:
		{
			XamlUserType xamlUserType120 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType120.CollectionAdd = VectorAdd_96_List;
			xamlUserType120.SetIsReturnTypeStub();
			result = xamlUserType120;
			break;
		}
		case 97:
		{
			XamlUserType xamlUserType119 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType119.SetIsReturnTypeStub();
			xamlUserType119.SetIsLocalType();
			result = xamlUserType119;
			break;
		}
		case 98:
		{
			XamlUserType xamlUserType118 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType118.SetIsReturnTypeStub();
			xamlUserType118.SetIsLocalType();
			result = xamlUserType118;
			break;
		}
		case 99:
		{
			XamlUserType xamlUserType117 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType117.SetIsReturnTypeStub();
			xamlUserType117.SetIsLocalType();
			result = xamlUserType117;
			break;
		}
		case 100:
		{
			XamlUserType xamlUserType116 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType116.CollectionAdd = VectorAdd_100_List;
			xamlUserType116.SetIsReturnTypeStub();
			result = xamlUserType116;
			break;
		}
		case 101:
		{
			XamlUserType xamlUserType115 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType115.Activator = Activate_101_AudioTracks;
			xamlUserType115.AddMemberName("Track");
			xamlUserType115.SetIsLocalType();
			result = xamlUserType115;
			break;
		}
		case 102:
		{
			XamlUserType xamlUserType114 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType114.SetIsReturnTypeStub();
			xamlUserType114.SetIsLocalType();
			result = xamlUserType114;
			break;
		}
		case 103:
		{
			XamlUserType xamlUserType113 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType113.CollectionAdd = VectorAdd_103_List;
			xamlUserType113.SetIsReturnTypeStub();
			result = xamlUserType113;
			break;
		}
		case 104:
		{
			XamlUserType xamlUserType112 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Enum"));
			xamlUserType112.AddEnumValue("Unknown", RecordType.Unknown);
			xamlUserType112.AddEnumValue("Post", RecordType.Post);
			xamlUserType112.AddEnumValue("PostMosaic", RecordType.PostMosaic);
			xamlUserType112.AddEnumValue("UrlAction", RecordType.UrlAction);
			xamlUserType112.AddEnumValue("UserMosaic", RecordType.UserMosaic);
			xamlUserType112.SetIsLocalType();
			result = xamlUserType112;
			break;
		}
		case 105:
		{
			XamlUserType xamlUserType111 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Enum"));
			xamlUserType111.AddEnumValue("Unknown", MosaicType.Unknown);
			xamlUserType111.AddEnumValue("Default", MosaicType.Default);
			xamlUserType111.AddEnumValue("AvatarIncluded", MosaicType.AvatarIncluded);
			xamlUserType111.SetIsLocalType();
			result = xamlUserType111;
			break;
		}
		case 106:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 107:
		{
			XamlUserType xamlUserType110 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType110.Activator = Activate_107_ChannelVineListView;
			xamlUserType110.AddMemberName("FeaturedPivotHeaderBrush");
			xamlUserType110.AddMemberName("RecentPivotHeaderBrush");
			xamlUserType110.AddMemberName("ChannelBrush");
			xamlUserType110.AddMemberName("PageTitle");
			xamlUserType110.AddMemberName("IsBusy");
			xamlUserType110.AddMemberName("FeaturedBrush");
			xamlUserType110.AddMemberName("RecentBrush");
			xamlUserType110.AddMemberName("Model");
			xamlUserType110.AddMemberName("MuteIcon");
			xamlUserType110.AddMemberName("MuteLabel");
			xamlUserType110.SetIsLocalType();
			result = xamlUserType110;
			break;
		}
		case 108:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 109:
		{
			XamlUserType xamlUserType109 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType109.Activator = Activate_109_CommentsView;
			xamlUserType109.AddMemberName("KeyboardHeight");
			xamlUserType109.AddMemberName("IsBusy");
			xamlUserType109.AddMemberName("IsBusyPosting");
			xamlUserType109.AddMemberName("HasError");
			xamlUserType109.AddMemberName("ErrorText");
			xamlUserType109.AddMemberName("ShowRetry");
			xamlUserType109.AddMemberName("IsEmpty");
			xamlUserType109.AddMemberName("SendEnabled");
			xamlUserType109.AddMemberName("IsFocusedByDefault");
			xamlUserType109.AddMemberName("TextInput");
			xamlUserType109.AddMemberName("CharsLeft");
			xamlUserType109.AddMemberName("ScrollOffset");
			xamlUserType109.AddMemberName("IsFinishedLoading");
			xamlUserType109.AddMemberName("Items");
			xamlUserType109.AddMemberName("PostId");
			xamlUserType109.AddMemberName("Section");
			xamlUserType109.AddMemberName("IsCommenting");
			xamlUserType109.AddMemberName("TagBarVisibility");
			xamlUserType109.AddMemberName("AutoCompleteList");
			xamlUserType109.AddMemberName("IsAutoCompleteListOpen");
			xamlUserType109.SetIsLocalType();
			result = xamlUserType109;
			break;
		}
		case 110:
		{
			XamlUserType xamlUserType108 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.CommentModel>"));
			xamlUserType108.CollectionAdd = VectorAdd_110_IncrementalLoadingCollection;
			xamlUserType108.SetIsReturnTypeStub();
			xamlUserType108.SetIsLocalType();
			result = xamlUserType108;
			break;
		}
		case 111:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Vine.Models.CommentModel>"))
			{
				Activator = Activate_111_ObservableCollection,
				CollectionAdd = VectorAdd_111_ObservableCollection
			};
			break;
		case 112:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_112_Collection,
				CollectionAdd = VectorAdd_112_Collection
			};
			break;
		case 113:
		{
			XamlUserType xamlUserType107 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType107.Activator = Activate_113_CommentModel;
			xamlUserType107.AddMemberName("IsUserComment");
			xamlUserType107.AddMemberName("Comment");
			xamlUserType107.AddMemberName("Entities");
			xamlUserType107.AddMemberName("User");
			xamlUserType107.AddMemberName("PostId");
			xamlUserType107.AddMemberName("CommentId");
			xamlUserType107.AddMemberName("Created");
			xamlUserType107.AddMemberName("CreatedText");
			xamlUserType107.AddMemberName("RichBody");
			xamlUserType107.SetIsLocalType();
			result = xamlUserType107;
			break;
		}
		case 114:
		{
			XamlUserType xamlUserType106 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType106.Activator = Activate_114_CaptureView8;
			xamlUserType106.AddMemberName("CurrentTutorialState");
			xamlUserType106.AddMemberName("TutorialHintVisibility");
			xamlUserType106.AddMemberName("TutorialWelcomeVisibility");
			xamlUserType106.AddMemberName("TutorialMessage");
			xamlUserType106.AddMemberName("ButtonTutorialCameraToolsVisibility");
			xamlUserType106.AddMemberName("ButtonTutorialUndoVisibility");
			xamlUserType106.AddMemberName("ButtonTutorialGrabVideoVisibility");
			xamlUserType106.AddMemberName("ButtonTutorialState");
			xamlUserType106.AddMemberName("NextButtonVisibility");
			xamlUserType106.AddMemberName("IsBusy");
			xamlUserType106.AddMemberName("IsTorchSupported");
			xamlUserType106.AddMemberName("IsFrontCameraSupported");
			xamlUserType106.AddMemberName("IsFocusSupported");
			xamlUserType106.AddMemberName("IsFocusLocked");
			xamlUserType106.AddMemberName("IsGhostModeHighlighted");
			xamlUserType106.AddMemberName("IsGridHighlighted");
			xamlUserType106.AddMemberName("IsExpanded");
			xamlUserType106.AddMemberName("IsGridVisible");
			xamlUserType106.AddMemberName("IsUndoHighlighted");
			xamlUserType106.AddMemberName("IsCameraHighlighted");
			xamlUserType106.AddMemberName("IsTorchHighlighted");
			xamlUserType106.AddMemberName("IsFocusModeHighlighted");
			xamlUserType106.AddMemberName("PendingChanges");
			xamlUserType106.AddMemberName("RecordingDraftCount");
			xamlUserType106.AddMemberName("DraftNumber");
			xamlUserType106.AddMemberName("IsDraftsEnabled");
			xamlUserType106.AddMemberName("IsUndoEnabled");
			xamlUserType106.AddMemberName("GhostImageSource");
			xamlUserType106.AddMemberName("FocusButtonBrush");
			xamlUserType106.AddMemberName("GhostButtonBrush");
			xamlUserType106.AddMemberName("GridButtonBrush");
			xamlUserType106.AddMemberName("CameraButtonBrush");
			xamlUserType106.AddMemberName("WrenchBrush");
			xamlUserType106.AddMemberName("TorchButtonBrush");
			xamlUserType106.AddMemberName("MediaCapture");
			xamlUserType106.AddMemberName("VMParameters");
			xamlUserType106.SetIsLocalType();
			result = xamlUserType106;
			break;
		}
		case 115:
		{
			XamlUserType xamlUserType105 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Enum"));
			xamlUserType105.AddEnumValue("Welcome", CaptureView8.TutorialState.Welcome);
			xamlUserType105.AddEnumValue("FirstCapture", CaptureView8.TutorialState.FirstCapture);
			xamlUserType105.AddEnumValue("FirstCaptureRunning", CaptureView8.TutorialState.FirstCaptureRunning);
			xamlUserType105.AddEnumValue("FirstCaptureDone", CaptureView8.TutorialState.FirstCaptureDone);
			xamlUserType105.AddEnumValue("SecondCapture", CaptureView8.TutorialState.SecondCapture);
			xamlUserType105.AddEnumValue("SecondCaptureRunning", CaptureView8.TutorialState.SecondCaptureRunning);
			xamlUserType105.AddEnumValue("SecondCaptureDone", CaptureView8.TutorialState.SecondCaptureDone);
			xamlUserType105.AddEnumValue("ThirdCapture", CaptureView8.TutorialState.ThirdCapture);
			xamlUserType105.AddEnumValue("ThirdCaptureRunning", CaptureView8.TutorialState.ThirdCaptureRunning);
			xamlUserType105.AddEnumValue("ThirdCaptureDone", CaptureView8.TutorialState.ThirdCaptureDone);
			xamlUserType105.AddEnumValue("Encoding", CaptureView8.TutorialState.Encoding);
			xamlUserType105.AddEnumValue("FinalMessage", CaptureView8.TutorialState.FinalMessage);
			xamlUserType105.AddEnumValue("NotRunning", CaptureView8.TutorialState.NotRunning);
			xamlUserType105.SetIsLocalType();
			result = xamlUserType105;
			break;
		}
		case 116:
		{
			XamlUserType xamlUserType104 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Enum"));
			xamlUserType104.AddEnumValue("CameraRoll", CaptureView8.ButtonsTutorialEnum.CameraRoll);
			xamlUserType104.AddEnumValue("Undo", CaptureView8.ButtonsTutorialEnum.Undo);
			xamlUserType104.AddEnumValue("Delete", CaptureView8.ButtonsTutorialEnum.Delete);
			xamlUserType104.AddEnumValue("Tools", CaptureView8.ButtonsTutorialEnum.Tools);
			xamlUserType104.AddEnumValue("Done", CaptureView8.ButtonsTutorialEnum.Done);
			xamlUserType104.SetIsLocalType();
			result = xamlUserType104;
			break;
		}
		case 117:
		{
			XamlUserType xamlUserType103 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BaseUserControl"));
			xamlUserType103.Activator = Activate_117_PullToRefreshListControl;
			xamlUserType103.AddMemberName("DefaultPadding");
			xamlUserType103.AddMemberName("ListView");
			xamlUserType103.AddMemberName("ScrollViewer");
			xamlUserType103.SetIsLocalType();
			result = xamlUserType103;
			break;
		}
		case 118:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 119:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 120:
		{
			XamlUserType xamlUserType102 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BaseUserControl"));
			xamlUserType102.Activator = Activate_120_ConversationList;
			xamlUserType102.AddMemberName("Items");
			xamlUserType102.AddMemberName("ItemWidth");
			xamlUserType102.AddMemberName("IsInbox");
			xamlUserType102.AddMemberName("IsBusy");
			xamlUserType102.AddMemberName("IsEmpty");
			xamlUserType102.AddMemberName("HasError");
			xamlUserType102.AddMemberName("ErrorText");
			xamlUserType102.AddMemberName("ShowRetry");
			xamlUserType102.AddMemberName("HasNew");
			xamlUserType102.AddMemberName("IsFinishedLoading");
			xamlUserType102.AddMemberName("EmptyIcon");
			xamlUserType102.AddMemberName("EmptyHeader");
			xamlUserType102.AddMemberName("EmptyMessage");
			xamlUserType102.SetIsLocalType();
			result = xamlUserType102;
			break;
		}
		case 121:
		{
			XamlUserType xamlUserType101 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.ConversationViewModel>"));
			xamlUserType101.CollectionAdd = VectorAdd_121_IncrementalLoadingCollection;
			xamlUserType101.SetIsReturnTypeStub();
			xamlUserType101.SetIsLocalType();
			result = xamlUserType101;
			break;
		}
		case 122:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Vine.Models.ConversationViewModel>"))
			{
				Activator = Activate_122_ObservableCollection,
				CollectionAdd = VectorAdd_122_ObservableCollection
			};
			break;
		case 123:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_123_Collection,
				CollectionAdd = VectorAdd_123_Collection
			};
			break;
		case 124:
		{
			XamlUserType xamlUserType100 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType100.Activator = Activate_124_ConversationViewModel;
			xamlUserType100.AddMemberName("OtherUser");
			xamlUserType100.AddMemberName("Record");
			xamlUserType100.AddMemberName("DeletedMsgIds");
			xamlUserType100.AddMemberName("CurrentUser");
			xamlUserType100.AddMemberName("LastMessageDateDisplay");
			xamlUserType100.AddMemberName("CurrentUserBrush");
			xamlUserType100.AddMemberName("CurrentUserLightBrush");
			xamlUserType100.AddMemberName("OtherUserBrush");
			xamlUserType100.AddMemberName("OtherUserLightBrush");
			xamlUserType100.SetIsLocalType();
			result = xamlUserType100;
			break;
		}
		case 125:
		{
			XamlUserType xamlUserType99 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Models.NotifyObject"));
			xamlUserType99.SetIsReturnTypeStub();
			xamlUserType99.SetIsLocalType();
			result = xamlUserType99;
			break;
		}
		case 126:
		{
			XamlUserType xamlUserType98 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType98.CollectionAdd = VectorAdd_126_List;
			xamlUserType98.SetIsReturnTypeStub();
			result = xamlUserType98;
			break;
		}
		case 127:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 128:
		{
			XamlUserType xamlUserType97 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.Page"));
			xamlUserType97.Activator = Activate_128_InteractionsTemplate;
			xamlUserType97.SetIsLocalType();
			result = xamlUserType97;
			break;
		}
		case 129:
		{
			XamlUserType xamlUserType96 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.Page"));
			xamlUserType96.Activator = Activate_129_MilestoneNotificationDesign;
			xamlUserType96.SetIsLocalType();
			result = xamlUserType96;
			break;
		}
		case 130:
		{
			XamlUserType xamlUserType95 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.Page"));
			xamlUserType95.Activator = Activate_130_TimelineTemplate1;
			xamlUserType95.SetIsLocalType();
			result = xamlUserType95;
			break;
		}
		case 131:
		{
			XamlUserType xamlUserType94 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.Page"));
			xamlUserType94.Activator = Activate_131_TimelineTemplate2;
			xamlUserType94.SetIsLocalType();
			result = xamlUserType94;
			break;
		}
		case 132:
		{
			XamlUserType xamlUserType93 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType93.Activator = Activate_132_CaptchaView;
			xamlUserType93.AddMemberName("IsFinishedLoading");
			xamlUserType93.AddMemberName("IsLoading");
			xamlUserType93.AddMemberName("HasError");
			xamlUserType93.SetIsLocalType();
			result = xamlUserType93;
			break;
		}
		case 133:
		{
			XamlUserType xamlUserType92 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.Page"));
			xamlUserType92.Activator = Activate_133_VMTemplate1;
			xamlUserType92.SetIsLocalType();
			result = xamlUserType92;
			break;
		}
		case 134:
		{
			XamlUserType xamlUserType91 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.Page"));
			xamlUserType91.Activator = Activate_134_VMTemplate2;
			xamlUserType91.SetIsLocalType();
			result = xamlUserType91;
			break;
		}
		case 135:
		{
			XamlUserType xamlUserType90 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.DataTemplateSelector"));
			xamlUserType90.Activator = Activate_135_FriendFinderTemplateSelector;
			xamlUserType90.AddMemberName("HeaderTemplate");
			xamlUserType90.AddMemberName("UserTemplate");
			xamlUserType90.SetIsLocalType();
			result = xamlUserType90;
			break;
		}
		case 136:
		{
			XamlUserType xamlUserType89 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.Button"));
			xamlUserType89.Activator = Activate_136_VineToggleButton;
			xamlUserType89.AddMemberName("State");
			xamlUserType89.AddMemberName("FollowingVisual");
			xamlUserType89.AddMemberName("NotFollowingVisual");
			xamlUserType89.AddMemberName("FollowRequestedVisual");
			xamlUserType89.AddMemberName("ActiveVisual");
			xamlUserType89.AddMemberName("OnVisual");
			xamlUserType89.AddMemberName("OffVisual");
			xamlUserType89.AddMemberName("DisabledVisual");
			xamlUserType89.SetIsLocalType();
			result = xamlUserType89;
			break;
		}
		case 137:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 138:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 139:
		{
			XamlUserType xamlUserType88 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType88.Activator = Activate_139_FriendFinderAllView;
			xamlUserType88.AddMemberName("IsBusy");
			xamlUserType88.AddMemberName("IsNux");
			xamlUserType88.AddMemberName("HasError");
			xamlUserType88.AddMemberName("ErrorText");
			xamlUserType88.AddMemberName("ShowRetry");
			xamlUserType88.AddMemberName("Items");
			xamlUserType88.AddMemberName("ListSource");
			xamlUserType88.AddMemberName("HeaderText");
			xamlUserType88.AddMemberName("IsSuggestedVisible");
			xamlUserType88.AddMemberName("IsScrollViewVisible");
			xamlUserType88.AddMemberName("IsFinishedLoading");
			xamlUserType88.SetIsLocalType();
			result = xamlUserType88;
			break;
		}
		case 140:
		{
			XamlUserType xamlUserType87 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Vine.Models.FriendFinderModel>"));
			xamlUserType87.CollectionAdd = VectorAdd_140_ObservableCollection;
			xamlUserType87.SetIsReturnTypeStub();
			result = xamlUserType87;
			break;
		}
		case 141:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_141_Collection,
				CollectionAdd = VectorAdd_141_Collection
			};
			break;
		case 142:
		{
			XamlUserType xamlUserType86 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType86.Activator = Activate_142_FriendFinderModel;
			xamlUserType86.AddMemberName("Source");
			xamlUserType86.AddMemberName("HeaderText");
			xamlUserType86.AddMemberName("SeeAllVisible");
			xamlUserType86.AddMemberName("VineUserModel");
			xamlUserType86.AddMemberName("IsHeader");
			xamlUserType86.AddMemberName("IsUser");
			xamlUserType86.SetIsLocalType();
			result = xamlUserType86;
			break;
		}
		case 143:
		{
			XamlUserType xamlUserType85 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Enum"));
			xamlUserType85.AddEnumValue("Contacts", FriendFinderListSource.Contacts);
			xamlUserType85.AddEnumValue("Twitter", FriendFinderListSource.Twitter);
			xamlUserType85.AddEnumValue("Suggested", FriendFinderListSource.Suggested);
			xamlUserType85.SetIsLocalType();
			result = xamlUserType85;
			break;
		}
		case 144:
		{
			XamlUserType xamlUserType84 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType84.Activator = Activate_144_FriendFinderView;
			xamlUserType84.AddMemberName("IsBusy");
			xamlUserType84.AddMemberName("IsNux");
			xamlUserType84.AddMemberName("PlaceholderVisibility");
			xamlUserType84.AddMemberName("TwitterEnabled");
			xamlUserType84.AddMemberName("ContactsEnabled");
			xamlUserType84.AddMemberName("ConnectAccountsVisble");
			xamlUserType84.AddMemberName("HasError");
			xamlUserType84.AddMemberName("ErrorText");
			xamlUserType84.AddMemberName("ShowRetry");
			xamlUserType84.AddMemberName("IsFinishedLoading");
			xamlUserType84.AddMemberName("Items");
			xamlUserType84.SetIsLocalType();
			result = xamlUserType84;
			break;
		}
		case 145:
		{
			XamlUserType xamlUserType83 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.DataTemplateSelector"));
			xamlUserType83.Activator = Activate_145_SearchResultTemplateSelector;
			xamlUserType83.AddMemberName("HeaderTemplate");
			xamlUserType83.AddMemberName("SuggestedSearchTermTemplate");
			xamlUserType83.AddMemberName("UserTemplate");
			xamlUserType83.AddMemberName("TagTemplate");
			xamlUserType83.AddMemberName("RecentTemplate");
			xamlUserType83.AddMemberName("VineTemplate");
			xamlUserType83.SetIsLocalType();
			result = xamlUserType83;
			break;
		}
		case 146:
		{
			XamlUserType xamlUserType82 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BaseUserControl"));
			xamlUserType82.Activator = Activate_146_SearchControl;
			xamlUserType82.AddMemberName("ControlWrapper");
			xamlUserType82.AddMemberName("SearchResults");
			xamlUserType82.AddMemberName("Pending");
			xamlUserType82.AddMemberName("RecentSearches");
			xamlUserType82.AddMemberName("IsBusy");
			xamlUserType82.AddMemberName("IsEmpty");
			xamlUserType82.AddMemberName("HasError");
			xamlUserType82.AddMemberName("ShowError");
			xamlUserType82.AddMemberName("ShowRetry");
			xamlUserType82.AddMemberName("ErrorText");
			xamlUserType82.AddMemberName("SearchListVisible");
			xamlUserType82.AddMemberName("SearchText");
			xamlUserType82.AddMemberName("LastSearchText");
			xamlUserType82.AddMemberName("PlaceHolderLabel");
			xamlUserType82.AddMemberName("EmptyText");
			xamlUserType82.AddMemberName("TileText");
			xamlUserType82.AddMemberName("HasTile");
			xamlUserType82.SetIsLocalType();
			result = xamlUserType82;
			break;
		}
		case 147:
		{
			XamlUserType xamlUserType81 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Models.NotifyObject"));
			xamlUserType81.SetIsReturnTypeStub();
			xamlUserType81.SetIsLocalType();
			result = xamlUserType81;
			break;
		}
		case 148:
		{
			XamlUserType xamlUserType80 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Vine.Models.SearchResultModel>"));
			xamlUserType80.CollectionAdd = VectorAdd_148_ObservableCollection;
			xamlUserType80.SetIsReturnTypeStub();
			result = xamlUserType80;
			break;
		}
		case 149:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_149_Collection,
				CollectionAdd = VectorAdd_149_Collection
			};
			break;
		case 150:
		{
			XamlUserType xamlUserType79 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType79.Activator = Activate_150_SearchResultModel;
			xamlUserType79.AddMemberName("Suggestion");
			xamlUserType79.AddMemberName("User");
			xamlUserType79.AddMemberName("Tag");
			xamlUserType79.AddMemberName("Recent");
			xamlUserType79.AddMemberName("HeaderViewAllVisible");
			xamlUserType79.AddMemberName("HeaderText");
			xamlUserType79.AddMemberName("SearchType");
			xamlUserType79.AddMemberName("Vine");
			xamlUserType79.AddMemberName("IsSearchSuggestion");
			xamlUserType79.AddMemberName("IsHeader");
			xamlUserType79.AddMemberName("IsUser");
			xamlUserType79.AddMemberName("IsTag");
			xamlUserType79.AddMemberName("IsVine");
			xamlUserType79.AddMemberName("IsRecent");
			xamlUserType79.SetIsLocalType();
			result = xamlUserType79;
			break;
		}
		case 151:
		{
			XamlUserType xamlUserType78 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType78.SetIsReturnTypeStub();
			xamlUserType78.SetIsLocalType();
			result = xamlUserType78;
			break;
		}
		case 152:
		{
			XamlUserType xamlUserType77 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Models.NotifyObject"));
			xamlUserType77.Activator = Activate_152_VineTagModel;
			xamlUserType77.AddMemberName("TagId");
			xamlUserType77.AddMemberName("Tag");
			xamlUserType77.AddMemberName("PostCount");
			xamlUserType77.AddMemberName("FormattedTag");
			xamlUserType77.AddMemberName("FormattedPostCount");
			xamlUserType77.SetIsLocalType();
			result = xamlUserType77;
			break;
		}
		case 153:
		{
			XamlUserType xamlUserType76 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType76.Activator = Activate_153_VineRecentSearch;
			xamlUserType76.AddMemberName("Query");
			xamlUserType76.SetIsLocalType();
			result = xamlUserType76;
			break;
		}
		case 154:
		{
			XamlUserType xamlUserType75 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Enum"));
			xamlUserType75.AddEnumValue("Posts", SearchType.Posts);
			xamlUserType75.AddEnumValue("People", SearchType.People);
			xamlUserType75.AddEnumValue("Tags", SearchType.Tags);
			xamlUserType75.SetIsLocalType();
			result = xamlUserType75;
			break;
		}
		case 155:
		{
			XamlUserType xamlUserType74 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType74.SetIsReturnTypeStub();
			result = xamlUserType74;
			break;
		}
		case 156:
		{
			XamlUserType xamlUserType73 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Vine.Models.VineRecentSearch>"));
			xamlUserType73.CollectionAdd = VectorAdd_156_ObservableCollection;
			xamlUserType73.SetIsReturnTypeStub();
			result = xamlUserType73;
			break;
		}
		case 157:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_157_Collection,
				CollectionAdd = VectorAdd_157_Collection
			};
			break;
		case 158:
		{
			XamlUserType xamlUserType72 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType72.Activator = Activate_158_SearchTagsAllView;
			xamlUserType72.AddMemberName("Items");
			xamlUserType72.AddMemberName("IsFinishedLoading");
			xamlUserType72.AddMemberName("IsEmpty");
			xamlUserType72.AddMemberName("ShowRetry");
			xamlUserType72.AddMemberName("HasError");
			xamlUserType72.AddMemberName("PageTitle");
			xamlUserType72.AddMemberName("IsBusy");
			xamlUserType72.AddMemberName("EmptyText");
			xamlUserType72.AddMemberName("ErrorText");
			xamlUserType72.SetIsLocalType();
			result = xamlUserType72;
			break;
		}
		case 159:
		{
			XamlUserType xamlUserType71 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.VineTagModel>"));
			xamlUserType71.CollectionAdd = VectorAdd_159_IncrementalLoadingCollection;
			xamlUserType71.SetIsReturnTypeStub();
			xamlUserType71.SetIsLocalType();
			result = xamlUserType71;
			break;
		}
		case 160:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Vine.Models.VineTagModel>"))
			{
				Activator = Activate_160_ObservableCollection,
				CollectionAdd = VectorAdd_160_ObservableCollection
			};
			break;
		case 161:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_161_Collection,
				CollectionAdd = VectorAdd_161_Collection
			};
			break;
		case 162:
		{
			XamlUserType xamlUserType70 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType70.Activator = Activate_162_SettingsPrivacyView;
			xamlUserType70.AddMemberName("User");
			xamlUserType70.AddMemberName("IsError");
			xamlUserType70.AddMemberName("ErrorText");
			xamlUserType70.AddMemberName("ShowRetry");
			xamlUserType70.AddMemberName("IsLoaded");
			xamlUserType70.SetIsLocalType();
			result = xamlUserType70;
			break;
		}
		case 163:
		{
			XamlUserType xamlUserType69 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.DataTemplateSelector"));
			xamlUserType69.Activator = Activate_163_ContactTemplateSelector;
			xamlUserType69.AddMemberName("ContactTemplate");
			xamlUserType69.AddMemberName("VineUserTemplate");
			xamlUserType69.AddMemberName("HeaderTemplate");
			xamlUserType69.SetIsLocalType();
			result = xamlUserType69;
			break;
		}
		case 164:
		{
			XamlUserType xamlUserType68 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BaseUserControl"));
			xamlUserType68.Activator = Activate_164_ShareMessageControl;
			xamlUserType68.AddMemberName("SelectedItems");
			xamlUserType68.AddMemberName("SearchText");
			xamlUserType68.AddMemberName("IsPplFinishedLoading");
			xamlUserType68.AddMemberName("IsSingleSelect");
			xamlUserType68.AddMemberName("PplHasError");
			xamlUserType68.AddMemberName("PplIsEmpty");
			xamlUserType68.AddMemberName("Pending");
			xamlUserType68.AddMemberName("Friends");
			xamlUserType68.AddMemberName("Contacts");
			xamlUserType68.AddMemberName("IsFriendsEmpty");
			xamlUserType68.AddMemberName("IsContactsEmpty");
			xamlUserType68.AddMemberName("FriendsEmptyVisibility");
			xamlUserType68.AddMemberName("ContactsEmptyVisibility");
			xamlUserType68.AddMemberName("IsBusy");
			xamlUserType68.AddMemberName("HasError");
			xamlUserType68.AddMemberName("ErrorText");
			xamlUserType68.AddMemberName("ShowRetry");
			xamlUserType68.AddMemberName("ErrorVisibility");
			xamlUserType68.AddMemberName("IsFinishedLoading");
			xamlUserType68.SetIsLocalType();
			result = xamlUserType68;
			break;
		}
		case 165:
		{
			XamlUserType xamlUserType67 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType67.CollectionAdd = VectorAdd_165_List;
			xamlUserType67.SetIsReturnTypeStub();
			result = xamlUserType67;
			break;
		}
		case 166:
		{
			XamlUserType xamlUserType66 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Models.NotifyObject"));
			xamlUserType66.Activator = Activate_166_VineContactViewModel;
			xamlUserType66.AddMemberName("Section");
			xamlUserType66.AddMemberName("Emails");
			xamlUserType66.AddMemberName("Phones");
			xamlUserType66.AddMemberName("SelectionVisibility");
			xamlUserType66.AddMemberName("User");
			xamlUserType66.AddMemberName("UserVisibility");
			xamlUserType66.AddMemberName("PhoneSelectionVisibility");
			xamlUserType66.AddMemberName("PhoneVisibility");
			xamlUserType66.AddMemberName("EmailSelectionVisibility");
			xamlUserType66.AddMemberName("EmailVisibility");
			xamlUserType66.AddMemberName("HeaderText");
			xamlUserType66.AddMemberName("IsSelected");
			xamlUserType66.SetIsLocalType();
			result = xamlUserType66;
			break;
		}
		case 167:
		{
			XamlUserType xamlUserType65 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType65.CollectionAdd = VectorAdd_167_List;
			xamlUserType65.SetIsReturnTypeStub();
			result = xamlUserType65;
			break;
		}
		case 168:
		{
			XamlUserType xamlUserType64 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType64.AddMemberName("Item1");
			xamlUserType64.AddMemberName("Item2");
			result = xamlUserType64;
			break;
		}
		case 169:
		{
			XamlUserType xamlUserType63 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.VineContactViewModel>"));
			xamlUserType63.CollectionAdd = VectorAdd_169_IncrementalLoadingCollection;
			xamlUserType63.SetIsReturnTypeStub();
			xamlUserType63.SetIsLocalType();
			result = xamlUserType63;
			break;
		}
		case 170:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Vine.Models.VineContactViewModel>"))
			{
				Activator = Activate_170_ObservableCollection,
				CollectionAdd = VectorAdd_170_ObservableCollection
			};
			break;
		case 171:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_171_Collection,
				CollectionAdd = VectorAdd_171_Collection
			};
			break;
		case 172:
		{
			XamlUserType xamlUserType62 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType62.Activator = Activate_172_ShareMessageView;
			xamlUserType62.AddMemberName("TitleText");
			xamlUserType62.AddMemberName("TutorialHintVisibility");
			xamlUserType62.AddMemberName("HeaderBrush");
			xamlUserType62.SetIsLocalType();
			result = xamlUserType62;
			break;
		}
		case 173:
		{
			XamlUserType xamlUserType61 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.ContentControl"));
			xamlUserType61.Activator = Activate_173_TappedToLikeControl;
			xamlUserType61.AddMemberName("ZRotation");
			xamlUserType61.SetIsLocalType();
			result = xamlUserType61;
			break;
		}
		case 174:
		{
			XamlUserType xamlUserType60 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType60.Activator = Activate_174_UpgradeView;
			xamlUserType60.AddMemberName("IsBusy");
			xamlUserType60.SetIsLocalType();
			result = xamlUserType60;
			break;
		}
		case 175:
		{
			XamlUserType xamlUserType59 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType59.Activator = Activate_175_VerifyPhoneCodeEnterView;
			xamlUserType59.AddMemberName("IsBusy");
			xamlUserType59.AddMemberName("RetryText");
			xamlUserType59.AddMemberName("User");
			xamlUserType59.AddMemberName("HeaderText");
			xamlUserType59.SetIsLocalType();
			result = xamlUserType59;
			break;
		}
		case 176:
		{
			XamlUserType xamlUserType58 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType58.Activator = Activate_176_VerifyEmailCodeEnterView;
			xamlUserType58.AddMemberName("IsBusy");
			xamlUserType58.AddMemberName("RetryText");
			xamlUserType58.AddMemberName("User");
			xamlUserType58.AddMemberName("HeaderText");
			xamlUserType58.SetIsLocalType();
			result = xamlUserType58;
			break;
		}
		case 177:
		{
			XamlUserType xamlUserType57 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BaseUserControl"));
			xamlUserType57.Activator = Activate_177_ExploreControl;
			xamlUserType57.AddMemberName("IsFinishedLoading");
			xamlUserType57.AddMemberName("IsLoading");
			xamlUserType57.AddMemberName("HasError");
			xamlUserType57.AddMemberName("BrowserVisible");
			xamlUserType57.AddMemberName("SearchActive");
			xamlUserType57.AddMemberName("ErrorText");
			xamlUserType57.AddMemberName("ShowRetry");
			xamlUserType57.SetIsLocalType();
			result = xamlUserType57;
			break;
		}
		case 178:
		{
			XamlUserType xamlUserType56 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.UserControl"));
			xamlUserType56.Activator = Activate_178_TOSControl;
			xamlUserType56.SetIsLocalType();
			result = xamlUserType56;
			break;
		}
		case 179:
		{
			XamlUserType xamlUserType55 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.Behavior`1<Windows.UI.Xaml.Controls.PasswordBox>"));
			xamlUserType55.Activator = Activate_179_PasswordBoxUpdateBehavior;
			xamlUserType55.SetIsLocalType();
			result = xamlUserType55;
			break;
		}
		case 180:
		{
			XamlUserType xamlUserType54 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.DependencyObject"));
			xamlUserType54.AddMemberName("AssociatedObject");
			xamlUserType54.AddMemberName("Object");
			xamlUserType54.SetIsLocalType();
			result = xamlUserType54;
			break;
		}
		case 181:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 182:
		{
			XamlUserType xamlUserType53 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType53.Activator = Activate_182_SignUpEmailDetailsView;
			xamlUserType53.AddMemberName("User");
			xamlUserType53.AddMemberName("Email");
			xamlUserType53.AddMemberName("PhoneNumber");
			xamlUserType53.AddMemberName("Password");
			xamlUserType53.AddMemberName("IsBusy");
			xamlUserType53.AddMemberName("ErrorText");
			xamlUserType53.AddMemberName("IsError");
			xamlUserType53.AddMemberName("IsFinishedLoading");
			xamlUserType53.SetIsLocalType();
			result = xamlUserType53;
			break;
		}
		case 183:
		{
			XamlUserType xamlUserType52 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType52.Activator = Activate_183_SignUpEmailView;
			xamlUserType52.AddMemberName("User");
			xamlUserType52.AddMemberName("IsFinishedLoading");
			xamlUserType52.SetIsLocalType();
			result = xamlUserType52;
			break;
		}
		case 184:
		{
			XamlUserType xamlUserType51 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BaseUserControl"));
			xamlUserType51.Activator = Activate_184_VineMessagesInbox;
			xamlUserType51.AddMemberName("TutorialHintVisibility");
			xamlUserType51.AddMemberName("NewCount");
			xamlUserType51.AddMemberName("IsOtherInboxActive");
			xamlUserType51.SetIsLocalType();
			result = xamlUserType51;
			break;
		}
		case 185:
		{
			XamlUserType xamlUserType50 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType50.CollectionAdd = VectorAdd_185_List;
			xamlUserType50.SetIsReturnTypeStub();
			result = xamlUserType50;
			break;
		}
		case 186:
		{
			XamlUserType xamlUserType49 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType49.SetIsReturnTypeStub();
			xamlUserType49.SetIsLocalType();
			result = xamlUserType49;
			break;
		}
		case 187:
		{
			XamlUserType xamlUserType48 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Enum"));
			xamlUserType48.AddEnumValue("User", VineUserType.User);
			xamlUserType48.AddEnumValue("Contact", VineUserType.Contact);
			xamlUserType48.AddEnumValue("Phone", VineUserType.Phone);
			xamlUserType48.AddEnumValue("Email", VineUserType.Email);
			xamlUserType48.SetIsLocalType();
			result = xamlUserType48;
			break;
		}
		case 188:
		{
			XamlUserType xamlUserType47 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType47.CollectionAdd = VectorAdd_188_List;
			xamlUserType47.SetIsReturnTypeStub();
			result = xamlUserType47;
			break;
		}
		case 189:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 190:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Vine.Models.VineUserModel>"))
			{
				Activator = Activate_190_ObservableCollection,
				CollectionAdd = VectorAdd_190_ObservableCollection
			};
			break;
		case 191:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_191_Collection,
				CollectionAdd = VectorAdd_191_Collection
			};
			break;
		case 192:
		{
			XamlUserType xamlUserType46 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.NotifyPage"));
			xamlUserType46.Activator = Activate_192_InteractionsControl;
			xamlUserType46.AddMemberName("Items");
			xamlUserType46.AddMemberName("IsBusy");
			xamlUserType46.AddMemberName("IsEmpty");
			xamlUserType46.AddMemberName("HasError");
			xamlUserType46.AddMemberName("ErrorText");
			xamlUserType46.AddMemberName("ShowRetry");
			xamlUserType46.AddMemberName("NewCount");
			xamlUserType46.AddMemberName("IsFinishedLoading");
			xamlUserType46.AddMemberName("IsActive");
			xamlUserType46.SetIsLocalType();
			result = xamlUserType46;
			break;
		}
		case 193:
		{
			XamlUserType xamlUserType45 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.InteractionModel>"));
			xamlUserType45.CollectionAdd = VectorAdd_193_IncrementalLoadingCollection;
			xamlUserType45.SetIsReturnTypeStub();
			xamlUserType45.SetIsLocalType();
			result = xamlUserType45;
			break;
		}
		case 194:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Vine.Models.InteractionModel>"))
			{
				Activator = Activate_194_ObservableCollection,
				CollectionAdd = VectorAdd_194_ObservableCollection
			};
			break;
		case 195:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_195_Collection,
				CollectionAdd = VectorAdd_195_Collection
			};
			break;
		case 196:
		{
			XamlUserType xamlUserType44 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType44.Activator = Activate_196_InteractionModel;
			xamlUserType44.AddMemberName("Type");
			xamlUserType44.AddMemberName("InteractionType");
			xamlUserType44.AddMemberName("Milestone");
			xamlUserType44.AddMemberName("Body");
			xamlUserType44.AddMemberName("IsNew");
			xamlUserType44.AddMemberName("NotificationId");
			xamlUserType44.AddMemberName("ActivityId");
			xamlUserType44.AddMemberName("Id");
			xamlUserType44.AddMemberName("User");
			xamlUserType44.AddMemberName("Post");
			xamlUserType44.AddMemberName("UserId");
			xamlUserType44.AddMemberName("AvatarUrl");
			xamlUserType44.AddMemberName("NotificationTypeId");
			xamlUserType44.AddMemberName("FollowVisibility");
			xamlUserType44.AddMemberName("UserThumbnail");
			xamlUserType44.AddMemberName("MilestoneThumbUrl");
			xamlUserType44.AddMemberName("PostId");
			xamlUserType44.AddMemberName("ThumbnailUrl");
			xamlUserType44.AddMemberName("PostThumbnailUrl");
			xamlUserType44.AddMemberName("HasPost");
			xamlUserType44.AddMemberName("Created");
			xamlUserType44.AddMemberName("LastActivityTime");
			xamlUserType44.AddMemberName("RichBody");
			xamlUserType44.AddMemberName("GlyphBrush");
			xamlUserType44.AddMemberName("GlyphVisibility");
			xamlUserType44.AddMemberName("GlyphFollowedVisibility");
			xamlUserType44.AddMemberName("GlyphData");
			xamlUserType44.AddMemberName("HeartGlyphVisibility");
			xamlUserType44.AddMemberName("Entities");
			xamlUserType44.AddMemberName("ShortBodyEntities");
			xamlUserType44.AddMemberName("CommentTextEntities");
			xamlUserType44.AddMemberName("CreatedText");
			xamlUserType44.AddMemberName("HeaderText");
			xamlUserType44.AddMemberName("IsHeaderVisible");
			xamlUserType44.AddMemberName("ShortBody");
			xamlUserType44.AddMemberName("CommentText");
			xamlUserType44.AddMemberName("RichCommentText");
			xamlUserType44.SetIsLocalType();
			result = xamlUserType44;
			break;
		}
		case 197:
		{
			XamlUserType xamlUserType43 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Enum"));
			xamlUserType43.AddEnumValue("Unknown", InteractionType.Unknown);
			xamlUserType43.AddEnumValue("FollowRequest", InteractionType.FollowRequest);
			xamlUserType43.AddEnumValue("Count", InteractionType.Count);
			xamlUserType43.AddEnumValue("Followed", InteractionType.Followed);
			xamlUserType43.AddEnumValue("Liked", InteractionType.Liked);
			xamlUserType43.AddEnumValue("GroupedComment", InteractionType.GroupedComment);
			xamlUserType43.AddEnumValue("Reposted", InteractionType.Reposted);
			xamlUserType43.AddEnumValue("TwitterFriendJoined", InteractionType.TwitterFriendJoined);
			xamlUserType43.AddEnumValue("AddressBookFriendJoined", InteractionType.AddressBookFriendJoined);
			xamlUserType43.AddEnumValue("MentionedPost", InteractionType.MentionedPost);
			xamlUserType43.AddEnumValue("MentionedComment", InteractionType.MentionedComment);
			xamlUserType43.AddEnumValue("Mentioned", InteractionType.Mentioned);
			xamlUserType43.AddEnumValue("FollowApproved", InteractionType.FollowApproved);
			xamlUserType43.AddEnumValue("MilestoneFollowers", InteractionType.MilestoneFollowers);
			xamlUserType43.AddEnumValue("MilestoneUserLoops", InteractionType.MilestoneUserLoops);
			xamlUserType43.AddEnumValue("MilestonePostLoops", InteractionType.MilestonePostLoops);
			xamlUserType43.AddEnumValue("MilestonePosts", InteractionType.MilestonePosts);
			xamlUserType43.AddEnumValue("FriendPost", InteractionType.FriendPost);
			xamlUserType43.AddEnumValue("Header", InteractionType.Header);
			xamlUserType43.AddEnumValue("RepostRepost", InteractionType.RepostRepost);
			xamlUserType43.AddEnumValue("RepostLike", InteractionType.RepostLike);
			xamlUserType43.AddEnumValue("FirstPopNow", InteractionType.FirstPopNow);
			xamlUserType43.AddEnumValue("FirstFriendFinder", InteractionType.FirstFriendFinder);
			xamlUserType43.AddEnumValue("FirstExplore", InteractionType.FirstExplore);
			xamlUserType43.AddEnumValue("First", InteractionType.First);
			xamlUserType43.AddEnumValue("CampaignChannel", InteractionType.CampaignChannel);
			xamlUserType43.AddEnumValue("Recommendation", InteractionType.Recommendation);
			xamlUserType43.AddEnumValue("FriendHiatusPost", InteractionType.FriendHiatusPost);
			xamlUserType43.SetIsLocalType();
			result = xamlUserType43;
			break;
		}
		case 198:
		{
			XamlUserType xamlUserType42 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType42.SetIsReturnTypeStub();
			xamlUserType42.SetIsLocalType();
			result = xamlUserType42;
			break;
		}
		case 199:
		{
			XamlUserType xamlUserType41 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType41.Activator = Activate_199_HomeView;
			xamlUserType41.AddMemberName("PinSearchVisible");
			xamlUserType41.AddMemberName("HomeIconFill");
			xamlUserType41.AddMemberName("NotificationIconFill");
			xamlUserType41.AddMemberName("DiscoverIconFill");
			xamlUserType41.AddMemberName("MeIconFill");
			xamlUserType41.AddMemberName("VMsIconFill");
			xamlUserType41.AddMemberName("VideoAppBarIcon");
			xamlUserType41.AddMemberName("AppBarBrush");
			xamlUserType41.AddMemberName("ControlWrapper");
			xamlUserType41.AddMemberName("UploadJobs");
			xamlUserType41.AddMemberName("User");
			xamlUserType41.AddMemberName("MuteIcon");
			xamlUserType41.AddMemberName("MuteLabel");
			xamlUserType41.AddMemberName("TutorialWelcomeVisibility");
			xamlUserType41.AddMemberName("IsRedOn");
			xamlUserType41.AddMemberName("IsOrangeOn");
			xamlUserType41.AddMemberName("IsYellowOn");
			xamlUserType41.AddMemberName("IsGreenOn");
			xamlUserType41.AddMemberName("IsTealOn");
			xamlUserType41.AddMemberName("IsBlueLightOn");
			xamlUserType41.AddMemberName("IsBlueDarkOn");
			xamlUserType41.AddMemberName("IsPurpleOn");
			xamlUserType41.AddMemberName("IsPinkOn");
			xamlUserType41.SetIsLocalType();
			result = xamlUserType41;
			break;
		}
		case 200:
		{
			XamlUserType xamlUserType40 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Models.NotifyObject"));
			xamlUserType40.SetIsReturnTypeStub();
			xamlUserType40.SetIsLocalType();
			result = xamlUserType40;
			break;
		}
		case 201:
		{
			XamlUserType xamlUserType39 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.DataTemplateSelector"));
			xamlUserType39.Activator = Activate_201_InteractionTemplateSelector;
			xamlUserType39.AddMemberName("HeaderTemplate");
			xamlUserType39.AddMemberName("FollowRequestTemplate");
			xamlUserType39.AddMemberName("NotificationTemplate");
			xamlUserType39.AddMemberName("MilestoneTemplate");
			xamlUserType39.AddMemberName("GroupedCountTemplate");
			xamlUserType39.SetIsLocalType();
			result = xamlUserType39;
			break;
		}
		case 202:
		{
			XamlUserType xamlUserType38 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType38.Activator = Activate_202_LoginEmailView;
			xamlUserType38.AddMemberName("Username");
			xamlUserType38.AddMemberName("Password");
			xamlUserType38.AddMemberName("IsBusy");
			xamlUserType38.AddMemberName("IsNotBusy");
			xamlUserType38.SetIsLocalType();
			result = xamlUserType38;
			break;
		}
		case 203:
		{
			XamlUserType xamlUserType37 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType37.Activator = Activate_203_LoginTwitterView;
			xamlUserType37.AddMemberName("ErrorText");
			xamlUserType37.AddMemberName("IsError");
			xamlUserType37.AddMemberName("Username");
			xamlUserType37.AddMemberName("Password");
			xamlUserType37.AddMemberName("IsBusy");
			xamlUserType37.AddMemberName("IsNotBusy");
			xamlUserType37.SetIsLocalType();
			result = xamlUserType37;
			break;
		}
		case 204:
		{
			XamlUserType xamlUserType36 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType36.AddMemberName("RunList");
			xamlUserType36.AddMemberName("ExtraTag");
			xamlUserType36.AddMemberName("MediaStream");
			xamlUserType36.SetIsLocalType();
			result = xamlUserType36;
			break;
		}
		case 205:
		{
			XamlUserType xamlUserType35 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType35.SetIsReturnTypeStub();
			result = xamlUserType35;
			break;
		}
		case 206:
		{
			XamlUserType xamlUserType34 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType34.Activator = Activate_206_ProfileView;
			xamlUserType34.AddMemberName("MuteIcon");
			xamlUserType34.AddMemberName("MuteLabel");
			xamlUserType34.AddMemberName("AppBarButtonVisibility");
			xamlUserType34.AddMemberName("BlockedText");
			xamlUserType34.SetIsLocalType();
			result = xamlUserType34;
			break;
		}
		case 207:
		{
			XamlUserType xamlUserType33 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType33.Activator = Activate_207_FacebookView;
			xamlUserType33.AddMemberName("HasError");
			xamlUserType33.AddMemberName("IsLoading");
			xamlUserType33.AddMemberName("QueryParameters");
			xamlUserType33.SetIsLocalType();
			result = xamlUserType33;
			break;
		}
		case 208:
		{
			XamlUserType xamlUserType32 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType32.DictionaryAdd = MapAdd_208_Dictionary;
			xamlUserType32.SetIsReturnTypeStub();
			result = xamlUserType32;
			break;
		}
		case 209:
		{
			XamlUserType xamlUserType31 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Enum"));
			xamlUserType31.AddEnumValue("Unknown", UriHostNameType.Unknown);
			xamlUserType31.AddEnumValue("Basic", UriHostNameType.Basic);
			xamlUserType31.AddEnumValue("Dns", UriHostNameType.Dns);
			xamlUserType31.AddEnumValue("IPv4", UriHostNameType.IPv4);
			xamlUserType31.AddEnumValue("IPv6", UriHostNameType.IPv6);
			result = xamlUserType31;
			break;
		}
		case 210:
		{
			XamlUserType xamlUserType30 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Array"));
			xamlUserType30.SetIsReturnTypeStub();
			result = xamlUserType30;
			break;
		}
		case 211:
		{
			XamlUserType xamlUserType29 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType29.Activator = Activate_211_SettingsContentView;
			xamlUserType29.AddMemberName("User");
			xamlUserType29.AddMemberName("IsError");
			xamlUserType29.AddMemberName("ErrorText");
			xamlUserType29.AddMemberName("ShowRetry");
			xamlUserType29.AddMemberName("IsLoaded");
			xamlUserType29.AddMemberName("AllowAddressBookButtonState");
			xamlUserType29.SetIsLocalType();
			result = xamlUserType29;
			break;
		}
		case 212:
		{
			XamlUserType xamlUserType28 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType28.Activator = Activate_212_SettingsView;
			xamlUserType28.AddMemberName("User");
			xamlUserType28.AddMemberName("IsRedOn");
			xamlUserType28.AddMemberName("IsOrangeOn");
			xamlUserType28.AddMemberName("IsYellowOn");
			xamlUserType28.AddMemberName("IsGreenOn");
			xamlUserType28.AddMemberName("IsTealOn");
			xamlUserType28.AddMemberName("IsBlueLightOn");
			xamlUserType28.AddMemberName("IsBlueDarkOn");
			xamlUserType28.AddMemberName("IsPurpleOn");
			xamlUserType28.AddMemberName("IsPinkOn");
			xamlUserType28.AddMemberName("IsBusy");
			xamlUserType28.AddMemberName("IsError");
			xamlUserType28.AddMemberName("IsErrorOrBusy");
			xamlUserType28.AddMemberName("ErrorText");
			xamlUserType28.AddMemberName("ShowRetry");
			xamlUserType28.AddMemberName("EmailVerifiedImage");
			xamlUserType28.AddMemberName("PhoneVerifiedImage");
			xamlUserType28.AddMemberName("EmailVerifiedFill");
			xamlUserType28.AddMemberName("PhoneVerifiedFill");
			xamlUserType28.AddMemberName("TwitterConnectedText");
			xamlUserType28.AddMemberName("FacebookConnectedText");
			xamlUserType28.AddMemberName("Version");
			xamlUserType28.AddMemberName("IsFinishedLoading");
			xamlUserType28.SetIsLocalType();
			result = xamlUserType28;
			break;
		}
		case 213:
		{
			XamlUserType xamlUserType27 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType27.Activator = Activate_213_AttributionView;
			xamlUserType27.SetIsLocalType();
			result = xamlUserType27;
			break;
		}
		case 214:
		{
			XamlUserType xamlUserType26 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType26.Activator = Activate_214_SingleVineView;
			xamlUserType26.AddMemberName("Params");
			xamlUserType26.AddMemberName("Section");
			xamlUserType26.AddMemberName("MuteIcon");
			xamlUserType26.AddMemberName("MuteLabel");
			xamlUserType26.SetIsLocalType();
			result = xamlUserType26;
			break;
		}
		case 215:
		{
			XamlUserType xamlUserType25 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType25.SetIsReturnTypeStub();
			xamlUserType25.SetIsLocalType();
			result = xamlUserType25;
			break;
		}
		case 216:
		{
			XamlUserType xamlUserType24 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType24.Activator = Activate_216_TagVineListView;
			xamlUserType24.AddMemberName("MuteIcon");
			xamlUserType24.AddMemberName("MuteLabel");
			xamlUserType24.AddMemberName("PageTitle");
			xamlUserType24.AddMemberName("IsBusy");
			xamlUserType24.AddMemberName("SearchTerm");
			xamlUserType24.AddMemberName("CanPin");
			xamlUserType24.SetIsLocalType();
			result = xamlUserType24;
			break;
		}
		case 217:
		{
			XamlUserType xamlUserType23 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType23.Activator = Activate_217_UploadJobsView;
			xamlUserType23.AddMemberName("ViewModel");
			xamlUserType23.SetIsLocalType();
			result = xamlUserType23;
			break;
		}
		case 218:
		{
			XamlUserType xamlUserType22 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.UserControl"));
			xamlUserType22.AddMemberName("User");
			xamlUserType22.AddMemberName("IsNextEnabled");
			xamlUserType22.SetIsLocalType();
			result = xamlUserType22;
			break;
		}
		case 219:
		{
			XamlUserType xamlUserType21 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.UserControl"));
			xamlUserType21.SetIsLocalType();
			result = xamlUserType21;
			break;
		}
		case 220:
		{
			XamlUserType xamlUserType20 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.UserControl"));
			xamlUserType20.SetIsLocalType();
			result = xamlUserType20;
			break;
		}
		case 221:
		{
			XamlUserType xamlUserType19 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.DataTemplateSelector"));
			xamlUserType19.Activator = Activate_221_MessageTemplateSelector;
			xamlUserType19.AddMemberName("VideoUploadJobTemplate");
			xamlUserType19.AddMemberName("CurrentUserMessageTextTemplate");
			xamlUserType19.AddMemberName("CurrentUserMessageVideoTemplate");
			xamlUserType19.AddMemberName("CurrentUserMessagePostTemplate");
			xamlUserType19.AddMemberName("OtherUserMessagePostTemplate");
			xamlUserType19.AddMemberName("OtherUserMessageTextTemplate");
			xamlUserType19.AddMemberName("OtherUserMessageVideoTemplate");
			xamlUserType19.SetIsLocalType();
			result = xamlUserType19;
			break;
		}
		case 222:
		{
			XamlUserType xamlUserType18 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType18.Activator = Activate_222_VineMessagesThreadView;
			xamlUserType18.AddMemberName("Items");
			xamlUserType18.AddMemberName("ViewModel");
			xamlUserType18.AddMemberName("ConversationId");
			xamlUserType18.AddMemberName("IgnoreLabel");
			xamlUserType18.AddMemberName("UserProfileLabel");
			xamlUserType18.AddMemberName("CurrentUserBrush");
			xamlUserType18.AddMemberName("CurrentUserLightBrush");
			xamlUserType18.AddMemberName("OtherUserBrush");
			xamlUserType18.AddMemberName("OtherUserLightBrush");
			xamlUserType18.AddMemberName("OtherUsername");
			xamlUserType18.AddMemberName("NewComment");
			xamlUserType18.AddMemberName("HasError");
			xamlUserType18.AddMemberName("IsEmpty");
			xamlUserType18.AddMemberName("IsBusy");
			xamlUserType18.AddMemberName("TutorialHintVisibility");
			xamlUserType18.AddMemberName("IsKeyboardVisible");
			xamlUserType18.AddMemberName("IsFinishedLoading");
			xamlUserType18.AddMemberName("SendEnabled");
			xamlUserType18.AddMemberName("IsViewModelLoaded");
			xamlUserType18.AddMemberName("IsVineUser");
			xamlUserType18.AddMemberName("IsVolumeMuted");
			xamlUserType18.AddMemberName("MuteIcon");
			xamlUserType18.AddMemberName("MuteLabel");
			xamlUserType18.SetIsLocalType();
			result = xamlUserType18;
			break;
		}
		case 223:
		{
			XamlUserType xamlUserType17 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.VineMessageViewModel>"));
			xamlUserType17.CollectionAdd = VectorAdd_223_IncrementalLoadingCollection;
			xamlUserType17.SetIsReturnTypeStub();
			xamlUserType17.SetIsLocalType();
			result = xamlUserType17;
			break;
		}
		case 224:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.Collection`1<Vine.Models.VineMessageViewModel>"))
			{
				Activator = Activate_224_ObservableCollection,
				CollectionAdd = VectorAdd_224_ObservableCollection
			};
			break;
		case 225:
			result = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"))
			{
				Activator = Activate_225_Collection,
				CollectionAdd = VectorAdd_225_Collection
			};
			break;
		case 226:
		{
			XamlUserType xamlUserType16 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Models.NotifyObject"));
			xamlUserType16.Activator = Activate_226_VineMessageViewModel;
			xamlUserType16.AddMemberName("YouShareText");
			xamlUserType16.AddMemberName("ShareText");
			xamlUserType16.AddMemberName("Model");
			xamlUserType16.AddMemberName("User");
			xamlUserType16.AddMemberName("ShowCreatedDisplay");
			xamlUserType16.AddMemberName("CreatedDisplay");
			xamlUserType16.AddMemberName("RequiresVerification");
			xamlUserType16.AddMemberName("ErrorMessage");
			xamlUserType16.AddMemberName("HasError");
			xamlUserType16.AddMemberName("UserBrush");
			xamlUserType16.AddMemberName("LightBrush");
			xamlUserType16.AddMemberName("IsPlaying");
			xamlUserType16.AddMemberName("IsLoadingVideo");
			xamlUserType16.AddMemberName("IsFinishedBuffering");
			xamlUserType16.AddMemberName("PlayingVideoUrl");
			xamlUserType16.AddMemberName("PostDescription");
			xamlUserType16.AddMemberName("IsPostDeleted");
			xamlUserType16.AddMemberName("ThumbnailUrlAuth");
			xamlUserType16.AddMemberName("ThumbVisibility");
			xamlUserType16.SetIsLocalType();
			result = xamlUserType16;
			break;
		}
		case 227:
		{
			XamlUserType xamlUserType15 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType15.SetIsReturnTypeStub();
			xamlUserType15.SetIsLocalType();
			result = xamlUserType15;
			break;
		}
		case 228:
		{
			XamlUserType xamlUserType14 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.DataTemplateSelector"));
			xamlUserType14.Activator = Activate_228_VineListTemplateSelector;
			xamlUserType14.AddMemberName("PostTemplate");
			xamlUserType14.AddMemberName("PostMosaicTemplate");
			xamlUserType14.AddMemberName("AvatarPostMosaicTemplate");
			xamlUserType14.AddMemberName("UrlActionTemplate");
			xamlUserType14.AddMemberName("UserMosaicTemplate");
			xamlUserType14.SetIsLocalType();
			result = xamlUserType14;
			break;
		}
		case 229:
		{
			XamlUserType xamlUserType13 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.Button"));
			xamlUserType13.Activator = Activate_229_VinePressedButton;
			xamlUserType13.AddMemberName("ReleasedUI");
			xamlUserType13.AddMemberName("PressedUI");
			xamlUserType13.SetIsLocalType();
			result = xamlUserType13;
			break;
		}
		case 230:
		{
			XamlUserType xamlUserType12 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType12.Activator = Activate_230_ResetPasswordView;
			xamlUserType12.AddMemberName("ExampleEmail");
			xamlUserType12.AddMemberName("ResetPasswordText");
			xamlUserType12.AddMemberName("IsBusy");
			xamlUserType12.AddMemberName("Email");
			xamlUserType12.AddMemberName("Items");
			xamlUserType12.SetIsLocalType();
			result = xamlUserType12;
			break;
		}
		case 231:
		{
			XamlUserType xamlUserType11 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType11.Activator = Activate_231_VineUserListView;
			xamlUserType11.AddMemberName("HasError");
			xamlUserType11.AddMemberName("PageTitle");
			xamlUserType11.AddMemberName("IsBusy");
			xamlUserType11.AddMemberName("EmptyText");
			xamlUserType11.AddMemberName("Items");
			xamlUserType11.AddMemberName("Params");
			xamlUserType11.AddMemberName("IsFinishedLoading");
			xamlUserType11.AddMemberName("IsEmpty");
			xamlUserType11.AddMemberName("ShowLoopCount");
			xamlUserType11.SetIsLocalType();
			result = xamlUserType11;
			break;
		}
		case 232:
		{
			XamlUserType xamlUserType10 = new XamlUserType(this, fullName, type, GetXamlTypeByName("System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.VineUserModel>"));
			xamlUserType10.CollectionAdd = VectorAdd_232_IncrementalLoadingCollection;
			xamlUserType10.SetIsReturnTypeStub();
			xamlUserType10.SetIsLocalType();
			result = xamlUserType10;
			break;
		}
		case 233:
		{
			XamlUserType xamlUserType9 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType9.SetIsReturnTypeStub();
			xamlUserType9.SetIsLocalType();
			result = xamlUserType9;
			break;
		}
		case 234:
		{
			XamlUserType xamlUserType8 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType8.Activator = Activate_234_WebView;
			xamlUserType8.AddMemberName("WebUrl");
			xamlUserType8.AddMemberName("IsBusy");
			xamlUserType8.SetIsLocalType();
			result = xamlUserType8;
			break;
		}
		case 235:
		{
			XamlUserType xamlUserType7 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Vine.Framework.BasePage"));
			xamlUserType7.Activator = Activate_235_WelcomeView;
			xamlUserType7.SetIsLocalType();
			result = xamlUserType7;
			break;
		}
		case 236:
		{
			XamlUserType xamlUserType6 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Windows.UI.Xaml.Controls.UserControl"));
			xamlUserType6.Activator = Activate_236_InstanceCountPopupControl;
			xamlUserType6.SetIsLocalType();
			result = xamlUserType6;
			break;
		}
		case 237:
		{
			XamlUserType xamlUserType5 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType5.Activator = Activate_237_MediaFile;
			xamlUserType5.AddMemberName("VideoIndex");
			xamlUserType5.AddMemberName("AudioIndex");
			xamlUserType5.AddMemberName("VideoStreamCount");
			xamlUserType5.AddMemberName("AudioStreamCount");
			xamlUserType5.AddMemberName("StreamCount");
			xamlUserType5.AddMemberName("bEOF");
			xamlUserType5.AddMemberName("AudioStream");
			xamlUserType5.AddMemberName("VideoStream");
			xamlUserType5.AddMemberName("HasAudio");
			xamlUserType5.AddMemberName("HasVideo");
			xamlUserType5.AddMemberName("AudioProperty");
			xamlUserType5.AddMemberName("VideoProperty");
			xamlUserType5.AddMemberName("BitmapImg");
			xamlUserType5.AddMemberName("FileThumbnail");
			xamlUserType5.AddMemberName("File");
			xamlUserType5.AddMemberName("Path");
			xamlUserType5.AddMemberName("Name");
			xamlUserType5.AddMemberName("ValidInfo");
			xamlUserType5.AddMemberName("Prepped");
			xamlUserType5.AddMemberName("UseHardware");
			xamlUserType5.AddMemberName("EditedLength");
			xamlUserType5.AddMemberName("OriginalLength");
			xamlUserType5.AddMemberName("EndOffset");
			xamlUserType5.AddMemberName("StartOffset");
			xamlUserType5.AddMemberName("VideoOnly");
			xamlUserType5.SetIsBindable();
			result = xamlUserType5;
			break;
		}
		case 238:
			result = new XamlSystemBaseType(fullName, type);
			break;
		case 239:
		{
			XamlUserType xamlUserType4 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType4.SetIsReturnTypeStub();
			result = xamlUserType4;
			break;
		}
		case 240:
		{
			XamlUserType xamlUserType3 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType3.SetIsReturnTypeStub();
			result = xamlUserType3;
			break;
		}
		case 241:
		{
			XamlUserType xamlUserType2 = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType2.SetIsReturnTypeStub();
			result = xamlUserType2;
			break;
		}
		case 242:
		{
			XamlUserType xamlUserType = new XamlUserType(this, fullName, type, GetXamlTypeByName("Object"));
			xamlUserType.SetIsReturnTypeStub();
			result = xamlUserType;
			break;
		}
		}
		return (IXamlType)(object)result;
	}

	private IXamlType CheckOtherMetadataProvidersForName(string typeName)
	{
		IXamlType val = null;
		IXamlType result = null;
		foreach (IXamlMetadataProvider otherProvider in OtherProviders)
		{
			val = otherProvider.GetXamlType(typeName);
			if (val != null)
			{
				if (val.IsConstructible)
				{
					return val;
				}
				result = val;
			}
		}
		return result;
	}

	private IXamlType CheckOtherMetadataProvidersForType(Type type)
	{
		IXamlType val = null;
		IXamlType result = null;
		foreach (IXamlMetadataProvider otherProvider in OtherProviders)
		{
			val = otherProvider.GetXamlType(type);
			if (val != null)
			{
				if (val.IsConstructible)
				{
					return val;
				}
				result = val;
			}
		}
		return result;
	}

	private object get_0_Color_A(object instance)
	{
		return ((Color)instance).A;
	}

	private void set_0_Color_A(object instance, object Value)
	{
		Color color = (Color)instance;
		color.A = (byte)Value;
	}

	private object get_1_Color_B(object instance)
	{
		return ((Color)instance).B;
	}

	private void set_1_Color_B(object instance, object Value)
	{
		Color color = (Color)instance;
		color.B = (byte)Value;
	}

	private object get_2_Color_G(object instance)
	{
		return ((Color)instance).G;
	}

	private void set_2_Color_G(object instance, object Value)
	{
		Color color = (Color)instance;
		color.G = (byte)Value;
	}

	private object get_3_Color_R(object instance)
	{
		return ((Color)instance).R;
	}

	private void set_3_Color_R(object instance, object Value)
	{
		Color color = (Color)instance;
		color.R = (byte)Value;
	}

	private object get_4_VisibleIfTrueConverter_InvertValue(object instance)
	{
		return ((VisibleIfTrueConverter)instance).InvertValue;
	}

	private void set_4_VisibleIfTrueConverter_InvertValue(object instance, object Value)
	{
		((VisibleIfTrueConverter)instance).InvertValue = (bool)Value;
	}

	private object get_5_NoneToVisibilityConverter_InvertValue(object instance)
	{
		return ((NoneToVisibilityConverter)instance).InvertValue;
	}

	private void set_5_NoneToVisibilityConverter_InvertValue(object instance, object Value)
	{
		((NoneToVisibilityConverter)instance).InvertValue = (bool)Value;
	}

	private object get_6_AvatarControl_IsBusy(object instance)
	{
		return ((AvatarControl)instance).IsBusy;
	}

	private void set_6_AvatarControl_IsBusy(object instance, object Value)
	{
		((AvatarControl)instance).IsBusy = (bool)Value;
	}

	private object get_7_AvatarControl_BusyVisible(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((AvatarControl)instance).BusyVisible;
	}

	private void set_7_AvatarControl_BusyVisible(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		((AvatarControl)instance).BusyVisible = (Visibility)Value;
	}

	private object get_8_AvatarControl_DisableFlyout(object instance)
	{
		return ((AvatarControl)instance).DisableFlyout;
	}

	private void set_8_AvatarControl_DisableFlyout(object instance, object Value)
	{
		((AvatarControl)instance).DisableFlyout = (bool)Value;
	}

	private object get_9_AvatarControl_User(object instance)
	{
		return ((AvatarControl)instance).User;
	}

	private object get_10_BasePage_NavigationHelper(object instance)
	{
		return ((BasePage)instance).NavigationHelper;
	}

	private object get_11_BasePage_NavigationParam(object instance)
	{
		return ((BasePage)instance).NavigationParam;
	}

	private void set_11_BasePage_NavigationParam(object instance, object Value)
	{
		((BasePage)instance).NavigationParam = Value;
	}

	private object get_12_BasePage_NavigationObject(object instance)
	{
		return ((BasePage)instance).NavigationObject;
	}

	private void set_12_BasePage_NavigationObject(object instance, object Value)
	{
		((BasePage)instance).NavigationObject = Value;
	}

	private object get_13_BasePage_AlwaysClearBackStack(object instance)
	{
		return ((BasePage)instance).AlwaysClearBackStack;
	}

	private void set_13_BasePage_AlwaysClearBackStack(object instance, object Value)
	{
		((BasePage)instance).AlwaysClearBackStack = (bool)Value;
	}

	private object get_14_NotifyPage_WindowWidth(object instance)
	{
		return ((NotifyPage)instance).WindowWidth;
	}

	private object get_15_NotifyPage_WindowHeight(object instance)
	{
		return ((NotifyPage)instance).WindowHeight;
	}

	private object get_16_NotifyPage_WindowWidthGridLength(object instance)
	{
		return ((NotifyPage)instance).WindowWidthGridLength;
	}

	private object get_17_CaptureView10_CurrentTutorialState(object instance)
	{
		return ((CaptureView10)instance).CurrentTutorialState;
	}

	private void set_17_CaptureView10_CurrentTutorialState(object instance, object Value)
	{
		((CaptureView10)instance).CurrentTutorialState = (CaptureView10.TutorialState)Value;
	}

	private object get_18_CaptureView10_TutorialHintVisibility(object instance)
	{
		return ((CaptureView10)instance).TutorialHintVisibility;
	}

	private object get_19_CaptureView10_TutorialWelcomeVisibility(object instance)
	{
		return ((CaptureView10)instance).TutorialWelcomeVisibility;
	}

	private object get_20_CaptureView10_TutorialMessage(object instance)
	{
		return ((CaptureView10)instance).TutorialMessage;
	}

	private object get_21_CaptureView10_ButtonTutorialCameraToolsVisibility(object instance)
	{
		return ((CaptureView10)instance).ButtonTutorialCameraToolsVisibility;
	}

	private object get_22_CaptureView10_ButtonTutorialUndoVisibility(object instance)
	{
		return ((CaptureView10)instance).ButtonTutorialUndoVisibility;
	}

	private void set_22_CaptureView10_ButtonTutorialUndoVisibility(object instance, object Value)
	{
		((CaptureView10)instance).ButtonTutorialUndoVisibility = (bool)Value;
	}

	private object get_23_CaptureView10_ButtonTutorialGrabVideoVisibility(object instance)
	{
		return ((CaptureView10)instance).ButtonTutorialGrabVideoVisibility;
	}

	private object get_24_CaptureView10_ButtonTutorialState(object instance)
	{
		return ((CaptureView10)instance).ButtonTutorialState;
	}

	private void set_24_CaptureView10_ButtonTutorialState(object instance, object Value)
	{
		((CaptureView10)instance).ButtonTutorialState = (CaptureView10.ButtonsTutorialEnum)Value;
	}

	private object get_25_CaptureView10_PreviewGridClip(object instance)
	{
		return ((CaptureView10)instance).PreviewGridClip;
	}

	private object get_26_CaptureView10_CaptureMargin(object instance)
	{
		return ((CaptureView10)instance).CaptureMargin;
	}

	private object get_27_CaptureView10_NextButtonVisibility(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((CaptureView10)instance).NextButtonVisibility;
	}

	private void set_27_CaptureView10_NextButtonVisibility(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		((CaptureView10)instance).NextButtonVisibility = (Visibility)Value;
	}

	private object get_28_CaptureView10_IsBusy(object instance)
	{
		return ((CaptureView10)instance).IsBusy;
	}

	private void set_28_CaptureView10_IsBusy(object instance, object Value)
	{
		((CaptureView10)instance).IsBusy = (bool)Value;
	}

	private object get_29_CaptureView10_IsTorchSupported(object instance)
	{
		return ((CaptureView10)instance).IsTorchSupported;
	}

	private void set_29_CaptureView10_IsTorchSupported(object instance, object Value)
	{
		((CaptureView10)instance).IsTorchSupported = (bool)Value;
	}

	private object get_30_CaptureView10_IsFrontCameraSupported(object instance)
	{
		return ((CaptureView10)instance).IsFrontCameraSupported;
	}

	private void set_30_CaptureView10_IsFrontCameraSupported(object instance, object Value)
	{
		((CaptureView10)instance).IsFrontCameraSupported = (bool)Value;
	}

	private object get_31_CaptureView10_IsFocusSupported(object instance)
	{
		return ((CaptureView10)instance).IsFocusSupported;
	}

	private void set_31_CaptureView10_IsFocusSupported(object instance, object Value)
	{
		((CaptureView10)instance).IsFocusSupported = (bool)Value;
	}

	private object get_32_CaptureView10_IsFocusLocked(object instance)
	{
		return ((CaptureView10)instance).IsFocusLocked;
	}

	private void set_32_CaptureView10_IsFocusLocked(object instance, object Value)
	{
		((CaptureView10)instance).IsFocusLocked = (bool)Value;
	}

	private object get_33_CaptureView10_IsGhostModeHighlighted(object instance)
	{
		return ((CaptureView10)instance).IsGhostModeHighlighted;
	}

	private void set_33_CaptureView10_IsGhostModeHighlighted(object instance, object Value)
	{
		((CaptureView10)instance).IsGhostModeHighlighted = (bool)Value;
	}

	private object get_34_CaptureView10_IsGridHighlighted(object instance)
	{
		return ((CaptureView10)instance).IsGridHighlighted;
	}

	private void set_34_CaptureView10_IsGridHighlighted(object instance, object Value)
	{
		((CaptureView10)instance).IsGridHighlighted = (bool)Value;
	}

	private object get_35_CaptureView10_IsExpanded(object instance)
	{
		return ((CaptureView10)instance).IsExpanded;
	}

	private void set_35_CaptureView10_IsExpanded(object instance, object Value)
	{
		((CaptureView10)instance).IsExpanded = (bool)Value;
	}

	private object get_36_CaptureView10_IsGridVisible(object instance)
	{
		return ((CaptureView10)instance).IsGridVisible;
	}

	private void set_36_CaptureView10_IsGridVisible(object instance, object Value)
	{
		((CaptureView10)instance).IsGridVisible = (bool)Value;
	}

	private object get_37_CaptureView10_IsUndoHighlighted(object instance)
	{
		return ((CaptureView10)instance).IsUndoHighlighted;
	}

	private void set_37_CaptureView10_IsUndoHighlighted(object instance, object Value)
	{
		((CaptureView10)instance).IsUndoHighlighted = (bool)Value;
	}

	private object get_38_CaptureView10_IsCameraHighlighted(object instance)
	{
		return ((CaptureView10)instance).IsCameraHighlighted;
	}

	private void set_38_CaptureView10_IsCameraHighlighted(object instance, object Value)
	{
		((CaptureView10)instance).IsCameraHighlighted = (bool)Value;
	}

	private object get_39_CaptureView10_IsTorchHighlighted(object instance)
	{
		return ((CaptureView10)instance).IsTorchHighlighted;
	}

	private void set_39_CaptureView10_IsTorchHighlighted(object instance, object Value)
	{
		((CaptureView10)instance).IsTorchHighlighted = (bool)Value;
	}

	private object get_40_CaptureView10_IsFocusModeHighlighted(object instance)
	{
		return ((CaptureView10)instance).IsFocusModeHighlighted;
	}

	private void set_40_CaptureView10_IsFocusModeHighlighted(object instance, object Value)
	{
		((CaptureView10)instance).IsFocusModeHighlighted = (bool)Value;
	}

	private object get_41_CaptureView10_PendingChanges(object instance)
	{
		return ((CaptureView10)instance).PendingChanges;
	}

	private void set_41_CaptureView10_PendingChanges(object instance, object Value)
	{
		((CaptureView10)instance).PendingChanges = (bool)Value;
	}

	private object get_42_CaptureView10_RecordingDraftCount(object instance)
	{
		return ((CaptureView10)instance).RecordingDraftCount;
	}

	private void set_42_CaptureView10_RecordingDraftCount(object instance, object Value)
	{
		((CaptureView10)instance).RecordingDraftCount = (int)Value;
	}

	private object get_43_CaptureView10_DraftNumber(object instance)
	{
		return ((CaptureView10)instance).DraftNumber;
	}

	private object get_44_CaptureView10_IsDraftsEnabled(object instance)
	{
		return ((CaptureView10)instance).IsDraftsEnabled;
	}

	private void set_44_CaptureView10_IsDraftsEnabled(object instance, object Value)
	{
		((CaptureView10)instance).IsDraftsEnabled = (bool)Value;
	}

	private object get_45_CaptureView10_IsUndoEnabled(object instance)
	{
		return ((CaptureView10)instance).IsUndoEnabled;
	}

	private void set_45_CaptureView10_IsUndoEnabled(object instance, object Value)
	{
		((CaptureView10)instance).IsUndoEnabled = (bool)Value;
	}

	private object get_46_CaptureView10_GhostImageSource(object instance)
	{
		return ((CaptureView10)instance).GhostImageSource;
	}

	private void set_46_CaptureView10_GhostImageSource(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((CaptureView10)instance).GhostImageSource = (ImageSource)Value;
	}

	private object get_47_CaptureView10_FocusButtonBrush(object instance)
	{
		return ((CaptureView10)instance).FocusButtonBrush;
	}

	private object get_48_CaptureView10_GhostButtonBrush(object instance)
	{
		return ((CaptureView10)instance).GhostButtonBrush;
	}

	private object get_49_CaptureView10_GridButtonBrush(object instance)
	{
		return ((CaptureView10)instance).GridButtonBrush;
	}

	private object get_50_CaptureView10_CameraButtonBrush(object instance)
	{
		return ((CaptureView10)instance).CameraButtonBrush;
	}

	private object get_51_CaptureView10_WrenchBrush(object instance)
	{
		return ((CaptureView10)instance).WrenchBrush;
	}

	private object get_52_CaptureView10_TorchButtonBrush(object instance)
	{
		return ((CaptureView10)instance).TorchButtonBrush;
	}

	private object get_53_CaptureView10_MediaCapture(object instance)
	{
		return ((CaptureView10)instance).MediaCapture;
	}

	private void set_53_CaptureView10_MediaCapture(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((CaptureView10)instance).MediaCapture = (MediaCapture)Value;
	}

	private object get_54_CaptureView10_VMParameters(object instance)
	{
		return ((CaptureView10)instance).VMParameters;
	}

	private object get_55_ChannelSelectView_Items(object instance)
	{
		return ((ChannelSelectView)instance).Items;
	}

	private void set_55_ChannelSelectView_Items(object instance, object Value)
	{
		((ChannelSelectView)instance).Items = (ObservableCollection<ChannelModel>)Value;
	}

	private object get_56_ChannelModel_IconFullUrl(object instance)
	{
		return ((ChannelModel)instance).IconFullUrl;
	}

	private void set_56_ChannelModel_IconFullUrl(object instance, object Value)
	{
		((ChannelModel)instance).IconFullUrl = (string)Value;
	}

	private object get_57_ChannelModel_ChannelId(object instance)
	{
		return ((ChannelModel)instance).ChannelId;
	}

	private void set_57_ChannelModel_ChannelId(object instance, object Value)
	{
		((ChannelModel)instance).ChannelId = (string)Value;
	}

	private object get_58_ChannelModel_ExploreName(object instance)
	{
		return ((ChannelModel)instance).ExploreName;
	}

	private void set_58_ChannelModel_ExploreName(object instance, object Value)
	{
		((ChannelModel)instance).ExploreName = (string)Value;
	}

	private object get_59_ChannelSelectView_IsBusy(object instance)
	{
		return ((ChannelSelectView)instance).IsBusy;
	}

	private void set_59_ChannelSelectView_IsBusy(object instance, object Value)
	{
		((ChannelSelectView)instance).IsBusy = (bool)Value;
	}

	private object get_60_DraftsView_Items(object instance)
	{
		return ((DraftsView)instance).Items;
	}

	private void set_60_DraftsView_Items(object instance, object Value)
	{
		((DraftsView)instance).Items = (ObservableCollection<RecordingVineModel>)Value;
	}

	private object get_61_RecordingVineModel_DraftId(object instance)
	{
		return ((RecordingVineModel)instance).DraftId;
	}

	private void set_61_RecordingVineModel_DraftId(object instance, object Value)
	{
		((RecordingVineModel)instance).DraftId = (string)Value;
	}

	private object get_62_RecordingVineModel_UploadId(object instance)
	{
		return ((RecordingVineModel)instance).UploadId;
	}

	private void set_62_RecordingVineModel_UploadId(object instance, object Value)
	{
		((RecordingVineModel)instance).UploadId = (string)Value;
	}

	private object get_63_RecordingVineModel_Clips(object instance)
	{
		return ((RecordingVineModel)instance).Clips;
	}

	private void set_63_RecordingVineModel_Clips(object instance, object Value)
	{
		((RecordingVineModel)instance).Clips = (List<RecordingClipModel>)Value;
	}

	private object get_64_RecordingClipModel_VideoFilePath(object instance)
	{
		return ((RecordingClipModel)instance).VideoFilePath;
	}

	private void set_64_RecordingClipModel_VideoFilePath(object instance, object Value)
	{
		((RecordingClipModel)instance).VideoFilePath = (string)Value;
	}

	private object get_65_RecordingClipModel_GhostFilePath(object instance)
	{
		return ((RecordingClipModel)instance).GhostFilePath;
	}

	private void set_65_RecordingClipModel_GhostFilePath(object instance, object Value)
	{
		((RecordingClipModel)instance).GhostFilePath = (string)Value;
	}

	private object get_66_RecordingClipModel_VideoFileDuration(object instance)
	{
		return ((RecordingClipModel)instance).VideoFileDuration;
	}

	private void set_66_RecordingClipModel_VideoFileDuration(object instance, object Value)
	{
		((RecordingClipModel)instance).VideoFileDuration = (long)Value;
	}

	private object get_67_RecordingClipModel_FrameRate(object instance)
	{
		return ((RecordingClipModel)instance).FrameRate;
	}

	private void set_67_RecordingClipModel_FrameRate(object instance, object Value)
	{
		((RecordingClipModel)instance).FrameRate = (long)Value;
	}

	private object get_68_RecordingClipModel_FileStartTime(object instance)
	{
		return ((RecordingClipModel)instance).FileStartTime;
	}

	private void set_68_RecordingClipModel_FileStartTime(object instance, object Value)
	{
		((RecordingClipModel)instance).FileStartTime = (long)Value;
	}

	private object get_69_RecordingClipModel_FileEndTime(object instance)
	{
		return ((RecordingClipModel)instance).FileEndTime;
	}

	private void set_69_RecordingClipModel_FileEndTime(object instance, object Value)
	{
		((RecordingClipModel)instance).FileEndTime = (long)Value;
	}

	private object get_70_RecordingClipModel_EditStartTime(object instance)
	{
		return ((RecordingClipModel)instance).EditStartTime;
	}

	private void set_70_RecordingClipModel_EditStartTime(object instance, object Value)
	{
		((RecordingClipModel)instance).EditStartTime = (long)Value;
	}

	private object get_71_RecordingClipModel_EditEndTime(object instance)
	{
		return ((RecordingClipModel)instance).EditEndTime;
	}

	private void set_71_RecordingClipModel_EditEndTime(object instance, object Value)
	{
		((RecordingClipModel)instance).EditEndTime = (long)Value;
	}

	private object get_72_RecordingVineModel_LastRenderedVideoFilePath(object instance)
	{
		return ((RecordingVineModel)instance).LastRenderedVideoFilePath;
	}

	private void set_72_RecordingVineModel_LastRenderedVideoFilePath(object instance, object Value)
	{
		((RecordingVineModel)instance).LastRenderedVideoFilePath = (string)Value;
	}

	private object get_73_RecordingVineModel_LastRenderedThumbFilePath(object instance)
	{
		return ((RecordingVineModel)instance).LastRenderedThumbFilePath;
	}

	private void set_73_RecordingVineModel_LastRenderedThumbFilePath(object instance, object Value)
	{
		((RecordingVineModel)instance).LastRenderedThumbFilePath = (string)Value;
	}

	private object get_74_RecordingVineModel_SavedClips(object instance)
	{
		return ((RecordingVineModel)instance).SavedClips;
	}

	private void set_74_RecordingVineModel_SavedClips(object instance, object Value)
	{
		((RecordingVineModel)instance).SavedClips = (int)Value;
	}

	private object get_75_RecordingVineModel_HasPendingChangesOnCapture(object instance)
	{
		return ((RecordingVineModel)instance).HasPendingChangesOnCapture;
	}

	private object get_76_RecordingVineModel_Duration(object instance)
	{
		return ((RecordingVineModel)instance).Duration;
	}

	private object get_77_RecordingVineModel_IsClipsSequentialOneFile(object instance)
	{
		return ((RecordingVineModel)instance).IsClipsSequentialOneFile;
	}

	private object get_78_DraftsView_DraftId(object instance)
	{
		return ((DraftsView)instance).DraftId;
	}

	private void set_78_DraftsView_DraftId(object instance, object Value)
	{
		((DraftsView)instance).DraftId = (string)Value;
	}

	private object get_79_EditClipsView_Items(object instance)
	{
		return ((EditClipsView)instance).Items;
	}

	private void set_79_EditClipsView_Items(object instance, object Value)
	{
		((EditClipsView)instance).Items = (ObservableCollection<EditClipsViewModel>)Value;
	}

	private object get_80_EditClipsViewModel_Thumb(object instance)
	{
		return ((EditClipsViewModel)instance).Thumb;
	}

	private void set_80_EditClipsViewModel_Thumb(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((EditClipsViewModel)instance).Thumb = (ImageSource)Value;
	}

	private object get_81_EditClipsViewModel_Composition(object instance)
	{
		return ((EditClipsViewModel)instance).Composition;
	}

	private void set_81_EditClipsViewModel_Composition(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((EditClipsViewModel)instance).Composition = (MediaComposition)Value;
	}

	private object get_82_EditClipsViewModel_MediaClip(object instance)
	{
		return ((EditClipsViewModel)instance).MediaClip;
	}

	private void set_82_EditClipsViewModel_MediaClip(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((EditClipsViewModel)instance).MediaClip = (MediaClip)Value;
	}

	private object get_83_EditClipsViewModel_ClipModel(object instance)
	{
		return ((EditClipsViewModel)instance).ClipModel;
	}

	private void set_83_EditClipsViewModel_ClipModel(object instance, object Value)
	{
		((EditClipsViewModel)instance).ClipModel = (RecordingClipModel)Value;
	}

	private object get_84_EditClipsViewModel_IsActive(object instance)
	{
		return ((EditClipsViewModel)instance).IsActive;
	}

	private void set_84_EditClipsViewModel_IsActive(object instance, object Value)
	{
		((EditClipsViewModel)instance).IsActive = (bool)Value;
	}

	private object get_85_EditClipsViewModel_Opacity(object instance)
	{
		return ((EditClipsViewModel)instance).Opacity;
	}

	private void set_85_EditClipsViewModel_Opacity(object instance, object Value)
	{
		((EditClipsViewModel)instance).Opacity = (double)Value;
	}

	private object get_86_EditClipsViewModel_IsPlaying(object instance)
	{
		return ((EditClipsViewModel)instance).IsPlaying;
	}

	private void set_86_EditClipsViewModel_IsPlaying(object instance, object Value)
	{
		((EditClipsViewModel)instance).IsPlaying = (bool)Value;
	}

	private object get_87_EditClipsView_TrimThumbnails(object instance)
	{
		return ((EditClipsView)instance).TrimThumbnails;
	}

	private void set_87_EditClipsView_TrimThumbnails(object instance, object Value)
	{
		((EditClipsView)instance).TrimThumbnails = (ObservableCollection<BitmapImage>)Value;
	}

	private object get_88_EditClipsView_TrimThumbnailWidth(object instance)
	{
		return ((EditClipsView)instance).TrimThumbnailWidth;
	}

	private object get_89_EditClipsView_IsFinishedLoading(object instance)
	{
		return ((EditClipsView)instance).IsFinishedLoading;
	}

	private void set_89_EditClipsView_IsFinishedLoading(object instance, object Value)
	{
		((EditClipsView)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_90_EditClipsView_IsBusy(object instance)
	{
		return ((EditClipsView)instance).IsBusy;
	}

	private void set_90_EditClipsView_IsBusy(object instance, object Value)
	{
		((EditClipsView)instance).IsBusy = (bool)Value;
	}

	private object get_91_EditClipsView_TrimHighlightRectX(object instance)
	{
		return ((EditClipsView)instance).TrimHighlightRectX;
	}

	private object get_92_EditClipsView_TrimHighlightRectWidth(object instance)
	{
		return ((EditClipsView)instance).TrimHighlightRectWidth;
	}

	private object get_93_EditClipsView_Selected(object instance)
	{
		return ((EditClipsView)instance).Selected;
	}

	private void set_93_EditClipsView_Selected(object instance, object Value)
	{
		((EditClipsView)instance).Selected = (EditClipsViewModel)Value;
	}

	private object get_94_EditClipsView_HasSelection(object instance)
	{
		return ((EditClipsView)instance).HasSelection;
	}

	private object get_95_EditClipsView_HasMoreThanOneClip(object instance)
	{
		return ((EditClipsView)instance).HasMoreThanOneClip;
	}

	private object get_96_EditClipsView_RightSliderValue(object instance)
	{
		return ((EditClipsView)instance).RightSliderValue;
	}

	private void set_96_EditClipsView_RightSliderValue(object instance, object Value)
	{
		((EditClipsView)instance).RightSliderValue = (double)Value;
	}

	private object get_97_EditClipsView_LeftSliderValue(object instance)
	{
		return ((EditClipsView)instance).LeftSliderValue;
	}

	private void set_97_EditClipsView_LeftSliderValue(object instance, object Value)
	{
		((EditClipsView)instance).LeftSliderValue = (double)Value;
	}

	private object get_98_ImportView_Items(object instance)
	{
		return ((ImportView)instance).Items;
	}

	private void set_98_ImportView_Items(object instance, object Value)
	{
		((ImportView)instance).Items = (RandomAccessLoadingCollection<ImportViewModel>)Value;
	}

	private object get_99_ImportView_IsAutoPlay(object instance)
	{
		return ((ImportView)instance).IsAutoPlay;
	}

	private void set_99_ImportView_IsAutoPlay(object instance, object Value)
	{
		((ImportView)instance).IsAutoPlay = (bool)Value;
	}

	private object get_100_ImportView_TrimSliderValue(object instance)
	{
		return ((ImportView)instance).TrimSliderValue;
	}

	private void set_100_ImportView_TrimSliderValue(object instance, object Value)
	{
		((ImportView)instance).TrimSliderValue = (double)Value;
	}

	private object get_101_ImportView_ScrubValue(object instance)
	{
		return ((ImportView)instance).ScrubValue;
	}

	private void set_101_ImportView_ScrubValue(object instance, object Value)
	{
		((ImportView)instance).ScrubValue = (double)Value;
	}

	private object get_102_ImportView_ScrubSliderVisibility(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((ImportView)instance).ScrubSliderVisibility;
	}

	private void set_102_ImportView_ScrubSliderVisibility(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		((ImportView)instance).ScrubSliderVisibility = (Visibility)Value;
	}

	private object get_103_ImportView_ScrubImg(object instance)
	{
		return ((ImportView)instance).ScrubImg;
	}

	private void set_103_ImportView_ScrubImg(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((ImportView)instance).ScrubImg = (ImageSource)Value;
	}

	private object get_104_ImportView_IsBusy(object instance)
	{
		return ((ImportView)instance).IsBusy;
	}

	private void set_104_ImportView_IsBusy(object instance, object Value)
	{
		((ImportView)instance).IsBusy = (bool)Value;
	}

	private object get_105_ImportView_IsProgressZero(object instance)
	{
		return ((ImportView)instance).IsProgressZero;
	}

	private object get_106_ImportView_ProgressValue(object instance)
	{
		return ((ImportView)instance).ProgressValue;
	}

	private void set_106_ImportView_ProgressValue(object instance, object Value)
	{
		((ImportView)instance).ProgressValue = (double)Value;
	}

	private object get_107_PreviewCaptureView_HasTutorialBeenSeen(object instance)
	{
		return ((PreviewCaptureView)instance).HasTutorialBeenSeen;
	}

	private void set_107_PreviewCaptureView_HasTutorialBeenSeen(object instance, object Value)
	{
		((PreviewCaptureView)instance).HasTutorialBeenSeen = (bool)Value;
	}

	private object get_108_PreviewCaptureView_TutorialMessage(object instance)
	{
		return ((PreviewCaptureView)instance).TutorialMessage;
	}

	private object get_109_PreviewCaptureView_Params(object instance)
	{
		return ((PreviewCaptureView)instance).Params;
	}

	private object get_110_PreviewCaptureView_RenderFile(object instance)
	{
		return ((PreviewCaptureView)instance).RenderFile;
	}

	private void set_110_PreviewCaptureView_RenderFile(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((PreviewCaptureView)instance).RenderFile = (StorageFile)Value;
	}

	private object get_111_PreviewCaptureView_IsBusy(object instance)
	{
		return ((PreviewCaptureView)instance).IsBusy;
	}

	private void set_111_PreviewCaptureView_IsBusy(object instance, object Value)
	{
		((PreviewCaptureView)instance).IsBusy = (bool)Value;
	}

	private object get_112_PreviewCaptureView_IsProgressZero(object instance)
	{
		return ((PreviewCaptureView)instance).IsProgressZero;
	}

	private object get_113_PreviewCaptureView_ProgressValue(object instance)
	{
		return ((PreviewCaptureView)instance).ProgressValue;
	}

	private void set_113_PreviewCaptureView_ProgressValue(object instance, object Value)
	{
		((PreviewCaptureView)instance).ProgressValue = (double)Value;
	}

	private object get_114_PreviewCaptureView_ShareButtonBrush(object instance)
	{
		return ((PreviewCaptureView)instance).ShareButtonBrush;
	}

	private object get_115_PreviewCaptureView_IsFinishedLoading(object instance)
	{
		return ((PreviewCaptureView)instance).IsFinishedLoading;
	}

	private void set_115_PreviewCaptureView_IsFinishedLoading(object instance, object Value)
	{
		((PreviewCaptureView)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_116_TaggingTemplateSelector_MentionTemplate(object instance)
	{
		return ((TaggingTemplateSelector)instance).MentionTemplate;
	}

	private void set_116_TaggingTemplateSelector_MentionTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((TaggingTemplateSelector)instance).MentionTemplate = (DataTemplate)Value;
	}

	private object get_117_TaggingTemplateSelector_HashtagTemplate(object instance)
	{
		return ((TaggingTemplateSelector)instance).HashtagTemplate;
	}

	private void set_117_TaggingTemplateSelector_HashtagTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((TaggingTemplateSelector)instance).HashtagTemplate = (DataTemplate)Value;
	}

	private object get_118_Interaction_Behaviors(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		return Interaction.GetBehaviors((DependencyObject)instance);
	}

	private void set_118_Interaction_Behaviors(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		Interaction.SetBehaviors((DependencyObject)instance, (BehaviorCollection)Value);
	}

	private object get_119_Behavior_AssociatedObject(object instance)
	{
		return ((Behavior<TextBox>)instance).AssociatedObject;
	}

	private void set_119_Behavior_AssociatedObject(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((Behavior<TextBox>)instance).AssociatedObject = (DependencyObject)Value;
	}

	private object get_120_Behavior_Object(object instance)
	{
		return ((Behavior<TextBox>)instance).Object;
	}

	private object get_121_ShareCaptureView_MessageHeader(object instance)
	{
		return ((ShareCaptureView)instance).MessageHeader;
	}

	private object get_122_ShareCaptureView_OkLabel(object instance)
	{
		return ((ShareCaptureView)instance).OkLabel;
	}

	private object get_123_ShareCaptureView_AddTagTitle(object instance)
	{
		return ((ShareCaptureView)instance).AddTagTitle;
	}

	private object get_124_ShareCaptureView_IsMention(object instance)
	{
		return ((ShareCaptureView)instance).IsMention;
	}

	private void set_124_ShareCaptureView_IsMention(object instance, object Value)
	{
		((ShareCaptureView)instance).IsMention = (bool)Value;
	}

	private object get_125_ShareCaptureView_TutorialHintVisibility(object instance)
	{
		return ((ShareCaptureView)instance).TutorialHintVisibility;
	}

	private void set_125_ShareCaptureView_TutorialHintVisibility(object instance, object Value)
	{
		((ShareCaptureView)instance).TutorialHintVisibility = (bool)Value;
	}

	private object get_126_ShareCaptureView_IsBusy(object instance)
	{
		return ((ShareCaptureView)instance).IsBusy;
	}

	private void set_126_ShareCaptureView_IsBusy(object instance, object Value)
	{
		((ShareCaptureView)instance).IsBusy = (bool)Value;
	}

	private object get_127_ShareCaptureView_TextInput(object instance)
	{
		return ((ShareCaptureView)instance).TextInput;
	}

	private void set_127_ShareCaptureView_TextInput(object instance, object Value)
	{
		((ShareCaptureView)instance).TextInput = (string)Value;
	}

	private object get_128_ShareCaptureView_CharsLeft(object instance)
	{
		return ((ShareCaptureView)instance).CharsLeft;
	}

	private object get_129_ShareCaptureView_IsTwitterOn(object instance)
	{
		return ((ShareCaptureView)instance).IsTwitterOn;
	}

	private void set_129_ShareCaptureView_IsTwitterOn(object instance, object Value)
	{
		((ShareCaptureView)instance).IsTwitterOn = (bool)Value;
	}

	private object get_130_ShareCaptureView_IsFacebookOn(object instance)
	{
		return ((ShareCaptureView)instance).IsFacebookOn;
	}

	private void set_130_ShareCaptureView_IsFacebookOn(object instance, object Value)
	{
		((ShareCaptureView)instance).IsFacebookOn = (bool)Value;
	}

	private object get_131_ShareCaptureView_HeaderBrush(object instance)
	{
		return ((ShareCaptureView)instance).HeaderBrush;
	}

	private void set_131_ShareCaptureView_HeaderBrush(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((ShareCaptureView)instance).HeaderBrush = (Brush)Value;
	}

	private object get_132_ShareCaptureView_IsVineOn(object instance)
	{
		return ((ShareCaptureView)instance).IsVineOn;
	}

	private void set_132_ShareCaptureView_IsVineOn(object instance, object Value)
	{
		((ShareCaptureView)instance).IsVineOn = (bool)Value;
	}

	private object get_133_ShareCaptureView_Channel(object instance)
	{
		return ((ShareCaptureView)instance).Channel;
	}

	private void set_133_ShareCaptureView_Channel(object instance, object Value)
	{
		((ShareCaptureView)instance).Channel = (ChannelModel)Value;
	}

	private object get_134_ShareCaptureView_ChannelStatus(object instance)
	{
		return ((ShareCaptureView)instance).ChannelStatus;
	}

	private object get_135_ShareCaptureView_IsCommenting(object instance)
	{
		return ((ShareCaptureView)instance).IsCommenting;
	}

	private void set_135_ShareCaptureView_IsCommenting(object instance, object Value)
	{
		((ShareCaptureView)instance).IsCommenting = (bool)Value;
	}

	private object get_136_ShareCaptureView_TagBarVisibility(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((ShareCaptureView)instance).TagBarVisibility;
	}

	private void set_136_ShareCaptureView_TagBarVisibility(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		((ShareCaptureView)instance).TagBarVisibility = (Visibility)Value;
	}

	private object get_137_ShareCaptureView_AutoCompleteList(object instance)
	{
		return ((ShareCaptureView)instance).AutoCompleteList;
	}

	private void set_137_ShareCaptureView_AutoCompleteList(object instance, object Value)
	{
		((ShareCaptureView)instance).AutoCompleteList = (ObservableCollection<Entity>)Value;
	}

	private object get_138_Entity_Type(object instance)
	{
		return ((Entity)instance).Type;
	}

	private void set_138_Entity_Type(object instance, object Value)
	{
		((Entity)instance).Type = (string)Value;
	}

	private object get_139_Entity_EntityType(object instance)
	{
		return ((Entity)instance).EntityType;
	}

	private object get_140_Entity_Id(object instance)
	{
		return ((Entity)instance).Id;
	}

	private void set_140_Entity_Id(object instance, object Value)
	{
		((Entity)instance).Id = (string)Value;
	}

	private object get_141_Entity_Title(object instance)
	{
		return ((Entity)instance).Title;
	}

	private void set_141_Entity_Title(object instance, object Value)
	{
		((Entity)instance).Title = (string)Value;
	}

	private object get_142_Entity_Link(object instance)
	{
		return ((Entity)instance).Link;
	}

	private void set_142_Entity_Link(object instance, object Value)
	{
		((Entity)instance).Link = (string)Value;
	}

	private object get_143_Entity_Range(object instance)
	{
		return ((Entity)instance).Range;
	}

	private void set_143_Entity_Range(object instance, object Value)
	{
		((Entity)instance).Range = (int[])Value;
	}

	private object get_144_Entity_Text(object instance)
	{
		return ((Entity)instance).Text;
	}

	private void set_144_Entity_Text(object instance, object Value)
	{
		((Entity)instance).Text = (string)Value;
	}

	private object get_145_Entity_User(object instance)
	{
		return ((Entity)instance).User;
	}

	private void set_145_Entity_User(object instance, object Value)
	{
		((Entity)instance).User = (VineUserModel)Value;
	}

	private object get_146_ShareCaptureView_IsAutoCompleteListOpen(object instance)
	{
		return ((ShareCaptureView)instance).IsAutoCompleteListOpen;
	}

	private void set_146_ShareCaptureView_IsAutoCompleteListOpen(object instance, object Value)
	{
		((ShareCaptureView)instance).IsAutoCompleteListOpen = (bool)Value;
	}

	private object get_147_MusicInformationControl_MusicTrack(object instance)
	{
		return ((MusicInformationControl)instance).MusicTrack;
	}

	private void set_147_MusicInformationControl_MusicTrack(object instance, object Value)
	{
		((MusicInformationControl)instance).MusicTrack = (string)Value;
	}

	private object get_148_MusicInformationControl_MusicArtist(object instance)
	{
		return ((MusicInformationControl)instance).MusicArtist;
	}

	private void set_148_MusicInformationControl_MusicArtist(object instance, object Value)
	{
		((MusicInformationControl)instance).MusicArtist = (string)Value;
	}

	private object get_149_VineListControl_Section(object instance)
	{
		return ((VineListControl)instance).Section;
	}

	private void set_149_VineListControl_Section(object instance, object Value)
	{
		((VineListControl)instance).Section = (Section)Value;
	}

	private object get_150_VineListControl_SecondaryBrush(object instance)
	{
		return ((VineListControl)instance).SecondaryBrush;
	}

	private void set_150_VineListControl_SecondaryBrush(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineListControl)instance).SecondaryBrush = (Brush)Value;
	}

	private object get_151_VineListControl_MosaicThumbnailMargin(object instance)
	{
		return ((VineListControl)instance).MosaicThumbnailMargin;
	}

	private void set_151_VineListControl_MosaicThumbnailMargin(object instance, object Value)
	{
		((VineListControl)instance).MosaicThumbnailMargin = (Thickness)Value;
	}

	private void set_152_VineListControl_PullToRefreshMargin(object instance, object Value)
	{
		((VineListControl)instance).PullToRefreshMargin = (Thickness)Value;
	}

	private void set_153_VineListControl_ListViewPadding(object instance, object Value)
	{
		((VineListControl)instance).ListViewPadding = (Thickness)Value;
	}

	private object get_154_VineListControl_Header(object instance)
	{
		return ((VineListControl)instance).Header;
	}

	private void set_154_VineListControl_Header(object instance, object Value)
	{
		((VineListControl)instance).Header = Value;
	}

	private object get_155_VineListControl_MusicControl(object instance)
	{
		return ((VineListControl)instance).MusicControl;
	}

	private void set_155_VineListControl_MusicControl(object instance, object Value)
	{
		((VineListControl)instance).MusicControl = (MusicInformationControl)Value;
	}

	private object get_156_VineListControl_HeaderSuccessfulTemplate(object instance)
	{
		return ((VineListControl)instance).HeaderSuccessfulTemplate;
	}

	private void set_156_VineListControl_HeaderSuccessfulTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineListControl)instance).HeaderSuccessfulTemplate = (DataTemplate)Value;
	}

	private object get_157_VineListControl_Footer(object instance)
	{
		return ((VineListControl)instance).Footer;
	}

	private void set_157_VineListControl_Footer(object instance, object Value)
	{
		((VineListControl)instance).Footer = Value;
	}

	private object get_158_VineListControl_FooterTemplate(object instance)
	{
		return ((VineListControl)instance).FooterTemplate;
	}

	private void set_158_VineListControl_FooterTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineListControl)instance).FooterTemplate = (DataTemplate)Value;
	}

	private object get_159_VineListControl_EmptyText(object instance)
	{
		return ((VineListControl)instance).EmptyText;
	}

	private void set_159_VineListControl_EmptyText(object instance, object Value)
	{
		((VineListControl)instance).EmptyText = (string)Value;
	}

	private object get_160_VineListControl_IsEmpty(object instance)
	{
		return ((VineListControl)instance).IsEmpty;
	}

	private void set_160_VineListControl_IsEmpty(object instance, object Value)
	{
		((VineListControl)instance).IsEmpty = (bool)Value;
	}

	private object get_161_VineListControl_HasError(object instance)
	{
		return ((VineListControl)instance).HasError;
	}

	private void set_161_VineListControl_HasError(object instance, object Value)
	{
		((VineListControl)instance).HasError = (bool)Value;
	}

	private object get_162_VineListControl_ErrorText(object instance)
	{
		return ((VineListControl)instance).ErrorText;
	}

	private void set_162_VineListControl_ErrorText(object instance, object Value)
	{
		((VineListControl)instance).ErrorText = (string)Value;
	}

	private object get_163_VineListControl_ShowRetry(object instance)
	{
		return ((VineListControl)instance).ShowRetry;
	}

	private void set_163_VineListControl_ShowRetry(object instance, object Value)
	{
		((VineListControl)instance).ShowRetry = (bool)Value;
	}

	private object get_164_VineListControl_DisablePullToRefresh(object instance)
	{
		return ((VineListControl)instance).DisablePullToRefresh;
	}

	private void set_164_VineListControl_DisablePullToRefresh(object instance, object Value)
	{
		((VineListControl)instance).DisablePullToRefresh = (bool)Value;
	}

	private object get_165_VineListControl_ProfileView(object instance)
	{
		return ((VineListControl)instance).ProfileView;
	}

	private object get_166_VineListControl_Items(object instance)
	{
		return ((VineListControl)instance).Items;
	}

	private void set_166_VineListControl_Items(object instance, object Value)
	{
		((VineListControl)instance).Items = (IncrementalLoadingCollection<VineViewModel>)Value;
	}

	private object get_167_VineViewModel_LikeStatText(object instance)
	{
		return ((VineViewModel)instance).LikeStatText;
	}

	private object get_168_VineViewModel_CommentStatText(object instance)
	{
		return ((VineViewModel)instance).CommentStatText;
	}

	private object get_169_VineViewModel_RevineStatText(object instance)
	{
		return ((VineViewModel)instance).RevineStatText;
	}

	private object get_170_VineViewModel_IsRevinedByMe(object instance)
	{
		return ((VineViewModel)instance).IsRevinedByMe;
	}

	private object get_171_VineViewModel_IsRevined(object instance)
	{
		return ((VineViewModel)instance).IsRevined;
	}

	private object get_172_VineViewModel_RevinedByText(object instance)
	{
		return ((VineViewModel)instance).RevinedByText;
	}

	private object get_173_VineViewModel_HasSimilarVines(object instance)
	{
		return ((VineViewModel)instance).HasSimilarVines;
	}

	private object get_174_VineViewModel_IsMyPost(object instance)
	{
		return ((VineViewModel)instance).IsMyPost;
	}

	private object get_175_VineViewModel_RevineEnabled(object instance)
	{
		return ((VineViewModel)instance).RevineEnabled;
	}

	private object get_176_VineViewModel_IsPlaying(object instance)
	{
		return ((VineViewModel)instance).IsPlaying;
	}

	private void set_176_VineViewModel_IsPlaying(object instance, object Value)
	{
		((VineViewModel)instance).IsPlaying = (bool)Value;
	}

	private object get_177_VineViewModel_IsFinishedBuffering(object instance)
	{
		return ((VineViewModel)instance).IsFinishedBuffering;
	}

	private void set_177_VineViewModel_IsFinishedBuffering(object instance, object Value)
	{
		((VineViewModel)instance).IsFinishedBuffering = (bool)Value;
	}

	private object get_178_VineViewModel_IsLoadingVideo(object instance)
	{
		return ((VineViewModel)instance).IsLoadingVideo;
	}

	private void set_178_VineViewModel_IsLoadingVideo(object instance, object Value)
	{
		((VineViewModel)instance).IsLoadingVideo = (bool)Value;
	}

	private object get_179_VineViewModel_PlayingVideoUrl(object instance)
	{
		return ((VineViewModel)instance).PlayingVideoUrl;
	}

	private object get_180_VineViewModel_ThumbnailUrlAuth(object instance)
	{
		return ((VineViewModel)instance).ThumbnailUrlAuth;
	}

	private object get_181_VineViewModel_ThumbVisibility(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((VineViewModel)instance).ThumbVisibility;
	}

	private void set_181_VineViewModel_ThumbVisibility(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		((VineViewModel)instance).ThumbVisibility = (Visibility)Value;
	}

	private object get_182_VineViewModel_LikeButtonState(object instance)
	{
		return ((VineViewModel)instance).LikeButtonState;
	}

	private void set_182_VineViewModel_LikeButtonState(object instance, object Value)
	{
		((VineViewModel)instance).LikeButtonState = (VineToggleButtonState)Value;
	}

	private object get_183_VineViewModel_RevineButtonState(object instance)
	{
		return ((VineViewModel)instance).RevineButtonState;
	}

	private void set_183_VineViewModel_RevineButtonState(object instance, object Value)
	{
		((VineViewModel)instance).RevineButtonState = (VineToggleButtonState)Value;
	}

	private object get_184_VineViewModel_LoopText(object instance)
	{
		return ((VineViewModel)instance).LoopText;
	}

	private object get_185_VineViewModel_LoopLabelText(object instance)
	{
		return ((VineViewModel)instance).LoopLabelText;
	}

	private object get_186_VineViewModel_CreatedText(object instance)
	{
		return ((VineViewModel)instance).CreatedText;
	}

	private object get_187_VineViewModel_LocationVisibility(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((VineViewModel)instance).LocationVisibility;
	}

	private object get_188_VineViewModel_RichBody(object instance)
	{
		return ((VineViewModel)instance).RichBody;
	}

	private void set_188_VineViewModel_RichBody(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineViewModel)instance).RichBody = (FrameworkElement)Value;
	}

	private object get_189_VineViewModel_Model(object instance)
	{
		return ((VineViewModel)instance).Model;
	}

	private void set_189_VineViewModel_Model(object instance, object Value)
	{
		((VineViewModel)instance).Model = (VineModel)Value;
	}

	private object get_190_VineViewModel_SecondaryBrush(object instance)
	{
		return ((VineViewModel)instance).SecondaryBrush;
	}

	private void set_190_VineViewModel_SecondaryBrush(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineViewModel)instance).SecondaryBrush = (Brush)Value;
	}

	private object get_191_VineViewModel_PendingLoopCount(object instance)
	{
		return ((VineViewModel)instance).PendingLoopCount;
	}

	private void set_191_VineViewModel_PendingLoopCount(object instance, object Value)
	{
		((VineViewModel)instance).PendingLoopCount = (int)Value;
	}

	private object get_192_VineViewModel_LoopsWatchedCount(object instance)
	{
		return ((VineViewModel)instance).LoopsWatchedCount;
	}

	private void set_192_VineViewModel_LoopsWatchedCount(object instance, object Value)
	{
		((VineViewModel)instance).LoopsWatchedCount = (int)Value;
	}

	private object get_193_VineViewModel_DisplayLoops(object instance)
	{
		return ((VineViewModel)instance).DisplayLoops;
	}

	private void set_193_VineViewModel_DisplayLoops(object instance, object Value)
	{
		((VineViewModel)instance).DisplayLoops = (long)Value;
	}

	private object get_194_VineViewModel_LastLoopFinishTime(object instance)
	{
		return ((VineViewModel)instance).LastLoopFinishTime;
	}

	private void set_194_VineViewModel_LastLoopFinishTime(object instance, object Value)
	{
		((VineViewModel)instance).LastLoopFinishTime = (DateTime)Value;
	}

	private object get_195_VineViewModel_FirstLoopStartTime(object instance)
	{
		return ((VineViewModel)instance).FirstLoopStartTime;
	}

	private void set_195_VineViewModel_FirstLoopStartTime(object instance, object Value)
	{
		((VineViewModel)instance).FirstLoopStartTime = (DateTime)Value;
	}

	private object get_196_VineViewModel_IsDownloaded(object instance)
	{
		return ((VineViewModel)instance).IsDownloaded;
	}

	private void set_196_VineViewModel_IsDownloaded(object instance, object Value)
	{
		((VineViewModel)instance).IsDownloaded = (bool)Value;
	}

	private object get_197_VineViewModel_Section(object instance)
	{
		return ((VineViewModel)instance).Section;
	}

	private void set_197_VineViewModel_Section(object instance, object Value)
	{
		((VineViewModel)instance).Section = (Section)Value;
	}

	private object get_198_VineViewModel_View(object instance)
	{
		return ((VineViewModel)instance).View;
	}

	private void set_198_VineViewModel_View(object instance, object Value)
	{
		((VineViewModel)instance).View = (string)Value;
	}

	private object get_199_VineViewModel_TimelineApiUrl(object instance)
	{
		return ((VineViewModel)instance).TimelineApiUrl;
	}

	private void set_199_VineViewModel_TimelineApiUrl(object instance, object Value)
	{
		((VineViewModel)instance).TimelineApiUrl = (string)Value;
	}

	private object get_200_VineViewModel_HasMusic(object instance)
	{
		return ((VineViewModel)instance).HasMusic;
	}

	private object get_201_VineViewModel_IsSeamlessLooping(object instance)
	{
		return ((VineViewModel)instance).IsSeamlessLooping;
	}

	private void set_201_VineViewModel_IsSeamlessLooping(object instance, object Value)
	{
		((VineViewModel)instance).IsSeamlessLooping = (bool)Value;
	}

	private object get_202_VineViewModel_LoopsPerClip(object instance)
	{
		return ((VineViewModel)instance).LoopsPerClip;
	}

	private void set_202_VineViewModel_LoopsPerClip(object instance, object Value)
	{
		((VineViewModel)instance).LoopsPerClip = (int)Value;
	}

	private object get_203_VineViewModel_MosaicThumbnails(object instance)
	{
		return ((VineViewModel)instance).MosaicThumbnails;
	}

	private object get_204_VineListControl_IsVolumeMuted(object instance)
	{
		return ((VineListControl)instance).IsVolumeMuted;
	}

	private object get_205_VineListControl_CurrentTab(object instance)
	{
		return ((VineListControl)instance).CurrentTab;
	}

	private void set_205_VineListControl_CurrentTab(object instance, object Value)
	{
		((VineListControl)instance).CurrentTab = (VineListControl.Tab)Value;
	}

	private object get_206_VineListControl_UserId(object instance)
	{
		return ((VineListControl)instance).UserId;
	}

	private void set_206_VineListControl_UserId(object instance, object Value)
	{
		((VineListControl)instance).UserId = (string)Value;
	}

	private object get_207_VineListControl_PostId(object instance)
	{
		return ((VineListControl)instance).PostId;
	}

	private void set_207_VineListControl_PostId(object instance, object Value)
	{
		((VineListControl)instance).PostId = (string)Value;
	}

	private object get_208_VineListControl_SearchTag(object instance)
	{
		return ((VineListControl)instance).SearchTag;
	}

	private void set_208_VineListControl_SearchTag(object instance, object Value)
	{
		((VineListControl)instance).SearchTag = (string)Value;
	}

	private object get_209_VineListControl_ListParams(object instance)
	{
		return ((VineListControl)instance).ListParams;
	}

	private void set_209_VineListControl_ListParams(object instance, object Value)
	{
		((VineListControl)instance).ListParams = (VineListViewParams)Value;
	}

	private object get_210_VineListControl_PageStateItems(object instance)
	{
		return ((VineListControl)instance).PageStateItems;
	}

	private void set_210_VineListControl_PageStateItems(object instance, object Value)
	{
		((VineListControl)instance).PageStateItems = (List<VineModel>)Value;
	}

	private object get_211_VineModel_UserId(object instance)
	{
		return ((VineModel)instance).UserId;
	}

	private void set_211_VineModel_UserId(object instance, object Value)
	{
		((VineModel)instance).UserId = (string)Value;
	}

	private object get_212_VineModel_UserName(object instance)
	{
		return ((VineModel)instance).UserName;
	}

	private void set_212_VineModel_UserName(object instance, object Value)
	{
		((VineModel)instance).UserName = (string)Value;
	}

	private object get_213_VineModel_AvatarUrl(object instance)
	{
		return ((VineModel)instance).AvatarUrl;
	}

	private void set_213_VineModel_AvatarUrl(object instance, object Value)
	{
		((VineModel)instance).AvatarUrl = (string)Value;
	}

	private object get_214_VineModel_ProfileBackground(object instance)
	{
		return ((VineModel)instance).ProfileBackground;
	}

	private void set_214_VineModel_ProfileBackground(object instance, object Value)
	{
		((VineModel)instance).ProfileBackground = (string)Value;
	}

	private object get_215_VineModel_PostId(object instance)
	{
		return ((VineModel)instance).PostId;
	}

	private void set_215_VineModel_PostId(object instance, object Value)
	{
		((VineModel)instance).PostId = (string)Value;
	}

	private object get_216_VineModel_Liked(object instance)
	{
		return ((VineModel)instance).Liked;
	}

	private void set_216_VineModel_Liked(object instance, object Value)
	{
		((VineModel)instance).Liked = (bool)Value;
	}

	private object get_217_VineModel_Private(object instance)
	{
		return ((VineModel)instance).Private;
	}

	private void set_217_VineModel_Private(object instance, object Value)
	{
		((VineModel)instance).Private = (bool)Value;
	}

	private object get_218_VineModel_MyRepostId(object instance)
	{
		return ((VineModel)instance).MyRepostId;
	}

	private void set_218_VineModel_MyRepostId(object instance, object Value)
	{
		((VineModel)instance).MyRepostId = (string)Value;
	}

	private object get_219_VineModel_ThumbnailUrl(object instance)
	{
		return ((VineModel)instance).ThumbnailUrl;
	}

	private void set_219_VineModel_ThumbnailUrl(object instance, object Value)
	{
		((VineModel)instance).ThumbnailUrl = (string)Value;
	}

	private object get_220_VineModel_VideoUrl(object instance)
	{
		return ((VineModel)instance).VideoUrl;
	}

	private void set_220_VineModel_VideoUrl(object instance, object Value)
	{
		((VineModel)instance).VideoUrl = (string)Value;
	}

	private object get_221_VineModel_VideoLowUrl(object instance)
	{
		return ((VineModel)instance).VideoLowUrl;
	}

	private void set_221_VineModel_VideoLowUrl(object instance, object Value)
	{
		((VineModel)instance).VideoLowUrl = (string)Value;
	}

	private object get_222_VineModel_ShareUrl(object instance)
	{
		return ((VineModel)instance).ShareUrl;
	}

	private void set_222_VineModel_ShareUrl(object instance, object Value)
	{
		((VineModel)instance).ShareUrl = (string)Value;
	}

	private object get_223_VineModel_Description(object instance)
	{
		return ((VineModel)instance).Description;
	}

	private void set_223_VineModel_Description(object instance, object Value)
	{
		((VineModel)instance).Description = (string)Value;
	}

	private object get_224_VineModel_PermalinkUrl(object instance)
	{
		return ((VineModel)instance).PermalinkUrl;
	}

	private void set_224_VineModel_PermalinkUrl(object instance, object Value)
	{
		((VineModel)instance).PermalinkUrl = (string)Value;
	}

	private object get_225_VineModel_VenueName(object instance)
	{
		return ((VineModel)instance).VenueName;
	}

	private void set_225_VineModel_VenueName(object instance, object Value)
	{
		((VineModel)instance).VenueName = (string)Value;
	}

	private object get_226_VineModel_Created(object instance)
	{
		return ((VineModel)instance).Created;
	}

	private void set_226_VineModel_Created(object instance, object Value)
	{
		((VineModel)instance).Created = (DateTime)Value;
	}

	private object get_227_VineModel_Repost(object instance)
	{
		return ((VineModel)instance).Repost;
	}

	private void set_227_VineModel_Repost(object instance, object Value)
	{
		((VineModel)instance).Repost = (RepostModel)Value;
	}

	private object get_228_VineModel_Likes(object instance)
	{
		return ((VineModel)instance).Likes;
	}

	private void set_228_VineModel_Likes(object instance, object Value)
	{
		((VineModel)instance).Likes = (VineStatModel)Value;
	}

	private object get_229_VineModel_Reposts(object instance)
	{
		return ((VineModel)instance).Reposts;
	}

	private void set_229_VineModel_Reposts(object instance, object Value)
	{
		((VineModel)instance).Reposts = (VineStatModel)Value;
	}

	private object get_230_VineModel_Comments(object instance)
	{
		return ((VineModel)instance).Comments;
	}

	private void set_230_VineModel_Comments(object instance, object Value)
	{
		((VineModel)instance).Comments = (VineStatModel)Value;
	}

	private object get_231_VineModel_Loops(object instance)
	{
		return ((VineModel)instance).Loops;
	}

	private void set_231_VineModel_Loops(object instance, object Value)
	{
		((VineModel)instance).Loops = (VineLoopModel)Value;
	}

	private object get_232_VineModel_HasSimilarPosts(object instance)
	{
		return ((VineModel)instance).HasSimilarPosts;
	}

	private void set_232_VineModel_HasSimilarPosts(object instance, object Value)
	{
		((VineModel)instance).HasSimilarPosts = (bool)Value;
	}

	private object get_233_VineModel_AudioTracks(object instance)
	{
		return ((VineModel)instance).AudioTracks;
	}

	private void set_233_VineModel_AudioTracks(object instance, object Value)
	{
		((VineModel)instance).AudioTracks = (List<AudioTracks>)Value;
	}

	private object get_234_AudioTracks_Track(object instance)
	{
		return ((AudioTracks)instance).Track;
	}

	private void set_234_AudioTracks_Track(object instance, object Value)
	{
		((AudioTracks)instance).Track = (Track)Value;
	}

	private object get_235_VineModel_Entities(object instance)
	{
		return ((VineModel)instance).Entities;
	}

	private void set_235_VineModel_Entities(object instance, object Value)
	{
		((VineModel)instance).Entities = (List<Entity>)Value;
	}

	private object get_236_VineModel_VineUrl(object instance)
	{
		return ((VineModel)instance).VineUrl;
	}

	private object get_237_VineModel_Reference(object instance)
	{
		return ((VineModel)instance).Reference;
	}

	private void set_237_VineModel_Reference(object instance, object Value)
	{
		((VineModel)instance).Reference = (string)Value;
	}

	private object get_238_VineModel_MosaicType(object instance)
	{
		return ((VineModel)instance).MosaicType;
	}

	private void set_238_VineModel_MosaicType(object instance, object Value)
	{
		((VineModel)instance).MosaicType = (string)Value;
	}

	private object get_239_VineModel_Title(object instance)
	{
		return ((VineModel)instance).Title;
	}

	private void set_239_VineModel_Title(object instance, object Value)
	{
		((VineModel)instance).Title = (string)Value;
	}

	private object get_240_VineModel_Records(object instance)
	{
		return ((VineModel)instance).Records;
	}

	private void set_240_VineModel_Records(object instance, object Value)
	{
		((VineModel)instance).Records = (List<VineModel>)Value;
	}

	private object get_241_VineModel_Link(object instance)
	{
		return ((VineModel)instance).Link;
	}

	private void set_241_VineModel_Link(object instance, object Value)
	{
		((VineModel)instance).Link = (string)Value;
	}

	private object get_242_VineModel_LinkPath(object instance)
	{
		return ((VineModel)instance).LinkPath;
	}

	private object get_243_VineModel_Type(object instance)
	{
		return ((VineModel)instance).Type;
	}

	private void set_243_VineModel_Type(object instance, object Value)
	{
		((VineModel)instance).Type = (string)Value;
	}

	private object get_244_VineModel_Page(object instance)
	{
		return ((VineModel)instance).Page;
	}

	private void set_244_VineModel_Page(object instance, object Value)
	{
		((VineModel)instance).Page = (int)Value;
	}

	private object get_245_VineModel_Size(object instance)
	{
		return ((VineModel)instance).Size;
	}

	private void set_245_VineModel_Size(object instance, object Value)
	{
		((VineModel)instance).Size = (int)Value;
	}

	private object get_246_VineModel_RecordType(object instance)
	{
		return ((VineModel)instance).RecordType;
	}

	private object get_247_VineModel_ParsedMosaicType(object instance)
	{
		return ((VineModel)instance).ParsedMosaicType;
	}

	private object get_248_VineListControl_PageStateScrollOffset(object instance)
	{
		return ((VineListControl)instance).PageStateScrollOffset;
	}

	private void set_248_VineListControl_PageStateScrollOffset(object instance, object Value)
	{
		((VineListControl)instance).PageStateScrollOffset = (double)Value;
	}

	private object get_249_VineListControl_IsBusy(object instance)
	{
		return ((VineListControl)instance).IsBusy;
	}

	private void set_249_VineListControl_IsBusy(object instance, object Value)
	{
		((VineListControl)instance).IsBusy = (bool)Value;
	}

	private object get_250_VineListControl_Active(object instance)
	{
		return ((VineListControl)instance).Active;
	}

	private void set_250_VineListControl_Active(object instance, object Value)
	{
		((VineListControl)instance).Active = (bool)Value;
	}

	private object get_251_VineListControl_IsFinishedLoading(object instance)
	{
		return ((VineListControl)instance).IsFinishedLoading;
	}

	private void set_251_VineListControl_IsFinishedLoading(object instance, object Value)
	{
		((VineListControl)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_252_VineListControl_ScrollOffset(object instance)
	{
		return ((VineListControl)instance).ScrollOffset;
	}

	private object get_253_VineListControl_ScrollingDirection(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((VineListControl)instance).ScrollingDirection;
	}

	private void set_253_VineListControl_ScrollingDirection(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		((VineListControl)instance).ScrollingDirection = (PanelScrollingDirection)Value;
	}

	private object get_254_ChannelVineListView_FeaturedPivotHeaderBrush(object instance)
	{
		return ((ChannelVineListView)instance).FeaturedPivotHeaderBrush;
	}

	private void set_254_ChannelVineListView_FeaturedPivotHeaderBrush(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((ChannelVineListView)instance).FeaturedPivotHeaderBrush = (Brush)Value;
	}

	private object get_255_ChannelVineListView_RecentPivotHeaderBrush(object instance)
	{
		return ((ChannelVineListView)instance).RecentPivotHeaderBrush;
	}

	private void set_255_ChannelVineListView_RecentPivotHeaderBrush(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((ChannelVineListView)instance).RecentPivotHeaderBrush = (Brush)Value;
	}

	private object get_256_ChannelVineListView_ChannelBrush(object instance)
	{
		return ((ChannelVineListView)instance).ChannelBrush;
	}

	private void set_256_ChannelVineListView_ChannelBrush(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((ChannelVineListView)instance).ChannelBrush = (Brush)Value;
	}

	private object get_257_ChannelVineListView_PageTitle(object instance)
	{
		return ((ChannelVineListView)instance).PageTitle;
	}

	private void set_257_ChannelVineListView_PageTitle(object instance, object Value)
	{
		((ChannelVineListView)instance).PageTitle = (string)Value;
	}

	private object get_258_ChannelVineListView_IsBusy(object instance)
	{
		return ((ChannelVineListView)instance).IsBusy;
	}

	private void set_258_ChannelVineListView_IsBusy(object instance, object Value)
	{
		((ChannelVineListView)instance).IsBusy = (bool)Value;
	}

	private object get_259_ChannelVineListView_FeaturedBrush(object instance)
	{
		return ((ChannelVineListView)instance).FeaturedBrush;
	}

	private void set_259_ChannelVineListView_FeaturedBrush(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((ChannelVineListView)instance).FeaturedBrush = (Brush)Value;
	}

	private object get_260_ChannelVineListView_RecentBrush(object instance)
	{
		return ((ChannelVineListView)instance).RecentBrush;
	}

	private void set_260_ChannelVineListView_RecentBrush(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((ChannelVineListView)instance).RecentBrush = (Brush)Value;
	}

	private object get_261_ChannelVineListView_Model(object instance)
	{
		return ((ChannelVineListView)instance).Model;
	}

	private object get_262_ChannelVineListView_MuteIcon(object instance)
	{
		return ((ChannelVineListView)instance).MuteIcon;
	}

	private object get_263_ChannelVineListView_MuteLabel(object instance)
	{
		return ((ChannelVineListView)instance).MuteLabel;
	}

	private object get_264_CommentsView_KeyboardHeight(object instance)
	{
		return ((CommentsView)instance).KeyboardHeight;
	}

	private void set_264_CommentsView_KeyboardHeight(object instance, object Value)
	{
		((CommentsView)instance).KeyboardHeight = (double)Value;
	}

	private object get_265_CommentsView_IsBusy(object instance)
	{
		return ((CommentsView)instance).IsBusy;
	}

	private void set_265_CommentsView_IsBusy(object instance, object Value)
	{
		((CommentsView)instance).IsBusy = (bool)Value;
	}

	private object get_266_CommentsView_IsBusyPosting(object instance)
	{
		return ((CommentsView)instance).IsBusyPosting;
	}

	private void set_266_CommentsView_IsBusyPosting(object instance, object Value)
	{
		((CommentsView)instance).IsBusyPosting = (bool)Value;
	}

	private object get_267_CommentsView_HasError(object instance)
	{
		return ((CommentsView)instance).HasError;
	}

	private void set_267_CommentsView_HasError(object instance, object Value)
	{
		((CommentsView)instance).HasError = (bool)Value;
	}

	private object get_268_CommentsView_ErrorText(object instance)
	{
		return ((CommentsView)instance).ErrorText;
	}

	private void set_268_CommentsView_ErrorText(object instance, object Value)
	{
		((CommentsView)instance).ErrorText = (string)Value;
	}

	private object get_269_CommentsView_ShowRetry(object instance)
	{
		return ((CommentsView)instance).ShowRetry;
	}

	private void set_269_CommentsView_ShowRetry(object instance, object Value)
	{
		((CommentsView)instance).ShowRetry = (bool)Value;
	}

	private object get_270_CommentsView_IsEmpty(object instance)
	{
		return ((CommentsView)instance).IsEmpty;
	}

	private void set_270_CommentsView_IsEmpty(object instance, object Value)
	{
		((CommentsView)instance).IsEmpty = (bool)Value;
	}

	private object get_271_CommentsView_SendEnabled(object instance)
	{
		return ((CommentsView)instance).SendEnabled;
	}

	private object get_272_CommentsView_IsFocusedByDefault(object instance)
	{
		return ((CommentsView)instance).IsFocusedByDefault;
	}

	private void set_272_CommentsView_IsFocusedByDefault(object instance, object Value)
	{
		((CommentsView)instance).IsFocusedByDefault = (bool)Value;
	}

	private object get_273_CommentsView_TextInput(object instance)
	{
		return ((CommentsView)instance).TextInput;
	}

	private void set_273_CommentsView_TextInput(object instance, object Value)
	{
		((CommentsView)instance).TextInput = (string)Value;
	}

	private object get_274_CommentsView_CharsLeft(object instance)
	{
		return ((CommentsView)instance).CharsLeft;
	}

	private object get_275_CommentsView_ScrollOffset(object instance)
	{
		return ((CommentsView)instance).ScrollOffset;
	}

	private object get_276_CommentsView_IsFinishedLoading(object instance)
	{
		return ((CommentsView)instance).IsFinishedLoading;
	}

	private void set_276_CommentsView_IsFinishedLoading(object instance, object Value)
	{
		((CommentsView)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_277_CommentsView_Items(object instance)
	{
		return ((CommentsView)instance).Items;
	}

	private void set_277_CommentsView_Items(object instance, object Value)
	{
		((CommentsView)instance).Items = (IncrementalLoadingCollection<CommentModel>)Value;
	}

	private object get_278_CommentModel_IsUserComment(object instance)
	{
		return ((CommentModel)instance).IsUserComment;
	}

	private object get_279_CommentModel_Comment(object instance)
	{
		return ((CommentModel)instance).Comment;
	}

	private void set_279_CommentModel_Comment(object instance, object Value)
	{
		((CommentModel)instance).Comment = (string)Value;
	}

	private object get_280_CommentModel_Entities(object instance)
	{
		return ((CommentModel)instance).Entities;
	}

	private void set_280_CommentModel_Entities(object instance, object Value)
	{
		((CommentModel)instance).Entities = (List<Entity>)Value;
	}

	private object get_281_CommentModel_User(object instance)
	{
		return ((CommentModel)instance).User;
	}

	private void set_281_CommentModel_User(object instance, object Value)
	{
		((CommentModel)instance).User = (VineUserModel)Value;
	}

	private object get_282_CommentModel_PostId(object instance)
	{
		return ((CommentModel)instance).PostId;
	}

	private void set_282_CommentModel_PostId(object instance, object Value)
	{
		((CommentModel)instance).PostId = (string)Value;
	}

	private object get_283_CommentModel_CommentId(object instance)
	{
		return ((CommentModel)instance).CommentId;
	}

	private void set_283_CommentModel_CommentId(object instance, object Value)
	{
		((CommentModel)instance).CommentId = (string)Value;
	}

	private object get_284_CommentModel_Created(object instance)
	{
		return ((CommentModel)instance).Created;
	}

	private void set_284_CommentModel_Created(object instance, object Value)
	{
		((CommentModel)instance).Created = (DateTime)Value;
	}

	private object get_285_CommentModel_CreatedText(object instance)
	{
		return ((CommentModel)instance).CreatedText;
	}

	private object get_286_CommentModel_RichBody(object instance)
	{
		return ((CommentModel)instance).RichBody;
	}

	private object get_287_CommentsView_PostId(object instance)
	{
		return ((CommentsView)instance).PostId;
	}

	private void set_287_CommentsView_PostId(object instance, object Value)
	{
		((CommentsView)instance).PostId = (string)Value;
	}

	private object get_288_CommentsView_Section(object instance)
	{
		return ((CommentsView)instance).Section;
	}

	private void set_288_CommentsView_Section(object instance, object Value)
	{
		((CommentsView)instance).Section = (Section)Value;
	}

	private object get_289_CommentsView_IsCommenting(object instance)
	{
		return ((CommentsView)instance).IsCommenting;
	}

	private void set_289_CommentsView_IsCommenting(object instance, object Value)
	{
		((CommentsView)instance).IsCommenting = (bool)Value;
	}

	private object get_290_CommentsView_TagBarVisibility(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((CommentsView)instance).TagBarVisibility;
	}

	private void set_290_CommentsView_TagBarVisibility(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		((CommentsView)instance).TagBarVisibility = (Visibility)Value;
	}

	private object get_291_CommentsView_AutoCompleteList(object instance)
	{
		return ((CommentsView)instance).AutoCompleteList;
	}

	private void set_291_CommentsView_AutoCompleteList(object instance, object Value)
	{
		((CommentsView)instance).AutoCompleteList = (ObservableCollection<Entity>)Value;
	}

	private object get_292_CommentsView_IsAutoCompleteListOpen(object instance)
	{
		return ((CommentsView)instance).IsAutoCompleteListOpen;
	}

	private void set_292_CommentsView_IsAutoCompleteListOpen(object instance, object Value)
	{
		((CommentsView)instance).IsAutoCompleteListOpen = (bool)Value;
	}

	private object get_293_CaptureView8_CurrentTutorialState(object instance)
	{
		return ((CaptureView8)instance).CurrentTutorialState;
	}

	private void set_293_CaptureView8_CurrentTutorialState(object instance, object Value)
	{
		((CaptureView8)instance).CurrentTutorialState = (CaptureView8.TutorialState)Value;
	}

	private object get_294_CaptureView8_TutorialHintVisibility(object instance)
	{
		return ((CaptureView8)instance).TutorialHintVisibility;
	}

	private object get_295_CaptureView8_TutorialWelcomeVisibility(object instance)
	{
		return ((CaptureView8)instance).TutorialWelcomeVisibility;
	}

	private object get_296_CaptureView8_TutorialMessage(object instance)
	{
		return ((CaptureView8)instance).TutorialMessage;
	}

	private object get_297_CaptureView8_ButtonTutorialCameraToolsVisibility(object instance)
	{
		return ((CaptureView8)instance).ButtonTutorialCameraToolsVisibility;
	}

	private object get_298_CaptureView8_ButtonTutorialUndoVisibility(object instance)
	{
		return ((CaptureView8)instance).ButtonTutorialUndoVisibility;
	}

	private void set_298_CaptureView8_ButtonTutorialUndoVisibility(object instance, object Value)
	{
		((CaptureView8)instance).ButtonTutorialUndoVisibility = (bool)Value;
	}

	private object get_299_CaptureView8_ButtonTutorialGrabVideoVisibility(object instance)
	{
		return ((CaptureView8)instance).ButtonTutorialGrabVideoVisibility;
	}

	private object get_300_CaptureView8_ButtonTutorialState(object instance)
	{
		return ((CaptureView8)instance).ButtonTutorialState;
	}

	private void set_300_CaptureView8_ButtonTutorialState(object instance, object Value)
	{
		((CaptureView8)instance).ButtonTutorialState = (CaptureView8.ButtonsTutorialEnum)Value;
	}

	private object get_301_CaptureView8_NextButtonVisibility(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((CaptureView8)instance).NextButtonVisibility;
	}

	private void set_301_CaptureView8_NextButtonVisibility(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		((CaptureView8)instance).NextButtonVisibility = (Visibility)Value;
	}

	private object get_302_CaptureView8_IsBusy(object instance)
	{
		return ((CaptureView8)instance).IsBusy;
	}

	private void set_302_CaptureView8_IsBusy(object instance, object Value)
	{
		((CaptureView8)instance).IsBusy = (bool)Value;
	}

	private object get_303_CaptureView8_IsTorchSupported(object instance)
	{
		return ((CaptureView8)instance).IsTorchSupported;
	}

	private void set_303_CaptureView8_IsTorchSupported(object instance, object Value)
	{
		((CaptureView8)instance).IsTorchSupported = (bool)Value;
	}

	private object get_304_CaptureView8_IsFrontCameraSupported(object instance)
	{
		return ((CaptureView8)instance).IsFrontCameraSupported;
	}

	private void set_304_CaptureView8_IsFrontCameraSupported(object instance, object Value)
	{
		((CaptureView8)instance).IsFrontCameraSupported = (bool)Value;
	}

	private object get_305_CaptureView8_IsFocusSupported(object instance)
	{
		return ((CaptureView8)instance).IsFocusSupported;
	}

	private void set_305_CaptureView8_IsFocusSupported(object instance, object Value)
	{
		((CaptureView8)instance).IsFocusSupported = (bool)Value;
	}

	private object get_306_CaptureView8_IsFocusLocked(object instance)
	{
		return ((CaptureView8)instance).IsFocusLocked;
	}

	private void set_306_CaptureView8_IsFocusLocked(object instance, object Value)
	{
		((CaptureView8)instance).IsFocusLocked = (bool)Value;
	}

	private object get_307_CaptureView8_IsGhostModeHighlighted(object instance)
	{
		return ((CaptureView8)instance).IsGhostModeHighlighted;
	}

	private void set_307_CaptureView8_IsGhostModeHighlighted(object instance, object Value)
	{
		((CaptureView8)instance).IsGhostModeHighlighted = (bool)Value;
	}

	private object get_308_CaptureView8_IsGridHighlighted(object instance)
	{
		return ((CaptureView8)instance).IsGridHighlighted;
	}

	private void set_308_CaptureView8_IsGridHighlighted(object instance, object Value)
	{
		((CaptureView8)instance).IsGridHighlighted = (bool)Value;
	}

	private object get_309_CaptureView8_IsExpanded(object instance)
	{
		return ((CaptureView8)instance).IsExpanded;
	}

	private void set_309_CaptureView8_IsExpanded(object instance, object Value)
	{
		((CaptureView8)instance).IsExpanded = (bool)Value;
	}

	private object get_310_CaptureView8_IsGridVisible(object instance)
	{
		return ((CaptureView8)instance).IsGridVisible;
	}

	private void set_310_CaptureView8_IsGridVisible(object instance, object Value)
	{
		((CaptureView8)instance).IsGridVisible = (bool)Value;
	}

	private object get_311_CaptureView8_IsUndoHighlighted(object instance)
	{
		return ((CaptureView8)instance).IsUndoHighlighted;
	}

	private void set_311_CaptureView8_IsUndoHighlighted(object instance, object Value)
	{
		((CaptureView8)instance).IsUndoHighlighted = (bool)Value;
	}

	private object get_312_CaptureView8_IsCameraHighlighted(object instance)
	{
		return ((CaptureView8)instance).IsCameraHighlighted;
	}

	private void set_312_CaptureView8_IsCameraHighlighted(object instance, object Value)
	{
		((CaptureView8)instance).IsCameraHighlighted = (bool)Value;
	}

	private object get_313_CaptureView8_IsTorchHighlighted(object instance)
	{
		return ((CaptureView8)instance).IsTorchHighlighted;
	}

	private void set_313_CaptureView8_IsTorchHighlighted(object instance, object Value)
	{
		((CaptureView8)instance).IsTorchHighlighted = (bool)Value;
	}

	private object get_314_CaptureView8_IsFocusModeHighlighted(object instance)
	{
		return ((CaptureView8)instance).IsFocusModeHighlighted;
	}

	private void set_314_CaptureView8_IsFocusModeHighlighted(object instance, object Value)
	{
		((CaptureView8)instance).IsFocusModeHighlighted = (bool)Value;
	}

	private object get_315_CaptureView8_PendingChanges(object instance)
	{
		return ((CaptureView8)instance).PendingChanges;
	}

	private void set_315_CaptureView8_PendingChanges(object instance, object Value)
	{
		((CaptureView8)instance).PendingChanges = (bool)Value;
	}

	private object get_316_CaptureView8_RecordingDraftCount(object instance)
	{
		return ((CaptureView8)instance).RecordingDraftCount;
	}

	private void set_316_CaptureView8_RecordingDraftCount(object instance, object Value)
	{
		((CaptureView8)instance).RecordingDraftCount = (int)Value;
	}

	private object get_317_CaptureView8_DraftNumber(object instance)
	{
		return ((CaptureView8)instance).DraftNumber;
	}

	private object get_318_CaptureView8_IsDraftsEnabled(object instance)
	{
		return ((CaptureView8)instance).IsDraftsEnabled;
	}

	private void set_318_CaptureView8_IsDraftsEnabled(object instance, object Value)
	{
		((CaptureView8)instance).IsDraftsEnabled = (bool)Value;
	}

	private object get_319_CaptureView8_IsUndoEnabled(object instance)
	{
		return ((CaptureView8)instance).IsUndoEnabled;
	}

	private void set_319_CaptureView8_IsUndoEnabled(object instance, object Value)
	{
		((CaptureView8)instance).IsUndoEnabled = (bool)Value;
	}

	private object get_320_CaptureView8_GhostImageSource(object instance)
	{
		return ((CaptureView8)instance).GhostImageSource;
	}

	private void set_320_CaptureView8_GhostImageSource(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((CaptureView8)instance).GhostImageSource = (ImageSource)Value;
	}

	private object get_321_CaptureView8_FocusButtonBrush(object instance)
	{
		return ((CaptureView8)instance).FocusButtonBrush;
	}

	private object get_322_CaptureView8_GhostButtonBrush(object instance)
	{
		return ((CaptureView8)instance).GhostButtonBrush;
	}

	private object get_323_CaptureView8_GridButtonBrush(object instance)
	{
		return ((CaptureView8)instance).GridButtonBrush;
	}

	private object get_324_CaptureView8_CameraButtonBrush(object instance)
	{
		return ((CaptureView8)instance).CameraButtonBrush;
	}

	private object get_325_CaptureView8_WrenchBrush(object instance)
	{
		return ((CaptureView8)instance).WrenchBrush;
	}

	private object get_326_CaptureView8_TorchButtonBrush(object instance)
	{
		return ((CaptureView8)instance).TorchButtonBrush;
	}

	private object get_327_CaptureView8_MediaCapture(object instance)
	{
		return ((CaptureView8)instance).MediaCapture;
	}

	private void set_327_CaptureView8_MediaCapture(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((CaptureView8)instance).MediaCapture = (MediaCapture)Value;
	}

	private object get_328_CaptureView8_VMParameters(object instance)
	{
		return ((CaptureView8)instance).VMParameters;
	}

	private object get_329_PullToRefreshListControl_DefaultPadding(object instance)
	{
		return ((PullToRefreshListControl)instance).DefaultPadding;
	}

	private void set_329_PullToRefreshListControl_DefaultPadding(object instance, object Value)
	{
		((PullToRefreshListControl)instance).DefaultPadding = (Thickness)Value;
	}

	private object get_330_PullToRefreshListControl_ListView(object instance)
	{
		return ((PullToRefreshListControl)instance).ListView;
	}

	private object get_331_PullToRefreshListControl_ScrollViewer(object instance)
	{
		return ((PullToRefreshListControl)instance).ScrollViewer;
	}

	private object get_332_ConversationList_Items(object instance)
	{
		return ((ConversationList)instance).Items;
	}

	private void set_332_ConversationList_Items(object instance, object Value)
	{
		((ConversationList)instance).Items = (IncrementalLoadingCollection<ConversationViewModel>)Value;
	}

	private object get_333_ConversationViewModel_OtherUser(object instance)
	{
		return ((ConversationViewModel)instance).OtherUser;
	}

	private void set_333_ConversationViewModel_OtherUser(object instance, object Value)
	{
		((ConversationViewModel)instance).OtherUser = (VineUserModel)Value;
	}

	private object get_334_ConversationViewModel_Record(object instance)
	{
		return ((ConversationViewModel)instance).Record;
	}

	private void set_334_ConversationViewModel_Record(object instance, object Value)
	{
		((ConversationViewModel)instance).Record = (BaseConversationModel)Value;
	}

	private object get_335_ConversationViewModel_DeletedMsgIds(object instance)
	{
		return ((ConversationViewModel)instance).DeletedMsgIds;
	}

	private void set_335_ConversationViewModel_DeletedMsgIds(object instance, object Value)
	{
		((ConversationViewModel)instance).DeletedMsgIds = (List<string>)Value;
	}

	private object get_336_ConversationViewModel_CurrentUser(object instance)
	{
		return ((ConversationViewModel)instance).CurrentUser;
	}

	private object get_337_ConversationViewModel_LastMessageDateDisplay(object instance)
	{
		return ((ConversationViewModel)instance).LastMessageDateDisplay;
	}

	private object get_338_ConversationViewModel_CurrentUserBrush(object instance)
	{
		return ((ConversationViewModel)instance).CurrentUserBrush;
	}

	private object get_339_ConversationViewModel_CurrentUserLightBrush(object instance)
	{
		return ((ConversationViewModel)instance).CurrentUserLightBrush;
	}

	private object get_340_ConversationViewModel_OtherUserBrush(object instance)
	{
		return ((ConversationViewModel)instance).OtherUserBrush;
	}

	private object get_341_ConversationViewModel_OtherUserLightBrush(object instance)
	{
		return ((ConversationViewModel)instance).OtherUserLightBrush;
	}

	private object get_342_ConversationList_ItemWidth(object instance)
	{
		return ((ConversationList)instance).ItemWidth;
	}

	private object get_343_ConversationList_IsInbox(object instance)
	{
		return ((ConversationList)instance).IsInbox;
	}

	private void set_343_ConversationList_IsInbox(object instance, object Value)
	{
		((ConversationList)instance).IsInbox = (bool)Value;
	}

	private object get_344_ConversationList_IsBusy(object instance)
	{
		return ((ConversationList)instance).IsBusy;
	}

	private void set_344_ConversationList_IsBusy(object instance, object Value)
	{
		((ConversationList)instance).IsBusy = (bool)Value;
	}

	private object get_345_ConversationList_IsEmpty(object instance)
	{
		return ((ConversationList)instance).IsEmpty;
	}

	private void set_345_ConversationList_IsEmpty(object instance, object Value)
	{
		((ConversationList)instance).IsEmpty = (bool)Value;
	}

	private object get_346_ConversationList_HasError(object instance)
	{
		return ((ConversationList)instance).HasError;
	}

	private void set_346_ConversationList_HasError(object instance, object Value)
	{
		((ConversationList)instance).HasError = (bool)Value;
	}

	private object get_347_ConversationList_ErrorText(object instance)
	{
		return ((ConversationList)instance).ErrorText;
	}

	private void set_347_ConversationList_ErrorText(object instance, object Value)
	{
		((ConversationList)instance).ErrorText = (string)Value;
	}

	private object get_348_ConversationList_ShowRetry(object instance)
	{
		return ((ConversationList)instance).ShowRetry;
	}

	private void set_348_ConversationList_ShowRetry(object instance, object Value)
	{
		((ConversationList)instance).ShowRetry = (bool)Value;
	}

	private object get_349_ConversationList_HasNew(object instance)
	{
		return ((ConversationList)instance).HasNew;
	}

	private void set_349_ConversationList_HasNew(object instance, object Value)
	{
		((ConversationList)instance).HasNew = (bool)Value;
	}

	private object get_350_ConversationList_IsFinishedLoading(object instance)
	{
		return ((ConversationList)instance).IsFinishedLoading;
	}

	private void set_350_ConversationList_IsFinishedLoading(object instance, object Value)
	{
		((ConversationList)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_351_ConversationList_EmptyIcon(object instance)
	{
		return ((ConversationList)instance).EmptyIcon;
	}

	private object get_352_ConversationList_EmptyHeader(object instance)
	{
		return ((ConversationList)instance).EmptyHeader;
	}

	private object get_353_ConversationList_EmptyMessage(object instance)
	{
		return ((ConversationList)instance).EmptyMessage;
	}

	private object get_354_CaptchaView_IsFinishedLoading(object instance)
	{
		return ((CaptchaView)instance).IsFinishedLoading;
	}

	private void set_354_CaptchaView_IsFinishedLoading(object instance, object Value)
	{
		((CaptchaView)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_355_CaptchaView_IsLoading(object instance)
	{
		return ((CaptchaView)instance).IsLoading;
	}

	private void set_355_CaptchaView_IsLoading(object instance, object Value)
	{
		((CaptchaView)instance).IsLoading = (bool)Value;
	}

	private object get_356_CaptchaView_HasError(object instance)
	{
		return ((CaptchaView)instance).HasError;
	}

	private void set_356_CaptchaView_HasError(object instance, object Value)
	{
		((CaptchaView)instance).HasError = (bool)Value;
	}

	private object get_357_FriendFinderTemplateSelector_HeaderTemplate(object instance)
	{
		return ((FriendFinderTemplateSelector)instance).HeaderTemplate;
	}

	private void set_357_FriendFinderTemplateSelector_HeaderTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((FriendFinderTemplateSelector)instance).HeaderTemplate = (DataTemplate)Value;
	}

	private object get_358_FriendFinderTemplateSelector_UserTemplate(object instance)
	{
		return ((FriendFinderTemplateSelector)instance).UserTemplate;
	}

	private void set_358_FriendFinderTemplateSelector_UserTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((FriendFinderTemplateSelector)instance).UserTemplate = (DataTemplate)Value;
	}

	private object get_359_VineToggleButton_State(object instance)
	{
		return ((VineToggleButton)instance).State;
	}

	private void set_359_VineToggleButton_State(object instance, object Value)
	{
		((VineToggleButton)instance).State = (VineToggleButtonState)Value;
	}

	private object get_360_VineToggleButton_FollowingVisual(object instance)
	{
		return ((VineToggleButton)instance).FollowingVisual;
	}

	private void set_360_VineToggleButton_FollowingVisual(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineToggleButton)instance).FollowingVisual = (FrameworkElement)Value;
	}

	private object get_361_VineToggleButton_NotFollowingVisual(object instance)
	{
		return ((VineToggleButton)instance).NotFollowingVisual;
	}

	private void set_361_VineToggleButton_NotFollowingVisual(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineToggleButton)instance).NotFollowingVisual = (FrameworkElement)Value;
	}

	private object get_362_VineToggleButton_FollowRequestedVisual(object instance)
	{
		return ((VineToggleButton)instance).FollowRequestedVisual;
	}

	private void set_362_VineToggleButton_FollowRequestedVisual(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineToggleButton)instance).FollowRequestedVisual = (FrameworkElement)Value;
	}

	private void set_363_VineToggleButton_ActiveVisual(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineToggleButton)instance).ActiveVisual = (FrameworkElement)Value;
	}

	private object get_364_VineToggleButton_OnVisual(object instance)
	{
		return ((VineToggleButton)instance).OnVisual;
	}

	private void set_364_VineToggleButton_OnVisual(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineToggleButton)instance).OnVisual = (FrameworkElement)Value;
	}

	private object get_365_VineToggleButton_OffVisual(object instance)
	{
		return ((VineToggleButton)instance).OffVisual;
	}

	private void set_365_VineToggleButton_OffVisual(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineToggleButton)instance).OffVisual = (FrameworkElement)Value;
	}

	private object get_366_VineToggleButton_DisabledVisual(object instance)
	{
		return ((VineToggleButton)instance).DisabledVisual;
	}

	private void set_366_VineToggleButton_DisabledVisual(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineToggleButton)instance).DisabledVisual = (FrameworkElement)Value;
	}

	private object get_367_FriendFinderAllView_IsBusy(object instance)
	{
		return ((FriendFinderAllView)instance).IsBusy;
	}

	private void set_367_FriendFinderAllView_IsBusy(object instance, object Value)
	{
		((FriendFinderAllView)instance).IsBusy = (bool)Value;
	}

	private object get_368_FriendFinderAllView_IsNux(object instance)
	{
		return ((FriendFinderAllView)instance).IsNux;
	}

	private void set_368_FriendFinderAllView_IsNux(object instance, object Value)
	{
		((FriendFinderAllView)instance).IsNux = (bool)Value;
	}

	private object get_369_FriendFinderAllView_HasError(object instance)
	{
		return ((FriendFinderAllView)instance).HasError;
	}

	private void set_369_FriendFinderAllView_HasError(object instance, object Value)
	{
		((FriendFinderAllView)instance).HasError = (bool)Value;
	}

	private object get_370_FriendFinderAllView_ErrorText(object instance)
	{
		return ((FriendFinderAllView)instance).ErrorText;
	}

	private void set_370_FriendFinderAllView_ErrorText(object instance, object Value)
	{
		((FriendFinderAllView)instance).ErrorText = (string)Value;
	}

	private object get_371_FriendFinderAllView_ShowRetry(object instance)
	{
		return ((FriendFinderAllView)instance).ShowRetry;
	}

	private void set_371_FriendFinderAllView_ShowRetry(object instance, object Value)
	{
		((FriendFinderAllView)instance).ShowRetry = (bool)Value;
	}

	private object get_372_FriendFinderAllView_Items(object instance)
	{
		return ((FriendFinderAllView)instance).Items;
	}

	private void set_372_FriendFinderAllView_Items(object instance, object Value)
	{
		((FriendFinderAllView)instance).Items = (ObservableCollection<FriendFinderModel>)Value;
	}

	private object get_373_FriendFinderModel_Source(object instance)
	{
		return ((FriendFinderModel)instance).Source;
	}

	private void set_373_FriendFinderModel_Source(object instance, object Value)
	{
		((FriendFinderModel)instance).Source = (FriendFinderListSource)Value;
	}

	private object get_374_FriendFinderModel_HeaderText(object instance)
	{
		return ((FriendFinderModel)instance).HeaderText;
	}

	private void set_374_FriendFinderModel_HeaderText(object instance, object Value)
	{
		((FriendFinderModel)instance).HeaderText = (string)Value;
	}

	private object get_375_FriendFinderModel_SeeAllVisible(object instance)
	{
		return ((FriendFinderModel)instance).SeeAllVisible;
	}

	private void set_375_FriendFinderModel_SeeAllVisible(object instance, object Value)
	{
		((FriendFinderModel)instance).SeeAllVisible = (bool)Value;
	}

	private object get_376_FriendFinderModel_VineUserModel(object instance)
	{
		return ((FriendFinderModel)instance).VineUserModel;
	}

	private void set_376_FriendFinderModel_VineUserModel(object instance, object Value)
	{
		((FriendFinderModel)instance).VineUserModel = (VineUserModel)Value;
	}

	private object get_377_FriendFinderModel_IsHeader(object instance)
	{
		return ((FriendFinderModel)instance).IsHeader;
	}

	private object get_378_FriendFinderModel_IsUser(object instance)
	{
		return ((FriendFinderModel)instance).IsUser;
	}

	private object get_379_FriendFinderAllView_ListSource(object instance)
	{
		return ((FriendFinderAllView)instance).ListSource;
	}

	private void set_379_FriendFinderAllView_ListSource(object instance, object Value)
	{
		((FriendFinderAllView)instance).ListSource = (FriendFinderListSource)Value;
	}

	private object get_380_FriendFinderAllView_HeaderText(object instance)
	{
		return ((FriendFinderAllView)instance).HeaderText;
	}

	private object get_381_FriendFinderAllView_IsSuggestedVisible(object instance)
	{
		return ((FriendFinderAllView)instance).IsSuggestedVisible;
	}

	private object get_382_FriendFinderAllView_IsScrollViewVisible(object instance)
	{
		return ((FriendFinderAllView)instance).IsScrollViewVisible;
	}

	private object get_383_FriendFinderAllView_IsFinishedLoading(object instance)
	{
		return ((FriendFinderAllView)instance).IsFinishedLoading;
	}

	private void set_383_FriendFinderAllView_IsFinishedLoading(object instance, object Value)
	{
		((FriendFinderAllView)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_384_FriendFinderView_IsBusy(object instance)
	{
		return ((FriendFinderView)instance).IsBusy;
	}

	private void set_384_FriendFinderView_IsBusy(object instance, object Value)
	{
		((FriendFinderView)instance).IsBusy = (bool)Value;
	}

	private object get_385_FriendFinderView_IsNux(object instance)
	{
		return ((FriendFinderView)instance).IsNux;
	}

	private void set_385_FriendFinderView_IsNux(object instance, object Value)
	{
		((FriendFinderView)instance).IsNux = (bool)Value;
	}

	private object get_386_FriendFinderView_PlaceholderVisibility(object instance)
	{
		return ((FriendFinderView)instance).PlaceholderVisibility;
	}

	private void set_386_FriendFinderView_PlaceholderVisibility(object instance, object Value)
	{
		((FriendFinderView)instance).PlaceholderVisibility = (bool)Value;
	}

	private object get_387_FriendFinderView_TwitterEnabled(object instance)
	{
		return ((FriendFinderView)instance).TwitterEnabled;
	}

	private void set_387_FriendFinderView_TwitterEnabled(object instance, object Value)
	{
		((FriendFinderView)instance).TwitterEnabled = (bool)Value;
	}

	private object get_388_FriendFinderView_ContactsEnabled(object instance)
	{
		return ((FriendFinderView)instance).ContactsEnabled;
	}

	private void set_388_FriendFinderView_ContactsEnabled(object instance, object Value)
	{
		((FriendFinderView)instance).ContactsEnabled = (bool)Value;
	}

	private object get_389_FriendFinderView_ConnectAccountsVisble(object instance)
	{
		return ((FriendFinderView)instance).ConnectAccountsVisble;
	}

	private object get_390_FriendFinderView_HasError(object instance)
	{
		return ((FriendFinderView)instance).HasError;
	}

	private void set_390_FriendFinderView_HasError(object instance, object Value)
	{
		((FriendFinderView)instance).HasError = (bool)Value;
	}

	private object get_391_FriendFinderView_ErrorText(object instance)
	{
		return ((FriendFinderView)instance).ErrorText;
	}

	private void set_391_FriendFinderView_ErrorText(object instance, object Value)
	{
		((FriendFinderView)instance).ErrorText = (string)Value;
	}

	private object get_392_FriendFinderView_ShowRetry(object instance)
	{
		return ((FriendFinderView)instance).ShowRetry;
	}

	private void set_392_FriendFinderView_ShowRetry(object instance, object Value)
	{
		((FriendFinderView)instance).ShowRetry = (bool)Value;
	}

	private object get_393_FriendFinderView_IsFinishedLoading(object instance)
	{
		return ((FriendFinderView)instance).IsFinishedLoading;
	}

	private void set_393_FriendFinderView_IsFinishedLoading(object instance, object Value)
	{
		((FriendFinderView)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_394_FriendFinderView_Items(object instance)
	{
		return ((FriendFinderView)instance).Items;
	}

	private void set_394_FriendFinderView_Items(object instance, object Value)
	{
		((FriendFinderView)instance).Items = (ObservableCollection<FriendFinderModel>)Value;
	}

	private object get_395_SearchResultTemplateSelector_HeaderTemplate(object instance)
	{
		return ((SearchResultTemplateSelector)instance).HeaderTemplate;
	}

	private void set_395_SearchResultTemplateSelector_HeaderTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((SearchResultTemplateSelector)instance).HeaderTemplate = (DataTemplate)Value;
	}

	private object get_396_SearchResultTemplateSelector_SuggestedSearchTermTemplate(object instance)
	{
		return ((SearchResultTemplateSelector)instance).SuggestedSearchTermTemplate;
	}

	private void set_396_SearchResultTemplateSelector_SuggestedSearchTermTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((SearchResultTemplateSelector)instance).SuggestedSearchTermTemplate = (DataTemplate)Value;
	}

	private object get_397_SearchResultTemplateSelector_UserTemplate(object instance)
	{
		return ((SearchResultTemplateSelector)instance).UserTemplate;
	}

	private void set_397_SearchResultTemplateSelector_UserTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((SearchResultTemplateSelector)instance).UserTemplate = (DataTemplate)Value;
	}

	private object get_398_SearchResultTemplateSelector_TagTemplate(object instance)
	{
		return ((SearchResultTemplateSelector)instance).TagTemplate;
	}

	private void set_398_SearchResultTemplateSelector_TagTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((SearchResultTemplateSelector)instance).TagTemplate = (DataTemplate)Value;
	}

	private object get_399_SearchResultTemplateSelector_RecentTemplate(object instance)
	{
		return ((SearchResultTemplateSelector)instance).RecentTemplate;
	}

	private void set_399_SearchResultTemplateSelector_RecentTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((SearchResultTemplateSelector)instance).RecentTemplate = (DataTemplate)Value;
	}

	private object get_400_SearchResultTemplateSelector_VineTemplate(object instance)
	{
		return ((SearchResultTemplateSelector)instance).VineTemplate;
	}

	private void set_400_SearchResultTemplateSelector_VineTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((SearchResultTemplateSelector)instance).VineTemplate = (DataTemplate)Value;
	}

	private object get_401_SearchControl_ControlWrapper(object instance)
	{
		return ((SearchControl)instance).ControlWrapper;
	}

	private void set_401_SearchControl_ControlWrapper(object instance, object Value)
	{
		((SearchControl)instance).ControlWrapper = (UserControlWrapper)Value;
	}

	private object get_402_SearchControl_SearchResults(object instance)
	{
		return ((SearchControl)instance).SearchResults;
	}

	private void set_402_SearchControl_SearchResults(object instance, object Value)
	{
		((SearchControl)instance).SearchResults = (ObservableCollection<SearchResultModel>)Value;
	}

	private object get_403_SearchResultModel_Suggestion(object instance)
	{
		return ((SearchResultModel)instance).Suggestion;
	}

	private void set_403_SearchResultModel_Suggestion(object instance, object Value)
	{
		((SearchResultModel)instance).Suggestion = (VineSearchSuggestions)Value;
	}

	private object get_404_SearchResultModel_User(object instance)
	{
		return ((SearchResultModel)instance).User;
	}

	private void set_404_SearchResultModel_User(object instance, object Value)
	{
		((SearchResultModel)instance).User = (VineUserModel)Value;
	}

	private object get_405_SearchResultModel_Tag(object instance)
	{
		return ((SearchResultModel)instance).Tag;
	}

	private void set_405_SearchResultModel_Tag(object instance, object Value)
	{
		((SearchResultModel)instance).Tag = (VineTagModel)Value;
	}

	private object get_406_SearchResultModel_Recent(object instance)
	{
		return ((SearchResultModel)instance).Recent;
	}

	private void set_406_SearchResultModel_Recent(object instance, object Value)
	{
		((SearchResultModel)instance).Recent = (VineRecentSearch)Value;
	}

	private object get_407_SearchResultModel_HeaderViewAllVisible(object instance)
	{
		return ((SearchResultModel)instance).HeaderViewAllVisible;
	}

	private void set_407_SearchResultModel_HeaderViewAllVisible(object instance, object Value)
	{
		((SearchResultModel)instance).HeaderViewAllVisible = (bool)Value;
	}

	private object get_408_SearchResultModel_HeaderText(object instance)
	{
		return ((SearchResultModel)instance).HeaderText;
	}

	private void set_408_SearchResultModel_HeaderText(object instance, object Value)
	{
		((SearchResultModel)instance).HeaderText = (string)Value;
	}

	private object get_409_SearchResultModel_SearchType(object instance)
	{
		return ((SearchResultModel)instance).SearchType;
	}

	private void set_409_SearchResultModel_SearchType(object instance, object Value)
	{
		((SearchResultModel)instance).SearchType = (SearchType)Value;
	}

	private object get_410_SearchResultModel_Vine(object instance)
	{
		return ((SearchResultModel)instance).Vine;
	}

	private void set_410_SearchResultModel_Vine(object instance, object Value)
	{
		((SearchResultModel)instance).Vine = (VineModel)Value;
	}

	private object get_411_SearchResultModel_IsSearchSuggestion(object instance)
	{
		return ((SearchResultModel)instance).IsSearchSuggestion;
	}

	private object get_412_SearchResultModel_IsHeader(object instance)
	{
		return ((SearchResultModel)instance).IsHeader;
	}

	private object get_413_SearchResultModel_IsUser(object instance)
	{
		return ((SearchResultModel)instance).IsUser;
	}

	private object get_414_SearchResultModel_IsTag(object instance)
	{
		return ((SearchResultModel)instance).IsTag;
	}

	private object get_415_SearchResultModel_IsVine(object instance)
	{
		return ((SearchResultModel)instance).IsVine;
	}

	private object get_416_SearchResultModel_IsRecent(object instance)
	{
		return ((SearchResultModel)instance).IsRecent;
	}

	private object get_417_SearchControl_Pending(object instance)
	{
		return ((SearchControl)instance).Pending;
	}

	private void set_417_SearchControl_Pending(object instance, object Value)
	{
		((SearchControl)instance).Pending = (CancellationTokenSource)Value;
	}

	private object get_418_SearchControl_RecentSearches(object instance)
	{
		return ((SearchControl)instance).RecentSearches;
	}

	private void set_418_SearchControl_RecentSearches(object instance, object Value)
	{
		((SearchControl)instance).RecentSearches = (ObservableCollection<VineRecentSearch>)Value;
	}

	private object get_419_VineRecentSearch_Query(object instance)
	{
		return ((VineRecentSearch)instance).Query;
	}

	private void set_419_VineRecentSearch_Query(object instance, object Value)
	{
		((VineRecentSearch)instance).Query = (string)Value;
	}

	private object get_420_SearchControl_IsBusy(object instance)
	{
		return ((SearchControl)instance).IsBusy;
	}

	private void set_420_SearchControl_IsBusy(object instance, object Value)
	{
		((SearchControl)instance).IsBusy = (bool)Value;
	}

	private object get_421_SearchControl_IsEmpty(object instance)
	{
		return ((SearchControl)instance).IsEmpty;
	}

	private void set_421_SearchControl_IsEmpty(object instance, object Value)
	{
		((SearchControl)instance).IsEmpty = (bool)Value;
	}

	private object get_422_SearchControl_HasError(object instance)
	{
		return ((SearchControl)instance).HasError;
	}

	private void set_422_SearchControl_HasError(object instance, object Value)
	{
		((SearchControl)instance).HasError = (bool)Value;
	}

	private object get_423_SearchControl_ShowError(object instance)
	{
		return ((SearchControl)instance).ShowError;
	}

	private object get_424_SearchControl_ShowRetry(object instance)
	{
		return ((SearchControl)instance).ShowRetry;
	}

	private void set_424_SearchControl_ShowRetry(object instance, object Value)
	{
		((SearchControl)instance).ShowRetry = (bool)Value;
	}

	private object get_425_SearchControl_ErrorText(object instance)
	{
		return ((SearchControl)instance).ErrorText;
	}

	private void set_425_SearchControl_ErrorText(object instance, object Value)
	{
		((SearchControl)instance).ErrorText = (string)Value;
	}

	private object get_426_SearchControl_SearchListVisible(object instance)
	{
		return ((SearchControl)instance).SearchListVisible;
	}

	private void set_426_SearchControl_SearchListVisible(object instance, object Value)
	{
		((SearchControl)instance).SearchListVisible = (bool)Value;
	}

	private object get_427_SearchControl_SearchText(object instance)
	{
		return ((SearchControl)instance).SearchText;
	}

	private void set_427_SearchControl_SearchText(object instance, object Value)
	{
		((SearchControl)instance).SearchText = (string)Value;
	}

	private object get_428_SearchControl_LastSearchText(object instance)
	{
		return ((SearchControl)instance).LastSearchText;
	}

	private void set_428_SearchControl_LastSearchText(object instance, object Value)
	{
		((SearchControl)instance).LastSearchText = (string)Value;
	}

	private object get_429_SearchControl_PlaceHolderLabel(object instance)
	{
		return ((SearchControl)instance).PlaceHolderLabel;
	}

	private object get_430_SearchControl_EmptyText(object instance)
	{
		return ((SearchControl)instance).EmptyText;
	}

	private object get_431_SearchControl_TileText(object instance)
	{
		return ((SearchControl)instance).TileText;
	}

	private void set_431_SearchControl_TileText(object instance, object Value)
	{
		((SearchControl)instance).TileText = (string)Value;
	}

	private object get_432_SearchControl_HasTile(object instance)
	{
		return ((SearchControl)instance).HasTile;
	}

	private object get_433_SearchTagsAllView_Items(object instance)
	{
		return ((SearchTagsAllView)instance).Items;
	}

	private void set_433_SearchTagsAllView_Items(object instance, object Value)
	{
		((SearchTagsAllView)instance).Items = (IncrementalLoadingCollection<VineTagModel>)Value;
	}

	private object get_434_VineTagModel_TagId(object instance)
	{
		return ((VineTagModel)instance).TagId;
	}

	private void set_434_VineTagModel_TagId(object instance, object Value)
	{
		((VineTagModel)instance).TagId = (string)Value;
	}

	private object get_435_VineTagModel_Tag(object instance)
	{
		return ((VineTagModel)instance).Tag;
	}

	private void set_435_VineTagModel_Tag(object instance, object Value)
	{
		((VineTagModel)instance).Tag = (string)Value;
	}

	private object get_436_VineTagModel_PostCount(object instance)
	{
		return ((VineTagModel)instance).PostCount;
	}

	private void set_436_VineTagModel_PostCount(object instance, object Value)
	{
		((VineTagModel)instance).PostCount = (int)Value;
	}

	private object get_437_VineTagModel_FormattedTag(object instance)
	{
		return ((VineTagModel)instance).FormattedTag;
	}

	private object get_438_VineTagModel_FormattedPostCount(object instance)
	{
		return ((VineTagModel)instance).FormattedPostCount;
	}

	private object get_439_SearchTagsAllView_IsFinishedLoading(object instance)
	{
		return ((SearchTagsAllView)instance).IsFinishedLoading;
	}

	private void set_439_SearchTagsAllView_IsFinishedLoading(object instance, object Value)
	{
		((SearchTagsAllView)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_440_SearchTagsAllView_IsEmpty(object instance)
	{
		return ((SearchTagsAllView)instance).IsEmpty;
	}

	private void set_440_SearchTagsAllView_IsEmpty(object instance, object Value)
	{
		((SearchTagsAllView)instance).IsEmpty = (bool)Value;
	}

	private object get_441_SearchTagsAllView_ShowRetry(object instance)
	{
		return ((SearchTagsAllView)instance).ShowRetry;
	}

	private void set_441_SearchTagsAllView_ShowRetry(object instance, object Value)
	{
		((SearchTagsAllView)instance).ShowRetry = (bool)Value;
	}

	private object get_442_SearchTagsAllView_HasError(object instance)
	{
		return ((SearchTagsAllView)instance).HasError;
	}

	private void set_442_SearchTagsAllView_HasError(object instance, object Value)
	{
		((SearchTagsAllView)instance).HasError = (bool)Value;
	}

	private object get_443_SearchTagsAllView_PageTitle(object instance)
	{
		return ((SearchTagsAllView)instance).PageTitle;
	}

	private void set_443_SearchTagsAllView_PageTitle(object instance, object Value)
	{
		((SearchTagsAllView)instance).PageTitle = (string)Value;
	}

	private object get_444_SearchTagsAllView_IsBusy(object instance)
	{
		return ((SearchTagsAllView)instance).IsBusy;
	}

	private void set_444_SearchTagsAllView_IsBusy(object instance, object Value)
	{
		((SearchTagsAllView)instance).IsBusy = (bool)Value;
	}

	private object get_445_SearchTagsAllView_EmptyText(object instance)
	{
		return ((SearchTagsAllView)instance).EmptyText;
	}

	private void set_445_SearchTagsAllView_EmptyText(object instance, object Value)
	{
		((SearchTagsAllView)instance).EmptyText = (string)Value;
	}

	private object get_446_SearchTagsAllView_ErrorText(object instance)
	{
		return ((SearchTagsAllView)instance).ErrorText;
	}

	private void set_446_SearchTagsAllView_ErrorText(object instance, object Value)
	{
		((SearchTagsAllView)instance).ErrorText = (string)Value;
	}

	private object get_447_SettingsPrivacyView_User(object instance)
	{
		return ((SettingsPrivacyView)instance).User;
	}

	private void set_447_SettingsPrivacyView_User(object instance, object Value)
	{
		((SettingsPrivacyView)instance).User = (VineUserModel)Value;
	}

	private object get_448_SettingsPrivacyView_IsError(object instance)
	{
		return ((SettingsPrivacyView)instance).IsError;
	}

	private void set_448_SettingsPrivacyView_IsError(object instance, object Value)
	{
		((SettingsPrivacyView)instance).IsError = (bool)Value;
	}

	private object get_449_SettingsPrivacyView_ErrorText(object instance)
	{
		return ((SettingsPrivacyView)instance).ErrorText;
	}

	private void set_449_SettingsPrivacyView_ErrorText(object instance, object Value)
	{
		((SettingsPrivacyView)instance).ErrorText = (string)Value;
	}

	private object get_450_SettingsPrivacyView_ShowRetry(object instance)
	{
		return ((SettingsPrivacyView)instance).ShowRetry;
	}

	private void set_450_SettingsPrivacyView_ShowRetry(object instance, object Value)
	{
		((SettingsPrivacyView)instance).ShowRetry = (bool)Value;
	}

	private object get_451_SettingsPrivacyView_IsLoaded(object instance)
	{
		return ((SettingsPrivacyView)instance).IsLoaded;
	}

	private void set_451_SettingsPrivacyView_IsLoaded(object instance, object Value)
	{
		((SettingsPrivacyView)instance).IsLoaded = (bool)Value;
	}

	private object get_452_ContactTemplateSelector_ContactTemplate(object instance)
	{
		return ((ContactTemplateSelector)instance).ContactTemplate;
	}

	private void set_452_ContactTemplateSelector_ContactTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((ContactTemplateSelector)instance).ContactTemplate = (DataTemplate)Value;
	}

	private object get_453_ContactTemplateSelector_VineUserTemplate(object instance)
	{
		return ((ContactTemplateSelector)instance).VineUserTemplate;
	}

	private void set_453_ContactTemplateSelector_VineUserTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((ContactTemplateSelector)instance).VineUserTemplate = (DataTemplate)Value;
	}

	private object get_454_ContactTemplateSelector_HeaderTemplate(object instance)
	{
		return ((ContactTemplateSelector)instance).HeaderTemplate;
	}

	private void set_454_ContactTemplateSelector_HeaderTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((ContactTemplateSelector)instance).HeaderTemplate = (DataTemplate)Value;
	}

	private object get_455_ShareMessageControl_SelectedItems(object instance)
	{
		return ((ShareMessageControl)instance).SelectedItems;
	}

	private void set_455_ShareMessageControl_SelectedItems(object instance, object Value)
	{
		((ShareMessageControl)instance).SelectedItems = (List<VineContactViewModel>)Value;
	}

	private object get_456_VineContactViewModel_Section(object instance)
	{
		return ((VineContactViewModel)instance).Section;
	}

	private void set_456_VineContactViewModel_Section(object instance, object Value)
	{
		((VineContactViewModel)instance).Section = (string)Value;
	}

	private object get_457_VineContactViewModel_Emails(object instance)
	{
		return ((VineContactViewModel)instance).Emails;
	}

	private void set_457_VineContactViewModel_Emails(object instance, object Value)
	{
		((VineContactViewModel)instance).Emails = (List<string>)Value;
	}

	private object get_458_VineContactViewModel_Phones(object instance)
	{
		return ((VineContactViewModel)instance).Phones;
	}

	private void set_458_VineContactViewModel_Phones(object instance, object Value)
	{
		((VineContactViewModel)instance).Phones = (List<Tuple<string, string>>)Value;
	}

	private object get_459_Tuple_Item1(object instance)
	{
		return ((Tuple<string, string>)instance).Item1;
	}

	private object get_460_Tuple_Item2(object instance)
	{
		return ((Tuple<string, string>)instance).Item2;
	}

	private object get_461_VineContactViewModel_SelectionVisibility(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((VineContactViewModel)instance).SelectionVisibility;
	}

	private void set_461_VineContactViewModel_SelectionVisibility(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		((VineContactViewModel)instance).SelectionVisibility = (Visibility)Value;
	}

	private object get_462_VineContactViewModel_User(object instance)
	{
		return ((VineContactViewModel)instance).User;
	}

	private void set_462_VineContactViewModel_User(object instance, object Value)
	{
		((VineContactViewModel)instance).User = (VineUserModel)Value;
	}

	private object get_463_VineContactViewModel_UserVisibility(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((VineContactViewModel)instance).UserVisibility;
	}

	private void set_463_VineContactViewModel_UserVisibility(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		((VineContactViewModel)instance).UserVisibility = (Visibility)Value;
	}

	private object get_464_VineContactViewModel_PhoneSelectionVisibility(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((VineContactViewModel)instance).PhoneSelectionVisibility;
	}

	private void set_464_VineContactViewModel_PhoneSelectionVisibility(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		((VineContactViewModel)instance).PhoneSelectionVisibility = (Visibility)Value;
	}

	private object get_465_VineContactViewModel_PhoneVisibility(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((VineContactViewModel)instance).PhoneVisibility;
	}

	private void set_465_VineContactViewModel_PhoneVisibility(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		((VineContactViewModel)instance).PhoneVisibility = (Visibility)Value;
	}

	private object get_466_VineContactViewModel_EmailSelectionVisibility(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((VineContactViewModel)instance).EmailSelectionVisibility;
	}

	private void set_466_VineContactViewModel_EmailSelectionVisibility(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		((VineContactViewModel)instance).EmailSelectionVisibility = (Visibility)Value;
	}

	private object get_467_VineContactViewModel_EmailVisibility(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((VineContactViewModel)instance).EmailVisibility;
	}

	private void set_467_VineContactViewModel_EmailVisibility(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		((VineContactViewModel)instance).EmailVisibility = (Visibility)Value;
	}

	private object get_468_VineContactViewModel_HeaderText(object instance)
	{
		return ((VineContactViewModel)instance).HeaderText;
	}

	private void set_468_VineContactViewModel_HeaderText(object instance, object Value)
	{
		((VineContactViewModel)instance).HeaderText = (string)Value;
	}

	private object get_469_VineContactViewModel_IsSelected(object instance)
	{
		return ((VineContactViewModel)instance).IsSelected;
	}

	private void set_469_VineContactViewModel_IsSelected(object instance, object Value)
	{
		((VineContactViewModel)instance).IsSelected = (bool)Value;
	}

	private object get_470_ShareMessageControl_SearchText(object instance)
	{
		return ((ShareMessageControl)instance).SearchText;
	}

	private void set_470_ShareMessageControl_SearchText(object instance, object Value)
	{
		((ShareMessageControl)instance).SearchText = (string)Value;
	}

	private object get_471_ShareMessageControl_IsPplFinishedLoading(object instance)
	{
		return ((ShareMessageControl)instance).IsPplFinishedLoading;
	}

	private void set_471_ShareMessageControl_IsPplFinishedLoading(object instance, object Value)
	{
		((ShareMessageControl)instance).IsPplFinishedLoading = (bool)Value;
	}

	private object get_472_ShareMessageControl_IsSingleSelect(object instance)
	{
		return ((ShareMessageControl)instance).IsSingleSelect;
	}

	private void set_472_ShareMessageControl_IsSingleSelect(object instance, object Value)
	{
		((ShareMessageControl)instance).IsSingleSelect = (bool)Value;
	}

	private object get_473_ShareMessageControl_PplHasError(object instance)
	{
		return ((ShareMessageControl)instance).PplHasError;
	}

	private void set_473_ShareMessageControl_PplHasError(object instance, object Value)
	{
		((ShareMessageControl)instance).PplHasError = (bool)Value;
	}

	private object get_474_ShareMessageControl_PplIsEmpty(object instance)
	{
		return ((ShareMessageControl)instance).PplIsEmpty;
	}

	private void set_474_ShareMessageControl_PplIsEmpty(object instance, object Value)
	{
		((ShareMessageControl)instance).PplIsEmpty = (bool)Value;
	}

	private object get_475_ShareMessageControl_Pending(object instance)
	{
		return ((ShareMessageControl)instance).Pending;
	}

	private void set_475_ShareMessageControl_Pending(object instance, object Value)
	{
		((ShareMessageControl)instance).Pending = (CancellationTokenSource)Value;
	}

	private object get_476_ShareMessageControl_Friends(object instance)
	{
		return ((ShareMessageControl)instance).Friends;
	}

	private void set_476_ShareMessageControl_Friends(object instance, object Value)
	{
		((ShareMessageControl)instance).Friends = (IncrementalLoadingCollection<VineContactViewModel>)Value;
	}

	private object get_477_ShareMessageControl_Contacts(object instance)
	{
		return ((ShareMessageControl)instance).Contacts;
	}

	private void set_477_ShareMessageControl_Contacts(object instance, object Value)
	{
		((ShareMessageControl)instance).Contacts = (ObservableCollection<VineContactViewModel>)Value;
	}

	private object get_478_ShareMessageControl_IsFriendsEmpty(object instance)
	{
		return ((ShareMessageControl)instance).IsFriendsEmpty;
	}

	private void set_478_ShareMessageControl_IsFriendsEmpty(object instance, object Value)
	{
		((ShareMessageControl)instance).IsFriendsEmpty = (bool)Value;
	}

	private object get_479_ShareMessageControl_IsContactsEmpty(object instance)
	{
		return ((ShareMessageControl)instance).IsContactsEmpty;
	}

	private void set_479_ShareMessageControl_IsContactsEmpty(object instance, object Value)
	{
		((ShareMessageControl)instance).IsContactsEmpty = (bool)Value;
	}

	private object get_480_ShareMessageControl_FriendsEmptyVisibility(object instance)
	{
		return ((ShareMessageControl)instance).FriendsEmptyVisibility;
	}

	private object get_481_ShareMessageControl_ContactsEmptyVisibility(object instance)
	{
		return ((ShareMessageControl)instance).ContactsEmptyVisibility;
	}

	private object get_482_ShareMessageControl_IsBusy(object instance)
	{
		return ((ShareMessageControl)instance).IsBusy;
	}

	private void set_482_ShareMessageControl_IsBusy(object instance, object Value)
	{
		((ShareMessageControl)instance).IsBusy = (bool)Value;
	}

	private object get_483_ShareMessageControl_HasError(object instance)
	{
		return ((ShareMessageControl)instance).HasError;
	}

	private void set_483_ShareMessageControl_HasError(object instance, object Value)
	{
		((ShareMessageControl)instance).HasError = (bool)Value;
	}

	private object get_484_ShareMessageControl_ErrorText(object instance)
	{
		return ((ShareMessageControl)instance).ErrorText;
	}

	private void set_484_ShareMessageControl_ErrorText(object instance, object Value)
	{
		((ShareMessageControl)instance).ErrorText = (string)Value;
	}

	private object get_485_ShareMessageControl_ShowRetry(object instance)
	{
		return ((ShareMessageControl)instance).ShowRetry;
	}

	private void set_485_ShareMessageControl_ShowRetry(object instance, object Value)
	{
		((ShareMessageControl)instance).ShowRetry = (bool)Value;
	}

	private object get_486_ShareMessageControl_ErrorVisibility(object instance)
	{
		return ((ShareMessageControl)instance).ErrorVisibility;
	}

	private object get_487_ShareMessageControl_IsFinishedLoading(object instance)
	{
		return ((ShareMessageControl)instance).IsFinishedLoading;
	}

	private void set_487_ShareMessageControl_IsFinishedLoading(object instance, object Value)
	{
		((ShareMessageControl)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_488_ShareMessageView_TitleText(object instance)
	{
		return ((ShareMessageView)instance).TitleText;
	}

	private void set_488_ShareMessageView_TitleText(object instance, object Value)
	{
		((ShareMessageView)instance).TitleText = (string)Value;
	}

	private object get_489_ShareMessageView_TutorialHintVisibility(object instance)
	{
		return ((ShareMessageView)instance).TutorialHintVisibility;
	}

	private void set_489_ShareMessageView_TutorialHintVisibility(object instance, object Value)
	{
		((ShareMessageView)instance).TutorialHintVisibility = (bool)Value;
	}

	private object get_490_ShareMessageView_HeaderBrush(object instance)
	{
		return ((ShareMessageView)instance).HeaderBrush;
	}

	private void set_490_ShareMessageView_HeaderBrush(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((ShareMessageView)instance).HeaderBrush = (Brush)Value;
	}

	private object get_491_TappedToLikeControl_ZRotation(object instance)
	{
		return ((TappedToLikeControl)instance).ZRotation;
	}

	private object get_492_UpgradeView_IsBusy(object instance)
	{
		return ((UpgradeView)instance).IsBusy;
	}

	private void set_492_UpgradeView_IsBusy(object instance, object Value)
	{
		((UpgradeView)instance).IsBusy = (bool)Value;
	}

	private object get_493_VerifyPhoneCodeEnterView_IsBusy(object instance)
	{
		return ((VerifyPhoneCodeEnterView)instance).IsBusy;
	}

	private void set_493_VerifyPhoneCodeEnterView_IsBusy(object instance, object Value)
	{
		((VerifyPhoneCodeEnterView)instance).IsBusy = (bool)Value;
	}

	private object get_494_VerifyPhoneCodeEnterView_RetryText(object instance)
	{
		return ((VerifyPhoneCodeEnterView)instance).RetryText;
	}

	private object get_495_VerifyPhoneCodeEnterView_User(object instance)
	{
		return ((VerifyPhoneCodeEnterView)instance).User;
	}

	private object get_496_VerifyPhoneCodeEnterView_HeaderText(object instance)
	{
		return ((VerifyPhoneCodeEnterView)instance).HeaderText;
	}

	private void set_496_VerifyPhoneCodeEnterView_HeaderText(object instance, object Value)
	{
		((VerifyPhoneCodeEnterView)instance).HeaderText = (string)Value;
	}

	private object get_497_VerifyEmailCodeEnterView_IsBusy(object instance)
	{
		return ((VerifyEmailCodeEnterView)instance).IsBusy;
	}

	private void set_497_VerifyEmailCodeEnterView_IsBusy(object instance, object Value)
	{
		((VerifyEmailCodeEnterView)instance).IsBusy = (bool)Value;
	}

	private object get_498_VerifyEmailCodeEnterView_RetryText(object instance)
	{
		return ((VerifyEmailCodeEnterView)instance).RetryText;
	}

	private object get_499_VerifyEmailCodeEnterView_User(object instance)
	{
		return ((VerifyEmailCodeEnterView)instance).User;
	}

	private void set_499_VerifyEmailCodeEnterView_User(object instance, object Value)
	{
		((VerifyEmailCodeEnterView)instance).User = (VineUserModel)Value;
	}

	private object get_500_VerifyEmailCodeEnterView_HeaderText(object instance)
	{
		return ((VerifyEmailCodeEnterView)instance).HeaderText;
	}

	private void set_500_VerifyEmailCodeEnterView_HeaderText(object instance, object Value)
	{
		((VerifyEmailCodeEnterView)instance).HeaderText = (string)Value;
	}

	private object get_501_ExploreControl_IsFinishedLoading(object instance)
	{
		return ((ExploreControl)instance).IsFinishedLoading;
	}

	private void set_501_ExploreControl_IsFinishedLoading(object instance, object Value)
	{
		((ExploreControl)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_502_ExploreControl_IsLoading(object instance)
	{
		return ((ExploreControl)instance).IsLoading;
	}

	private void set_502_ExploreControl_IsLoading(object instance, object Value)
	{
		((ExploreControl)instance).IsLoading = (bool)Value;
	}

	private object get_503_ExploreControl_HasError(object instance)
	{
		return ((ExploreControl)instance).HasError;
	}

	private void set_503_ExploreControl_HasError(object instance, object Value)
	{
		((ExploreControl)instance).HasError = (bool)Value;
	}

	private object get_504_ExploreControl_BrowserVisible(object instance)
	{
		return ((ExploreControl)instance).BrowserVisible;
	}

	private object get_505_ExploreControl_SearchActive(object instance)
	{
		return ((ExploreControl)instance).SearchActive;
	}

	private void set_505_ExploreControl_SearchActive(object instance, object Value)
	{
		((ExploreControl)instance).SearchActive = (bool)Value;
	}

	private object get_506_ExploreControl_ErrorText(object instance)
	{
		return ((ExploreControl)instance).ErrorText;
	}

	private void set_506_ExploreControl_ErrorText(object instance, object Value)
	{
		((ExploreControl)instance).ErrorText = (string)Value;
	}

	private object get_507_ExploreControl_ShowRetry(object instance)
	{
		return ((ExploreControl)instance).ShowRetry;
	}

	private void set_507_ExploreControl_ShowRetry(object instance, object Value)
	{
		((ExploreControl)instance).ShowRetry = (bool)Value;
	}

	private object get_508_Behavior_AssociatedObject(object instance)
	{
		return ((Behavior<PasswordBox>)instance).AssociatedObject;
	}

	private void set_508_Behavior_AssociatedObject(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((Behavior<PasswordBox>)instance).AssociatedObject = (DependencyObject)Value;
	}

	private object get_509_Behavior_Object(object instance)
	{
		return ((Behavior<PasswordBox>)instance).Object;
	}

	private object get_510_SignUpEmailDetailsView_User(object instance)
	{
		return ((SignUpEmailDetailsView)instance).User;
	}

	private object get_511_SignUpEmailDetailsView_Email(object instance)
	{
		return ((SignUpEmailDetailsView)instance).Email;
	}

	private void set_511_SignUpEmailDetailsView_Email(object instance, object Value)
	{
		((SignUpEmailDetailsView)instance).Email = (string)Value;
	}

	private object get_512_SignUpEmailDetailsView_PhoneNumber(object instance)
	{
		return ((SignUpEmailDetailsView)instance).PhoneNumber;
	}

	private void set_512_SignUpEmailDetailsView_PhoneNumber(object instance, object Value)
	{
		((SignUpEmailDetailsView)instance).PhoneNumber = (string)Value;
	}

	private object get_513_SignUpEmailDetailsView_Password(object instance)
	{
		return ((SignUpEmailDetailsView)instance).Password;
	}

	private void set_513_SignUpEmailDetailsView_Password(object instance, object Value)
	{
		((SignUpEmailDetailsView)instance).Password = (string)Value;
	}

	private object get_514_SignUpEmailDetailsView_IsBusy(object instance)
	{
		return ((SignUpEmailDetailsView)instance).IsBusy;
	}

	private void set_514_SignUpEmailDetailsView_IsBusy(object instance, object Value)
	{
		((SignUpEmailDetailsView)instance).IsBusy = (bool)Value;
	}

	private object get_515_SignUpEmailDetailsView_ErrorText(object instance)
	{
		return ((SignUpEmailDetailsView)instance).ErrorText;
	}

	private void set_515_SignUpEmailDetailsView_ErrorText(object instance, object Value)
	{
		((SignUpEmailDetailsView)instance).ErrorText = (string)Value;
	}

	private object get_516_SignUpEmailDetailsView_IsError(object instance)
	{
		return ((SignUpEmailDetailsView)instance).IsError;
	}

	private void set_516_SignUpEmailDetailsView_IsError(object instance, object Value)
	{
		((SignUpEmailDetailsView)instance).IsError = (bool)Value;
	}

	private object get_517_SignUpEmailDetailsView_IsFinishedLoading(object instance)
	{
		return ((SignUpEmailDetailsView)instance).IsFinishedLoading;
	}

	private void set_517_SignUpEmailDetailsView_IsFinishedLoading(object instance, object Value)
	{
		((SignUpEmailDetailsView)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_518_SignUpEmailView_User(object instance)
	{
		return ((SignUpEmailView)instance).User;
	}

	private void set_518_SignUpEmailView_User(object instance, object Value)
	{
		((SignUpEmailView)instance).User = (VineUserModel)Value;
	}

	private object get_519_SignUpEmailView_IsFinishedLoading(object instance)
	{
		return ((SignUpEmailView)instance).IsFinishedLoading;
	}

	private void set_519_SignUpEmailView_IsFinishedLoading(object instance, object Value)
	{
		((SignUpEmailView)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_520_VineMessagesInbox_TutorialHintVisibility(object instance)
	{
		return ((VineMessagesInbox)instance).TutorialHintVisibility;
	}

	private void set_520_VineMessagesInbox_TutorialHintVisibility(object instance, object Value)
	{
		((VineMessagesInbox)instance).TutorialHintVisibility = (bool)Value;
	}

	private object get_521_VineMessagesInbox_NewCount(object instance)
	{
		return ((VineMessagesInbox)instance).NewCount;
	}

	private void set_521_VineMessagesInbox_NewCount(object instance, object Value)
	{
		((VineMessagesInbox)instance).NewCount = (long)Value;
	}

	private object get_522_VineMessagesInbox_IsOtherInboxActive(object instance)
	{
		return ((VineMessagesInbox)instance).IsOtherInboxActive;
	}

	private void set_522_VineMessagesInbox_IsOtherInboxActive(object instance, object Value)
	{
		((VineMessagesInbox)instance).IsOtherInboxActive = (bool)Value;
	}

	private object get_523_ProfileControl_Section(object instance)
	{
		return ((ProfileControl)instance).Section;
	}

	private void set_523_ProfileControl_Section(object instance, object Value)
	{
		((ProfileControl)instance).Section = (Section)Value;
	}

	private void set_524_ProfileControl_PullToRefreshMargin(object instance, object Value)
	{
		((ProfileControl)instance).PullToRefreshMargin = (Thickness)Value;
	}

	private void set_525_ProfileControl_ListViewPadding(object instance, object Value)
	{
		((ProfileControl)instance).ListViewPadding = (Thickness)Value;
	}

	private object get_526_ProfileControl_IsScrolledBelowPlaceholder(object instance)
	{
		return ((ProfileControl)instance).IsScrolledBelowPlaceholder;
	}

	private void set_526_ProfileControl_IsScrolledBelowPlaceholder(object instance, object Value)
	{
		((ProfileControl)instance).IsScrolledBelowPlaceholder = (bool)Value;
	}

	private object get_527_ProfileControl_ProfileHeaderHeight(object instance)
	{
		return ((ProfileControl)instance).ProfileHeaderHeight;
	}

	private void set_527_ProfileControl_ProfileHeaderHeight(object instance, object Value)
	{
		((ProfileControl)instance).ProfileHeaderHeight = (double)Value;
	}

	private object get_528_ProfileControl_FollowApprovalBusy(object instance)
	{
		return ((ProfileControl)instance).FollowApprovalBusy;
	}

	private void set_528_ProfileControl_FollowApprovalBusy(object instance, object Value)
	{
		((ProfileControl)instance).FollowApprovalBusy = (bool)Value;
	}

	private object get_529_ProfileControl_FollowApprovalNotBusy(object instance)
	{
		return ((ProfileControl)instance).FollowApprovalNotBusy;
	}

	private object get_530_ProfileControl_FooterMargin(object instance)
	{
		return ((ProfileControl)instance).FooterMargin;
	}

	private void set_530_ProfileControl_FooterMargin(object instance, object Value)
	{
		((ProfileControl)instance).FooterMargin = (Thickness)Value;
	}

	private object get_531_ProfileControl_LikeBrush(object instance)
	{
		return ((ProfileControl)instance).LikeBrush;
	}

	private void set_531_ProfileControl_LikeBrush(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((ProfileControl)instance).LikeBrush = (Brush)Value;
	}

	private object get_532_ProfileControl_ControlWrapper(object instance)
	{
		return ((ProfileControl)instance).ControlWrapper;
	}

	private void set_532_ProfileControl_ControlWrapper(object instance, object Value)
	{
		((ProfileControl)instance).ControlWrapper = (UserControlWrapper)Value;
	}

	private object get_533_ProfileControl_User(object instance)
	{
		return ((ProfileControl)instance).User;
	}

	private void set_533_ProfileControl_User(object instance, object Value)
	{
		((ProfileControl)instance).User = (VineUserModel)Value;
	}

	private object get_534_ProfileControl_UserId(object instance)
	{
		return ((ProfileControl)instance).UserId;
	}

	private void set_534_ProfileControl_UserId(object instance, object Value)
	{
		((ProfileControl)instance).UserId = (string)Value;
	}

	private object get_535_ProfileControl_IsBusy(object instance)
	{
		return ((ProfileControl)instance).IsBusy;
	}

	private void set_535_ProfileControl_IsBusy(object instance, object Value)
	{
		((ProfileControl)instance).IsBusy = (bool)Value;
	}

	private object get_536_ProfileControl_IsEmpty(object instance)
	{
		return ((ProfileControl)instance).IsEmpty;
	}

	private void set_536_ProfileControl_IsEmpty(object instance, object Value)
	{
		((ProfileControl)instance).IsEmpty = (bool)Value;
	}

	private object get_537_ProfileControl_EmptyText(object instance)
	{
		return ((ProfileControl)instance).EmptyText;
	}

	private object get_538_ProfileControl_IsSwitchingTab(object instance)
	{
		return ((ProfileControl)instance).IsSwitchingTab;
	}

	private void set_538_ProfileControl_IsSwitchingTab(object instance, object Value)
	{
		((ProfileControl)instance).IsSwitchingTab = (bool)Value;
	}

	private object get_539_ProfileControl_HasError(object instance)
	{
		return ((ProfileControl)instance).HasError;
	}

	private void set_539_ProfileControl_HasError(object instance, object Value)
	{
		((ProfileControl)instance).HasError = (bool)Value;
	}

	private object get_540_ProfileControl_ErrorText(object instance)
	{
		return ((ProfileControl)instance).ErrorText;
	}

	private void set_540_ProfileControl_ErrorText(object instance, object Value)
	{
		((ProfileControl)instance).ErrorText = (string)Value;
	}

	private object get_541_ProfileControl_ShowRetry(object instance)
	{
		return ((ProfileControl)instance).ShowRetry;
	}

	private void set_541_ProfileControl_ShowRetry(object instance, object Value)
	{
		((ProfileControl)instance).ShowRetry = (bool)Value;
	}

	private object get_542_ProfileControl_IsFinishedLoading(object instance)
	{
		return ((ProfileControl)instance).IsFinishedLoading;
	}

	private void set_542_ProfileControl_IsFinishedLoading(object instance, object Value)
	{
		((ProfileControl)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_543_ProfileControl_TutorialHintVisibility(object instance)
	{
		return ((ProfileControl)instance).TutorialHintVisibility;
	}

	private object get_544_ProfileControl_ShowExplore(object instance)
	{
		return ((ProfileControl)instance).ShowExplore;
	}

	private void set_544_ProfileControl_ShowExplore(object instance, object Value)
	{
		((ProfileControl)instance).ShowExplore = (bool)Value;
	}

	private object get_545_ProfileControl_ShowSuggestions(object instance)
	{
		return ((ProfileControl)instance).ShowSuggestions;
	}

	private void set_545_ProfileControl_ShowSuggestions(object instance, object Value)
	{
		((ProfileControl)instance).ShowSuggestions = (bool)Value;
	}

	private object get_546_ProfileControl__suggestedToFollow(object instance)
	{
		return ((ProfileControl)instance)._suggestedToFollow;
	}

	private void set_546_ProfileControl__suggestedToFollow(object instance, object Value)
	{
		((ProfileControl)instance)._suggestedToFollow = (List<VineUserModel>)Value;
	}

	private object get_547_VineUserModel_Section(object instance)
	{
		return ((VineUserModel)instance).Section;
	}

	private void set_547_VineUserModel_Section(object instance, object Value)
	{
		((VineUserModel)instance).Section = (string)Value;
	}

	private object get_548_VineUserModel_User(object instance)
	{
		return ((VineUserModel)instance).User;
	}

	private void set_548_VineUserModel_User(object instance, object Value)
	{
		((VineUserModel)instance).User = (VineUserModel)Value;
	}

	private object get_549_VineUserModel_ExternalUser(object instance)
	{
		return ((VineUserModel)instance).ExternalUser;
	}

	private void set_549_VineUserModel_ExternalUser(object instance, object Value)
	{
		((VineUserModel)instance).ExternalUser = (bool)Value;
	}

	private object get_550_VineUserModel_ProfileBackground(object instance)
	{
		return ((VineUserModel)instance).ProfileBackground;
	}

	private void set_550_VineUserModel_ProfileBackground(object instance, object Value)
	{
		((VineUserModel)instance).ProfileBackground = (string)Value;
	}

	private object get_551_VineUserModel_ProfileBgBannerBrush(object instance)
	{
		return ((VineUserModel)instance).ProfileBgBannerBrush;
	}

	private object get_552_VineUserModel_ProfileBgBrush(object instance)
	{
		return ((VineUserModel)instance).ProfileBgBrush;
	}

	private object get_553_VineUserModel_ProfileBgLightBrush(object instance)
	{
		return ((VineUserModel)instance).ProfileBgLightBrush;
	}

	private object get_554_VineUserModel_PhoneNumber(object instance)
	{
		return ((VineUserModel)instance).PhoneNumber;
	}

	private void set_554_VineUserModel_PhoneNumber(object instance, object Value)
	{
		((VineUserModel)instance).PhoneNumber = (string)Value;
	}

	private object get_555_VineUserModel_Email(object instance)
	{
		return ((VineUserModel)instance).Email;
	}

	private void set_555_VineUserModel_Email(object instance, object Value)
	{
		((VineUserModel)instance).Email = (string)Value;
	}

	private object get_556_VineUserModel_Username(object instance)
	{
		return ((VineUserModel)instance).Username;
	}

	private void set_556_VineUserModel_Username(object instance, object Value)
	{
		((VineUserModel)instance).Username = (string)Value;
	}

	private object get_557_VineUserModel_Description(object instance)
	{
		return ((VineUserModel)instance).Description;
	}

	private void set_557_VineUserModel_Description(object instance, object Value)
	{
		((VineUserModel)instance).Description = (string)Value;
	}

	private object get_558_VineUserModel_AvatarUrl(object instance)
	{
		return ((VineUserModel)instance).AvatarUrl;
	}

	private void set_558_VineUserModel_AvatarUrl(object instance, object Value)
	{
		((VineUserModel)instance).AvatarUrl = (string)Value;
	}

	private object get_559_VineUserModel_Location(object instance)
	{
		return ((VineUserModel)instance).Location;
	}

	private void set_559_VineUserModel_Location(object instance, object Value)
	{
		((VineUserModel)instance).Location = (string)Value;
	}

	private object get_560_VineUserModel_UserId(object instance)
	{
		return ((VineUserModel)instance).UserId;
	}

	private void set_560_VineUserModel_UserId(object instance, object Value)
	{
		((VineUserModel)instance).UserId = (string)Value;
	}

	private object get_561_VineUserModel_FollowingCount(object instance)
	{
		return ((VineUserModel)instance).FollowingCount;
	}

	private void set_561_VineUserModel_FollowingCount(object instance, object Value)
	{
		((VineUserModel)instance).FollowingCount = (long)Value;
	}

	private object get_562_VineUserModel_FollowerCount(object instance)
	{
		return ((VineUserModel)instance).FollowerCount;
	}

	private void set_562_VineUserModel_FollowerCount(object instance, object Value)
	{
		((VineUserModel)instance).FollowerCount = (long)Value;
	}

	private object get_563_VineUserModel_PostCount(object instance)
	{
		return ((VineUserModel)instance).PostCount;
	}

	private void set_563_VineUserModel_PostCount(object instance, object Value)
	{
		((VineUserModel)instance).PostCount = (long)Value;
	}

	private object get_564_VineUserModel_LikeCount(object instance)
	{
		return ((VineUserModel)instance).LikeCount;
	}

	private void set_564_VineUserModel_LikeCount(object instance, object Value)
	{
		((VineUserModel)instance).LikeCount = (long)Value;
	}

	private object get_565_VineUserModel_LoopCount(object instance)
	{
		return ((VineUserModel)instance).LoopCount;
	}

	private void set_565_VineUserModel_LoopCount(object instance, object Value)
	{
		((VineUserModel)instance).LoopCount = (long)Value;
	}

	private object get_566_VineUserModel_Blocked(object instance)
	{
		return ((VineUserModel)instance).Blocked;
	}

	private void set_566_VineUserModel_Blocked(object instance, object Value)
	{
		((VineUserModel)instance).Blocked = (bool)Value;
	}

	private object get_567_VineUserModel_Following(object instance)
	{
		return ((VineUserModel)instance).Following;
	}

	private void set_567_VineUserModel_Following(object instance, object Value)
	{
		((VineUserModel)instance).Following = (bool)Value;
	}

	private object get_568_VineUserModel_Verified(object instance)
	{
		return ((VineUserModel)instance).Verified;
	}

	private void set_568_VineUserModel_Verified(object instance, object Value)
	{
		((VineUserModel)instance).Verified = (bool)Value;
	}

	private object get_569_VineUserModel_FollowRequested(object instance)
	{
		return ((VineUserModel)instance).FollowRequested;
	}

	private void set_569_VineUserModel_FollowRequested(object instance, object Value)
	{
		((VineUserModel)instance).FollowRequested = (bool)Value;
	}

	private object get_570_VineUserModel_Private(object instance)
	{
		return ((VineUserModel)instance).Private;
	}

	private void set_570_VineUserModel_Private(object instance, object Value)
	{
		((VineUserModel)instance).Private = (bool)Value;
	}

	private object get_571_VineUserModel_TwitterScreenname(object instance)
	{
		return ((VineUserModel)instance).TwitterScreenname;
	}

	private void set_571_VineUserModel_TwitterScreenname(object instance, object Value)
	{
		((VineUserModel)instance).TwitterScreenname = (string)Value;
	}

	private object get_572_VineUserModel_TwitterDisplayScreenname(object instance)
	{
		return ((VineUserModel)instance).TwitterDisplayScreenname;
	}

	private object get_573_VineUserModel_TwitterConnected(object instance)
	{
		return ((VineUserModel)instance).TwitterConnected;
	}

	private void set_573_VineUserModel_TwitterConnected(object instance, object Value)
	{
		((VineUserModel)instance).TwitterConnected = (bool)Value;
	}

	private object get_574_VineUserModel_VerifiedPhoneNumber(object instance)
	{
		return ((VineUserModel)instance).VerifiedPhoneNumber;
	}

	private void set_574_VineUserModel_VerifiedPhoneNumber(object instance, object Value)
	{
		((VineUserModel)instance).VerifiedPhoneNumber = (bool)Value;
	}

	private object get_575_VineUserModel_VerifiedEmail(object instance)
	{
		return ((VineUserModel)instance).VerifiedEmail;
	}

	private void set_575_VineUserModel_VerifiedEmail(object instance, object Value)
	{
		((VineUserModel)instance).VerifiedEmail = (bool)Value;
	}

	private object get_576_VineUserModel_FacebookConnected(object instance)
	{
		return ((VineUserModel)instance).FacebookConnected;
	}

	private void set_576_VineUserModel_FacebookConnected(object instance, object Value)
	{
		((VineUserModel)instance).FacebookConnected = (bool)Value;
	}

	private object get_577_VineUserModel_ExplicitContent(object instance)
	{
		return ((VineUserModel)instance).ExplicitContent;
	}

	private void set_577_VineUserModel_ExplicitContent(object instance, object Value)
	{
		((VineUserModel)instance).ExplicitContent = (bool)Value;
	}

	private object get_578_VineUserModel_HiddenEmail(object instance)
	{
		return ((VineUserModel)instance).HiddenEmail;
	}

	private void set_578_VineUserModel_HiddenEmail(object instance, object Value)
	{
		((VineUserModel)instance).HiddenEmail = (bool)Value;
	}

	private object get_579_VineUserModel_HiddenEmailButtonState(object instance)
	{
		return ((VineUserModel)instance).HiddenEmailButtonState;
	}

	private object get_580_VineUserModel_HiddenPhoneNumber(object instance)
	{
		return ((VineUserModel)instance).HiddenPhoneNumber;
	}

	private void set_580_VineUserModel_HiddenPhoneNumber(object instance, object Value)
	{
		((VineUserModel)instance).HiddenPhoneNumber = (bool)Value;
	}

	private object get_581_VineUserModel_HiddenPhoneNumberButtonState(object instance)
	{
		return ((VineUserModel)instance).HiddenPhoneNumberButtonState;
	}

	private object get_582_VineUserModel_HiddenTwitter(object instance)
	{
		return ((VineUserModel)instance).HiddenTwitter;
	}

	private void set_582_VineUserModel_HiddenTwitter(object instance, object Value)
	{
		((VineUserModel)instance).HiddenTwitter = (bool)Value;
	}

	private object get_583_VineUserModel_HiddenTwitterButtonState(object instance)
	{
		return ((VineUserModel)instance).HiddenTwitterButtonState;
	}

	private object get_584_VineUserModel_FollowApprovalPending(object instance)
	{
		return ((VineUserModel)instance).FollowApprovalPending;
	}

	private void set_584_VineUserModel_FollowApprovalPending(object instance, object Value)
	{
		((VineUserModel)instance).FollowApprovalPending = (bool)Value;
	}

	private object get_585_VineUserModel_ByLine(object instance)
	{
		return ((VineUserModel)instance).ByLine;
	}

	private void set_585_VineUserModel_ByLine(object instance, object Value)
	{
		((VineUserModel)instance).ByLine = (string)Value;
	}

	private object get_586_VineUserModel_FollowingEnabled(object instance)
	{
		return ((VineUserModel)instance).FollowingEnabled;
	}

	private object get_587_VineUserModel_FollowButtonState(object instance)
	{
		return ((VineUserModel)instance).FollowButtonState;
	}

	private object get_588_VineUserModel_ExplicitContentButtonState(object instance)
	{
		return ((VineUserModel)instance).ExplicitContentButtonState;
	}

	private object get_589_VineUserModel_ProtectedButtonState(object instance)
	{
		return ((VineUserModel)instance).ProtectedButtonState;
	}

	private object get_590_VineUserModel_IsCurrentUser(object instance)
	{
		return ((VineUserModel)instance).IsCurrentUser;
	}

	private object get_591_VineUserModel_AreVinesViewable(object instance)
	{
		return ((VineUserModel)instance).AreVinesViewable;
	}

	private object get_592_VineUserModel_FollowCommand(object instance)
	{
		return ((VineUserModel)instance).FollowCommand;
	}

	private object get_593_VineUserModel_UserType(object instance)
	{
		return ((VineUserModel)instance).UserType;
	}

	private void set_593_VineUserModel_UserType(object instance, object Value)
	{
		((VineUserModel)instance).UserType = (VineUserType)Value;
	}

	private object get_594_VineUserModel_RichFollowers(object instance)
	{
		return ((VineUserModel)instance).RichFollowers;
	}

	private object get_595_VineUserModel_RichFollowing(object instance)
	{
		return ((VineUserModel)instance).RichFollowing;
	}

	private object get_596_VineUserModel_RichLoops(object instance)
	{
		return ((VineUserModel)instance).RichLoops;
	}

	private object get_597_VineUserModel_LoopCountShortened(object instance)
	{
		return ((VineUserModel)instance).LoopCountShortened;
	}

	private object get_598_VineUserModel_RichPostCount(object instance)
	{
		return ((VineUserModel)instance).RichPostCount;
	}

	private object get_599_VineUserModel_RichLikesCount(object instance)
	{
		return ((VineUserModel)instance).RichLikesCount;
	}

	private object get_600_ProfileControl_VisibleSuggestedToFollow(object instance)
	{
		return ((ProfileControl)instance).VisibleSuggestedToFollow;
	}

	private void set_600_ProfileControl_VisibleSuggestedToFollow(object instance, object Value)
	{
		((ProfileControl)instance).VisibleSuggestedToFollow = (ObservableCollection<VineUserModel>)Value;
	}

	private object get_601_ProfileControl_IsSuggestedLoaded(object instance)
	{
		return ((ProfileControl)instance).IsSuggestedLoaded;
	}

	private void set_601_ProfileControl_IsSuggestedLoaded(object instance, object Value)
	{
		((ProfileControl)instance).IsSuggestedLoaded = (bool)Value;
	}

	private object get_602_ProfileControl_IsActive(object instance)
	{
		return ((ProfileControl)instance).IsActive;
	}

	private void set_602_ProfileControl_IsActive(object instance, object Value)
	{
		((ProfileControl)instance).IsActive = (bool)Value;
	}

	private object get_603_ProfileControl_PostBrush(object instance)
	{
		return ((ProfileControl)instance).PostBrush;
	}

	private void set_603_ProfileControl_PostBrush(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((ProfileControl)instance).PostBrush = (Brush)Value;
	}

	private object get_604_InteractionsControl_Items(object instance)
	{
		return ((InteractionsControl)instance).Items;
	}

	private void set_604_InteractionsControl_Items(object instance, object Value)
	{
		((InteractionsControl)instance).Items = (IncrementalLoadingCollection<InteractionModel>)Value;
	}

	private object get_605_InteractionModel_Type(object instance)
	{
		return ((InteractionModel)instance).Type;
	}

	private void set_605_InteractionModel_Type(object instance, object Value)
	{
		((InteractionModel)instance).Type = (string)Value;
	}

	private object get_606_InteractionModel_InteractionType(object instance)
	{
		return ((InteractionModel)instance).InteractionType;
	}

	private object get_607_InteractionModel_Milestone(object instance)
	{
		return ((InteractionModel)instance).Milestone;
	}

	private void set_607_InteractionModel_Milestone(object instance, object Value)
	{
		((InteractionModel)instance).Milestone = (Milestone)Value;
	}

	private object get_608_InteractionModel_Body(object instance)
	{
		return ((InteractionModel)instance).Body;
	}

	private void set_608_InteractionModel_Body(object instance, object Value)
	{
		((InteractionModel)instance).Body = (string)Value;
	}

	private object get_609_InteractionModel_IsNew(object instance)
	{
		return ((InteractionModel)instance).IsNew;
	}

	private void set_609_InteractionModel_IsNew(object instance, object Value)
	{
		((InteractionModel)instance).IsNew = (bool)Value;
	}

	private object get_610_InteractionModel_NotificationId(object instance)
	{
		return ((InteractionModel)instance).NotificationId;
	}

	private void set_610_InteractionModel_NotificationId(object instance, object Value)
	{
		((InteractionModel)instance).NotificationId = (string)Value;
	}

	private object get_611_InteractionModel_ActivityId(object instance)
	{
		return ((InteractionModel)instance).ActivityId;
	}

	private void set_611_InteractionModel_ActivityId(object instance, object Value)
	{
		((InteractionModel)instance).ActivityId = (string)Value;
	}

	private object get_612_InteractionModel_Id(object instance)
	{
		return ((InteractionModel)instance).Id;
	}

	private object get_613_InteractionModel_User(object instance)
	{
		return ((InteractionModel)instance).User;
	}

	private void set_613_InteractionModel_User(object instance, object Value)
	{
		((InteractionModel)instance).User = (VineUserModel)Value;
	}

	private object get_614_InteractionModel_Post(object instance)
	{
		return ((InteractionModel)instance).Post;
	}

	private void set_614_InteractionModel_Post(object instance, object Value)
	{
		((InteractionModel)instance).Post = (VineModel)Value;
	}

	private object get_615_InteractionModel_UserId(object instance)
	{
		return ((InteractionModel)instance).UserId;
	}

	private void set_615_InteractionModel_UserId(object instance, object Value)
	{
		((InteractionModel)instance).UserId = (string)Value;
	}

	private object get_616_InteractionModel_AvatarUrl(object instance)
	{
		return ((InteractionModel)instance).AvatarUrl;
	}

	private void set_616_InteractionModel_AvatarUrl(object instance, object Value)
	{
		((InteractionModel)instance).AvatarUrl = (string)Value;
	}

	private object get_617_InteractionModel_NotificationTypeId(object instance)
	{
		return ((InteractionModel)instance).NotificationTypeId;
	}

	private void set_617_InteractionModel_NotificationTypeId(object instance, object Value)
	{
		((InteractionModel)instance).NotificationTypeId = (InteractionType)Value;
	}

	private object get_618_InteractionModel_FollowVisibility(object instance)
	{
		return ((InteractionModel)instance).FollowVisibility;
	}

	private object get_619_InteractionModel_UserThumbnail(object instance)
	{
		return ((InteractionModel)instance).UserThumbnail;
	}

	private object get_620_InteractionModel_MilestoneThumbUrl(object instance)
	{
		return ((InteractionModel)instance).MilestoneThumbUrl;
	}

	private object get_621_InteractionModel_PostId(object instance)
	{
		return ((InteractionModel)instance).PostId;
	}

	private void set_621_InteractionModel_PostId(object instance, object Value)
	{
		((InteractionModel)instance).PostId = (string)Value;
	}

	private object get_622_InteractionModel_ThumbnailUrl(object instance)
	{
		return ((InteractionModel)instance).ThumbnailUrl;
	}

	private void set_622_InteractionModel_ThumbnailUrl(object instance, object Value)
	{
		((InteractionModel)instance).ThumbnailUrl = (string)Value;
	}

	private object get_623_InteractionModel_PostThumbnailUrl(object instance)
	{
		return ((InteractionModel)instance).PostThumbnailUrl;
	}

	private object get_624_InteractionModel_HasPost(object instance)
	{
		return ((InteractionModel)instance).HasPost;
	}

	private object get_625_InteractionModel_Created(object instance)
	{
		return ((InteractionModel)instance).Created;
	}

	private void set_625_InteractionModel_Created(object instance, object Value)
	{
		((InteractionModel)instance).Created = (DateTime)Value;
	}

	private object get_626_InteractionModel_LastActivityTime(object instance)
	{
		return ((InteractionModel)instance).LastActivityTime;
	}

	private void set_626_InteractionModel_LastActivityTime(object instance, object Value)
	{
		((InteractionModel)instance).LastActivityTime = (DateTime)Value;
	}

	private object get_627_InteractionModel_RichBody(object instance)
	{
		return ((InteractionModel)instance).RichBody;
	}

	private object get_628_InteractionModel_GlyphBrush(object instance)
	{
		return ((InteractionModel)instance).GlyphBrush;
	}

	private object get_629_InteractionModel_GlyphVisibility(object instance)
	{
		return ((InteractionModel)instance).GlyphVisibility;
	}

	private object get_630_InteractionModel_GlyphFollowedVisibility(object instance)
	{
		return ((InteractionModel)instance).GlyphFollowedVisibility;
	}

	private object get_631_InteractionModel_GlyphData(object instance)
	{
		return ((InteractionModel)instance).GlyphData;
	}

	private object get_632_InteractionModel_HeartGlyphVisibility(object instance)
	{
		return ((InteractionModel)instance).HeartGlyphVisibility;
	}

	private object get_633_InteractionModel_Entities(object instance)
	{
		return ((InteractionModel)instance).Entities;
	}

	private void set_633_InteractionModel_Entities(object instance, object Value)
	{
		((InteractionModel)instance).Entities = (List<Entity>)Value;
	}

	private object get_634_InteractionModel_ShortBodyEntities(object instance)
	{
		return ((InteractionModel)instance).ShortBodyEntities;
	}

	private void set_634_InteractionModel_ShortBodyEntities(object instance, object Value)
	{
		((InteractionModel)instance).ShortBodyEntities = (List<Entity>)Value;
	}

	private object get_635_InteractionModel_CommentTextEntities(object instance)
	{
		return ((InteractionModel)instance).CommentTextEntities;
	}

	private void set_635_InteractionModel_CommentTextEntities(object instance, object Value)
	{
		((InteractionModel)instance).CommentTextEntities = (List<Entity>)Value;
	}

	private object get_636_InteractionModel_CreatedText(object instance)
	{
		return ((InteractionModel)instance).CreatedText;
	}

	private object get_637_InteractionModel_HeaderText(object instance)
	{
		return ((InteractionModel)instance).HeaderText;
	}

	private void set_637_InteractionModel_HeaderText(object instance, object Value)
	{
		((InteractionModel)instance).HeaderText = (string)Value;
	}

	private object get_638_InteractionModel_IsHeaderVisible(object instance)
	{
		return ((InteractionModel)instance).IsHeaderVisible;
	}

	private void set_638_InteractionModel_IsHeaderVisible(object instance, object Value)
	{
		((InteractionModel)instance).IsHeaderVisible = (bool)Value;
	}

	private object get_639_InteractionModel_ShortBody(object instance)
	{
		return ((InteractionModel)instance).ShortBody;
	}

	private void set_639_InteractionModel_ShortBody(object instance, object Value)
	{
		((InteractionModel)instance).ShortBody = (string)Value;
	}

	private object get_640_InteractionModel_CommentText(object instance)
	{
		return ((InteractionModel)instance).CommentText;
	}

	private void set_640_InteractionModel_CommentText(object instance, object Value)
	{
		((InteractionModel)instance).CommentText = (string)Value;
	}

	private object get_641_InteractionModel_RichCommentText(object instance)
	{
		return ((InteractionModel)instance).RichCommentText;
	}

	private object get_642_InteractionsControl_IsBusy(object instance)
	{
		return ((InteractionsControl)instance).IsBusy;
	}

	private void set_642_InteractionsControl_IsBusy(object instance, object Value)
	{
		((InteractionsControl)instance).IsBusy = (bool)Value;
	}

	private object get_643_InteractionsControl_IsEmpty(object instance)
	{
		return ((InteractionsControl)instance).IsEmpty;
	}

	private void set_643_InteractionsControl_IsEmpty(object instance, object Value)
	{
		((InteractionsControl)instance).IsEmpty = (bool)Value;
	}

	private object get_644_InteractionsControl_HasError(object instance)
	{
		return ((InteractionsControl)instance).HasError;
	}

	private void set_644_InteractionsControl_HasError(object instance, object Value)
	{
		((InteractionsControl)instance).HasError = (bool)Value;
	}

	private object get_645_InteractionsControl_ErrorText(object instance)
	{
		return ((InteractionsControl)instance).ErrorText;
	}

	private void set_645_InteractionsControl_ErrorText(object instance, object Value)
	{
		((InteractionsControl)instance).ErrorText = (string)Value;
	}

	private object get_646_InteractionsControl_ShowRetry(object instance)
	{
		return ((InteractionsControl)instance).ShowRetry;
	}

	private void set_646_InteractionsControl_ShowRetry(object instance, object Value)
	{
		((InteractionsControl)instance).ShowRetry = (bool)Value;
	}

	private object get_647_InteractionsControl_NewCount(object instance)
	{
		return ((InteractionsControl)instance).NewCount;
	}

	private void set_647_InteractionsControl_NewCount(object instance, object Value)
	{
		((InteractionsControl)instance).NewCount = (long)Value;
	}

	private object get_648_InteractionsControl_IsFinishedLoading(object instance)
	{
		return ((InteractionsControl)instance).IsFinishedLoading;
	}

	private void set_648_InteractionsControl_IsFinishedLoading(object instance, object Value)
	{
		((InteractionsControl)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_649_InteractionsControl_IsActive(object instance)
	{
		return ((InteractionsControl)instance).IsActive;
	}

	private void set_649_InteractionsControl_IsActive(object instance, object Value)
	{
		((InteractionsControl)instance).IsActive = (bool)Value;
	}

	private object get_650_HomeView_PinSearchVisible(object instance)
	{
		return ((HomeView)instance).PinSearchVisible;
	}

	private void set_650_HomeView_PinSearchVisible(object instance, object Value)
	{
		((HomeView)instance).PinSearchVisible = (bool)Value;
	}

	private object get_651_HomeView_HomeIconFill(object instance)
	{
		return ((HomeView)instance).HomeIconFill;
	}

	private void set_651_HomeView_HomeIconFill(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((HomeView)instance).HomeIconFill = (Brush)Value;
	}

	private object get_652_HomeView_NotificationIconFill(object instance)
	{
		return ((HomeView)instance).NotificationIconFill;
	}

	private void set_652_HomeView_NotificationIconFill(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((HomeView)instance).NotificationIconFill = (Brush)Value;
	}

	private object get_653_HomeView_DiscoverIconFill(object instance)
	{
		return ((HomeView)instance).DiscoverIconFill;
	}

	private void set_653_HomeView_DiscoverIconFill(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((HomeView)instance).DiscoverIconFill = (Brush)Value;
	}

	private object get_654_HomeView_MeIconFill(object instance)
	{
		return ((HomeView)instance).MeIconFill;
	}

	private void set_654_HomeView_MeIconFill(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((HomeView)instance).MeIconFill = (Brush)Value;
	}

	private object get_655_HomeView_VMsIconFill(object instance)
	{
		return ((HomeView)instance).VMsIconFill;
	}

	private void set_655_HomeView_VMsIconFill(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((HomeView)instance).VMsIconFill = (Brush)Value;
	}

	private object get_656_HomeView_VideoAppBarIcon(object instance)
	{
		return ((HomeView)instance).VideoAppBarIcon;
	}

	private object get_657_HomeView_AppBarBrush(object instance)
	{
		return ((HomeView)instance).AppBarBrush;
	}

	private object get_658_HomeView_ControlWrapper(object instance)
	{
		return ((HomeView)instance).ControlWrapper;
	}

	private void set_658_HomeView_ControlWrapper(object instance, object Value)
	{
		((HomeView)instance).ControlWrapper = (UserControlWrapper)Value;
	}

	private object get_659_HomeView_UploadJobs(object instance)
	{
		return ((HomeView)instance).UploadJobs;
	}

	private object get_660_HomeView_User(object instance)
	{
		return ((HomeView)instance).User;
	}

	private object get_661_HomeView_MuteIcon(object instance)
	{
		return ((HomeView)instance).MuteIcon;
	}

	private object get_662_HomeView_MuteLabel(object instance)
	{
		return ((HomeView)instance).MuteLabel;
	}

	private object get_663_HomeView_TutorialWelcomeVisibility(object instance)
	{
		return ((HomeView)instance).TutorialWelcomeVisibility;
	}

	private void set_663_HomeView_TutorialWelcomeVisibility(object instance, object Value)
	{
		((HomeView)instance).TutorialWelcomeVisibility = (bool)Value;
	}

	private object get_664_HomeView_IsRedOn(object instance)
	{
		return ((HomeView)instance).IsRedOn;
	}

	private void set_664_HomeView_IsRedOn(object instance, object Value)
	{
		((HomeView)instance).IsRedOn = (bool)Value;
	}

	private object get_665_HomeView_IsOrangeOn(object instance)
	{
		return ((HomeView)instance).IsOrangeOn;
	}

	private void set_665_HomeView_IsOrangeOn(object instance, object Value)
	{
		((HomeView)instance).IsOrangeOn = (bool)Value;
	}

	private object get_666_HomeView_IsYellowOn(object instance)
	{
		return ((HomeView)instance).IsYellowOn;
	}

	private void set_666_HomeView_IsYellowOn(object instance, object Value)
	{
		((HomeView)instance).IsYellowOn = (bool)Value;
	}

	private object get_667_HomeView_IsGreenOn(object instance)
	{
		return ((HomeView)instance).IsGreenOn;
	}

	private void set_667_HomeView_IsGreenOn(object instance, object Value)
	{
		((HomeView)instance).IsGreenOn = (bool)Value;
	}

	private object get_668_HomeView_IsTealOn(object instance)
	{
		return ((HomeView)instance).IsTealOn;
	}

	private void set_668_HomeView_IsTealOn(object instance, object Value)
	{
		((HomeView)instance).IsTealOn = (bool)Value;
	}

	private object get_669_HomeView_IsBlueLightOn(object instance)
	{
		return ((HomeView)instance).IsBlueLightOn;
	}

	private void set_669_HomeView_IsBlueLightOn(object instance, object Value)
	{
		((HomeView)instance).IsBlueLightOn = (bool)Value;
	}

	private object get_670_HomeView_IsBlueDarkOn(object instance)
	{
		return ((HomeView)instance).IsBlueDarkOn;
	}

	private void set_670_HomeView_IsBlueDarkOn(object instance, object Value)
	{
		((HomeView)instance).IsBlueDarkOn = (bool)Value;
	}

	private object get_671_HomeView_IsPurpleOn(object instance)
	{
		return ((HomeView)instance).IsPurpleOn;
	}

	private void set_671_HomeView_IsPurpleOn(object instance, object Value)
	{
		((HomeView)instance).IsPurpleOn = (bool)Value;
	}

	private object get_672_HomeView_IsPinkOn(object instance)
	{
		return ((HomeView)instance).IsPinkOn;
	}

	private void set_672_HomeView_IsPinkOn(object instance, object Value)
	{
		((HomeView)instance).IsPinkOn = (bool)Value;
	}

	private object get_673_InteractionTemplateSelector_HeaderTemplate(object instance)
	{
		return ((InteractionTemplateSelector)instance).HeaderTemplate;
	}

	private void set_673_InteractionTemplateSelector_HeaderTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((InteractionTemplateSelector)instance).HeaderTemplate = (DataTemplate)Value;
	}

	private object get_674_InteractionTemplateSelector_FollowRequestTemplate(object instance)
	{
		return ((InteractionTemplateSelector)instance).FollowRequestTemplate;
	}

	private void set_674_InteractionTemplateSelector_FollowRequestTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((InteractionTemplateSelector)instance).FollowRequestTemplate = (DataTemplate)Value;
	}

	private object get_675_InteractionTemplateSelector_NotificationTemplate(object instance)
	{
		return ((InteractionTemplateSelector)instance).NotificationTemplate;
	}

	private void set_675_InteractionTemplateSelector_NotificationTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((InteractionTemplateSelector)instance).NotificationTemplate = (DataTemplate)Value;
	}

	private object get_676_InteractionTemplateSelector_MilestoneTemplate(object instance)
	{
		return ((InteractionTemplateSelector)instance).MilestoneTemplate;
	}

	private void set_676_InteractionTemplateSelector_MilestoneTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((InteractionTemplateSelector)instance).MilestoneTemplate = (DataTemplate)Value;
	}

	private object get_677_InteractionTemplateSelector_GroupedCountTemplate(object instance)
	{
		return ((InteractionTemplateSelector)instance).GroupedCountTemplate;
	}

	private void set_677_InteractionTemplateSelector_GroupedCountTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((InteractionTemplateSelector)instance).GroupedCountTemplate = (DataTemplate)Value;
	}

	private object get_678_LoginEmailView_Username(object instance)
	{
		return ((LoginEmailView)instance).Username;
	}

	private void set_678_LoginEmailView_Username(object instance, object Value)
	{
		((LoginEmailView)instance).Username = (string)Value;
	}

	private object get_679_LoginEmailView_Password(object instance)
	{
		return ((LoginEmailView)instance).Password;
	}

	private void set_679_LoginEmailView_Password(object instance, object Value)
	{
		((LoginEmailView)instance).Password = (string)Value;
	}

	private object get_680_LoginEmailView_IsBusy(object instance)
	{
		return ((LoginEmailView)instance).IsBusy;
	}

	private void set_680_LoginEmailView_IsBusy(object instance, object Value)
	{
		((LoginEmailView)instance).IsBusy = (bool)Value;
	}

	private object get_681_LoginEmailView_IsNotBusy(object instance)
	{
		return ((LoginEmailView)instance).IsNotBusy;
	}

	private object get_682_LoginTwitterView_ErrorText(object instance)
	{
		return ((LoginTwitterView)instance).ErrorText;
	}

	private void set_682_LoginTwitterView_ErrorText(object instance, object Value)
	{
		((LoginTwitterView)instance).ErrorText = (string)Value;
	}

	private object get_683_LoginTwitterView_IsError(object instance)
	{
		return ((LoginTwitterView)instance).IsError;
	}

	private void set_683_LoginTwitterView_IsError(object instance, object Value)
	{
		((LoginTwitterView)instance).IsError = (bool)Value;
	}

	private object get_684_LoginTwitterView_Username(object instance)
	{
		return ((LoginTwitterView)instance).Username;
	}

	private void set_684_LoginTwitterView_Username(object instance, object Value)
	{
		((LoginTwitterView)instance).Username = (string)Value;
	}

	private object get_685_LoginTwitterView_Password(object instance)
	{
		return ((LoginTwitterView)instance).Password;
	}

	private void set_685_LoginTwitterView_Password(object instance, object Value)
	{
		((LoginTwitterView)instance).Password = (string)Value;
	}

	private object get_686_LoginTwitterView_IsBusy(object instance)
	{
		return ((LoginTwitterView)instance).IsBusy;
	}

	private void set_686_LoginTwitterView_IsBusy(object instance, object Value)
	{
		((LoginTwitterView)instance).IsBusy = (bool)Value;
	}

	private object get_687_LoginTwitterView_IsNotBusy(object instance)
	{
		return ((LoginTwitterView)instance).IsNotBusy;
	}

	private object get_688_ExtensionsForUi_RunList(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		return ExtensionsForUi.GetRunList((DependencyObject)instance);
	}

	private void set_688_ExtensionsForUi_RunList(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		ExtensionsForUi.SetRunList((DependencyObject)instance, (List<Run>)Value);
	}

	private object get_689_ExtensionsForUi_ExtraTag(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		return ExtensionsForUi.GetExtraTag((DependencyObject)instance);
	}

	private void set_689_ExtensionsForUi_ExtraTag(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		//IL_0011: Expected O, but got Unknown
		ExtensionsForUi.SetExtraTag((DependencyObject)instance, (DependencyObject)Value);
	}

	private object get_690_ExtensionsForUi_MediaStream(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_000b: Expected O, but got Unknown
		return ExtensionsForUi.GetMediaStream((DependencyObject)instance);
	}

	private void set_690_ExtensionsForUi_MediaStream(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		ExtensionsForUi.SetMediaStream((DependencyObject)instance, (Stream)Value);
	}

	private object get_691_ProfileView_MuteIcon(object instance)
	{
		return ((ProfileView)instance).MuteIcon;
	}

	private object get_692_ProfileView_MuteLabel(object instance)
	{
		return ((ProfileView)instance).MuteLabel;
	}

	private object get_693_ProfileView_AppBarButtonVisibility(object instance)
	{
		return ((ProfileView)instance).AppBarButtonVisibility;
	}

	private object get_694_ProfileView_BlockedText(object instance)
	{
		return ((ProfileView)instance).BlockedText;
	}

	private object get_695_FacebookView_HasError(object instance)
	{
		return ((FacebookView)instance).HasError;
	}

	private void set_695_FacebookView_HasError(object instance, object Value)
	{
		((FacebookView)instance).HasError = (bool)Value;
	}

	private object get_696_FacebookView_IsLoading(object instance)
	{
		return ((FacebookView)instance).IsLoading;
	}

	private void set_696_FacebookView_IsLoading(object instance, object Value)
	{
		((FacebookView)instance).IsLoading = (bool)Value;
	}

	private object get_697_FacebookView_QueryParameters(object instance)
	{
		return FacebookView.GetQueryParameters((Uri)instance);
	}

	private object get_698_Uri_AbsolutePath(object instance)
	{
		return ((Uri)instance).AbsolutePath;
	}

	private object get_699_Uri_AbsoluteUri(object instance)
	{
		return ((Uri)instance).AbsoluteUri;
	}

	private object get_700_Uri_Authority(object instance)
	{
		return ((Uri)instance).Authority;
	}

	private object get_701_Uri_DnsSafeHost(object instance)
	{
		return ((Uri)instance).DnsSafeHost;
	}

	private object get_702_Uri_Fragment(object instance)
	{
		return ((Uri)instance).Fragment;
	}

	private object get_703_Uri_Host(object instance)
	{
		return ((Uri)instance).Host;
	}

	private object get_704_Uri_HostNameType(object instance)
	{
		return ((Uri)instance).HostNameType;
	}

	private object get_705_Uri_IsAbsoluteUri(object instance)
	{
		return ((Uri)instance).IsAbsoluteUri;
	}

	private object get_706_Uri_IsDefaultPort(object instance)
	{
		return ((Uri)instance).IsDefaultPort;
	}

	private object get_707_Uri_IsFile(object instance)
	{
		return ((Uri)instance).IsFile;
	}

	private object get_708_Uri_IsLoopback(object instance)
	{
		return ((Uri)instance).IsLoopback;
	}

	private object get_709_Uri_IsUnc(object instance)
	{
		return ((Uri)instance).IsUnc;
	}

	private object get_710_Uri_LocalPath(object instance)
	{
		return ((Uri)instance).LocalPath;
	}

	private object get_711_Uri_OriginalString(object instance)
	{
		return ((Uri)instance).OriginalString;
	}

	private object get_712_Uri_PathAndQuery(object instance)
	{
		return ((Uri)instance).PathAndQuery;
	}

	private object get_713_Uri_Port(object instance)
	{
		return ((Uri)instance).Port;
	}

	private object get_714_Uri_Query(object instance)
	{
		return ((Uri)instance).Query;
	}

	private object get_715_Uri_Scheme(object instance)
	{
		return ((Uri)instance).Scheme;
	}

	private object get_716_Uri_Segments(object instance)
	{
		return ((Uri)instance).Segments;
	}

	private object get_717_Uri_UserEscaped(object instance)
	{
		return ((Uri)instance).UserEscaped;
	}

	private object get_718_Uri_UserInfo(object instance)
	{
		return ((Uri)instance).UserInfo;
	}

	private object get_719_SettingsContentView_User(object instance)
	{
		return ((SettingsContentView)instance).User;
	}

	private void set_719_SettingsContentView_User(object instance, object Value)
	{
		((SettingsContentView)instance).User = (VineUserModel)Value;
	}

	private object get_720_SettingsContentView_IsError(object instance)
	{
		return ((SettingsContentView)instance).IsError;
	}

	private void set_720_SettingsContentView_IsError(object instance, object Value)
	{
		((SettingsContentView)instance).IsError = (bool)Value;
	}

	private object get_721_SettingsContentView_ErrorText(object instance)
	{
		return ((SettingsContentView)instance).ErrorText;
	}

	private void set_721_SettingsContentView_ErrorText(object instance, object Value)
	{
		((SettingsContentView)instance).ErrorText = (string)Value;
	}

	private object get_722_SettingsContentView_ShowRetry(object instance)
	{
		return ((SettingsContentView)instance).ShowRetry;
	}

	private void set_722_SettingsContentView_ShowRetry(object instance, object Value)
	{
		((SettingsContentView)instance).ShowRetry = (bool)Value;
	}

	private object get_723_SettingsContentView_IsLoaded(object instance)
	{
		return ((SettingsContentView)instance).IsLoaded;
	}

	private void set_723_SettingsContentView_IsLoaded(object instance, object Value)
	{
		((SettingsContentView)instance).IsLoaded = (bool)Value;
	}

	private object get_724_SettingsContentView_AllowAddressBookButtonState(object instance)
	{
		return ((SettingsContentView)instance).AllowAddressBookButtonState;
	}

	private object get_725_SettingsView_User(object instance)
	{
		return ((SettingsView)instance).User;
	}

	private void set_725_SettingsView_User(object instance, object Value)
	{
		((SettingsView)instance).User = (VineUserModel)Value;
	}

	private object get_726_SettingsView_IsRedOn(object instance)
	{
		return ((SettingsView)instance).IsRedOn;
	}

	private void set_726_SettingsView_IsRedOn(object instance, object Value)
	{
		((SettingsView)instance).IsRedOn = (bool)Value;
	}

	private object get_727_SettingsView_IsOrangeOn(object instance)
	{
		return ((SettingsView)instance).IsOrangeOn;
	}

	private void set_727_SettingsView_IsOrangeOn(object instance, object Value)
	{
		((SettingsView)instance).IsOrangeOn = (bool)Value;
	}

	private object get_728_SettingsView_IsYellowOn(object instance)
	{
		return ((SettingsView)instance).IsYellowOn;
	}

	private void set_728_SettingsView_IsYellowOn(object instance, object Value)
	{
		((SettingsView)instance).IsYellowOn = (bool)Value;
	}

	private object get_729_SettingsView_IsGreenOn(object instance)
	{
		return ((SettingsView)instance).IsGreenOn;
	}

	private void set_729_SettingsView_IsGreenOn(object instance, object Value)
	{
		((SettingsView)instance).IsGreenOn = (bool)Value;
	}

	private object get_730_SettingsView_IsTealOn(object instance)
	{
		return ((SettingsView)instance).IsTealOn;
	}

	private void set_730_SettingsView_IsTealOn(object instance, object Value)
	{
		((SettingsView)instance).IsTealOn = (bool)Value;
	}

	private object get_731_SettingsView_IsBlueLightOn(object instance)
	{
		return ((SettingsView)instance).IsBlueLightOn;
	}

	private void set_731_SettingsView_IsBlueLightOn(object instance, object Value)
	{
		((SettingsView)instance).IsBlueLightOn = (bool)Value;
	}

	private object get_732_SettingsView_IsBlueDarkOn(object instance)
	{
		return ((SettingsView)instance).IsBlueDarkOn;
	}

	private void set_732_SettingsView_IsBlueDarkOn(object instance, object Value)
	{
		((SettingsView)instance).IsBlueDarkOn = (bool)Value;
	}

	private object get_733_SettingsView_IsPurpleOn(object instance)
	{
		return ((SettingsView)instance).IsPurpleOn;
	}

	private void set_733_SettingsView_IsPurpleOn(object instance, object Value)
	{
		((SettingsView)instance).IsPurpleOn = (bool)Value;
	}

	private object get_734_SettingsView_IsPinkOn(object instance)
	{
		return ((SettingsView)instance).IsPinkOn;
	}

	private void set_734_SettingsView_IsPinkOn(object instance, object Value)
	{
		((SettingsView)instance).IsPinkOn = (bool)Value;
	}

	private object get_735_SettingsView_IsBusy(object instance)
	{
		return ((SettingsView)instance).IsBusy;
	}

	private void set_735_SettingsView_IsBusy(object instance, object Value)
	{
		((SettingsView)instance).IsBusy = (bool)Value;
	}

	private object get_736_SettingsView_IsError(object instance)
	{
		return ((SettingsView)instance).IsError;
	}

	private void set_736_SettingsView_IsError(object instance, object Value)
	{
		((SettingsView)instance).IsError = (bool)Value;
	}

	private object get_737_SettingsView_IsErrorOrBusy(object instance)
	{
		return ((SettingsView)instance).IsErrorOrBusy;
	}

	private object get_738_SettingsView_ErrorText(object instance)
	{
		return ((SettingsView)instance).ErrorText;
	}

	private void set_738_SettingsView_ErrorText(object instance, object Value)
	{
		((SettingsView)instance).ErrorText = (string)Value;
	}

	private object get_739_SettingsView_ShowRetry(object instance)
	{
		return ((SettingsView)instance).ShowRetry;
	}

	private void set_739_SettingsView_ShowRetry(object instance, object Value)
	{
		((SettingsView)instance).ShowRetry = (bool)Value;
	}

	private object get_740_SettingsView_EmailVerifiedImage(object instance)
	{
		return ((SettingsView)instance).EmailVerifiedImage;
	}

	private object get_741_SettingsView_PhoneVerifiedImage(object instance)
	{
		return ((SettingsView)instance).PhoneVerifiedImage;
	}

	private object get_742_SettingsView_EmailVerifiedFill(object instance)
	{
		return ((SettingsView)instance).EmailVerifiedFill;
	}

	private object get_743_SettingsView_PhoneVerifiedFill(object instance)
	{
		return ((SettingsView)instance).PhoneVerifiedFill;
	}

	private object get_744_SettingsView_TwitterConnectedText(object instance)
	{
		return ((SettingsView)instance).TwitterConnectedText;
	}

	private object get_745_SettingsView_FacebookConnectedText(object instance)
	{
		return ((SettingsView)instance).FacebookConnectedText;
	}

	private object get_746_SettingsView_Version(object instance)
	{
		return ((SettingsView)instance).Version;
	}

	private object get_747_SettingsView_IsFinishedLoading(object instance)
	{
		return ((SettingsView)instance).IsFinishedLoading;
	}

	private void set_747_SettingsView_IsFinishedLoading(object instance, object Value)
	{
		((SettingsView)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_748_SingleVineView_Params(object instance)
	{
		return ((SingleVineView)instance).Params;
	}

	private object get_749_SingleVineView_Section(object instance)
	{
		return ((SingleVineView)instance).Section;
	}

	private void set_749_SingleVineView_Section(object instance, object Value)
	{
		((SingleVineView)instance).Section = (Section)Value;
	}

	private object get_750_SingleVineView_MuteIcon(object instance)
	{
		return ((SingleVineView)instance).MuteIcon;
	}

	private object get_751_SingleVineView_MuteLabel(object instance)
	{
		return ((SingleVineView)instance).MuteLabel;
	}

	private object get_752_TagVineListView_MuteIcon(object instance)
	{
		return ((TagVineListView)instance).MuteIcon;
	}

	private object get_753_TagVineListView_MuteLabel(object instance)
	{
		return ((TagVineListView)instance).MuteLabel;
	}

	private object get_754_TagVineListView_PageTitle(object instance)
	{
		return ((TagVineListView)instance).PageTitle;
	}

	private void set_754_TagVineListView_PageTitle(object instance, object Value)
	{
		((TagVineListView)instance).PageTitle = (string)Value;
	}

	private object get_755_TagVineListView_IsBusy(object instance)
	{
		return ((TagVineListView)instance).IsBusy;
	}

	private void set_755_TagVineListView_IsBusy(object instance, object Value)
	{
		((TagVineListView)instance).IsBusy = (bool)Value;
	}

	private object get_756_TagVineListView_SearchTerm(object instance)
	{
		return ((TagVineListView)instance).SearchTerm;
	}

	private void set_756_TagVineListView_SearchTerm(object instance, object Value)
	{
		((TagVineListView)instance).SearchTerm = (string)Value;
	}

	private object get_757_TagVineListView_CanPin(object instance)
	{
		return ((TagVineListView)instance).CanPin;
	}

	private void set_757_TagVineListView_CanPin(object instance, object Value)
	{
		((TagVineListView)instance).CanPin = (bool)Value;
	}

	private object get_758_UploadJobsView_ViewModel(object instance)
	{
		return ((UploadJobsView)instance).ViewModel;
	}

	private object get_759_VerifyPhoneEditControl_User(object instance)
	{
		return ((VerifyPhoneEditControl)instance).User;
	}

	private void set_759_VerifyPhoneEditControl_User(object instance, object Value)
	{
		((VerifyPhoneEditControl)instance).User = (VineUserModel)Value;
	}

	private object get_760_VerifyPhoneEditControl_IsNextEnabled(object instance)
	{
		return ((VerifyPhoneEditControl)instance).IsNextEnabled;
	}

	private void set_760_VerifyPhoneEditControl_IsNextEnabled(object instance, object Value)
	{
		((VerifyPhoneEditControl)instance).IsNextEnabled = (bool)Value;
	}

	private object get_761_MessageTemplateSelector_VideoUploadJobTemplate(object instance)
	{
		return ((MessageTemplateSelector)instance).VideoUploadJobTemplate;
	}

	private void set_761_MessageTemplateSelector_VideoUploadJobTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((MessageTemplateSelector)instance).VideoUploadJobTemplate = (DataTemplate)Value;
	}

	private object get_762_MessageTemplateSelector_CurrentUserMessageTextTemplate(object instance)
	{
		return ((MessageTemplateSelector)instance).CurrentUserMessageTextTemplate;
	}

	private void set_762_MessageTemplateSelector_CurrentUserMessageTextTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((MessageTemplateSelector)instance).CurrentUserMessageTextTemplate = (DataTemplate)Value;
	}

	private object get_763_MessageTemplateSelector_CurrentUserMessageVideoTemplate(object instance)
	{
		return ((MessageTemplateSelector)instance).CurrentUserMessageVideoTemplate;
	}

	private void set_763_MessageTemplateSelector_CurrentUserMessageVideoTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((MessageTemplateSelector)instance).CurrentUserMessageVideoTemplate = (DataTemplate)Value;
	}

	private object get_764_MessageTemplateSelector_CurrentUserMessagePostTemplate(object instance)
	{
		return ((MessageTemplateSelector)instance).CurrentUserMessagePostTemplate;
	}

	private void set_764_MessageTemplateSelector_CurrentUserMessagePostTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((MessageTemplateSelector)instance).CurrentUserMessagePostTemplate = (DataTemplate)Value;
	}

	private object get_765_MessageTemplateSelector_OtherUserMessagePostTemplate(object instance)
	{
		return ((MessageTemplateSelector)instance).OtherUserMessagePostTemplate;
	}

	private void set_765_MessageTemplateSelector_OtherUserMessagePostTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((MessageTemplateSelector)instance).OtherUserMessagePostTemplate = (DataTemplate)Value;
	}

	private object get_766_MessageTemplateSelector_OtherUserMessageTextTemplate(object instance)
	{
		return ((MessageTemplateSelector)instance).OtherUserMessageTextTemplate;
	}

	private void set_766_MessageTemplateSelector_OtherUserMessageTextTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((MessageTemplateSelector)instance).OtherUserMessageTextTemplate = (DataTemplate)Value;
	}

	private object get_767_MessageTemplateSelector_OtherUserMessageVideoTemplate(object instance)
	{
		return ((MessageTemplateSelector)instance).OtherUserMessageVideoTemplate;
	}

	private void set_767_MessageTemplateSelector_OtherUserMessageVideoTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((MessageTemplateSelector)instance).OtherUserMessageVideoTemplate = (DataTemplate)Value;
	}

	private object get_768_VineMessagesThreadView_Items(object instance)
	{
		return ((VineMessagesThreadView)instance).Items;
	}

	private void set_768_VineMessagesThreadView_Items(object instance, object Value)
	{
		((VineMessagesThreadView)instance).Items = (IncrementalLoadingCollection<VineMessageViewModel>)Value;
	}

	private object get_769_VineMessageViewModel_YouShareText(object instance)
	{
		return ((VineMessageViewModel)instance).YouShareText;
	}

	private object get_770_VineMessageViewModel_ShareText(object instance)
	{
		return ((VineMessageViewModel)instance).ShareText;
	}

	private object get_771_VineMessageViewModel_Model(object instance)
	{
		return ((VineMessageViewModel)instance).Model;
	}

	private void set_771_VineMessageViewModel_Model(object instance, object Value)
	{
		((VineMessageViewModel)instance).Model = (VineMessageModel)Value;
	}

	private object get_772_VineMessageViewModel_User(object instance)
	{
		return ((VineMessageViewModel)instance).User;
	}

	private void set_772_VineMessageViewModel_User(object instance, object Value)
	{
		((VineMessageViewModel)instance).User = (VineUserModel)Value;
	}

	private object get_773_VineMessageViewModel_ShowCreatedDisplay(object instance)
	{
		return ((VineMessageViewModel)instance).ShowCreatedDisplay;
	}

	private void set_773_VineMessageViewModel_ShowCreatedDisplay(object instance, object Value)
	{
		((VineMessageViewModel)instance).ShowCreatedDisplay = (bool)Value;
	}

	private object get_774_VineMessageViewModel_CreatedDisplay(object instance)
	{
		return ((VineMessageViewModel)instance).CreatedDisplay;
	}

	private object get_775_VineMessageViewModel_RequiresVerification(object instance)
	{
		return ((VineMessageViewModel)instance).RequiresVerification;
	}

	private void set_775_VineMessageViewModel_RequiresVerification(object instance, object Value)
	{
		((VineMessageViewModel)instance).RequiresVerification = (bool)Value;
	}

	private object get_776_VineMessageViewModel_ErrorMessage(object instance)
	{
		return ((VineMessageViewModel)instance).ErrorMessage;
	}

	private void set_776_VineMessageViewModel_ErrorMessage(object instance, object Value)
	{
		((VineMessageViewModel)instance).ErrorMessage = (string)Value;
	}

	private object get_777_VineMessageViewModel_HasError(object instance)
	{
		return ((VineMessageViewModel)instance).HasError;
	}

	private void set_777_VineMessageViewModel_HasError(object instance, object Value)
	{
		((VineMessageViewModel)instance).HasError = (bool)Value;
	}

	private object get_778_VineMessageViewModel_UserBrush(object instance)
	{
		return ((VineMessageViewModel)instance).UserBrush;
	}

	private object get_779_VineMessageViewModel_LightBrush(object instance)
	{
		return ((VineMessageViewModel)instance).LightBrush;
	}

	private object get_780_VineMessageViewModel_IsPlaying(object instance)
	{
		return ((VineMessageViewModel)instance).IsPlaying;
	}

	private void set_780_VineMessageViewModel_IsPlaying(object instance, object Value)
	{
		((VineMessageViewModel)instance).IsPlaying = (bool)Value;
	}

	private object get_781_VineMessageViewModel_IsLoadingVideo(object instance)
	{
		return ((VineMessageViewModel)instance).IsLoadingVideo;
	}

	private void set_781_VineMessageViewModel_IsLoadingVideo(object instance, object Value)
	{
		((VineMessageViewModel)instance).IsLoadingVideo = (bool)Value;
	}

	private object get_782_VineMessageViewModel_IsFinishedBuffering(object instance)
	{
		return ((VineMessageViewModel)instance).IsFinishedBuffering;
	}

	private void set_782_VineMessageViewModel_IsFinishedBuffering(object instance, object Value)
	{
		((VineMessageViewModel)instance).IsFinishedBuffering = (bool)Value;
	}

	private object get_783_VineMessageViewModel_PlayingVideoUrl(object instance)
	{
		return ((VineMessageViewModel)instance).PlayingVideoUrl;
	}

	private object get_784_VineMessageViewModel_PostDescription(object instance)
	{
		return ((VineMessageViewModel)instance).PostDescription;
	}

	private object get_785_VineMessageViewModel_IsPostDeleted(object instance)
	{
		return ((VineMessageViewModel)instance).IsPostDeleted;
	}

	private object get_786_VineMessageViewModel_ThumbnailUrlAuth(object instance)
	{
		return ((VineMessageViewModel)instance).ThumbnailUrlAuth;
	}

	private object get_787_VineMessageViewModel_ThumbVisibility(object instance)
	{
		//IL_0006: Unknown result type (might be due to invalid IL or missing references)
		return ((VineMessageViewModel)instance).ThumbVisibility;
	}

	private void set_787_VineMessageViewModel_ThumbVisibility(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		((VineMessageViewModel)instance).ThumbVisibility = (Visibility)Value;
	}

	private object get_788_VineMessagesThreadView_ViewModel(object instance)
	{
		return ((VineMessagesThreadView)instance).ViewModel;
	}

	private void set_788_VineMessagesThreadView_ViewModel(object instance, object Value)
	{
		((VineMessagesThreadView)instance).ViewModel = (ConversationViewModel)Value;
	}

	private object get_789_VineMessagesThreadView_ConversationId(object instance)
	{
		return ((VineMessagesThreadView)instance).ConversationId;
	}

	private object get_790_VineMessagesThreadView_IgnoreLabel(object instance)
	{
		return ((VineMessagesThreadView)instance).IgnoreLabel;
	}

	private object get_791_VineMessagesThreadView_UserProfileLabel(object instance)
	{
		return ((VineMessagesThreadView)instance).UserProfileLabel;
	}

	private object get_792_VineMessagesThreadView_CurrentUserBrush(object instance)
	{
		return ((VineMessagesThreadView)instance).CurrentUserBrush;
	}

	private object get_793_VineMessagesThreadView_CurrentUserLightBrush(object instance)
	{
		return ((VineMessagesThreadView)instance).CurrentUserLightBrush;
	}

	private object get_794_VineMessagesThreadView_OtherUserBrush(object instance)
	{
		return ((VineMessagesThreadView)instance).OtherUserBrush;
	}

	private object get_795_VineMessagesThreadView_OtherUserLightBrush(object instance)
	{
		return ((VineMessagesThreadView)instance).OtherUserLightBrush;
	}

	private object get_796_VineMessagesThreadView_OtherUsername(object instance)
	{
		return ((VineMessagesThreadView)instance).OtherUsername;
	}

	private object get_797_VineMessagesThreadView_NewComment(object instance)
	{
		return ((VineMessagesThreadView)instance).NewComment;
	}

	private void set_797_VineMessagesThreadView_NewComment(object instance, object Value)
	{
		((VineMessagesThreadView)instance).NewComment = (string)Value;
	}

	private object get_798_VineMessagesThreadView_HasError(object instance)
	{
		return ((VineMessagesThreadView)instance).HasError;
	}

	private void set_798_VineMessagesThreadView_HasError(object instance, object Value)
	{
		((VineMessagesThreadView)instance).HasError = (bool)Value;
	}

	private object get_799_VineMessagesThreadView_IsEmpty(object instance)
	{
		return ((VineMessagesThreadView)instance).IsEmpty;
	}

	private void set_799_VineMessagesThreadView_IsEmpty(object instance, object Value)
	{
		((VineMessagesThreadView)instance).IsEmpty = (bool)Value;
	}

	private object get_800_VineMessagesThreadView_IsBusy(object instance)
	{
		return ((VineMessagesThreadView)instance).IsBusy;
	}

	private void set_800_VineMessagesThreadView_IsBusy(object instance, object Value)
	{
		((VineMessagesThreadView)instance).IsBusy = (bool)Value;
	}

	private object get_801_VineMessagesThreadView_TutorialHintVisibility(object instance)
	{
		return ((VineMessagesThreadView)instance).TutorialHintVisibility;
	}

	private void set_801_VineMessagesThreadView_TutorialHintVisibility(object instance, object Value)
	{
		((VineMessagesThreadView)instance).TutorialHintVisibility = (bool)Value;
	}

	private object get_802_VineMessagesThreadView_IsKeyboardVisible(object instance)
	{
		return ((VineMessagesThreadView)instance).IsKeyboardVisible;
	}

	private void set_802_VineMessagesThreadView_IsKeyboardVisible(object instance, object Value)
	{
		((VineMessagesThreadView)instance).IsKeyboardVisible = (bool)Value;
	}

	private object get_803_VineMessagesThreadView_IsFinishedLoading(object instance)
	{
		return ((VineMessagesThreadView)instance).IsFinishedLoading;
	}

	private void set_803_VineMessagesThreadView_IsFinishedLoading(object instance, object Value)
	{
		((VineMessagesThreadView)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_804_VineMessagesThreadView_SendEnabled(object instance)
	{
		return ((VineMessagesThreadView)instance).SendEnabled;
	}

	private object get_805_VineMessagesThreadView_IsViewModelLoaded(object instance)
	{
		return ((VineMessagesThreadView)instance).IsViewModelLoaded;
	}

	private void set_805_VineMessagesThreadView_IsViewModelLoaded(object instance, object Value)
	{
		((VineMessagesThreadView)instance).IsViewModelLoaded = (bool)Value;
	}

	private object get_806_VineMessagesThreadView_IsVineUser(object instance)
	{
		return ((VineMessagesThreadView)instance).IsVineUser;
	}

	private object get_807_VineMessagesThreadView_IsVolumeMuted(object instance)
	{
		return ((VineMessagesThreadView)instance).IsVolumeMuted;
	}

	private object get_808_VineMessagesThreadView_MuteIcon(object instance)
	{
		return ((VineMessagesThreadView)instance).MuteIcon;
	}

	private object get_809_VineMessagesThreadView_MuteLabel(object instance)
	{
		return ((VineMessagesThreadView)instance).MuteLabel;
	}

	private object get_810_VineListTemplateSelector_PostTemplate(object instance)
	{
		return ((VineListTemplateSelector)instance).PostTemplate;
	}

	private void set_810_VineListTemplateSelector_PostTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineListTemplateSelector)instance).PostTemplate = (DataTemplate)Value;
	}

	private object get_811_VineListTemplateSelector_PostMosaicTemplate(object instance)
	{
		return ((VineListTemplateSelector)instance).PostMosaicTemplate;
	}

	private void set_811_VineListTemplateSelector_PostMosaicTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineListTemplateSelector)instance).PostMosaicTemplate = (DataTemplate)Value;
	}

	private object get_812_VineListTemplateSelector_AvatarPostMosaicTemplate(object instance)
	{
		return ((VineListTemplateSelector)instance).AvatarPostMosaicTemplate;
	}

	private void set_812_VineListTemplateSelector_AvatarPostMosaicTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineListTemplateSelector)instance).AvatarPostMosaicTemplate = (DataTemplate)Value;
	}

	private object get_813_VineListTemplateSelector_UrlActionTemplate(object instance)
	{
		return ((VineListTemplateSelector)instance).UrlActionTemplate;
	}

	private void set_813_VineListTemplateSelector_UrlActionTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineListTemplateSelector)instance).UrlActionTemplate = (DataTemplate)Value;
	}

	private object get_814_VineListTemplateSelector_UserMosaicTemplate(object instance)
	{
		return ((VineListTemplateSelector)instance).UserMosaicTemplate;
	}

	private void set_814_VineListTemplateSelector_UserMosaicTemplate(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VineListTemplateSelector)instance).UserMosaicTemplate = (DataTemplate)Value;
	}

	private object get_815_VinePressedButton_ReleasedUI(object instance)
	{
		return ((VinePressedButton)instance).ReleasedUI;
	}

	private void set_815_VinePressedButton_ReleasedUI(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VinePressedButton)instance).ReleasedUI = (FrameworkElement)Value;
	}

	private object get_816_VinePressedButton_PressedUI(object instance)
	{
		return ((VinePressedButton)instance).PressedUI;
	}

	private void set_816_VinePressedButton_PressedUI(object instance, object Value)
	{
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((VinePressedButton)instance).PressedUI = (FrameworkElement)Value;
	}

	private object get_817_ResetPasswordView_ExampleEmail(object instance)
	{
		return ((ResetPasswordView)instance).ExampleEmail;
	}

	private object get_818_ResetPasswordView_ResetPasswordText(object instance)
	{
		return ((ResetPasswordView)instance).ResetPasswordText;
	}

	private object get_819_ResetPasswordView_IsBusy(object instance)
	{
		return ((ResetPasswordView)instance).IsBusy;
	}

	private void set_819_ResetPasswordView_IsBusy(object instance, object Value)
	{
		((ResetPasswordView)instance).IsBusy = (bool)Value;
	}

	private object get_820_ResetPasswordView_Email(object instance)
	{
		return ((ResetPasswordView)instance).Email;
	}

	private void set_820_ResetPasswordView_Email(object instance, object Value)
	{
		((ResetPasswordView)instance).Email = (string)Value;
	}

	private object get_821_ResetPasswordView_Items(object instance)
	{
		return ((ResetPasswordView)instance).Items;
	}

	private void set_821_ResetPasswordView_Items(object instance, object Value)
	{
		((ResetPasswordView)instance).Items = (ObservableCollection<VineUserModel>)Value;
	}

	private object get_822_VineUserListView_HasError(object instance)
	{
		return ((VineUserListView)instance).HasError;
	}

	private void set_822_VineUserListView_HasError(object instance, object Value)
	{
		((VineUserListView)instance).HasError = (bool)Value;
	}

	private object get_823_VineUserListView_PageTitle(object instance)
	{
		return ((VineUserListView)instance).PageTitle;
	}

	private void set_823_VineUserListView_PageTitle(object instance, object Value)
	{
		((VineUserListView)instance).PageTitle = (string)Value;
	}

	private object get_824_VineUserListView_IsBusy(object instance)
	{
		return ((VineUserListView)instance).IsBusy;
	}

	private void set_824_VineUserListView_IsBusy(object instance, object Value)
	{
		((VineUserListView)instance).IsBusy = (bool)Value;
	}

	private object get_825_VineUserListView_EmptyText(object instance)
	{
		return ((VineUserListView)instance).EmptyText;
	}

	private void set_825_VineUserListView_EmptyText(object instance, object Value)
	{
		((VineUserListView)instance).EmptyText = (string)Value;
	}

	private object get_826_VineUserListView_Items(object instance)
	{
		return ((VineUserListView)instance).Items;
	}

	private void set_826_VineUserListView_Items(object instance, object Value)
	{
		((VineUserListView)instance).Items = (IncrementalLoadingCollection<VineUserModel>)Value;
	}

	private object get_827_VineUserListView_Params(object instance)
	{
		return ((VineUserListView)instance).Params;
	}

	private object get_828_VineUserListView_IsFinishedLoading(object instance)
	{
		return ((VineUserListView)instance).IsFinishedLoading;
	}

	private void set_828_VineUserListView_IsFinishedLoading(object instance, object Value)
	{
		((VineUserListView)instance).IsFinishedLoading = (bool)Value;
	}

	private object get_829_VineUserListView_IsEmpty(object instance)
	{
		return ((VineUserListView)instance).IsEmpty;
	}

	private void set_829_VineUserListView_IsEmpty(object instance, object Value)
	{
		((VineUserListView)instance).IsEmpty = (bool)Value;
	}

	private object get_830_VineUserListView_ShowLoopCount(object instance)
	{
		return ((VineUserListView)instance).ShowLoopCount;
	}

	private void set_830_VineUserListView_ShowLoopCount(object instance, object Value)
	{
		((VineUserListView)instance).ShowLoopCount = (bool)Value;
	}

	private object get_831_WebView_WebUrl(object instance)
	{
		return ((WebView)instance).WebUrl;
	}

	private void set_831_WebView_WebUrl(object instance, object Value)
	{
		((WebView)instance).WebUrl = (string)Value;
	}

	private object get_832_WebView_IsBusy(object instance)
	{
		return ((WebView)instance).IsBusy;
	}

	private void set_832_WebView_IsBusy(object instance, object Value)
	{
		((WebView)instance).IsBusy = (bool)Value;
	}

	private object get_833_MediaFile_VideoIndex(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).VideoIndex;
	}

	private void set_833_MediaFile_VideoIndex(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		((MediaFile)instance).VideoIndex = (int)Value;
	}

	private object get_834_MediaFile_AudioIndex(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).AudioIndex;
	}

	private void set_834_MediaFile_AudioIndex(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		((MediaFile)instance).AudioIndex = (int)Value;
	}

	private object get_835_MediaFile_VideoStreamCount(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).VideoStreamCount;
	}

	private void set_835_MediaFile_VideoStreamCount(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		((MediaFile)instance).VideoStreamCount = (int)Value;
	}

	private object get_836_MediaFile_AudioStreamCount(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).AudioStreamCount;
	}

	private void set_836_MediaFile_AudioStreamCount(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		((MediaFile)instance).AudioStreamCount = (int)Value;
	}

	private object get_837_MediaFile_StreamCount(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).StreamCount;
	}

	private void set_837_MediaFile_StreamCount(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		((MediaFile)instance).StreamCount = (uint)Value;
	}

	private object get_838_MediaFile_bEOF(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).bEOF;
	}

	private void set_838_MediaFile_bEOF(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		((MediaFile)instance).bEOF = (bool)Value;
	}

	private object get_839_MediaFile_AudioStream(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).AudioStream;
	}

	private void set_839_MediaFile_AudioStream(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((MediaFile)instance).AudioStream = (StreamInfo)Value;
	}

	private object get_840_MediaFile_VideoStream(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).VideoStream;
	}

	private void set_840_MediaFile_VideoStream(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((MediaFile)instance).VideoStream = (StreamInfo)Value;
	}

	private object get_841_MediaFile_HasAudio(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).HasAudio;
	}

	private void set_841_MediaFile_HasAudio(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		((MediaFile)instance).HasAudio = (bool)Value;
	}

	private object get_842_MediaFile_HasVideo(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).HasVideo;
	}

	private void set_842_MediaFile_HasVideo(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		((MediaFile)instance).HasVideo = (bool)Value;
	}

	private object get_843_MediaFile_AudioProperty(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).AudioProperty;
	}

	private void set_843_MediaFile_AudioProperty(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((MediaFile)instance).AudioProperty = (AudioProp)Value;
	}

	private object get_844_MediaFile_VideoProperty(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).VideoProperty;
	}

	private void set_844_MediaFile_VideoProperty(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((MediaFile)instance).VideoProperty = (VideoProp)Value;
	}

	private object get_845_MediaFile_BitmapImg(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).BitmapImg;
	}

	private void set_845_MediaFile_BitmapImg(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((MediaFile)instance).BitmapImg = (BitmapImage)Value;
	}

	private object get_846_MediaFile_FileThumbnail(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).FileThumbnail;
	}

	private void set_846_MediaFile_FileThumbnail(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		//IL_0007: Unknown result type (might be due to invalid IL or missing references)
		//IL_0011: Expected O, but got Unknown
		((MediaFile)instance).FileThumbnail = (StorageItemThumbnail)Value;
	}

	private object get_847_MediaFile_File(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).File;
	}

	private object get_848_MediaFile_Path(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).Path;
	}

	private object get_849_MediaFile_Name(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).Name;
	}

	private object get_850_MediaFile_ValidInfo(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).ValidInfo;
	}

	private void set_850_MediaFile_ValidInfo(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		((MediaFile)instance).ValidInfo = (string)Value;
	}

	private object get_851_MediaFile_Prepped(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).Prepped;
	}

	private void set_851_MediaFile_Prepped(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		((MediaFile)instance).Prepped = (bool)Value;
	}

	private object get_852_MediaFile_UseHardware(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).UseHardware;
	}

	private void set_852_MediaFile_UseHardware(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		((MediaFile)instance).UseHardware = (bool)Value;
	}

	private object get_853_MediaFile_EditedLength(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).EditedLength;
	}

	private object get_854_MediaFile_OriginalLength(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).OriginalLength;
	}

	private void set_854_MediaFile_OriginalLength(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		((MediaFile)instance).OriginalLength = (long)Value;
	}

	private object get_855_MediaFile_EndOffset(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).EndOffset;
	}

	private void set_855_MediaFile_EndOffset(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		((MediaFile)instance).EndOffset = (long)Value;
	}

	private object get_856_MediaFile_StartOffset(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).StartOffset;
	}

	private void set_856_MediaFile_StartOffset(object instance, object Value)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		((MediaFile)instance).StartOffset = (long)Value;
	}

	private object get_857_MediaFile_VideoOnly(object instance)
	{
		//IL_0001: Unknown result type (might be due to invalid IL or missing references)
		return ((MediaFile)instance).VideoOnly;
	}

	private IXamlMember CreateXamlMember(string longMemberName)
	{
		XamlMember xamlMember = null;
		switch (longMemberName)
		{
		case "Windows.UI.Color.A":
			_ = (XamlUserType)(object)GetXamlTypeByName("Windows.UI.Color");
			xamlMember = new XamlMember(this, "A", "Byte");
			xamlMember.Getter = get_0_Color_A;
			xamlMember.Setter = set_0_Color_A;
			break;
		case "Windows.UI.Color.B":
			_ = (XamlUserType)(object)GetXamlTypeByName("Windows.UI.Color");
			xamlMember = new XamlMember(this, "B", "Byte");
			xamlMember.Getter = get_1_Color_B;
			xamlMember.Setter = set_1_Color_B;
			break;
		case "Windows.UI.Color.G":
			_ = (XamlUserType)(object)GetXamlTypeByName("Windows.UI.Color");
			xamlMember = new XamlMember(this, "G", "Byte");
			xamlMember.Getter = get_2_Color_G;
			xamlMember.Setter = set_2_Color_G;
			break;
		case "Windows.UI.Color.R":
			_ = (XamlUserType)(object)GetXamlTypeByName("Windows.UI.Color");
			xamlMember = new XamlMember(this, "R", "Byte");
			xamlMember.Getter = get_3_Color_R;
			xamlMember.Setter = set_3_Color_R;
			break;
		case "Vine.Converters.VisibleIfTrueConverter.InvertValue":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Converters.VisibleIfTrueConverter");
			xamlMember = new XamlMember(this, "InvertValue", "Boolean");
			xamlMember.Getter = get_4_VisibleIfTrueConverter_InvertValue;
			xamlMember.Setter = set_4_VisibleIfTrueConverter_InvertValue;
			break;
		case "Vine.Converters.NoneToVisibilityConverter.InvertValue":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Converters.NoneToVisibilityConverter");
			xamlMember = new XamlMember(this, "InvertValue", "Boolean");
			xamlMember.Getter = get_5_NoneToVisibilityConverter_InvertValue;
			xamlMember.Setter = set_5_NoneToVisibilityConverter_InvertValue;
			break;
		case "Vine.Views.AvatarControl.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.AvatarControl");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_6_AvatarControl_IsBusy;
			xamlMember.Setter = set_6_AvatarControl_IsBusy;
			break;
		case "Vine.Views.AvatarControl.BusyVisible":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.AvatarControl");
			xamlMember = new XamlMember(this, "BusyVisible", "Windows.UI.Xaml.Visibility");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_7_AvatarControl_BusyVisible;
			xamlMember.Setter = set_7_AvatarControl_BusyVisible;
			break;
		case "Vine.Views.AvatarControl.DisableFlyout":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.AvatarControl");
			xamlMember = new XamlMember(this, "DisableFlyout", "Boolean");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_8_AvatarControl_DisableFlyout;
			xamlMember.Setter = set_8_AvatarControl_DisableFlyout;
			break;
		case "Vine.Views.AvatarControl.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.AvatarControl");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_9_AvatarControl_User;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Framework.BasePage.NavigationHelper":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Framework.BasePage");
			xamlMember = new XamlMember(this, "NavigationHelper", "Vine.Framework.NavigationHelper");
			xamlMember.Getter = get_10_BasePage_NavigationHelper;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Framework.BasePage.NavigationParam":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Framework.BasePage");
			xamlMember = new XamlMember(this, "NavigationParam", "Object");
			xamlMember.Getter = get_11_BasePage_NavigationParam;
			xamlMember.Setter = set_11_BasePage_NavigationParam;
			break;
		case "Vine.Framework.BasePage.NavigationObject":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Framework.BasePage");
			xamlMember = new XamlMember(this, "NavigationObject", "Object");
			xamlMember.Getter = get_12_BasePage_NavigationObject;
			xamlMember.Setter = set_12_BasePage_NavigationObject;
			break;
		case "Vine.Framework.BasePage.AlwaysClearBackStack":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Framework.BasePage");
			xamlMember = new XamlMember(this, "AlwaysClearBackStack", "Boolean");
			xamlMember.Getter = get_13_BasePage_AlwaysClearBackStack;
			xamlMember.Setter = set_13_BasePage_AlwaysClearBackStack;
			break;
		case "Vine.Framework.NotifyPage.WindowWidth":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Framework.NotifyPage");
			xamlMember = new XamlMember(this, "WindowWidth", "Double");
			xamlMember.Getter = get_14_NotifyPage_WindowWidth;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Framework.NotifyPage.WindowHeight":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Framework.NotifyPage");
			xamlMember = new XamlMember(this, "WindowHeight", "Double");
			xamlMember.Getter = get_15_NotifyPage_WindowHeight;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Framework.NotifyPage.WindowWidthGridLength":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Framework.NotifyPage");
			xamlMember = new XamlMember(this, "WindowWidthGridLength", "Windows.UI.Xaml.GridLength");
			xamlMember.Getter = get_16_NotifyPage_WindowWidthGridLength;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView10.CurrentTutorialState":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "CurrentTutorialState", "Vine.Views.Capture.CaptureView10.TutorialState");
			xamlMember.Getter = get_17_CaptureView10_CurrentTutorialState;
			xamlMember.Setter = set_17_CaptureView10_CurrentTutorialState;
			break;
		case "Vine.Views.Capture.CaptureView10.TutorialHintVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "TutorialHintVisibility", "Boolean");
			xamlMember.Getter = get_18_CaptureView10_TutorialHintVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView10.TutorialWelcomeVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "TutorialWelcomeVisibility", "Boolean");
			xamlMember.Getter = get_19_CaptureView10_TutorialWelcomeVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView10.TutorialMessage":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "TutorialMessage", "String");
			xamlMember.Getter = get_20_CaptureView10_TutorialMessage;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView10.ButtonTutorialCameraToolsVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "ButtonTutorialCameraToolsVisibility", "Boolean");
			xamlMember.Getter = get_21_CaptureView10_ButtonTutorialCameraToolsVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView10.ButtonTutorialUndoVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "ButtonTutorialUndoVisibility", "Boolean");
			xamlMember.Getter = get_22_CaptureView10_ButtonTutorialUndoVisibility;
			xamlMember.Setter = set_22_CaptureView10_ButtonTutorialUndoVisibility;
			break;
		case "Vine.Views.Capture.CaptureView10.ButtonTutorialGrabVideoVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "ButtonTutorialGrabVideoVisibility", "Boolean");
			xamlMember.Getter = get_23_CaptureView10_ButtonTutorialGrabVideoVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView10.ButtonTutorialState":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "ButtonTutorialState", "Vine.Views.Capture.CaptureView10.ButtonsTutorialEnum");
			xamlMember.Getter = get_24_CaptureView10_ButtonTutorialState;
			xamlMember.Setter = set_24_CaptureView10_ButtonTutorialState;
			break;
		case "Vine.Views.Capture.CaptureView10.PreviewGridClip":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "PreviewGridClip", "Windows.Foundation.Rect");
			xamlMember.Getter = get_25_CaptureView10_PreviewGridClip;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView10.CaptureMargin":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "CaptureMargin", "Windows.UI.Xaml.Thickness");
			xamlMember.Getter = get_26_CaptureView10_CaptureMargin;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView10.NextButtonVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "NextButtonVisibility", "Windows.UI.Xaml.Visibility");
			xamlMember.Getter = get_27_CaptureView10_NextButtonVisibility;
			xamlMember.Setter = set_27_CaptureView10_NextButtonVisibility;
			break;
		case "Vine.Views.Capture.CaptureView10.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_28_CaptureView10_IsBusy;
			xamlMember.Setter = set_28_CaptureView10_IsBusy;
			break;
		case "Vine.Views.Capture.CaptureView10.IsTorchSupported":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "IsTorchSupported", "Boolean");
			xamlMember.Getter = get_29_CaptureView10_IsTorchSupported;
			xamlMember.Setter = set_29_CaptureView10_IsTorchSupported;
			break;
		case "Vine.Views.Capture.CaptureView10.IsFrontCameraSupported":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "IsFrontCameraSupported", "Boolean");
			xamlMember.Getter = get_30_CaptureView10_IsFrontCameraSupported;
			xamlMember.Setter = set_30_CaptureView10_IsFrontCameraSupported;
			break;
		case "Vine.Views.Capture.CaptureView10.IsFocusSupported":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "IsFocusSupported", "Boolean");
			xamlMember.Getter = get_31_CaptureView10_IsFocusSupported;
			xamlMember.Setter = set_31_CaptureView10_IsFocusSupported;
			break;
		case "Vine.Views.Capture.CaptureView10.IsFocusLocked":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "IsFocusLocked", "Boolean");
			xamlMember.Getter = get_32_CaptureView10_IsFocusLocked;
			xamlMember.Setter = set_32_CaptureView10_IsFocusLocked;
			break;
		case "Vine.Views.Capture.CaptureView10.IsGhostModeHighlighted":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "IsGhostModeHighlighted", "Boolean");
			xamlMember.Getter = get_33_CaptureView10_IsGhostModeHighlighted;
			xamlMember.Setter = set_33_CaptureView10_IsGhostModeHighlighted;
			break;
		case "Vine.Views.Capture.CaptureView10.IsGridHighlighted":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "IsGridHighlighted", "Boolean");
			xamlMember.Getter = get_34_CaptureView10_IsGridHighlighted;
			xamlMember.Setter = set_34_CaptureView10_IsGridHighlighted;
			break;
		case "Vine.Views.Capture.CaptureView10.IsExpanded":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "IsExpanded", "Boolean");
			xamlMember.Getter = get_35_CaptureView10_IsExpanded;
			xamlMember.Setter = set_35_CaptureView10_IsExpanded;
			break;
		case "Vine.Views.Capture.CaptureView10.IsGridVisible":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "IsGridVisible", "Boolean");
			xamlMember.Getter = get_36_CaptureView10_IsGridVisible;
			xamlMember.Setter = set_36_CaptureView10_IsGridVisible;
			break;
		case "Vine.Views.Capture.CaptureView10.IsUndoHighlighted":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "IsUndoHighlighted", "Boolean");
			xamlMember.Getter = get_37_CaptureView10_IsUndoHighlighted;
			xamlMember.Setter = set_37_CaptureView10_IsUndoHighlighted;
			break;
		case "Vine.Views.Capture.CaptureView10.IsCameraHighlighted":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "IsCameraHighlighted", "Boolean");
			xamlMember.Getter = get_38_CaptureView10_IsCameraHighlighted;
			xamlMember.Setter = set_38_CaptureView10_IsCameraHighlighted;
			break;
		case "Vine.Views.Capture.CaptureView10.IsTorchHighlighted":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "IsTorchHighlighted", "Boolean");
			xamlMember.Getter = get_39_CaptureView10_IsTorchHighlighted;
			xamlMember.Setter = set_39_CaptureView10_IsTorchHighlighted;
			break;
		case "Vine.Views.Capture.CaptureView10.IsFocusModeHighlighted":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "IsFocusModeHighlighted", "Boolean");
			xamlMember.Getter = get_40_CaptureView10_IsFocusModeHighlighted;
			xamlMember.Setter = set_40_CaptureView10_IsFocusModeHighlighted;
			break;
		case "Vine.Views.Capture.CaptureView10.PendingChanges":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "PendingChanges", "Boolean");
			xamlMember.Getter = get_41_CaptureView10_PendingChanges;
			xamlMember.Setter = set_41_CaptureView10_PendingChanges;
			break;
		case "Vine.Views.Capture.CaptureView10.RecordingDraftCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "RecordingDraftCount", "Int32");
			xamlMember.Getter = get_42_CaptureView10_RecordingDraftCount;
			xamlMember.Setter = set_42_CaptureView10_RecordingDraftCount;
			break;
		case "Vine.Views.Capture.CaptureView10.DraftNumber":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "DraftNumber", "String");
			xamlMember.Getter = get_43_CaptureView10_DraftNumber;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView10.IsDraftsEnabled":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "IsDraftsEnabled", "Boolean");
			xamlMember.Getter = get_44_CaptureView10_IsDraftsEnabled;
			xamlMember.Setter = set_44_CaptureView10_IsDraftsEnabled;
			break;
		case "Vine.Views.Capture.CaptureView10.IsUndoEnabled":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "IsUndoEnabled", "Boolean");
			xamlMember.Getter = get_45_CaptureView10_IsUndoEnabled;
			xamlMember.Setter = set_45_CaptureView10_IsUndoEnabled;
			break;
		case "Vine.Views.Capture.CaptureView10.GhostImageSource":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "GhostImageSource", "Windows.UI.Xaml.Media.ImageSource");
			xamlMember.Getter = get_46_CaptureView10_GhostImageSource;
			xamlMember.Setter = set_46_CaptureView10_GhostImageSource;
			break;
		case "Vine.Views.Capture.CaptureView10.FocusButtonBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "FocusButtonBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_47_CaptureView10_FocusButtonBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView10.GhostButtonBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "GhostButtonBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_48_CaptureView10_GhostButtonBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView10.GridButtonBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "GridButtonBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_49_CaptureView10_GridButtonBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView10.CameraButtonBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "CameraButtonBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_50_CaptureView10_CameraButtonBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView10.WrenchBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "WrenchBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_51_CaptureView10_WrenchBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView10.TorchButtonBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "TorchButtonBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_52_CaptureView10_TorchButtonBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView10.MediaCapture":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "MediaCapture", "Windows.Media.Capture.MediaCapture");
			xamlMember.Getter = get_53_CaptureView10_MediaCapture;
			xamlMember.Setter = set_53_CaptureView10_MediaCapture;
			break;
		case "Vine.Views.Capture.CaptureView10.VMParameters":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView10");
			xamlMember = new XamlMember(this, "VMParameters", "Vine.Models.ReplyVmParameters");
			xamlMember.Getter = get_54_CaptureView10_VMParameters;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.ChannelSelectView.Items":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ChannelSelectView");
			xamlMember = new XamlMember(this, "Items", "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.ChannelModel>");
			xamlMember.Getter = get_55_ChannelSelectView_Items;
			xamlMember.Setter = set_55_ChannelSelectView_Items;
			break;
		case "Vine.Models.ChannelModel.IconFullUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.ChannelModel");
			xamlMember = new XamlMember(this, "IconFullUrl", "String");
			xamlMember.Getter = get_56_ChannelModel_IconFullUrl;
			xamlMember.Setter = set_56_ChannelModel_IconFullUrl;
			break;
		case "Vine.Models.ChannelModel.ChannelId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.ChannelModel");
			xamlMember = new XamlMember(this, "ChannelId", "String");
			xamlMember.Getter = get_57_ChannelModel_ChannelId;
			xamlMember.Setter = set_57_ChannelModel_ChannelId;
			break;
		case "Vine.Models.ChannelModel.ExploreName":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.ChannelModel");
			xamlMember = new XamlMember(this, "ExploreName", "String");
			xamlMember.Getter = get_58_ChannelModel_ExploreName;
			xamlMember.Setter = set_58_ChannelModel_ExploreName;
			break;
		case "Vine.Views.Capture.ChannelSelectView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ChannelSelectView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_59_ChannelSelectView_IsBusy;
			xamlMember.Setter = set_59_ChannelSelectView_IsBusy;
			break;
		case "Vine.Views.Capture.DraftsView.Items":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.DraftsView");
			xamlMember = new XamlMember(this, "Items", "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.RecordingVineModel>");
			xamlMember.Getter = get_60_DraftsView_Items;
			xamlMember.Setter = set_60_DraftsView_Items;
			break;
		case "Vine.Models.RecordingVineModel.DraftId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingVineModel");
			xamlMember = new XamlMember(this, "DraftId", "String");
			xamlMember.Getter = get_61_RecordingVineModel_DraftId;
			xamlMember.Setter = set_61_RecordingVineModel_DraftId;
			break;
		case "Vine.Models.RecordingVineModel.UploadId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingVineModel");
			xamlMember = new XamlMember(this, "UploadId", "String");
			xamlMember.Getter = get_62_RecordingVineModel_UploadId;
			xamlMember.Setter = set_62_RecordingVineModel_UploadId;
			break;
		case "Vine.Models.RecordingVineModel.Clips":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingVineModel");
			xamlMember = new XamlMember(this, "Clips", "System.Collections.Generic.List`1<Vine.Models.RecordingClipModel>");
			xamlMember.Getter = get_63_RecordingVineModel_Clips;
			xamlMember.Setter = set_63_RecordingVineModel_Clips;
			break;
		case "Vine.Models.RecordingClipModel.VideoFilePath":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingClipModel");
			xamlMember = new XamlMember(this, "VideoFilePath", "String");
			xamlMember.Getter = get_64_RecordingClipModel_VideoFilePath;
			xamlMember.Setter = set_64_RecordingClipModel_VideoFilePath;
			break;
		case "Vine.Models.RecordingClipModel.GhostFilePath":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingClipModel");
			xamlMember = new XamlMember(this, "GhostFilePath", "String");
			xamlMember.Getter = get_65_RecordingClipModel_GhostFilePath;
			xamlMember.Setter = set_65_RecordingClipModel_GhostFilePath;
			break;
		case "Vine.Models.RecordingClipModel.VideoFileDuration":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingClipModel");
			xamlMember = new XamlMember(this, "VideoFileDuration", "Int64");
			xamlMember.Getter = get_66_RecordingClipModel_VideoFileDuration;
			xamlMember.Setter = set_66_RecordingClipModel_VideoFileDuration;
			break;
		case "Vine.Models.RecordingClipModel.FrameRate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingClipModel");
			xamlMember = new XamlMember(this, "FrameRate", "Int64");
			xamlMember.Getter = get_67_RecordingClipModel_FrameRate;
			xamlMember.Setter = set_67_RecordingClipModel_FrameRate;
			break;
		case "Vine.Models.RecordingClipModel.FileStartTime":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingClipModel");
			xamlMember = new XamlMember(this, "FileStartTime", "Int64");
			xamlMember.Getter = get_68_RecordingClipModel_FileStartTime;
			xamlMember.Setter = set_68_RecordingClipModel_FileStartTime;
			break;
		case "Vine.Models.RecordingClipModel.FileEndTime":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingClipModel");
			xamlMember = new XamlMember(this, "FileEndTime", "Int64");
			xamlMember.Getter = get_69_RecordingClipModel_FileEndTime;
			xamlMember.Setter = set_69_RecordingClipModel_FileEndTime;
			break;
		case "Vine.Models.RecordingClipModel.EditStartTime":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingClipModel");
			xamlMember = new XamlMember(this, "EditStartTime", "Int64");
			xamlMember.Getter = get_70_RecordingClipModel_EditStartTime;
			xamlMember.Setter = set_70_RecordingClipModel_EditStartTime;
			break;
		case "Vine.Models.RecordingClipModel.EditEndTime":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingClipModel");
			xamlMember = new XamlMember(this, "EditEndTime", "Int64");
			xamlMember.Getter = get_71_RecordingClipModel_EditEndTime;
			xamlMember.Setter = set_71_RecordingClipModel_EditEndTime;
			break;
		case "Vine.Models.RecordingVineModel.LastRenderedVideoFilePath":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingVineModel");
			xamlMember = new XamlMember(this, "LastRenderedVideoFilePath", "String");
			xamlMember.Getter = get_72_RecordingVineModel_LastRenderedVideoFilePath;
			xamlMember.Setter = set_72_RecordingVineModel_LastRenderedVideoFilePath;
			break;
		case "Vine.Models.RecordingVineModel.LastRenderedThumbFilePath":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingVineModel");
			xamlMember = new XamlMember(this, "LastRenderedThumbFilePath", "String");
			xamlMember.Getter = get_73_RecordingVineModel_LastRenderedThumbFilePath;
			xamlMember.Setter = set_73_RecordingVineModel_LastRenderedThumbFilePath;
			break;
		case "Vine.Models.RecordingVineModel.SavedClips":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingVineModel");
			xamlMember = new XamlMember(this, "SavedClips", "Int32");
			xamlMember.Getter = get_74_RecordingVineModel_SavedClips;
			xamlMember.Setter = set_74_RecordingVineModel_SavedClips;
			break;
		case "Vine.Models.RecordingVineModel.HasPendingChangesOnCapture":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingVineModel");
			xamlMember = new XamlMember(this, "HasPendingChangesOnCapture", "Boolean");
			xamlMember.Getter = get_75_RecordingVineModel_HasPendingChangesOnCapture;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.RecordingVineModel.Duration":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingVineModel");
			xamlMember = new XamlMember(this, "Duration", "Int64");
			xamlMember.Getter = get_76_RecordingVineModel_Duration;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.RecordingVineModel.IsClipsSequentialOneFile":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.RecordingVineModel");
			xamlMember = new XamlMember(this, "IsClipsSequentialOneFile", "Boolean");
			xamlMember.Getter = get_77_RecordingVineModel_IsClipsSequentialOneFile;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.DraftsView.DraftId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.DraftsView");
			xamlMember = new XamlMember(this, "DraftId", "String");
			xamlMember.Getter = get_78_DraftsView_DraftId;
			xamlMember.Setter = set_78_DraftsView_DraftId;
			break;
		case "Vine.Views.Capture.EditClipsView.Items":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsView");
			xamlMember = new XamlMember(this, "Items", "System.Collections.ObjectModel.ObservableCollection`1<Vine.Views.Capture.EditClipsViewModel>");
			xamlMember.Getter = get_79_EditClipsView_Items;
			xamlMember.Setter = set_79_EditClipsView_Items;
			break;
		case "Vine.Views.Capture.EditClipsViewModel.Thumb":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsViewModel");
			xamlMember = new XamlMember(this, "Thumb", "Windows.UI.Xaml.Media.ImageSource");
			xamlMember.Getter = get_80_EditClipsViewModel_Thumb;
			xamlMember.Setter = set_80_EditClipsViewModel_Thumb;
			break;
		case "Vine.Views.Capture.EditClipsViewModel.Composition":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsViewModel");
			xamlMember = new XamlMember(this, "Composition", "Windows.Media.Editing.MediaComposition");
			xamlMember.Getter = get_81_EditClipsViewModel_Composition;
			xamlMember.Setter = set_81_EditClipsViewModel_Composition;
			break;
		case "Vine.Views.Capture.EditClipsViewModel.MediaClip":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsViewModel");
			xamlMember = new XamlMember(this, "MediaClip", "Windows.Media.Editing.MediaClip");
			xamlMember.Getter = get_82_EditClipsViewModel_MediaClip;
			xamlMember.Setter = set_82_EditClipsViewModel_MediaClip;
			break;
		case "Vine.Views.Capture.EditClipsViewModel.ClipModel":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsViewModel");
			xamlMember = new XamlMember(this, "ClipModel", "Vine.Models.RecordingClipModel");
			xamlMember.Getter = get_83_EditClipsViewModel_ClipModel;
			xamlMember.Setter = set_83_EditClipsViewModel_ClipModel;
			break;
		case "Vine.Views.Capture.EditClipsViewModel.IsActive":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsViewModel");
			xamlMember = new XamlMember(this, "IsActive", "Boolean");
			xamlMember.Getter = get_84_EditClipsViewModel_IsActive;
			xamlMember.Setter = set_84_EditClipsViewModel_IsActive;
			break;
		case "Vine.Views.Capture.EditClipsViewModel.Opacity":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsViewModel");
			xamlMember = new XamlMember(this, "Opacity", "Double");
			xamlMember.Getter = get_85_EditClipsViewModel_Opacity;
			xamlMember.Setter = set_85_EditClipsViewModel_Opacity;
			break;
		case "Vine.Views.Capture.EditClipsViewModel.IsPlaying":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsViewModel");
			xamlMember = new XamlMember(this, "IsPlaying", "Boolean");
			xamlMember.Getter = get_86_EditClipsViewModel_IsPlaying;
			xamlMember.Setter = set_86_EditClipsViewModel_IsPlaying;
			break;
		case "Vine.Views.Capture.EditClipsView.TrimThumbnails":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsView");
			xamlMember = new XamlMember(this, "TrimThumbnails", "System.Collections.ObjectModel.ObservableCollection`1<Windows.UI.Xaml.Media.Imaging.BitmapImage>");
			xamlMember.Getter = get_87_EditClipsView_TrimThumbnails;
			xamlMember.Setter = set_87_EditClipsView_TrimThumbnails;
			break;
		case "Vine.Views.Capture.EditClipsView.TrimThumbnailWidth":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsView");
			xamlMember = new XamlMember(this, "TrimThumbnailWidth", "Double");
			xamlMember.Getter = get_88_EditClipsView_TrimThumbnailWidth;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.EditClipsView.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsView");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_89_EditClipsView_IsFinishedLoading;
			xamlMember.Setter = set_89_EditClipsView_IsFinishedLoading;
			break;
		case "Vine.Views.Capture.EditClipsView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_90_EditClipsView_IsBusy;
			xamlMember.Setter = set_90_EditClipsView_IsBusy;
			break;
		case "Vine.Views.Capture.EditClipsView.TrimHighlightRectX":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsView");
			xamlMember = new XamlMember(this, "TrimHighlightRectX", "Double");
			xamlMember.Getter = get_91_EditClipsView_TrimHighlightRectX;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.EditClipsView.TrimHighlightRectWidth":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsView");
			xamlMember = new XamlMember(this, "TrimHighlightRectWidth", "Double");
			xamlMember.Getter = get_92_EditClipsView_TrimHighlightRectWidth;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.EditClipsView.Selected":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsView");
			xamlMember = new XamlMember(this, "Selected", "Vine.Views.Capture.EditClipsViewModel");
			xamlMember.Getter = get_93_EditClipsView_Selected;
			xamlMember.Setter = set_93_EditClipsView_Selected;
			break;
		case "Vine.Views.Capture.EditClipsView.HasSelection":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsView");
			xamlMember = new XamlMember(this, "HasSelection", "Boolean");
			xamlMember.Getter = get_94_EditClipsView_HasSelection;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.EditClipsView.HasMoreThanOneClip":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsView");
			xamlMember = new XamlMember(this, "HasMoreThanOneClip", "Boolean");
			xamlMember.Getter = get_95_EditClipsView_HasMoreThanOneClip;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.EditClipsView.RightSliderValue":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsView");
			xamlMember = new XamlMember(this, "RightSliderValue", "Double");
			xamlMember.Getter = get_96_EditClipsView_RightSliderValue;
			xamlMember.Setter = set_96_EditClipsView_RightSliderValue;
			break;
		case "Vine.Views.Capture.EditClipsView.LeftSliderValue":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.EditClipsView");
			xamlMember = new XamlMember(this, "LeftSliderValue", "Double");
			xamlMember.Getter = get_97_EditClipsView_LeftSliderValue;
			xamlMember.Setter = set_97_EditClipsView_LeftSliderValue;
			break;
		case "Vine.Views.Capture.ImportView.Items":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ImportView");
			xamlMember = new XamlMember(this, "Items", "Vine.Framework.RandomAccessLoadingCollection`1<Vine.Views.Capture.ImportViewModel>");
			xamlMember.Getter = get_98_ImportView_Items;
			xamlMember.Setter = set_98_ImportView_Items;
			break;
		case "Vine.Views.Capture.ImportView.IsAutoPlay":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ImportView");
			xamlMember = new XamlMember(this, "IsAutoPlay", "Boolean");
			xamlMember.Getter = get_99_ImportView_IsAutoPlay;
			xamlMember.Setter = set_99_ImportView_IsAutoPlay;
			break;
		case "Vine.Views.Capture.ImportView.TrimSliderValue":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ImportView");
			xamlMember = new XamlMember(this, "TrimSliderValue", "Double");
			xamlMember.Getter = get_100_ImportView_TrimSliderValue;
			xamlMember.Setter = set_100_ImportView_TrimSliderValue;
			break;
		case "Vine.Views.Capture.ImportView.ScrubValue":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ImportView");
			xamlMember = new XamlMember(this, "ScrubValue", "Double");
			xamlMember.Getter = get_101_ImportView_ScrubValue;
			xamlMember.Setter = set_101_ImportView_ScrubValue;
			break;
		case "Vine.Views.Capture.ImportView.ScrubSliderVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ImportView");
			xamlMember = new XamlMember(this, "ScrubSliderVisibility", "Windows.UI.Xaml.Visibility");
			xamlMember.Getter = get_102_ImportView_ScrubSliderVisibility;
			xamlMember.Setter = set_102_ImportView_ScrubSliderVisibility;
			break;
		case "Vine.Views.Capture.ImportView.ScrubImg":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ImportView");
			xamlMember = new XamlMember(this, "ScrubImg", "Windows.UI.Xaml.Media.ImageSource");
			xamlMember.Getter = get_103_ImportView_ScrubImg;
			xamlMember.Setter = set_103_ImportView_ScrubImg;
			break;
		case "Vine.Views.Capture.ImportView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ImportView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_104_ImportView_IsBusy;
			xamlMember.Setter = set_104_ImportView_IsBusy;
			break;
		case "Vine.Views.Capture.ImportView.IsProgressZero":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ImportView");
			xamlMember = new XamlMember(this, "IsProgressZero", "Boolean");
			xamlMember.Getter = get_105_ImportView_IsProgressZero;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.ImportView.ProgressValue":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ImportView");
			xamlMember = new XamlMember(this, "ProgressValue", "Double");
			xamlMember.Getter = get_106_ImportView_ProgressValue;
			xamlMember.Setter = set_106_ImportView_ProgressValue;
			break;
		case "Vine.Views.Capture.PreviewCaptureView.HasTutorialBeenSeen":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.PreviewCaptureView");
			xamlMember = new XamlMember(this, "HasTutorialBeenSeen", "Boolean");
			xamlMember.Getter = get_107_PreviewCaptureView_HasTutorialBeenSeen;
			xamlMember.Setter = set_107_PreviewCaptureView_HasTutorialBeenSeen;
			break;
		case "Vine.Views.Capture.PreviewCaptureView.TutorialMessage":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.PreviewCaptureView");
			xamlMember = new XamlMember(this, "TutorialMessage", "String");
			xamlMember.Getter = get_108_PreviewCaptureView_TutorialMessage;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.PreviewCaptureView.Params":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.PreviewCaptureView");
			xamlMember = new XamlMember(this, "Params", "Vine.Views.Capture.PreviewCaptureParams");
			xamlMember.Getter = get_109_PreviewCaptureView_Params;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.PreviewCaptureView.RenderFile":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.PreviewCaptureView");
			xamlMember = new XamlMember(this, "RenderFile", "Windows.Storage.StorageFile");
			xamlMember.Getter = get_110_PreviewCaptureView_RenderFile;
			xamlMember.Setter = set_110_PreviewCaptureView_RenderFile;
			break;
		case "Vine.Views.Capture.PreviewCaptureView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.PreviewCaptureView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_111_PreviewCaptureView_IsBusy;
			xamlMember.Setter = set_111_PreviewCaptureView_IsBusy;
			break;
		case "Vine.Views.Capture.PreviewCaptureView.IsProgressZero":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.PreviewCaptureView");
			xamlMember = new XamlMember(this, "IsProgressZero", "Boolean");
			xamlMember.Getter = get_112_PreviewCaptureView_IsProgressZero;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.PreviewCaptureView.ProgressValue":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.PreviewCaptureView");
			xamlMember = new XamlMember(this, "ProgressValue", "Double");
			xamlMember.Getter = get_113_PreviewCaptureView_ProgressValue;
			xamlMember.Setter = set_113_PreviewCaptureView_ProgressValue;
			break;
		case "Vine.Views.Capture.PreviewCaptureView.ShareButtonBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.PreviewCaptureView");
			xamlMember = new XamlMember(this, "ShareButtonBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_114_PreviewCaptureView_ShareButtonBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.PreviewCaptureView.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.PreviewCaptureView");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_115_PreviewCaptureView_IsFinishedLoading;
			xamlMember.Setter = set_115_PreviewCaptureView_IsFinishedLoading;
			break;
		case "Vine.Views.TemplateSelectors.TaggingTemplateSelector.MentionTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.TaggingTemplateSelector");
			xamlMember = new XamlMember(this, "MentionTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_116_TaggingTemplateSelector_MentionTemplate;
			xamlMember.Setter = set_116_TaggingTemplateSelector_MentionTemplate;
			break;
		case "Vine.Views.TemplateSelectors.TaggingTemplateSelector.HashtagTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.TaggingTemplateSelector");
			xamlMember = new XamlMember(this, "HashtagTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_117_TaggingTemplateSelector_HashtagTemplate;
			xamlMember.Setter = set_117_TaggingTemplateSelector_HashtagTemplate;
			break;
		case "Microsoft.Xaml.Interactivity.Interaction.Behaviors":
			_ = (XamlUserType)(object)GetXamlTypeByName("Microsoft.Xaml.Interactivity.Interaction");
			xamlMember = new XamlMember(this, "Behaviors", "Microsoft.Xaml.Interactivity.BehaviorCollection");
			xamlMember.SetTargetTypeName("Windows.UI.Xaml.DependencyObject");
			xamlMember.SetIsAttachable();
			xamlMember.Getter = get_118_Interaction_Behaviors;
			xamlMember.Setter = set_118_Interaction_Behaviors;
			break;
		case "Vine.Framework.Behavior`1<Windows.UI.Xaml.Controls.TextBox>.AssociatedObject":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Framework.Behavior`1<Windows.UI.Xaml.Controls.TextBox>");
			xamlMember = new XamlMember(this, "AssociatedObject", "Windows.UI.Xaml.DependencyObject");
			xamlMember.Getter = get_119_Behavior_AssociatedObject;
			xamlMember.Setter = set_119_Behavior_AssociatedObject;
			break;
		case "Vine.Framework.Behavior`1<Windows.UI.Xaml.Controls.TextBox>.Object":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Framework.Behavior`1<Windows.UI.Xaml.Controls.TextBox>");
			xamlMember = new XamlMember(this, "Object", "Windows.UI.Xaml.Controls.TextBox");
			xamlMember.Getter = get_120_Behavior_Object;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.ShareCaptureView.MessageHeader":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "MessageHeader", "String");
			xamlMember.Getter = get_121_ShareCaptureView_MessageHeader;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.ShareCaptureView.OkLabel":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "OkLabel", "String");
			xamlMember.Getter = get_122_ShareCaptureView_OkLabel;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.ShareCaptureView.AddTagTitle":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "AddTagTitle", "String");
			xamlMember.Getter = get_123_ShareCaptureView_AddTagTitle;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.ShareCaptureView.IsMention":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "IsMention", "Boolean");
			xamlMember.Getter = get_124_ShareCaptureView_IsMention;
			xamlMember.Setter = set_124_ShareCaptureView_IsMention;
			break;
		case "Vine.Views.Capture.ShareCaptureView.TutorialHintVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "TutorialHintVisibility", "Boolean");
			xamlMember.Getter = get_125_ShareCaptureView_TutorialHintVisibility;
			xamlMember.Setter = set_125_ShareCaptureView_TutorialHintVisibility;
			break;
		case "Vine.Views.Capture.ShareCaptureView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_126_ShareCaptureView_IsBusy;
			xamlMember.Setter = set_126_ShareCaptureView_IsBusy;
			break;
		case "Vine.Views.Capture.ShareCaptureView.TextInput":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "TextInput", "String");
			xamlMember.Getter = get_127_ShareCaptureView_TextInput;
			xamlMember.Setter = set_127_ShareCaptureView_TextInput;
			break;
		case "Vine.Views.Capture.ShareCaptureView.CharsLeft":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "CharsLeft", "String");
			xamlMember.Getter = get_128_ShareCaptureView_CharsLeft;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.ShareCaptureView.IsTwitterOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "IsTwitterOn", "Boolean");
			xamlMember.Getter = get_129_ShareCaptureView_IsTwitterOn;
			xamlMember.Setter = set_129_ShareCaptureView_IsTwitterOn;
			break;
		case "Vine.Views.Capture.ShareCaptureView.IsFacebookOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "IsFacebookOn", "Boolean");
			xamlMember.Getter = get_130_ShareCaptureView_IsFacebookOn;
			xamlMember.Setter = set_130_ShareCaptureView_IsFacebookOn;
			break;
		case "Vine.Views.Capture.ShareCaptureView.HeaderBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "HeaderBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_131_ShareCaptureView_HeaderBrush;
			xamlMember.Setter = set_131_ShareCaptureView_HeaderBrush;
			break;
		case "Vine.Views.Capture.ShareCaptureView.IsVineOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "IsVineOn", "Boolean");
			xamlMember.Getter = get_132_ShareCaptureView_IsVineOn;
			xamlMember.Setter = set_132_ShareCaptureView_IsVineOn;
			break;
		case "Vine.Views.Capture.ShareCaptureView.Channel":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "Channel", "Vine.Models.ChannelModel");
			xamlMember.Getter = get_133_ShareCaptureView_Channel;
			xamlMember.Setter = set_133_ShareCaptureView_Channel;
			break;
		case "Vine.Views.Capture.ShareCaptureView.ChannelStatus":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "ChannelStatus", "String");
			xamlMember.Getter = get_134_ShareCaptureView_ChannelStatus;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.ShareCaptureView.IsCommenting":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "IsCommenting", "Boolean");
			xamlMember.Getter = get_135_ShareCaptureView_IsCommenting;
			xamlMember.Setter = set_135_ShareCaptureView_IsCommenting;
			break;
		case "Vine.Views.Capture.ShareCaptureView.TagBarVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "TagBarVisibility", "Windows.UI.Xaml.Visibility");
			xamlMember.Getter = get_136_ShareCaptureView_TagBarVisibility;
			xamlMember.Setter = set_136_ShareCaptureView_TagBarVisibility;
			break;
		case "Vine.Views.Capture.ShareCaptureView.AutoCompleteList":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "AutoCompleteList", "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.Entity>");
			xamlMember.Getter = get_137_ShareCaptureView_AutoCompleteList;
			xamlMember.Setter = set_137_ShareCaptureView_AutoCompleteList;
			break;
		case "Vine.Models.Entity.Type":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.Entity");
			xamlMember = new XamlMember(this, "Type", "String");
			xamlMember.Getter = get_138_Entity_Type;
			xamlMember.Setter = set_138_Entity_Type;
			break;
		case "Vine.Models.Entity.EntityType":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.Entity");
			xamlMember = new XamlMember(this, "EntityType", "Vine.Models.EntityType");
			xamlMember.Getter = get_139_Entity_EntityType;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.Entity.Id":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.Entity");
			xamlMember = new XamlMember(this, "Id", "String");
			xamlMember.Getter = get_140_Entity_Id;
			xamlMember.Setter = set_140_Entity_Id;
			break;
		case "Vine.Models.Entity.Title":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.Entity");
			xamlMember = new XamlMember(this, "Title", "String");
			xamlMember.Getter = get_141_Entity_Title;
			xamlMember.Setter = set_141_Entity_Title;
			break;
		case "Vine.Models.Entity.Link":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.Entity");
			xamlMember = new XamlMember(this, "Link", "String");
			xamlMember.Getter = get_142_Entity_Link;
			xamlMember.Setter = set_142_Entity_Link;
			break;
		case "Vine.Models.Entity.Range":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.Entity");
			xamlMember = new XamlMember(this, "Range", "Int32[]");
			xamlMember.Getter = get_143_Entity_Range;
			xamlMember.Setter = set_143_Entity_Range;
			break;
		case "Vine.Models.Entity.Text":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.Entity");
			xamlMember = new XamlMember(this, "Text", "String");
			xamlMember.Getter = get_144_Entity_Text;
			xamlMember.Setter = set_144_Entity_Text;
			break;
		case "Vine.Models.Entity.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.Entity");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_145_Entity_User;
			xamlMember.Setter = set_145_Entity_User;
			break;
		case "Vine.Views.Capture.ShareCaptureView.IsAutoCompleteListOpen":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.ShareCaptureView");
			xamlMember = new XamlMember(this, "IsAutoCompleteListOpen", "Boolean");
			xamlMember.Getter = get_146_ShareCaptureView_IsAutoCompleteListOpen;
			xamlMember.Setter = set_146_ShareCaptureView_IsAutoCompleteListOpen;
			break;
		case "Vine.Views.MusicInformationControl.MusicTrack":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.MusicInformationControl");
			xamlMember = new XamlMember(this, "MusicTrack", "String");
			xamlMember.Getter = get_147_MusicInformationControl_MusicTrack;
			xamlMember.Setter = set_147_MusicInformationControl_MusicTrack;
			break;
		case "Vine.Views.MusicInformationControl.MusicArtist":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.MusicInformationControl");
			xamlMember = new XamlMember(this, "MusicArtist", "String");
			xamlMember.Getter = get_148_MusicInformationControl_MusicArtist;
			xamlMember.Setter = set_148_MusicInformationControl_MusicArtist;
			break;
		case "Vine.Views.VineListControl.Section":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "Section", "Vine.Models.Analytics.Section");
			xamlMember.Getter = get_149_VineListControl_Section;
			xamlMember.Setter = set_149_VineListControl_Section;
			break;
		case "Vine.Views.VineListControl.SecondaryBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "SecondaryBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_150_VineListControl_SecondaryBrush;
			xamlMember.Setter = set_150_VineListControl_SecondaryBrush;
			break;
		case "Vine.Views.VineListControl.MosaicThumbnailMargin":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "MosaicThumbnailMargin", "Windows.UI.Xaml.Thickness");
			xamlMember.Getter = get_151_VineListControl_MosaicThumbnailMargin;
			xamlMember.Setter = set_151_VineListControl_MosaicThumbnailMargin;
			break;
		case "Vine.Views.VineListControl.PullToRefreshMargin":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "PullToRefreshMargin", "Windows.UI.Xaml.Thickness");
			xamlMember.Setter = set_152_VineListControl_PullToRefreshMargin;
			break;
		case "Vine.Views.VineListControl.ListViewPadding":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "ListViewPadding", "Windows.UI.Xaml.Thickness");
			xamlMember.Setter = set_153_VineListControl_ListViewPadding;
			break;
		case "Vine.Views.VineListControl.Header":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "Header", "Object");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_154_VineListControl_Header;
			xamlMember.Setter = set_154_VineListControl_Header;
			break;
		case "Vine.Views.VineListControl.MusicControl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "MusicControl", "Vine.Views.MusicInformationControl");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_155_VineListControl_MusicControl;
			xamlMember.Setter = set_155_VineListControl_MusicControl;
			break;
		case "Vine.Views.VineListControl.HeaderSuccessfulTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "HeaderSuccessfulTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_156_VineListControl_HeaderSuccessfulTemplate;
			xamlMember.Setter = set_156_VineListControl_HeaderSuccessfulTemplate;
			break;
		case "Vine.Views.VineListControl.Footer":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "Footer", "Object");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_157_VineListControl_Footer;
			xamlMember.Setter = set_157_VineListControl_Footer;
			break;
		case "Vine.Views.VineListControl.FooterTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "FooterTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_158_VineListControl_FooterTemplate;
			xamlMember.Setter = set_158_VineListControl_FooterTemplate;
			break;
		case "Vine.Views.VineListControl.EmptyText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "EmptyText", "String");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_159_VineListControl_EmptyText;
			xamlMember.Setter = set_159_VineListControl_EmptyText;
			break;
		case "Vine.Views.VineListControl.IsEmpty":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "IsEmpty", "Boolean");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_160_VineListControl_IsEmpty;
			xamlMember.Setter = set_160_VineListControl_IsEmpty;
			break;
		case "Vine.Views.VineListControl.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_161_VineListControl_HasError;
			xamlMember.Setter = set_161_VineListControl_HasError;
			break;
		case "Vine.Views.VineListControl.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_162_VineListControl_ErrorText;
			xamlMember.Setter = set_162_VineListControl_ErrorText;
			break;
		case "Vine.Views.VineListControl.ShowRetry":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "ShowRetry", "Boolean");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_163_VineListControl_ShowRetry;
			xamlMember.Setter = set_163_VineListControl_ShowRetry;
			break;
		case "Vine.Views.VineListControl.DisablePullToRefresh":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "DisablePullToRefresh", "Boolean");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_164_VineListControl_DisablePullToRefresh;
			xamlMember.Setter = set_164_VineListControl_DisablePullToRefresh;
			break;
		case "Vine.Views.VineListControl.ProfileView":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "ProfileView", "Vine.Views.ProfileControl");
			xamlMember.Getter = get_165_VineListControl_ProfileView;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineListControl.Items":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "Items", "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.VineViewModel>");
			xamlMember.Getter = get_166_VineListControl_Items;
			xamlMember.Setter = set_166_VineListControl_Items;
			break;
		case "Vine.Models.VineViewModel.LikeStatText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "LikeStatText", "String");
			xamlMember.Getter = get_167_VineViewModel_LikeStatText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.CommentStatText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "CommentStatText", "String");
			xamlMember.Getter = get_168_VineViewModel_CommentStatText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.RevineStatText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "RevineStatText", "String");
			xamlMember.Getter = get_169_VineViewModel_RevineStatText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.IsRevinedByMe":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "IsRevinedByMe", "Boolean");
			xamlMember.Getter = get_170_VineViewModel_IsRevinedByMe;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.IsRevined":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "IsRevined", "Boolean");
			xamlMember.Getter = get_171_VineViewModel_IsRevined;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.RevinedByText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "RevinedByText", "String");
			xamlMember.Getter = get_172_VineViewModel_RevinedByText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.HasSimilarVines":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "HasSimilarVines", "Boolean");
			xamlMember.Getter = get_173_VineViewModel_HasSimilarVines;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.IsMyPost":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "IsMyPost", "Boolean");
			xamlMember.Getter = get_174_VineViewModel_IsMyPost;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.RevineEnabled":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "RevineEnabled", "Boolean");
			xamlMember.Getter = get_175_VineViewModel_RevineEnabled;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.IsPlaying":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "IsPlaying", "Boolean");
			xamlMember.Getter = get_176_VineViewModel_IsPlaying;
			xamlMember.Setter = set_176_VineViewModel_IsPlaying;
			break;
		case "Vine.Models.VineViewModel.IsFinishedBuffering":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "IsFinishedBuffering", "Boolean");
			xamlMember.Getter = get_177_VineViewModel_IsFinishedBuffering;
			xamlMember.Setter = set_177_VineViewModel_IsFinishedBuffering;
			break;
		case "Vine.Models.VineViewModel.IsLoadingVideo":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "IsLoadingVideo", "Boolean");
			xamlMember.Getter = get_178_VineViewModel_IsLoadingVideo;
			xamlMember.Setter = set_178_VineViewModel_IsLoadingVideo;
			break;
		case "Vine.Models.VineViewModel.PlayingVideoUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "PlayingVideoUrl", "System.Uri");
			xamlMember.Getter = get_179_VineViewModel_PlayingVideoUrl;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.ThumbnailUrlAuth":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "ThumbnailUrlAuth", "String");
			xamlMember.Getter = get_180_VineViewModel_ThumbnailUrlAuth;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.ThumbVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "ThumbVisibility", "Windows.UI.Xaml.Visibility");
			xamlMember.Getter = get_181_VineViewModel_ThumbVisibility;
			xamlMember.Setter = set_181_VineViewModel_ThumbVisibility;
			break;
		case "Vine.Models.VineViewModel.LikeButtonState":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "LikeButtonState", "Vine.Views.VineToggleButtonState");
			xamlMember.Getter = get_182_VineViewModel_LikeButtonState;
			xamlMember.Setter = set_182_VineViewModel_LikeButtonState;
			break;
		case "Vine.Models.VineViewModel.RevineButtonState":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "RevineButtonState", "Vine.Views.VineToggleButtonState");
			xamlMember.Getter = get_183_VineViewModel_RevineButtonState;
			xamlMember.Setter = set_183_VineViewModel_RevineButtonState;
			break;
		case "Vine.Models.VineViewModel.LoopText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "LoopText", "String");
			xamlMember.Getter = get_184_VineViewModel_LoopText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.LoopLabelText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "LoopLabelText", "String");
			xamlMember.Getter = get_185_VineViewModel_LoopLabelText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.CreatedText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "CreatedText", "String");
			xamlMember.Getter = get_186_VineViewModel_CreatedText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.LocationVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "LocationVisibility", "Windows.UI.Xaml.Visibility");
			xamlMember.Getter = get_187_VineViewModel_LocationVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.RichBody":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "RichBody", "Windows.UI.Xaml.FrameworkElement");
			xamlMember.Getter = get_188_VineViewModel_RichBody;
			xamlMember.Setter = set_188_VineViewModel_RichBody;
			break;
		case "Vine.Models.VineViewModel.Model":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "Model", "Vine.Models.VineModel");
			xamlMember.Getter = get_189_VineViewModel_Model;
			xamlMember.Setter = set_189_VineViewModel_Model;
			break;
		case "Vine.Models.VineViewModel.SecondaryBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "SecondaryBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_190_VineViewModel_SecondaryBrush;
			xamlMember.Setter = set_190_VineViewModel_SecondaryBrush;
			break;
		case "Vine.Models.VineViewModel.PendingLoopCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "PendingLoopCount", "Int32");
			xamlMember.Getter = get_191_VineViewModel_PendingLoopCount;
			xamlMember.Setter = set_191_VineViewModel_PendingLoopCount;
			break;
		case "Vine.Models.VineViewModel.LoopsWatchedCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "LoopsWatchedCount", "Int32");
			xamlMember.Getter = get_192_VineViewModel_LoopsWatchedCount;
			xamlMember.Setter = set_192_VineViewModel_LoopsWatchedCount;
			break;
		case "Vine.Models.VineViewModel.DisplayLoops":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "DisplayLoops", "Int64");
			xamlMember.Getter = get_193_VineViewModel_DisplayLoops;
			xamlMember.Setter = set_193_VineViewModel_DisplayLoops;
			break;
		case "Vine.Models.VineViewModel.LastLoopFinishTime":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "LastLoopFinishTime", "System.DateTime");
			xamlMember.Getter = get_194_VineViewModel_LastLoopFinishTime;
			xamlMember.Setter = set_194_VineViewModel_LastLoopFinishTime;
			break;
		case "Vine.Models.VineViewModel.FirstLoopStartTime":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "FirstLoopStartTime", "System.DateTime");
			xamlMember.Getter = get_195_VineViewModel_FirstLoopStartTime;
			xamlMember.Setter = set_195_VineViewModel_FirstLoopStartTime;
			break;
		case "Vine.Models.VineViewModel.IsDownloaded":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "IsDownloaded", "Boolean");
			xamlMember.Getter = get_196_VineViewModel_IsDownloaded;
			xamlMember.Setter = set_196_VineViewModel_IsDownloaded;
			break;
		case "Vine.Models.VineViewModel.Section":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "Section", "Vine.Models.Analytics.Section");
			xamlMember.Getter = get_197_VineViewModel_Section;
			xamlMember.Setter = set_197_VineViewModel_Section;
			break;
		case "Vine.Models.VineViewModel.View":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "View", "String");
			xamlMember.Getter = get_198_VineViewModel_View;
			xamlMember.Setter = set_198_VineViewModel_View;
			break;
		case "Vine.Models.VineViewModel.TimelineApiUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "TimelineApiUrl", "String");
			xamlMember.Getter = get_199_VineViewModel_TimelineApiUrl;
			xamlMember.Setter = set_199_VineViewModel_TimelineApiUrl;
			break;
		case "Vine.Models.VineViewModel.HasMusic":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "HasMusic", "Boolean");
			xamlMember.Getter = get_200_VineViewModel_HasMusic;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineViewModel.IsSeamlessLooping":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "IsSeamlessLooping", "Boolean");
			xamlMember.Getter = get_201_VineViewModel_IsSeamlessLooping;
			xamlMember.Setter = set_201_VineViewModel_IsSeamlessLooping;
			break;
		case "Vine.Models.VineViewModel.LoopsPerClip":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "LoopsPerClip", "Int32");
			xamlMember.Getter = get_202_VineViewModel_LoopsPerClip;
			xamlMember.Setter = set_202_VineViewModel_LoopsPerClip;
			break;
		case "Vine.Models.VineViewModel.MosaicThumbnails":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineViewModel");
			xamlMember = new XamlMember(this, "MosaicThumbnails", "System.Collections.Generic.IEnumerable`1<Vine.Models.MosaicImageViewModel>");
			xamlMember.Getter = get_203_VineViewModel_MosaicThumbnails;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineListControl.IsVolumeMuted":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "IsVolumeMuted", "Boolean");
			xamlMember.Getter = get_204_VineListControl_IsVolumeMuted;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineListControl.CurrentTab":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "CurrentTab", "Vine.Views.VineListControl.Tab");
			xamlMember.Getter = get_205_VineListControl_CurrentTab;
			xamlMember.Setter = set_205_VineListControl_CurrentTab;
			break;
		case "Vine.Views.VineListControl.UserId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "UserId", "String");
			xamlMember.Getter = get_206_VineListControl_UserId;
			xamlMember.Setter = set_206_VineListControl_UserId;
			break;
		case "Vine.Views.VineListControl.PostId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "PostId", "String");
			xamlMember.Getter = get_207_VineListControl_PostId;
			xamlMember.Setter = set_207_VineListControl_PostId;
			break;
		case "Vine.Views.VineListControl.SearchTag":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "SearchTag", "String");
			xamlMember.Getter = get_208_VineListControl_SearchTag;
			xamlMember.Setter = set_208_VineListControl_SearchTag;
			break;
		case "Vine.Views.VineListControl.ListParams":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "ListParams", "Vine.Models.VineListViewParams");
			xamlMember.Getter = get_209_VineListControl_ListParams;
			xamlMember.Setter = set_209_VineListControl_ListParams;
			break;
		case "Vine.Views.VineListControl.PageStateItems":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "PageStateItems", "System.Collections.Generic.List`1<Vine.Models.VineModel>");
			xamlMember.Getter = get_210_VineListControl_PageStateItems;
			xamlMember.Setter = set_210_VineListControl_PageStateItems;
			break;
		case "Vine.Models.VineModel.UserId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "UserId", "String");
			xamlMember.Getter = get_211_VineModel_UserId;
			xamlMember.Setter = set_211_VineModel_UserId;
			break;
		case "Vine.Models.VineModel.UserName":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "UserName", "String");
			xamlMember.Getter = get_212_VineModel_UserName;
			xamlMember.Setter = set_212_VineModel_UserName;
			break;
		case "Vine.Models.VineModel.AvatarUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "AvatarUrl", "String");
			xamlMember.Getter = get_213_VineModel_AvatarUrl;
			xamlMember.Setter = set_213_VineModel_AvatarUrl;
			break;
		case "Vine.Models.VineModel.ProfileBackground":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "ProfileBackground", "String");
			xamlMember.Getter = get_214_VineModel_ProfileBackground;
			xamlMember.Setter = set_214_VineModel_ProfileBackground;
			break;
		case "Vine.Models.VineModel.PostId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "PostId", "String");
			xamlMember.Getter = get_215_VineModel_PostId;
			xamlMember.Setter = set_215_VineModel_PostId;
			break;
		case "Vine.Models.VineModel.Liked":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Liked", "Boolean");
			xamlMember.Getter = get_216_VineModel_Liked;
			xamlMember.Setter = set_216_VineModel_Liked;
			break;
		case "Vine.Models.VineModel.Private":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Private", "Boolean");
			xamlMember.Getter = get_217_VineModel_Private;
			xamlMember.Setter = set_217_VineModel_Private;
			break;
		case "Vine.Models.VineModel.MyRepostId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "MyRepostId", "String");
			xamlMember.Getter = get_218_VineModel_MyRepostId;
			xamlMember.Setter = set_218_VineModel_MyRepostId;
			break;
		case "Vine.Models.VineModel.ThumbnailUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "ThumbnailUrl", "String");
			xamlMember.Getter = get_219_VineModel_ThumbnailUrl;
			xamlMember.Setter = set_219_VineModel_ThumbnailUrl;
			break;
		case "Vine.Models.VineModel.VideoUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "VideoUrl", "String");
			xamlMember.Getter = get_220_VineModel_VideoUrl;
			xamlMember.Setter = set_220_VineModel_VideoUrl;
			break;
		case "Vine.Models.VineModel.VideoLowUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "VideoLowUrl", "String");
			xamlMember.Getter = get_221_VineModel_VideoLowUrl;
			xamlMember.Setter = set_221_VineModel_VideoLowUrl;
			break;
		case "Vine.Models.VineModel.ShareUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "ShareUrl", "String");
			xamlMember.Getter = get_222_VineModel_ShareUrl;
			xamlMember.Setter = set_222_VineModel_ShareUrl;
			break;
		case "Vine.Models.VineModel.Description":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Description", "String");
			xamlMember.Getter = get_223_VineModel_Description;
			xamlMember.Setter = set_223_VineModel_Description;
			break;
		case "Vine.Models.VineModel.PermalinkUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "PermalinkUrl", "String");
			xamlMember.Getter = get_224_VineModel_PermalinkUrl;
			xamlMember.Setter = set_224_VineModel_PermalinkUrl;
			break;
		case "Vine.Models.VineModel.VenueName":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "VenueName", "String");
			xamlMember.Getter = get_225_VineModel_VenueName;
			xamlMember.Setter = set_225_VineModel_VenueName;
			break;
		case "Vine.Models.VineModel.Created":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Created", "System.DateTime");
			xamlMember.Getter = get_226_VineModel_Created;
			xamlMember.Setter = set_226_VineModel_Created;
			break;
		case "Vine.Models.VineModel.Repost":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Repost", "Vine.Models.RepostModel");
			xamlMember.Getter = get_227_VineModel_Repost;
			xamlMember.Setter = set_227_VineModel_Repost;
			break;
		case "Vine.Models.VineModel.Likes":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Likes", "Vine.Models.VineStatModel");
			xamlMember.Getter = get_228_VineModel_Likes;
			xamlMember.Setter = set_228_VineModel_Likes;
			break;
		case "Vine.Models.VineModel.Reposts":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Reposts", "Vine.Models.VineStatModel");
			xamlMember.Getter = get_229_VineModel_Reposts;
			xamlMember.Setter = set_229_VineModel_Reposts;
			break;
		case "Vine.Models.VineModel.Comments":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Comments", "Vine.Models.VineStatModel");
			xamlMember.Getter = get_230_VineModel_Comments;
			xamlMember.Setter = set_230_VineModel_Comments;
			break;
		case "Vine.Models.VineModel.Loops":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Loops", "Vine.Models.VineLoopModel");
			xamlMember.Getter = get_231_VineModel_Loops;
			xamlMember.Setter = set_231_VineModel_Loops;
			break;
		case "Vine.Models.VineModel.HasSimilarPosts":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "HasSimilarPosts", "Boolean");
			xamlMember.Getter = get_232_VineModel_HasSimilarPosts;
			xamlMember.Setter = set_232_VineModel_HasSimilarPosts;
			break;
		case "Vine.Models.VineModel.AudioTracks":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "AudioTracks", "System.Collections.Generic.List`1<Vine.Models.AudioTracks>");
			xamlMember.Getter = get_233_VineModel_AudioTracks;
			xamlMember.Setter = set_233_VineModel_AudioTracks;
			break;
		case "Vine.Models.AudioTracks.Track":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.AudioTracks");
			xamlMember = new XamlMember(this, "Track", "Vine.Models.Track");
			xamlMember.Getter = get_234_AudioTracks_Track;
			xamlMember.Setter = set_234_AudioTracks_Track;
			break;
		case "Vine.Models.VineModel.Entities":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Entities", "System.Collections.Generic.List`1<Vine.Models.Entity>");
			xamlMember.Getter = get_235_VineModel_Entities;
			xamlMember.Setter = set_235_VineModel_Entities;
			break;
		case "Vine.Models.VineModel.VineUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "VineUrl", "String");
			xamlMember.Getter = get_236_VineModel_VineUrl;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineModel.Reference":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Reference", "String");
			xamlMember.Getter = get_237_VineModel_Reference;
			xamlMember.Setter = set_237_VineModel_Reference;
			break;
		case "Vine.Models.VineModel.MosaicType":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "MosaicType", "String");
			xamlMember.Getter = get_238_VineModel_MosaicType;
			xamlMember.Setter = set_238_VineModel_MosaicType;
			break;
		case "Vine.Models.VineModel.Title":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Title", "String");
			xamlMember.Getter = get_239_VineModel_Title;
			xamlMember.Setter = set_239_VineModel_Title;
			break;
		case "Vine.Models.VineModel.Records":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Records", "System.Collections.Generic.List`1<Vine.Models.VineModel>");
			xamlMember.Getter = get_240_VineModel_Records;
			xamlMember.Setter = set_240_VineModel_Records;
			break;
		case "Vine.Models.VineModel.Link":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Link", "String");
			xamlMember.Getter = get_241_VineModel_Link;
			xamlMember.Setter = set_241_VineModel_Link;
			break;
		case "Vine.Models.VineModel.LinkPath":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "LinkPath", "String");
			xamlMember.Getter = get_242_VineModel_LinkPath;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineModel.Type":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Type", "String");
			xamlMember.Getter = get_243_VineModel_Type;
			xamlMember.Setter = set_243_VineModel_Type;
			break;
		case "Vine.Models.VineModel.Page":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Page", "Int32");
			xamlMember.Getter = get_244_VineModel_Page;
			xamlMember.Setter = set_244_VineModel_Page;
			break;
		case "Vine.Models.VineModel.Size":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "Size", "Int32");
			xamlMember.Getter = get_245_VineModel_Size;
			xamlMember.Setter = set_245_VineModel_Size;
			break;
		case "Vine.Models.VineModel.RecordType":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "RecordType", "Vine.Models.RecordType");
			xamlMember.Getter = get_246_VineModel_RecordType;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineModel.ParsedMosaicType":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineModel");
			xamlMember = new XamlMember(this, "ParsedMosaicType", "Vine.Models.MosaicType");
			xamlMember.Getter = get_247_VineModel_ParsedMosaicType;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineListControl.PageStateScrollOffset":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "PageStateScrollOffset", "Double");
			xamlMember.Getter = get_248_VineListControl_PageStateScrollOffset;
			xamlMember.Setter = set_248_VineListControl_PageStateScrollOffset;
			break;
		case "Vine.Views.VineListControl.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_249_VineListControl_IsBusy;
			xamlMember.Setter = set_249_VineListControl_IsBusy;
			break;
		case "Vine.Views.VineListControl.Active":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "Active", "Boolean");
			xamlMember.Getter = get_250_VineListControl_Active;
			xamlMember.Setter = set_250_VineListControl_Active;
			break;
		case "Vine.Views.VineListControl.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_251_VineListControl_IsFinishedLoading;
			xamlMember.Setter = set_251_VineListControl_IsFinishedLoading;
			break;
		case "Vine.Views.VineListControl.ScrollOffset":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "ScrollOffset", "Double");
			xamlMember.Getter = get_252_VineListControl_ScrollOffset;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineListControl.ScrollingDirection":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineListControl");
			xamlMember = new XamlMember(this, "ScrollingDirection", "Windows.UI.Xaml.Controls.PanelScrollingDirection");
			xamlMember.Getter = get_253_VineListControl_ScrollingDirection;
			xamlMember.Setter = set_253_VineListControl_ScrollingDirection;
			break;
		case "Vine.Views.ChannelVineListView.FeaturedPivotHeaderBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ChannelVineListView");
			xamlMember = new XamlMember(this, "FeaturedPivotHeaderBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_254_ChannelVineListView_FeaturedPivotHeaderBrush;
			xamlMember.Setter = set_254_ChannelVineListView_FeaturedPivotHeaderBrush;
			break;
		case "Vine.Views.ChannelVineListView.RecentPivotHeaderBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ChannelVineListView");
			xamlMember = new XamlMember(this, "RecentPivotHeaderBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_255_ChannelVineListView_RecentPivotHeaderBrush;
			xamlMember.Setter = set_255_ChannelVineListView_RecentPivotHeaderBrush;
			break;
		case "Vine.Views.ChannelVineListView.ChannelBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ChannelVineListView");
			xamlMember = new XamlMember(this, "ChannelBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_256_ChannelVineListView_ChannelBrush;
			xamlMember.Setter = set_256_ChannelVineListView_ChannelBrush;
			break;
		case "Vine.Views.ChannelVineListView.PageTitle":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ChannelVineListView");
			xamlMember = new XamlMember(this, "PageTitle", "String");
			xamlMember.Getter = get_257_ChannelVineListView_PageTitle;
			xamlMember.Setter = set_257_ChannelVineListView_PageTitle;
			break;
		case "Vine.Views.ChannelVineListView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ChannelVineListView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_258_ChannelVineListView_IsBusy;
			xamlMember.Setter = set_258_ChannelVineListView_IsBusy;
			break;
		case "Vine.Views.ChannelVineListView.FeaturedBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ChannelVineListView");
			xamlMember = new XamlMember(this, "FeaturedBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_259_ChannelVineListView_FeaturedBrush;
			xamlMember.Setter = set_259_ChannelVineListView_FeaturedBrush;
			break;
		case "Vine.Views.ChannelVineListView.RecentBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ChannelVineListView");
			xamlMember = new XamlMember(this, "RecentBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_260_ChannelVineListView_RecentBrush;
			xamlMember.Setter = set_260_ChannelVineListView_RecentBrush;
			break;
		case "Vine.Views.ChannelVineListView.Model":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ChannelVineListView");
			xamlMember = new XamlMember(this, "Model", "Vine.Models.VineListViewParams");
			xamlMember.Getter = get_261_ChannelVineListView_Model;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ChannelVineListView.MuteIcon":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ChannelVineListView");
			xamlMember = new XamlMember(this, "MuteIcon", "Windows.UI.Xaml.Controls.IconElement");
			xamlMember.Getter = get_262_ChannelVineListView_MuteIcon;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ChannelVineListView.MuteLabel":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ChannelVineListView");
			xamlMember = new XamlMember(this, "MuteLabel", "String");
			xamlMember.Getter = get_263_ChannelVineListView_MuteLabel;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.CommentsView.KeyboardHeight":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "KeyboardHeight", "Double");
			xamlMember.Getter = get_264_CommentsView_KeyboardHeight;
			xamlMember.Setter = set_264_CommentsView_KeyboardHeight;
			break;
		case "Vine.Views.CommentsView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_265_CommentsView_IsBusy;
			xamlMember.Setter = set_265_CommentsView_IsBusy;
			break;
		case "Vine.Views.CommentsView.IsBusyPosting":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "IsBusyPosting", "Boolean");
			xamlMember.Getter = get_266_CommentsView_IsBusyPosting;
			xamlMember.Setter = set_266_CommentsView_IsBusyPosting;
			break;
		case "Vine.Views.CommentsView.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.Getter = get_267_CommentsView_HasError;
			xamlMember.Setter = set_267_CommentsView_HasError;
			break;
		case "Vine.Views.CommentsView.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.Getter = get_268_CommentsView_ErrorText;
			xamlMember.Setter = set_268_CommentsView_ErrorText;
			break;
		case "Vine.Views.CommentsView.ShowRetry":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "ShowRetry", "Boolean");
			xamlMember.Getter = get_269_CommentsView_ShowRetry;
			xamlMember.Setter = set_269_CommentsView_ShowRetry;
			break;
		case "Vine.Views.CommentsView.IsEmpty":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "IsEmpty", "Boolean");
			xamlMember.Getter = get_270_CommentsView_IsEmpty;
			xamlMember.Setter = set_270_CommentsView_IsEmpty;
			break;
		case "Vine.Views.CommentsView.SendEnabled":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "SendEnabled", "Boolean");
			xamlMember.Getter = get_271_CommentsView_SendEnabled;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.CommentsView.IsFocusedByDefault":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "IsFocusedByDefault", "Boolean");
			xamlMember.Getter = get_272_CommentsView_IsFocusedByDefault;
			xamlMember.Setter = set_272_CommentsView_IsFocusedByDefault;
			break;
		case "Vine.Views.CommentsView.TextInput":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "TextInput", "String");
			xamlMember.Getter = get_273_CommentsView_TextInput;
			xamlMember.Setter = set_273_CommentsView_TextInput;
			break;
		case "Vine.Views.CommentsView.CharsLeft":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "CharsLeft", "String");
			xamlMember.Getter = get_274_CommentsView_CharsLeft;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.CommentsView.ScrollOffset":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "ScrollOffset", "Double");
			xamlMember.Getter = get_275_CommentsView_ScrollOffset;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.CommentsView.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_276_CommentsView_IsFinishedLoading;
			xamlMember.Setter = set_276_CommentsView_IsFinishedLoading;
			break;
		case "Vine.Views.CommentsView.Items":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "Items", "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.CommentModel>");
			xamlMember.Getter = get_277_CommentsView_Items;
			xamlMember.Setter = set_277_CommentsView_Items;
			break;
		case "Vine.Models.CommentModel.IsUserComment":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.CommentModel");
			xamlMember = new XamlMember(this, "IsUserComment", "Boolean");
			xamlMember.Getter = get_278_CommentModel_IsUserComment;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.CommentModel.Comment":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.CommentModel");
			xamlMember = new XamlMember(this, "Comment", "String");
			xamlMember.Getter = get_279_CommentModel_Comment;
			xamlMember.Setter = set_279_CommentModel_Comment;
			break;
		case "Vine.Models.CommentModel.Entities":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.CommentModel");
			xamlMember = new XamlMember(this, "Entities", "System.Collections.Generic.List`1<Vine.Models.Entity>");
			xamlMember.Getter = get_280_CommentModel_Entities;
			xamlMember.Setter = set_280_CommentModel_Entities;
			break;
		case "Vine.Models.CommentModel.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.CommentModel");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_281_CommentModel_User;
			xamlMember.Setter = set_281_CommentModel_User;
			break;
		case "Vine.Models.CommentModel.PostId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.CommentModel");
			xamlMember = new XamlMember(this, "PostId", "String");
			xamlMember.Getter = get_282_CommentModel_PostId;
			xamlMember.Setter = set_282_CommentModel_PostId;
			break;
		case "Vine.Models.CommentModel.CommentId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.CommentModel");
			xamlMember = new XamlMember(this, "CommentId", "String");
			xamlMember.Getter = get_283_CommentModel_CommentId;
			xamlMember.Setter = set_283_CommentModel_CommentId;
			break;
		case "Vine.Models.CommentModel.Created":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.CommentModel");
			xamlMember = new XamlMember(this, "Created", "System.DateTime");
			xamlMember.Getter = get_284_CommentModel_Created;
			xamlMember.Setter = set_284_CommentModel_Created;
			break;
		case "Vine.Models.CommentModel.CreatedText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.CommentModel");
			xamlMember = new XamlMember(this, "CreatedText", "String");
			xamlMember.Getter = get_285_CommentModel_CreatedText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.CommentModel.RichBody":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.CommentModel");
			xamlMember = new XamlMember(this, "RichBody", "Windows.UI.Xaml.FrameworkElement");
			xamlMember.Getter = get_286_CommentModel_RichBody;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.CommentsView.PostId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "PostId", "String");
			xamlMember.Getter = get_287_CommentsView_PostId;
			xamlMember.Setter = set_287_CommentsView_PostId;
			break;
		case "Vine.Views.CommentsView.Section":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "Section", "Vine.Models.Analytics.Section");
			xamlMember.Getter = get_288_CommentsView_Section;
			xamlMember.Setter = set_288_CommentsView_Section;
			break;
		case "Vine.Views.CommentsView.IsCommenting":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "IsCommenting", "Boolean");
			xamlMember.Getter = get_289_CommentsView_IsCommenting;
			xamlMember.Setter = set_289_CommentsView_IsCommenting;
			break;
		case "Vine.Views.CommentsView.TagBarVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "TagBarVisibility", "Windows.UI.Xaml.Visibility");
			xamlMember.Getter = get_290_CommentsView_TagBarVisibility;
			xamlMember.Setter = set_290_CommentsView_TagBarVisibility;
			break;
		case "Vine.Views.CommentsView.AutoCompleteList":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "AutoCompleteList", "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.Entity>");
			xamlMember.Getter = get_291_CommentsView_AutoCompleteList;
			xamlMember.Setter = set_291_CommentsView_AutoCompleteList;
			break;
		case "Vine.Views.CommentsView.IsAutoCompleteListOpen":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CommentsView");
			xamlMember = new XamlMember(this, "IsAutoCompleteListOpen", "Boolean");
			xamlMember.Getter = get_292_CommentsView_IsAutoCompleteListOpen;
			xamlMember.Setter = set_292_CommentsView_IsAutoCompleteListOpen;
			break;
		case "Vine.Views.Capture.CaptureView8.CurrentTutorialState":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "CurrentTutorialState", "Vine.Views.Capture.CaptureView8.TutorialState");
			xamlMember.Getter = get_293_CaptureView8_CurrentTutorialState;
			xamlMember.Setter = set_293_CaptureView8_CurrentTutorialState;
			break;
		case "Vine.Views.Capture.CaptureView8.TutorialHintVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "TutorialHintVisibility", "Boolean");
			xamlMember.Getter = get_294_CaptureView8_TutorialHintVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView8.TutorialWelcomeVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "TutorialWelcomeVisibility", "Boolean");
			xamlMember.Getter = get_295_CaptureView8_TutorialWelcomeVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView8.TutorialMessage":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "TutorialMessage", "String");
			xamlMember.Getter = get_296_CaptureView8_TutorialMessage;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView8.ButtonTutorialCameraToolsVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "ButtonTutorialCameraToolsVisibility", "Boolean");
			xamlMember.Getter = get_297_CaptureView8_ButtonTutorialCameraToolsVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView8.ButtonTutorialUndoVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "ButtonTutorialUndoVisibility", "Boolean");
			xamlMember.Getter = get_298_CaptureView8_ButtonTutorialUndoVisibility;
			xamlMember.Setter = set_298_CaptureView8_ButtonTutorialUndoVisibility;
			break;
		case "Vine.Views.Capture.CaptureView8.ButtonTutorialGrabVideoVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "ButtonTutorialGrabVideoVisibility", "Boolean");
			xamlMember.Getter = get_299_CaptureView8_ButtonTutorialGrabVideoVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView8.ButtonTutorialState":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "ButtonTutorialState", "Vine.Views.Capture.CaptureView8.ButtonsTutorialEnum");
			xamlMember.Getter = get_300_CaptureView8_ButtonTutorialState;
			xamlMember.Setter = set_300_CaptureView8_ButtonTutorialState;
			break;
		case "Vine.Views.Capture.CaptureView8.NextButtonVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "NextButtonVisibility", "Windows.UI.Xaml.Visibility");
			xamlMember.Getter = get_301_CaptureView8_NextButtonVisibility;
			xamlMember.Setter = set_301_CaptureView8_NextButtonVisibility;
			break;
		case "Vine.Views.Capture.CaptureView8.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_302_CaptureView8_IsBusy;
			xamlMember.Setter = set_302_CaptureView8_IsBusy;
			break;
		case "Vine.Views.Capture.CaptureView8.IsTorchSupported":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "IsTorchSupported", "Boolean");
			xamlMember.Getter = get_303_CaptureView8_IsTorchSupported;
			xamlMember.Setter = set_303_CaptureView8_IsTorchSupported;
			break;
		case "Vine.Views.Capture.CaptureView8.IsFrontCameraSupported":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "IsFrontCameraSupported", "Boolean");
			xamlMember.Getter = get_304_CaptureView8_IsFrontCameraSupported;
			xamlMember.Setter = set_304_CaptureView8_IsFrontCameraSupported;
			break;
		case "Vine.Views.Capture.CaptureView8.IsFocusSupported":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "IsFocusSupported", "Boolean");
			xamlMember.Getter = get_305_CaptureView8_IsFocusSupported;
			xamlMember.Setter = set_305_CaptureView8_IsFocusSupported;
			break;
		case "Vine.Views.Capture.CaptureView8.IsFocusLocked":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "IsFocusLocked", "Boolean");
			xamlMember.Getter = get_306_CaptureView8_IsFocusLocked;
			xamlMember.Setter = set_306_CaptureView8_IsFocusLocked;
			break;
		case "Vine.Views.Capture.CaptureView8.IsGhostModeHighlighted":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "IsGhostModeHighlighted", "Boolean");
			xamlMember.Getter = get_307_CaptureView8_IsGhostModeHighlighted;
			xamlMember.Setter = set_307_CaptureView8_IsGhostModeHighlighted;
			break;
		case "Vine.Views.Capture.CaptureView8.IsGridHighlighted":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "IsGridHighlighted", "Boolean");
			xamlMember.Getter = get_308_CaptureView8_IsGridHighlighted;
			xamlMember.Setter = set_308_CaptureView8_IsGridHighlighted;
			break;
		case "Vine.Views.Capture.CaptureView8.IsExpanded":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "IsExpanded", "Boolean");
			xamlMember.Getter = get_309_CaptureView8_IsExpanded;
			xamlMember.Setter = set_309_CaptureView8_IsExpanded;
			break;
		case "Vine.Views.Capture.CaptureView8.IsGridVisible":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "IsGridVisible", "Boolean");
			xamlMember.Getter = get_310_CaptureView8_IsGridVisible;
			xamlMember.Setter = set_310_CaptureView8_IsGridVisible;
			break;
		case "Vine.Views.Capture.CaptureView8.IsUndoHighlighted":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "IsUndoHighlighted", "Boolean");
			xamlMember.Getter = get_311_CaptureView8_IsUndoHighlighted;
			xamlMember.Setter = set_311_CaptureView8_IsUndoHighlighted;
			break;
		case "Vine.Views.Capture.CaptureView8.IsCameraHighlighted":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "IsCameraHighlighted", "Boolean");
			xamlMember.Getter = get_312_CaptureView8_IsCameraHighlighted;
			xamlMember.Setter = set_312_CaptureView8_IsCameraHighlighted;
			break;
		case "Vine.Views.Capture.CaptureView8.IsTorchHighlighted":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "IsTorchHighlighted", "Boolean");
			xamlMember.Getter = get_313_CaptureView8_IsTorchHighlighted;
			xamlMember.Setter = set_313_CaptureView8_IsTorchHighlighted;
			break;
		case "Vine.Views.Capture.CaptureView8.IsFocusModeHighlighted":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "IsFocusModeHighlighted", "Boolean");
			xamlMember.Getter = get_314_CaptureView8_IsFocusModeHighlighted;
			xamlMember.Setter = set_314_CaptureView8_IsFocusModeHighlighted;
			break;
		case "Vine.Views.Capture.CaptureView8.PendingChanges":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "PendingChanges", "Boolean");
			xamlMember.Getter = get_315_CaptureView8_PendingChanges;
			xamlMember.Setter = set_315_CaptureView8_PendingChanges;
			break;
		case "Vine.Views.Capture.CaptureView8.RecordingDraftCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "RecordingDraftCount", "Int32");
			xamlMember.Getter = get_316_CaptureView8_RecordingDraftCount;
			xamlMember.Setter = set_316_CaptureView8_RecordingDraftCount;
			break;
		case "Vine.Views.Capture.CaptureView8.DraftNumber":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "DraftNumber", "String");
			xamlMember.Getter = get_317_CaptureView8_DraftNumber;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView8.IsDraftsEnabled":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "IsDraftsEnabled", "Boolean");
			xamlMember.Getter = get_318_CaptureView8_IsDraftsEnabled;
			xamlMember.Setter = set_318_CaptureView8_IsDraftsEnabled;
			break;
		case "Vine.Views.Capture.CaptureView8.IsUndoEnabled":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "IsUndoEnabled", "Boolean");
			xamlMember.Getter = get_319_CaptureView8_IsUndoEnabled;
			xamlMember.Setter = set_319_CaptureView8_IsUndoEnabled;
			break;
		case "Vine.Views.Capture.CaptureView8.GhostImageSource":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "GhostImageSource", "Windows.UI.Xaml.Media.ImageSource");
			xamlMember.Getter = get_320_CaptureView8_GhostImageSource;
			xamlMember.Setter = set_320_CaptureView8_GhostImageSource;
			break;
		case "Vine.Views.Capture.CaptureView8.FocusButtonBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "FocusButtonBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_321_CaptureView8_FocusButtonBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView8.GhostButtonBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "GhostButtonBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_322_CaptureView8_GhostButtonBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView8.GridButtonBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "GridButtonBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_323_CaptureView8_GridButtonBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView8.CameraButtonBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "CameraButtonBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_324_CaptureView8_CameraButtonBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView8.WrenchBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "WrenchBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_325_CaptureView8_WrenchBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView8.TorchButtonBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "TorchButtonBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_326_CaptureView8_TorchButtonBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.Capture.CaptureView8.MediaCapture":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "MediaCapture", "Windows.Media.Capture.MediaCapture");
			xamlMember.Getter = get_327_CaptureView8_MediaCapture;
			xamlMember.Setter = set_327_CaptureView8_MediaCapture;
			break;
		case "Vine.Views.Capture.CaptureView8.VMParameters":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.Capture.CaptureView8");
			xamlMember = new XamlMember(this, "VMParameters", "Vine.Models.ReplyVmParameters");
			xamlMember.Getter = get_328_CaptureView8_VMParameters;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.PullToRefreshListControl.DefaultPadding":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.PullToRefreshListControl");
			xamlMember = new XamlMember(this, "DefaultPadding", "Windows.UI.Xaml.Thickness");
			xamlMember.Getter = get_329_PullToRefreshListControl_DefaultPadding;
			xamlMember.Setter = set_329_PullToRefreshListControl_DefaultPadding;
			break;
		case "Vine.Views.PullToRefreshListControl.ListView":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.PullToRefreshListControl");
			xamlMember = new XamlMember(this, "ListView", "Windows.UI.Xaml.Controls.ListView");
			xamlMember.Getter = get_330_PullToRefreshListControl_ListView;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.PullToRefreshListControl.ScrollViewer":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.PullToRefreshListControl");
			xamlMember = new XamlMember(this, "ScrollViewer", "Windows.UI.Xaml.Controls.ScrollViewer");
			xamlMember.Getter = get_331_PullToRefreshListControl_ScrollViewer;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ConversationList.Items":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ConversationList");
			xamlMember = new XamlMember(this, "Items", "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.ConversationViewModel>");
			xamlMember.Getter = get_332_ConversationList_Items;
			xamlMember.Setter = set_332_ConversationList_Items;
			break;
		case "Vine.Models.ConversationViewModel.OtherUser":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.ConversationViewModel");
			xamlMember = new XamlMember(this, "OtherUser", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_333_ConversationViewModel_OtherUser;
			xamlMember.Setter = set_333_ConversationViewModel_OtherUser;
			break;
		case "Vine.Models.ConversationViewModel.Record":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.ConversationViewModel");
			xamlMember = new XamlMember(this, "Record", "Vine.Models.BaseConversationModel");
			xamlMember.Getter = get_334_ConversationViewModel_Record;
			xamlMember.Setter = set_334_ConversationViewModel_Record;
			break;
		case "Vine.Models.ConversationViewModel.DeletedMsgIds":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.ConversationViewModel");
			xamlMember = new XamlMember(this, "DeletedMsgIds", "System.Collections.Generic.List`1<String>");
			xamlMember.Getter = get_335_ConversationViewModel_DeletedMsgIds;
			xamlMember.Setter = set_335_ConversationViewModel_DeletedMsgIds;
			break;
		case "Vine.Models.ConversationViewModel.CurrentUser":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.ConversationViewModel");
			xamlMember = new XamlMember(this, "CurrentUser", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_336_ConversationViewModel_CurrentUser;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.ConversationViewModel.LastMessageDateDisplay":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.ConversationViewModel");
			xamlMember = new XamlMember(this, "LastMessageDateDisplay", "String");
			xamlMember.Getter = get_337_ConversationViewModel_LastMessageDateDisplay;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.ConversationViewModel.CurrentUserBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.ConversationViewModel");
			xamlMember = new XamlMember(this, "CurrentUserBrush", "Windows.UI.Xaml.Media.SolidColorBrush");
			xamlMember.Getter = get_338_ConversationViewModel_CurrentUserBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.ConversationViewModel.CurrentUserLightBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.ConversationViewModel");
			xamlMember = new XamlMember(this, "CurrentUserLightBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_339_ConversationViewModel_CurrentUserLightBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.ConversationViewModel.OtherUserBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.ConversationViewModel");
			xamlMember = new XamlMember(this, "OtherUserBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_340_ConversationViewModel_OtherUserBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.ConversationViewModel.OtherUserLightBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.ConversationViewModel");
			xamlMember = new XamlMember(this, "OtherUserLightBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_341_ConversationViewModel_OtherUserLightBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ConversationList.ItemWidth":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ConversationList");
			xamlMember = new XamlMember(this, "ItemWidth", "Double");
			xamlMember.Getter = get_342_ConversationList_ItemWidth;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ConversationList.IsInbox":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ConversationList");
			xamlMember = new XamlMember(this, "IsInbox", "Boolean");
			xamlMember.Getter = get_343_ConversationList_IsInbox;
			xamlMember.Setter = set_343_ConversationList_IsInbox;
			break;
		case "Vine.Views.ConversationList.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ConversationList");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_344_ConversationList_IsBusy;
			xamlMember.Setter = set_344_ConversationList_IsBusy;
			break;
		case "Vine.Views.ConversationList.IsEmpty":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ConversationList");
			xamlMember = new XamlMember(this, "IsEmpty", "Boolean");
			xamlMember.Getter = get_345_ConversationList_IsEmpty;
			xamlMember.Setter = set_345_ConversationList_IsEmpty;
			break;
		case "Vine.Views.ConversationList.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ConversationList");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.Getter = get_346_ConversationList_HasError;
			xamlMember.Setter = set_346_ConversationList_HasError;
			break;
		case "Vine.Views.ConversationList.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ConversationList");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.Getter = get_347_ConversationList_ErrorText;
			xamlMember.Setter = set_347_ConversationList_ErrorText;
			break;
		case "Vine.Views.ConversationList.ShowRetry":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ConversationList");
			xamlMember = new XamlMember(this, "ShowRetry", "Boolean");
			xamlMember.Getter = get_348_ConversationList_ShowRetry;
			xamlMember.Setter = set_348_ConversationList_ShowRetry;
			break;
		case "Vine.Views.ConversationList.HasNew":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ConversationList");
			xamlMember = new XamlMember(this, "HasNew", "Boolean");
			xamlMember.Getter = get_349_ConversationList_HasNew;
			xamlMember.Setter = set_349_ConversationList_HasNew;
			break;
		case "Vine.Views.ConversationList.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ConversationList");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_350_ConversationList_IsFinishedLoading;
			xamlMember.Setter = set_350_ConversationList_IsFinishedLoading;
			break;
		case "Vine.Views.ConversationList.EmptyIcon":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ConversationList");
			xamlMember = new XamlMember(this, "EmptyIcon", "String");
			xamlMember.Getter = get_351_ConversationList_EmptyIcon;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ConversationList.EmptyHeader":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ConversationList");
			xamlMember = new XamlMember(this, "EmptyHeader", "String");
			xamlMember.Getter = get_352_ConversationList_EmptyHeader;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ConversationList.EmptyMessage":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ConversationList");
			xamlMember = new XamlMember(this, "EmptyMessage", "String");
			xamlMember.Getter = get_353_ConversationList_EmptyMessage;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.CaptchaView.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CaptchaView");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_354_CaptchaView_IsFinishedLoading;
			xamlMember.Setter = set_354_CaptchaView_IsFinishedLoading;
			break;
		case "Vine.Views.CaptchaView.IsLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CaptchaView");
			xamlMember = new XamlMember(this, "IsLoading", "Boolean");
			xamlMember.Getter = get_355_CaptchaView_IsLoading;
			xamlMember.Setter = set_355_CaptchaView_IsLoading;
			break;
		case "Vine.Views.CaptchaView.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.CaptchaView");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.Getter = get_356_CaptchaView_HasError;
			xamlMember.Setter = set_356_CaptchaView_HasError;
			break;
		case "Vine.Views.TemplateSelectors.FriendFinderTemplateSelector.HeaderTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.FriendFinderTemplateSelector");
			xamlMember = new XamlMember(this, "HeaderTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_357_FriendFinderTemplateSelector_HeaderTemplate;
			xamlMember.Setter = set_357_FriendFinderTemplateSelector_HeaderTemplate;
			break;
		case "Vine.Views.TemplateSelectors.FriendFinderTemplateSelector.UserTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.FriendFinderTemplateSelector");
			xamlMember = new XamlMember(this, "UserTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_358_FriendFinderTemplateSelector_UserTemplate;
			xamlMember.Setter = set_358_FriendFinderTemplateSelector_UserTemplate;
			break;
		case "Vine.Views.VineToggleButton.State":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineToggleButton");
			xamlMember = new XamlMember(this, "State", "Vine.Views.VineToggleButtonState");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_359_VineToggleButton_State;
			xamlMember.Setter = set_359_VineToggleButton_State;
			break;
		case "Vine.Views.VineToggleButton.FollowingVisual":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineToggleButton");
			xamlMember = new XamlMember(this, "FollowingVisual", "Windows.UI.Xaml.FrameworkElement");
			xamlMember.Getter = get_360_VineToggleButton_FollowingVisual;
			xamlMember.Setter = set_360_VineToggleButton_FollowingVisual;
			break;
		case "Vine.Views.VineToggleButton.NotFollowingVisual":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineToggleButton");
			xamlMember = new XamlMember(this, "NotFollowingVisual", "Windows.UI.Xaml.FrameworkElement");
			xamlMember.Getter = get_361_VineToggleButton_NotFollowingVisual;
			xamlMember.Setter = set_361_VineToggleButton_NotFollowingVisual;
			break;
		case "Vine.Views.VineToggleButton.FollowRequestedVisual":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineToggleButton");
			xamlMember = new XamlMember(this, "FollowRequestedVisual", "Windows.UI.Xaml.FrameworkElement");
			xamlMember.Getter = get_362_VineToggleButton_FollowRequestedVisual;
			xamlMember.Setter = set_362_VineToggleButton_FollowRequestedVisual;
			break;
		case "Vine.Views.VineToggleButton.ActiveVisual":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineToggleButton");
			xamlMember = new XamlMember(this, "ActiveVisual", "Windows.UI.Xaml.FrameworkElement");
			xamlMember.Setter = set_363_VineToggleButton_ActiveVisual;
			break;
		case "Vine.Views.VineToggleButton.OnVisual":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineToggleButton");
			xamlMember = new XamlMember(this, "OnVisual", "Windows.UI.Xaml.FrameworkElement");
			xamlMember.Getter = get_364_VineToggleButton_OnVisual;
			xamlMember.Setter = set_364_VineToggleButton_OnVisual;
			break;
		case "Vine.Views.VineToggleButton.OffVisual":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineToggleButton");
			xamlMember = new XamlMember(this, "OffVisual", "Windows.UI.Xaml.FrameworkElement");
			xamlMember.Getter = get_365_VineToggleButton_OffVisual;
			xamlMember.Setter = set_365_VineToggleButton_OffVisual;
			break;
		case "Vine.Views.VineToggleButton.DisabledVisual":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineToggleButton");
			xamlMember = new XamlMember(this, "DisabledVisual", "Windows.UI.Xaml.FrameworkElement");
			xamlMember.Getter = get_366_VineToggleButton_DisabledVisual;
			xamlMember.Setter = set_366_VineToggleButton_DisabledVisual;
			break;
		case "Vine.Views.FriendFinderAllView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderAllView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_367_FriendFinderAllView_IsBusy;
			xamlMember.Setter = set_367_FriendFinderAllView_IsBusy;
			break;
		case "Vine.Views.FriendFinderAllView.IsNux":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderAllView");
			xamlMember = new XamlMember(this, "IsNux", "Boolean");
			xamlMember.Getter = get_368_FriendFinderAllView_IsNux;
			xamlMember.Setter = set_368_FriendFinderAllView_IsNux;
			break;
		case "Vine.Views.FriendFinderAllView.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderAllView");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.Getter = get_369_FriendFinderAllView_HasError;
			xamlMember.Setter = set_369_FriendFinderAllView_HasError;
			break;
		case "Vine.Views.FriendFinderAllView.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderAllView");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.Getter = get_370_FriendFinderAllView_ErrorText;
			xamlMember.Setter = set_370_FriendFinderAllView_ErrorText;
			break;
		case "Vine.Views.FriendFinderAllView.ShowRetry":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderAllView");
			xamlMember = new XamlMember(this, "ShowRetry", "Boolean");
			xamlMember.Getter = get_371_FriendFinderAllView_ShowRetry;
			xamlMember.Setter = set_371_FriendFinderAllView_ShowRetry;
			break;
		case "Vine.Views.FriendFinderAllView.Items":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderAllView");
			xamlMember = new XamlMember(this, "Items", "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.FriendFinderModel>");
			xamlMember.Getter = get_372_FriendFinderAllView_Items;
			xamlMember.Setter = set_372_FriendFinderAllView_Items;
			break;
		case "Vine.Models.FriendFinderModel.Source":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.FriendFinderModel");
			xamlMember = new XamlMember(this, "Source", "Vine.Models.FriendFinderListSource");
			xamlMember.Getter = get_373_FriendFinderModel_Source;
			xamlMember.Setter = set_373_FriendFinderModel_Source;
			break;
		case "Vine.Models.FriendFinderModel.HeaderText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.FriendFinderModel");
			xamlMember = new XamlMember(this, "HeaderText", "String");
			xamlMember.Getter = get_374_FriendFinderModel_HeaderText;
			xamlMember.Setter = set_374_FriendFinderModel_HeaderText;
			break;
		case "Vine.Models.FriendFinderModel.SeeAllVisible":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.FriendFinderModel");
			xamlMember = new XamlMember(this, "SeeAllVisible", "Boolean");
			xamlMember.Getter = get_375_FriendFinderModel_SeeAllVisible;
			xamlMember.Setter = set_375_FriendFinderModel_SeeAllVisible;
			break;
		case "Vine.Models.FriendFinderModel.VineUserModel":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.FriendFinderModel");
			xamlMember = new XamlMember(this, "VineUserModel", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_376_FriendFinderModel_VineUserModel;
			xamlMember.Setter = set_376_FriendFinderModel_VineUserModel;
			break;
		case "Vine.Models.FriendFinderModel.IsHeader":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.FriendFinderModel");
			xamlMember = new XamlMember(this, "IsHeader", "Boolean");
			xamlMember.Getter = get_377_FriendFinderModel_IsHeader;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.FriendFinderModel.IsUser":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.FriendFinderModel");
			xamlMember = new XamlMember(this, "IsUser", "Boolean");
			xamlMember.Getter = get_378_FriendFinderModel_IsUser;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.FriendFinderAllView.ListSource":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderAllView");
			xamlMember = new XamlMember(this, "ListSource", "Vine.Models.FriendFinderListSource");
			xamlMember.Getter = get_379_FriendFinderAllView_ListSource;
			xamlMember.Setter = set_379_FriendFinderAllView_ListSource;
			break;
		case "Vine.Views.FriendFinderAllView.HeaderText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderAllView");
			xamlMember = new XamlMember(this, "HeaderText", "String");
			xamlMember.Getter = get_380_FriendFinderAllView_HeaderText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.FriendFinderAllView.IsSuggestedVisible":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderAllView");
			xamlMember = new XamlMember(this, "IsSuggestedVisible", "Boolean");
			xamlMember.Getter = get_381_FriendFinderAllView_IsSuggestedVisible;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.FriendFinderAllView.IsScrollViewVisible":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderAllView");
			xamlMember = new XamlMember(this, "IsScrollViewVisible", "Boolean");
			xamlMember.Getter = get_382_FriendFinderAllView_IsScrollViewVisible;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.FriendFinderAllView.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderAllView");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_383_FriendFinderAllView_IsFinishedLoading;
			xamlMember.Setter = set_383_FriendFinderAllView_IsFinishedLoading;
			break;
		case "Vine.Views.FriendFinderView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_384_FriendFinderView_IsBusy;
			xamlMember.Setter = set_384_FriendFinderView_IsBusy;
			break;
		case "Vine.Views.FriendFinderView.IsNux":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderView");
			xamlMember = new XamlMember(this, "IsNux", "Boolean");
			xamlMember.Getter = get_385_FriendFinderView_IsNux;
			xamlMember.Setter = set_385_FriendFinderView_IsNux;
			break;
		case "Vine.Views.FriendFinderView.PlaceholderVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderView");
			xamlMember = new XamlMember(this, "PlaceholderVisibility", "Boolean");
			xamlMember.Getter = get_386_FriendFinderView_PlaceholderVisibility;
			xamlMember.Setter = set_386_FriendFinderView_PlaceholderVisibility;
			break;
		case "Vine.Views.FriendFinderView.TwitterEnabled":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderView");
			xamlMember = new XamlMember(this, "TwitterEnabled", "Boolean");
			xamlMember.Getter = get_387_FriendFinderView_TwitterEnabled;
			xamlMember.Setter = set_387_FriendFinderView_TwitterEnabled;
			break;
		case "Vine.Views.FriendFinderView.ContactsEnabled":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderView");
			xamlMember = new XamlMember(this, "ContactsEnabled", "Boolean");
			xamlMember.Getter = get_388_FriendFinderView_ContactsEnabled;
			xamlMember.Setter = set_388_FriendFinderView_ContactsEnabled;
			break;
		case "Vine.Views.FriendFinderView.ConnectAccountsVisble":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderView");
			xamlMember = new XamlMember(this, "ConnectAccountsVisble", "Boolean");
			xamlMember.Getter = get_389_FriendFinderView_ConnectAccountsVisble;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.FriendFinderView.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderView");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.Getter = get_390_FriendFinderView_HasError;
			xamlMember.Setter = set_390_FriendFinderView_HasError;
			break;
		case "Vine.Views.FriendFinderView.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderView");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.Getter = get_391_FriendFinderView_ErrorText;
			xamlMember.Setter = set_391_FriendFinderView_ErrorText;
			break;
		case "Vine.Views.FriendFinderView.ShowRetry":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderView");
			xamlMember = new XamlMember(this, "ShowRetry", "Boolean");
			xamlMember.Getter = get_392_FriendFinderView_ShowRetry;
			xamlMember.Setter = set_392_FriendFinderView_ShowRetry;
			break;
		case "Vine.Views.FriendFinderView.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderView");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_393_FriendFinderView_IsFinishedLoading;
			xamlMember.Setter = set_393_FriendFinderView_IsFinishedLoading;
			break;
		case "Vine.Views.FriendFinderView.Items":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FriendFinderView");
			xamlMember = new XamlMember(this, "Items", "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.FriendFinderModel>");
			xamlMember.Getter = get_394_FriendFinderView_Items;
			xamlMember.Setter = set_394_FriendFinderView_Items;
			break;
		case "Vine.Views.TemplateSelectors.SearchResultTemplateSelector.HeaderTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.SearchResultTemplateSelector");
			xamlMember = new XamlMember(this, "HeaderTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_395_SearchResultTemplateSelector_HeaderTemplate;
			xamlMember.Setter = set_395_SearchResultTemplateSelector_HeaderTemplate;
			break;
		case "Vine.Views.TemplateSelectors.SearchResultTemplateSelector.SuggestedSearchTermTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.SearchResultTemplateSelector");
			xamlMember = new XamlMember(this, "SuggestedSearchTermTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_396_SearchResultTemplateSelector_SuggestedSearchTermTemplate;
			xamlMember.Setter = set_396_SearchResultTemplateSelector_SuggestedSearchTermTemplate;
			break;
		case "Vine.Views.TemplateSelectors.SearchResultTemplateSelector.UserTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.SearchResultTemplateSelector");
			xamlMember = new XamlMember(this, "UserTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_397_SearchResultTemplateSelector_UserTemplate;
			xamlMember.Setter = set_397_SearchResultTemplateSelector_UserTemplate;
			break;
		case "Vine.Views.TemplateSelectors.SearchResultTemplateSelector.TagTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.SearchResultTemplateSelector");
			xamlMember = new XamlMember(this, "TagTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_398_SearchResultTemplateSelector_TagTemplate;
			xamlMember.Setter = set_398_SearchResultTemplateSelector_TagTemplate;
			break;
		case "Vine.Views.TemplateSelectors.SearchResultTemplateSelector.RecentTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.SearchResultTemplateSelector");
			xamlMember = new XamlMember(this, "RecentTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_399_SearchResultTemplateSelector_RecentTemplate;
			xamlMember.Setter = set_399_SearchResultTemplateSelector_RecentTemplate;
			break;
		case "Vine.Views.TemplateSelectors.SearchResultTemplateSelector.VineTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.SearchResultTemplateSelector");
			xamlMember = new XamlMember(this, "VineTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_400_SearchResultTemplateSelector_VineTemplate;
			xamlMember.Setter = set_400_SearchResultTemplateSelector_VineTemplate;
			break;
		case "Vine.Views.SearchControl.ControlWrapper":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "ControlWrapper", "Vine.Models.UserControlWrapper");
			xamlMember.Getter = get_401_SearchControl_ControlWrapper;
			xamlMember.Setter = set_401_SearchControl_ControlWrapper;
			break;
		case "Vine.Views.SearchControl.SearchResults":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "SearchResults", "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.SearchResultModel>");
			xamlMember.Getter = get_402_SearchControl_SearchResults;
			xamlMember.Setter = set_402_SearchControl_SearchResults;
			break;
		case "Vine.Models.SearchResultModel.Suggestion":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.SearchResultModel");
			xamlMember = new XamlMember(this, "Suggestion", "Vine.Models.VineSearchSuggestions");
			xamlMember.Getter = get_403_SearchResultModel_Suggestion;
			xamlMember.Setter = set_403_SearchResultModel_Suggestion;
			break;
		case "Vine.Models.SearchResultModel.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.SearchResultModel");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_404_SearchResultModel_User;
			xamlMember.Setter = set_404_SearchResultModel_User;
			break;
		case "Vine.Models.SearchResultModel.Tag":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.SearchResultModel");
			xamlMember = new XamlMember(this, "Tag", "Vine.Models.VineTagModel");
			xamlMember.Getter = get_405_SearchResultModel_Tag;
			xamlMember.Setter = set_405_SearchResultModel_Tag;
			break;
		case "Vine.Models.SearchResultModel.Recent":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.SearchResultModel");
			xamlMember = new XamlMember(this, "Recent", "Vine.Models.VineRecentSearch");
			xamlMember.Getter = get_406_SearchResultModel_Recent;
			xamlMember.Setter = set_406_SearchResultModel_Recent;
			break;
		case "Vine.Models.SearchResultModel.HeaderViewAllVisible":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.SearchResultModel");
			xamlMember = new XamlMember(this, "HeaderViewAllVisible", "Boolean");
			xamlMember.Getter = get_407_SearchResultModel_HeaderViewAllVisible;
			xamlMember.Setter = set_407_SearchResultModel_HeaderViewAllVisible;
			break;
		case "Vine.Models.SearchResultModel.HeaderText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.SearchResultModel");
			xamlMember = new XamlMember(this, "HeaderText", "String");
			xamlMember.Getter = get_408_SearchResultModel_HeaderText;
			xamlMember.Setter = set_408_SearchResultModel_HeaderText;
			break;
		case "Vine.Models.SearchResultModel.SearchType":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.SearchResultModel");
			xamlMember = new XamlMember(this, "SearchType", "Vine.Models.SearchType");
			xamlMember.Getter = get_409_SearchResultModel_SearchType;
			xamlMember.Setter = set_409_SearchResultModel_SearchType;
			break;
		case "Vine.Models.SearchResultModel.Vine":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.SearchResultModel");
			xamlMember = new XamlMember(this, "Vine", "Vine.Models.VineModel");
			xamlMember.Getter = get_410_SearchResultModel_Vine;
			xamlMember.Setter = set_410_SearchResultModel_Vine;
			break;
		case "Vine.Models.SearchResultModel.IsSearchSuggestion":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.SearchResultModel");
			xamlMember = new XamlMember(this, "IsSearchSuggestion", "Boolean");
			xamlMember.Getter = get_411_SearchResultModel_IsSearchSuggestion;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.SearchResultModel.IsHeader":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.SearchResultModel");
			xamlMember = new XamlMember(this, "IsHeader", "Boolean");
			xamlMember.Getter = get_412_SearchResultModel_IsHeader;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.SearchResultModel.IsUser":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.SearchResultModel");
			xamlMember = new XamlMember(this, "IsUser", "Boolean");
			xamlMember.Getter = get_413_SearchResultModel_IsUser;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.SearchResultModel.IsTag":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.SearchResultModel");
			xamlMember = new XamlMember(this, "IsTag", "Boolean");
			xamlMember.Getter = get_414_SearchResultModel_IsTag;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.SearchResultModel.IsVine":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.SearchResultModel");
			xamlMember = new XamlMember(this, "IsVine", "Boolean");
			xamlMember.Getter = get_415_SearchResultModel_IsVine;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.SearchResultModel.IsRecent":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.SearchResultModel");
			xamlMember = new XamlMember(this, "IsRecent", "Boolean");
			xamlMember.Getter = get_416_SearchResultModel_IsRecent;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SearchControl.Pending":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "Pending", "System.Threading.CancellationTokenSource");
			xamlMember.Getter = get_417_SearchControl_Pending;
			xamlMember.Setter = set_417_SearchControl_Pending;
			break;
		case "Vine.Views.SearchControl.RecentSearches":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "RecentSearches", "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.VineRecentSearch>");
			xamlMember.Getter = get_418_SearchControl_RecentSearches;
			xamlMember.Setter = set_418_SearchControl_RecentSearches;
			break;
		case "Vine.Models.VineRecentSearch.Query":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineRecentSearch");
			xamlMember = new XamlMember(this, "Query", "String");
			xamlMember.Getter = get_419_VineRecentSearch_Query;
			xamlMember.Setter = set_419_VineRecentSearch_Query;
			break;
		case "Vine.Views.SearchControl.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_420_SearchControl_IsBusy;
			xamlMember.Setter = set_420_SearchControl_IsBusy;
			break;
		case "Vine.Views.SearchControl.IsEmpty":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "IsEmpty", "Boolean");
			xamlMember.Getter = get_421_SearchControl_IsEmpty;
			xamlMember.Setter = set_421_SearchControl_IsEmpty;
			break;
		case "Vine.Views.SearchControl.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.Getter = get_422_SearchControl_HasError;
			xamlMember.Setter = set_422_SearchControl_HasError;
			break;
		case "Vine.Views.SearchControl.ShowError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "ShowError", "Boolean");
			xamlMember.Getter = get_423_SearchControl_ShowError;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SearchControl.ShowRetry":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "ShowRetry", "Boolean");
			xamlMember.Getter = get_424_SearchControl_ShowRetry;
			xamlMember.Setter = set_424_SearchControl_ShowRetry;
			break;
		case "Vine.Views.SearchControl.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.Getter = get_425_SearchControl_ErrorText;
			xamlMember.Setter = set_425_SearchControl_ErrorText;
			break;
		case "Vine.Views.SearchControl.SearchListVisible":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "SearchListVisible", "Boolean");
			xamlMember.Getter = get_426_SearchControl_SearchListVisible;
			xamlMember.Setter = set_426_SearchControl_SearchListVisible;
			break;
		case "Vine.Views.SearchControl.SearchText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "SearchText", "String");
			xamlMember.Getter = get_427_SearchControl_SearchText;
			xamlMember.Setter = set_427_SearchControl_SearchText;
			break;
		case "Vine.Views.SearchControl.LastSearchText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "LastSearchText", "String");
			xamlMember.Getter = get_428_SearchControl_LastSearchText;
			xamlMember.Setter = set_428_SearchControl_LastSearchText;
			break;
		case "Vine.Views.SearchControl.PlaceHolderLabel":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "PlaceHolderLabel", "String");
			xamlMember.Getter = get_429_SearchControl_PlaceHolderLabel;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SearchControl.EmptyText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "EmptyText", "String");
			xamlMember.Getter = get_430_SearchControl_EmptyText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SearchControl.TileText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "TileText", "String");
			xamlMember.Getter = get_431_SearchControl_TileText;
			xamlMember.Setter = set_431_SearchControl_TileText;
			break;
		case "Vine.Views.SearchControl.HasTile":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchControl");
			xamlMember = new XamlMember(this, "HasTile", "Boolean");
			xamlMember.Getter = get_432_SearchControl_HasTile;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SearchTagsAllView.Items":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchTagsAllView");
			xamlMember = new XamlMember(this, "Items", "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.VineTagModel>");
			xamlMember.Getter = get_433_SearchTagsAllView_Items;
			xamlMember.Setter = set_433_SearchTagsAllView_Items;
			break;
		case "Vine.Models.VineTagModel.TagId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineTagModel");
			xamlMember = new XamlMember(this, "TagId", "String");
			xamlMember.Getter = get_434_VineTagModel_TagId;
			xamlMember.Setter = set_434_VineTagModel_TagId;
			break;
		case "Vine.Models.VineTagModel.Tag":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineTagModel");
			xamlMember = new XamlMember(this, "Tag", "String");
			xamlMember.Getter = get_435_VineTagModel_Tag;
			xamlMember.Setter = set_435_VineTagModel_Tag;
			break;
		case "Vine.Models.VineTagModel.PostCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineTagModel");
			xamlMember = new XamlMember(this, "PostCount", "Int32");
			xamlMember.Getter = get_436_VineTagModel_PostCount;
			xamlMember.Setter = set_436_VineTagModel_PostCount;
			break;
		case "Vine.Models.VineTagModel.FormattedTag":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineTagModel");
			xamlMember = new XamlMember(this, "FormattedTag", "String");
			xamlMember.Getter = get_437_VineTagModel_FormattedTag;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineTagModel.FormattedPostCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineTagModel");
			xamlMember = new XamlMember(this, "FormattedPostCount", "String");
			xamlMember.Getter = get_438_VineTagModel_FormattedPostCount;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SearchTagsAllView.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchTagsAllView");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_439_SearchTagsAllView_IsFinishedLoading;
			xamlMember.Setter = set_439_SearchTagsAllView_IsFinishedLoading;
			break;
		case "Vine.Views.SearchTagsAllView.IsEmpty":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchTagsAllView");
			xamlMember = new XamlMember(this, "IsEmpty", "Boolean");
			xamlMember.Getter = get_440_SearchTagsAllView_IsEmpty;
			xamlMember.Setter = set_440_SearchTagsAllView_IsEmpty;
			break;
		case "Vine.Views.SearchTagsAllView.ShowRetry":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchTagsAllView");
			xamlMember = new XamlMember(this, "ShowRetry", "Boolean");
			xamlMember.Getter = get_441_SearchTagsAllView_ShowRetry;
			xamlMember.Setter = set_441_SearchTagsAllView_ShowRetry;
			break;
		case "Vine.Views.SearchTagsAllView.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchTagsAllView");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.Getter = get_442_SearchTagsAllView_HasError;
			xamlMember.Setter = set_442_SearchTagsAllView_HasError;
			break;
		case "Vine.Views.SearchTagsAllView.PageTitle":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchTagsAllView");
			xamlMember = new XamlMember(this, "PageTitle", "String");
			xamlMember.Getter = get_443_SearchTagsAllView_PageTitle;
			xamlMember.Setter = set_443_SearchTagsAllView_PageTitle;
			break;
		case "Vine.Views.SearchTagsAllView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchTagsAllView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_444_SearchTagsAllView_IsBusy;
			xamlMember.Setter = set_444_SearchTagsAllView_IsBusy;
			break;
		case "Vine.Views.SearchTagsAllView.EmptyText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchTagsAllView");
			xamlMember = new XamlMember(this, "EmptyText", "String");
			xamlMember.Getter = get_445_SearchTagsAllView_EmptyText;
			xamlMember.Setter = set_445_SearchTagsAllView_EmptyText;
			break;
		case "Vine.Views.SearchTagsAllView.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SearchTagsAllView");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.Getter = get_446_SearchTagsAllView_ErrorText;
			xamlMember.Setter = set_446_SearchTagsAllView_ErrorText;
			break;
		case "Vine.Views.SettingsPrivacyView.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsPrivacyView");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_447_SettingsPrivacyView_User;
			xamlMember.Setter = set_447_SettingsPrivacyView_User;
			break;
		case "Vine.Views.SettingsPrivacyView.IsError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsPrivacyView");
			xamlMember = new XamlMember(this, "IsError", "Boolean");
			xamlMember.Getter = get_448_SettingsPrivacyView_IsError;
			xamlMember.Setter = set_448_SettingsPrivacyView_IsError;
			break;
		case "Vine.Views.SettingsPrivacyView.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsPrivacyView");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.Getter = get_449_SettingsPrivacyView_ErrorText;
			xamlMember.Setter = set_449_SettingsPrivacyView_ErrorText;
			break;
		case "Vine.Views.SettingsPrivacyView.ShowRetry":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsPrivacyView");
			xamlMember = new XamlMember(this, "ShowRetry", "Boolean");
			xamlMember.Getter = get_450_SettingsPrivacyView_ShowRetry;
			xamlMember.Setter = set_450_SettingsPrivacyView_ShowRetry;
			break;
		case "Vine.Views.SettingsPrivacyView.IsLoaded":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsPrivacyView");
			xamlMember = new XamlMember(this, "IsLoaded", "Boolean");
			xamlMember.Getter = get_451_SettingsPrivacyView_IsLoaded;
			xamlMember.Setter = set_451_SettingsPrivacyView_IsLoaded;
			break;
		case "Vine.Views.TemplateSelectors.ContactTemplateSelector.ContactTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.ContactTemplateSelector");
			xamlMember = new XamlMember(this, "ContactTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_452_ContactTemplateSelector_ContactTemplate;
			xamlMember.Setter = set_452_ContactTemplateSelector_ContactTemplate;
			break;
		case "Vine.Views.TemplateSelectors.ContactTemplateSelector.VineUserTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.ContactTemplateSelector");
			xamlMember = new XamlMember(this, "VineUserTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_453_ContactTemplateSelector_VineUserTemplate;
			xamlMember.Setter = set_453_ContactTemplateSelector_VineUserTemplate;
			break;
		case "Vine.Views.TemplateSelectors.ContactTemplateSelector.HeaderTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.ContactTemplateSelector");
			xamlMember = new XamlMember(this, "HeaderTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_454_ContactTemplateSelector_HeaderTemplate;
			xamlMember.Setter = set_454_ContactTemplateSelector_HeaderTemplate;
			break;
		case "Vine.Views.ShareMessageControl.SelectedItems":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "SelectedItems", "System.Collections.Generic.List`1<Vine.Models.VineContactViewModel>");
			xamlMember.Getter = get_455_ShareMessageControl_SelectedItems;
			xamlMember.Setter = set_455_ShareMessageControl_SelectedItems;
			break;
		case "Vine.Models.VineContactViewModel.Section":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineContactViewModel");
			xamlMember = new XamlMember(this, "Section", "String");
			xamlMember.Getter = get_456_VineContactViewModel_Section;
			xamlMember.Setter = set_456_VineContactViewModel_Section;
			break;
		case "Vine.Models.VineContactViewModel.Emails":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineContactViewModel");
			xamlMember = new XamlMember(this, "Emails", "System.Collections.Generic.List`1<String>");
			xamlMember.Getter = get_457_VineContactViewModel_Emails;
			xamlMember.Setter = set_457_VineContactViewModel_Emails;
			break;
		case "Vine.Models.VineContactViewModel.Phones":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineContactViewModel");
			xamlMember = new XamlMember(this, "Phones", "System.Collections.Generic.List`1<System.Tuple`2<String, String>>");
			xamlMember.Getter = get_458_VineContactViewModel_Phones;
			xamlMember.Setter = set_458_VineContactViewModel_Phones;
			break;
		case "System.Tuple`2<String, String>.Item1":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Tuple`2<String, String>");
			xamlMember = new XamlMember(this, "Item1", "String");
			xamlMember.Getter = get_459_Tuple_Item1;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Tuple`2<String, String>.Item2":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Tuple`2<String, String>");
			xamlMember = new XamlMember(this, "Item2", "String");
			xamlMember.Getter = get_460_Tuple_Item2;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineContactViewModel.SelectionVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineContactViewModel");
			xamlMember = new XamlMember(this, "SelectionVisibility", "Windows.UI.Xaml.Visibility");
			xamlMember.Getter = get_461_VineContactViewModel_SelectionVisibility;
			xamlMember.Setter = set_461_VineContactViewModel_SelectionVisibility;
			break;
		case "Vine.Models.VineContactViewModel.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineContactViewModel");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_462_VineContactViewModel_User;
			xamlMember.Setter = set_462_VineContactViewModel_User;
			break;
		case "Vine.Models.VineContactViewModel.UserVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineContactViewModel");
			xamlMember = new XamlMember(this, "UserVisibility", "Windows.UI.Xaml.Visibility");
			xamlMember.Getter = get_463_VineContactViewModel_UserVisibility;
			xamlMember.Setter = set_463_VineContactViewModel_UserVisibility;
			break;
		case "Vine.Models.VineContactViewModel.PhoneSelectionVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineContactViewModel");
			xamlMember = new XamlMember(this, "PhoneSelectionVisibility", "Windows.UI.Xaml.Visibility");
			xamlMember.Getter = get_464_VineContactViewModel_PhoneSelectionVisibility;
			xamlMember.Setter = set_464_VineContactViewModel_PhoneSelectionVisibility;
			break;
		case "Vine.Models.VineContactViewModel.PhoneVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineContactViewModel");
			xamlMember = new XamlMember(this, "PhoneVisibility", "Windows.UI.Xaml.Visibility");
			xamlMember.Getter = get_465_VineContactViewModel_PhoneVisibility;
			xamlMember.Setter = set_465_VineContactViewModel_PhoneVisibility;
			break;
		case "Vine.Models.VineContactViewModel.EmailSelectionVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineContactViewModel");
			xamlMember = new XamlMember(this, "EmailSelectionVisibility", "Windows.UI.Xaml.Visibility");
			xamlMember.Getter = get_466_VineContactViewModel_EmailSelectionVisibility;
			xamlMember.Setter = set_466_VineContactViewModel_EmailSelectionVisibility;
			break;
		case "Vine.Models.VineContactViewModel.EmailVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineContactViewModel");
			xamlMember = new XamlMember(this, "EmailVisibility", "Windows.UI.Xaml.Visibility");
			xamlMember.Getter = get_467_VineContactViewModel_EmailVisibility;
			xamlMember.Setter = set_467_VineContactViewModel_EmailVisibility;
			break;
		case "Vine.Models.VineContactViewModel.HeaderText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineContactViewModel");
			xamlMember = new XamlMember(this, "HeaderText", "String");
			xamlMember.Getter = get_468_VineContactViewModel_HeaderText;
			xamlMember.Setter = set_468_VineContactViewModel_HeaderText;
			break;
		case "Vine.Models.VineContactViewModel.IsSelected":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineContactViewModel");
			xamlMember = new XamlMember(this, "IsSelected", "Boolean");
			xamlMember.Getter = get_469_VineContactViewModel_IsSelected;
			xamlMember.Setter = set_469_VineContactViewModel_IsSelected;
			break;
		case "Vine.Views.ShareMessageControl.SearchText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "SearchText", "String");
			xamlMember.Getter = get_470_ShareMessageControl_SearchText;
			xamlMember.Setter = set_470_ShareMessageControl_SearchText;
			break;
		case "Vine.Views.ShareMessageControl.IsPplFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "IsPplFinishedLoading", "Boolean");
			xamlMember.Getter = get_471_ShareMessageControl_IsPplFinishedLoading;
			xamlMember.Setter = set_471_ShareMessageControl_IsPplFinishedLoading;
			break;
		case "Vine.Views.ShareMessageControl.IsSingleSelect":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "IsSingleSelect", "Boolean");
			xamlMember.Getter = get_472_ShareMessageControl_IsSingleSelect;
			xamlMember.Setter = set_472_ShareMessageControl_IsSingleSelect;
			break;
		case "Vine.Views.ShareMessageControl.PplHasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "PplHasError", "Boolean");
			xamlMember.Getter = get_473_ShareMessageControl_PplHasError;
			xamlMember.Setter = set_473_ShareMessageControl_PplHasError;
			break;
		case "Vine.Views.ShareMessageControl.PplIsEmpty":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "PplIsEmpty", "Boolean");
			xamlMember.Getter = get_474_ShareMessageControl_PplIsEmpty;
			xamlMember.Setter = set_474_ShareMessageControl_PplIsEmpty;
			break;
		case "Vine.Views.ShareMessageControl.Pending":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "Pending", "System.Threading.CancellationTokenSource");
			xamlMember.Getter = get_475_ShareMessageControl_Pending;
			xamlMember.Setter = set_475_ShareMessageControl_Pending;
			break;
		case "Vine.Views.ShareMessageControl.Friends":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "Friends", "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.VineContactViewModel>");
			xamlMember.Getter = get_476_ShareMessageControl_Friends;
			xamlMember.Setter = set_476_ShareMessageControl_Friends;
			break;
		case "Vine.Views.ShareMessageControl.Contacts":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "Contacts", "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.VineContactViewModel>");
			xamlMember.Getter = get_477_ShareMessageControl_Contacts;
			xamlMember.Setter = set_477_ShareMessageControl_Contacts;
			break;
		case "Vine.Views.ShareMessageControl.IsFriendsEmpty":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "IsFriendsEmpty", "Boolean");
			xamlMember.Getter = get_478_ShareMessageControl_IsFriendsEmpty;
			xamlMember.Setter = set_478_ShareMessageControl_IsFriendsEmpty;
			break;
		case "Vine.Views.ShareMessageControl.IsContactsEmpty":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "IsContactsEmpty", "Boolean");
			xamlMember.Getter = get_479_ShareMessageControl_IsContactsEmpty;
			xamlMember.Setter = set_479_ShareMessageControl_IsContactsEmpty;
			break;
		case "Vine.Views.ShareMessageControl.FriendsEmptyVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "FriendsEmptyVisibility", "Boolean");
			xamlMember.Getter = get_480_ShareMessageControl_FriendsEmptyVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ShareMessageControl.ContactsEmptyVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "ContactsEmptyVisibility", "Boolean");
			xamlMember.Getter = get_481_ShareMessageControl_ContactsEmptyVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ShareMessageControl.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_482_ShareMessageControl_IsBusy;
			xamlMember.Setter = set_482_ShareMessageControl_IsBusy;
			break;
		case "Vine.Views.ShareMessageControl.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.Getter = get_483_ShareMessageControl_HasError;
			xamlMember.Setter = set_483_ShareMessageControl_HasError;
			break;
		case "Vine.Views.ShareMessageControl.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.Getter = get_484_ShareMessageControl_ErrorText;
			xamlMember.Setter = set_484_ShareMessageControl_ErrorText;
			break;
		case "Vine.Views.ShareMessageControl.ShowRetry":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "ShowRetry", "Boolean");
			xamlMember.Getter = get_485_ShareMessageControl_ShowRetry;
			xamlMember.Setter = set_485_ShareMessageControl_ShowRetry;
			break;
		case "Vine.Views.ShareMessageControl.ErrorVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "ErrorVisibility", "Boolean");
			xamlMember.Getter = get_486_ShareMessageControl_ErrorVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ShareMessageControl.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageControl");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_487_ShareMessageControl_IsFinishedLoading;
			xamlMember.Setter = set_487_ShareMessageControl_IsFinishedLoading;
			break;
		case "Vine.Views.ShareMessageView.TitleText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageView");
			xamlMember = new XamlMember(this, "TitleText", "String");
			xamlMember.Getter = get_488_ShareMessageView_TitleText;
			xamlMember.Setter = set_488_ShareMessageView_TitleText;
			break;
		case "Vine.Views.ShareMessageView.TutorialHintVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageView");
			xamlMember = new XamlMember(this, "TutorialHintVisibility", "Boolean");
			xamlMember.Getter = get_489_ShareMessageView_TutorialHintVisibility;
			xamlMember.Setter = set_489_ShareMessageView_TutorialHintVisibility;
			break;
		case "Vine.Views.ShareMessageView.HeaderBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ShareMessageView");
			xamlMember = new XamlMember(this, "HeaderBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_490_ShareMessageView_HeaderBrush;
			xamlMember.Setter = set_490_ShareMessageView_HeaderBrush;
			break;
		case "Vine.Views.TappedToLikeControl.ZRotation":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TappedToLikeControl");
			xamlMember = new XamlMember(this, "ZRotation", "Int32");
			xamlMember.Getter = get_491_TappedToLikeControl_ZRotation;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.UpgradeView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.UpgradeView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_492_UpgradeView_IsBusy;
			xamlMember.Setter = set_492_UpgradeView_IsBusy;
			break;
		case "Vine.Views.VerifyPhoneCodeEnterView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VerifyPhoneCodeEnterView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_493_VerifyPhoneCodeEnterView_IsBusy;
			xamlMember.Setter = set_493_VerifyPhoneCodeEnterView_IsBusy;
			break;
		case "Vine.Views.VerifyPhoneCodeEnterView.RetryText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VerifyPhoneCodeEnterView");
			xamlMember = new XamlMember(this, "RetryText", "String");
			xamlMember.Getter = get_494_VerifyPhoneCodeEnterView_RetryText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VerifyPhoneCodeEnterView.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VerifyPhoneCodeEnterView");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_495_VerifyPhoneCodeEnterView_User;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VerifyPhoneCodeEnterView.HeaderText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VerifyPhoneCodeEnterView");
			xamlMember = new XamlMember(this, "HeaderText", "String");
			xamlMember.Getter = get_496_VerifyPhoneCodeEnterView_HeaderText;
			xamlMember.Setter = set_496_VerifyPhoneCodeEnterView_HeaderText;
			break;
		case "Vine.Views.VerifyEmailCodeEnterView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VerifyEmailCodeEnterView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_497_VerifyEmailCodeEnterView_IsBusy;
			xamlMember.Setter = set_497_VerifyEmailCodeEnterView_IsBusy;
			break;
		case "Vine.Views.VerifyEmailCodeEnterView.RetryText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VerifyEmailCodeEnterView");
			xamlMember = new XamlMember(this, "RetryText", "String");
			xamlMember.Getter = get_498_VerifyEmailCodeEnterView_RetryText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VerifyEmailCodeEnterView.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VerifyEmailCodeEnterView");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_499_VerifyEmailCodeEnterView_User;
			xamlMember.Setter = set_499_VerifyEmailCodeEnterView_User;
			break;
		case "Vine.Views.VerifyEmailCodeEnterView.HeaderText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VerifyEmailCodeEnterView");
			xamlMember = new XamlMember(this, "HeaderText", "String");
			xamlMember.Getter = get_500_VerifyEmailCodeEnterView_HeaderText;
			xamlMember.Setter = set_500_VerifyEmailCodeEnterView_HeaderText;
			break;
		case "Vine.Views.ExploreControl.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ExploreControl");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_501_ExploreControl_IsFinishedLoading;
			xamlMember.Setter = set_501_ExploreControl_IsFinishedLoading;
			break;
		case "Vine.Views.ExploreControl.IsLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ExploreControl");
			xamlMember = new XamlMember(this, "IsLoading", "Boolean");
			xamlMember.Getter = get_502_ExploreControl_IsLoading;
			xamlMember.Setter = set_502_ExploreControl_IsLoading;
			break;
		case "Vine.Views.ExploreControl.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ExploreControl");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.Getter = get_503_ExploreControl_HasError;
			xamlMember.Setter = set_503_ExploreControl_HasError;
			break;
		case "Vine.Views.ExploreControl.BrowserVisible":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ExploreControl");
			xamlMember = new XamlMember(this, "BrowserVisible", "Boolean");
			xamlMember.Getter = get_504_ExploreControl_BrowserVisible;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ExploreControl.SearchActive":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ExploreControl");
			xamlMember = new XamlMember(this, "SearchActive", "Boolean");
			xamlMember.Getter = get_505_ExploreControl_SearchActive;
			xamlMember.Setter = set_505_ExploreControl_SearchActive;
			break;
		case "Vine.Views.ExploreControl.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ExploreControl");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.Getter = get_506_ExploreControl_ErrorText;
			xamlMember.Setter = set_506_ExploreControl_ErrorText;
			break;
		case "Vine.Views.ExploreControl.ShowRetry":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ExploreControl");
			xamlMember = new XamlMember(this, "ShowRetry", "Boolean");
			xamlMember.Getter = get_507_ExploreControl_ShowRetry;
			xamlMember.Setter = set_507_ExploreControl_ShowRetry;
			break;
		case "Vine.Framework.Behavior`1<Windows.UI.Xaml.Controls.PasswordBox>.AssociatedObject":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Framework.Behavior`1<Windows.UI.Xaml.Controls.PasswordBox>");
			xamlMember = new XamlMember(this, "AssociatedObject", "Windows.UI.Xaml.DependencyObject");
			xamlMember.Getter = get_508_Behavior_AssociatedObject;
			xamlMember.Setter = set_508_Behavior_AssociatedObject;
			break;
		case "Vine.Framework.Behavior`1<Windows.UI.Xaml.Controls.PasswordBox>.Object":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Framework.Behavior`1<Windows.UI.Xaml.Controls.PasswordBox>");
			xamlMember = new XamlMember(this, "Object", "Windows.UI.Xaml.Controls.PasswordBox");
			xamlMember.Getter = get_509_Behavior_Object;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SignUpEmailDetailsView.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SignUpEmailDetailsView");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_510_SignUpEmailDetailsView_User;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SignUpEmailDetailsView.Email":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SignUpEmailDetailsView");
			xamlMember = new XamlMember(this, "Email", "String");
			xamlMember.Getter = get_511_SignUpEmailDetailsView_Email;
			xamlMember.Setter = set_511_SignUpEmailDetailsView_Email;
			break;
		case "Vine.Views.SignUpEmailDetailsView.PhoneNumber":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SignUpEmailDetailsView");
			xamlMember = new XamlMember(this, "PhoneNumber", "String");
			xamlMember.Getter = get_512_SignUpEmailDetailsView_PhoneNumber;
			xamlMember.Setter = set_512_SignUpEmailDetailsView_PhoneNumber;
			break;
		case "Vine.Views.SignUpEmailDetailsView.Password":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SignUpEmailDetailsView");
			xamlMember = new XamlMember(this, "Password", "String");
			xamlMember.Getter = get_513_SignUpEmailDetailsView_Password;
			xamlMember.Setter = set_513_SignUpEmailDetailsView_Password;
			break;
		case "Vine.Views.SignUpEmailDetailsView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SignUpEmailDetailsView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_514_SignUpEmailDetailsView_IsBusy;
			xamlMember.Setter = set_514_SignUpEmailDetailsView_IsBusy;
			break;
		case "Vine.Views.SignUpEmailDetailsView.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SignUpEmailDetailsView");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.Getter = get_515_SignUpEmailDetailsView_ErrorText;
			xamlMember.Setter = set_515_SignUpEmailDetailsView_ErrorText;
			break;
		case "Vine.Views.SignUpEmailDetailsView.IsError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SignUpEmailDetailsView");
			xamlMember = new XamlMember(this, "IsError", "Boolean");
			xamlMember.Getter = get_516_SignUpEmailDetailsView_IsError;
			xamlMember.Setter = set_516_SignUpEmailDetailsView_IsError;
			break;
		case "Vine.Views.SignUpEmailDetailsView.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SignUpEmailDetailsView");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_517_SignUpEmailDetailsView_IsFinishedLoading;
			xamlMember.Setter = set_517_SignUpEmailDetailsView_IsFinishedLoading;
			break;
		case "Vine.Views.SignUpEmailView.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SignUpEmailView");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_518_SignUpEmailView_User;
			xamlMember.Setter = set_518_SignUpEmailView_User;
			break;
		case "Vine.Views.SignUpEmailView.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SignUpEmailView");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_519_SignUpEmailView_IsFinishedLoading;
			xamlMember.Setter = set_519_SignUpEmailView_IsFinishedLoading;
			break;
		case "Vine.Views.VineMessagesInbox.TutorialHintVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesInbox");
			xamlMember = new XamlMember(this, "TutorialHintVisibility", "Boolean");
			xamlMember.Getter = get_520_VineMessagesInbox_TutorialHintVisibility;
			xamlMember.Setter = set_520_VineMessagesInbox_TutorialHintVisibility;
			break;
		case "Vine.Views.VineMessagesInbox.NewCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesInbox");
			xamlMember = new XamlMember(this, "NewCount", "Int64");
			xamlMember.Getter = get_521_VineMessagesInbox_NewCount;
			xamlMember.Setter = set_521_VineMessagesInbox_NewCount;
			break;
		case "Vine.Views.VineMessagesInbox.IsOtherInboxActive":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesInbox");
			xamlMember = new XamlMember(this, "IsOtherInboxActive", "Boolean");
			xamlMember.Getter = get_522_VineMessagesInbox_IsOtherInboxActive;
			xamlMember.Setter = set_522_VineMessagesInbox_IsOtherInboxActive;
			break;
		case "Vine.Views.ProfileControl.Section":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "Section", "Vine.Models.Analytics.Section");
			xamlMember.Getter = get_523_ProfileControl_Section;
			xamlMember.Setter = set_523_ProfileControl_Section;
			break;
		case "Vine.Views.ProfileControl.PullToRefreshMargin":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "PullToRefreshMargin", "Windows.UI.Xaml.Thickness");
			xamlMember.Setter = set_524_ProfileControl_PullToRefreshMargin;
			break;
		case "Vine.Views.ProfileControl.ListViewPadding":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "ListViewPadding", "Windows.UI.Xaml.Thickness");
			xamlMember.Setter = set_525_ProfileControl_ListViewPadding;
			break;
		case "Vine.Views.ProfileControl.IsScrolledBelowPlaceholder":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "IsScrolledBelowPlaceholder", "Boolean");
			xamlMember.Getter = get_526_ProfileControl_IsScrolledBelowPlaceholder;
			xamlMember.Setter = set_526_ProfileControl_IsScrolledBelowPlaceholder;
			break;
		case "Vine.Views.ProfileControl.ProfileHeaderHeight":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "ProfileHeaderHeight", "Double");
			xamlMember.Getter = get_527_ProfileControl_ProfileHeaderHeight;
			xamlMember.Setter = set_527_ProfileControl_ProfileHeaderHeight;
			break;
		case "Vine.Views.ProfileControl.FollowApprovalBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "FollowApprovalBusy", "Boolean");
			xamlMember.Getter = get_528_ProfileControl_FollowApprovalBusy;
			xamlMember.Setter = set_528_ProfileControl_FollowApprovalBusy;
			break;
		case "Vine.Views.ProfileControl.FollowApprovalNotBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "FollowApprovalNotBusy", "Boolean");
			xamlMember.Getter = get_529_ProfileControl_FollowApprovalNotBusy;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ProfileControl.FooterMargin":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "FooterMargin", "Windows.UI.Xaml.Thickness");
			xamlMember.Getter = get_530_ProfileControl_FooterMargin;
			xamlMember.Setter = set_530_ProfileControl_FooterMargin;
			break;
		case "Vine.Views.ProfileControl.LikeBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "LikeBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_531_ProfileControl_LikeBrush;
			xamlMember.Setter = set_531_ProfileControl_LikeBrush;
			break;
		case "Vine.Views.ProfileControl.ControlWrapper":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "ControlWrapper", "Vine.Models.UserControlWrapper");
			xamlMember.Getter = get_532_ProfileControl_ControlWrapper;
			xamlMember.Setter = set_532_ProfileControl_ControlWrapper;
			break;
		case "Vine.Views.ProfileControl.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_533_ProfileControl_User;
			xamlMember.Setter = set_533_ProfileControl_User;
			break;
		case "Vine.Views.ProfileControl.UserId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "UserId", "String");
			xamlMember.Getter = get_534_ProfileControl_UserId;
			xamlMember.Setter = set_534_ProfileControl_UserId;
			break;
		case "Vine.Views.ProfileControl.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_535_ProfileControl_IsBusy;
			xamlMember.Setter = set_535_ProfileControl_IsBusy;
			break;
		case "Vine.Views.ProfileControl.IsEmpty":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "IsEmpty", "Boolean");
			xamlMember.Getter = get_536_ProfileControl_IsEmpty;
			xamlMember.Setter = set_536_ProfileControl_IsEmpty;
			break;
		case "Vine.Views.ProfileControl.EmptyText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "EmptyText", "String");
			xamlMember.Getter = get_537_ProfileControl_EmptyText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ProfileControl.IsSwitchingTab":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "IsSwitchingTab", "Boolean");
			xamlMember.Getter = get_538_ProfileControl_IsSwitchingTab;
			xamlMember.Setter = set_538_ProfileControl_IsSwitchingTab;
			break;
		case "Vine.Views.ProfileControl.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.Getter = get_539_ProfileControl_HasError;
			xamlMember.Setter = set_539_ProfileControl_HasError;
			break;
		case "Vine.Views.ProfileControl.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.Getter = get_540_ProfileControl_ErrorText;
			xamlMember.Setter = set_540_ProfileControl_ErrorText;
			break;
		case "Vine.Views.ProfileControl.ShowRetry":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "ShowRetry", "Boolean");
			xamlMember.Getter = get_541_ProfileControl_ShowRetry;
			xamlMember.Setter = set_541_ProfileControl_ShowRetry;
			break;
		case "Vine.Views.ProfileControl.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_542_ProfileControl_IsFinishedLoading;
			xamlMember.Setter = set_542_ProfileControl_IsFinishedLoading;
			break;
		case "Vine.Views.ProfileControl.TutorialHintVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "TutorialHintVisibility", "Boolean");
			xamlMember.Getter = get_543_ProfileControl_TutorialHintVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ProfileControl.ShowExplore":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "ShowExplore", "Boolean");
			xamlMember.Getter = get_544_ProfileControl_ShowExplore;
			xamlMember.Setter = set_544_ProfileControl_ShowExplore;
			break;
		case "Vine.Views.ProfileControl.ShowSuggestions":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "ShowSuggestions", "Boolean");
			xamlMember.Getter = get_545_ProfileControl_ShowSuggestions;
			xamlMember.Setter = set_545_ProfileControl_ShowSuggestions;
			break;
		case "Vine.Views.ProfileControl._suggestedToFollow":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "_suggestedToFollow", "System.Collections.Generic.List`1<Vine.Models.VineUserModel>");
			xamlMember.Getter = get_546_ProfileControl__suggestedToFollow;
			xamlMember.Setter = set_546_ProfileControl__suggestedToFollow;
			break;
		case "Vine.Models.VineUserModel.Section":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "Section", "String");
			xamlMember.Getter = get_547_VineUserModel_Section;
			xamlMember.Setter = set_547_VineUserModel_Section;
			break;
		case "Vine.Models.VineUserModel.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_548_VineUserModel_User;
			xamlMember.Setter = set_548_VineUserModel_User;
			break;
		case "Vine.Models.VineUserModel.ExternalUser":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "ExternalUser", "Boolean");
			xamlMember.Getter = get_549_VineUserModel_ExternalUser;
			xamlMember.Setter = set_549_VineUserModel_ExternalUser;
			break;
		case "Vine.Models.VineUserModel.ProfileBackground":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "ProfileBackground", "String");
			xamlMember.Getter = get_550_VineUserModel_ProfileBackground;
			xamlMember.Setter = set_550_VineUserModel_ProfileBackground;
			break;
		case "Vine.Models.VineUserModel.ProfileBgBannerBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "ProfileBgBannerBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_551_VineUserModel_ProfileBgBannerBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.ProfileBgBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "ProfileBgBrush", "Windows.UI.Xaml.Media.SolidColorBrush");
			xamlMember.Getter = get_552_VineUserModel_ProfileBgBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.ProfileBgLightBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "ProfileBgLightBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_553_VineUserModel_ProfileBgLightBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.PhoneNumber":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "PhoneNumber", "String");
			xamlMember.Getter = get_554_VineUserModel_PhoneNumber;
			xamlMember.Setter = set_554_VineUserModel_PhoneNumber;
			break;
		case "Vine.Models.VineUserModel.Email":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "Email", "String");
			xamlMember.Getter = get_555_VineUserModel_Email;
			xamlMember.Setter = set_555_VineUserModel_Email;
			break;
		case "Vine.Models.VineUserModel.Username":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "Username", "String");
			xamlMember.Getter = get_556_VineUserModel_Username;
			xamlMember.Setter = set_556_VineUserModel_Username;
			break;
		case "Vine.Models.VineUserModel.Description":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "Description", "String");
			xamlMember.Getter = get_557_VineUserModel_Description;
			xamlMember.Setter = set_557_VineUserModel_Description;
			break;
		case "Vine.Models.VineUserModel.AvatarUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "AvatarUrl", "String");
			xamlMember.Getter = get_558_VineUserModel_AvatarUrl;
			xamlMember.Setter = set_558_VineUserModel_AvatarUrl;
			break;
		case "Vine.Models.VineUserModel.Location":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "Location", "String");
			xamlMember.Getter = get_559_VineUserModel_Location;
			xamlMember.Setter = set_559_VineUserModel_Location;
			break;
		case "Vine.Models.VineUserModel.UserId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "UserId", "String");
			xamlMember.Getter = get_560_VineUserModel_UserId;
			xamlMember.Setter = set_560_VineUserModel_UserId;
			break;
		case "Vine.Models.VineUserModel.FollowingCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "FollowingCount", "Int64");
			xamlMember.Getter = get_561_VineUserModel_FollowingCount;
			xamlMember.Setter = set_561_VineUserModel_FollowingCount;
			break;
		case "Vine.Models.VineUserModel.FollowerCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "FollowerCount", "Int64");
			xamlMember.Getter = get_562_VineUserModel_FollowerCount;
			xamlMember.Setter = set_562_VineUserModel_FollowerCount;
			break;
		case "Vine.Models.VineUserModel.PostCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "PostCount", "Int64");
			xamlMember.Getter = get_563_VineUserModel_PostCount;
			xamlMember.Setter = set_563_VineUserModel_PostCount;
			break;
		case "Vine.Models.VineUserModel.LikeCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "LikeCount", "Int64");
			xamlMember.Getter = get_564_VineUserModel_LikeCount;
			xamlMember.Setter = set_564_VineUserModel_LikeCount;
			break;
		case "Vine.Models.VineUserModel.LoopCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "LoopCount", "Int64");
			xamlMember.Getter = get_565_VineUserModel_LoopCount;
			xamlMember.Setter = set_565_VineUserModel_LoopCount;
			break;
		case "Vine.Models.VineUserModel.Blocked":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "Blocked", "Boolean");
			xamlMember.Getter = get_566_VineUserModel_Blocked;
			xamlMember.Setter = set_566_VineUserModel_Blocked;
			break;
		case "Vine.Models.VineUserModel.Following":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "Following", "Boolean");
			xamlMember.Getter = get_567_VineUserModel_Following;
			xamlMember.Setter = set_567_VineUserModel_Following;
			break;
		case "Vine.Models.VineUserModel.Verified":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "Verified", "Boolean");
			xamlMember.Getter = get_568_VineUserModel_Verified;
			xamlMember.Setter = set_568_VineUserModel_Verified;
			break;
		case "Vine.Models.VineUserModel.FollowRequested":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "FollowRequested", "Boolean");
			xamlMember.Getter = get_569_VineUserModel_FollowRequested;
			xamlMember.Setter = set_569_VineUserModel_FollowRequested;
			break;
		case "Vine.Models.VineUserModel.Private":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "Private", "Boolean");
			xamlMember.Getter = get_570_VineUserModel_Private;
			xamlMember.Setter = set_570_VineUserModel_Private;
			break;
		case "Vine.Models.VineUserModel.TwitterScreenname":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "TwitterScreenname", "String");
			xamlMember.Getter = get_571_VineUserModel_TwitterScreenname;
			xamlMember.Setter = set_571_VineUserModel_TwitterScreenname;
			break;
		case "Vine.Models.VineUserModel.TwitterDisplayScreenname":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "TwitterDisplayScreenname", "String");
			xamlMember.Getter = get_572_VineUserModel_TwitterDisplayScreenname;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.TwitterConnected":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "TwitterConnected", "Boolean");
			xamlMember.Getter = get_573_VineUserModel_TwitterConnected;
			xamlMember.Setter = set_573_VineUserModel_TwitterConnected;
			break;
		case "Vine.Models.VineUserModel.VerifiedPhoneNumber":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "VerifiedPhoneNumber", "Boolean");
			xamlMember.Getter = get_574_VineUserModel_VerifiedPhoneNumber;
			xamlMember.Setter = set_574_VineUserModel_VerifiedPhoneNumber;
			break;
		case "Vine.Models.VineUserModel.VerifiedEmail":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "VerifiedEmail", "Boolean");
			xamlMember.Getter = get_575_VineUserModel_VerifiedEmail;
			xamlMember.Setter = set_575_VineUserModel_VerifiedEmail;
			break;
		case "Vine.Models.VineUserModel.FacebookConnected":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "FacebookConnected", "Boolean");
			xamlMember.Getter = get_576_VineUserModel_FacebookConnected;
			xamlMember.Setter = set_576_VineUserModel_FacebookConnected;
			break;
		case "Vine.Models.VineUserModel.ExplicitContent":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "ExplicitContent", "Boolean");
			xamlMember.Getter = get_577_VineUserModel_ExplicitContent;
			xamlMember.Setter = set_577_VineUserModel_ExplicitContent;
			break;
		case "Vine.Models.VineUserModel.HiddenEmail":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "HiddenEmail", "Boolean");
			xamlMember.Getter = get_578_VineUserModel_HiddenEmail;
			xamlMember.Setter = set_578_VineUserModel_HiddenEmail;
			break;
		case "Vine.Models.VineUserModel.HiddenEmailButtonState":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "HiddenEmailButtonState", "Vine.Views.VineToggleButtonState");
			xamlMember.Getter = get_579_VineUserModel_HiddenEmailButtonState;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.HiddenPhoneNumber":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "HiddenPhoneNumber", "Boolean");
			xamlMember.Getter = get_580_VineUserModel_HiddenPhoneNumber;
			xamlMember.Setter = set_580_VineUserModel_HiddenPhoneNumber;
			break;
		case "Vine.Models.VineUserModel.HiddenPhoneNumberButtonState":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "HiddenPhoneNumberButtonState", "Vine.Views.VineToggleButtonState");
			xamlMember.Getter = get_581_VineUserModel_HiddenPhoneNumberButtonState;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.HiddenTwitter":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "HiddenTwitter", "Boolean");
			xamlMember.Getter = get_582_VineUserModel_HiddenTwitter;
			xamlMember.Setter = set_582_VineUserModel_HiddenTwitter;
			break;
		case "Vine.Models.VineUserModel.HiddenTwitterButtonState":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "HiddenTwitterButtonState", "Vine.Views.VineToggleButtonState");
			xamlMember.Getter = get_583_VineUserModel_HiddenTwitterButtonState;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.FollowApprovalPending":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "FollowApprovalPending", "Boolean");
			xamlMember.Getter = get_584_VineUserModel_FollowApprovalPending;
			xamlMember.Setter = set_584_VineUserModel_FollowApprovalPending;
			break;
		case "Vine.Models.VineUserModel.ByLine":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "ByLine", "String");
			xamlMember.Getter = get_585_VineUserModel_ByLine;
			xamlMember.Setter = set_585_VineUserModel_ByLine;
			break;
		case "Vine.Models.VineUserModel.FollowingEnabled":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "FollowingEnabled", "Boolean");
			xamlMember.Getter = get_586_VineUserModel_FollowingEnabled;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.FollowButtonState":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "FollowButtonState", "Vine.Views.VineToggleButtonState");
			xamlMember.Getter = get_587_VineUserModel_FollowButtonState;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.ExplicitContentButtonState":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "ExplicitContentButtonState", "Vine.Views.VineToggleButtonState");
			xamlMember.Getter = get_588_VineUserModel_ExplicitContentButtonState;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.ProtectedButtonState":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "ProtectedButtonState", "Vine.Views.VineToggleButtonState");
			xamlMember.Getter = get_589_VineUserModel_ProtectedButtonState;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.IsCurrentUser":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "IsCurrentUser", "Boolean");
			xamlMember.Getter = get_590_VineUserModel_IsCurrentUser;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.AreVinesViewable":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "AreVinesViewable", "Boolean");
			xamlMember.Getter = get_591_VineUserModel_AreVinesViewable;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.FollowCommand":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "FollowCommand", "Vine.Framework.RelayCommand");
			xamlMember.Getter = get_592_VineUserModel_FollowCommand;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.UserType":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "UserType", "Vine.Models.VineUserType");
			xamlMember.Getter = get_593_VineUserModel_UserType;
			xamlMember.Setter = set_593_VineUserModel_UserType;
			break;
		case "Vine.Models.VineUserModel.RichFollowers":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "RichFollowers", "System.Collections.Generic.List`1<Windows.UI.Xaml.Documents.Run>");
			xamlMember.Getter = get_594_VineUserModel_RichFollowers;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.RichFollowing":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "RichFollowing", "System.Collections.Generic.List`1<Windows.UI.Xaml.Documents.Run>");
			xamlMember.Getter = get_595_VineUserModel_RichFollowing;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.RichLoops":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "RichLoops", "System.Collections.Generic.List`1<Windows.UI.Xaml.Documents.Run>");
			xamlMember.Getter = get_596_VineUserModel_RichLoops;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.LoopCountShortened":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "LoopCountShortened", "String");
			xamlMember.Getter = get_597_VineUserModel_LoopCountShortened;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.RichPostCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "RichPostCount", "System.Collections.Generic.List`1<Windows.UI.Xaml.Documents.Run>");
			xamlMember.Getter = get_598_VineUserModel_RichPostCount;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineUserModel.RichLikesCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineUserModel");
			xamlMember = new XamlMember(this, "RichLikesCount", "System.Collections.Generic.List`1<Windows.UI.Xaml.Documents.Run>");
			xamlMember.Getter = get_599_VineUserModel_RichLikesCount;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ProfileControl.VisibleSuggestedToFollow":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "VisibleSuggestedToFollow", "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.VineUserModel>");
			xamlMember.Getter = get_600_ProfileControl_VisibleSuggestedToFollow;
			xamlMember.Setter = set_600_ProfileControl_VisibleSuggestedToFollow;
			break;
		case "Vine.Views.ProfileControl.IsSuggestedLoaded":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "IsSuggestedLoaded", "Boolean");
			xamlMember.Getter = get_601_ProfileControl_IsSuggestedLoaded;
			xamlMember.Setter = set_601_ProfileControl_IsSuggestedLoaded;
			break;
		case "Vine.Views.ProfileControl.IsActive":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "IsActive", "Boolean");
			xamlMember.Getter = get_602_ProfileControl_IsActive;
			xamlMember.Setter = set_602_ProfileControl_IsActive;
			break;
		case "Vine.Views.ProfileControl.PostBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileControl");
			xamlMember = new XamlMember(this, "PostBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_603_ProfileControl_PostBrush;
			xamlMember.Setter = set_603_ProfileControl_PostBrush;
			break;
		case "Vine.Views.InteractionsControl.Items":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.InteractionsControl");
			xamlMember = new XamlMember(this, "Items", "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.InteractionModel>");
			xamlMember.Getter = get_604_InteractionsControl_Items;
			xamlMember.Setter = set_604_InteractionsControl_Items;
			break;
		case "Vine.Models.InteractionModel.Type":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "Type", "String");
			xamlMember.Getter = get_605_InteractionModel_Type;
			xamlMember.Setter = set_605_InteractionModel_Type;
			break;
		case "Vine.Models.InteractionModel.InteractionType":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "InteractionType", "Vine.Models.InteractionType");
			xamlMember.Getter = get_606_InteractionModel_InteractionType;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.InteractionModel.Milestone":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "Milestone", "Vine.Models.Milestone");
			xamlMember.Getter = get_607_InteractionModel_Milestone;
			xamlMember.Setter = set_607_InteractionModel_Milestone;
			break;
		case "Vine.Models.InteractionModel.Body":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "Body", "String");
			xamlMember.Getter = get_608_InteractionModel_Body;
			xamlMember.Setter = set_608_InteractionModel_Body;
			break;
		case "Vine.Models.InteractionModel.IsNew":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "IsNew", "Boolean");
			xamlMember.Getter = get_609_InteractionModel_IsNew;
			xamlMember.Setter = set_609_InteractionModel_IsNew;
			break;
		case "Vine.Models.InteractionModel.NotificationId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "NotificationId", "String");
			xamlMember.Getter = get_610_InteractionModel_NotificationId;
			xamlMember.Setter = set_610_InteractionModel_NotificationId;
			break;
		case "Vine.Models.InteractionModel.ActivityId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "ActivityId", "String");
			xamlMember.Getter = get_611_InteractionModel_ActivityId;
			xamlMember.Setter = set_611_InteractionModel_ActivityId;
			break;
		case "Vine.Models.InteractionModel.Id":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "Id", "String");
			xamlMember.Getter = get_612_InteractionModel_Id;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.InteractionModel.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_613_InteractionModel_User;
			xamlMember.Setter = set_613_InteractionModel_User;
			break;
		case "Vine.Models.InteractionModel.Post":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "Post", "Vine.Models.VineModel");
			xamlMember.Getter = get_614_InteractionModel_Post;
			xamlMember.Setter = set_614_InteractionModel_Post;
			break;
		case "Vine.Models.InteractionModel.UserId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "UserId", "String");
			xamlMember.Getter = get_615_InteractionModel_UserId;
			xamlMember.Setter = set_615_InteractionModel_UserId;
			break;
		case "Vine.Models.InteractionModel.AvatarUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "AvatarUrl", "String");
			xamlMember.Getter = get_616_InteractionModel_AvatarUrl;
			xamlMember.Setter = set_616_InteractionModel_AvatarUrl;
			break;
		case "Vine.Models.InteractionModel.NotificationTypeId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "NotificationTypeId", "Vine.Models.InteractionType");
			xamlMember.Getter = get_617_InteractionModel_NotificationTypeId;
			xamlMember.Setter = set_617_InteractionModel_NotificationTypeId;
			break;
		case "Vine.Models.InteractionModel.FollowVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "FollowVisibility", "Boolean");
			xamlMember.Getter = get_618_InteractionModel_FollowVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.InteractionModel.UserThumbnail":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "UserThumbnail", "String");
			xamlMember.Getter = get_619_InteractionModel_UserThumbnail;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.InteractionModel.MilestoneThumbUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "MilestoneThumbUrl", "String");
			xamlMember.Getter = get_620_InteractionModel_MilestoneThumbUrl;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.InteractionModel.PostId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "PostId", "String");
			xamlMember.Getter = get_621_InteractionModel_PostId;
			xamlMember.Setter = set_621_InteractionModel_PostId;
			break;
		case "Vine.Models.InteractionModel.ThumbnailUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "ThumbnailUrl", "String");
			xamlMember.Getter = get_622_InteractionModel_ThumbnailUrl;
			xamlMember.Setter = set_622_InteractionModel_ThumbnailUrl;
			break;
		case "Vine.Models.InteractionModel.PostThumbnailUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "PostThumbnailUrl", "String");
			xamlMember.Getter = get_623_InteractionModel_PostThumbnailUrl;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.InteractionModel.HasPost":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "HasPost", "Boolean");
			xamlMember.Getter = get_624_InteractionModel_HasPost;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.InteractionModel.Created":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "Created", "System.DateTime");
			xamlMember.Getter = get_625_InteractionModel_Created;
			xamlMember.Setter = set_625_InteractionModel_Created;
			break;
		case "Vine.Models.InteractionModel.LastActivityTime":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "LastActivityTime", "System.DateTime");
			xamlMember.Getter = get_626_InteractionModel_LastActivityTime;
			xamlMember.Setter = set_626_InteractionModel_LastActivityTime;
			break;
		case "Vine.Models.InteractionModel.RichBody":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "RichBody", "Windows.UI.Xaml.FrameworkElement");
			xamlMember.Getter = get_627_InteractionModel_RichBody;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.InteractionModel.GlyphBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "GlyphBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_628_InteractionModel_GlyphBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.InteractionModel.GlyphVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "GlyphVisibility", "Boolean");
			xamlMember.Getter = get_629_InteractionModel_GlyphVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.InteractionModel.GlyphFollowedVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "GlyphFollowedVisibility", "Boolean");
			xamlMember.Getter = get_630_InteractionModel_GlyphFollowedVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.InteractionModel.GlyphData":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "GlyphData", "String");
			xamlMember.Getter = get_631_InteractionModel_GlyphData;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.InteractionModel.HeartGlyphVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "HeartGlyphVisibility", "Boolean");
			xamlMember.Getter = get_632_InteractionModel_HeartGlyphVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.InteractionModel.Entities":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "Entities", "System.Collections.Generic.List`1<Vine.Models.Entity>");
			xamlMember.Getter = get_633_InteractionModel_Entities;
			xamlMember.Setter = set_633_InteractionModel_Entities;
			break;
		case "Vine.Models.InteractionModel.ShortBodyEntities":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "ShortBodyEntities", "System.Collections.Generic.List`1<Vine.Models.Entity>");
			xamlMember.Getter = get_634_InteractionModel_ShortBodyEntities;
			xamlMember.Setter = set_634_InteractionModel_ShortBodyEntities;
			break;
		case "Vine.Models.InteractionModel.CommentTextEntities":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "CommentTextEntities", "System.Collections.Generic.List`1<Vine.Models.Entity>");
			xamlMember.Getter = get_635_InteractionModel_CommentTextEntities;
			xamlMember.Setter = set_635_InteractionModel_CommentTextEntities;
			break;
		case "Vine.Models.InteractionModel.CreatedText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "CreatedText", "String");
			xamlMember.Getter = get_636_InteractionModel_CreatedText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.InteractionModel.HeaderText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "HeaderText", "String");
			xamlMember.Getter = get_637_InteractionModel_HeaderText;
			xamlMember.Setter = set_637_InteractionModel_HeaderText;
			break;
		case "Vine.Models.InteractionModel.IsHeaderVisible":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "IsHeaderVisible", "Boolean");
			xamlMember.Getter = get_638_InteractionModel_IsHeaderVisible;
			xamlMember.Setter = set_638_InteractionModel_IsHeaderVisible;
			break;
		case "Vine.Models.InteractionModel.ShortBody":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "ShortBody", "String");
			xamlMember.Getter = get_639_InteractionModel_ShortBody;
			xamlMember.Setter = set_639_InteractionModel_ShortBody;
			break;
		case "Vine.Models.InteractionModel.CommentText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "CommentText", "String");
			xamlMember.Getter = get_640_InteractionModel_CommentText;
			xamlMember.Setter = set_640_InteractionModel_CommentText;
			break;
		case "Vine.Models.InteractionModel.RichCommentText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.InteractionModel");
			xamlMember = new XamlMember(this, "RichCommentText", "Windows.UI.Xaml.FrameworkElement");
			xamlMember.Getter = get_641_InteractionModel_RichCommentText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.InteractionsControl.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.InteractionsControl");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_642_InteractionsControl_IsBusy;
			xamlMember.Setter = set_642_InteractionsControl_IsBusy;
			break;
		case "Vine.Views.InteractionsControl.IsEmpty":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.InteractionsControl");
			xamlMember = new XamlMember(this, "IsEmpty", "Boolean");
			xamlMember.Getter = get_643_InteractionsControl_IsEmpty;
			xamlMember.Setter = set_643_InteractionsControl_IsEmpty;
			break;
		case "Vine.Views.InteractionsControl.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.InteractionsControl");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.Getter = get_644_InteractionsControl_HasError;
			xamlMember.Setter = set_644_InteractionsControl_HasError;
			break;
		case "Vine.Views.InteractionsControl.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.InteractionsControl");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.Getter = get_645_InteractionsControl_ErrorText;
			xamlMember.Setter = set_645_InteractionsControl_ErrorText;
			break;
		case "Vine.Views.InteractionsControl.ShowRetry":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.InteractionsControl");
			xamlMember = new XamlMember(this, "ShowRetry", "Boolean");
			xamlMember.Getter = get_646_InteractionsControl_ShowRetry;
			xamlMember.Setter = set_646_InteractionsControl_ShowRetry;
			break;
		case "Vine.Views.InteractionsControl.NewCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.InteractionsControl");
			xamlMember = new XamlMember(this, "NewCount", "Int64");
			xamlMember.Getter = get_647_InteractionsControl_NewCount;
			xamlMember.Setter = set_647_InteractionsControl_NewCount;
			break;
		case "Vine.Views.InteractionsControl.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.InteractionsControl");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_648_InteractionsControl_IsFinishedLoading;
			xamlMember.Setter = set_648_InteractionsControl_IsFinishedLoading;
			break;
		case "Vine.Views.InteractionsControl.IsActive":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.InteractionsControl");
			xamlMember = new XamlMember(this, "IsActive", "Boolean");
			xamlMember.Getter = get_649_InteractionsControl_IsActive;
			xamlMember.Setter = set_649_InteractionsControl_IsActive;
			break;
		case "Vine.Views.HomeView.PinSearchVisible":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "PinSearchVisible", "Boolean");
			xamlMember.Getter = get_650_HomeView_PinSearchVisible;
			xamlMember.Setter = set_650_HomeView_PinSearchVisible;
			break;
		case "Vine.Views.HomeView.HomeIconFill":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "HomeIconFill", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_651_HomeView_HomeIconFill;
			xamlMember.Setter = set_651_HomeView_HomeIconFill;
			break;
		case "Vine.Views.HomeView.NotificationIconFill":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "NotificationIconFill", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_652_HomeView_NotificationIconFill;
			xamlMember.Setter = set_652_HomeView_NotificationIconFill;
			break;
		case "Vine.Views.HomeView.DiscoverIconFill":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "DiscoverIconFill", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_653_HomeView_DiscoverIconFill;
			xamlMember.Setter = set_653_HomeView_DiscoverIconFill;
			break;
		case "Vine.Views.HomeView.MeIconFill":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "MeIconFill", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_654_HomeView_MeIconFill;
			xamlMember.Setter = set_654_HomeView_MeIconFill;
			break;
		case "Vine.Views.HomeView.VMsIconFill":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "VMsIconFill", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_655_HomeView_VMsIconFill;
			xamlMember.Setter = set_655_HomeView_VMsIconFill;
			break;
		case "Vine.Views.HomeView.VideoAppBarIcon":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "VideoAppBarIcon", "String");
			xamlMember.Getter = get_656_HomeView_VideoAppBarIcon;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.HomeView.AppBarBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "AppBarBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_657_HomeView_AppBarBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.HomeView.ControlWrapper":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "ControlWrapper", "Vine.Models.UserControlWrapper");
			xamlMember.Getter = get_658_HomeView_ControlWrapper;
			xamlMember.Setter = set_658_HomeView_ControlWrapper;
			break;
		case "Vine.Views.HomeView.UploadJobs":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "UploadJobs", "Vine.Models.UploadJobsViewModel");
			xamlMember.Getter = get_659_HomeView_UploadJobs;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.HomeView.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_660_HomeView_User;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.HomeView.MuteIcon":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "MuteIcon", "Windows.UI.Xaml.Controls.IconElement");
			xamlMember.Getter = get_661_HomeView_MuteIcon;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.HomeView.MuteLabel":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "MuteLabel", "String");
			xamlMember.Getter = get_662_HomeView_MuteLabel;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.HomeView.TutorialWelcomeVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "TutorialWelcomeVisibility", "Boolean");
			xamlMember.Getter = get_663_HomeView_TutorialWelcomeVisibility;
			xamlMember.Setter = set_663_HomeView_TutorialWelcomeVisibility;
			break;
		case "Vine.Views.HomeView.IsRedOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "IsRedOn", "Boolean");
			xamlMember.Getter = get_664_HomeView_IsRedOn;
			xamlMember.Setter = set_664_HomeView_IsRedOn;
			break;
		case "Vine.Views.HomeView.IsOrangeOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "IsOrangeOn", "Boolean");
			xamlMember.Getter = get_665_HomeView_IsOrangeOn;
			xamlMember.Setter = set_665_HomeView_IsOrangeOn;
			break;
		case "Vine.Views.HomeView.IsYellowOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "IsYellowOn", "Boolean");
			xamlMember.Getter = get_666_HomeView_IsYellowOn;
			xamlMember.Setter = set_666_HomeView_IsYellowOn;
			break;
		case "Vine.Views.HomeView.IsGreenOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "IsGreenOn", "Boolean");
			xamlMember.Getter = get_667_HomeView_IsGreenOn;
			xamlMember.Setter = set_667_HomeView_IsGreenOn;
			break;
		case "Vine.Views.HomeView.IsTealOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "IsTealOn", "Boolean");
			xamlMember.Getter = get_668_HomeView_IsTealOn;
			xamlMember.Setter = set_668_HomeView_IsTealOn;
			break;
		case "Vine.Views.HomeView.IsBlueLightOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "IsBlueLightOn", "Boolean");
			xamlMember.Getter = get_669_HomeView_IsBlueLightOn;
			xamlMember.Setter = set_669_HomeView_IsBlueLightOn;
			break;
		case "Vine.Views.HomeView.IsBlueDarkOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "IsBlueDarkOn", "Boolean");
			xamlMember.Getter = get_670_HomeView_IsBlueDarkOn;
			xamlMember.Setter = set_670_HomeView_IsBlueDarkOn;
			break;
		case "Vine.Views.HomeView.IsPurpleOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "IsPurpleOn", "Boolean");
			xamlMember.Getter = get_671_HomeView_IsPurpleOn;
			xamlMember.Setter = set_671_HomeView_IsPurpleOn;
			break;
		case "Vine.Views.HomeView.IsPinkOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.HomeView");
			xamlMember = new XamlMember(this, "IsPinkOn", "Boolean");
			xamlMember.Getter = get_672_HomeView_IsPinkOn;
			xamlMember.Setter = set_672_HomeView_IsPinkOn;
			break;
		case "Vine.Views.TemplateSelectors.InteractionTemplateSelector.HeaderTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.InteractionTemplateSelector");
			xamlMember = new XamlMember(this, "HeaderTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_673_InteractionTemplateSelector_HeaderTemplate;
			xamlMember.Setter = set_673_InteractionTemplateSelector_HeaderTemplate;
			break;
		case "Vine.Views.TemplateSelectors.InteractionTemplateSelector.FollowRequestTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.InteractionTemplateSelector");
			xamlMember = new XamlMember(this, "FollowRequestTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_674_InteractionTemplateSelector_FollowRequestTemplate;
			xamlMember.Setter = set_674_InteractionTemplateSelector_FollowRequestTemplate;
			break;
		case "Vine.Views.TemplateSelectors.InteractionTemplateSelector.NotificationTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.InteractionTemplateSelector");
			xamlMember = new XamlMember(this, "NotificationTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_675_InteractionTemplateSelector_NotificationTemplate;
			xamlMember.Setter = set_675_InteractionTemplateSelector_NotificationTemplate;
			break;
		case "Vine.Views.TemplateSelectors.InteractionTemplateSelector.MilestoneTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.InteractionTemplateSelector");
			xamlMember = new XamlMember(this, "MilestoneTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_676_InteractionTemplateSelector_MilestoneTemplate;
			xamlMember.Setter = set_676_InteractionTemplateSelector_MilestoneTemplate;
			break;
		case "Vine.Views.TemplateSelectors.InteractionTemplateSelector.GroupedCountTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.InteractionTemplateSelector");
			xamlMember = new XamlMember(this, "GroupedCountTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_677_InteractionTemplateSelector_GroupedCountTemplate;
			xamlMember.Setter = set_677_InteractionTemplateSelector_GroupedCountTemplate;
			break;
		case "Vine.Views.LoginEmailView.Username":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.LoginEmailView");
			xamlMember = new XamlMember(this, "Username", "String");
			xamlMember.Getter = get_678_LoginEmailView_Username;
			xamlMember.Setter = set_678_LoginEmailView_Username;
			break;
		case "Vine.Views.LoginEmailView.Password":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.LoginEmailView");
			xamlMember = new XamlMember(this, "Password", "String");
			xamlMember.Getter = get_679_LoginEmailView_Password;
			xamlMember.Setter = set_679_LoginEmailView_Password;
			break;
		case "Vine.Views.LoginEmailView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.LoginEmailView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_680_LoginEmailView_IsBusy;
			xamlMember.Setter = set_680_LoginEmailView_IsBusy;
			break;
		case "Vine.Views.LoginEmailView.IsNotBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.LoginEmailView");
			xamlMember = new XamlMember(this, "IsNotBusy", "Boolean");
			xamlMember.Getter = get_681_LoginEmailView_IsNotBusy;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.LoginTwitterView.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.LoginTwitterView");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.Getter = get_682_LoginTwitterView_ErrorText;
			xamlMember.Setter = set_682_LoginTwitterView_ErrorText;
			break;
		case "Vine.Views.LoginTwitterView.IsError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.LoginTwitterView");
			xamlMember = new XamlMember(this, "IsError", "Boolean");
			xamlMember.Getter = get_683_LoginTwitterView_IsError;
			xamlMember.Setter = set_683_LoginTwitterView_IsError;
			break;
		case "Vine.Views.LoginTwitterView.Username":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.LoginTwitterView");
			xamlMember = new XamlMember(this, "Username", "String");
			xamlMember.Getter = get_684_LoginTwitterView_Username;
			xamlMember.Setter = set_684_LoginTwitterView_Username;
			break;
		case "Vine.Views.LoginTwitterView.Password":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.LoginTwitterView");
			xamlMember = new XamlMember(this, "Password", "String");
			xamlMember.Getter = get_685_LoginTwitterView_Password;
			xamlMember.Setter = set_685_LoginTwitterView_Password;
			break;
		case "Vine.Views.LoginTwitterView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.LoginTwitterView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_686_LoginTwitterView_IsBusy;
			xamlMember.Setter = set_686_LoginTwitterView_IsBusy;
			break;
		case "Vine.Views.LoginTwitterView.IsNotBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.LoginTwitterView");
			xamlMember = new XamlMember(this, "IsNotBusy", "Boolean");
			xamlMember.Getter = get_687_LoginTwitterView_IsNotBusy;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Framework.ExtensionsForUi.RunList":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Framework.ExtensionsForUi");
			xamlMember = new XamlMember(this, "RunList", "System.Collections.Generic.List`1<Windows.UI.Xaml.Documents.Run>");
			xamlMember.SetTargetTypeName("Windows.UI.Xaml.DependencyObject");
			xamlMember.SetIsAttachable();
			xamlMember.Getter = get_688_ExtensionsForUi_RunList;
			xamlMember.Setter = set_688_ExtensionsForUi_RunList;
			break;
		case "Vine.Framework.ExtensionsForUi.ExtraTag":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Framework.ExtensionsForUi");
			xamlMember = new XamlMember(this, "ExtraTag", "Windows.UI.Xaml.DependencyObject");
			xamlMember.SetTargetTypeName("Windows.UI.Xaml.DependencyObject");
			xamlMember.SetIsAttachable();
			xamlMember.Getter = get_689_ExtensionsForUi_ExtraTag;
			xamlMember.Setter = set_689_ExtensionsForUi_ExtraTag;
			break;
		case "Vine.Framework.ExtensionsForUi.MediaStream":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Framework.ExtensionsForUi");
			xamlMember = new XamlMember(this, "MediaStream", "System.IO.Stream");
			xamlMember.SetTargetTypeName("Windows.UI.Xaml.DependencyObject");
			xamlMember.SetIsAttachable();
			xamlMember.Getter = get_690_ExtensionsForUi_MediaStream;
			xamlMember.Setter = set_690_ExtensionsForUi_MediaStream;
			break;
		case "Vine.Views.ProfileView.MuteIcon":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileView");
			xamlMember = new XamlMember(this, "MuteIcon", "Windows.UI.Xaml.Controls.IconElement");
			xamlMember.Getter = get_691_ProfileView_MuteIcon;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ProfileView.MuteLabel":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileView");
			xamlMember = new XamlMember(this, "MuteLabel", "String");
			xamlMember.Getter = get_692_ProfileView_MuteLabel;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ProfileView.AppBarButtonVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileView");
			xamlMember = new XamlMember(this, "AppBarButtonVisibility", "Boolean");
			xamlMember.Getter = get_693_ProfileView_AppBarButtonVisibility;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ProfileView.BlockedText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ProfileView");
			xamlMember = new XamlMember(this, "BlockedText", "String");
			xamlMember.Getter = get_694_ProfileView_BlockedText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.FacebookView.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FacebookView");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.Getter = get_695_FacebookView_HasError;
			xamlMember.Setter = set_695_FacebookView_HasError;
			break;
		case "Vine.Views.FacebookView.IsLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FacebookView");
			xamlMember = new XamlMember(this, "IsLoading", "Boolean");
			xamlMember.Getter = get_696_FacebookView_IsLoading;
			xamlMember.Setter = set_696_FacebookView_IsLoading;
			break;
		case "Vine.Views.FacebookView.QueryParameters":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.FacebookView");
			xamlMember = new XamlMember(this, "QueryParameters", "System.Collections.Generic.Dictionary`2<String, String>");
			xamlMember.SetTargetTypeName("System.Uri");
			xamlMember.SetIsAttachable();
			xamlMember.Getter = get_697_FacebookView_QueryParameters;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.AbsolutePath":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "AbsolutePath", "String");
			xamlMember.Getter = get_698_Uri_AbsolutePath;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.AbsoluteUri":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "AbsoluteUri", "String");
			xamlMember.Getter = get_699_Uri_AbsoluteUri;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.Authority":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "Authority", "String");
			xamlMember.Getter = get_700_Uri_Authority;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.DnsSafeHost":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "DnsSafeHost", "String");
			xamlMember.Getter = get_701_Uri_DnsSafeHost;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.Fragment":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "Fragment", "String");
			xamlMember.Getter = get_702_Uri_Fragment;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.Host":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "Host", "String");
			xamlMember.Getter = get_703_Uri_Host;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.HostNameType":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "HostNameType", "System.UriHostNameType");
			xamlMember.Getter = get_704_Uri_HostNameType;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.IsAbsoluteUri":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "IsAbsoluteUri", "Boolean");
			xamlMember.Getter = get_705_Uri_IsAbsoluteUri;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.IsDefaultPort":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "IsDefaultPort", "Boolean");
			xamlMember.Getter = get_706_Uri_IsDefaultPort;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.IsFile":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "IsFile", "Boolean");
			xamlMember.Getter = get_707_Uri_IsFile;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.IsLoopback":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "IsLoopback", "Boolean");
			xamlMember.Getter = get_708_Uri_IsLoopback;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.IsUnc":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "IsUnc", "Boolean");
			xamlMember.Getter = get_709_Uri_IsUnc;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.LocalPath":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "LocalPath", "String");
			xamlMember.Getter = get_710_Uri_LocalPath;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.OriginalString":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "OriginalString", "String");
			xamlMember.Getter = get_711_Uri_OriginalString;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.PathAndQuery":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "PathAndQuery", "String");
			xamlMember.Getter = get_712_Uri_PathAndQuery;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.Port":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "Port", "Int32");
			xamlMember.Getter = get_713_Uri_Port;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.Query":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "Query", "String");
			xamlMember.Getter = get_714_Uri_Query;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.Scheme":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "Scheme", "String");
			xamlMember.Getter = get_715_Uri_Scheme;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.Segments":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "Segments", "String[]");
			xamlMember.Getter = get_716_Uri_Segments;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.UserEscaped":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "UserEscaped", "Boolean");
			xamlMember.Getter = get_717_Uri_UserEscaped;
			xamlMember.SetIsReadOnly();
			break;
		case "System.Uri.UserInfo":
			_ = (XamlUserType)(object)GetXamlTypeByName("System.Uri");
			xamlMember = new XamlMember(this, "UserInfo", "String");
			xamlMember.Getter = get_718_Uri_UserInfo;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SettingsContentView.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsContentView");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_719_SettingsContentView_User;
			xamlMember.Setter = set_719_SettingsContentView_User;
			break;
		case "Vine.Views.SettingsContentView.IsError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsContentView");
			xamlMember = new XamlMember(this, "IsError", "Boolean");
			xamlMember.Getter = get_720_SettingsContentView_IsError;
			xamlMember.Setter = set_720_SettingsContentView_IsError;
			break;
		case "Vine.Views.SettingsContentView.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsContentView");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.Getter = get_721_SettingsContentView_ErrorText;
			xamlMember.Setter = set_721_SettingsContentView_ErrorText;
			break;
		case "Vine.Views.SettingsContentView.ShowRetry":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsContentView");
			xamlMember = new XamlMember(this, "ShowRetry", "Boolean");
			xamlMember.Getter = get_722_SettingsContentView_ShowRetry;
			xamlMember.Setter = set_722_SettingsContentView_ShowRetry;
			break;
		case "Vine.Views.SettingsContentView.IsLoaded":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsContentView");
			xamlMember = new XamlMember(this, "IsLoaded", "Boolean");
			xamlMember.Getter = get_723_SettingsContentView_IsLoaded;
			xamlMember.Setter = set_723_SettingsContentView_IsLoaded;
			break;
		case "Vine.Views.SettingsContentView.AllowAddressBookButtonState":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsContentView");
			xamlMember = new XamlMember(this, "AllowAddressBookButtonState", "Vine.Views.VineToggleButtonState");
			xamlMember.Getter = get_724_SettingsContentView_AllowAddressBookButtonState;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SettingsView.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_725_SettingsView_User;
			xamlMember.Setter = set_725_SettingsView_User;
			break;
		case "Vine.Views.SettingsView.IsRedOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "IsRedOn", "Boolean");
			xamlMember.Getter = get_726_SettingsView_IsRedOn;
			xamlMember.Setter = set_726_SettingsView_IsRedOn;
			break;
		case "Vine.Views.SettingsView.IsOrangeOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "IsOrangeOn", "Boolean");
			xamlMember.Getter = get_727_SettingsView_IsOrangeOn;
			xamlMember.Setter = set_727_SettingsView_IsOrangeOn;
			break;
		case "Vine.Views.SettingsView.IsYellowOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "IsYellowOn", "Boolean");
			xamlMember.Getter = get_728_SettingsView_IsYellowOn;
			xamlMember.Setter = set_728_SettingsView_IsYellowOn;
			break;
		case "Vine.Views.SettingsView.IsGreenOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "IsGreenOn", "Boolean");
			xamlMember.Getter = get_729_SettingsView_IsGreenOn;
			xamlMember.Setter = set_729_SettingsView_IsGreenOn;
			break;
		case "Vine.Views.SettingsView.IsTealOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "IsTealOn", "Boolean");
			xamlMember.Getter = get_730_SettingsView_IsTealOn;
			xamlMember.Setter = set_730_SettingsView_IsTealOn;
			break;
		case "Vine.Views.SettingsView.IsBlueLightOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "IsBlueLightOn", "Boolean");
			xamlMember.Getter = get_731_SettingsView_IsBlueLightOn;
			xamlMember.Setter = set_731_SettingsView_IsBlueLightOn;
			break;
		case "Vine.Views.SettingsView.IsBlueDarkOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "IsBlueDarkOn", "Boolean");
			xamlMember.Getter = get_732_SettingsView_IsBlueDarkOn;
			xamlMember.Setter = set_732_SettingsView_IsBlueDarkOn;
			break;
		case "Vine.Views.SettingsView.IsPurpleOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "IsPurpleOn", "Boolean");
			xamlMember.Getter = get_733_SettingsView_IsPurpleOn;
			xamlMember.Setter = set_733_SettingsView_IsPurpleOn;
			break;
		case "Vine.Views.SettingsView.IsPinkOn":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "IsPinkOn", "Boolean");
			xamlMember.Getter = get_734_SettingsView_IsPinkOn;
			xamlMember.Setter = set_734_SettingsView_IsPinkOn;
			break;
		case "Vine.Views.SettingsView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_735_SettingsView_IsBusy;
			xamlMember.Setter = set_735_SettingsView_IsBusy;
			break;
		case "Vine.Views.SettingsView.IsError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "IsError", "Boolean");
			xamlMember.Getter = get_736_SettingsView_IsError;
			xamlMember.Setter = set_736_SettingsView_IsError;
			break;
		case "Vine.Views.SettingsView.IsErrorOrBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "IsErrorOrBusy", "Boolean");
			xamlMember.Getter = get_737_SettingsView_IsErrorOrBusy;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SettingsView.ErrorText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "ErrorText", "String");
			xamlMember.Getter = get_738_SettingsView_ErrorText;
			xamlMember.Setter = set_738_SettingsView_ErrorText;
			break;
		case "Vine.Views.SettingsView.ShowRetry":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "ShowRetry", "Boolean");
			xamlMember.Getter = get_739_SettingsView_ShowRetry;
			xamlMember.Setter = set_739_SettingsView_ShowRetry;
			break;
		case "Vine.Views.SettingsView.EmailVerifiedImage":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "EmailVerifiedImage", "String");
			xamlMember.Getter = get_740_SettingsView_EmailVerifiedImage;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SettingsView.PhoneVerifiedImage":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "PhoneVerifiedImage", "String");
			xamlMember.Getter = get_741_SettingsView_PhoneVerifiedImage;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SettingsView.EmailVerifiedFill":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "EmailVerifiedFill", "String");
			xamlMember.Getter = get_742_SettingsView_EmailVerifiedFill;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SettingsView.PhoneVerifiedFill":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "PhoneVerifiedFill", "String");
			xamlMember.Getter = get_743_SettingsView_PhoneVerifiedFill;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SettingsView.TwitterConnectedText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "TwitterConnectedText", "String");
			xamlMember.Getter = get_744_SettingsView_TwitterConnectedText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SettingsView.FacebookConnectedText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "FacebookConnectedText", "String");
			xamlMember.Getter = get_745_SettingsView_FacebookConnectedText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SettingsView.Version":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "Version", "String");
			xamlMember.Getter = get_746_SettingsView_Version;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SettingsView.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SettingsView");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_747_SettingsView_IsFinishedLoading;
			xamlMember.Setter = set_747_SettingsView_IsFinishedLoading;
			break;
		case "Vine.Views.SingleVineView.Params":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SingleVineView");
			xamlMember = new XamlMember(this, "Params", "Vine.Models.SingleVineViewParams");
			xamlMember.Getter = get_748_SingleVineView_Params;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SingleVineView.Section":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SingleVineView");
			xamlMember = new XamlMember(this, "Section", "Vine.Models.Analytics.Section");
			xamlMember.Getter = get_749_SingleVineView_Section;
			xamlMember.Setter = set_749_SingleVineView_Section;
			break;
		case "Vine.Views.SingleVineView.MuteIcon":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SingleVineView");
			xamlMember = new XamlMember(this, "MuteIcon", "Windows.UI.Xaml.Controls.IconElement");
			xamlMember.Getter = get_750_SingleVineView_MuteIcon;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.SingleVineView.MuteLabel":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.SingleVineView");
			xamlMember = new XamlMember(this, "MuteLabel", "String");
			xamlMember.Getter = get_751_SingleVineView_MuteLabel;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.TagVineListView.MuteIcon":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TagVineListView");
			xamlMember = new XamlMember(this, "MuteIcon", "Windows.UI.Xaml.Controls.IconElement");
			xamlMember.Getter = get_752_TagVineListView_MuteIcon;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.TagVineListView.MuteLabel":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TagVineListView");
			xamlMember = new XamlMember(this, "MuteLabel", "String");
			xamlMember.Getter = get_753_TagVineListView_MuteLabel;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.TagVineListView.PageTitle":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TagVineListView");
			xamlMember = new XamlMember(this, "PageTitle", "String");
			xamlMember.Getter = get_754_TagVineListView_PageTitle;
			xamlMember.Setter = set_754_TagVineListView_PageTitle;
			break;
		case "Vine.Views.TagVineListView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TagVineListView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_755_TagVineListView_IsBusy;
			xamlMember.Setter = set_755_TagVineListView_IsBusy;
			break;
		case "Vine.Views.TagVineListView.SearchTerm":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TagVineListView");
			xamlMember = new XamlMember(this, "SearchTerm", "String");
			xamlMember.Getter = get_756_TagVineListView_SearchTerm;
			xamlMember.Setter = set_756_TagVineListView_SearchTerm;
			break;
		case "Vine.Views.TagVineListView.CanPin":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TagVineListView");
			xamlMember = new XamlMember(this, "CanPin", "Boolean");
			xamlMember.Getter = get_757_TagVineListView_CanPin;
			xamlMember.Setter = set_757_TagVineListView_CanPin;
			break;
		case "Vine.Views.UploadJobsView.ViewModel":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.UploadJobsView");
			xamlMember = new XamlMember(this, "ViewModel", "Vine.Models.UploadJobsViewModel");
			xamlMember.Getter = get_758_UploadJobsView_ViewModel;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VerifyPhoneEditControl.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VerifyPhoneEditControl");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_759_VerifyPhoneEditControl_User;
			xamlMember.Setter = set_759_VerifyPhoneEditControl_User;
			break;
		case "Vine.Views.VerifyPhoneEditControl.IsNextEnabled":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VerifyPhoneEditControl");
			xamlMember = new XamlMember(this, "IsNextEnabled", "Boolean");
			xamlMember.SetIsDependencyProperty();
			xamlMember.Getter = get_760_VerifyPhoneEditControl_IsNextEnabled;
			xamlMember.Setter = set_760_VerifyPhoneEditControl_IsNextEnabled;
			break;
		case "Vine.Views.TemplateSelectors.MessageTemplateSelector.VideoUploadJobTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.MessageTemplateSelector");
			xamlMember = new XamlMember(this, "VideoUploadJobTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_761_MessageTemplateSelector_VideoUploadJobTemplate;
			xamlMember.Setter = set_761_MessageTemplateSelector_VideoUploadJobTemplate;
			break;
		case "Vine.Views.TemplateSelectors.MessageTemplateSelector.CurrentUserMessageTextTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.MessageTemplateSelector");
			xamlMember = new XamlMember(this, "CurrentUserMessageTextTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_762_MessageTemplateSelector_CurrentUserMessageTextTemplate;
			xamlMember.Setter = set_762_MessageTemplateSelector_CurrentUserMessageTextTemplate;
			break;
		case "Vine.Views.TemplateSelectors.MessageTemplateSelector.CurrentUserMessageVideoTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.MessageTemplateSelector");
			xamlMember = new XamlMember(this, "CurrentUserMessageVideoTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_763_MessageTemplateSelector_CurrentUserMessageVideoTemplate;
			xamlMember.Setter = set_763_MessageTemplateSelector_CurrentUserMessageVideoTemplate;
			break;
		case "Vine.Views.TemplateSelectors.MessageTemplateSelector.CurrentUserMessagePostTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.MessageTemplateSelector");
			xamlMember = new XamlMember(this, "CurrentUserMessagePostTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_764_MessageTemplateSelector_CurrentUserMessagePostTemplate;
			xamlMember.Setter = set_764_MessageTemplateSelector_CurrentUserMessagePostTemplate;
			break;
		case "Vine.Views.TemplateSelectors.MessageTemplateSelector.OtherUserMessagePostTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.MessageTemplateSelector");
			xamlMember = new XamlMember(this, "OtherUserMessagePostTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_765_MessageTemplateSelector_OtherUserMessagePostTemplate;
			xamlMember.Setter = set_765_MessageTemplateSelector_OtherUserMessagePostTemplate;
			break;
		case "Vine.Views.TemplateSelectors.MessageTemplateSelector.OtherUserMessageTextTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.MessageTemplateSelector");
			xamlMember = new XamlMember(this, "OtherUserMessageTextTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_766_MessageTemplateSelector_OtherUserMessageTextTemplate;
			xamlMember.Setter = set_766_MessageTemplateSelector_OtherUserMessageTextTemplate;
			break;
		case "Vine.Views.TemplateSelectors.MessageTemplateSelector.OtherUserMessageVideoTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.MessageTemplateSelector");
			xamlMember = new XamlMember(this, "OtherUserMessageVideoTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_767_MessageTemplateSelector_OtherUserMessageVideoTemplate;
			xamlMember.Setter = set_767_MessageTemplateSelector_OtherUserMessageVideoTemplate;
			break;
		case "Vine.Views.VineMessagesThreadView.Items":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "Items", "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.VineMessageViewModel>");
			xamlMember.Getter = get_768_VineMessagesThreadView_Items;
			xamlMember.Setter = set_768_VineMessagesThreadView_Items;
			break;
		case "Vine.Models.VineMessageViewModel.YouShareText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "YouShareText", "System.Collections.Generic.List`1<Windows.UI.Xaml.Documents.Run>");
			xamlMember.Getter = get_769_VineMessageViewModel_YouShareText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineMessageViewModel.ShareText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "ShareText", "System.Collections.Generic.List`1<Windows.UI.Xaml.Documents.Run>");
			xamlMember.Getter = get_770_VineMessageViewModel_ShareText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineMessageViewModel.Model":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "Model", "Vine.Models.VineMessageModel");
			xamlMember.Getter = get_771_VineMessageViewModel_Model;
			xamlMember.Setter = set_771_VineMessageViewModel_Model;
			break;
		case "Vine.Models.VineMessageViewModel.User":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "User", "Vine.Models.VineUserModel");
			xamlMember.Getter = get_772_VineMessageViewModel_User;
			xamlMember.Setter = set_772_VineMessageViewModel_User;
			break;
		case "Vine.Models.VineMessageViewModel.ShowCreatedDisplay":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "ShowCreatedDisplay", "Boolean");
			xamlMember.Getter = get_773_VineMessageViewModel_ShowCreatedDisplay;
			xamlMember.Setter = set_773_VineMessageViewModel_ShowCreatedDisplay;
			break;
		case "Vine.Models.VineMessageViewModel.CreatedDisplay":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "CreatedDisplay", "String");
			xamlMember.Getter = get_774_VineMessageViewModel_CreatedDisplay;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineMessageViewModel.RequiresVerification":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "RequiresVerification", "Boolean");
			xamlMember.Getter = get_775_VineMessageViewModel_RequiresVerification;
			xamlMember.Setter = set_775_VineMessageViewModel_RequiresVerification;
			break;
		case "Vine.Models.VineMessageViewModel.ErrorMessage":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "ErrorMessage", "String");
			xamlMember.Getter = get_776_VineMessageViewModel_ErrorMessage;
			xamlMember.Setter = set_776_VineMessageViewModel_ErrorMessage;
			break;
		case "Vine.Models.VineMessageViewModel.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.Getter = get_777_VineMessageViewModel_HasError;
			xamlMember.Setter = set_777_VineMessageViewModel_HasError;
			break;
		case "Vine.Models.VineMessageViewModel.UserBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "UserBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_778_VineMessageViewModel_UserBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineMessageViewModel.LightBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "LightBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_779_VineMessageViewModel_LightBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineMessageViewModel.IsPlaying":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "IsPlaying", "Boolean");
			xamlMember.Getter = get_780_VineMessageViewModel_IsPlaying;
			xamlMember.Setter = set_780_VineMessageViewModel_IsPlaying;
			break;
		case "Vine.Models.VineMessageViewModel.IsLoadingVideo":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "IsLoadingVideo", "Boolean");
			xamlMember.Getter = get_781_VineMessageViewModel_IsLoadingVideo;
			xamlMember.Setter = set_781_VineMessageViewModel_IsLoadingVideo;
			break;
		case "Vine.Models.VineMessageViewModel.IsFinishedBuffering":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "IsFinishedBuffering", "Boolean");
			xamlMember.Getter = get_782_VineMessageViewModel_IsFinishedBuffering;
			xamlMember.Setter = set_782_VineMessageViewModel_IsFinishedBuffering;
			break;
		case "Vine.Models.VineMessageViewModel.PlayingVideoUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "PlayingVideoUrl", "String");
			xamlMember.Getter = get_783_VineMessageViewModel_PlayingVideoUrl;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineMessageViewModel.PostDescription":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "PostDescription", "String");
			xamlMember.Getter = get_784_VineMessageViewModel_PostDescription;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineMessageViewModel.IsPostDeleted":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "IsPostDeleted", "Boolean");
			xamlMember.Getter = get_785_VineMessageViewModel_IsPostDeleted;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineMessageViewModel.ThumbnailUrlAuth":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "ThumbnailUrlAuth", "String");
			xamlMember.Getter = get_786_VineMessageViewModel_ThumbnailUrlAuth;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Models.VineMessageViewModel.ThumbVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Models.VineMessageViewModel");
			xamlMember = new XamlMember(this, "ThumbVisibility", "Windows.UI.Xaml.Visibility");
			xamlMember.Getter = get_787_VineMessageViewModel_ThumbVisibility;
			xamlMember.Setter = set_787_VineMessageViewModel_ThumbVisibility;
			break;
		case "Vine.Views.VineMessagesThreadView.ViewModel":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "ViewModel", "Vine.Models.ConversationViewModel");
			xamlMember.Getter = get_788_VineMessagesThreadView_ViewModel;
			xamlMember.Setter = set_788_VineMessagesThreadView_ViewModel;
			break;
		case "Vine.Views.VineMessagesThreadView.ConversationId":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "ConversationId", "String");
			xamlMember.Getter = get_789_VineMessagesThreadView_ConversationId;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineMessagesThreadView.IgnoreLabel":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "IgnoreLabel", "String");
			xamlMember.Getter = get_790_VineMessagesThreadView_IgnoreLabel;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineMessagesThreadView.UserProfileLabel":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "UserProfileLabel", "String");
			xamlMember.Getter = get_791_VineMessagesThreadView_UserProfileLabel;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineMessagesThreadView.CurrentUserBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "CurrentUserBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_792_VineMessagesThreadView_CurrentUserBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineMessagesThreadView.CurrentUserLightBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "CurrentUserLightBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_793_VineMessagesThreadView_CurrentUserLightBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineMessagesThreadView.OtherUserBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "OtherUserBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_794_VineMessagesThreadView_OtherUserBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineMessagesThreadView.OtherUserLightBrush":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "OtherUserLightBrush", "Windows.UI.Xaml.Media.Brush");
			xamlMember.Getter = get_795_VineMessagesThreadView_OtherUserLightBrush;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineMessagesThreadView.OtherUsername":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "OtherUsername", "String");
			xamlMember.Getter = get_796_VineMessagesThreadView_OtherUsername;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineMessagesThreadView.NewComment":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "NewComment", "String");
			xamlMember.Getter = get_797_VineMessagesThreadView_NewComment;
			xamlMember.Setter = set_797_VineMessagesThreadView_NewComment;
			break;
		case "Vine.Views.VineMessagesThreadView.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.Getter = get_798_VineMessagesThreadView_HasError;
			xamlMember.Setter = set_798_VineMessagesThreadView_HasError;
			break;
		case "Vine.Views.VineMessagesThreadView.IsEmpty":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "IsEmpty", "Boolean");
			xamlMember.Getter = get_799_VineMessagesThreadView_IsEmpty;
			xamlMember.Setter = set_799_VineMessagesThreadView_IsEmpty;
			break;
		case "Vine.Views.VineMessagesThreadView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_800_VineMessagesThreadView_IsBusy;
			xamlMember.Setter = set_800_VineMessagesThreadView_IsBusy;
			break;
		case "Vine.Views.VineMessagesThreadView.TutorialHintVisibility":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "TutorialHintVisibility", "Boolean");
			xamlMember.Getter = get_801_VineMessagesThreadView_TutorialHintVisibility;
			xamlMember.Setter = set_801_VineMessagesThreadView_TutorialHintVisibility;
			break;
		case "Vine.Views.VineMessagesThreadView.IsKeyboardVisible":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "IsKeyboardVisible", "Boolean");
			xamlMember.Getter = get_802_VineMessagesThreadView_IsKeyboardVisible;
			xamlMember.Setter = set_802_VineMessagesThreadView_IsKeyboardVisible;
			break;
		case "Vine.Views.VineMessagesThreadView.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_803_VineMessagesThreadView_IsFinishedLoading;
			xamlMember.Setter = set_803_VineMessagesThreadView_IsFinishedLoading;
			break;
		case "Vine.Views.VineMessagesThreadView.SendEnabled":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "SendEnabled", "Boolean");
			xamlMember.Getter = get_804_VineMessagesThreadView_SendEnabled;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineMessagesThreadView.IsViewModelLoaded":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "IsViewModelLoaded", "Boolean");
			xamlMember.Getter = get_805_VineMessagesThreadView_IsViewModelLoaded;
			xamlMember.Setter = set_805_VineMessagesThreadView_IsViewModelLoaded;
			break;
		case "Vine.Views.VineMessagesThreadView.IsVineUser":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "IsVineUser", "Boolean");
			xamlMember.Getter = get_806_VineMessagesThreadView_IsVineUser;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineMessagesThreadView.IsVolumeMuted":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "IsVolumeMuted", "Boolean");
			xamlMember.Getter = get_807_VineMessagesThreadView_IsVolumeMuted;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineMessagesThreadView.MuteIcon":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "MuteIcon", "Windows.UI.Xaml.Controls.IconElement");
			xamlMember.Getter = get_808_VineMessagesThreadView_MuteIcon;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineMessagesThreadView.MuteLabel":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineMessagesThreadView");
			xamlMember = new XamlMember(this, "MuteLabel", "String");
			xamlMember.Getter = get_809_VineMessagesThreadView_MuteLabel;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.TemplateSelectors.VineListTemplateSelector.PostTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.VineListTemplateSelector");
			xamlMember = new XamlMember(this, "PostTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_810_VineListTemplateSelector_PostTemplate;
			xamlMember.Setter = set_810_VineListTemplateSelector_PostTemplate;
			break;
		case "Vine.Views.TemplateSelectors.VineListTemplateSelector.PostMosaicTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.VineListTemplateSelector");
			xamlMember = new XamlMember(this, "PostMosaicTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_811_VineListTemplateSelector_PostMosaicTemplate;
			xamlMember.Setter = set_811_VineListTemplateSelector_PostMosaicTemplate;
			break;
		case "Vine.Views.TemplateSelectors.VineListTemplateSelector.AvatarPostMosaicTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.VineListTemplateSelector");
			xamlMember = new XamlMember(this, "AvatarPostMosaicTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_812_VineListTemplateSelector_AvatarPostMosaicTemplate;
			xamlMember.Setter = set_812_VineListTemplateSelector_AvatarPostMosaicTemplate;
			break;
		case "Vine.Views.TemplateSelectors.VineListTemplateSelector.UrlActionTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.VineListTemplateSelector");
			xamlMember = new XamlMember(this, "UrlActionTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_813_VineListTemplateSelector_UrlActionTemplate;
			xamlMember.Setter = set_813_VineListTemplateSelector_UrlActionTemplate;
			break;
		case "Vine.Views.TemplateSelectors.VineListTemplateSelector.UserMosaicTemplate":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.TemplateSelectors.VineListTemplateSelector");
			xamlMember = new XamlMember(this, "UserMosaicTemplate", "Windows.UI.Xaml.DataTemplate");
			xamlMember.Getter = get_814_VineListTemplateSelector_UserMosaicTemplate;
			xamlMember.Setter = set_814_VineListTemplateSelector_UserMosaicTemplate;
			break;
		case "Vine.Views.VinePressedButton.ReleasedUI":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VinePressedButton");
			xamlMember = new XamlMember(this, "ReleasedUI", "Windows.UI.Xaml.FrameworkElement");
			xamlMember.Getter = get_815_VinePressedButton_ReleasedUI;
			xamlMember.Setter = set_815_VinePressedButton_ReleasedUI;
			break;
		case "Vine.Views.VinePressedButton.PressedUI":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VinePressedButton");
			xamlMember = new XamlMember(this, "PressedUI", "Windows.UI.Xaml.FrameworkElement");
			xamlMember.Getter = get_816_VinePressedButton_PressedUI;
			xamlMember.Setter = set_816_VinePressedButton_PressedUI;
			break;
		case "Vine.Views.ResetPasswordView.ExampleEmail":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ResetPasswordView");
			xamlMember = new XamlMember(this, "ExampleEmail", "String");
			xamlMember.Getter = get_817_ResetPasswordView_ExampleEmail;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ResetPasswordView.ResetPasswordText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ResetPasswordView");
			xamlMember = new XamlMember(this, "ResetPasswordText", "String");
			xamlMember.Getter = get_818_ResetPasswordView_ResetPasswordText;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.ResetPasswordView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ResetPasswordView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_819_ResetPasswordView_IsBusy;
			xamlMember.Setter = set_819_ResetPasswordView_IsBusy;
			break;
		case "Vine.Views.ResetPasswordView.Email":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ResetPasswordView");
			xamlMember = new XamlMember(this, "Email", "String");
			xamlMember.Getter = get_820_ResetPasswordView_Email;
			xamlMember.Setter = set_820_ResetPasswordView_Email;
			break;
		case "Vine.Views.ResetPasswordView.Items":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.ResetPasswordView");
			xamlMember = new XamlMember(this, "Items", "System.Collections.ObjectModel.ObservableCollection`1<Vine.Models.VineUserModel>");
			xamlMember.Getter = get_821_ResetPasswordView_Items;
			xamlMember.Setter = set_821_ResetPasswordView_Items;
			break;
		case "Vine.Views.VineUserListView.HasError":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineUserListView");
			xamlMember = new XamlMember(this, "HasError", "Boolean");
			xamlMember.Getter = get_822_VineUserListView_HasError;
			xamlMember.Setter = set_822_VineUserListView_HasError;
			break;
		case "Vine.Views.VineUserListView.PageTitle":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineUserListView");
			xamlMember = new XamlMember(this, "PageTitle", "String");
			xamlMember.Getter = get_823_VineUserListView_PageTitle;
			xamlMember.Setter = set_823_VineUserListView_PageTitle;
			break;
		case "Vine.Views.VineUserListView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineUserListView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_824_VineUserListView_IsBusy;
			xamlMember.Setter = set_824_VineUserListView_IsBusy;
			break;
		case "Vine.Views.VineUserListView.EmptyText":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineUserListView");
			xamlMember = new XamlMember(this, "EmptyText", "String");
			xamlMember.Getter = get_825_VineUserListView_EmptyText;
			xamlMember.Setter = set_825_VineUserListView_EmptyText;
			break;
		case "Vine.Views.VineUserListView.Items":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineUserListView");
			xamlMember = new XamlMember(this, "Items", "Vine.Framework.IncrementalLoadingCollection`1<Vine.Models.VineUserModel>");
			xamlMember.Getter = get_826_VineUserListView_Items;
			xamlMember.Setter = set_826_VineUserListView_Items;
			break;
		case "Vine.Views.VineUserListView.Params":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineUserListView");
			xamlMember = new XamlMember(this, "Params", "Vine.Models.VineUserListViewParams");
			xamlMember.Getter = get_827_VineUserListView_Params;
			xamlMember.SetIsReadOnly();
			break;
		case "Vine.Views.VineUserListView.IsFinishedLoading":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineUserListView");
			xamlMember = new XamlMember(this, "IsFinishedLoading", "Boolean");
			xamlMember.Getter = get_828_VineUserListView_IsFinishedLoading;
			xamlMember.Setter = set_828_VineUserListView_IsFinishedLoading;
			break;
		case "Vine.Views.VineUserListView.IsEmpty":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineUserListView");
			xamlMember = new XamlMember(this, "IsEmpty", "Boolean");
			xamlMember.Getter = get_829_VineUserListView_IsEmpty;
			xamlMember.Setter = set_829_VineUserListView_IsEmpty;
			break;
		case "Vine.Views.VineUserListView.ShowLoopCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.VineUserListView");
			xamlMember = new XamlMember(this, "ShowLoopCount", "Boolean");
			xamlMember.Getter = get_830_VineUserListView_ShowLoopCount;
			xamlMember.Setter = set_830_VineUserListView_ShowLoopCount;
			break;
		case "Vine.Views.WebView.WebUrl":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.WebView");
			xamlMember = new XamlMember(this, "WebUrl", "String");
			xamlMember.Getter = get_831_WebView_WebUrl;
			xamlMember.Setter = set_831_WebView_WebUrl;
			break;
		case "Vine.Views.WebView.IsBusy":
			_ = (XamlUserType)(object)GetXamlTypeByName("Vine.Views.WebView");
			xamlMember = new XamlMember(this, "IsBusy", "Boolean");
			xamlMember.Getter = get_832_WebView_IsBusy;
			xamlMember.Setter = set_832_WebView_IsBusy;
			break;
		case "VideoEdit.MediaFile.VideoIndex":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "VideoIndex", "Int32");
			xamlMember.Getter = get_833_MediaFile_VideoIndex;
			xamlMember.Setter = set_833_MediaFile_VideoIndex;
			break;
		case "VideoEdit.MediaFile.AudioIndex":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "AudioIndex", "Int32");
			xamlMember.Getter = get_834_MediaFile_AudioIndex;
			xamlMember.Setter = set_834_MediaFile_AudioIndex;
			break;
		case "VideoEdit.MediaFile.VideoStreamCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "VideoStreamCount", "Int32");
			xamlMember.Getter = get_835_MediaFile_VideoStreamCount;
			xamlMember.Setter = set_835_MediaFile_VideoStreamCount;
			break;
		case "VideoEdit.MediaFile.AudioStreamCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "AudioStreamCount", "Int32");
			xamlMember.Getter = get_836_MediaFile_AudioStreamCount;
			xamlMember.Setter = set_836_MediaFile_AudioStreamCount;
			break;
		case "VideoEdit.MediaFile.StreamCount":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "StreamCount", "UInt32");
			xamlMember.Getter = get_837_MediaFile_StreamCount;
			xamlMember.Setter = set_837_MediaFile_StreamCount;
			break;
		case "VideoEdit.MediaFile.bEOF":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "bEOF", "Boolean");
			xamlMember.Getter = get_838_MediaFile_bEOF;
			xamlMember.Setter = set_838_MediaFile_bEOF;
			break;
		case "VideoEdit.MediaFile.AudioStream":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "AudioStream", "VideoEdit.StreamInfo");
			xamlMember.Getter = get_839_MediaFile_AudioStream;
			xamlMember.Setter = set_839_MediaFile_AudioStream;
			break;
		case "VideoEdit.MediaFile.VideoStream":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "VideoStream", "VideoEdit.StreamInfo");
			xamlMember.Getter = get_840_MediaFile_VideoStream;
			xamlMember.Setter = set_840_MediaFile_VideoStream;
			break;
		case "VideoEdit.MediaFile.HasAudio":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "HasAudio", "Boolean");
			xamlMember.Getter = get_841_MediaFile_HasAudio;
			xamlMember.Setter = set_841_MediaFile_HasAudio;
			break;
		case "VideoEdit.MediaFile.HasVideo":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "HasVideo", "Boolean");
			xamlMember.Getter = get_842_MediaFile_HasVideo;
			xamlMember.Setter = set_842_MediaFile_HasVideo;
			break;
		case "VideoEdit.MediaFile.AudioProperty":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "AudioProperty", "VideoEdit.AudioProp");
			xamlMember.Getter = get_843_MediaFile_AudioProperty;
			xamlMember.Setter = set_843_MediaFile_AudioProperty;
			break;
		case "VideoEdit.MediaFile.VideoProperty":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "VideoProperty", "VideoEdit.VideoProp");
			xamlMember.Getter = get_844_MediaFile_VideoProperty;
			xamlMember.Setter = set_844_MediaFile_VideoProperty;
			break;
		case "VideoEdit.MediaFile.BitmapImg":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "BitmapImg", "Windows.UI.Xaml.Media.Imaging.BitmapImage");
			xamlMember.Getter = get_845_MediaFile_BitmapImg;
			xamlMember.Setter = set_845_MediaFile_BitmapImg;
			break;
		case "VideoEdit.MediaFile.FileThumbnail":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "FileThumbnail", "Windows.Storage.FileProperties.StorageItemThumbnail");
			xamlMember.Getter = get_846_MediaFile_FileThumbnail;
			xamlMember.Setter = set_846_MediaFile_FileThumbnail;
			break;
		case "VideoEdit.MediaFile.File":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "File", "Windows.Storage.StorageFile");
			xamlMember.Getter = get_847_MediaFile_File;
			xamlMember.SetIsReadOnly();
			break;
		case "VideoEdit.MediaFile.Path":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "Path", "String");
			xamlMember.Getter = get_848_MediaFile_Path;
			xamlMember.SetIsReadOnly();
			break;
		case "VideoEdit.MediaFile.Name":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "Name", "String");
			xamlMember.Getter = get_849_MediaFile_Name;
			xamlMember.SetIsReadOnly();
			break;
		case "VideoEdit.MediaFile.ValidInfo":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "ValidInfo", "String");
			xamlMember.Getter = get_850_MediaFile_ValidInfo;
			xamlMember.Setter = set_850_MediaFile_ValidInfo;
			break;
		case "VideoEdit.MediaFile.Prepped":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "Prepped", "Boolean");
			xamlMember.Getter = get_851_MediaFile_Prepped;
			xamlMember.Setter = set_851_MediaFile_Prepped;
			break;
		case "VideoEdit.MediaFile.UseHardware":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "UseHardware", "Boolean");
			xamlMember.Getter = get_852_MediaFile_UseHardware;
			xamlMember.Setter = set_852_MediaFile_UseHardware;
			break;
		case "VideoEdit.MediaFile.EditedLength":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "EditedLength", "Int64");
			xamlMember.Getter = get_853_MediaFile_EditedLength;
			xamlMember.SetIsReadOnly();
			break;
		case "VideoEdit.MediaFile.OriginalLength":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "OriginalLength", "Int64");
			xamlMember.Getter = get_854_MediaFile_OriginalLength;
			xamlMember.Setter = set_854_MediaFile_OriginalLength;
			break;
		case "VideoEdit.MediaFile.EndOffset":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "EndOffset", "Int64");
			xamlMember.Getter = get_855_MediaFile_EndOffset;
			xamlMember.Setter = set_855_MediaFile_EndOffset;
			break;
		case "VideoEdit.MediaFile.StartOffset":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "StartOffset", "Int64");
			xamlMember.Getter = get_856_MediaFile_StartOffset;
			xamlMember.Setter = set_856_MediaFile_StartOffset;
			break;
		case "VideoEdit.MediaFile.VideoOnly":
			_ = (XamlUserType)(object)GetXamlTypeByName("VideoEdit.MediaFile");
			xamlMember = new XamlMember(this, "VideoOnly", "Boolean");
			xamlMember.Getter = get_857_MediaFile_VideoOnly;
			xamlMember.SetIsReadOnly();
			break;
		}
		return (IXamlMember)(object)xamlMember;
	}
}
