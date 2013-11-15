package cn.gotom.service;


import java.util.List;

import cn.gotom.pojos.Right;
import cn.gotom.service.impl.RightServiceImpl;
import cn.gotom.service.model.RightChecked;

import com.google.inject.ImplementedBy;

@ImplementedBy(RightServiceImpl.class)
public interface RightService extends GenericService<Right, String>
{
	List<Right> findByParentId(String parentId);

	List<Right> loadTree();
	
	List<Right> loadTreeByParentId(String parentId);

	List<RightChecked> loadCheckedTree(List<Right> rights);
}
