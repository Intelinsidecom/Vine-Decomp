package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@zzha
/* loaded from: classes.dex */
public class zzjd {

    public interface zza<D, R> {
        R zze(D d);
    }

    public static <A, B> zzje<B> zza(final zzje<A> zzjeVar, final zza<A, B> zzaVar) {
        final zzjb zzjbVar = new zzjb();
        zzjeVar.zzb(new Runnable() { // from class: com.google.android.gms.internal.zzjd.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    zzjbVar.zzf(zzaVar.zze(zzjeVar.get()));
                } catch (InterruptedException | CancellationException | ExecutionException e) {
                    zzjbVar.cancel(true);
                }
            }
        });
        return zzjbVar;
    }

    public static <V> zzje<List<V>> zzj(final List<zzje<V>> list) {
        final zzjb zzjbVar = new zzjb();
        final int size = list.size();
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        Iterator<zzje<V>> it = list.iterator();
        while (it.hasNext()) {
            it.next().zzb(new Runnable() { // from class: com.google.android.gms.internal.zzjd.2
                @Override // java.lang.Runnable
                public void run() {
                    if (atomicInteger.incrementAndGet() >= size) {
                        try {
                            zzjbVar.zzf(zzjd.zzk(list));
                        } catch (InterruptedException | ExecutionException e) {
                            com.google.android.gms.ads.internal.util.client.zzb.zzd("Unable to convert list of futures to a future of list", e);
                        }
                    }
                }
            });
        }
        return zzjbVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <V> List<V> zzk(List<zzje<V>> list) throws ExecutionException, InterruptedException {
        ArrayList arrayList = new ArrayList();
        Iterator<zzje<V>> it = list.iterator();
        while (it.hasNext()) {
            V v = it.next().get();
            if (v != null) {
                arrayList.add(v);
            }
        }
        return arrayList;
    }
}
