package com.digits.sdk.android;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import io.fabric.sdk.android.services.concurrency.internal.DefaultRetryPolicy;
import io.fabric.sdk.android.services.concurrency.internal.ExponentialBackoff;
import io.fabric.sdk.android.services.concurrency.internal.RetryThreadPoolExecutor;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class ContactsUploadService extends IntentService {
    private ContactsClient contactsClient;
    private RetryThreadPoolExecutor executor;
    private ContactsHelper helper;
    private ContactsPreferenceManager prefManager;

    public ContactsUploadService() {
        super("UPLOAD_WORKER");
        init(Digits.getInstance().getContactsClient(), new ContactsHelper(this), new ContactsPreferenceManager(), new RetryThreadPoolExecutor(2, new DefaultRetryPolicy(1), new ExponentialBackoff(1000L)));
    }

    ContactsUploadService(ContactsClient contactsClient, ContactsHelper helper, ContactsPreferenceManager prefManager, RetryThreadPoolExecutor executor) {
        super("UPLOAD_WORKER");
        init(contactsClient, helper, prefManager, executor);
    }

    private void init(ContactsClient contactsClient, ContactsHelper helper, ContactsPreferenceManager prefManager, RetryThreadPoolExecutor executor) {
        this.contactsClient = contactsClient;
        this.helper = helper;
        this.prefManager = prefManager;
        this.executor = executor;
        setIntentRedelivery(true);
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent intent) {
        this.prefManager.setContactImportPermissionGranted();
        try {
            List<String> allCards = getAllCards();
            int totalCount = allCards.size();
            int pages = getNumberOfPages(totalCount);
            final AtomicInteger successCount = new AtomicInteger(0);
            for (int i = 0; i < pages; i++) {
                int startIndex = i * 100;
                int endIndex = Math.min(totalCount, startIndex + 100);
                List<String> subList = allCards.subList(startIndex, endIndex);
                final Vcards vCards = new Vcards(subList);
                this.executor.scheduleWithRetry(new Runnable() { // from class: com.digits.sdk.android.ContactsUploadService.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ContactsUploadService.this.contactsClient.uploadContacts(vCards);
                        successCount.addAndGet(vCards.vcards.size());
                    }
                });
            }
            this.executor.shutdown();
            boolean success = this.executor.awaitTermination(300L, TimeUnit.SECONDS);
            if (!success) {
                this.executor.shutdownNow();
                sendFailureBroadcast();
            } else {
                if (successCount.get() == 0) {
                    sendFailureBroadcast();
                    return;
                }
                this.prefManager.setContactsReadTimestamp(System.currentTimeMillis());
                this.prefManager.setContactsUploaded(successCount.get());
                sendSuccessBroadcast(new ContactsUploadResult(successCount.get(), totalCount));
            }
        } catch (Exception e) {
            sendFailureBroadcast();
        }
    }

    int getNumberOfPages(int numCards) {
        return ((numCards + 100) - 1) / 100;
    }

    private List<String> getAllCards() {
        Cursor cursor = null;
        Collections.emptyList();
        try {
            cursor = this.helper.getContactsCursor();
            List<String> allCards = this.helper.createContactList(cursor);
            return allCards;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    void sendFailureBroadcast() {
        Intent intent = new Intent("com.digits.sdk.android.UPLOAD_FAILED");
        sendBroadcast(intent);
    }

    void sendSuccessBroadcast(ContactsUploadResult extra) {
        Intent intent = new Intent("com.digits.sdk.android.UPLOAD_COMPLETE");
        intent.putExtra("com.digits.sdk.android.UPLOAD_COMPLETE_EXTRA", extra);
        sendBroadcast(intent);
    }
}
