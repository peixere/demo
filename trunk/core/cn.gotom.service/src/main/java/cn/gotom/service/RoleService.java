package cn.gotom.service;

import java.util.List;

import cn.gotom.pojos.Role;
import cn.gotom.service.impl.RoleServiceImpl;
import cn.gotom.vo.TreeCheckedModel;

import com.google.inject.ImplementedBy;

@ImplementedBy(RoleServiceImpl.class)
public interface RoleService extends GenericService<Role, String>
{
	List<TreeCheckedModel> loadRightTree(String id);
}
