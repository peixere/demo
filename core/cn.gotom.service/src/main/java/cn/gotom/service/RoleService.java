package cn.gotom.service;

import java.util.List;

import cn.gotom.pojos.Right;
import cn.gotom.pojos.Role;
import cn.gotom.service.impl.RoleServiceImpl;
import cn.gotom.vo.TreeCheckedModel;

import com.google.inject.ImplementedBy;

@ImplementedBy(RoleServiceImpl.class)
public interface RoleService extends GenericService<Role, String>
{

	// List<Role> findAllAndChecked(List<Role> userRoles);

	List<TreeCheckedModel> findAndChecked(String customId, List<Role> userRoles);

	List<Role> findByCustomId(String customId);

	List<Right> findRight(String roleId);

	void removeRoleRight(List<Right> oldRights);
}
