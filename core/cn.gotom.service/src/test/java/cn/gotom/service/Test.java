package cn.gotom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import cn.gotom.pojos.Right;

public class Test
{

	public Test(){
		
	}
	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		ResourceBundle bundle = ResourceBundle.getBundle("DefaultData", Locale.ROOT);
		String rightString = bundle.getString("Right");
		JSON json = JSONSerializer.toJSON(rightString);
		JsonConfig jsonConfig = new JsonConfig();
		List<Right> rights = new ArrayList<Right>();
		jsonConfig.setRootClass(Right.class);
		rights = (List<Right>) JSONSerializer.toJava(json, jsonConfig);
		System.out.println(rights);
		//JSONArray.toc(json, Right.class);
		// System.out.println(JSONArray.fromObject(resList).toString());
	}

}
