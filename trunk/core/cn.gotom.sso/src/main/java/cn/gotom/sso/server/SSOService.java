package cn.gotom.sso.server;

import cn.gotom.sso.SSOException;

public interface SSOService
{

	boolean login(String username, String password, String sql) throws SSOException;

}
