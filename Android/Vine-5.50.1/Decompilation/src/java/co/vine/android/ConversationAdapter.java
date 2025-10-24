package co.vine.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.CursorAdapter;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VinePost;
import co.vine.android.api.VinePrivateMessage;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppController;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.player.HasVideoPlayerAdapter;
import co.vine.android.player.OnListVideoClickListener;
import co.vine.android.player.SdkVideoView;
import co.vine.android.service.VineUploadService;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.MuteUtil;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.Util;
import co.vine.android.util.ViewUtil;
import co.vine.android.util.VineDateFormatter;
import co.vine.android.widget.ColoredSpan;
import co.vine.android.widget.EntityLinkClickableSpan;
import co.vine.android.widget.Typefaces;
import co.vine.android.widget.TypefacesSpan;
import co.vine.android.widget.VineClickableSpan;
import com.edisonwang.android.slog.SLog;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ConversationAdapter extends CursorAdapter implements View.OnClickListener, HasVideoPlayerAdapter {
    private final ConversationActivity mActivity;
    private final AppController mAppController;
    private final float mAvatarMargin;
    private final SparseArray<Bitmap> mAvatars;
    private ClickableSpanFactory mClickableSpanFactory;
    private int mCurrentPlaying;
    private final ColoredSpan mDarkGreySpan;
    private final Handler mHandler;
    private final HashMap<Long, Integer> mIdToPositionMap;
    private SdkVideoView mLastPlayer;
    private final ListView mListView;
    private final SparseArray<VinePrivateMessage> mMessages;
    private boolean mMuted;
    private final int mMyColor;
    private long mMyUserId;
    private final SparseArray<String> mPaths;
    private final Runnable mPlayCurrentPositionRunnable;
    private final SparseArray<VideoViewInterface> mPlayers;
    private final int mScreenWidth;
    private final float mSharedDescSize;
    private int mShouldBePlaying;
    private final float mSidePadding;
    private final SparseArray<Bitmap> mThumbnails;
    private final HashMap<VideoKey, Integer> mUrlReverse;
    private AttributeSet mVideoAttr;
    private final ArrayList<WeakReference<ConversationViewHolder>> mViewHolders;
    private final VineDateFormatter mVineDateFormatter;

    public ConversationAdapter(ConversationActivity activity, AppController appController, ListView listView, long myUserId, int flags) {
        super(activity, (Cursor) null, flags);
        this.mHandler = new Handler();
        this.mPaths = new SparseArray<>();
        this.mUrlReverse = new HashMap<>();
        this.mViewHolders = new ArrayList<>();
        this.mMessages = new SparseArray<>();
        this.mThumbnails = new SparseArray<>();
        this.mAvatars = new SparseArray<>();
        this.mPlayers = new SparseArray<>();
        this.mIdToPositionMap = new HashMap<>();
        this.mCurrentPlaying = Integer.MIN_VALUE;
        this.mShouldBePlaying = Integer.MIN_VALUE;
        this.mPlayCurrentPositionRunnable = new Runnable() { // from class: co.vine.android.ConversationAdapter.2
            @Override // java.lang.Runnable
            public synchronized void run() {
                VinePrivateMessage next;
                ListView listView2 = ConversationAdapter.this.mListView;
                int currentFirst = listView2.getFirstVisiblePosition();
                VinePrivateMessage msg = (VinePrivateMessage) ConversationAdapter.this.mMessages.get(currentFirst);
                boolean currentHasVideo = msg != null && msg.hasVideo;
                View currentView = ConversationAdapter.this.getViewAt(currentFirst);
                float ratio = -1.0f;
                if (currentView != null) {
                    int currentlyPlayingOffset = 0;
                    if (ConversationAdapter.this.mLastPlayer != null) {
                        currentlyPlayingOffset = ConversationAdapter.this.mLastPlayer.getHeight() / 2;
                    }
                    float bottom = currentView.getBottom() + currentlyPlayingOffset;
                    float height = currentView.getHeight() + currentlyPlayingOffset;
                    ratio = bottom / height;
                }
                SLog.d("Current First: {}, ratio: {}", Integer.valueOf(currentFirst), Float.valueOf(ratio));
                int currentNext = -1;
                if (ratio >= 0.5d && currentFirst != -1 && currentHasVideo) {
                    if (currentFirst != ConversationAdapter.this.mCurrentPlaying) {
                        ConversationAdapter.this.getVideoPathAndPlayForPosition(currentFirst, false);
                    } else {
                        ConversationAdapter.this.playFileAtPosition(currentFirst, false);
                    }
                } else {
                    int nextPlayer = currentFirst + 1;
                    while (true) {
                        if (nextPlayer >= ConversationAdapter.this.getCount() || (next = (VinePrivateMessage) ConversationAdapter.this.mMessages.get(nextPlayer)) == null) {
                            break;
                        }
                        if (!next.hasVideo) {
                            nextPlayer++;
                        } else {
                            currentNext = nextPlayer;
                            break;
                        }
                    }
                    SLog.d("Current Next: {}", Integer.valueOf(currentNext));
                    if (currentNext != -1 && currentNext != ConversationAdapter.this.mCurrentPlaying) {
                        ConversationAdapter.this.getVideoPathAndPlayForPosition(currentNext, false);
                    }
                }
            }
        };
        this.mMyUserId = myUserId;
        this.mActivity = activity;
        this.mAppController = appController;
        this.mListView = listView;
        this.mScreenWidth = SystemUtil.getDisplaySize(activity).x;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        this.mMyColor = prefs.getInt("profile_background", Settings.DEFAULT_PROFILE_COLOR);
        Resources res = activity.getResources();
        this.mSharedDescSize = res.getDimension(R.dimen.font_size_default);
        this.mSidePadding = res.getDimension(R.dimen.conversation_side_padding);
        this.mAvatarMargin = res.getDimension(R.dimen.conversation_avatar_size) + res.getDimension(R.dimen.spacing_small);
        this.mDarkGreySpan = new ColoredSpan(res.getColor(R.color.solid_dark_gray));
        this.mVineDateFormatter = new VineDateFormatter(res);
        this.mVineDateFormatter.refreshDates();
        this.mVineDateFormatter.addSpanForDateSection(new TypefacesSpan(null, Typefaces.get(this.mContext).getContentTypeface(1, 3)));
        this.mClickableSpanFactory = new ClickableSpanFactory(0);
    }

    @Override // android.support.v4.widget.CursorAdapter
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean isCurrentUser = ((long) cursor.getInt(3)) == this.mMyUserId;
        if (isCurrentUser) {
            v = layoutInflater.inflate(R.layout.message_row_view_me, parent, false);
        } else {
            v = layoutInflater.inflate(R.layout.message_row_view, parent, false);
        }
        ConversationViewHolder holder = new ConversationViewHolder(v);
        holder.videoContainer.setVisibility(0);
        holder.videoListener = new ConversationVideoClickedListener();
        ViewGroup.LayoutParams params = holder.videoContainer.getLayoutParams();
        params.height = (int) (this.mScreenWidth - (this.mSidePadding * 2.0f));
        params.width = (int) (this.mScreenWidth - (this.mSidePadding * 2.0f));
        holder.videoContainer.setLayoutParams(params);
        ViewGroup.LayoutParams params2 = holder.videoImage.getLayoutParams();
        params2.width = (int) (this.mScreenWidth - (this.mSidePadding * 2.0f));
        params2.height = (int) (this.mScreenWidth - (this.mSidePadding * 2.0f));
        holder.videoImage.setLayoutParams(params2);
        holder.messageContainerUserImage.bringToFront();
        v.setTag(holder);
        this.mViewHolders.add(new WeakReference<>(holder));
        return v;
    }

    private boolean shouldShowThumbnail(int position) {
        return (this.mCurrentPlaying == position && hasPlayerPlaying()) ? false : true;
    }

    private void setupUserImageProperties(int messagePosition, ImageView userImage, boolean isCurrentUser) {
        userImage.setVisibility(0);
        userImage.setTag(Integer.valueOf(messagePosition));
        userImage.setOnClickListener(this);
        RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) userImage.getLayoutParams();
        if (!isCurrentUser) {
            imageParams.addRule(11, 0);
            imageParams.addRule(9);
        } else {
            imageParams.addRule(9, 0);
            imageParams.addRule(11);
        }
        userImage.setLayoutParams(imageParams);
    }

    private void setupUserImageImage(VinePrivateMessage vpm, ConversationViewHolder holder, ImageView userImage, boolean isError) {
        if (isError) {
            userImage.setColorFilter((ColorFilter) null);
            userImage.setImageResource(R.drawable.ic_failed_delivery_light);
            return;
        }
        if (Util.isDefaultAvatarUrl(vpm.avatarUrl)) {
            Util.safeSetDefaultAvatar(userImage, Util.ProfileImageSize.MEDIUM, (-16777216) | holder.color);
            return;
        }
        userImage.setColorFilter((ColorFilter) null);
        ImageKey avatarKey = new ImageKey(vpm.avatarUrl, true);
        holder.avatarKey = avatarKey;
        Bitmap avatar = this.mAvatars.get(holder.position);
        if (avatar != null) {
            setImage(userImage, avatar, holder);
        } else {
            setImage(userImage, this.mAppController.getPhotoBitmap(avatarKey), holder);
        }
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int position) {
        Cursor c = (Cursor) getItem(position);
        return this.mMyUserId == c.getLong(3) ? 1 : 0;
    }

    private VinePrivateMessage initMessage(Cursor cursor) {
        int position = cursor.getPosition();
        VinePrivateMessage vpm = new VinePrivateMessage(cursor);
        this.mMessages.put(position, vpm);
        return vpm;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 2;
    }

    @Override // android.support.v4.widget.CursorAdapter
    public void bindView(View view, Context context, Cursor cursor) throws IllegalStateException, Resources.NotFoundException {
        int color;
        int position = cursor.getPosition();
        final VinePrivateMessage vpm = initMessage(cursor);
        Resources res = context.getResources();
        View.OnLongClickListener longClickListener = new View.OnLongClickListener() { // from class: co.vine.android.ConversationAdapter.1
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                ConversationAdapter.this.mActivity.showDeleteMessageDialog(vpm.messageId);
                return true;
            }
        };
        ConversationViewHolder holder = (ConversationViewHolder) view.getTag();
        view.setOnLongClickListener(longClickListener);
        holder.videoContainer.setOnLongClickListener(longClickListener);
        holder.avatarKey = null;
        holder.videoImageKey = null;
        holder.position = position;
        if (holder.userImageProgressNormal != null && holder.userImageProgressTop != null) {
            holder.userImageProgressTop.setVisibility(8);
            holder.userImageProgressNormal.setVisibility(8);
        }
        boolean isCurrentUser = this.mMyUserId == vpm.userId;
        holder.isCurrentUser = isCurrentUser;
        holder.videoListener.setPosition(position);
        invalidateTimestamp(holder, position, cursor, vpm);
        if (isCurrentUser) {
            color = this.mMyColor;
        } else {
            color = cursor.getInt(25);
        }
        if (color <= 0) {
            color = Settings.DEFAULT_PROFILE_COLOR;
        }
        if (color == Settings.DEFAULT_PROFILE_COLOR || color <= 0) {
            color = 16777215 & this.mContext.getResources().getColor(R.color.vine_green);
        }
        holder.color = color;
        boolean isSharedPost = vpm.postId > 0;
        holder.isSharedPost = isSharedPost;
        boolean deletedSharedPost = isSharedPost && (vpm.videoUrl == null || vpm.thumbnailUrl == null || vpm.post.username == null);
        boolean showVideoContainer = vpm.hasVideo || deletedSharedPost;
        String message = TextUtils.isEmpty(vpm.message) ? "" : vpm.message;
        if (deletedSharedPost) {
            holder.videoImage.setScaleType(ImageView.ScaleType.CENTER);
            holder.videoImage.setBackgroundColor(res.getColor(R.color.solid_light_gray));
        } else {
            holder.videoImage.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.videoImage.setBackgroundColor(res.getColor(android.R.color.transparent));
        }
        if (deletedSharedPost) {
            holder.videoContainer.setOnClickListener(null);
            holder.videoImage.setImageResource(R.drawable.background_sadface);
        } else {
            holder.videoContainer.setOnClickListener(holder.videoListener);
            holder.videoImage.setImageBitmap(null);
        }
        holder.messageContainer.setVisibility(0);
        holder.postMessageContainer.setVisibility(8);
        TextView messageContainerMessage = holder.messageContainerMessage;
        if (isSharedPost) {
            holder.messageContainer.setVisibility(8);
            holder.postMessageContainer.setVisibility(0);
            holder.topMessageContainer.setVisibility(0);
            setupUserImageProperties(position, holder.topMessageContainerUserImage, isCurrentUser);
            setupUserImageImage(vpm, holder, holder.topMessageContainerUserImage, vpm.errorCode != 0);
            holder.topMessageContainerMessage.setGravity((isCurrentUser ? 5 : 3) | 16);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.topMessageContainerMessage.getLayoutParams();
            params.rightMargin = isCurrentUser ? (int) this.mAvatarMargin : 0;
            params.leftMargin = isCurrentUser ? 0 : (int) this.mAvatarMargin;
            styleTopSharedDescription(vpm.post, isCurrentUser, color, holder.topMessageContainerMessage, deletedSharedPost);
            styleDescription(holder.postMessageContainerMessage, vpm.post, color, deletedSharedPost);
            holder.postMessageContainerMessage.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            holder.postMessageContainerMessage.setTextSize(0, this.mSharedDescSize);
            ViewUtil.setBackground(holder.postMessageContainerMessage, null);
        } else {
            messageContainerMessage.setText(message);
            setupUserImageProperties(position, holder.messageContainerUserImage, isCurrentUser);
            setupUserImageImage(vpm, holder, holder.messageContainerUserImage, vpm.errorCode != 0);
            holder.topMessageContainer.setVisibility(8);
            if (TextUtils.isEmpty(vpm.errorReason)) {
                if (holder.errorMessage != null) {
                    holder.errorMessage.setVisibility(8);
                }
                messageContainerMessage.setVisibility(0);
                messageContainerMessage.setTextColor((-16777216) | color);
                if (!TextUtils.isEmpty(message)) {
                    if (isCurrentUser) {
                        ViewUtil.fillColor(res, 855638016 | color, R.drawable.bubble, messageContainerMessage);
                        ViewUtil.fillColor(res, 855638016 | color, R.drawable.vm_nib_me, holder.nibsChatNib);
                    } else if (this.mMyColor != color) {
                        ViewUtil.fillColor(res, 855638016 | color, R.drawable.bubble, messageContainerMessage);
                        ViewUtil.fillColor(res, 855638016 | color, R.drawable.vm_nib_you, holder.nibsChatNib);
                    } else {
                        ViewUtil.fillColor(res, (-16777216) | color, R.drawable.bubble, messageContainerMessage);
                        ViewUtil.fillColor(res, (-16777216) | color, R.drawable.vm_nib_you, holder.nibsChatNib);
                        messageContainerMessage.setTextColor(-1);
                    }
                } else {
                    messageContainerMessage.setVisibility(8);
                }
            } else {
                if (TextUtils.isEmpty(message)) {
                    messageContainerMessage.setVisibility(8);
                } else {
                    int grayTextColor = res.getColor(R.color.solid_light_gray_vm);
                    int grayBubbleColor = res.getColor(R.color.solid_light_gray_bubble_vm);
                    messageContainerMessage.setVisibility(0);
                    messageContainerMessage.setTextColor(grayTextColor);
                    ViewUtil.fillColor(res, grayBubbleColor, R.drawable.bubble, messageContainerMessage);
                    ViewUtil.fillColor(res, grayBubbleColor, R.drawable.vm_nib_me, holder.nibsChatNib);
                }
                if (holder.errorMessage != null) {
                    holder.errorMessage.setVisibility(0);
                    holder.errorMessage.setText(vpm.errorReason);
                }
            }
        }
        String thumbnailUrl = deletedSharedPost ? null : vpm.thumbnailUrl;
        holder.hasVideoImage = thumbnailUrl != null;
        if (holder.hasVideoImage) {
            ImageKey videoImageKey = new ImageKey(thumbnailUrl);
            holder.videoImageKey = videoImageKey;
            Bitmap videoThumbnail = this.mThumbnails.get(position);
            if (videoThumbnail != null) {
                SLog.d("setting thumbnail for {}", Integer.valueOf(position));
                holder.isVideoImageLoaded = setImage(holder.videoImage, videoThumbnail, holder);
            } else {
                SLog.d("fetching thumbnail for {}", Integer.valueOf(position));
                Bitmap bitmap = this.mAppController.getPhotoBitmap(videoImageKey);
                if (bitmap != null) {
                    this.mThumbnails.put(position, bitmap);
                }
                holder.isVideoImageLoaded = setImage(holder.videoImage, bitmap, holder);
            }
            holder.loadImage.setVisibility(8);
        } else if (deletedSharedPost) {
            holder.loadImage.setVisibility(8);
        } else if (!isCurrentUser) {
            holder.loadImage.setVisibility(0);
        }
        if (showVideoContainer) {
            holder.videoContainer.setVisibility(0);
            holder.nibsChatNib.setVisibility(4);
            if (shouldShowThumbnail(position) && holder.videoView.getPlayingPosition() != position) {
                holder.videoView.setVisibility(4);
            }
            initVideoView(position, holder);
            holder.nibs.setVisibility(isSharedPost ? 8 : 0);
            holder.postNibs.setVisibility(isSharedPost ? 0 : 8);
        } else {
            holder.videoContainer.setVisibility(8);
            holder.nibsChatNib.setVisibility(0);
            holder.videoView.setTag(holder);
            this.mPlayers.remove(position);
        }
        if (cursor.getPosition() == cursor.getCount() - 1) {
            holder.bottomPaddingView.setVisibility(0);
        } else {
            holder.bottomPaddingView.setVisibility(8);
        }
        holder.videoView.setMute(this.mMuted);
    }

    private void invalidateTimestamp(ConversationViewHolder holder, int position, Cursor cursor, VinePrivateMessage vpm) {
        boolean shouldShow = false;
        if (position == 0) {
            shouldShow = true;
        } else {
            VinePrivateMessage previous = this.mMessages.get(position - 1);
            if (previous == null) {
                if (cursor.moveToPrevious()) {
                    previous = initMessage(cursor);
                    cursor.moveToNext();
                } else {
                    shouldShow = true;
                }
            }
            if (!shouldShow) {
                shouldShow = vpm.created <= 0 || previous.created <= 0 || Math.abs(vpm.created - previous.created) >= 3600000;
            }
        }
        if (shouldShow) {
            holder.timestamp.setText(this.mVineDateFormatter.format(vpm.created));
            holder.timestamp.setVisibility(0);
        } else {
            holder.timestamp.setVisibility(8);
        }
    }

    private void styleTopSharedDescription(VinePost post, boolean isCurrentUser, int color, TextView view, boolean deleted) {
        String replacer;
        int color2 = color | ViewCompat.MEASURED_STATE_MASK;
        if (deleted || post == null || post.username == null) {
            SpannableStringBuilder sb = new SpannableStringBuilder();
            sb.append((CharSequence) this.mContext.getString(isCurrentUser ? R.string.share_vm_sender_deleted : R.string.share_vm_receiver_deleted));
            Util.safeSetSpan(sb, this.mDarkGreySpan, 0, sb.length(), 33);
            view.setText(sb);
            if (post != null && post.username == null) {
                CrashUtil.logException(new IllegalArgumentException("Username is null: " + post.postId));
                return;
            }
            return;
        }
        if (post.sharedVmSb == null) {
            String string = this.mContext.getString(isCurrentUser ? R.string.share_vm_sender_text : R.string.share_vm_receiver_text);
            post.sharedVmSb = new SpannableStringBuilder();
            SpannableStringBuilder sharedVmSb = post.sharedVmSb;
            int start = string.indexOf("{");
            if (start > 0) {
                sharedVmSb.append((CharSequence) string.substring(0, start));
            }
            int index = string.indexOf("}");
            if (index < 0) {
                string = string + "}";
            }
            try {
                replacer = string.substring(start + 1, string.indexOf("}"));
            } catch (Exception e) {
                CrashUtil.logException(e, "Failed top post span thing. locale: {}", Util.getLocale());
                replacer = "%s";
            }
            sharedVmSb.append((CharSequence) replacer.replace("%s", post.username));
            int end = sharedVmSb.length();
            int endOfReplacer = string.indexOf("}");
            if (endOfReplacer < string.length() - 1) {
                sharedVmSb.append((CharSequence) string.substring(endOfReplacer + 1));
            }
            VineClickableSpan clickableSpan = this.mClickableSpanFactory.newSinglePostClickableSpan(this.mContext, color2, post.postId);
            Util.safeSetSpan(sharedVmSb, clickableSpan, start, end, 33);
            Util.safeSetSpan(sharedVmSb, this.mDarkGreySpan, 0, start, 33);
        }
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(post.sharedVmSb);
    }

    private void styleDescription(TextView textView, VinePost post, int color, boolean deletedSharedPost) {
        int color2 = color | ViewCompat.MEASURED_STATE_MASK;
        if (post != null && post.transientEntities == null && post.entities != null) {
            post.transientEntities = new ArrayList<>();
            Iterator<VineEntity> it = post.entities.iterator();
            while (it.hasNext()) {
                post.transientEntities.add(it.next().duplicate());
            }
        }
        ArrayList<VineEntity> postEntities = post != null ? post.transientEntities : null;
        if (postEntities != null) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            if (post.descriptionSb == null && post.description != null) {
                post.descriptionSb = new SpannableStringBuilder(post.description);
                SpannableStringBuilder descriptionSb = post.descriptionSb;
                try {
                    Util.adjustEntities(postEntities, descriptionSb, 0, false);
                    Iterator<VineEntity> it2 = postEntities.iterator();
                    while (it2.hasNext()) {
                        VineEntity entity = it2.next();
                        EntityLinkClickableSpan clickableSpan = new EntityLinkClickableSpan(this.mContext, color2, entity.link);
                        Util.safeSetSpan(descriptionSb, clickableSpan, entity.start, entity.end, 33);
                    }
                } catch (Exception e) {
                    CrashUtil.logException(e);
                }
            }
            textView.setText(post.descriptionSb);
            return;
        }
        if (deletedSharedPost || post == null) {
            SpannableStringBuilder sb = new SpannableStringBuilder(this.mActivity.getString(R.string.share_vm_deleted));
            Util.safeSetSpan(sb, this.mDarkGreySpan, 0, sb.length(), 33);
            textView.setText(sb);
            return;
        }
        textView.setText(post.description);
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public synchronized void getVideoPathAndPlayForPosition(int requestedPosition, boolean forceLowKey) {
        ConversationViewHolder tag;
        SLog.d("Play {} requested.", Integer.valueOf(requestedPosition));
        if (this.mPaths.get(requestedPosition) != null) {
            playFileAtPosition(requestedPosition, true);
        } else if (this.mMessages.get(requestedPosition) != null) {
            VinePrivateMessage vpm = this.mMessages.get(requestedPosition);
            VideoKey videoKey = new VideoKey(vpm.videoUrl);
            String path = this.mAppController.getVideoFilePath(videoKey);
            if (path != null) {
                this.mPaths.put(requestedPosition, path);
                SLog.d("playing file at position {}", Integer.valueOf(requestedPosition));
                playFileAtPosition(requestedPosition, true);
            } else {
                View currentView = getViewAt(requestedPosition);
                if (currentView != null && (tag = (ConversationViewHolder) currentView.getTag()) != null) {
                    tag.loadImage.setVisibility(0);
                }
                this.mShouldBePlaying = requestedPosition;
                this.mUrlReverse.put(videoKey, Integer.valueOf(requestedPosition));
            }
        }
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public void showAttributions() {
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public void hideAttributions() {
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public void playFileAtPosition(int position, boolean isVideoLoaded) throws IllegalStateException {
        SdkVideoView currentVideoView;
        if (position >= 0) {
            View currentView = getViewAt(position);
            if (currentView == null) {
                SLog.i("Item is not visible: {}", Integer.valueOf(position));
                return;
            }
            ConversationViewHolder holder = (ConversationViewHolder) currentView.getTag();
            if (holder != null) {
                String newPath = this.mPaths.get(position);
                String oldPath = holder.videoView.getPath();
                if (newPath != null) {
                    pauseCurrentPlayer();
                    if (!newPath.equals(oldPath)) {
                        refreshVideoView(position, holder);
                        currentVideoView = holder.videoView;
                        currentVideoView.setVideoPath(newPath);
                        currentVideoView.setPlayingPosition(position);
                        currentVideoView.setTag(holder);
                        holder.videoListener.setPosition(position);
                    } else {
                        currentVideoView = holder.videoView;
                        if (currentVideoView.getVisibility() != 0) {
                            currentVideoView.setVisibility(0);
                        }
                        currentVideoView.setPlayingPosition(position);
                        if (currentVideoView.isInPlaybackState() && currentVideoView.isPathPlaying(newPath)) {
                            currentVideoView.start();
                        } else {
                            currentVideoView.setVideoPath(newPath);
                        }
                    }
                    this.mCurrentPlaying = position;
                    this.mLastPlayer = currentVideoView;
                    currentVideoView.setMute(this.mMuted);
                }
            }
        }
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public void pausePlayer(VideoViewInterface player) {
        player.pause();
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public void resumePlayer(VideoViewInterface player) {
        player.resume();
    }

    private void refreshVideoView(int position, ConversationViewHolder tag) throws IllegalStateException {
        ViewGroup group = (ViewGroup) tag.videoView.getParent();
        if (this.mVideoAttr == null) {
            this.mVideoAttr = tag.videoView.getAttributes();
        }
        tag.videoView.suspend();
        try {
            group.removeView(tag.videoView);
        } catch (RuntimeException e) {
            try {
                group.removeView(tag.videoView);
            } catch (Exception e2) {
                CrashUtil.logException(e, "Weird things are happening.", new Object[0]);
            }
        }
        tag.videoView = new SdkVideoView(this.mContext, this.mVideoAttr);
        group.addView(tag.videoView);
        initVideoView(position, tag);
    }

    public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
        for (VideoKey key : videos.keySet()) {
            UrlVideo video = videos.get(key);
            if (video.isValid() && this.mUrlReverse.get(key) != null) {
                this.mPaths.put(this.mUrlReverse.get(key).intValue(), video.getAbsolutePath());
                if (this.mUrlReverse.get(key).intValue() == this.mShouldBePlaying) {
                    postNewPlayCurrentPositionRunnable();
                }
            }
        }
    }

    public synchronized void postNewPlayCurrentPositionRunnable() {
        this.mHandler.removeCallbacks(this.mPlayCurrentPositionRunnable);
        this.mHandler.postDelayed(this.mPlayCurrentPositionRunnable, 50L);
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public VideoViewInterface getPlayer(int position, boolean b) {
        return this.mPlayers.get(position);
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public void pauseCurrentPlayer() throws IllegalStateException {
        if (hasPlayerPlaying()) {
            this.mLastPlayer.pause();
        }
    }

    private boolean hasPlayerPlaying() {
        return (this.mLastPlayer == null || this.mLastPlayer.isPaused()) ? false : true;
    }

    public void stopCurrentPlayer() {
        if (this.mLastPlayer != null) {
            this.mLastPlayer.suspend();
            this.mLastPlayer = null;
            this.mCurrentPlaying = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public View getViewAt(int position) {
        ListView listView = this.mListView;
        int firstPosition = (listView.getFirstVisiblePosition() - listView.getHeaderViewsCount()) - 0;
        int wantedChild = position - firstPosition;
        if (wantedChild >= 0 && wantedChild < listView.getChildCount()) {
            return listView.getChildAt(wantedChild);
        }
        SLog.w("Unable to get view for desired position, because it's not being displayed on screen: {} {} {}.", new Object[]{Integer.valueOf(wantedChild), Integer.valueOf(firstPosition), Integer.valueOf(listView.getChildCount())});
        return null;
    }

    public void onResume() {
        this.mMuted = MuteUtil.isMuted(this.mContext);
        postNewPlayCurrentPositionRunnable();
        this.mVineDateFormatter.refreshDates();
    }

    public void onPause() {
        stopCurrentPlayer();
        releaseOtherPlayers(null);
    }

    private void initVideoView(int position, final ConversationViewHolder holder) {
        final SdkVideoView view = holder.videoView;
        view.setAutoShow(true);
        view.setOnPreparedListener(new VideoViewInterface.OnPreparedListener() { // from class: co.vine.android.ConversationAdapter.3
            @Override // co.vine.android.embed.player.VideoViewInterface.OnPreparedListener
            public void onPrepared(VideoViewInterface view2) {
                view2.start();
                ConversationAdapter.this.onLoadFinish(holder);
                holder.nibs.bringToFront();
                holder.postNibs.bringToFront();
            }
        });
        view.setOnErrorListener(new VideoViewInterface.OnErrorListener() { // from class: co.vine.android.ConversationAdapter.4
            @Override // co.vine.android.embed.player.VideoViewInterface.OnErrorListener
            public boolean onError(VideoViewInterface videoView, int what, int extra) throws IllegalStateException {
                SLog.d("Got error, try recycling it more aggressively: {}, {}", Integer.valueOf(what), Integer.valueOf(extra));
                ConversationAdapter.this.releaseOtherPlayers(view);
                view.retryOpenVideo(false);
                return true;
            }
        });
        view.setOnCompletionListener(new VideoViewInterface.OnCompletionListener() { // from class: co.vine.android.ConversationAdapter.5
            @Override // co.vine.android.embed.player.VideoViewInterface.OnCompletionListener
            public void onCompletion(VideoViewInterface videoView) throws IllegalStateException {
                view.seekTo(0);
                view.start();
            }
        });
        this.mPlayers.put(position, view);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseOtherPlayers(VideoViewInterface view) {
        int viewKey = -1;
        for (int i = 0; i < this.mPlayers.size(); i++) {
            int key = this.mPlayers.keyAt(i);
            VideoViewInterface v = this.mPlayers.get(key);
            if (v != view) {
                v.suspend();
            } else {
                viewKey = key;
            }
        }
        this.mPlayers.clear();
        if (view != null) {
            if (viewKey >= 0) {
                this.mPlayers.put(viewKey, view);
            } else {
                CrashUtil.logException(new RuntimeException("Invalid state in conversation adapter. Was seeing this a lot coming back from full record. Commented out the runtime and am just logging"));
            }
        }
    }

    void onLoadFinish(ConversationViewHolder holder) {
        VinePrivateMessage message = this.mMessages.get(holder.position);
        if (message != null && holder.videoView.getVisibility() != 0) {
            holder.videoView.setVisibility(0);
        }
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public VideoViewInterface getLastPlayer() {
        return this.mLastPlayer;
    }

    private boolean setImage(ImageView imageView, Bitmap bitmap, ConversationViewHolder holder) {
        if (bitmap == null) {
            imageView.setBackgroundColor(this.mContext.getResources().getColor(R.color.solid_light_gray));
            imageView.setImageBitmap(null);
            return false;
        }
        imageView.setImageDrawable(new RecyclableBitmapDrawable(this.mContext.getResources(), bitmap));
        imageView.setBackgroundColor(this.mContext.getResources().getColor(android.R.color.transparent));
        return true;
    }

    public void setImages(HashMap<ImageKey, UrlImage> images) {
        UrlImage image;
        UrlImage image2;
        ArrayList<WeakReference<ConversationViewHolder>> toRemove = new ArrayList<>();
        Iterator<WeakReference<ConversationViewHolder>> it = this.mViewHolders.iterator();
        while (it.hasNext()) {
            WeakReference<ConversationViewHolder> ref = it.next();
            ConversationViewHolder holder = ref.get();
            if (holder == null) {
                toRemove.add(ref);
            } else {
                if (holder.avatarKey != null && (image2 = images.get(holder.avatarKey)) != null && image2.isValid()) {
                    if (holder.isSharedPost) {
                        setImage(holder.topMessageContainerUserImage, image2.bitmap, holder);
                    } else {
                        setImage(holder.messageContainerUserImage, image2.bitmap, holder);
                    }
                }
                if (holder.videoImageKey != null && (image = images.get(holder.videoImageKey)) != null && image.isValid()) {
                    holder.isVideoImageLoaded = setImage(holder.videoImage, image.bitmap, holder);
                    this.mThumbnails.put(holder.videoListener.getPosition(), image.bitmap);
                    SLog.d("Image found: {}.", Integer.valueOf(holder.videoListener.getPosition()));
                    postNewPlayCurrentPositionRunnable();
                }
            }
        }
        Iterator<WeakReference<ConversationViewHolder>> it2 = toRemove.iterator();
        while (it2.hasNext()) {
            WeakReference<ConversationViewHolder> h = it2.next();
            this.mViewHolders.remove(h);
        }
    }

    @Override // android.support.v4.widget.CursorAdapter
    public Cursor swapCursor(Cursor c) {
        Cursor oldCursor = super.swapCursor(c);
        this.mIdToPositionMap.clear();
        this.mThumbnails.clear();
        this.mPaths.clear();
        if (c != null && c.moveToFirst()) {
            do {
                this.mIdToPositionMap.put(Long.valueOf(c.getLong(0)), Integer.valueOf(c.getPosition()));
            } while (c.moveToNext());
        }
        return oldCursor;
    }

    @Override // android.support.v4.widget.CursorAdapter, android.widget.Adapter
    public long getItemId(int pos) {
        if (this.mCursor == null || !this.mCursor.moveToPosition(pos)) {
            return -1L;
        }
        return this.mCursor.getLong(0);
    }

    public boolean atLastPage() {
        return this.mCursor != null && this.mCursor.moveToFirst() && this.mCursor.getInt(8) == 1;
    }

    public int getPositionForId(long id) {
        if (this.mIdToPositionMap.containsKey(Long.valueOf(id))) {
            return this.mIdToPositionMap.get(Long.valueOf(id)).intValue();
        }
        return -1;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(final View v) {
        Object tag = v.getTag();
        if (tag instanceof Integer) {
            final VinePrivateMessage vpm = this.mMessages.get(((Integer) tag).intValue());
            Runnable retryRunnable = new Runnable() { // from class: co.vine.android.ConversationAdapter.6
                @Override // java.lang.Runnable
                public void run() {
                    ConversationAdapter.this.mContext.startService(VineUploadService.getVMPostIntent(ConversationAdapter.this.mContext, vpm.uploadPath, true, vpm.messageRowId, vpm.conversationRowId, null, vpm.message, vpm.postId, vpm.videoUrl, vpm.thumbnailUrl));
                    ViewGroup parent = (ViewGroup) v.getParent();
                    View progress = parent.findViewById(R.id.retry_progress_normal);
                    if (progress == null) {
                        progress = parent.findViewById(R.id.retry_progress_top);
                    }
                    if (progress != null) {
                        progress.setVisibility(0);
                        v.setVisibility(4);
                    }
                }
            };
            if (vpm.errorCode == 616) {
                this.mActivity.startPhoneConfirmation(retryRunnable);
            } else if (vpm.errorCode != 0) {
                retryRunnable.run();
            }
        }
    }

    private class ConversationVideoClickedListener extends OnListVideoClickListener {
        public ConversationVideoClickedListener() {
            super(ConversationAdapter.this);
        }

        @Override // co.vine.android.player.OnListVideoClickListener, android.view.View.OnClickListener
        public void onClick(View v) {
            super.onClick(v);
        }
    }

    private static class ConversationViewHolder {
        public ImageKey avatarKey;
        public final View bottomPaddingView;
        public int color;
        public final TextView errorMessage;
        public boolean hasVideoImage;
        public boolean isCurrentUser;
        public boolean isSharedPost;
        public boolean isVideoImageLoaded;
        public final View loadImage;
        public final ViewGroup messageContainer;
        public final TextView messageContainerMessage;
        public final ImageView messageContainerUserImage;
        public final View nibs;
        public final View nibsChatNib;
        public int position;
        public final ViewGroup postMessageContainer;
        public final TextView postMessageContainerMessage;
        public View postNibs;
        public final TextView timestamp;
        public final ViewGroup topMessageContainer;
        public final TextView topMessageContainerMessage;
        public final ImageView topMessageContainerUserImage;
        public View userImageProgressNormal;
        public View userImageProgressTop;
        public final View videoContainer;
        public final ImageView videoImage;
        public ImageKey videoImageKey;
        public OnListVideoClickListener videoListener;
        public SdkVideoView videoView;

        public ConversationViewHolder(View v) {
            this.messageContainer = (ViewGroup) v.findViewById(R.id.message_container);
            this.messageContainerUserImage = (ImageView) v.findViewById(R.id.message_container_user_image);
            this.messageContainerMessage = (TextView) v.findViewById(R.id.message_container_message);
            this.topMessageContainer = (ViewGroup) v.findViewById(R.id.top_message_container);
            this.topMessageContainerUserImage = (ImageView) v.findViewById(R.id.top_message_container_user_image);
            this.topMessageContainerMessage = (TextView) v.findViewById(R.id.top_message_container_message);
            this.postMessageContainer = (ViewGroup) v.findViewById(R.id.post_message_container);
            this.postMessageContainerMessage = (TextView) v.findViewById(R.id.post_message_container_message);
            this.userImageProgressNormal = v.findViewById(R.id.retry_progress_normal);
            this.userImageProgressTop = v.findViewById(R.id.retry_progress_top);
            this.videoContainer = v.findViewById(R.id.video_container);
            this.videoImage = (ImageView) v.findViewById(R.id.video_image);
            this.videoView = (SdkVideoView) v.findViewById(R.id.video_view);
            this.loadImage = v.findViewById(R.id.video_load_image);
            this.errorMessage = (TextView) v.findViewById(R.id.error_message);
            this.bottomPaddingView = v.findViewById(R.id.message_bottom_padding_for_last_item);
            this.nibs = v.findViewById(R.id.nibs);
            this.postNibs = v.findViewById(R.id.shared_post_nibs);
            this.nibsChatNib = v.findViewById(R.id.nibs_chat_nib);
            this.timestamp = (TextView) v.findViewById(R.id.timestampText);
        }
    }

    public void toggleMute(boolean mute) {
        this.mMuted = mute;
        SLog.d("Mute state changed to muted? {}.", Boolean.valueOf(mute));
        VideoViewInterface lastPlayer = getLastPlayer();
        if (lastPlayer != null) {
            lastPlayer.setMute(this.mMuted);
        }
    }

    @Override // co.vine.android.player.HasVideoPlayerAdapter
    public int getCurrentPosition() {
        return this.mCurrentPlaying;
    }
}
