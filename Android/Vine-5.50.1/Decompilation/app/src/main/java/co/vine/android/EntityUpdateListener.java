package co.vine.android;

import android.app.Activity;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import co.vine.android.ComposeFilter;
import co.vine.android.ComposeTokenizer;
import co.vine.android.api.VineUser;
import co.vine.android.client.AppController;
import co.vine.android.model.ModelEvents;
import co.vine.android.model.TagModel;
import co.vine.android.model.TimelineModel;
import co.vine.android.model.VineTag;
import co.vine.android.model.impl.TimelineDetails;
import co.vine.android.model.impl.VineModelFactory;
import co.vine.android.provider.UserSuggestionsProvider;
import co.vine.android.service.components.suggestions.SuggestionsActionListener;
import co.vine.android.service.components.suggestions.SuggestionsComponent;
import co.vine.android.widget.PopupEditText;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class EntityUpdateListener implements TextWatcher, Filterable, TextView.OnEditorActionListener {
    private Activity mActivity;
    private AppController mAppController;
    private PopupEditText mEdit;
    private Filter mFilter;
    private BaseAdapterFragment mFragment;
    private LifetimeSafeModelListener mModelListener;
    private EntitySuggestionsActionListener mSuggestionsListener;
    private TagsAutoCompleteAdapter mTagsAdapter;
    private ComposeTokenizer.TokenListener mTokenListener;
    private CursorAdapter mTypeaheadAdapter;
    private UsersAutoCompleteAdapter mUsersAdapter;

    public EntityUpdateListener(Activity activity, AppController appController, BaseAdapterFragment fragment, PopupEditText editText) {
        this.mSuggestionsListener = new EntitySuggestionsActionListener();
        this.mActivity = activity;
        this.mAppController = appController;
        this.mFragment = fragment;
        this.mEdit = editText;
        this.mModelListener = new LifetimeSafeModelListener(this.mFragment, new ModelListener());
        VineModelFactory.getModelInstance().getModelEvents().addListener(this.mModelListener);
        this.mTokenListener = new TokenListener();
        this.mUsersAdapter = new UsersAutoCompleteAdapter(activity, this.mAppController);
        this.mTagsAdapter = new TagsAutoCompleteAdapter(activity, this.mAppController);
        this.mTypeaheadAdapter = this.mTagsAdapter;
        this.mEdit.setAdapter(this.mTypeaheadAdapter);
        this.mEdit.setOnEditorActionListener(this);
        ComposeTokenizer tokenizer = new ComposeTokenizer(new TokenListener());
        this.mEdit.setTokenizer(tokenizer, this, SuggestionsComponent.getTypeaheadThrottle());
        this.mEdit.setPopupEditTextListener(new EditTextListener());
        this.mEdit.addTextChangedListener(this);
    }

    public SuggestionsActionListener getSuggestionsActionListener() {
        return this.mSuggestionsListener;
    }

    @Override // android.widget.TextView.OnEditorActionListener
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return true;
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable s) {
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        if (this.mFilter == null) {
            long activeId = this.mAppController.getActiveId();
            this.mFilter = new ComposeFilter(this.mActivity, new ComposeFilter.CursorAdapterProvider() { // from class: co.vine.android.EntityUpdateListener.1
                @Override // co.vine.android.ComposeFilter.CursorAdapterProvider
                public CursorAdapter provideAdapter() {
                    return EntityUpdateListener.this.mTypeaheadAdapter;
                }
            }, activeId);
        }
        return this.mFilter;
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public final class TokenListener implements ComposeTokenizer.TokenListener {
        public TokenListener() {
        }

        @Override // co.vine.android.ComposeTokenizer.TokenListener
        public void onUserTokenFound() {
            if (EntityUpdateListener.this.mTypeaheadAdapter != EntityUpdateListener.this.mUsersAdapter) {
                EntityUpdateListener.this.mTypeaheadAdapter = EntityUpdateListener.this.mUsersAdapter;
                EntityUpdateListener.this.mEdit.setAdapter(EntityUpdateListener.this.mTypeaheadAdapter);
            }
        }

        @Override // co.vine.android.ComposeTokenizer.TokenListener
        public void onTagTokenFound() {
            if (EntityUpdateListener.this.mTypeaheadAdapter != EntityUpdateListener.this.mTagsAdapter) {
                EntityUpdateListener.this.mTypeaheadAdapter = EntityUpdateListener.this.mTagsAdapter;
                EntityUpdateListener.this.mEdit.setAdapter(EntityUpdateListener.this.mTypeaheadAdapter);
            }
        }
    }

    private final class EntitySuggestionsActionListener extends SuggestionsActionListener {
        private EntitySuggestionsActionListener() {
        }

        @Override // co.vine.android.service.components.suggestions.SuggestionsActionListener
        public void onGetUserTypeAheadComplete(String reqId, int statusCode, String reasonPhrase, String query, ArrayList<VineUser> users) {
            if (EntityUpdateListener.this.mFragment.removeRequest(reqId) != null && statusCode == 200 && !users.isEmpty()) {
                UserSuggestionsProvider.addRealtimeUserSuggestions(query, users);
                EntityUpdateListener.this.mEdit.performFilter(false);
            }
        }
    }

    private final class ModelListener implements ModelEvents.ModelListener {
        private ModelListener() {
        }

        @Override // co.vine.android.model.ModelEvents.ModelListener
        public void onTagsAdded(TagModel tagModel, String query) {
            EntityUpdateListener.this.addTags(tagModel, query);
        }

        @Override // co.vine.android.model.ModelEvents.ModelListener
        public void onTimelineUpdated(TimelineModel timelineModel, TimelineDetails timelineDetails) {
        }
    }

    private final class EditTextListener extends TypeAheadEditTextListener {
        public EditTextListener() {
            super(EntityUpdateListener.this.mAppController);
        }

        @Override // co.vine.android.TypeAheadEditTextListener
        public void onTagsAvailable(TagModel tagModel, String query) {
            EntityUpdateListener.this.addTags(tagModel, query);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addTags(TagModel tagModel, String query) {
        List<VineTag> tags = tagModel.getTagsForQuery(query);
        if (tags != null && !tags.isEmpty()) {
            this.mEdit.performFilter(false);
        }
    }
}
