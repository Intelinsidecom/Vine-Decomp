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
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.animation.SimpleAnimatorListener;
import co.vine.android.client.AppController;
import co.vine.android.share.widgets.FakeActionBar;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.ViewUtil;
import co.vine.android.views.SimpleTextWatcher;
import co.vine.android.widget.ListenableEditText;
import co.vine.android.widget.Typefaces;

/* loaded from: classes.dex */
public class CommentScreen extends Screen {
    private ViewGroup mActionBarActionView;
    private View mActionBarBackView;
    private Button mActionButton;
    private int mActionButtonTextColor;
    private int mActionButtonTextTooLongColor;
    private String mCachedComment;
    private final ListenableEditText mEditText;
    private TextView mRemainingCharacterCount;
    private int mRemainingCharacterCountTextColor;
    private int mRemainingCharacterCountTextTooLongColor;
    private ScreenManager mScreenManager;

    public CommentScreen(Context context) {
        this(context, null);
    }

    public CommentScreen(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        inflater.inflate(R.layout.comment_layout, this);
        this.mEditText = (ListenableEditText) findViewById(R.id.comment_edit_text);
        this.mActionButtonTextColor = -1;
        this.mActionButtonTextTooLongColor = getResources().getColor(R.color.white_fifty_percent);
        this.mRemainingCharacterCountTextColor = getResources().getColor(R.color.white_fifty_percent);
        this.mRemainingCharacterCountTextTooLongColor = -1;
        this.mEditText.setHorizontallyScrolling(false);
        this.mEditText.setMaxLines(Integer.MAX_VALUE);
        this.mEditText.addTextChangedListener(new SimpleTextWatcher() { // from class: co.vine.android.share.screens.CommentScreen.1
            @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                CommentScreen.this.ensureActionBarState();
            }
        });
        this.mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: co.vine.android.share.screens.CommentScreen.2
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != 6) {
                    return false;
                }
                if (CommentScreen.this.mEditText.getText().length() <= 110) {
                    Bundle result = new Bundle();
                    result.putString("comment", CommentScreen.this.mEditText.getText().toString());
                    CommentScreen.this.mScreenManager.setScreenResult(result);
                    CommentScreen.this.hideKeyboardAndPopScreen();
                }
                return true;
            }
        });
        this.mEditText.setEditTextBackListener(new ListenableEditText.EditTextBackListener() { // from class: co.vine.android.share.screens.CommentScreen.3
            @Override // co.vine.android.widget.ListenableEditText.EditTextBackListener
            public void onBackPressed() {
                CommentScreen.this.mEditText.setText(CommentScreen.this.mCachedComment);
                CommentScreen.this.hideKeyboardAndPopScreen();
            }
        });
    }

    @Override // co.vine.android.share.screens.Screen
    public void onInitialize(ScreenManager screenManager, AppController appController, Bundle initialData) throws Resources.NotFoundException {
        this.mScreenManager = screenManager;
        Point windowSize = SystemUtil.getDisplaySize(getContext());
        int statusBarHeight = ViewUtil.getStatusBarHeightPx(getResources());
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        params.height = (windowSize.y - windowSize.x) - statusBarHeight;
        params.gravity = 80;
        FakeActionBar fakeActionBar = screenManager.getFakeActionBar();
        this.mActionBarBackView = fakeActionBar.inflateBackView(R.layout.fake_action_bar_back_arrow);
        this.mActionBarActionView = (ViewGroup) fakeActionBar.inflateActionView(R.layout.comment_screen_action_bar_action);
        this.mRemainingCharacterCount = (TextView) this.mActionBarActionView.findViewById(R.id.characters_remaining);
        this.mActionButton = (Button) this.mActionBarActionView.findViewById(R.id.action);
        this.mActionButton.setTypeface(Typefaces.get(getContext()).getContentTypeface(Typefaces.get(getContext()).mediumContentBold.getStyle(), 3));
        this.mActionButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.share.screens.CommentScreen.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Bundle result = new Bundle();
                result.putString("comment", CommentScreen.this.mEditText.getText().toString());
                CommentScreen.this.mScreenManager.setScreenResult(result);
                CommentScreen.this.hideKeyboardAndPopScreen();
            }
        });
    }

    @Override // co.vine.android.share.screens.Screen
    public void onBindFakeActionBar(FakeActionBar fakeActionBar) throws Resources.NotFoundException {
        Resources resources = getResources();
        String comment = resources.getString(R.string.comment);
        fakeActionBar.setLabelText(comment);
        fakeActionBar.setBackView(this.mActionBarBackView);
        fakeActionBar.setActionView(this.mActionBarActionView);
    }

    @Override // co.vine.android.share.screens.Screen
    public boolean onBack() {
        this.mEditText.setText(this.mCachedComment);
        return false;
    }

    @Override // co.vine.android.share.screens.Screen
    public AnimatorSet getShowAnimatorSet() {
        ValueAnimator alpha = ValueAnimator.ofFloat(0.8f, 1.0f);
        alpha.addListener(new SimpleAnimatorListener() { // from class: co.vine.android.share.screens.CommentScreen.5
            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
                ViewUtil.enableAndShow(CommentScreen.this);
                CommentScreen.this.mCachedComment = CommentScreen.this.mEditText.getText().toString();
                CommentScreen.this.ensureActionBarState();
            }

            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                CommentScreen.this.focus();
            }
        });
        alpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: co.vine.android.share.screens.CommentScreen.6
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float valueY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                CommentScreen.this.setAlpha(valueY);
            }
        });
        alpha.setTarget(this);
        alpha.setDuration(250L);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alpha);
        return animatorSet;
    }

    @Override // co.vine.android.share.screens.Screen
    public AnimatorSet getHideAnimatorSet() {
        ValueAnimator alpha = ValueAnimator.ofFloat(1.0f, 0.8f);
        alpha.addListener(new SimpleAnimatorListener() { // from class: co.vine.android.share.screens.CommentScreen.7
            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
                CommonUtil.setSoftKeyboardVisibility(CommentScreen.this.getContext(), CommentScreen.this.mEditText, false);
                CommentScreen.this.mEditText.clearFocus();
            }

            @Override // co.vine.android.animation.SimpleAnimatorListener, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                ViewUtil.disableAndHide(CommentScreen.this);
            }
        });
        alpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: co.vine.android.share.screens.CommentScreen.8
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float valueY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                CommentScreen.this.setAlpha(valueY);
            }
        });
        alpha.setTarget(this);
        alpha.setDuration(10L);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alpha);
        return animatorSet;
    }

    @Override // co.vine.android.share.screens.Screen
    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        return true;
    }

    public String getComment() {
        return this.mEditText.getText().toString();
    }

    public void focus() {
        if (this.mEditText.requestFocus()) {
            Activity activity = (Activity) getContext();
            activity.getWindow().setSoftInputMode(32);
            CommonUtil.setSoftKeyboardVisibility(getContext(), this.mEditText, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideKeyboardAndPopScreen() {
        this.mEditText.clearFocus();
        CommonUtil.setSoftKeyboardVisibility(getContext(), this, false);
        this.mScreenManager.popScreen();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureActionBarState() {
        String text = this.mEditText.getText().toString();
        this.mRemainingCharacterCount.setText(String.valueOf(110 - text.length()));
        this.mActionButton.setText(TextUtils.isEmpty(this.mCachedComment) ? R.string.add : R.string.done);
        if (text.length() <= 110) {
            this.mActionButton.setEnabled(true);
            this.mActionButton.setTextColor(this.mActionButtonTextColor);
            this.mRemainingCharacterCount.setTextColor(this.mRemainingCharacterCountTextColor);
        } else {
            this.mActionButton.setEnabled(false);
            this.mActionButton.setTextColor(this.mActionButtonTextTooLongColor);
            this.mRemainingCharacterCount.setTextColor(this.mRemainingCharacterCountTextTooLongColor);
        }
        if (TextUtils.isEmpty(text) && TextUtils.isEmpty(this.mCachedComment)) {
            ViewUtil.disableAndHide(this.mActionBarActionView);
        } else {
            ViewUtil.enableAndShow(this.mActionBarActionView);
        }
    }

    @Override // co.vine.android.share.screens.Screen
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString("extra_comment", getComment());
    }

    @Override // co.vine.android.share.screens.Screen
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        String comment = savedInstanceState.getString("extra_comment");
        if (!TextUtils.isEmpty(comment)) {
            this.mEditText.setText(comment);
        }
    }
}
