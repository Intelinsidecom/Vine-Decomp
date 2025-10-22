package co.vine.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import co.vine.android.ComposeFilter;
import co.vine.android.ComposeTokenizer;
import co.vine.android.api.VineComment;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VineUser;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppSessionListener;
import co.vine.android.model.ModelEvents;
import co.vine.android.model.TagModel;
import co.vine.android.model.TimelineModel;
import co.vine.android.model.VineTag;
import co.vine.android.model.impl.TimelineDetails;
import co.vine.android.model.impl.VineModelFactory;
import co.vine.android.provider.UserSuggestionsProvider;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.service.PendingAction;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.suggestions.SuggestionsActionListener;
import co.vine.android.service.components.suggestions.SuggestionsComponent;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.Util;
import co.vine.android.widget.ConversationList;
import co.vine.android.widget.PopupEditText;
import co.vine.android.widgets.PromptDialogSupportFragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class CommentsFragment extends BaseArrayListFragment implements TextWatcher, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, Filterable, TextView.OnEditorActionListener, ConversationList.GetMoreListener {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();
    private static ActionMode mActionMode;
    private String mAnchor;
    private CommentActionCallback mDeleteActionModeCallback;
    private PopupEditText mEdit;
    private boolean mFetched;
    private Filter mFilter;
    private LifetimeSafeModelListener mModelListener;
    private long mPostAuthorId;
    private long mPostId;
    private long mRepostId;
    private String mSelectedCommentId;
    private long mSelectedUserId;
    private String mSelectedUsername;
    private View mSelectedView;
    private ImageView mSendButton;
    private CommentActionCallback mSpamActionModeCallback;
    private CommentsSuggestionsActionListener mSuggestionsListener;
    private TagsAutoCompleteAdapter mTagsAdapter;
    private CursorAdapter mTypeaheadAdapter;
    private UsersAutoCompleteAdapter mUsersAdapter;
    private long mLastTopItemId = -1;
    private int mLastTopItemPixelOffset = 0;
    private int mNextPage = 1;
    private final PromptDialogSupportFragment.OnDialogDoneListener mBlockDoneListener = new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.CommentsFragment.1
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialogInterface, int id, int which) {
            switch (which) {
                case -2:
                    CommentsFragment.this.showReportingMenu();
                    break;
                case -1:
                    dialogInterface.dismiss();
                    break;
            }
        }
    };

    private enum CommentActionType {
        DELETE_ACTION,
        SPAM_ACTION
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showBlockUserDialog(int messageId) {
        PromptDialogSupportFragment p = PromptDialogSupportFragment.newInstance(0);
        p.setListener(this.mBlockDoneListener);
        p.setMessage(messageId);
        p.setPositiveButton(R.string.no);
        p.setNegativeButton(R.string.yes);
        p.show(getFragmentManager());
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DATE_FORMAT.applyPattern(getResources().getString(R.string.datetime_format_long));
        if (savedInstanceState != null && savedInstanceState.containsKey("fetched")) {
            this.mFetched = savedInstanceState.getBoolean("fetched");
        }
        setAppSessionListener(new CommentsAppSessionListener());
        this.mUsersAdapter = new UsersAutoCompleteAdapter(getActivity(), this.mAppController);
        this.mTagsAdapter = new TagsAutoCompleteAdapter(getActivity(), this.mAppController);
        this.mTypeaheadAdapter = this.mTagsAdapter;
        this.mDeleteActionModeCallback = createCommentActionCallback(CommentActionType.DELETE_ACTION);
        this.mSpamActionModeCallback = createCommentActionCallback(CommentActionType.SPAM_ACTION);
        this.mSuggestionsListener = new CommentsSuggestionsActionListener();
        this.mModelListener = new LifetimeSafeModelListener(this, new ModelListener());
        VineModelFactory.getModelInstance().getModelEvents().addListener(this.mModelListener);
        setHasOptionsMenu(true);
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        VineModelFactory.getModelInstance().getModelEvents().removeListener(this.mModelListener);
        super.onDestroy();
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return createView(inflater, R.layout.comments_thread, container);
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PopupEditText edit = (PopupEditText) view.findViewById(R.id.edit_reply);
        edit.setOnEditorActionListener(this);
        edit.setAdapter(this.mTypeaheadAdapter);
        ComposeTokenizer tokenizer = new ComposeTokenizer(new TokenListener());
        edit.setTokenizer(tokenizer, this, SuggestionsComponent.getTypeaheadThrottle());
        edit.setPopupEditTextListener(new EditTextListener());
        this.mEdit = edit;
        this.mEdit.addTextChangedListener(this);
        Bundle args = getArguments();
        this.mPostId = args.getLong("post_id");
        this.mRepostId = args.getLong("repost_id");
        this.mPostAuthorId = args.getLong("post_author_id");
        this.mSendButton = (ImageView) view.findViewById(R.id.send_button);
        this.mSendButton.setOnClickListener(this);
        this.mSendButton.setColorFilter((-16777216) | getResources().getColor(R.color.btn_pressed_fill), PorterDuff.Mode.SRC_ATOP);
        this.mListView.setOnItemLongClickListener(this);
        this.mListView.setDividerHeight(0);
        this.mListView.setOnItemClickListener(this);
        this.mAdapter = new CommentsAdapter(getActivity(), this.mPostId, this.mAppController);
        ((ConversationList) this.mListView).setGetMoreListener(this);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        boolean hideKeyboard = getArguments().getBoolean("hide_keyboard", false);
        if (!hideKeyboard) {
            this.mEdit.requestFocus();
            Util.setSoftKeyboardVisibility(getActivity(), this.mEdit, true);
        }
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 0) {
            long commentId = data.getLongExtra("commentId", -1L);
            String event = data.getStringExtra("event");
            boolean blocked = data.getBooleanExtra("blocked", false);
            if (commentId != -1 && !TextUtils.isEmpty(event)) {
                ((CommentsAdapter) this.mAdapter).spamComment(commentId);
                if (this.mAdapter.getCount() == 0) {
                    showSadface(false);
                    return;
                }
                return;
            }
            if (blocked) {
                Util.showDefaultToast(getActivity(), getString(R.string.user_blocked, this.mSelectedUsername));
            }
        }
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mAdapter.isEmpty() && !this.mFetched) {
            fetchContent();
        }
        Components.suggestionsComponent().addListener(this.mSuggestionsListener);
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        Components.suggestionsComponent().removeListener(this.mSuggestionsListener);
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mention) {
            insertText("@");
        } else if (id == R.id.tag) {
            insertText("#");
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void insertText(String toInsert) {
        if (this.mEdit != null) {
            int selStart = this.mEdit.getSelectionStart();
            int selEnd = this.mEdit.getSelectionEnd();
            if (this.mEdit.getText() != null && selStart >= 0 && selEnd <= this.mEdit.length()) {
                this.mEdit.getText().replace(selStart, selEnd, toInsert);
            }
        }
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fetched", this.mFetched);
    }

    private void fetchContent() {
        if (!hasPendingRequest()) {
            this.mFetched = true;
            addRequest(this.mAppController.fetchComments(this.mPostId, this.mNextPage, this.mAnchor, 20));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveTopItemInfo() {
        if (this.mAdapter.getCount() != 0) {
            this.mLastTopItemId = this.mAdapter.getItemId(this.mListView.getFirstVisiblePosition());
            View topChild = this.mListView.getChildAt(this.mListView.getFirstVisiblePosition() + this.mListView.getHeaderViewsCount());
            if (topChild != null) {
                this.mLastTopItemPixelOffset = topChild.getTop();
            }
        }
    }

    @Override // co.vine.android.BaseArrayListFragment
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CommentViewHolder h = (CommentViewHolder) view.getTag();
        ChannelActivity.startProfile(getActivity(), h.userId, "Comments List");
    }

    private boolean validate(EditText editText) {
        return editText != null && editText.length() > 0 && editText.length() <= 140;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.send_button) {
            sendComment();
        } else {
            CommentViewHolder h = (CommentViewHolder) view.getTag();
            ChannelActivity.startProfile(getActivity(), h.userId, "Comments List");
        }
    }

    public void saveCurrentCommentViewState(View view) {
        this.mSelectedView = view;
        this.mSelectedView.setSelected(true);
        this.mSelectedCommentId = ((CommentViewHolder) view.getTag()).commentId;
        this.mSelectedUserId = ((CommentViewHolder) view.getTag()).userId;
        this.mSelectedUsername = ((CommentViewHolder) view.getTag()).username;
    }

    @Override // android.widget.AdapterView.OnItemLongClickListener
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mActionMode != null || !(view.getTag() instanceof CommentViewHolder)) {
            return false;
        }
        CommentViewHolder commentViewHolder = (CommentViewHolder) view.getTag();
        long userId = this.mAppController.getActiveId();
        saveCurrentCommentViewState(view);
        if (userId == commentViewHolder.userId || userId == this.mPostAuthorId) {
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(this.mDeleteActionModeCallback);
        } else {
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(this.mSpamActionModeCallback);
        }
        return true;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() != R.id.edit_reply) {
            return false;
        }
        if (actionId == 4) {
            sendComment();
        }
        return true;
    }

    private void sendComment() {
        if (validate(this.mEdit)) {
            ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.ProgressDialogTheme);
            dialog.setMessage(getString(R.string.comments_posting));
            dialog.setProgressStyle(0);
            dialog.show();
            this.mProgressDialog = dialog;
            addRequest(this.mAppController.postComment(this.mPostId, this.mRepostId, this.mAppController.getActiveSession(), this.mEdit.getText().toString(), this.mEdit.getEntities()));
            this.mEdit.getText().clear();
            Util.setSoftKeyboardVisibility(getActivity(), this.mEdit, false);
            this.mEdit.clearFocus();
            return;
        }
        if (this.mEdit != null && this.mEdit.length() > 140) {
            Util.showDefaultToast(getActivity(), R.string.comment_length_exceeded);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createReply() {
        int insertPosition = this.mEdit.getSelectionEnd() > 0 ? this.mEdit.getSelectionEnd() : this.mEdit.getText().length();
        if (this.mEdit.getSelectionEnd() != this.mEdit.getSelectionStart()) {
            this.mEdit.append(" ");
            insertPosition++;
        }
        this.mEdit.putEntity(insertPosition, insertPosition, new VineEntity("mention", this.mSelectedUsername, null, insertPosition, this.mSelectedUsername.length() + insertPosition, this.mSelectedUserId));
        this.mEdit.append(" ");
        this.mEdit.requestFocusFromTouch();
        CommonUtil.setSoftKeyboardVisibility(getActivity(), this.mEdit, true);
    }

    private abstract class CommentActionCallback implements ActionMode.Callback {
        private PromptDialogSupportFragment.OnDialogDoneListener mDialogDoneListener;
        private int mDialogMessageResourceId;
        private int mMenuResourceId;

        public abstract void performAction();

        private CommentActionCallback() {
            this.mDialogDoneListener = new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.CommentsFragment.CommentActionCallback.1
                @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
                public void onDialogDone(DialogInterface dialogInterface, int id, int which) {
                    switch (which) {
                        case -2:
                            CommentActionCallback.this.performAction();
                            break;
                        case -1:
                            dialogInterface.dismiss();
                            break;
                    }
                }
            };
        }

        public void setMenuResourceId(int mMenuResourceId) {
            this.mMenuResourceId = mMenuResourceId;
        }

        public void setDialogMessageResourceId(int mDialogMessageResourceId) {
            this.mDialogMessageResourceId = mDialogMessageResourceId;
        }

        @Override // android.support.v7.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(this.mMenuResourceId, menu);
            return true;
        }

        @Override // android.support.v7.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override // android.support.v7.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_reply) {
                CommentsFragment.this.createReply();
            } else {
                showDialog();
            }
            mode.finish();
            return true;
        }

        @Override // android.support.v7.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode mode) {
            if (CommentsFragment.this.mSelectedView != null) {
                CommentsFragment.this.mSelectedView.setSelected(false);
            }
            ActionMode unused = CommentsFragment.mActionMode = null;
        }

        public void showDialog() {
            PromptDialogSupportFragment p = PromptDialogSupportFragment.newInstance(0);
            p.setListener(this.mDialogDoneListener);
            p.setMessage(this.mDialogMessageResourceId);
            p.setPositiveButton(R.string.no);
            p.setNegativeButton(R.string.yes);
            p.show(CommentsFragment.this.getFragmentManager());
        }
    }

    private class CommentDeleteActionCallback extends CommentActionCallback {
        public CommentDeleteActionCallback(int menuId, int messageId) {
            super();
            setMenuResourceId(menuId);
            setDialogMessageResourceId(messageId);
        }

        @Override // co.vine.android.CommentsFragment.CommentActionCallback, android.support.v7.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            super.onCreateActionMode(mode, menu);
            MenuItem spamButton = menu.findItem(R.id.comment_action_spam);
            spamButton.setVisible(false);
            return true;
        }

        @Override // co.vine.android.CommentsFragment.CommentActionCallback
        public void performAction() {
            CommentsFragment.this.addRequest(CommentsFragment.this.mAppController.deleteComment(CommentsFragment.this.mPostId, CommentsFragment.this.mRepostId, CommentsFragment.this.mSelectedCommentId));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showReportingMenu() {
        Activity activity = getActivity();
        if (activity != null) {
            Intent i = ReportingActivity.getReportCommentIntent(getActivity(), this.mSelectedCommentId, this.mSelectedUserId, this.mSelectedUsername, this.mPostId);
            getActivity().startActivityForResult(i, 0);
        }
    }

    private class CommentSpamActionCallback extends CommentActionCallback {
        public CommentSpamActionCallback(int menuId, int messageId) {
            super();
            setMenuResourceId(menuId);
            setDialogMessageResourceId(messageId);
        }

        @Override // co.vine.android.CommentsFragment.CommentActionCallback, android.support.v7.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            super.onCreateActionMode(mode, menu);
            MenuItem spamButton = menu.findItem(R.id.comment_action_delete);
            spamButton.setVisible(false);
            return true;
        }

        @Override // co.vine.android.CommentsFragment.CommentActionCallback, android.support.v7.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_reply) {
                CommentsFragment.this.createReply();
            } else {
                CommentsFragment.this.showReportingMenu();
            }
            mode.finish();
            return true;
        }

        @Override // co.vine.android.CommentsFragment.CommentActionCallback
        public void performAction() {
            CommentsFragment.this.showReportingMenu();
        }
    }

    private CommentActionCallback createCommentActionCallback(CommentActionType type) {
        return type == CommentActionType.DELETE_ACTION ? new CommentDeleteActionCallback(R.menu.comment_actions, R.string.delete_comment_prompt) : new CommentSpamActionCallback(R.menu.comment_actions, R.string.block_or_report_comment);
    }

    private final class TokenListener implements ComposeTokenizer.TokenListener {
        private TokenListener() {
        }

        @Override // co.vine.android.ComposeTokenizer.TokenListener
        public void onUserTokenFound() {
            if (CommentsFragment.this.mTypeaheadAdapter != CommentsFragment.this.mUsersAdapter) {
                CommentsFragment.this.mTypeaheadAdapter = CommentsFragment.this.mUsersAdapter;
                CommentsFragment.this.mEdit.setAdapter(CommentsFragment.this.mTypeaheadAdapter);
            }
        }

        @Override // co.vine.android.ComposeTokenizer.TokenListener
        public void onTagTokenFound() {
            if (CommentsFragment.this.mTypeaheadAdapter != CommentsFragment.this.mTagsAdapter) {
                CommentsFragment.this.mTypeaheadAdapter = CommentsFragment.this.mTagsAdapter;
                CommentsFragment.this.mEdit.setAdapter(CommentsFragment.this.mTypeaheadAdapter);
            }
        }
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        if (this.mFilter == null) {
            long activeId = this.mAppController.getActiveId();
            this.mFilter = new ComposeFilter(getActivity(), new ComposeFilter.CursorAdapterProvider() { // from class: co.vine.android.CommentsFragment.2
                @Override // co.vine.android.ComposeFilter.CursorAdapterProvider
                public CursorAdapter provideAdapter() {
                    return CommentsFragment.this.mTypeaheadAdapter;
                }
            }, activeId);
        }
        return this.mFilter;
    }

    @Override // co.vine.android.widget.ConversationList.GetMoreListener
    public void getMore() {
        fetchContent();
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment
    public void showProgress(int fetchType) {
        super.showProgress(fetchType);
        if (fetchType == 1 && this.mNextPage > 0) {
            ((ConversationList) this.mListView).showProgress();
        }
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment
    public void hideProgress(int fetchType) {
        super.hideProgress(fetchType);
        if (fetchType == 1) {
            ((ConversationList) this.mListView).hideProgress();
        }
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (validate(this.mEdit)) {
            this.mSendButton.setColorFilter(getResources().getColor(R.color.vine_green) | ViewCompat.MEASURED_STATE_MASK);
        } else {
            this.mSendButton.setColorFilter(getResources().getColor(R.color.btn_pressed_fill) | ViewCompat.MEASURED_STATE_MASK);
        }
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable s) {
    }

    private final class CommentsSuggestionsActionListener extends SuggestionsActionListener {
        private CommentsSuggestionsActionListener() {
        }

        @Override // co.vine.android.service.components.suggestions.SuggestionsActionListener
        public void onGetUserTypeAheadComplete(String reqId, int statusCode, String reasonPhrase, String query, ArrayList<VineUser> users) {
            if (CommentsFragment.this.removeRequest(reqId) != null && statusCode == 200 && !users.isEmpty()) {
                UserSuggestionsProvider.addRealtimeUserSuggestions(query, users);
                CommentsFragment.this.mEdit.performFilter(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addTags(TagModel tagModel, String query) {
        List<VineTag> tags = tagModel.getTagsForQuery(query);
        if (tags != null && !tags.isEmpty()) {
            this.mEdit.performFilter(false);
        }
    }

    private final class ModelListener implements ModelEvents.ModelListener {
        private ModelListener() {
        }

        @Override // co.vine.android.model.ModelEvents.ModelListener
        public void onTagsAdded(TagModel tagModel, String query) {
            CommentsFragment.this.addTags(tagModel, query);
        }

        @Override // co.vine.android.model.ModelEvents.ModelListener
        public void onTimelineUpdated(TimelineModel timelineModel, TimelineDetails timelineDetails) {
        }
    }

    private final class CommentsAppSessionListener extends AppSessionListener {
        private CommentsAppSessionListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetCommentsComplete(String reqId, int statusCode, String reasonPhrase, int nextPage, String anchor, ArrayList<VineComment> comments) {
            PendingRequest req = CommentsFragment.this.removeRequest(reqId);
            if (req != null) {
                CommentsFragment.this.hideProgress(3);
                if (comments == null || comments.size() == 0) {
                    if (statusCode != 200) {
                        CommentsFragment.this.setEmptyStringMessage(R.string.failed_to_load_comments);
                    }
                    CommentsFragment.this.showSadface(false);
                } else {
                    CommentsFragment.this.hideSadface();
                }
                if (statusCode == 200) {
                    CommentsFragment.this.mNextPage = nextPage;
                    CommentsFragment.this.mAnchor = anchor;
                    CommentsFragment.this.saveTopItemInfo();
                    CommentsAdapter adapter = (CommentsAdapter) CommentsFragment.this.mAdapter;
                    adapter.mergeComments(comments);
                    int pos = adapter.getPositionForId(CommentsFragment.this.mLastTopItemId);
                    CommentsFragment.this.mListView.setSelectionFromTop(CommentsFragment.this.mListView.getHeaderViewsCount() + pos, CommentsFragment.this.mLastTopItemPixelOffset);
                }
                if (CommentsFragment.this.mFetched && (CommentsFragment.this.mNextPage <= 0 || comments == null || comments.size() == 0)) {
                    ((ConversationList) CommentsFragment.this.mListView).deactivateRefresh();
                }
                if (statusCode != 200) {
                    ((ConversationList) CommentsFragment.this.mListView).deactivateRefresh();
                }
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
            super.onPhotoImageLoaded(images);
            ((CommentsAdapter) CommentsFragment.this.mAdapter).updateProfileImages(images);
            CommentsFragment.this.mUsersAdapter.setUserImages(images);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPostCommentComplete(String reqId, int statusCode, String reasonPhrase, long postId, String comment, VineComment commentToMerge) {
            CommentsFragment.this.dismissProgressDialog();
            PendingRequest req = CommentsFragment.this.removeRequest(reqId);
            if (req != null && postId == CommentsFragment.this.mPostId) {
                final ListView listView = CommentsFragment.this.mListView;
                listView.post(new Runnable() { // from class: co.vine.android.CommentsFragment.CommentsAppSessionListener.1
                    @Override // java.lang.Runnable
                    public void run() {
                        listView.setSelection(listView.getCount() - 1);
                    }
                });
                if (statusCode != 200) {
                    CommentsFragment.this.mEdit.setText(comment);
                } else {
                    ((CommentsAdapter) CommentsFragment.this.mAdapter).addMyComment(commentToMerge);
                    CommentsFragment.this.hideSadface();
                }
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onDeleteCommentComplete(String reqId, int statusCode, String reasonPhrase, long commentId) {
            PendingRequest request = CommentsFragment.this.removeRequest(reqId);
            if (request != null && statusCode == 200) {
                if (((CommentViewHolder) CommentsFragment.this.mSelectedView.getTag()).userId != CommentsFragment.this.mAppController.getActiveId()) {
                    CommentsFragment.this.showBlockUserDialog(R.string.block_user_comment_delete_prompt);
                }
                ((CommentsAdapter) CommentsFragment.this.mAdapter).deleteComment(commentId);
                if (CommentsFragment.this.mAdapter.getCount() == 0) {
                    CommentsFragment.this.showSadface(false);
                }
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onCaptchaRequired(String reqId, String captchaUrl, PendingAction action) {
            PendingRequest req = CommentsFragment.this.removeRequest(reqId);
            if (req != null) {
                CommentsFragment.this.mPendingRequestHelper.onCaptchaRequired(CommentsFragment.this.getActivity(), reqId, action.actionCode, action.bundle, captchaUrl);
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhoneVerificationRequired(String reqId, String verifyMsg, PendingAction action) {
            PendingRequest req = CommentsFragment.this.removeRequest(reqId);
            if (req != null) {
                CommentsFragment.this.mPendingRequestHelper.onPhoneVerificationRequired(CommentsFragment.this.getActivity(), reqId, action.actionCode, action.bundle, verifyMsg);
            }
        }
    }

    @Override // co.vine.android.BaseFragment
    public AppNavigation.Views getAppNavigationView() {
        return AppNavigation.Views.COMMENTS;
    }

    private class EditTextListener extends TypeAheadEditTextListener {
        public EditTextListener() {
            super(CommentsFragment.this.mAppController);
        }

        @Override // co.vine.android.TypeAheadEditTextListener
        void onTagsAvailable(TagModel tagModel, String query) {
            CommentsFragment.this.addTags(tagModel, query);
        }
    }
}
