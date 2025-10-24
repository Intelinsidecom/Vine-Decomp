package com.google.android.gms.maps;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.dynamic.zzf;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.IMapViewDelegate;
import com.google.android.gms.maps.internal.MapLifecycleDelegate;
import com.google.android.gms.maps.internal.zzl;
import com.google.android.gms.maps.internal.zzy;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class MapView extends FrameLayout {
    private GoogleMap zzaPf;
    private final zzb zzaPl;

    static class zza implements MapLifecycleDelegate {
        private final ViewGroup zzaPm;
        private final IMapViewDelegate zzaPn;
        private View zzaPo;

        public zza(ViewGroup viewGroup, IMapViewDelegate iMapViewDelegate) {
            this.zzaPn = (IMapViewDelegate) zzx.zzy(iMapViewDelegate);
            this.zzaPm = (ViewGroup) zzx.zzy(viewGroup);
        }

        public void getMapAsync(final OnMapReadyCallback callback) {
            try {
                this.zzaPn.getMapAsync(new zzl.zza() { // from class: com.google.android.gms.maps.MapView.zza.1
                    @Override // com.google.android.gms.maps.internal.zzl
                    public void zza(IGoogleMapDelegate iGoogleMapDelegate) throws RemoteException {
                        callback.onMapReady(new GoogleMap(iGoogleMapDelegate));
                    }
                });
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onCreate(Bundle savedInstanceState) {
            try {
                this.zzaPn.onCreate(savedInstanceState);
                this.zzaPo = (View) zze.zzp(this.zzaPn.getView());
                this.zzaPm.removeAllViews();
                this.zzaPm.addView(this.zzaPo);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            throw new UnsupportedOperationException("onCreateView not allowed on MapViewDelegate");
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onDestroy() {
            try {
                this.zzaPn.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onDestroyView() {
            throw new UnsupportedOperationException("onDestroyView not allowed on MapViewDelegate");
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onInflate(Activity activity, Bundle attrs, Bundle savedInstanceState) {
            throw new UnsupportedOperationException("onInflate not allowed on MapViewDelegate");
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onLowMemory() {
            try {
                this.zzaPn.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onPause() {
            try {
                this.zzaPn.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onResume() {
            try {
                this.zzaPn.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public void onSaveInstanceState(Bundle outState) {
            try {
                this.zzaPn.onSaveInstanceState(outState);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public IMapViewDelegate zzzi() {
            return this.zzaPn;
        }
    }

    static class zzb extends com.google.android.gms.dynamic.zza<zza> {
        private final Context mContext;
        protected zzf<zza> zzaPj;
        private final List<OnMapReadyCallback> zzaPk = new ArrayList();
        private final ViewGroup zzaPq;
        private final GoogleMapOptions zzaPr;

        zzb(ViewGroup viewGroup, Context context, GoogleMapOptions googleMapOptions) {
            this.zzaPq = viewGroup;
            this.mContext = context;
            this.zzaPr = googleMapOptions;
        }

        @Override // com.google.android.gms.dynamic.zza
        protected void zza(zzf<zza> zzfVar) {
            this.zzaPj = zzfVar;
            zzzh();
        }

        public void zzzh() {
            if (this.zzaPj == null || zzts() != null) {
                return;
            }
            try {
                MapsInitializer.initialize(this.mContext);
                IMapViewDelegate iMapViewDelegateZza = zzy.zzaP(this.mContext).zza(zze.zzB(this.mContext), this.zzaPr);
                if (iMapViewDelegateZza == null) {
                    return;
                }
                this.zzaPj.zza(new zza(this.zzaPq, iMapViewDelegateZza));
                Iterator<OnMapReadyCallback> it = this.zzaPk.iterator();
                while (it.hasNext()) {
                    zzts().getMapAsync(it.next());
                }
                this.zzaPk.clear();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            } catch (GooglePlayServicesNotAvailableException e2) {
            }
        }
    }

    public MapView(Context context) {
        super(context);
        this.zzaPl = new zzb(this, context, null);
        init();
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.zzaPl = new zzb(this, context, GoogleMapOptions.createFromAttributes(context, attrs));
        init();
    }

    public MapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.zzaPl = new zzb(this, context, GoogleMapOptions.createFromAttributes(context, attrs));
        init();
    }

    public MapView(Context context, GoogleMapOptions options) {
        super(context);
        this.zzaPl = new zzb(this, context, options);
        init();
    }

    private void init() {
        setClickable(true);
    }

    @Deprecated
    public final GoogleMap getMap() {
        if (this.zzaPf != null) {
            return this.zzaPf;
        }
        this.zzaPl.zzzh();
        if (this.zzaPl.zzts() == null) {
            return null;
        }
        try {
            this.zzaPf = new GoogleMap(this.zzaPl.zzts().zzzi().getMap());
            return this.zzaPf;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void onCreate(Bundle savedInstanceState) throws PackageManager.NameNotFoundException {
        this.zzaPl.onCreate(savedInstanceState);
        if (this.zzaPl.zzts() == null) {
            com.google.android.gms.dynamic.zza.zzb(this);
        }
    }

    public final void onDestroy() {
        this.zzaPl.onDestroy();
    }

    public final void onLowMemory() {
        this.zzaPl.onLowMemory();
    }

    public final void onPause() {
        this.zzaPl.onPause();
    }

    public final void onResume() {
        this.zzaPl.onResume();
    }

    public final void onSaveInstanceState(Bundle outState) {
        this.zzaPl.onSaveInstanceState(outState);
    }
}
