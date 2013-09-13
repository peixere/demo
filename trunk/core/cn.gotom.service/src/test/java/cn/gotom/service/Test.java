package cn.gotom.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import cn.gotom.pojos.Resource;

public class Test
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		List<Resource> resList = new ArrayList<Resource>();
		Resource res = new Resource();
		res.setId(1);
		res.setComponent("com");
		res.setName("名称");
		res.setDescription("des");
		res.setSort(1);
		resList.add(res);
		System.out.println(JSONArray.fromObject(resList).toString());
	}

}
