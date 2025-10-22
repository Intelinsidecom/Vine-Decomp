package co.vine.android.util;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import co.vine.android.Settings;
import com.edisonwang.android.slog.MessageFormatter;
import com.edisonwang.android.slog.SLog;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.flurry.android.Constants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.Character;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.message.BasicNameValuePair;

/* loaded from: classes.dex */
public class CommonUtil {
    private static String sPackageVersionName;
    public static boolean DEBUG_VERBOSE = false;
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();
    public static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols();
    private static Toast t = null;
    public static final Pattern PATTERN_USERNAME = Pattern.compile("<:\\s*?user\\s*?\\|\\s*?.*?:>([\\w\\s-]*?)<:>");
    public static final SecureRandom sRandom = new SecureRandom();
    private static final char[] numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    public static final SimpleDateFormat DATE_TIME_RFC1123 = new SynchronizedDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

    public static boolean isUrlLocal(String path) {
        if (path == null) {
            return false;
        }
        try {
            String scheme = Uri.parse(path).getScheme();
            return "file".equals(scheme);
        } catch (Exception e) {
            return false;
        }
    }

    public static String printCursorWindowStats() throws Throwable {
        String str;
        CursorWindow window = null;
        try {
            try {
                CursorWindow window2 = new CursorWindow(true);
                try {
                    Method m = window2.getClass().getDeclaredMethod("printStats", new Class[0]);
                    m.setAccessible(true);
                    str = (String) m.invoke(window2, new Object[0]);
                    if (window2 != null) {
                        window2.close();
                    }
                } catch (Exception e) {
                    e = e;
                    window = window2;
                    SLog.e("print cursor error", (Throwable) e);
                    if (window != null) {
                        window.close();
                    }
                    str = null;
                    return str;
                } catch (Throwable th) {
                    th = th;
                    window = window2;
                    if (window != null) {
                        window.close();
                    }
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
            }
            return str;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static String stripUsernameEntities(String string) {
        String username = getUsernameFromActivity(string);
        Matcher matcher = PATTERN_USERNAME.matcher(string);
        return matcher.replaceAll(username);
    }

    public static void closeSilently(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static String randomString(int length) {
        if (length < 1) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[sRandom.nextInt(71)];
        }
        return new String(randBuffer);
    }

    public static File getTempFile(Context context, int fileNameSize) {
        File cacheDir = getCacheDir(context);
        if (cacheDir != null) {
            return new File(cacheDir, randomString(fileNameSize));
        }
        return null;
    }

    public static Uri getUriFromResouce(Context context, int res) {
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + res);
    }

    public static File getTempFile(Context context) {
        return getTempFile(context, 6);
    }

    public static File getCacheDir(Context context) {
        File externalCacheDir = getExternalCacheDir(context);
        return externalCacheDir != null ? externalCacheDir : context.getCacheDir();
    }

    public static File getExternalCacheDir(Context c) {
        try {
            return c.getExternalCacheDir();
        } catch (Throwable th) {
            return null;
        }
    }

    public static int readFullyWriteTo(InputStream is, OutputStream os, int bufferSize) throws IOException {
        byte[] buf = new byte[bufferSize];
        int totalBytes = 0;
        while (true) {
            int bytesRead = is.read(buf);
            if (bytesRead != -1) {
                if (os != null) {
                    os.write(buf, 0, bytesRead);
                }
                totalBytes += bytesRead;
            } else {
                return totalBytes;
            }
        }
    }

    public static File getFilesDir(Context context) {
        File f = null;
        try {
            f = getExternalFilesDir(context);
        } catch (Exception e) {
            SLog.w("Failed to get external dir.", (Throwable) e);
        }
        if (f != null) {
            return f;
        }
        File f2 = context.getFilesDir();
        return f2;
    }

    @TargetApi(8)
    public static File getExternalFilesDir(Context c) {
        return Build.VERSION.SDK_INT > 7 ? c.getExternalFilesDir(null) : Environment.getExternalStorageDirectory();
    }

    public static void showCenteredToast(Context context, int resId, int length) {
        if (t != null) {
            t.cancel();
        }
        t = Toast.makeText(context, resId, length);
        t.setGravity(17, 0, 0);
        t.show();
    }

    public static void showCenteredToast(Context context, int resId) {
        showCenteredToast(context, resId, 1);
    }

    public static boolean isDefaultAvatarUrl(String url) {
        return TextUtils.isEmpty(url) || url.contains("default.png");
    }

    public static void showDefaultToast(Context context, int resId) {
        if (t != null) {
            t.cancel();
        }
        t = Toast.makeText(context, context.getString(resId), 1);
        t.show();
    }

    public static void showDefaultToast(Context context, String text) {
        if (t != null) {
            t.cancel();
        }
        t = Toast.makeText(context, text, 1);
        t.show();
    }

    public static void showTopToast(Context context, int resId) {
        if (t != null) {
            t.cancel();
        }
        t = Toast.makeText(context, context.getText(resId), 1);
        t.setGravity(48, 0, 0);
        t.show();
    }

    public static Toast showCenteredToast(Context context, String text) {
        if (t != null) {
            t.cancel();
        }
        t = Toast.makeText(context, text, 1);
        ViewGroup toastView = (ViewGroup) t.getView();
        if (toastView != null) {
            View textView = toastView.getChildAt(0);
            textView.setBackgroundColor(0);
        }
        t.setGravity(17, 0, 0);
        t.show();
        return t;
    }

    public static Toast showCenteredToast(Context context, String format, Object... args) {
        if (t != null) {
            t.cancel();
        }
        t = Toast.makeText(context, MessageFormatter.toStringMessage(format, args), 1);
        ViewGroup toastView = (ViewGroup) t.getView();
        if (toastView != null) {
            View textView = toastView.getChildAt(0);
            textView.setBackgroundColor(0);
        }
        t.setGravity(17, 0, 0);
        t.show();
        return t;
    }

    public static void showShortCenteredToast(Context context, String text) {
        if (t != null) {
            t.cancel();
        }
        t = Toast.makeText(context, text, 0);
        t.setGravity(17, 0, 0);
        t.show();
    }

    public static URI parseURI(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            int colonIndex = uri.indexOf(58);
            if (colonIndex != -1 && colonIndex != uri.length()) {
                String scheme = uri.substring(0, colonIndex);
                String ssp = uri.substring(colonIndex + 1);
                try {
                    return new URI(scheme, ssp, null);
                } catch (URISyntaxException e2) {
                    return null;
                }
            }
            return null;
        }
    }

    public static String md5Digest(String msg) throws NoSuchAlgorithmException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] md5bytes = messageDigest.digest(msg.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte md5byte : md5bytes) {
                String hex = Integer.toHexString(md5byte & Constants.UNKNOWN);
                if (hex.length() < 2) {
                    hexString.append('0').append(hex);
                } else {
                    hexString.append(hex);
                }
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static void setSoftKeyboardVisibility(Context context, View view, boolean visible) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService("input_method");
        if (inputMethodManager != null) {
            if (visible) {
                inputMethodManager.showSoftInput(view, 0);
            } else {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static String getUsernameFromActivity(String body) {
        Matcher m = PATTERN_USERNAME.matcher(body);
        if (m.find()) {
            return m.group(1).trim();
        }
        return null;
    }

    public static String getAddressBookJson(ContentResolver resolver) throws IOException {
        HashMap<String, AddressBookContact> contactsHashMap = new HashMap<>();
        JsonFactory jf = new JsonFactory();
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        JsonGenerator jg = jf.createJsonGenerator(ba);
        String[] projection = {"starred", "times_contacted", "display_name", "display_name", "display_name_alt", "data1", "data2"};
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                boolean starred = cursor.getInt(0) > 0;
                int timesContacted = cursor.getInt(1);
                String name = cursor.getString(2);
                if (TextUtils.isEmpty(name)) {
                    name = cursor.getString(3);
                }
                if (TextUtils.isEmpty(name)) {
                    name = cursor.getString(4);
                }
                String phone = cursor.getString(5);
                String label = getPhoneLabelFromType(cursor.getInt(6));
                if (!TextUtils.isEmpty(phone)) {
                    if (TextUtils.isEmpty(name)) {
                        name = phone;
                    }
                    AddressBookContact contact = contactsHashMap.containsKey(name) ? contactsHashMap.get(name) : new AddressBookContact(name);
                    contact.addPhone(label, phone);
                    if (starred) {
                        contact.setStarred();
                    }
                    if (timesContacted > 0) {
                        contact.setTimesContacted(timesContacted);
                    }
                    contactsHashMap.put(name, contact);
                }
            }
            cursor.close();
        }
        String[] projection2 = {"starred", "times_contacted", "display_name", "data4", "display_name_alt", "data1", "data2"};
        Cursor cursor2 = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection2, null, null, null);
        if (cursor2 != null) {
            while (cursor2.moveToNext()) {
                boolean starred2 = cursor2.getInt(0) > 0;
                int timesContacted2 = cursor2.getInt(1);
                String name2 = cursor2.getString(2);
                if (TextUtils.isEmpty(name2)) {
                    name2 = cursor2.getString(3);
                }
                if (TextUtils.isEmpty(name2)) {
                    name2 = cursor2.getString(4);
                }
                String email = cursor2.getString(5);
                String label2 = getEmailLabelFromType(cursor2.getInt(6));
                if (!TextUtils.isEmpty(email)) {
                    if (TextUtils.isEmpty(name2)) {
                        name2 = email;
                    }
                    AddressBookContact contact2 = contactsHashMap.containsKey(name2) ? contactsHashMap.get(name2) : new AddressBookContact(name2);
                    if (starred2) {
                        contact2.setStarred();
                    }
                    if (timesContacted2 > 0) {
                        contact2.setTimesContacted(timesContacted2);
                    }
                    contact2.addEmail(label2, email);
                    contactsHashMap.put(name2, contact2);
                }
            }
            cursor2.close();
        }
        if (!contactsHashMap.isEmpty()) {
            jg.writeStartArray();
            for (AddressBookContact contact3 : contactsHashMap.values()) {
                jg.writeStartObject();
                jg.writeStringField("name", contact3.name);
                if (contact3.starred) {
                    jg.writeBooleanField("starred", true);
                }
                if (contact3.timesContacted > 0) {
                    jg.writeNumberField("timesContacted", contact3.timesContacted);
                }
                if (contact3.emails != null) {
                    jg.writeFieldName("emailAddresses");
                    jg.writeStartArray();
                    Iterator<HashSet<BasicNameValuePair>> it = contact3.emails.iterator();
                    while (it.hasNext()) {
                        HashSet<BasicNameValuePair> fieldSet = it.next();
                        jg.writeStartObject();
                        Iterator<BasicNameValuePair> it2 = fieldSet.iterator();
                        while (it2.hasNext()) {
                            BasicNameValuePair field = it2.next();
                            jg.writeObjectField(field.getName(), field.getValue());
                        }
                        jg.writeEndObject();
                    }
                    jg.writeEndArray();
                }
                if (contact3.phones != null) {
                    jg.writeFieldName("phoneNumbers");
                    jg.writeStartArray();
                    Iterator<HashSet<BasicNameValuePair>> it3 = contact3.phones.iterator();
                    while (it3.hasNext()) {
                        HashSet<BasicNameValuePair> fieldSet2 = it3.next();
                        jg.writeStartObject();
                        Iterator<BasicNameValuePair> it4 = fieldSet2.iterator();
                        while (it4.hasNext()) {
                            BasicNameValuePair field2 = it4.next();
                            jg.writeObjectField(field2.getName(), field2.getValue());
                        }
                        jg.writeEndObject();
                    }
                    jg.writeEndArray();
                }
                jg.writeEndObject();
            }
            jg.writeEndArray();
            jg.close();
        }
        return ba.toString();
    }

