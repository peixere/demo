package cn.gotom.service;

import cn.gotom.pojos.Custom;
import cn.gotom.pojos.User;

public interface CustomService extends GenericService<Custom, String>
{

	void init(User user);

}
