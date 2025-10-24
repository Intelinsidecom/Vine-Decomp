package co.vine.android.share.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import co.vine.android.ContactEntry;
import co.vine.android.OnContactEntryClickedListener;
import co.vine.android.R;
import co.vine.android.api.VineRecipient;
import co.vine.android.share.widgets.SectionHeaderRow;
import co.vine.android.share.widgets.VineContactRow;
import co.vine.android.widget.ObservableSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public final class ContactsAdapter extends BaseAdapter {
    private final Context mContext;
    private final FragmentManager mFragmentManager;
    private final String mHeader;
    private final ObservableSet<VineRecipient> mSelectedRecipientsRepository;
    private final OnContactEntryClickedListener mOnContactEntryClickedListener = new OnContactEntryClickedListener() { // from class: co.vine.android.share.adapters.ContactsAdapter.2
        @Override // co.vine.android.OnContactEntryClickedListener
        protected void onContactEntryClicked(ContactEntry entry, VineRecipient recipient, int typeFlag, String value) {
            ContactTypeCounts countByContactType;
            boolean isSelected = ContactsAdapter.this.mSelectedRecipientsRepository.contains(recipient);
            long contactId = recipient.contactId;
            if (ContactsAdapter.this.mContactIdToCountByContactTypeMap.containsKey(Long.valueOf(contactId))) {
                countByContactType = (ContactTypeCounts) ContactsAdapter.this.mContactIdToCountByContactTypeMap.get(Long.valueOf(contactId));
            } else {
                countByContactType = new ContactTypeCounts();
                ContactsAdapter.this.mContactIdToCountByContactTypeMap.put(Long.valueOf(contactId), countByContactType);
            }
            if (!isSelected) {
                ContactsAdapter.this.updateCountByType(countByContactType, typeFlag, 1);
                ContactsAdapter.this.mSelectedRecipientsRepository.add(recipient);
            } else {
                ContactsAdapter.this.updateCountByType(countByContactType, typeFlag, -1);
                ContactsAdapter.this.mSelectedRecipientsRepository.remove(recipient);
            }
        }
    };
    private final List<ContactEntry> mContactEntries = new ArrayList();
    private final Map<Long, ContactTypeCounts> mContactIdToCountByContactTypeMap = new HashMap();

    public ContactsAdapter(Context context, String header, FragmentManager fragmentManager, ObservableSet<VineRecipient> selectedRecipientsRepository) {
        this.mContext = context;
        this.mHeader = header;
        this.mFragmentManager = fragmentManager;
        this.mSelectedRecipientsRepository = selectedRecipientsRepository;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        if (this.mContactEntries.isEmpty()) {
            return 0;
        }
        return this.mContactEntries.size() + 1;
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return position == 0 ? this.mHeader : this.mContactEntries.get(position - 1);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return position;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 2;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        VineContactRow vineContactRow;
        VineContactRow vineContactRow2;
        SectionHeaderRow sectionHeaderRow;
        if (i == 0) {
            if (view == null) {
                sectionHeaderRow = (SectionHeaderRow) LayoutInflater.from(this.mContext).inflate(R.layout.vm_recipient_section_standalone, viewGroup, false);
            } else {
                sectionHeaderRow = (SectionHeaderRow) view;
            }
            sectionHeaderRow.bind(this.mHeader);
            vineContactRow2 = sectionHeaderRow;
        } else {
            if (view == null) {
                vineContactRow = (VineContactRow) LayoutInflater.from(this.mContext).inflate(R.layout.contact_row_standalone, viewGroup, false);
            } else {
                vineContactRow = (VineContactRow) view;
            }
            final ContactEntry contactEntry = (ContactEntry) getItem(i);
            vineContactRow.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.share.adapters.ContactsAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    ContactsAdapter.this.mOnContactEntryClickedListener.onClick(ContactsAdapter.this.mFragmentManager, contactEntry);
                }
            });
            vineContactRow.bind(contactEntry, getSelectedTypeMask(contactEntry));
            vineContactRow2 = vineContactRow;
        }
        return vineContactRow2;
    }

    public void replaceData(List<ContactEntry> contactEntries) {
        this.mContactEntries.clear();
        this.mContactEntries.addAll(contactEntries);
        notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCountByType(ContactTypeCounts countByContactType, int typeFlag, int delta) {
        if (typeFlag != 1) {
            if (typeFlag != 2) {
                return;
            }
            countByContactType.setPhoneTypeCount(countByContactType.getPhoneTypeCount() + delta);
            return;
        }
        countByContactType.setEmailTypeCount(countByContactType.getEmailTypeCount() + delta);
    }

    private int getSelectedTypeMask(ContactEntry contactEntry) {
        int selectedTypes = 0;
        if (!this.mContactIdToCountByContactTypeMap.containsKey(Long.valueOf(contactEntry.contactId))) {
            return 0;
        }
        ContactTypeCounts countByContactType = this.mContactIdToCountByContactTypeMap.get(Long.valueOf(contactEntry.contactId));
        if (countByContactType.getEmailTypeCount() > 0) {
            selectedTypes = 0 | 1;
        }
        if (countByContactType.getPhoneTypeCount() > 0) {
            return selectedTypes | 2;
        }
        return selectedTypes;
    }

    private static class ContactTypeCounts {
        private int mEmailTypeCount;
        private int mPhoneTypeCount;

        private ContactTypeCounts() {
            this.mEmailTypeCount = 0;
            this.mPhoneTypeCount = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getEmailTypeCount() {
            return this.mEmailTypeCount;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getPhoneTypeCount() {
            return this.mPhoneTypeCount;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setEmailTypeCount(int emailTypeCount) {
            this.mEmailTypeCount = emailTypeCount;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setPhoneTypeCount(int phoneTypeCount) {
            this.mPhoneTypeCount = phoneTypeCount;
        }
    }
}
