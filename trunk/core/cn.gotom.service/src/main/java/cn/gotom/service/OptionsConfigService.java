package cn.gotom.service;

import java.util.List;

import cn.gotom.pojos.OptionsConfig;
import cn.gotom.service.impl.OptionsConfigServiceImpl;

import com.google.inject.ImplementedBy;
 
@ImplementedBy(OptionsConfigServiceImpl.class)
public interface OptionsConfigService extends GenericService<OptionsConfig, String>
{
	List<OptionsConfig> findByName(String name);
}
