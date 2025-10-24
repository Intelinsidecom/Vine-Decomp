package co.vine.android.share.screens;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import co.vine.android.R;
import co.vine.android.animation.SimpleAnimatorListener;
import co.vine.android.api.VineChannel;
import co.vine.android.share.screens.ShareScreenHelper;
import co.vine.android.share.widgets.ChannelPickerRow;
import co.vine.android.share.widgets.FakeActionBar;
import co.vine.android.util.ColorUtils;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.ViewUtil;
import com.googlecode.javacv.cpp.avcodec;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public class PostShareScreenHelper extends ShareScreenHelper {
    protected final boolean mIsPrivateUser;
    protected final PostShareScreenHolder postHolder;

    @Override // co.vine.android.share.screens.ShareScreenHelper
    protected void onVineCheckStateChanged(boolean selected) {
        ensureVmAndChannelPickerContainerState(true);
        setExternalNetworkShareTargetRowsEnabled(selected);
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    public void onInitialize(ScreenManager screenManager, Bundle initialData) {
        super.onInitialize(screenManager, initialData);
        ensureVmAndChannelPickerContainerState(false);
    }

    public PostShareScreenHelper(Context context, ShareScreenHelper.ShareScreenViewHolder viewHolder, PostShareScreenHolder holder, boolean isPrivateUser, VineChannel selectedChannel) {
        super(context, viewHolder, true);
        this.postHolder = holder;
        this.mIsPrivateUser = isPrivateUser;
        this.postHolder.mChannelPicker.setSelectedChannel(selectedChannel);
        this.postHolder.mChannelPicker.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.share.screens.PostShareScreenHelper.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                PostShareScreenHelper.this.mScreenManager.showScreen("channel_picker");
            }
        });
        this.postHolder.mChannelPicker.setOnLongClickListener(new View.OnLongClickListener() { // from class: co.vine.android.share.screens.PostShareScreenHelper.2
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View v) {
                PostShareScreenHelper.this.postHolder.mChannelPicker.clearSelectedChannel();
                return true;
            }
        });
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    public void onBindFakeActionBar(FakeActionBar fakeActionBar) throws Resources.NotFoundException {
        String share = this.mRes.getString(R.string.share_title);
        fakeActionBar.setLabelText(share);
        View fakeActionBarBackArrow = this.mScreenManager.getFakeActionBar().inflateBackView(R.layout.fake_action_bar_back_arrow);
        fakeActionBar.setBackView(fakeActionBarBackArrow);
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    protected long getSelectedChannelId() {
        return this.postHolder.mChannelPicker.getSelectedChannelId();
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    public VineChannel getSelectedChannel() {
        return this.postHolder.mChannelPicker.getSelectedChannel();
    }

    private Animator getVmAndCommentContainerTranslationAnimator(boolean isIn, int animationDuration) {
        int startY;
        final int endY;
        Point windowSize = SystemUtil.getDisplaySize(this.mContext);
        Point channelPickerContainerSize = ViewUtil.getAtMostSize(this.postHolder.mChannelPickerContainer, windowSize.x, windowSize.y);
        if (isIn) {
            startY = channelPickerContainerSize.y;
            endY = 0;
        } else {
            startY = 0;
            endY = channelPickerContainerSize.y;
        }
        ValueAnimator translation = ValueAnimator.ofFloat(startY, endY);
        translation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: co.vine.android.share.screens.PostShareScreenHelper.3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float valueY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                PostShareScreenHelper.this.postHolder.mVmAndChannelPickerContainer.setY(valueY);
            }
        });
        translation.addListener(new SimpleAnimatorListener() { // from class: co.vine.android.share.screens.PostShareScreenHelper.4
            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                PostShareScreenHelper.this.postHolder.mVmAndChannelPickerContainer.setY(endY);
            }
        });
        translation.setInterpolator(new DecelerateInterpolator());
        translation.setDuration(animationDuration);
        translation.setTarget(this.postHolder.mVmAndChannelPickerContainer);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translation);
        return animatorSet;
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) throws Resources.NotFoundException {
        if (requestCode != 4) {
            return super.onActivityResult(activity, requestCode, resultCode, data);
        }
        if (resultCode == -1) {
            long channelId = data.getLongExtra("channel_id", -1L);
            String channelName = data.getStringExtra("channel");
            String channelColorHexString = data.getStringExtra("channel_color");
            String channelUrl = data.getStringExtra("channel_icon_url");
            int vineGreen = this.mRes.getColor(R.color.vine_green);
            int channelColorHex = ColorUtils.parseColorHex(channelColorHexString, vineGreen);
            VineChannel channel = null;
            if (channelId > -1 && !TextUtils.isEmpty(channelName)) {
                channel = new VineChannel();
                channel.channelId = channelId;
                channel.channel = channelName;
                channel.colorHex = channelColorHex;
                channel.iconFullUrl = channelUrl;
                this.mViewHolder.mVineToggleRow.setChecked(true);
            }
            if (this.postHolder != null) {
                this.postHolder.mChannelPicker.setSelectedChannel(channel);
            }
        }
        return true;
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        VineChannel channel = (VineChannel) Parcels.unwrap(savedInstanceState.getParcelable("extra_channel"));
        this.postHolder.mChannelPicker.setSelectedChannel(channel);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    protected void ensureSubmitButton() {
        String actionButtonText = getActionButtonTextPost(isVineSelected(), this.mViewHolder.mVmRecipientIndicator.numRecipients());
        this.mViewHolder.mSubmitButton.setText(actionButtonText);
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable("extra_channel", Parcels.wrap(this.postHolder.mChannelPicker.getSelectedChannel()));
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    public void onShow(Bundle previousResult) {
        super.onShow(previousResult);
        VineChannel selectedChannel = (VineChannel) Parcels.unwrap(previousResult.getParcelable("selected_channel"));
        if (selectedChannel != null) {
            selectedChannel.colorHex = ColorUtils.parseColorHex(selectedChannel.backgroundColor, 0);
            this.postHolder.mChannelPicker.setSelectedChannel(selectedChannel);
            this.mViewHolder.mVineToggleRow.setChecked(true);
        }
    }

    @Override // co.vine.android.share.screens.ShareScreenHelper
    protected void onShowAnimationStart() {
        setExternalNetworkShareTargetRowsEnabled(isVineSelected());
    }

    public void ensureVmAndChannelPickerContainerState(boolean withAnimation) {
        int i = avcodec.AV_CODEC_ID_JV;
        if (this.mIsPrivateUser) {
            Point windowSize = SystemUtil.getDisplaySize(this.mContext);
            Point channelPickerContainerSize = ViewUtil.getAtMostSize(this.postHolder.mChannelPickerContainer, windowSize.x, windowSize.y);
            this.postHolder.mVmAndChannelPickerContainer.setY(channelPickerContainerSize.y);
        } else {
            if (!isVineSelected() && this.mIsVmAndChannelPickerContainerVisible) {
                if (!withAnimation) {
                    i = 0;
                }
                getVmAndCommentContainerTranslationAnimator(false, i).start();
                this.mIsVmAndChannelPickerContainerVisible = false;
                return;
            }
            if (isVineSelected() && !this.mIsVmAndChannelPickerContainerVisible) {
                if (withAnimation) {
                    getVmAndCommentContainerTranslationAnimator(true, avcodec.AV_CODEC_ID_JV).start();
                } else {
                    this.postHolder.mVmAndChannelPickerContainer.setY(0.0f);
                }
                this.mIsVmAndChannelPickerContainerVisible = true;
            }
        }
    }

    static final class PostShareScreenHolder {
        public final ChannelPickerRow mChannelPicker;
        public final ViewGroup mChannelPickerContainer;
        public final ViewGroup mVmAndChannelPickerContainer;

        public PostShareScreenHolder(View view) {
            this.mChannelPicker = (ChannelPickerRow) view.findViewById(R.id.channel_picker);
            this.mVmAndChannelPickerContainer = (ViewGroup) view.findViewById(R.id.vm_and_channel_container);
            this.mChannelPickerContainer = (ViewGroup) view.findViewById(R.id.channel_picker_container);
        }
    }
}
