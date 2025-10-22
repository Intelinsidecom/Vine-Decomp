package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;

/* loaded from: classes2.dex */
public final class NumberInput {
    static final String MIN_LONG_STR_NO_SIGN = String.valueOf(Long.MIN_VALUE).substring(1);
    static final String MAX_LONG_STR = String.valueOf(Long.MAX_VALUE);

    public static int parseInt(char[] ch, int off, int len) {
        int num = ch[off] - '0';
        if (len > 4) {
            int i = ((num * 10) + (ch[r6] - '0')) * 10;
            int i2 = (i + (ch[r6] - '0')) * 10;
            int i3 = (i2 + (ch[r6] - '0')) * 10;
            off = off + 1 + 1 + 1 + 1;
            num = i3 + (ch[off] - '0');
            len -= 4;
            if (len > 4) {
                int i4 = ((num * 10) + (ch[r6] - '0')) * 10;
                int i5 = (i4 + (ch[r6] - '0')) * 10;
                int off2 = off + 1 + 1 + 1;
                return ((i5 + (ch[off2] - '0')) * 10) + (ch[off2 + 1] - '0');
            }
        }
        if (len > 1) {
            int off3 = off + 1;
            num = (num * 10) + (ch[off3] - '0');
            if (len > 2) {
                int off4 = off3 + 1;
                num = (num * 10) + (ch[off4] - '0');
                if (len > 3) {
                    num = (num * 10) + (ch[off4 + 1] - '0');
                }
            }
        }
        return num;
    }

    public static long parseLong(char[] ch, int off, int len) {
        int len1 = len - 9;
        long val = parseInt(ch, off, len1) * 1000000000;
        return parseInt(ch, off + len1, 9) + val;
    }

    public static boolean inLongRange(char[] ch, int off, int len, boolean negative) {
        String cmpStr = negative ? MIN_LONG_STR_NO_SIGN : MAX_LONG_STR;
        int cmpLen = cmpStr.length();
        if (len < cmpLen) {
            return true;
        }
        if (len > cmpLen) {
            return false;
        }
        for (int i = 0; i < cmpLen; i++) {
            int diff = ch[off + i] - cmpStr.charAt(i);
            if (diff != 0) {
                return diff < 0;
            }
        }
        return true;
    }

    public static int parseAsInt(String s, int def) {
        String s2;
        int len;
        if (s != null && (len = (s2 = s.trim()).length()) != 0) {
            int i = 0;
            if (0 < len) {
                char c = s2.charAt(0);
                if (c == '+') {
                    s2 = s2.substring(1);
                    len = s2.length();
                } else if (c == '-') {
                    i = 0 + 1;
                }
            }
            while (i < len) {
                char c2 = s2.charAt(i);
                if (c2 <= '9' && c2 >= '0') {
                    i++;
                } else {
                    try {
                        return (int) parseDouble(s2);
                    } catch (NumberFormatException e) {
                        return def;
                    }
                }
            }
            try {
                return Integer.parseInt(s2);
            } catch (NumberFormatException e2) {
                return def;
            }
        }
        return def;
    }

    public static long parseAsLong(String s, long def) {
        String s2;
        int len;
        if (s != null && (len = (s2 = s.trim()).length()) != 0) {
            int i = 0;
            if (0 < len) {
                char c = s2.charAt(0);
                if (c == '+') {
                    s2 = s2.substring(1);
                    len = s2.length();
                } else if (c == '-') {
                    i = 0 + 1;
                }
            }
            while (i < len) {
                char c2 = s2.charAt(i);
                if (c2 <= '9' && c2 >= '0') {
                    i++;
                } else {
                    try {
                        return (long) parseDouble(s2);
                    } catch (NumberFormatException e) {
                        return def;
                    }
                }
            }
            try {
                return Long.parseLong(s2);
            } catch (NumberFormatException e2) {
                return def;
            }
        }
        return def;
    }

    public static double parseAsDouble(String s, double def) {
        if (s != null) {
            String s2 = s.trim();
            int len = s2.length();
            if (len != 0) {
                try {
                    return parseDouble(s2);
                } catch (NumberFormatException e) {
                    return def;
                }
            }
            return def;
        }
        return def;
    }

    public static double parseDouble(String s) throws NumberFormatException {
        if ("2.2250738585072012e-308".equals(s)) {
            return Double.MIN_VALUE;
        }
        return Double.parseDouble(s);
    }

    public static BigDecimal parseBigDecimal(char[] b) throws NumberFormatException {
        return parseBigDecimal(b, 0, b.length);
    }

    public static BigDecimal parseBigDecimal(char[] b, int off, int len) throws NumberFormatException {
        try {
            return new BigDecimal(b, off, len);
        } catch (NumberFormatException e) {
            throw _badBD(new String(b, off, len));
        }
    }

    private static NumberFormatException _badBD(String s) {
        return new NumberFormatException("Value \"" + s + "\" can not be represented as BigDecimal");
    }
}
