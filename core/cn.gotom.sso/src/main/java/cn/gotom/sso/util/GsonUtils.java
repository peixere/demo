package cn.gotom.sso.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

public class GsonUtils
{
	private static final Logger log = Logger.getLogger(GsonUtils.class);

	public static final String dateFormat = "yyyy年MM月dd日 HH时mm分ss秒";


	public static <T> String toJson(T value)
	{
		return toJson(value, null, null, null, null);
	}

	public static <T> String toJson(T value, String dateFormat)
	{
		return toJson(value, dateFormat, null, null, null);
	}

	public static <T> T toObject(Class<T> classofT, String json)
	{
		try
		{
			Gson gson = new Gson();
			return gson.fromJson(json, classofT);
		}
		catch (Exception ex)
		{
			log.warn(ex.getMessage());
			log.debug(ex.getMessage(), ex);
		}
		return null;
	}

	public static <T> T[] toArray(Class<T[]> classofT, String json)
	{
		try
		{
			Gson gson = new Gson();
			T[] array = gson.fromJson(json, classofT);
			return array;
		}
		catch (Exception ex)
		{
			log.warn(ex.getMessage());
			log.debug(ex.getMessage(), ex);
		}
		return null;
	}

	public static <T> List<T> toList(Class<T[]> classofT, String json)
	{
		List<T> list = new ArrayList<T>();
		try
		{
			Gson gson = new Gson();
			T[] array = gson.fromJson(json, classofT);
			for (T o : array)
			{
				list.add(o);
			}
			return list;
		}
		catch (Exception ex)
		{
			log.warn(ex.getMessage());
			log.debug(ex.getMessage(), ex);
		}
		return null;
	}

	/**
	 * 
	 * @param value
	 *            要系列化的对象
	 * @param dateFormat
	 *            日期格式
	 * @param registerTypeAdapterFactorys
	 *            注册适配置器类型
	 * @param excludeFields
	 *            排除字段
	 * @param excludeClasses
	 *            排除类型
	 * @return
	 */
	public static <T> String toJson(T value, String dateFormat, TypeAdapterFactory[] registerTypeAdapterFactorys, String[] excludeFields, Class<?>[] excludeClasses)
	{
		GsonBuilder gb = new GsonBuilder();
		if (dateFormat != null && dateFormat.trim().length() > 0)
		{
			gb.setDateFormat(dateFormat);
			//gb.registerTypeAdapter(Timestamp.class, new GsonTimestampTypeAdapter(dateFormat));
		}
		if (registerTypeAdapterFactorys != null)
		{
			for (TypeAdapterFactory factory : registerTypeAdapterFactorys)
			{
				gb.registerTypeAdapterFactory(factory);
			}
		}
		if ((excludeFields != null && excludeFields.length > 0) || (excludeClasses != null && excludeClasses.length > 0))
		{
			GsonExclusionStrategy strategy = new GsonExclusionStrategy(excludeFields, excludeClasses);
			gb.setExclusionStrategies(strategy);
			gb.serializeNulls();
		}
		Gson gson = gb.create();
		String json = gson.toJson(value);
		log.debug(json);
		return json;
	}

	public static void writer(HttpServletRequest request, HttpServletResponse response, String jsonString)
	{
		String encoing = request.getCharacterEncoding();
		if (CommonUtils.isEmpty(encoing))
		{
			encoing = "utf-8";
		}
		// response.setContentType("text/html;charset=" + encoing);
		response.setContentType("application/json;charset=" + encoing);
		try
		{
			response.getWriter().println(jsonString);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			log.error("输出JSON异常 " + jsonString);
		}
		finally
		{
			try
			{
				response.getWriter().flush();
				response.getWriter().close();
			}
			catch (IOException e)
			{
				log.error("输出JSON异常 " + jsonString);
			}
		}
	}
}