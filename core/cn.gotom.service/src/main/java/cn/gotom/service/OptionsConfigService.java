package cn.gotom.service;

import cn.gotom.pojos.OptionsConfig;
import cn.gotom.service.impl.OptionsConfigServiceImpl;

import com.google.inject.ImplementedBy;
 
@ImplementedBy(OptionsConfigServiceImpl.class)
public interface OptionsConfigService extends GenericService<OptionsConfig, String>
{

}
