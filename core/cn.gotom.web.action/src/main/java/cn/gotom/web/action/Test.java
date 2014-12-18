package cn.gotom.web.action;

import java.util.ArrayList;
import java.util.List;

import cn.gotom.pojos.User;
import cn.gotom.sso.TicketImpl;
import cn.gotom.sso.util.GsonUtils;

public class Test
{
	public static void main(String[] agrs)
	{
		List<TicketImpl> userList = new ArrayList<TicketImpl>();
		TicketImpl user = new TicketImpl("");
		userList.add(user);
		user = new TicketImpl("");
		userList.add(user);
		String json = GsonUtils.toJson(userList, GsonUtils.dateFormat);
		System.out.println(json);
		User[] users = GsonUtils.toArray(User[].class, json);
		System.out.println(users.length);
	}
}
