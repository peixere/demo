package cn.gotom.service.impl;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.Custom;
import cn.gotom.pojos.User;
import cn.gotom.service.CustomService;

public class CustomServiceImpl extends GenericDaoJpa<Custom, String> implements CustomService
{

	public CustomServiceImpl()
	{
		super(Custom.class);
	}

	@Override
	public void init(User user)
	{
		try
		{
			Custom custom = this.get("creater.id", user.getId());
			if (custom == null)
			{
				custom = new Custom();
				custom.setName("默认客户");
				custom.setCreater(user);
				save(custom);
			}
		}
		catch (Exception ex)
		{
			log.error("", ex);
		}
	}

}
