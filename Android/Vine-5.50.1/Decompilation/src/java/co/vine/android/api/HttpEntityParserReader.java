package co.vine.android.api;

import android.util.Log;
import co.vine.android.network.HttpResult;
import co.vine.android.network.NetworkOperationReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;

/* loaded from: classes.dex */
public class HttpEntityParserReader implements NetworkOperationReader {
    private List<BasicNameValuePair> mParsedObject;
    public static final boolean LOGGABLE = Log.isLoggable("VineParserReader", 3);
    private static final char[] DELIM = {'&'};

    @Override // co.vine.android.network.NetworkOperationReader
    public void readInput(int statusCode, long contentLength, InputStream in) throws IOException {
        if (statusCode == 200) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            try {
                try {
                    if (contentLength < 0) {
                        while (true) {
                            int bytesRead = in.read(buffer, 0, buffer.length);
                            if (bytesRead == -1) {
                                break;
                            } else {
                                out.write(buffer, 0, bytesRead);
                            }
                        }
                    } else {
                        long bytesRemaining = contentLength;
                        while (bytesRemaining > 0) {
                            int bytesRead2 = in.read(buffer, 0, (int) Math.min(bytesRemaining, buffer.length));
                            if (bytesRead2 == -1) {
                                throw new IOException("Invalid content length: " + bytesRemaining);
                            }
                            if (bytesRead2 > 0) {
                                bytesRemaining -= bytesRead2;
                                out.write(buffer, 0, bytesRead2);
                            }
                        }
                    }
                    CharArrayBuffer charBuffer = new CharArrayBuffer(out.size());
                    charBuffer.append(out.toByteArray(), 0, out.size());
                    BasicHeaderValueParser parser = BasicHeaderValueParser.DEFAULT;
                    ParserCursor cursor = new ParserCursor(0, charBuffer.length());
                    List<BasicNameValuePair> list = new ArrayList<>();
                    while (!cursor.atEnd()) {
                        NameValuePair nvp = parser.parseNameValuePair(charBuffer, cursor, DELIM);
                        if (nvp.getName().length() > 0) {
                            list.add(new BasicNameValuePair(URLDecoder.decode(nvp.getName(), "UTF-8"), URLDecoder.decode(nvp.getValue(), "UTF-8")));
                        }
                    }
                    this.mParsedObject = list;
                    do {
                        try {
                        } catch (IOException e) {
                            return;
                        }
                    } while (in.read(buffer, 0, buffer.length) != -1);
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (Exception e2) {
                    if (LOGGABLE) {
                        Log.e("VineParserReader", "Error while reading response body ", e2);
                    }
                    do {
                        try {
                        } catch (IOException e3) {
                            return;
                        }
                    } while (in.read(buffer, 0, buffer.length) != -1);
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                }
            } catch (Throwable th) {
                do {
                } while (in.read(buffer, 0, buffer.length) != -1);
                if (out != null) {
                    out.flush();
                    out.close();
                }
                throw th;
            }
        }
    }

    public <T> T getParsedObject() {
        return (T) this.mParsedObject;
    }

    @Override // co.vine.android.network.NetworkOperationReader
    public void onHandleError(HttpResult result) {
    }
}
