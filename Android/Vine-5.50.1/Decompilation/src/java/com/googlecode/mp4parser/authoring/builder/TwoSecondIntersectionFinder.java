package com.googlecode.mp4parser.authoring.builder;

import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class TwoSecondIntersectionFinder implements FragmentIntersectionFinder {
    private int fragmentLength;
    private Movie movie;

    public TwoSecondIntersectionFinder(Movie movie, int fragmentLength) {
        this.fragmentLength = 2;
        this.movie = movie;
        this.fragmentLength = fragmentLength;
    }

    @Override // com.googlecode.mp4parser.authoring.builder.FragmentIntersectionFinder
    public long[] sampleNumbers(Track track) {
        int samples;
        double trackLength = 0.0d;
        for (Track thisTrack : this.movie.getTracks()) {
            double thisTracksLength = thisTrack.getDuration() / thisTrack.getTrackMetaData().getTimescale();
            if (trackLength < thisTracksLength) {
                trackLength = thisTracksLength;
            }
        }
        int fragmentCount = Math.min(((int) Math.ceil(trackLength / this.fragmentLength)) - 1, track.getSamples().size());
        if (fragmentCount < 1) {
            fragmentCount = 1;
        }
        long[] fragments = new long[fragmentCount];
        Arrays.fill(fragments, -1L);
        fragments[0] = 1;
        long time = 0;
        int samples2 = 0;
        long[] sampleDurations = track.getSampleDurations();
        int length = sampleDurations.length;
        int i = 0;
        while (true) {
            samples = samples2;
            if (i >= length) {
                break;
            }
            long delta = sampleDurations[i];
            int currentFragment = ((int) ((time / track.getTrackMetaData().getTimescale()) / this.fragmentLength)) + 1;
            if (currentFragment >= fragments.length) {
                break;
            }
            samples2 = samples + 1;
            fragments[currentFragment] = samples + 1;
            time += delta;
            i++;
        }
        long last = samples + 1;
        for (int i2 = fragments.length - 1; i2 >= 0; i2--) {
            if (fragments[i2] == -1) {
                fragments[i2] = last;
            }
            last = fragments[i2];
        }
        return fragments;
    }
}
