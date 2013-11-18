package cn.gotom.service.impl;

import java.util.ArrayList;
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
	public List<Role> findAllAndChecked(List<Role> userRoles)
	{
		List<Role> roles = this.findAll();
		for (Role r : roles)
		{
			for (Role role : userRoles)
			{
				if (role.getId().equals(r.getId()))
				{

				}
			}
		}
		return roles;
	}

	@Override
	public List<TreeCheckedModel> findAndChecked(List<Role> userRoles)
	{
		List<TreeCheckedModel> tree = new ArrayList<TreeCheckedModel>();
		List<Role> roles = this.findAll();
		for (Role r : roles)
		{
			TreeCheckedModel m = new TreeCheckedModel();
			m.setId(r.getId());
			m.setText(r.getName());
			m.setSort(r.getSort());
			m.setLeaf(true);
			if (userRoles != null)
				for (Role role : userRoles)
				{
					if (role.getId().equals(r.getId()))
					{
						m.setChecked(true);
						break;
					}
				}
			tree.add(m);
		}
		return tree;
	}
}
