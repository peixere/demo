package cn.gotom.service;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;

import cn.gotom.pojos.Organization;
import cn.gotom.pojos.Right;

import com.google.inject.Inject;

public class DataInitializeServiceImpl implements DataInitializeService
{
	protected final Logger log = Logger.getLogger(getClass());

	@Inject
	private UserService userService;

	@Inject
	private RightService rightService;

	@Inject
	private OrganizationService organizationService;

	public void init()
	{
		initUser();
		defalutData();
	}

	private void initUser()
	{
		userService.init();
	}

	@SuppressWarnings("unchecked")
	private void defalutData()
	{
		try
		{
			ResourceBundle bundle = ResourceBundle.getBundle("DefaultData", Locale.ROOT);
			if (rightService.findAll().size() == 0)
			{
				String rightString = bundle.getString("Right");
				JSON json = JSONSerializer.toJSON(rightString);
				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.setRootClass(Right.class);
				List<Right> rights = (List<Right>) JSONSerializer.toJava(json, jsonConfig);
				rightService.saveAll(rights);
			}
			if (this.organizationService.findAll().size() == 0)
			{
				String rightString = bundle.getString("Organization");
				JSON json = JSONSerializer.toJSON(rightString);
				JsonConfig jsonConfig = new JsonConfig();
				jsonConfig.setRootClass(Organization.class);
				List<Organization> list = (List<Organization>) JSONSerializer.toJava(json, jsonConfig);
				organizationService.saveAll(list);
			}
		}
		catch (Exception ex)
		{
			log.error("初始化数据异常", ex);
		}
	}
}
