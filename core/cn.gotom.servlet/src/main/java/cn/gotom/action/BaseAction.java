package cn.gotom.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.gotom.converter.HibernateProxyTypeAdapter;
import cn.gotom.sso.util.GsonUtils;
import cn.gotom.vo.JsonResponse;

import com.google.gson.TypeAdapterFactory;

public abstract class BaseAction
{
	protected final Logger log = Logger.getLogger(getClass());
	protected String id;
	private int start;
	private int limit;
	protected String query;

	public int getPageNum()
	{
		int pageNum = start / getLimit() + 1;
		return pageNum;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public int getStart()
	{
		return start;
	}

	public void setStart(int start)
	{
		this.start = start;
	}

	public int getLimit()
	{
		if (limit <= 0)
		{
			limit = 20;
		}
		return limit;
	}

	public void setLimit(int limit)
	{
		this.limit = limit;
	}

	public String getQuery()
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}

	public HttpServletRequest getRequest()
	{
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse()
	{
		return ServletActionContext.getResponse();
	}

	public void toJsonResponse(Object value, boolean success)
	{
		toJsonResponse(value, success, null);
	}

	public void toJsonResponse(Object value, String... excludeFields)
	{
		toJsonResponse(value, true, null, excludeFields);
	}

	public void toJsonResponse(Object value, boolean success, String dateFormat, String... excludeFields)
	{
		JsonResponse json = new JsonResponse();
		try
		{
			json.setSuccess(success);
			json.setData(value);
		}
		catch (Exception ex)
		{
			json.setSuccess(false);
			json.setMsg(ex.getMessage());
			log.warn(ex.getMessage());
		}
		toJSON(dateFormat, json, excludeFields);
	}

	public void toJsonResponse(Object value)
	{
		toJsonResponse(value, true);
	}

	public <T> String toJSON(T value, String... excludeFields)
	{
		return this.toJSON(null, value, excludeFields);
	}

	public <T> String toJSON(String dateFormat, T value, String... excludeFields)
	{
		TypeAdapterFactory[] factorys = new TypeAdapterFactory[] { HibernateProxyTypeAdapter.FACTORY };
		String json = GsonUtils.toJson(value, dateFormat, factorys, excludeFields, null);
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		writer(request, response, json);
		return json;
	}

	public void writer(HttpServletRequest request, HttpServletResponse response, String jsonString)
	{
		GsonUtils.writer(request, response, jsonString);
	}

	public String getCurrentUsername()
	{
		return ServletActionContext.getRequest().getRemoteUser();
	}

}
