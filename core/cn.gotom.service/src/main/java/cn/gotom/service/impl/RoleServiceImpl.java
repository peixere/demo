package cn.gotom.service.impl;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.Role;
import cn.gotom.service.RoleService;

public class RoleServiceImpl extends GenericDaoJpa<Role, String> implements RoleService
{
	public RoleServiceImpl()
	{
		super(Role.class);
	}

	@Override
	public void saveRoleAndRights(Role role)
	{
		// TODO Auto-generated method stub
		
	}
}
