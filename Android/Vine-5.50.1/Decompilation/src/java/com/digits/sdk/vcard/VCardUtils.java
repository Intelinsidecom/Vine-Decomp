package com.digits.sdk.vcard;

import android.telephony.PhoneNumberUtils;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.SparseArray;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class VCardUtils {
    private static final int[] sEscapeIndicatorsV30;
    private static final int[] sEscapeIndicatorsV40;
    private static final SparseArray<String> sKnownImPropNameMap_ItoS;
    private static final Set<String> sMobilePhoneLabelSet;
    private static final Set<String> sPhoneTypesUnknownToContactsSet;
    private static final Set<Character> sUnAcceptableAsciiInV21WordSet;
    private static final SparseArray<String> sKnownPhoneTypesMap_ItoS = new SparseArray<>();
    private static final Map<String, Integer> sKnownPhoneTypeMap_StoI = new HashMap();

    public static class PhoneNumberUtilsPort {
        public static String formatNumber(String source, int defaultFormattingType) {
            SpannableStringBuilder text = new SpannableStringBuilder(source);
            PhoneNumberUtils.formatNumber(text, defaultFormattingType);
            return text.toString();
        }
    }

    public static class TextUtilsPort {
        public static boolean isPrintableAscii(char c) {
            return (' ' <= c && c <= '~') || c == '\r' || c == '\n';
        }

        public static boolean isPrintableAsciiOnly(CharSequence str) {
            int len = str.length();
            for (int i = 0; i < len; i++) {
                if (!isPrintableAscii(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    static {
        sKnownPhoneTypesMap_ItoS.put(9, "CAR");
        sKnownPhoneTypeMap_StoI.put("CAR", 9);
        sKnownPhoneTypesMap_ItoS.put(6, "PAGER");
        sKnownPhoneTypeMap_StoI.put("PAGER", 6);
        sKnownPhoneTypesMap_ItoS.put(11, "ISDN");
        sKnownPhoneTypeMap_StoI.put("ISDN", 11);
        sKnownPhoneTypeMap_StoI.put("HOME", 1);
        sKnownPhoneTypeMap_StoI.put("WORK", 3);
        sKnownPhoneTypeMap_StoI.put("CELL", 2);
        sKnownPhoneTypeMap_StoI.put("OTHER", 7);
        sKnownPhoneTypeMap_StoI.put("CALLBACK", 8);
        sKnownPhoneTypeMap_StoI.put("COMPANY-MAIN", 10);
        sKnownPhoneTypeMap_StoI.put("RADIO", 14);
        sKnownPhoneTypeMap_StoI.put("TTY-TDD", 16);
        sKnownPhoneTypeMap_StoI.put("ASSISTANT", 19);
        sKnownPhoneTypeMap_StoI.put("VOICE", 7);
        sPhoneTypesUnknownToContactsSet = new HashSet();
        sPhoneTypesUnknownToContactsSet.add("MODEM");
        sPhoneTypesUnknownToContactsSet.add("MSG");
        sPhoneTypesUnknownToContactsSet.add("BBS");
        sPhoneTypesUnknownToContactsSet.add("VIDEO");
        sKnownImPropNameMap_ItoS = new SparseArray<>();
        sKnownImPropNameMap_ItoS.put(0, "X-AIM");
        sKnownImPropNameMap_ItoS.put(1, "X-MSN");
        sKnownImPropNameMap_ItoS.put(2, "X-YAHOO");
        sKnownImPropNameMap_ItoS.put(3, "X-SKYPE-USERNAME");
        sKnownImPropNameMap_ItoS.put(5, "X-GOOGLE-TALK");
        sKnownImPropNameMap_ItoS.put(6, "X-ICQ");
        sKnownImPropNameMap_ItoS.put(7, "X-JABBER");
        sKnownImPropNameMap_ItoS.put(4, "X-QQ");
        sKnownImPropNameMap_ItoS.put(8, "X-NETMEETING");
        sMobilePhoneLabelSet = new HashSet(Arrays.asList("MOBILE", "携帯電話", "携帯", "ケイタイ", "ｹｲﾀｲ"));
        sUnAcceptableAsciiInV21WordSet = new HashSet(Arrays.asList('[', ']', '=', ':', '.', ',', ' '));
        sEscapeIndicatorsV30 = new int[]{58, 59, 44, 32};
        sEscapeIndicatorsV40 = new int[]{59, 58};
    }

    public static String getPhoneTypeString(Integer type) {
        return sKnownPhoneTypesMap_ItoS.get(type.intValue());
    }

    public static boolean isMobilePhoneLabel(String label) {
        return "_AUTO_CELL".equals(label) || sMobilePhoneLabelSet.contains(label);
    }

    public static boolean isValidInV21ButUnknownToContactsPhoteType(String label) {
        return sPhoneTypesUnknownToContactsSet.contains(label);
    }

    public static String[] sortNameElements(int nameOrder, String familyName, String middleName, String givenName) {
        String[] list = new String[3];
        int nameOrderType = VCardConfig.getNameOrderType(nameOrder);
        switch (nameOrderType) {
            case 4:
                list[0] = middleName;
                list[1] = givenName;
                list[2] = familyName;
                return list;
            case 8:
                if (containsOnlyPrintableAscii(familyName) && containsOnlyPrintableAscii(givenName)) {
                    list[0] = givenName;
                    list[1] = middleName;
                    list[2] = familyName;
                } else {
                    list[0] = familyName;
                    list[1] = middleName;
                    list[2] = givenName;
                }
                return list;
            default:
                list[0] = givenName;
                list[1] = middleName;
                list[2] = familyName;
                return list;
        }
    }

    public static int getPhoneNumberFormat(int vcardType) {
        return VCardConfig.isJapaneseDevice(vcardType) ? 2 : 1;
    }

    public static String constructNameFromElements(int nameOrder, String familyName, String middleName, String givenName) {
        return constructNameFromElements(nameOrder, familyName, middleName, givenName, null, null);
    }

    public static String constructNameFromElements(int nameOrder, String familyName, String middleName, String givenName, String prefix, String suffix) {
        StringBuilder builder = new StringBuilder();
        String[] nameList = sortNameElements(nameOrder, familyName, middleName, givenName);
        boolean first = true;
        if (!TextUtils.isEmpty(prefix)) {
            first = false;
            builder.append(prefix);
        }
        for (String namePart : nameList) {
            if (!TextUtils.isEmpty(namePart)) {
                if (first) {
                    first = false;
                } else {
                    builder.append(' ');
                }
                builder.append(namePart);
            }
        }
        if (!TextUtils.isEmpty(suffix)) {
            if (!first) {
                builder.append(' ');
            }
            builder.append(suffix);
        }
        return builder.toString();
    }

    public static boolean containsOnlyPrintableAscii(String... values) {
        if (values == null) {
            return true;
        }
        return containsOnlyPrintableAscii(Arrays.asList(values));
    }

    public static boolean containsOnlyPrintableAscii(Collection<String> values) {
        if (values == null) {
            return true;
        }
        for (String value : values) {
            if (!TextUtils.isEmpty(value) && !TextUtilsPort.isPrintableAsciiOnly(value)) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsOnlyNonCrLfPrintableAscii(String... values) {
        if (values == null) {
            return true;
        }
        return containsOnlyNonCrLfPrintableAscii(Arrays.asList(values));
    }

    public static boolean containsOnlyNonCrLfPrintableAscii(Collection<String> values) {
        if (values == null) {
            return true;
        }
        for (String value : values) {
            if (!TextUtils.isEmpty(value)) {
                int length = value.length();
                for (int i = 0; i < length; i = value.offsetByCodePoints(i, 1)) {
                    int c = value.codePointAt(i);
                    if (32 > c || c > 126) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean containsOnlyAlphaDigitHyphen(String... values) {
        if (values == null) {
            return true;
        }
        return containsOnlyAlphaDigitHyphen(Arrays.asList(values));
    }

    public static boolean containsOnlyAlphaDigitHyphen(Collection<String> values) {
        if (values == null) {
            return true;
        }
        for (String str : values) {
            if (!TextUtils.isEmpty(str)) {
                int length = str.length();
                for (int i = 0; i < length; i = str.offsetByCodePoints(i, 1)) {
                    int codepoint = str.codePointAt(i);
                    if ((97 > codepoint || codepoint >= 123) && ((65 > codepoint || codepoint >= 91) && ((48 > codepoint || codepoint >= 58) && codepoint != 45))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean containsOnlyWhiteSpaces(String... values) {
        if (values == null) {
            return true;
        }
        return containsOnlyWhiteSpaces(Arrays.asList(values));
    }

    public static boolean containsOnlyWhiteSpaces(Collection<String> values) {
        if (values == null) {
            return true;
        }
        for (String str : values) {
            if (!TextUtils.isEmpty(str)) {
                int length = str.length();
                for (int i = 0; i < length; i = str.offsetByCodePoints(i, 1)) {
                    if (!Character.isWhitespace(str.codePointAt(i))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean isV21Word(String value) {
        if (TextUtils.isEmpty(value)) {
            return true;
        }
        int length = value.length();
        int i = 0;
        while (i < length) {
            int c = value.codePointAt(i);
            if (32 <= c && c <= 126 && !sUnAcceptableAsciiInV21WordSet.contains(Character.valueOf((char) c))) {
                i = value.offsetByCodePoints(i, 1);
            } else {
                return false;
            }
        }
        return true;
    }

    public static String toStringAsV30ParamValue(String value) {
        return toStringAsParamValue(value, sEscapeIndicatorsV30);
    }

    public static String toStringAsV40ParamValue(String value) {
        return toStringAsParamValue(value, sEscapeIndicatorsV40);
    }

    private static String toStringAsParamValue(String value, int[] escapeIndicators) {
        if (TextUtils.isEmpty(value)) {
            value = "";
        }
        StringBuilder builder = new StringBuilder();
        int length = value.length();
        boolean needQuote = false;
        for (int i = 0; i < length; i = value.offsetByCodePoints(i, 1)) {
            int codePoint = value.codePointAt(i);
            if (codePoint >= 32 && codePoint != 34) {
                builder.appendCodePoint(codePoint);
                int len$ = escapeIndicators.length;
                int i$ = 0;
                while (true) {
                    if (i$ < len$) {
                        int indicator = escapeIndicators[i$];
                        if (codePoint != indicator) {
                            i$++;
                        } else {
                            needQuote = true;
                            break;
                        }
                    }
                }
            }
        }
        String result = builder.toString();
        return (result.length() == 0 || containsOnlyWhiteSpaces(result)) ? "" : needQuote ? '\"' + result + '\"' : result;
    }

    public static String toHalfWidthString(String orgString) {
        if (TextUtils.isEmpty(orgString)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        int length = orgString.length();
        int i = 0;
        while (i < length) {
            char ch = orgString.charAt(i);
            String halfWidthText = JapaneseUtils.tryGetHalfWidthText(ch);
            if (halfWidthText != null) {
                builder.append(halfWidthText);
            } else {
                builder.append(ch);
            }
            i = orgString.offsetByCodePoints(i, 1);
        }
        return builder.toString();
    }
}
