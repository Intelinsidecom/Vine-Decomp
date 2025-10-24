package co.vine.android.api;

import android.util.Log;
import co.vine.android.network.HttpResult;
import co.vine.android.network.NetworkOperationReader;
import com.edisonwang.android.slog.SLog;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class VineParserReader implements NetworkOperationReader {
    public static final boolean LOGGABLE = Log.isLoggable("VineParserReader", 3);
    private static VineParsersInterface sParserCreator;
    private Object mParsedObject;
    private final VineParsersInterface mParserCreator;
    private final int mType;

    public interface VineParsersInterface {
        Object parseError(InputStream inputStream) throws IOException;

        Object parseVineResponse(InputStream inputStream, int i) throws IOException;
    }

    public static void setParserCreator(VineParsersInterface parserCreator) {
        sParserCreator = parserCreator;
    }

    @Override // co.vine.android.network.NetworkOperationReader
    public void readInput(int statusCode, long contentLength, InputStream is) throws IOException {
        if (LOGGABLE && SLog.sLogsOn) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int len = is.read(buffer);
                if (len <= -1) {
                    break;
                }
                baos.write(buffer, 0, len);
                Log.d("VineParserReader", "Response: '" + new String(buffer, 0, len) + "'");
            }
            baos.flush();
            is.close();
            is = new ByteArrayInputStream(baos.toByteArray());
        }
        if (statusCode == 200) {
            this.mParsedObject = this.mParserCreator.parseVineResponse(is, this.mType);
            return;
        }
        this.mParsedObject = this.mParserCreator.parseError(is);
        if (LOGGABLE) {
            Log.d("VineParserReader", "ERROR: Response with " + statusCode);
        }
    }

    public <T> T getParsedObject() {
        return (T) this.mParsedObject;
    }

    public static VineParserReader createParserReader(int type) {
        return new VineParserReader(sParserCreator, type);
    }

    private VineParserReader(VineParsersInterface parserCreator, int type) {
        this.mType = type;
        this.mParserCreator = parserCreator;
    }

    @Override // co.vine.android.network.NetworkOperationReader
    public void onHandleError(HttpResult result) {
    }
}