    private static String getEmailLabelFromType(int type) {
        switch (type) {
            case 1:
                return "home";
            case 2:
                return "work";
            default:
                return "other";
        }
    }

    private static String getPhoneLabelFromType(int type) {
        switch (type) {
            case 1:
                return "home";
            case 2:
                return "mobile";
            case 3:
                return "work";
            case 4:
            case 5:
            case 13:
                return "fax";
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            default:
                return "other";
            case 12:
                return "main";
        }
    }

    private static class AddressBookContact {
        public HashSet<HashSet<BasicNameValuePair>> emails;
        public String name;
        public HashSet<HashSet<BasicNameValuePair>> phones;
        public boolean starred = false;
        public int timesContacted = 0;

        public AddressBookContact(String name) {
            this.name = name;
        }

        public void addEmail(String label, String email) {
            if (this.emails == null) {
                this.emails = new HashSet<>();
            }
            HashSet<BasicNameValuePair> fields = new HashSet<>();
            fields.add(new BasicNameValuePair("emailAddress", email));
            if (!TextUtils.isEmpty(label)) {
                fields.add(new BasicNameValuePair("label", label));
            }
            this.emails.add(fields);
        }

        public void addPhone(String label, String phone) {
            if (this.phones == null) {
                this.phones = new HashSet<>();
            }
            HashSet<BasicNameValuePair> fields = new HashSet<>();
            fields.add(new BasicNameValuePair("phoneNumber", phone));
            if (!TextUtils.isEmpty(label)) {
                fields.add(new BasicNameValuePair("label", label));
            }
            this.phones.add(fields);
        }

