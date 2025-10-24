package com.google.android.exoplayer.hls;

import com.google.android.exoplayer.ParserException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
final class HlsParserUtil {
    public static String parseStringAttr(String line, Pattern pattern, String tag) throws ParserException {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find() && matcher.groupCount() == 1) {
            return matcher.group(1);
        }
        throw new ParserException("Couldn't match " + tag + " tag in " + line);
    }

    public static int parseIntAttr(String line, Pattern pattern, String tag) throws ParserException {
        return Integer.parseInt(parseStringAttr(line, pattern, tag));
    }

    public static double parseDoubleAttr(String line, Pattern pattern, String tag) throws ParserException {
        return Double.parseDouble(parseStringAttr(line, pattern, tag));
    }

    public static String parseOptionalStringAttr(String line, Pattern pattern) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
