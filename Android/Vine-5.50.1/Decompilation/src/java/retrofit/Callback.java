package retrofit;

import retrofit.client.Response;

/* loaded from: classes.dex */
public interface Callback<T> {
    void failure(RetrofitError retrofitError);

    void success(T t, Response response);
}
