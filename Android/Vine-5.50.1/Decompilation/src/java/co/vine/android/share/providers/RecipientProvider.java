package co.vine.android.share.providers;

import android.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.PointerIconCompat;
import co.vine.android.ContactEntry;
import co.vine.android.api.VineRecipient;
import co.vine.android.api.VineUser;
import co.vine.android.client.AppController;
import co.vine.android.provider.Vine;
import co.vine.android.service.components.Components;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.StringAnchorManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public final class RecipientProvider {
    private final AppController mAppController;
    private List<ContactEntry> mContactsCache;
    private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks;
    private final LoaderManager mLoaderManager;
    private OnDataChangedListener mOnDataChangedListener;
    private final StringAnchorManager mRecipientsGroupedBySectionAnchorManager;
    private long mRecipientsGroupedBySectionLastRefreshTime;

    public interface OnDataChangedListener {
        void onContactsChanged(List<ContactEntry> list);

        void onDefaultsChanged(List<VineRecipient> list, List<VineRecipient> list2, List<VineRecipient> list3);

        void onUserSearchChanged(List<VineRecipient> list);
    }

    public RecipientProvider(Context context, LoaderManager loaderManager, AppController appController) {
        this.mLoaderManager = loaderManager;
        this.mAppController = appController;
        this.mLoaderCallbacks = new RecipientLoaderCallback(context, appController, this);
        this.mRecipientsGroupedBySectionAnchorManager = Vine.getUserSectionsAnchorManager(context, 0);
        this.mLoaderManager.initLoader(1000, null, this.mLoaderCallbacks);
    }

    public void requestContacts(String filter) {
        if (this.mContactsCache == null) {
            this.mLoaderManager.restartLoader(PointerIconCompat.TYPE_CONTEXT_MENU, null, this.mLoaderCallbacks);
            return;
        }
        if (CommonUtil.isNullOrWhitespace(filter)) {
            fireOnContactsChanged(this.mContactsCache);
            return;
        }
        List<ContactEntry> filteredContactEntries = new ArrayList<>();
        for (ContactEntry contactEntry : this.mContactsCache) {
            if (contactEntry.displayName != null && contactEntry.displayName.toLowerCase().startsWith(filter.toLowerCase())) {
                filteredContactEntries.add(contactEntry);
            }
        }
        fireOnContactsChanged(filteredContactEntries);
    }

    public void requestUserSearch(String filter) {
        if (filter != null && filter.length() < 2) {
            fireOnUserSearchChanged(Collections.EMPTY_LIST);
        } else {
            Components.suggestionsComponent().fetchFriendsTypeAhead(this.mAppController, filter);
        }
    }

    public void requestRecipientsGroupedBySection() {
        this.mAppController.fetchFriends(1, 0);
    }

    public void requestNextRecipientsGroupedBySection() {
        if (this.mRecipientsGroupedBySectionAnchorManager.haveMore()) {
            this.mAppController.fetchFriends(3, 0);
        }
    }

    public boolean hasMoreRecipientsGroupedBySection() {
        return this.mRecipientsGroupedBySectionAnchorManager.haveMore();
    }

    public void onGetUsersComplete() {
        long serverRecipientsGroupedBySectionRefreshTime = this.mRecipientsGroupedBySectionAnchorManager.getRefreshTime();
        if (this.mRecipientsGroupedBySectionLastRefreshTime != serverRecipientsGroupedBySectionRefreshTime) {
            this.mRecipientsGroupedBySectionLastRefreshTime = serverRecipientsGroupedBySectionRefreshTime;
            Bundle args = new Bundle();
            args.putString("refresh_time", String.valueOf(serverRecipientsGroupedBySectionRefreshTime));
            this.mLoaderManager.restartLoader(PointerIconCompat.TYPE_HAND, args, this.mLoaderCallbacks);
        }
    }

    public void onGetFriendsTypeAheadComplete(ArrayList<VineUser> users) {
        if (users == null) {
            users = new ArrayList<>();
        }
        ArrayList<VineRecipient> recipients = new ArrayList<>();
        Iterator<VineUser> it = users.iterator();
        while (it.hasNext()) {
            VineUser user = it.next();
            int color = getUserProfileColor(user);
            VineRecipient recipient = VineRecipient.fromUser(user.username, user.userId, color, -1L);
            recipient.avatarUrl = user.avatarUrl;
            recipients.add(recipient);
        }
        fireOnUserSearchChanged(recipients);
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        this.mOnDataChangedListener = listener;
    }

    public void setContactsCache(List<ContactEntry> contactsCache) {
        this.mContactsCache = contactsCache;
    }

    public void fireOnDefaultsChanged(List<VineRecipient> recent, List<VineRecipient> friends, List<VineRecipient> following) {
        if (this.mOnDataChangedListener != null) {
            this.mOnDataChangedListener.onDefaultsChanged(recent, friends, following);
        }
    }

    public void fireOnContactsChanged(List<ContactEntry> contacts) {
        if (this.mOnDataChangedListener != null) {
            this.mOnDataChangedListener.onContactsChanged(contacts);
        }
    }

    private void fireOnUserSearchChanged(List<VineRecipient> search) {
        if (this.mOnDataChangedListener != null) {
            this.mOnDataChangedListener.onUserSearchChanged(search);
        }
    }

    private int getUserProfileColor(VineUser user) {
        int color = user.profileBackground;
        if (user.profileBackground == -1) {
            return 0;
        }
        return color;
    }
}
