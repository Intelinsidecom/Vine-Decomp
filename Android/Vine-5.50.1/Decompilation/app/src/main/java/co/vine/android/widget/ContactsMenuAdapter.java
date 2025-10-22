package co.vine.android.widget;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.ContactEntry;
import co.vine.android.R;
import co.vine.android.util.Util;
import java.lang.ref.WeakReference;
import java.util.Collection;

/* loaded from: classes.dex */
public class ContactsMenuAdapter extends ArrayAdapter<ContactEntry> {
    private int[] mRowHeaderStates;
    private final SparseArray<WeakReference<ContactsPickerViewHolder>> mViewHolders;

    public ContactsMenuAdapter(Context context) {
        super(context, 0);
        this.mViewHolders = new SparseArray<>();
    }

    @Override // android.widget.ArrayAdapter
    public void addAll(Collection<? extends ContactEntry> collection) {
        super.addAll(collection);
        this.mRowHeaderStates = new int[collection.size()];
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactsPickerViewHolder holder;
        boolean showSeparator;
        boolean showDivider;
        if (convertView == null || !(convertView.getTag() instanceof ContactsPickerViewHolder)) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.menu_contacts_row, parent, false);
            holder = new ContactsPickerViewHolder(getContext(), convertView);
            convertView.setTag(holder);
        } else {
            holder = (ContactsPickerViewHolder) convertView.getTag();
        }
        this.mViewHolders.put(position, new WeakReference<>(holder));
        ContactEntry entry = getItem(position);
        switch (this.mRowHeaderStates[position]) {
            case 1:
                showSeparator = true;
                break;
            case 2:
                showSeparator = false;
                break;
            default:
                if (position == 0) {
                    showSeparator = true;
                } else {
                    ContactEntry lastEntry = getItem(position - 1);
                    showSeparator = !entry.isSameSection(lastEntry);
                }
                this.mRowHeaderStates[position] = showSeparator ? 1 : 2;
                break;
        }
        if (position < getCount() - 1) {
            ContactEntry nextEntry = getItem(position + 1);
            showDivider = entry.isSameSection(nextEntry);
        } else {
            showDivider = true;
        }
        holder.divider.setVisibility(showDivider ? 0 : 8);
        boolean showEmail = (entry.typeFlag & 1) != 0;
        boolean showPhone = (entry.typeFlag & 2) != 0;
        holder.username.setText(entry.displayName);
        holder.emailIndicator.setVisibility(showEmail ? 0 : 8);
        holder.phoneIndicator.setVisibility(showPhone ? 0 : 8);
        holder.contactId = entry.contactId;
        if (position == 0) {
            holder.sectionTitle.setText(getContext().getString(R.string.contacts));
            holder.sectionTitle.setVisibility(0);
        } else {
            holder.sectionTitle.setVisibility(8);
        }
        if (showSeparator) {
            holder.sectionIndicator.setVisibility(0);
            if (entry.displayName != null) {
                holder.sectionSort.setText(new char[]{entry.displayName.toUpperCase().charAt(0)}, 0, 1);
            } else {
                holder.sectionSort.setText(new char[]{' '}, 0, 1);
            }
        } else {
            holder.sectionIndicator.setVisibility(8);
        }
        return convertView;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public long getItemId(int position) {
        ContactEntry entry = getItem(position);
        if (entry != null) {
            return entry.contactId;
        }
        return 0L;
    }

    private static class ContactsPickerViewHolder {
        public long contactId;
        public View divider;
        public ImageView emailIndicator;
        public ImageView phoneIndicator;
        public View sectionIndicator;
        public TextView sectionSort;
        public TextView sectionTitle;
        public TextView username;

        public ContactsPickerViewHolder(Context context, View v) {
            this.emailIndicator = (ImageView) v.findViewById(R.id.email_indicator);
            this.phoneIndicator = (ImageView) v.findViewById(R.id.phone_indicator);
            this.sectionIndicator = v.findViewById(R.id.section_indicator);
            this.sectionTitle = (TextView) v.findViewById(R.id.section_title);
            this.sectionSort = (TextView) v.findViewById(R.id.section_sort);
            this.username = (TextView) v.findViewById(R.id.contact_name);
            this.divider = v.findViewById(R.id.divider);
            Util.styleSectionHeader(context, this.sectionTitle, this.sectionSort);
        }
    }
}
