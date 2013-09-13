package cn.gotom.service;

import cn.gotom.pojos.Role;
import cn.gotom.service.impl.RoleServiceImpl;

import com.google.inject.ImplementedBy;

@ImplementedBy(RoleServiceImpl.class)
public interface RoleService extends GenericService<Role, String>
{

}
