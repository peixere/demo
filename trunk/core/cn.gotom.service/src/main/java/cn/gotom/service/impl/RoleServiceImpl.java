package cn.gotom.service.impl;

import java.util.List;

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
	public List<Role> findAllAndChecked(List<Role> hasRoles)
	{
		List<Role> roles = this.findAll();
		for (Role r : roles)
		{
			
		}
		return roles;
	}
}
