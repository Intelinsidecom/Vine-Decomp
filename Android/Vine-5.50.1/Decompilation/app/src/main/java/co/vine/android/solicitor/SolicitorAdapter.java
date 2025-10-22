package co.vine.android.solicitor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.SolicitorActivity;
import co.vine.android.api.VinePost;
import co.vine.android.client.AppController;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.player.SdkVideoView;
import co.vine.android.service.components.Components;
import co.vine.android.util.ResourceLoader;
import co.vine.android.views.swipeable.SwipeFlingAdapterView;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class SolicitorAdapter extends ArrayAdapter<VinePost> {
    private final AppController mAppController;
    private View mBackground;
    private final SwipeFlingAdapterView mBaseView;
    private final Context mContext;
    private float mCurrentFadeProgress;
    private float mCurrentScaleDownProgress;
    private float mCurrentScaleUpProgress;
    private VideoViewInterface mCurrentlyPlaying;
    private ImageView mDislikeView;
    private final LayoutInflater mInflater;
    private int mLikeCounter;
    private ImageView mLikeView;
    private ArrayList<VinePost> mLikedPosts;
    private final ArrayList<VinePost> mPosts;
    private int mPostsDismissed;
    private final ResourceLoader mResourceLoader;

    public SolicitorAdapter(Context context, AppController appController, ArrayList<VinePost> posts, SwipeFlingAdapterView baseView, View background) {
        super(context, R.layout.solicitor, posts);
        this.mLikeCounter = 0;
        this.mPostsDismissed = 0;
        this.mCurrentScaleUpProgress = 1.0f;
        this.mCurrentFadeProgress = 0.5f;
        this.mCurrentScaleDownProgress = 1.0f;
        this.mPosts = posts;
        this.mLikedPosts = new ArrayList<>();
        this.mContext = context;
        this.mAppController = appController;
        this.mResourceLoader = new ResourceLoader(this.mContext, this.mAppController);
        this.mBaseView = baseView;
        this.mBackground = background;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.mLikeCounter = 0;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public int getCount() {
        if (this.mPosts == null || this.mPosts.isEmpty()) {
            return 0;
        }
        return this.mPosts.size() + 1;
    }

    private View getExitView(ViewGroup parent) {
        View v = LayoutInflater.from(this.mContext).inflate(R.layout.solicitor_exit, parent, false);
        int[] thumbnailIds = {R.id.thumbnail1, R.id.thumbnail2, R.id.thumbnail3, R.id.thumbnail4, R.id.thumbnail5};
        int i = 0;
        while (true) {
            if (i < (this.mLikedPosts.size() < 5 ? this.mLikedPosts.size() : 5)) {
                this.mResourceLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter((ImageView) v.findViewById(thumbnailIds[i])), this.mLikedPosts.get(i).thumbnailUrl);
                i++;
            } else {
                View done = v.findViewById(R.id.done_text);
                done.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.solicitor.SolicitorAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v2) {
                        ((FragmentActivity) SolicitorAdapter.this.mContext).finish();
                    }
                });
                Button again = (Button) v.findViewById(R.id.again);
                again.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.solicitor.SolicitorAdapter.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v2) {
                        SolicitorActivity.start(SolicitorAdapter.this.mContext);
                        ((FragmentActivity) SolicitorAdapter.this.mContext).finish();
                    }
                });
                ProgressBar progress = (ProgressBar) v.findViewById(R.id.progress_bar);
                progress.setProgress(progress.getMax());
                progress.getProgressDrawable().setColorFilter(this.mContext.getResources().getColor(R.color.white_fifty_percent), PorterDuff.Mode.SRC_IN);
                this.mBaseView.setSwipeEnabled(false);
                return v;
            }
        }
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) throws IllegalStateException, Resources.NotFoundException {
        if (position >= this.mPosts.size() || this.mLikeCounter >= 5) {
            return getExitView(parent);
        }
        View v = convertView;
        if (v == null) {
            v = this.mInflater.inflate(R.layout.solicitor, parent, false);
            PostHolder tag = new PostHolder();
            initView(v, tag);
            v.setTag(tag);
        }
        if (this.mLikeCounter <= 0) {
            v.findViewById(R.id.progress_bar).setVisibility(8);
        } else {
            v.findViewById(R.id.progress_bar).setVisibility(0);
        }
        bind((ViewGroup) v, (PostHolder) v.getTag(), position);
        return v;
    }

    private void initView(View v, final PostHolder tag) throws IllegalStateException {
        tag.videoImage = (ImageView) v.findViewById(R.id.video_thumbnail);
        tag.videoView = (SdkVideoView) v.findViewById(R.id.video);
        tag.skip = v.findViewById(R.id.skip_text);
        tag.videoView.setOnPreparedListener(new VideoViewInterface.OnPreparedListener() { // from class: co.vine.android.solicitor.SolicitorAdapter.3
            @Override // co.vine.android.embed.player.VideoViewInterface.OnPreparedListener
            public void onPrepared(VideoViewInterface view) {
                tag.swipeShield.setVisibility(8);
            }
        });
        tag.videoView.setOnErrorListener(new VideoViewInterface.OnErrorListener() { // from class: co.vine.android.solicitor.SolicitorAdapter.4
            @Override // co.vine.android.embed.player.VideoViewInterface.OnErrorListener
            public boolean onError(VideoViewInterface videoViewInterface, int what, int extra) {
                tag.videoView.setVisibility(8);
                return true;
            }
        });
        tag.videoView.setSurfaceUpdatedListener(new VideoViewInterface.SurfaceUpdatedListener() { // from class: co.vine.android.solicitor.SolicitorAdapter.5
            @Override // co.vine.android.embed.player.VideoViewInterface.SurfaceUpdatedListener
            public void onSurfaceUpdated() {
            }
        });
        tag.videoView.setLooping(true);
        tag.videoView.setMute(false);
        tag.videoView.setAutoPlayOnPrepared(true);
        tag.videoView.setSize(tag.videoImage.getWidth(), tag.videoImage.getHeight());
        tag.videoView.setSize((int) this.mContext.getResources().getDimension(R.dimen.video_width), tag.videoImage.getHeight());
        tag.videoView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: co.vine.android.solicitor.SolicitorAdapter.6
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View v2) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View v2) throws IllegalStateException {
                tag.videoView.pause();
            }
        });
        tag.caption = (TextView) v.findViewById(R.id.video_caption);
        tag.heart = (ImageView) v.findViewById(R.id.heart);
        tag.cross = (ImageView) v.findViewById(R.id.cross);
        tag.progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        tag.done = v.findViewById(R.id.done_text);
        tag.swipeShield = (ViewGroup) v.findViewById(R.id.swipe_shield);
        tag.swipeShield.setOnTouchListener(new View.OnTouchListener() { // from class: co.vine.android.solicitor.SolicitorAdapter.7
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v2, MotionEvent event) {
                return true;
            }
        });
    }

    private void updateBackground() {
        if (this.mPosts.size() != 0) {
            VinePost post = this.mPosts.get(0);
            this.mResourceLoader.setImageWhenLoaded(new BackgroundImageSetter(this.mBackground), post.thumbnailUrl, 0, 25, false, 0);
        }
    }

    private void bind(ViewGroup parent, PostHolder holder, int position) throws IllegalStateException, Resources.NotFoundException {
        parent.setVisibility(4);
        holder.swipeShield.setVisibility(0);
        this.mResourceLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(holder.videoImage), this.mPosts.get(position).thumbnailUrl);
        this.mResourceLoader.loadVideo(holder.videoView, this.mPosts.get(position).videoUrl, true);
        holder.caption.setText(this.mPosts.get(position).description);
        holder.heart.setAlpha(0.5f);
        holder.cross.setAlpha(0.5f);
        this.mLikeView = holder.heart;
        this.mDislikeView = holder.cross;
        holder.heart.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.solicitor.SolicitorAdapter.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) throws Resources.NotFoundException {
                SolicitorAdapter.this.mBaseView.getTopCardListener().selectRight();
                SolicitorAdapter.this.mLikeView.setAlpha(1.0f);
                Animation scaleUp = AnimationUtils.loadAnimation(SolicitorAdapter.this.mContext, R.anim.scale_up);
                SolicitorAdapter.this.mLikeView.startAnimation(scaleUp);
                Animation scaleDown = AnimationUtils.loadAnimation(SolicitorAdapter.this.mContext, R.anim.scale_down);
                SolicitorAdapter.this.mDislikeView.startAnimation(scaleDown);
            }
        });
        holder.cross.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.solicitor.SolicitorAdapter.9
            @Override // android.view.View.OnClickListener
            public void onClick(View v) throws Resources.NotFoundException {
                SolicitorAdapter.this.mBaseView.getTopCardListener().selectLeft();
                SolicitorAdapter.this.mDislikeView.setAlpha(1.0f);
                Animation anim = AnimationUtils.loadAnimation(SolicitorAdapter.this.mContext, R.anim.scale_up);
                SolicitorAdapter.this.mDislikeView.startAnimation(anim);
                Animation scaleDown = AnimationUtils.loadAnimation(SolicitorAdapter.this.mContext, R.anim.scale_down);
                SolicitorAdapter.this.mLikeView.startAnimation(scaleDown);
            }
        });
        holder.progressBar.setMax(5);
        holder.progressBar.setProgress(this.mLikeCounter);
        holder.progressBar.getProgressDrawable().setColorFilter(this.mContext.getResources().getColor(R.color.white_fifty_percent), PorterDuff.Mode.SRC_IN);
        if (this.mCurrentlyPlaying != null) {
            this.mCurrentlyPlaying.setMute(true);
        }
        this.mCurrentlyPlaying = holder.videoView;
        this.mBaseView.setSwipeEnabled(true);
        parent.setVisibility(0);
        Animation fadein = AnimationUtils.loadAnimation(this.mContext, R.anim.fade_in);
        fadein.setDuration(500L);
        View content = parent.findViewById(R.id.content);
        if (content == null) {
            content = parent;
        }
        content.startAnimation(fadein);
        updateBackground();
        this.mCurrentScaleUpProgress = 1.0f;
        holder.skip.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.solicitor.SolicitorAdapter.10
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (!SolicitorAdapter.this.mPosts.isEmpty()) {
                    SolicitorAdapter.this.mBaseView.getTopCardListener().removeTop();
                    SolicitorAdapter.this.incrementDismissCount();
                } else {
                    ((FragmentActivity) SolicitorAdapter.this.mContext).finish();
                }
            }
        });
        holder.done.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.solicitor.SolicitorAdapter.11
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ((FragmentActivity) SolicitorAdapter.this.mContext).finish();
            }
        });
    }

    public void pauseCurrentVideo() {
        if (this.mCurrentlyPlaying != null) {
            this.mCurrentlyPlaying.pause();
        }
    }

    public void playCurrentVideo() {
        if (this.mCurrentlyPlaying != null) {
            this.mCurrentlyPlaying.start();
        }
    }

    public void addLike(VinePost post) {
        this.mLikeCounter++;
        Components.postActionsComponent().likePost(this.mAppController, "solicitor", post.postId, 0L, false);
        if (post != null) {
            this.mLikedPosts.add(post);
        }
    }

    public void incrementDismissCount() {
        this.mPostsDismissed++;
    }

    public int getDismissCount() {
        return this.mPostsDismissed;
    }

    public void animateLike(float scrollProgress) {
        if (this.mLikeView != null) {
            animateViewProgress(true, scrollProgress);
        }
    }

    public void animateDislike(float scrollProgress) {
        if (this.mDislikeView != null) {
            animateViewProgress(false, scrollProgress);
        }
    }

    public void deanimateLike(float scrollProgress) {
        if (this.mLikeView != null) {
            this.mLikeView.setAlpha(0.5f);
        }
    }

    public void deanimateDislike(float scrollProgress) {
        if (this.mDislikeView != null) {
            this.mDislikeView.setAlpha(0.5f);
        }
    }

    private void animateViewProgress(boolean isLike, float scrollProgress) {
        ImageView scaleUpView;
        ImageView scaleDownView;
        if (isLike) {
            scaleUpView = this.mLikeView;
            scaleDownView = this.mDislikeView;
        } else {
            scaleUpView = this.mDislikeView;
            scaleDownView = this.mLikeView;
        }
        float scrollProgress2 = Math.abs(scrollProgress);
        Animation scaleUp = new ScaleAnimation(this.mCurrentScaleUpProgress, 1.0f + scrollProgress2, this.mCurrentScaleUpProgress, 1.0f + scrollProgress2);
        this.mCurrentScaleUpProgress = 1.0f + scrollProgress2;
        scaleUp.setDuration(500L);
        Animation alphaAnimation = new AlphaAnimation(this.mCurrentFadeProgress, 0.5f + (0.5f * scrollProgress2));
        alphaAnimation.setDuration(500L);
        this.mCurrentFadeProgress = 0.5f + (0.5f * scrollProgress2);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(scaleUp);
        animationSet.addAnimation(alphaAnimation);
        scaleUpView.startAnimation(animationSet);
        Animation scaleDown = new ScaleAnimation(this.mCurrentScaleDownProgress, 1.0f - (0.5f * scrollProgress2), this.mCurrentScaleDownProgress, 1.0f - (0.5f * scrollProgress2));
        this.mCurrentScaleDownProgress = 1.0f - (0.5f * scrollProgress2);
        scaleDown.setDuration(500L);
        scaleDownView.startAnimation(scaleDown);
    }

    private static final class PostHolder {
        TextView caption;
        ImageView cross;
        View done;
        ImageView heart;
        ProgressBar progressBar;
        View skip;
        ViewGroup swipeShield;
        ImageView videoImage;
        SdkVideoView videoView;

        private PostHolder() {
        }
    }

    private static final class BackgroundImageSetter implements ResourceLoader.ImageSetter {
        private final View layoutView;

        public BackgroundImageSetter(View layoutView) {
            this.layoutView = layoutView;
        }

        @Override // co.vine.android.util.ResourceLoader.ImageSetter
        public void setImage(RecyclableBitmapDrawable drawable) {
            this.layoutView.setBackgroundDrawable(drawable);
        }

        @Override // co.vine.android.util.ResourceLoader.ImageSetter
        public View getControllingView() {
            return this.layoutView;
        }

        @Override // co.vine.android.util.ResourceLoader.ImageSetter
        public void startAnimation(Animation animation) {
        }
    }
}
