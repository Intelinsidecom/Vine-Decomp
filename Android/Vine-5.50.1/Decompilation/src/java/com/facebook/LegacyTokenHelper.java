package com.facebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
final class LegacyTokenHelper {
    private static final String TAG = LegacyTokenHelper.class.getSimpleName();
    private SharedPreferences cache;
    private String cacheKey;

    public LegacyTokenHelper(Context context) {
        this(context, null);
    }

    public LegacyTokenHelper(Context context, String cacheKey) {
        Validate.notNull(context, "context");
        this.cacheKey = Utility.isNullOrEmpty(cacheKey) ? "com.facebook.SharedPreferencesTokenCachingStrategy.DEFAULT_KEY" : cacheKey;
        Context applicationContext = context.getApplicationContext();
        this.cache = (applicationContext != null ? applicationContext : context).getSharedPreferences(this.cacheKey, 0);
    }

    public Bundle load() {
        Bundle settings = new Bundle();
        Map<String, ?> allCachedEntries = this.cache.getAll();
        for (String key : allCachedEntries.keySet()) {
            try {
                deserializeKey(key, settings);
            } catch (JSONException e) {
                Logger.log(LoggingBehavior.CACHE, 5, TAG, "Error reading cached value for key: '" + key + "' -- " + e);
                return null;
            }
        }
        return settings;
    }

    public void clear() {
        this.cache.edit().clear().apply();
    }

    public static boolean hasTokenInformation(Bundle bundle) {
        String token;
        if (bundle == null || (token = bundle.getString("com.facebook.TokenCachingStrategy.Token")) == null || token.length() == 0) {
            return false;
        }
        long expiresMilliseconds = bundle.getLong("com.facebook.TokenCachingStrategy.ExpirationDate", 0L);
        return expiresMilliseconds != 0;
    }

    public static String getToken(Bundle bundle) {
        Validate.notNull(bundle, "bundle");
        return bundle.getString("com.facebook.TokenCachingStrategy.Token");
    }

    public static AccessTokenSource getSource(Bundle bundle) {
        Validate.notNull(bundle, "bundle");
        if (bundle.containsKey("com.facebook.TokenCachingStrategy.AccessTokenSource")) {
            return (AccessTokenSource) bundle.getSerializable("com.facebook.TokenCachingStrategy.AccessTokenSource");
        }
        boolean isSSO = bundle.getBoolean("com.facebook.TokenCachingStrategy.IsSSO");
        return isSSO ? AccessTokenSource.FACEBOOK_APPLICATION_WEB : AccessTokenSource.WEB_VIEW;
    }

    public static String getApplicationId(Bundle bundle) {
        Validate.notNull(bundle, "bundle");
        return bundle.getString("com.facebook.TokenCachingStrategy.ApplicationId");
    }

    static Date getDate(Bundle bundle, String key) {
        if (bundle == null) {
            return null;
        }
        long n = bundle.getLong(key, Long.MIN_VALUE);
        if (n != Long.MIN_VALUE) {
            return new Date(n);
        }
        return null;
    }

