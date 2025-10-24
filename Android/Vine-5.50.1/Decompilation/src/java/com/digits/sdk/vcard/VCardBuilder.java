package com.digits.sdk.vcard;

import android.content.ContentValues;
import android.text.TextUtils;
import android.util.Log;
import com.digits.sdk.vcard.VCardUtils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/* loaded from: classes.dex */
public class VCardBuilder {
    private static final Set<String> sAllowedAndroidPropertySet = Collections.unmodifiableSet(new HashSet(Arrays.asList("vnd.android.cursor.item/nickname", "vnd.android.cursor.item/contact_event", "vnd.android.cursor.item/relation")));
    private final boolean mAppendTypeParamName;
    private StringBuilder mBuilder;
    private final String mCharset;
    private boolean mEndAppended;
    private final boolean mIsDoCoMo;
    private final boolean mIsJapaneseMobilePhone;
    private final boolean mIsV30OrV40;
    private final boolean mNeedsToConvertPhoneticString;
    private final boolean mRefrainsQPToNameProperties;
    private final boolean mShouldAppendCharsetParam;
    private final boolean mShouldUseQuotedPrintable;
    private final boolean mUsesDefactProperty;
    private final String mVCardCharsetParameter;
    private final int mVCardType;

    public VCardBuilder(int vcardType, String charset) throws UnsupportedEncodingException {
        this.mVCardType = vcardType;
        if (VCardConfig.isVersion40(vcardType)) {
            Log.w("vCard", "Should not use vCard 4.0 when building vCard. It is not officially published yet.");
        }
        this.mIsV30OrV40 = VCardConfig.isVersion30(vcardType) || VCardConfig.isVersion40(vcardType);
        this.mShouldUseQuotedPrintable = VCardConfig.shouldUseQuotedPrintable(vcardType);
        this.mIsDoCoMo = VCardConfig.isDoCoMo(vcardType);
        this.mIsJapaneseMobilePhone = VCardConfig.needsToConvertPhoneticString(vcardType);
        this.mUsesDefactProperty = VCardConfig.usesDefactProperty(vcardType);
        this.mRefrainsQPToNameProperties = VCardConfig.shouldRefrainQPToNameProperties(vcardType);
        this.mAppendTypeParamName = VCardConfig.appendTypeParamName(vcardType);
        this.mNeedsToConvertPhoneticString = VCardConfig.needsToConvertPhoneticString(vcardType);
        this.mShouldAppendCharsetParam = (VCardConfig.isVersion30(vcardType) && "UTF-8".equalsIgnoreCase(charset)) ? false : true;
        if (VCardConfig.isDoCoMo(vcardType)) {
            if (!"SHIFT_JIS".equalsIgnoreCase(charset) && TextUtils.isEmpty(charset)) {
                this.mCharset = "SHIFT_JIS";
            } else {
                this.mCharset = charset;
            }
            this.mVCardCharsetParameter = "CHARSET=SHIFT_JIS";
        } else if (TextUtils.isEmpty(charset)) {
            Log.i("vCard", "Use the charset \"UTF-8\" for export.");
            this.mCharset = "UTF-8";
            this.mVCardCharsetParameter = "CHARSET=UTF-8";
        } else {
            this.mCharset = charset;
            this.mVCardCharsetParameter = "CHARSET=" + charset;
        }
        clear();
    }

    public void clear() throws UnsupportedEncodingException {
        this.mBuilder = new StringBuilder();
        this.mEndAppended = false;
        appendLine("BEGIN", "VCARD");
        if (VCardConfig.isVersion40(this.mVCardType)) {
            appendLine("VERSION", "4.0");
        } else {
            if (VCardConfig.isVersion30(this.mVCardType)) {
                appendLine("VERSION", "3.0");
                return;
            }
            if (!VCardConfig.isVersion21(this.mVCardType)) {
                Log.w("vCard", "Unknown vCard version detected.");
            }
            appendLine("VERSION", "2.1");
        }
    }

    private boolean containsNonEmptyName(ContentValues contentValues) {
        String familyName = contentValues.getAsString("data3");
        String middleName = contentValues.getAsString("data5");
        String givenName = contentValues.getAsString("data2");
        String prefix = contentValues.getAsString("data4");
        String suffix = contentValues.getAsString("data6");
        String phoneticFamilyName = contentValues.getAsString("data9");
        String phoneticMiddleName = contentValues.getAsString("data8");
        String phoneticGivenName = contentValues.getAsString("data7");
        String displayName = contentValues.getAsString("data1");
        return (TextUtils.isEmpty(familyName) && TextUtils.isEmpty(middleName) && TextUtils.isEmpty(givenName) && TextUtils.isEmpty(prefix) && TextUtils.isEmpty(suffix) && TextUtils.isEmpty(phoneticFamilyName) && TextUtils.isEmpty(phoneticMiddleName) && TextUtils.isEmpty(phoneticGivenName) && TextUtils.isEmpty(displayName)) ? false : true;
    }

