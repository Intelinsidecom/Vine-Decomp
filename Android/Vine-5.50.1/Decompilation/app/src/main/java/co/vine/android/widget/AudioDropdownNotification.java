package co.vine.android.widget;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.api.VineAudioMetadata;
import co.vine.android.client.AppController;
import co.vine.android.util.LinkDispatcher;
import co.vine.android.util.ResourceLoader;

/* loaded from: classes.dex */
public final class AudioDropdownNotification {
    public static void showOverlay(final Activity activity, AppController appController, final VineAudioMetadata audioMetadata) throws Resources.NotFoundException {
        DropdownNotification dropdownNotification = new DropdownNotification(activity, 0L);
        LayoutInflater inflater = activity.getLayoutInflater();
        View audioView = inflater.inflate(R.layout.audio_dropdown_notification, (ViewGroup) null);
        TextView artistName = (TextView) audioView.findViewById(R.id.artist_name_label);
        TextView trackName = (TextView) audioView.findViewById(R.id.track_name_label);
        artistName.setText(audioMetadata.getArtistName());
        trackName.setText(audioMetadata.getTrackName());
        artistName.setSelected(true);
        trackName.setSelected(true);
        ImageView thumbnail = (ImageView) audioView.findViewById(R.id.thumbnail);
        String albumArtUrl = audioMetadata.getAlbumArtUrl();
        if (!albumArtUrl.isEmpty()) {
            ResourceLoader bitmapLoader = new ResourceLoader(activity, appController);
            bitmapLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(thumbnail), albumArtUrl);
        } else {
            thumbnail.setImageResource(R.drawable.no_album_art_thumb);
        }
        thumbnail.setVisibility(0);
        if (audioMetadata.getHasAudioTrackTimeline()) {
            audioView.findViewById(R.id.arrow).setVisibility(0);
            audioView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.widget.AudioDropdownNotification.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    ((ViewGroup) v.getParent()).setVisibility(8);
                    LinkDispatcher.dispatch("vine://timelines/audio/" + audioMetadata.getTrackId(), activity);
                }
            });
        }
        dropdownNotification.showOverlay(audioView);
    }
}