    private void deserializeKey(String key, Bundle bundle) throws JSONException {
        String jsonString = this.cache.getString(key, "{}");
        JSONObject json = new JSONObject(jsonString);
        String valueType = json.getString("valueType");
        if (valueType.equals("bool")) {
            bundle.putBoolean(key, json.getBoolean("value"));
            return;
        }
        if (valueType.equals("bool[]")) {
            JSONArray jsonArray = json.getJSONArray("value");
            boolean[] array = new boolean[jsonArray.length()];
            for (int i = 0; i < array.length; i++) {
                array[i] = jsonArray.getBoolean(i);
            }
            bundle.putBooleanArray(key, array);
            return;
        }
        if (valueType.equals("byte")) {
            bundle.putByte(key, (byte) json.getInt("value"));
            return;
        }
        if (valueType.equals("byte[]")) {
            JSONArray jsonArray2 = json.getJSONArray("value");
            byte[] array2 = new byte[jsonArray2.length()];
            for (int i2 = 0; i2 < array2.length; i2++) {
                array2[i2] = (byte) jsonArray2.getInt(i2);
            }
            bundle.putByteArray(key, array2);
            return;
        }
        if (valueType.equals("short")) {
            bundle.putShort(key, (short) json.getInt("value"));
            return;
        }
        if (valueType.equals("short[]")) {
            JSONArray jsonArray3 = json.getJSONArray("value");
            short[] array3 = new short[jsonArray3.length()];
            for (int i3 = 0; i3 < array3.length; i3++) {
                array3[i3] = (short) jsonArray3.getInt(i3);
            }
            bundle.putShortArray(key, array3);
            return;
        }
        if (valueType.equals("int")) {
            bundle.putInt(key, json.getInt("value"));
            return;
        }
        if (valueType.equals("int[]")) {
            JSONArray jsonArray4 = json.getJSONArray("value");
            int[] array4 = new int[jsonArray4.length()];
            for (int i4 = 0; i4 < array4.length; i4++) {
                array4[i4] = jsonArray4.getInt(i4);
            }
            bundle.putIntArray(key, array4);
            return;
        }
        if (valueType.equals("long")) {
            bundle.putLong(key, json.getLong("value"));
            return;
        }
        if (valueType.equals("long[]")) {
            JSONArray jsonArray5 = json.getJSONArray("value");
            long[] array5 = new long[jsonArray5.length()];
            for (int i5 = 0; i5 < array5.length; i5++) {
                array5[i5] = jsonArray5.getLong(i5);
            }
            bundle.putLongArray(key, array5);
            return;
        }
        if (valueType.equals("float")) {
            bundle.putFloat(key, (float) json.getDouble("value"));
            return;
        }
        if (valueType.equals("float[]")) {
            JSONArray jsonArray6 = json.getJSONArray("value");
            float[] array6 = new float[jsonArray6.length()];
            for (int i6 = 0; i6 < array6.length; i6++) {
                array6[i6] = (float) jsonArray6.getDouble(i6);
            }
            bundle.putFloatArray(key, array6);
            return;
        }
        if (valueType.equals("double")) {
            bundle.putDouble(key, json.getDouble("value"));
            return;
        }
        if (valueType.equals("double[]")) {
            JSONArray jsonArray7 = json.getJSONArray("value");
            double[] array7 = new double[jsonArray7.length()];
            for (int i7 = 0; i7 < array7.length; i7++) {
                array7[i7] = jsonArray7.getDouble(i7);
            }
            bundle.putDoubleArray(key, array7);
            return;
        }
        if (valueType.equals("char")) {
            String charString = json.getString("value");
            if (charString != null && charString.length() == 1) {
                bundle.putChar(key, charString.charAt(0));
                return;
            }
            return;
        }
        if (valueType.equals("char[]")) {
            JSONArray jsonArray8 = json.getJSONArray("value");
            char[] array8 = new char[jsonArray8.length()];
            for (int i8 = 0; i8 < array8.length; i8++) {
                String charString2 = jsonArray8.getString(i8);
                if (charString2 != null && charString2.length() == 1) {
                    array8[i8] = charString2.charAt(0);
                }
            }
            bundle.putCharArray(key, array8);
            return;
        }
        if (valueType.equals("string")) {
            bundle.putString(key, json.getString("value"));
            return;
        }
        if (valueType.equals("stringList")) {
            JSONArray jsonArray9 = json.getJSONArray("value");
            int numStrings = jsonArray9.length();
            ArrayList<String> stringList = new ArrayList<>(numStrings);
            for (int i9 = 0; i9 < numStrings; i9++) {
                Object jsonStringValue = jsonArray9.get(i9);
                stringList.add(i9, jsonStringValue == JSONObject.NULL ? null : (String) jsonStringValue);
            }
            bundle.putStringArrayList(key, stringList);
            return;
        }
        if (valueType.equals("enum")) {
            try {
                String enumType = json.getString("enumType");
                Enum<?> enumValue = Enum.valueOf(Class.forName(enumType), json.getString("value"));
                bundle.putSerializable(key, enumValue);
            } catch (ClassNotFoundException e) {
            } catch (IllegalArgumentException e2) {
            }
        }
    }
}
