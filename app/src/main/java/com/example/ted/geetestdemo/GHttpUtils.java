package com.example.ted.geetestdemo;

import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by 谷闹年 on 2018/4/2.
 */
public class GHttpUtils {
    private static final int TIME_OUT = 15000;
    private static final String TAG = "DPHttpUtils";

    private static final String HTTPS_GET_TITLE = "https";



    /**
     * 拼接方法
     *
     * @param params
     * @return
     */
    private static StringBuffer getRequestData(Map<String, Object> params) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey()).append("=")
                        .append(entry.getValue())
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /**
     * 数据流
     *
     * @param inputStream
     * @return
     */
    private static String dealResponseResult(InputStream inputStream) {
        String resultData;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

    /**
     * X509证书
     */
    private static class TrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    /**
     * url获取
     *
     * @param urlString
     * @return
     */
    private static URL getValidateURL(String urlString) {
        try {
            return new URL(urlString);
        } catch (Exception e) {
            Log.i(TAG, "error:" + e.toString());
        }
        return null;
    }
    public static String postHttpOfMap(String getUrl, Map<String, Object> params) {
        URL url = getValidateURL(getUrl);
        try {

            HttpURLConnection mSubmitConneciton2 = null;
            HttpsURLConnection mSSLSubmitConnection2 = null;
            if (HTTPS_GET_TITLE.equals(url.getProtocol().toLowerCase())) {


                byte[] data = getRequestData(params).toString().getBytes();

                try {
                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(null, new TrustManager[]{new TrustAllManager()}, null);

                    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                        @Override
                        public boolean verify(String arg0, SSLSession arg1) {
                            return true;
                        }
                    });
                    mSSLSubmitConnection2 = (HttpsURLConnection) url.openConnection();
                    mSSLSubmitConnection2.setConnectTimeout(TIME_OUT);
                    mSSLSubmitConnection2.setReadTimeout(TIME_OUT);
                    mSSLSubmitConnection2.setDoInput(true);
                    mSSLSubmitConnection2.setDoOutput(true);
                    mSSLSubmitConnection2.setRequestMethod("POST");
                    mSSLSubmitConnection2.setUseCaches(false);

                    mSSLSubmitConnection2.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    mSSLSubmitConnection2.setRequestProperty("Content-Length",
                            String.valueOf(data.length));
                    mSSLSubmitConnection2.connect();
                    OutputStream outputStream = mSSLSubmitConnection2.getOutputStream();
                    outputStream.write(data);


                    outputStream.flush();
                    outputStream.close();
                    int responseCode = mSSLSubmitConnection2.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        InputStream inptStream = mSSLSubmitConnection2.getInputStream();
                        return dealResponseResult(inptStream);
                    } else {
                        Log.i(TAG, "url:" + getUrl + ",responseCode:" + responseCode);
                        return "";
                    }

                } catch (Exception e) {
                    Log.i(TAG, "url:" + getUrl + ",error:" + e.toString());
                    return "";
                } finally {
                    if (mSSLSubmitConnection2 != null) {
                        mSSLSubmitConnection2.disconnect();
                    }
                }

            } else {


                byte[] data = getRequestData(params).toString().getBytes();

                try {
                    mSubmitConneciton2 = (HttpURLConnection) url.openConnection();


                    mSubmitConneciton2.setConnectTimeout(TIME_OUT);
                    mSubmitConneciton2.setReadTimeout(TIME_OUT);
                    mSubmitConneciton2.setDoInput(true);
                    mSubmitConneciton2.setDoOutput(false);
                    mSubmitConneciton2.setRequestMethod("POST");
                    mSubmitConneciton2.setUseCaches(false);


                    mSubmitConneciton2.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    mSubmitConneciton2.setRequestProperty("Content-Length",
                            String.valueOf(data.length));
                    mSubmitConneciton2.connect();
                    OutputStream outputStream = mSubmitConneciton2.getOutputStream();
                    outputStream.write(data);

                    outputStream.flush();
                    outputStream.close();
                    int responseCode = mSubmitConneciton2.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inptStream = mSubmitConneciton2.getInputStream();
                        return dealResponseResult(inptStream);
                    } else {
                        Log.i(TAG, "url:" + getUrl + ",responseCode:" + responseCode);
                        return "";
                    }

                } catch (Exception e) {
                    Log.i(TAG, "url:" + getUrl + ",error:" + e.toString());
                    return "";
                } finally {
                    if (mSubmitConneciton2 != null) {
                        mSubmitConneciton2.disconnect();
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "error:" + e.toString());
            return "";
        }
    }
}
