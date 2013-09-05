package cn.gotom.service.impl;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.ResourceConfig;
import cn.gotom.service.ResourceConfigService;

public class ResourceConfigServiceImpl extends GenericDaoJpa<ResourceConfig, String> implements ResourceConfigService
{

	public ResourceConfigServiceImpl()
	{
		super(ResourceConfig.class);
	}

	@Override
	public ResourceConfig getByName(String name)
	{
		return this.get("name", name);
	}

	@Override
	public boolean existByName(String name)
	{
		return null != getByName(name);
	}
}
