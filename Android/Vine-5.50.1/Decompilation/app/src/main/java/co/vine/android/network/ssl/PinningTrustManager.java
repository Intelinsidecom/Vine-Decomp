package co.vine.android.network.ssl;

import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/* loaded from: classes.dex */
public class PinningTrustManager implements X509TrustManager {
    private final SystemKeyStore mSystemKeyStore;
    private final TrustManager[] mSystemTrustManagers;
    private final List<byte[]> mPins = new LinkedList();
    private final Set<X509Certificate> mCache = Collections.synchronizedSet(new HashSet());

    public PinningTrustManager(SystemKeyStore keyStore, String[] pins) {
        this.mSystemTrustManagers = initializeSystemTrustManagers(keyStore);
        this.mSystemKeyStore = keyStore;
        for (String pin : pins) {
            this.mPins.add(hexStringToByteArray(pin));
        }
    }

    private TrustManager[] initializeSystemTrustManagers(SystemKeyStore keyStore) throws NoSuchAlgorithmException, KeyStoreException {
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore.getTrustStore());
            return tmf.getTrustManagers();
        } catch (KeyStoreException e) {
            throw new AssertionError(e);
        } catch (NoSuchAlgorithmException nsae) {
            throw new AssertionError(nsae);
        }
    }

    private boolean isValidPin(X509Certificate certificate) throws NoSuchAlgorithmException, CertificateException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            byte[] spki = certificate.getPublicKey().getEncoded();
            byte[] pin = digest.digest(spki);
            for (byte[] validPin : this.mPins) {
                if (Arrays.equals(validPin, pin)) {
                    return true;
                }
            }
            return false;
        } catch (NoSuchAlgorithmException nsae) {
            throw new CertificateException(nsae);
        }
    }

    private void checkSystemTrust(X509Certificate[] chain, String authType) throws CertificateException {
        for (TrustManager systemTrustManager : this.mSystemTrustManagers) {
            ((X509TrustManager) systemTrustManager).checkServerTrusted(chain, authType);
        }
    }

    private void checkPinTrust(X509Certificate[] chain) throws CertificateException {
        X509Certificate[] cleanChain = CertificateChainCleaner.getCleanChain(chain, this.mSystemKeyStore);
        for (X509Certificate certificate : cleanChain) {
            if (isValidPin(certificate)) {
                return;
            }
        }
        throw new CertificateException("No valid pins found in chain!");
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        throw new CertificateException("Client certificates not supported!");
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (!this.mCache.contains(chain[0])) {
            checkSystemTrust(chain, authType);
            checkPinTrust(chain);
            this.mCache.add(chain[0]);
        }
    }

    @Override // javax.net.ssl.X509TrustManager
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
