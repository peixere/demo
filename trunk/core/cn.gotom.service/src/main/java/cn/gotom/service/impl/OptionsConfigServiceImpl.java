package cn.gotom.service.impl;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.OptionsConfig;
import cn.gotom.service.OptionsConfigService;

public class OptionsConfigServiceImpl extends GenericDaoJpa<OptionsConfig, String> implements OptionsConfigService
{
	public OptionsConfigServiceImpl()
	{
		super(OptionsConfig.class);
	}
}
