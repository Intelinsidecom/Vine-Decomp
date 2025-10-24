package co.vine.android.network.apache;

import android.content.Context;
import co.vine.android.network.ssl.VineSSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.SSLSocket;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/* loaded from: classes.dex */
public class PinningSSLSocketFactory extends SSLSocketFactory {
    private final VineSSLSocketFactory mSSLSocketFactory;

    public PinningSSLSocketFactory(Context context) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        super(null);
        this.mSSLSocketFactory = new VineSSLSocketFactory(context);
    }

    @Override // org.apache.http.conn.ssl.SSLSocketFactory, org.apache.http.conn.scheme.SocketFactory
    public Socket createSocket() throws IOException {
        return this.mSSLSocketFactory.createSocket();
    }

    @Override // org.apache.http.conn.ssl.SSLSocketFactory, org.apache.http.conn.scheme.SocketFactory
    public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException {
        SSLSocket sslSock = (SSLSocket) (sock != null ? sock : createSocket());
        if (localAddress != null || localPort > 0) {
            if (localPort < 0) {
                localPort = 0;
            }
            sslSock.bind(new InetSocketAddress(localAddress, localPort));
        }
        int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
        int soTimeout = HttpConnectionParams.getSoTimeout(params);
        InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
        sslSock.connect(remoteAddress, connTimeout);
        sslSock.setSoTimeout(soTimeout);
        try {
            SSLSocketFactory.STRICT_HOSTNAME_VERIFIER.verify(host, sslSock);
            return sslSock;
        } catch (IOException iox) {
            try {
                sslSock.close();
            } catch (Exception e) {
            }
            throw iox;
        }
    }

    @Override // org.apache.http.conn.ssl.SSLSocketFactory, org.apache.http.conn.scheme.LayeredSocketFactory
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return this.mSSLSocketFactory.createSocket(socket, host, port, autoClose);
    }

    @Override // org.apache.http.conn.ssl.SSLSocketFactory
    public void setHostnameVerifier(X509HostnameVerifier hostnameVerifier) {
        this.mSSLSocketFactory.setHostnameVerifier();
    }

    @Override // org.apache.http.conn.ssl.SSLSocketFactory
    public X509HostnameVerifier getHostnameVerifier() {
        return SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
    }
}
