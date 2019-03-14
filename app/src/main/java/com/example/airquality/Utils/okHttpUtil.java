package com.example.airquality.Utils;

import android.os.Build;
import android.text.TextUtils;
import com.example.airquality.View.MainApplication;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;

public class okHttpUtil {
    private static final String SSL_PROTOCOL = "TLS";
    private static final String Certificate_FORMAT = "X.509";
    private static final String KEY_STORE_PASSWORD = "password";

    static public OkHttpClient getTrustClient(String certificate) {
        if (TextUtils.isEmpty(certificate)) {
            return new OkHttpClient();
        }

        X509TrustManager x509TrustManager;
        SSLSocketFactory sslSocketFactory;
        try {
            x509TrustManager = trustManagerForCertificates(MainApplication.getAppContext().getAssets().open(certificate));
            SSLContext sslContext = SSLContext.getInstance(SSL_PROTOCOL);
            sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
            sslSocketFactory = new SSLSocketFactoryCompatible(sslContext.getSocketFactory());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, x509TrustManager).build();
    }

    private static X509TrustManager trustManagerForCertificates(InputStream in) throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(Certificate_FORMAT);
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }
        char[] password = KEY_STORE_PASSWORD.toCharArray();
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private static class SSLSocketFactoryCompatible extends SSLSocketFactory {
        private static final String[] TLS_V12_ONLY = {"TLSv1.2"};

        private final SSLSocketFactory delegate;

        public SSLSocketFactoryCompatible(SSLSocketFactory delegate) {
            if (delegate == null) {
                throw new NullPointerException();
            }
            this.delegate = delegate;
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return delegate.getDefaultCipherSuites();
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return delegate.getSupportedCipherSuites();
        }

        private Socket enableTls12(Socket socket) {
            if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 20) {
                if (socket instanceof SSLSocket) {
                    ((SSLSocket) socket).setEnabledProtocols(TLS_V12_ONLY);
                }
            }
            return socket;
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
            return enableTls12(delegate.createSocket(s, host, port, autoClose));
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException {
            return enableTls12(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
            return enableTls12(delegate.createSocket(host, port, localHost, localPort));
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            return enableTls12(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            return enableTls12(delegate.createSocket(address, port, localAddress, localPort));
        }
    }
}
