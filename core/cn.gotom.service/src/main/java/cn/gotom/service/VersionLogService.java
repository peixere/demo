package cn.gotom.service;

import java.util.List;

import cn.gotom.pojos.IdSerializable;
import cn.gotom.pojos.VersionLog;
import cn.gotom.service.impl.VersionLogServiceImpl;

import com.google.inject.ImplementedBy;

@ImplementedBy(VersionLogServiceImpl.class)
public interface VersionLogService
{
	void save(IdSerializable object);

	void remove(IdSerializable object);

	void remove(VersionLog versionLog);

	List<VersionLog> find(int maxResults, int firstResult);
}
