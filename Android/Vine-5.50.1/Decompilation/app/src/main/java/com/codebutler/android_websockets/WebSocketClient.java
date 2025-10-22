package com.codebutler.android_websockets;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.codebutler.android_websockets.HybiParser;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.BasicNameValuePair;

/* loaded from: classes.dex */
public class WebSocketClient {
    private static TrustManager[] sTrustManagers;
    private volatile int mConnectionFlag;
    private List<BasicNameValuePair> mExtraHeaders;
    private Handler mHandler;
    private Listener mListener;
    private Socket mSocket;
    private Thread mThread;
    private URI mURI;
    private final Object mSendLock = new Object();
    private HybiParser mParser = new HybiParser(this);
    private HandlerThread mHandlerThread = new HandlerThread("websocket-thread");

    public interface Listener {
        void onConnect();

        void onDisconnect(int i, String str);

        void onError(Exception exc);

        void onMessage(String str);

        void onMessage(byte[] bArr);
    }

    public WebSocketClient(URI uri, Listener listener, List<BasicNameValuePair> extraHeaders) {
        this.mURI = uri;
        this.mListener = listener;
        this.mExtraHeaders = extraHeaders;
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper());
        this.mConnectionFlag = 0;
    }

    public boolean isConnected() {
        return (this.mSocket == null || (this.mConnectionFlag & 2) == 0) ? false : true;
    }

    public boolean isConnecting() {
        return (this.mSocket == null || (this.mConnectionFlag & 1) == 0) ? false : true;
    }

    public Listener getListener() {
        return this.mListener;
    }

    public void connect() {
        if (this.mThread == null || !this.mThread.isAlive()) {
            this.mThread = new Thread(new Runnable() { // from class: com.codebutler.android_websockets.WebSocketClient.1
                @Override // java.lang.Runnable
                public void run() throws HttpException, HttpResponseException {
                    try {
                        WebSocketClient.this.mConnectionFlag = 1;
                        String secret = WebSocketClient.this.createSecret();
                        String path = TextUtils.isEmpty(WebSocketClient.this.mURI.getPath()) ? "/" : WebSocketClient.this.mURI.getPath();
                        if (!TextUtils.isEmpty(WebSocketClient.this.mURI.getQuery())) {
                            path = path + "?" + WebSocketClient.this.mURI.getQuery();
                        }
                        URI origin = new URI("https", "//" + WebSocketClient.this.mURI.getHost(), null);
                        SocketFactory factory = WebSocketClient.this.getSSLSocketFactory();
                        WebSocketClient.this.mSocket = factory.createSocket(WebSocketClient.this.mURI.getHost(), 443);
                        PrintWriter out = new PrintWriter(WebSocketClient.this.mSocket.getOutputStream());
                        out.print("GET " + path + " HTTP/1.1\r\n");
                        out.print("Upgrade: websocket\r\n");
                        out.print("Connection: Upgrade\r\n");
                        out.print("Host: " + WebSocketClient.this.mURI.getHost() + "\r\n");
                        out.print("Origin: " + origin.toString() + "\r\n");
                        out.print("Sec-WebSocket-Key: " + secret + "\r\n");
                        out.print("Sec-WebSocket-Version: 13\r\n");
                        if (WebSocketClient.this.mExtraHeaders != null) {
                            for (NameValuePair pair : WebSocketClient.this.mExtraHeaders) {
                                out.print(String.format("%s: %s\r\n", pair.getName(), pair.getValue()));
                            }
                        }
                        out.print("\r\n");
                        out.flush();
                        HybiParser.HappyDataInputStream stream = new HybiParser.HappyDataInputStream(WebSocketClient.this.mSocket.getInputStream());
                        StatusLine statusLine = WebSocketClient.this.parseStatusLine(WebSocketClient.this.readLine(stream));
                        if (statusLine == null) {
                            throw new HttpException("Received no reply from server.");
                        }
                        if (statusLine.getStatusCode() != 101) {
                            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                        }
                        boolean validated = false;
                        while (true) {
                            String line = WebSocketClient.this.readLine(stream);
                            if (!TextUtils.isEmpty(line)) {
                                Header header = WebSocketClient.this.parseHeader(line);
                                if (header.getName().equals("Sec-WebSocket-Accept")) {
                                    String expected = WebSocketClient.this.createSecretValidation(secret);
                                    String actual = header.getValue().trim();
                                    if (!expected.equals(actual)) {
                                        throw new HttpException("Bad Sec-WebSocket-Accept header value.");
                                    }
                                    validated = true;
                                }
                            } else {
                                if (validated) {
                                    WebSocketClient.this.mConnectionFlag = 2;
                                    WebSocketClient.this.mListener.onConnect();
                                    WebSocketClient.this.mParser.start(stream);
                                    return;
                                }
                                throw new HttpException("No Sec-WebSocket-Accept header.");
                            }
                        }
                    } catch (EOFException ex) {
                        Log.d("WebSocketClient", "WebSocket EOF!", ex);
                        WebSocketClient.this.mListener.onDisconnect(0, "EOF");
                        WebSocketClient.this.mConnectionFlag = 0;
                    } catch (SSLException ex2) {
                        Log.d("WebSocketClient", "Websocket SSL error!", ex2);
                        WebSocketClient.this.mListener.onDisconnect(0, "SSL");
                        WebSocketClient.this.mConnectionFlag = 0;
                    } catch (Exception ex3) {
                        WebSocketClient.this.mListener.onError(ex3);
                        WebSocketClient.this.mConnectionFlag = 0;
                    }
                }
            });
            this.mThread.start();
        }
    }

    public void disconnect() {
        this.mConnectionFlag = 0;
        if (this.mSocket != null) {
            this.mHandler.post(new Runnable() { // from class: com.codebutler.android_websockets.WebSocketClient.2
                @Override // java.lang.Runnable
                public void run() throws IOException {
                    try {
                        WebSocketClient.this.mSocket.close();
                        Log.d("RTC", "WebSocket disconnected");
                        WebSocketClient.this.mSocket = null;
                    } catch (IOException ex) {
                        Log.d("WebSocketClient", "Error while disconnecting", ex);
                        WebSocketClient.this.mListener.onError(ex);
                    }
                }
            });
        }
    }

    public void send(String data) {
        sendFrame(this.mParser.frame(data));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StatusLine parseStatusLine(String line) {
        if (TextUtils.isEmpty(line)) {
            return null;
        }
        return BasicLineParser.parseStatusLine(line, new BasicLineParser());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Header parseHeader(String line) {
        return BasicLineParser.parseHeader(line, new BasicLineParser());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String readLine(HybiParser.HappyDataInputStream reader) throws IOException {
        int readChar = reader.read();
        if (readChar == -1) {
            return null;
        }
        StringBuilder string = new StringBuilder("");
        while (readChar != 10) {
            if (readChar != 13) {
                string.append((char) readChar);
            }
            readChar = reader.read();
            if (readChar == -1) {
                return null;
            }
        }
        return string.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String createSecret() {
        byte[] nonce = new byte[16];
        for (int i = 0; i < 16; i++) {
            nonce[i] = (byte) (Math.random() * 256.0d);
        }
        return Base64.encodeToString(nonce, 0).trim();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String createSecretValidation(String secret) throws NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update((secret + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes());
            return Base64.encodeToString(md.digest(), 0).trim();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    void sendFrame(final byte[] frame) {
        this.mHandler.post(new Runnable() { // from class: com.codebutler.android_websockets.WebSocketClient.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    synchronized (WebSocketClient.this.mSendLock) {
                        if (WebSocketClient.this.mSocket != null) {
                            OutputStream outputStream = WebSocketClient.this.mSocket.getOutputStream();
                            outputStream.write(frame);
                            outputStream.flush();
                        } else {
                            throw new IOException("Socket not connected while trying to write: " + Arrays.toString(frame));
                        }
                    }
                } catch (IOException e) {
                    WebSocketClient.this.mListener.onError(e);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SSLSocketFactory getSSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, sTrustManagers, null);
        return context.getSocketFactory();
    }
}