    private ContentValues getPrimaryContentValueWithStructuredName(List<ContentValues> contentValuesList) {
        ContentValues primaryContentValues = null;
        ContentValues subprimaryContentValues = null;
        Iterator i$ = contentValuesList.iterator();
        while (true) {
            if (!i$.hasNext()) {
                break;
            }
            ContentValues contentValues = i$.next();
            if (contentValues != null) {
                Integer isSuperPrimary = contentValues.getAsInteger("is_super_primary");
                if (isSuperPrimary != null && isSuperPrimary.intValue() > 0) {
                    primaryContentValues = contentValues;
                    break;
                }
                if (primaryContentValues == null) {
                    Integer isPrimary = contentValues.getAsInteger("is_primary");
                    if (isPrimary != null && isPrimary.intValue() > 0 && containsNonEmptyName(contentValues)) {
                        primaryContentValues = contentValues;
                    } else if (subprimaryContentValues == null && containsNonEmptyName(contentValues)) {
                        subprimaryContentValues = contentValues;
                    }
                }
            }
        }
        if (primaryContentValues == null) {
            if (subprimaryContentValues != null) {
                ContentValues primaryContentValues2 = subprimaryContentValues;
                return primaryContentValues2;
            }
            ContentValues primaryContentValues3 = new ContentValues();
            return primaryContentValues3;
        }
        return primaryContentValues;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x019f  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x01c9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.digits.sdk.vcard.VCardBuilder appendNamePropertiesV40(java.util.List<android.content.ContentValues> r23) throws java.io.UnsupportedEncodingException {
        /*
            Method dump skipped, instructions count: 506
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.digits.sdk.vcard.VCardBuilder.appendNamePropertiesV40(java.util.List):com.digits.sdk.vcard.VCardBuilder");
    }

    public VCardBuilder appendNameProperties(List<ContentValues> contentValuesList) throws UnsupportedEncodingException {
        String formattedName;
        String encodedFamily;
        String encodedGiven;
        String encodedMiddle;
        String encodedPrefix;
        String encodedSuffix;
        if (VCardConfig.isVersion40(this.mVCardType)) {
            return appendNamePropertiesV40(contentValuesList);
        }
        if (contentValuesList == null || contentValuesList.isEmpty()) {
            if (VCardConfig.isVersion30(this.mVCardType)) {
                appendLine("N", "");
                appendLine("FN", "");
                return this;
            }
            if (this.mIsDoCoMo) {
                appendLine("N", "");
                return this;
            }
            return this;
        }
        ContentValues contentValues = getPrimaryContentValueWithStructuredName(contentValuesList);
        String familyName = contentValues.getAsString("data3");
        String middleName = contentValues.getAsString("data5");
        String givenName = contentValues.getAsString("data2");
        String prefix = contentValues.getAsString("data4");
        String suffix = contentValues.getAsString("data6");
        String displayName = contentValues.getAsString("data1");
        if (!TextUtils.isEmpty(familyName) || !TextUtils.isEmpty(givenName)) {
            boolean reallyAppendCharsetParameterToName = shouldAppendCharsetParam(familyName, givenName, middleName, prefix, suffix);
            boolean reallyUseQuotedPrintableToName = (this.mRefrainsQPToNameProperties || (VCardUtils.containsOnlyNonCrLfPrintableAscii(familyName) && VCardUtils.containsOnlyNonCrLfPrintableAscii(givenName) && VCardUtils.containsOnlyNonCrLfPrintableAscii(middleName) && VCardUtils.containsOnlyNonCrLfPrintableAscii(prefix) && VCardUtils.containsOnlyNonCrLfPrintableAscii(suffix))) ? false : true;
            if (!TextUtils.isEmpty(displayName)) {
                formattedName = displayName;
            } else {
                formattedName = VCardUtils.constructNameFromElements(VCardConfig.getNameOrderType(this.mVCardType), familyName, middleName, givenName, prefix, suffix);
            }
            boolean reallyAppendCharsetParameterToFN = shouldAppendCharsetParam(formattedName);
            boolean reallyUseQuotedPrintableToFN = (this.mRefrainsQPToNameProperties || VCardUtils.containsOnlyNonCrLfPrintableAscii(formattedName)) ? false : true;
            if (reallyUseQuotedPrintableToName) {
                encodedFamily = encodeQuotedPrintable(familyName);
                encodedGiven = encodeQuotedPrintable(givenName);
                encodedMiddle = encodeQuotedPrintable(middleName);
                encodedPrefix = encodeQuotedPrintable(prefix);
                encodedSuffix = encodeQuotedPrintable(suffix);
            } else {
                encodedFamily = escapeCharacters(familyName);
                encodedGiven = escapeCharacters(givenName);
                encodedMiddle = escapeCharacters(middleName);
                encodedPrefix = escapeCharacters(prefix);
                encodedSuffix = escapeCharacters(suffix);
            }
            String encodedFormattedname = reallyUseQuotedPrintableToFN ? encodeQuotedPrintable(formattedName) : escapeCharacters(formattedName);
            this.mBuilder.append("N");
            if (this.mIsDoCoMo) {
                if (reallyAppendCharsetParameterToName) {
                    this.mBuilder.append(";");
                    this.mBuilder.append(this.mVCardCharsetParameter);
                }
                if (reallyUseQuotedPrintableToName) {
                    this.mBuilder.append(";");
                    this.mBuilder.append("ENCODING=QUOTED-PRINTABLE");
                }
                this.mBuilder.append(":");
                this.mBuilder.append(formattedName);
                this.mBuilder.append(";");
                this.mBuilder.append(";");
                this.mBuilder.append(";");
                this.mBuilder.append(";");
            } else {
                if (reallyAppendCharsetParameterToName) {
                    this.mBuilder.append(";");
                    this.mBuilder.append(this.mVCardCharsetParameter);
                }
                if (reallyUseQuotedPrintableToName) {
                    this.mBuilder.append(";");
                    this.mBuilder.append("ENCODING=QUOTED-PRINTABLE");
                }
                this.mBuilder.append(":");
                this.mBuilder.append(encodedFamily);
                this.mBuilder.append(";");
                this.mBuilder.append(encodedGiven);
                this.mBuilder.append(";");
                this.mBuilder.append(encodedMiddle);
                this.mBuilder.append(";");
                this.mBuilder.append(encodedPrefix);
                this.mBuilder.append(";");
                this.mBuilder.append(encodedSuffix);
            }
            this.mBuilder.append("\r\n");
            this.mBuilder.append("FN");
            if (reallyAppendCharsetParameterToFN) {
                this.mBuilder.append(";");
                this.mBuilder.append(this.mVCardCharsetParameter);
            }
            if (reallyUseQuotedPrintableToFN) {
                this.mBuilder.append(";");
                this.mBuilder.append("ENCODING=QUOTED-PRINTABLE");
            }
            this.mBuilder.append(":");
            this.mBuilder.append(encodedFormattedname);
            this.mBuilder.append("\r\n");
        } else if (!TextUtils.isEmpty(displayName)) {
            buildSinglePartNameField("N", displayName);
            this.mBuilder.append(";");
            this.mBuilder.append(";");
            this.mBuilder.append(";");
            this.mBuilder.append(";");
            this.mBuilder.append("\r\n");
            buildSinglePartNameField("FN", displayName);
            this.mBuilder.append("\r\n");
        } else if (VCardConfig.isVersion30(this.mVCardType)) {
            appendLine("N", "");
            appendLine("FN", "");
        } else if (this.mIsDoCoMo) {
            appendLine("N", "");
        }
        appendPhoneticNameFields(contentValues);
        return this;
    }

    private void buildSinglePartNameField(String property, String part) {
        boolean reallyUseQuotedPrintable = (this.mRefrainsQPToNameProperties || VCardUtils.containsOnlyNonCrLfPrintableAscii(part)) ? false : true;
        String encodedPart = reallyUseQuotedPrintable ? encodeQuotedPrintable(part) : escapeCharacters(part);
        this.mBuilder.append(property);
        if (shouldAppendCharsetParam(part)) {
            this.mBuilder.append(";");
            this.mBuilder.append(this.mVCardCharsetParameter);
        }
        if (reallyUseQuotedPrintable) {
            this.mBuilder.append(";");
            this.mBuilder.append("ENCODING=QUOTED-PRINTABLE");
        }
        this.mBuilder.append(":");
        this.mBuilder.append(encodedPart);
    }

    private void appendPhoneticNameFields(ContentValues contentValues) throws UnsupportedEncodingException {
        String phoneticFamilyName;
        String phoneticMiddleName;
        String phoneticGivenName;
        String encodedPhoneticFamilyName;
        String encodedPhoneticMiddleName;
        String encodedPhoneticGivenName;
        String encodedPhoneticFamilyName2;
        String encodedPhoneticMiddleName2;
        String encodedPhoneticGivenName2;
        String tmpPhoneticFamilyName = contentValues.getAsString("data9");
        String tmpPhoneticMiddleName = contentValues.getAsString("data8");
        String tmpPhoneticGivenName = contentValues.getAsString("data7");
        if (this.mNeedsToConvertPhoneticString) {
            phoneticFamilyName = VCardUtils.toHalfWidthString(tmpPhoneticFamilyName);
            phoneticMiddleName = VCardUtils.toHalfWidthString(tmpPhoneticMiddleName);
            phoneticGivenName = VCardUtils.toHalfWidthString(tmpPhoneticGivenName);
        } else {
            phoneticFamilyName = tmpPhoneticFamilyName;
            phoneticMiddleName = tmpPhoneticMiddleName;
            phoneticGivenName = tmpPhoneticGivenName;
        }
        if (TextUtils.isEmpty(phoneticFamilyName) && TextUtils.isEmpty(phoneticMiddleName) && TextUtils.isEmpty(phoneticGivenName)) {
            if (this.mIsDoCoMo) {
                this.mBuilder.append("SOUND");
                this.mBuilder.append(";");
                this.mBuilder.append("X-IRMC-N");
                this.mBuilder.append(":");
                this.mBuilder.append(";");
                this.mBuilder.append(";");
                this.mBuilder.append(";");
                this.mBuilder.append(";");
                this.mBuilder.append("\r\n");
                return;
            }
            return;
        }
        if (!VCardConfig.isVersion40(this.mVCardType)) {
            if (VCardConfig.isVersion30(this.mVCardType)) {
                String sortString = VCardUtils.constructNameFromElements(this.mVCardType, phoneticFamilyName, phoneticMiddleName, phoneticGivenName);
                this.mBuilder.append("SORT-STRING");
                if (VCardConfig.isVersion30(this.mVCardType) && shouldAppendCharsetParam(sortString)) {
                    this.mBuilder.append(";");
                    this.mBuilder.append(this.mVCardCharsetParameter);
                }
                this.mBuilder.append(":");
                this.mBuilder.append(escapeCharacters(sortString));
                this.mBuilder.append("\r\n");
            } else if (this.mIsJapaneseMobilePhone) {
                this.mBuilder.append("SOUND");
                this.mBuilder.append(";");
                this.mBuilder.append("X-IRMC-N");
                if ((this.mRefrainsQPToNameProperties || (VCardUtils.containsOnlyNonCrLfPrintableAscii(phoneticFamilyName) && VCardUtils.containsOnlyNonCrLfPrintableAscii(phoneticMiddleName) && VCardUtils.containsOnlyNonCrLfPrintableAscii(phoneticGivenName))) ? false : true) {
                    encodedPhoneticFamilyName = encodeQuotedPrintable(phoneticFamilyName);
                    encodedPhoneticMiddleName = encodeQuotedPrintable(phoneticMiddleName);
                    encodedPhoneticGivenName = encodeQuotedPrintable(phoneticGivenName);
                } else {
                    encodedPhoneticFamilyName = escapeCharacters(phoneticFamilyName);
                    encodedPhoneticMiddleName = escapeCharacters(phoneticMiddleName);
                    encodedPhoneticGivenName = escapeCharacters(phoneticGivenName);
                }
                if (shouldAppendCharsetParam(encodedPhoneticFamilyName, encodedPhoneticMiddleName, encodedPhoneticGivenName)) {
                    this.mBuilder.append(";");
                    this.mBuilder.append(this.mVCardCharsetParameter);
                }
                this.mBuilder.append(":");
                boolean first = true;
                if (!TextUtils.isEmpty(encodedPhoneticFamilyName)) {
                    this.mBuilder.append(encodedPhoneticFamilyName);
                    first = false;
                }
                if (!TextUtils.isEmpty(encodedPhoneticMiddleName)) {
                    if (first) {
                        first = false;
                    } else {
                        this.mBuilder.append(' ');
                    }
                    this.mBuilder.append(encodedPhoneticMiddleName);
                }
                if (!TextUtils.isEmpty(encodedPhoneticGivenName)) {
                    if (!first) {
                        this.mBuilder.append(' ');
                    }
                    this.mBuilder.append(encodedPhoneticGivenName);
                }
                this.mBuilder.append(";");
                this.mBuilder.append(";");
                this.mBuilder.append(";");
                this.mBuilder.append(";");
                this.mBuilder.append("\r\n");
            }
        }
        if (this.mUsesDefactProperty) {
            if (!TextUtils.isEmpty(phoneticGivenName)) {
                boolean reallyUseQuotedPrintable = this.mShouldUseQuotedPrintable && !VCardUtils.containsOnlyNonCrLfPrintableAscii(phoneticGivenName);
                if (reallyUseQuotedPrintable) {
                    encodedPhoneticGivenName2 = encodeQuotedPrintable(phoneticGivenName);
                } else {
                    encodedPhoneticGivenName2 = escapeCharacters(phoneticGivenName);
                }
                this.mBuilder.append("X-PHONETIC-FIRST-NAME");
                if (shouldAppendCharsetParam(phoneticGivenName)) {
                    this.mBuilder.append(";");
                    this.mBuilder.append(this.mVCardCharsetParameter);
                }
                if (reallyUseQuotedPrintable) {
                    this.mBuilder.append(";");
                    this.mBuilder.append("ENCODING=QUOTED-PRINTABLE");
                }
                this.mBuilder.append(":");
                this.mBuilder.append(encodedPhoneticGivenName2);
                this.mBuilder.append("\r\n");
            }
            if (!TextUtils.isEmpty(phoneticMiddleName)) {
                boolean reallyUseQuotedPrintable2 = this.mShouldUseQuotedPrintable && !VCardUtils.containsOnlyNonCrLfPrintableAscii(phoneticMiddleName);
                if (reallyUseQuotedPrintable2) {
                    encodedPhoneticMiddleName2 = encodeQuotedPrintable(phoneticMiddleName);
                } else {
                    encodedPhoneticMiddleName2 = escapeCharacters(phoneticMiddleName);
                }
                this.mBuilder.append("X-PHONETIC-MIDDLE-NAME");
                if (shouldAppendCharsetParam(phoneticMiddleName)) {
                    this.mBuilder.append(";");
                    this.mBuilder.append(this.mVCardCharsetParameter);
                }
                if (reallyUseQuotedPrintable2) {
                    this.mBuilder.append(";");
                    this.mBuilder.append("ENCODING=QUOTED-PRINTABLE");
                }
                this.mBuilder.append(":");
                this.mBuilder.append(encodedPhoneticMiddleName2);
                this.mBuilder.append("\r\n");
            }
            if (!TextUtils.isEmpty(phoneticFamilyName)) {
                boolean reallyUseQuotedPrintable3 = this.mShouldUseQuotedPrintable && !VCardUtils.containsOnlyNonCrLfPrintableAscii(phoneticFamilyName);
                if (reallyUseQuotedPrintable3) {
                    encodedPhoneticFamilyName2 = encodeQuotedPrintable(phoneticFamilyName);
                } else {
                    encodedPhoneticFamilyName2 = escapeCharacters(phoneticFamilyName);
                }
                this.mBuilder.append("X-PHONETIC-LAST-NAME");
                if (shouldAppendCharsetParam(phoneticFamilyName)) {
                    this.mBuilder.append(";");
                    this.mBuilder.append(this.mVCardCharsetParameter);
                }
                if (reallyUseQuotedPrintable3) {
                    this.mBuilder.append(";");
                    this.mBuilder.append("ENCODING=QUOTED-PRINTABLE");
                }
                this.mBuilder.append(":");
                this.mBuilder.append(encodedPhoneticFamilyName2);
                this.mBuilder.append("\r\n");
            }
        }
    }

    public VCardBuilder appendPhones(List<ContentValues> contentValuesList, VCardPhoneNumberTranslationCallback translationCallback) {
        String formatted;
        boolean phoneLineExists = false;
        if (contentValuesList != null) {
            Set<String> phoneSet = new HashSet<>();
            for (ContentValues contentValues : contentValuesList) {
                Integer typeAsObject = contentValues.getAsInteger("data2");
                String label = contentValues.getAsString("data3");
                Integer isPrimaryAsInteger = contentValues.getAsInteger("is_primary");
                boolean isPrimary = isPrimaryAsInteger != null && isPrimaryAsInteger.intValue() > 0;
                String phoneNumber = contentValues.getAsString("data1");
                if (phoneNumber != null) {
                    phoneNumber = phoneNumber.trim();
                }
                if (!TextUtils.isEmpty(phoneNumber)) {
                    int type = typeAsObject != null ? typeAsObject.intValue() : 1;
                    if (translationCallback != null) {
                        String phoneNumber2 = translationCallback.onValueReceived(phoneNumber, type, label, isPrimary);
                        if (!phoneSet.contains(phoneNumber2)) {
                            phoneSet.add(phoneNumber2);
                            appendTelLine(Integer.valueOf(type), label, phoneNumber2, isPrimary);
                        }
                    } else if (type == 6 || VCardConfig.refrainPhoneNumberFormatting(this.mVCardType)) {
                        phoneLineExists = true;
                        if (!phoneSet.contains(phoneNumber)) {
                            phoneSet.add(phoneNumber);
                            appendTelLine(Integer.valueOf(type), label, phoneNumber, isPrimary);
                        }
                    } else {
                        List<String> phoneNumberList = splitPhoneNumbers(phoneNumber);
                        if (!phoneNumberList.isEmpty()) {
                            phoneLineExists = true;
                            for (String actualPhoneNumber : phoneNumberList) {
                                if (!phoneSet.contains(actualPhoneNumber)) {
                                    String numberWithControlSequence = actualPhoneNumber.replace(',', 'p').replace(';', 'w');
                                    if (TextUtils.equals(numberWithControlSequence, actualPhoneNumber)) {
                                        StringBuilder digitsOnlyBuilder = new StringBuilder();
                                        int length = actualPhoneNumber.length();
                                        for (int i = 0; i < length; i++) {
                                            char ch = actualPhoneNumber.charAt(i);
                                            if (Character.isDigit(ch) || ch == '+') {
                                                digitsOnlyBuilder.append(ch);
                                            }
                                        }
                                        int phoneFormat = VCardUtils.getPhoneNumberFormat(this.mVCardType);
                                        formatted = VCardUtils.PhoneNumberUtilsPort.formatNumber(digitsOnlyBuilder.toString(), phoneFormat);
                                    } else {
                                        formatted = numberWithControlSequence;
                                    }
                                    if (VCardConfig.isVersion40(this.mVCardType) && !TextUtils.isEmpty(formatted) && !formatted.startsWith("tel:")) {
                                        formatted = "tel:" + formatted;
                                    }
                                    phoneSet.add(actualPhoneNumber);
                                    appendTelLine(Integer.valueOf(type), label, formatted, isPrimary);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!phoneLineExists && this.mIsDoCoMo) {
            appendTelLine(1, "", "", false);
        }
        return this;
    }

    private List<String> splitPhoneNumbers(String phoneNumber) {
        List<String> phoneList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        int length = phoneNumber.length();
        for (int i = 0; i < length; i++) {
            char ch = phoneNumber.charAt(i);
            if (ch == '\n' && builder.length() > 0) {
                phoneList.add(builder.toString());
                builder = new StringBuilder();
            } else {
                builder.append(ch);
            }
        }
        if (builder.length() > 0) {
            phoneList.add(builder.toString());
        }
        return phoneList;
    }

    public VCardBuilder appendEmails(List<ContentValues> contentValuesList) throws UnsupportedEncodingException {
        boolean emailAddressExists = false;
        if (contentValuesList != null) {
            Set<String> addressSet = new HashSet<>();
            for (ContentValues contentValues : contentValuesList) {
                String emailAddress = contentValues.getAsString("data1");
                if (emailAddress != null) {
                    emailAddress = emailAddress.trim();
                }
                if (!TextUtils.isEmpty(emailAddress)) {
                    Integer typeAsObject = contentValues.getAsInteger("data2");
                    int type = typeAsObject != null ? typeAsObject.intValue() : 3;
                    String label = contentValues.getAsString("data3");
                    Integer isPrimaryAsInteger = contentValues.getAsInteger("is_primary");
                    boolean isPrimary = isPrimaryAsInteger != null && isPrimaryAsInteger.intValue() > 0;
                    emailAddressExists = true;
                    if (!addressSet.contains(emailAddress)) {
                        addressSet.add(emailAddress);
                        appendEmailLine(type, label, emailAddress, isPrimary);
                    }
                }
            }
        }
        if (!emailAddressExists && this.mIsDoCoMo) {
            appendEmailLine(1, "", "", false);
        }
        return this;
    }

    public void appendEmailLine(int type, String label, String rawValue, boolean isPrimary) throws UnsupportedEncodingException {
        String typeAsString;
        switch (type) {
            case 0:
                if (VCardUtils.isMobilePhoneLabel(label)) {
                    typeAsString = "CELL";
                    break;
                } else if (!TextUtils.isEmpty(label) && VCardUtils.containsOnlyAlphaDigitHyphen(label)) {
                    typeAsString = "X-" + label;
                    break;
                } else {
                    typeAsString = null;
                    break;
                }
                break;
            case 1:
                typeAsString = "HOME";
                break;
            case 2:
                typeAsString = "WORK";
                break;
            case 3:
                typeAsString = null;
                break;
            case 4:
                typeAsString = "CELL";
                break;
            default:
                Log.e("vCard", "Unknown Email type: " + type);
                typeAsString = null;
                break;
        }
        List<String> parameterList = new ArrayList<>();
        if (isPrimary) {
            parameterList.add("PREF");
        }
        if (!TextUtils.isEmpty(typeAsString)) {
            parameterList.add(typeAsString);
        }
        appendLineWithCharsetAndQPDetection("EMAIL", parameterList, rawValue);
    }

    public void appendTelLine(Integer typeAsInteger, String label, String encodedValue, boolean isPrimary) {
        int type;
        this.mBuilder.append("TEL");
        this.mBuilder.append(";");
        if (typeAsInteger == null) {
            type = 7;
        } else {
            type = typeAsInteger.intValue();
        }
        ArrayList<String> parameterList = new ArrayList<>();
        switch (type) {
            case 0:
                if (TextUtils.isEmpty(label)) {
                    parameterList.add("VOICE");
                    break;
                } else if (VCardUtils.isMobilePhoneLabel(label)) {
                    parameterList.add("CELL");
                    break;
                } else if (this.mIsV30OrV40) {
                    parameterList.add(label);
                    break;
                } else {
                    String upperLabel = label.toUpperCase(Locale.getDefault());
                    if (!VCardUtils.isValidInV21ButUnknownToContactsPhoteType(upperLabel)) {
                        if (VCardUtils.containsOnlyAlphaDigitHyphen(label)) {
                            parameterList.add("X-" + label);
                            break;
                        }
                    } else {
                        parameterList.add(upperLabel);
                        break;
                    }
                }
                break;
            case 1:
                parameterList.addAll(Arrays.asList("HOME"));
                break;
            case 2:
                parameterList.add("CELL");
                break;
            case 3:
                parameterList.addAll(Arrays.asList("WORK"));
                break;
            case 4:
                parameterList.addAll(Arrays.asList("WORK", "FAX"));
                break;
            case 5:
                parameterList.addAll(Arrays.asList("HOME", "FAX"));
                break;
            case 6:
                if (this.mIsDoCoMo) {
                    parameterList.add("VOICE");
                    break;
                } else {
                    parameterList.add("PAGER");
                    break;
                }
            case 7:
                parameterList.add("VOICE");
                break;
            case 9:
                parameterList.add("CAR");
                break;
            case 10:
                parameterList.add("WORK");
                isPrimary = true;
                break;
            case 11:
                parameterList.add("ISDN");
                break;
            case 12:
                isPrimary = true;
                break;
            case 13:
                parameterList.add("FAX");
                break;
            case 15:
                parameterList.add("TLX");
                break;
            case 17:
                parameterList.addAll(Arrays.asList("WORK", "CELL"));
                break;
            case 18:
                parameterList.add("WORK");
                if (this.mIsDoCoMo) {
                    parameterList.add("VOICE");
                    break;
                } else {
                    parameterList.add("PAGER");
                    break;
                }
            case 20:
                parameterList.add("MSG");
                break;
        }
        if (isPrimary) {
            parameterList.add("PREF");
        }
        if (parameterList.isEmpty()) {
            appendUncommonPhoneType(this.mBuilder, Integer.valueOf(type));
        } else {
            appendTypeParameters(parameterList);
        }
        this.mBuilder.append(":");
        this.mBuilder.append(encodedValue);
        this.mBuilder.append("\r\n");
    }

    private void appendUncommonPhoneType(StringBuilder builder, Integer type) {
        if (this.mIsDoCoMo) {
            builder.append("VOICE");
            return;
        }
        String phoneType = VCardUtils.getPhoneTypeString(type);
        if (phoneType != null) {
            appendTypeParameter(phoneType);
        } else {
            Log.e("vCard", "Unknown or unsupported (by vCard) Phone type: " + type);
        }
    }

    public void appendLineWithCharsetAndQPDetection(String propertyName, List<String> parameterList, String rawValue) throws UnsupportedEncodingException {
        boolean needCharset = !VCardUtils.containsOnlyPrintableAscii(rawValue);
        boolean reallyUseQuotedPrintable = this.mShouldUseQuotedPrintable && !VCardUtils.containsOnlyNonCrLfPrintableAscii(rawValue);
        appendLine(propertyName, parameterList, rawValue, needCharset, reallyUseQuotedPrintable);
    }

    public void appendLine(String propertyName, String rawValue) throws UnsupportedEncodingException {
        appendLine(propertyName, rawValue, false, false);
    }

    public void appendLine(String propertyName, String rawValue, boolean needCharset, boolean reallyUseQuotedPrintable) throws UnsupportedEncodingException {
        appendLine(propertyName, null, rawValue, needCharset, reallyUseQuotedPrintable);
    }

    public void appendLine(String propertyName, List<String> parameterList, String rawValue, boolean needCharset, boolean reallyUseQuotedPrintable) throws UnsupportedEncodingException {
        String encodedValue;
        this.mBuilder.append(propertyName);
        if (parameterList != null && parameterList.size() > 0) {
            this.mBuilder.append(";");
            appendTypeParameters(parameterList);
        }
        if (needCharset) {
            this.mBuilder.append(";");
            this.mBuilder.append(this.mVCardCharsetParameter);
        }
        if (reallyUseQuotedPrintable) {
            this.mBuilder.append(";");
            this.mBuilder.append("ENCODING=QUOTED-PRINTABLE");
            encodedValue = encodeQuotedPrintable(rawValue);
        } else {
            encodedValue = escapeCharacters(rawValue);
        }
        this.mBuilder.append(":");
        this.mBuilder.append(encodedValue);
        this.mBuilder.append("\r\n");
    }

    private void appendTypeParameters(List<String> types) {
        boolean first = true;
        for (String typeValue : types) {
            if (VCardConfig.isVersion30(this.mVCardType) || VCardConfig.isVersion40(this.mVCardType)) {
                String encoded = VCardConfig.isVersion40(this.mVCardType) ? VCardUtils.toStringAsV40ParamValue(typeValue) : VCardUtils.toStringAsV30ParamValue(typeValue);
                if (!TextUtils.isEmpty(encoded)) {
                    if (first) {
                        first = false;
                    } else {
                        this.mBuilder.append(";");
                    }
                    appendTypeParameter(encoded);
                }
            } else if (VCardUtils.isV21Word(typeValue)) {
                if (first) {
                    first = false;
                } else {
                    this.mBuilder.append(";");
                }
                appendTypeParameter(typeValue);
            }
        }
    }

    private void appendTypeParameter(String type) {
        appendTypeParameter(this.mBuilder, type);
    }

    private void appendTypeParameter(StringBuilder builder, String type) {
        if (VCardConfig.isVersion40(this.mVCardType) || ((VCardConfig.isVersion30(this.mVCardType) || this.mAppendTypeParamName) && !this.mIsDoCoMo)) {
            builder.append("TYPE").append("=");
        }
        builder.append(type);
    }

    private boolean shouldAppendCharsetParam(String... propertyValueList) {
        if (!this.mShouldAppendCharsetParam) {
            return false;
        }
        for (String propertyValue : propertyValueList) {
            if (!VCardUtils.containsOnlyPrintableAscii(propertyValue)) {
                return true;
            }
        }
        return false;
    }

    private String encodeQuotedPrintable(String str) throws UnsupportedEncodingException {
        byte[] strArray;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int index = 0;
        int lineCount = 0;
        try {
            strArray = str.getBytes(this.mCharset);
        } catch (UnsupportedEncodingException e) {
            Log.e("vCard", "Charset " + this.mCharset + " cannot be used. Try default charset");
            strArray = str.getBytes();
        }
        while (index < strArray.length) {
            builder.append(String.format("=%02X", Byte.valueOf(strArray[index])));
            index++;
            lineCount += 3;
            if (lineCount >= 67) {
                builder.append("=\r\n");
                lineCount = 0;
            }
        }
        return builder.toString();
    }

    private String escapeCharacters(String unescaped) {
        if (TextUtils.isEmpty(unescaped)) {
            return "";
        }
        StringBuilder tmpBuilder = new StringBuilder();
        int length = unescaped.length();
        for (int i = 0; i < length; i++) {
            char ch = unescaped.charAt(i);
            switch (ch) {
                case '\r':
                    if (i + 1 < length) {
                        char nextChar = unescaped.charAt(i);
                        if (nextChar == '\n') {
                            break;
                        }
                    }
                case '\n':
                    tmpBuilder.append("\\n");
                    break;
                case ',':
                    if (this.mIsV30OrV40) {
                        tmpBuilder.append("\\,");
                        break;
                    } else {
                        tmpBuilder.append(ch);
                        break;
                    }
                case ';':
                    tmpBuilder.append('\\');
                    tmpBuilder.append(';');
                    break;
                case '\\':
                    if (this.mIsV30OrV40) {
                        tmpBuilder.append("\\\\");
                        break;
                    }
                case '<':
                case '>':
                    if (this.mIsDoCoMo) {
                        tmpBuilder.append('\\');
                        tmpBuilder.append(ch);
                        break;
                    } else {
                        tmpBuilder.append(ch);
                        break;
                    }
                default:
                    tmpBuilder.append(ch);
                    break;
            }
        }
        return tmpBuilder.toString();
    }

    public String toString() throws UnsupportedEncodingException {
        if (!this.mEndAppended) {
            if (this.mIsDoCoMo) {
                appendLine("X-CLASS", "PUBLIC");
                appendLine("X-REDUCTION", "");
                appendLine("X-NO", "");
                appendLine("X-DCM-HMN-MODE", "");
            }
            appendLine("END", "VCARD");
            this.mEndAppended = true;
        }
        return this.mBuilder.toString();
    }
}
