package cn.gotom.dao;

import javax.persistence.EntityManager;

/**
 * 
 * Persistence 生命周期管理
 * 
 * @author peixere@qq.com
 *
 */
public interface PersistenceLifeCycle
{
	public abstract void startService();

	public abstract void stopService();

	public abstract void beginUnitOfWork();

	public abstract void endUnitOfWork();
	
	public abstract EntityManager get();

}
