package co.vine.android.util;

import com.edisonwang.android.slog.MessageFormatter;
import com.edisonwang.android.slog.SLog;
import com.edisonwang.android.slog.SLogger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes.dex */
public class FileLogger {
    private static final HashMap<String, FileLogger> sLoggers = new HashMap<>();
    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss:SSS", Locale.US);
    private File mFile;
    private BufferedReader mInputStream;
    private PrintStream mOutputStream;

    public static synchronized FileLogger getLogger(File file) {
        FileLogger logger;
        if (file == null) {
            logger = new FileLogger(null);
        } else {
            String path = file.getAbsolutePath();
            logger = sLoggers.get(path);
            if (logger == null) {
                File parent = file.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                logger = new FileLogger(file);
                sLoggers.put(path, logger);
            }
        }
        return logger;
    }

    private FileLogger(File file) {
        this.mFile = file;
    }

    public synchronized void write(SLogger consoleLogger, String data, Object... stuff) {
        if (this.mOutputStream == null) {
            prepareOutput();
        }
        if (this.mOutputStream != null) {
            this.mOutputStream.print(this.mDateFormat.format(new Date()));
            this.mOutputStream.print(" ");
            String message = MessageFormatter.toStringMessage(data, stuff);
            this.mOutputStream.print(message);
            this.mOutputStream.println();
            if (consoleLogger != null) {
                consoleLogger.i(message);
            }
        }
    }

    public synchronized String read() {
        String line;
        if (this.mInputStream == null) {
            prepareInput();
        }
        if (this.mInputStream != null) {
            try {
                line = this.mInputStream.readLine();
            } catch (IOException e) {
                closeInput();
                line = "Failed to read input file.";
            }
        } else {
            line = null;
        }
        return line;
    }

    public synchronized void prepareInput() {
        closeOutput();
        if (this.mFile != null && this.mInputStream == null) {
            try {
                this.mInputStream = new BufferedReader(new FileReader(this.mFile));
            } catch (FileNotFoundException e) {
                SLog.e("Cannot open log file to read.", (Throwable) e);
            }
        }
    }

    public synchronized void closeOutput() {
        if (this.mOutputStream != null) {
            this.mOutputStream.flush();
            this.mOutputStream.close();
            this.mOutputStream = null;
        }
    }

    public synchronized void closeInput() {
        if (this.mInputStream != null) {
            try {
                try {
                    this.mInputStream.close();
                } finally {
                    this.mInputStream = null;
                }
            } catch (IOException e) {
                SLog.e("Failed to close the reading file.", (Throwable) e);
                this.mInputStream = null;
            }
        }
    }

    public synchronized void clear() {
        closeOutput();
        closeInput();
        if (this.mFile != null) {
            try {
                this.mOutputStream = new PrintStream(new FileOutputStream(this.mFile, false));
                this.mOutputStream.write(0);
                this.mOutputStream.flush();
                this.mOutputStream.close();
            } catch (FileNotFoundException e) {
            }
        }
    }

    public synchronized void prepareOutput() {
    }
}
