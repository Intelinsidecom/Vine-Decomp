package com.googlecode.mp4parser.h264.model;

/* loaded from: classes2.dex */
public class ChromaFormat {
    public static ChromaFormat MONOCHROME = new ChromaFormat(0, 0, 0);
    public static ChromaFormat YUV_420 = new ChromaFormat(1, 2, 2);
    public static ChromaFormat YUV_422 = new ChromaFormat(2, 2, 1);
    public static ChromaFormat YUV_444 = new ChromaFormat(3, 1, 1);
    private int id;
    private int subHeight;
    private int subWidth;

    public ChromaFormat(int id, int subWidth, int subHeight) {
        this.id = id;
        this.subWidth = subWidth;
        this.subHeight = subHeight;
    }

    public static ChromaFormat fromId(int id) {
        if (id == MONOCHROME.id) {
            return MONOCHROME;
        }
        if (id == YUV_420.id) {
            return YUV_420;
        }
        if (id == YUV_422.id) {
            return YUV_422;
        }
        if (id == YUV_444.id) {
            return YUV_444;
        }
        return null;
    }

    public String toString() {
        return "ChromaFormat{\nid=" + this.id + ",\n subWidth=" + this.subWidth + ",\n subHeight=" + this.subHeight + '}';
    }
}
