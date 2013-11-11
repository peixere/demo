package cn.gotom.service.impl;

import java.util.List;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.OptionsConfig;
import cn.gotom.service.OptionsConfigService;

public class OptionsConfigServiceImpl extends GenericDaoJpa<OptionsConfig, String> implements OptionsConfigService
{
	public OptionsConfigServiceImpl()
	{
		super(OptionsConfig.class);
	}

	@Override
	public List<OptionsConfig> findAll()
	{
		List<OptionsConfig> list = super.findAll();
		if (list.size() == 0)
		{
			OptionsConfig of = new OptionsConfig();
			of.setName("airconditionerSystemCode");
			of.setOptionCode("A");
			of.setOptionValue("集中式全空气系统");
			list.add(of);
			of = new OptionsConfig();
			of.setName("airconditionerSystemCode");
			of.setOptionCode("B");
			of.setOptionValue("风机盘管＋新风系统");
			list.add(of);
			of = new OptionsConfig();
			of.setName("airconditionerSystemCode");
			of.setOptionCode("C");
			of.setOptionValue("分体式空调或VRV的局部式机组系统");
			list.add(of);
			of = new OptionsConfig();
			of.setName("airconditionerSystemCode");
			of.setOptionCode("D");
			of.setOptionValue("其它");
			list.add(of);
			
			of = new OptionsConfig();
			of.setName("heatingFormCode");
			of.setOptionCode("A");
			of.setOptionValue("散热器采暖");
			list.add(of);
			of = new OptionsConfig();
			of.setName("heatingFormCode");
			of.setOptionCode("B");
			of.setOptionValue("地板辐射采暖");
			list.add(of);
			of = new OptionsConfig();
			of.setName("heatingFormCode");
			of.setOptionCode("C");
			of.setOptionValue("电辐射采暖");
			list.add(of);
			of = new OptionsConfig();
			of.setName("heatingFormCode");
			of.setOptionCode("D");
			of.setOptionValue("其它");
			list.add(of);

			of = new OptionsConfig();
			of.setName("structureCode");
			of.setOptionCode("A");
			of.setOptionValue("砖混结构");
			list.add(of);
			of = new OptionsConfig();
			of.setName("structureCode");
			of.setOptionCode("B");
			of.setOptionValue("混凝土剪力墙");
			list.add(of);
			of = new OptionsConfig();
			of.setName("structureCode");
			of.setOptionCode("C");
			of.setOptionValue("钢结构");
			list.add(of);
			of = new OptionsConfig();
			of.setName("structureCode");
			of.setOptionCode("D");
			of.setOptionValue("木结构");
			list.add(of);
			of = new OptionsConfig();
			of.setName("structureCode");
			of.setOptionCode("E");
			of.setOptionValue("玻璃幕墙");
			list.add(of);
			of = new OptionsConfig();
			of.setName("structureCode");
			of.setOptionCode("F");
			of.setOptionValue("其它");
			list.add(of);

			of = new OptionsConfig();
			of.setName("exteriorCode");
			of.setOptionCode("A");
			of.setOptionValue("实心粘土砖");
			list.add(of);
			of = new OptionsConfig();
			of.setName("exteriorCode");
			of.setOptionCode("B");
			of.setOptionValue("空心粘土砖(多孔)");
			list.add(of);
			of = new OptionsConfig();
			of.setName("exteriorCode");
			of.setOptionCode("C");
			of.setOptionValue("灰砂砖");
			list.add(of);
			of = new OptionsConfig();
			of.setName("exteriorCode");
			of.setOptionCode("D");
			of.setOptionValue("混凝土砌块");
			list.add(of);
			of = new OptionsConfig();
			of.setName("exteriorCode");
			of.setOptionCode("E");
			of.setOptionValue("混凝土小型空心砌块(多孔)");
			list.add(of);
			of = new OptionsConfig();
			of.setName("exteriorCode");
			of.setOptionCode("F");
			of.setOptionValue("其它");
			list.add(of);
			
			of = new OptionsConfig();
			of.setName("exteriorWallCode");
			of.setOptionCode("A");
			of.setOptionValue("内保温");
			list.add(of);
			of = new OptionsConfig();
			of.setName("exteriorWallCode");
			of.setOptionCode("B");
			of.setOptionValue("外保温");
			list.add(of);
			of = new OptionsConfig();
			of.setName("exteriorWallCode");
			of.setOptionCode("C");
			of.setOptionValue("夹芯保温");
			list.add(of);
			of = new OptionsConfig();
			of.setName("exteriorWallCode");
			of.setOptionCode("D");
			of.setOptionValue("其它");
			list.add(of);

			of = new OptionsConfig();
			of.setName("windowTypeCode");
			of.setOptionCode("A");
			of.setOptionValue("单玻单层窗");
			list.add(of);
			of = new OptionsConfig();
			of.setName("windowTypeCode");
			of.setOptionCode("B");
			of.setOptionValue("单玻双层窗");
			list.add(of);
			of = new OptionsConfig();
			of.setName("windowTypeCode");
			of.setOptionCode("C");
			of.setOptionValue("单玻单层窗+单玻双层窗");
			list.add(of);
			of = new OptionsConfig();
			of.setName("windowTypeCode");
			of.setOptionCode("D");
			of.setOptionValue("中空双层玻璃窗");
			list.add(of);
			of = new OptionsConfig();
			of.setName("windowTypeCode");
			of.setOptionCode("E");
			of.setOptionValue("中空三层玻璃窗");
			list.add(of);
			of = new OptionsConfig();
			of.setName("windowTypeCode");
			of.setOptionCode("F");
			of.setOptionValue("中空充惰性气体");
			list.add(of);
			of = new OptionsConfig();
			of.setName("windowTypeCode");
			of.setOptionCode("G");
			of.setOptionValue("其它");
			list.add(of);
			
			of = new OptionsConfig();
			of.setName("glassTypesCode");
			of.setOptionCode("A");
			of.setOptionValue("普通玻璃");
			list.add(of);
			of = new OptionsConfig();
			of.setName("glassTypesCode");
			of.setOptionCode("B");
			of.setOptionValue("镀膜玻璃");
			list.add(of);
			of = new OptionsConfig();
			of.setName("glassTypesCode");
			of.setOptionCode("C");
			of.setOptionValue("Low-e玻璃");
			list.add(of);
			of = new OptionsConfig();
			of.setName("glassTypesCode");
			of.setOptionCode("D");
			of.setOptionValue("其它");
			list.add(of);
			
			of = new OptionsConfig();
			of.setName("windowFrameMaterialCode");
			of.setOptionCode("A");
			of.setOptionValue("钢窗");
			list.add(of);
			of = new OptionsConfig();
			of.setName("windowFrameMaterialCode");
			of.setOptionCode("B");
			of.setOptionValue("铝合金");
			list.add(of);
			of = new OptionsConfig();
			of.setName("windowFrameMaterialCode");
			of.setOptionCode("C");
			of.setOptionValue("木窗");
			list.add(of);
			of = new OptionsConfig();
			of.setName("windowFrameMaterialCode");
			of.setOptionCode("D");
			of.setOptionValue("断热窗框");
			list.add(of);
			of = new OptionsConfig();
			of.setName("windowFrameMaterialCode");
			of.setOptionCode("E");
			of.setOptionValue("其它");
			list.add(of);
			this.saveAll(list);
		}
		return list;
	}
}
