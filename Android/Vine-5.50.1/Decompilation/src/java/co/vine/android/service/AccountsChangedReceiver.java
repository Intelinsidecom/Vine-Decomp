package co.vine.android.service;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import co.vine.android.client.AppController;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.service.components.Components;

/* loaded from: classes.dex */
public class AccountsChangedReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType(VineAccountHelper.ACCOUNT_TYPE);
        if (accounts.length == 0) {
            Components.authComponent().logout(AppController.getInstance(context));
        }
    }
}
