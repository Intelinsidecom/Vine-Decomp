package co.vine.android.widget;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.api.VineSource;
import co.vine.android.client.AppController;
import co.vine.android.util.LinkDispatcher;
import co.vine.android.util.ResourceLoader;

/* loaded from: classes.dex */
public final class SourceDropdownNotification {
    public static void showOverlay(final Activity activity, AppController appController, final VineSource source, int type) throws Resources.NotFoundException {
        DropdownNotification dropdownNotification = new DropdownNotification(activity, 0L);
        LayoutInflater inflater = activity.getLayoutInflater();
        View sourceView = inflater.inflate(R.layout.source_dropdown_notification, (ViewGroup) null);
        if (type == 2) {
            sourceView.findViewById(R.id.audio_icon).setVisibility(8);
            sourceView.findViewById(R.id.video_icon).setVisibility(0);
        }
        TextView username = (TextView) sourceView.findViewById(R.id.username_label);
        TextView description = (TextView) sourceView.findViewById(R.id.description_label);
        description.setSelected(true);
        username.setText(source.getUsername());
        String postDescription = source.getDescription();
        if (!postDescription.isEmpty()) {
            description.setText(postDescription);
        } else {
            description.setVisibility(8);
        }
        ImageView thumbnail = (ImageView) sourceView.findViewById(R.id.thumbnail);
        thumbnail.setVisibility(0);
        sourceView.findViewById(R.id.arrow).setVisibility(0);
        ResourceLoader bitmapLoader = new ResourceLoader(activity, appController);
        bitmapLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(thumbnail), source.getThumbnailUrl());
        sourceView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.widget.SourceDropdownNotification.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ((ViewGroup) v.getParent()).setVisibility(8);
                LinkDispatcher.dispatch("vine://timelines/remixes/post/" + source.getPostId(), activity);
            }
        });
        dropdownNotification.showOverlay(sourceView);
    }
}
