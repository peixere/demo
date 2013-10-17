package cn.gotom.auth.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class AuthenticatedClient
{

	public AuthenticatedClient()
	{

	}

	public Authenticated auth(String authServiceUrl, Authenticated request)
	{
		Authenticated authenticated = new Authenticated();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try
		{
			StringEntity entity = new StringEntity(request.toString());
			HttpPost httppost = new HttpPost(authServiceUrl);
			httppost.setEntity(entity);
			HttpResponse response = httpclient.execute(httppost);
			String jsonString = getStringFromHttp(response.getEntity());
			authenticated = fromJsonString(jsonString);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return authenticated;
	}

	public static Authenticated fromJsonString(String jsonString)
	{
		Authenticated authenticated;
		JSON json = JSONSerializer.toJSON(jsonString);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setRootClass(Authenticated.class);
		authenticated = (Authenticated) JSONSerializer.toJava(json, jsonConfig);
		return authenticated;
	}

	public static String convertStreamToString(InputStream is) throws Exception
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf-8"));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null)
		{
			buffer.append(line).append("\n");
		}
		return buffer.toString();
	}

	public static String getStringFromHttp(HttpEntity entity)
	{
		StringBuffer buffer = new StringBuffer();
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
			String temp = null;
			while ((temp = reader.readLine()) != null)
			{
				buffer.append(temp);
			}
		}
		catch (IllegalStateException e)
		{
		}
		catch (IOException e)
		{
		}
		return buffer.toString();
	}

	public static void main(String[] args)
	{
		int i = 1000;
		while (i-- > 0)
		{
			Authenticated request = new Authenticated();
			request.setAppCode("appCode");
			request.setUsername("admin");
			request.setUrl("http://localhost:8080/authService.do");
			AuthenticatedClient ac = new AuthenticatedClient();
			request = ac.auth("http://localhost:8080/AuthenticatedService", request);
			System.out.println(request);
		}
	}
}