        public void setStarred() {
            this.starred = true;
        }

        public void setTimesContacted(int timesContacted) {
            this.timesContacted = timesContacted;
        }
    }

    public static Object fromByteArray(byte[] bytes) throws Throwable {
        ObjectInputStream ois;
        Object object = null;
        if (bytes != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois2 = null;
            try {
                ois = new ObjectInputStream(bis);
            } catch (IOException e) {
            } catch (ClassNotFoundException e2) {
            } catch (Throwable th) {
                th = th;
            }
            try {
                object = ois.readObject();
                closeSilently(ois);
                closeSilently(bis);
            } catch (IOException e3) {
                ois2 = ois;
                closeSilently(ois2);
                closeSilently(bis);
                return object;
            } catch (ClassNotFoundException e4) {
                ois2 = ois;
                closeSilently(ois2);
                closeSilently(bis);
                return object;
            } catch (Throwable th2) {
                th = th2;
                ois2 = ois;
                closeSilently(ois2);
                closeSilently(bis);
                throw th;
            }
        }
        return object;
    }

    public static byte[] toByteArray(Object o) throws Throwable {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
        ObjectOutputStream oos = null;
        try {
            ObjectOutputStream oos2 = new ObjectOutputStream(bos);
            try {
                oos2.writeObject(o);
                byte[] byteArray = bos.toByteArray();
                closeSilently(oos2);
                closeSilently(bos);
                return byteArray;
            } catch (IOException e) {
                oos = oos2;
                closeSilently(oos);
                closeSilently(bos);
                return null;
            } catch (Throwable th) {
                th = th;
                oos = oos2;
                closeSilently(oos);
                closeSilently(bos);
                throw th;
            }
        } catch (IOException e2) {
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static String getVersionName(Context context) throws PackageManager.NameNotFoundException {
        String packageName = context.getPackageName();
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        return pInfo.versionName;
    }

    public static String cleanse(String text) {
        return text.replace("\n", " ").trim();
    }

    public static void removeCache(Context context) {
        removeDir(getExternalCacheDir(context));
        removeDir(getExternalFilesDir(context));
        removeDir(getCacheDir(context));
    }

    public static void removeDir(File dir) {
        if (dir != null) {
            deleteFileRecursively(dir);
        }
    }

    public static void deleteFileRecursively(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        deleteFileRecursively(f);
                    }
                    return;
                }
                return;
            }
            file.delete();
        }
    }

    public static SharedPreferences getDefaultSharedPrefs(Context context) {
        return context.getSharedPreferences(context.getPackageName() + "_preferences", 4);
    }

    public static boolean isXauth2FactorError(Exception e) {
        String message = e.getCause().getMessage();
        return message.contains("231") && message.contains("User must verify login");
    }

    public static boolean isRtlLanguage(String text) {
        if (text.length() <= 0) {
            return false;
        }
        float totalCount = 0.0f;
        float rtlCount = 0.0f;
        for (char c : text.toCharArray()) {
            if (Character.UnicodeBlock.of(c) != Character.UnicodeBlock.GENERAL_PUNCTUATION) {
                if (isRtlCharacter(c)) {
                    rtlCount += 1.0f;
                }
                if (!isEmojiOrSymbol(c)) {
                    totalCount += 1.0f;
                }
            }
        }
        return ((double) (rtlCount / totalCount)) > 0.3d;
    }

    public static boolean isRtlCharacter(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return block == Character.UnicodeBlock.ARABIC || block == Character.UnicodeBlock.ARABIC_PRESENTATION_FORMS_A || block == Character.UnicodeBlock.ARABIC_PRESENTATION_FORMS_B || block == Character.UnicodeBlock.HEBREW;
    }

    public static boolean isEmojiOrSymbol(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return block == Character.UnicodeBlock.DINGBATS || block == Character.UnicodeBlock.MISCELLANEOUS_MATHEMATICAL_SYMBOLS_A || block == Character.UnicodeBlock.SUPPLEMENTAL_ARROWS_A || block == Character.UnicodeBlock.BRAILLE_PATTERNS || block == Character.UnicodeBlock.SUPPLEMENTAL_ARROWS_B || block == Character.UnicodeBlock.MISCELLANEOUS_MATHEMATICAL_SYMBOLS_B || block == Character.UnicodeBlock.SUPPLEMENTAL_MATHEMATICAL_OPERATORS || block == Character.UnicodeBlock.MISCELLANEOUS_SYMBOLS_AND_ARROWS || block == Character.UnicodeBlock.PRIVATE_USE_AREA;
    }

    public static String addDirectionalMarkers(CharSequence text) {
        StringBuilder builder = new StringBuilder();
        char prev = 0;
        boolean isFirst = true;
        for (char c : text.toString().toCharArray()) {
            if (prev == '#') {
                if (isRtlCharacter(c)) {
                    builder.append("\u200f");
                } else {
                    builder.append("\u200e");
                }
            }
            if (isFirst) {
                isFirst = false;
            } else {
                builder.append(prev);
            }
            prev = c;
        }
        builder.append(prev);
        return builder.toString();
    }

    public static void restartApp(Context context) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        AlarmManager am = (AlarmManager) context.getSystemService("alarm");
        am.set(1, System.currentTimeMillis() + 1000, PendingIntent.getActivity(context, 0, launchIntent, 0));
        System.exit(0);
    }

    public static String getLocale() {
        Locale current = Locale.getDefault();
        return ((Locale.TRADITIONAL_CHINESE.getLanguage().equals(current.getLanguage()) && Locale.TRADITIONAL_CHINESE.getCountry().equals(current.getCountry())) || (Locale.SIMPLIFIED_CHINESE.getLanguage().equals(current.getLanguage()) && Locale.SIMPLIFIED_CHINESE.getCountry().equals(current.getCountry()))) ? current.toString() : current.getLanguage();
    }

    public static boolean isNullOrWhitespace(String input) {
        if (input != null) {
            int length = input.length();
            for (int i = 0; i < length; i++) {
                if (!Character.isWhitespace(input.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String getCountryCode() {
        Locale locale = Locale.getDefault();
        return locale.getCountry();
    }

    public static int getProfileColor(Context context) {
        return getDefaultSharedPrefs(context).getInt("profile_background", Settings.DEFAULT_PROFILE_COLOR);
    }

    public static String getPackageVersionName(Context context) {
        try {
            if (sPackageVersionName == null) {
                sPackageVersionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            }
            return sPackageVersionName;
        } catch (Throwable e) {
            CrashUtil.logException(e);
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }
}
