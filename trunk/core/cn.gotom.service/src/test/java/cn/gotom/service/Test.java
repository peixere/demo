package cn.gotom.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import cn.gotom.pojos.Menu;

public class Test
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		List<Menu> resList = new ArrayList<Menu>();
		Menu res = new Menu();
		res.setId(1);
		res.setComponent("com");
		res.setText("名称");
		res.setDescription("des");
		res.setSort(1);
		resList.add(res);
		System.out.println(JSONArray.fromObject(resList).toString());
	}

}
