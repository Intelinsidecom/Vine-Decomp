package co.vine.android.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/* loaded from: classes.dex */
public class ContactsHelper implements LoaderManager.LoaderCallbacks<Cursor> {
    private Fragment mFragment;
    private ContactHelperListener mListener;

    public interface ContactHelperListener {
        void onEmailLoaded(String str);

        void onNameLoaded(String str);

        void onPhoneNumberLoaded(String str);
    }

    private interface ProfileQuery {
        public static final String[] PROJECTION = {"_id", "data1", "mimetype"};
    }

    public static void loadContacts(Fragment fragment, ContactHelperListener listener) {
        ContactsHelper helper = new ContactsHelper(fragment, listener);
        helper.init();
    }

    public ContactsHelper(Fragment fragment, ContactHelperListener listener) {
        this.mFragment = fragment;
        this.mListener = listener;
    }

    public void init() {
        if (!BuildUtil.isExplore()) {
            LoaderManager lm = this.mFragment.getLoaderManager();
            lm.initLoader(1, null, this);
            lm.initLoader(2, null, this);
            AccountManager manager = AccountManager.get(this.mFragment.getActivity());
            Account[] accounts = manager.getAccountsByType("com.google");
            if (accounts != null && accounts.length > 0) {
                this.mListener.onEmailLoaded(accounts[0].name);
            }
        }
    }

    @Override // android.support.v4.app.LoaderManager.LoaderCallbacks
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 1:
                return new ContactCursorLoader(this.mFragment.getActivity(), ContactsContract.Profile.CONTENT_URI, new String[]{"display_name"}, null, null, null);
            case 2:
                return new ContactCursorLoader(this.mFragment.getActivity(), Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI, "data"), ProfileQuery.PROJECTION, "mimetype=?", new String[]{"vnd.android.cursor.item/phone_v2"}, null);
            default:
                return null;
        }
    }

    private static class ContactCursorLoader extends CursorLoader {
        public ContactCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            super(context, uri, projection, selection, selectionArgs, sortOrder);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.support.v4.content.CursorLoader, android.support.v4.content.AsyncTaskLoader
        public Cursor loadInBackground() {
            try {
                return super.loadInBackground();
            } catch (Throwable e) {
                CrashUtil.logException(e);
                return null;
            }
        }
    }

    @Override // android.support.v4.app.LoaderManager.LoaderCallbacks
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case 1:
                if (cursor != null && cursor.moveToFirst()) {
                    String name = cursor.getString(cursor.getColumnIndex("display_name"));
                    this.mListener.onNameLoaded(name);
                    break;
                }
                break;
            case 2:
                String phone = null;
                if (cursor != null && cursor.moveToFirst()) {
                    String mimeType = cursor.getString(2);
                    if ("vnd.android.cursor.item/phone_v2".equals(mimeType)) {
                        phone = cursor.getString(1);
                    }
                }
                if (TextUtils.isEmpty(phone)) {
                    TelephonyManager tm = (TelephonyManager) this.mFragment.getActivity().getSystemService("phone");
                    phone = tm.getLine1Number();
                }
                this.mListener.onPhoneNumberLoaded(phone);
                break;
        }
    }

    @Override // android.support.v4.app.LoaderManager.LoaderCallbacks
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
