package cn.gotom.service.impl;

import java.util.List;

import javax.persistence.Query;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.OptionsConfig;
import cn.gotom.service.OptionsConfigService;
import cn.gotom.util.StringUtils;

public class OptionsConfigServiceImpl extends GenericDaoJpa<OptionsConfig, String> implements OptionsConfigService
{
	public OptionsConfigServiceImpl()
	{
		super(OptionsConfig.class);
	}

	@Override
	public List<OptionsConfig> findByName(String name)
	{
		if (StringUtils.isNullOrEmpty(name))
		{
			name = "";
		}
		StringBuffer jpql = new StringBuffer();
		jpql.append("select p from " + persistentClass.getSimpleName() + " p");
		jpql.append(" where p.name = :name");
		jpql.append(" order by sort, optionCode");
		Query q = getEntityManager().createQuery(jpql.toString());
		q.setParameter("name", name);
		@SuppressWarnings("unchecked")
		List<OptionsConfig> list = q.getResultList();
		return list;
	}
}
