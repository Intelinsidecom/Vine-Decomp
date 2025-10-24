package co.vine.android.recorder.audio;

/* loaded from: classes.dex */
public class AudioArrays {

    public enum AudioArrayType {
        BYTE,
        SHORT
    }

    public static AudioArray wrap(Object audioData) {
        if (audioData instanceof byte[]) {
            return new AudioByteArray((byte[]) audioData);
        }
        if (audioData instanceof short[]) {
            return new AudioShortArray((short[]) audioData);
        }
        throw new IllegalArgumentException("type invalid: " + audioData);
    }

    public static AudioArrayType typeOf(AudioArray audioArray) {
        if (audioArray instanceof AudioByteArray) {
            return AudioArrayType.BYTE;
        }
        if (audioArray instanceof AudioShortArray) {
            return AudioArrayType.SHORT;
        }
        throw new IllegalArgumentException("type unknown: " + audioArray);
    }

    public static AudioArray newInstance(int audioRunnableSampleSize, AudioArrayType type) {
        if (type == AudioArrayType.SHORT) {
            return new AudioShortArray(audioRunnableSampleSize);
        }
        if (type == AudioArrayType.BYTE) {
            return new AudioByteArray(audioRunnableSampleSize);
        }
        throw new IllegalArgumentException("type invalid: " + type);
    }
}
