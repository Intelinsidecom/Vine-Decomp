package com.twitter.android.sdk;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

/* loaded from: classes.dex */
public class Twitter {
    private static final ComponentName AUTH_ACTIVITY = new ComponentName("com.twitter.android", "com.twitter.android.AuthorizeAppActivity");
    private String mConsumerKey;
    private String mConsumerSecret;

    public Twitter(String consumerKey, String consumerSecret) {
        if (consumerKey == null || consumerSecret == null) {
            throw new IllegalArgumentException("consumerKey, consumerSecret, and getTokenListener must be non-null.");
        }
        this.mConsumerKey = consumerKey;
        this.mConsumerSecret = consumerSecret;
    }

    public boolean startAuthActivityForResult(Activity activity, int requestCode) {
        PackageManager pm = activity.getPackageManager();
        if (!checkAppSignature(pm)) {
            return false;
        }
        Intent intent = new Intent().setComponent(AUTH_ACTIVITY);
        ComponentName cn = intent.resolveActivity(pm);
        if (!cn.equals(AUTH_ACTIVITY)) {
            return false;
        }
        intent.putExtra("ck", this.mConsumerKey).putExtra("cs", this.mConsumerSecret);
        try {
            activity.startActivityForResult(intent, requestCode);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    private static boolean checkAppSignature(PackageManager pm) throws PackageManager.NameNotFoundException {
        try {
            PackageInfo p = pm.getPackageInfo("com.twitter.android", 64);
            for (Signature s : p.signatures) {
                if ("3082025d308201c6a00302010202044bd76cce300d06092a864886f70d01010505003073310b3009060355040613025553310b3009060355040813024341311630140603550407130d53616e204672616e636973636f31163014060355040a130d547769747465722c20496e632e310f300d060355040b13064d6f62696c65311630140603550403130d4c656c616e6420526563686973301e170d3130303432373233303133345a170d3438303832353233303133345a3073310b3009060355040613025553310b3009060355040813024341311630140603550407130d53616e204672616e636973636f31163014060355040a130d547769747465722c20496e632e310f300d060355040b13064d6f62696c65311630140603550403130d4c656c616e642052656368697330819f300d06092a864886f70d010101050003818d003081890281810086233c2e51c62232d49cc932e470713d63a6a1106b38f9e442e01bc79ca4f95c72b2cb3f1369ef7dea6036bff7c4b2828cb3787e7657ad83986751ced5b131fcc6f413efb7334e32ed9787f9e9a249ae108fa66009ac7a7932c25d37e1e07d4f9f66aa494c270dbac87d261c9668d321c2fba4ef2800e46671a597ff2eac5d7f0203010001300d06092a864886f70d0101050500038181003e1f01cb6ea8be8d2cecef5cd2a64c97ba8728aa5f08f8275d00508d64d139b6a72c5716b40a040df0eeeda04de9361107e123ee8d3dc05e70c8a355f46dbadf1235443b0b214c57211afd4edd147451c443d49498d2a7ff27e45a99c39b9e47429a1dae843ba233bf8ca81296dbe1dc5c5434514d995b0279246809392a219b".equals(s.toCharsString())) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
