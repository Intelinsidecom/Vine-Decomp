package com.mobileapptracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.concurrent.Semaphore;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class MATEventQueue {
    private static long retryTimeout = 0;
    private SharedPreferences eventQueue;
    private Semaphore queueAvailable = new Semaphore(1, true);
    private MobileAppTracker tune;

    public MATEventQueue(Context context, MobileAppTracker tune) {
        this.eventQueue = context.getSharedPreferences("mat_queue", 0);
        this.tune = tune;
    }

    protected synchronized void setQueueSize(int size) {
        SharedPreferences.Editor editor = this.eventQueue.edit();
        if (size < 0) {
            size = 0;
        }
        editor.putInt("queuesize", size);
        editor.commit();
    }

    protected synchronized int getQueueSize() {
        return this.eventQueue.getInt("queuesize", 0);
    }

    protected synchronized void removeKeyFromQueue(String key) {
        setQueueSize(getQueueSize() - 1);
        SharedPreferences.Editor editor = this.eventQueue.edit();
        editor.remove(key);
        editor.commit();
    }

    protected synchronized String getKeyFromQueue(String key) {
        return this.eventQueue.getString(key, null);
    }

    protected synchronized void setQueueItemForKey(JSONObject item, String key) {
        SharedPreferences.Editor editor = this.eventQueue.edit();
        editor.putString(key, item.toString());
        editor.commit();
    }

    protected class Add implements Runnable {
        private String data;
        private boolean firstSession;
        private String link;
        private JSONObject postBody;

        protected Add(String link, String data, JSONObject postBody, boolean firstSession) {
            this.link = null;
            this.data = null;
            this.postBody = null;
            this.firstSession = false;
            this.link = link;
            this.data = data;
            this.postBody = postBody;
            this.firstSession = firstSession;
        }

        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:12:0x006a -> B:16:0x0049). Please report as a decompilation issue!!! */
        @Override // java.lang.Runnable
        public void run() {
            try {
                MATEventQueue.this.queueAvailable.acquire();
                JSONObject jsonEvent = new JSONObject();
                try {
                    jsonEvent.put("link", this.link);
                    jsonEvent.put("data", this.data);
                    jsonEvent.put("post_body", this.postBody);
                    jsonEvent.put("first_session", this.firstSession);
                    int count = MATEventQueue.this.getQueueSize() + 1;
                    MATEventQueue.this.setQueueSize(count);
                    String eventIndex = Integer.toString(count);
                    MATEventQueue.this.setQueueItemForKey(jsonEvent, eventIndex);
                } catch (JSONException e) {
                    Log.w("MobileAppTracker", "Failed creating event for queueing");
                    e.printStackTrace();
                    MATEventQueue.this.queueAvailable.release();
                }
            } catch (InterruptedException e2) {
                Log.w("MobileAppTracker", "Interrupted adding event to queue");
                e2.printStackTrace();
            } finally {
                MATEventQueue.this.queueAvailable.release();
            }
        }
    }

    protected class Dump implements Runnable {
        protected Dump() {
        }

        /* JADX WARN: Removed duplicated region for block: B:51:0x0171 A[Catch: InterruptedException -> 0x00f8, all -> 0x01a9, Merged into TryCatch #6 {all -> 0x01a9, InterruptedException -> 0x00f8, blocks: (B:5:0x000d, B:12:0x0039, B:15:0x0050, B:17:0x0077, B:18:0x0087, B:32:0x00f7, B:21:0x00a3, B:23:0x00af, B:25:0x00c3, B:26:0x00d3, B:37:0x010d, B:39:0x0119, B:42:0x0125, B:43:0x012b, B:47:0x0135, B:48:0x0150, B:59:0x01a5, B:49:0x0167, B:51:0x0171, B:52:0x0176, B:54:0x019c, B:64:0x01b8, B:66:0x01c2, B:67:0x01c8, B:69:0x01d2, B:70:0x01d8, B:72:0x01e2, B:73:0x01e8, B:75:0x01f2, B:76:0x01f9, B:77:0x0201, B:28:0x00d8, B:78:0x0219, B:34:0x00f9), top: B:94:0x000a }] */
        /* JADX WARN: Removed duplicated region for block: B:64:0x01b8 A[Catch: InterruptedException -> 0x00f8, all -> 0x01a9, Merged into TryCatch #6 {all -> 0x01a9, InterruptedException -> 0x00f8, blocks: (B:5:0x000d, B:12:0x0039, B:15:0x0050, B:17:0x0077, B:18:0x0087, B:32:0x00f7, B:21:0x00a3, B:23:0x00af, B:25:0x00c3, B:26:0x00d3, B:37:0x010d, B:39:0x0119, B:42:0x0125, B:43:0x012b, B:47:0x0135, B:48:0x0150, B:59:0x01a5, B:49:0x0167, B:51:0x0171, B:52:0x0176, B:54:0x019c, B:64:0x01b8, B:66:0x01c2, B:67:0x01c8, B:69:0x01d2, B:70:0x01d8, B:72:0x01e2, B:73:0x01e8, B:75:0x01f2, B:76:0x01f9, B:77:0x0201, B:28:0x00d8, B:78:0x0219, B:34:0x00f9), top: B:94:0x000a }, TRY_ENTER] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() {
            /*
                Method dump skipped, instructions count: 564
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mobileapptracker.MATEventQueue.Dump.run():void");
        }
    }
}
