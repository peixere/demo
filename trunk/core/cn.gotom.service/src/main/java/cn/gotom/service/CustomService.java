package cn.gotom.service;

import cn.gotom.pojos.Custom;
import cn.gotom.pojos.User;
import cn.gotom.service.impl.CustomServiceImpl;

import com.google.inject.ImplementedBy;

@ImplementedBy(CustomServiceImpl.class)
public interface CustomService extends GenericService<Custom, String>
{

	void init(User user);

}
