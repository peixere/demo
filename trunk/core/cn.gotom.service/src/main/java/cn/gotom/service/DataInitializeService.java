package cn.gotom.service;

import cn.gotom.pojos.User;

import com.google.inject.Inject;

public class DataInitializeService
{
	@Inject
	private UserService userService;

	@Inject
	private CustomService customService;

	public void init()
	{
		initUser();
		initCustom();
	}

	private void initUser()
	{
		userService.init();
	}
	
	private void initCustom()
	{
		User user = userService.getByUsername(User.admin);
		customService.init(user);
	}
}
