package com.google.android.exoplayer.text.webvtt;

import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public final class WebvttCueParser {
    public static final Pattern CUE_HEADER_PATTERN = Pattern.compile("^(\\S+)\\s+-->\\s+(\\S+)(.*)?$");
    private static final Pattern COMMENT = Pattern.compile("^NOTE(( |\t).*)?$");
    private static final Pattern CUE_SETTING_PATTERN = Pattern.compile("(\\S+?):(\\S+)");

    public static Matcher findNextCueHeader(ParsableByteArray input) {
        String line;
        while (true) {
            String line2 = input.readLine();
            if (line2 == null) {
                return null;
            }
            if (COMMENT.matcher(line2).matches()) {
                do {
                    line = input.readLine();
                    if (line != null) {
                    }
                } while (!line.isEmpty());
            } else {
                Matcher cueHeaderMatcher = CUE_HEADER_PATTERN.matcher(line2);
                if (cueHeaderMatcher.matches()) {
                    return cueHeaderMatcher;
                }
            }
        }
    }
}
