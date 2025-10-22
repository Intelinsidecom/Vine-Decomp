package com.google.android.exoplayer.hls;

import android.content.Context;
import android.text.TextUtils;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.google.android.exoplayer.chunk.VideoFormatSelectorUtil;
import com.google.android.exoplayer.hls.HlsTrackSelector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class DefaultHlsTrackSelector implements HlsTrackSelector {
    private final Context context;
    private final int type;

    public static DefaultHlsTrackSelector newDefaultInstance(Context context) {
        return new DefaultHlsTrackSelector(context, 0);
    }

    private DefaultHlsTrackSelector(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    @Override // com.google.android.exoplayer.hls.HlsTrackSelector
    public void selectTracks(HlsMasterPlaylist playlist, HlsTrackSelector.Output output) throws IOException {
        if (this.type == 1) {
            List<Variant> subtitleVariants = playlist.subtitles;
            if (subtitleVariants != null && !subtitleVariants.isEmpty()) {
                for (int i = 0; i < subtitleVariants.size(); i++) {
                    output.fixedTrack(playlist, subtitleVariants.get(i));
                }
                return;
            }
            return;
        }
        ArrayList<Variant> enabledVariantList = new ArrayList<>();
        int[] variantIndices = VideoFormatSelectorUtil.selectVideoFormatsForDefaultDisplay(this.context, playlist.variants, null, false);
        for (int i2 : variantIndices) {
            enabledVariantList.add(playlist.variants.get(i2));
        }
        ArrayList<Variant> definiteVideoVariants = new ArrayList<>();
        ArrayList<Variant> definiteAudioOnlyVariants = new ArrayList<>();
        for (int i3 = 0; i3 < enabledVariantList.size(); i3++) {
            Variant variant = enabledVariantList.get(i3);
            if (variant.format.height > 0 || variantHasExplicitCodecWithPrefix(variant, "avc")) {
                definiteVideoVariants.add(variant);
            } else if (variantHasExplicitCodecWithPrefix(variant, AudioSampleEntry.TYPE3)) {
                definiteAudioOnlyVariants.add(variant);
            }
        }
        if (!definiteVideoVariants.isEmpty()) {
            enabledVariantList = definiteVideoVariants;
        } else if (definiteAudioOnlyVariants.size() < enabledVariantList.size()) {
            enabledVariantList.removeAll(definiteAudioOnlyVariants);
        }
        if (enabledVariantList.size() > 1) {
            Variant[] enabledVariants = new Variant[enabledVariantList.size()];
            enabledVariantList.toArray(enabledVariants);
            output.adaptiveTrack(playlist, enabledVariants);
        }
        for (int i4 = 0; i4 < enabledVariantList.size(); i4++) {
            output.fixedTrack(playlist, enabledVariantList.get(i4));
        }
    }

    private static boolean variantHasExplicitCodecWithPrefix(Variant variant, String prefix) {
        String codecs = variant.format.codecs;
        if (TextUtils.isEmpty(codecs)) {
            return false;
        }
        String[] codecArray = codecs.split("(\\s*,\\s*)|(\\s*$)");
        for (String str : codecArray) {
            if (str.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
