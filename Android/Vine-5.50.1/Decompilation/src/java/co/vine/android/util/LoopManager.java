package co.vine.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import co.vine.android.client.AppController;
import com.edisonwang.android.slog.SLog;
import com.edisonwang.android.slog.SLogger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/* loaded from: classes.dex */
public class LoopManager {
    private static LoopManager sInstance;
    private final Context mContext;
    private final String mLoopPreferenceName;
    private final SharedPreferences mPrefs;
    private LinkedList<Record> mRecords = new LinkedList<>();
    private long mViewingId;
    private long mViewingIdSetTime;
    private static final SLogger LOGGER = ConsoleLoggers.LOOP_REPORTING.get();
    private static final int[] LOCK = new int[0];
    private static final LongSparseArray<Integer> sCounts = new LongSparseArray<>();

    public static class Record implements Parcelable {
        public static final Parcelable.Creator<Record> CREATOR = new Parcelable.Creator<Record>() { // from class: co.vine.android.util.LoopManager.Record.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Record createFromParcel(Parcel in) {
                return new Record(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Record[] newArray(int size) {
                return new Record[size];
            }
        };
        public int loopCount;
        public long postId;
        public long timeStamp;
        public long userId;

        public Record(long userId, long postId) {
            this.userId = userId;
            this.postId = postId;
            this.loopCount = 0;
        }

        private Record() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void increment() {
            this.loopCount++;
            this.timeStamp = System.currentTimeMillis();
            int currentCount = ((Integer) LoopManager.sCounts.get(this.postId, -1)).intValue();
            if (currentCount == -1) {
                LoopManager.sCounts.put(this.postId, 1);
            } else {
                LoopManager.sCounts.put(this.postId, Integer.valueOf(currentCount + 1));
            }
        }

        public String toSerializedString() {
            return String.valueOf(this.userId) + ':' + this.postId + ':' + this.loopCount + ':' + this.timeStamp;
        }

        public static Record fromSerializedString(String s) {
            String[] spl = s.split(":");
            if (spl.length != 4) {
                return null;
            }
            Record r = new Record();
            r.userId = Long.parseLong(spl[0]);
            r.postId = Long.parseLong(spl[1]);
            r.loopCount = Integer.parseInt(spl[2]);
            r.timeStamp = Long.parseLong(spl[3]);
            return r;
        }

        public String toString() {
            return toSerializedString();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeLong(this.userId);
            parcel.writeLong(this.postId);
            parcel.writeInt(this.loopCount);
            parcel.writeLong(this.timeStamp);
        }

        private Record(Parcel in) {
            this.userId = in.readLong();
            this.postId = in.readLong();
            this.loopCount = in.readInt();
            this.timeStamp = in.readLong();
        }
    }

    private LoopManager(Context context) {
        this.mPrefs = Util.getDefaultSharedPrefs(context);
        this.mContext = context;
        this.mLoopPreferenceName = SystemUtil.isRunningOnServiceProcess(context) ? "pref_loop_count_records" : "pref_loop_count_records" + SystemUtil.getSubProcessName(context);
        reloadFromPreference();
    }

    public static LoopManager get(Context context) {
        synchronized (LOCK) {
            if (sInstance == null) {
                sInstance = new LoopManager(context);
            }
        }
        return sInstance;
    }

    public static int getLocalLoopCount(long postId) {
        return sCounts.get(postId, 0).intValue();
    }

    public void increment(long postId) {
        Record record;
        synchronized (LOCK) {
            resetViewingIdIfNeeded();
            if (this.mViewingId != postId) {
                record = new Record(AppController.getInstance(this.mContext).getActiveId(), postId);
                addRecord(record);
            } else {
                record = this.mRecords.getLast();
            }
            this.mViewingId = postId;
            this.mViewingIdSetTime = SystemClock.elapsedRealtime();
            record.increment();
            if (SLog.sLogsOn) {
                LOGGER.i(" " + postId + "  --- " + record.loopCount);
            }
        }
    }

