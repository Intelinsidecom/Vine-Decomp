package co.vine.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import co.vine.android.views.SquareMatch;

/* loaded from: classes.dex */
public class SquareRelativeLayout extends RelativeLayout implements SquareMatch.SquareMatchView, Runnable {
    private SquareMatch.SquareMatchRules mSpec;

    public SquareRelativeLayout(Context context) {
        super(context);
        this.mSpec = SquareMatch.SquareMatchRules.MATCH_LESS;
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mSpec = SquareMatch.SquareMatchRules.MATCH_LESS;
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mSpec = SquareMatch.SquareMatchRules.MATCH_LESS;
    }

    @Override // co.vine.android.views.SquareMatch.SquareMatchView
    public SquareMatch.SquareMatchRules getMatchSpec() {
        return this.mSpec;
    }

    @Override // java.lang.Runnable
    public void run() {
        SquareMatch.setMatchingLayoutAction(this);
    }

    @Override // co.vine.android.views.SquareMatch.SquareMatchView
    public Runnable getMatchLayoutRunnable() {
        return this;
    }

    @Override // co.vine.android.views.SquareMatch.SquareMatchView
    public void setMeasuredDimension(int size) {
        setMeasuredDimension(size, size);
    }

    public void setMatchRule(SquareMatch.SquareMatchRules spec) {
        this.mSpec = spec;
    }

    @Override // android.widget.RelativeLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        SquareMatch.setupSquareMatchView(this);
    }
}
