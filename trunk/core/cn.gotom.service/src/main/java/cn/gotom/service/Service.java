package cn.gotom.service;

import com.google.inject.ImplementedBy;

@ImplementedBy(ServiceImpl.class)
public interface Service
{
	void init();
}