package co.vine.android;

import co.vine.android.client.AppController;
import co.vine.android.model.TagModel;
import co.vine.android.model.VineTag;
import co.vine.android.model.impl.VineModelFactory;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.suggestions.SuggestionsComponent;
import co.vine.android.widget.PopupEditText;
import java.util.List;

/* loaded from: classes.dex */
public abstract class TypeAheadEditTextListener implements PopupEditText.PopupEditTextListener {
    private AppController mAppController;

    abstract void onTagsAvailable(TagModel tagModel, String str);

    public TypeAheadEditTextListener(AppController controller) {
        this.mAppController = controller;
    }

    @Override // co.vine.android.widget.PopupEditText.PopupEditTextListener
    public void onFiltering(CharSequence constraint) {
        fetchTypeAheadTokens(constraint, false);
    }

    @Override // co.vine.android.widget.PopupEditText.PopupEditTextListener
    public void onItemsExhausted(CharSequence currentToken) {
        fetchTypeAheadTokens(currentToken, true);
    }

    private void fetchTypeAheadTokens(CharSequence token, boolean forceFetch) {
        if (SuggestionsComponent.shouldFetchUsersTypeahead(token)) {
            Components.suggestionsComponent().fetchUserTypeahead(this.mAppController, token.toString().substring(1, token.length()));
        } else if (SuggestionsComponent.shouldFetchTagsTypeahead(token)) {
            String query = token.toString().substring(1, token.length());
            TagModel tagModel = VineModelFactory.getModelInstance().getTagModel();
            List<VineTag> tags = tagModel.getTagsForQuery(query);
            if (tags == null || forceFetch) {
                Components.suggestionsComponent().fetchTags(this.mAppController, query);
            }
            onTagsAvailable(tagModel, query);
        }
    }
}
