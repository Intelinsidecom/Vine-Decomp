package org.scribe.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/* loaded from: classes.dex */
public class ParameterList {
    private final List<Parameter> params;

    public ParameterList() {
        this.params = new ArrayList();
    }

    ParameterList(List<Parameter> params) {
        this.params = new ArrayList(params);
    }

    public ParameterList(Map<String, String> map) {
        this();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            this.params.add(new Parameter(entry.getKey(), entry.getValue()));
        }
    }

    public void add(String key, String value) {
        this.params.add(new Parameter(key, value));
    }

    public String appendTo(String url) {
        Preconditions.checkNotNull(url, "Cannot append to null URL");
        String queryString = asFormUrlEncodedString();
        if (queryString.equals("")) {
            return url;
        }
        return (url + (url.indexOf(63) != -1 ? "&" : '?')) + queryString;
    }

    public String asOauthBaseString() {
        return OAuthEncoder.encode(asFormUrlEncodedString());
    }

    public String asFormUrlEncodedString() {
        if (this.params.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Parameter p : this.params) {
            builder.append('&').append(p.asUrlEncodedPair());
        }
        return builder.toString().substring(1);
    }

    public void addAll(ParameterList other) {
        this.params.addAll(other.params);
    }

    public void addQuerystring(String queryString) {
        if (queryString != null && queryString.length() > 0) {
            String[] arr$ = queryString.split("&");
            for (String param : arr$) {
                String[] pair = param.split("=");
                String key = OAuthEncoder.decode(pair[0]);
                String value = pair.length > 1 ? OAuthEncoder.decode(pair[1]) : "";
                this.params.add(new Parameter(key, value));
            }
        }
    }

    public ParameterList sort() {
        ParameterList sorted = new ParameterList(this.params);
        Collections.sort(sorted.params);
        return sorted;
    }
}
