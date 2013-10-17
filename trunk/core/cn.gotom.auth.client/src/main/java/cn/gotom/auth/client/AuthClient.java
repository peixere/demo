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

public class AuthClient
{

	public AuthClient()
	{

	}

	public Authenticated auth(String authServiceUrl, Authenticated request)
	{
		Authenticated authenticated = new Authenticated();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try
		{
			JSON json = JSONSerializer.toJSON(request);
			StringEntity entity = new StringEntity(json.toString());
			HttpPost httppost = new HttpPost(authServiceUrl);
			httppost.setEntity(entity);
			HttpResponse response = httpclient.execute(httppost);
			String jsonString = getStringFromHttp(response.getEntity());
			json = JSONSerializer.toJSON(jsonString);
			// authenticated = (Authenticated) JSONSerializer.toJava(json);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(Authenticated.class);
			authenticated = (Authenticated) JSONSerializer.toJava(json, jsonConfig);
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
		Authenticated request = new Authenticated();
		request.setAppCode("appCode");
		request.setUsername("admin");
		request.setUrl("http://localhost:8080/authService.do");
		AuthClient ac = new AuthClient();
		request = ac.auth("http://localhost:8080/authServiceServlet", request);
	}
}
