package com.digits.sdk.android;

import com.twitter.sdk.android.core.internal.scribe.EventNamespace;

/* loaded from: classes.dex */
class DigitsScribeConstants {
    static final EventNamespace.Builder DIGITS_EVENT_BUILDER = new EventNamespace.Builder().setClient("tfw").setPage("android").setSection("digits");

    enum Element {
        COUNTRY_CODE("country_code"),
        SUBMIT("submit"),
        RETRY("retry"),
        CALL("call"),
        CANCEL("cancel"),
        RESEND("resend"),
        DISMISS("dismiss");

        private final String element;

        Element(String element) {
            this.element = element;
        }

        public String getElement() {
            return this.element;
        }
    }
}
