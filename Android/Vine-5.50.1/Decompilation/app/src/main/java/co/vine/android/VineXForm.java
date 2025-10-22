package co.vine.android;

/* loaded from: classes.dex */
public final class VineXForm {
    public static String xform(byte[] m, byte x) {
        StringBuilder sb = new StringBuilder(m.length);
        for (byte b : m) {
            sb.append((char) (x - b));
        }
        return sb.toString();
    }
}
