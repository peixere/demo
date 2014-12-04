package cn.gotom.sso.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import cn.gotom.util.ClassLoaderUtils;

public class HttpHelper
{
	public static void main(String[] args) throws Exception
	{

		HttpGet httpget = new HttpGet("https://localhost:8443/");
		try
		{
			String s = getResponse("https://localhost/", "utf-8");
			System.out.println(s);
			String store = ClassLoaderUtils.getPath(HttpHelper.class) + "keystore/gotom.key.p12";
			HttpResponse response = HttpHelper.getClient(store).execute(httpget);
			System.out.println(HttpHelper.getContent(response, "utf-8"));
		}
		finally
		{
			httpget.releaseConnection();
		}
	}

	private HttpHelper()
	{
	}

	public static String getResponse(String url, String encoding) throws Exception
	{
		HttpGet httpget = new HttpGet(url);
		try
		{
			HttpResponse response = HttpHelper.getClient().execute(httpget);
			return HttpHelper.getContent(response, encoding);
		}
		finally
		{
			httpget.releaseConnection();
		}
	}

	public static String getContent(HttpResponse response, String encoding) throws IOException, UnsupportedEncodingException
	{
		if (response.getEntity().getContentEncoding() != null)
		{
			encoding = response.getEntity().getContentEncoding().getName();
		}
		final BufferedReader in;
		if (CommonUtils.isEmpty(encoding))
		{
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		}
		else
		{
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), encoding));
		}
		String line;
		final StringBuilder stringBuffer = new StringBuilder(255);
		while ((line = in.readLine()) != null)
		{
			stringBuffer.append(line);
			stringBuffer.append("\n");
		}
		return stringBuffer.toString();
	}

	public static CloseableHttpClient getClient() throws Exception
	{
		return getClient(null);
	}

	public static synchronized CloseableHttpClient getClient(String keyStoreFile) throws Exception
	{
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		SSLContextBuilder sslContextBuilder = SSLContexts.custom();
		sslContextBuilder.useTLS();
		sslContextBuilder.loadTrustMaterial(trustStore, new TrustSelfSignedStrategy());

		FileInputStream instream = null;
		try
		{
			if (keyStoreFile != null && keyStoreFile.trim().length() > 0)
			{
				KeyStore keyStore = KeyStore.getInstance("PKCS12");
				instream = new FileInputStream(new File(keyStoreFile));
				keyStore.load(instream, "888888".toCharArray());
				sslContextBuilder.loadKeyMaterial(keyStore, "888888".toCharArray());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (instream != null)
				instream.close();
		}
		// new String[] { "TLSv1", }
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContextBuilder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		HttpClientBuilder builder = HttpClients.custom();
		builder.setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		builder.setSSLSocketFactory(sslsf);
		return builder.build();
	}

}