package co.vine.android.feedadapter.viewmanager;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import co.vine.android.api.VineAudioMetadata;
import co.vine.android.api.VineSource;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.ConsumptionButtonViewHolder;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.widget.AudioDropdownNotification;
import co.vine.android.widget.SourceDropdownNotification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/* loaded from: classes.dex */
public class ConsumptionButtonViewManager implements ViewManager {
    private final AppController mAppController;
    private final Activity mContext;

    public ConsumptionButtonViewManager(Activity context, AppController appController) {
        this.mContext = context;
        this.mAppController = appController;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.CONSUMPTION_BUTTON;
    }

    public void bind(ConsumptionButtonViewHolder h, ArrayList<VineSource> sources) {
        if (h.button != null && h.musicIcon != null && h.videoIcon != null) {
            ArrayList<VineSource> sortedSources = new ArrayList<>(sources);
            Collections.sort(sortedSources, new Comparator<VineSource>() { // from class: co.vine.android.feedadapter.viewmanager.ConsumptionButtonViewManager.1
                @Override // java.util.Comparator
                public int compare(VineSource first, VineSource second) {
                    return first.getContentType() - second.getContentType();
                }
            });
            final VineSource source = sortedSources.get(0);
            boolean isVideoComsumptionActive = ClientFlagsHelper.isVideoRemixConsumptionEnabled(this.mContext);
            if (source.getContentType() == 2 && isVideoComsumptionActive) {
                h.musicIcon.setVisibility(8);
                h.videoIcon.setVisibility(0);
                h.button.setVisibility(0);
                h.button.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.feedadapter.viewmanager.ConsumptionButtonViewManager.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) throws Resources.NotFoundException {
                        SourceDropdownNotification.showOverlay(ConsumptionButtonViewManager.this.mContext, ConsumptionButtonViewManager.this.mAppController, source, 2);
                    }
                });
                return;
            }
            if (source.getContentType() == 1) {
                h.musicIcon.setVisibility(0);
                h.videoIcon.setVisibility(8);
                h.button.setVisibility(0);
                h.musicIcon.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.feedadapter.viewmanager.ConsumptionButtonViewManager.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) throws Resources.NotFoundException {
                        SourceDropdownNotification.showOverlay(ConsumptionButtonViewManager.this.mContext, ConsumptionButtonViewManager.this.mAppController, source, 1);
                    }
                });
            }
        }
    }

    public void bind(ConsumptionButtonViewHolder h, final VineAudioMetadata metadata) {
        if (h.button != null && h.musicIcon != null && h.videoIcon != null) {
            h.musicIcon.setVisibility(0);
            h.videoIcon.setVisibility(8);
            h.button.setVisibility(0);
            h.musicIcon.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.feedadapter.viewmanager.ConsumptionButtonViewManager.4
                @Override // android.view.View.OnClickListener
                public void onClick(View v) throws Resources.NotFoundException {
                    AudioDropdownNotification.showOverlay(ConsumptionButtonViewManager.this.mContext, ConsumptionButtonViewManager.this.mAppController, metadata);
                }
            });
        }
    }
}
