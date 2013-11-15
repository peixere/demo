package cn.gotom.service.impl;

import java.util.List;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.Role;
import cn.gotom.service.RoleService;
import cn.gotom.vo.TreeCheckedModel;

public class RoleServiceImpl extends GenericDaoJpa<Role, String> implements RoleService
{
	public RoleServiceImpl()
	{
		super(Role.class);
	}

	@Override
	public List<TreeCheckedModel> loadRightTree(String id)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
