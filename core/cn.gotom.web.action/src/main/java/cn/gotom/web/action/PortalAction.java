package cn.gotom.web.action;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Custom;
import cn.gotom.pojos.UploadFile;
import cn.gotom.service.CustomService;
import cn.gotom.service.UploadFileService;
import cn.gotom.util.StringUtils;
import cn.gotom.vo.JsonResponse;

import com.google.inject.Inject;

@Namespace(value = "/")
@Action(value = "p", results = { @Result(name = "success", location = "/WEB-INF/view/index.jsp") })
public class PortalAction extends AbsPortalAction
{
	protected final Logger log = Logger.getLogger(getClass());
	private String id;
	private String alt;
	private Custom custom;
	@Inject
	private CustomService customService;
	@Inject
	private UploadFileService uploadFileService;

	public String execute()
	{
		custom = customService.get(getCurrentCustomId());
		log.debug(getCurrentCustomId());
		return "success";
	}

	public void context()
	{
		custom = customService.get(getCurrentCustomId());
		JsonResponse jr = new JsonResponse();
		jr.setData(custom);
		jr.setSuccess(true);
		toJSON(jr);
	}
	
	@Action(results = { @Result(name = "success", type = "stream") })
	public void down()
	{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=utf-8");
		OutputStream os = null;
		try
		{
			UploadFile uf = uploadFileService.get(id);
			if (uf != null && uf.getFileStream() != null)
			{
				// response.setContentType("application/x-msdownload");
				response.setContentType(uf.getContentType());
				response.setContentLength(uf.getFileStream().length);
				String filename = uf.getFileName();
				if (StringUtils.isNotEmpty(alt) && StringUtils.isNotEmpty(filename))
				{
					response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("gbk"), "iso-8859-1"));
				}
				os = response.getOutputStream();
				os.write(uf.getFileStream());
				os.flush();
			}
			else
			{
				response.getWriter().print("找不到文件：" + id);
			}
		}
		catch (Exception ex)
		{
			log.error(ex.getClass() + " " + ex.getMessage());
		}
		finally
		{
			if (os != null)
			{
				try
				{
					os.close();
				}
				catch (IOException e)
				{
					log.error(e.getClass() + " " + e.getMessage());
				}
			}
		}
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Custom getCustom()
	{
		return custom;
	}

	public void setCustom(Custom custom)
	{
		this.custom = custom;
	}

}
