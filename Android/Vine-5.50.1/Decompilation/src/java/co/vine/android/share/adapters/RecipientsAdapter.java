package co.vine.android.share.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import co.vine.android.R;
import co.vine.android.api.VineRecipient;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.share.widgets.SectionHeaderRow;
import co.vine.android.share.widgets.VineRecipientRow;
import co.vine.android.share.widgets.VineToggleRow;
import co.vine.android.util.Util;
import co.vine.android.widget.ObservableSet;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public final class RecipientsAdapter extends BaseAdapter {
    private final int mAvatarImageSizePx;
    private final Context mContext;
    private final String mSectionName;
    private final ObservableSet<VineRecipient> mSelectedRecipientsRepository;
    private final List<VineRecipient> mRecipients = new ArrayList();
    private final List<WeakReference<VineRecipientRow>> mViews = new ArrayList();

    public RecipientsAdapter(Context context, String sectionName, ObservableSet<VineRecipient> selectedRecipientsRepository) {
        this.mContext = context;
        this.mSectionName = sectionName;
        this.mSelectedRecipientsRepository = selectedRecipientsRepository;
        this.mAvatarImageSizePx = context.getResources().getDimensionPixelOffset(R.dimen.user_image_size);
    }

    @Override // android.widget.Adapter
    public int getCount() {
        if (this.mRecipients.isEmpty()) {
            return 0;
        }
        return this.mRecipients.size() + 1;
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return position == 0 ? this.mSectionName : this.mRecipients.get(position - 1);
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
        VineRecipientRow vineRecipientRow;
        VineRecipientRow vineRecipientRow2;
        SectionHeaderRow sectionHeaderRow;
        if (i == 0) {
            if (view == null) {
                sectionHeaderRow = (SectionHeaderRow) LayoutInflater.from(this.mContext).inflate(R.layout.vm_recipient_section_standalone, viewGroup, false);
            } else {
                sectionHeaderRow = (SectionHeaderRow) view;
            }
            sectionHeaderRow.bind(this.mSectionName);
            vineRecipientRow2 = sectionHeaderRow;
        } else {
            if (view == null) {
                final VineRecipientRow vineRecipientRow3 = (VineRecipientRow) LayoutInflater.from(this.mContext).inflate(R.layout.vm_recipient, viewGroup, false);
                vineRecipientRow3.setOnCheckedStateChangedListener(new VineToggleRow.OnCheckedStateChangedListener() { // from class: co.vine.android.share.adapters.RecipientsAdapter.1
                    @Override // co.vine.android.share.widgets.VineToggleRow.OnCheckedStateChangedListener
                    public void onCheckedStateChanged(boolean selected, boolean fromUserInput) {
                        VineRecipient recipient = vineRecipientRow3.getRecipient();
                        if (selected) {
                            RecipientsAdapter.this.mSelectedRecipientsRepository.add(recipient);
                        } else {
                            RecipientsAdapter.this.mSelectedRecipientsRepository.remove(recipient);
                        }
                    }
                });
                this.mViews.add(new WeakReference<>(vineRecipientRow3));
                vineRecipientRow = vineRecipientRow3;
            } else {
                vineRecipientRow = (VineRecipientRow) view;
            }
            VineRecipient vineRecipient = (VineRecipient) getItem(i);
            vineRecipientRow.bind(vineRecipient, vineRecipient.getDisplay(), getRecipientAvatarImageKey(vineRecipient.avatarUrl, this.mAvatarImageSizePx), this.mSelectedRecipientsRepository.contains(vineRecipient));
            vineRecipientRow2 = vineRecipientRow;
        }
        return vineRecipientRow2;
    }

    public void replaceData(List<VineRecipient> recipients) {
        this.mRecipients.clear();
        this.mRecipients.addAll(recipients);
        notifyDataSetChanged();
    }

    public void updateAvatarImages(HashMap<ImageKey, UrlImage> images) {
        List<WeakReference<VineRecipientRow>> viewReferencesToRemove = new ArrayList<>();
        for (WeakReference<VineRecipientRow> viewReference : this.mViews) {
            VineRecipientRow view = viewReference.get();
            if (view == null) {
                viewReferencesToRemove.add(viewReference);
            } else {
                view.updateAvatarIfNeeded(images);
            }
        }
        this.mViews.removeAll(viewReferencesToRemove);
    }

    private ImageKey getRecipientAvatarImageKey(String avatarUrl, int avatarImageSizePx) {
        if (TextUtils.isEmpty(avatarUrl) || Util.isDefaultAvatarUrl(avatarUrl)) {
            return null;
        }
        return new ImageKey(avatarUrl, avatarImageSizePx, avatarImageSizePx, true);
    }
}
