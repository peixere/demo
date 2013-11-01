package cn.gotom.service;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import cn.gotom.pojos.Right;

import com.google.inject.Inject;

public class DataInitializeServiceImpl implements DataInitializeService
{
	protected final Logger log = Logger.getLogger(getClass());

	@Inject
	private UserService userService;

	@Inject
	private RightService rightService;

	public void init()
	{
		initUser();
		initData();
	}

	private void initUser()
	{
		userService.init();
	}

	@SuppressWarnings("unchecked")
	private void initData()
	{
		try
		{
			ResourceBundle bundle = ResourceBundle.getBundle("DefaultData", Locale.ROOT);
			String rightString = bundle.getString("Right");
			JSON json = JSONSerializer.toJSON(rightString);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(Right.class);
			List<Right> rights = (List<Right>) JSONSerializer.toJava(json, jsonConfig);
			rightService.saveAll(rights);
		}
		catch (Exception ex)
		{
			log.error("初始化数据异常", ex);
		}
	}
}
