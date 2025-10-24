package co.vine.android;

import android.graphics.drawable.Drawable;

/* loaded from: classes.dex */
public class Video {
    public String duration;
    public long id;
    public Drawable thumbnail;
    public String videoPath;

    public Video() {
    }

    public Video(long id, String duration, String videoPath, Drawable thumbnail) {
        this.id = id;
        this.duration = duration;
        this.videoPath = videoPath;
        this.thumbnail = thumbnail;
    }
}
