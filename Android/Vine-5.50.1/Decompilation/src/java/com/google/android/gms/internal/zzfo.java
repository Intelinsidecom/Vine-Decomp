package com.google.android.gms.internal;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.URLUtil;
import com.google.android.gms.R;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public class zzfo extends zzfr {
    private final Context mContext;
    private final Map<String, String> zzxc;

    public zzfo(zzjn zzjnVar, Map<String, String> map) {
        super(zzjnVar, "storePicture");
        this.zzxc = map;
        this.mContext = zzjnVar.zzhx();
    }

    public void execute() {
        if (this.mContext == null) {
            zzal("Activity context is not available");
            return;
        }
        if (!com.google.android.gms.ads.internal.zzp.zzbx().zzN(this.mContext).zzdf()) {
            zzal("Feature is not supported by the device.");
            return;
        }
        final String str = this.zzxc.get("iurl");
        if (TextUtils.isEmpty(str)) {
            zzal("Image url cannot be empty.");
            return;
        }
        if (!URLUtil.isValidUrl(str)) {
            zzal("Invalid image url: " + str);
            return;
        }
        final String strZzak = zzak(str);
        if (!com.google.android.gms.ads.internal.zzp.zzbx().zzaB(strZzak)) {
            zzal("Image type not recognized: " + strZzak);
            return;
        }
        AlertDialog.Builder builderZzM = com.google.android.gms.ads.internal.zzp.zzbx().zzM(this.mContext);
        builderZzM.setTitle(com.google.android.gms.ads.internal.zzp.zzbA().zzf(R.string.store_picture_title, "Save image"));
        builderZzM.setMessage(com.google.android.gms.ads.internal.zzp.zzbA().zzf(R.string.store_picture_message, "Allow Ad to store image in Picture gallery?"));
        builderZzM.setPositiveButton(com.google.android.gms.ads.internal.zzp.zzbA().zzf(R.string.accept, "Accept"), new DialogInterface.OnClickListener() { // from class: com.google.android.gms.internal.zzfo.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                try {
                    ((DownloadManager) zzfo.this.mContext.getSystemService("download")).enqueue(zzfo.this.zzf(str, strZzak));
                } catch (IllegalStateException e) {
                    zzfo.this.zzal("Could not store picture.");
                }
            }
        });
        builderZzM.setNegativeButton(com.google.android.gms.ads.internal.zzp.zzbA().zzf(R.string.decline, "Decline"), new DialogInterface.OnClickListener() { // from class: com.google.android.gms.internal.zzfo.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                zzfo.this.zzal("User canceled the download.");
            }
        });
        builderZzM.create().show();
    }

    String zzak(String str) {
        return Uri.parse(str).getLastPathSegment();
    }

    DownloadManager.Request zzf(String str, String str2) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, str2);
        com.google.android.gms.ads.internal.zzp.zzbz().zza(request);
        return request;
    }
}
