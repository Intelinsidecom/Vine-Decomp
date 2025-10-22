package co.vine.android.network.ssl;

import android.content.Context;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/* loaded from: classes.dex */
public class VineSSLSocketFactory extends SSLSocketFactory {
    private static final Set<String> PINNING_HOSTS = new HashSet();
    private final SSLSocketFactory mPinningSocketFactory;
    private final SSLSocketFactory mSystemSocketFactory;

    static {
        PINNING_HOSTS.add("api.twitter.com");
        PINNING_HOSTS.add("api.vineapp.com");
        PINNING_HOSTS.add("media.vineapp.com");
        PINNING_HOSTS.add("vine.co");
        PINNING_HOSTS.add("rtc.vineapp.com");
    }

    public VineSSLSocketFactory(Context context) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        SystemKeyStore keyStore = SystemKeyStore.getInstance(context);
        SSLContext pinningSslContext = SSLContext.getInstance("TLS");
        SSLContext systemSslContext = SSLContext.getInstance("TLS");
        TrustManager[] pinningTrustManagers = initializePinningTrustManagers(keyStore);
        TrustManager[] systemTrustManagers = initializeSystemTrustManagers(keyStore);
        pinningSslContext.init(null, pinningTrustManagers, null);
        systemSslContext.init(null, systemTrustManagers, null);
        this.mPinningSocketFactory = pinningSslContext.getSocketFactory();
        this.mSystemSocketFactory = systemSslContext.getSocketFactory();
    }

    public TrustManager[] initializeSystemTrustManagers(SystemKeyStore keyStore) throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(keyStore.getTrustStore());
        return tmf.getTrustManagers();
    }

    public TrustManager[] initializePinningTrustManagers(SystemKeyStore keyStore) {
        TrustManager[] trustManagers = {new PinningTrustManager(keyStore, VinePins.PINS)};
        return trustManagers;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket() throws IOException {
        return this.mSystemSocketFactory.createSocket();
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getSupportedCipherSuites() {
        return new String[0];
    }

    private void verify(String host, SSLSocket sslSocket) throws IOException {
        org.apache.http.conn.ssl.SSLSocketFactory.STRICT_HOSTNAME_VERIFIER.verify(host, sslSocket);
    }

    private int verifyPost(int port) {
        if (port == -1) {
            return 443;
        }
        return port;
    }

    private SSLSocketFactory getFactoryForHost(String host) {
        return PINNING_HOSTS.contains(host) ? this.mPinningSocketFactory : this.mSystemSocketFactory;
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        SSLSocketFactory socketFactory = getFactoryForHost(host);
        SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(socket, host, verifyPost(port), autoClose);
        verify(host, sslSocket);
        return sslSocket;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String host, int port) throws IOException {
        SSLSocketFactory socketFactory = getFactoryForHost(host);
        SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(host, verifyPost(port));
        verify(host, sslSocket);
        return sslSocket;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        SSLSocketFactory socketFactory = getFactoryForHost(host);
        SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(host, verifyPost(port), localHost, localPort);
        verify(host, sslSocket);
        return sslSocket;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return this.mSystemSocketFactory.createSocket(host, port);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return this.mSystemSocketFactory.createSocket(address, port, localAddress, localPort);
    }

    public void setHostnameVerifier() {
        throw new IllegalArgumentException("Only strict hostname verification (default)  is supported!");
    }
}
