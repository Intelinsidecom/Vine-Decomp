package com.digits.sdk.android;

import com.twitter.sdk.android.core.AuthenticatedClient;
import com.twitter.sdk.android.core.TwitterCore;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/* loaded from: classes.dex */
public class ContactsClient {
    private ActivityClassManagerFactory activityClassManagerFactory;
    private ContactsService contactsService;
    private final ContactsPreferenceManager prefManager;
    private final TwitterCore twitterCore;

    interface ContactsService {
        @POST("/1.1/contacts/destroy/all.json")
        void deleteAll(ContactsCallback<Response> contactsCallback);

        @POST("/1.1/contacts/upload.json")
        UploadResponse upload(@Body Vcards vcards);

        @GET("/1.1/contacts/users_and_uploaded_by.json")
        void usersAndUploadedBy(@Query("next_cursor") String str, @Query("count") Integer num, ContactsCallback<Object> contactsCallback);
    }

    ContactsClient() {
        this(TwitterCore.getInstance(), new ContactsPreferenceManager(), new ActivityClassManagerFactory(), null);
    }

    ContactsClient(TwitterCore twitterCore, ContactsPreferenceManager prefManager, ActivityClassManagerFactory activityClassManagerFactory, ContactsService contactsService) {
        if (twitterCore == null) {
            throw new IllegalArgumentException("twitter must not be null");
        }
        if (prefManager == null) {
            throw new IllegalArgumentException("preference manager must not be null");
        }
        if (activityClassManagerFactory == null) {
            throw new IllegalArgumentException("activityClassManagerFactory must not be null");
        }
        this.twitterCore = twitterCore;
        this.prefManager = prefManager;
        this.activityClassManagerFactory = activityClassManagerFactory;
        this.contactsService = contactsService;
    }

    private ContactsService getContactsService() {
        if (this.contactsService != null) {
            return this.contactsService;
        }
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(new DigitsApi().getBaseHostUrl()).setClient(new AuthenticatedClient(this.twitterCore.getAuthConfig(), Digits.getSessionManager().getActiveSession(), this.twitterCore.getSSLSocketFactory())).build();
        this.contactsService = (ContactsService) adapter.create(ContactsService.class);
        return this.contactsService;
    }

    UploadResponse uploadContacts(Vcards vcards) {
        return getContactsService().upload(vcards);
    }
}
