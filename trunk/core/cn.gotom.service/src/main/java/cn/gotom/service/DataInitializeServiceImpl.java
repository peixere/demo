package cn.gotom.service;

import com.google.inject.Inject;

public class DataInitializeServiceImpl implements DataInitializeService
{
	@Inject
	private UserService userService;

	public void init()
	{
		initUser();
	}

	private void initUser()
	{
		userService.init();
	}
	
}
