package com.vandalsoftware.io;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public final class DiskLruCache implements Closeable {
    private final int appVersion;
    private final File directory;
    private final File journalFile;
    private final File journalFileTmp;
    private DataOutputStream journalStream;
    private final long maxSize;
    private int redundantOpCount;
    private final int valueCount;
    private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<>(0, 0.75f, true);
    private final ExecutorService executorService = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
    private final Callable<Void> cleanupCallable = new Callable<Void>() { // from class: com.vandalsoftware.io.DiskLruCache.1
        @Override // java.util.concurrent.Callable
        public Void call() throws Exception {
            synchronized (DiskLruCache.this) {
                if (DiskLruCache.this.journalStream != null) {
                    DiskLruCache.this.trimToSize();
                    if (DiskLruCache.this.journalRebuildRequired()) {
                        DiskLruCache.this.rebuildJournal();
                    }
                }
            }
            return null;
        }
    };
    private long size = 0;

    private DiskLruCache(File directory, int appVersion, int valueCount, long maxSize) {
        this.directory = directory;
        this.appVersion = appVersion;
        this.journalFile = new File(directory, "journal");
        this.journalFileTmp = new File(directory, "journal.tmp");
        this.valueCount = valueCount;
        this.maxSize = maxSize;
    }

    public static DiskLruCache open(File directory, int appVersion, int valueCount, long maxSize) throws IOException {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (valueCount <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        }
        DiskLruCache cache = new DiskLruCache(directory, appVersion, valueCount, maxSize);
        if (cache.journalFile.exists()) {
            try {
                int journalEntries = cache.readJournal();
                cache.processJournal();
                cache.journalStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(cache.journalFile, true)));
                cache.redundantOpCount = journalEntries - cache.lruEntries.size();
                return cache;
            } catch (IOException journalIsCorrupt) {
                Log.w("DiskLruCache", directory + " is corrupt: " + journalIsCorrupt.getMessage() + ", removing");
                cache.delete();
            }
        }
        if (directory.mkdirs() || directory.exists()) {
            DiskLruCache cache2 = new DiskLruCache(directory, appVersion, valueCount, maxSize);
            cache2.rebuildJournal();
            return cache2;
        }
        throw new FileNotFoundException("directory not found " + directory);
    }

    private static boolean deleteIfExists(File file) throws IOException {
        return file.exists() && file.delete();
    }

    private int readJournal() throws IOException {
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(this.journalFile)));
        try {
            long fileMagic = in.readLong();
            int fileVersion = in.readUnsignedByte();
            int fileAppVersion = in.readInt();
            int fileValueCount = in.readInt();
            byte blank = in.readByte();
            if (-9130401435085039094L != fileMagic || 2 != fileVersion || this.appVersion != fileAppVersion || this.valueCount != fileValueCount || blank != 10) {
                throw new IOException("unexpected journal header: [" + fileMagic + ", " + fileVersion + ", " + fileValueCount + ", " + ((int) blank) + "]");
            }
            int entries = 0;
            while (true) {
                try {
                    readJournalLine(in);
                    entries++;
                } catch (EOFException e) {
                    return entries;
                }
            }
        } finally {
            IoUtils.closeQuietly(in);
        }
    }

    private void readJournalLine(DataInput in) throws IOException {
        int op = in.readUnsignedByte();
        String key = in.readUTF();
        if (op == 3 && in.readByte() == 10) {
            this.lruEntries.remove(key);
            return;
        }
        Entry entry = this.lruEntries.get(key);
        if (entry == null) {
            entry = new Entry(key);
            this.lruEntries.put(key, entry);
        }
        if (op == 1) {
            long[] temp = new long[this.valueCount];
            for (int i = 0; i < this.valueCount; i++) {
                temp[i] = in.readLong();
            }
            if (in.readByte() != 10) {
                throw new IOException("unexpected journal entry: " + op + " " + key);
            }
            entry.readable = true;
            entry.currentEditor = null;
            System.arraycopy(temp, 0, entry.lengths, 0, this.valueCount);
            return;
        }
        if (op != 2 || in.readByte() != 10) {
            if (op != 4 || in.readByte() != 10) {
                throw new IOException("unexpected journal entry: " + op + " " + key);
            }
            return;
        }
        entry.currentEditor = new Editor(entry);
    }

    private void processJournal() throws IOException {
        deleteIfExists(this.journalFileTmp);
        Iterator<Entry> i = this.lruEntries.values().iterator();
        while (i.hasNext()) {
            Entry entry = i.next();
            if (entry.currentEditor == null) {
                for (int t = 0; t < this.valueCount; t++) {
                    this.size += entry.lengths[t];
                }
            } else {
                entry.currentEditor = null;
                for (int t2 = 0; t2 < this.valueCount; t2++) {
                    deleteIfExists(entry.getCleanFile(t2));
                    deleteIfExists(entry.getDirtyFile(t2));
                }
                i.remove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void rebuildJournal() throws IOException {
        if (this.journalStream != null) {
            this.journalStream.close();
        }
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(this.journalFileTmp)));
        try {
            out.writeLong(-9130401435085039094L);
            out.writeByte(2);
            out.writeInt(this.appVersion);
            out.writeInt(this.valueCount);
            out.writeByte(10);
            for (Entry entry : this.lruEntries.values()) {
                if (entry.currentEditor != null) {
                    writeEntryKey(out, 2, entry.key);
                } else {
                    writeCleanEntry(out, entry);
                }
            }
            out.close();
            IoUtils.renameFileOrThrow(this.journalFileTmp, this.journalFile);
            this.journalStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(this.journalFile, true)));
            this.redundantOpCount = 0;
        } catch (Throwable th) {
            out.close();
            throw th;
        }
    }

    private void writeEntryKey(DataOutput out, int s, String key) throws IOException {
        out.writeByte(s);
        out.writeUTF(key);
        out.writeByte(10);
    }

    private void writeCleanEntry(DataOutput out, Entry entry) throws IOException {
        out.writeByte(1);
        out.writeUTF(entry.key);
        for (long len : entry.lengths) {
            out.writeLong(len);
        }
        out.writeByte(10);
    }

    public synchronized Snapshot get(String key, boolean peek) throws IOException {
        Snapshot snapshot = null;
        synchronized (this) {
            checkNotClosed();
            validateKey(key);
            Entry entry = this.lruEntries.get(key);
            if (entry != null && entry.readable) {
                InputStream[] ins = new InputStream[this.valueCount];
                String[] paths = new String[this.valueCount];
                for (int i = 0; i < this.valueCount; i++) {
                    try {
                        File file = entry.getCleanFile(i);
                        ins[i] = new FileInputStream(file);
                        paths[i] = file.getAbsolutePath();
                    } catch (FileNotFoundException e) {
                    }
                }
                if (!peek) {
                    this.redundantOpCount++;
                    writeEntryKey(this.journalStream, 4, key);
                    if (journalRebuildRequired()) {
                        this.executorService.submit(this.cleanupCallable);
                    }
                }
                snapshot = new Snapshot(ins, paths);
            }
        }
        return snapshot;
    }

    public synchronized Snapshot get(String key) throws IOException {
        return get(key, false);
    }

    public synchronized Editor edit(String key) throws IOException {
        Editor editor = null;
        synchronized (this) {
            checkNotClosed();
            validateKey(key);
            Entry entry = this.lruEntries.get(key);
            if (entry == null) {
                entry = new Entry(key);
                this.lruEntries.put(key, entry);
            } else if (entry.currentEditor == null) {
            }
            editor = new Editor(entry);
            entry.currentEditor = editor;
            writeEntryKey(this.journalStream, 2, key);
            this.journalStream.flush();
        }
        return editor;
    }

    public synchronized long size() {
        return this.size;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void completeEdit(Editor editor, boolean success) throws IOException {
        Entry entry = editor.entry;
        if (entry.currentEditor != editor) {
            throw new IllegalStateException();
        }
        if (success && !entry.readable) {
            for (int i = 0; i < this.valueCount; i++) {
                if (!entry.getDirtyFile(i).exists()) {
                    editor.abort();
                    throw new IllegalStateException("edit didn't create file " + i);
                }
            }
        }
        for (int i2 = 0; i2 < this.valueCount; i2++) {
            File dirty = entry.getDirtyFile(i2);
            if (success) {
                if (dirty.exists()) {
                    File clean = entry.getCleanFile(i2);
                    IoUtils.renameFileOrThrow(dirty, clean);
                    long oldLength = entry.lengths[i2];
                    long newLength = clean.length();
                    entry.lengths[i2] = newLength;
                    this.size = (this.size - oldLength) + newLength;
                }
            } else {
                deleteIfExists(dirty);
            }
        }
        this.redundantOpCount++;
        entry.currentEditor = null;
        if (entry.readable | success) {
            entry.readable = true;
            writeCleanEntry(this.journalStream, entry);
        } else {
            this.lruEntries.remove(entry.key);
            writeEntryKey(this.journalStream, 3, entry.key);
        }
        if (this.size > this.maxSize || journalRebuildRequired()) {
            this.executorService.submit(this.cleanupCallable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }

    public synchronized boolean remove(String key) throws IOException {
        boolean z;
        checkNotClosed();
        validateKey(key);
        Entry entry = this.lruEntries.get(key);
        if (entry == null || entry.currentEditor != null) {
            z = false;
        } else {
            for (int i = 0; i < this.valueCount; i++) {
                File file = entry.getCleanFile(i);
                IoUtils.deleteFileOrThrow(file);
                this.size -= entry.lengths[i];
                entry.lengths[i] = 0;
            }
            this.redundantOpCount++;
            writeEntryKey(this.journalStream, 3, key);
            this.lruEntries.remove(key);
            if (journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
            z = true;
        }
        return z;
    }

    public boolean isClosed() {
        return this.journalStream == null;
    }

    private void checkNotClosed() {
        if (this.journalStream == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    public synchronized void flush() throws IOException {
        checkNotClosed();
        trimToSize();
        this.journalStream.flush();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (this.journalStream != null) {
            Iterator it = new ArrayList(this.lruEntries.values()).iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                if (entry.currentEditor != null) {
                    entry.currentEditor.abort();
                }
            }
            trimToSize();
            this.journalStream.close();
            this.journalStream = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            Map.Entry<String, Entry> toEvict = this.lruEntries.entrySet().iterator().next();
            remove(toEvict.getKey());
        }
    }

    public void delete() throws IOException {
        close();
        if (this.directory.isDirectory()) {
            IoUtils.deleteContents(this.directory);
        }
    }

    private void validateKey(String key) {
        if (key.contains(" ") || key.contains("\n") || key.contains("\r")) {
            throw new IllegalArgumentException("keys must not contain spaces or newlines: \"" + key + "\"");
        }
    }

    public static final class Snapshot implements Closeable {
        private final InputStream[] ins;
        private final String[] paths;

        private Snapshot(InputStream[] ins, String[] paths) {
            this.ins = ins;
            this.paths = paths;
        }

        public InputStream getInputStream(int index) {
            return this.ins[index];
        }

        public String getPath(int index) {
            return this.paths[index];
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            for (InputStream in : this.ins) {
                IoUtils.closeQuietly(in);
            }
        }
    }

    public final class Editor {
        private final Entry entry;
        private boolean hasErrors;

        private Editor(Entry entry) {
            this.entry = entry;
        }

        public OutputStream newOutputStream(int index) throws IOException {
            ErrorCatchingOutputStream errorCatchingOutputStream;
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                errorCatchingOutputStream = new ErrorCatchingOutputStream(new FileOutputStream(this.entry.getDirtyFile(index)));
            }
            return errorCatchingOutputStream;
        }

        public void commit() throws IOException {
            if (this.hasErrors) {
                DiskLruCache.this.completeEdit(this, false);
                DiskLruCache.this.remove(this.entry.key);
            } else {
                DiskLruCache.this.completeEdit(this, true);
            }
        }

        public void abort() throws IOException {
            DiskLruCache.this.completeEdit(this, false);
        }

        private class ErrorCatchingOutputStream extends FilterOutputStream {
            private ErrorCatchingOutputStream(OutputStream out) {
                super(out);
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream
            public void write(int oneByte) throws IOException {
                try {
                    this.out.write(oneByte);
                } catch (IOException e) {
                    Editor.this.hasErrors = true;
                    throw e;
                }
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream
            public void write(byte[] buffer, int offset, int length) throws IOException {
                try {
                    this.out.write(buffer, offset, length);
                } catch (IOException e) {
                    Editor.this.hasErrors = true;
                    throw e;
                }
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                try {
                    this.out.close();
                } catch (IOException e) {
                    Editor.this.hasErrors = true;
                    throw e;
                }
            }

            @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
            public void flush() throws IOException {
                try {
                    this.out.flush();
                } catch (IOException e) {
                    Editor.this.hasErrors = true;
                    throw e;
                }
            }
        }
    }

    public final class Entry {
        private Editor currentEditor;
        private final String key;
        private final long[] lengths;
        private boolean readable;

        private Entry(String key) {
            this.key = key;
            this.lengths = new long[DiskLruCache.this.valueCount];
        }

        public File getCleanFile(int i) {
            return new File(DiskLruCache.this.directory, this.key + "." + i);
        }

        public File getDirtyFile(int i) {
            return new File(DiskLruCache.this.directory, this.key + "." + i + ".tmp");
        }
    }
}
