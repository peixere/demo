package cn.gotom.sso.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import cn.gotom.util.ClassLoaderUtils;

public class HttpHelper
{
	public static void main(String[] args) throws Exception
	{

		HttpGet httpget = new HttpGet("https://localhost:8443/");
		try
		{
			String store = ClassLoaderUtils.getPath(HttpHelper.class) + "keystore/server.keystore";
			HttpResponse response = HttpHelper.getClient(store).execute(httpget);
			System.out.println(HttpHelper.getContent(response, "utf-8"));
		}
		finally
		{
			httpget.releaseConnection();
		}

		String s = getResponse("https://localhost:8443/", "utf-8");
		System.out.println(s);
	}

	private static CloseableHttpClient httpClient;

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

	public static synchronized CloseableHttpClient getClient(String keyStore) throws Exception
	{
		if (httpClient != null)
		{
			return httpClient;
		}
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream instream = null;
		try
		{
			if (keyStore != null && keyStore.trim().length() > 0)
			{
				instream = new FileInputStream(new File(keyStore));
				trustStore.load(instream, "888888".toCharArray());
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
		SSLContext sslcontext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
		// Allow TLSv1 protocol only
		// SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1", }, null, SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER);
		httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		return httpClient;
	}

}