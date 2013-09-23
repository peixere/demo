package cn.gotom.service;

import cn.gotom.pojos.ResourceConfig;
import cn.gotom.service.impl.ResourceConfigServiceImpl;

import com.google.inject.ImplementedBy;

@ImplementedBy(ResourceConfigServiceImpl.class)
public interface ResourceConfigService extends GenericService<ResourceConfig, String>
{
	ResourceConfig getByName(String name);

	boolean existByName(String name);
}
