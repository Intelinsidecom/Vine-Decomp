package com.android.debug.hv;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public class ViewServer implements Runnable {
    private static ViewServer sServer;
    private final ReentrantReadWriteLock mFocusLock;
    private View mFocusedWindow;
    private final List<WindowListener> mListeners;
    private final int mPort;
    private ServerSocket mServer;
    private Thread mThread;
    private ExecutorService mThreadPool;
    private final HashMap<View, String> mWindows;
    private final ReentrantReadWriteLock mWindowsLock;

    private interface WindowListener {
        void focusChanged();

        void windowsChanged();
    }

    public static ViewServer get(Context context) {
        ApplicationInfo info = context.getApplicationInfo();
        if (PropertyConfiguration.USER.equals(Build.TYPE) && (info.flags & 2) != 0) {
            if (sServer == null) {
                sServer = new ViewServer(4939);
            }
            if (!sServer.isRunning()) {
                try {
                    sServer.start();
                } catch (IOException e) {
                    Log.d("ViewServer", "Error:", e);
                }
            }
        } else {
            sServer = new NoopViewServer();
        }
        return sServer;
    }

    private ViewServer() {
        this.mListeners = new CopyOnWriteArrayList();
        this.mWindows = new HashMap<>();
        this.mWindowsLock = new ReentrantReadWriteLock();
        this.mFocusLock = new ReentrantReadWriteLock();
        this.mPort = -1;
    }

    private ViewServer(int port) {
        this.mListeners = new CopyOnWriteArrayList();
        this.mWindows = new HashMap<>();
        this.mWindowsLock = new ReentrantReadWriteLock();
        this.mFocusLock = new ReentrantReadWriteLock();
        this.mPort = port;
    }

    public boolean start() throws IOException {
        if (this.mThread != null) {
            return false;
        }
        this.mThread = new Thread(this, "Local View Server [port=" + this.mPort + "]");
        this.mThreadPool = Executors.newFixedThreadPool(10);
        this.mThread.start();
        return true;
    }

    public boolean isRunning() {
        return this.mThread != null && this.mThread.isAlive();
    }

    public void addWindow(Activity activity) {
        String name;
        String name2 = activity.getTitle().toString();
        if (TextUtils.isEmpty(name2)) {
            name = activity.getClass().getCanonicalName() + "/0x" + System.identityHashCode(activity);
        } else {
            name = name2 + "(" + activity.getClass().getCanonicalName() + ")";
        }
        addWindow(activity.getWindow().getDecorView(), name);
    }

    public void removeWindow(Activity activity) {
        removeWindow(activity.getWindow().getDecorView());
    }

    public void addWindow(View view, String name) {
        this.mWindowsLock.writeLock().lock();
        try {
            this.mWindows.put(view.getRootView(), name);
            this.mWindowsLock.writeLock().unlock();
            fireWindowsChangedEvent();
        } catch (Throwable th) {
            this.mWindowsLock.writeLock().unlock();
            throw th;
        }
    }

    public void removeWindow(View view) {
        this.mWindowsLock.writeLock().lock();
        try {
            this.mWindows.remove(view.getRootView());
            this.mWindowsLock.writeLock().unlock();
            fireWindowsChangedEvent();
        } catch (Throwable th) {
            this.mWindowsLock.writeLock().unlock();
            throw th;
        }
    }

    public void setFocusedWindow(Activity activity) {
        setFocusedWindow(activity.getWindow().getDecorView());
    }

    public void setFocusedWindow(View view) {
        View rootView;
        this.mFocusLock.writeLock().lock();
        if (view == null) {
            rootView = null;
        } else {
            try {
                rootView = view.getRootView();
            } catch (Throwable th) {
                this.mFocusLock.writeLock().unlock();
                throw th;
            }
        }
        this.mFocusedWindow = rootView;
        this.mFocusLock.writeLock().unlock();
        fireFocusChangedEvent();
    }

    @Override // java.lang.Runnable
    public void run() throws IOException {
        try {
            this.mServer = new ServerSocket(this.mPort, 10, InetAddress.getLocalHost());
        } catch (Exception e) {
            Log.w("ViewServer", "Starting ServerSocket error: ", e);
        }
        while (this.mServer != null && Thread.currentThread() == this.mThread) {
            try {
                Socket client = this.mServer.accept();
                if (this.mThreadPool != null) {
                    this.mThreadPool.submit(new ViewServerWorker(client));
                } else {
                    try {
                        client.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (Exception e3) {
                Log.w("ViewServer", "Connection error: ", e3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean writeValue(Socket client, String value) throws Throwable {
        BufferedWriter out;
        BufferedWriter out2 = null;
        try {
            OutputStream clientStream = client.getOutputStream();
            out = new BufferedWriter(new OutputStreamWriter(clientStream), 8192);
        } catch (Exception e) {
        } catch (Throwable th) {
            th = th;
        }
        try {
            out.write(value);
            out.write("\n");
            out.flush();
            if (out == null) {
                return true;
            }
            try {
                out.close();
                return true;
            } catch (IOException e2) {
                return false;
            }
        } catch (Exception e3) {
            out2 = out;
            if (out2 == null) {
                return false;
            }
            try {
                out2.close();
                return false;
            } catch (IOException e4) {
                return false;
            }
        } catch (Throwable th2) {
            th = th2;
            out2 = out;
            if (out2 != null) {
                try {
                    out2.close();
                } catch (IOException e5) {
                }
            }
            throw th;
        }
    }

    private void fireWindowsChangedEvent() {
        for (WindowListener listener : this.mListeners) {
            listener.windowsChanged();
        }
    }

    private void fireFocusChangedEvent() {
        for (WindowListener listener : this.mListeners) {
            listener.focusChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addWindowListener(WindowListener listener) {
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeWindowListener(WindowListener listener) {
        this.mListeners.remove(listener);
    }

    private static class UncloseableOutputStream extends OutputStream {
        private final OutputStream mStream;

        UncloseableOutputStream(OutputStream stream) {
            this.mStream = stream;
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
        }

        public boolean equals(Object o) {
            return this.mStream.equals(o);
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            this.mStream.flush();
        }

        public int hashCode() {
            return this.mStream.hashCode();
        }

        public String toString() {
            return this.mStream.toString();
        }

        @Override // java.io.OutputStream
        public void write(byte[] buffer, int offset, int count) throws IOException {
            this.mStream.write(buffer, offset, count);
        }

        @Override // java.io.OutputStream
        public void write(byte[] buffer) throws IOException {
            this.mStream.write(buffer);
        }

        @Override // java.io.OutputStream
        public void write(int oneByte) throws IOException {
            this.mStream.write(oneByte);
        }
    }

    private static class NoopViewServer extends ViewServer {
        private NoopViewServer() {
            super();
        }

        @Override // com.android.debug.hv.ViewServer
        public boolean start() throws IOException {
            return false;
        }

        @Override // com.android.debug.hv.ViewServer
        public boolean isRunning() {
            return false;
        }

        @Override // com.android.debug.hv.ViewServer
        public void addWindow(Activity activity) {
        }

        @Override // com.android.debug.hv.ViewServer
        public void removeWindow(Activity activity) {
        }

        @Override // com.android.debug.hv.ViewServer
        public void addWindow(View view, String name) {
        }

        @Override // com.android.debug.hv.ViewServer
        public void removeWindow(View view) {
        }

        @Override // com.android.debug.hv.ViewServer
        public void setFocusedWindow(Activity activity) {
        }

        @Override // com.android.debug.hv.ViewServer
        public void setFocusedWindow(View view) {
        }

        @Override // com.android.debug.hv.ViewServer, java.lang.Runnable
        public void run() {
        }
    }

    private class ViewServerWorker implements WindowListener, Runnable {
        private Socket mClient;
        private final Object[] mLock = new Object[0];
        private boolean mNeedWindowListUpdate = false;
        private boolean mNeedFocusedWindowUpdate = false;

        public ViewServerWorker(Socket client) {
            this.mClient = client;
        }

        @Override // java.lang.Runnable
        public void run() throws Throwable {
            String command;
            String parameters;
            BufferedReader in = null;
            try {
                try {
                    BufferedReader in2 = new BufferedReader(new InputStreamReader(this.mClient.getInputStream()), 1024);
                    try {
                        String request = in2.readLine();
                        int index = request.indexOf(32);
                        if (index == -1) {
                            command = request;
                            parameters = "";
                        } else {
                            command = request.substring(0, index);
                            parameters = request.substring(index + 1);
                        }
                        boolean result = ("PROTOCOL".equalsIgnoreCase(command) || "SERVER".equalsIgnoreCase(command)) ? ViewServer.writeValue(this.mClient, "4") : "LIST".equalsIgnoreCase(command) ? listWindows(this.mClient) : "GET_FOCUS".equalsIgnoreCase(command) ? getFocusedWindow(this.mClient) : "AUTOLIST".equalsIgnoreCase(command) ? windowManagerAutolistLoop() : windowCommand(this.mClient, command, parameters);
                        if (!result) {
                            Log.w("ViewServer", "An error occurred with the command: " + command);
                        }
                        if (in2 != null) {
                            try {
                                in2.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (this.mClient != null) {
                            try {
                                this.mClient.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                    } catch (IOException e3) {
                        e = e3;
                        in = in2;
                        Log.w("ViewServer", "Connection error: ", e);
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        if (this.mClient != null) {
                            try {
                                this.mClient.close();
                            } catch (IOException e5) {
                                e5.printStackTrace();
                            }
                        }
                    } catch (Throwable th) {
                        th = th;
                        in = in2;
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e6) {
                                e6.printStackTrace();
                            }
                        }
                        if (this.mClient != null) {
                            try {
                                this.mClient.close();
                            } catch (IOException e7) {
                                e7.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (IOException e8) {
                e = e8;
            }
        }

        private boolean windowCommand(Socket client, String command, String parameters) throws Throwable {
            View window;
            boolean success = true;
            BufferedWriter out = null;
            try {
                try {
                    int index = parameters.indexOf(32);
                    if (index == -1) {
                        index = parameters.length();
                    }
                    String code = parameters.substring(0, index);
                    int hashCode = (int) Long.parseLong(code, 16);
                    parameters = index < parameters.length() ? parameters.substring(index + 1) : "";
                    window = findWindow(hashCode);
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Exception e) {
                e = e;
            }
            if (window == null) {
                if (0 == 0) {
                    return false;
                }
                try {
                    out.close();
                    return false;
                } catch (IOException e2) {
                    return false;
                }
            }
            Method dispatch = ViewDebug.class.getDeclaredMethod("dispatchCommand", View.class, String.class, String.class, OutputStream.class);
            dispatch.setAccessible(true);
            dispatch.invoke(null, window, command, parameters, new UncloseableOutputStream(client.getOutputStream()));
            if (!client.isOutputShutdown()) {
                BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                try {
                    out2.write("DONE\n");
                    out2.flush();
                    out = out2;
                } catch (Exception e3) {
                    e = e3;
                    out = out2;
                    Log.w("ViewServer", "Could not send command " + command + " with parameters " + parameters, e);
                    success = false;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e4) {
                            success = false;
                        }
                    }
                    return success;
                } catch (Throwable th2) {
                    th = th2;
                    out = out2;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e5) {
                        }
                    }
                    throw th;
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e6) {
                    success = false;
                }
            }
            return success;
        }

        private View findWindow(int hashCode) {
            if (hashCode == -1) {
                ViewServer.this.mWindowsLock.readLock().lock();
                try {
                    View window = ViewServer.this.mFocusedWindow;
                    return window;
                } finally {
                }
            }
            ViewServer.this.mWindowsLock.readLock().lock();
            try {
                for (Map.Entry<View, String> entry : ViewServer.this.mWindows.entrySet()) {
                    if (System.identityHashCode(entry.getKey()) == hashCode) {
                        View window2 = entry.getKey();
                        return window2;
                    }
                }
                ViewServer.this.mWindowsLock.readLock().unlock();
                return null;
            } finally {
            }
        }

        private boolean listWindows(Socket client) throws Throwable {
            BufferedWriter out = null;
            try {
                ViewServer.this.mWindowsLock.readLock().lock();
                OutputStream clientStream = client.getOutputStream();
                BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(clientStream), 8192);
                try {
                    for (Map.Entry<View, String> entry : ViewServer.this.mWindows.entrySet()) {
                        out2.write(Integer.toHexString(System.identityHashCode(entry.getKey())));
                        out2.write(32);
                        out2.append((CharSequence) entry.getValue());
                        out2.write(10);
                    }
                    out2.write("DONE.\n");
                    out2.flush();
                    ViewServer.this.mWindowsLock.readLock().unlock();
                    if (out2 == null) {
                        return true;
                    }
                    try {
                        out2.close();
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                } catch (Exception e2) {
                    out = out2;
                    ViewServer.this.mWindowsLock.readLock().unlock();
                    if (out == null) {
                        return false;
                    }
                    try {
                        out.close();
                        return false;
                    } catch (IOException e3) {
                        return false;
                    }
                } catch (Throwable th) {
                    th = th;
                    out = out2;
                    ViewServer.this.mWindowsLock.readLock().unlock();
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e4) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
            } catch (Throwable th2) {
                th = th2;
            }
        }

        private boolean getFocusedWindow(Socket client) throws Throwable {
            BufferedWriter out;
            BufferedWriter out2 = null;
            try {
                OutputStream clientStream = client.getOutputStream();
                out = new BufferedWriter(new OutputStreamWriter(clientStream), 8192);
            } catch (Exception e) {
            } catch (Throwable th) {
                th = th;
            }
            try {
                ViewServer.this.mFocusLock.readLock().lock();
                try {
                    View focusedWindow = ViewServer.this.mFocusedWindow;
                    if (focusedWindow != null) {
                        ViewServer.this.mWindowsLock.readLock().lock();
                        try {
                            String focusName = (String) ViewServer.this.mWindows.get(ViewServer.this.mFocusedWindow);
                            ViewServer.this.mWindowsLock.readLock().unlock();
                            out.write(Integer.toHexString(System.identityHashCode(focusedWindow)));
                            out.write(32);
                            out.append((CharSequence) focusName);
                        } catch (Throwable th2) {
                            ViewServer.this.mWindowsLock.readLock().unlock();
                            throw th2;
                        }
                    }
                    out.write(10);
                    out.flush();
                    if (out == null) {
                        return true;
                    }
                    try {
                        out.close();
                        return true;
                    } catch (IOException e2) {
                        return false;
                    }
                } finally {
                    ViewServer.this.mFocusLock.readLock().unlock();
                }
            } catch (Exception e3) {
                out2 = out;
                if (out2 == null) {
                    return false;
                }
                try {
                    out2.close();
                    return false;
                } catch (IOException e4) {
                    return false;
                }
            } catch (Throwable th3) {
                th = th3;
                out2 = out;
                if (out2 != null) {
                    try {
                        out2.close();
                    } catch (IOException e5) {
                    }
                }
                throw th;
            }
        }

        @Override // com.android.debug.hv.ViewServer.WindowListener
        public void windowsChanged() {
            synchronized (this.mLock) {
                this.mNeedWindowListUpdate = true;
                this.mLock.notifyAll();
            }
        }

        @Override // com.android.debug.hv.ViewServer.WindowListener
        public void focusChanged() {
            synchronized (this.mLock) {
                this.mNeedFocusedWindowUpdate = true;
                this.mLock.notifyAll();
            }
        }

        private boolean windowManagerAutolistLoop() throws Throwable {
            ViewServer.this.addWindowListener(this);
            BufferedWriter out = null;
            try {
                try {
                    BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(this.mClient.getOutputStream()));
                    while (!Thread.interrupted()) {
                        try {
                            boolean needWindowListUpdate = false;
                            boolean needFocusedWindowUpdate = false;
                            synchronized (this.mLock) {
                                while (!this.mNeedWindowListUpdate && !this.mNeedFocusedWindowUpdate) {
                                    this.mLock.wait();
                                }
                                if (this.mNeedWindowListUpdate) {
                                    this.mNeedWindowListUpdate = false;
                                    needWindowListUpdate = true;
                                }
                                if (this.mNeedFocusedWindowUpdate) {
                                    this.mNeedFocusedWindowUpdate = false;
                                    needFocusedWindowUpdate = true;
                                }
                            }
                            if (needWindowListUpdate) {
                                out2.write("LIST UPDATE\n");
                                out2.flush();
                            }
                            if (needFocusedWindowUpdate) {
                                out2.write("FOCUS UPDATE\n");
                                out2.flush();
                            }
                        } catch (Exception e) {
                            e = e;
                            out = out2;
                            Log.w("ViewServer", "Connection error: ", e);
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (IOException e2) {
                                }
                            }
                            ViewServer.this.removeWindowListener(this);
                            return true;
                        } catch (Throwable th) {
                            th = th;
                            out = out2;
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (IOException e3) {
                                }
                            }
                            ViewServer.this.removeWindowListener(this);
                            throw th;
                        }
                    }
                    if (out2 != null) {
                        try {
                            out2.close();
                        } catch (IOException e4) {
                        }
                    }
                    ViewServer.this.removeWindowListener(this);
                    return true;
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e5) {
                e = e5;
            }
        }
    }
}
