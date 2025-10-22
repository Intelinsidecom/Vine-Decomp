package co.vine.android.network.apache;

import android.content.Context;
import co.vine.android.client.VineAPI;
import co.vine.android.network.FileNetworkEntity;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.NetworkOperationFactory;
import co.vine.android.network.NetworkOperationReader;
import co.vine.android.util.ConsoleLoggers;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLogger;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class HttpOperationFactory implements NetworkOperationFactory<VineAPI> {
    private static final HttpOperationFactory INSTANCE = new HttpOperationFactory();
    private static final SLogger LOGGER = ConsoleLoggers.NETWORK.get();

    @Override // co.vine.android.network.NetworkOperationFactory
    public /* bridge */ /* synthetic */ NetworkOperation createBasicAuthPostRequest(Context context, StringBuilder sb, VineAPI vineAPI, List list, NetworkOperationReader networkOperationReader) {
        return createBasicAuthPostRequest2(context, sb, vineAPI, (List<BasicNameValuePair>) list, networkOperationReader);
    }

    @Override // co.vine.android.network.NetworkOperationFactory
    public /* bridge */ /* synthetic */ NetworkOperation createBasicAuthPutRequest(Context context, StringBuilder sb, VineAPI vineAPI, List list, NetworkOperationReader networkOperationReader) {
        return createBasicAuthPutRequest2(context, sb, vineAPI, (List<BasicNameValuePair>) list, networkOperationReader);
    }

    @Override // co.vine.android.network.NetworkOperationFactory
    public /* bridge */ /* synthetic */ NetworkOperation createPostRequest(Context context, StringBuilder sb, List list, NetworkOperationReader networkOperationReader, VineAPI vineAPI) {
        return createPostRequest2(context, sb, (List<BasicNameValuePair>) list, networkOperationReader, vineAPI);
    }

    public static HttpOperationFactory getInstance() {
        return INSTANCE;
    }

    private HttpOperationFactory() {
    }

    public HttpOperation createBasicAuthGetRequest(HttpOperationClient client, StringBuilder url, VineAPI api, NetworkOperationReader reader) {
        HttpGet get = new HttpGet(url.toString());
        HttpOperation op = new HttpOperation(client, get, reader, api);
        api.addSessionKeyAuthHeader(op);
        return op;
    }

    public HttpOperation createBasicAuthGetRequest(HttpOperationClient client, StringBuilder url, VineAPI api, NetworkOperationReader reader, String key) {
        HttpGet get = new HttpGet(url.toString());
        HttpOperation op = new HttpOperation(client, get, reader, api);
        VineAPI.addSessionKeyAuthHeader(op, key);
        return op;
    }

    public HttpOperation createBasicAuthPutRequest(HttpOperationClient client, StringBuilder url, VineAPI api, List<BasicNameValuePair> params, NetworkOperationReader reader) {
        HttpPut put = new HttpPut(url.toString());
        if (params != null) {
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
                put.setEntity(entity);
            } catch (UnsupportedEncodingException e) {
                LOGGER.e("Failed to create url entity", (Throwable) e);
            }
        }
        HttpOperation op = new HttpOperation(client, put, reader, api);
        api.addSessionKeyAuthHeader(op);
        return op;
    }

    public HttpOperation createBasicAuthPostRequest(HttpOperationClient client, StringBuilder url, VineAPI api, List<BasicNameValuePair> params, NetworkOperationReader reader, String... headers) {
        HttpOperation op = createPostRequest(client, url, params, reader, api);
        api.addSessionKeyAuthHeader(op);
        for (String header : headers) {
            VineAPI.addContentTypeHeader(op, header);
        }
        return op;
    }

    public HttpOperation createBasicAuthJsonPostRequest(HttpOperationClient client, StringBuilder url, VineAPI api, JSONObject body, NetworkOperationReader reader) {
        String bodyString = body == null ? null : body.toString();
        return createBasicAuthJsonPostRequest(client, url, api, bodyString, reader);
    }

    public HttpOperation createBasicAuthJsonPostRequest(HttpOperationClient client, StringBuilder url, VineAPI api, String body, NetworkOperationReader reader) {
        HttpOperation op = createPostRequest(client, url, body, reader, api);
        api.addSessionKeyAuthHeader(op);
        VineAPI.addContentTypeHeader(op, "application/json");
        return op;
    }

    public HttpOperation createBasicAuthJsonPutRequest(HttpOperationClient client, StringBuilder url, VineAPI api, JSONObject body, NetworkOperationReader reader) {
        String bodyString = body == null ? null : body.toString();
        return createBasicAuthJsonPutRequest(client, url, api, bodyString, reader);
    }

    public HttpOperation createBasicAuthJsonPutRequest(HttpOperationClient client, StringBuilder url, VineAPI api, String body, NetworkOperationReader reader) {
        HttpOperation op = createPutRequest(client, url, body, reader, api);
        api.addSessionKeyAuthHeader(op);
        VineAPI.addContentTypeHeader(op, "application/json");
        return op;
    }

    public HttpOperation createBasicAuthDeleteRequest(HttpOperationClient client, StringBuilder url, VineAPI api, NetworkOperationReader reader) {
        HttpDelete delete = new HttpDelete(url.toString());
        HttpOperation op = new HttpOperation(client, delete, reader, api);
        api.addSessionKeyAuthHeader(op);
        return op;
    }

    public HttpOperation createBasicAuthDeleteRequest(HttpOperationClient client, StringBuilder url, VineAPI api, NetworkOperationReader reader, String key) {
        HttpDelete delete = new HttpDelete(url.toString());
        HttpOperation op = new HttpOperation(client, delete, reader, api);
        VineAPI.addSessionKeyAuthHeader(op, key);
        return op;
    }

    public HttpOperation createGetRequest(HttpOperationClient client, StringBuilder url, NetworkOperationReader reader, VineAPI api) {
        HttpGet get = new HttpGet(url.toString());
        return new HttpOperation(client, get, reader, api);
    }

    public HttpOperation createResourceGetRequest(HttpOperationClient client, StringBuilder url, VineAPI api, NetworkOperationReader reader) {
        HttpGet get = new HttpGet(url.toString());
        HttpOperation op = new HttpOperation(client, get, reader, api);
        if (url.toString().startsWith("https")) {
            api.addSessionKeyAuthHeader(op);
        }
        return op;
    }

    public HttpOperation createMediaPutRequest(HttpOperationClient client, StringBuilder url, NetworkOperationReader reader, HttpEntity entity, VineAPI api) {
        HttpPut put = new HttpPut(url.toString());
        if (entity != null) {
            put.setEntity(entity);
        }
        HttpOperation op = new HttpOperation(client, put, reader, api);
        api.addSessionKeyAuthHeader(op);
        return op;
    }

    public HttpOperation createPostRequest(HttpOperationClient client, StringBuilder url, List<BasicNameValuePair> params, NetworkOperationReader reader, VineAPI api) {
        HttpPost post = new HttpPost(url.toString());
        if (params != null) {
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
                post.setEntity(entity);
            } catch (UnsupportedEncodingException e) {
                LOGGER.e("Failed to create url entity", (Throwable) e);
            }
        }
        return new HttpOperation(client, post, reader, api);
    }

    private HttpOperation createPostRequest(HttpOperationClient client, CharSequence url, String body, NetworkOperationReader reader, VineAPI api) {
        HttpPost post = new HttpPost(url.toString());
        if (body != null) {
            try {
                LOGGER.i("Posting Video:\n{}\n", body);
                StringEntity entity = new StringEntity(body, "UTF-8");
                entity.setContentType("application/json");
                post.setEntity(entity);
            } catch (IOException e) {
                LOGGER.e("Failed to create Post Request.", (Throwable) e);
            }
        }
        return new HttpOperation(client, post, reader, api);
    }

    private HttpOperation createPutRequest(HttpOperationClient client, CharSequence url, String body, NetworkOperationReader reader, VineAPI api) {
        HttpPut put = new HttpPut(url.toString());
        if (body != null) {
            try {
                StringEntity entity = new StringEntity(body, "UTF-8");
                entity.setContentType("application/json");
                put.setEntity(entity);
            } catch (IOException e) {
                LOGGER.e("Failed to create Put Request.", (Throwable) e);
            }
        }
        return new HttpOperation(client, put, reader, api);
    }

    public HttpOperation createOauth10aSignedPostRequest(HttpOperationClient client, CharSequence url, List<BasicNameValuePair> params, NetworkOperationReader reader, String consumerKey, String consumerSecret) {
        HttpPost post = new HttpPost(url.toString());
        if (params != null) {
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
                post.setEntity(entity);
            } catch (UnsupportedEncodingException e) {
                LOGGER.e("Failed to create url entity", (Throwable) e);
            }
        }
        try {
            CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
            consumer.sign(post);
        } catch (OAuthCommunicationException | OAuthExpectationFailedException | OAuthMessageSignerException e2) {
            CrashUtil.logException(e2, "Failed to create a signed OAuth request for consumerKey={}", consumerKey);
        }
        return new HttpOperation(client, post, reader, null);
    }

    public HttpOperationClient getDefaultClient(Context context) {
        return ThreadedHttpOperationClient.getInstance(context);
    }

    @Override // co.vine.android.network.NetworkOperationFactory
    public NetworkOperation createResourceGetRequest(Context context, StringBuilder url, VineAPI api, NetworkOperationReader reader) {
        return createResourceGetRequest(getDefaultClient(context), url, api, reader);
    }

    /* renamed from: createPostRequest, reason: avoid collision after fix types in other method */
    public NetworkOperation createPostRequest2(Context context, StringBuilder url, List<BasicNameValuePair> params, NetworkOperationReader reader, VineAPI api) {
        return createPostRequest(getDefaultClient(context), url, params, reader, api);
    }

    @Override // co.vine.android.network.NetworkOperationFactory
    public NetworkOperation createBasicAuthGetRequest(Context context, StringBuilder url, VineAPI api, NetworkOperationReader reader, String key) {
        return createBasicAuthGetRequest(getDefaultClient(context), url, api, reader, key);
    }

    /* renamed from: createBasicAuthPutRequest, reason: avoid collision after fix types in other method */
    public NetworkOperation createBasicAuthPutRequest2(Context context, StringBuilder url, VineAPI api, List<BasicNameValuePair> params, NetworkOperationReader reader) {
        return createBasicAuthPutRequest(getDefaultClient(context), url, api, params, reader);
    }

    @Override // co.vine.android.network.NetworkOperationFactory
    public NetworkOperation createBasicAuthGetRequest(Context context, StringBuilder url, VineAPI api, NetworkOperationReader reader) {
        return createBasicAuthGetRequest(getDefaultClient(context), url, api, reader);
    }

    @Override // co.vine.android.network.NetworkOperationFactory
    public NetworkOperation createGetRequest(Context context, StringBuilder url, NetworkOperationReader vp, VineAPI api) {
        return createGetRequest(getDefaultClient(context), url, vp, api);
    }

    /* renamed from: createBasicAuthPostRequest, reason: avoid collision after fix types in other method */
    public NetworkOperation createBasicAuthPostRequest2(Context context, StringBuilder url, VineAPI api, List<BasicNameValuePair> params, NetworkOperationReader reader) {
        return createBasicAuthPostRequest(getDefaultClient(context), url, api, params, reader, new String[0]);
    }

    @Override // co.vine.android.network.NetworkOperationFactory
    public NetworkOperation createBasicAuthDeleteRequest(Context context, StringBuilder url, VineAPI api, NetworkOperationReader reader) {
        return createBasicAuthDeleteRequest(getDefaultClient(context), url, api, reader);
    }

    @Override // co.vine.android.network.NetworkOperationFactory
    public NetworkOperation createBasicAuthJsonPostRequest(Context context, StringBuilder url, VineAPI api, JSONObject postBody, NetworkOperationReader reader) {
        return createBasicAuthJsonPostRequest(getDefaultClient(context), url, api, postBody, reader);
    }

    @Override // co.vine.android.network.NetworkOperationFactory
    public NetworkOperation createBasicAuthJsonPostRequest(Context context, StringBuilder url, VineAPI api, String postBody, NetworkOperationReader reader) {
        return createBasicAuthJsonPostRequest(getDefaultClient(context), url, api, postBody, reader);
    }

    @Override // co.vine.android.network.NetworkOperationFactory
    public NetworkOperation createBasicAuthJsonPutRequest(Context context, StringBuilder url, VineAPI api, JSONObject postBody, NetworkOperationReader reader) {
        return createBasicAuthJsonPutRequest(getDefaultClient(context), url, api, postBody, reader);
    }

    @Override // co.vine.android.network.NetworkOperationFactory
    public NetworkOperation createBasicAuthDeleteRequest(Context context, StringBuilder url, VineAPI api, NetworkOperationReader reader, String cachedKey) {
        return createBasicAuthDeleteRequest(getDefaultClient(context), url, api, reader, cachedKey);
    }

    @Override // co.vine.android.network.NetworkOperationFactory
    public NetworkOperation createMediaPutRequest(Context context, StringBuilder url, NetworkOperationReader reader, FileNetworkEntity entity, VineAPI api) {
        return createMediaPutRequest(getDefaultClient(context), url, reader, (HttpEntity) entity, api);
    }

    @Override // co.vine.android.network.NetworkOperationFactory
    public NetworkOperation createOauth10aSignedPostRequest(Context context, StringBuilder url, List<BasicNameValuePair> params, NetworkOperationReader reader, String consumerKey, String consumerSecret) {
        return createOauth10aSignedPostRequest(getDefaultClient(context), url, params, reader, consumerKey, consumerSecret);
    }
}
