package cn.gotom.service;

import java.util.List;

import cn.gotom.pojos.Organization;
import cn.gotom.service.impl.OrganizationServiceImpl;

import com.google.inject.ImplementedBy;

@ImplementedBy(OrganizationServiceImpl.class)
public interface OrganizationService extends GenericService<Organization, String>
{
	List<Organization> findByParentId(String parentId);

	List<Organization> loadTree();

	List<Organization> loadTreeByParentId(String parentId);
	
	Organization findParentByCode(String code);
	
	Organization getByCode(String code);
}
