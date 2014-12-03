package cn.gotom.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.Right;
import cn.gotom.pojos.Role;
import cn.gotom.pojos.RoleRight;
import cn.gotom.service.RoleService;
import cn.gotom.vo.TreeCheckedModel;

import com.google.inject.persist.Transactional;

public class RoleServiceImpl extends GenericDaoJpa<Role, String> implements RoleService
{
	public RoleServiceImpl()
	{
		super(Role.class);
	}

	// @Override
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
	public List<TreeCheckedModel> findAndChecked(String customId, List<Role> userRoles)
	{
		List<TreeCheckedModel> tree = new ArrayList<TreeCheckedModel>();
		List<Role> roles = this.findByCustomId(customId);
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

	@Override
	public List<Role> findByCustomId(String customId)
	{
		StringBuffer jpql = new StringBuffer();
		jpql.append("select p from " + persistentClass.getSimpleName() + " p");
		jpql.append(" where p.organization.custom.id = :customId");
		jpql.append(" order by sort");
		Query q = getEntityManager().createQuery(jpql.toString());
		q.setParameter("customId", customId);
		@SuppressWarnings("unchecked")
		List<Role> list = q.getResultList();
		return list;
	}

	@Transactional
	@Override
	public void removeById(String id)
	{
		this.nativeRemove("core_user_role", "role_id", id);
		Role entity = this.get(id);
		if (entity != null)
		{
			getEntityManager().remove(getEntityManager().getReference(persistentClass, id));
		}
	}

	@Override
	public List<Right> findRight(String roleId)
	{
		StringBuffer jpql = new StringBuffer();
		jpql.append("select p.right from " + RoleRight.class.getSimpleName() + " p");
		jpql.append(" where p.role.id = :roleId");
		Query q = getEntityManager().createQuery(jpql.toString());
		q.setParameter("roleId", roleId);
		@SuppressWarnings("unchecked")
		List<Right> list = q.getResultList();
		return list;
	}

	@Transactional
	@Override
	public void removeRoleRight(List<Right> oldRights)
	{
		for (Right right : oldRights)
			this.remove(RoleRight.class.getSimpleName(), "right_id", right.getId());
	}
}
