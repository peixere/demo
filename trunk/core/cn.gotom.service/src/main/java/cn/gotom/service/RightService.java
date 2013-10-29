package cn.gotom.service;


import java.util.List;

import cn.gotom.pojos.Right;
import cn.gotom.service.impl.RightServiceImpl;

import com.google.inject.ImplementedBy;

@ImplementedBy(RightServiceImpl.class)
public interface RightService extends GenericService<Right, String>
{
	List<Right> findByParentId(String parentId);

	List<Right> loadTree();
}
