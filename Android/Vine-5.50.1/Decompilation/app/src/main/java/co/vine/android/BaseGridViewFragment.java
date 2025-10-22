package co.vine.android;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.api.VinePost;
import co.vine.android.client.AppController;
import co.vine.android.util.MediaUtil;

/* loaded from: classes.dex */
public class BaseGridViewFragment extends BaseFragment {
    protected RecyclerView.Adapter mAdapter;
    protected AppController mAppController;
    private int mColNum;
    protected GridLayoutManager mGridLayoutManager;
    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mRefreshLayout;

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            this.mColNum = getArguments().getInt("columns");
            this.mGridLayoutManager = new GridLayoutManager(getActivity(), this.mColNum);
            this.mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: co.vine.android.BaseGridViewFragment.1
                @Override // android.support.v7.widget.GridLayoutManager.SpanSizeLookup
                public int getSpanSize(int position) {
                    switch (BaseGridViewFragment.this.mAdapter.getItemViewType(position)) {
                        case 0:
                            return BaseGridViewFragment.this.mColNum;
                        case 1:
                            return 1;
                        default:
                            return 0;
                    }
                }
            });
        }
        this.mAppController = AppController.getInstance(getActivity());
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.import_video_fragment, container, false);
        this.mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        this.mRefreshLayout.setEnabled(true);
        this.mRefreshLayout.setProgressViewOffset(false, 0, 0);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        this.mRecyclerView.setLayoutManager(this.mGridLayoutManager);
        int spacingInPixels = MediaUtil.convertDpToPixel(9, getActivity());
        this.mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        return view;
    }

    public void updateSelection(String url, String localPath, String sourcePostId) {
        if (getActivity() instanceof ImportVideoTabActivity) {
            ((ImportVideoTabActivity) getActivity()).updateSelection(url, localPath, sourcePostId);
        }
    }

    public void updateSelection() {
        if (getActivity() instanceof DownloadVineActivity) {
            ((DownloadVineActivity) getActivity()).updateSelection();
        }
    }

    public void setLocalPath(String path, String localPath) {
        if (getActivity() instanceof ImportVideoTabActivity) {
            ((ImportVideoTabActivity) getActivity()).setLocalPath(path, localPath);
        } else {
            if (getActivity() instanceof DownloadVineActivity) {
            }
        }
    }

    public void updateAdapter() {
        this.mAdapter.notifyDataSetChanged();
    }

    public int getVideoOrder(String path) {
        if (getActivity() instanceof ImportVideoTabActivity) {
            return ((ImportVideoTabActivity) getActivity()).getVideoOrder(path);
        }
        if (getActivity() instanceof DownloadVineActivity) {
        }
        return -1;
    }

    public void toPreviewFragment(String videoUrl, VinePost vinePost) {
        if (getActivity() instanceof ImportVideoTabActivity) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(((ImportVideoTabActivity) getActivity()).getPreviewContainerID(), ImportScreenVideoPreview.newInstance(videoUrl, vinePost, vinePost == null)).addToBackStack("preview");
            ft.commit();
        }
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override // android.support.v7.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position >= 0) {
                int column = position % BaseGridViewFragment.this.mColNum;
                outRect.left = (this.space * column) / BaseGridViewFragment.this.mColNum;
                outRect.right = this.space - (((column + 1) * this.space) / BaseGridViewFragment.this.mColNum);
                if (position >= BaseGridViewFragment.this.mColNum) {
                    outRect.top = this.space;
                    return;
                }
                return;
            }
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
        }
    }
}
