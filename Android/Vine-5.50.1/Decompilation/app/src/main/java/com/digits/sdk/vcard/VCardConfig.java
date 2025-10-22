package com.digits.sdk.vcard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class VCardConfig {
    private static final Set<Integer> sJapaneseMobileTypeSet;
    static String VCARD_TYPE_V21_GENERIC_STR = "v21_generic";
    public static int VCARD_TYPE_DEFAULT = -1073741824;
    private static final Map<String, Integer> sVCardTypeMap = new HashMap();

    static {
        sVCardTypeMap.put(VCARD_TYPE_V21_GENERIC_STR, -1073741824);
        sVCardTypeMap.put("v30_generic", -1073741823);
        sVCardTypeMap.put("v21_europe", -1073741820);
        sVCardTypeMap.put("v30_europe", -1073741819);
        sVCardTypeMap.put("v21_japanese_utf8", -1073741816);
        sVCardTypeMap.put("v30_japanese_utf8", -1073741815);
        sVCardTypeMap.put("v21_japanese_mobile", 402653192);
        sVCardTypeMap.put("docomo", 939524104);
        sJapaneseMobileTypeSet = new HashSet();
        sJapaneseMobileTypeSet.add(-1073741816);
        sJapaneseMobileTypeSet.add(-1073741815);
        sJapaneseMobileTypeSet.add(402653192);
        sJapaneseMobileTypeSet.add(939524104);
    }

    public static boolean isVersion21(int vcardType) {
        return (vcardType & 3) == 0;
    }

    public static boolean isVersion30(int vcardType) {
        return (vcardType & 3) == 1;
    }

    public static boolean isVersion40(int vcardType) {
        return (vcardType & 3) == 2;
    }

    public static boolean shouldUseQuotedPrintable(int vcardType) {
        return !isVersion30(vcardType);
    }

    public static int getNameOrderType(int vcardType) {
        return vcardType & 12;
    }

    public static boolean usesDefactProperty(int vcardType) {
        return (1073741824 & vcardType) != 0;
    }

    public static boolean shouldRefrainQPToNameProperties(int vcardType) {
        return (shouldUseQuotedPrintable(vcardType) && (268435456 & vcardType) == 0) ? false : true;
    }

    public static boolean appendTypeParamName(int vcardType) {
        return isVersion30(vcardType) || (67108864 & vcardType) != 0;
    }

    public static boolean isJapaneseDevice(int vcardType) {
        return sJapaneseMobileTypeSet.contains(Integer.valueOf(vcardType));
    }

    static boolean refrainPhoneNumberFormatting(int vcardType) {
        return (33554432 & vcardType) != 0;
    }

    public static boolean needsToConvertPhoneticString(int vcardType) {
        return (134217728 & vcardType) != 0;
    }

    public static boolean isDoCoMo(int vcardType) {
        return (536870912 & vcardType) != 0;
    }
}
