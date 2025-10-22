package com.codebutler.android_websockets;

import android.util.Log;
import com.flurry.android.Constants;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class HybiParser {
    private WebSocketClient mClient;
    private boolean mFinal;
    private int mLength;
    private int mLengthSize;
    private boolean mMasked;
    private int mMode;
    private int mOpcode;
    private int mStage;
    private static final List<Integer> OPCODES = Arrays.asList(0, 1, 2, 8, 9, 10);
    private static final List<Integer> FRAGMENTED_OPCODES = Arrays.asList(0, 1, 2);
    private boolean mMasking = true;
    private byte[] mMask = new byte[0];
    private byte[] mPayload = new byte[0];
    private boolean mClosed = false;
    private ByteArrayOutputStream mBuffer = new ByteArrayOutputStream();

    public HybiParser(WebSocketClient client) {
        this.mClient = client;
    }

    private static byte[] mask(byte[] payload, byte[] mask, int offset) {
        if (mask.length != 0) {
            for (int i = 0; i < payload.length - offset; i++) {
                payload[offset + i] = (byte) (payload[offset + i] ^ mask[i % 4]);
            }
        }
        return payload;
    }

    public void start(HappyDataInputStream stream) throws IOException {
        while (stream.available() != -1) {
            switch (this.mStage) {
                case 0:
                    parseOpcode(stream.readByte());
                    break;
                case 1:
                    parseLength(stream.readByte());
                    break;
                case 2:
                    parseExtendedLength(stream.readBytes(this.mLengthSize));
                    break;
                case 3:
                    this.mMask = stream.readBytes(4);
                    this.mStage = 4;
                    break;
                case 4:
                    this.mPayload = stream.readBytes(this.mLength);
                    emitFrame();
                    this.mStage = 0;
                    break;
            }
        }
        this.mClient.getListener().onDisconnect(0, "EOF");
    }

    private void parseOpcode(byte data) throws ProtocolError {
        boolean rsv1 = (data & 64) == 64;
        boolean rsv2 = (data & 32) == 32;
        boolean rsv3 = (data & 16) == 16;
        if (rsv1 || rsv2 || rsv3) {
            throw new ProtocolError("RSV not zero");
        }
        this.mFinal = (data & 128) == 128;
        this.mOpcode = data & 15;
        this.mMask = new byte[0];
        this.mPayload = new byte[0];
        if (!OPCODES.contains(Integer.valueOf(this.mOpcode))) {
            throw new ProtocolError("Bad opcode");
        }
        if (!FRAGMENTED_OPCODES.contains(Integer.valueOf(this.mOpcode)) && !this.mFinal) {
            throw new ProtocolError("Expected non-final packet");
        }
        this.mStage = 1;
    }

    private void parseLength(byte data) {
        this.mMasked = (data & 128) == 128;
        this.mLength = data & 127;
        if (this.mLength >= 0 && this.mLength <= 125) {
            this.mStage = this.mMasked ? 3 : 4;
        } else {
            this.mLengthSize = this.mLength == 126 ? 2 : 8;
            this.mStage = 2;
        }
    }

    private void parseExtendedLength(byte[] buffer) throws ProtocolError {
        this.mLength = getInteger(buffer);
        this.mStage = this.mMasked ? 3 : 4;
    }

    public byte[] frame(String data) {
        return frame(data, 1, -1);
    }

    private byte[] frame(byte[] data, int opcode, int errorCode) {
        return frame((Object) data, opcode, errorCode);
    }

    private byte[] frame(String data, int opcode, int errorCode) {
        return frame((Object) data, opcode, errorCode);
    }

    private byte[] frame(Object data, int opcode, int errorCode) {
        int header;
        if (this.mClosed) {
            return null;
        }
        Log.d("HybiParser", "Creating frame for: " + data + " op: " + opcode + " err: " + errorCode);
        byte[] buffer = data instanceof String ? decode((String) data) : (byte[]) data;
        int insert = errorCode > 0 ? 2 : 0;
        int length = buffer.length + insert;
        if (length <= 125) {
            header = 2;
        } else {
            header = length <= 65535 ? 4 : 10;
        }
        int offset = header + (this.mMasking ? 4 : 0);
        int masked = this.mMasking ? 128 : 0;
        byte[] frame = new byte[length + offset];
        frame[0] = (byte) (((byte) opcode) | (-128));
        if (length <= 125) {
            frame[1] = (byte) (masked | length);
        } else if (length <= 65535) {
            frame[1] = (byte) (masked | 126);
            frame[2] = (byte) Math.floor(length / 256);
            frame[3] = (byte) (length & 255);
        } else {
            frame[1] = (byte) (masked | 127);
            frame[2] = (byte) (((int) Math.floor(length / Math.pow(2.0d, 56.0d))) & 255);
            frame[3] = (byte) (((int) Math.floor(length / Math.pow(2.0d, 48.0d))) & 255);
            frame[4] = (byte) (((int) Math.floor(length / Math.pow(2.0d, 40.0d))) & 255);
            frame[5] = (byte) (((int) Math.floor(length / Math.pow(2.0d, 32.0d))) & 255);
            frame[6] = (byte) (((int) Math.floor(length / Math.pow(2.0d, 24.0d))) & 255);
            frame[7] = (byte) (((int) Math.floor(length / Math.pow(2.0d, 16.0d))) & 255);
            frame[8] = (byte) (((int) Math.floor(length / Math.pow(2.0d, 8.0d))) & 255);
            frame[9] = (byte) (length & 255);
        }
        if (errorCode > 0) {
            frame[offset] = (byte) (((int) Math.floor(errorCode / 256)) & 255);
            frame[offset + 1] = (byte) (errorCode & 255);
        }
        System.arraycopy(buffer, 0, frame, offset + insert, buffer.length);
        if (this.mMasking) {
            byte[] mask = {(byte) Math.floor(Math.random() * 256.0d), (byte) Math.floor(Math.random() * 256.0d), (byte) Math.floor(Math.random() * 256.0d), (byte) Math.floor(Math.random() * 256.0d)};
            System.arraycopy(mask, 0, frame, header, mask.length);
            mask(frame, mask, offset);
            return frame;
        }
        return frame;
    }

    private void emitFrame() throws IOException {
        byte[] payload = mask(this.mPayload, this.mMask, 0);
        int opcode = this.mOpcode;
        if (opcode == 0) {
            if (this.mMode == 0) {
                throw new ProtocolError("Mode was not set.");
            }
            this.mBuffer.write(payload);
            if (this.mFinal) {
                byte[] message = this.mBuffer.toByteArray();
                if (this.mMode == 1) {
                    this.mClient.getListener().onMessage(encode(message));
                } else {
                    this.mClient.getListener().onMessage(message);
                }
                reset();
                return;
            }
            return;
        }
        if (opcode == 1) {
            if (this.mFinal) {
                String messageText = encode(payload);
                this.mClient.getListener().onMessage(messageText);
                return;
            } else {
                this.mMode = 1;
                this.mBuffer.write(payload);
                return;
            }
        }
        if (opcode == 2) {
            if (this.mFinal) {
                this.mClient.getListener().onMessage(payload);
                return;
            } else {
                this.mMode = 2;
                this.mBuffer.write(payload);
                return;
            }
        }
        if (opcode == 8) {
            int code = payload.length >= 2 ? (payload[0] * Constants.FEMALE) + payload[1] : 0;
            String reason = payload.length > 2 ? encode(slice(payload, 2)) : null;
            Log.d("HybiParser", "Got close op! " + code + " " + reason);
            this.mClient.getListener().onDisconnect(code, reason);
            return;
        }
        if (opcode == 9) {
            if (payload.length > 125) {
                throw new ProtocolError("Ping payload too large");
            }
            Log.d("HybiParser", "Sending pong!!");
            this.mClient.sendFrame(frame(payload, 10, -1));
            return;
        }
        if (opcode == 10) {
            Log.d("HybiParser", "Got pong! " + encode(payload));
        }
    }

    private void reset() {
        this.mMode = 0;
        this.mBuffer.reset();
    }

    private String encode(byte[] buffer) {
        try {
            return new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] decode(String string) {
        try {
            return string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private int getInteger(byte[] bytes) throws ProtocolError {
        long i = byteArrayToLong(bytes, 0, bytes.length);
        if (i < 0 || i > 2147483647L) {
            throw new ProtocolError("Bad integer: " + i);
        }
        return (int) i;
    }

    private byte[] slice(byte[] array, int start) {
        return Arrays.copyOfRange(array, start, array.length);
    }

    public static class ProtocolError extends IOException {
        public ProtocolError(String detailMessage) {
            super(detailMessage);
        }
    }

    private static long byteArrayToLong(byte[] b, int offset, int length) {
        if (b.length < length) {
            throw new IllegalArgumentException("length must be less than or equal to b.length");
        }
        long value = 0;
        for (int i = 0; i < length; i++) {
            int shift = ((length - 1) - i) * 8;
            value += (b[i + offset] & 255) << shift;
        }
        return value;
    }

    public static class HappyDataInputStream extends DataInputStream {
        public HappyDataInputStream(InputStream in) {
            super(in);
        }

        public byte[] readBytes(int length) throws IOException {
            byte[] buffer = new byte[length];
            readFully(buffer);
            return buffer;
        }
    }
}
