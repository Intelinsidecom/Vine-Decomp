package co.vine.android.util;

import android.telephony.SmsManager;
import com.googlecode.javacv.cpp.avcodec;

/* loaded from: classes.dex */
public class SMSUtil {
    public static boolean sendSMS(String dest, String message) {
        SmsManager defaultManager = SmsManager.getDefault();
        if (message.length() > 140) {
            message = message.substring(0, avcodec.AV_CODEC_ID_YOP);
            CrashUtil.log("Message is truncated.");
        }
        if (defaultManager == null) {
            return false;
        }
        try {
            defaultManager.sendTextMessage(dest, null, message, null, null);
        } catch (Exception e) {
            CrashUtil.logException(e, "Failed to send: {" + message + "}", new Object[0]);
        }
        return true;
    }
}
