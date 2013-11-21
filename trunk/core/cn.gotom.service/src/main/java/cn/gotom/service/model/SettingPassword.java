package cn.gotom.service.model;

import cn.gotom.pojos.User;

public class SettingPassword extends User
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String newpass;
	private String newpassCheck;
	public String getNewpass()
	{
		return newpass;
	}
	public void setNewpass(String newpass)
	{
		this.newpass = newpass;
	}
	public String getNewpassCheck()
	{
		return newpassCheck;
	}
	public void setNewpassCheck(String newpassCheck)
	{
		this.newpassCheck = newpassCheck;
	}
	
	
}
