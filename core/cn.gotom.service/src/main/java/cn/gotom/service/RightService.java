package cn.gotom.service;

import java.util.List;

import cn.gotom.pojos.Right;
import cn.gotom.service.impl.RightServiceImpl;
import cn.gotom.service.model.RightChecked;
import cn.gotom.service.model.RightTree;

import com.google.inject.ImplementedBy;

@ImplementedBy(RightServiceImpl.class)
public interface RightService extends GenericService<Right, String>
{
	List<Right> findByParentId(String parentId);

	List<RightTree> loadTree();

	//List<Right> loadTreeByParentId(String parentId);

	List<RightChecked> loadCheckedTree(List<Right> rights);
	
	List<RightChecked> loadCustomCheckedTree(String customId, List<Right> rights);

	List<Right> findByParentId(String parentId, String customId);
}
