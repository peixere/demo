package cn.gotom.web.action;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.util.StringUtils;

@ParentPackage("json-default")
@Namespace(value = "")
@Action(value = "/p", results = { @Result(name = "success", location = "/WEB-INF/view/index.jsp") })
public class PluginsScanAction
{
	protected final Logger log = Logger.getLogger(getClass());

	private String id;
	private String plugins;

	public String execute()
	{
		String pluginsPath = ServletActionContext.getServletContext().getInitParameter("pluginsPath");
		if (StringUtils.isNullOrEmpty(pluginsPath))
		{
			pluginsPath = "/plugins";
		}
		String path = ServletActionContext.getServletContext().getRealPath(pluginsPath);
		File file = new File(path);
		if (file.exists() && file.isDirectory())
		{
			String[] names = file.list();
			for (String name : names)
			{
				plugins += "Ext.Loader.setPath('"+name+"', '${ctxp}/plugins/"+name+"/classes');\n\t";
			}
		}
		//log.info(message);
		return "success";
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getPlugins()
	{
		return plugins;
	}

	public void setPlugins(String plugins)
	{
		this.plugins = plugins;
	}

}