    private void resetViewingIdIfNeeded() {
        if (SystemClock.elapsedRealtime() - this.mViewingIdSetTime > 120000) {
            this.mViewingId = 0L;
            this.mViewingIdSetTime = 0L;
        }
    }

    public void addRecord(Record record) {
        synchronized (LOCK) {
            if (SLog.sLogsOn) {
                LOGGER.i("Record added to mRecords. {}", record);
            }
            this.mRecords.add(record);
            if (record.loopCount > 0) {
                int currentCount = sCounts.get(record.postId, -1).intValue();
                if (currentCount == -1) {
                    sCounts.put(record.postId, Integer.valueOf(record.loopCount));
                } else {
                    sCounts.put(record.postId, Integer.valueOf(record.loopCount + currentCount));
                }
            }
        }
    }

    public void save() {
        Set<String> recordsToSave = new HashSet<>();
        synchronized (LOCK) {
            if (SLog.sLogsOn) {
                LOGGER.d("Loop saving started.");
            }
            Iterator<Record> it = this.mRecords.iterator();
            while (it.hasNext()) {
                Record r = it.next();
                recordsToSave.add(r.toSerializedString());
                if (SLog.sLogsOn) {
                    LOGGER.d("Saved " + r.postId + " --- " + r.loopCount);
                }
            }
            if (SLog.sLogsOn) {
                LOGGER.d("Loop saving complete.");
            }
        }
        this.mPrefs.edit().putStringSet(this.mLoopPreferenceName, recordsToSave).apply();
    }

    public List<Record> popPendingLoops() {
        LinkedList<Record> pendingLoops;
        LinkedList<Record> substitute = new LinkedList<>();
        synchronized (LOCK) {
            pendingLoops = this.mRecords;
            this.mRecords = substitute;
            resetViewingIdIfNeeded();
            if (!pendingLoops.isEmpty() && this.mViewingId == pendingLoops.getLast().postId) {
                this.mRecords.add(pendingLoops.removeLast());
            }
        }
        filterNonActiveUserLoops(pendingLoops, this.mRecords);
        return pendingLoops;
    }

    private void filterNonActiveUserLoops(List<Record> listToCheck, List<Record> listToSaveRemoved) {
        long userId = AppController.getInstance(this.mContext).getActiveId();
        ListIterator<Record> it = listToCheck.listIterator();
        while (it.hasNext()) {
            Record r = it.next();
            if (r.userId != userId) {
                if (listToSaveRemoved != null) {
                    listToSaveRemoved.add(r);
                }
                it.remove();
                if (SLog.sLogsOn) {
                    LOGGER.w("Found loops that does not belong to this user happen. {} {}", Long.valueOf(userId), Long.valueOf(r.userId));
                }
            }
        }
    }

    public void onServerSuccess() {
        synchronized (LOCK) {
            save();
        }
    }

    public void onServerFailure(List<Record> poppedRecords) {
        synchronized (LOCK) {
            if (SLog.sLogsOn) {
                LOGGER.i("Adding back failed loops back to LM.");
            }
            this.mRecords.addAll(0, poppedRecords);
            save();
        }
    }

    public String toString() {
        String string;
        synchronized (LOCK) {
            StringBuilder sb = new StringBuilder();
            Iterator<Record> it = this.mRecords.iterator();
            while (it.hasNext()) {
                Record r = it.next();
                sb.append(r.toString()).append('\n');
            }
            string = sb.toString();
        }
        return string;
    }

    public void reloadFromPreference() {
        synchronized (LOCK) {
            this.mViewingId = 0L;
            this.mViewingIdSetTime = 0L;
            this.mRecords.clear();
            Set<String> savedLoops = this.mPrefs.getStringSet(this.mLoopPreferenceName, null);
            if (savedLoops != null) {
                for (String s : savedLoops) {
                    addRecord(Record.fromSerializedString(s));
                }
            }
        }
    }
}
