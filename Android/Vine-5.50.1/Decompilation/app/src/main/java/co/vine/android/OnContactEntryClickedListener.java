package co.vine.android;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import co.vine.android.api.VineRecipient;
import co.vine.android.widgets.PromptDialogSupportFragment;
import java.util.Set;

/* loaded from: classes.dex */
public abstract class OnContactEntryClickedListener implements PromptDialogSupportFragment.OnDialogDoneListener {
    private ContactEntry mEntry;
    private String[] mValues;

    protected abstract void onContactEntryClicked(ContactEntry contactEntry, VineRecipient vineRecipient, int i, String str);

    public void onClick(FragmentManager fragmentManager, ContactEntry entry) {
        if (entry != null) {
            Set<String> keySet = entry.valueMimeMap.keySet();
            this.mValues = (String[]) keySet.toArray(new String[keySet.size()]);
            this.mEntry = entry;
            PromptDialogSupportFragment dialog = PromptDialogSupportFragment.newInstance(0);
            dialog.setItems(this.mValues);
            dialog.setListener(this);
            try {
                dialog.show(fragmentManager);
            } catch (IllegalStateException e) {
            }
        }
    }

    @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
    public void onDialogDone(DialogInterface dialog, int id, int which) {
        Integer typeFlag;
        if (this.mValues.length > 0 && which >= 0 && (typeFlag = this.mEntry.valueMimeMap.get(this.mValues[which])) != null) {
            VineRecipient recipient = null;
            if (typeFlag.intValue() == 1) {
                recipient = VineRecipient.fromEmail(this.mEntry.displayName, -1L, this.mValues[which], 0L);
            }
            if (typeFlag.intValue() == 2) {
                recipient = VineRecipient.fromPhone(this.mEntry.displayName, -1L, this.mValues[which], 0L);
            }
            if (recipient != null) {
                recipient.contactId = this.mEntry.contactId;
                onContactEntryClicked(this.mEntry, recipient, typeFlag.intValue(), this.mValues[which]);
            }
        }
    }
}
