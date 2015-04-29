package cn.gotom.jms;

import cn.gotom.service.InitializeService;

import com.google.inject.ImplementedBy;

@ImplementedBy(JMSServiceImpl.class)
public interface JMSService extends InitializeService
{

}
