package co.vine.android.network;

import android.content.Context;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/* loaded from: classes.dex */
public interface NetworkOperationFactory<T> {
    NetworkOperation createBasicAuthDeleteRequest(Context context, StringBuilder sb, T t, NetworkOperationReader networkOperationReader);

    NetworkOperation createBasicAuthDeleteRequest(Context context, StringBuilder sb, T t, NetworkOperationReader networkOperationReader, String str);

    NetworkOperation createBasicAuthGetRequest(Context context, StringBuilder sb, T t, NetworkOperationReader networkOperationReader);

    NetworkOperation createBasicAuthGetRequest(Context context, StringBuilder sb, T t, NetworkOperationReader networkOperationReader, String str);

    NetworkOperation createBasicAuthJsonPostRequest(Context context, StringBuilder sb, T t, String str, NetworkOperationReader networkOperationReader);

    NetworkOperation createBasicAuthJsonPostRequest(Context context, StringBuilder sb, T t, JSONObject jSONObject, NetworkOperationReader networkOperationReader);

    NetworkOperation createBasicAuthJsonPutRequest(Context context, StringBuilder sb, T t, JSONObject jSONObject, NetworkOperationReader networkOperationReader);

    NetworkOperation createBasicAuthPostRequest(Context context, StringBuilder sb, T t, List<BasicNameValuePair> list, NetworkOperationReader networkOperationReader);

    NetworkOperation createBasicAuthPutRequest(Context context, StringBuilder sb, T t, List<BasicNameValuePair> list, NetworkOperationReader networkOperationReader);

    NetworkOperation createGetRequest(Context context, StringBuilder sb, NetworkOperationReader networkOperationReader, T t);

    NetworkOperation createMediaPutRequest(Context context, StringBuilder sb, NetworkOperationReader networkOperationReader, FileNetworkEntity fileNetworkEntity, T t);

    NetworkOperation createOauth10aSignedPostRequest(Context context, StringBuilder sb, List<BasicNameValuePair> list, NetworkOperationReader networkOperationReader, String str, String str2);

    NetworkOperation createPostRequest(Context context, StringBuilder sb, List<BasicNameValuePair> list, NetworkOperationReader networkOperationReader, T t);

    NetworkOperation createResourceGetRequest(Context context, StringBuilder sb, T t, NetworkOperationReader networkOperationReader);
}
