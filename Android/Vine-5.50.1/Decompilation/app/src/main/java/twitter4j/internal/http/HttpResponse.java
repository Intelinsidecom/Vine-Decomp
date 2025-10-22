package twitter4j.internal.http;

import android.util.Log;
import com.edisonwang.android.slog.SLog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationContext;

/* loaded from: classes.dex */
public abstract class HttpResponse {
    private static final String TAG = "T4JHttpResposne";
    protected final HttpClientConfiguration CONF;
    protected InputStream is;
    protected String responseAsString;
    protected int statusCode;
    private boolean streamConsumed;

    public abstract void disconnect() throws IOException;

    public abstract String getResponseHeader(String str);

    public abstract Map<String, List<String>> getResponseHeaderFields();

    HttpResponse() {
        this.responseAsString = null;
        this.streamConsumed = false;
        this.CONF = ConfigurationContext.getInstance();
    }

    public HttpResponse(HttpClientConfiguration conf) {
        this.responseAsString = null;
        this.streamConsumed = false;
        this.CONF = conf;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public InputStream asStream() {
        if (this.streamConsumed) {
            throw new IllegalStateException("Stream has already been consumed.");
        }
        return this.is;
    }

    public String asString() throws Throwable {
        if (this.responseAsString == null) {
            BufferedReader br = null;
            InputStream stream = null;
            try {
                try {
                    stream = asStream();
                    if (stream == null) {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException e) {
                            }
                        }
                        if (0 != 0) {
                            try {
                                br.close();
                            } catch (IOException e2) {
                            }
                        }
                        disconnectForcibly();
                        return null;
                    }
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                    try {
                        StringBuilder buf = new StringBuilder();
                        while (true) {
                            String line = br2.readLine();
                            if (line == null) {
                                break;
                            }
                            buf.append(line).append("\n");
                        }
                        this.responseAsString = buf.toString();
                        if (SLog.sLogsOn) {
                            Log.d(TAG, this.responseAsString);
                        }
                        stream.close();
                        this.streamConsumed = true;
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException e3) {
                            }
                        }
                        if (br2 != null) {
                            try {
                                br2.close();
                            } catch (IOException e4) {
                            }
                        }
                        disconnectForcibly();
                    } catch (IOException e5) {
                        ioe = e5;
                        throw new TwitterException(ioe.getMessage(), ioe);
                    } catch (Throwable th) {
                        th = th;
                        br = br2;
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException e6) {
                            }
                        }
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e7) {
                            }
                        }
                        disconnectForcibly();
                        throw th;
                    }
                } catch (IOException e8) {
                    ioe = e8;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        return this.responseAsString;
    }

    private void disconnectForcibly() {
        try {
            disconnect();
        } catch (Exception e) {
        }
    }

    public String toString() {
        return "HttpResponse{statusCode=" + this.statusCode + ", responseAsString='" + this.responseAsString + "', is=" + this.is + ", streamConsumed=" + this.streamConsumed + '}';
    }
}
