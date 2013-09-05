package cn.gotom.service;

import cn.gotom.pojos.ResourceConfig;

public interface ResourceConfigService extends GenericService<ResourceConfig, String>
{
	ResourceConfig getByName(String name);

	boolean existByName(String name);
}
