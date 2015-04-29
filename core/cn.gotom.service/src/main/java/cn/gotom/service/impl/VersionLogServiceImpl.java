package cn.gotom.service.impl;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.IdSerializable;
import cn.gotom.pojos.VersionLog;
import cn.gotom.service.VersionLogService;

import com.google.inject.Singleton;

@Singleton
public class VersionLogServiceImpl extends GenericDaoJpa<VersionLog, String> implements VersionLogService
{
	public VersionLogServiceImpl()
	{
		super(VersionLog.class);
	}

	@Override
	public void save(IdSerializable o)
	{
		VersionLog log = new VersionLog();
		o.setVersionNow(log.getVersionNow());
		log.setEntityId(o.getId());
		log.setEntityCtrl(0);
		log.setEntityClazz(o.getClass().getSimpleName());
		super.save(log);
	}

	@Override
	public void remove(IdSerializable o)
	{
		VersionLog log = new VersionLog();
		o.setVersionNow(o.getVersionNow());
		log.setEntityId(o.getId());
		log.setEntityCtrl(1);
		log.setEntityClazz(o.getClass().getSimpleName());
		super.save(log);
	}

	@Override
	public void remove(VersionLog versionLog)
	{
		super.remove(versionLog);
	}
	
	public void sync(){
		
	}
}
