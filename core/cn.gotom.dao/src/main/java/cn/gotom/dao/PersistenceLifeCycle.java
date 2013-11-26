package cn.gotom.dao;

import com.google.inject.ImplementedBy;

/**
 * 
 * Persistence 生命周期管理
 * 
 * @author peixere@qq.com
 * 
 */
@ImplementedBy(Persistence.class)
public interface PersistenceLifeCycle
{
	public abstract void startService();

	public abstract void stopService();

	public abstract void beginUnitOfWork();

	public abstract void endUnitOfWork();

	String getPattern();

	void setPattern(String pattern);

}
