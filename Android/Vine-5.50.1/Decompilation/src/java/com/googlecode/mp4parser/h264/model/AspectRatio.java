package com.googlecode.mp4parser.h264.model;

/* loaded from: classes2.dex */
public class AspectRatio {
    public static final AspectRatio Extended_SAR = new AspectRatio(255);
    private int value;

    private AspectRatio(int value) {
        this.value = value;
    }

    public static AspectRatio fromValue(int value) {
        return value == Extended_SAR.value ? Extended_SAR : new AspectRatio(value);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("AspectRatio{");
        sb.append("value=").append(this.value);
        sb.append('}');
        return sb.toString();
    }
}
