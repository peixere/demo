package cn.gotom.servlet;

import java.io.IOException;

import net.sf.json.JSON;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class ResponseUtils
{
	private static final Logger log = Logger.getLogger(ResponseUtils.class);

	public static void toJSON(Object value)
	{
		log.debug(value);
		JsonConfig config = new JsonConfig();      
        config.setIgnoreDefaultExcludes(false);         
        config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);  		
		//DateTransformer dateTransformer = new DateTransformer("yyyy-MM-dd HH:mm:ss.SSS");
		//JSONSerializer serializer = new JSONSerializer();
		//JSON json = serializer.transform(dateTransformer, Date.class).toJSON(value);
		JSON json = net.sf.json.JSONSerializer.toJSON(value,config);
		String encoing = ServletActionContext.getRequest().getCharacterEncoding();
		//ServletActionContext.getResponse().setContentType("text/html;charset=" + encoing);
		ServletActionContext.getResponse().setContentType("application/json;charset=" + encoing);
		try
		{
			ServletActionContext.getResponse().getWriter().println(json.toString());
			ServletActionContext.getResponse().getWriter().flush();
			ServletActionContext.getResponse().getWriter().close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
