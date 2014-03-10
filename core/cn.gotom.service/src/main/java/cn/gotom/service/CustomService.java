package cn.gotom.service;

import java.util.List;

import cn.gotom.pojos.Custom;
import cn.gotom.pojos.CustomUser;
import cn.gotom.pojos.Right;
import cn.gotom.pojos.User;
import cn.gotom.service.impl.CustomServiceImpl;

import com.google.inject.ImplementedBy;

@ImplementedBy(CustomServiceImpl.class)
public interface CustomService extends GenericService<Custom, String>
{
	public List<User> findUserByCustomId(String customId);

	public void saveAndRight(Custom custom, List<Right> rights);

	public List<Right> findRights(String customId);

	public void removeByIds(String[] ids);

	public CustomUser getCustomUser(String id, String customId);
}
