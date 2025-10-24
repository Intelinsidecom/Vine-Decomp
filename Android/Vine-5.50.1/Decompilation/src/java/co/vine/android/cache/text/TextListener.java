package co.vine.android.cache.text;

import co.vine.android.network.HttpResult;
import java.util.HashMap;

/* loaded from: classes.dex */
public interface TextListener {
    void onPathError(TextCache textCache, TextKey textKey, HttpResult httpResult);

    void onPathObtained(TextCache textCache, HashMap<TextKey, UrlText> map);
}
