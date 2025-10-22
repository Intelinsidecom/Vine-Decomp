package co.vine.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import co.vine.android.R;
import co.vine.android.views.SdkListView;

/* loaded from: classes.dex */
public class ConversationList extends SdkListView implements AbsListView.OnScrollListener {
    private View mGetMoreHeaderProgressContainer;
    private GetMoreListener mGetMoreListener;
    private View mProgressView;
    private AbsListView.OnScrollListener mScrollListener;

    public interface GetMoreListener {
        void getMore();
    }

    public ConversationList(Context context) {
        super(context);
    }

    public ConversationList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConversationList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // android.widget.ListView, android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        super.setOnScrollListener(this);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        this.mGetMoreHeaderProgressContainer = inflater.inflate(R.layout.conversation_header, (ViewGroup) null);
        this.mProgressView = this.mGetMoreHeaderProgressContainer.findViewById(R.id.header_content);
        addHeaderView(this.mGetMoreHeaderProgressContainer, null, false);
    }

    @Override // android.widget.AbsListView
    public void setOnScrollListener(AbsListView.OnScrollListener l) {
        this.mScrollListener = l;
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (visibleItemCount != 0) {
            if (this.mGetMoreListener != null && firstVisibleItem == 0 && this.mProgressView.getVisibility() != 8) {
                View child = getChildAt(0);
                if (child == this.mGetMoreHeaderProgressContainer) {
                    this.mGetMoreListener.getMore();
                }
            }
            if (this.mScrollListener != null) {
                this.mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }
    }

    public void setGetMoreListener(GetMoreListener listener) {
        this.mGetMoreListener = listener;
    }

    public void deactivateRefresh(boolean permanently) {
        this.mProgressView.setVisibility(8);
        if (permanently) {
            removeHeaderView(this.mGetMoreHeaderProgressContainer);
        }
    }

    public void deactivateRefresh() {
        deactivateRefresh(false);
    }

    public void showProgress() {
        if (this.mProgressView.getVisibility() != 8) {
            this.mProgressView.setVisibility(0);
        }
    }

    public void hideProgress() {
        this.mProgressView.setVisibility(4);
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.View
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        if (getCount() > 0) {
            setSelection(getCount() - 1);
        }
    }
}
