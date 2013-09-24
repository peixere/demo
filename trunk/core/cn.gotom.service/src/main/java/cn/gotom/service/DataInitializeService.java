package cn.gotom.service;

import com.google.inject.ImplementedBy;

@ImplementedBy(DataInitializeServiceImpl.class)
public interface DataInitializeService
{
	void init();
}