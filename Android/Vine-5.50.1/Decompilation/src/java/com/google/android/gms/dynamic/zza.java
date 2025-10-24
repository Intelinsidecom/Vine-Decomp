package com.google.android.gms.dynamic;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.dynamic.LifecycleDelegate;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public abstract class zza<T extends LifecycleDelegate> {
    private T zzatr;
    private Bundle zzats;
    private LinkedList<InterfaceC0041zza> zzatt;
    private final zzf<T> zzatu = (zzf<T>) new zzf<T>() { // from class: com.google.android.gms.dynamic.zza.1
        @Override // com.google.android.gms.dynamic.zzf
        public void zza(T t) {
            zza.this.zzatr = t;
            Iterator it = zza.this.zzatt.iterator();
            while (it.hasNext()) {
                ((InterfaceC0041zza) it.next()).zzb(zza.this.zzatr);
            }
            zza.this.zzatt.clear();
            zza.this.zzats = null;
        }
    };

    /* renamed from: com.google.android.gms.dynamic.zza$zza, reason: collision with other inner class name */
    private interface InterfaceC0041zza {
        int getState();

        void zzb(LifecycleDelegate lifecycleDelegate);
    }

    private void zza(Bundle bundle, InterfaceC0041zza interfaceC0041zza) {
        if (this.zzatr != null) {
            interfaceC0041zza.zzb(this.zzatr);
            return;
        }
        if (this.zzatt == null) {
            this.zzatt = new LinkedList<>();
        }
        this.zzatt.add(interfaceC0041zza);
        if (bundle != null) {
            if (this.zzats == null) {
                this.zzats = (Bundle) bundle.clone();
            } else {
                this.zzats.putAll(bundle);
            }
        }
        zza(this.zzatu);
    }

    public static void zzb(FrameLayout frameLayout) throws PackageManager.NameNotFoundException {
        final Context context = frameLayout.getContext();
        final int iIsGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        String strZzc = com.google.android.gms.common.internal.zzg.zzc(context, iIsGooglePlayServicesAvailable, GooglePlayServicesUtil.zzam(context));
        String strZzh = com.google.android.gms.common.internal.zzg.zzh(context, iIsGooglePlayServicesAvailable);
        LinearLayout linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        frameLayout.addView(linearLayout);
        TextView textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        textView.setText(strZzc);
        linearLayout.addView(textView);
        if (strZzh != null) {
            Button button = new Button(context);
            button.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
            button.setText(strZzh);
            linearLayout.addView(button);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.gms.dynamic.zza.5
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    context.startActivity(GooglePlayServicesUtil.zzbv(iIsGooglePlayServicesAvailable));
                }
            });
        }
    }

    private void zzeF(int i) {
        while (!this.zzatt.isEmpty() && this.zzatt.getLast().getState() >= i) {
            this.zzatt.removeLast();
        }
    }

    public void onCreate(final Bundle savedInstanceState) {
        zza(savedInstanceState, new InterfaceC0041zza() { // from class: com.google.android.gms.dynamic.zza.3
            @Override // com.google.android.gms.dynamic.zza.InterfaceC0041zza
            public int getState() {
                return 1;
            }

            @Override // com.google.android.gms.dynamic.zza.InterfaceC0041zza
            public void zzb(LifecycleDelegate lifecycleDelegate) {
                zza.this.zzatr.onCreate(savedInstanceState);
            }
        });
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) throws PackageManager.NameNotFoundException {
        final FrameLayout frameLayout = new FrameLayout(inflater.getContext());
        zza(savedInstanceState, new InterfaceC0041zza() { // from class: com.google.android.gms.dynamic.zza.4
            @Override // com.google.android.gms.dynamic.zza.InterfaceC0041zza
            public int getState() {
                return 2;
            }

            @Override // com.google.android.gms.dynamic.zza.InterfaceC0041zza
            public void zzb(LifecycleDelegate lifecycleDelegate) {
                frameLayout.removeAllViews();
                frameLayout.addView(zza.this.zzatr.onCreateView(inflater, container, savedInstanceState));
            }
        });
        if (this.zzatr == null) {
            zza(frameLayout);
        }
        return frameLayout;
    }

    public void onDestroy() {
        if (this.zzatr != null) {
            this.zzatr.onDestroy();
        } else {
            zzeF(1);
        }
    }

    public void onDestroyView() {
        if (this.zzatr != null) {
            this.zzatr.onDestroyView();
        } else {
            zzeF(2);
        }
    }

    public void onInflate(final Activity activity, final Bundle attrs, final Bundle savedInstanceState) {
        zza(savedInstanceState, new InterfaceC0041zza() { // from class: com.google.android.gms.dynamic.zza.2
            @Override // com.google.android.gms.dynamic.zza.InterfaceC0041zza
            public int getState() {
                return 0;
            }

            @Override // com.google.android.gms.dynamic.zza.InterfaceC0041zza
            public void zzb(LifecycleDelegate lifecycleDelegate) {
                zza.this.zzatr.onInflate(activity, attrs, savedInstanceState);
            }
        });
    }

    public void onLowMemory() {
        if (this.zzatr != null) {
            this.zzatr.onLowMemory();
        }
    }

    public void onPause() {
        if (this.zzatr != null) {
            this.zzatr.onPause();
        } else {
            zzeF(5);
        }
    }

    public void onResume() {
        zza((Bundle) null, new InterfaceC0041zza() { // from class: com.google.android.gms.dynamic.zza.7
            @Override // com.google.android.gms.dynamic.zza.InterfaceC0041zza
            public int getState() {
                return 5;
            }

            @Override // com.google.android.gms.dynamic.zza.InterfaceC0041zza
            public void zzb(LifecycleDelegate lifecycleDelegate) {
                zza.this.zzatr.onResume();
            }
        });
    }

    public void onSaveInstanceState(Bundle outState) {
        if (this.zzatr != null) {
            this.zzatr.onSaveInstanceState(outState);
        } else if (this.zzats != null) {
            outState.putAll(this.zzats);
        }
    }

    protected void zza(FrameLayout frameLayout) throws PackageManager.NameNotFoundException {
        zzb(frameLayout);
    }

    protected abstract void zza(zzf<T> zzfVar);

    public T zzts() {
        return this.zzatr;
    }
}
