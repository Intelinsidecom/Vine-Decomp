package com.google.android.gms.common.data;

import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes2.dex */
public final class DataHolder implements SafeParcelable {
    public static final zze CREATOR = new zze();
    private static final zza zzahI = new zza(new String[0], null) { // from class: com.google.android.gms.common.data.DataHolder.1
    };
    private final int mVersionCode;
    private final int zzabx;
    private final String[] zzahA;
    Bundle zzahB;
    private final CursorWindow[] zzahC;
    private final Bundle zzahD;
    int[] zzahE;
    int zzahF;
    private Object zzahG;
    boolean mClosed = false;
    private boolean zzahH = true;

    public static class zza {
        private final String[] zzahA;
        private final ArrayList<HashMap<String, Object>> zzahJ;
        private final String zzahK;
        private final HashMap<Object, Integer> zzahL;
        private boolean zzahM;
        private String zzahN;

        private zza(String[] strArr, String str) {
            this.zzahA = (String[]) zzx.zzy(strArr);
            this.zzahJ = new ArrayList<>();
            this.zzahK = str;
            this.zzahL = new HashMap<>();
            this.zzahM = false;
            this.zzahN = null;
        }
    }

    DataHolder(int versionCode, String[] columns, CursorWindow[] windows, int statusCode, Bundle metadata) {
        this.mVersionCode = versionCode;
        this.zzahA = columns;
        this.zzahC = windows;
        this.zzabx = statusCode;
        this.zzahD = metadata;
    }

    public void close() {
        synchronized (this) {
            if (!this.mClosed) {
                this.mClosed = true;
                for (int i = 0; i < this.zzahC.length; i++) {
                    this.zzahC[i].close();
                }
            }
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    protected void finalize() throws Throwable {
        try {
            if (this.zzahH && this.zzahC.length > 0 && !isClosed()) {
                Log.e("DataBuffer", "Internal data leak within a DataBuffer object detected!  Be sure to explicitly call release() on all DataBuffer extending objects when you are done with them. (" + (this.zzahG == null ? "internal object: " + toString() : this.zzahG.toString()) + ")");
                close();
            }
        } finally {
            super.finalize();
        }
    }

    public int getStatusCode() {
        return this.zzabx;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public boolean isClosed() {
        boolean z;
        synchronized (this) {
            z = this.mClosed;
        }
        return z;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        zze.zza(this, dest, flags);
    }

    public Bundle zzpH() {
        return this.zzahD;
    }

    public void zzpL() {
        this.zzahB = new Bundle();
        for (int i = 0; i < this.zzahA.length; i++) {
            this.zzahB.putInt(this.zzahA[i], i);
        }
        this.zzahE = new int[this.zzahC.length];
        int numRows = 0;
        for (int i2 = 0; i2 < this.zzahC.length; i2++) {
            this.zzahE[i2] = numRows;
            numRows += this.zzahC[i2].getNumRows() - (numRows - this.zzahC[i2].getStartPosition());
        }
        this.zzahF = numRows;
    }

    String[] zzpM() {
        return this.zzahA;
    }

    CursorWindow[] zzpN() {
        return this.zzahC;
    }
}
