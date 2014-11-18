package cn.gotom.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import cn.gotom.pojos.Custom;
import cn.gotom.pojos.User;
import cn.gotom.sso.util.GsonUtils;
import cn.gotom.util.StringUtils;
import cn.gotom.vo.JsonResponse;

import com.google.gson.TypeAdapterFactory;

public abstract class AbsPortalAction
{
	protected final Logger log = Logger.getLogger(getClass());
	protected String id;
	private int start;
	private int limit;

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

	public HttpServletRequest getRequest()
	{
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse()
	{
		return ServletActionContext.getResponse();
	}

	public String getCurrentCustomId()
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		String customId = (String) request.getSession().getAttribute(Custom.currentCustomId);
		if (StringUtils.isNullOrEmpty(customId))
		{
			customId = (String) request.getParameter("customId");
		}
		else
		{
			request.getSession().setAttribute(Custom.currentCustomId, customId);
		}
		if (StringUtils.isNullOrEmpty(customId))
		{
			User user = getCurrentUser();
			customId = user.getDefaultCustomId();
		}
		return customId;
	}

	public void toJsonResponse(Object value, boolean success)
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
			json.setData(ex.getMessage());
			log.warn(ex.getMessage());
		}
		toJSON(json);
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

	public User getCurrentUser()
	{
		return (User) ServletActionContext.getRequest().getAttribute(User.CurrentLoginUser);
	}
}
