package com.google.android.exoplayer.text.webvtt;

import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public final class WebvttParserUtil {
    private static final Pattern HEADER = Pattern.compile("^\ufeff?WEBVTT(( |\t).*)?$");

    public static void validateWebvttHeaderLine(ParsableByteArray input) throws ParserException {
        String line = input.readLine();
        if (line == null || !HEADER.matcher(line).matches()) {
            throw new ParserException("Expected WEBVTT. Got " + line);
        }
    }

    public static long parseTimestampUs(String timestamp) throws NumberFormatException {
        long value = 0;
        String[] parts = timestamp.split("\\.", 2);
        String[] subparts = parts[0].split(":");
        for (String str : subparts) {
            value = (60 * value) + Long.parseLong(str);
        }
        return ((value * 1000) + Long.parseLong(parts[1])) * 1000;
    }
}
