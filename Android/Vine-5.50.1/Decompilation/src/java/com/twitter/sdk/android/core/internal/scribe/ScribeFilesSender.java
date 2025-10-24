package com.twitter.sdk.android.core.internal.scribe;

import android.content.Context;
import android.text.TextUtils;
import com.twitter.sdk.android.core.AuthenticatedClient;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.common.QueueFile;
import io.fabric.sdk.android.services.events.FilesSender;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import javax.net.ssl.SSLSocketFactory;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.MainThreadExecutor;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

/* loaded from: classes.dex */
class ScribeFilesSender implements FilesSender {
    private final AtomicReference<RestAdapter> apiAdapter = new AtomicReference<>();
    private final TwitterAuthConfig authConfig;
    private final Context context;
    private final ExecutorService executorService;
    private final IdManager idManager;
    private final long ownerId;
    private final ScribeConfig scribeConfig;
    private final List<SessionManager<? extends Session>> sessionManagers;
    private final SSLSocketFactory sslSocketFactory;
    private static final byte[] START_JSON_ARRAY = {91};
    private static final byte[] COMMA = {44};
    private static final byte[] END_JSON_ARRAY = {93};

    interface ScribeService {
        @POST("/{version}/jot/{type}")
        @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
        @FormUrlEncoded
        Response upload(@Path("version") String str, @Path("type") String str2, @Field("log[]") String str3);

        @POST("/scribe/{sequence}")
        @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
        @FormUrlEncoded
        Response uploadSequence(@Path("sequence") String str, @Field("log[]") String str2);
    }

    public ScribeFilesSender(Context context, ScribeConfig scribeConfig, long ownerId, TwitterAuthConfig authConfig, List<SessionManager<? extends Session>> sessionManagers, SSLSocketFactory sslSocketFactory, ExecutorService executorService, IdManager idManager) {
        this.context = context;
        this.scribeConfig = scribeConfig;
        this.ownerId = ownerId;
        this.authConfig = authConfig;
        this.sessionManagers = sessionManagers;
        this.sslSocketFactory = sslSocketFactory;
        this.executorService = executorService;
        this.idManager = idManager;
    }

    @Override // io.fabric.sdk.android.services.events.FilesSender
    public boolean send(List<File> files) throws Throwable {
        if (hasApiAdapter()) {
            try {
                String scribeEvents = getScribeEventsAsJsonArrayString(files);
                CommonUtils.logControlled(this.context, scribeEvents);
                Response response = upload(scribeEvents);
                if (response.getStatus() == 200) {
                    return true;
                }
                CommonUtils.logControlledError(this.context, "Failed sending files", null);
            } catch (IOException e) {
                CommonUtils.logControlledError(this.context, "Failed sending files", e);
            } catch (RetrofitError e2) {
                CommonUtils.logControlledError(this.context, "Failed sending files", e2);
                if (e2.getResponse() != null && (e2.getResponse().getStatus() == 500 || e2.getResponse().getStatus() == 400)) {
                    return true;
                }
            }
        } else {
            CommonUtils.logControlled(this.context, "Cannot attempt upload at this time");
        }
        return false;
    }

    String getScribeEventsAsJsonArrayString(List<File> files) throws Throwable {
        final ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        final boolean[] appendComma = new boolean[1];
        out.write(START_JSON_ARRAY);
        for (File f : files) {
            QueueFile qf = null;
            try {
                QueueFile qf2 = new QueueFile(f);
                try {
                    qf2.forEach(new QueueFile.ElementReader() { // from class: com.twitter.sdk.android.core.internal.scribe.ScribeFilesSender.1
                        @Override // io.fabric.sdk.android.services.common.QueueFile.ElementReader
                        public void read(InputStream in, int length) throws IOException {
                            byte[] buf = new byte[length];
                            in.read(buf);
                            if (appendComma[0]) {
                                out.write(ScribeFilesSender.COMMA);
                            } else {
                                appendComma[0] = true;
                            }
                            out.write(buf);
                        }
                    });
                    CommonUtils.closeQuietly(qf2);
                } catch (Throwable th) {
                    th = th;
                    qf = qf2;
                    CommonUtils.closeQuietly(qf);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        out.write(END_JSON_ARRAY);
        return out.toString("UTF-8");
    }

    private boolean hasApiAdapter() {
        return getApiAdapter() != null;
    }

    synchronized RestAdapter getApiAdapter() {
        if (this.apiAdapter.get() == null) {
            Session session = getSession(this.ownerId);
            RequestInterceptor interceptor = new ConfigRequestInterceptor(this.scribeConfig, this.idManager);
            if (isValidSession(session)) {
                this.apiAdapter.compareAndSet(null, new RestAdapter.Builder().setEndpoint(this.scribeConfig.baseUrl).setExecutors(this.executorService, new MainThreadExecutor()).setRequestInterceptor(interceptor).setClient(new AuthenticatedClient(this.authConfig, session, this.sslSocketFactory)).build());
            } else {
                CommonUtils.logControlled(this.context, "No valid session at this time");
            }
        }
        return this.apiAdapter.get();
    }

    private Session getSession(long ownerId) {
        Session sessionToReturn = null;
        for (SessionManager<? extends Session> sessionManager : this.sessionManagers) {
            sessionToReturn = sessionManager.getSession(ownerId);
            if (sessionToReturn != null) {
                break;
            }
        }
        return sessionToReturn;
    }

    private boolean isValidSession(Session session) {
        return (session == null || session.getAuthToken() == null) ? false : true;
    }

    Response upload(String scribeEvents) {
        ScribeService service = (ScribeService) this.apiAdapter.get().create(ScribeService.class);
        return !TextUtils.isEmpty(this.scribeConfig.sequence) ? service.uploadSequence(this.scribeConfig.sequence, scribeEvents) : service.upload(this.scribeConfig.pathVersion, this.scribeConfig.pathType, scribeEvents);
    }

    /* loaded from: classes2.dex */
    static class ConfigRequestInterceptor implements RequestInterceptor {
        private final IdManager idManager;
        private final ScribeConfig scribeConfig;

        ConfigRequestInterceptor(ScribeConfig scribeConfig, IdManager idManager) {
            this.scribeConfig = scribeConfig;
            this.idManager = idManager;
        }

        @Override // retrofit.RequestInterceptor
        public void intercept(RequestInterceptor.RequestFacade request) {
            if (!TextUtils.isEmpty(this.scribeConfig.userAgent)) {
                request.addHeader("User-Agent", this.scribeConfig.userAgent);
            }
            if (!TextUtils.isEmpty(this.idManager.getDeviceUUID())) {
                request.addHeader("X-Client-UUID", this.idManager.getDeviceUUID());
            }
            request.addHeader("X-Twitter-Polling", "true");
        }
    }
}
