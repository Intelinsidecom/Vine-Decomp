package co.vine.android.share.screens;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import co.vine.android.R;
import co.vine.android.animation.SimpleAnimatorListener;
import co.vine.android.api.VineChannel;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.share.adapters.ChannelsAdapter;
import co.vine.android.share.providers.ChannelProvider;
import co.vine.android.share.widgets.FakeActionBar;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.ViewUtil;
import co.vine.android.widget.TypefacesTextView;
import java.util.List;
import java.util.Map;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public class ChannelPickerScreen extends Screen {
    private final ChannelsAdapter mChannelsAdapter;
    private ScreenManager mScreenManager;

    public ChannelPickerScreen(Context context) {
        this(context, null);
    }

    public ChannelPickerScreen(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChannelPickerScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        inflater.inflate(R.layout.channel_picker_screen, this);
        ListView listView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.list_view_empty);
        this.mChannelsAdapter = new ChannelsAdapter(context);
        TypefacesTextView emptyTextView = (TypefacesTextView) emptyView.findViewById(R.id.empty_text);
        emptyTextView.setText(getResources().getString(R.string.failed_to_load_channels));
        listView.setAdapter((ListAdapter) this.mChannelsAdapter);
        listView.setEmptyView(emptyView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: co.vine.android.share.screens.ChannelPickerScreen.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = ChannelPickerScreen.this.mChannelsAdapter.getItem(position);
                if (item instanceof VineChannel) {
                    VineChannel channel = (VineChannel) item;
                    Bundle result = new Bundle();
                    result.putParcelable("selected_channel", Parcels.wrap(channel));
                    ChannelPickerScreen.this.mScreenManager.setScreenResult(result);
                    ChannelPickerScreen.this.mScreenManager.popScreen();
                }
            }
        });
    }

    @Override // co.vine.android.share.screens.Screen
    public void onInitialize(ScreenManager screenManager, AppController appController, Bundle initialData) {
        this.mScreenManager = screenManager;
        Point windowSize = SystemUtil.getDisplaySize(getContext());
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = windowSize.x;
        layoutParams.height = (windowSize.y - ViewUtil.getStatusBarHeightPx(getResources())) - screenManager.getFakeActionBar().calculateBounds(windowSize).y;
    }

    @Override // co.vine.android.share.screens.Screen
    public void onBindFakeActionBar(FakeActionBar fakeActionBar) {
        fakeActionBar.setBackView(this.mScreenManager.getFakeActionBar().inflateActionView(R.layout.fake_action_bar_down_arrow));
        fakeActionBar.setLabelText(getResources().getString(R.string.select_channel_title));
    }

    @Override // co.vine.android.share.screens.Screen
    public AnimatorSet getShowAnimatorSet() {
        Point windowSize = SystemUtil.getDisplaySize(getContext());
        ValueAnimator translation = ValueAnimator.ofFloat(windowSize.y, this.mScreenManager.getFakeActionBar().calculateBounds(windowSize).y);
        translation.addListener(new SimpleAnimatorListener() { // from class: co.vine.android.share.screens.ChannelPickerScreen.2
            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
                ViewUtil.enableAndShow(ChannelPickerScreen.this);
            }
        });
        translation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: co.vine.android.share.screens.ChannelPickerScreen.3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float valueY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                ChannelPickerScreen.this.setY(valueY);
            }
        });
        translation.setInterpolator(new DecelerateInterpolator());
        translation.setDuration(250L);
        translation.setTarget(this);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translation);
        return animatorSet;
    }

    @Override // co.vine.android.share.screens.Screen
    public AnimatorSet getHideAnimatorSet() {
        Point windowSize = SystemUtil.getDisplaySize(getContext());
        ValueAnimator translation = ValueAnimator.ofFloat(this.mScreenManager.getFakeActionBar().calculateBounds(windowSize).y, windowSize.y);
        translation.addListener(new SimpleAnimatorListener() { // from class: co.vine.android.share.screens.ChannelPickerScreen.4
            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                ViewUtil.disableAndHide(ChannelPickerScreen.this);
                ChannelPickerScreen.this.setY(0.0f);
            }
        });
        translation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: co.vine.android.share.screens.ChannelPickerScreen.5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float valueY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                ChannelPickerScreen.this.setY(valueY);
            }
        });
        translation.setInterpolator(new DecelerateInterpolator());
        translation.setDuration(250L);
        translation.setTarget(this);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translation);
        return animatorSet;
    }

    public void setChannelProvider(ChannelProvider channelProvider) {
        channelProvider.setOnDataFetchedListener(new ChannelProvider.OnDataFetchedListener() { // from class: co.vine.android.share.screens.ChannelPickerScreen.6
            @Override // co.vine.android.share.providers.ChannelProvider.OnDataFetchedListener
            public void onChannelsFetched(List<VineChannel> channels) {
                ChannelPickerScreen.this.mChannelsAdapter.replaceData(channels);
            }

            @Override // co.vine.android.share.providers.ChannelProvider.OnDataFetchedListener
            public void onChannelImagesFetched(Map<ImageKey, UrlImage> images) throws Resources.NotFoundException {
                ChannelPickerScreen.this.mChannelsAdapter.updateImages(images);
            }
        });
    }
}
